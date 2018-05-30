package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.dwh.HechoVentasEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@Named("controladorVentasDWH")
@SessionScoped
public class ControladorVentasDWH implements Serializable {

	private List<HechoVentas> hechosVenta;
	
	@EJB
	private HechoVentasEJB hechoVentaEJB;
	
	@PostConstruct
	private void cargarDatos(){		
		hechosVenta = hechoVentaEJB.obtenerListaHechos(); 		
	}
	

	public List<HechoVentas> getHechosVenta() {
		return hechosVenta;
	}

	public void setHechosVenta(List<HechoVentas> hechosVenta) {
		this.hechosVenta = hechosVenta;
	}
	
	
	
	
}
