package co.edu.unbosque.proyectonucleo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
			UsuarioNoEncontradoException.class, HabilitacionNoEncontradaException.class,
			RestriccionNoEncontradaException.class })
	public ResponseEntity<Map<String, Object>> handleNoEncontrado(RuntimeException ex) {
		return responder(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler({ IdInvalidoException.class, DatosObligatoriosException.class, NombreInvalidoException.class,
			NombreDocenteInvalidoException.class, CapacidadInvalidaException.class, EscalafonInvalidoException.class,
			EstudiantesProyectadosInvalidosException.class, RangoHorarioInvalidoException.class,
			UsuarioInvalidoException.class, ContrasenaInvalidaException.class, DocenteRequeridoException.class })
	public ResponseEntity<Map<String, Object>> handleDatoInvalido(RuntimeException ex) {
		return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	}

	@ExceptionHandler(ReglaNegocioException.class)
	public ResponseEntity<Map<String, Object>> handleReglaNegocio(ReglaNegocioException ex) {
		return responder(HttpStatus.CONFLICT, ex.getMessage(), ex.getErrores());
	}

	@ExceptionHandler({ UsuarioDuplicadoException.class, HabilitacionDuplicadaException.class,
			AulaEnUsoException.class, CursoEnUsoException.class, DocenteEnUsoException.class,
			UltimoDirectorException.class })
	public ResponseEntity<Map<String, Object>> handleConflicto(RuntimeException ex) {
		return responder(HttpStatus.CONFLICT, ex.getMessage(), null);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<Map<String, Object>> handleCredenciales(CredencialesInvalidasException ex) {
		return responder(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
		List<String> errores = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
		return responder(HttpStatus.BAD_REQUEST, "Datos invalidos", errores);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
		return responder(HttpStatus.BAD_REQUEST,
				"El cuerpo de la solicitud no es un JSON válido o tiene valores incorrectos (revisa las horas en formato HH:mm y los valores permitidos)",
				null);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, Object>> handleTipoParametro(MethodArgumentTypeMismatchException ex) {
		return responder(HttpStatus.BAD_REQUEST, "El valor del parámetro '" + ex.getName() + "' no es válido", null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleIntegridad(DataIntegrityViolationException ex) {
		return responder(HttpStatus.CONFLICT,
				"La operacion viola una restriccion de la base de datos (registro duplicado o con datos relacionados)", null);
	}

	@ExceptionHandler({ DataAccessResourceFailureException.class, CannotCreateTransactionException.class })
	public ResponseEntity<Map<String, Object>> handleBaseNoDisponible(RuntimeException ex) {
		return responder(HttpStatus.SERVICE_UNAVAILABLE,
				"No se pudo conectar con la base de datos. Intenta de nuevo en unos minutos.", null);
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