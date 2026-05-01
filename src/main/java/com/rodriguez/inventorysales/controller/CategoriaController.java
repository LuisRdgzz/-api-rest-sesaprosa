package com.rodriguez.inventorysales.controller;

import com.rodriguez.inventorysales.dto.request.CategoriaRequest;
import com.rodriguez.inventorysales.dto.response.CategoriaResponse;
import com.rodriguez.inventorysales.service.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorías")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public CategoriaResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse crear(@Valid @RequestBody CategoriaRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(@PathVariable Long id,
                                        @Valid @RequestBody CategoriaRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}