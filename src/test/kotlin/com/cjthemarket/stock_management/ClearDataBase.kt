package com.cjthemarket.stock_management

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class ClearDataBaseBefore : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val flyway = SpringExtension.getApplicationContext(context).getBean(Flyway::class.java)
        flyway.clean()
        flyway.migrate()
    }
}

class ClearDatabaseAfter : AfterAllCallback {
    override fun afterAll(context: ExtensionContext) {
        val flyway = SpringExtension.getApplicationContext(context).getBean(Flyway::class.java)
        flyway.clean()
        flyway.migrate()
    }
}
