package com.cjthemarket.stock_management.stock.controller

import com.cjthemarket.stock_management.response.CommonResult
import com.cjthemarket.stock_management.response.getResult
import com.cjthemarket.stock_management.stock.dto.StockResponse
import com.cjthemarket.stock_management.stock.service.StockQueryService
import com.cjthemarket.stock_management.stock.util.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "상품 재고 조회 API")
@RestController
@RequestMapping("/api/v1/stock")
class StockQueryController(
    private val stockQueryService: StockQueryService,
) {
    @Operation(
        summary = "상품 재고 조회",
        description = "상품 ID로 상품 재고를 조회합니다.",
    )
    @GetMapping
    fun getStock(
        @Schema(description = "상품 ID", required = true)
        @RequestParam("product_id") productId: Long,
    ): CommonResult {
        val stockResponse: StockResponse = stockQueryService.getStockDtoByProductId(productId).toResponse()

        return getResult(stockResponse)
    }
}
