package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@LocalBean
@Stateless
public class AuditoriaEJB {

	@EJB
	private Persistencia em;
	
	@EJB
	private UsuarioEJB usuarioEJB;

	private String userAgent = "";
	private String os = "";
	private String browser = "";
	private String user2 = "";
	private String browserDetails = "";

	/**
	 * Identifico el nevegador y el os
	 */
	public void identificarNavegadorPeticion() {

		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
			os = "IPhone";
		} else {
			os = "UnKnown, More-Info: " + userAgent;
		}

		// ===============Browser===========================
		if (user2.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user2.contains("safari") && user2.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user2.contains("opr") || user2.contains("opera")) {
			if (user2.contains("opera"))
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			else if (user2.contains("opr"))
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
		} else if (user2.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user2.indexOf("mozilla/7.0") > -1) || (user2.indexOf("netscape6") != -1)
				|| (user2.indexOf("mozilla/4.7") != -1) || (user2.indexOf("mozilla/4.78") != -1)
				|| (user2.indexOf("mozilla/4.08") != -1) || (user2.indexOf("mozilla/3") != -1)) {
			// browser=(userAgent.substring(userAgent.indexOf("MSIE")).split("
			// ")[0]).replace("/", "-");
			browser = "Netscape-?";

		} else if (user2.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user2.contains("rv")) {
			browser = "IE-" + user2.substring(user2.indexOf("rv") + 3, user2.indexOf(")"));
		} else {
			browser = "UnKnown, More-Info: " + userAgent;
		}

	}

	/**
	 * 
	 * @param persona
	 * @param accion
	 * @param nombreReg
	 * @param browserDeta
	 * @param usuario
	 * @param usuarioAf
	 */
	public void crearAuditoria(String referencia, String accion, String descripcion, String usuario,
			String browserDeta) {

		this.browserDetails = browserDeta;
		userAgent = browserDetails;
		user2 = userAgent.toLowerCase();

		identificarNavegadorPeticion();

		Calendar fechaActual = new GregorianCalendar();
		int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
		int mes = fechaActual.get(Calendar.MONTH);
		int anio = fechaActual.get(Calendar.YEAR);
		int minutos = fechaActual.get(Calendar.SECOND);
		int hora = fechaActual.get(Calendar.HOUR);

		Date horaGuadar = new Date();
		horaGuadar.setMinutes(minutos);
		horaGuadar.setHours(hora);

		Calendar fechaGuardar = new GregorianCalendar();
		fechaGuardar.set(anio, mes, dia);
		fechaGuardar.setTime(horaGuadar);

		System.out.println(fechaGuardar);

		Auditoria auditoria = new Auditoria();
		auditoria.setAccion(accion);
		auditoria.setFechaHora(fechaGuardar);
		auditoria.setReferencia(referencia);
		auditoria.setDescripcion(descripcion);
		auditoria.setUsuario(usuario);
		auditoria.setDispositivo(os);
		auditoria.setNavegador(browser);

		em.setBd(ConexionEJB.getBd());
		em.crear(auditoria);

	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */
	public List<Auditoria> listarAuditorias(String condicion) {
		em.setBd(ConexionEJB.getBd());
		return (List<Auditoria>) (Object) em.listarConParametroString(Auditoria.LISTA_AUDITORIA, condicion);

	}

	public List<Auditoria> listarPorFechaActual(Date fecha) {

		List<Auditoria> lista = (List<Auditoria>) (Object) em
				.listarConParametroObjeto(Auditoria.LISTA_AUDITORIA_FECHA_ACT, fecha);

		List<Usuario>listaUsuarios = new ArrayList<Usuario>();
		
		for(int i=0; i<lista.size(); i++){
			
			Usuario us = usuarioEJB.buscarUsu(lista.get(i).getUsuario());
		}
		return lista;
	}

}
