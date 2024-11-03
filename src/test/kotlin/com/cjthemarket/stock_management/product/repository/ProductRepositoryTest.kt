package com.cjthemarket.stock_management.product.repository

import com.cjthemarket.stock_management.TestDatabase
import com.cjthemarket.stock_management.product.getProduct
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductRepositoryTest(
    private val productJpaRepository: ProductJpaRepository,
    private val entityManager: EntityManager,
) : TestDatabase() {
    private val sut = ProductRepository(productJpaRepository)

    @BeforeEach
    fun setUp() {
        sut.setEntityManager(entityManager)
        sut.deleteAllInBatch()
    }

    @Test
    fun `findProductById - 상품 조회`() {
        // given
        val product = getProduct(1, "상품1", 1000)
        sut.save(product)

        // when
        val productEntity = sut.findProductById(1L)

        // then
        productEntity shouldNotBe null
        productEntity!!.id shouldBe 1
        productEntity.name shouldBe "상품1"
        productEntity.originalPrice shouldBe 1000
    }
}
