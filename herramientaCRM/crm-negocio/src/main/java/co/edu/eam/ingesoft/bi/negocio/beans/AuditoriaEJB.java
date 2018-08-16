package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.etl.ETLVentasEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;

@LocalBean
@Stateless
public class AuditoriaEJB {

	@EJB
	private Persistencia em;

	@EJB
	private ETLVentasEJB etlEJB;

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

	public List<HechoAuditoria> listarFechaActualAuditoria(int bd, String fecha) {

		List<Auditoria> lista = listarPorFechaActual(fecha, bd);
		List<HechoAuditoria> listaHecho = new ArrayList<HechoAuditoria>();

		if (lista.size() == 0) {

			throw new ExcepcionNegocio("No hay auditorias registradas en el periodo ingresado");

		} else {
			em.setBd(bd);
			ConexionEJB.setBd(bd);
			for (int i = 0; i < lista.size(); i++) {

				Usuario us = usuarioEJB.buscarUsu(lista.get(i).getUsuario());

				DimensionUsuario dimension = new DimensionUsuario();
				dimension.setTipoUsuario(us.getTipoUsuario().getNombre());
				dimension.setCedula(us.getCedula());
				dimension.setNombre(us.getNombre());
				dimension.setApellido(us.getApellido());
				dimension.setGenero(String.valueOf(us.getGenero()));
				dimension.setEdad((short) etlEJB.calcularEdad(us.getFechaNacimiento()));
				dimension.setCargo(us.getCargo().getDescripcion());

				HechoAuditoria hecho = new HechoAuditoria();
				hecho.setAccion(lista.get(i).getAccion());
				hecho.setDispositivo(lista.get(i).getDispositivo());
				hecho.setNavegador(lista.get(i).getNavegador());
				hecho.setFecha(lista.get(i).getFechaHora());
				hecho.setUsuario(dimension);

				listaHecho.add(hecho);

			}
		}

		return listaHecho;

	}

	public List<Auditoria> listarPorFechaActual(String fecha, int bd) {
		em.setBd(bd);

		List<Object> lista = em.listarFechaActual(fecha);
		List<Auditoria> listaAudit = new ArrayList<Auditoria>();

		for (int i = 0; i < lista.size(); i++) {

			int cod = (int) (Integer) lista.get(i);

			Auditoria aud = (Auditoria) em.buscar(Auditoria.class, cod);
			listaAudit.add(aud);
		}

		return listaAudit;
	}

	public List<Auditoria> listarPorFechaSemana(String fecha, String fecha2, int bd) {
		em.setBd(bd);

		String[] datos = fecha.split("-");
		int dia = Integer.parseInt(datos[0]);
		int mes = Integer.parseInt(datos[1]);

		String[] datosFecha2 = fecha2.split("-");
		int diaDos = Integer.parseInt(datos[0]);
		int mesDos = Integer.parseInt(datos[1]);
		int anioDos = Integer.parseInt(datos[2]);

		String nueva31 = "";
		String nueva30 = "";
		List<Auditoria> listaAudit = new ArrayList<Auditoria>();

		if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
			mesDos++;
			if (dia == 25) {
				diaDos = 1;
			}
			if (dia == 26) {
				diaDos = 2;
			}
			if (dia == 27) {
				diaDos = 3;
			}
			if (dia == 28) {
				diaDos = 4;
			}
			if (dia == 29) {
				diaDos = 5;
			}
			if (dia == 30) {
				diaDos = 6;
			}
			if (dia == 31) {
				diaDos = 7;
			}
			nueva31 = anioDos + "-" + mesDos + "-" + diaDos;

			List<Object> lista = em.listarFechaSemana(fecha, nueva31);
			for (int i = 0; i < lista.size(); i++) {

				int cod = (int) (Integer) lista.get(i);

				Auditoria aud = (Auditoria) em.buscar(Auditoria.class, cod);
				listaAudit.add(aud);
			}

		}
		if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
			if (dia == 24) {
				diaDos = 1;
			}
			if (dia == 25) {
				diaDos = 2;
			}
			if (dia == 26) {
				diaDos = 3;
			}
			if (dia == 27) {
				diaDos = 4;
			}
			if (dia == 28) {
				diaDos = 5;
			}
			if (dia == 29) {
				diaDos = 6;

			}
			if (dia == 30) {
				diaDos = 7;

			}
			nueva30 = anioDos + "-" + mesDos + "-" + diaDos;

			List<Object> lista = em.listarFechaSemana(fecha, nueva30);

			for (int i = 0; i < lista.size(); i++) {

				int cod = (int) (Integer) lista.get(i);

				Auditoria aud = (Auditoria) em.buscar(Auditoria.class, cod);
				listaAudit.add(aud);
			}

		}
		return listaAudit;
	}

	public List<HechoAuditoria> listarFechaSemanaAuditoria(int bd, String fecha, String fecha2) {

		List<Auditoria> lista = listarPorFechaSemana(fecha, fecha2, bd);
		List<HechoAuditoria> listaHecho = new ArrayList<HechoAuditoria>();

		if (lista.size() == 0) {

			throw new ExcepcionNegocio("No hay facturas registradas en el periodo ingresado");

		} else {
			for (int i = 0; i < lista.size(); i++) {

				Usuario us = usuarioEJB.buscarUsu(lista.get(i).getUsuario());

				DimensionUsuario dimension = new DimensionUsuario();
				dimension.setTipoUsuario(us.getTipoUsuario().getNombre());
				dimension.setCedula(us.getCedula());
				dimension.setNombre(us.getNombre());
				dimension.setApellido(us.getApellido());
				dimension.setGenero(String.valueOf(us.getGenero()));
				dimension.setEdad((short) etlEJB.calcularEdad(us.getFechaNacimiento()));
				dimension.setCargo(us.getCargo().getDescripcion());

				HechoAuditoria hecho = new HechoAuditoria();
				hecho.setAccion(lista.get(i).getAccion());
				hecho.setDispositivo(lista.get(i).getDispositivo());
				hecho.setNavegador(lista.get(i).getNavegador());
				hecho.setFecha(lista.get(i).getFechaHora());
				hecho.setUsuario(dimension);

				listaHecho.add(hecho);

			}
		}

		return listaHecho;

	}

	/**
	 * Convierte un formato de fecha Calendar a string
	 * 
	 * @param fecha
	 *            fecha que se desea convertir
	 * @return la fecha en formato String
	 */
	public String convertirCalendarAString(Calendar fecha) {
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH);
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		return anio + "-" + mes + "-" + dia;
	}

	/**
	 * Convierte una fecha de tipo String a Date
	 * 
	 * @param fecha
	 *            fecha que se desea convertir
	 * @return la fecha en tipo Date
	 */
	public Calendar convertirFechaStrintADate(String fecha) {
		String[] datos = fecha.split("/");
		int dia = Integer.parseInt(datos[0]);
		int mes = Integer.parseInt(datos[1]);
		int anio = Integer.parseInt(datos[2]);

		Calendar c = new GregorianCalendar();
		c.set(anio, mes, dia);

		System.out.println(c.get(Calendar.DATE) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR));

		return c;

	}
	
	
	

}
