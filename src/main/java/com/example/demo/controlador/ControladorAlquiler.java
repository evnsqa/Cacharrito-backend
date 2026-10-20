package com.example.demo.controlador;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.dao.OptimisticLockingFailureException;

import com.example.demo.modelo.Alquiler;
import com.example.demo.modelo.Vehiculos;
import com.example.demo.repositorio.Vehiculo;
import com.example.demo.repositorio.alquileres;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/alquileres/")
public class ControladorAlquiler {

	private static final Logger LOGGER = Logger.getLogger(ControladorAlquiler.class.getName());

	@Autowired
	private alquileres repositorioAlquiler;

	@Autowired
	private Vehiculo repositorioVehiculo;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.frontend-url:http://localhost:4200}")
	private String frontendUrl;

	@Value("${spring.mail.from:noreply@cacharrito.local}")
	private String correoRemitente;
	@Value("${MAIL_FROM_NAME:Cacharrito}")
	private String nombreRemitente;

	@Value("${BREVO_API_KEY:}")
	private String brevoApiKey;

	private final RestClient brevoClient = RestClient.builder().baseUrl("https://api.brevo.com").build();

	@PostMapping("/guardarAlquiler/")
	@Transactional
	public ResponseEntity<?> guardar(@RequestBody Alquiler alquiler) {
		if (alquiler.getFechaInicio() == null || alquiler.getFechaEntrega() == null
				|| alquiler.getVehiculos() == null || alquiler.getUsuario() == null) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "Faltan fechas, vehÃ­culo o usuario para registrar el alquiler."));
		}

		if (alquiler.getFechaInicio().isBefore(java.time.LocalDate.now(java.time.ZoneId.of("America/Bogota")))) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "La fecha de inicio no puede estar en el pasado."));
		}

		long dias = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaEntrega());
		if (dias <= 0) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "La fecha de entrega debe ser posterior a la fecha de inicio."));
		}

		Optional<Vehiculos> vehiculoOpt = repositorioVehiculo.findById(alquiler.getVehiculos().getPlaca());
		if (vehiculoOpt.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "El vehículo seleccionado ya no existe."));
		}

		Vehiculos vehiculo = vehiculoOpt.get();
		if (!"Disponible".equalsIgnoreCase(vehiculo.getEstado())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El vehículo ya no está disponible."));
		}
		if (vehiculo.getPrecio() == null || vehiculo.getPrecio() <= 0) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "El vehículo no tiene una tarifa diaria válida."));
		}

		vehiculo.setEstado("Reservado");
		alquiler.setVehiculos(vehiculo);
		alquiler.setValorTotal(vehiculo.getPrecio() * dias);
		alquiler.setEstado("pendiente de aceptación");
		alquiler.setPagado(false);
		repositorioVehiculo.save(vehiculo);
		return ResponseEntity.ok(repositorioAlquiler.save(alquiler));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(OptimisticLockingFailureException.class)
	public ResponseEntity<?> conflictoDeReserva(OptimisticLockingFailureException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("mensaje", "El vehículo acaba de ser reservado por otra persona. Actualiza el catálogo y elige otro."));
	}

	@GetMapping("/listarPorUsuario/")
	public List<Alquiler> listarPorUsuario(@RequestParam("idUsuario") Long idUsuario) {
		return repositorioAlquiler.alquileresPorUsuario(idUsuario);
	}

	@GetMapping("/historialPorUsuario/")
	public List<Alquiler> historialPorUsuario(@RequestParam("idUsuario") String idUsuario) {
		return repositorioAlquiler.historialAlquileresPorUsuario(idUsuario);
	}

	@GetMapping("/historialCompleto/")
	public List<Alquiler> historialCompleto() {
		return repositorioAlquiler.findAll();
	}
	@PostMapping("/cancelarAlquiler/")
	@Transactional
	public ResponseEntity<?> cancelar(@RequestParam("id") Long id) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();

		Alquiler alquiler = alquilerOpt.get();
		boolean pendiente = "pendiente de aceptación".equalsIgnoreCase(alquiler.getEstado())
				|| ("pendiente de entrega".equalsIgnoreCase(alquiler.getEstado()) && !Boolean.TRUE.equals(alquiler.getPagado()));
		if (!pendiente) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "Solo se pueden cancelar solicitudes que aún no han sido aprobadas."));
		}

		alquiler.setEstado("cancelado");
		alquiler.getVehiculos().setEstado("Disponible");
		repositorioVehiculo.save(alquiler.getVehiculos());
		return ResponseEntity.ok(repositorioAlquiler.save(alquiler));
	}

	@GetMapping("/listarPendientes/")
	public List<Alquiler> pendientes() {
		return repositorioAlquiler.alquileresPendientes();
	}

	@GetMapping("/listarPendientesEntrega/")
	public List<Alquiler> pendientesEntrega() {
		return repositorioAlquiler.alquileresPendientesEntrega();
	}

	@GetMapping("/listarEntregados/")
	public List<Alquiler> entregados() {
		return repositorioAlquiler.alquileresEntregados();
	}

	@PostMapping("/aceptarAlquiler/")
	@Transactional
	public ResponseEntity<?> aceptar(@RequestParam("id") Long id) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();

		Alquiler alquiler = alquilerOpt.get();
		boolean pendiente = "pendiente de aceptación".equalsIgnoreCase(alquiler.getEstado())
				|| ("pendiente de entrega".equalsIgnoreCase(alquiler.getEstado()) && !Boolean.TRUE.equals(alquiler.getPagado()));
		if (!pendiente) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "Esta solicitud ya fue procesada."));
		}

		alquiler.setEstado("pendiente de pago");
		alquiler = repositorioAlquiler.save(alquiler);
		boolean correoEnviado = false;
		try {
			String enlacePago = frontendUrl.replaceAll("/+$", "") + "/#/pago/" + alquiler.getNumeroAlquiler();
			SimpleMailMessage mensaje = new SimpleMailMessage();
			mensaje.setFrom(correoRemitente);
			mensaje.setTo(alquiler.getUsuario().getCorreoElectronico());
			mensaje.setSubject("Tu solicitud de alquiler fue aprobada - Cacharrito");
			mensaje.setText("Hola " + alquiler.getUsuario().getNombreCompleto() + ",\n\n"
					+ "Aprobamos tu solicitud para el vehículo " + alquiler.getVehiculos().getNombre() + " (" + alquiler.getVehiculos().getPlaca() + ").\n"
					+ "Total de la reserva: $" + alquiler.getValorTotal() + " COP.\n\n"
					+ "Completa el pago simulado desde este enlace: " + enlacePago + "\n\nEquipo Cacharrito");
			enviarCorreo(mensaje);
			correoEnviado = true;
		} catch (RuntimeException ex) {
			LOGGER.log(Level.WARNING, "No se pudo enviar el correo de aprobación del alquiler {0}: {1}",
					new Object[] { id, ex.getMessage() });
		}

		return ResponseEntity.ok(Map.of(
				"mensaje", correoEnviado ? "Solicitud aprobada y correo enviado." : "Solicitud aprobada; el correo no se pudo enviar.",
				"correoEnviado", correoEnviado));
	}

	@PostMapping("/rechazarAlquiler/")
	@Transactional
	public ResponseEntity<?> rechazar(@RequestParam("id") Long id) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();

		Alquiler alquiler = alquilerOpt.get();
		boolean pendiente = "pendiente de aceptación".equalsIgnoreCase(alquiler.getEstado())
				|| ("pendiente de entrega".equalsIgnoreCase(alquiler.getEstado()) && !Boolean.TRUE.equals(alquiler.getPagado()));
		if (!pendiente) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("mensaje", "Solo se pueden rechazar solicitudes que aún no han sido aprobadas o pagadas."));
		}

		alquiler.setEstado("cancelado");
		if (alquiler.getVehiculos() != null) {
			alquiler.getVehiculos().setEstado("Disponible");
			repositorioVehiculo.save(alquiler.getVehiculos());
		}
		repositorioAlquiler.save(alquiler);

		boolean correoEnviado = false;
		try {
			SimpleMailMessage mensaje = new SimpleMailMessage();
			mensaje.setFrom(correoRemitente);
			mensaje.setTo(alquiler.getUsuario().getCorreoElectronico());
			mensaje.setSubject("Actualización de tu solicitud de alquiler - Cacharrito");
			mensaje.setText("Hola " + alquiler.getUsuario().getNombreCompleto() + ",\n\n"
					+ "Lamentamos informarte que tu solicitud de alquiler #" + id + " fue rechazada.\n"
					+ "El vehículo " + alquiler.getVehiculos().getNombre() + " (" + alquiler.getVehiculos().getPlaca() + ") vuelve a estar disponible.\n\n"
					+ "Si tienes preguntas, comunícate con nuestro equipo.\n\nEquipo Cacharrito");
			enviarCorreo(mensaje);
			correoEnviado = true;
		} catch (RuntimeException ex) {
			LOGGER.log(Level.WARNING, "No se pudo enviar el correo de rechazo del alquiler {0}: {1}",
					new Object[] { id, ex.getMessage() });
		}

		return ResponseEntity.ok(Map.of(
				"mensaje", correoEnviado ? "Solicitud rechazada y correo enviado." : "Solicitud rechazada; el correo no se pudo enviar.",
				"correoEnviado", correoEnviado));
	}
	private void enviarCorreo(SimpleMailMessage mensaje) {
		if (brevoApiKey != null && !brevoApiKey.isBlank()) {
			String remitente = mensaje.getFrom();
			String[] destinatarios = mensaje.getTo();
			if (remitente == null || remitente.isBlank() || remitente.endsWith(".local")) {
				throw new IllegalStateException("Configura MAIL_FROM con un remitente verificado en Brevo.");
			}
			if (destinatarios == null || destinatarios.length == 0) {
				throw new IllegalStateException("El mensaje no tiene destinatario.");
			}

			Map<String, Object> payload = Map.of(
					"sender", Map.of("name", nombreRemitente, "email", remitente),
					"to", List.of(Map.of("email", destinatarios[0])),
					"subject", mensaje.getSubject(),
					"textContent", mensaje.getText());
			brevoClient.post()
					.uri("/v3/smtp/email")
					.header("api-key", brevoApiKey)
					.contentType(MediaType.APPLICATION_JSON)
					.body(payload)
					.retrieve()
					.toBodilessEntity();
			return;
		}
		mailSender.send(mensaje);
	}
	@GetMapping("/buscarPorId/")
	public ResponseEntity<?> buscarPorId(@RequestParam("id") Long id) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();

		Alquiler alquiler = alquilerOpt.get();
		Map<String, Object> respuesta = new java.util.HashMap<>();
		respuesta.put("numeroAlquiler", alquiler.getNumeroAlquiler());
		respuesta.put("fechaInicio", alquiler.getFechaInicio());
		respuesta.put("fechaEntrega", alquiler.getFechaEntrega());
		respuesta.put("valorTotal", alquiler.getValorTotal());
		respuesta.put("estado", alquiler.getEstado());
		respuesta.put("pagado", alquiler.getPagado());

		if (alquiler.getVehiculos() != null) {
			Map<String, Object> vehiculo = new java.util.HashMap<>();
			vehiculo.put("nombre", alquiler.getVehiculos().getNombre());
			vehiculo.put("placa", alquiler.getVehiculos().getPlaca());
			vehiculo.put("color", alquiler.getVehiculos().getColor());
			if (alquiler.getVehiculos().getTipoVehiculo() != null) {
				Map<String, Object> tipo = new java.util.HashMap<>();
				tipo.put("nombre", alquiler.getVehiculos().getTipoVehiculo().getNombre());
				vehiculo.put("tipoVehiculo", tipo);
			}
			respuesta.put("vehiculos", vehiculo);
		}

		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/pagar/")
	@Transactional
	public ResponseEntity<?> pagar(@RequestParam("id") Long id) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();

		Alquiler alquiler = alquilerOpt.get();
		if (!"pendiente de pago".equalsIgnoreCase(alquiler.getEstado()) || Boolean.TRUE.equals(alquiler.getPagado())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El alquiler no está pendiente de pago."));
		}

		alquiler.setPagado(true);
		alquiler.setEstado("pendiente de entrega");
		return ResponseEntity.ok(repositorioAlquiler.save(alquiler));
	}

	@PostMapping("/entregarVehiculo/")
	@Transactional
	public ResponseEntity<?> entregar(@RequestParam("placa") String placa) {
		Optional<Alquiler> alquilerOpt = repositorioAlquiler.alquileresPendientesEntrega().stream()
				.filter(a -> a.getVehiculos() != null && placa.equalsIgnoreCase(a.getVehiculos().getPlaca()))
				.findFirst();
		if (alquilerOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "No hay un alquiler pagado pendiente de entrega para esa placa."));
		}

		Alquiler alquiler = alquilerOpt.get();
		alquiler.setEstado("entregado");
		alquiler.getVehiculos().setEstado("Alquilado");
		repositorioVehiculo.save(alquiler.getVehiculos());
		return ResponseEntity.ok(repositorioAlquiler.save(alquiler));
	}

	@PostMapping("/devolverVehiculo/")
	@Transactional
	public ResponseEntity<?> devolver(@RequestParam("id") Long id,
			@RequestParam(value = "valorExtra", defaultValue = "0") Double valorExtra) {
		if (valorExtra == null || valorExtra < 0) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "El valor adicional debe ser cero o positivo."));
		}

		Optional<Alquiler> alquilerOpt = repositorioAlquiler.findById(id);
		if (alquilerOpt.isEmpty()) return ResponseEntity.notFound().build();
		Alquiler alquiler = alquilerOpt.get();
		if (!"entregado".equalsIgnoreCase(alquiler.getEstado())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "Solo se pueden devolver vehículos que figuran como entregados."));
		}

		alquiler.setEstado("devuelto");
		alquiler.setFechaEntregaReal(java.time.LocalDate.now());
		alquiler.setValorTotal(alquiler.getValorTotal() + valorExtra);
		alquiler.getVehiculos().setEstado("Disponible");
		repositorioVehiculo.save(alquiler.getVehiculos());
		repositorioAlquiler.save(alquiler);
		return ResponseEntity.ok("Vehículo devuelto correctamente.");
	}
}
