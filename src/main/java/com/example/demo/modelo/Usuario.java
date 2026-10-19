package com.example.demo.modelo;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table (name = "Usuario")
public class Usuario {
		
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
		
		
		@Column (name ="identificacion_usuario", unique = true, nullable = false, length = 20)
		private String idUsuario;
		
		@Column (name = "nombre_completo", length = 100, nullable = false)
		private String nombreCompleto;
		
		@Column (name = "fecha_expedicion_licencia", nullable = false)
		private LocalDate fechaExpedicionLicencia;
		
		@Column (name = "categoria_licencia", nullable = false)
		private String categoriaLicencia;
		
		@Column (name = "vigencia_licencia", nullable = false)
		private LocalDate vigenciaLicencia;
		
		@Column (name = "correo_electronico", unique = true, nullable = false)
		private String correoElectronico;
		
		@Column (name = "telefono", length = 30, nullable = false)
		private String telefono;
		
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		@Column (name = "password", nullable = false)
		private String password;

		public Usuario(Long id, String idUsuario, String nombreCompleto, LocalDate fechaExpedicionLicencia,
				String categoriaLicencia, LocalDate vigenciaLicencia, String correoElectronico, String telefono,
				String password) {
			super();
			this.id = id;
			this.idUsuario = idUsuario;
			this.nombreCompleto = nombreCompleto;
			this.fechaExpedicionLicencia = fechaExpedicionLicencia;
			this.categoriaLicencia = categoriaLicencia;
			this.vigenciaLicencia = vigenciaLicencia;
			this.correoElectronico = correoElectronico;
			this.telefono = telefono;
			this.password = password;
		}

		public Usuario() {
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getIdUsuario() {
			return idUsuario;
		}

		public void setIdUsuario(String idUsuario) {
			this.idUsuario = idUsuario;
		}

		public String getNombreCompleto() {
			return nombreCompleto;
		}

		public void setNombreCompleto(String nombreCompleto) {
			this.nombreCompleto = nombreCompleto;
		}

		public LocalDate getFechaExpedicionLicencia() {
			return fechaExpedicionLicencia;
		}

		public void setFechaExpedicionLicencia(LocalDate fechaExpedicionLicencia) {
			this.fechaExpedicionLicencia = fechaExpedicionLicencia;
		}

		public String getCategoriaLicencia() {
			return categoriaLicencia;
		}

		public void setCategoriaLicencia(String categoriaLicencia) {
			this.categoriaLicencia = categoriaLicencia;
		}

		public LocalDate getVigenciaLicencia() {
			return vigenciaLicencia;
		}

		public void setVigenciaLicencia(LocalDate vigenciaLicencia) {
			this.vigenciaLicencia = vigenciaLicencia;
		}

		public String getCorreoElectronico() {
			return correoElectronico;
		}

		public void setCorreoElectronico(String correoElectronico) {
			this.correoElectronico = correoElectronico;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
}

		
