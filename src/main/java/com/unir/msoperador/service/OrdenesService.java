package com.unir.msoperador.service;

import java.util.List;

import com.unir.msoperador.model.pojo.Orden;
import com.unir.msoperador.model.request.CreateOrdenRequest;

public interface OrdenesService {
	List<Orden> getOrdenes();
	
	Orden getOrden(long ordenId);
	
	Boolean removeOrden(long ordenId);
	
	Orden createOrden(CreateOrdenRequest request);
	
}
