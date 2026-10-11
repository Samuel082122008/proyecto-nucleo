package co.edu.unbosque.proyectonucleo.dto;

import java.util.Objects;

import co.edu.unbosque.proyectonucleo.entity.TipoVinculacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocenteDTO {
	private Long id;
	@NotBlank private String nombre;
	@NotNull private TipoVinculacion tipoVinculacion;
	private String escalafon;
	
	public DocenteDTO() {
		
	}
	
	public DocenteDTO( @NotBlank String nombre, @NotNull TipoVinculacion tipoVinculacion, String escalafon) {
		super();
		this.nombre = nombre;
		this.tipoVinculacion = tipoVinculacion;
		this.escalafon = escalafon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoVinculacion getTipoVinculacion() {
		return tipoVinculacion;
	}

	public void setTipoVinculacion(TipoVinculacion tipoVinculacion) {
		this.tipoVinculacion = tipoVinculacion;
	}

	public String getEscalafon() {
		return escalafon;
	}

	public void setEscalafon(String escalafon) {
		this.escalafon = escalafon;
	}

	@Override
	public int hashCode() {
		return Objects.hash(escalafon, id, nombre, tipoVinculacion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocenteDTO other = (DocenteDTO) obj;
		return Objects.equals(escalafon, other.escalafon) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && tipoVinculacion == other.tipoVinculacion;
	}

	@Override
	public String toString() {
		return "DocenteDTO [id=" + id + ", nombre=" + nombre + ", tipoVinculacion=" + tipoVinculacion + ", escalafon="
				+ escalafon + "]";
	}
	
	
	
}
