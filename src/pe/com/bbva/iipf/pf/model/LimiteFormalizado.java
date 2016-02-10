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
import javax.persistence.Transient;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="TIIPF_LIMITE_FORMALIZADO")
@SequenceGenerator(name = "SEQ_LIMITE_FORMALI", sequenceName = "PROFIN.SEQ_LIMITE_FORMALI", allocationSize = 1, initialValue = 20000)
public class LimiteFormalizado extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1837841246626451954L;
	private Long id; //	IIPF_LIMITE_FORMALIZADO_ID	NUMBER	Y	
	private Tabla tipoRiesgo;	//	TT_ID_TIPO_RIESGO	NUMBER	Y			
	private String limiteAutorizado; 	//	LIMITE_AUTO	varchar2(20)	Y			
	private String comprometido;	//	COMPROMETIDO	varchar2(20)	Y			
	private String noComprometido;	//	NO_COMPROMETIDO	varchar2(20)	Y			
	private String total;	//	TOTAL	varchar2(20)	Y			
	private String dispuesto;	//	DISPUESTO	varchar2(20)	Y			
	private String limitePropuesto;	//	LTE_PROPUESTO	varchar2(20)	Y			
	private Programa programa;	//	IIPF_PROGRAMA_ID	INTEGER	N
	
	private transient String tipoTotal;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LIMITE_FORMALI")
	@Column(name = "IIPF_LIMITE_FORMALIZADO_ID")	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_RIESGO")
	public Tabla getTipoRiesgo() {
		return tipoRiesgo;
	}
	public void setTipoRiesgo(Tabla tipoRiesgo) {
		this.tipoRiesgo = tipoRiesgo;
	}
	
	@Column(name = "LIMITE_AUTO")
	public String getLimiteAutorizado() {
		return limiteAutorizado;
	}
	public void setLimiteAutorizado(String limiteAutorizado) {
		this.limiteAutorizado = limiteAutorizado;
	}
	
	@Column(name = "COMPROMETIDO")
	public String getComprometido() {
		return comprometido;
	}
	public void setComprometido(String comprometido) {
		this.comprometido = comprometido;
	}
	
	@Column(name = "NO_COMPROMETIDO")
	public String getNoComprometido() {
		return noComprometido;
	}
	public void setNoComprometido(String noComprometido) {
		this.noComprometido = noComprometido;
	}
	
	@Column(name = "TOTAL")
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}	
	
	@Column(name = "DISPUESTO")
	public String getDispuesto() {
		return dispuesto;
	}
	public void setDispuesto(String dispuesto) {
		this.dispuesto = dispuesto;
	}
	
	@Column(name = "LTE_PROPUESTO")
	public String getLimitePropuesto() {
		return limitePropuesto;
	}
	public void setLimitePropuesto(String limitePropuesto) {
		this.limitePropuesto = limitePropuesto;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	@Transient // no mapeada
	public String getTipoTotal() {
		return tipoTotal;
	}
	public void setTipoTotal(String tipoTotal) {
		this.tipoTotal = tipoTotal;
	}
								
	
	
	
	
}
