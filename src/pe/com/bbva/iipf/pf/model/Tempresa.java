package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;


@Entity
public class Tempresa {

//	private Long id	; 	
	private String codEmpresa;				
	private String nomEmpresa;	
	
//	@Id
//	@GeneratedValue	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public String getCodEmpresa() {
		return codEmpresa;
	}
	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	public String getNomEmpresa() {
		return nomEmpresa;
	}
	public void setNomEmpresa(String nomEmpresa) {
		this.nomEmpresa = nomEmpresa;
	} 
	
	
	
	
}
