package co.edu.unbosque.proyectonucleo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.AulaDTO;
import co.edu.unbosque.proyectonucleo.entity.Aula;
import co.edu.unbosque.proyectonucleo.exception.AulaNoEncontradaException;
import co.edu.unbosque.proyectonucleo.repository.AulaRepository;

@Service
@Transactional
public class AulaService {

	@Autowired
	private AulaRepository aulaRepository;

	@Transactional(readOnly = true)
	public List<AulaDTO> listar() {
		return aulaRepository.findAll()
				.stream()
				.map(this::convertirADTO)
				.toList();
	}

	@Transactional(readOnly = true)
	public AulaDTO buscar(Long id) {
		Aula aula = aulaRepository.findById(id)
				.orElseThrow(AulaNoEncontradaException::new);
		return convertirADTO(aula);
	}

	public AulaDTO crear(AulaDTO dto) {
		Aula nuevaAula = mapiarEntidad(new Aula(), dto);
		Aula guardada = aulaRepository.save(nuevaAula);
		return convertirADTO(guardada);
	}

	public AulaDTO actualizar(Long id, AulaDTO dto) {
		Aula aulaExistente = aulaRepository.findById(id)
				.orElseThrow(AulaNoEncontradaException::new);
		
		mapiarEntidad(aulaExistente, dto);
		Aula actualizada = aulaRepository.save(aulaExistente);
		return convertirADTO(actualizada);
	}

	public void eliminar(Long id) {
		if (!aulaRepository.existsById(id)) {
			throw new AulaNoEncontradaException();
		}
		aulaRepository.deleteById(id);
	}

	// Métodos auxiliares de mapeo (Evitan problemas con tipos primitivos/Objetos)
	private AulaDTO convertirADTO(Aula entidad) {
		AulaDTO dto = new AulaDTO();
		dto.setId(entidad.getId());
		dto.setNombre(entidad.getNombre());
		dto.setCapacidad(entidad.getCapacidad());
		dto.setTieneComputadores(entidad.isTieneComputadores());
		dto.setSillasMoviles(entidad.isSillasMoviles());
		return dto;
	}

	private Aula mapiarEntidad(Aula entidad, AulaDTO dto) {
		entidad.setNombre(dto.getNombre());
		entidad.setCapacidad(dto.getCapacidad());
		entidad.setTieneComputadores(dto.isTieneComputadores());
		entidad.setSillasMoviles(dto.isSillasMoviles());
		return entidad;
	}
}
