package co.edu.unbosque.proyectonucleo.exception;

public class DatosObligatoriosException extends RuntimeException {

	public DatosObligatoriosException() {
		super("Los datos de la solicitud son obligatorios y no pueden venir vacíos.");
	}

	public DatosObligatoriosException(String campo) {
		super("El campo '" + campo + "' es obligatorio.");
	}

}