package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;

@Named("controladorTipoUsuario")
@SessionScoped
public class ControladorTipoUsuario implements Serializable {

	private String nombre;
	private String descripcion;

	private List<TipoUsuario> tiposUsuario;
	private TipoUsuario tipoEditar;	

	@EJB
	private TipoUsuarioEJB tipoUsuarioEJB;

	@PostConstruct
	private void cargarDatos() {
		listarTipos();
	}

	public void registrar() {
		if (!validarCamposVacios()) {
			Messages.addFlashGlobalError("Debe ingresar todos los campos");
		} else {
			TipoUsuario tu = new TipoUsuario(nombre, descripcion);
			try {
				tipoUsuarioEJB.registrar(tu);
				listarTipos();
				Messages.addFlashGlobalInfo("Registro exitoso");
				tiposUsuario.add(tu);
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
		tiposUsuario.remove(tu);
		tipoUsuarioEJB.eliminar(tu);
		listarTipos();
	}

	/**
	 * Edita un tipo de usuario
	 */
	public void editarTipo() {
		if (!validarCamposVacios()){
			Messages.addFlashGlobalError("Debe ingresar todos los campos");
		} else {
			tiposUsuario.remove(tipoEditar);
			tipoEditar.setNombre(nombre);
			tipoEditar.setDescripcion(descripcion);
			tipoUsuarioEJB.editar(tipoEditar);
			listarTipos();
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			tiposUsuario.add(tipoEditar);
			tipoEditar = null;
			nombre = "";
			descripcion = "";
		}
	}
	
	/**
	 * Habilita la opción de editar tipo de usuario
	 * @param tu tipo de usuario a editar
	 */
	public void habilitarEdicion(TipoUsuario tu){
		tipoEditar = tu;
		nombre = tipoEditar.getNombre();
		descripcion = tipoEditar.getDescripcion();
	}
	
	/**
	 * Verifica si se esta editando un tipo de usuario
	 * @return true si se esta editando, de lo contrario false
	 */
	public boolean isEditando(){
		return tipoEditar != null;
	}

	/**
	 * Validas si los campos nombre y descripcion están vacios
	 * @return true si no lo están, de lo contrario false
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
