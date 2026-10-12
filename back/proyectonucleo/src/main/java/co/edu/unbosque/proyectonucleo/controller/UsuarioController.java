package co.edu.unbosque.proyectonucleo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.proyectonucleo.dto.UsuarioDTO;
import co.edu.unbosque.proyectonucleo.service.UsuarioService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(usuarioService.listar());
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody UsuarioDTO dto) {
		return new ResponseEntity<>(usuarioService.crear(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
		return ResponseEntity.ok(usuarioService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		usuarioService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}