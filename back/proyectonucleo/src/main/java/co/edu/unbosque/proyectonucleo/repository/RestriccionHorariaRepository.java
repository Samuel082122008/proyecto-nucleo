package co.edu.unbosque.proyectonucleo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.proyectonucleo.entity.RestriccionHoraria;

public interface RestriccionHorariaRepository extends JpaRepository<RestriccionHoraria, Long> {

	List<RestriccionHoraria> findByDocente_Id(Long docenteId);

	boolean existsByDocente_Id(Long docenteId);
}