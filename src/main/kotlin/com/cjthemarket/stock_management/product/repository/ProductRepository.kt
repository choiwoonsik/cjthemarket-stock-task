package com.cjthemarket.stock_management.product.repository

import com.cjthemarket.stock_management.product.model.Product
import com.cjthemarket.stock_management.product.model.QProduct.Companion.product
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val productJpaRepository: ProductJpaRepository,
) : QuerydslRepositorySupport(Product::class.java),
    ProductJpaRepository by productJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }

    fun findProductById(id: Long): Product? {
        return from(product)
            .where(product.id.eq(id))
            .fetchOne()
    }
}
