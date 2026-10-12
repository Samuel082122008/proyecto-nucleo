package co.edu.unbosque.proyectonucleo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.AulaDTO;
import co.edu.unbosque.proyectonucleo.entity.Aula;
import co.edu.unbosque.proyectonucleo.repository.AulaRepository;

@Service
@Transactional
public class AulaService {

	@Autowired
	private AulaRepository aulaRepo;

	@Transactional(readOnly = true)
	public List<AulaDTO> listar() {
		return aulaRepo.findAll().stream().map(this::aDTO).toList();
	}

	@Transactional(readOnly = true)
	public AulaDTO buscar(Long id) {
		Aula aula = aulaRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con ID: " + id));
		return aDTO(aula);
	}

	public AulaDTO crear(AulaDTO dto) {
		Aula aula = new Aula();
		aula.setNombre(dto.getNombre());
		aula.setCapacidad(dto.getCapacidad());
		aula.setTieneComputadores(dto.isTieneComputadores());
		aula.setSillasMoviles(dto.isSillasMoviles());

		return aDTO(aulaRepo.save(aula));
	}

	public AulaDTO actualizar(Long id, AulaDTO dto) {
		Aula aula = aulaRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con ID: " + id));

		aula.setNombre(dto.getNombre());
		aula.setCapacidad(dto.getCapacidad());
		aula.setTieneComputadores(dto.isTieneComputadores());
		aula.setSillasMoviles(dto.isSillasMoviles());

		return aDTO(aula);
	}

	public void eliminar(Long id) {
		if (!aulaRepo.existsById(id)) {
			throw new IllegalArgumentException("Aula no encontrada con ID: " + id);
		}
		aulaRepo.deleteById(id);
	}

	private AulaDTO aDTO(Aula aula) {
		AulaDTO dto = new AulaDTO();
		dto.setId(aula.getId());
		dto.setNombre(aula.getNombre());
		dto.setCapacidad(aula.getCapacidad());
		dto.setTieneComputadores(aula.isTieneComputadores());
		dto.setSillasMoviles(aula.isSillasMoviles());
		return dto;
	}
}