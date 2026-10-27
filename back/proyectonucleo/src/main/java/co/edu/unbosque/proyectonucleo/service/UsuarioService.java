package co.edu.unbosque.proyectonucleo.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.LoginDTO;
import co.edu.unbosque.proyectonucleo.dto.UsuarioDTO;
import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import co.edu.unbosque.proyectonucleo.entity.Usuario;
import co.edu.unbosque.proyectonucleo.exception.ContrasenaInvalidaException;
import co.edu.unbosque.proyectonucleo.exception.CredencialesInvalidasException;
import co.edu.unbosque.proyectonucleo.exception.DatosObligatoriosException;
import co.edu.unbosque.proyectonucleo.exception.DocenteNoEncontradoException;
import co.edu.unbosque.proyectonucleo.exception.DocenteRequeridoException;
import co.edu.unbosque.proyectonucleo.exception.IdInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.UltimoDirectorException;
import co.edu.unbosque.proyectonucleo.exception.UsuarioDuplicadoException;
import co.edu.unbosque.proyectonucleo.exception.UsuarioInvalidoException;
import co.edu.unbosque.proyectonucleo.exception.UsuarioNoEncontradoException;
import co.edu.unbosque.proyectonucleo.repository.DocenteRepository;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;
import co.edu.unbosque.proyectonucleo.security.JwtUtil;

@Service
@Transactional
public class UsuarioService {

	private static final Pattern PATRON_USERNAME = Pattern.compile("^[A-Za-z0-9._@-]{4,50}$");

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
		validar(dto, true);
		if (usuarioRepo.existsByUsername(dto.getUsername())) throw new UsuarioDuplicadoException();
		validarDocenteExiste(dto);

		Usuario u = new Usuario();
		u.setUsername(dto.getUsername());
		u.setPassword(passwordEncoder.encode(dto.getPassword()));
		u.setRol(dto.getRol());
		u.setDocenteId(dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		return aDTO(usuarioRepo.save(u));
	}

	public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
		validarId(id);
		validar(dto, false);
		Usuario u = usuarioRepo.findById(id).orElseThrow(UsuarioNoEncontradoException::new);

		if (!u.getUsername().equals(dto.getUsername()) && usuarioRepo.existsByUsername(dto.getUsername())) {
			throw new UsuarioDuplicadoException();
		}
		validarDocenteExiste(dto);
		if (u.getRol() == RolUsuario.DIRECTOR && dto.getRol() != RolUsuario.DIRECTOR
				&& usuarioRepo.countByRol(RolUsuario.DIRECTOR) <= 1) {
			throw new UltimoDirectorException();
		}

		u.setUsername(dto.getUsername());
		u.setRol(dto.getRol());
		u.setDocenteId(dto.getRol() == RolUsuario.DOCENTE ? dto.getDocenteId() : null);
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			u.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		return aDTO(u);
	}

	public int eliminar(Long id) {
		validarId(id);
		Usuario u = usuarioRepo.findById(id).orElseThrow(UsuarioNoEncontradoException::new);
		if (u.getRol() == RolUsuario.DIRECTOR && usuarioRepo.countByRol(RolUsuario.DIRECTOR) <= 1) {
			throw new UltimoDirectorException();
		}
		usuarioRepo.deleteById(id);
		return 0;
	}

	/** Devuelve {"token", "rol"} o lanza CredencialesInvalidasException (401). */
	@Transactional(readOnly = true)
	public Map<String, String> login(LoginDTO dto) {
		if (dto == null) throw new DatosObligatoriosException();
		Optional<Usuario> usuario = dto.getUsername() == null ? Optional.empty()
				: usuarioRepo.findByUsername(dto.getUsername());
		if (usuario.isEmpty() || dto.getPassword() == null
				|| !passwordEncoder.matches(dto.getPassword(), usuario.get().getPassword())) {
			throw new CredencialesInvalidasException();
		}
		String rol = usuario.get().getRol().name();
		return Map.of("token", jwtUtil.generarToken(usuario.get().getUsername(), rol), "rol", rol);
	}

	private void validarId(Long id) {
		if (id == null || id <= 0) throw new IdInvalidoException();
	}

	private void validar(UsuarioDTO dto, boolean passwordObligatoria) {
		if (dto == null) throw new DatosObligatoriosException();
		if (dto.getUsername() == null || !PATRON_USERNAME.matcher(dto.getUsername()).matches()) {
			throw new UsuarioInvalidoException();
		}
		String p = dto.getPassword();
		boolean hayPassword = p != null && !p.isBlank();
		if ((passwordObligatoria || hayPassword)
				&& (p == null || p.isBlank() || p.length() < 6 || p.getBytes(StandardCharsets.UTF_8).length > 72)) {
			throw new ContrasenaInvalidaException();
		}
		if (dto.getRol() == null) throw new DatosObligatoriosException("rol");
		if (dto.getRol() == RolUsuario.DOCENTE) {
			if (dto.getDocenteId() == null) throw new DocenteRequeridoException();
			validarId(dto.getDocenteId());
		}
	}

	private void validarDocenteExiste(UsuarioDTO dto) {
		if (dto.getRol() == RolUsuario.DOCENTE && !docenteRepo.existsById(dto.getDocenteId())) {
			throw new DocenteNoEncontradoException();
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