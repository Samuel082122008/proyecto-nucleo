package co.edu.unbosque.proyectonucleo.dto;

import java.util.Objects;

import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

	private Long id;
	
	@NotBlank 
	@Size(min = 5) 
	private String username;
	
	private String password;
	
	@NotNull 
	private RolUsuario rol;
	
	private Long docenteId;

	public UsuarioDTO() {
	}

	public UsuarioDTO(String username, String password, RolUsuario rol, Long docenteId) {
		super();
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.docenteId = docenteId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RolUsuario getRol() {
		return rol;
	}

	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public Long getDocenteId() {
		return docenteId;
	}

	public void setDocenteId(Long docenteId) {
		this.docenteId = docenteId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(docenteId, id, rol, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(docenteId, other.docenteId) && Objects.equals(id, other.id) 
				&& rol == other.rol && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", username=" + username + ", rol=" + rol + ", docenteId=" + docenteId + "]";
	}
}