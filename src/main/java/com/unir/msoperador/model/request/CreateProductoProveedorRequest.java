package com.unir.msoperador.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductoProveedorRequest {
	private String nombre;
	private String codigo;
	private Double precio;
	private int cantidad;
}
