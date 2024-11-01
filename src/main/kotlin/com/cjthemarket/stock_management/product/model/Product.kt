package com.cjthemarket.stock_management.product.model

import com.cjthemarket.stock_management.config.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val originalPrice: Long,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
) : BaseTimeEntity()
