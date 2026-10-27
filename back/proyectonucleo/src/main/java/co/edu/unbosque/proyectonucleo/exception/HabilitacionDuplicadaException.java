package co.edu.unbosque.proyectonucleo.exception;

public class HabilitacionDuplicadaException extends RuntimeException {

	public HabilitacionDuplicadaException() {
		super("El docente ya está habilitado para este curso.");
	}

}