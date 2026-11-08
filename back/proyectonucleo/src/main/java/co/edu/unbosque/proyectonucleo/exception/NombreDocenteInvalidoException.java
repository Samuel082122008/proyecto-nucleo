package co.edu.unbosque.proyectonucleo.exception;

public class NombreDocenteInvalidoException extends RuntimeException {

	public NombreDocenteInvalidoException() {
		super("El nombre del docente no es válido. Debe tener entre 3 y 100 caracteres y solo puede contener letras (con o sin acentos), espacios, puntos, apóstrofes y guiones.");
	}

}