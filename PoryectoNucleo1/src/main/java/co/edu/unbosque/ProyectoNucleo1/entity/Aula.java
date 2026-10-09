package co.edu.unbosque.ProyectoNucleo1.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aula")
public class Aula {
	@Id
	private long id;
	
	private String nombre;
    private int capacidad;
    private boolean tieneComputadores;
    private boolean sillasMoviles;
public Aula() {
	}

public Aula(String nombre, int capacidad, boolean tieneComputadores, boolean sillasMoviles) {
	super();
	this.nombre = nombre;
	this.capacidad = capacidad;
	this.tieneComputadores = tieneComputadores;
	this.sillasMoviles = sillasMoviles;
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
public int getCapacidad() {
	return capacidad;
}
public void setCapacidad(int capacidad) {
	this.capacidad = capacidad;
}
public boolean isTieneComputadores() {
	return tieneComputadores;
}
public void setTieneComputadores(boolean tieneComputadores) {
	this.tieneComputadores = tieneComputadores;
}
public boolean isSillasMoviles() {
	return sillasMoviles;
}
public void setSillasMoviles(boolean sillasMoviles) {
	this.sillasMoviles = sillasMoviles;
}

@Override
public String toString() {
	return "Aula [id=" + id + ", nombre=" + nombre + ", capacidad=" + capacidad + ", tieneComputadores="
			+ tieneComputadores + ", sillasMoviles=" + sillasMoviles + "]";
}

@Override
public int hashCode() {
	return Objects.hash(capacidad, id, nombre, sillasMoviles, tieneComputadores);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Aula other = (Aula) obj;
	return capacidad == other.capacidad && id == other.id && Objects.equals(nombre, other.nombre)
			&& sillasMoviles == other.sillasMoviles && tieneComputadores == other.tieneComputadores;
}


}
