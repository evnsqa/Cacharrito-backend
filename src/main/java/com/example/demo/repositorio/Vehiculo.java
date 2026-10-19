package com.example.demo.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.modelo.Vehiculos;

public interface Vehiculo  extends JpaRepository<Vehiculos, String> {
	
	public List<Vehiculos> findByNombre (String nombre);
	
	public List<Vehiculos> findByPrecio (Double precio);
	
	public List<Vehiculos> findByEstado (String estado);
	
	public List<Vehiculos> findByTipoVehiculoNombre (String nombre);

	@Query(value = "SELECT v.* FROM vehiculos v " +
            "INNER JOIN tipo_vehiculo t ON v.id_tipo_vehiculo = t.id_tipo_vehiculo " +
            "WHERE t.nombre = :nombreTipo AND v.estado = 'Disponible' ", nativeQuery = true)
	public List<Vehiculos> buscarVehiculosDisponiblesPorTipo(@Param("nombreTipo") String nombreTipo);
	
}
