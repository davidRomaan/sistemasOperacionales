package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

public class ModulosUsuarioPK implements Serializable{

	private int modulo_id;
	
	private int tipoUsiario_id;

	public ModulosUsuarioPK() {
		// TODO Auto-generated constructor stub
	}
	
	
	public int getModulo_id() {
		return modulo_id;
	}




	public void setModulo_id(int modulo_id) {
		this.modulo_id = modulo_id;
	}




	public int getTipoUsiario_id() {
		return tipoUsiario_id;
	}




	public void setTipoUsiario_id(int tipoUsiario_id) {
		this.tipoUsiario_id = tipoUsiario_id;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + modulo_id;
		result = prime * result + tipoUsiario_id;
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
		ModulosUsuarioPK other = (ModulosUsuarioPK) obj;
		if (modulo_id != other.modulo_id)
			return false;
		if (tipoUsiario_id != other.tipoUsiario_id)
			return false;
		return true;
	}
	
	
	
}
