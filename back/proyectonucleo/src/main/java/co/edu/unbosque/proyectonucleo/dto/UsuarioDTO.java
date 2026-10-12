package co.edu.unbosque.proyectonucleo.dto;

import co.edu.unbosque.proyectonucleo.entity.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "El nombre de usuario es obligatorio") 
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria") 
    private String password;
    
    @NotNull(message = "El rol es obligatorio") 
    private Rol rol;

    public UsuarioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
