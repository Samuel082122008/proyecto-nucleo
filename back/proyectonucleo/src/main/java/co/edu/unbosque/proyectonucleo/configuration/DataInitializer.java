package co.edu.unbosque.proyectonucleo.configuration;

import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.proyectonucleo.entity.ParametroSemestre;
import co.edu.unbosque.proyectonucleo.repository.ParametroSemestreRepository;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner cargarParametros(ParametroSemestreRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				ParametroSemestre p = new ParametroSemestre();
				p.setCupoMinimo(10);
				p.setCupoMaximo(40);
				p.setToleranciaCupo(10);
				p.setFranjaInicio(LocalTime.of(7, 0));
				p.setFranjaFin(LocalTime.of(22, 0));
				p.setSabadoFin(LocalTime.of(13, 0));
				p.setAlmuerzoInicio(LocalTime.of(12, 0));
				p.setAlmuerzoFin(LocalTime.of(13, 0));
				p.setSesionesMaxSemana(4);
				p.setDuracionSesionHoras(2);
				repo.save(p);
			}
		};
	}
}