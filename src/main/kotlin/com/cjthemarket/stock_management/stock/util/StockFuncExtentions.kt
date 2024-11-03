package com.cjthemarket.stock_management.stock.util

import com.cjthemarket.stock_management.stock.dto.StockResponse
import com.cjthemarket.stock_management.stock.model.StockDto

fun StockDto.toResponse(): StockResponse {
    return StockResponse(
        id = this.id,
        productId = this.productId,
        stockQuantity = this.quantity,
    )
}
