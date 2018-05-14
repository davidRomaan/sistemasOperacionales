package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocio.etl.ETLVentasEJB;

@SessionScoped
@Named("controladorDWH")
public class ControladorDWH implements Serializable {

	private String tipoCarga;
	private String fechaInicio;
	private String fechaFin;
	
	//Para identificar si se seleccionó la carga como tipo rolling
	private boolean rollingSeleccionado;
	
	//EJB
	@EJB
	private ETLVentasEJB etlVentasEJB;
	
	@EJB
	private VentaEJB ventaEJB;
	
	private void reload() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void gestionarCarga(){
		if (tipoCarga.equalsIgnoreCase("rolling")){
			rollingSeleccionado = true;
		} else {
			rollingSeleccionado = false;
		}
		reload();
	}
	
	public void cargar(){
				
		Calendar fecha1 = ventaEJB.convertirFechaStrintADate(fechaInicio);
		Calendar fecha2 = ventaEJB.convertirFechaStrintADate(fechaFin);
		
		etlVentasEJB.listaVentasPeriodo(fecha1, fecha2);
		
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
	
}
