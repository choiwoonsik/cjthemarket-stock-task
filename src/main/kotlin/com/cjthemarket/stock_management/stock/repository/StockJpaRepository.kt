package com.cjthemarket.stock_management.stock.repository

import com.cjthemarket.stock_management.stock.model.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockJpaRepository : JpaRepository<Stock, Long>
