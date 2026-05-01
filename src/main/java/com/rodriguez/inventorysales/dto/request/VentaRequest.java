package com.rodriguez.inventorysales.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class VentaRequest {
    @NotEmpty
    @Valid
    private List<DetalleVentaRequest> detalles;
}