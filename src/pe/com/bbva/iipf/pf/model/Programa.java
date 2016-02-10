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

import pe.com.bbva.iipf.mantenimiento.model.Tabla;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name = "TIIPF_PROGRAMA")
@NamedQueries({
@NamedQuery(name="updateDatosBasicos", 
		    query = " update Programa set comentAccionariado = ? ," +
		    		" comentPartiSignificativa = ?, comentRatinExterno = ?," +
		    		" comentvaloraGlobal = ? , actividadPrincipal = ?  " +
		    		"where id = ?"),
@NamedQuery(name="updateLineasRelBancarias", 
		    query = " update Programa set tipoMilesRB = ?  " +
		    		" where id = ?"),
@NamedQuery(name="updateRelacionesBancarias", 
		    query = " update Programa set cuotaFinanciera=?,porcentajeNormalSF=?,porcentajeProblemaPotencialSF=?,porcentajeDeficienteSF=?,porcentajeDudosoSF=?,porcentajePerdidaSF=?,calificacionBanco=?, " +
		    		" efectividadProm6sol = ? , efectividadProm6dol = ?,  " +
		    		" protestoProm6sol = ? , protestoProm6dol = ?,  " +
		    		" efectividadUltmaniosol = ? , efectividadUltmaniodol = ?,  " +
		    		" comentcuotaFinanciera = ? , " +
		    		" idmodeloRentabilidad = ?,  " +
		    		" saldoMedioRecGest = ?, caja = ?, " +
		    		" flujoTransaccional = ?, ventaCostoVenta = ?, " +
		    		" fechaIndiceTransa = ? " +		    		
					" where id = ?"),
@NamedQuery(name="updatePoliticasRiesgo", 
		    query = " update Programa set limiteAutorizadoPRG=?,proximaRevisionPRG=?,motivoProximaPRG=?, tipoMilesPLR=? where id=?"),
@NamedQuery(name="updatePropuestaRiesgo", 
		    query = " update Programa set tipoEstructura=?,tipoMiles=? where id=?"),
@NamedQuery(name="updateFechaActualizacion", 
		    query = " update Programa set fechaModificacion = sysdate " +
		    		"where id = ?"),
@NamedQuery(name="updateFechaUsuarioActualizacion", 
	    query = " update Programa set fechaModificacion = sysdate,codUsuarioModificacion=? " +
	    		"where id = ?"),		    		
@NamedQuery(name="concluirPrograma", 
			query = " update Programa set estadoPrograma = 8203 where id=?"),
@NamedQuery(name="concluirPrograma2", 
					query = " update Programa set estadoPrograma = 8203,motivoCierre.id=?,observacionCierre=?,fechaCierre=?,codUsuarioCierre=? where id=?"),
@NamedQuery(name="buscarProgramaEmprEstPend", 
			query = " from Programa where tipoEmpresa=2 and  estadoPrograma = 9617 and  idEmpresa = ?"),
@NamedQuery(name="buscarProgramaGrupEstPend", 
			query = " from Programa where tipoEmpresa=3 and  estadoPrograma = 9617 and  idGrupo = ?"),
@NamedQuery(name="updateReporteCredito", 
			query = " update Programa set cuentaCorriente = ? ," +				    
				    " fechaRDC = ?, numeroRVGL = ?," +
				    " salem = ?," +
				    " vulnerabilidad = ?, totalInversion = ?," +
				    " montoPrestamo = ?, entorno = ?," +
				    " poblacionAfectada = ?, categorizacionAmbiental = ?," +				   
				    " comentarioAdmision = ?, ciiuRDC = ? " +				    
				    " where id = ?"),
@NamedQuery(name="updateClasificacionBancaria", 
	query = " update Programa set calificacionBanco = ?  " +
						    " where id = ?"),					    
@NamedQuery(name="updateEfectividadPrograma", 
			query = " update Programa set efectividadProm6sol = ? , efectividadProm6dol = ? , protestoProm6sol = ? , protestoProm6dol = ? where id=?"),
@NamedQuery(name="updateClasificacionFinanciera", 
			query = " update Programa set porcentajeNormalSF = ? , porcentajeProblemaPotencialSF = ? , porcentajeDeficienteSF = ? , porcentajeDudosoSF = ? , porcentajePerdidaSF = ? where id=?"),			
@NamedQuery(name="refreshDatosBasicosHost", 
	    query = " update Programa set pais = ? ," +
	    		" antiguedadNegocio = ?, antiguedadCliente = ?," +	    		
	    		" calificacionBanco = ?, gestor = ?," +
	    		" oficina = ?, segmento = ?," +
	    		" grupoRiesgoBuro = ? " +
	    		" where id = ? "),
@NamedQuery(name="updateClasificacionBancariaCuotaFinanciera", 
		query = " update Programa set calificacionBanco = ?,  " +
				" cuotaFinanciera = ? " +	  
				" where id = ?"),
@NamedQuery(name="updateTipoOperacion", 
		query = " update Programa set tipoOperacion.id = ? " +					  
				" where id = ?"),
@NamedQuery(name="updateEstadoDescargaPDF", 
query = " update Programa set estadoDescargaPDF = ? " +
		"where id = ?")
})
@SequenceGenerator(name = "SEQ_PROGRAMA", sequenceName = "PROFIN.SEQ_PROGRAMA", allocationSize = 1, initialValue = 20000)
public class Programa extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long Id; //ID_PROGRAMA	NUMBER

	private Integer anio;//ANIO	NUMBER
	
	/**
	 * Codigo del grupo
	 */
	private String idGrupo;//ID_GRUPO	VARCHAR2(60 BYTE)
	//PERIODO	VARCHAR2(4 BYTE)
	private Tabla tipoPrograma;// TT_ID_TIPO_PROGRAMA	VARCHAR2(30 BYTE)

	/**
	 * tipo de Empresa o Grupo
	 */
	private Tabla tipoEmpresa;//TT_ID_TIPO_EMPRESA	NUMBER
	
	/**
	 * Codigo de la empresa
	 */
	private String idEmpresa;//	ID_EMPRESA	NUMBER
	private String ruc;//DB_RUC VARCHAR2(11)
	private String nombreGrupoEmpresa;//DB_NOMBRE_GRUP_EMPR	VARCHAR2(400 BYTE)
	private String actividadPrincipal;//	DB_ACTIVIDAD_PRIN	VARCHAR2(500 BYTE)
