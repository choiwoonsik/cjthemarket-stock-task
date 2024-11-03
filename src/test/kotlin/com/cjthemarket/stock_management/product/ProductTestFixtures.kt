package com.cjthemarket.stock_management.product

import com.cjthemarket.stock_management.product.model.Product

fun getProduct(
    id: Long? = null,
    name: String? = null,
    originalPrice: Long? = null,
): Product {
    return Product(
        id = id ?: 1L,
        name = name ?: "상품명",
        originalPrice = originalPrice ?: 1000,
    )
}
