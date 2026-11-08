package co.edu.unbosque.proyectonucleo.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.ParametroSemestreDTO;
import co.edu.unbosque.proyectonucleo.entity.ParametroSemestre;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
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
		validar(dto);

		ParametroSemestre p = parametroRepo.findFirstByOrderByIdAsc()
				.orElseThrow(ParametroSemestreNoConfiguradoException::new);
		modelMapper.map(dto, p);
		return modelMapper.map(parametroRepo.save(p), ParametroSemestreDTO.class);
	}

	private void validar(ParametroSemestreDTO dto) {
		if (dto == null) throw new DatosObligatoriosException();
		if (dto.getFranjaInicio() == null) throw new DatosObligatoriosException("franjaInicio");
		if (dto.getFranjaFin() == null) throw new DatosObligatoriosException("franjaFin");
		if (dto.getSabadoFin() == null) throw new DatosObligatoriosException("sabadoFin");
		if (dto.getAlmuerzoInicio() == null) throw new DatosObligatoriosException("almuerzoInicio");
		if (dto.getAlmuerzoFin() == null) throw new DatosObligatoriosException("almuerzoFin");

		List<String> errores = new ArrayList<>();
		if (dto.getCupoMinimo() < 1) errores.add("El cupo minimo debe ser al menos 1.");
		if (dto.getCupoMaximo() < 1) errores.add("El cupo maximo debe ser al menos 1.");
		if (dto.getCupoMinimo() > dto.getCupoMaximo()) errores.add("El cupo minimo no puede superar al maximo.");
		if (dto.getToleranciaCupo() < 0 || dto.getToleranciaCupo() > 100) errores.add("La tolerancia del cupo debe estar entre 0 y 100 %.");
		if (dto.getSesionesMaxSemana() < 1 || dto.getSesionesMaxSemana() > 6) errores.add("Las sesiones maximas por semana deben estar entre 1 y 6.");
		if (dto.getDuracionSesionHoras() < 1) errores.add("La duracion de la sesion debe ser de al menos 1 hora.");
		if (!dto.getFranjaInicio().isBefore(dto.getFranjaFin())) {
			errores.add("La franja de inicio debe ser anterior a la de fin.");
		} else if (Duration.between(dto.getFranjaInicio(), dto.getFranjaFin()).toMinutes() < dto.getDuracionSesionHoras() * 60L) {
			errores.add("La duracion de la sesion no cabe dentro de la franja diaria.");
		}
		if (!dto.getAlmuerzoInicio().isBefore(dto.getAlmuerzoFin())) errores.add("El inicio del almuerzo debe ser anterior a su fin.");
		if (!dto.getSabadoFin().isAfter(dto.getFranjaInicio())) errores.add("El fin de la franja del sabado debe ser posterior al inicio de la franja diaria.");
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);
	}
}