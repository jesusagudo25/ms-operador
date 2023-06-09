package com.unir.msoperador.data;


import org.springframework.data.jpa.repository.JpaRepository;

import com.unir.msoperador.model.pojo.Orden;


public interface OrdenRepository extends JpaRepository<Orden, Long> {

}
