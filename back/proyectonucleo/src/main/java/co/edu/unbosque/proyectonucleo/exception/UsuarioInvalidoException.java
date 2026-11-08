package co.edu.unbosque.proyectonucleo.exception;

public class UsuarioInvalidoException extends RuntimeException {

	public UsuarioInvalidoException() {
		super("El nombre de usuario no es válido. Debe tener entre 4 y 50 caracteres y solo puede contener letras, números, punto, guion, guion bajo y arroba, sin espacios.");
	}

}