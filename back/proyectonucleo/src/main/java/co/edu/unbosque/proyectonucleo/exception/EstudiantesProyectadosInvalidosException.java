package co.edu.unbosque.proyectonucleo.exception;

public class EstudiantesProyectadosInvalidosException extends RuntimeException {

	public EstudiantesProyectadosInvalidosException() {
		super("El número de estudiantes proyectados no puede ser negativo.");
	}

}