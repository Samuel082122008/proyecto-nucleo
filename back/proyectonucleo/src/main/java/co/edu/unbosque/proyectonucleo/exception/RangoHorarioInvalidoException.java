package co.edu.unbosque.proyectonucleo.exception;

public class RangoHorarioInvalidoException extends RuntimeException {

	public RangoHorarioInvalidoException() {
		super("La hora de inicio debe ser anterior a la hora de fin.");
	}

}