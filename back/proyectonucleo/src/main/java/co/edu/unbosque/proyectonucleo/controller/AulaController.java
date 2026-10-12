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

import co.edu.unbosque.proyectonucleo.dto.AulaDTO;
import co.edu.unbosque.proyectonucleo.service.AulaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aulas")
public class AulaController {

	@Autowired
	private AulaService aulaService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(aulaService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id) {
		return ResponseEntity.ok(aulaService.buscar(id));
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody AulaDTO dto) {
		return new ResponseEntity<>(aulaService.crear(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody AulaDTO dto) {
		return ResponseEntity.ok(aulaService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		aulaService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}