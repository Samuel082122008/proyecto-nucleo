package co.edu.unbosque.proyectonucleo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Value("${app.frontend.allowed-origins}")
	private String[] origenes;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(origenes)
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
	}
}