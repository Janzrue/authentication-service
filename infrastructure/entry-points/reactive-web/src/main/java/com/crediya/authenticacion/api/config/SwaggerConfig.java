package com.crediya.authenticacion.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Authentication - CrediYa")
                        .version("1.0.0")
                        .description("Complete documentation for authentication and user/role management for CrediYa PRAGMA bootcamp.")
                        .termsOfService("https://www.crediya.com/terms")
                        .contact(new Contact().name("JUAN MANUEL PEREZ ALVAREZ - juanmaperez5826@gmail.com").email("juanmaperez5826@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}