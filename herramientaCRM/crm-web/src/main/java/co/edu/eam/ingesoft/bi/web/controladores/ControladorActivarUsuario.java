package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;

@Named("controladorActivar")
@SessionScoped
public class ControladorActivarUsuario implements Serializable {

	private List<Persona> personas;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private PersonaEJB personaEJB;

	@PostConstruct
	public void postconstructor() {
		listarActivosInActivos();
	}

	public void listarActivosInActivos() {
		personas = usuarioEJB.listarActivosInactivos();
	}

	public void activarUsuario(Persona per) {

		Persona p = usuarioEJB.buscarUsuarioCedula(per.getCedula());
		
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

		Persona p = usuarioEJB.buscarUsuarioCedula(per.getCedula());
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

}
