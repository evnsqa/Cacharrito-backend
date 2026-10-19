package com.example.demo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Tipo_vehiculo")
public class TipoVehiculo {
    
    @Id
	@GeneratedValue (strategy = GenerationType. SEQUENCE)
    @Column (name ="id_tipo_vehiculo")
    private Long idTipoVehiculo;

	@Column (name="nombre", length=50, nullable=false)
	private String nombre;
  

	public TipoVehiculo(Long idTipoVehiculo, String nombre, String descripcion) {
		
		this.idTipoVehiculo = idTipoVehiculo;
		this.nombre = nombre;	
	}

	public TipoVehiculo() {
		super();
	}

	public Long getIdTipoVehiculo() {
		return idTipoVehiculo;
	}

	public void setIdTipoVehiculo(Long idTipoVehiculo) {
		this.idTipoVehiculo = idTipoVehiculo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


  
    
    
}