package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Conexion;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorSesion")
@SessionScoped
public class ControladorSesion implements Serializable {

	private String username;
	private String password;
	private String accion;
	private Usuario user;
	
	private static int bd = 0;
	
	@EJB
	private ConexionEJB conexionEJB;

	@EJB
	private UsuarioEJB usuarioEJB;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;
	
	private void obtenerBD (){
		conexionEJB.ultimaBD();
	}

	public String login() {
		
		obtenerBD();
		
		Usuario usuarioTemporal = usuarioEJB.buscarUsuario(username);

		if ((usuarioTemporal != null)) {

			if (usuarioTemporal.getContrasenia().equals(password)
					&& (usuarioTemporal.getTipoUsuario().getNombre().equalsIgnoreCase("Administrador")
							&& usuarioTemporal.isActivo() == true)) {
				user = usuarioTemporal;
				Faces.setSessionAttribute("user", user);
				
				accion = "Iniciar Sesion";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaSesion", accion, "sesion creado por: Administrador", user.getNombre(), browserDetail);
				System.out.println(user.getNombre());
				
				return "/templates/inicioBases.xhtml?faces-redirect=true";

			}
			if (usuarioTemporal.getContrasenia().equals(password)
					&& (usuarioTemporal.getTipoUsuario().getNombre().equalsIgnoreCase("CRM")
							&& usuarioTemporal.isActivo() == true)) {
				user = usuarioTemporal;
				Faces.setSessionAttribute("user", user);
				
				accion = "Iniciar Sesion";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaSesion", accion, "sesion creado por: CRM", user.getNombre(), browserDetail);
				
				return "/templates/inicioCRM.xhtml?faces-redirect=true";

			}
			if (usuarioTemporal.getContrasenia().equals(password)
					&& (usuarioTemporal.getTipoUsuario().getNombre().equalsIgnoreCase("ERP")
							&& usuarioTemporal.isActivo() == true)) {
				user = usuarioTemporal;
				Faces.setSessionAttribute("user", user);
				
				accion = "Iniciar Sesion";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaSesion", accion, "sesion creado por: ERP", user.getNombre(), browserDetail);
				
				return "/templates/inicioERP.xhtml?faces-redirect=true";

			}
		} else {
			
			accion = "Error Log-In";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaSesion", accion, "error Inicion Sesion", "error Inicion Sesion", browserDetail);
			
			Messages.addFlashGlobalError("este usuario no existe");
			
		}
		return null;
	}

	public String cerrarSession() {
		user = null;
		HttpSession sesion;
		sesion = (HttpSession) Faces.getSession();
		sesion.invalidate();
		return "/paginas/publico/login.xhtml?faces-redirect=true";

	}

	public boolean isSesion() {
		return user != null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

}
