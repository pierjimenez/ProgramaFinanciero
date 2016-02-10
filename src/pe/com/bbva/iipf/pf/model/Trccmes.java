package pe.com.bbva.iipf.pf.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedNativeQuery(name = "findPoolBancarioPrograma", query = "{ ? = call PROFIN.PFPKG.FN_IIPF_POOLBANCARIO_PROGRAMA(:vtipoDocumento,:vcodEmpresa,:vcodTipoDeuda,:vcodBanco,:vtipoempresagrupo,:vidprograma) }", resultClass = Trccmes.class, hints = { 
@QueryHint(name = "org.hibernate.callable", value = "true") })
@Table(schema="PROFIN", name = "TIIPF_TRCCMES")
@SequenceGenerator(name = "SEQ_TRCC_MES", sequenceName = "PROFIN.SEQ_TRCC_MES", allocationSize = 1, initialValue = 20000)
public class Trccmes {
	
	
	 private String id; 
	 private String mes;
	 private String anio;
	 private String codEmpresa;
	 private String codbanco;
	 private String nombbanco;
	 private String deudaindirecta;
	 private String deudadirecta;
	 private String deudatotal;
	 
	 @Id 
	 @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TRCC_MES")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
		
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getCodEmpresa() {
		return codEmpresa;
	}
	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	public String getCodbanco() {
		return codbanco;
	}
	public void setCodbanco(String codbanco) {
		this.codbanco = codbanco;
	}
	public String getNombbanco() {
		return nombbanco;
	}
	public void setNombbanco(String nombbanco) {
		this.nombbanco = nombbanco;
	}
	public String getDeudaindirecta() {
		return deudaindirecta;
	}
	public void setDeudaindirecta(String deudaindirecta) {
		this.deudaindirecta = deudaindirecta;
	}
	public String getDeudadirecta() {
		return deudadirecta;
	}
	public void setDeudadirecta(String deudadirecta) {
		this.deudadirecta = deudadirecta;
	}
	public String getDeudatotal() {
		return deudatotal;
	}
	public void setDeudatotal(String deudatotal) {
		this.deudatotal = deudatotal;
	}
	 
	 

	

}




