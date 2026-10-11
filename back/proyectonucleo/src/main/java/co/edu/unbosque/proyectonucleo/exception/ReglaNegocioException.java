package co.edu.unbosque.proyectonucleo.exception;

import java.util.List;

public class ReglaNegocioException extends RuntimeException {
	private final List<String> errores;

	public ReglaNegocioException(List<String> errores) {
		super(String.join(" | ", errores));
		this.errores = errores;
	}

	public List<String> getErrores() { return errores; }
}
