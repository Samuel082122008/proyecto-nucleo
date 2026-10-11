package co.edu.unbosque.proyectonucleo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.ParametroSemestreDTO;
import co.edu.unbosque.proyectonucleo.entity.ParametroSemestre;
import co.edu.unbosque.proyectonucleo.exception.ParametroSemestreNoConfiguradoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.ParametroSemestreRepository;

@Service
@Transactional
public class ParametroService {

	@Autowired
	private ParametroSemestreRepository parametroRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public ParametroSemestreDTO obtener() {
		ParametroSemestre p = parametroRepo.findFirstByOrderByIdAsc()
				.orElseThrow(ParametroSemestreNoConfiguradoException::new);
		return modelMapper.map(p, ParametroSemestreDTO.class);
	}

	public ParametroSemestreDTO actualizar(ParametroSemestreDTO dto) {
		List<String> errores = new ArrayList<>();
		if (dto.getCupoMinimo() > dto.getCupoMaximo()) errores.add("El cupo minimo no puede superar al maximo.");
		if (!dto.getFranjaInicio().isBefore(dto.getFranjaFin())) errores.add("La franja de inicio debe ser anterior a la de fin.");
		if (!dto.getAlmuerzoInicio().isBefore(dto.getAlmuerzoFin())) errores.add("El inicio del almuerzo debe ser anterior a su fin.");
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);

		ParametroSemestre p = parametroRepo.findFirstByOrderByIdAsc()
				.orElseThrow(ParametroSemestreNoConfiguradoException::new);
		modelMapper.map(dto, p);
		return modelMapper.map(parametroRepo.save(p), ParametroSemestreDTO.class);
	}
}
