package com.unir.msoperador.facade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unir.msoperador.model.request.CreateProductoProveedorRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoProveedorFacade {
	
	private final RestTemplate restTemplate;
	
	@Value("${unir.app.buscador.productoproveedor.get.url}")
	private String getProductoUrl;
	
	//Se utiliza para consultar un producto de un determinado proveedor en el ms-operador

	public ResponseEntity<CreateProductoProveedorRequest> getProducto(long proveedorId, long productoId) {
		try {
			return restTemplate.getForEntity(String.format(getProductoUrl, proveedorId, productoId), CreateProductoProveedorRequest.class);
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), productoId);
			return ResponseEntity.badRequest().build();
		}
	}
	
	//Se utiliza para actualizar la cantidad existente del producto -> proveedor
	
	public ResponseEntity<CreateProductoProveedorRequest> updateProductoCantidad(long proveedorId, long productoId, Integer nuevaCantidad) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			ObjectMapper objectMapper = new ObjectMapper();
		    ObjectNode requestBody = objectMapper.createObjectNode();
		    requestBody.put("nuevaCantidad", nuevaCantidad);
		    
		    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
			
			return restTemplate.exchange(String.format(getProductoUrl, proveedorId, productoId), HttpMethod.PATCH, requestEntity, CreateProductoProveedorRequest.class );
			
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), productoId);
			return ResponseEntity.badRequest().build();
		}
	}
}
