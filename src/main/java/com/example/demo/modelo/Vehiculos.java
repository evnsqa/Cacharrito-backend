package com.example.demo.modelo;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table (name="Vehiculos")
public class Vehiculos {
	
	@Id
	@Column (name="placa", nullable=false)
	private String placa;
	
	@Column (name="nombre") 
	private String nombre;

	@Column (name="color") 
	private String color;
	
	@Column (name="precio")
	private Double precio;

	@Column (name="estado")
	private String estado;

	@Version
	private long version;
	
	@ManyToOne ()
	@JoinColumn (name = "id_tipo_vehiculo", referencedColumnName="id_tipo_vehiculo")
	private TipoVehiculo tipoVehiculo;
	
	@Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imagen;

	public Vehiculos(String placa, String nombre, String color, Double precio, String estado, TipoVehiculo tipoVehiculo, String imagen) {
		super();
		this.placa = placa;
		this.nombre = nombre;
		this.color = color;
		this.precio = precio;
		this.estado = estado;
		this.tipoVehiculo = tipoVehiculo;
		this.imagen = imagen;
	}

	public Vehiculos() {
		super();
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public long getVersion() { return version; }
	public void setVersion(long version) { this.version = version; }

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }



	
}
