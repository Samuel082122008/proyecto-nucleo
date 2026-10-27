package co.edu.unbosque.proyectonucleo.exception;

public class NombreInvalidoException extends RuntimeException {

	public NombreInvalidoException() {
		super("El nombre es obligatorio, no puede estar vacío y no puede superar los 100 caracteres.");
	}

}