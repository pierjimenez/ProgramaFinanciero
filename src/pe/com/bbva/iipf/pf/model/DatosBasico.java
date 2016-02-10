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
@Table(schema="PROFIN", name = "TIIPF_DATOS_BASICO")
@NamedQueries({
@NamedQuery(name="updateDatosBasicosByEmpresa", 
		    query = " update DatosBasico set comentAccionariado = ? ," +
		    		" comentPartiSignificativa = ?, comentRatinExterno = ?," +
		    		" comentvaloraGlobal = ? , actividadPrincipal = ?  " +
		    		" where programa.id = ? and codigoEmpresa=? "),
@NamedQuery(name="refreshDatosBasicosHostByEmpresa", 
	    query = " update DatosBasico set pais = ? ," +
	    		" antiguedadNegocio = ?, antiguedadCliente = ?," +	    		
	    		" calificacionBanco = ?, gestor = ?," +
	    		" oficina = ?, segmento = ?," +
	    		" grupoRiesgoBuro = ? " +
	    		" where programa.id = ? and codigoEmpresa=? ")
})
@SequenceGenerator(name = "SEQ_DATOSBASICO", sequenceName = "PROFIN.SEQ_DATOSBASICO", allocationSize = 1, initialValue = 20000)
public class DatosBasico extends EntidadBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1606726327959098942L;
	private Long Id; //ID_DATOS_BASICOS	NUMBER(19)
	private Programa programa; //ID_PROGRAMA	INTEGER	N	
	private Integer anio;//ANIO	NUMBER
	private String codigoEmpresa ; //CODIGO_EMPRESA	VARCHAR2(30)	Y	
	private String nombreGrupoEmpresa;//DB_NOMBRE_GRUP_EMPR	VARCHAR2(400 BYTE)
	private String actividadPrincipal;//	DB_ACTIVIDAD_PRIN	VARCHAR2(4000 BYTE)		
	private String pais;//	DB_T_EXT_PAIS_CODIGO	VARCHAR2(60 BYTE)
	private Integer antiguedadNegocio;//	DB_ANTIGUEDAD_NEGO	INTEGER
	private Integer antiguedadCliente;//	DB_ANTIGUEDAD_CLIE	INTEGER
	private String  grupoRiesgoBuro;//	DB_GRUP_RIESGO	VARCHAR2(2)
	private String comentAccionariado;//	DB_COMEN_ACCI	VARCHAR2(4000)
	private String comentPartiSignificativa;//	DB_COMEN_PART_SIGN	VARCHAR2(4000)
	private String comentRatinExterno;//	DB_COMEN_RAIT_EXTE	VARCHAR2(4000)
	private String comentvaloraGlobal;//	DB_VALORACION_GLOBAL	VARCHAR2(4000)
	private String ruc;//DB_RUC VARCHAR2(12)
	

	private String calificacionBanco;//RB_CALIFICACION_BANC		VARCHAR2(60)	
	private String gestor; //RDC_GESTOR				VARCHAR2(300)		
	
	/*
	private String cuentaCorriente;//RDC_CUENTA_CORRIENTE		VARCHAR2(25)
	private String fechaRDC;//RDC_FECHA				VARCHAR2(10)		
	private String numeroRVGL;//RDC_NUMERO_RVGL			VARCHAR2(20)				
	private String salem;//RDC_SALEM				VARCHAR2(5)				
	private String posicionClienteGrupo;//RDC_POSI_CLIENT_GRUPECON	VARCHAR2(150)			
	private String vulnerabilidad;//RDC_VULNERABILIDAD		VARCHAR2(150)				
	private String totalInversion;//RDC_IMPACAMB_TOTAL_INVER	VARCHAR2(20)				
	private String montoPrestamo;//RDC_IMPACAMB_MONTO_PREST	VARCHAR2(20)				
	private String entorno;//RDC_IMPACAMB_ENTORNO_KM		VARCHAR2(20)				
	private String poblacionAfectada;//RDC_IMPACAMB_POBLA_AFECT	VARCHAR2(20)				
	private String categorizacionAmbiental;//RDC_IMPACAMB_CATEG_AMB		VARCHAR2(20)				
	private String comentarioAdmision;//RDC_COMENTARIOS_ADMISION	VARCHAR2(2000)				
	private String ciiuRDC;//RDC_CIIU					VARCHAR2(500)				
	private String contrato;//RDC_CONTRATO				VARCHAR2(25)			
	*/
	private String oficina; //RDC_OFICINA			   VARCHAR2(300)	
	private String segmento;//RDC_SEGMENTO	  		   VARCHAR2(300)	
	
	
	
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DATOSBASICO")
	@Column(name="ID_DATOS_BASICOS")
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
	
	@Column(name="ANIO")
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	@Column(name="CODIGO_EMPRESA", length = 30)
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}	
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	
	@Column(name="DB_NOMBRE_GRUP_EMPR")
	public String getNombreGrupoEmpresa() {
		return nombreGrupoEmpresa;
	}
	public void setNombreGrupoEmpresa(String nombreGrupoEmpresa) {
		this.nombreGrupoEmpresa = nombreGrupoEmpresa;
	}
	
	@Column(name="DB_ACTIVIDAD_PRIN")
	public String getActividadPrincipal() {
		return actividadPrincipal;
	}
	public void setActividadPrincipal(String actividadPrincipal) {
		this.actividadPrincipal = actividadPrincipal;
	}
	
	@Column(name="DB_T_EXT_PAIS_CODIGO")
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	@Column(name="DB_ANTIGUEDAD_NEGO")
	public Integer getAntiguedadNegocio() {
		return antiguedadNegocio;
	}
	public void setAntiguedadNegocio(Integer antiguedadNegocio) {
		this.antiguedadNegocio = antiguedadNegocio;
	}
	
	@Column(name="DB_ANTIGUEDAD_CLIE")
	public Integer getAntiguedadCliente() {
		return antiguedadCliente;
	}
	public void setAntiguedadCliente(Integer antiguedadCliente) {
		this.antiguedadCliente = antiguedadCliente;
	}
	
	@Column(name="DB_GRUP_RIESGO")
	public String getGrupoRiesgoBuro() {
		return grupoRiesgoBuro;
	}
	public void setGrupoRiesgoBuro(String grupoRiesgoBuro) {
		this.grupoRiesgoBuro = grupoRiesgoBuro;
	}
	
	@Column(name="DB_COMEN_ACCI")
	public String getComentAccionariado() {
		return comentAccionariado;
	}
	public void setComentAccionariado(String comentAccionariado) {
		this.comentAccionariado = comentAccionariado;
	}
	
	@Column(name="DB_COMEN_PART_SIGN")
	public String getComentPartiSignificativa() {
		return comentPartiSignificativa;
	}
	public void setComentPartiSignificativa(String comentPartiSignificativa) {
		this.comentPartiSignificativa = comentPartiSignificativa;
	}
	
	@Column(name="DB_COMEN_RAIT_EXTE")
	public String getComentRatinExterno() {
		return comentRatinExterno;
	}
	public void setComentRatinExterno(String comentRatinExterno) {
		this.comentRatinExterno = comentRatinExterno;
	}
	
	@Column(name="DB_VALORACION_GLOBAL")
	public String getComentvaloraGlobal() {
		return comentvaloraGlobal;
	}
	public void setComentvaloraGlobal(String comentvaloraGlobal) {
		this.comentvaloraGlobal = comentvaloraGlobal;
	}
	
	@Column(name="DB_RUC")
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	
	@Column(name="RB_CALIFICACION_BANC")
	public String getCalificacionBanco() {
		return calificacionBanco;
	}
	public void setCalificacionBanco(String calificacionBanco) {
		this.calificacionBanco = calificacionBanco;
	}
	
	/*
	@Column(name="RDC_CUENTA_CORRIENTE")
	public String getCuentaCorriente() {
		return cuentaCorriente;
	}
	public void setCuentaCorriente(String cuentaCorriente) {
		this.cuentaCorriente = cuentaCorriente;
	}
	*/
	
	@Column(name="RDC_GESTOR")
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	
	
	/*
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
	
	@Column(name="RDC_POSI_CLIENT_GRUPECON")
	public String getPosicionClienteGrupo() {
		return posicionClienteGrupo;
	}
	public void setPosicionClienteGrupo(String posicionClienteGrupo) {
		this.posicionClienteGrupo = posicionClienteGrupo;
	}
	
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
	
	@Column(name="RDC_CONTRATO")
	public String getContrato() {
		return contrato;
	}
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	*/
	
	@Column(name="RDC_OFICINA")
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	
	@Column(name="RDC_SEGMENTO")
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	
	
	
	
	
	
	
	
		
}
