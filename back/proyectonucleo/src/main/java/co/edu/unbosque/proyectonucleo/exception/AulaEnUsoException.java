package co.edu.unbosque.proyectonucleo.exception;

public class AulaEnUsoException extends RuntimeException {

	public AulaEnUsoException() {
		super("No se puede eliminar el aula porque tiene sesiones asignadas.");
	}

}