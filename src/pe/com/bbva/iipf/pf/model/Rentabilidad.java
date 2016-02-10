package pe.com.bbva.iipf.pf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema="PROFIN",  name="TIIPF_PFRENBEC")
@SequenceGenerator(name = "SEQ_RENTABILIDAD", sequenceName = "PROFIN.SEQ_RENTABILIDAD", allocationSize = 1, initialValue = 20000)
public class Rentabilidad {
	
	
	private Long id; //	ID_PFRENBEC	INTEGER	Y		
	private String codCentral;//	CODCENTRAL	VARCHAR2(8)	Y			
	private String salmedioInversion;//	SALMEDIO_INVERSION	VARCHAR2(13)	Y			
	private String salmedioRecurso;//	SALMEDIO_RECURSO	VARCHAR2(13)	Y			
	private String salmedioDesintermed;//	SALMEDIO_DESINTERMED	VARCHAR2(13)	Y			
	private String importeServicio;//	IMPORTE_SERVICIO	VARCHAR2(13)	Y			
	private String cuentaCentral;//	CUENTA_CENTRAL	VARCHAR2(13)	Y			
	private String spfmesInversion;//	SPF_MES_INVERSION	VARCHAR2(13)	Y			
	private String spfmesRecurso;//	SPF_MES_RECURSO	VARCHAR2(13)	Y			
	private String spfmesDesintermed;//	SPF_MES_DESINTERMED	VARCHAR2(13)	Y			
	private String margenFinanciero;//	MARGEN_FINANCIERO	VARCHAR2(13)	Y			
	private String comisiones ;//	COMISIONES	VARCHAR2(13)	Y			
	private String margenOrdinario;//	MARGEN_ORDINARIO	VARCHAR2(13)	Y			
	private String saneamientoNeto;//	SANEAMIENTO_NETO	VARCHAR2(13)	Y			
	private String costesOperativo;//	COSTES_OPERATIVO	VARCHAR2(13)	Y			
	private String resultado;//	RESULTADO	VARCHAR2(13)	Y			
	private String roa;//	ROA	VARCHAR2(13)	Y			
	private Date fechaProceso;//	FECHA_PROCESO	DATE	Y
	
	
	private String salmedioInversionAcu;//  salmedio_inversion_acu   VARCHAR2(13),
	private String salmedioRecursoAcu;//  salmedio_recurso_acu     VARCHAR2(13),
	private String salmedioDesintermedAcu;//  salmedio_desintermed_acu VARCHAR2(13),
	private String importeServicioAcu;//  importe_servicio_acu     VARCHAR2(13),
	private String cuentaCentralAcu;//  cuenta_central_acu       VARCHAR2(13),
	
