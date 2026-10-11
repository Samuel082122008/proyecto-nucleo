package co.edu.unbosque.proyectonucleo.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "habilitacion", uniqueConstraints = @UniqueConstraint(columnNames = {"id_docente", "id_curso"}))
public class Habilitacion {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "id_docente", nullable = false)
	private Docente docente;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "id_curso", nullable = false)
	private Curso curso;
	
	
	public Habilitacion() {
		
	}


	public Habilitacion(Docente docente, Curso curso) {
		super();
		this.docente = docente;
		this.curso = curso;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Docente getDocente() {
		return docente;
	}


	public void setDocente(Docente docente) {
		this.docente = docente;
	}


	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	@Override
	public int hashCode() {
		return Objects.hash(curso, docente, id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Habilitacion other = (Habilitacion) obj;
		return Objects.equals(curso, other.curso) && Objects.equals(docente, other.docente) && id == other.id;
	}


	@Override
	public String toString() {
		return "Habilitacion [id=" + id + ", docente=" + docente + ", curso=" + curso + "]";
	}
	
	
}


