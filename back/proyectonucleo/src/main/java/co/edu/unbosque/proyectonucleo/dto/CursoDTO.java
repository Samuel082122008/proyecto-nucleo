package co.edu.unbosque.proyectonucleo.dto;

import jakarta.validation.constraints.NotBlank;

public class CursoDTO {
	private Long id;
	@NotBlank private String nombre;
	// El rango (1 a sesionesMaxSemana) se valida contra ParametroSemestre en CursoService
	private int sesionesPorSemana;
	private boolean requiereComputadores;
	private boolean requiereSillasMoviles;

	public CursoDTO() {
	}

	public CursoDTO(@NotBlank String nombre, int sesionesPorSemana, boolean requiereComputadores,
			boolean requiereSillasMoviles) {
		super();
		this.nombre = nombre;
		this.sesionesPorSemana = sesionesPorSemana;
		this.requiereComputadores = requiereComputadores;
		this.requiereSillasMoviles = requiereSillasMoviles;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public int getSesionesPorSemana() { return sesionesPorSemana; }
	public void setSesionesPorSemana(int sesionesPorSemana) { this.sesionesPorSemana = sesionesPorSemana; }

	public boolean isRequiereComputadores() { return requiereComputadores; }
	public void setRequiereComputadores(boolean requiereComputadores) { this.requiereComputadores = requiereComputadores; }

	public boolean isRequiereSillasMoviles() { return requiereSillasMoviles; }
	public void setRequiereSillasMoviles(boolean requiereSillasMoviles) { this.requiereSillasMoviles = requiereSillasMoviles; }

	@Override
	public String toString() {
		return "CursoDTO [id=" + id + ", nombre=" + nombre + ", sesionesPorSemana=" + sesionesPorSemana
				+ ", requiereComputadores=" + requiereComputadores + ", requiereSillasMoviles="
				+ requiereSillasMoviles + "]";
	}
}