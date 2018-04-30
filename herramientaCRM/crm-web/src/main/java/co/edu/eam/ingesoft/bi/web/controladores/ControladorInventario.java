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

import org.omnifaces.util.Messages;
import org.omnifaces.util.Messages.Message;

import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;

@SessionScoped
@Named("controladorInventario")
public class ControladorInventario implements Serializable {

	private String nombre;
	private String descripcion;
	private Inventario inventarioEditar;
	private List<Inventario> inventarios;
	
	@EJB
	private ProductoEJB productoEJB;
	
	@PostConstruct
	private void postConstruct(){
		refrescarListaInventarios();
	}
	
	private void refrescarListaInventarios(){
		inventarios = productoEJB.listarInventario();
	}
	
	public boolean isEditando(){
		return inventarioEditar != null;
	}
	
	public void registrar(){
		
		Inventario inventario = new Inventario();
		inventario.setDescripcion(descripcion);
		inventario.setNombre(nombre.toUpperCase());
		
		try{
		productoEJB.registrarInventario(inventario);
		
		Messages.addFlashGlobalInfo("Registro exitoso");
		limpiarCampos();
		
		refrescarListaInventarios();
		
		} catch (ExcepcionNegocio e){
			Messages.addFlashGlobalError(e.getMessage());
		}
		
	}
	
	public void habilitarEditar(Inventario inventario){
		inventarioEditar = inventario;
		
		nombre = inventario.getNombre();
		descripcion = inventario.getDescripcion();
		
		try{
			reload();
		}catch (IOException e){
			
		}
	}
	
	private void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	public void editar(){
		
		if (inventarioEditar != null){
						
			inventarioEditar.setDescripcion(descripcion);
			
			String nombreUpper = nombre.toUpperCase();
			
			if (!nombreUpper.equals(inventarioEditar.getNombre())){
				if (productoEJB.buscarNombre(nombreUpper) != null){
					Messages.addFlashGlobalError("Ya existe un inventario con este nombre");
				} else {
					inventarioEditar.setNombre(nombreUpper);
				}
			} else {
				inventarioEditar.setNombre(nombreUpper);
			}			
			
			productoEJB.editarInventario(inventarioEditar);
			
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			limpiarCampos();
			
			refrescarListaInventarios();
			
		}
		
	}
	
	public void eliminarInventario(Inventario inventario){
		productoEJB.eliminarInventario(inventario); 
	}
	
	private void limpiarCampos(){
		nombre = "";
		descripcion = "";
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

	public List<Inventario> getInventarios() {
		return inventarios;
	}

	public void setInventarios(List<Inventario> inventarios) {
		this.inventarios = inventarios;
	}
	
	
	
	
}
