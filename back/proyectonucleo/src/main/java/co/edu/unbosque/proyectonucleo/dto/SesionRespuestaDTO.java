package co.edu.unbosque.proyectonucleo.dto;

import java.util.List;
import java.util.Objects;

public class SesionRespuestaDTO {
	private SesionDTO sesion;
	private List<String> advertencias;
	
	
	public SesionRespuestaDTO() {
		
	}
	
	public SesionRespuestaDTO(SesionDTO sesion, List<String> advertencias) {
		super();
		this.sesion = sesion;
		this.advertencias = advertencias;
	}

	public SesionDTO getSesion() {
		return sesion;
	}

	public void setSesion(SesionDTO sesion) {
		this.sesion = sesion;
	}

	public List<String> getAdvertencias() {
		return advertencias;
	}

	public void setAdvertencias(List<String> advertencias) {
		this.advertencias = advertencias;
	}

	@Override
	public int hashCode() {
		return Objects.hash(advertencias, sesion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SesionRespuestaDTO other = (SesionRespuestaDTO) obj;
		return Objects.equals(advertencias, other.advertencias) && Objects.equals(sesion, other.sesion);
	}

	@Override
	public String toString() {
		return "SesionRespuestaDTO [sesion=" + sesion + ", advertencias=" + advertencias + "]";
	}
	
	
}
