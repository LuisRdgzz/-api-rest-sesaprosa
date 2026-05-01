package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.CategoriaRequest;
import com.rodriguez.inventorysales.dto.response.CategoriaResponse;
import com.rodriguez.inventorysales.entity.Categoria;
import com.rodriguez.inventorysales.exception.ResourceNotFoundException;
import com.rodriguez.inventorysales.mapper.CategoriaMapper;
import com.rodriguez.inventorysales.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;

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
        log.info("La categoria se esta creando: {}", req.getNombre());
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
        log.info("Se esta eliminado la categoria con  id={}", id);
        Categoria c = buscar(id);
        categoriaRepository.delete(c);
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No ha sido encontrada la caetgoria con id=" + id));
    }
}