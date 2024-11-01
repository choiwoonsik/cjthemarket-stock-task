package com.cjthemarket.stock_management.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun cjthemarketStockManagementApi(): OpenAPI {
        val apiInfo = Info()
            .title("CJ The Market 쇼핑몰 재고 차감 관리 API 명세서")
        val apiDocs = ExternalDocumentation()
            .description("CJ The Market 쇼핑몰 재고 차감 관리 API 명세서")
            .url("https://woonsik.notion.site/API-130efd1222118037bce4fadb72f129cb?pvs=4")

        return OpenAPI()
            .info(apiInfo)
            .externalDocs(apiDocs)
    }
}
