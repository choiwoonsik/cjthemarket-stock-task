package com.cjthemarket.stock_management.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Optional

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "datetimeProvider")
class AuditorConfig {
    @Bean
    fun datetimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(OffsetDateTime.now(ZoneId.of("Asia/Seoul"))) }
    }
}
