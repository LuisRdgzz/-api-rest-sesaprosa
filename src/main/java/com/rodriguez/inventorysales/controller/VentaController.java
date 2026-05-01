package com.rodriguez.inventorysales.controller;

import com.rodriguez.inventorysales.dto.request.VentaRequest;
import com.rodriguez.inventorysales.dto.response.VentaResponse;
import com.rodriguez.inventorysales.service.VentaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ventas")
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponse registrar(@Valid @RequestBody VentaRequest req) {
        return service.registrarVenta(req);
    }

    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable Long id) { return service.obtener(id); }
}