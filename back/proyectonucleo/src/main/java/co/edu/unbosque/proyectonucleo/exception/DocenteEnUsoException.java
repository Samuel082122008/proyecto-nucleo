package co.edu.unbosque.proyectonucleo.exception;

public class DocenteEnUsoException extends RuntimeException {

	public DocenteEnUsoException() {
		super("No se puede eliminar el docente porque tiene sesiones, habilitaciones, restricciones horarias o un usuario asociado.");
	}

}