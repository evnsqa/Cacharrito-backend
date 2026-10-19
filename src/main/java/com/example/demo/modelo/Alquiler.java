package com.example.demo.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table (name = "Alquiler")
public class Alquiler {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "numero_alquiler")
	private Long numeroAlquiler;

	@Column(name = "fecha_inicio", length = 50, nullable = false)
	private LocalDate fechaInicio;

	@Column(name = "fecha_entrega", length = 50, nullable = false)
	private LocalDate fechaEntrega;

	@Column(name = "valor_total", nullable = false)
	private Double valorTotal;

	@Column(name = "estado", length = 50, nullable = false)
	private String estado;
	
	@Column(name = "fecha_entrega_real", length = 50)
	private LocalDate fechaEntregaReal;

	@Column(name = "pagado")
	private Boolean pagado = false;
	
	@ManyToOne ()
	@JoinColumn (name = "id_usuario", referencedColumnName="id")
	private Usuario usuario;
	
	@ManyToOne ()
	@JoinColumn (name = "id_admi", referencedColumnName="idAdministrador")
	private Administrador administrador;
	
	@ManyToOne ()
	@JoinColumn (name = "placa", referencedColumnName="placa")
	private Vehiculos vehiculos;

	public Alquiler(Long numeroAlquiler, LocalDate fechaInicio, LocalDate fechaEntrega, Double valorTotal,
			String estado, LocalDate fechaEntregaReal, Usuario usuario, Administrador administrador,
			Vehiculos vehiculos) {
		super();
		this.numeroAlquiler = numeroAlquiler;
		this.fechaInicio = fechaInicio;
		this.fechaEntrega = fechaEntrega;
		this.valorTotal = valorTotal;
		this.estado = estado;
		this.fechaEntregaReal = fechaEntregaReal;
		this.usuario = usuario;
		this.administrador = administrador;
		this.vehiculos = vehiculos;
	}

	public Alquiler() {
		
	}

	public Long getNumeroAlquiler() {
		return numeroAlquiler;
	}

	public void setNumeroAlquiler(Long numeroAlquiler) {
		this.numeroAlquiler = numeroAlquiler;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(LocalDate fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDate getFechaEntregaReal() {
		return fechaEntregaReal;
	}

	public void setFechaEntregaReal(LocalDate fechaEntregaReal) {
		this.fechaEntregaReal = fechaEntregaReal;
	}

	public Boolean getPagado() {
		return pagado;
	}

	public void setPagado(Boolean pagado) {
		this.pagado = pagado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public Vehiculos getVehiculos() {
		return vehiculos;
	}

	public void setVehiculos(Vehiculos vehiculos) {
		this.vehiculos = vehiculos;
	}
}

