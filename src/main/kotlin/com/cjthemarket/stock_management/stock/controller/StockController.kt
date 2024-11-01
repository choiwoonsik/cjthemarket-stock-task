package com.cjthemarket.stock_management.stock.controller

import com.cjthemarket.stock_management.response.CommonResult
import com.cjthemarket.stock_management.response.getSuccessResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stock")
class StockController {

    @GetMapping
    fun getStock(): CommonResult {
        return getSuccessResult()
    }
}