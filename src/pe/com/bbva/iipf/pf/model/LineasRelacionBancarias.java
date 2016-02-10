package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;


@Entity
@Table(schema="PROFIN",  name="TIIPF_LINEAS")
@NamedQueries({
@NamedQuery(name="findLineasRelaBancByPrograma", 
		    query = "select l from LineasRelacionBancarias  l " +
		    		"where l.programa.id=? order by l.posicion asc"),
})
@SequenceGenerator(name = "SEQ_LINEA_RELA_BANC", sequenceName = "PROFIN.SEQ_LINEA_RELA_BANC", allocationSize = 1, initialValue = 20000)
public class LineasRelacionBancarias extends EntidadBase {
	
	private static final long serialVersionUID = 1L;
		
	private Long id;//  IIPF_LINEAS_ID
	private String linea;//	LINEA	VARCHAR2(100)	Y			
	private String sgrclteform;//	SG_RC_LTE_FORM	NUMBER	Y			
	private String sgrcdpto;//	SG_RC_DPTO	NUMBER	Y			
	private String sgrplteform;//	SG_RP_LTE_FORM	NUMBER	Y			
	private String sgrpdpto;//	SG_RP_DPTO	NUMBER	Y			
	private String sgrflteform;//	SG_RF_LTE_FORM	NUMBER	Y			
	private String sgrfdpto;//	SG_RF_DPTO	NUMBER	Y			
	private String sgtesolteform;//	SG_TESO_LTE_FORM	NUMBER	Y			
	private String sgtesodpto;//	SG_TESO_DPTO	NUMBER	Y			
	private String sgtotallteform;//	SG_TOTAL_LTE_FORM	NUMBER	Y			
	private String sgtotaldpto;//	SG_TOTAL_DPTO	NUMBER	Y	
	private Integer posicion ;//POSICION NUMBER
	private Programa programa;//	IIPF_PROGRAMA_ID	INTEGER	N	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LINEA_RELA_BANC")
	@Column(name="IIPF_LINEAS_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="LINEA")
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	
	@Column(name="SG_RC_LTE_FORM")
	public String getSgrclteform() {
		return sgrclteform;
	}
	public void setSgrclteform(String sgrclteform) {
		this.sgrclteform = sgrclteform;
	}
	@Column(name="SG_RC_DPTO")
	public String getSgrcdpto() {
		return sgrcdpto;
	}
	public void setSgrcdpto(String sgrcdpto) {
		this.sgrcdpto = sgrcdpto;
	}
	@Column(name="SG_RP_LTE_FORM")
	public String getSgrplteform() {
		return sgrplteform;
	}
	public void setSgrplteform(String sgrplteform) {
		this.sgrplteform = sgrplteform;
	}
	
	@Column(name="SG_RP_DPTO")
	public String getSgrpdpto() {
		return sgrpdpto;
	}
	public void setSgrpdpto(String sgrpdpto) {
		this.sgrpdpto = sgrpdpto;
	}
	
	@Column(name="SG_RF_LTE_FORM")
	public String getSgrflteform() {
		return sgrflteform;
	}
	public void setSgrflteform(String sgrflteform) {
		this.sgrflteform = sgrflteform;
	}
	
	@Column(name="SG_RF_DPTO")
	public String getSgrfdpto() {
		return sgrfdpto;
	}
	public void setSgrfdpto(String sgrfdpto) {
		this.sgrfdpto = sgrfdpto;
	}
	
	@Column(name="SG_TESO_LTE_FORM")
	public String getSgtesolteform() {
		return sgtesolteform;
	}
	public void setSgtesolteform(String sgtesolteform) {
		this.sgtesolteform = sgtesolteform;
	}
	
	@Column(name="SG_TESO_DPTO")
	public String getSgtesodpto() {
		return sgtesodpto;
	}
	public void setSgtesodpto(String sgtesodpto) {
		this.sgtesodpto = sgtesodpto;
	}
	
	@Column(name="SG_TOTAL_LTE_FORM")
	public String getSgtotallteform() {
		return sgtotallteform;
	}
	public void setSgtotallteform(String sgtotallteform) {
		this.sgtotallteform = sgtotallteform;
	}
	
	@Column(name="SG_TOTAL_DPTO")
	public String getSgtotaldpto() {
		return sgtotaldpto;
	}
	public void setSgtotaldpto(String sgtotaldpto) {
		this.sgtotaldpto = sgtotaldpto;
	}
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}
	
	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	
	@ManyToOne
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}	
}
