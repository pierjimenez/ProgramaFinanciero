package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;

@Entity
public class TsubCaratula {
	private Long idSubcaratula	; 	
	private String indiceSubcaratula;
	private String tituloSubcaratula;
	private String codTituloInfxEmpresa;
	private String pagina;
	
	public Long getIdSubcaratula() {
		return idSubcaratula;
	}
	public void setIdSubcaratula(Long idSubcaratula) {
		this.idSubcaratula = idSubcaratula;
	}
	public String getIndiceSubcaratula() {
		return indiceSubcaratula;
	}
	public void setIndiceSubcaratula(String indiceSubcaratula) {
		this.indiceSubcaratula = indiceSubcaratula;
	}
	public String getTituloSubcaratula() {
		return tituloSubcaratula;
	}
	public void setTituloSubcaratula(String tituloSubcaratula) {
		this.tituloSubcaratula = tituloSubcaratula;
	}

	
	
	public String getCodTituloInfxEmpresa() {
		return codTituloInfxEmpresa;
	}
	public void setCodTituloInfxEmpresa(String codTituloInfxEmpresa) {
		this.codTituloInfxEmpresa = codTituloInfxEmpresa;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	
	
	
}


