package com.cjthemarket.stock_management.stock.model

import com.cjthemarket.stock_management.config.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stocks")
class Stock(
    @Column(nullable = false) val quantity: Long,
    @Column(nullable = false) val productId: Long,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
) :  BaseTimeEntity()
