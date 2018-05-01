package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
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
	private Genero tipoGenero;
	private int deptoSeleccionado;
	private int municipioSeleccionado;
	private List<Genero> generos;
	private List<Departamento> departamentos;
	private List<Municipio> municipios;
	
	private Persona cliente;
	
	@EJB
	private UsuarioEJB usuarioEJB;
	
	@EJB
	private DepartamentoEJB deptoEJB;
	
	@PostConstruct
	private void cargarCampos(){
		
		generos = Arrays.asList(Genero.values());
		departamentos = deptoEJB.listarDepartamentos();
		municipios = deptoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());
		
	}
	
	/**
	 * Registra un cliente
	 */
	public void registrarCliente() {

		cliente = new Persona();
		cliente.setApellido(apellido);
		cliente.setNombre(nombre);
		cliente.setCedula(cedula);
		cliente.setCorreo(correo);
		cliente.setTelefono(telefono);
		cliente.setGenero(tipoGenero);

		Municipio municipio = deptoEJB.buscarMunicipio(municipioSeleccionado);
		cliente.setMunicipio(municipio);

		try {

			usuarioEJB.registrarCliente(cliente);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Registrado Exitosamente", null));
			
			

		} catch (ExcepcionNegocio e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));

		}

	}
	
	public void buscarCliente (){
		
		
		
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
	
	
	
}
