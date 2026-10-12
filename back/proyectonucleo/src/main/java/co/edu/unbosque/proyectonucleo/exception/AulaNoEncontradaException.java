package co.edu.unbosque.proyectonucleo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El aula solicitada no existe")
public class AulaNoEncontradaException extends RuntimeException {
    
    public AulaNoEncontradaException() {
        super("El aula no fue encontrada.");
    }
    
    public AulaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
