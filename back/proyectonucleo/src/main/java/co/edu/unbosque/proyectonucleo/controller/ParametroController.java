package co.edu.unbosque.proyectonucleo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectonucleo.dto.ParametroSemestreDTO;
import co.edu.unbosque.proyectonucleo.service.ParametroService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

	@Autowired
	private ParametroService parametroService;

	@GetMapping
	public ResponseEntity<?> obtener() {
		return ResponseEntity.ok(parametroService.obtener());
	}

	@PutMapping
	public ResponseEntity<?> actualizar(@Valid @RequestBody ParametroSemestreDTO dto) {
		return ResponseEntity.ok(parametroService.actualizar(dto));
	}
}