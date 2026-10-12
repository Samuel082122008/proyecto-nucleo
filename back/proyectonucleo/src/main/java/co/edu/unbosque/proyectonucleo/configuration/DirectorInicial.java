package co.edu.unbosque.proyectonucleo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import co.edu.unbosque.proyectonucleo.entity.Usuario;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;

/**
 * Crea un usuario DIRECTOR inicial si todavia no existe, para poder hacer
 * login la primera vez. Las credenciales se pueden cambiar con las propiedades
 * app.director.username y app.director.password.
 */
@Configuration
public class DirectorInicial {

	@Bean
	CommandLineRunner crearDirectorInicial(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder,
			@Value("${app.director.username:director}") String username,
			@Value("${app.director.password:12345678}") String password) {
		return args -> {
			if (!usuarioRepo.existsByUsername(username)) {
				Usuario u = new Usuario();
				u.setUsername(username);
				u.setPassword(passwordEncoder.encode(password));
				u.setRol(RolUsuario.DIRECTOR);
				u.setDocenteId(null);
				usuarioRepo.save(u);
				System.out.println(">>> Usuario DIRECTOR inicial creado: " + username);
			}
		};
	}
}