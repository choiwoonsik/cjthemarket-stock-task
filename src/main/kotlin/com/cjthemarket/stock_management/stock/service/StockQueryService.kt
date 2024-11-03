package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.response.DataNotFoundException
import com.cjthemarket.stock_management.response.ErrorCode.DATA_NOT_FOUND
import com.cjthemarket.stock_management.stock.model.Stock
import com.cjthemarket.stock_management.stock.model.StockDto
import com.cjthemarket.stock_management.stock.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockQueryService(
    private val stockRepository: StockRepository,
) {
    @Transactional(readOnly = true)
    fun getStockByProductId(productId: Long): Stock {
        return stockRepository.findStockByProductId(productId)
            ?: throw DataNotFoundException(DATA_NOT_FOUND, "[상품 ID: $productId] 해당 상품의 재고가 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun getStockDtoByProductId(productId: Long): StockDto {
        val stock: Stock = getStockByProductId(productId)

        return StockDto(stock)
    }
}
