package com.forum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearer-jwt";
        OpenAPI api = new OpenAPI()
            .info(new Info().title("Forum API").version("v1"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components().addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
        ApiResponse unauthorized = new ApiResponse().description("Unauthorized").content(new Content().addMediaType("application/json", new MediaType()));
        ApiResponse forbidden = new ApiResponse().description("Forbidden").content(new Content().addMediaType("application/json", new MediaType()));
        api.getComponents().addResponses("UnauthorizedResponse", unauthorized);
        api.getComponents().addResponses("ForbiddenResponse", forbidden);
        return api;
    }
}


