package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@LocalBean
@Stateless
public class ETLAuditoriaEJB {

	@EJB
	private Persistencia em;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	public List<Auditoria> listaVentasPeriodo(Calendar fechaInicio, Calendar fechaFin, int bd) {

		em.setBd(bd);

		String fecha1 = auditoriaEJB.convertirCalendarAString(fechaInicio);
		String fecha2 = auditoriaEJB.convertirCalendarAString(fechaFin);

		List<Object> lista = em.listarAuditoriasIntervaloFecha(fecha1, fecha2);

		List<Auditoria> listaFacturas = new ArrayList<Auditoria>();

		for (int i = 0; i < lista.size(); i++) {

			int cod = (int) (Integer) lista.get(i);

			Auditoria factura = (Auditoria) em.buscar(Auditoria.class, cod);

			listaFacturas.add(factura);

		}

		return listaFacturas;
	}

	/**
	 * Obtiene todos los datos asociados a un hecho venta
	 * 
	 * @param fechaInicio
	 *            fehca de inicio desde la cual se desea cargar los datos
	 * @param fechaFin
	 *            fecha final hasta la cual se desea cargr los datos
	 */
	public List<HechoAuditoria> obtenerDatosHechoVentasAcumulacionSimple(Calendar fechaInicio, Calendar fechaFin,
			int bd, List<HechoAuditoria> listaHechos) {

		// Lista de facturas registradas en la bd
		List<Auditoria> audi = listaVentasPeriodo(fechaInicio, fechaFin, bd);

		if (audi.size() == 0) {

			throw new ExcepcionNegocio("No hay auditorias registradas en el periodo ingresado");

		} else {

			em.setBd(bd);
			ConexionEJB.setBd(bd);

			// Se recorre la segunda lista obtenida de postgres
			for (Auditoria auditorias : audi) {

				String ced = auditorias.getUsuario();
				Usuario per = usuarioEJB.buscarUsu(ced);

				DimensionUsuario dimUsu = new DimensionUsuario();
				dimUsu.setNombre(per.getNombre());
				dimUsu.setApellido(per.getApellido());
				dimUsu.setCargo(per.getCargo().getDescripcion());
				dimUsu.setCedula(ced);
				dimUsu.setEdad(calcularEdad("27/09/1995"));
				dimUsu.setGenero(per.getGenero().name());
				dimUsu.setTipoUsuario(per.getTipoUsuario().getNombre());

				HechoAuditoria hechoAudi = new HechoAuditoria();
				hechoAudi.setAccion(auditorias.getAccion());
				hechoAudi.setDispositivo(auditorias.getDispositivo());
				hechoAudi.setNavegador(auditorias.getNavegador());
				hechoAudi.setUsuario(dimUsu);

				hechoAudi.setFecha(auditorias.getFechaHora());

				listaHechos.add(hechoAudi);

			}
		}

		return listaHechos;

	}

	/**
	 * Calcula la edad de una persona
	 * 
	 * @param fechaNacimiento
	 *            fecha de nacimiento de la persona
	 * @return la edad de la persona
	 */
	public int calcularEdad(String fechaNacimiento) {

		String fecha[] = fechaNacimiento.split("/");
		int anio = Integer.parseInt(fecha[2]);

		Calendar fechaActual = new GregorianCalendar();
		int anioActual = fechaActual.get(Calendar.YEAR);

		int edad = anioActual - anio;

		return edad;

	}

//	public void cargarDatosOracle(List<HechoAuditoria> hechos) {
//
//		limpiarTablasDWH();
//		cargarDatosDWH(hechos);
//
//	}

	public void limpiarTablasDWH() {
		em.limpiarDWH("HECHO_AUDITORIA");
		em.limpiarDWH("DIMENSION_USUARIO");
	}

	public void cargarDatosRollingAud(List<HechoAuditoria> lista) {

		DimensionUsuario usuario;
		List<String> listaCed = new ArrayList();

		if (lista.size() == 0) {

		} else {

			for (HechoAuditoria hechoAudi : lista) {

				if (hechoAudi.getUsuario().getCargo().equals("")) {
					throw new ExcepcionNegocio("El campo cargo no puede quedar vacio");
				}
				if (hechoAudi.getUsuario().getEdad() > 130) {
					throw new ExcepcionNegocio("La edad excede el limite existencial");
				}
				if (hechoAudi.getUsuario().getTipoUsuario().equals("")) {
					throw new ExcepcionNegocio("El campo tipo usuario no puede quedar vacio");
				}

				// usuario =
				// em.dimensionUsuarioExiste(hechoAudi.getUsuario().getCedula());

				// String cedula = hechoAudi.getUsuario().getCedula();

				// if (usuario != null && !listaCed.contains(cedula)) {

				usuario = new DimensionUsuario();

				DimensionUsuario usuarioAuditoria = hechoAudi.getUsuario();

				usuario.setApellido(usuarioAuditoria.getApellido());
				usuario.setCargo(usuarioAuditoria.getCargo());
				usuario.setCedula(usuarioAuditoria.getCedula());
				usuario.setEdad(usuarioAuditoria.getEdad());
				usuario.setGenero(usuarioAuditoria.getGenero());
				usuario.setNombre(usuarioAuditoria.getNombre());
				usuario.setTipoUsuario(usuarioAuditoria.getTipoUsuario());

				em.crearDimensionUsuario(usuario);
				// listaCed.add(cedula);

				hechoAudi.setUsuario(usuario);

				// }

				em.crearHechoAuditoria(hechoAudi);

			}

		}

	}

