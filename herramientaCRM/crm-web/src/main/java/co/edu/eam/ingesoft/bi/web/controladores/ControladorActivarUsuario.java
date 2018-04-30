package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AreasEmpresaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.CargoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.MunicipioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoUsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.persistencia.DTO.UsuariosDTO;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;
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

	private List<Municipio> municipios;

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

	@PostConstruct
	public void postconstructor() {
		listarActivosInActivos();
		municipios = personaEJB.listaMunicipios();
		generos = Arrays.asList(Genero.values());
		areas = areasEJB.listarAreas();
		cargos = cargoEJB.listarCargos();
		tiposUsu = tipoEJB.listar();

	}

	public void listarActivosInActivos() {
		usuarios = usuarioEJB.llenarDTO();
	}

	public void crearUsuario() {

		if (cedula.isEmpty() || apellido.isEmpty() || correo.isEmpty() || nombre.isEmpty() || telefono.isEmpty()
				|| contrasenia.isEmpty() || nombreUsuario.isEmpty()) {
			Messages.addFlashGlobalInfo("ingrese todos los campos");

		} else {

			Persona p = personaEJB.buscar(cedula);
			Municipio m = municipioEJB.buscar(municipioSeleccionado);
			Area a = areasEJB.buscarArea(areaSeleccionada);
			Cargo c = cargoEJB.buscarCargo(cargoSeleccionado);
			TipoUsuario tip = tipoEJB.buscar(tipoUsuarioSeleccionado);

			if (p == null) {

				Persona persona = new Persona();
				persona.setCedula(cedula);
				persona.setApellido(apellido);
				persona.setCorreo(correo);
				persona.setFechaNacimiento(fechaNacimiento);
				persona.setGenero(tipoGenero);
				persona.setNombre(nombre);
				persona.setTelefono(telefono);
				persona.setMunicipio(m);
				personaEJB.crearPersona(persona);

				Usuario usu = new Usuario();
				usu.setContrasenia(contrasenia);
				usu.setFechaIngreso(fechaIngreso);
				usu.setNombreUsuario(nombreUsuario);
				usu.setCedula(cedula);
				usu.setActivo(true);
				usu.setArea(a);
				usu.setCargo(c);
				usu.setTipoUsuario(tip);
				usuarioEJB.registrarUsuario(usu);

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

}
