package co.edu.unbosque.proyectonucleo.exception;

public class DocenteRequeridoException extends RuntimeException {

	public DocenteRequeridoException() {
		super("Un usuario con rol DOCENTE debe estar asociado a un docente.");
	}

}