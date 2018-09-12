package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.RecentChanges;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.User;

@LocalBean
@Stateless
public class ETLWikiEJB {

	@EJB
	private Persistencia em;

	// ----------------- extracci�n de datos ----------------------------

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

		// Lista en la que se guardar�n todos los datos obtenidos
		List<RecentChanges> listaCambios = new ArrayList<RecentChanges>();

		for (Object[] objects : lista) {

			int rcId = Integer.parseInt(String.valueOf(objects[0]));
			String rcTimestamp = ((String) objects[1]);

			String datosFecha[] = rcTimestamp.split("/");

			int dia = Integer.parseInt(datosFecha[0]);
			int mes = Integer.parseInt(datosFecha[1]);
			int anio = Integer.parseInt(datosFecha[2]);

			Date fecha = new Date();
			fecha.setDate(dia);
			fecha.setMonth(mes);
			fecha.setYear(anio);

			String rcTitle = ((String) objects[2]);
			String rcComment = ((String) objects[3]);
			int rcOldLen = Integer.parseInt(String.valueOf(objects[4]));
			int rcNewLen = Integer.parseInt(String.valueOf(objects[5]));
			boolean rcNew = (1 == Integer.parseInt(String.valueOf(objects[6])));
			int userId = Integer.parseInt(String.valueOf(objects[7]));

			if (rcComment.equals("")) {
				rcComment = "Sin Resumen";
			}

			RecentChanges recentChanges = new RecentChanges();
			recentChanges.setRcId(rcId);
			recentChanges.setRcComment(rcComment);
			recentChanges.setRcNew(rcNew);
			recentChanges.setRcNewLen(rcNewLen);
			recentChanges.setRcOldLen(rcOldLen);
			recentChanges.setRcTimestamp(fecha);
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
	
	private void obtenerDatosPage(List<RecentChanges> listaCambios){
		
		for (RecentChanges recentChanges : listaCambios) {
			
			String tituloPagina = recentChanges.getRcTitle();
			
			int idPagina = em.obtenerIdPage(tituloPagina);
			
			recentChanges.setPageId(idPagina);
			
		}
		
	}

	/**
	 * Obtiene los datos del usario que realiz� alg�n cambio
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

				if (realName.equals("")) {
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

		List<String> listaUsers = new ArrayList<String>();
		List<User> usuariosRegistrados = new ArrayList<User>();
		
		User userRegistro = null;
				
		for (RecentChanges recentChanges : hecho) {

			User user = recentChanges.getUser();
			
			System.out.println("user reg " + user.getUserName());
			
			//userBuscado = (User) em.dimensionExiste(User.BUSCAR_USER, user.getUserName());
			
			boolean userRegistrado = false;

			if (!listaUsers.contains(user.getUserName())) {
				
				userRegistrado = true;
				
				userRegistro = new User();
								
				user.setUserId(userRegistro.getUserId());
				
				userRegistro = user;
				
				em.crearDimension(userRegistro);
				
				listaUsers.add(userRegistro.getUserName());
				usuariosRegistrados.add(userRegistro);
				
			}
			
			if (!userRegistrado){
				
				for (User userReg : usuariosRegistrados) {
					
					if (userReg.getUserName().equals(user.getUserName())){
						
						recentChanges.setUser(userReg);
						break;
						
					}
					
				}
				
			} else {
				
				recentChanges.setUser(userRegistro);
				
			}

			System.out.println("user registrar " + recentChanges.getUser().getUserId());
			
			em.crearHechoRecentChanges(recentChanges);

		}

	}

	/**
	 * vac�a las tablas relacionados con las ventas en la base de datos de
	 * oracle
	 */
	public void limpiarBDOracle() {

		em.eliminarDatosDWHRecentChanges();
		em.eliminarDatosDWHUser();

	}

	// ----------------------------------- Obtener Datos DWH
	// -------------------------

	public List<RecentChanges> obtenerDatosDWHWiki() {
		
		return em.obtenerDatosDWHWiki();

//		List<Object[]> lista = em.obtenerDatosDWHWiki();
//
//		List<RecentChanges> listaObtenida = new ArrayList<RecentChanges>();
//
//		for (Object[] objects : lista) {
//
//			int rcId = Integer.parseInt(String.valueOf(objects[0]));
//			String rcTimestamp = String.valueOf(objects[1]);
//			String rcTitle = String.valueOf(objects[2]);
//			String rcComment = String.valueOf(objects[3]);
//			int rcOldLen = Integer.parseInt(String.valueOf(objects[4]));
//			int rcNewLen = Integer.parseInt(String.valueOf(objects[5]));
//			boolean rcNew = (1 == Integer.parseInt(String.valueOf(objects[6])));
//			int userId = Integer.parseInt(String.valueOf(objects[7]));
//			int pageId = Integer.parseInt(String.valueOf(objects[8]));
//			String userName = String.valueOf(objects[9]);
//			String userRealName = String.valueOf(objects[10]);
//			String text = String.valueOf(objects[11]);
//
//			RecentChanges recentChanges = new RecentChanges();
//			recentChanges.setRcId(rcId);
//			recentChanges.setRcComment(rcComment);
//			recentChanges.setRcNew(rcNew);
//			recentChanges.setRcNewLen(rcNewLen);
//			recentChanges.setRcOldLen(rcOldLen);
//			// recentChanges.setRcTimestamp(rcTimestamp);
//			recentChanges.setRcTitle(rcTitle);
//
//			User user = new User();
//			user.setUserId(userId);
//			user.setUserName(userName);
//			user.setUserRealName(userRealName);
//
//			recentChanges.setUser(user);
//			// recentChanges.setPage(page);
//
//			listaObtenida.add(recentChanges);
//
//		}
//
//		return listaObtenida;

	}

}
