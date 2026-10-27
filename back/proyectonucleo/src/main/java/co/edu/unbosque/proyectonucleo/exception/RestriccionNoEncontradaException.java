package co.edu.unbosque.proyectonucleo.exception;

public class RestriccionNoEncontradaException extends RuntimeException {

	public RestriccionNoEncontradaException() {
		super("Restricción horaria no encontrada");
	}

}