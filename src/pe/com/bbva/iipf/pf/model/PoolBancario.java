package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table( schema="PROFIN", name="TIIPF_POOL_BANCARIO")
@SequenceGenerator(name = "SEQ_POOL_BANCARIO", sequenceName = "PROFIN.SEQ_POOL_BANCARIO", allocationSize = 1, initialValue = 20000)
public class PoolBancario extends EntidadBase {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5947479166984689170L;
	
	private Long id; //	IIPF_POOL_BANCARIO_ID	NUMBER	Y			ID POOL BANCARIO
	private Empresa empresa;	//	ID_EMPRESA	NUMBER	Y			
	private String 	idbanco;	//	TT_ID_BANCO	NUMBER	Y			
	private String tipoDeuda;	//	TT_ID_TIPO_DEUDA	NUMBER	Y			
	private String mes;	//	MES	INTEGER	Y			
	private String anio;	//	ANIO	INTEGER	Y			
	private Double total;	//	TOTAL	NUMBER	Y			
	private Programa programa;		//	IIPF_PROGRAMA_ID	INTEGER	N			
	private String tipoEmpresa;	//	TT_ID_TIPO_EMPRESA	NUMBER	Y			SI ES GRUPO O EMPRESA
	private String idGrupo;	//	ID_GRUPO	VARCHAR2(60)	Y			CODIGO DEL GRUPO
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_POOL_BANCARIO")
	@Column(name="IIPF_POOL_BANCARIO_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="ID_EMPRESA")
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@Column(name="TT_ID_BANCO")
	public String getIdbanco() {
		return idbanco;
	}
	public void setIdbanco(String idbanco) {
		this.idbanco = idbanco;
	}
	
	@Column(name="TT_ID_TIPO_DEUDA")
	public String getTipoDeuda() {
		return tipoDeuda;
	}
	public void setTipoDeuda(String tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}
	@Column(name="MES")
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	@Column(name="ANIO")
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	@Column(name="TOTAL")
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	@ManyToOne
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name="TT_ID_TIPO_EMPRESA")
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}
	@Column(name="ID_GRUPO")
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
}
