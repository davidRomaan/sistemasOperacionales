package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;

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

			// Se recorre la segunda lista obtenida de postgres
			for (Auditoria auditorias : audi) {

				String ced = auditorias.getUsuario();
				Usuario per = usuarioEJB.buscarUsu(ced);

				DimensionUsuario dimUsu = new DimensionUsuario();
				dimUsu.setNombre(per.getNombre());
				dimUsu.setApellido(per.getApellido());
				dimUsu.setCargo(per.getCargo().getDescripcion());
				dimUsu.setCedula(ced);
				dimUsu.setEdad(calcularEdad(per.getFechaNacimiento()));
				dimUsu.setGenero(per.getGenero().name());
				dimUsu.setTipoUsuario(per.getTipoUsuario().getNombre());

				HechoAuditoria hechoAudi = new HechoAuditoria();
				hechoAudi.setAccion(auditorias.getAccion());
				hechoAudi.setDispositivo(auditorias.getDispositivo());
				hechoAudi.setNavegador(auditorias.getNavegador());
				hechoAudi.setUsuario(dimUsu);
				
				String [] datos = auditorias.getFechaHora().split("/");
				int anio = Integer.parseInt(datos[0]);
				int mes = Integer.parseInt(datos[1]);
				int dia = Integer.parseInt(datos[2]);
				
				Calendar fecha = new GregorianCalendar();
				fecha.set(anio, mes, dia);
				
				hechoAudi.setFecha(fecha);

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

	public void cargarDatosDWH(List<HechoAuditoria> hechos) {

		boolean usuExiste;
		List<String> listaCed = new ArrayList();

		if (hechos.size() == 0) {

		} else {

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

				usuExiste = em.dimensionUsuarioExiste(hechoAudi.getUsuario().getCedula());

				if (!usuExiste) {

					for (String ced : listaCed) {
						if (ced.equals(hechoAudi.getUsuario().getCedula())) {
							em.crearHechoAuditoria(hechoAudi.getAccion(), hechoAudi.getDispositivo(),
									hechoAudi.getNavegador(), hechoAudi.getFecha(), hechoAudi.getUsuario());
						} else {
							em.crearDimensionUsuario(hechoAudi.getUsuario());
							em.crearHechoAuditoria(hechoAudi.getAccion(), hechoAudi.getDispositivo(),
									hechoAudi.getNavegador(), hechoAudi.getFecha(), hechoAudi.getUsuario());

							listaCed.add(hechoAudi.getUsuario().getCedula());
						}
					}

				} else {

					em.crearHechoAuditoria(hechoAudi.getAccion(), hechoAudi.getDispositivo(), hechoAudi.getNavegador(),
							hechoAudi.getFecha(), hechoAudi.getUsuario());

				}

			}

		}

	}

}