	private String margenFinancieroAcu;//  margen_financiero_acu    VARCHAR2(13),
	private String comisionesAcu ;// comisiones_acu           VARCHAR2(13),
	private String margenOrdinarioAcu;//  margen_ordinario_acu     VARCHAR2(13),
	private String saneamientoNetoAcu;//  saneamiento_neto_acu     VARCHAR2(13),
	private String costesOperativoAcu;//  costes_operativo_acu     VARCHAR2(13),
	private String resultadoAcu;//  resultado_acu            VARCHAR2(13),
	private String roaAcu;//  roa_acu                  VARCHAR2(13),
	private String tipoarchivo;//  tipo_archivo             VARCHAR2(1)
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RENTABILIDAD")
	@Column(name="ID_PFRENBEC")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="CODCENTRAL")
	public String getCodCentral() {
		return codCentral;
	}
	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}
	@Column(name="SALMEDIO_INVERSION")
	public String getSalmedioInversion() {
		return salmedioInversion;
	}
	public void setSalmedioInversion(String salmedioInversion) {
		this.salmedioInversion = salmedioInversion;
	}
	@Column(name="SALMEDIO_RECURSO")
	public String getSalmedioRecurso() {
		return salmedioRecurso;
	}
	public void setSalmedioRecurso(String salmedioRecurso) {
		this.salmedioRecurso = salmedioRecurso;
	}
	@Column(name="SALMEDIO_DESINTERMED")
	public String getSalmedioDesintermed() {
		return salmedioDesintermed;
	}
	public void setSalmedioDesintermed(String salmedioDesintermed) {
		this.salmedioDesintermed = salmedioDesintermed;
	}
	@Column(name="IMPORTE_SERVICIO")
	public String getImporteServicio() {
		return importeServicio;
	}
	public void setImporteServicio(String importeServicio) {
		this.importeServicio = importeServicio;
	}
	@Column(name="CUENTA_CENTRAL")
	public String getCuentaCentral() {
		return cuentaCentral;
	}
	public void setCuentaCentral(String cuentaCentral) {
		this.cuentaCentral = cuentaCentral;
	}
	@Column(name="SPF_MES_INVERSION")
	public String getSpfmesInversion() {
		return spfmesInversion;
	}
	public void setSpfmesInversion(String spfmesInversion) {
		this.spfmesInversion = spfmesInversion;
	}
	@Column(name="SPF_MES_RECURSO")
	public String getSpfmesRecurso() {
		return spfmesRecurso;
	}
	public void setSpfmesRecurso(String spfmesRecurso) {
		this.spfmesRecurso = spfmesRecurso;
	}
	@Column(name="SPF_MES_DESINTERMED")
	public String getSpfmesDesintermed() {
		return spfmesDesintermed;
	}
	public void setSpfmesDesintermed(String spfmesDesintermed) {
		this.spfmesDesintermed = spfmesDesintermed;
	}
	@Column(name="MARGEN_FINANCIERO")
	public String getMargenFinanciero() {
		return margenFinanciero;
	}
	public void setMargenFinanciero(String margenFinanciero) {
		this.margenFinanciero = margenFinanciero;
	}
	@Column(name="COMISIONES")
	public String getComisiones() {
		return comisiones;
	}
	public void setComisiones(String comisiones) {
		this.comisiones = comisiones;
	}
	@Column(name="MARGEN_ORDINARIO")
	public String getMargenOrdinario() {
		return margenOrdinario;
	}
	public void setMargenOrdinario(String margenOrdinario) {
		this.margenOrdinario = margenOrdinario;
	}
	@Column(name="SANEAMIENTO_NETO")
	public String getSaneamientoNeto() {
		return saneamientoNeto;
	}
	public void setSaneamientoNeto(String saneamientoNeto) {
		this.saneamientoNeto = saneamientoNeto;
	}
	@Column(name="COSTES_OPERATIVO")
	public String getCostesOperativo() {
		return costesOperativo;
	}
	public void setCostesOperativo(String costesOperativo) {
		this.costesOperativo = costesOperativo;
	}
	@Column(name="RESULTADO")
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	@Column(name="ROA")
	public String getRoa() {
		return roa;
	}
	public void setRoa(String roa) {
		this.roa = roa;
	}
	@Column(name="FECHA_PROCESO")
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
	@Column(name="SALMEDIO_INVERSION_ACU")
	public String getSalmedioInversionAcu() {
		return salmedioInversionAcu;
	}
	public void setSalmedioInversionAcu(String salmedioInversionAcu) {
		this.salmedioInversionAcu = salmedioInversionAcu;
	}
	@Column(name="SALMEDIO_RECURSO_ACU")
	public String getSalmedioRecursoAcu() {
		return salmedioRecursoAcu;
	}
	public void setSalmedioRecursoAcu(String salmedioRecursoAcu) {
		this.salmedioRecursoAcu = salmedioRecursoAcu;
	}
	
	@Column(name="SALMEDIO_DESINTERMED_ACU")	
	public String getSalmedioDesintermedAcu() {
		return salmedioDesintermedAcu;
	}
	public void setSalmedioDesintermedAcu(String salmedioDesintermedAcu) {
		this.salmedioDesintermedAcu = salmedioDesintermedAcu;
	}
	
	@Column(name="IMPORTE_SERVICIO_ACU")
	public String getImporteServicioAcu() {
		return importeServicioAcu;
	}
	public void setImporteServicioAcu(String importeServicioAcu) {
		this.importeServicioAcu = importeServicioAcu;
	}
	
	@Column(name="CUENTA_CENTRAL_ACU")
	public String getCuentaCentralAcu() {
		return cuentaCentralAcu;
	}
	public void setCuentaCentralAcu(String cuentaCentralAcu) {
		this.cuentaCentralAcu = cuentaCentralAcu;
	}
	@Column(name="MARGEN_FINANCIERO_ACU")
	public String getMargenFinancieroAcu() {
		return margenFinancieroAcu;
	}
	public void setMargenFinancieroAcu(String margenFinancieroAcu) {
		this.margenFinancieroAcu = margenFinancieroAcu;
	}
	@Column(name="COMISIONES_ACU")
	public String getComisionesAcu() {
		return comisionesAcu;
	}
	public void setComisionesAcu(String comisionesAcu) {
		this.comisionesAcu = comisionesAcu;
	}
	@Column(name="MARGEN_ORDINARIO_ACU")
	public String getMargenOrdinarioAcu() {
		return margenOrdinarioAcu;
	}
	public void setMargenOrdinarioAcu(String margenOrdinarioAcu) {
		this.margenOrdinarioAcu = margenOrdinarioAcu;
	}
	@Column(name="SANEAMIENTO_NETO_ACU")
	public String getSaneamientoNetoAcu() {
		return saneamientoNetoAcu;
	}
	public void setSaneamientoNetoAcu(String saneamientoNetoAcu) {
		this.saneamientoNetoAcu = saneamientoNetoAcu;
	}
	@Column(name="COSTES_OPERATIVO_ACU")
	public String getCostesOperativoAcu() {
		return costesOperativoAcu;
	}
	public void setCostesOperativoAcu(String costesOperativoAcu) {
		this.costesOperativoAcu = costesOperativoAcu;
	}
	@Column(name="RESULTADO_ACU")
	public String getResultadoAcu() {
		return resultadoAcu;
	}
	public void setResultadoAcu(String resultadoAcu) {
		this.resultadoAcu = resultadoAcu;
	}
	@Column(name="ROA_ACU")
	public String getRoaAcu() {
		return roaAcu;
	}
	public void setRoaAcu(String roaAcu) {
		this.roaAcu = roaAcu;
	}
	@Column(name="TIPO_ARCHIVO")
	public String getTipoarchivo() {
		return tipoarchivo;
	}
	public void setTipoarchivo(String tipoarchivo) {
		this.tipoarchivo = tipoarchivo;
	}
	
	

}
