package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import com.cjthemarket.stock_management.stock.model.StockDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockMutationService(
    private val stockQueryService: StockQueryService,
) {
    @Transactional
    fun decreaseStock(input: ProductStockDecreaseInput): StockDto {
        val stock = stockQueryService.getStockByProductId(input.productId)

        stock.decreaseQuantity(input.decreaseQuantity)

        return StockDto(stock)
    }

    @Transactional
    fun setStock(input: ProductStockSetInput): StockDto {
        val stock = stockQueryService.getStockByProductId(input.productId)

        stock.setFixQuantity(input.setQuantity)

        return StockDto(stock)
    }
}
