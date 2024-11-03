package com.cjthemarket.stock_management.stock.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 재고 차감 요청값")
data class ProductStockDecreaseInput(
    @Schema(description = "상품 ID", example = "1")
    @JsonProperty("product_id")
    val productId: Long,

    @Schema(description = "재고 차감 수량", example = "10")
    @JsonProperty("decrease_quantity")
    val decreaseQuantity: Long,
)

@Schema(description = "상품 재고 설정 요청값")
data class ProductStockSetInput(
    @Schema(description = "상품 ID", example = "1")
    @JsonProperty("product_id")
    val productId: Long,

    @Schema(description = "재고 설정 수량", example = "100")
    @JsonProperty("set_quantity")
    val setQuantity: Long,
)
