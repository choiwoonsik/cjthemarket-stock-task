package com.cjthemarket.stock_management.product.repository

import com.cjthemarket.stock_management.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<Product, Long>
