package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Calendar;

public class HechoAuditoria implements Serializable {

	private String accion;
	private String dispositivo;
	private String navegador;
	private Calendar fecha;
	private DimensionUsuario usuario;

	public HechoAuditoria() {
		// TODO Auto-generated constructor stub
	}

	public HechoAuditoria(String accion, String dispositivo, String navegador, Calendar fecha,
			DimensionUsuario usuario) {
		super();
		this.accion = accion;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.fecha = fecha;
		this.usuario = usuario;
	}

	public String getFechaHora() {

		return this.fecha.get(Calendar.YEAR) + "/" + this.fecha.get(Calendar.MONTH) + 1 +"/"
				+ this.fecha.get(Calendar.DAY_OF_MONTH);
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public DimensionUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(DimensionUsuario usuario) {
		this.usuario = usuario;
	}

}
