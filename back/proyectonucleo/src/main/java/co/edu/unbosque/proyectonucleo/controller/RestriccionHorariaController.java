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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectonucleo.dto.RestriccionHorariaDTO;
import co.edu.unbosque.proyectonucleo.service.RestriccionHorariaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/restricciones")
public class RestriccionHorariaController {

	@Autowired
	private RestriccionHorariaService restriccionService;

	/** GET /restricciones  o  GET /restricciones?docenteId=3 */
	@GetMapping
	public ResponseEntity<?> listar(@RequestParam(required = false) Long docenteId) {
		return ResponseEntity.ok(restriccionService.listar(docenteId));
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody RestriccionHorariaDTO dto) {
		return new ResponseEntity<>(restriccionService.crear(dto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		restriccionService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}