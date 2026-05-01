package com.rodriguez.inventorysales.service;

import com.rodriguez.inventorysales.dto.request.DetalleVentaRequest;
import com.rodriguez.inventorysales.dto.request.VentaRequest;
import com.rodriguez.inventorysales.dto.response.VentaResponse;
import com.rodriguez.inventorysales.entity.DetalleVenta;
import com.rodriguez.inventorysales.entity.Producto;
import com.rodriguez.inventorysales.entity.Venta;
import com.rodriguez.inventorysales.exception.ConcurrencyConflictException;
import com.rodriguez.inventorysales.exception.InsufficientStockException;
import com.rodriguez.inventorysales.exception.ResourceNotFoundException;
import com.rodriguez.inventorysales.mapper.VentaMapper;
import com.rodriguez.inventorysales.repository.ProductoRepository;
import com.rodriguez.inventorysales.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    public VentaService(ProductoRepository productoRepository,
                        VentaRepository ventaRepository,
                        VentaMapper ventaMapper) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public VentaResponse registrarVenta(VentaRequest request) {
        log.info("Iniciando registro de venta con {} ítems", request.getDetalles().size());

        try {
            Venta venta = Venta.builder()
                    .fechaVenta(LocalDateTime.now())
                    .total(BigDecimal.ZERO)
                    .build();

            BigDecimal total = BigDecimal.ZERO;

            for (DetalleVentaRequest item : request.getDetalles()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado id=" + item.getProductoId()));

                if (producto.getStockActual() < item.getCantidad()) {
                    throw new InsufficientStockException(String.format(
                            "Stock insuficiente para producto '%s' (disponible: %d, solicitado: %d)",
                            producto.getNombre(),
                            producto.getStockActual(),
                            item.getCantidad()));
                }

                producto.setStockActual(producto.getStockActual() - item.getCantidad());
                productoRepository.save(producto);

                BigDecimal subtotal = producto.getPrecio()
                        .multiply(BigDecimal.valueOf(item.getCantidad()));
                total = total.add(subtotal);

                DetalleVenta detalle = DetalleVenta.builder()
                        .producto(producto)
                        .cantidad(item.getCantidad())
                        .precioUnitario(producto.getPrecio())
                        .build();

                venta.agregarDetalle(detalle);

                log.debug("Producto {} — descontadas {} unidades. Stock restante: {}",
                        producto.getSku(), item.getCantidad(), producto.getStockActual());
            }

            venta.setTotal(total);
            Venta guardada = ventaRepository.save(venta);

            log.info("Venta id={} registrada exitosamente. Total: {}",
                    guardada.getId(), guardada.getTotal());

            return ventaMapper.toResponse(guardada);

        } catch (OptimisticLockingFailureException ex) {
            log.warn("Conflicto de concurrencia detectado durante venta", ex);
            throw new ConcurrencyConflictException(
                    "Otro usuario modificó un producto al mismo tiempo. Reintenta la venta.");
        }
    }

    @Transactional(readOnly = true)
    public VentaResponse obtener(Long id) {
        Venta v = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venta no encontrada id=" + id));
        return ventaMapper.toResponse(v);
    }
}