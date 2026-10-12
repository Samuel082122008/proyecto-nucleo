package co.edu.unbosque.proyectonucleo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.unbosque.proyectonucleo.entity.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

	private Long id;
	@NotBlank private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(min = 6) private String password;

	@NotNull private RolUsuario rol;
	private Long docenteId;

	public UsuarioDTO() {
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public RolUsuario getRol() { return rol; }
	public void setRol(RolUsuario rol) { this.rol = rol; }

	public Long getDocenteId() { return docenteId; }
	public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }
}
