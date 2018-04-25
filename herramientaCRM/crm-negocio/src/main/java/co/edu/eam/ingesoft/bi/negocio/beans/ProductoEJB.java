package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@LocalBean
@Stateless
public class ProductoEJB {
	
	@PersistenceContext
	private EntityManager em;

	public void registrarProducto(Producto producto) {
		// TODO Auto-generated method stub
		em.persist(producto);
	}

	public Producto buscarProducto(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
