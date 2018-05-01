package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.PersonaEJB;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.ClasesAuditorias;
import co.edu.eam.ingesoft.bi.presistencia.entidades.AuditoriaProducto;

@SessionScoped
@Named("controladorAuditoria")
public class ControladorAuditorias implements Serializable{

	
	private String tipoAuditorias;
	
	
	private List<ClasesAuditorias> auditorias;
	
	@EJB
	private PersonaEJB per;
	
	private List<AuditoriaProducto> producto;
	
	
	@PostConstruct
	public void listares() {
		auditorias = Arrays.asList(ClasesAuditorias.values());


	}
	
	
	
	public void listarAusitorias() {
		
		System.out.println(auditorias);
		
		if(tipoAuditorias.equals("AuditoriaProducto")) {
			producto = per.listaaudi();
		}
		
		if(tipoAuditorias.equals("AuditoriaInventario")) {
			
		}
		
		if(tipoAuditorias.equals("AuditoriaPersonas")) {
			
		}
		
	}



	



	public String getTipoAuditorias() {
		return tipoAuditorias;
	}



	public void setTipoAuditorias(String tipoAuditorias) {
		this.tipoAuditorias = tipoAuditorias;
	}



	public List<ClasesAuditorias> getAuditorias() {
		return auditorias;
	}



	public void setAuditorias(List<ClasesAuditorias> auditorias) {
		this.auditorias = auditorias;
	}



	public PersonaEJB getPer() {
		return per;
	}



	public void setPer(PersonaEJB per) {
		this.per = per;
	}



	public List<AuditoriaProducto> getProducto() {
		return producto;
	}



	public void setProducto(List<AuditoriaProducto> producto) {
		this.producto = producto;
	}
	
	
	
	
}
