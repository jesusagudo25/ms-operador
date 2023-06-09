package com.unir.msoperador.model.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDistribucionRequest {
	private Long sucursalId;
	private Long productoId;
	private int cantidad;
	private Date fecha;
}
