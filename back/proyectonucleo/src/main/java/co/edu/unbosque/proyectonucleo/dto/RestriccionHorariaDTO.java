package co.edu.unbosque.proyectonucleo.dto;

import java.time.LocalTime;

import co.edu.unbosque.proyectonucleo.entity.DiaSemana;
import jakarta.validation.constraints.NotNull;

public class RestriccionHorariaDTO {
	private Long id;
	@NotNull private Long docenteId;
	@NotNull private DiaSemana diaSemana;
	@NotNull private LocalTime horaInicio;
	@NotNull private LocalTime horaFin;

	public RestriccionHorariaDTO() {
	}

	public RestriccionHorariaDTO(@NotNull Long docenteId, @NotNull DiaSemana diaSemana,
			@NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {
		super();
		this.docenteId = docenteId;
		this.diaSemana = diaSemana;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getDocenteId() { return docenteId; }
	public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

	public DiaSemana getDiaSemana() { return diaSemana; }
	public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }

	public LocalTime getHoraInicio() { return horaInicio; }
	public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

	public LocalTime getHoraFin() { return horaFin; }
	public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

	@Override
	public String toString() {
		return "RestriccionHorariaDTO [id=" + id + ", docenteId=" + docenteId + ", diaSemana=" + diaSemana
				+ ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + "]";
	}
}