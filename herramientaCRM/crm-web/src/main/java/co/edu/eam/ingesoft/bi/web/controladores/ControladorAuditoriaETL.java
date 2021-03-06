package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.annotation.PostConstruct;
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
import co.edu.eam.ingesoft.bi.negocio.etl.ETLAuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;

@SessionScoped
@Named("controladorAuditoriaETL")
public class ControladorAuditoriaETL implements Serializable {

	private String tipoCarga;
	private String fechaSeleccionada;
	private String fechaCampo;
	private String fechaCampo2;

	public String getFechaCampo2() {
		return fechaCampo2;
	}

	public void setFechaCampo2(String fechaCampo2) {
		this.fechaCampo2 = fechaCampo2;
	}

	private List<HechoAuditoria> listaHechoAct;

	private int baseDatos;

	private String fechaInicio;
	private String fechaFin;
	private String accion;

	private String base;

	private boolean datosPostgresCargados;
	private boolean datosMysqlCargados;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@EJB
	private ETLAuditoriaEJB auditoriaETL;

	@EJB
	private VentaEJB ventaEJB;
	
	@Inject
	private ControladorSesion sesion;

	@PostConstruct
	private void inicializarCampos() {
		listaHechoAct = new ArrayList<HechoAuditoria>();
		datosPostgresCargados = false;
		datosMysqlCargados = false;
	}

	// Para identificar si se seleccion� la carga como tipo rolling
	private boolean rollingSeleccionado;

	private boolean semanaSeleccionada;

	private void reload() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void extraerDatos() {

		if (baseDatos == 1 && datosMysqlCargados) {

			Messages.addFlashGlobalError("Ya se cargaron los datos de la base de datos de POSTGRES ");

		} else if (baseDatos == 2 && datosPostgresCargados) {

			Messages.addFlashGlobalError("Ya se cargaron los datos de la base de datos de MYSQL");

		} else {

			if (fechaSeleccionada.equals("0")) {
				Messages.addFlashGlobalInfo("Selecione una opcion");
			}
			if (fechaSeleccionada.equals("1")) {
				try {
					//listaHechoAct = new ArrayList<HechoAuditoria>();
					listaHechoAct = auditoriaEJB.listarFechaActualAuditoria(baseDatos, fechaCampo);
					cargaRealizada(baseDatos);
					
					accion = "Extraer Datos Rolling";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos dia", sesion.getUser().getCedula(), browserDetail);
					
				} catch (Exception e) {
					Messages.addFlashGlobalError(e.getMessage());
					cargaNoRealizada(baseDatos);
				}

			}
			if (fechaSeleccionada.equals("2")) {


				try {
					//listaHechoAct = new ArrayList<HechoAuditoria>();
					listaHechoAct = auditoriaETL.obtnerHechoAuditoriaRollingMes(fechaCampo, baseDatos, listaHechoAct);
					cargaRealizada(baseDatos);
					
					accion = "Extraer Datos Rolling";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos mes", sesion.getUser().getCedula(), browserDetail);

				} catch (ExcepcionNegocio e) {
					// TODO: handle exception
					Messages.addFlashGlobalError(e.getMessage());
					cargaNoRealizada(baseDatos);
				}

				reload();

			}
			if (fechaSeleccionada.equals("3")) {

				try {
					listaHechoAct = new ArrayList<HechoAuditoria>();
					listaHechoAct = auditoriaETL.obtnerHechoAuditoriasRollingAnio(fechaCampo, baseDatos, listaHechoAct);
					cargaRealizada(baseDatos);
					
					accion = "Extraer Datos Rolling";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos año", sesion.getUser().getCedula(), browserDetail);
					
				} catch (ExcepcionNegocio e) {
					// TODO: handle exception
					Messages.addFlashGlobalError(e.getMessage());
					cargaNoRealizada(baseDatos);
				}

				reload();

			}
		}
		reload();
	}

	public void cargarDat() {

		auditoriaETL.limpiarTablasDWH();
		auditoriaETL.cargarDatosDWH(listaHechoAct);
		Messages.addFlashGlobalInfo("se cargo correctamente");
		
		reload();

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

	/**
	 * Carga los datos
	 */
	public void extraer() {

		Calendar fecha1 = auditoriaEJB.convertirFechaStrintADate(fechaInicio);
		Calendar fecha2 = auditoriaEJB.convertirFechaStrintADate(fechaFin);

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

			try {

				//listaHechoAct = new ArrayList<HechoAuditoria>();
				listaHechoAct = auditoriaETL.obtenerDatosHechoVentasAcumulacionSimple(fecha1, fecha2, bd,
						listaHechoAct);
				
				accion = "Extraer Datos AC";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Extraer Datos", sesion.getUser().getCedula(), browserDetail);

				Messages.addFlashGlobalInfo("Datos cargados exitosamente");
				if (bd == 1) {
					datosMysqlCargados = true;
				} else {
					datosPostgresCargados = true;
				}
			} catch (ExcepcionNegocio e) {
				// TODO: handle exception
				Messages.addFlashGlobalError(e.getMessage());
				if (bd == 1) {
					datosMysqlCargados = false;
				} else {
					datosPostgresCargados = false;
				}
			}

			reload();
		}

	}

