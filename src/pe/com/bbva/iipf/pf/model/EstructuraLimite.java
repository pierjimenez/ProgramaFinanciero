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
import javax.persistence.Transient;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="TIIPF_ESTRUCTURA_LIMITES")
@NamedQueries({
@NamedQuery(name="listarEstructuraLimite", 
			query = " select e from EstructuraLimite e where e.programa.id = ? order by e.posicion, e.empresa.id, e.tipo")				
})
@SequenceGenerator(name = "SEQ_ESTRUCTURA_LIMI", sequenceName = "PROFIN.SEQ_ESTRUCTURA_LIMI", allocationSize = 1, initialValue = 20000)
public class EstructuraLimite extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152539714996447323L;

	private Long id;		//	IIPF_ESTRUCTURALIMITE_ID	NUMBER(19)	Y
	private Empresa empresa;	//	ID_EMPRESA	NUMBER	N	
	private transient String codEmpresatmp; // ID_EMPRESA	NUMBER	N
	private String tipoOperacion;		//	TIPO_OPERACION	VARCHAR2(60)	Y			
	private String nombEmpresa;		//	NOMBEMPRESA	VARCHAR2(100)	Y						
	private String limiteAutorizado;		//	LIMITE_AUTORIZADO	INTEGER	Y			
	private String limitePropuesto;		//	LIMITE_PROPUESTO	INTEGER	Y		
	private String saldoUtilizado; //SALDO_UTILIZADO
	private String propuestoOficina; //PROP_OFICINA
	private String propuestaRiesgo; //PROP_RIESGO
	private String observacion;		//	OBSERVACION	VARCHAR2(200)	Y			
	private Programa programa;		//	IIPF_PROGRAMA_ID	INTEGER	N			
	private String tipo; //TIPO
	private Integer posicion;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ESTRUCTURA_LIMI")
	@Column(name="IIPF_ESTRUCTURALIMITE_ID")
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
			
	@Transient // no mapeada
	public String getCodEmpresatmp() {
		return codEmpresatmp;
	}
	public void setCodEmpresatmp(String codEmpresatmp) {
		this.codEmpresatmp = codEmpresatmp;
	}	
	
	@Column(name="TIPO_OPERACION")
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	
	@Column(name="NOMBEMPRESA")
	public String getNombEmpresa() {
		return nombEmpresa;
	}
	public void setNombEmpresa(String nombEmpresa) {
		this.nombEmpresa = nombEmpresa;
	}
	
	@Column(name="LIMITE_AUTORIZADO")
	public String getLimiteAutorizado() {
		return limiteAutorizado;
	}
	public void setLimiteAutorizado(String limiteAutorizado) {
		this.limiteAutorizado = limiteAutorizado;
	}
	
	@Column(name="LIMITE_PROPUESTO")
	public String getLimitePropuesto() {
		return limitePropuesto;
	}
	public void setLimitePropuesto(String limitePropuesto) {
		this.limitePropuesto = limitePropuesto;
	}
	
	@Column(name="OBSERVACION")
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@ManyToOne
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name="TIPO")
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Column(name="SALDO_UTILIZADO",length=20)
	public String getSaldoUtilizado() {
		return saldoUtilizado;
	}
	
	public void setSaldoUtilizado(String saldoUtilizado) {
		this.saldoUtilizado = saldoUtilizado;
	}
	
	@Column(name="PROP_OFICINA", length=20)
	public String getPropuestoOficina() {
		return propuestoOficina;
	}
	
	public void setPropuestoOficina(String propuestoOficina) {
		this.propuestoOficina = propuestoOficina;
	}
	
	@Column(name="PROP_RIESGO", length=20)
	public String getPropuestaRiesgo() {
		return propuestaRiesgo;
	}
	
	public void setPropuestaRiesgo(String propuestaRiesgo) {
		this.propuestaRiesgo = propuestaRiesgo;
	}
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	
}
