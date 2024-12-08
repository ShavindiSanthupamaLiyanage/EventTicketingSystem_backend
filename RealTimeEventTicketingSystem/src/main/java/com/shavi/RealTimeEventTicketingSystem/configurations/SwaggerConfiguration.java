package com.shavi.RealTimeEventTicketingSystem.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class SwaggerConfiguration extends LoggerConfiguration implements WebMvcConfigurer {
    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Initializing Swagger Configuration for Real-Time Event Ticketing System API");

        try {
            OpenAPI openAPI = new OpenAPI()
                    .info(new Info().title("Real-Time Event Ticketing System API").version("1.0.0"))
                    .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                    .components(new Components()
                            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                    .name("bearerAuth")
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Enter JWT token")));

            logger.info("Swagger API configuration setup successfully.");
            return openAPI;
        } catch (Exception e) {
            logger.error("Error occurred during Swagger configuration setup", e);
            throw e; // Rethrow the exception after logging
        }
    }
}

