package co.edu.unbosque.proyectonucleo.exception;

public class CapacidadInvalidaException extends RuntimeException {

	public CapacidadInvalidaException() {
		super("La capacidad del aula debe ser un número entre 1 y 1000.");
	}

}