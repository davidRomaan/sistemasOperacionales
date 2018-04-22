package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(ModulosUsuarioPK.class)
@Table(name="MODULOS_USUARIO")
public class ModulosUsuario implements Serializable{

	@Id
	@JoinColumn(name="modulo_id")
	@ManyToOne
	private Modulo modulo_id;
	
	@Id
	@JoinColumn(name="tipoUsuario_id")
	@ManyToOne
	private TipoUsuario tipoUsiario_id;
	
	
	public ModulosUsuario(){
		
	}


	public ModulosUsuario(Modulo modulo_id, TipoUsuario tipoUsiario_id) {
		super();
		this.modulo_id = modulo_id;
		this.tipoUsiario_id = tipoUsiario_id;
	}


	public Modulo getModulo_id() {
		return modulo_id;
	}


	public void setModulo_id(Modulo modulo_id) {
		this.modulo_id = modulo_id;
	}


	public TipoUsuario getTipoUsiario_id() {
		return tipoUsiario_id;
	}


	public void setTipoUsiario_id(TipoUsuario tipoUsiario_id) {
		this.tipoUsiario_id = tipoUsiario_id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modulo_id == null) ? 0 : modulo_id.hashCode());
		result = prime * result + ((tipoUsiario_id == null) ? 0 : tipoUsiario_id.hashCode());
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
		ModulosUsuario other = (ModulosUsuario) obj;
		if (modulo_id == null) {
			if (other.modulo_id != null)
				return false;
		} else if (!modulo_id.equals(other.modulo_id))
			return false;
		if (tipoUsiario_id == null) {
			if (other.tipoUsiario_id != null)
				return false;
		} else if (!tipoUsiario_id.equals(other.tipoUsiario_id))
			return false;
		return true;
	}
	
	
}
