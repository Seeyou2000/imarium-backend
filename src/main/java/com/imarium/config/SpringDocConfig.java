package com.imarium.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class SpringDocConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Imarium API 명세서")
                        .description("Imarium REST API 서비스")
                        .version("v1.0")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("http://myapiserver.com"))

        );
    }
}

