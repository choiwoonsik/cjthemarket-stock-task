package com.cjthemarket.stock_management.config

import com.cjthemarket.stock_management.response.CommonResult
import com.cjthemarket.stock_management.response.CustomClientException
import com.cjthemarket.stock_management.response.getFailResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): CommonResult {
        return getFailResult("[미정의 에러]: 원인: ${e.cause}, 내용: ${e.message}\n${e.printStackTrace()}")
    }

    @ExceptionHandler(CustomClientException::class)
    fun handleCustomClientException(e: CustomClientException): CommonResult {
        return getFailResult("[${e.errorCode}]: ${e.message}")
    }
}
