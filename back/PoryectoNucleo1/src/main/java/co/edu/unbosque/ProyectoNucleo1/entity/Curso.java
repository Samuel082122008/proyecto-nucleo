package co.edu.unbosque.ProyectoNucleo1.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "curso")
public class Curso {

	@Id
	private long id;
	
	private String nombre;
	private int sesionesPorSemana;
	private boolean requiereComputadores;
	private boolean requiereSillasMoviles; 
	
public Curso() {
}

public Curso(String nombre, int sesionesPorSemana, boolean requiereComputadores, boolean requiereSillasMoviles) {
	super();
	this.nombre = nombre;
	this.sesionesPorSemana = sesionesPorSemana;
	this.requiereComputadores = requiereComputadores;
	this.requiereSillasMoviles = requiereSillasMoviles;
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

public int getSesionesPorSemana() {
	return sesionesPorSemana;
}

public void setSesionesPorSemana(int sesionesPorSemana) {
	this.sesionesPorSemana = sesionesPorSemana;
}

public boolean isRequiereComputadores() {
	return requiereComputadores;
}

public void setRequiereComputadores(boolean requiereComputadores) {
	this.requiereComputadores = requiereComputadores;
}

public boolean isRequiereSillasMoviles() {
	return requiereSillasMoviles;
}

public void setRequiereSillasMoviles(boolean requiereSillasMoviles) {
	this.requiereSillasMoviles = requiereSillasMoviles;
}

@Override
public int hashCode() {
	return Objects.hash(id, nombre, requiereComputadores, requiereSillasMoviles, sesionesPorSemana);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Curso other = (Curso) obj;
	return id == other.id && Objects.equals(nombre, other.nombre) && requiereComputadores == other.requiereComputadores
			&& requiereSillasMoviles == other.requiereSillasMoviles && sesionesPorSemana == other.sesionesPorSemana;
}

@Override
public String toString() {
	return "Curso [id=" + id + ", nombre=" + nombre + ", sesionesPorSemana=" + sesionesPorSemana
			+ ", requiereComputadores=" + requiereComputadores + ", requiereSillasMoviles=" + requiereSillasMoviles
			+ "]";
}


}
