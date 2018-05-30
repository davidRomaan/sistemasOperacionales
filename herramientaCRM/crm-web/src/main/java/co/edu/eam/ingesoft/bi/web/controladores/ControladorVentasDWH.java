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

import co.edu.eam.ingesoft.bi.negocio.dwh.HechoVentasEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@Named("controladorVentasDWH")
@SessionScoped
public class ControladorVentasDWH implements Serializable {

	private List<HechoVentas> hechosVenta;

	@EJB
	private HechoVentasEJB hechoVentaEJB;

	@PostConstruct
	private void cargarDatos() {
		hechosVenta = hechoVentaEJB.obtenerListaHechos();
	}

	public void creandoCSV(){
		System.out.println("Entra pre");
	}
	
	public void postProcessor(Object document) {
		
		System.out.println("entra");

		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header;
		
		System.out.println("nombre: " + sheet.getSheetName());

		int index = 0;

		for (int j = 1; j < hechosVenta.size()+1; j++) {
			header = sheet.getRow(j);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				
				HSSFCell cell = header.getCell(i);

				if (i == 2) {
					cell.setCellValue(hechosVenta.get(index).getProducto().getPrecio());
				}
				
				if (i == 4){
					cell.setCellValue(hechosVenta.get(index).getUnidades());
				}
				
				if (i == 5){
					cell.setCellValue(hechosVenta.get(index).getSubtotal());
				}
				
				if (i == 8){
					cell.setCellValue(hechosVenta.get(index).getPersona().getEdad());
				}
				
				if (i == 11){
					cell.setCellValue(hechosVenta.get(index).getEmpleado().getEdad());
				}

			}
			
			index++;

		}

	}

	public List<HechoVentas> getHechosVenta() {
		return hechosVenta;
	}

	public void setHechosVenta(List<HechoVentas> hechosVenta) {
		this.hechosVenta = hechosVenta;
	}

}
