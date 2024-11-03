package com.cjthemarket.stock_management.product.service

import com.cjthemarket.stock_management.product.dto.ProductDto
import com.cjthemarket.stock_management.product.repository.ProductRepository
import com.cjthemarket.stock_management.response.DataNotFoundException
import com.cjthemarket.stock_management.response.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    @Transactional(readOnly = true)
    fun getProductDtoById(id: Long): ProductDto {
        val product = productRepository.findProductById(id)
            ?: throw DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "[상품 ID: $id] 상품이 존재하지 않습니다.")

        return ProductDto(product)
    }

    @Transactional(readOnly = true)
    fun getAllProductDtoList(): List<ProductDto> {
        return productRepository.findAll().map { ProductDto(it) }
    }
}
