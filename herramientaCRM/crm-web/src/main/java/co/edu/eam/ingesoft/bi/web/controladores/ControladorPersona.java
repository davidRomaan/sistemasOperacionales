package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.MunicipioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@Named("controladorPersonas")
@SessionScoped
public class ControladorPersona implements Serializable{
	
	private String cedula;
	
	private String nombre;
	
	private String apellido;
	
	private String telefono;
	
	private String correo;
	
	private Date fechaNacimiento;
	
	private Genero tipoGenero;
	
	private int municipioSeleccinado;
	
	private Persona personaBuscada;
	
	private Municipio municipioBuscado;

	@EJB
	private PersonaEJB personaEJB;
	
	@EJB
	private MunicipioEJB municipioEJB;
	
	private List<Municipio> municipios;
	
	private List<Genero> generos;
	
	
	@PostConstruct
	public void listares() {
		municipios = personaEJB.listaMunicipios();
		generos = Arrays.asList(Genero.values());
	}
	
	
	public void registrar() {
		
		personaBuscada = personaEJB.buscar(cedula);
		
		if (personaBuscada == null){
			
			municipioBuscado = municipioEJB.buscar(municipioSeleccinado);
		
			Persona persona = new Persona();
			persona.setCedula(cedula);
			persona.setNombre(nombre);
			persona.setApellido(apellido);
			persona.setTelefono(telefono);
			persona.setCorreo(correo);
			persona.setActivo(false);
			persona.setGenero(tipoGenero);
			persona.setMunicipio(municipioBuscado);
			
			personaEJB.crearPersona(persona);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"La persona ha sido registrada exitosamente", null));
		
		cedula = "";
		nombre = "";
		apellido = "";
		correo = "";
		telefono = "";
		
		
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Ya existe una persona con la cedula ingresado", null));
			
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


}
