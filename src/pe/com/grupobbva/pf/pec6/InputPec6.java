package pe.com.grupobbva.pf.pec6;

import java.io.Serializable;

public class InputPec6 implements Serializable {
	
	  private String numeroCliente;
	  private String teclaPulsada;
	  private String usuario;
	  private String flagControl;
	  private String pathWebServicePEC6;
	  	  
	  
	public String getNumeroCliente() {
		return numeroCliente;
	}
	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}
	public String getTeclaPulsada() {
		return teclaPulsada;
	}
	public void setTeclaPulsada(String teclaPulsada) {
		this.teclaPulsada = teclaPulsada;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getFlagControl() {
		return flagControl;
	}
	public void setFlagControl(String flagControl) {
		this.flagControl = flagControl;
	}
	public String getPathWebServicePEC6() {
		return pathWebServicePEC6;
	}
	public void setPathWebServicePEC6(String pathWebServicePEC6) {
		this.pathWebServicePEC6 = pathWebServicePEC6;
	}

	  
	  
}
