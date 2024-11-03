package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.response.ErrorCode
import com.cjthemarket.stock_management.response.InvalidInputException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import org.springframework.stereotype.Service

@Service
class StockValidateService {
    fun validate(input: ProductStockDecreaseInput) {
        if (input.decreaseQuantity <= 0) {
            throw InvalidInputException(ErrorCode.INVALID_INPUT, "재고 차감은 0보다 작거나 같을 수 없습니다.")
        }
        if (input.decreaseQuantity > 1000) {
            throw InvalidInputException(ErrorCode.INVALID_INPUT, "재고 차감은 1회에 최대 1,000개 까지 가능합니다.")
        }
    }

    fun validate(input: ProductStockSetInput) {
        /**
         * 테스트를 위해 별다른 validation 없이 종료
         */
    }
}
