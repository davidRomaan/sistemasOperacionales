package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaCargoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.CargoEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;

@Named("controladorCargo")
@SessionScoped
public class ControladorCargo implements Serializable {

	private int codigo;

	private String nombre;

	private double salario;
	
	private String accion;

	private List<Cargo> cargosEmpresa;

	@EJB
	private CargoEJB cargoEJB;
	
	@EJB
	private AuditoriaCargoEJB audiCargoEJB;

	@PostConstruct
	public void postconstructor() {
		listarCargos();
	}

	public void listarCargos() {
		cargosEmpresa = cargoEJB.listarCargos();
	}

	public void registrarCargo() {

		if (codigo == 0 || nombre.equals("") || salario == 0) {
			Messages.addFlashGlobalInfo("Ingrese todos los campos");
		} else {
			Cargo c = cargoEJB.buscarCargo(codigo);
			if (c == null) {
				Cargo cargo = new Cargo(codigo, nombre, salario);
				cargoEJB.registrarCargo(cargo);
				
				Messages.addFlashGlobalInfo("Registro exitoso");
				
				listarCargos();
				try {

					accion = "Registrar Cargo";

					String browserDetail = Faces.getRequest().getHeader("User-Agent");

					audiCargoEJB.crearAuditoriaCargo(cargo.getDescripcion(), accion, browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}

				codigo = 0;
				nombre = "";
				salario = 0;
			} else {
				Messages.addFlashGlobalInfo("este cargo ya existe");
			}
		}

	}

	public void buscarCargo() {

		if (codigo == 0) {
			Messages.addFlashGlobalInfo("Ingrese todos los campos");
		} else {
			Cargo c = cargoEJB.buscarCargo(codigo);
			if (c != null) {
				
				try {

					accion = "Buscar Cargo";

					String browserDetail = Faces.getRequest().getHeader("User-Agent");

					audiCargoEJB.crearAuditoriaCargo(c.getDescripcion(), accion, browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				codigo = c.getId();
				nombre = c.getDescripcion();
				salario = c.getSalario();
			} else {
				Messages.addFlashGlobalInfo("este cargo no existe");
			}
		}
	}

	public void editarCargo() {

		if (codigo == 0) {
			Messages.addFlashGlobalInfo("Ingrese el codigo para buscar el cargo");
		} else {
			Cargo c = cargoEJB.buscarCargo(codigo);
			if (c != null) {

				Cargo cargo = new Cargo(codigo, nombre, salario);
				cargoEJB.editarCargo(cargo);
				listarCargos();
				Messages.addFlashGlobalInfo("se edito exitosamente");
				try {

					accion = "Editar Cargo";

					String browserDetail = Faces.getRequest().getHeader("User-Agent");

					audiCargoEJB.crearAuditoriaCargo(cargo.getDescripcion(), accion, browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Messages.addFlashGlobalError("No existe este cargo");

			}
		}
	}
	
	public void eliminarCargo(Cargo c){
		
		cargoEJB.eliminarCargo(c);
		listarCargos();
		Messages.addFlashGlobalError("se elimino correctamente");
		try {

			accion = "Eliminar Cargo";

			String browserDetail = Faces.getRequest().getHeader("User-Agent");

			audiCargoEJB.crearAuditoriaCargo(c.getDescripcion(), accion, browserDetail);

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

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public List<Cargo> getCargosEmpresa() {
		return cargosEmpresa;
	}

	public void setCargosEmpresa(List<Cargo> cargosEmpresa) {
		this.cargosEmpresa = cargosEmpresa;
	}

}
