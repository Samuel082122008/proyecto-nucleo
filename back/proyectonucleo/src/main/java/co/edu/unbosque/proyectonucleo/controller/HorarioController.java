package co.edu.unbosque.proyectonucleo.controller;

import java.util.Map;

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

import co.edu.unbosque.proyectonucleo.dto.SesionDTO;
import co.edu.unbosque.proyectonucleo.dto.SesionRespuestaDTO;
import co.edu.unbosque.proyectonucleo.service.HorarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sesiones")
public class HorarioController {

	@Autowired
	private HorarioService horarioService;

	@GetMapping
	public ResponseEntity<?> listar() {
		return ResponseEntity.ok(horarioService.listar());
	}

	@PostMapping
	public ResponseEntity<?> asignar(@Valid @RequestBody SesionDTO dto) {
		return responder(horarioService.asignar(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> mover(@PathVariable Long id, @Valid @RequestBody SesionDTO dto) {
		return responder(horarioService.mover(id, dto), HttpStatus.OK);
	}

	@PostMapping("/validar")
	public ResponseEntity<?> validar(@Valid @RequestBody SesionDTO dto) {
		return ResponseEntity.ok(horarioService.validar(dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		horarioService.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private ResponseEntity<?> responder(HorarioService.ResultadoSesion r, HttpStatus exito) {
		if (r.getCodigo() == 0) {
			return new ResponseEntity<>(new SesionRespuestaDTO(r.getSesion(), r.getAdvertencias()), exito);
		} else if (r.getCodigo() == 2) {
			return new ResponseEntity<>(Map.of("errores", r.getErrores()), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}