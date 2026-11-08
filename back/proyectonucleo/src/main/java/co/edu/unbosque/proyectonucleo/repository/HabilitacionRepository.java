package co.edu.unbosque.proyectonucleo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectonucleo.entity.Habilitacion;

public interface HabilitacionRepository extends JpaRepository<Habilitacion, Long> {

	boolean existsByDocente_IdAndCurso_Id(Long docenteId, Long cursoId);

	boolean existsByDocente_Id(Long docenteId);

	boolean existsByCurso_Id(Long cursoId);
}