	/**
	 * Carga los datos al data warehouse
	 */
	public void cargar() {

		if (listaHechoAct.size() == 0) {

			Messages.addFlashGlobalError("No hay datos para cargar");

		} else {

			try {
				auditoriaETL.cargarDatosDWH(listaHechoAct);
				
				accion = "Cargar Datos";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Cargar Datos", sesion.getUser().getCedula(), browserDetail);
				
				Messages.addFlashGlobalInfo("Se han cargado los datos exitosamente");
				vaciarTabla();
			} catch (ExcepcionNegocio e) {
				Messages.addFlashGlobalError(e.getMessage());
			}

		}

	}

	/**
	 * Verifica el cambio que se realiz� y realiza el cambio en todas los
	 * datos relacionados
	 * 
	 * @param posicion
	 * @param columna
	 * @param newValue
	 */
	private void verificarCambio(int posicion, String columna, Object newValue) {

		HechoAuditoria hecho = listaHechoAct.get(posicion);

		if (columna.equalsIgnoreCase("tipo_usuario")) {

			String cedula = hecho.getUsuario().getCedula();

			for (HechoAuditoria hechoAudi : listaHechoAct) {

				if (hechoAudi.getUsuario().getCedula().equals(cedula)) {

					String tipoUsuario = (String) newValue;

					hechoAudi.getUsuario().setTipoUsuario(tipoUsuario);

				}

			}

		} else {

			String cedula = hecho.getUsuario().getCedula();

			for (HechoAuditoria hechoAudi : listaHechoAct) {

				if (hechoAudi.getUsuario().getCedula().equals(cedula)) {

					int edad = (Integer) newValue;

					hechoAudi.getUsuario().setEdad(edad);

				}

			}

		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			verificarCambio(event.getRowIndex(), event.getColumn().getHeaderText(), newValue);
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			reload();
		}
	}

	public void gestionarComboFecha() {
		if (fechaSeleccionada.equals("1")) {
			semanaSeleccionada = false;
		}
		if (fechaSeleccionada.equalsIgnoreCase("2")) {
			semanaSeleccionada = true;
		}
		if (fechaSeleccionada.equalsIgnoreCase("3")) {
			semanaSeleccionada = true;
		}
		reload();
	}

	/**
	 * Vac�a la tabla de hechos de ventas
	 */
	public void vaciarTabla() {

		listaHechoAct = new ArrayList<HechoAuditoria>();
		datosMysqlCargados = false;
		datosPostgresCargados = false;

		reload();

	}

	public void gestionarCarga() {
		if (tipoCarga.equalsIgnoreCase("rolling")) {
			rollingSeleccionado = true;
			vaciarTabla();
		} else {
			rollingSeleccionado = false;
		}
		reload();
	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public boolean isRollingSeleccionado() {
		return rollingSeleccionado;
	}

	public void setRollingSeleccionado(boolean rollingSeleccionado) {
		this.rollingSeleccionado = rollingSeleccionado;
	}

	public String getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	public void setFechaSeleccionada(String fechaSeleccionada) {
		this.fechaSeleccionada = fechaSeleccionada;
	}

	public List<HechoAuditoria> getListaHechoAct() {
		return listaHechoAct;
	}

	public void setListaHechoAct(List<HechoAuditoria> listaHechoAct) {
		this.listaHechoAct = listaHechoAct;
	}

	public int getBaseDatos() {
		return baseDatos;
	}

	public void setBaseDatos(int baseDatos) {
		this.baseDatos = baseDatos;
	}

	public String getFechaCampo() {
		return fechaCampo;
	}

	public void setFechaCampo(String fechaCampo) {
		this.fechaCampo = fechaCampo;
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

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
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

	public AuditoriaEJB getAuditoriaEJB() {
		return auditoriaEJB;
	}

	public void setAuditoriaEJB(AuditoriaEJB auditoriaEJB) {
		this.auditoriaEJB = auditoriaEJB;
	}

	public ETLAuditoriaEJB getAuditoriaETL() {
		return auditoriaETL;
	}

	public void setAuditoriaETL(ETLAuditoriaEJB auditoriaETL) {
		this.auditoriaETL = auditoriaETL;
	}

	public VentaEJB getVentaEJB() {
		return ventaEJB;
	}

	public void setVentaEJB(VentaEJB ventaEJB) {
		this.ventaEJB = ventaEJB;
	}

	public boolean isSemanaSeleccionada() {
		return semanaSeleccionada;
	}

	public void setSemanaSeleccionada(boolean semanaSeleccionada) {
		this.semanaSeleccionada = semanaSeleccionada;
	}

}
