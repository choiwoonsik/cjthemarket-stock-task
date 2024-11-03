package com.cjthemarket.stock_management.stock

import com.cjthemarket.stock_management.stock.model.Stock

fun getStock(
    id: Long = 1L,
    quantity: Long? = null,
    productId: Long? = null,
) = Stock(
    id = id,
    quantity = quantity ?: 1L,
    productId = productId ?: 1L,
)