//	DB_ACTIVIDAD_EMPRESARIAL	VARCHAR2(300 BYTE)
	private String pais;//	DB_T_EXT_PAIS_CODIGO	VARCHAR2(60 BYTE)
	private Integer antiguedadNegocio;//	DB_ANTIGUEDAD_NEGO	NUMBER
	private Integer antiguedadCliente;//	DB_ANTIGUEDAD_CLIE	NUMBER
	private String  grupoRiesgoBuro;//	DB_GRUP_RIESGO	NUMBER
	private String comentAccionariado;//	DB_COMEN_ACCI	VARCHAR2(120 BYTE)
	private String comentPartiSignificativa;//	DB_COMEN_PART_SIGN	VARCHAR2(160 BYTE)
	private String comentRatinExterno;//	DB_COMEN_RAIT_EXTE	VARCHAR2(160 BYTE)
	private String comentvaloraGlobal;//	DB_VALORACION_GLOBAL	VARCHAR2(160 BYTE)
	private String porcentajeNormalSF;//	RB_CALI_SIST_FINA_NORM	VARCHAR2(4 BYTE)
	private String porcentajeProblemaPotencialSF; //	RB_CALI_SIST_FINA_CPP	VARCHAR2(4 BYTE)
	private String porcentajeDeficienteSF;//	RB_CALI_SIST_FINA_DEFI	VARCHAR2(4 BYTE)
	private String porcentajeDudosoSF;//	RB_CALI_SIST_FINA_DUDO	VARCHAR2(4 BYTE)
	private String porcentajePerdidaSF;	//RB_CALI_SIST_FINA_PERD	NUMBER	Y			
	private String calificacionBanco;//	RB_CALIFICACION_BANC	VARCHAR2(4 BYTE)
	private String	cuotaFinanciera; //RB_CUOTA_FINANCIERA	NUMBER
	private String comentcuotaFinanciera;//RB_COMENT_CUOTA_FINANCIERA VARCHAR2(1000)
	private String idmodeloRentabilidad;//	RB_TT_ID_MODEL_RENTABI	NUMBER
	private String efectividadProm6sol;//	RB_EFEC_PROM6SOL	NUMBER
	private String efectividadProm6dol;//	RB_EFEC_PROM6DOL	NUMBER
	private String protestoProm6sol;//	RB_PROP_PROM6SOL	NUMBER
	private String protestoProm6dol;//	RB_PROP_PROM6DOL	NUMBER
	private String efectividadUltmaniosol;//	RB_EFEC_ULTM_ANIOSOLES	NUMBER
	private String efectividadUltmaniodol;//	RB_EFEC_ULTM_ANIODOLA	NUMBER
	private String limiteAutorizadoPRG;//	PRG_LIMITES_AUTO	VARCHAR2(60 BYTE)
	private String proximaRevisionPRG;//	PRG_PROXIMA_REVI_RIES	VARCHAR2(60 BYTE)
	private String motivoProximaPRG;//	PRG_MOTIVO_PROX_REV	VARCHAR2(60 BYTE)
	private String tipoEstructura;//PR_TT_ID_TIPO_ESTRUCTURA VARCHAR2(1 BYTE)

	private Tabla estadoPrograma;
	
	private String tipoMiles;// TT_ID_TIPO_MILES	VARCHAR2(30 BYTE)

	private Long tipoMilesPLR; //PLR_TT_ID_TIPO_MILES	NUMBER
	
	private Long tipoMilesRB; //RB_TT_ID_TIPO_MILES	NUMBER
	
	//add ini MCG20121031
	private String numeroSolicitud;//NUMERO_SOLICITUD
	
	private String cuentaCorriente;//RDC_CUENTA_CORRIENTE	VARCHAR2(25)	Y			
	private String dniCarnetExt;//RDC_DNI_CARNETEXT	VARCHAR2(20)	Y			
	private String rucRDC;//RDC_RUC	VARCHAR2(20)	Y			
	private String ciiuRDC; //RDC_CIIU VARCHAR2(500)
	private String gestor;//RDC_GESTOR	VARCHAR2(50)	Y			
	//private Tabla segmento;//TT_ID_SEGMENTO	NUMBER	Y	
	private String idsegmento;//TT_ID_SEGMENTO	NUMBER	Y
	private String fechaRDC;//RDC_FECHA	VARCHAR2(10)	Y			
	//private Tabla oficina;//TT_ID_OFICINA	NUMBER	Y			
	private String idoficina;//TT_ID_OFICINA	NUMBER	Y
	private String numeroRVGL;//RDC_NUMERO_RVGL	VARCHAR2(20)	Y			
	private String salem;//RDC_SALEM	VARCHAR2(5)	Y			
	private String posicionClienteGrupo;//RDC_POSI_CLIENT_GRUPECON	VARCHAR2(150)	Y			
	private String vulnerabilidad;//RDC_VULNERABILIDAD	VARCHAR2(150)	Y			
	private String totalInversion;//RDC_IMPACAMB_TOTAL_INVER	VARCHAR2(20)	Y			
	private String montoPrestamo;//RDC_IMPACAMB_MONTO_PREST	VARCHAR2(20)	Y			
	private String entorno;//RDC_IMPACAMB_ENTORNO_KM	VARCHAR2(20)	Y			
	private String poblacionAfectada;//RDC_IMPACAMB_POBLA_AFECT	VARCHAR2(20)	Y			
	private String categorizacionAmbiental;//RDC_IMPACAMB_CATEG_AMB	VARCHAR2(20)	Y			
	private String comentarioAdmision;//RDC_COMENTARIOS_ADMISION	VARCHAR2(2000)	Y
	private String contrato;//RDC_CONTRATO	VARCHAR2(25)	
	private String oficina;//RDC_OFICINA VARCHAR2(25)
	private String segmento;//RDC_SEGMENTO
	
	private transient String activoValida;
	private transient String periodoArchivo;
	
	private Tabla motivoCierre;
	private String observacionCierre;
	private String fechaCierre;
	private String codUsuarioCierre;
	
	private transient String cadEmpresaxGrupo;
	

	//add fin MCG20121031
	
	//INI MCG20140815
	private String saldoMedioRecGest; //RB_IT_SALDO_MEDIO_RECURGEST	N	VARCHAR2(30)	Y			
	private String caja;//RB_IT_CAJA	N	VARCHAR2(30)	Y			
	private String flujoTransaccional;//RB_IT_FLUJO_TRANSACCIONAL	N	VARCHAR2(30)	Y			
	private String ventaCostoVenta; //RB_IT_VENTA_COSTOVENTA	N	VARCHAR2(30)	Y		
	private String fechaIndiceTransa;//RB_IT_FECHA	N	VARCHAR2(10)	Y	
	
	private transient String resultSaldo; 			
	private transient String resultFlujo; 
	//FIN mcg20140815
	
	private Tabla tipoOperacion;
	
	private String estadoDescargaPDF;
	
	
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PROGRAMA")
	@Column(name="ID_PROGRAMA")
	public Long getId() { 
		return Id;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_ESTADO_PROGRAMA")
	public Tabla getEstadoPrograma() {
		return estadoPrograma;
	}

	public void setEstadoPrograma(Tabla estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}
	
	@Column(name="ID_GRUPO", length=20)
	public String getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	@Column(name="ID_EMPRESA", length=12)
	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	@Column(name="DB_RUC", length=12)
	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	@Column(name="DB_NOMBRE_GRUP_EMPR", length=400)
	public String getNombreGrupoEmpresa() {
		return nombreGrupoEmpresa;
	}

	public void setNombreGrupoEmpresa(String nombreGrupoEmpresa) {
		this.nombreGrupoEmpresa = nombreGrupoEmpresa;
	}

	public void setId(Long id) {
		Id = id;
	}
	
	@Column(name="ANIO")
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_PROGRAMA")
	public Tabla getTipoPrograma() {
		return tipoPrograma;
	}

	public void setTipoPrograma(Tabla tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_EMPRESA")
	public Tabla getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(Tabla tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	@Column( name="DB_ACTIVIDAD_PRIN", length=500)
	public String getActividadPrincipal() {
		return actividadPrincipal;
	}

	public void setActividadPrincipal(String actividadPrincipal) {
		this.actividadPrincipal = actividadPrincipal;
	}

	@Column( name="DB_T_EXT_PAIS_CODIGO", length=60)
	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Column( name="DB_ANTIGUEDAD_NEGO",nullable=true)	
	public Integer getAntiguedadNegocio() {
		return antiguedadNegocio;
	}

	public void setAntiguedadNegocio(Integer antiguedadNegocio) {
		this.antiguedadNegocio = antiguedadNegocio;
	}

	@Column( name="DB_ANTIGUEDAD_CLIE",nullable=true)
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

	@Column( name="DB_COMEN_ACCI", length=1000)
	public String getComentAccionariado() {
		return comentAccionariado;
	}

	public void setComentAccionariado(String comentAccionariado) {
		this.comentAccionariado = comentAccionariado;
	}

	@Column( name="DB_COMEN_PART_SIGN", length=1000)
	public String getComentPartiSignificativa() {
		return comentPartiSignificativa;
	}

	public void setComentPartiSignificativa(String comentPartiSignificativa) {
		this.comentPartiSignificativa = comentPartiSignificativa;
	}

	@Column( name="DB_COMEN_RAIT_EXTE", length=1000)
	public String getComentRatinExterno() {
		return comentRatinExterno;
	}

	public void setComentRatinExterno(String comentRatinExterno) {
		this.comentRatinExterno = comentRatinExterno;
	}

	@Column( name="DB_VALORACION_GLOBAL", length=1000)
	public String getComentvaloraGlobal() {
		return comentvaloraGlobal;
	}

	public void setComentvaloraGlobal(String comentvaloraGlobal) {
		this.comentvaloraGlobal = comentvaloraGlobal;
	}

	@Column( name="RB_CUOTA_FINANCIERA")
	public String getCuotaFinanciera() {
		try {
			Double valPorcDoubleGet=Double.parseDouble(cuotaFinanciera);
			if(valPorcDoubleGet<=1){
					valPorcDoubleGet=Double.parseDouble(cuotaFinanciera) *100;
			}
			String cuotaFinanciera=String.valueOf(valPorcDoubleGet);
			return cuotaFinanciera;
		} catch (Exception e) {
			 
		}
		return cuotaFinanciera;
	}

	public void setCuotaFinanciera(String cuotaFinanciera) {
		this.cuotaFinanciera = cuotaFinanciera;
	}
	@Column( name="RB_CALI_SIST_FINA_NORM")
	public String getPorcentajeNormalSF() {
		return porcentajeNormalSF;
	}

	public void setPorcentajeNormalSF(String porcentajeNormalSF) {
		this.porcentajeNormalSF = porcentajeNormalSF;
	}
	@Column( name="RB_CALI_SIST_FINA_CPP")
	public String getPorcentajeProblemaPotencialSF() {
		return porcentajeProblemaPotencialSF;
	}

	public void setPorcentajeProblemaPotencialSF(
			String porcentajeProblemaPotencialSF) {
		this.porcentajeProblemaPotencialSF = porcentajeProblemaPotencialSF;
	}
	@Column( name="RB_CALI_SIST_FINA_DEFI")
	public String getPorcentajeDeficienteSF() {
		return porcentajeDeficienteSF;
	}

	public void setPorcentajeDeficienteSF(String porcentajeDeficienteSF) {
		this.porcentajeDeficienteSF = porcentajeDeficienteSF;
	}
	@Column( name="RB_CALI_SIST_FINA_DUDO")
	public String getPorcentajeDudosoSF() {
		return porcentajeDudosoSF;
	}

	public void setPorcentajeDudosoSF(String porcentajeDudosoSF) {
		this.porcentajeDudosoSF = porcentajeDudosoSF;
	}
	@Column( name="RB_CALI_SIST_FINA_PERD")
	public String getPorcentajePerdidaSF() {
		return porcentajePerdidaSF;
	}

	public void setPorcentajePerdidaSF(String porcentajePerdidaSF) {
		this.porcentajePerdidaSF = porcentajePerdidaSF;
	}
	@Column( name="RB_CALIFICACION_BANC", length = 60)
	public String getCalificacionBanco() {
		return calificacionBanco;
	}

	public void setCalificacionBanco(String calificacionBanco) {
		this.calificacionBanco = calificacionBanco;
	}

	@Column( name="PRG_LIMITES_AUTO")
	public String getLimiteAutorizadoPRG() {
		return limiteAutorizadoPRG;
	}

	public void setLimiteAutorizadoPRG(String limiteAutorizadoPRG) {
		this.limiteAutorizadoPRG = limiteAutorizadoPRG;
	}

	@Column( name="PRG_PROXIMA_REVI_RIES")
	public String getProximaRevisionPRG() {
		return proximaRevisionPRG;
	}

	public void setProximaRevisionPRG(String proximaRevisionPRG) {
		this.proximaRevisionPRG = proximaRevisionPRG;
	}

	@Column( name="PRG_MOTIVO_PROX_REV")
	public String getMotivoProximaPRG() {
		return motivoProximaPRG;
	}

	public void setMotivoProximaPRG(String motivoProximaPRG) {
		this.motivoProximaPRG = motivoProximaPRG;
	}

	@Column( name="RB_EFEC_PROM6SOL")
	public String getEfectividadProm6sol() {
		return efectividadProm6sol;
	}

	public void setEfectividadProm6sol(String efectividadProm6sol) {
		this.efectividadProm6sol = efectividadProm6sol;
	}
	
	@Column( name="RB_EFEC_PROM6DOL")
	public String getEfectividadProm6dol() {
		return efectividadProm6dol;
	}

	public void setEfectividadProm6dol(String efectividadProm6dol) {
		this.efectividadProm6dol = efectividadProm6dol;
	}

	@Column( name="RB_PROP_PROM6SOL")
	public String getProtestoProm6sol() {
		return protestoProm6sol;
	}

	public void setProtestoProm6sol(String protestoProm6sol) {
		this.protestoProm6sol = protestoProm6sol;
	}
	
	@Column( name="RB_PROP_PROM6DOL")
	public String getProtestoProm6dol() {
		return protestoProm6dol;
	}

	public void setProtestoProm6dol(String protestoProm6dol) {
		this.protestoProm6dol = protestoProm6dol;
	}
	
	@Column( name="RB_EFEC_ULTM_ANIOSOLES")
	public String getEfectividadUltmaniosol() {
		return efectividadUltmaniosol;
	}

	public void setEfectividadUltmaniosol(String efectividadUltmaniosol) {
		this.efectividadUltmaniosol = efectividadUltmaniosol;
	}

	@Column( name="RB_EFEC_ULTM_ANIODOLA")
	public String getEfectividadUltmaniodol() {
		return efectividadUltmaniodol;
	}

	public void setEfectividadUltmaniodol(String efectividadUltmaniodol) {
		this.efectividadUltmaniodol = efectividadUltmaniodol;
	}
	@Column( name="RB_COMENT_CUOTA_FINANCIERA")
	public String getComentcuotaFinanciera() {
		return comentcuotaFinanciera;
	}

	public void setComentcuotaFinanciera(String comentcuotaFinanciera) {
		this.comentcuotaFinanciera = comentcuotaFinanciera;
	}
	
	@Column( name="RB_TT_ID_MODEL_RENTABI")
	public String getIdmodeloRentabilidad() {
		return idmodeloRentabilidad;
	}

	public void setIdmodeloRentabilidad(String idmodeloRentabilidad) {
		this.idmodeloRentabilidad = idmodeloRentabilidad;
	}

	@Column( name="PR_TT_ID_TIPO_ESTRUCTURA")
	public String getTipoEstructura() {
		return tipoEstructura;
	}

	public void setTipoEstructura(String tipoEstructura) {
		this.tipoEstructura = tipoEstructura;
	}

	@Column( name="PR_TT_ID_TIPO_MILES")
	public String getTipoMiles() {
		return tipoMiles;
	}

	public void setTipoMiles(String tipoMiles) {
		this.tipoMiles = tipoMiles;
	}

	@Column(name="PLR_TT_ID_TIPO_MILES")
	public Long getTipoMilesPLR() {
		return tipoMilesPLR;
	}

	public void setTipoMilesPLR(Long tipoMilesPLR) {
		this.tipoMilesPLR = tipoMilesPLR;
	}

	@Column(name="RB_TT_ID_TIPO_MILES")
	public Long getTipoMilesRB() {
		return tipoMilesRB;
	}

	public void setTipoMilesRB(Long tipoMilesRB) {
		this.tipoMilesRB = tipoMilesRB;
	}

	@Column(name="NUMERO_SOLICITUD")
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	@Column(name="RDC_CUENTA_CORRIENTE")
	public String getCuentaCorriente() {
		return cuentaCorriente;
	}

	public void setCuentaCorriente(String cuentaCorriente) {
		this.cuentaCorriente = cuentaCorriente;
	}

	@Column(name="RDC_DNI_CARNETEXT")
	public String getDniCarnetExt() {
		return dniCarnetExt;
	}

	public void setDniCarnetExt(String dniCarnetExt) {
		this.dniCarnetExt = dniCarnetExt;
	}

	@Column(name="RDC_RUC")
	public String getRucRDC() {
		return rucRDC;
	}

	public void setRucRDC(String rucRDC) {
		this.rucRDC = rucRDC;
	}
	
	
	@Column(name="RDC_CIIU")
	public String getCiiuRDC() {
		return ciiuRDC;
	}

	public void setCiiuRDC(String ciiuRDC) {
		this.ciiuRDC = ciiuRDC;
	}

	@Column(name="RDC_GESTOR")
	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

//	@ManyToOne(targetEntity = Tabla.class)
//	@JoinColumn(name="TT_ID_SEGMENTO")
//	public Tabla getSegmento() {
//		return segmento;
//	}
//
//	public void setSegmento(Tabla segmento) {
//		this.segmento = segmento;
//	}

	@Column(name="TT_ID_SEGMENTO")
	public String getIdsegmento() {
		return idsegmento;
	}

	public void setIdsegmento(String idsegmento) {
		this.idsegmento = idsegmento;
	}
	
	
	@Column(name="RDC_FECHA")
	public String getFechaRDC() {
		return fechaRDC;
	}

	public void setFechaRDC(String fechaRDC) {
		this.fechaRDC = fechaRDC;
	}

	
//	@ManyToOne(targetEntity = Tabla.class)
//	@JoinColumn(name="TT_ID_OFICINA")
//	public Tabla getOficina() {
//		return oficina;
//	}
//
//	public void setOficina(Tabla oficina) {
//		this.oficina = oficina;
//	}
	@Column(name="TT_ID_OFICINA")
	public String getIdoficina() {
		return idoficina;
	}

	public void setIdoficina(String idoficina) {
		this.idoficina = idoficina;
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
		
	@Column(name="RDC_CONTRATO")
	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	
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

	@Transient // no mapeada
	public String getActivoValida() {
		return activoValida;
	}

	public void setActivoValida(String activoValida) {
		this.activoValida = activoValida;
	}
	
	@Transient
	public String getPeriodoArchivo() {
		return periodoArchivo;
	}

	public void setPeriodoArchivo(String periodoArchivo) {
		this.periodoArchivo = periodoArchivo;
	}

	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_MOTIVO_CIERRE")
	public Tabla getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(Tabla motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	@Column(name="OBSERVACION_CIERRE")
	public String getObservacionCierre() {
		return observacionCierre;
	}

	public void setObservacionCierre(String observacionCierre) {
		this.observacionCierre = observacionCierre;
	}

	@Column(name="FECHA_CIERRE")
	public String getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@Column(name="COD_USUARIO_CIERRE")
	public String getCodUsuarioCierre() {
		return codUsuarioCierre;
	}

	public void setCodUsuarioCierre(String codUsuarioCierre) {
		this.codUsuarioCierre = codUsuarioCierre;
	}

	
	@Transient
	public String getCadEmpresaxGrupo() {
		return cadEmpresaxGrupo;
	}

	public void setCadEmpresaxGrupo(String cadEmpresaxGrupo) {
		this.cadEmpresaxGrupo = cadEmpresaxGrupo;
	}

	
	@Column(name="RB_IT_SALDO_MEDIO_RECURGEST")
	public String getSaldoMedioRecGest() {
		return saldoMedioRecGest;
	}

	public void setSaldoMedioRecGest(String saldoMedioRecGest) {
		this.saldoMedioRecGest = saldoMedioRecGest;
	}

	@Column(name="RB_IT_CAJA")
	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	@Column(name="RB_IT_FLUJO_TRANSACCIONAL")
	public String getFlujoTransaccional() {
		return flujoTransaccional;
	}

	public void setFlujoTransaccional(String flujoTransaccional) {
		this.flujoTransaccional = flujoTransaccional;
	}

	@Column(name="RB_IT_VENTA_COSTOVENTA")
	public String getVentaCostoVenta() {
		return ventaCostoVenta;
	}

	public void setVentaCostoVenta(String ventaCostoVenta) {
		this.ventaCostoVenta = ventaCostoVenta;
	}


	@Column(name="RB_IT_FECHA")
	public String getFechaIndiceTransa() {
		return fechaIndiceTransa;
	}

	public void setFechaIndiceTransa(String fechaIndiceTransa) {
		this.fechaIndiceTransa = fechaIndiceTransa;
	}	

	@Transient // no mapeada
	public String getResultSaldo() {
		return resultSaldo;
	}

	public void setResultSaldo(String resultSaldo) {
		this.resultSaldo = resultSaldo;
	}

	@Transient // no mapeada
	public String getResultFlujo() {
		return resultFlujo;
	}

	public void setResultFlujo(String resultFlujo) {
		this.resultFlujo = resultFlujo;
	}
	
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_OPERACION")
	public Tabla getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(Tabla tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	@Column(name="ESTADO_DESCARGAPDF")
	public String getEstadoDescargaPDF() {
		return estadoDescargaPDF;
	}
	
	public void setEstadoDescargaPDF(String estadoDescargaPDF) {
		this.estadoDescargaPDF = estadoDescargaPDF;
	}
	
	
	
	
	
}
