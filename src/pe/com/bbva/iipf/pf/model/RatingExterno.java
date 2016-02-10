package pe.com.bbva.iipf.pf.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_RATING_EXTERNO")
@NamedQueries({
@NamedQuery(name="findRatingExternoByPrograma", 
		    query = "select r from RatingExterno  r " +
		    		"where r.programa.id=? order by r.posicion asc"),
@NamedQuery(name="findRatingExternoByProgramaCodEmpGrup", 
	        query = "select r from RatingExterno  r " +
	    		    "where r.programa.id=? and r.codEmpresaGrupo=? order by r.posicion asc"),
})
@SequenceGenerator(name = "SEQ_RATING_EXTER", sequenceName = "PROFIN.SEQ_RATING_EXTER", allocationSize = 1, initialValue = 20000)
public class RatingExterno extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; ////ID_RATI_EXTE	NUMBER
	private String companiaGrupo;//COMPANIA_GRUP	VARCHAR2(100 BYTE)
	private String agencia;//AGENCIA	VARCHAR2(60 BYTE)
	private String cp;//CP	VARCHAR2(15 BYTE)
	private String lp; //LP	VARCHAR2(15 BYTE)
	private String outlook; //OUTLOOK	VARCHAR2(15 BYTE)
	private String moneda;//MONEDA	VARCHAR2(15 BYTE)
	private String fecha ; //FECHA	DATE
	private Integer posicion; //POSICION NUMBER
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RATING_EXTER")
	@Column(name="ID_RATI_EXTE")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="COMPANIA_GRUP", length = 100)
	public String getCompaniaGrupo() {
		return companiaGrupo;
	}
	
	public void setCompaniaGrupo(String companiaGrupo) {
		this.companiaGrupo = companiaGrupo;
	}
	
	@Column(name="AGENCIA", length = 60)
	public String getAgencia() {
		return agencia;
	}
	
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	
	@Column(name="CP", length = 15)
	public String getCp() {
		return cp;
	}
	
	public void setCp(String cp) {
		this.cp = cp;
	}
	
	@Column(name="LP", length = 15)
	public String getLp() {
		return lp;
	}
	
	public void setLp(String lp) {
		this.lp = lp;
	}
	
	@Column(name="OUTLOOK", length = 15)
	public String getOutlook() {
		return outlook;
	}
	
	public void setOutlook(String outlook) {
		this.outlook = outlook;
	}
	
	@Column(name="MONEDA", length = 15)
	public String getMoneda() {
		return moneda;
	}
	
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	@Column(name="FECHA", length = 15)
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@Column(name = "COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}

	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	
	
}
