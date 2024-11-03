package com.cjthemarket.stock_management.response

enum class ErrorCode(
    val message: String? = "오류가 발생헸습니다",
) {
    DEFAULT,
    DATA_NOT_FOUND("원하는 데이터를 찾을 수 없습니다."),
    INVALID_INPUT("잘못된 입력 값입니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
}

/** 4xx 클라이언트 에러 */
open class CustomClientException(
    open val errorCode: ErrorCode? = ErrorCode.DEFAULT,
    msg: String? = "",
    throwable: Throwable? = null,
) : RuntimeException(msg, throwable)

/** 찾고자 하는 데이터가 없는 경우 */
data class DataNotFoundException(
    override val errorCode: ErrorCode? = ErrorCode.DEFAULT,
    val msg: String? = null,
    val throwable: Throwable? = null,
) : CustomClientException(errorCode, msg, throwable)

/** 입력값이 잘못된 경우 */
data class InvalidInputException(
    override val errorCode: ErrorCode? = ErrorCode.INVALID_INPUT,
    val msg: String? = null,
    val throwable: Throwable? = null,
) : CustomClientException(errorCode, msg, throwable)

/** 요청값이 잘못된 경우 */
data class InvalidRequestException(
    override val errorCode: ErrorCode? = ErrorCode.INVALID_REQUEST,
    val msg: String? = null,
    val throwable: Throwable? = null,
) : CustomClientException(errorCode, msg, throwable)
