package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.CategoriaRequest;
import com.rodriguez.inventorysales.dto.response.CategoriaResponse;
import com.rodriguez.inventorysales.entity.Categoria;
import com.rodriguez.inventorysales.exception.ResourceNotFoundException;
import com.rodriguez.inventorysales.mapper.CategoriaMapper;
import com.rodriguez.inventorysales.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest req) {
        log.info("Creando categoría: {}", req.getNombre());
        Categoria c = Categoria.builder()
                .nombre(req.getNombre())
                .descripcion(req.getDescripcion())
                .build();
        return mapper.toResponse(categoriaRepository.save(c));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest req) {
        Categoria c = buscar(id);
        c.setNombre(req.getNombre());
        c.setDescripcion(req.getDescripcion());
        return mapper.toResponse(categoriaRepository.save(c));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando categoría id={}", id);
        Categoria c = buscar(id);
        categoriaRepository.delete(c);
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id=" + id));
    }
}