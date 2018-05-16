package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;

@SessionScoped
@Named("controladorAuditoriaETL")
public class ControladorAuditoriaETL implements Serializable {

	private String tipoCarga;

	private String fechaSeleccionada;

	private List<HechoAuditoria> listaHechoAct;

	private int baseDatos;
	@EJB
	AuditoriaEJB auditoriaEJB;
	
	@EJB
	private VentaEJB ventaEJB;

	// Para identificar si se seleccionó la carga como tipo rolling
	private boolean rollingSeleccionado;

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
		if (fechaSeleccionada.equals("0")) {
			Messages.addFlashGlobalInfo("Selecione una opcion");
		}
		if (fechaSeleccionada.equals("1")) {
			try {
				String fecha = ventaEJB.convertirCalendarAString(new GregorianCalendar());
				listaHechoAct = auditoriaEJB.listarFechaActualAuditoria(baseDatos,fecha);
			} catch (Exception e) {
				e.getMessage();
			}

		}
		if (fechaSeleccionada.equals("2")) {

		}
		if (fechaSeleccionada.equals("3")) {

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

}
