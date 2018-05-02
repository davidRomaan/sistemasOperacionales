package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaAreaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaDetalleVentaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaFacturaVentaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaInventarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaPermisoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaPersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaTipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.ClasesAuditorias;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaArea;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaConexion;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaDetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaFacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaInventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaPermisos;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaTipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaUsuario;

@SessionScoped
@Named("controladorAuditoria")
public class ControladorAuditorias implements Serializable {

	private String tipoAuditorias;

	private List<ClasesAuditorias> auditorias;

	@EJB
	private PersonaEJB per;

	@EJB
	private AuditoriaPersonaEJB audiEJB;
	
	@EJB
	private AuditoriaInventarioEJB inventarioEJB;
	
	@EJB
	private AuditoriaDetalleVentaEJB detalleVentaEJB;
	
	@EJB
	private AuditoriaAreaEJB areaEJB;
	
	@EJB
	private AuditoriaConexionEJB conexionEJB;
	
	@EJB
	private AuditoriaTipoUsuarioEJB tipoUsuarioEJB;
	
	@EJB
	private AuditoriaFacturaVentaEJB facturaVentaEJB;
	
	@EJB
	private AuditoriaPermisoEJB permisoEJB;
	
	private List list;

	@PostConstruct
	public void listares() {
		auditorias = Arrays.asList(ClasesAuditorias.values());

	}

	public void listarAusitorias() {

		if (tipoAuditorias.equals("AuditoriaProducto")) {

			List<AuditoriaProducto> producto = per.listaaudi();
			list = producto;

		}

		if (tipoAuditorias.equals("AuditoriaInventario")) {
			
			List<AuditoriaInventario> inventario = inventarioEJB.listaAuditoria();
			list = inventario;
			
		}

		if (tipoAuditorias.equals("AuditoriaUsuario")) {
			
			List<AuditoriaUsuario> usuario = audiEJB.listAudiUsu();
			list = usuario;
		}

		if (tipoAuditorias.equals("AuditoriaPersona")) {
			
			List<AuditoriaPersona> persona = audiEJB.listAudiPer();
			list = persona;

		}
		
		if (tipoAuditorias.equals("AuditoriaDetalleVenta")) {
			
			List<AuditoriaDetalleVenta> detalleVenta = detalleVentaEJB.listaAuditoria();
			list = detalleVenta;

		}
		if (tipoAuditorias.equals("AuditoriaArea")) {
			
			List<AuditoriaArea> area = areaEJB.listaAuditoria();
			list = area;

		}

		if (tipoAuditorias.equals("AuditoriaConexiones")) {
			
			List<AuditoriaConexion> conexion = conexionEJB.listAudi();
			list = conexion;

		}

		if (tipoAuditorias.equals("AuditoriaTiposUsuarios")) {
			
			List<AuditoriaTipoUsuario> tipoUsuario = tipoUsuarioEJB.listAudi();
			list = tipoUsuario;

		}

		if (tipoAuditorias.equals("AuditoriaFacturaVenta")) {
			
			List<AuditoriaFacturaVenta> facturaVenta = facturaVentaEJB.listAudi();
			list = facturaVenta;

		}

		if (tipoAuditorias.equals("AuditoriaPermisos")) {
			
			List<AuditoriaPermisos> permisos = permisoEJB.listAudi();
			list = permisos;

		}

	}
	
	
	
	

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getTipoAuditorias() {
		return tipoAuditorias;
	}

	public void setTipoAuditorias(String tipoAuditorias) {
		this.tipoAuditorias = tipoAuditorias;
	}

	public List<ClasesAuditorias> getAuditorias() {
		return auditorias;
	}

	public void setAuditorias(List<ClasesAuditorias> auditorias) {
		this.auditorias = auditorias;
	}

	public PersonaEJB getPer() {
		return per;
	}

	public void setPer(PersonaEJB per) {
		this.per = per;
	}

}
