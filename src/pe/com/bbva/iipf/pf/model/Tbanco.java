package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;


@Entity
public class Tbanco {
	
	private String codBanco;				
	private String nombreBanco; 
	



	public String getCodBanco() {
		return codBanco;
	}
	
	
	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}
	public String getNombreBanco() {
		return nombreBanco;
	}
	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

}
