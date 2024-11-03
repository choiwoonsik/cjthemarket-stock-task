package com.cjthemarket.stock_management.product.service

import com.cjthemarket.stock_management.TestDatabase
import com.cjthemarket.stock_management.product.getProduct
import com.cjthemarket.stock_management.product.repository.ProductJpaRepository
import com.cjthemarket.stock_management.product.repository.ProductRepository
import com.cjthemarket.stock_management.response.DataNotFoundException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductQueryServiceTest(
    private val productJpaRepository: ProductJpaRepository,
    private val entityManager: EntityManager,
) : TestDatabase() {

    private val productRepository = ProductRepository(productJpaRepository)
    private val sut = ProductQueryService(productRepository)

    @BeforeEach
    fun init() {
        productRepository.setEntityManager(entityManager)
        productRepository.deleteAllInBatch()
        entityManager.createNativeQuery("ALTER TABLE `products` AUTO_INCREMENT = 1").executeUpdate()
    }

    @Test
    fun `getProductDtoById 상품 조회 - 있는 경우 Dto로 변환하여 반환한다`() {
        // given
        val product = getProduct(1L, "상품1", 1000)
        productRepository.save(product)

        // when
        val productDto = sut.getProductDtoById(1L)

        // then
        productDto shouldNotBe null
        productDto.id shouldBe product.id
        productDto.name shouldBe product.name
        productDto.originalPrice shouldBe product.originalPrice
    }

    @Test
    fun `getProductDtoById 상품 조회 - 없는 경우 DataNotFound 예외를 던진다`() {
        // given
        val product = getProduct(1L, "상품1", 1000)
        productRepository.save(product)

        // when
        kotlin.runCatching {
            sut.getProductDtoById(999L)
        }.onFailure {
            // then
            it.javaClass shouldBe DataNotFoundException::class.java
            it.shouldHaveMessage("[상품 ID: 999] 상품이 존재하지 않습니다.")
        }
    }
}
