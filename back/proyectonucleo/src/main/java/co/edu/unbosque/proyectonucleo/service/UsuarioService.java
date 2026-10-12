package co.edu.unbosque.proyectonucleo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.LoginDTO;
import co.edu.unbosque.proyectonucleo.dto.UsuarioDTO;
import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import co.edu.unbosque.proyectonucleo.entity.Usuario;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.ReglaNegocioException;
import co.edu.unbosque.proyectonucleo.exception.UsuarioNoEncontradoException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;
import co.edu.unbosque.proyectonucleo.security.JwtUtil;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private DocenteRepository docenteRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Transactional(readOnly = true)
	public List<UsuarioDTO> listar() {
		return usuarioRepo.findAll().stream().map(this::aDTO).toList();
	}

	public UsuarioDTO crear(UsuarioDTO dto) {
		List<String> errores = new ArrayList<>();
		if (dto.getPassword() == null || dto.getPassword().isBlank()) {
			errores.add("La contraseña es obligatoria.");
		}
		if (usuarioRepo.existsByUsername(dto.getUsername())) {
			errores.add("Ya existe un usuario con ese nombre.");
		}
		validarDocente(dto, errores);
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);

		Usuario u = new Usuario();
		u.setUsername(dto.getUsername());
		u.setPassword(passwordEncoder.encode(dto.getPassword()));
		u.setRol(dto.getRol());
		u.setDocenteId(dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		return aDTO(usuarioRepo.save(u));
	}

	public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
		Usuario u = usuarioRepo.findById(id).orElseThrow(UsuarioNoEncontradoException::new);

		List<String> errores = new ArrayList<>();
		if (!u.getUsername().equals(dto.getUsername()) && usuarioRepo.existsByUsername(dto.getUsername())) {
			errores.add("Ya existe un usuario con ese nombre.");
		}
		validarDocente(dto, errores);
		if (!errores.isEmpty()) throw new ReglaNegocioException(errores);

		u.setUsername(dto.getUsername());
		u.setRol(dto.getRol());
		u.setDocenteId(dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			u.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		return aDTO(u);
	}

	public int eliminar(Long id) {
		if (!usuarioRepo.existsById(id)) throw new UsuarioNoEncontradoException();
		usuarioRepo.deleteById(id);
		return 0;
	}

	/** Devuelve {"token", "rol"} o null si las credenciales no son correctas. */
	@Transactional(readOnly = true)
	public Map<String, String> login(LoginDTO dto) {
		Optional<Usuario> usuario = usuarioRepo.findByUsername(dto.getUsername());
		if (usuario.isEmpty() || !passwordEncoder.matches(dto.getPassword(), usuario.get().getPassword())) {
			return null;
		}
		String rol = usuario.get().getRol().name();
		return Map.of("token", jwtUtil.generarToken(usuario.get().getUsername(), rol), "rol", rol);
	}

	private void validarDocente(UsuarioDTO dto, List<String> errores) {
		if (dto.getRol() == RolUsuario.DOCENTE) {
			if (dto.getDocenteId() == null) {
				errores.add("Un usuario con rol DOCENTE debe estar asociado a un docente.");
			} else if (!docenteRepo.existsById(dto.getDocenteId())) {
				throw new DocenteNoEncontradoException();
			}
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