package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import co.edu.eam.ingesoft.bi.negocio.etl.ETLWikiEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.RecentChanges;

@SessionScoped
@Named("controladorWikiETL")
public class ControladorWikiETL implements Serializable {
	
	private String tipoCarga;
	private String fechaInicio;
	private String fechaFin;
	private String base;
	private String accion;
	
	// Para identificar si se seleccion� la carga como tipo rolling
	private boolean rollingSeleccionado;
	private boolean datosPostgresCargados;
	private boolean datosMysqlCargados;
	
	@Inject
	private ControladorSesion sesion;
	
	@EJB
	private ETLWikiEJB wikiEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;
	
	private List<RecentChanges> recentChanges;
	
	@PostConstruct
	private void inicializarCampos() {
		recentChanges = new ArrayList<RecentChanges>();
		datosPostgresCargados = false;
		datosMysqlCargados = false;
	}
	
	public void obtenerDatosWiki(){
		
		
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
		return recentChanges.size() != 0;
		
	}
	
	/**
	 * Vac�a la tabla de hechos de ventas
	 */
	public void vaciarTabla() {

		recentChanges = new ArrayList<RecentChanges>();
		datosMysqlCargados = false;
		datosPostgresCargados = false;

		reload();

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

				wikiEJB.obtenerDatosWikiRolling();
				
				accion = "Extraer";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos dia", sesion.getUser().getCedula(),
						browserDetail);
				
				

			} else {

				try {
					
					wikiEJB.obtenerDatosWikiAcumulacionSimple(fechaInicio, fechaFin);
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
	
	/**
	 * Carga los datos al data warehouse
	 */
	public void cargar() {

		if (recentChanges.size() == 0) {

			Messages.addFlashGlobalError("No hay datos para cargar");

		} else {
			
			if (rollingSeleccionado){
				
				wikiEJB.limpiarBDOracle();
				
			}

			try {
				wikiEJB.cargarDatosDWH(recentChanges);
				Messages.addFlashGlobalInfo("Se han cargado los datos exitosamente");
				vaciarTabla();
			} catch (ExcepcionNegocio e) {
				Messages.addFlashGlobalError(e.getMessage());
			}

		}

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
		recentChanges.remove(hecho);
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

	public String getBase() {
		return base;
	}


	public void setBase(String base) {
		this.base = base;
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

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public boolean isDatosPostgresCargados() {
		return datosPostgresCargados;
	}

	public void setDatosPostgresCargados(boolean datosPostgresCargados) {
		this.datosPostgresCargados = datosPostgresCargados;
	}

	public boolean isDatosMysqlCargados() {
		return datosMysqlCargados;
	}

	public void setDatosMysqlCargados(boolean datosMysqlCargados) {
		this.datosMysqlCargados = datosMysqlCargados;
	}

	public ControladorSesion getSesion() {
		return sesion;
	}

	public void setSesion(ControladorSesion sesion) {
		this.sesion = sesion;
	}

	public List<RecentChanges> getRecentChanges() {
		return recentChanges;
	}

	public void setRecentChanges(List<RecentChanges> recentChanges) {
		this.recentChanges = recentChanges;
	}
	
	
	 

}
