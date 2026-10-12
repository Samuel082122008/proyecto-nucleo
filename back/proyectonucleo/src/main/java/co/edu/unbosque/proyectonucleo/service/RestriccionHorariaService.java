package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.RestriccionHorariaDTO;
import co.edu.unbosque.proyectonucleo.entity.Docente;
import co.edu.unbosque.proyectonucleo.entity.RestriccionHoraria;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.RestriccionHorariaRepository;

@Service
@Transactional
public class RestriccionHorariaService {

	@Autowired
	private RestriccionHorariaRepository restriccionRepo;

	@Autowired
	private DocenteRepository docenteRepo;

	public RestriccionHorariaDTO crear(RestriccionHorariaDTO dto) {
		Docente d = docenteRepo.findById(dto.getDocenteId()).orElseThrow(DocenteNoEncontradoException::new);
		if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
			throw new ReglaNegocioException(List.of("La hora de inicio debe ser anterior a la hora de fin."));
		}
		return aDTO(restriccionRepo
				.save(new RestriccionHoraria(d, dto.getDiaSemana(), dto.getHoraInicio(), dto.getHoraFin())));
	}

	@Transactional(readOnly = true)
	public List<RestriccionHorariaDTO> listar(Long docenteId) {
		List<RestriccionHoraria> lista = docenteId == null ? restriccionRepo.findAll()
				: restriccionRepo.findByDocente_Id(docenteId);
		return lista.stream().map(this::aDTO).toList();
	}

	public int eliminar(Long id) {
		if (!restriccionRepo.existsById(id)) throw new IllegalArgumentException("Restriccion no encontrada");
		restriccionRepo.deleteById(id);
		return 0;
	}

	private RestriccionHorariaDTO aDTO(RestriccionHoraria r) {
		RestriccionHorariaDTO dto = new RestriccionHorariaDTO();
		dto.setId(r.getId());
		dto.setDocenteId(r.getDocente().getId());
		dto.setDiaSemana(r.getDiaSemana());
		dto.setHoraInicio(r.getHoraInicio());
		dto.setHoraFin(r.getHoraFin());
		return dto;
	}
}