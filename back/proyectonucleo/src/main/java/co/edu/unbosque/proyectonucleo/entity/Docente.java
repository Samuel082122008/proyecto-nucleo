package co.edu.unbosque.proyectonucleo.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "docente")
public class Docente {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false) private String nombre;
	@Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
	private TipoVinculacion tipoVinculacion;
	private String escalafon;
	
	public Docente() {
		
	}

	

	public Docente(String nombre, TipoVinculacion tipoVinculacion, String escalafon) {
		super();
		this.nombre = nombre;
		this.tipoVinculacion = tipoVinculacion;
		this.escalafon = escalafon;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
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
		Docente other = (Docente) obj;
		return Objects.equals(escalafon, other.escalafon) && id == other.id && Objects.equals(nombre, other.nombre)
				&& tipoVinculacion == other.tipoVinculacion;
	}

	@Override
	public String toString() {
		return "Docente [id=" + id + ", nombre=" + nombre + ", tipoVinculacion=" + tipoVinculacion + ", escalafon="
				+ escalafon + "]";
	}
	
	
	
}