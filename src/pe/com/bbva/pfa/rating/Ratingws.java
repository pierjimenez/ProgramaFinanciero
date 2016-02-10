package pe.com.bbva.pfa.rating;

import java.io.Serializable;


public class Ratingws implements Serializable{
		
	  private String fechaPeriodoCalculado;
	  private String nombreEmpresa;
	  private String codigoCentralCliente;
	  private String mesEjercicio;
	  private String inflacion;
	  private String calificacionRating;
	  private String codigoCuenta;
	  private String descripcionCuenta;
	  private String monto;
	  private String status;
	  private String tipoEstadoFinanciero;
	  private String factorCualitativo;
	  private String factorCuantitativo;
	  private String factorBureau;
	  private String puntuacionRating;
	  
	public String getFechaPeriodoCalculado() {
		return fechaPeriodoCalculado;
	}
	public void setFechaPeriodoCalculado(String fechaPeriodoCalculado) {
		this.fechaPeriodoCalculado = fechaPeriodoCalculado;
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
	public String getMesEjercicio() {
		return mesEjercicio;
	}
	public void setMesEjercicio(String mesEjercicio) {
		this.mesEjercicio = mesEjercicio;
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
	public String getTipoEstadoFinanciero() {
		return tipoEstadoFinanciero;
	}
	public void setTipoEstadoFinanciero(String tipoEstadoFinanciero) {
		this.tipoEstadoFinanciero = tipoEstadoFinanciero;
	}
	public String getFactorCualitativo() {
		return factorCualitativo;
	}
	public void setFactorCualitativo(String factorCualitativo) {
		this.factorCualitativo = factorCualitativo;
	}
	public String getFactorCuantitativo() {
		return factorCuantitativo;
	}
	public void setFactorCuantitativo(String factorCuantitativo) {
		this.factorCuantitativo = factorCuantitativo;
	}
	public String getFactorBureau() {
		return factorBureau;
	}
	public void setFactorBureau(String factorBureau) {
		this.factorBureau = factorBureau;
	}
	public String getPuntuacionRating() {
		return puntuacionRating;
	}
	public void setPuntuacionRating(String puntuacionRating) {
		this.puntuacionRating = puntuacionRating;
	}
	  
	 

}
