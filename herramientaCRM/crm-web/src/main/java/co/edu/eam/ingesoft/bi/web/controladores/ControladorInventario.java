package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@SessionScoped
@Named("controladorInventario")
public class ControladorInventario implements Serializable {

	private String nombre;
	private String descripcion;
	private Inventario inventarioEditar;
	private List<Inventario> inventarios;
	private String accion;
	
	@EJB
	private ProductoEJB productoEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;
	
	@Inject
	private ControladorSesion sesion;
	
	@PostConstruct
	private void postConstruct(){
		refrescarListaInventarios();
	}
	
	private void refrescarListaInventarios(){
		inventarios = productoEJB.listarInventarios();
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
		
		try {
			accion = "Regitro Inventario";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaIventario", accion, "inventario creado: "+inventario.getNombre(), sesion.getUser().getCedula(), browserDetail);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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
			
			try {
				accion = "Editar Inventario";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				
				auditoriaEJB.crearAuditoria("AuditoriaIventario", accion, "inventario editado: "+nombre, sesion.getUser().getCedula(), browserDetail);
			
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			Messages.addFlashGlobalInfo("Se ha editado correctamente");
			limpiarCampos();
			
			refrescarListaInventarios();
			
			inventarioEditar = null;
			
			try {
				reload();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void eliminarInventario(Inventario inventario){	
		productoEJB.eliminarInventario(inventario);
		try {
			accion = "Eliminar Inventario";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			
			auditoriaEJB.crearAuditoria("AuditoriaInventario", accion, "inventario eliminado: "+inventario.getNombre(), sesion.getUser().getCedula(), browserDetail);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		productoEJB.eliminarInventario(inventario); 
		refrescarListaInventarios();
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
