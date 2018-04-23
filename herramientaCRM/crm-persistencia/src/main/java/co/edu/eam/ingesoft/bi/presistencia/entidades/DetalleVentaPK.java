package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

public class DetalleVentaPK implements Serializable{

	private String facturaVentaId;
	
	private String productoId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((facturaVentaId == null) ? 0 : facturaVentaId.hashCode());
		result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleVentaPK other = (DetalleVentaPK) obj;
		if (facturaVentaId == null) {
			if (other.facturaVentaId != null)
				return false;
		} else if (!facturaVentaId.equals(other.facturaVentaId))
			return false;
		if (productoId == null) {
			if (other.productoId != null)
				return false;
		} else if (!productoId.equals(other.productoId))
			return false;
		return true;
	}
	
	
	
}
