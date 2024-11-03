package com.cjthemarket.stock_management.product.util

import com.cjthemarket.stock_management.product.dto.ProductDto
import com.cjthemarket.stock_management.product.dto.ProductResponse

fun ProductDto.toResponse(): ProductResponse {
    return ProductResponse(
        id = this.id,
        name = this.name,
        originalPrice = this.originalPrice,
    )
}
