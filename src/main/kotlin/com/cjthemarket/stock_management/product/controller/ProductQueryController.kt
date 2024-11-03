package com.cjthemarket.stock_management.product.controller

import com.cjthemarket.stock_management.product.dto.ProductResponse
import com.cjthemarket.stock_management.product.service.ProductQueryService
import com.cjthemarket.stock_management.product.util.toResponse
import com.cjthemarket.stock_management.response.CommonResult
import com.cjthemarket.stock_management.response.getResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "상품 조회 API")
@RestController
@RequestMapping("/api/v1/product")
class ProductQueryController(
    private val productQueryService: ProductQueryService,
) {
    @Operation(
        summary = "상품 조회",
        description = "상품 ID를 이용하여 조회합니다.",
    )
    @GetMapping
    fun getProduct(
        @Schema(description = "상품 ID", required = true)
        @RequestParam(name = "id") id: Long,
    ): CommonResult {
        val productResponse: ProductResponse = productQueryService.getProductDtoById(id).toResponse()

        return getResult(productResponse)
    }

    @Operation(
        summary = "상품 전체 조회",
        description = "모든 상품을 조회합니다.",
    )
    @GetMapping("/all")
    fun getAllProducts(): CommonResult {
        val productResponses: List<ProductResponse> = productQueryService.getAllProductDtoList().map { it.toResponse() }

        return getResult(productResponses)
    }
}
