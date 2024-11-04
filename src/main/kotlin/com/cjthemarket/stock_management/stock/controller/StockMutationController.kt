package com.cjthemarket.stock_management.stock.controller

import com.cjthemarket.stock_management.response.CommonResult
import com.cjthemarket.stock_management.response.getResult
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import com.cjthemarket.stock_management.stock.dto.StockResponse
import com.cjthemarket.stock_management.stock.service.StockMutator
import com.cjthemarket.stock_management.stock.util.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "상품 재고 변경 API")
@RestController
@RequestMapping("/api/v1/stock")
class StockMutationController(
    private val stockMutator: StockMutator,
) {
    @Operation(
        summary = "상품 재고 차감",
        description = "상품 재고를 요청 수 만큼 차감합니다.",
    )
    @PutMapping("/decrease")
    fun decreaseStock(
        @RequestBody input: ProductStockDecreaseInput,
    ): CommonResult {
        val stockResponse: StockResponse = stockMutator.decreaseStockWithLock(input).toResponse()

        return getResult(stockResponse)
    }

    @Operation(
        summary = "상품 재고 설정 (테스트 용)",
        description = "상품 재고를 요청 수로 설정합니다.",
    )
    @PutMapping("/set")
    fun setStock(
        @RequestBody input: ProductStockSetInput,
    ): CommonResult {
        val stockResponse: StockResponse = stockMutator.setStock(input).toResponse()

        return getResult(stockResponse)
    }
}
