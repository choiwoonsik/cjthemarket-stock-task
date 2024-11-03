package com.cjthemarket.stock_management.stock.repository

import com.cjthemarket.stock_management.TestDatabase
import com.cjthemarket.stock_management.stock.getStock
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.orm.jpa.JpaSystemException

class StockRepositoryTest(
    private val stockJpaRepository: StockJpaRepository,
    private val entityManager: EntityManager,
) : TestDatabase() {
    private val sut = StockRepository(stockJpaRepository)

    @BeforeEach
    fun setUp() {
        sut.setEntityManager(entityManager)
        stockJpaRepository.deleteAllInBatch()
        entityManager.createNativeQuery("ALTER TABLE stocks AUTO_INCREMENT = 1").executeUpdate()
    }

    @Test
    fun `save - 상품 재고 저장`() {
        // given
        val stock = getStock(quantity = 10, productId = 1)

        // when
        val stockPersist = sut.save(stock)

        // then
        stockPersist.id shouldBe 1L
        stockPersist.productId shouldBe 1L
        stockPersist.quantity shouldBe 10
    }

    @Test
    fun `save - 상품 재고 0 미만 저장 시 오류를 던진다`() {
        // given
        val stock = getStock(quantity = -1, productId = 1)

        // when
        kotlin.runCatching {
            sut.save(stock)
        }.onFailure {
            // then
            it.javaClass shouldBe JpaSystemException::class.java
            ("Check constraint 'stocks_chk_1' is violated" in it.message!!) shouldBe true
        }.onSuccess { throw AssertionError("예외가 발생해야 합니다.") }
    }
}
