package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.ClearDataBaseBefore
import com.cjthemarket.stock_management.ClearDatabaseAfter
import com.cjthemarket.stock_management.distributedlock.DistributedLockService
import com.cjthemarket.stock_management.product.repository.ProductRepository
import com.cjthemarket.stock_management.response.InvalidRequestException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.getStock
import com.cjthemarket.stock_management.stock.repository.StockRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.util.concurrent.CompletableFuture

@ActiveProfiles("test")
@SpringBootTest
@Import(ClearDataBaseBefore::class, ClearDatabaseAfter::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StockMutatorAsyncTest(
    @Qualifier("redissonClient") private val redissonClient: RedissonClient,
    stockMutationService: StockMutationService,
    private val stockRepository: StockRepository,
    private val productRepository: ProductRepository,
) {
    private val stockValidateService: StockValidateService = mockk(relaxed = true)
    private val distributedLockService: DistributedLockService = DistributedLockService(redissonClient)
    private val sut = StockMutator(stockValidateService, stockMutationService, distributedLockService)

    @BeforeEach
    fun setUp() {
        stockRepository.deleteAllInBatch()
        productRepository.deleteAllInBatch()
    }

    @Test
    fun `비동기 환경에서 재고 감소 요청이 와도 누락 없이 정확한 재고 감소가 이뤄줘야 한다 - 단건 요청`() {
        // given
        val stock = getStock(id = 5L, quantity = 10, productId = 5L)
        val input = ProductStockDecreaseInput(
            productId = 5L,
            decreaseQuantity = 10L,
        )
        stockRepository.save(stock)

        // when
        /**
         * 잔여 재고 10개, 1번 비동기 10개 요청, 잔여 0개
         */
        val futures = (1 until 2).map {
            CompletableFuture.supplyAsync(
                {
                    sut.decreaseStockWithLock(input)
                },
                getThreadTaskExecutor(),
            )
        }

        // then
        futures.map { it.join() }
        stockRepository.findStockByProductId(5L)!!.quantity shouldBe 0L
    }

    @Test
    fun `비동기 환경에서 재고 감소 요청이 와도 누락 없이 정확한 재고 감소가 이뤄줘야 한다 - 비동기 복수 요청`() {
        // given
        val quantity = 1000L
        val orderQuantity = 15L

        val stock = getStock(id = 1L, quantity = quantity, productId = 2L)
        val input = ProductStockDecreaseInput(
            productId = 2L,
            decreaseQuantity = orderQuantity,
        )
        stockRepository.save(stock)

        // when
        /**
         * 잔여 재고 1000개, 15 씩 * 66번 비동기 = 990개 요청, 잔여 10개
         */
        val futures = (1..(quantity / orderQuantity)).map {
            CompletableFuture.supplyAsync(
                {
                    sut.decreaseStockWithLock(input)
                },
                getThreadTaskExecutor(),
            )
        }

        // then
        futures.map { it.join() }
        stockRepository.findStockByProductId(2L)!!.quantity shouldBe 10
    }

    @Test
    fun `비동기 환경에서 재고 감소 요청이 너무 몰려서 Lock을 대기하는 시간이 제한 시간(waitSeconds)을 초과하면 예외를 던진다`() {
        // given
        val quantity = 1000L
        val orderQuantity = 1L

        val stock = getStock(id = 1L, quantity = quantity, productId = 1L)
        val input = ProductStockDecreaseInput(
            productId = 1L,
            decreaseQuantity = orderQuantity,
        )
        stockRepository.save(stock)

        // when
        /**
         * 잔여재고 1,000개, 1개 씩 * 1000번 비동기 = 1,000개 요청, 잔여 0개
         */
        val futures = (1..(quantity / orderQuantity)).map {
            CompletableFuture.supplyAsync(
                {
                    sut.decreaseStockWithLock(input)
                },
                getThreadTaskExecutor(),
            )
        }

        // then
        kotlin.runCatching {
            futures.map { it.join() }
        }.onFailure { e ->
            val rootCause = getRootCause(e)
            rootCause.javaClass shouldBe InvalidRequestException::class.java
            rootCause shouldHaveMessage "락 키 [lock:stock:1] 획득에 실패했습니다."
        }.onSuccess { throw IllegalStateException("예외가 발생해야 합니다.") }
    }

    private fun getThreadTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 1000
        executor.maxPoolSize = 1000
        executor.queueCapacity = 10000
        executor.setThreadNamePrefix("multi thread -")
        executor.initialize()

        return executor
    }

    private fun getRootCause(throwable: Throwable): Throwable {
        var rootCause = throwable
        while (rootCause.cause != null) {
            rootCause = rootCause.cause!!
        }
        return rootCause
    }
}
