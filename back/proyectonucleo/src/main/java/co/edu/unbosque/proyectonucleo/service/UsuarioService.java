package co.edu.unbosque.proyectonucleo.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectonucleo.dto.LoginDTO;
import co.edu.unbosque.proyectonucleo.dto.UsuarioDTO;
import co.edu.unbosque.proyectonucleo.entity.Usuario;
import co.edu.unbosque.proyectonucleo.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Map<String, String> login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!usuario.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        return Map.of(
            "token", "token-simulado-unbosque-" + usuario.getUsername(),
            "rol", usuario.getRol().name()
        );
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario nuevo = mapiarEntidad(new Usuario(), dto);
        return convertirADTO(usuarioRepository.save(nuevo));
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        mapiarEntidad(existente, dto);
        return convertirADTO(usuarioRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirADTO(Usuario entidad) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entidad.getId());
        dto.setUsername(entidad.getUsername());
        dto.setRol(entidad.getRol());
        return dto;
    }

    private Usuario mapiarEntidad(Usuario entidad, UsuarioDTO dto) {
        entidad.setUsername(dto.getUsername());
        entidad.setPassword(dto.getPassword());
        entidad.setRol(dto.getRol());
        return entidad;
    }
}
