package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ModuloEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Modulo;
import co.edu.eam.ingesoft.bi.presistencia.entidades.ModulosUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;

@Named("controladorModulos")
@SessionScoped
public class ControladorModulos implements Serializable {

	//Modulos
	private int moduloSeleccionado;
	private List<Modulo> modulos;
	
	//Tipos de usuario
	private int tipoUsuarioSeleccionado;
	private String accion;
	
	private List<TipoUsuario> tiposUsuario;
	
	@EJB
	private TipoUsuarioEJB tipoUsuarioEJB;
	
	@EJB
	private ModuloEJB moduloEJB;
	
	@Inject
	private ControladorSesion sesion;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;
	
	@PostConstruct
	private void cargarDatos(){
		tiposUsuario = tipoUsuarioEJB.listar();
		modulos = moduloEJB.listar();
	}
	
	public void agregar(){
		Modulo m = moduloEJB.buscar(moduloSeleccionado);
		TipoUsuario tu = tipoUsuarioEJB.buscarID(tipoUsuarioSeleccionado);
		
		ModulosUsuario mu = new ModulosUsuario();
		mu.setModulo_id(m);
		mu.setTipoUsiario_id(tu);
		
		try {
			moduloEJB.registrarModuloUsuario(mu);
			
			accion = "Registrar Modulo";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaModulo", accion,
					"modulo registrado: " + m.getNombre(), sesion.getUser().getCedula(),
					browserDetail);
			
			Messages.addFlashGlobalInfo("Se ha agregado el m�dulo al tipo de usuario seleccionado");
			
		} catch (ExcepcionNegocio e){
			Messages.addFlashGlobalError(e.getMessage());
		}
		
	}
	
	public int getModuloSeleccionado() {
		return moduloSeleccionado;
	}
	public void setModuloSeleccionado(int moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}
	public List<Modulo> getModulos() {
		return modulos;
	}
	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	public int getTipoUsuarioSeleccionado() {
		return tipoUsuarioSeleccionado;
	}
	public void setTipoUsuarioSeleccionado(int tipoUsuarioSeleccionado) {
		this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
	}
	public List<TipoUsuario> getTiposUsuario() {
		return tiposUsuario;
	}
	public void setTiposUsuario(List<TipoUsuario> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}
	
	
	
}
