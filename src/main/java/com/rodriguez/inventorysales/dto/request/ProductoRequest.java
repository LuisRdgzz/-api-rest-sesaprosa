package com.rodriguez.inventorysales.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoRequest {
    @NotBlank @Size(max = 50)
    private String sku;

    @NotBlank @Size(max = 200)
    private String nombre;

    @NotNull @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precio;

    @NotNull @Min(0)
    private Integer stockActual;

    @NotNull
    private Long categoriaId;
}