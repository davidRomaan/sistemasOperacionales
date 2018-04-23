package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

public class DetalleVentaPK implements Serializable{

	private int facturaVenta;
	
	private int producto;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + facturaVenta;
		result = prime * result + producto;
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
		if (facturaVenta != other.facturaVenta)
			return false;
		if (producto != other.producto)
			return false;
		return true;
	}

	
	
	
	
}
