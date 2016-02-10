package pe.com.bbva.iipf.cpy.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Tabla que contiene la data del archivo PFRATING descargado de 
 * xcom
 * @author EPOMAYAY
 *
 */

public class PFRating {

	private Date fechaPeridoCalculado ; //FECH_PERIODO_CALC	DATE
	private String nombreEmpresa; //NOMB_EMPRESA	VARCHAR2(60 BYTE)
	private String codigoCentralCliente; //COD_CENTRAL_CLI	VARCHAR2(8 BYTE)
	private String mesesEjercicios; //MESES_EJERCICIOS	VARCHAR2(2 BYTE)
	private String inflacion; //INFLACION	VARCHAR2(11 BYTE)
	private String calificacionRating;//CALIF_RATING	VARCHAR2(6 BYTE)
	private String codigoCuenta; //COD_CUENTA	VARCHAR2(20 BYTE)
	private String descripcionCuenta;//DESC_CUENTA	VARCHAR2(60 BYTE)
	private String monto; //MONTO	VARCHAR2(17 BYTE)
	private String status; //STATUS	VARCHAR2(20 BYTE)
	private String tipoEstadoFinan; //TIP_ESTADO_FINAN	VARCHAR2(1 BYTE)
	private String factoresCualitativos;//FACT_CUALITATIVOS	VARCHAR2(5 BYTE)
	private String factoresCuantitativos;//FACT_CUANTITATIVOS	VARCHAR2(5 BYTE)
	private String factoresBureau;//FACT_BUREAU	VARCHAR2(5 BYTE)
	private String puntajeRating;//PUNT_RATING	VARCHAR2(5 BYTE)
	
	public Date getFechaPeridoCalculado() {
		return fechaPeridoCalculado;
	}
	public void setFechaPeridoCalculado(Date fechaPeridoCalculado) {
		this.fechaPeridoCalculado = fechaPeridoCalculado;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public String getCodigoCentralCliente() {
		return codigoCentralCliente;
	}
	public void setCodigoCentralCliente(String codigoCentralCliente) {
		this.codigoCentralCliente = codigoCentralCliente;
	}
	public String getMesesEjercicios() {
		return mesesEjercicios;
	}
	public void setMesesEjercicios(String mesesEjercicios) {
		this.mesesEjercicios = mesesEjercicios;
	}
	public String getInflacion() {
		return inflacion;
	}
	public void setInflacion(String inflacion) {
		this.inflacion = inflacion;
	}
	public String getCalificacionRating() {
		return calificacionRating;
	}
	public void setCalificacionRating(String calificacionRating) {
		this.calificacionRating = calificacionRating;
	}
	public String getCodigoCuenta() {
		return codigoCuenta;
	}
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	public String getDescripcionCuenta() {
		return descripcionCuenta;
	}
	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTipoEstadoFinan() {
		return tipoEstadoFinan;
	}
	public void setTipoEstadoFinan(String tipoEstadoFinan) {
		this.tipoEstadoFinan = tipoEstadoFinan;
	}
	public String getFactoresCualitativos() {
		return factoresCualitativos;
	}
	public void setFactoresCualitativos(String factoresCualitativos) {
		this.factoresCualitativos = factoresCualitativos;
	}
	public String getFactoresCuantitativos() {
		return factoresCuantitativos;
	}
	public void setFactoresCuantitativos(String factoresCuantitativos) {
		this.factoresCuantitativos = factoresCuantitativos;
	}
	public String getFactoresBureau() {
		return factoresBureau;
	}
	public void setFactoresBureau(String factoresBureau) {
		this.factoresBureau = factoresBureau;
	}
	public String getPuntajeRating() {
		return puntajeRating;
	}
	public void setPuntajeRating(String puntajeRating) {
		this.puntajeRating = puntajeRating;
	}
	
	
}
