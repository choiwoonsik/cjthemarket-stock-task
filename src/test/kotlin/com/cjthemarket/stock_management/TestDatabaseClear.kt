package com.cjthemarket.stock_management

import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(ClearDataBaseBefore::class, ClearDatabaseAfter::class)
annotation class TestDatabaseClear
