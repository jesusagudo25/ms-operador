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
			
			//Se consulta con el ms-buscador el producto del proveedor seleccionado
			ResponseEntity<CreateProductoProveedorRequest> productoProveedorResponse = ppfacade.getProducto(request.getProveedorId(), request.getProductoProveedorId());

			if (productoProveedorResponse.getStatusCode().equals(HttpStatus.OK)) {

				CreateProductoProveedorRequest productoProveedor = productoProveedorResponse.getBody();
				
				//Se verifica si la cantidad seleccionada es menor que la existente en inventario por el proveedor
				if(productoProveedor.getCantidad() >= request.getCantidad()) {
					Orden orden = Orden.builder().productoProveedorId(request.getProductoProveedorId()).proveedorId(request.getProveedorId())
							.cantidad(request.getCantidad()).fecha(request.getFecha()).total(request.getTotal()).build();
					
					int nuevaCantidad = productoProveedor.getCantidad() - request.getCantidad();
					
					//Se actualiza la cantidad del producto seleccionado en inventario
					ppfacade.updateProductoCantidad(request.getProveedorId(), request.getProductoProveedorId(), nuevaCantidad);
					
					//Se verifica si existe algun producto con ese codigo
					ResponseEntity<CreateProductoRequest> productoResponse = pfacade.getProducto(request.getProductoProveedorId());
					
					if (productoResponse.getStatusCode().equals(HttpStatus.OK)) {
						CreateProductoRequest producto = productoResponse.getBody();
						
						
						//Si es existe,se actualiza el estado. De lo contrario, se crea el nuevo producto.
						nuevaCantidad = producto.getCantidad() + request.getCantidad();
						pfacade.updateProductoCantidad(producto.getCodigo(), nuevaCantidad);
					}
					else {
						pfacade.createProducto(productoProveedor.getNombre(), productoProveedor.getCodigo(), productoProveedor.getPrecio(), request.getCantidad());
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
