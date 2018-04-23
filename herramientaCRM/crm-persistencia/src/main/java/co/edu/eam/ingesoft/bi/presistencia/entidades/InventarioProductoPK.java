package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

public class InventarioProductoPK implements Serializable{
	
	private int inventarioId;
	
	private int productoId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + inventarioId;
		result = prime * result + productoId;
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
		InventarioProductoPK other = (InventarioProductoPK) obj;
		if (inventarioId != other.inventarioId)
			return false;
		if (productoId != other.productoId)
			return false;
		return true;
	}

	

}
