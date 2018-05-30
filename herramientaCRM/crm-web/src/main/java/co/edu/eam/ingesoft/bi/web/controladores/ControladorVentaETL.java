package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocio.etl.ETLVentasEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@SessionScoped
@Named("controladorVentaETL")
public class ControladorVentaETL implements Serializable {

	private String tipoCarga;
	private String fechaInicio;
	private String fechaFin;
	private String base;
	private List<HechoVentas> hechosVenta;

	private int fechaSeleccionada;
	private String fechaRolling;
	
	private String accion;
	
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	private boolean datosPostgresCargados;
	private boolean datosMysqlCargados;

	// Para identificar si se seleccion� la carga como tipo rolling
	private boolean rollingSeleccionado;

	// EJB
	@EJB
	private ETLVentasEJB etlVentasEJB;

	@EJB
	private VentaEJB ventaEJB;
	
	@Inject
	private ControladorSesion sesion;

	@PostConstruct
	private void inicializarCampos() {
		hechosVenta = new ArrayList<HechoVentas>();
		datosPostgresCargados = false;
		datosMysqlCargados = false;
	}

	private void reload() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gestionarCarga() {
		if (tipoCarga.equalsIgnoreCase("rolling")) {
			rollingSeleccionado = true;
		} else {
			rollingSeleccionado = false;
		}
		reload();
	}

	/**
	 * Verifica si la tabla de hechos de venta esta llena
	 * 
	 * @return true si lo esta, de lo contrario false
	 */
	public boolean isTablaLlena() {
		return hechosVenta.size() != 0;
	}

