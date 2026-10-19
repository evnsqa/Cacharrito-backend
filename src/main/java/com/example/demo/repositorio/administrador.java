package com.example.demo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.modelo.Administrador;
import java.util.Optional;

public interface administrador extends JpaRepository<Administrador, Long>{
	Optional<Administrador> findByUsuarioAdmin(String usuarioAdmin);
	
	

}
