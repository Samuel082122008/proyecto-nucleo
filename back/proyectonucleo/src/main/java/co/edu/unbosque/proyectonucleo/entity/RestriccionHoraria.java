package co.edu.unbosque.proyectonucleo.entity;

import java.time.LocalTime;
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

@Entity @Table(name = "restriccion_horaria")
public class RestriccionHoraria {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "id_docente", nullable = false)
	private Docente docente;
	@Enumerated(EnumType.STRING) @Column(nullable = false, length = 15)
	private DiaSemana diaSemana;
	@Column(nullable = false) private LocalTime horaInicio;
	@Column(nullable = false) private LocalTime horaFin;
	
	public RestriccionHoraria(){
		
	}

	public RestriccionHoraria(Docente docente, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) {
		super();
		this.docente = docente;
		this.diaSemana = diaSemana;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
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
		return Objects.hash(diaSemana, docente, horaFin, horaInicio, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestriccionHoraria other = (RestriccionHoraria) obj;
		return diaSemana == other.diaSemana && Objects.equals(docente, other.docente)
				&& Objects.equals(horaFin, other.horaFin) && Objects.equals(horaInicio, other.horaInicio)
				&& id == other.id;
	}

	@Override
	public String toString() {
		return "RestriccionHoraria [id=" + id + ", docente=" + docente + ", diaSemana=" + diaSemana + ", horaInicio="
				+ horaInicio + ", horaFin=" + horaFin + "]";
	}
	
	
}
