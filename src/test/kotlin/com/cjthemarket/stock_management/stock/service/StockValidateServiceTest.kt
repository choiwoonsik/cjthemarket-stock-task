package com.cjthemarket.stock_management.stock.service

import com.cjthemarket.stock_management.response.InvalidInputException
import com.cjthemarket.stock_management.stock.dto.ProductStockDecreaseInput
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test
import kotlin.test.fail

class StockValidateServiceTest {
    private val sut = StockValidateService()

    @Test
    fun `재고 감소 요청 시 0 이하의 개수를 요청하는 경우 에러를 반환한다`() {
        // given
        val input = ProductStockDecreaseInput(
            productId = 1,
            decreaseQuantity = 0,
        )

        // when
        kotlin.runCatching {
            sut.validate(input)
        }.onFailure {
            // then
            it.javaClass shouldBe InvalidInputException::class.java
            it shouldHaveMessage "재고 차감은 0보다 작거나 같을 수 없습니다."
        }.onSuccess { throw AssertionError("예외가 발생해야 합니다.") }
    }

    @Test
    fun `재고 감소 요청 시 1000개를 초과하는 개수를 요청하는 경우 에러를 반환한다`() {
        // given
        val input = ProductStockDecreaseInput(
            productId = 1,
            decreaseQuantity = 1001,
        )

        // when
        kotlin.runCatching {
            sut.validate(input)
        }.onFailure {
            // then
            it.javaClass shouldBe InvalidInputException::class.java
            it shouldHaveMessage "재고 차감은 1회에 최대 1,000개 까지 가능합니다."
        }.onSuccess { fail("예외가 발생해야 합니다") }
    }
}
