package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.distributedlock.DistributedLockService
import com.cjthemarket.stock_management.response.InvalidRequestException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import com.cjthemarket.stock_management.stock.dto.ProductStockSetInput
import com.cjthemarket.stock_management.stock.getStock
import com.cjthemarket.stock_management.stock.model.StockDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.orm.jpa.JpaSystemException

class StockMutatorTest {
    private val stockMutationService: StockMutationService = mockk(relaxed = true)
    private val stockValidateService: StockValidateService = mockk(relaxed = true)
    private val distributedLockService: DistributedLockService = mockk(relaxed = true)
    private val sut = StockMutator(stockValidateService, stockMutationService, distributedLockService)

    @Test
    fun `decreaseStock - 재고 감소 요청을 받아서 validation 후 요청을 수행하고 StockDto를 반환한다`() {
        // given
        val realStock = getStock(1L, 0L, 3L)
        val stockDto = StockDto(realStock)

        val decreaseInput = ProductStockDecreaseInput(3L, 2L)

        every { stockValidateService.validate(decreaseInput) } returns Unit
        every { distributedLockService.lock(any(), any(), any(), any<() -> StockDto>()) } returns stockDto

        // when
        val decreaseStock = sut.decreaseStockWithLock(decreaseInput)

        // then
        decreaseStock.id shouldBe stockDto.id
        decreaseStock.productId shouldBe stockDto.productId
        decreaseStock.quantity shouldBe stockDto.quantity
    }

    @Test
    fun `setStock - 재고 초기화 요청을 받아서 validation 후 요청을 수행하고 StockDto를 반환한다`() {
        // given
        val realStock = getStock(1L, 0L, 3L)
        val stockDto = StockDto(realStock)

        val setInput = ProductStockSetInput(3L, 0L)

        every { stockValidateService.validate(setInput) } returns Unit
        every { stockMutationService.setStock(setInput) } returns stockDto

        // when
        val decreaseStock = sut.setStock(setInput)

        // then
        decreaseStock.id shouldBe stockDto.id
        decreaseStock.productId shouldBe stockDto.productId
        decreaseStock.quantity shouldBe stockDto.quantity
    }

    @Test
    fun `setStock - 재고 초기화 요청 중 JpaSystemException 이 발생하면 InvalidRequestException 으로 변환하여 던진다`() {
        // given
        val setInput = ProductStockSetInput(3L, 0L)

        every { stockValidateService.validate(setInput) } returns Unit
        every { stockMutationService.setStock(setInput) } answers { throw JpaSystemException(RuntimeException()) }

        // when
        kotlin.runCatching {
            sut.setStock(setInput)
        }.onFailure {
            // then
            it.javaClass shouldBe InvalidRequestException::class.java
            it shouldHaveMessage "[상품 ID: ${setInput.productId}] 재고 설정 SQL 수행 중 오류가 발생했습니다."
        }.onSuccess { throw RuntimeException("Expected exception") }
    }
}
