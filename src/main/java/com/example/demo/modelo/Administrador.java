package com.example.demo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table (name = "Administrador")
public class Administrador {
    @Id
    @GeneratedValue (strategy = GenerationType. SEQUENCE)
    private Long idAdministrador;
    
    @Column (name = "usuario", length = 30, nullable = false, unique = true)
    private String usuarioAdmin;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (name = "password", nullable = false)
    private String passwordAdmin;

    public Administrador(Long idAdministrador, String usuarioAdmin, String passwordAdmin) {
        super();
        this.idAdministrador = idAdministrador;
        this.usuarioAdmin = usuarioAdmin;
        this.passwordAdmin = passwordAdmin;
    }

    public Administrador() {
    }

    public Long getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(Long idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(String usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }
    
    
}
