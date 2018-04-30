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
import javax.persistence.criteria.CriteriaBuilder.In;

import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;

@LocalBean
@Stateless
public class ProductoEJB {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Edita un producto en la BD
	 * 
	 * @param producto
	 *            producto que se desea editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarProducto(Producto producto) {
		em.merge(producto);
	}

	/**
	 * Edita un inventario de un producto
	 * 
	 * @param ip
	 *            inventario del producto a editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarInventarioProducto(InventarioProducto ip) {
		em.merge(ip);
	}

	/**
	 * Lista los tipos de producto regstrados en la base de datos
	 * 
	 * @return la lista de tipos de producto registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoProducto> listarTiposProducto() {
		Query q = em.createNamedQuery(TipoProducto.LISTAR);
		List<TipoProducto> tipos = q.getResultList();
		return tipos;
	}

	/**
	 * Registra un producto en la BD
	 * 
	 * @param producto
	 *            producto que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarProducto(Producto producto) throws ExcepcionNegocio {
		// TODO Auto-generated method stub
		if (buscarProducto(producto.getId()) != null) {
			throw new ExcepcionNegocio("El código del producto ya existe");
		} else {
			em.persist(producto);
		}
	}

	/**
	 * Busca un producto por su id
	 * 
	 * @param id
	 *            id del producto a buscar
	 * @return el producto si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Producto buscarProducto(int id) {
		// TODO Auto-generated method stub
		return em.find(Producto.class, id);
	}

	/**
	 * Busca el lote de un producto en la base de datos
	 * 
	 * @param codigo
	 *            código del lote
	 * @return el lote si lo encuetra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Lote buscarloteProducto(int codigo) {
		return em.find(Lote.class, codigo);
	}

	/**
	 * Lista los productos registrados
	 * 
	 * @return la lista de productos registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Producto> listarProductos() {
		Query q = em.createNamedQuery(Producto.LISTAR);
		List<Producto> lista = q.getResultList();
		return lista;
	}

	/**
	 * Lista los productos registrados
	 * 
	 * @return la lista de productos registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<InventarioProducto> listarInventariosProductos() {
		Query q = em.createNamedQuery(InventarioProducto.LISTAR);
		List<InventarioProducto> lista = q.getResultList();
		return lista;
	}

	/**
	 * Elimina un producto de la BD
	 * 
	 * @param producto
	 *            producto que se desea eliminar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarProducto(Producto producto) {
		em.remove(em.contains(producto) ? producto : em.merge(producto));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public InventarioProducto buscarInventarioProducto (Producto producto){
		Query q = em.createNamedQuery(InventarioProducto.BUSCAR_INVENTARIO_PRODUCTO);
		q.setParameter(1, producto);
		List<InventarioProducto> lista = q.getResultList();
		return lista.get(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<InventarioProducto> inventariosProducto (Producto prod){
		Query q = em.createNamedQuery(InventarioProducto.BUSCAR_INVENTARIO_PRODUCTO);
		q.setParameter(1, prod);
		List<InventarioProducto> lista = q.getResultList();
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Inventario> listarInventarios(){
		Query q = em.createNamedQuery(Inventario.LISTAR);
		List<Inventario> lista = q.getResultList();
		return lista;
 	}

	/**
	 * Obtiene la lista de lotes registrados en la base de datos
	 * 
	 * @return la lista de lotes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Lote> lotes() {
		Query q = em.createNamedQuery(Lote.LISTA_LOTES);
		List<Lote> lotes = q.getResultList();
		return lotes;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Inventario buscarNombre(String nombre) {
		Query q = em.createNamedQuery(Inventario.BUSCAR_NOMBRE);
		q.setParameter(1, nombre);
		List<Inventario> lista = q.getResultList();
		if (lista.size() != 0) {
			return lista.get(0);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Inventario buscarInventarioId (int id){
		return em.find(Inventario.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarInventario(Inventario inventario) throws ExcepcionNegocio {
		if (buscarNombre(inventario.getNombre()) != null) {
			throw new ExcepcionNegocio("Ya existe un inventario con este nombre");
		} else {
			em.persist(inventario);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarInventario(Inventario inventario) {
		em.merge(inventario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarInventario(Inventario inventario) {
		em.remove(em.contains(inventario) ? inventario : em.merge(inventario));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Inventario> listarInventario() {
		Query q = em.createNamedQuery(Inventario.LISTAR);
		List<Inventario> lista = q.getResultList();
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarInventarioProducto (InventarioProducto ip){
		em.merge(ip);
	}

}
