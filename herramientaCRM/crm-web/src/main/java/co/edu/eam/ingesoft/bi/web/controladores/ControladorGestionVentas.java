package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;

@SessionScoped
@Named("controladorGestionVentas")
public class ControladorGestionVentas implements Serializable {

	private String fechaCompra;
	private List<FacturaVenta> facturas;
	private List<DetalleVenta> detallesVenta;
	private FacturaVenta facturaSeleccionada;

	@EJB
	private VentaEJB ventasEJB;

	@PostConstruct
	private void constructor() {
		facturas = ventasEJB.listarFacturasPorFecha(ventasEJB.obtenerFechaActual());
	}

	/**
	 * Lista las facturas por la fecha ingresada
	 */
	public void listarFacturasFecha() {
		facturas = ventasEJB.listarFacturasPorFecha(fechaCompra);
		if (facturas.size() == 0) {
			Messages.addFlashGlobalError("No hay registros de esta fecha");
		}
	}

	/**
	 * Lista los detalles de venta de una factura
	 * 
	 * @param factura
	 *            la factura
	 */
	public void listarDetallesVentaFactura(FacturaVenta factura) {
		facturaSeleccionada = factura;
		detallesVenta = ventasEJB.listarDetallesVentaFactura(factura);
		if (detallesVenta.size() == 0) {
			Messages.addFlashGlobalWarn("Los detalles de venta de esta factura han sido eliminados");
		}
	}

	public void eliminarDetalleVenta(DetalleVenta dv) {
		ventasEJB.eliminarDetalleVenta(dv);
		detallesVenta = ventasEJB.listarDetallesVentaFactura(facturaSeleccionada);
	}

	/**
	 * Elimina todos los detalles de venta una factura
	 */
	public void eliminarTodosDetallesVenta() {
		if (facturaSeleccionada == null) {
			Messages.addFlashGlobalError("Debe listar los detalles de una factura");
		} else {
			if (detallesVenta.size() == 0) {
				Messages.addFlashGlobalError("No existen detalles de venta de la factura seleccionada");
			} else {
				for (DetalleVenta detalleVenta : detallesVenta) {
					ventasEJB.eliminarDetalleVenta(detalleVenta);
				}
				Messages.addFlashGlobalInfo("Se han eliminado todos los detalles de esta venta");
				detallesVenta = ventasEJB.listarDetallesVentaFactura(facturaSeleccionada);
				facturaSeleccionada = null;
			}
		}
	}

	public void eliminar(FacturaVenta fv) {
		try {
			String fechaVenta = fv.getFechaVenta();
			ventasEJB.eliminarVenta(fv);
			facturas = ventasEJB.listarFacturasPorFecha(fechaVenta);
		} catch (Exception e) {
			// TODO: handle exception
			Messages.addFlashGlobalWarn("Antes de eliminar esta factura debe eliminar todos sus detalles de venta");
		}
	}

	public String getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(String fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public List<FacturaVenta> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<FacturaVenta> facturas) {
		this.facturas = facturas;
	}

	public List<DetalleVenta> getDetallesVenta() {
		return detallesVenta;
	}

	public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
		this.detallesVenta = detallesVenta;
	}

}
