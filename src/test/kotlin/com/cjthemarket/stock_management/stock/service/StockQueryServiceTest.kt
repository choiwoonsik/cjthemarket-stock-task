package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.TestDatabase
import com.cjthemarket.stock_management.response.DataNotFoundException
import com.cjthemarket.stock_management.stock.getStock
import com.cjthemarket.stock_management.stock.repository.StockJpaRepository
import com.cjthemarket.stock_management.stock.repository.StockRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StockQueryServiceTest(
    private val stockJpaRepository: StockJpaRepository,
    private val entityManager: EntityManager,
) : TestDatabase() {
    private val stockRepository = StockRepository(stockJpaRepository)
    private val sut = StockQueryService(stockRepository)

    @BeforeEach
    fun setUp() {
        stockRepository.setEntityManager(entityManager)
        stockRepository.deleteAllInBatch()
        entityManager.createNativeQuery("ALTER TABLE `stocks` AUTO_INCREMENT = 1").executeUpdate()
    }

    @Test
    fun `getStockByProductId - 상품 아이디로 재고 Model 조회`() {
        // given
        val stock = getStock(quantity = 10, productId = 1)
        stockRepository.save(stock)

        // when
        val stockPersist = sut.getStockByProductId(1L)

        // then
        stockPersist.id shouldBe 1L
        stockPersist.productId shouldBe stock.productId
        stockPersist.quantity shouldBe stock.quantity
    }

    @Test
    fun `상품 아이디로 재고 DTO 조회`() {
        // given
        val stock = getStock(quantity = 10, productId = 1)
        stockRepository.save(stock)

        // when
        val stockDto = sut.getStockDtoByProductId(1L)

        // then
        stockDto.id shouldBe 1L
        stockDto.productId shouldBe stock.productId
        stockDto.quantity shouldBe stock.quantity
    }

    @Test
    fun `상품 아이디에 해당하는 재고가 없으면 오류를 반환한다`() {
        // given
        val stock = getStock(quantity = 10, productId = 1)
        stockRepository.save(stock)

        // when
        kotlin.runCatching {
            sut.getStockByProductId(99L)
        }.onFailure {
            // then
            it.javaClass shouldBe DataNotFoundException::class.java
            it shouldHaveMessage "[상품 ID: 99] 해당 상품의 재고가 존재하지 않습니다."
        }
    }
}
