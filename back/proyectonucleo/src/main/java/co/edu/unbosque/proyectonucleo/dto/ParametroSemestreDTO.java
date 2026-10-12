package co.edu.unbosque.proyectonucleo.dto;

import java.time.LocalTime;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ParametroSemestreDTO {
	@Min(1) private int cupoMinimo;
	@Min(1) private int cupoMaximo;
	@Min(0) private int toleranciaCupo;
	@NotNull private LocalTime franjaInicio;
	@NotNull private LocalTime franjaFin;
	@NotNull private LocalTime sabadoFin;
	@NotNull private LocalTime almuerzoInicio;
	@NotNull private LocalTime almuerzoFin;
	@Min(1) private int sesionesMaxSemana;
	@Min(1) private int duracionSesionHoras;
	
	public ParametroSemestreDTO() {
		
	}
	
	public ParametroSemestreDTO(@Min(1) int cupoMinimo, @Min(1) int cupoMaximo, @Min(0) int toleranciaCupo,
			@NotNull LocalTime franjaInicio, @NotNull LocalTime franjaFin, @NotNull LocalTime sabadoFin,
			@NotNull LocalTime almuerzoInicio, @NotNull LocalTime almuerzoFin, @Min(1) int sesionesMaxSemana,
			@Min(1) int duracionSesionHoras) {
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
				franjaInicio, sabadoFin, sesionesMaxSemana, toleranciaCupo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroSemestreDTO other = (ParametroSemestreDTO) obj;
		return Objects.equals(almuerzoFin, other.almuerzoFin) && Objects.equals(almuerzoInicio, other.almuerzoInicio)
				&& cupoMaximo == other.cupoMaximo && cupoMinimo == other.cupoMinimo
				&& duracionSesionHoras == other.duracionSesionHoras && Objects.equals(franjaFin, other.franjaFin)
				&& Objects.equals(franjaInicio, other.franjaInicio) && Objects.equals(sabadoFin, other.sabadoFin)
				&& sesionesMaxSemana == other.sesionesMaxSemana && toleranciaCupo == other.toleranciaCupo;
	}

	@Override
	public String toString() {
		return "ParametroSemestreDTO [cupoMinimo=" + cupoMinimo + ", cupoMaximo=" + cupoMaximo + ", toleranciaCupo="
				+ toleranciaCupo + ", franjaInicio=" + franjaInicio + ", franjaFin=" + franjaFin + ", sabadoFin="
				+ sabadoFin + ", almuerzoInicio=" + almuerzoInicio + ", almuerzoFin=" + almuerzoFin
				+ ", sesionesMaxSemana=" + sesionesMaxSemana + ", duracionSesionHoras=" + duracionSesionHoras + "]";
	}
	
	
	
}
