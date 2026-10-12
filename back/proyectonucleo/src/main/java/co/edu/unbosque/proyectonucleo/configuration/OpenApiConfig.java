package co.edu.unbosque.proyectonucleo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Agrega el boton "Authorize" a swagger para enviar el token JWT
 * (Authorization: Bearer ...) en todas las peticiones.
 */
@Configuration
public class OpenApiConfig {

	private static final String ESQUEMA = "bearerAuth";

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Proyecto Nucleo - Horarios").version("1.0"))
				.addSecurityItem(new SecurityRequirement().addList(ESQUEMA))
				.components(new Components().addSecuritySchemes(ESQUEMA,
						new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")));
	}
}