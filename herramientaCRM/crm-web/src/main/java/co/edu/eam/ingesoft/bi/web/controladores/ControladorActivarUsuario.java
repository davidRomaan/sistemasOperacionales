package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AreasEmpresaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.CargoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.MunicipioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.DTO.UsuariosDTO;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorActivar")
@SessionScoped
public class ControladorActivarUsuario implements Serializable {

	/* datos persona */
	private String cedula;

	private String apellido;

	private String correo;

	private String fechaNacimiento;

	private Genero tipoGenero;

	private String nombre;

	private String telefono;

	private int deptoSeleccionado;

	private int municipioSeleccionado;

	/* datos usuario */
	private String contrasenia;

	private String fechaIngreso;

	private String nombreUsuario;

	private int areaSeleccionada;

	private int cargoSeleccionado;

	private String tipoUsuarioSeleccionado;
	
	private String accion;
	
	private Usuario usuario;

	private List<Municipio> municipios;

	private List<Departamento> departamentos;

	private List<Genero> generos;

	private List<Area> areas;

	private List<Cargo> cargos;

	private List<TipoUsuario> tiposUsu;

	private List<UsuariosDTO> usuarios;

	@EJB
	private MunicipioEJB municipioEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private PersonaEJB personaEJB;

	@EJB
	private AreasEmpresaEJB areasEJB;

	@EJB
	private CargoEJB cargoEJB;

	@EJB
	private TipoUsuarioEJB tipoEJB;

	@EJB
	private DepartamentoEJB departamentoEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	public void postconstructor() {
		usuario = Faces.getApplicationAttribute("user");
		listarActivosInActivos();
		listarDepartamentos();
		municipios = departamentoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());
		generos = Arrays.asList(Genero.values());
		areas = areasEJB.listarAreas();
		cargos = cargoEJB.listarCargos();
		tiposUsu = tipoEJB.listar();

	}

	public void listarActivosInActivos() {
		usuarios = usuarioEJB.llenarDTO();
	}

	/**
	 * Lista los muncipios del departamento seleccionado
	 */
	public void listarMunicipios() {
		municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);
	}

	/**
	 * Lista los departamentos registrados
	 */
	private void listarDepartamentos() {
		departamentos = departamentoEJB.listarDepartamentos();
	}

	public void crearUsuario() {

		if (cedula.isEmpty() || apellido.isEmpty() || correo.isEmpty() || nombre.isEmpty() || telefono.isEmpty()
				|| contrasenia.isEmpty() || nombreUsuario.isEmpty()) {
			Messages.addFlashGlobalInfo("ingrese todos los campos");

		} else {

			Usuario us = usuarioEJB.buscarUsu(cedula);
			Municipio m = municipioEJB.buscar(municipioSeleccionado);
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
				usu.setActivo(true);
				usu.setArea(a);
				usu.setCargo(c);
				usu.setTipoUsuario(tip);

				try {
					usuarioEJB.registrarUsu(usu);
					listarActivosInActivos();
					
					accion = "Crear Usuario";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaUsuarios", accion, "usuario creado: " + usu.getNombre(), "", browserDetail);
					
					accion = "Crear Persona";
					String browserDetail2 = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona creada: " + usu.getNombre(), "", browserDetail2);

				} catch (ExcepcionNegocio e) {
					e.getMessage();
				}

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

	public void buscarUsuario() {

		Usuario u = usuarioEJB.buscarUsu(cedula);
		Persona p = usuarioEJB.buscarCliente(cedula);

		if (cedula.isEmpty()) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Exitoso", "el area se ha registrado"));
		} else {
			if (u != null) {
				
				accion = "Buscar Usuario";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaUsuarios", accion, "usuario buscado: " + u.getNombre(), "", browserDetail);

				apellido = u.getApellido();
				correo = u.getCorreo();
				fechaNacimiento = u.getFechaNacimiento();
				tipoGenero = u.getGenero();
				nombre = u.getNombre();
				telefono = u.getTelefono();
				contrasenia = u.getContrasenia();
				fechaIngreso = u.getFechaIngreso();
				nombreUsuario = u.getNombreUsuario();
				deptoSeleccionado = u.getMunicipio().getDepartamento().getId();

				municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);
				municipioSeleccionado = u.getMunicipio().getId();

				areaSeleccionada = u.getArea().getId();
				cargoSeleccionado = u.getCargo().getId();
				tipoUsuarioSeleccionado = u.getTipoUsuario().getNombre();
				reload();

			} else if (p != null) {
				
				accion = "Buscar Persona";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona buscada: " + p.getNombre(), "", browserDetail);
				
				apellido = p.getApellido();
				correo = p.getCorreo();
				fechaNacimiento = p.getFechaNacimiento();
				tipoGenero = p.getGenero();
				nombre = p.getNombre();
				telefono = p.getTelefono();
				deptoSeleccionado = p.getMunicipio().getDepartamento().getId();

				municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);
				municipioSeleccionado = p.getMunicipio().getId();
			
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("esta persona no se encuentra registrada"));
			}
		}

	}

	private void reload() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generarClave() {

		String clave = usuarioEJB.generarClave();
		contrasenia = clave;
	}

	public void activarUsuario(String ced) {

		Usuario usuar = usuarioEJB.buscarUsuarioCedula(ced);

		if (usuar != null) {
			if (usuar.isActivo() == true) {
				Messages.addFlashGlobalInfo("este Usuario ya esta activo");
			} else {
				usuar.setActivo(true);
				usuarioEJB.editarUsuario(usuar);
				listarActivosInActivos();
				Messages.addFlashGlobalInfo("se activo correctamente");
				
				accion = "Activar Usuario";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaUsuarios", accion, "usuario activado: " + usuar.getNombre(), "", browserDetail);
			}

		} else {
			Messages.addFlashGlobalInfo("este usuario no existe");
		}
	}

	public void desactivarUsuario(String ced) {

		Usuario usuar = usuarioEJB.buscarUsuarioCedula(ced);
		if (usuar != null) {
			if (usuar.isActivo() == true) {
				usuar.setActivo(false);
				usuarioEJB.editarUsuario(usuar);
				listarActivosInActivos();
				Messages.addFlashGlobalInfo("se desactivo correctamente");
				
				accion = "Desactivar Usuario";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaUsuarios", accion, "usuario desactivado: " + usuar.getNombre(), "", browserDetail);
			} else {
				Messages.addFlashGlobalInfo("este usuario ya esta desactivado");
			}

		} else {
			Messages.addFlashGlobalInfo("este usuario no existe");
		}
	}

	public List<UsuariosDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuariosDTO> usuarios) {
		this.usuarios = usuarios;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Genero getTipoGenero() {
		return tipoGenero;
	}

	public void setTipoGenero(Genero tipoGenero) {
		this.tipoGenero = tipoGenero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getDeptoSeleccionado() {
		return deptoSeleccionado;
	}

	public void setDeptoSeleccionado(int deptoSeleccionado) {
		this.deptoSeleccionado = deptoSeleccionado;
	}

	public int getMunicipioSeleccionado() {
		return municipioSeleccionado;
	}

	public void setMunicipioSeleccionado(int municipioSeleccionado) {
		this.municipioSeleccionado = municipioSeleccionado;
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

	public String getTipoUsuarioSeleccionado() {
		return tipoUsuarioSeleccionado;
	}

	public void setTipoUsuarioSeleccionado(String tipoUsuarioSeleccionado) {
		this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

}
