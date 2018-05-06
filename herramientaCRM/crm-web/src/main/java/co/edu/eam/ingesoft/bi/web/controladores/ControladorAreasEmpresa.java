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

import co.edu.eam.ingesoft.bi.negocio.beans.AreasEmpresaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorAreas")
@SessionScoped
public class ControladorAreasEmpresa implements Serializable {

	private int codigo;

	private String nombre;

	private String descripcion;
	
	private Usuario usuario;

	private List<Area> areasEmpresa;

	private List<Area> listaNueva;

	private AuditoriaEJB auditoriaEJB;

	private String accion;

	@PostConstruct
	public void postconstructor() {
		listarAreas();
		usuario = Faces.getApplicationAttribute("usu");
	}

	public void listarAreas() {
		areasEmpresa = areas.listarAreas();
	}

	@EJB
	private AreasEmpresaEJB areas;

	public void registrarArea() {

		if (codigo == 0 || nombre.equals("") || descripcion.equals("")) {
			Messages.addFlashGlobalInfo("Ingrese todos los campos");
		} else {

			Area a = new Area();
			a.setNombre(nombre);
			a.setDescripcion(descripcion);
			a.setId(codigo);

			try {
				areas.registrarAreas(a);

				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Exitoso", "el area se ha registrado"));
				listarAreas();
				

				accion = "Registrar Area";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaArea", accion, "area creada: " + a.getNombre(), usuario.getNombre(), browserDetail);
				
				codigo = 0;
				nombre = "";
				descripcion = "";
			} catch (ExcepcionNegocio e) {
				Messages.addFlashGlobalError(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void buscarArea() {
		if (codigo == 0) {
			Messages.addFlashGlobalInfo("Ingrese el codigo para buscar el area");
		} else {

			Area a = areas.buscarArea(codigo);
			if (a != null) {
				nombre = a.getNombre();
				descripcion = a.getDescripcion();
				
				try {
					accion = "Buscar Area";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaArea", accion, "area buscada: " + a.getNombre(), usuario.getNombre(), browserDetail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Messages.addFlashGlobalError("No existe esta area de la empresa");

			}

		}
	}

	public void editarArea() {

		if (codigo == 0) {
			Messages.addFlashGlobalInfo("Ingrese el codigo para buscar el area");
		} else {

			Area a = areas.buscarArea(codigo);
			if (a != null) {

				Area area = new Area(codigo, nombre, descripcion);
				areas.editarArea(area);
				listarAreas();
				Messages.addFlashGlobalInfo("se edito exitosamente");
				try {
					accion = "Editar Area";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaArea", accion, "area editada: " + a.getNombre(), usuario.getNombre(), browserDetail);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Messages.addFlashGlobalError("No existe esta area de la empresa");

			}
		}

	}

	public void eliminarArea(Area a) {
		areas.eliminarArea(a);
		listarAreas();
		Messages.addFlashGlobalInfo("se elimino correctamente");
		try {
			accion = "Eliminar Area";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaArea", accion, "area eliminada: " + a.getNombre(), usuario.getNombre(), browserDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
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

	public List<Area> getAreasEmpresa() {
		return areasEmpresa;
	}

	public void setAreasEmpresa(List<Area> areasEmpresa) {
		this.areasEmpresa = areasEmpresa;
	}

	public List<Area> getListaNueva() {
		return listaNueva;
	}

	public void setListaNueva(List<Area> listaNueva) {
		this.listaNueva = listaNueva;
	}

}
