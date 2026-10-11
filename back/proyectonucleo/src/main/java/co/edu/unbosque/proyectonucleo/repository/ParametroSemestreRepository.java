package co.edu.unbosque.proyectonucleo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectonucleo.entity.ParametroSemestre;

public interface ParametroSemestreRepository extends JpaRepository<ParametroSemestre, Long> {
	Optional<ParametroSemestre> findFirstByOrderByIdAsc();
}

