package com.cjthemarket.stock_management.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ProductResponse(
    @Schema(
        description = "상품 ID",
        nullable = false,
        example = "1",
    )
    @JsonProperty("id")
    val id: Long,

    @Schema(
        description = "상품명",
        nullable = false,
        example = "비비고 왕교자 1kg",
    )
    @JsonProperty("name")
    val name: String,

    @Schema(
        description = "상품 원가",
        nullable = false,
        example = "4900",
    )
    @JsonProperty("original_price")
    val originalPrice: Long,
)
