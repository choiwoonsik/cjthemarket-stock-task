package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.TestDatabase
import com.cjthemarket.stock_management.response.InvalidRequestException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import com.cjthemarket.stock_management.stock.getStock
import com.cjthemarket.stock_management.stock.repository.StockJpaRepository
import com.cjthemarket.stock_management.stock.repository.StockRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import jakarta.persistence.EntityManager
import org.hibernate.exception.GenericJDBCException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StockMutationServiceTest(
    private val stockJpaRepository: StockJpaRepository,
    private val entityManager: EntityManager,
) : TestDatabase() {
    private val stockRepository = StockRepository(stockJpaRepository)
    private val stockQueryService = StockQueryService(stockRepository)
    private val sut = StockMutationService(stockQueryService)

    @BeforeEach
    fun setUp() {
        stockRepository.setEntityManager(entityManager)
        stockJpaRepository.deleteAllInBatch()
        entityManager.createNativeQuery("ALTER TABLE stocks AUTO_INCREMENT = 1").executeUpdate()
    }

    @Test
    fun `decreaseStock - 재고 수량 차감`() {
        // given
        val stock = getStock(1L, 10L, 3L)
        stockRepository.save(stock)

        val decreaseInput = ProductStockDecreaseInput(3L, 5L)

        // when
        val resultStockDto = sut.decreaseStock(decreaseInput)

        // then
        resultStockDto.id shouldBe 1L
        resultStockDto.quantity shouldBe 5L
        resultStockDto.productId shouldBe 3L
    }

    @Test
    fun `decreaseStock - 재고 수량을 초과하여 차감하는 경우 오류를 반환한다`() {
        // given
        val stock = getStock(1L, 10L, 3L)
        stockRepository.save(stock)

        val decreaseInput = ProductStockDecreaseInput(3L, 11L)

        // when
        kotlin.runCatching {
            sut.decreaseStock(decreaseInput)
            entityManager.flush()
        }.onFailure {
            // then
            it.javaClass shouldBe InvalidRequestException::class.java
            it shouldHaveMessage "[재고 ID: 1] 재고가 부족합니다."
        }.onSuccess { throw AssertionError("예외가 발생해야 합니다.") }
    }

    @Test
    fun `setStock - 재고 수량 특정 값으로 초기화`() {
        // given
        val stock = getStock(1L, 10L, 3L)
        stockRepository.save(stock)

        val input = ProductStockSetInput(3L, 99L)

        // when
        val resultStockDto = sut.setStock(input)

        // then
        resultStockDto.id shouldBe 1L
        resultStockDto.quantity shouldBe 99L
        resultStockDto.productId shouldBe 3L
    }

    @Test
    fun `setStock - 재고를 0 미만의 값으로 설정하는 경우 SQL 제약조건 위배로 인해 에러가 발생한다`() {
        // given
        val stock = getStock(1L, 10L, 3L)
        stockRepository.save(stock)

        val input = ProductStockSetInput(3L, -1L)

        // when
        kotlin.runCatching {
            sut.setStock(input)
            entityManager.flush()
        }.onFailure {
            // then
            it.javaClass shouldBe GenericJDBCException::class.java
            it.message!!.contains("Check constraint 'stocks_chk_1' is violated.") shouldBe true
        }.onSuccess { throw AssertionError("예외가 발생해야 합니다.") }
    }
}
