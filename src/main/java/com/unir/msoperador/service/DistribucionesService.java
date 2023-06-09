package com.unir.msoperador.service;

import java.util.List;

import com.unir.msoperador.model.pojo.Distribucion;
import com.unir.msoperador.model.request.CreateDistribucionRequest;

public interface DistribucionesService {
	List<Distribucion> getDistribuciones();
	
	Distribucion getDistribucion(long distribucionId);
	
	Boolean removeDistribucion(long distribucionId);
	
	Distribucion createDistribucion(CreateDistribucionRequest request);
}
