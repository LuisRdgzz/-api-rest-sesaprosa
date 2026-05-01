package com.rodriguez.inventorysales.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class VentaRequest {

    @NotEmpty
    @Valid
    private List<DetalleVentaRequest> detalles;

    public VentaRequest() {}

    public List<DetalleVentaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequest> detalles) { this.detalles = detalles; }
}