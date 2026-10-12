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

import co.edu.unbosque.proyectonucleo.dto.CursoDTO;
import co.edu.unbosque.proyectonucleo.service.CursoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
public class CursoController {

	@Autowired
	private CursoService cursoService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(cursoService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id) {
		return ResponseEntity.ok(cursoService.buscar(id));
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody CursoDTO dto) {
		return new ResponseEntity<>(cursoService.crear(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CursoDTO dto) {
		return ResponseEntity.ok(cursoService.actualizar(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		cursoService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}