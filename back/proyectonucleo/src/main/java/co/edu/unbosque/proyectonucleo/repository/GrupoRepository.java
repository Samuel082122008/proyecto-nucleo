package co.edu.unbosque.proyectonucleo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectonucleo.entity.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	boolean existsByCurso_Id(Long cursoId);

}