package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AUDITORIA_PERMISOS")
@NamedQueries({
	@NamedQuery(name=AuditoriaPermisos.LISTA_PERMISOS, query="SELECT p FROM AuditoriaPermisos p")
})
public class AuditoriaPermisos implements Serializable {
	
	public static final String LISTA_PERMISOS = "lista.permisos";

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "accion", length = 30, nullable = false)
	private String accion;

	@Column(name = "fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaHora;

	@Column(name = "dispositivo", length = 30, nullable = false)
	private String dispositivo;

	@Column(name = "navegador", length = 30, nullable = false)
	private String navegador;

	@Column(name="modulo_usuario")
	private String modulosUsuario;
	
	
	public AuditoriaPermisos(){
		
	}


	


	public AuditoriaPermisos(int id, String accion, Calendar fechaHora, String dispositivo, String navegador,
			String modulosUsuario) {
		super();
		this.id = id;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.modulosUsuario = modulosUsuario;
	}





	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public String getAccion() {
		return accion;
	}





	public void setAccion(String accion) {
		this.accion = accion;
	}





	public Calendar getFechaHora() {
		return fechaHora;
	}





	public void setFechaHora(Calendar fechaHora) {
		this.fechaHora = fechaHora;
	}





	public String getDispositivo() {
		return dispositivo;
	}





	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}





	public String getNavegador() {
		return navegador;
	}





	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}





	public String getModulosUsuario() {
		return modulosUsuario;
	}





	public void setModulosUsuario(String modulosUsuario) {
		this.modulosUsuario = modulosUsuario;
	}
	
	
	

}
