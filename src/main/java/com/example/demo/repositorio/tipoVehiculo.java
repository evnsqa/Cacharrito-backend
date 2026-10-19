package com.example.demo.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.modelo.TipoVehiculo;

@Repository
public interface tipoVehiculo extends JpaRepository<TipoVehiculo, Long> {
	
    public List<TipoVehiculo> findByNombre (String nombre);
}