package co.edu.unbosque.proyectonucleo.dto;

import java.util.List;
import java.util.Objects;

public class ValidacionDTO {
	private boolean valido;
	private List<String> errores;
	private List<String> advertencias;
	
	public ValidacionDTO() {
		
	}
	
	public ValidacionDTO(boolean valido, List<String> errores, List<String> advertencias) {
		super();
		this.valido = valido;
		this.errores = errores;
		this.advertencias = advertencias;
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public List<String> getErrores() {
		return errores;
	}

	public void setErrores(List<String> errores) {
		this.errores = errores;
	}

	public List<String> getAdvertencias() {
		return advertencias;
	}

	public void setAdvertencias(List<String> advertencias) {
		this.advertencias = advertencias;
	}

	@Override
	public int hashCode() {
		return Objects.hash(advertencias, errores, valido);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidacionDTO other = (ValidacionDTO) obj;
		return Objects.equals(advertencias, other.advertencias) && Objects.equals(errores, other.errores)
				&& valido == other.valido;
	}

	@Override
	public String toString() {
		return "ValidacionDTO [valido=" + valido + ", errores=" + errores + ", advertencias=" + advertencias + "]";
	}
	
	
	
	
}
