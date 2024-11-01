package com.cjthemarket.stock_management.response

enum class ErrorCode(
    val message: String? = "오류가 발생헸습니다",
) {
    DEFAULT,
}

/** 4xx 클라이언트 에러 */
open class CustomClientException(
    open val errorCode: ErrorCode? = ErrorCode.DEFAULT,
    msg: String? = "",
    throwable: Throwable? = null,
) : RuntimeException(msg, throwable)
