package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AreasEmpresaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;

@Named("controladorAreas")
@SessionScoped
public class ControladorAreasEmpresa implements Serializable {

	private int codigo;

	private String nombre;

	private String descripcion;

	private List<Area> areasEmpresa;

	private List<Area> listaNueva;
	
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	public void postconstructor() {
		listarAreas();
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

			Area a = new Area(codigo, nombre, descripcion);
			try {
				areas.registrarAreas(a);
				
				try {
					
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					
					auditoriaEJB.crearAuditoriaArea(a, "Registrar", browserDetail);
				
				}catch (Exception e) {
					e.printStackTrace();
				}
				Messages.addFlashGlobalInfo("Registro exitoso");
				listarAreas();
				codigo = 0;
				nombre = "";
				descripcion = "";
			} catch (ExcepcionNegocio e) {
				Messages.addFlashGlobalError(e.getMessage());
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
				

			} else {
				Messages.addFlashGlobalError("No existe esta area de la empresa");

			}
		}

	}

	public void eliminarArea(Area a) {
		areas.eliminarArea(a);
		listarAreas();
		Messages.addFlashGlobalInfo("se elimino correctamente");
		
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
