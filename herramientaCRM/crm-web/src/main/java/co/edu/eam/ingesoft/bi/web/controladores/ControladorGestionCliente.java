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

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorGestioncliente")
@SessionScoped
public class ControladorGestionCliente implements Serializable {

	private String cedula;
	private String nombre;
	private String apellido;
	private String correo;
	private String telefono;
	private String accion;
	private Genero tipoGenero;
	private Usuario usuario;
	private int deptoSeleccionado;
	private int municipioSeleccionado;
	private List<Genero> generos;
	private List<Departamento> departamentos;
	private List<Municipio> municipios;
	private List<Persona> clientes;
	private String fechaNacimiento;

	private Persona cliente;
	
	@Inject
	private ControladorSesion sesion;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private DepartamentoEJB deptoEJB;

	@EJB
	private PersonaEJB personaEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	private void cargarCampos() {
		
		generos = Arrays.asList(Genero.values());
		departamentos = deptoEJB.listarDepartamentos();
		municipios = deptoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());
		clientes = personaEJB.listarClientes();

	}

	public void listarMunicipios() {
		municipios = deptoEJB.listarMunicipiosDepartamento(deptoSeleccionado);
	}

	/**
	 * Valida que se hayan ingresado todos los campos
	 * 
	 * @return true si se llenaron los campos, de lo contrario false
	 */
	private boolean validarCampos() {
		if (cedula.equals("") || nombre.equals("") || apellido.equals("") || telefono.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Registra un cliente
	 */
	public void registrarCliente() {

		if (!validarCampos()) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe ingresar todos los campos", null));

		} else {

			cliente = new Persona();
			cliente.setApellido(apellido);
			cliente.setNombre(nombre);
			cliente.setCedula(cedula);
			cliente.setCorreo(correo);
			cliente.setTelefono(telefono);
			cliente.setGenero(tipoGenero);
			// cliente.setFechaNacimiento(fechaNacimiento);

			Municipio municipio = deptoEJB.buscarMunicipio(municipioSeleccionado);
			cliente.setMunicipio(municipio);

			try {

				usuarioEJB.registrarCliente(cliente);

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Registrado Exitosamente", null));

				refrescarClientes();
				
				accion = "Crear Persona";
				String browserDetail2 = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona creada: " + cliente.getNombre(), sesion.getUser().getCedula(), browserDetail2);

			} catch (ExcepcionNegocio e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));

			}

		}

	}

	public void buscar() {

		cliente = usuarioEJB.buscarCliente(cedula);

		if (cliente != null) {
			
			accion = "Buscar Persona";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona buscada: " + cliente.getNombre(), sesion.getUser().getCedula(), browserDetail);

			cedula = cliente.getCedula();
			nombre = cliente.getNombre();
			apellido = cliente.getApellido();
			cedula = cliente.getCedula();
			telefono = cliente.getTelefono();
			tipoGenero = cliente.getGenero();
			deptoSeleccionado = cliente.getMunicipio().getDepartamento().getId();

			municipios = deptoEJB.listarMunicipiosDepartamento(deptoSeleccionado);

			municipioSeleccionado = cliente.getMunicipio().getId();
			correo = cliente.getCorreo();

			// fechaNacimiento = cliente.getFechaNacimiento();

		} else {

			Messages.addFlashGlobalError("El cliente no existe");

		}

	}

	private void refrescarClientes() {
		clientes = personaEJB.listarClientes();
	}

	public void editar() {

		if (cliente != null) {

			if (validarCampos()) {

				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setGenero(tipoGenero);
				cliente.setMunicipio(deptoEJB.buscarMunicipio(municipioSeleccionado));
				cliente.setTelefono(telefono);
				// cliente.setFechaNacimiento(fechaNacimiento);
				cliente.setCorreo(correo);

				usuarioEJB.editarCliente(cliente);

				Messages.addFlashGlobalInfo("Se ha editado correctamente");
				refrescarClientes();
				limpiarCampos();
				
				accion = "Editar Persona";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona editada: " + cliente.getNombre(), sesion.getUser().getCedula(), browserDetail);
				
				cliente = null;

			} else {

				Messages.addFlashGlobalError("Debe ingresar todos los campos");

			}

		} else {

			Messages.addFlashGlobalError("Debe buscar un cliente previamente");

		}

	}

	/**
	 * 
	 * <p:outputLabel id="maskNacimiento" value="Fecha de nacimiento" />
	 * <p:inputMask id="dateNacimiento" value=
	 * "#{controladorGestioncliente.fechaNacimiento}" mask="99/99/9999" />
	 */

	private void limpiarCampos() {
		nombre = "";
		apellido = "";
		correo = "";
		telefono = "";
		//fechaNacimiento = "";
	}

	public void eliminar(Persona p) {
		
		accion = "Eliminar Persona";
		String browserDetail = Faces.getRequest().getHeader("User-Agent");
		auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona eliminada: " + p.getNombre(), sesion.getUser().getCedula(), browserDetail);
		
		usuarioEJB.eliminarCliente(p);
		
		refrescarClientes();
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Genero getTipoGenero() {
		return tipoGenero;
	}

	public void setTipoGenero(Genero tipoGenero) {
		this.tipoGenero = tipoGenero;
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

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public List<Persona> getClientes() {
		return clientes;
	}

	public void setClientes(List<Persona> clientes) {
		this.clientes = clientes;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

}
