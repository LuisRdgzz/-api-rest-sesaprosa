package com.rodriguez.inventorysales.controller;

import com.rodriguez.inventorysales.dto.request.ProductoRequest;
import com.rodriguez.inventorysales.dto.response.ProductoResponse;
import com.rodriguez.inventorysales.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ProductoResponse> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Long categoriaId,
            Pageable pageable) {
        return service.listar(nombre, precioMin, precioMax, categoriaId, pageable);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProductoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}