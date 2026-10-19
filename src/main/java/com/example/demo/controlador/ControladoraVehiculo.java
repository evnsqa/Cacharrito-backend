package com.example.demo.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.modelo.TipoVehiculo;
import com.example.demo.modelo.Vehiculos;
import com.example.demo.repositorio.Vehiculo;

@RestController
@RequestMapping("/vehiculo/v/")
@CrossOrigin(origins = "*")
public class ControladoraVehiculo {
	
	@Autowired
	private Vehiculo repoVehiculo;
	
	
	@PostMapping("/buscarPlaca/")
	public Vehiculos buscarPlaca(@RequestParam("placa") String placa) {
		   return repoVehiculo.findById(placa).get();
	}
	
	
	@PostMapping("/buscarNom/")
	public List<Vehiculos> buscarNom(@RequestParam("nombre")String nombre) {
		   return this.repoVehiculo.findByNombre(nombre);
	}
	
	@PostMapping("/buscarPrecio/")
	public List<Vehiculos> buscarPrecio(@RequestParam("precio")Double precio) {
		   return this.repoVehiculo.findByPrecio(precio);
	}
	
	@PostMapping("/buscarEstado/")
	public List<Vehiculos> buscarEstado(@RequestParam("estado")String estado) {
		   return this.repoVehiculo.findByEstado(estado);
	}
	
	@PostMapping("/buscarPorTipo/")
	public List<Vehiculos> buscarPorTipo(@RequestParam("nombre") String nombre) {
	    return this.repoVehiculo.findByTipoVehiculoNombre(nombre);
	}
	
	@PostMapping("/buscarPorTipoEstado/")
	public List<Vehiculos> buscarPorTipoEstado(@RequestParam("nombreTipo") String nombreTipo) {
	    return this.repoVehiculo.buscarVehiculosDisponiblesPorTipo(nombreTipo);
	}


	@GetMapping("/listarTodo/")
	public List<Vehiculos> mostrarTodos(){
		return repoVehiculo.findAll();
	}
	
	@PostMapping("/guardarVehiculo/")
	public ResponseEntity<?> guardar(@RequestBody Vehiculos v, @RequestParam(value = "placaOriginal", required = false) String placaOriginal) {
	    
	    if (placaOriginal != null && !placaOriginal.isEmpty() && !placaOriginal.equalsIgnoreCase(v.getPlaca())) {

	        if (repoVehiculo.existsById(v.getPlaca())) {
	            return ResponseEntity.badRequest().body("La nueva placa " + v.getPlaca() + " ya está registrada en otro vehículo.");
	        }
	        
	        repoVehiculo.save(v);

	        repoVehiculo.deleteById(placaOriginal);
	        
	        return ResponseEntity.ok(v);
	    }
	    
	    if (placaOriginal == null || placaOriginal.isEmpty()) {
	        if (repoVehiculo.existsById(v.getPlaca())) {
	            return ResponseEntity.badRequest().body("La placa " + v.getPlaca() + " ya existe.");
	        }
	    }

	    repoVehiculo.save(v);
	    return ResponseEntity.ok(v);
	}

	

	@PostMapping("/eliminarVehiculo/")
	public Optional<Vehiculos> eliminarVehiculo(@RequestBody String n) {
		this.repoVehiculo.deleteById(n);
		return Optional.empty();
	}

	
	
}
