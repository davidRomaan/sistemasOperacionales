package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorTipoUsuario")
@SessionScoped
public class ControladorTipoUsuario implements Serializable {

	private String nombre;
	private String descripcion;
	private String accion;

	private List<TipoUsuario> tiposUsuario;
	private TipoUsuario tipoEditar;	
	private Usuario usuario;

	@EJB
	private TipoUsuarioEJB tipoUsuarioEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	private void cargarDatos() {
		usuario = Faces.getApplicationAttribute("usu");
		listarTipos();
	}

	public void registrar() {
		if (!validarCamposVacios()) {
			Messages.addFlashGlobalError("Debe ingresar todos los campos");
		} else {
			TipoUsuario tu = new TipoUsuario(nombre.toUpperCase(), descripcion);
			try {
				tipoUsuarioEJB.registrar(tu);
				listarTipos();
				Messages.addFlashGlobalInfo("Registro exitoso");
				try {
					
					accion = "Registrar TipoUsuario";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaTiposUsuarios", accion, "TU creado: " + tu.getNombre(), usuario.getNombre(), browserDetail);

				} catch (ExcepcionNegocio e) {
					e.getMessage();
				}
				limpiarCampos();
				tipoEditar = null;
			} catch (ExcepcionNegocio e) {
				// TODO: handle exception
				Messages.addFlashGlobalError(e.getMessage());
			}
		}
	}

	/**
	 * Lista los tipos de usuario registrados
	 */
	public void listarTipos() {
		tiposUsuario = tipoUsuarioEJB.listar();
	}

	/**
	 * Elimina un tipo de usuario
	 * 
	 * @param tu
	 *            tipo de usuario a eliminar
	 */
	public void eliminarTipo(TipoUsuario tu) {
		tipoUsuarioEJB.eliminar(tu);
		listarTipos();
		try {
			accion = "Eliminar TipoUsuario";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaTiposUsuarios", accion, "TU eliminado: " + tu.getNombre(), usuario.getNombre(), browserDetail);

		} catch (ExcepcionNegocio e) {
			e.getMessage();
		}
	}

	/**
	 * Edita un tipo de usuario
	 */
	public void editarTipo() {
		if (!validarCamposVacios()){
			Messages.addFlashGlobalError("Debe ingresar todos los campos");
		} else {
			tipoEditar.setNombre(nombre.toUpperCase());
			tipoEditar.setDescripcion(descripcion);
			tipoUsuarioEJB.editar(tipoEditar);
			listarTipos();
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			tipoEditar = null;
			limpiarCampos();
			try {
				accion = "Editar TipoUsuario";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaTiposUsuarios", accion, "TU editado: " + nombre, usuario.getNombre(), browserDetail);

			} catch (ExcepcionNegocio e) {
				e.getMessage();
			}
		}
	}
	
	/**
	 * Lismpia los campos
	 */
	private void limpiarCampos(){
		nombre = "";
		descripcion = "";
	}
	
	/**
	 * Habilita la opci�n de editar tipo de usuario
	 * @param tu tipo de usuario a editar
	 */
	public void habilitarEdicion(TipoUsuario tu){
		tipoEditar = tu;
		nombre = tipoEditar.getNombre();
		descripcion = tipoEditar.getDescripcion();
		try{
			reload();
		}catch (IOException e){
			
		}
	}
	
	private void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	/**
	 * Verifica si se esta editando un tipo de usuario
	 * @return true si se esta editando, de lo contrario false
	 */
	public boolean isEditando(){
		return tipoEditar != null;
	}

	/**
	 * Validas si los campos nombre y descripcion est�n vacios
	 * @return true si no lo est�n, de lo contrario false
	 */
	private boolean validarCamposVacios() {
		if (nombre.isEmpty() && descripcion.isEmpty()) {
			return false;
		}
		return true;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<TipoUsuario> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<TipoUsuario> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

}
