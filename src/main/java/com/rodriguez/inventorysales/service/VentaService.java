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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaService {

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public VentaResponse registrarVenta(VentaRequest request) {
        log.info("Se esta iniciando el registro de venta con {} ítems", request.getDetalles().size());

        try {
            Venta venta = Venta.builder()
                    .fechaVenta(LocalDateTime.now())
                    .total(BigDecimal.ZERO)
                    .build();

            BigDecimal total = BigDecimal.ZERO;

            for (DetalleVentaRequest item : request.getDetalles()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No ha sido encontrado el producto con id =" + item.getProductoId()));


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

            log.info("Venta id={} fue registrada exitosamente. Total: {}",
                    guardada.getId(), guardada.getTotal());

            return ventaMapper.toResponse(guardada);

        } catch (OptimisticLockingFailureException ex) {
            log.warn("Se ha detectado un conflicto de concurrencia detectado durante venta", ex);
            throw new ConcurrencyConflictException(
                    "otro usuario modifico un producto al mismo tiempo. Reintenta la venta.");
        }
    }

    @Transactional(readOnly = true)
    public VentaResponse obtener(Long id) {
        Venta v = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No ha sido encontrada la venta con id=" + id));
        return ventaMapper.toResponse(v);
    }
}