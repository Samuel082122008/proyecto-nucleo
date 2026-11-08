package co.edu.unbosque.proyectonucleo.exception;

public class ContrasenaInvalidaException extends RuntimeException {

	public ContrasenaInvalidaException() {
		super("La contraseña es obligatoria y debe tener entre 6 y 72 caracteres.");
	}

}