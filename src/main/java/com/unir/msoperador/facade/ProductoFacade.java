package com.unir.msoperador.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unir.msoperador.model.request.CreateProductoRequest;
import org.springframework.http.HttpEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoFacade {
	
	private final RestTemplate restTemplate;
	
	@Value("${unir.app.buscador.producto.get.url}")
	private String getProductoUrl;
	
	@Value("${unir.app.buscador.producto.codigo.url}")
	private String getProductoCodigoUrl;
	
	@Value("${unir.app.buscador.producto.update.url}")
	private String updateProductoUrl;
	
	@Value("${unir.app.buscador.producto.create.url}")
	private String createProductoUrl;
	
	//Se utiliza para consultar un determinado producto en el ms-buscador
	public ResponseEntity<CreateProductoRequest> getProducto(long productoId) {		
		try {
			return restTemplate.getForEntity(String.format(getProductoUrl, productoId), CreateProductoRequest.class);
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), productoId);
			return ResponseEntity.badRequest().build();
		}
	}
	
	//Se utiliza para consultar los productos en el ms-buscador por codigo
	public ResponseEntity<List<CreateProductoRequest>> getProductoCodigo(String codigo) {		
		try {
			return restTemplate.exchange(String.format(getProductoCodigoUrl, codigo), HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<List<CreateProductoRequest>>() {});
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product with CODE {}", e.getStatusCode(), codigo);
			return ResponseEntity.badRequest().build();
		}
	}
	
	//Se utiliza para actualizar la cantidad existente del producto en inventario
	public ResponseEntity<CreateProductoRequest> updateProductoCantidad(long productoId, Integer nuevaCantidad) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			ObjectMapper objectMapper = new ObjectMapper();
		    ObjectNode requestBody = objectMapper.createObjectNode();
		    requestBody.put("nuevaCantidad", nuevaCantidad);
		    
		    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
			
			return restTemplate.exchange(String.format(updateProductoUrl, productoId), HttpMethod.PATCH, requestEntity, CreateProductoRequest.class );
			
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), productoId);
			return ResponseEntity.badRequest().build();
		}
	}
	
	//Se utiliza para crear un producto en inventario
	public ResponseEntity<CreateProductoRequest> createProducto(String nombre, String codigo, Double precio, int cantidad) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			ObjectMapper objectMapper = new ObjectMapper();
		    ObjectNode requestBody = objectMapper.createObjectNode();
		    requestBody.put("nombre", nombre);
		    requestBody.put("codigo", codigo);
		    requestBody.put("precio", precio);
		    requestBody.put("cantidad", cantidad);
		    
		    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
			
			return restTemplate.exchange(String.format(createProductoUrl), HttpMethod.POST, requestEntity, CreateProductoRequest.class);
			
		} catch (HttpClientErrorException e) {
			log.error("Client Error: {}, Product{}", e.getStatusCode());
			return ResponseEntity.badRequest().build();
		}
	}

}
