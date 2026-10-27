package co.edu.unbosque.proyectonucleo.exception;

public class UltimoDirectorException extends RuntimeException {

	public UltimoDirectorException() {
		super("No se puede eliminar ni cambiar el rol del único usuario con rol DIRECTOR.");
	}

}