	/**
	 * Carga los datos
	 */
	public void extraer() {

		int bd;

		if (base.equalsIgnoreCase("mysql")) {
			bd = 1;
		} else {
			bd = 2;
		}

		if (bd == 1 && datosMysqlCargados) {

			Messages.addFlashGlobalError("Ya se cargaron los datos de la base de datos de MYSQL");

		} else if (bd == 2 && datosPostgresCargados) {

			Messages.addFlashGlobalError("Ya se cargaron los datos de la base de datos de POSTGRES");

		} else {

			if (rollingSeleccionado) {

				if (fechaSeleccionada == 0) {

					Messages.addFlashGlobalError("Debe seleccionar una opci�n");

				} else if (fechaSeleccionada == 1) {

					try {
						hechosVenta = etlVentasEJB.obtenerHechoVentasRollingDia(fechaRolling, bd, hechosVenta);
						
						accion = "Extraer";
						String browserDetail = Faces.getRequest().getHeader("User-Agent");
						auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos dia", sesion.getUser().getCedula(),
								browserDetail);
						
						cargaRealizada(bd);
					} catch (ExcepcionNegocio e) {
						// TODO: handle exception
						Messages.addFlashGlobalError(e.getMessage());
						cargaNoRealizada(bd);
					}

					reload();

				} else if (fechaSeleccionada == 2) {

					try{
					hechosVenta = etlVentasEJB.obtnerHechoVentasRollingMes(fechaRolling, bd, hechosVenta);
					cargaRealizada(bd);
					
					accion = "Extraer";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos Mes", sesion.getUser().getCedula(),
							browserDetail);
					
					} catch (ExcepcionNegocio e) {
						// TODO: handle exception
						Messages.addFlashGlobalError(e.getMessage());
						cargaNoRealizada(bd);
					}
					
					reload();
					
				} else {
					
					try{
						hechosVenta = etlVentasEJB.obtnerHechoVentasRollingAnio(fechaRolling, bd, hechosVenta);
						cargaRealizada(bd);
						
						accion = "Extraer";
						String browserDetail = Faces.getRequest().getHeader("User-Agent");
						auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos año", sesion.getUser().getCedula(),
								browserDetail);
						
					}catch (ExcepcionNegocio e) {
						// TODO: handle exception
						Messages.addFlashGlobalError(e.getMessage());
						cargaNoRealizada(bd);
					}
					
					reload();
					
				}

			} else {

				try {
					hechosVenta = etlVentasEJB.obtenerDatosHechoVentasAcumulacionSimple(fechaInicio, fechaFin, bd,
							hechosVenta);
					Messages.addFlashGlobalInfo("Datos cargados exitosamente");
					cargaRealizada(bd);
					
					accion = "Extraer";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos Acumalacion simple", sesion.getUser().getCedula(),
							browserDetail);
					
				} catch (ExcepcionNegocio e) {
					// TODO: handle exception
					Messages.addFlashGlobalError(e.getMessage());
					cargaNoRealizada(bd);
				}

				reload();
			}

		}

	}

	private void cargaRealizada(int bd) {
		if (bd == 1) {
			datosMysqlCargados = true;
		} else {
			datosPostgresCargados = true;
		}
	}

	private void cargaNoRealizada(int bd) {
		if (bd == 1) {
			datosMysqlCargados = false;
		} else {
			datosPostgresCargados = false;
		}
	}

	public void eliminar(HechoVentas hecho) {
		hechosVenta.remove(hecho);
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			String columna = event.getColumn().getHeaderText();
			verificarCambio(event.getRowIndex(), columna, newValue);
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			reload();
		}
	}

	/**
	 * Verifica el cambio que se realiz� y realiza el cambio en todas los datos
	 * relacionados
	 * 
	 * @param posicion
	 * @param columna
	 * @param newValue
	 */
	private void verificarCambio(int posicion, String columna, Object newValue) {

		HechoVentas hecho = hechosVenta.get(posicion);

		if (columna.equalsIgnoreCase("precio")) {

			int idProducto = hecho.getProducto().getId();

			double total = 0;

			for (HechoVentas hechoVentas : hechosVenta) {

				if (hechoVentas.getProducto().getId() == idProducto) {

					double precioNuevo = (Double) newValue;

					hechoVentas.getProducto().setPrecio(precioNuevo);

					double subtotal = precioNuevo * hechoVentas.getUnidades();

					hechoVentas.setSubtotal(subtotal);

					total += subtotal;

				}

			}

		} else {

			String cedula = hecho.getPersona().getCedula();

			for (HechoVentas hechoVentas : hechosVenta) {

				if (hechoVentas.getPersona().getCedula().equals(cedula)) {

					short edad = (Short) newValue;

					hechoVentas.getPersona().setEdad(edad);

				}

			}

		}

	}
	
	/**
	 * Transforma la lista de hechos 
	 */
	public void transformar(){
		
		if (hechosVenta.size() == 0){
			
			Messages.addFlashGlobalError("La tabla esta vac�a");
			
		} else {
			
			hechosVenta = etlVentasEJB.transformarDatos(hechosVenta);
			
			Messages.addFlashGlobalInfo("Datos transformados existosamente");
			
		}
		
	}

	/**
	 * Carga los datos al data warehouse
	 */
	public void cargar() {

		if (hechosVenta.size() == 0) {

			Messages.addFlashGlobalError("No hay datos para cargar");

		} else {
			
			if (rollingSeleccionado){
				
				etlVentasEJB.limpiarBDOracle();
				
			}

			try {
				etlVentasEJB.cargarDatosDWH(hechosVenta);
				Messages.addFlashGlobalInfo("Se han cargado los datos exitosamente");
				vaciarTabla();
			} catch (ExcepcionNegocio e) {
				Messages.addFlashGlobalError(e.getMessage());
			}

		}

	}

	/**
	 * Vac�a la tabla de hechos de ventas
	 */
	public void vaciarTabla() {

		hechosVenta = new ArrayList<HechoVentas>();
		datosMysqlCargados = false;
		datosPostgresCargados = false;

		reload();

	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isRollingSeleccionado() {
		return rollingSeleccionado;
	}

	public void setRollingSeleccionado(boolean rollingSeleccionado) {
		this.rollingSeleccionado = rollingSeleccionado;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public List<HechoVentas> getHechosVenta() {
		return hechosVenta;
	}

	public void setHechosVenta(List<HechoVentas> hechosVenta) {
		this.hechosVenta = hechosVenta;
	}

	public int getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	public void setFechaSeleccionada(int fechaSeleccionada) {
		this.fechaSeleccionada = fechaSeleccionada;
	}

	public String getFechaRolling() {
		return fechaRolling;
	}

	public void setFechaRolling(String fechaRolling) {
		this.fechaRolling = fechaRolling;
	}

}
