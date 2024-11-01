package com.cjthemarket.stock_management.response

open class CommonResult {
    var success: Boolean = SUCCESS
    var message: String = "성공"
}

data class SingleResult<T>(
    var data: T,
) : CommonResult()

fun <T> getSingleResult(data: T, message: String? = null): SingleResult<T> {
    return SingleResult(data)
        .also {
            it.success = SUCCESS
            it.message = message ?: "성공"
        }
}

fun getSuccessResult(message: String? = null): CommonResult {
    return CommonResult()
        .also {
            it.success = SUCCESS
            it.message = message ?: "성공"
        }
}

fun getFailResult(message: String? = null): CommonResult {
    return CommonResult()
        .also {
            it.success = FAIL
            it.message = message ?: "실패"
        }
}

const val SUCCESS = true
const val FAIL = false