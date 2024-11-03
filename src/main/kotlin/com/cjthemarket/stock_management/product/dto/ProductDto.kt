package com.cjthemarket.stock_management.product.dto

import com.cjthemarket.stock_management.product.model.Product

data class ProductDto(
    val id: Long,
    val name: String,
    val originalPrice: Long,
) {
    constructor(product: Product) : this(
        id = product.id,
        name = product.name,
        originalPrice = product.originalPrice,
    )
}
