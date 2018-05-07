package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoProductoEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorTipoProd")
@SessionScoped
public class ControladorTipoProducto implements Serializable {

	private int codigo;

	private String descripcion;

	private String nombre;

	private List<TipoProducto> tipoProducto;

	private List<TipoProducto> listaNueva;
	
	private String accion;
	
	private Usuario usuario;

	@EJB
	private TipoProductoEJB tipoEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	public void postconstructor() {
		usuario = Faces.getApplicationAttribute("user");
		listarTipos();
	}

	public void listarTipos() {
		tipoProducto = tipoEJB.listarTipos();
	}

	public void registrar() {

		if (codigo == 0 || nombre.isEmpty() || descripcion.isEmpty()) {
			Messages.addFlashGlobalInfo("ingrese todos los campos");
		} else {
			TipoProducto tipo = tipoEJB.buscar(codigo);

			if (tipo == null) {

				TipoProducto tip = new TipoProducto();
				tip.setId(codigo);
				tip.setNombre(nombre);
				tip.setDescripcion(descripcion);
				
				System.out.println(nombre);
				
				try {

					accion = "Registrar TipoProducto";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaTipoProducto", accion, "TP creado: " + nombre, "", browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}

				tipoEJB.registrarTipoProd(tip);
				listarTipos();
				
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Exitoso", "se ha registrado correctamente"));

				codigo = 0;
				nombre = "";
				descripcion = "";
			} else {

				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("este tipo no existe"));
			}
		}
	}

	public void buscarTipo() {

		if (codigo == 0) {
			System.out.println("----------------------" + codigo);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"complete el campo codigo para realizar la busqueda", null));
		} else {
			TipoProducto tipo = tipoEJB.buscar(codigo);

			if (tipo == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "no se ha encontrado el registro", null));
			} else {
				try {

					accion = "Buscar TipoProducto";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaTipoProducto", accion, "TP buscado: " + tipo.getNombre(), "", browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}
				nombre = tipo.getNombre();
				descripcion = tipo.getDescripcion();
			}
		}
	}

	public void editarTipos() {

		if (codigo == 0) {
			Messages.addFlashGlobalInfo("busque el tipo que quiere editar");
		} else {

			TipoProducto t = tipoEJB.buscar(codigo);
			if (t != null) {

				TipoProducto tipo = new TipoProducto();
				tipo.setId(codigo);
				tipo.setNombre(nombre);
				tipo.setDescripcion(descripcion);
				tipoEJB.editarTipoProducto(tipo);
				listarTipos();
				Messages.addFlashGlobalInfo("se edito exitosamente");
				try {

					accion = "Editar TipoProducto";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaTipoProducto", accion, "TP editado: " + nombre, "", browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Messages.addFlashGlobalError("No existe esta area de la empresa");

			}
		}

	}

	public void eliminarTipo(TipoProducto t) {
		tipoEJB.eliminarTipoProd(t);
		Messages.addFlashGlobalInfo("se elimino correctamente");
		try {

			accion = "Eliminar TipoProducto";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaTipoProducto", accion, "TP eliminado: " + t.getNombre(), "", browserDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
		listarTipos();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<TipoProducto> getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(List<TipoProducto> tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public List<TipoProducto> getListaNueva() {
		return listaNueva;
	}

	public void setListaNueva(List<TipoProducto> listaNueva) {
		this.listaNueva = listaNueva;
	}

}
