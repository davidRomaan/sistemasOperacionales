package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import co.edu.eam.ingesoft.bi.negocio.dwh.HechoAuditoriasEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoAuditoria;

@Named("controladorAudDWH")
@SessionScoped
public class ControladorAuditoriaDWH implements Serializable {

	private List<HechoAuditoria> hechosAuditorias;

	@EJB
	private HechoAuditoriasEJB hechosAudEJB;

	@PostConstruct
	private void cargarDatos() {
		hechosAuditorias = hechosAudEJB.obtenerListaHechos();
	}

	public void postProcessor(Object document) {

		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header;

		int index = 0;

		for (int j = 1; j < hechosAuditorias.size()+1; j++) {
			header = sheet.getRow(j);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				
				HSSFCell cell = header.getCell(i);

				if (i == 2) {
					cell.setCellValue(hechosAuditorias.get(index).getUsuario().getEdad());
				}
				
			}
			
		}
	}

	public List<HechoAuditoria> getHechosAuditorias() {
		return hechosAuditorias;
	}

	public void setHechosAuditorias(List<HechoAuditoria> hechosAuditorias) {
		this.hechosAuditorias = hechosAuditorias;
	}

}
