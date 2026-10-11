package co.edu.unbosque.proyectonucleo.entity;

import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "parametro_semestre")
public class ParametroSemestre {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int cupoMinimo;
	private int cupoMaximo;
	private int toleranciaCupo;       // en porcentaje (10 = 10 %)
	private LocalTime franjaInicio;
	private LocalTime franjaFin;      // lunes a viernes
	private LocalTime sabadoFin;
	private LocalTime almuerzoInicio;
	private LocalTime almuerzoFin;
	private int sesionesMaxSemana;
	private int duracionSesionHoras;
	
	public ParametroSemestre() {
		
	}

	public ParametroSemestre(int cupoMinimo, int cupoMaximo, int toleranciaCupo, LocalTime franjaInicio,
			LocalTime franjaFin, LocalTime sabadoFin, LocalTime almuerzoInicio, LocalTime almuerzoFin,
			int sesionesMaxSemana, int duracionSesionHoras) {
		super();
		this.cupoMinimo = cupoMinimo;
		this.cupoMaximo = cupoMaximo;
		this.toleranciaCupo = toleranciaCupo;
		this.franjaInicio = franjaInicio;
		this.franjaFin = franjaFin;
		this.sabadoFin = sabadoFin;
		this.almuerzoInicio = almuerzoInicio;
		this.almuerzoFin = almuerzoFin;
		this.sesionesMaxSemana = sesionesMaxSemana;
		this.duracionSesionHoras = duracionSesionHoras;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCupoMinimo() {
		return cupoMinimo;
	}

	public void setCupoMinimo(int cupoMinimo) {
		this.cupoMinimo = cupoMinimo;
	}

	public int getCupoMaximo() {
		return cupoMaximo;
	}

	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}

	public int getToleranciaCupo() {
		return toleranciaCupo;
	}

	public void setToleranciaCupo(int toleranciaCupo) {
		this.toleranciaCupo = toleranciaCupo;
	}

	public LocalTime getFranjaInicio() {
		return franjaInicio;
	}

	public void setFranjaInicio(LocalTime franjaInicio) {
		this.franjaInicio = franjaInicio;
	}

	public LocalTime getFranjaFin() {
		return franjaFin;
	}

	public void setFranjaFin(LocalTime franjaFin) {
		this.franjaFin = franjaFin;
	}

	public LocalTime getSabadoFin() {
		return sabadoFin;
	}

	public void setSabadoFin(LocalTime sabadoFin) {
		this.sabadoFin = sabadoFin;
	}

	public LocalTime getAlmuerzoInicio() {
		return almuerzoInicio;
	}

	public void setAlmuerzoInicio(LocalTime almuerzoInicio) {
		this.almuerzoInicio = almuerzoInicio;
	}

	public LocalTime getAlmuerzoFin() {
		return almuerzoFin;
	}

	public void setAlmuerzoFin(LocalTime almuerzoFin) {
		this.almuerzoFin = almuerzoFin;
	}

	public int getSesionesMaxSemana() {
		return sesionesMaxSemana;
	}

	public void setSesionesMaxSemana(int sesionesMaxSemana) {
		this.sesionesMaxSemana = sesionesMaxSemana;
	}

	public int getDuracionSesionHoras() {
		return duracionSesionHoras;
	}

	public void setDuracionSesionHoras(int duracionSesionHoras) {
		this.duracionSesionHoras = duracionSesionHoras;
	}

	@Override
	public int hashCode() {
		return Objects.hash(almuerzoFin, almuerzoInicio, cupoMaximo, cupoMinimo, duracionSesionHoras, franjaFin,
				franjaInicio, id, sabadoFin, sesionesMaxSemana, toleranciaCupo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroSemestre other = (ParametroSemestre) obj;
		return Objects.equals(almuerzoFin, other.almuerzoFin) && Objects.equals(almuerzoInicio, other.almuerzoInicio)
				&& cupoMaximo == other.cupoMaximo && cupoMinimo == other.cupoMinimo
				&& duracionSesionHoras == other.duracionSesionHoras && Objects.equals(franjaFin, other.franjaFin)
				&& Objects.equals(franjaInicio, other.franjaInicio) && id == other.id
				&& Objects.equals(sabadoFin, other.sabadoFin) && sesionesMaxSemana == other.sesionesMaxSemana
				&& toleranciaCupo == other.toleranciaCupo;
	}

	@Override
	public String toString() {
		return "ParametroSemestre [id=" + id + ", cupoMinimo=" + cupoMinimo + ", cupoMaximo=" + cupoMaximo
				+ ", toleranciaCupo=" + toleranciaCupo + ", franjaInicio=" + franjaInicio + ", franjaFin=" + franjaFin
				+ ", sabadoFin=" + sabadoFin + ", almuerzoInicio=" + almuerzoInicio + ", almuerzoFin=" + almuerzoFin
				+ ", sesionesMaxSemana=" + sesionesMaxSemana + ", duracionSesionHoras=" + duracionSesionHoras + "]";
	}
	
	
	
}