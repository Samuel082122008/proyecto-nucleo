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
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import co.edu.unbosque.proyectonucleo.security.UsuarioAutenticado;

@RestController
@RequestMapping("/restricciones")
public class RestriccionHorariaController {

	@Autowired
	private RestriccionHorariaService restriccionService;

	/** GET /restricciones  o  GET /restricciones?docenteId=3 (el docente solo ve las suyas) */
	@GetMapping
	public ResponseEntity<?> listar(@RequestParam(required = false) Long docenteId,
			@AuthenticationPrincipal UsuarioAutenticado u) {
		if (esDocente(u)) {
			if (u.docenteId() == null) return ResponseEntity.ok(List.of());
			return ResponseEntity.ok(restriccionService.listar(u.docenteId()));
		}
		return ResponseEntity.ok(restriccionService.listar(docenteId));
	}

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody RestriccionHorariaDTO dto,
			@AuthenticationPrincipal UsuarioAutenticado u) {
		if (esDocente(u)) {
			if (u.docenteId() == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			dto.setDocenteId(u.docenteId()); // ignora el docenteId que mande el cliente
		}
		return new ResponseEntity<>(restriccionService.crear(dto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado u) {
		if (esDocente(u)) {
			restriccionService.eliminarDeDocente(id, u.docenteId());
		} else {
			restriccionService.eliminar(id);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private boolean esDocente(UsuarioAutenticado u) {
		return u != null && "DOCENTE".equals(u.rol());
	}
}