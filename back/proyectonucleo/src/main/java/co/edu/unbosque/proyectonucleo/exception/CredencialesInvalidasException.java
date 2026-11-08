package co.edu.unbosque.proyectonucleo.exception;

public class CredencialesInvalidasException extends RuntimeException {

	public CredencialesInvalidasException() {
		super("Credenciales incorrectas");
	}

}