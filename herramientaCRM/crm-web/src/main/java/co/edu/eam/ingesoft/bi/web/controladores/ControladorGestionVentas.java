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

	@EJB
	private VentaEJB ventasEJB;

	@PostConstruct
	private void constructor() {
		Calendar fechaActual = new GregorianCalendar();
		int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
		int mes = fechaActual.get(Calendar.MONTH);
		int anio = fechaActual.get(Calendar.YEAR);

		String nuevaFecha = dia + "/" + mes + "/" + anio;
		facturas = ventasEJB.listarFacturasPorFecha(nuevaFecha);
	}

	/**
	 * Lista las facturas por la fecha ingresada
	 */
	public void listarFacturasFecha() {
		facturas = ventasEJB.listarFacturasPorFecha(fechaCompra);
		if (facturas.size() == 0){
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
		detallesVenta = ventasEJB.listarDetallesVentaFactura(factura);
	}
	
	public void eliminar(FacturaVenta fv){
		ventasEJB.eliminarVenta(fv);
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
