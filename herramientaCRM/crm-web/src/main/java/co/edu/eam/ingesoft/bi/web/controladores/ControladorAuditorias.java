package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.ClasesAuditorias;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;

@SessionScoped
@Named("controladorAuditoria")
public class ControladorAuditorias implements Serializable {

	private String tipoAuditorias;

	private List<ClasesAuditorias> auditorias;
	
	private List<Auditoria> auditariasListar;
	
	@EJB
	private AuditoriaEJB auditoriaEJB;

	@PostConstruct
	public void listares() {
		auditorias = Arrays.asList(ClasesAuditorias.values());

	}

	public void listarAusitorias() {

		auditariasListar = auditoriaEJB.listarAuditorias(tipoAuditorias);

	}
	

	public List<Auditoria> getAuditariasListar() {
		return auditariasListar;
	}

	public void setAuditariasListar(List<Auditoria> auditariasListar) {
		this.auditariasListar = auditariasListar;
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

}
