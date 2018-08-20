package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.Page;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.RecentChanges;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.User;

@LocalBean
@Stateless
public class ETLWikiEJB {

	@EJB
	private Persistencia em;

	

	// ----------------- extracción de datos ----------------------------

	/**
	 * Obtiene los datos almacenados en la bd my_wiki
	 */
	public List<RecentChanges> obtenerDatosWikiAcumulacionSimple(String fecha1, String fecha2) {

		// Lista con los datos obtenidos de la bd
		List<Object[]> lista = em.obtenerCambiosRecientesAcumulacionSimple(fecha1, fecha2);
		return crearDimensiones(lista);

	}

	/**
	 * Obtiene todos los cambios realizados
	 */
	public List<RecentChanges> obtenerDatosWikiRolling() {

		List<Object[]> lista = em.obtenerCambiosRecientesRolling();
		return crearDimensiones(lista);

	}

	/**
	 * Se crean las dimensiones y la tabla de hecho con los datos obtenidos de
	 * la bd
	 * 
	 * @param lista
	 *            lista de cambios obtenida de la bd
	 * @return la lista de cambios
	 */
	private List<RecentChanges> crearDimensiones(List<Object[]> lista) {

		// Lista en la que se guardarï¿½n todos los datos obtenidos
		List<RecentChanges> listaCambios = new ArrayList<RecentChanges>();

		for (Object[] objects : lista) {

			int rcId = Integer.parseInt(String.valueOf(objects[0]));
			String rcTimestamp = ((String) objects[1]);
			String rcTitle = ((String) objects[2]);
			String rcComment = ((String) objects[3]);
			int rcOldLen = Integer.parseInt(String.valueOf(objects[4]));
			int rcNewLen = Integer.parseInt(String.valueOf(objects[5]));
			boolean rcNew = (1 == Integer.parseInt(String.valueOf(objects[6])));
			int userId = Integer.parseInt(String.valueOf(objects[7]));

			RecentChanges recentChanges = new RecentChanges();
			recentChanges.setRcId(rcId);
			recentChanges.setRcComment(rcComment);
			recentChanges.setRcNew(rcNew);
			recentChanges.setRcNewLen(rcNewLen);
			recentChanges.setRcOldLen(rcOldLen);
			recentChanges.setRcTimestamp(rcTimestamp);
			recentChanges.setRcTitle(rcTitle);

			User user = new User();
			user.setUserId(userId);

			recentChanges.setUser(user);

			listaCambios.add(recentChanges);

		}

		obtenerDatosPage(listaCambios);
		obtenerDatosUsuario(listaCambios);

		return listaCambios;

	}

	/**
	 * Obtiene los datos de las pï¿½ginas en la que se realizï¿½ cambios recientes
	 * 
	 * @param listaCambios
	 *            lista de cambios recientes realizados
	 */
	public void obtenerDatosPage(List<RecentChanges> listaCambios) {

		for (RecentChanges recentChanges : listaCambios) {

			int idPagina = em.obtenerIdPage(recentChanges.getRcTitle());

			Page pagina = new Page();

			// Si el id de la pï¿½gina es diferente de -1 quiere decir que la
			// pï¿½gina existe, de lo contrario fue eliminada
			if (idPagina != -1) {

				pagina.setText(em.obtenerTextoPagina(idPagina));
				pagina.setPageId(idPagina);

			} else {

				pagina.setText("La pï¿½gina fue eliminada");
				pagina.setPageId(-1);

			}

			recentChanges.setPage(pagina);

		}

	}

	/**
	 * Obtiene los datos del usario que realizï¿½ algï¿½n cambio
	 * 
	 * @param listaCambios
	 *            lista de cambios realizados
	 */
	public void obtenerDatosUsuario(List<RecentChanges> listaCambios) {

		for (RecentChanges recentChanges : listaCambios) {

			User usuario = recentChanges.getUser();

			int userId = usuario.getUserId();

			if (userId != 0) {

				List<Object[]> lista = em.obtenerDatosUsuario(userId);

				Object[] datos = lista.get(0);

				String userName = (String) datos[0];
				String realName = (String) datos[1];
				
				if (realName.equals("")){
					realName = "Sin nombre";
				}

				usuario.setUserName(userName);
				usuario.setUserRealName(realName);

			} else {

				usuario.setUserName("No existe");
				usuario.setUserRealName("No existe");

			}

		}

	}

	// ---------------------- carga de datos ----------------------------

	/**
	 * Carga los datos en el dwh
	 * 
	 * @param hecho
	 *            lista de hechos con sus dimensiones a cargar
	 */
	public void cargarDatosDWH(List<RecentChanges> hecho) {

		List<Integer> listaIdPaginas = new ArrayList<Integer>();
		List<Integer> listaIdUsuarios = new ArrayList<Integer>();

		for (RecentChanges recentChanges : hecho) {

			int idPage = recentChanges.getPage().getPageId();
			int userId = recentChanges.getUser().getUserId();

			if (!em.dimensionExiste(recentChanges.getRcId(), "rc_id", "RECENT_CHANGES")) {

				if (!em.dimensionExiste(idPage, "page_id", "\"BI\".\"PAGE\"") && !listaIdPaginas.contains(idPage)) {
					em.crearDimensionPage(recentChanges.getPage());
					listaIdPaginas.add(idPage);
				}

				if (!em.dimensionExiste(userId, "user_id", "\"BI\".\"USER\"") && !listaIdUsuarios.contains(userId)) {
					em.crearDimensionUser(recentChanges.getUser());
					listaIdUsuarios.add(userId);
				}
				
				em.crearHechoRecentChanges(recentChanges);

			}

		}

	}
	/**
	 * vacï¿½a las tablas relacionados con las ventas en la base de datos de
	 * oracle
	 */
	public void limpiarBDOracle() {

		em.limpiarBDOracle("\"BI\".\"RECENT_CHANGES\"");
		em.limpiarBDOracle("\"BI\".\"PAGE\"");
		em.limpiarBDOracle("\"BI\".\"USER\"");

	}
	
	
	

}
