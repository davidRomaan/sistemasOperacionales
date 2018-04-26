package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@Stateless
public class ProductoEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Edita un producto en la BD
	 * @param producto producto que se desea editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarProducto(Producto producto){
		em.merge(producto);
	}

	/**
	 * Registra un producto en la BD
	 * @param producto producto que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarProducto(Producto producto) {
		// TODO Auto-generated method stub
		em.persist(producto);
	}

	/**
	 * Busca un producto por su id
	 * @param id id del producto a buscar
	 * @return el producto si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Producto buscarProducto(int id) {
		// TODO Auto-generated method stub
		return em.find(Producto.class, id);
	}
	
	/**
	 * Lista los productos registrados
	 * @return la lista de productos registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Producto> listarProductos(){
		Query q = em.createNamedQuery(Producto.LISTAR);
		List<Producto> lista = q.getResultList();
		return lista;
	}
	
	/**
	 * Elimina un producto de la BD
	 * @param producto producto que se desea eliminar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarProducto(Producto producto){
		em.remove(em.contains(producto) ? producto : em.merge(producto));
	}
	
	/**
	 * Obtiene la lista de lotes registrados en la base de datos
	 * @return la lista de lotes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Lote> lotes(){
		Query q = em.createNamedQuery(Lote.LISTA_LOTES);
		List<Lote> lotes = q.getResultList();
		return lotes;
	}

}
