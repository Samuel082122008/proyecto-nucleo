package co.edu.unbosque.proyectonucleo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.UsuarioDTO;
import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import co.edu.unbosque.proyectonucleo.entity.Usuario;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private DocenteRepository docenteRepo;

	@Transactional(readOnly = true)
	public List<UsuarioDTO> listar() {
		return usuarioRepo.findAll().stream().map(this::aDTO).toList();
	}

	public UsuarioDTO crear(UsuarioDTO dto) {
		List<String> errores = new ArrayList<>();
		if (dto.getPassword() == null || dto.getPassword().length() < 8) {
			errores.add("La contrasenia debe tener al menos 8 caracteres.");
		}
		if (usuarioRepo.findByUsername(dto.getUsername()).isPresent()) {
			errores.add("Ya existe un usuario con ese nombre.");
		}
		validarDocente(dto, errores);
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);

		Usuario u = new Usuario(dto.getUsername(), dto.getPassword(), dto.getRol(),
				dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		return aDTO(usuarioRepo.save(u));
	}

	public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
		Usuario u = usuarioRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
		List<String> errores = new ArrayList<>();
		if (!u.getUsername().equals(dto.getUsername()) && usuarioRepo.findByUsername(dto.getUsername()).isPresent()) {
			errores.add("Ya existe un usuario con ese nombre.");
		}
		boolean traeClave = dto.getPassword() != null && !dto.getPassword().isEmpty();
		if (traeClave && dto.getPassword().length() < 8) {
			errores.add("La contrasenia debe tener al menos 8 caracteres.");
		}
		validarDocente(dto, errores);
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);

		u.setUsername(dto.getUsername());
		u.setRol(dto.getRol());
		u.setDocenteId(dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		if (traeClave) u.setPassword(dto.getPassword());
		return aDTO(u);
	}

	public int eliminar(Long id) {
		if (!usuarioRepo.existsById(id)) throw new IllegalArgumentException("Usuario no encontrado");
		usuarioRepo.deleteById(id);
		return 0;
	}

	@Transactional(readOnly = true)
	public Long docenteIdDe(String username) {
		return usuarioRepo.findByUsername(username).map(Usuario::getDocenteId).orElse(null);
	}

	private void validarDocente(UsuarioDTO dto, List<String> errores) {
		if (dto.getRol() == RolUsuario.DOCENTE
				&& (dto.getDocenteId() == null || !docenteRepo.existsById(dto.getDocenteId()))) {
			errores.add("Un usuario DOCENTE necesita un docenteId existente.");
		}
	}

	private UsuarioDTO aDTO(Usuario u) {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(u.getId());
		dto.setUsername(u.getUsername());
		dto.setRol(u.getRol());
		dto.setDocenteId(u.getDocenteId());
		return dto;
	}

	
}