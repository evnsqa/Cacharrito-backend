package com.example.demo.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modelo.Usuario;

@Repository
public interface usuario extends JpaRepository<Usuario,Long> {
	
	Optional<Usuario> findByIdUsuario(String idUsuario);
	
    public List<Usuario> findByNombreCompleto(String nombreCompleto);

    public List<Usuario> findByFechaExpedicionLicencia(LocalDate fechaExpedicionLicencia);

    public List<Usuario> findByCategoriaLicencia(String categoriaLicencia);

    public List<Usuario> findByVigenciaLicencia(LocalDate vigenciaLicencia);

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    public List<Usuario> findByTelefono(String telefono);
    
    boolean existsByIdUsuario(String idUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);
	
}
