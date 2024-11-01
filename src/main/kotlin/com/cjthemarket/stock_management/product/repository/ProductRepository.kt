package com.cjthemarket.stock_management.product.repository

import com.cjthemarket.stock_management.product.model.Product
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val productJapRepository: ProductJapRepository,
) : QuerydslRepositorySupport(Product::class.java),
    ProductJapRepository by productJapRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}
