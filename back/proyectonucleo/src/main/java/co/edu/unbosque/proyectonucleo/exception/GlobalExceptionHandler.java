package co.edu.unbosque.proyectonucleo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<Map<String, Object>> responder(HttpStatus status, String mensaje, List<String> errores) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("mensaje", mensaje);
		error.put("estado", status.value());
		if (errores != null) error.put("errores", errores);
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler({ DocenteNoEncontradoException.class, AulaNoEncontradaException.class,
			CursoNoEncontradoException.class, GrupoNoEncontradoException.class,
			SesionNoEncontradaException.class, ParametroSemestreNoConfiguradoException.class,
			UsuarioNoEncontradoException.class })
	public ResponseEntity<Map<String, Object>> handleNoEncontrado(RuntimeException ex) {
		return responder(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler(ReglaNegocioException.class)
	public ResponseEntity<Map<String, Object>> handleReglaNegocio(ReglaNegocioException ex) {
		return responder(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrores());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
		List<String> errores = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
		return responder(HttpStatus.BAD_REQUEST, "Datos invalidos", errores);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleIntegridad(DataIntegrityViolationException ex) {
		return responder(HttpStatus.CONFLICT,
				"La operacion viola una restriccion de la base de datos (registro duplicado o con datos relacionados)", null);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
		return responder(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
	}
}