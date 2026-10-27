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

import co.edu.unbosque.proyectonucleo.dto.DocenteDTO;
import co.edu.unbosque.proyectonucleo.service.DocenteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/docentes")
public class DocenteController {

	@Autowired
	private DocenteService docenteService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(docenteService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id) {
		return ResponseEntity.ok(docenteService.buscar(id));
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody DocenteDTO dto) {
		return new ResponseEntity<>(docenteService.crear(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody DocenteDTO dto) {
		return ResponseEntity.ok(docenteService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		docenteService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}