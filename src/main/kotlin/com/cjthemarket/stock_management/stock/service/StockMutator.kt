package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.response.ErrorCode
import com.cjthemarket.stock_management.response.InvalidRequestException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import com.cjthemarket.stock_management.stock.model.StockDto
import org.springframework.orm.jpa.JpaSystemException
import org.springframework.stereotype.Service

@Service
class StockMutator(
    private val stockValidateService: StockValidateService,
    private val stockMutationService: StockMutationService,
) {
    fun decreaseStock(input: ProductStockDecreaseInput): StockDto {
        stockValidateService.validate(input)

        return try {
            stockMutationService.decreaseStock(input)
        } catch (e: JpaSystemException) {
            throw InvalidRequestException(
                errorCode = ErrorCode.INVALID_REQUEST,
                msg = "[상품 ID: ${input.productId}] 재고 감소 SQL 수행 중 오류가 발생했습니다.",
            )
        }
    }

    fun setStock(input: ProductStockSetInput): StockDto {
        stockValidateService.validate(input)

        return try {
            stockMutationService.setStock(input)
        } catch (e: JpaSystemException) {
            throw InvalidRequestException(
                errorCode = ErrorCode.INVALID_REQUEST,
                msg = "[상품 ID: ${input.productId}] 재고 설정 SQL 수행 중 오류가 발생했습니다.",
            )
        }
    }
}
