package co.edu.unbosque.proyectonucleo.dto;

import java.util.Objects;

import co.edu.unbosque.proyectonucleo.entity.EstadoGrupo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GrupoDTO {
	private Long id;
	@NotNull private Long cursoId;
	@Min(0) private int estudiantesProyectados;
	private EstadoGrupo estado;
	
	public GrupoDTO() {
		
	}
	
	public GrupoDTO( @NotNull Long cursoId, @Min(0) int estudiantesProyectados, EstadoGrupo estado) {
		super();
		this.cursoId = cursoId;
		this.estudiantesProyectados = estudiantesProyectados;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCursoId() {
		return cursoId;
	}

	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}

	public int getEstudiantesProyectados() {
		return estudiantesProyectados;
	}

	public void setEstudiantesProyectados(int estudiantesProyectados) {
		this.estudiantesProyectados = estudiantesProyectados;
	}

	public EstadoGrupo getEstado() {
		return estado;
	}

	public void setEstado(EstadoGrupo estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(estado, estudiantesProyectados, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoDTO other = (GrupoDTO) obj;
		return estado == other.estado && estudiantesProyectados == other.estudiantesProyectados
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "GrupoDTO [id=" + id + ", estudiantesProyectados=" + estudiantesProyectados + ", estado=" + estado + "]";
	}


	
	
	
}
