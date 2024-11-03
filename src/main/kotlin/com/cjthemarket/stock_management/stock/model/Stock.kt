package com.cjthemarket.stock_management.stock.model

import com.cjthemarket.stock_management.config.BaseTimeEntity
import com.cjthemarket.stock_management.response.ErrorCode
import com.cjthemarket.stock_management.response.InvalidRequestException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stocks")
class Stock(
    @Column(nullable = false) var quantity: Long,
    @Column(nullable = false) val productId: Long,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
) : BaseTimeEntity() {

    fun decreaseQuantity(decreaseQuantity: Long) {
        if (this.quantity < decreaseQuantity) {
            throw InvalidRequestException(ErrorCode.INVALID_REQUEST, "[재고 ID: $id] 재고가 부족합니다.")
        }

        this.quantity -= decreaseQuantity
    }

    fun setFixQuantity(setQuantity: Long) {
        this.quantity = setQuantity
    }
}
