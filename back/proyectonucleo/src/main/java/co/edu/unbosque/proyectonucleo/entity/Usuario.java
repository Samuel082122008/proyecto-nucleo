package co.edu.unbosque.proyectonucleo.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "clave", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RolUsuario rol;

	// Solo para usuarios con rol DOCENTE: a que docente corresponden
	private Long docenteId;

	public Usuario() {
	}

	public Usuario(String username, String password, RolUsuario rol, Long docenteId) {
		super();
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.docenteId = docenteId;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public RolUsuario getRol() { return rol; }
	public void setRol(RolUsuario rol) { this.rol = rol; }

	public Long getDocenteId() { return docenteId; }
	public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

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
		Usuario other = (Usuario) obj;
		return Objects.equals(docenteId, other.docenteId) && id == other.id && rol == other.rol
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", username=" + username + ", rol=" + rol + ", docenteId=" + docenteId + "]";
	}
}