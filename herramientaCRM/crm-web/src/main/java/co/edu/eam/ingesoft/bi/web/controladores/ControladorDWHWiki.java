package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omnifaces.util.Faces;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.etl.ETLWikiEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.RecentChanges;

@Named("controladorWikiDWH")
@SessionScoped
public class ControladorDWHWiki implements Serializable {

	@EJB
	private ETLWikiEJB wikiEJB;

	private List<RecentChanges> lista;

	private String accion;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@Inject
	private ControladorSesion sesion;

	@PostConstruct
	private void cargarDatos() {

		lista = wikiEJB.obtenerDatosDWHWiki();

	}

	public void creandoCSV() {

		accion = "Crear Documento Auditoria";
		String browserDetail = Faces.getRequest().getHeader("User-Agent");
		auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Crear documento CVS WIKI", sesion.getUser().getCedula(),
				browserDetail);

	}

	public void postProcessor(Object document) {

		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header;

		accion = "Crear Documento Auditoria";
		String browserDetail = Faces.getRequest().getHeader("User-Agent");
		auditoriaEJB.crearAuditoria("AuditoriaDW", accion, "Crear documento EXCEL WIKI", sesion.getUser().getCedula(),
				browserDetail);

		int index = 0;

		for (int j = 1; j < lista.size() + 1; j++) {
			header = sheet.getRow(j);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {

				HSSFCell cell = header.getCell(i);

				if (i == 0) {
					cell.setCellValue(lista.get(index).getRcId());
				}
				
				if (i == 4){
					cell.setCellValue(lista.get(index).getRcOldLen());
				}
				
				if (i == 5){
					cell.setCellValue(lista.get(index).getRcNewLen());
				}
				
				if (i == 7){
					cell.setCellValue(lista.get(index).getUser().getUserId());
				}
				
				if (i == 10){
					cell.setCellValue(lista.get(index).getPageId());
				}

			}
			
			index++;

		}
	}

	public List<RecentChanges> getLista() {
		return lista;
	}

	public void setLista(List<RecentChanges> lista) {
		this.lista = lista;
	}
	
}
