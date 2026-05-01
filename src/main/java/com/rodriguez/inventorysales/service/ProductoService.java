package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.ProductoRequest;
import com.rodriguez.inventorysales.dto.response.ProductoResponse;
import com.rodriguez.inventorysales.entity.Categoria;
import com.rodriguez.inventorysales.entity.Producto;
import com.rodriguez.inventorysales.exception.ResourceNotFoundException;
import com.rodriguez.inventorysales.mapper.ProductoMapper;
import com.rodriguez.inventorysales.repository.CategoriaRepository;
import com.rodriguez.inventorysales.repository.ProductoRepository;
import com.rodriguez.inventorysales.specification.ProductoSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper mapper;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           ProductoMapper mapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponse> listar(String nombre, BigDecimal precioMin,
                                         BigDecimal precioMax, Long categoriaId,
                                         Pageable pageable) {
        Specification<Producto> spec = Specification
                .where(ProductoSpecification.conNombre(nombre))
                .and(ProductoSpecification.conPrecioMinimo(precioMin))
                .and(ProductoSpecification.conPrecioMaximo(precioMax))
                .and(ProductoSpecification.conCategoria(categoriaId));

        return productoRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest req) {
        log.info("Creando producto sku={}", req.getSku());
        if (productoRepository.existsBySku(req.getSku()))
            throw new IllegalArgumentException("SKU ya existe: " + req.getSku());

        Categoria cat = categoriaRepository.findById(req.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada id=" + req.getCategoriaId()));

        Producto p = Producto.builder()
                .sku(req.getSku())
                .nombre(req.getNombre())
                .precio(req.getPrecio())
                .stockActual(req.getStockActual())
                .categoria(cat)
                .build();

        return mapper.toResponse(productoRepository.save(p));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        Producto p = buscar(id);
        Categoria cat = categoriaRepository.findById(req.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada id=" + req.getCategoriaId()));

        p.setSku(req.getSku());
        p.setNombre(req.getNombre());
        p.setPrecio(req.getPrecio());
        p.setStockActual(req.getStockActual());
        p.setCategoria(cat);
        return mapper.toResponse(productoRepository.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto p = buscar(id);
        productoRepository.delete(p);
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado id=" + id));
    }
}