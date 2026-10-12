package co.edu.unbosque.proyectonucleo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectonucleo.dto.LoginDTO;
import co.edu.unbosque.proyectonucleo.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;

	/** Responde {"token": "...", "rol": "COORDINADOR"} o 401 si las credenciales fallan. */
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
		try {
			Map<String, String> respuesta = usuarioService.login(dto);
			return ResponseEntity.ok(respuesta);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(Map.of("mensaje", "Credenciales incorrectas"), HttpStatus.UNAUTHORIZED);
		}
	}
}
