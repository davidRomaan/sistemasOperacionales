package co.edu.eam.ingesoft.bi.negocio.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.ModulosUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionFactura;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionMunicipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionUsuario;

@LocalBean
@Stateless
public class Persistencia implements Serializable {

	/**
	 * Conexi�n a MYSQL (1)
	 */
	@PersistenceContext(unitName = "mysql")
	private EntityManager emM;

	/**
	 * Conexi�n a postgress (2)
	 */
	@PersistenceContext(unitName = "postgres")
	private EntityManager emP;

	/**
	 * Conexi�n para Oracle (3)
	 */
	@PersistenceContext(unitName = "oracle")
	private EntityManager emO;

	/**
	 * Base de datos en la cual se est�n realizando las operaciones
	 */
	private int bd;

	/**
	 * Crea un objeto en todas las bases de datos
	 * 
	 * @param objeto
	 *            el objeto que se desea persistir
	 */
	public void crearEnTodasBD(Object objeto) {
		registrarMysql(objeto);
		registrarPostgres(objeto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void registrarMysql(Object o) {
		emM.persist(o);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarPostgres(Object objeto) {
		emP.persist(objeto);
	}

	/**
	 * Edita un objeto en todas las bases de datos
	 * 
	 * @param objeto
	 *            el objeto que se editar
	 */
	public void editarEnTodasBD(Object objeto) {
		emM.merge(objeto);
		emP.merge(objeto);
	}

	/**
	 * Elimina un objeto en todas las bases de datos
	 * 
	 * @param objeto
	 *            el objeto que se desea eliminar
	 */
	public void eliminarEnTodasBD(Object objeto) {
		emM.remove(emM.merge(objeto));
		emP.remove(emP.merge(objeto));
	}

	/**
	 * Busca un objeto en todas las beses de datos
	 * 
	 * @param c
	 *            clase que se desea buscar
	 * @param pk
	 *            identificador de la clase
	 * @return el objeto si lo ecuentra, de lo contrario null
	 */
	public Object buscarEnTodasBD(Class c, Object pk) {
		Object objectMysql = emM.find(c, pk);
		Object objetoPostgres = emP.find(c, pk);

		if (objectMysql != null) {
			return objectMysql;
		}
		if (objetoPostgres != null) {
			return objetoPostgres;
		}
		return null;
	}

	/**
	 * Guarda en la base de datos
	 */
	public void crear(Object objeto) {
		switch (this.bd) {
		case 1:
			emM.persist(objeto);
			break;
		case 2:
			emP.persist(objeto);
			break;
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Edita en la base de datos
	 */
	public void editar(Object objeto) {
		switch (this.bd) {
		case 1:
			emM.merge(objeto);
			break;
		case 2:
			emP.merge(objeto);
			break;
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Elimina de la base de datos
	 */
	public void eliminar(Object objeto) {
		switch (this.bd) {
		case 1:
			emM.remove(emM.merge(objeto));
			break;
		case 2:
			emP.remove(emP.merge(objeto));
			break;
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Busca en una base de datos
	 * 
	 * @param objeto
	 *            que se desea buscar
	 * @param pk
	 *            llave primaria del objeto a buscar
	 * @return el objeto si lo encuentra, de lo contrario null
	 */
	public Object buscar(Class objeto, Object pk) {
		switch (this.bd) {
		case 1:
			return emM.find(objeto, pk);
		case 2:
			return emP.find(objeto, pk);
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Listar objetos
	 * 
	 * @param sql
	 *            consulta a la bd
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listar(String sql) {
		switch (this.bd) {
		case 1:
			Query q = emM.createNamedQuery(sql);
			return q.getResultList();
		case 2:
			Query p = emP.createNamedQuery(sql);
			return p.getResultList();
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Lista con dos par�metros de tipo objeto
	 * 
	 * @param sql
	 *            Consulta a ejecutar
	 * @param param1
	 *            parametro 1
	 * @param param2
	 *            parametro 2
	 * @return la lista
	 */
	public List<Object> listarConDosParametrosObjeto(String sql, Object param1, Object param2) {

		Query q;

		switch (this.bd) {

		case 1:
			q = emM.createNamedQuery(sql);
			break;

		case 2:
			q = emP.createNamedQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		q.setParameter(1, param1);
		q.setParameter(2, param2);
		return q.getResultList();

	}

	/**
	 * Listar objetos usando un parametro de tipo objeto y la sql
	 * 
	 * @param sql
	 *            consulta que se desea ejectar
	 * @param objeto
	 *            El objeto par�metro
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listarConParametroObjeto(String sql, Object objeto) {
		switch (this.bd) {
		case 1:
			Query q = emM.createNamedQuery(sql);
			q.setParameter(1, objeto);
			return q.getResultList();
		case 2:
			Query p = emP.createNamedQuery(sql);
			p.setParameter(1, objeto);
			return p.getResultList();
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Listar objetos usando un parametro String
	 * 
	 * @param sql
	 *            consulta a ejecutar, nos traera objetos de una determinada
	 *            tabla
	 * @param parametro
	 *            el parametro necesario para la consulta
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listarConParametroString(String sql, String parametro) {
		switch (this.bd) {
		case 1:
			Query q = emM.createNamedQuery(sql);
			q.setParameter(1, parametro);
			return q.getResultList();
		case 2:
			Query p = emP.createNamedQuery(sql);
			p.setParameter(1, parametro);
			return p.getResultList();
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * trae una lista de objetos que reciben un par�metro int
	 * 
	 * @param sql
	 *            consulta que se desea realizar en la bd
	 * @param parametro
	 *            con el cual se desea listar
	 * @return la lista de objetos
	 */
	public List<Object> listarConParametroInt(String sql, int parametro) {
		switch (this.bd) {
		case 1:
			Query q = emM.createNamedQuery(sql);
			q.setParameter(1, parametro);
			return q.getResultList();
		case 2:
			Query p = emP.createNamedQuery(sql);
			p.setParameter(1, parametro);
			return p.getResultList();
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}

	/**
	 * Registra un detalle de venta
	 * 
	 * @param detalles
	 *            detalles de venta que se desea registar
	 * @param factura
	 *            factura que se desea registrar
	 */
	public void registrarDetalleVenta(List<DetalleVenta> detalles, FacturaVenta factura) {

		String sql = "INSERT INTO DETALLE_VENTA (factura_venta_id, "
				+ "producto_id, cantidad, subtotal) VALUES (?1,?2,?3,?4)";

		switch (this.bd) {
		case 1:
			for (DetalleVenta dv : detalles) {

				Query q = emM.createNativeQuery(sql);
				q.setParameter(1, factura.getId());
				q.setParameter(2, dv.getProducto().getId());
				q.setParameter(3, dv.getCantidad());
				q.setParameter(4, dv.getSubtotal());
				q.executeUpdate();

			}
			break;

		case 2:
			for (DetalleVenta dv : detalles) {

				Query q = emP.createNativeQuery(sql);
				q.setParameter(1, factura.getId());
				q.setParameter(2, dv.getProducto().getId());
				q.setParameter(3, dv.getCantidad());
				q.setParameter(4, dv.getSubtotal());
				q.executeUpdate();

			}
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

	}

	public List<Persona> listarClientes() {
		String sql = "select p.cedula from persona p left join usuario u on u.cedula = p.cedula where u.cedula is null";

		List<String> lista;

		switch (this.bd) {

		case 1:
			Query q = emM.createNativeQuery(sql);
			lista = q.getResultList();
			break;

		case 2:
			Query query = emP.createNativeQuery(sql);
			lista = query.getResultList();
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		List<Persona> clientes = new ArrayList<Persona>();
		for (String cedula : lista) {
			Persona p = (Persona) buscar(Persona.class, cedula);
			clientes.add(p);
		}
		return clientes;

	}

	/**
	 * Edita un inventario de un producto
	 * 
	 * @param ip
	 *            inventario del producto a editar
	 */
	public void editarInventarioProducto(InventarioProducto ip) {

		String sql = "UPDATE INVENTARIO_PRODUCTO SET cantidad = ?1 WHERE inventario_id = ?2 AND producto_id = ?3";

		switch (this.bd) {
		case 1:
			Query q = emM.createNativeQuery(sql);
			q.setParameter(1, ip.getCantidad());
			q.setParameter(2, ip.getInventarioId().getId());
			q.setParameter(3, ip.getProductoId().getId());
			q.executeUpdate();
			break;

		case 2:
			Query query = emP.createNativeQuery(sql);
			query.setParameter(1, ip.getCantidad());
			query.setParameter(2, ip.getInventarioId().getId());
			query.setParameter(3, ip.getProductoId().getId());
			query.executeUpdate();
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

	}

	public void registrarInventarioProducto(InventarioProducto ip) {

		String sql = "INSERT INTO INVENTARIO_PRODUCTO (inventario_id, producto_id, cantidad) VALUES (?1,?2,?3)";

		Query query;

		switch (this.bd) {
		case 1:
			query = emM.createNativeQuery(sql);
			break;

		case 2:
			query = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		query.setParameter(1, ip.getInventarioId().getId());
		query.setParameter(2, ip.getProductoId().getId());
		query.setParameter(3, ip.getCantidad());
		query.executeUpdate();

	}

	public Persona buscarCliente(String cedula) {

		switch (this.bd) {

		case 1:
			Query q = emM.createNativeQuery("SELECT * FROM USUARIO WHERE cedula = ?1");
			q.setParameter(1, cedula);
			List<Persona> lista = q.getResultList();
			if (lista.size() == 0) {
				return (Persona) buscar(Persona.class, cedula);
			}
			return null;

		case 2:
			Query query = emP.createNativeQuery("SELECT * FROM USUARIO WHERE cedula = ?1");
			query.setParameter(1, cedula);
			List<Persona> listaP = query.getResultList();
			if (listaP.size() == 0) {
				return (Persona) buscar(Persona.class, cedula);
			}
			return null;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");

		}

	}

	/**
	 * Obtiene el �ltimo c�digo de la factura generada a un cliente
	 * 
	 * @param cedulaCliente
	 *            c�dula del clienteal cual se le gener� la factura
	 * @return el id de la �ltima factura
	 */
	public int codigoUltimaFacturaCliente(String cedulaCliente) {
		switch (this.bd) {
		case 1:
			Query q = emM.createNamedQuery(FacturaVenta.OBTENER_ULTIMA_REGISTRADA);
			q.setParameter(1, cedulaCliente);
			return (int) ((Integer) q.getSingleResult());

		case 2:
			Query query = emP.createNamedQuery(FacturaVenta.OBTENER_ULTIMA_REGISTRADA);
			query.setParameter(1, cedulaCliente);
			return (int) ((Integer) query.getSingleResult());

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

	}

	/**
	 * Elimina un inventario producto
	 * 
	 * @param ip
	 *            inventario producto que se desea eliminar
	 */
	public void eliminarInventarioProducto(InventarioProducto ip) {

		String sql = "DELETE FROM INVENTARIO_PRODUCTO WHERE inventario_id = ?1 AND producto_id = ?2";

		Query q;

		switch (this.bd) {

		case 1:
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");

		}

		q.setParameter(1, ip.getInventarioId().getId());
		q.setParameter(2, ip.getProductoId().getId());
		q.executeUpdate();

	}

	public void eliminarDetalleVenta(DetalleVenta dv) {

		String sql = "DELETE FROM DETALLE_VENTA WHERE factura_venta_id = ?1 AND producto_id = ?2";

		Query q;

		switch (this.bd) {
		case 1:
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		q.setParameter(1, dv.getFacturaVenta().getId());
		q.setParameter(2, dv.getProducto().getId());
		q.executeUpdate();

	}

	/**
	 * Registra un m�dulo usuario en la base de datos
	 * 
	 * @param mu
	 *            modulo usuario que se desea registrar
	 */
	public void registrarModuloUsuario(ModulosUsuario mu) {

		String sql = "INSERT INTO modulos_usuario (modulo_id, tipousuario_id) VALUES (?1,?2)";

		Query q;

		switch (this.bd) {

		case 1:
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		q.setParameter(1, mu.getModulo_id().getId());
		q.setParameter(2, mu.getTipoUsiario_id().getId());
		q.executeUpdate();

	}

	/**
	 * Lista las ventas realizadas en una fecha determinada
	 * 
	 * @param dia
	 *            d�a de la venta
	 * @param mes
	 *            mes de la venta
	 * @param anio
	 *            a�o de la venta
	 * @return las ventas de la fecha ingresada
	 */
	public List<FacturaVenta> listarFacturasPorFecha(String sql, int dia, int mes, int anio) {

		Query q;

		switch (this.bd) {
		case 1:
			q = emM.createNamedQuery(sql);
			break;

		case 2:
			q = emP.createNamedQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}

		q.setParameter(1, dia);
		q.setParameter(2, mes);
		q.setParameter(3, anio);
		return q.getResultList();

	}

	/**
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public List<Object> listarAuditoriasIntervaloFecha(String fechaInicio, String fechaFin) {

		String sql = "";
		Query q;

		switch (this.bd) {

		case 1:
			sql = "SELECT id FROM bi.AUDITORIA WHERE fecha_hora BETWEEN " + "'" + fechaInicio + "' AND " + "'"
					+ fechaFin + "'";
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			sql = "SELECT id FROM AUDITORIA WHERE fecha_hora BETWEEN " + "'" + fechaInicio + "' AND " + "'" + fechaFin
					+ "'";
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");

		}

		List<Object> lista = q.getResultList();

		return lista;

	}

	public List<Object> listarFacturasIntervaloFecha(String fechaInicio, String fechaFin) {

		String sql = "";
		Query q;

		switch (this.bd) {

		case 1:
			sql = "SELECT id FROM bi.FACTURA_VENTA WHERE fecha_venta BETWEEN " + "'" + fechaInicio + "' AND " + "'"
					+ fechaFin + "'";
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			sql = "SELECT id FROM FACTURA_VENTA WHERE fecha_venta BETWEEN " + "'" + fechaInicio + "' AND " + "'"
					+ fechaFin + "'";
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");

		}

		List<Object> lista = q.getResultList();

		System.out.println("tamanio lista: " + lista.size());

		return lista;

	}

	public List<Object> listarFechaActual(String fechaAct) {

		String sql = "";
		Query q;

		switch (this.bd) {

		case 1:
			sql = "SELECT id FROM bi.auditoria WHERE fecha_hora = " + "'" + fechaAct + "'";
			q = emM.createNativeQuery(sql);
			break;

		case 2:
			sql = "SELECT id FROM auditoria WHERE fecha_hora = " + "'" + fechaAct + "'";
			q = emP.createNativeQuery(sql);
			break;

		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");

		}

		List<Object> lista = q.getResultList();

		System.out.println("tamanio lista: " + lista.size());

		return lista;

	}

	// ------------------------------ Gestion del dataWareHouse
	// -------------------------------

	/**
	 * Crea un hecho venta en la bd oracle
	 * 
	 * @param unidades
	 *            unidades vendidas
	 * @param subtotal
	 *            subtotal de la compra
	 * @param idFactura
	 *            c�digo de la factua
	 * @param cedulaCliente
	 *            c�dula del cliente que compr�
	 * @param idMunicipio
	 *            c�digo del municipio donde se realiz� la venta
	 * @param idProducto
	 *            c�digo del procuto que se vendi�
	 * @param cedulaEmpleado
	 *            c�dula del empleado que realiz� la venta
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearHechoVentas(int unidades, double subtotal, int idFactura, String cedulaCliente, int idMunicipio,
			int idProducto, String cedulaEmpleado) {

		String sql = "INSERT INTO HECHO_VENTAS (unidades, subtotal, factura_id, municipio_id, producto_id, "
				+ "cedula_empleado, cedula_cliente) VALUES (?1,?2,?3,?4,?5,?6,?7)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, unidades);
		q.setParameter(2, subtotal);
		q.setParameter(3, idFactura);
		q.setParameter(4, idMunicipio);
		q.setParameter(5, idProducto);
		q.setParameter(6, cedulaEmpleado);
		q.setParameter(7, cedulaCliente);
		q.executeUpdate();

	}

	/**
	 * Crea una dimensi�n de producto en la bd
	 * 
	 * @param dimension
	 *            dimensi�n del procuto que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearDimensionProducto(DimensionProducto dimension) {

		String sql = "INSERT INTO DIMENSION_PRODUCTO (id, nombre, precio, tipo_producto) VALUES (?1, ?2, ?3, ?4)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, dimension.getId());
		q.setParameter(2, dimension.getNombre());
		q.setParameter(3, dimension.getPrecio());
		q.setParameter(4, dimension.getTipoProducto());
		q.executeUpdate();

	}

	/**
	 * Crea una dimensi�n de persona en la bd oracle
	 * 
	 * @param dimension
	 *            dimensi�n de la persona que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearDimensionPersona(DimensionPersona dimension) {

		String sql = "INSERT INTO DIMENSION_PERSONA (cedula, nombre, apellido, genero, edad, tipo_persona) "
				+ "VALUES (?1,?2,?3,?4,?5,?6)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, dimension.getCedula());
		q.setParameter(2, dimension.getNombre());
		q.setParameter(3, dimension.getApellido());
		q.setParameter(4, dimension.getGenero());
		q.setParameter(5, dimension.getEdad());
		q.setParameter(6, dimension.getTipoPersona());
		q.executeUpdate();

	}
	
	/**
	 * Crea una dimensi�n de persona en la bd oracle
	 * 
	 * @param dimension
	 *            dimensi�n de la persona que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearDimensionUsuario(DimensionUsuario dimension) {

	
	
		String sql = "INSERT INTO DIMENSION_PERSONA (cedula, nombre, apellido, genero, edad, tipo_usuario, cargo) "
				+ "VALUES (?1,?2,?3,?4,?5,?6,?7)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, dimension.getCedula());
		q.setParameter(2, dimension.getNombre());
		q.setParameter(3, dimension.getApellido());
		q.setParameter(4, dimension.getGenero());
		q.setParameter(5, dimension.getEdad());
		q.setParameter(6, dimension.getTipoUsuario());
		q.setParameter(7, dimension.getCargo());
		q.executeUpdate();

	}

	/**
	 * Crea una dimension de una factura en la bd Oracle
	 * 
	 * @param dimension
	 *            la dimension que se desea crear
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearDimensionFactura(DimensionFactura dimension) {

		String sql = "INSERT INTO DIMENSION_FACTURA (id, total_venta, fecha_venta) VALUES (?1,?2,?3)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, dimension.getId());
		q.setParameter(2, dimension.getTotalVenta());
		q.setParameter(3, dimension.getFecha());
		q.executeUpdate();

	}

	/**
	 * Crea una dimensi�n de un municipio
	 * 
	 * @param dimension
	 *            dimnesi�n del muncipio que se desea crear
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearDimensionMunicipio(DimensionMunicipio dimension) {

		String sql = "INSERT INTO DIMENSION_MUNICIPIO (id, nombre, departamento) VALUES (?1,?2,?3)";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, dimension.getId());
		q.setParameter(2, dimension.getNombre());
		q.setParameter(3, dimension.getDepartamento());
		q.executeUpdate();

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean dimensionPersonaExiste(String cedula) {

		String sql = "SELECT * FROM DIMENSION_PERSONA WHERE cedula = ?1";
		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, cedula);

		List<Object> lista = q.getResultList();

		if (lista.size() != 0) {
			return true;
		}

		return false;

	}

	/**
	 * Verifica si existe una dimensi�n que tiene como prmaria un integer
	 * 
	 * @param id
	 *            identificador de la tabla
	 * @param tabla
	 *            tabla donde se desea buscar
	 * @return true si existe, de lo contrario false
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean dimensionExiste(int id, String tabla) {

		String sql = "SELECT * FROM " + tabla + " WHERE id = ?1";
		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, id);

		List<Object> lista = q.getResultList();

		if (lista.size() != 0) {
			return true;
		}

		return false;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarDimensionPersona(String cedula) {

		String sql = "DELETE FROM DIMENSION_PERSONA WHERE cedula = ?1";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, cedula);
		q.executeUpdate();

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarDimensionProducto(int id) {

		String sql = "DELETE FROM DIMENSION_PRODUCTO WHERE id = ?1";

		Query q = emO.createNativeQuery(sql);
		q.setParameter(1, id);
		q.executeUpdate();

	}

	public int getBd() {
		return bd;
	}

	public void setBd(int bd) {
		this.bd = bd;
	}

}
