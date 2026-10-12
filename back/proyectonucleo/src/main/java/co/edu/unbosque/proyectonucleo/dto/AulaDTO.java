package co.edu.unbosque.proyectonucleo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AulaDTO {
	private Long id;
	@NotBlank private String nombre;
	@Min(1) private int capacidad;
	private boolean tieneComputadores;
	private boolean sillasMoviles;

	public AulaDTO() {
	}

	public AulaDTO(@NotBlank String nombre, @Min(1) int capacidad, boolean tieneComputadores, boolean sillasMoviles) {
		super();
		this.nombre = nombre;
		this.capacidad = capacidad;
		this.tieneComputadores = tieneComputadores;
		this.sillasMoviles = sillasMoviles;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public int getCapacidad() { return capacidad; }
	public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

	public boolean isTieneComputadores() { return tieneComputadores; }
	public void setTieneComputadores(boolean tieneComputadores) { this.tieneComputadores = tieneComputadores; }

	public boolean isSillasMoviles() { return sillasMoviles; }
	public void setSillasMoviles(boolean sillasMoviles) { this.sillasMoviles = sillasMoviles; }

	@Override
	public String toString() {
		return "AulaDTO [id=" + id + ", nombre=" + nombre + ", capacidad=" + capacidad + ", tieneComputadores="
				+ tieneComputadores + ", sillasMoviles=" + sillasMoviles + "]";
	}
}