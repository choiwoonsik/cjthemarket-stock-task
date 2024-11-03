package com.cjthemarket.stock_management.stock.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class StockResponse(
    @Schema(
        description = "재고 ID",
        nullable = false,
        example = "1",
    )
    @JsonProperty("id")
    val id: Long,

    @Schema(
        description = "상품 ID",
        nullable = false,
        example = "1",
    )
    @JsonProperty("product_id")
    val productId: Long,

    @Schema(
        description = "재고 수량",
        nullable = false,
        example = "10",
    )
    @JsonProperty("stock_quantity")
    val stockQuantity: Long,
)
