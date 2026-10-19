package com.example.demo.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.modelo.Alquiler;
import com.example.demo.modelo.Usuario;

public interface alquileres extends JpaRepository<Alquiler, Long> {

	@Query("SELECT a FROM Alquiler a WHERE LOWER(a.estado) LIKE 'pendiente de acept%' OR (a.estado = 'pendiente de entrega' AND (a.pagado = false OR a.pagado IS NULL)) ORDER BY a.numeroAlquiler DESC")
	List<Alquiler> alquileresPendientes();

	@Query("SELECT a FROM Alquiler a WHERE a.estado = 'pendiente de entrega' AND a.pagado = true ORDER BY a.numeroAlquiler DESC")
	List<Alquiler> alquileresPendientesEntrega();

	@Query("SELECT a FROM Alquiler a WHERE a.usuario.id = :idUsuario AND a.estado NOT IN ('cancelado', 'devuelto') ORDER BY a.numeroAlquiler DESC")
	List<Alquiler> alquileresPorUsuario(@Param("idUsuario") Long idUsuario);
	
	@Query("SELECT a FROM Alquiler a WHERE LOWER(a.estado) = 'entregado' ORDER BY a.numeroAlquiler DESC")
	List<Alquiler> alquileresEntregados();


		@Modifying
		@Transactional
		@Query("UPDATE Alquiler a SET a.estado = 'cancelado' WHERE a.numeroAlquiler = :id")
		Integer cancelarAlquiler(@Param("id") Long id);

		@Modifying
		@Transactional
		@Query("UPDATE Alquiler a SET a.estado = 'entregado' WHERE a.vehiculos.placa = :placa AND a.estado = 'pendiente de entrega'")
		Integer entregarVehiculo(@Param("placa") String placa);

		@Modifying
		@Transactional
		@Query("UPDATE Alquiler a SET a.estado = 'disponible', a.valorTotal = a.valorTotal + :valorExtra WHERE a.numeroAlquiler = :id")
		Integer devolverVehiculo(@Param("id") Long id, @Param("valorExtra") Double valorExtra);
		

		public List<Alquiler> findByUsuario (Usuario usuario);

	}


