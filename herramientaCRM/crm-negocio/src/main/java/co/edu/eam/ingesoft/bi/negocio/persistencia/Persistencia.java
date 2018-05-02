package co.edu.eam.ingesoft.bi.negocio.persistencia;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;

@LocalBean
@Stateless
public class Persistencia  implements Serializable{
	
	/**
	 * Conexión a MYSQL (1)
	 */
	@PersistenceContext(unitName = "mysql")
	private EntityManager emM;
	
	/**
	 * Conexión a postgress (2)
	 */
	@PersistenceContext(unitName = "postgres")
	private EntityManager emP;
	
	/**
	 * Base de datos en la cual se están realizando las operaciones
	 */
	private int bd;
	
	/**
	 * Guarda en la base de datos
	 */
	public void crear(Object objeto){
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
	public void editar(Object objeto){
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
	public void eliminar(Object objeto){
		switch (this.bd) {
		case 1:
			emM.remove(objeto);
			break;
		case 2:
			emP.remove(objeto);
			break;
		default:
			throw new ExcepcionNegocio("La base de datos a la cual intenta acceder no existe");
		}
	}
	
	/**
	 * Busca en una base de datos
	 * @param objeto que se desea buscar
	 * @param pk llave primaria del objeto a buscar
	 * @return el objeto si lo encuentra, de lo contrario null
	 */
	public Object buscar(Class objeto, Object pk){
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
	 * @param sql consulta a la bd
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listar(String sql){
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
	 * Listar objetos usando un parametro de tipo objeto y la sql
	 * @param sql consulta que se desea ejectar
	 * @param objeto El objeto parámetro 
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listarConParametroObjeto(String sql, Object objeto){
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
	 * @param sql consulta a ejecutar, nos traera objetos de una determinada tabla
	 * @param parametro el parametro necesario para la consulta
	 * @return lista de los objetos encontrados
	 */
	public List<Object> listarConParametroString(String sql, String parametro){
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
	 * Accesores Y Modificadores
	 */
	
	public int getBd() {
		return bd;
	}

	public void setBd(int bd) {
		this.bd = bd;
	}	

}
