package com.cjthemarket.stock_management.distributedlock

import com.cjthemarket.stock_management.ClearDataBaseBefore
import com.cjthemarket.stock_management.ClearDatabaseAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CompletableFuture

@ActiveProfiles("test")
@SpringBootTest
@Import(ClearDataBaseBefore::class, ClearDatabaseAfter::class)
class DistributedLockServiceTest(
    @Qualifier("redissonClient") private val redissonClient: RedissonClient,
) {
    companion object {
        private var count: Int = 0
    }

    private val sut = DistributedLockService(redissonClient)
    private val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)

    @Test
    fun `비동기 환경에서 분산락을 이용하여 동시성 이슈를 방지해야한다`() {
        // given
        count = 100000
        val minusSize = 100
        val repeatCount = count / minusSize

        // when
        (1..repeatCount)
            .map {
                CompletableFuture.supplyAsync(
                    {
                        decreaseStock(minusSize)
                    },
                    getThreadTaskExecutor(),
                )
            }.forEach {
                it.join()
            }

        // then
        count shouldBe 0
    }

    @Test
    fun `비동기 환경에서 분산락이 없다면 동시성 이슈가 발생한다`() {
        // given
        count = 1000000
        val minusSize = 1
        val repeatCount = count / minusSize

        // when
        (1..repeatCount)
            .map {
                CompletableFuture.supplyAsync {
                    count -= minusSize
                }
            }.forEach {
                it.join()
            }

        // then
        logger.info("Result Count: $count")
        count shouldNotBe 0
    }

    private fun decreaseStock(minus: Int) {
        sut.lock("key", 10, 10) {
            count -= minus
        }
        logger.info("count: $count")
    }

    private fun getThreadTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 20
        executor.queueCapacity = 100
        executor.setThreadNamePrefix("multi thread -")
        executor.initialize()

        return executor
    }
}
