package com.unir.msoperador.model.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Orden {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "productoproveedor_id")
	private String productoProveedorId;
	
	@Column(name = "proveedor_id")
	private String proveedorId;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@Column(name = "fecha")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date fecha;
	
	@Column(name = "total")
	private Double total;
}
