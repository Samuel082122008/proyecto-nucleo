package co.edu.unbosque.proyectonucleo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectonucleo.dto.HabilitacionDTO;
import co.edu.unbosque.proyectonucleo.service.HabilitacionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/habilitaciones")
public class HabilitacionController {

	@Autowired
	private HabilitacionService habilitacionService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(habilitacionService.listar());
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody HabilitacionDTO dto) {
		return new ResponseEntity<>(habilitacionService.crear(dto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		habilitacionService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}