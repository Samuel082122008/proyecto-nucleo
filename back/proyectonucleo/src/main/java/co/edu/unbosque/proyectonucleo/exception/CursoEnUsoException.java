package co.edu.unbosque.proyectonucleo.exception;

public class CursoEnUsoException extends RuntimeException {

	public CursoEnUsoException() {
		super("No se puede eliminar el curso porque tiene grupos o docentes habilitados asociados.");
	}

}