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

import com.unir.msoperador.model.pojo.Orden;
import com.unir.msoperador.model.request.CreateOrdenRequest;
import com.unir.msoperador.service.OrdenesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrdenesController {
	private final OrdenesService service;

	@GetMapping("/ordenes")
	public ResponseEntity<List<Orden>> getOrdenes(@RequestHeader Map<String, String> headers) {

		log.info("headers: {}", headers);
		List<Orden> ordenes = service.getOrdenes();

		if (ordenes != null) {
			return ResponseEntity.ok(ordenes);
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}
	
	@GetMapping("/ordenes/{ordenId}")
	public ResponseEntity<Orden> getOrden(@PathVariable long ordenId) {

		log.info("Request received for product {}", ordenId);
		Orden orden = service.getOrden(ordenId);

		if (orden != null) {
			return ResponseEntity.ok(orden);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@DeleteMapping("/ordenes/{ordenId}")
	public ResponseEntity<Void> deleteOrden(@PathVariable long ordenId) {

		Boolean removed = service.removeOrden(ordenId);

		if (Boolean.TRUE.equals(removed)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/ordenes")
	public ResponseEntity<Orden> createOrden(@RequestBody CreateOrdenRequest request) {

		Orden createdOrden = service.createOrden(request);

		if (createdOrden != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdOrden);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
}
