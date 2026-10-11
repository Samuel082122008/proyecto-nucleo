package co.edu.unbosque.proyectonucleo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectonucleo.dto.GrupoDTO;
import co.edu.unbosque.proyectonucleo.service.GrupoService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private GrupoService grupoService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(grupoService.listar());
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody GrupoDTO dto) {
		return new ResponseEntity<>(grupoService.crear(dto), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}/cerrar")
	public ResponseEntity<?> cerrar(@PathVariable Long id) {
		return ResponseEntity.ok(grupoService.cerrar(id));
	}
}
