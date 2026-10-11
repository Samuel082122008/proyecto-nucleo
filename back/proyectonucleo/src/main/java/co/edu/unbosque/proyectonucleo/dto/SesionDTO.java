package co.edu.unbosque.proyectonucleo.dto;

import java.time.LocalTime;
import java.util.Objects;

import co.edu.unbosque.proyectonucleo.entity.DiaSemana;
import jakarta.validation.constraints.NotNull;

public class SesionDTO {
	private Long id;
	@NotNull private Long grupoId;
	@NotNull private Long docenteId;
	@NotNull private Long aulaId;
	@NotNull private DiaSemana diaSemana;
	@NotNull private LocalTime horaInicio;
	@NotNull private LocalTime horaFin;
	
	public SesionDTO() {
		
	}

	public SesionDTO(@NotNull Long grupoId, @NotNull Long docenteId, @NotNull Long aulaId,
			@NotNull DiaSemana diaSemana, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {
		super();
		this.grupoId = grupoId;
		this.docenteId = docenteId;
		this.aulaId = aulaId;
		this.diaSemana = diaSemana;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getDocenteId() {
		return docenteId;
	}

	public void setDocenteId(Long docenteId) {
		this.docenteId = docenteId;
	}

	public Long getAulaId() {
		return aulaId;
	}

	public void setAulaId(Long aulaId) {
		this.aulaId = aulaId;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(diaSemana, horaFin, horaInicio, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SesionDTO other = (SesionDTO) obj;
		return diaSemana == other.diaSemana && Objects.equals(horaFin, other.horaFin)
				&& Objects.equals(horaInicio, other.horaInicio) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "SesionDTO [id=" + id + ", diaSemana=" + diaSemana + ", horaInicio=" + horaInicio + ", horaFin="
				+ horaFin + "]";
	}

	
	
	
}

