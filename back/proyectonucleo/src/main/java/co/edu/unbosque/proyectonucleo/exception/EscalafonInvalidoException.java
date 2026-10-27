package co.edu.unbosque.proyectonucleo.exception;

public class EscalafonInvalidoException extends RuntimeException {

	public EscalafonInvalidoException() {
		super("El escalafón no puede superar los 50 caracteres.");
	}

}