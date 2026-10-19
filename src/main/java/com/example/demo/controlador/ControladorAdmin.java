package com.example.demo.controlador;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.modelo.Administrador;
import com.example.demo.repositorio.administrador;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class ControladorAdmin {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
    private administrador adminRepository;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginAdmin(@RequestParam String usuarioAdmin, @RequestParam String passwordAdmin) {
        Optional<Administrador> admin = adminRepository.findByUsuarioAdmin(usuarioAdmin);
        
		if (admin.isPresent() && validarPasswordAdmin(admin.get(), passwordAdmin)) {
            return new ResponseEntity<>(admin.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean validarPasswordAdmin(Administrador admin, String password) {
        String almacenada = admin.getPasswordAdmin();
        if (almacenada != null && almacenada.startsWith("$2") && passwordEncoder.matches(password, almacenada)) {
            return true;
        }
        if (almacenada != null && almacenada.equals(password)) {
            admin.setPasswordAdmin(passwordEncoder.encode(password));
            adminRepository.save(admin);
            return true;
        }
        return false;
    }
}
