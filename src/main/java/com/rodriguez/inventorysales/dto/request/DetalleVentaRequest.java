package com.rodriguez.inventorysales.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetalleVentaRequest {

    @NotNull
    private Long productoId;

    @NotNull @Min(1)
    private Integer cantidad;

    public DetalleVentaRequest() {}

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}