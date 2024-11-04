package com.cjthemarket.stock_management.distributedlock

import com.cjthemarket.stock_management.response.ErrorCode
import com.cjthemarket.stock_management.response.InvalidRequestException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class DistributedLockService(
    private val redissonClient: RedissonClient,
) {
    /**
     * @param lockKey: 락을 구분하는 키 값.
     * @param waitSeconds: 락을 획득하기 위해 시도하는 최대 대기 시간.
     * @param leaseSeconds: 락을 획득한 후 자동으로 해제될 때까지 유지되는 시간.
     */
    fun <T> lock(lockKey: String, waitSeconds: Long = 3, leaseSeconds: Long = 3, logic: () -> T): T {
        val lock = redissonClient.getLock(lockKey)
        val isGetLocked = lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS)
        return try {
            if (isGetLocked.not()) {
                throw InvalidRequestException(ErrorCode.INVALID_REQUEST, "락 키 [$lockKey] 획득에 실패했습니다.")
            } else {
                logic()
            }
        } finally {
            if (lock.isLocked && lock.isHeldByCurrentThread) lock.unlock()
        }
    }
}
