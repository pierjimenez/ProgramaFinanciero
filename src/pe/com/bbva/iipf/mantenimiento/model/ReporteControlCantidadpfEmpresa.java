package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Entity;

@Entity
public class ReporteControlCantidadpfEmpresa {
	private String empresaPrincipal;
	private String rucEmpprincipal;
	private String codcentralEmpprincipal;
	private String cantPrograma;
	
	public String getEmpresaPrincipal() {
		return empresaPrincipal;
	}
	public void setEmpresaPrincipal(String empresaPrincipal) {
		this.empresaPrincipal = empresaPrincipal;
	}
	public String getRucEmpprincipal() {
		return rucEmpprincipal;
	}
	public void setRucEmpprincipal(String rucEmpprincipal) {
		this.rucEmpprincipal = rucEmpprincipal;
	}
	public String getCodcentralEmpprincipal() {
		return codcentralEmpprincipal;
	}
	public void setCodcentralEmpprincipal(String codcentralEmpprincipal) {
		this.codcentralEmpprincipal = codcentralEmpprincipal;
	}
	public String getCantPrograma() {
		return cantPrograma;
	}
	public void setCantPrograma(String cantPrograma) {
		this.cantPrograma = cantPrograma;
	}
	
	
}
