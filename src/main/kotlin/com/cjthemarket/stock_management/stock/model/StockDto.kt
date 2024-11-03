package com.cjthemarket.stock_management.stock.model

data class StockDto(
    val id: Long,
    val productId: Long,
    val quantity: Long,
) {
    constructor(stock: Stock) : this(
        id = stock.id,
        productId = stock.productId,
        quantity = stock.quantity,
    )
}
