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

	private Date fechaNacimiento;

	private Genero tipoGenero;

	private String nombre;

	private String telefono;

	private int deptoSeleccionado;

	private int municipioSeleccionado;

	/* datos usuario */
	private String contrasenia;

	private Date fechaIngreso;

	private String nombreUsuario;

	private int areaSeleccionada;

	private int cargoSeleccionado;
	
	private int tipoUsuarioSeleccionado;

	private List<Municipio> municipios;

	private List<Genero> generos;
	
	private List<Area>areas;
	
	private List<Cargo>cargos;
	
	private List<TipoUsuario>tiposUsu;

	private List<Persona> personas;

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
		personas = usuarioEJB.listarActivosInactivos();
	}
	
	public void generarClave(){
		
		String clave = usuarioEJB.generarClave();
		contrasenia = clave;
	}
	

	public void activarUsuario(Persona per) {

		Usuario p = usuarioEJB.buscarUsuarioCedula(per.getCedula());

		if (p != null) {
			if (p.isActivo() == true) {
				Messages.addFlashGlobalInfo("este Usuario ya esta activo");
			} else {
				p.setActivo(true);
				personaEJB.editarPersona(p);
				listarActivosInActivos();
				Messages.addFlashGlobalInfo("se activo correctamente");
			}

		} else {
			Messages.addFlashGlobalInfo("este usuario no existe");
		}
	}

	public void desactivarUsuario(Persona per) {

		Usuario p = usuarioEJB.buscarUsuarioCedula(per.getCedula());
		if (p != null) {
			if (p.isActivo() == true) {
				p.setActivo(false);
				personaEJB.editarPersona(p);
				listarActivosInActivos();
				Messages.addFlashGlobalInfo("se desactivo correctamente");
			} else {
				Messages.addFlashGlobalInfo("este usuario ya esta desactivado");
			}

		} else {
			Messages.addFlashGlobalInfo("este usuario no existe");
		}
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
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

	public int getTipoUsuarioSeleccionado() {
		return tipoUsuarioSeleccionado;
	}

	public void setTipoUsuarioSeleccionado(int tipoUsuarioSeleccionado) {
		this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
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

}