	public void cargarDatosDWH(List<HechoAuditoria> hechos) {

		DimensionUsuario usuario = null;
		List<DimensionUsuario> listaUsuariosReg = new ArrayList();
		List<String> cedulasReg = new ArrayList<String>();

		for (HechoAuditoria hechoAudi : hechos) {

			if (hechoAudi.getUsuario().getCargo().equals("")) {
				throw new ExcepcionNegocio("El campo cargo no puede quedar vacio");
			}
			if (hechoAudi.getUsuario().getEdad() > 130) {
				throw new ExcepcionNegocio("La edad excede el limite existencial");
			}
			if (hechoAudi.getUsuario().getTipoUsuario().equals("")) {
				throw new ExcepcionNegocio("El campo tipo usuario no puede quedar vacio");
			}

			usuario = em.dimensionUsuarioExiste(hechoAudi.getUsuario().getCedula());

			boolean usuarioRegistrado = false;

			if (usuario == null && !cedulasReg.contains(hechoAudi.getUsuario().getCedula())) {

				usuarioRegistrado = true;

				usuario = new DimensionUsuario();

				DimensionUsuario usuarioAuditoria = hechoAudi.getUsuario();

				usuarioAuditoria.setId(usuario.getId());
				
				usuario = usuarioAuditoria;
				
//				usuario.setApellido(usuarioAuditoria.getApellido());
//				usuario.setCargo(usuarioAuditoria.getCargo());
//				usuario.setCedula(usuarioAuditoria.getCedula());
//				usuario.setEdad(calcularEdad("27/09/1995"));
//				usuario.setGenero(usuarioAuditoria.getGenero());
//				usuario.setNombre(usuarioAuditoria.getNombre());
//				usuario.setTipoUsuario(usuarioAuditoria.getTipoUsuario());

				em.crearDimensionUsuario(usuario);

				listaUsuariosReg.add(usuario);
				cedulasReg.add(usuario.getCedula());

			}

			if (!usuarioRegistrado && usuario == null) {

				for (DimensionUsuario dimensionUsuario : listaUsuariosReg) {

					if (dimensionUsuario.getCedula().equals(hechoAudi.getUsuario().getCedula())) {
						
						hechoAudi.setUsuario(dimensionUsuario);
						break;

					}

				}

			} else {

				hechoAudi.setUsuario(usuario);

			}

			em.crearHechoAuditoria(hechoAudi);

			// em.editarDimensionUsuario(usuario);

		}

	}

	public List<HechoAuditoria> obtnerHechoAuditoriaRollingMes(String fecha, int bd, List<HechoAuditoria> listaHechos) {

		List<Auditoria> facturas = obtenerAuditoriasPorMes(fecha, bd);

		if (facturas.size() == 0) {

			throw new ExcepcionNegocio("No hay Auditorias registradas en el mes ingresado");

		} else {

			em.setBd(bd);

			listaHechos = crearHechosAuditoria(facturas, listaHechos);

		}

		return listaHechos;

	}

	public List<HechoAuditoria> obtnerHechoAuditoriasRollingAnio(String fecha, int bd,
			List<HechoAuditoria> listaHechos) {

		List<Auditoria> facturas = obtenerFacturasPorAnio(fecha, bd);

		if (facturas.size() == 0) {

			throw new ExcepcionNegocio("No hay auditorias registradas en el anio ingresado");

		} else {

			em.setBd(bd);

			listaHechos = crearHechosAuditoria(facturas, listaHechos);

		}

		return listaHechos;

	}

	private List<Auditoria> obtenerAuditoriasPorMes(String fecha, int bd) {

		String datos[] = fecha.split("-");

		int mes = Integer.parseInt(datos[1]);
		int anio = Integer.parseInt(datos[0]);

		List<Auditoria> audis = new ArrayList<Auditoria>();

		List<Object> codigos = em.listaMesAuditoria(mes, anio, bd);

		for (Object object : codigos) {

			int cod = (Integer) object;

			em.setBd(bd);

			Auditoria factura = (Auditoria) em.buscar(Auditoria.class, cod);

			audis.add(factura);

		}

		return audis;

	}

	private List<Auditoria> obtenerFacturasPorAnio(String fecha, int bd) {

		String datos[] = fecha.split("-");

		int anio = Integer.parseInt(datos[0]);

		List<Auditoria> facturas = new ArrayList<Auditoria>();

		List<Object> codigos = em.listaAuditoriasAnio(anio, bd);

		for (Object object : codigos) {

			int cod = (Integer) object;

			em.setBd(bd);

			Auditoria factura = (Auditoria) em.buscar(Auditoria.class, cod);

			facturas.add(factura);

		}

		return facturas;

	}

	private List<HechoAuditoria> crearHechosAuditoria(List<Auditoria> listaAudi, List<HechoAuditoria> listaHechos) {

		// Se recorre la lista obtenida de la bd
		for (Auditoria auditorias : listaAudi) {

			String ced = auditorias.getUsuario();
			Usuario per = usuarioEJB.buscarUsu(ced);

			DimensionUsuario dimUsu = new DimensionUsuario();
			dimUsu.setNombre(per.getNombre());
			dimUsu.setApellido(per.getApellido());
			dimUsu.setCargo(per.getCargo().getDescripcion());
			dimUsu.setCedula(ced);
			dimUsu.setEdad(calcularEdad("27/09/1995"));
			dimUsu.setGenero(per.getGenero().name());
			dimUsu.setTipoUsuario(per.getTipoUsuario().getNombre());

			HechoAuditoria hechoAudi = new HechoAuditoria();
			hechoAudi.setAccion(auditorias.getAccion());
			hechoAudi.setDispositivo(auditorias.getDispositivo());
			hechoAudi.setNavegador(auditorias.getNavegador());
			hechoAudi.setUsuario(dimUsu);

			hechoAudi.setFecha(auditorias.getFechaHora());

			listaHechos.add(hechoAudi);
		}

		return listaHechos;

	}

}
