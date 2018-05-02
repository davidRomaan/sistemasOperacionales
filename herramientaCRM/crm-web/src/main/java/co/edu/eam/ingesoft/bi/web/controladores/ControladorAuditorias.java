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

	private List<AuditoriaProducto> productosListar;

	private List<AuditoriaUsuario> usuariosListar;
	
	private List<AuditoriaInventario> inventariosListar;
	
	private List<AuditoriaPersona> personasListar;
	
	private List<AuditoriaDetalleVenta> detalleVentasListar;
	
	private List<AuditoriaArea> areasListar;
	
	private List<AuditoriaConexion> conexionesListar;
	
	private List<AuditoriaTipoUsuario> tipoUsuariosListar;
	
	private List<AuditoriaFacturaVenta> facturaVentasListar;
	
	private List<AuditoriaPermisos> permisosListar;
	
	private List list;

	@PostConstruct
	public void listares() {
		auditorias = Arrays.asList(ClasesAuditorias.values());

	}

	public void listarAusitorias() {

		if (tipoAuditorias.equals("AuditoriaProducto")) {

			List<AuditoriaProducto> producto = per.listaaudi();
			list = producto;
			list  = new ArrayList();

		}

		if (tipoAuditorias.equals("AuditoriaInventario")) {
			
			List<AuditoriaInventario> inventario = inventarioEJB.listaAuditoria();
			list = inventario;
			list  = new ArrayList();
			
		}

		if (tipoAuditorias.equals("AuditoriaUsuario")) {
			
			List<AuditoriaUsuario> usuario = audiEJB.listAudiUsu();
			list = usuario;
			list  = new ArrayList();
		}

		if (tipoAuditorias.equals("AuditoriaPersona")) {
			
			List<AuditoriaPersona> persona = audiEJB.listAudiPer();
			list = persona;
			list  = new ArrayList();

		}
		
		if (tipoAuditorias.equals("AuditoriaDetalleVenta")) {
			
			List<AuditoriaDetalleVenta> detalleVenta = detalleVentaEJB.listaAuditoria();
			list = detalleVenta;
			list  = new ArrayList();

		}
		if (tipoAuditorias.equals("AuditoriaArea")) {
			
			List<AuditoriaArea> area = areaEJB.listaAuditoria();
			list = area;
			list  = new ArrayList();

		}

		if (tipoAuditorias.equals("AuditoriaConexiones")) {
			
			List<AuditoriaConexion> conexion = conexionEJB.listAudi();
			list = conexion;
			list  = new ArrayList();

		}

		if (tipoAuditorias.equals("AuditoriaTiposUsuarios")) {
			
			List<AuditoriaTipoUsuario> tipoUsuario = tipoUsuarioEJB.listAudi();
			list = tipoUsuario;
			list  = new ArrayList();

		}

		if (tipoAuditorias.equals("AuditoriaFacturaVenta")) {
			
			List<AuditoriaFacturaVenta> facturaVenta = facturaVentaEJB.listAudi();
			list = facturaVenta;
			list  = new ArrayList();

		}

		if (tipoAuditorias.equals("AuditoriaPermisos")) {
			
			List<AuditoriaPermisos> permisos = permisoEJB.listAudi();
			list = permisos;
			list  = new ArrayList();

		}

	}
	
	
	
	

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public List<AuditoriaProducto> getProductosListar() {
		return productosListar;
	}

	public void setProductosListar(List<AuditoriaProducto> productosListar) {
		this.productosListar = productosListar;
	}

	public List<AuditoriaUsuario> getUsuariosListar() {
		return usuariosListar;
	}

	public void setUsuariosListar(List<AuditoriaUsuario> usuariosListar) {
		this.usuariosListar = usuariosListar;
	}

	public List<AuditoriaInventario> getInvetariosListar() {
		return inventariosListar;
	}

	public void setInvetariosListar(List<AuditoriaInventario> invetariosListar) {
		this.inventariosListar = invetariosListar;
	}

	public List<AuditoriaPersona> getPersonasListar() {
		return personasListar;
	}

	public void setPersonasListar(List<AuditoriaPersona> personasListar) {
		this.personasListar = personasListar;
	}

	public List<AuditoriaDetalleVenta> getDetalleVentasListar() {
		return detalleVentasListar;
	}

	public void setDetalleVentasListar(List<AuditoriaDetalleVenta> detalleVentasListar) {
		this.detalleVentasListar = detalleVentasListar;
	}

	public List<AuditoriaArea> getAreasListar() {
		return areasListar;
	}

	public void setAreasListar(List<AuditoriaArea> areasListar) {
		this.areasListar = areasListar;
	}

	public List<AuditoriaConexion> getConexionesListar() {
		return conexionesListar;
	}

	public void setConexionesListar(List<AuditoriaConexion> conexionesListar) {
		this.conexionesListar = conexionesListar;
	}

	public List<AuditoriaTipoUsuario> getTipoUsuariosListar() {
		return tipoUsuariosListar;
	}

	public void setTipoUsuariosListar(List<AuditoriaTipoUsuario> tipoUsuariosListar) {
		this.tipoUsuariosListar = tipoUsuariosListar;
	}

	public List<AuditoriaFacturaVenta> getFacturaVentasListar() {
		return facturaVentasListar;
	}

	public void setFacturaVentasListar(List<AuditoriaFacturaVenta> facturaVentasListar) {
		this.facturaVentasListar = facturaVentasListar;
	}

	public List<AuditoriaPermisos> getPermisosListar() {
		return permisosListar;
	}

	public void setPermisosListar(List<AuditoriaPermisos> permisosListar) {
		this.permisosListar = permisosListar;
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

	public List<AuditoriaProducto> getProductoListar() {
		return productosListar;
	}

	public void setProductoListar(List<AuditoriaProducto> productoListar) {
		this.productosListar = productoListar;
	}

}
