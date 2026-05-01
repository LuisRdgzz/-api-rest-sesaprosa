package com.rodriguez.inventorysales.repository;

import com.rodriguez.inventorysales.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {}