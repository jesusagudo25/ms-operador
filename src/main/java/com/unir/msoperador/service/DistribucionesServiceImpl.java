package com.unir.msoperador.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unir.msoperador.data.DistribucionRepository;
import com.unir.msoperador.facade.ProductoFacade;
import com.unir.msoperador.model.pojo.Distribucion;
import com.unir.msoperador.model.request.CreateDistribucionRequest;
import com.unir.msoperador.model.request.CreateProductoRequest;

@Service
public class DistribucionesServiceImpl implements DistribucionesService{
	
	@Autowired
	private DistribucionRepository repository;
	
	@Autowired
	private ProductoFacade facade;

	@Override
	public List<Distribucion> getDistribuciones() {

		List<Distribucion> distribuciones = repository.findAll();
		return distribuciones.isEmpty() ? null : distribuciones;
	}

	@Override
	public Distribucion getDistribucion(long distribucionId) {
		return repository.findById(Long.valueOf(distribucionId)).orElse(null);
	}

	@Override
	public Boolean removeDistribucion(long distribucionId) {

		Distribucion distribucion = repository.findById(Long.valueOf(distribucionId)).orElse(null);

		if (distribucion != null) {
			repository.delete(distribucion);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Distribucion createDistribucion(CreateDistribucionRequest request) {

		if (request != null) {
			
			//Se consulta con el ms-buscador el producto seleccionado
			ResponseEntity<CreateProductoRequest> productoResponse = facade.getProducto(request.getProductoId());

			if (productoResponse.getStatusCode().equals(HttpStatus.OK)) {

				CreateProductoRequest producto = productoResponse.getBody();
				
				//Se verifica si la cantidad seleccionada es menor que la existente en inventario
				if(producto.getCantidad() >= request.getCantidad()) {
					Distribucion distribucion = Distribucion.builder().sucursalId(request.getSucursalId()).productoId(request.getProductoId())
							.cantidad(request.getCantidad()).fecha(request.getFecha()).build();
					
					int nuevaCantidad = producto.getCantidad() - request.getCantidad();
					
					//Se actualiza la cantidad del producto seleccionado en inventario
					facade.updateProductoCantidad(request.getProductoId(), nuevaCantidad);
					return repository.save(distribucion);
				}
				else {
					return null;
				}
			} else {
				throw new IllegalArgumentException("Invalid producto received");
			}
		} else {
			return null;
		}
	}
}
