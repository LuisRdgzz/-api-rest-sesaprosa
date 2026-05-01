package com.rodriguez.inventorysales.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

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

    public ProductoRequest() {}

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}