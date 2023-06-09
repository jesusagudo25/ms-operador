package com.unir.msoperador.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unir.msoperador.data.OrdenRepository;
import com.unir.msoperador.facade.ProductoFacade;
import com.unir.msoperador.facade.ProductoProveedorFacade;
import com.unir.msoperador.model.pojo.Orden;
import com.unir.msoperador.model.request.CreateOrdenRequest;
import com.unir.msoperador.model.request.CreateProductoProveedorRequest;
import com.unir.msoperador.model.request.CreateProductoRequest;

@Service
public class OrdenesServiceImpl implements OrdenesService{
	@Autowired
	private OrdenRepository repository;
	
	@Autowired
	private ProductoProveedorFacade ppfacade;
	
	@Autowired
	private ProductoFacade pfacade;

	@Override
	public List<Orden> getOrdenes() {

		List<Orden> ordenes = repository.findAll();
		return ordenes.isEmpty() ? null : ordenes;
	}

	@Override
	public Orden getOrden(long ordenId) {
		return repository.findById(Long.valueOf(ordenId)).orElse(null);
	}

	@Override
	public Boolean removeOrden(long ordenId) {

		Orden orden = repository.findById(Long.valueOf(ordenId)).orElse(null);

		if (orden != null) {
			repository.delete(orden);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Orden createOrden(CreateOrdenRequest request) {

		if (request != null) {
			ResponseEntity<CreateProductoProveedorRequest> productoProveedorResponse = ppfacade.getProducto(request.getProductoProveedorId());

			if (productoProveedorResponse.getStatusCode().equals(HttpStatus.OK)) {

				CreateProductoProveedorRequest productoProveedor = productoProveedorResponse.getBody();
				
				if(productoProveedor.getCantidad() >= request.getCantidad()) {
					Orden orden = Orden.builder().productoProveedorId(request.getProductoProveedorId()).proveedorId(request.getProveedorId())
							.cantidad(request.getCantidad()).fecha(request.getFecha()).total(request.getTotal()).build();
					
					int nuevaCantidad = productoProveedor.getCantidad() - request.getCantidad();
					ppfacade.updateProductoCantidad(request.getProductoProveedorId(), nuevaCantidad);
					
					ResponseEntity<CreateProductoRequest> productoResponse = pfacade.getProductoCodigo(productoProveedor.getCodigo());
					
					if (productoResponse.getStatusCode().equals(HttpStatus.OK)) {
						CreateProductoRequest producto = productoResponse.getBody();
						
						if(producto != null) {
							nuevaCantidad = producto.getCantidad() + request.getCantidad();
							pfacade.updateProductoCantidad(producto.getId(), nuevaCantidad);
						}
						else {
							pfacade.createProducto(productoProveedor.getNombre(), productoProveedor.getCodigo(), productoProveedor.getPrecio(), request.getCantidad());
						}
					}
					
					return repository.save(orden);
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
