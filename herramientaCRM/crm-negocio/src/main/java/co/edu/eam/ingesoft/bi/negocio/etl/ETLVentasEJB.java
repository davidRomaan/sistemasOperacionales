package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.hibernate.jpa.event.spi.jpa.ListenerFactory;

import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@LocalBean
@Stateless
public class ETLVentasEJB {

	@EJB
	private Persistencia em;

	
	public List<HechoVentas> listaVentasPeriodo(Calendar fechaInicio, Calendar fechaFin) {
		em.setBd(1);
		List<FacturaVenta> listaFacturas = (List<FacturaVenta>)(Object) 
				em.listarConDosParametrosObjeto(FacturaVenta.FACTURAS_FECHA_INICIO_FIN,	fechaInicio, fechaFin);
		if (listaFacturas.size() == 0){
			
		} else {
			
		}
		return null;
	}

}
