package co.edu.unbosque.proyectonucleo.exception;

public class IdInvalidoException extends RuntimeException {

	public IdInvalidoException() {
		super("El identificador es obligatorio y debe ser un número entero positivo.");
	}

}