package pe.com.bbva.iipf.pf.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Tcaratula {
	
	private Long id	; 	
	private String indice;
	private String subindice;
	private String titulo;
	private List<TsubCaratula> listSubCaratula;
	
	private String codTitulo;
	private String pagina;
	
	public Long getId() {
		return id;
	}
	public String getCodTitulo() {
		return codTitulo;
	}
	public void setCodTitulo(String codTitulo) {
		this.codTitulo = codTitulo;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIndice() {
		return indice;
	}
	public void setIndice(String indice) {
		this.indice = indice;
	}	
	
	public String getSubindice() {
		return subindice;
	}
	public void setSubindice(String subindice) {
		this.subindice = subindice;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<TsubCaratula> getListSubCaratula() {
		return listSubCaratula;
	}
	public void setListSubCaratula(List<TsubCaratula> listSubCaratula) {
		this.listSubCaratula = listSubCaratula;
	}
	

}
