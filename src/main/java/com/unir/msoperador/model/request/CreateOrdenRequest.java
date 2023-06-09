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
public class CreateOrdenRequest {
	private Long productoProveedorId;
	private Long proveedorId;
	private int cantidad;
	private Date fecha;
	private Double total;
}
