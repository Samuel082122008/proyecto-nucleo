package co.edu.unbosque.proyectonucleo.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unbosque.proyectonucleo.entity.DiaSemana;
import co.edu.unbosque.proyectonucleo.entity.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

	List<Sesion> findByGrupo_Id(Long grupoId);

	long countByGrupo_IdAndIdNot(Long grupoId, Long id);

	@Query("""
		select count(s) > 0 from Sesion s
		where s.docente.id = :docenteId and s.diaSemana = :dia
		  and s.horaInicio < :fin and s.horaFin > :inicio and s.id <> :excluir
		""")
	boolean existeChoqueDocente(@Param("docenteId") Long docenteId, @Param("dia") DiaSemana dia,
			@Param("inicio") LocalTime inicio, @Param("fin") LocalTime fin, @Param("excluir") Long excluir);

	@Query("""
		select count(s) > 0 from Sesion s
		where s.aula.id = :aulaId and s.diaSemana = :dia
		  and s.horaInicio < :fin and s.horaFin > :inicio and s.id <> :excluir
		""")
	boolean existeChoqueAula(@Param("aulaId") Long aulaId, @Param("dia") DiaSemana dia,
			@Param("inicio") LocalTime inicio, @Param("fin") LocalTime fin, @Param("excluir") Long excluir);

	@Query("""
		select count(s) > 0 from Sesion s
		where s.grupo.id = :grupoId and s.diaSemana = :dia
		  and s.horaInicio < :fin and s.horaFin > :inicio and s.id <> :excluir
		""")
	boolean existeChoqueGrupo(@Param("grupoId") Long grupoId, @Param("dia") DiaSemana dia,
			@Param("inicio") LocalTime inicio, @Param("fin") LocalTime fin, @Param("excluir") Long excluir);

	Optional<Sesion> findByDocente_Id(Long docenteId);
}
