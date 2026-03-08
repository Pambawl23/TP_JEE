package com.polytech.commandes.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI openAPI() {
        var scheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
        var openAPI = new OpenAPI().components(new Components().addSecuritySchemes("bearer-jwt", scheme))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
        openAPI.setInfo(new io.swagger.v3.oas.models.info.Info()
                .title("API Boutique").version("1.0")
                .description("1) POST /securite/token avec email et nom pour obtenir le token. " +
                             "2) Cliquer sur « Autoriser » et coller le token. " +
                             "3) Appeler les autres API."));
        return openAPI;
    }

    /**
     * Force Swagger/OpenAPI to expose at most one success response code plus 403 for every
     * operation. By default springdoc adds a bunch of codes (200, 201, 500 etc) which the
     * user found confusing; the requirement is to display only "created" (201) or
     * "not authorized" (403) on the UI.
     */
    // note: use US spelling 'Customizer' to match springdoc's class name

    @Bean
    public org.springdoc.core.customizers.OpenApiCustomizer responseCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        var existing = operation.getResponses();
                        if (existing == null || existing.isEmpty()) {
                            return;
                        }
                        // keep first non-403 code as success
                        String successCode = existing.keySet().stream()
                                .filter(code -> !"403".equals(code))
                                .findFirst().orElse(null);
                        io.swagger.v3.oas.models.responses.ApiResponses filtered = new io.swagger.v3.oas.models.responses.ApiResponses();
                        if (successCode != null) {
                            filtered.addApiResponse(successCode, existing.get(successCode));
                        }
                        // always expose a 403 response
                        filtered.addApiResponse("403", new io.swagger.v3.oas.models.responses.ApiResponse().description("Non autorisé"));
                        operation.setResponses(filtered);
                    })
            );
        };
    }
}
