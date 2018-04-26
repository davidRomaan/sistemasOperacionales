package co.edu.eam.ingesoft.bi.negocios.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ExcepcionNegocio extends RuntimeException {

	
	public ExcepcionNegocio(String message) {
		super(message);

	}
}
