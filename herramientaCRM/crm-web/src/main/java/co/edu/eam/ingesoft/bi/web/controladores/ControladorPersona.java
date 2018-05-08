package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AreasEmpresaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.CargoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.MunicipioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorPersonas")
@SessionScoped
public class ControladorPersona implements Serializable {

	private String cedula;

	private String nombre;

	private String apellido;

	private String telefono;

	private String correo;

	private String fechaNacimiento;

	private Genero tipoGenero;

	private int municipioSeleccinado;

	private int departamentoSelccionado;

	private String tipoUsuarioSeleccionado;

	private Persona personaBuscada;

	private Municipio municipioBuscado;

	private String contrasenia;

	private String nombreUsuario;

	private int areaSeleccionada;

	private int cargoSeleccionado;

	private String fechaIngreso;

	private int deptoSeleccionado;

	private String accion;
	
	private Usuario usuario;
	
	@Inject
	private ControladorSesion sesion;

	@EJB
	private PersonaEJB personaEJB;

	@EJB
	private MunicipioEJB municipioEJB;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private AreasEmpresaEJB areasEJB;

	@EJB
	private CargoEJB cargoEJB;

	@EJB
	private TipoUsuarioEJB tipoEJB;

	private List<Municipio> municipios;

	private List<Genero> generos;

	private List<Departamento> departamentos;

	private List<Area> areas;

	private List<Cargo> cargos;

	private List<TipoUsuario> tiposUsu;

	@EJB
	private DepartamentoEJB departamentoEJB;
	
	@EJB
	private ConexionEJB conexionEJB;

	@PostConstruct
	public void listar() {
		
		conexionEJB.ultimaBD();
		
		usuario = Faces.getApplicationAttribute("user");
		generos = Arrays.asList(Genero.values());
		listarDepartamentos();
		municipios = departamentoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());

		accion = "registrar";
		areas = areasEJB.listarAreas();
		cargos = cargoEJB.listarCargos();
		tiposUsu = tipoEJB.listar();
	
	}

	public void listarMunicipios() {
		
		municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);

	}

	/**
	 * Lista los departamentos registrados
	 */
	private void listarDepartamentos() {
		departamentos = departamentoEJB.listarDepartamentos();
	}

	public void generarClave() {

		String clave = usuarioEJB.generarClave();
		contrasenia = clave;
	}

	public void registrar() {

		if (cedula.isEmpty() || apellido.isEmpty() || correo.isEmpty() || nombre.isEmpty() || telefono.isEmpty()
				|| contrasenia.isEmpty() || nombreUsuario.isEmpty()) {
			Messages.addFlashGlobalInfo("ingrese todos los campos");

		} else {

			Usuario us = usuarioEJB.buscarUsu(cedula);
			Municipio m = municipioEJB.buscar(municipioSeleccinado);
			Area a = areasEJB.buscarArea(areaSeleccionada);
			Cargo c = cargoEJB.buscarCargo(cargoSeleccionado);
			TipoUsuario tip = tipoEJB.buscar(tipoUsuarioSeleccionado);

			if (us == null) {

				Usuario usu = new Usuario();
				usu.setCedula(cedula);
				usu.setApellido(apellido);
				usu.setCorreo(correo);
				usu.setFechaNacimiento(fechaNacimiento);
				usu.setGenero(tipoGenero);
				usu.setNombre(nombre);
				usu.setTelefono(telefono);
				usu.setMunicipio(m);

				usu.setContrasenia(contrasenia);
				usu.setFechaIngreso(fechaIngreso);
				usu.setNombreUsuario(nombreUsuario);
				usu.setCedula(cedula);
				usu.setActivo(false);
				usu.setArea(a);
				usu.setCargo(c);
				
				System.out.println(nombre);
				System.out.println(usuario.getNombre());
				
				try {
					usuarioEJB.registrarUsu(usu);
					
					accion = "Crear Usuario";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaUsuarios", accion, "usuario creado: " + nombre, sesion.getUser().getNombreUsuario(), browserDetail);
					
					accion = "Crear Persona";
					String browserDetail2 = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona creada: " + nombre, sesion.getUser().getNombreUsuario(), browserDetail2);

				} catch (ExcepcionNegocio e) {
					e.getMessage();
				}
				usu.setTipoUsuario(tip);

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "se ha registrado correctamente", null));

				cedula = "";
				nombre = "";
				apellido = "";
				correo = "";
				telefono = "";
				fechaNacimiento = null;
				contrasenia = "";
				fechaIngreso = null;
				nombreUsuario = "";

			} else {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Ya existe una persona con la cedula ingresado", null));

			}
		}

	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String controladorPersonas() {
		return telefono;
	}
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Genero getTipoGenero() {
		return tipoGenero;
	}

	public void setTipoGenero(Genero tipoGenero) {
		this.tipoGenero = tipoGenero;
	}

	public int getMunicipioSeleccinado() {
		return municipioSeleccinado;
	}

	public void setMunicipioSeleccinado(int municipioSeleccinado) {
		this.municipioSeleccinado = municipioSeleccinado;
	}

	public Persona getPersonaBuscada() {
		return personaBuscada;
	}

	public void setPersonaBuscada(Persona personaBuscada) {
		this.personaBuscada = personaBuscada;
	}

	public Municipio getMunicipioBuscado() {
		return municipioBuscado;
	}

	public void setMunicipioBuscado(Municipio municipioBuscado) {
		this.municipioBuscado = municipioBuscado;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public int getDepartamentoSelccionado() {
		return departamentoSelccionado;
	}

	public void setDepartamentoSelccionado(int departamentoSelccionado) {
		this.departamentoSelccionado = departamentoSelccionado;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTipoUsuarioSeleccionado() {
		return tipoUsuarioSeleccionado;
	}

	public void setTipoUsuarioSeleccionado(String tipoUsuarioSeleccionado) {
		this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public int getAreaSeleccionada() {
		return areaSeleccionada;
	}

	public void setAreaSeleccionada(int areaSeleccionada) {
		this.areaSeleccionada = areaSeleccionada;
	}

	public int getCargoSeleccionado() {
		return cargoSeleccionado;
	}

	public void setCargoSeleccionado(int cargoSeleccionado) {
		this.cargoSeleccionado = cargoSeleccionado;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<Cargo> getCargos() {
		return cargos;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public List<TipoUsuario> getTiposUsu() {
		return tiposUsu;
	}

	public void setTiposUsu(List<TipoUsuario> tiposUsu) {
		this.tiposUsu = tiposUsu;
	}

	public int getDeptoSeleccionado() {
		return deptoSeleccionado;
	}

	public void setDeptoSeleccionado(int deptoSeleccionado) {
		this.deptoSeleccionado = deptoSeleccionado;
	}

}
