package co.edu.unbosque.proyectonucleo.exception;

public class UsuarioDuplicadoException extends RuntimeException {

	public UsuarioDuplicadoException() {
		super("Ya existe un usuario con ese nombre.");
	}

}