package co.edu.eam.ingesoft.bi.persistencia.DTO;

public class UsuariosDTO {

	
	private String nombre;
	
	private String apellido;
	
	private String cedula;
	
	private boolean estado;
	
	
	public UsuariosDTO(){
		
	}

	

	public UsuariosDTO(String nombre, String apellido, String cedula, boolean estado) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.cedula = cedula;
		this.estado = estado;
	}



	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellido() {
		return apellido;
	}


	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public String getCedula() {
		return cedula;
	}


	public void setCedula(String cedula) {
		this.cedula = cedula;
	}


	public boolean isEstado() {
		return estado;
	}


	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
}
