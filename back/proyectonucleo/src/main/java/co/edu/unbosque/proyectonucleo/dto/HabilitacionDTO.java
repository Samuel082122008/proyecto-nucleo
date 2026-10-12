package co.edu.unbosque.proyectonucleo.dto;

import jakarta.validation.constraints.NotNull;

public class HabilitacionDTO {
	private Long id;
	@NotNull private Long docenteId;
	@NotNull private Long cursoId;

	public HabilitacionDTO() {
	}

	public HabilitacionDTO(@NotNull Long docenteId, @NotNull Long cursoId) {
		super();
		this.docenteId = docenteId;
		this.cursoId = cursoId;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getDocenteId() { return docenteId; }
	public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

	public Long getCursoId() { return cursoId; }
	public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

	@Override
	public String toString() {
		return "HabilitacionDTO [id=" + id + ", docenteId=" + docenteId + ", cursoId=" + cursoId + "]";
	}
}