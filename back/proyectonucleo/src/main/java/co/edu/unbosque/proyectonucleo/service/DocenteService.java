package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.DocenteDTO;
import co.edu.unbosque.proyectonucleo.entity.Docente;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;

@Service
@Transactional
public class DocenteService {

	@Autowired
	private DocenteRepository docenteRepo;

	@Autowired
	private ModelMapper modelMapper;

	public DocenteDTO crear(DocenteDTO dto) {
		Docente d = modelMapper.map(dto, Docente.class);
		d.setId(0);
		return modelMapper.map(docenteRepo.save(d), DocenteDTO.class);
	}

	@Transactional(readOnly = true)
	public List<DocenteDTO> listar() {
		return docenteRepo.findAll().stream().map(d -> modelMapper.map(d, DocenteDTO.class)).toList();
	}

	@Transactional(readOnly = true)
	public DocenteDTO buscar(Long id) {
		return modelMapper.map(docenteRepo.findById(id).orElseThrow(DocenteNoEncontradoException::new), DocenteDTO.class);
	}

	public DocenteDTO actualizar(Long id, DocenteDTO dto) {
		Docente d = docenteRepo.findById(id).orElseThrow(DocenteNoEncontradoException::new);
		d.setNombre(dto.getNombre());
		d.setTipoVinculacion(dto.getTipoVinculacion());
		d.setEscalafon(dto.getEscalafon());
		return modelMapper.map(d, DocenteDTO.class);
	}

	public int eliminar(Long id) {
		if (!docenteRepo.existsById(id)) throw new DocenteNoEncontradoException();
		docenteRepo.deleteById(id);
		return 0;
	}
}
