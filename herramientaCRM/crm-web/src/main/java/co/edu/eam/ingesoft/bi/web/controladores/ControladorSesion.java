package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorSesion")
@SessionScoped
public class ControladorSesion implements Serializable {

	private String username;
	private String password;
	private Usuario user;
	
	public boolean isSesion(){
		return user != null;
	}
	
}
