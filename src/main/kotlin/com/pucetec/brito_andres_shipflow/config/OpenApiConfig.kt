package com.pucetec.brito_andres_shipflow.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class OpenApiConfig {

    @Bean
    @Suppress("unused")
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("ShipFlow API")
                    .description("API para gestión de envíos")
                    .version("1.0.0")
            )
    }
}