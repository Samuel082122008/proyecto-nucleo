package co.edu.unbosque.proyectonucleo.service;

import java.util.List;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.DocenteDTO;
import co.edu.unbosque.proyectonucleo.entity.Docente;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
import co.edu.unbosque.proyectonucleo.exception.DocenteEnUsoException;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.EscalafonInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.IdInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.NombreDocenteInvalidoException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.HabilitacionRepository;
import co.edu.unbosque.proyectonucleo.repository.RestriccionHorariaRepository;
import co.edu.unbosque.proyectonucleo.repository.SesionRepository;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;

@Service
@Transactional
public class DocenteService {

	private static final Pattern PATRON_NOMBRE = Pattern.compile("^\\p{L}[\\p{L} .'-]{2,99}$");

	@Autowired
	private DocenteRepository docenteRepo;

	@Autowired
	private SesionRepository sesionRepo;

	@Autowired
	private HabilitacionRepository habilitacionRepo;

	@Autowired
	private RestriccionHorariaRepository restriccionRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private ModelMapper modelMapper;

	public DocenteDTO crear(DocenteDTO dto) {
		validar(dto);
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
		validarId(id);
		return modelMapper.map(docenteRepo.findById(id).orElseThrow(DocenteNoEncontradoException::new), DocenteDTO.class);
	}

	public DocenteDTO actualizar(Long id, DocenteDTO dto) {
		validarId(id);
		validar(dto);
		Docente d = docenteRepo.findById(id).orElseThrow(DocenteNoEncontradoException::new);
		d.setNombre(dto.getNombre());
		d.setTipoVinculacion(dto.getTipoVinculacion());
		d.setEscalafon(dto.getEscalafon());
		return modelMapper.map(d, DocenteDTO.class);
	}

	public int eliminar(Long id) {
		validarId(id);
		if (!docenteRepo.existsById(id)) throw new DocenteNoEncontradoException();
		if (sesionRepo.existsByDocente_Id(id) || habilitacionRepo.existsByDocente_Id(id)
				|| restriccionRepo.existsByDocente_Id(id) || usuarioRepo.existsByDocenteId(id)) {
			throw new DocenteEnUsoException();
		}
		docenteRepo.deleteById(id);
		return 0;
	}

	private void validarId(Long id) {
		if (id == null || id <= 0) throw new IdInvalidoException();
	}

	private void validar(DocenteDTO dto) {
		if (dto == null) throw new DatosObligatoriosException();
		if (dto.getNombre() == null || !PATRON_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
			throw new NombreDocenteInvalidoException();
		}
		if (dto.getTipoVinculacion() == null) throw new DatosObligatoriosException("tipoVinculacion");
		if (dto.getEscalafon() != null && dto.getEscalafon().length() > 50) throw new EscalafonInvalidoException();
	}
}