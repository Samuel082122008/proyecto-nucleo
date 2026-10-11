package co.edu.unbosque.proyectonucleo.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupo")
public class Grupo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_curso", nullable = false)
	private Curso curso;

	@Column(nullable = false)
	private int estudiantesProyectados;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoGrupo estado;

	public Grupo() {
	}

	public Grupo(Curso curso, int estudiantesProyectados, EstadoGrupo estado) {
		super();
		this.curso = curso;
		this.estudiantesProyectados = estudiantesProyectados;
		this.estado = estado;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public Curso getCurso() { return curso; }
	public void setCurso(Curso curso) { this.curso = curso; }

	public int getEstudiantesProyectados() { return estudiantesProyectados; }
	public void setEstudiantesProyectados(int estudiantesProyectados) { this.estudiantesProyectados = estudiantesProyectados; }

	public EstadoGrupo getEstado() { return estado; }
	public void setEstado(EstadoGrupo estado) { this.estado = estado; }

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
		Grupo other = (Grupo) obj;
		return estado == other.estado && estudiantesProyectados == other.estudiantesProyectados && id == other.id;
	}

	@Override
	public String toString() {
		return "Grupo [id=" + id + ", estudiantesProyectados=" + estudiantesProyectados + ", estado=" + estado + "]";
	}
}