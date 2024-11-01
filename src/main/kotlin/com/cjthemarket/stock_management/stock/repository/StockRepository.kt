package com.cjthemarket.stock_management.stock.repository

import com.cjthemarket.stock_management.stock.model.Stock
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class StockRepository(
    private val stockJpaRepository: StockJpaRepository,
) : QuerydslRepositorySupport(Stock::class.java),
    StockJpaRepository by stockJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}
