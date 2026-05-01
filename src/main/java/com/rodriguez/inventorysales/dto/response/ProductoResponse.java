package com.rodriguez.inventorysales.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private Long id;
    private String sku;
    private String nombre;
    private BigDecimal precio;
    private Integer stockActual;
    private Long categoriaId;
    private String categoriaNombre;
}