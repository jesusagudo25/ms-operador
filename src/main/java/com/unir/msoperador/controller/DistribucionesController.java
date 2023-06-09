package com.unir.msoperador.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.unir.msoperador.model.pojo.Distribucion;
import com.unir.msoperador.model.request.CreateDistribucionRequest;
import com.unir.msoperador.service.DistribucionesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DistribucionesController {
	private final DistribucionesService service;

	@GetMapping("/distribuciones")
	public ResponseEntity<List<Distribucion>> getDistribuciones(@RequestHeader Map<String, String> headers) {

		log.info("headers: {}", headers);
		List<Distribucion> distribuciones = service.getDistribuciones();

		if (distribuciones != null) {
			return ResponseEntity.ok(distribuciones);
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}
	
	@GetMapping("/distribuciones/{distribucionId}")
	public ResponseEntity<Distribucion> getDistribucion(@PathVariable long distribucionId) {

		log.info("Request received for product {}", distribucionId);
		Distribucion distribucion = service.getDistribucion(distribucionId);

		if (distribucion != null) {
			return ResponseEntity.ok(distribucion);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@DeleteMapping("/distribuciones/{distribucionId}")
	public ResponseEntity<Void> deleteDistribucion(@PathVariable long distribucionId) {

		Boolean removed = service.removeDistribucion(distribucionId);

		if (Boolean.TRUE.equals(removed)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/distribuciones")
	public ResponseEntity<Distribucion> createDistribucion(@RequestBody CreateDistribucionRequest request) {

		Distribucion createdDistribucion = service.createDistribucion(request);

		if (createdDistribucion != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdDistribucion);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
}
