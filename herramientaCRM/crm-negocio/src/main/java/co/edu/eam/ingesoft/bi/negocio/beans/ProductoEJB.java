package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder.In;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVentaPK;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProductoPK;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;

@LocalBean
@Stateless
public class ProductoEJB {

	@EJB
	private Persistencia em;

	/**
	 * Edita un producto en la BD
	 * 
	 * @param producto
	 *            producto que se desea editar
	 */
	public void editarProducto(Producto producto) {
		em.setBd(ConexionEJB.getBd());
		em.editar(producto);
	}

	/**
	 * Edita un inventario de un producto
	 * 
	 * @param ip
	 *            inventario del producto a editar
	 */
	public void editarInventarioProducto(InventarioProducto ip) {
		em.setBd(ConexionEJB.getBd());
		em.editarInventarioProducto(ip);
	}

	/**
	 * Lista los tipos de producto regstrados en la base de datos
	 * 
	 * @return la lista de tipos de producto registrados
	 */
	public List<TipoProducto> listarTiposProducto() {
		em.setBd(ConexionEJB.getBd());
		return (List<TipoProducto>) (Object) em.listar(TipoProducto.LISTAR);
	}

	/**
	 * Registra un producto en la BD
	 * 
	 * @param producto
	 *            producto que se desea registrar
	 */
	public void registrarProducto(Producto producto) throws ExcepcionNegocio {
		// TODO Auto-generated method stub
		if (buscarProducto(producto.getId()) != null) {
			throw new ExcepcionNegocio("El producto ya existe");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.crear(producto);
		}
	}

	/**
	 * Verifica si ya hay un producto registrada en un inventario determinado
	 * 
	 * @param inventario
	 *            el inventario
	 * @param producto
	 *            el producto
	 * @return true si el producto ya se encuentra en el inventario, de lo
	 *         contrario false
	 */
	public boolean validarRegistroInventarioProducto(Inventario inventario, Producto producto) {
		em.setBd(ConexionEJB.getBd());
		List<InventarioProducto> lista = (List<InventarioProducto>) (Object) em
				.listarConDosParametrosObjeto(InventarioProducto.BUSCAR_POR_PRODUCTO_INVENTARIO, inventario, producto);
		if (lista.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Busca un producto por su id
	 * 
	 * @param id
	 *            id del producto a buscar
	 * @return el producto si lo encuentra, de lo contrario null
	 */
	public Producto buscarProducto(int id) {
		// TODO Auto-generated method stub
		em.setBd(ConexionEJB.getBd());
		return (Producto) em.buscar(Producto.class, id);
	}

	/**
	 * Busca el lote de un producto en la base de datos
	 * 
	 * @param codigo
	 *            código del lote
	 * @return el lote si lo encuetra, de lo contrario null
	 */
	public Lote buscarloteProducto(int codigo) {
		em.setBd(ConexionEJB.getBd());
		return (Lote) em.buscar(Lote.class, codigo);
	}

	/**
	 * Lista los productos registrados
	 * 
	 * @return la lista de productos registrados
	 */
	public List<Producto> listarProductos() {
		em.setBd(ConexionEJB.getBd());
		return (List<Producto>) (Object) em.listar(Producto.LISTAR);
	}

	/**
	 * Lista los productos registrados
	 * 
	 * @return la lista de productos registrados
	 */
	public List<InventarioProducto> listarInventariosProductos() {
		em.setBd(ConexionEJB.getBd());
		return (List<InventarioProducto>) (Object) em.listar(InventarioProducto.LISTAR);
	}

	/**
	 * Elimina un producto de la BD
	 * 
	 * @param producto
	 *            producto que se desea eliminar
	 */
	public void eliminarProducto(Producto producto) {
		em.setBd(ConexionEJB.getBd());
		em.eliminar(producto);
	}

	public InventarioProducto buscarInventarioProducto(Producto producto) {
		em.setBd(ConexionEJB.getBd());
		List<InventarioProducto> lista = (List<InventarioProducto>) (Object) em
				.listarConParametroObjeto(InventarioProducto.BUSCAR_INVENTARIO_PRODUCTO, producto);
		return lista.get(0);
	}

	public List<InventarioProducto> inventariosProducto(Producto prod) {
		em.setBd(ConexionEJB.getBd());
		List<InventarioProducto> lista = (List<InventarioProducto>) (Object) em
				.listarConParametroObjeto(InventarioProducto.BUSCAR_INVENTARIO_PRODUCTO, prod);
		return lista;
	}

	public List<Inventario> listarInventarios() {
		em.setBd(ConexionEJB.getBd());
		return (List<Inventario>) (Object) em.listar(Inventario.LISTAR);
	}

	/**
	 * Obtiene la lista de lotes registrados en la base de datos
	 * 
	 * @return la lista de lotes
	 */
	public List<Lote> lotes() {
		em.setBd(ConexionEJB.getBd());
		return (List<Lote>) (Object) em.listar(Lote.LISTA_LOTES);
	}

	public Inventario buscarNombre(String nombre) {
		em.setBd(ConexionEJB.getBd());
		List<Inventario> lista = (List<Inventario>) (Object) em.listarConParametroString(Inventario.BUSCAR_NOMBRE,
				nombre);
		if (lista.size() != 0) {
			return lista.get(0);
		}
		return null;
	}

	public Inventario buscarInventarioId(int id) {
		em.setBd(ConexionEJB.getBd());
		return (Inventario) em.buscar(Inventario.class, id);
	}

	public void registrarInventario(Inventario inventario) throws ExcepcionNegocio {
		if (buscarNombre(inventario.getNombre()) != null) {
			throw new ExcepcionNegocio("Ya existe un inventario con este nombre");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.crear(inventario);
		}
	}

	public void editarInventario(Inventario inventario) {
		em.setBd(ConexionEJB.getBd());
		em.editar(inventario);
	}

	public void eliminarInventario(Inventario inventario) {
		em.setBd(ConexionEJB.getBd());
		em.eliminar(inventario);
	}

	public void registrarInventarioProducto(InventarioProducto ip) throws ExcepcionNegocio{
		
		if (validarRegistroInventarioProducto(ip.getInventarioId(), ip.getProductoId())){
			em.setBd(ConexionEJB.getBd());
			em.registrarInventarioProducto(ip);
		} else {
			throw new ExcepcionNegocio("Este producto ya esta asignado a este inventario");
		}
		
	}
	
	public void eliminarInventarioProducto(InventarioProducto ip){
		em.setBd(ConexionEJB.getBd());
		em.eliminarInventarioProducto(ip);
	}

	public InventarioProducto buscarInventarioProdPK(InventarioProductoPK inventario) {
		em.setBd(ConexionEJB.getBd());
		return (InventarioProducto) em.buscar(InventarioProducto.class, inventario);
	}

}
