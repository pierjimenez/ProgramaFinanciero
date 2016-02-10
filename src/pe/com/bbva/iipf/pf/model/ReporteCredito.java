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
@Table(schema="PROFIN", name = "TIIPF_REPORTE_CREDITO")
@NamedQueries({
	@NamedQuery(name="updateReporteCreditoByEmpresa", 
			query = " update ReporteCredito set cuentaCorriente = ? ," +	
				    " fechaRDC = ?, numeroRVGL = ?," +
				    " salem = ?," +
				    " vulnerabilidad = ?, totalInversion = ?," +
				    " montoPrestamo = ?, entorno = ?," +
				    " poblacionAfectada = ?, categorizacionAmbiental = ?," +				   
				    " comentarioAdmision = ?, ciiuRDC = ?" +				   
				    " where programa.id = ? and codigoEmpresa=? ")
})
@SequenceGenerator(name = "SEQ_REPORTECREDITO", sequenceName = "PROFIN.SEQ_REPORTECREDITO", allocationSize = 1, initialValue = 20000)
public class ReporteCredito extends EntidadBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2088881374163702614L;
	private Long Id; //ID_DATOS_BASICOS	NUMBER(19)
	private Programa programa; //ID_PROGRAMA	INTEGER	N	
	private String codigoEmpresa ; //CODIGO_EMPRESA	VARCHAR2(30)	Y		
				
	private String cuentaCorriente;//RDC_CUENTA_CORRIENTE		VARCHAR2(25)	
	private String fechaRDC;//RDC_FECHA				VARCHAR2(10)		
	private String numeroRVGL;//RDC_NUMERO_RVGL			VARCHAR2(20)				
	private String salem;//RDC_SALEM				VARCHAR2(5)				
	//private String posicionClienteGrupo;//RDC_POSI_CLIENT_GRUPECON	VARCHAR2(150)			
	private String vulnerabilidad;//RDC_VULNERABILIDAD		VARCHAR2(150)				
	private String totalInversion;//RDC_IMPACAMB_TOTAL_INVER	VARCHAR2(20)				
	private String montoPrestamo;//RDC_IMPACAMB_MONTO_PREST	VARCHAR2(20)				
	private String entorno;//RDC_IMPACAMB_ENTORNO_KM		VARCHAR2(20)				
	private String poblacionAfectada;//RDC_IMPACAMB_POBLA_AFECT	VARCHAR2(20)				
	private String categorizacionAmbiental;//RDC_IMPACAMB_CATEG_AMB		VARCHAR2(20)				
	private String comentarioAdmision;//RDC_COMENTARIOS_ADMISION	VARCHAR2(2000)				
	private String ciiuRDC;//RDC_CIIU					VARCHAR2(500)	
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_REPORTECREDITO")
	@Column(name="ID_REPORTE_CREDITO")
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	
	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="ID_PROGRAMA")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	
	@Column(name="CODIGO_EMPRESA", length = 30)
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}	
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	
	
	
	@Column(name="RDC_CUENTA_CORRIENTE")
	public String getCuentaCorriente() {
		return cuentaCorriente;
	}
	public void setCuentaCorriente(String cuentaCorriente) {
		this.cuentaCorriente = cuentaCorriente;
	}
	
		
	@Column(name="RDC_FECHA")
	public String getFechaRDC() {
		return fechaRDC;
	}
	public void setFechaRDC(String fechaRDC) {
		this.fechaRDC = fechaRDC;
	}
	
	@Column(name="RDC_NUMERO_RVGL")
	public String getNumeroRVGL() {
		return numeroRVGL;
	}
	public void setNumeroRVGL(String numeroRVGL) {
		this.numeroRVGL = numeroRVGL;
	}
	
	@Column(name="RDC_SALEM")
	public String getSalem() {
		return salem;
	}
	public void setSalem(String salem) {
		this.salem = salem;
	}
	
//	@Column(name="RDC_POSI_CLIENT_GRUPECON")
//	public String getPosicionClienteGrupo() {
//		return posicionClienteGrupo;
//	}
//	public void setPosicionClienteGrupo(String posicionClienteGrupo) {
//		this.posicionClienteGrupo = posicionClienteGrupo;
//	}
	
	@Column(name="RDC_VULNERABILIDAD")
	public String getVulnerabilidad() {
		return vulnerabilidad;
	}
	public void setVulnerabilidad(String vulnerabilidad) {
		this.vulnerabilidad = vulnerabilidad;
	}
	
	@Column(name="RDC_IMPACAMB_TOTAL_INVER")
	public String getTotalInversion() {
		return totalInversion;
	}
	public void setTotalInversion(String totalInversion) {
		this.totalInversion = totalInversion;
	}
	
	@Column(name="RDC_IMPACAMB_MONTO_PREST")
	public String getMontoPrestamo() {
		return montoPrestamo;
	}
	public void setMontoPrestamo(String montoPrestamo) {
		this.montoPrestamo = montoPrestamo;
	}
	
	@Column(name="RDC_IMPACAMB_ENTORNO_KM")
	public String getEntorno() {
		return entorno;
	}
	public void setEntorno(String entorno) {
		this.entorno = entorno;
	}
	
	@Column(name="RDC_IMPACAMB_POBLA_AFECT")
	public String getPoblacionAfectada() {
		return poblacionAfectada;
	}
	public void setPoblacionAfectada(String poblacionAfectada) {
		this.poblacionAfectada = poblacionAfectada;
	}
	
	@Column(name="RDC_IMPACAMB_CATEG_AMB")
	public String getCategorizacionAmbiental() {
		return categorizacionAmbiental;
	}
	public void setCategorizacionAmbiental(String categorizacionAmbiental) {
		this.categorizacionAmbiental = categorizacionAmbiental;
	}
	
	@Column(name="RDC_COMENTARIOS_ADMISION")
	public String getComentarioAdmision() {
		return comentarioAdmision;
	}
	public void setComentarioAdmision(String comentarioAdmision) {
		this.comentarioAdmision = comentarioAdmision;
	}
	
	@Column(name="RDC_CIIU")
	public String getCiiuRDC() {
		return ciiuRDC;
	}
	public void setCiiuRDC(String ciiuRDC) {
		this.ciiuRDC = ciiuRDC;
	}
	
}
