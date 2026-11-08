package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.RestriccionHorariaDTO;
import co.edu.unbosque.proyectonucleo.entity.Docente;
import co.edu.unbosque.proyectonucleo.entity.RestriccionHoraria;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.IdInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.RangoHorarioInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.RestriccionNoEncontradaException;
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
		if (dto == null) throw new DatosObligatoriosException();
		validarId(dto.getDocenteId());
		if (dto.getDiaSemana() == null) throw new DatosObligatoriosException("diaSemana");
		if (dto.getHoraInicio() == null) throw new DatosObligatoriosException("horaInicio");
		if (dto.getHoraFin() == null) throw new DatosObligatoriosException("horaFin");
		if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) throw new RangoHorarioInvalidoException();

		Docente d = docenteRepo.findById(dto.getDocenteId()).orElseThrow(DocenteNoEncontradoException::new);
		return aDTO(restriccionRepo
				.save(new RestriccionHoraria(d, dto.getDiaSemana(), dto.getHoraInicio(), dto.getHoraFin())));
	}

	@Transactional(readOnly = true)
	public List<RestriccionHorariaDTO> listar(Long docenteId) {
		if (docenteId != null) validarId(docenteId);
		List<RestriccionHoraria> lista = docenteId == null ? restriccionRepo.findAll()
				: restriccionRepo.findByDocente_Id(docenteId);
		return lista.stream().map(this::aDTO).toList();
	}

	public int eliminar(Long id) {
		validarId(id);
		if (!restriccionRepo.existsById(id)) throw new RestriccionNoEncontradaException();
		restriccionRepo.deleteById(id);
		return 0;
	}

	private void validarId(Long id) {
		if (id == null || id <= 0) throw new IdInvalidoException();
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