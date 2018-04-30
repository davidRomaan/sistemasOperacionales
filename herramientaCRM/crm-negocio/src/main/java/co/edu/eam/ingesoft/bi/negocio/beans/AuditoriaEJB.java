package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaArea;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaDetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaInventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@LocalBean
@Stateless
public class AuditoriaEJB {

	@PersistenceContext
	private EntityManager em;

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
	public void crearAuditoriaPersona(Persona persona, String accion, String browserDeta) {

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
		
		AuditoriaPersona audiPersona = new AuditoriaPersona();
		audiPersona.setAccion(accion);
		audiPersona.setFechaHora(fechaGuardar);
		audiPersona.setPersona(persona);
		audiPersona.setDispositivo(os);
		audiPersona.setNavegador(browser);		

		em.persist(audiPersona);
	

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
	public void crearAuditoriaArea(Area area, String accion, String browserDeta) {

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
		
		AuditoriaArea auditoriaArea = new AuditoriaArea();
		auditoriaArea.setAccion(accion);
		auditoriaArea.setFechaHora(fechaGuardar);
		auditoriaArea.setArea(area);
		auditoriaArea.setDispositivo(os);
		auditoriaArea.setNavegador(browser);	

		em.persist(auditoriaArea);

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
	public void crearAuditoriaDetalleVenta(DetalleVenta dt, String accion, String browserDeta) {

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
		
		AuditoriaDetalleVenta detalleVenta = new AuditoriaDetalleVenta();
		detalleVenta.setSeleccion(accion);
		detalleVenta.setFechaHora(fechaGuardar);
		detalleVenta.setDetalleVenta(dt);
		detalleVenta.setDispositivo(os);
		detalleVenta.setNavegador(browser);	

		em.persist(detalleVenta);

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
	public void crearAuditoriaInventario( Inventario inventario, String accion, String browserDeta) {

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
		
		AuditoriaInventario invenProducto = new AuditoriaInventario();
		invenProducto.setAccion(accion);
		invenProducto.setFechaHora(fechaGuardar);
		invenProducto.setInventario(inventario);
		invenProducto.setDispositivo(os);
		invenProducto.setNavegador(browser);	

		em.persist(invenProducto);

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
	public void crearAuditoriaProducto( Producto producto, String accion, String browserDeta) {

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
		
		AuditoriaProducto pro = new AuditoriaProducto();
		pro.setAccion(accion);
		pro.setFechaHora(fechaGuardar);
		pro.setProducto(producto);
		pro.setDispositivo(os);
		pro.setNavegador(browser);	

		em.persist(pro);

	}
	
}
