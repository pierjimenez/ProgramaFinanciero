package pe.com.grupobbva.rating.itcu;

import java.io.Serializable;

public class OutputITUC implements Serializable{
	
	//
	private String tipoRegistro;
	private String codigoModelo;
	private String codigoAnalisis;
	
	//Tipo de registro 01
	private String codigoCliente;
	private String anio;
	private String mes;
	private String tipoEmpresa;
	//codigo modelo
	//codigo analisis
	private String inflacionActual;
	private String inflacionAcumulada;
	private String calificacionRating;
	
	//Tipo de registro 02
	private String codigoEstudio;
	private String fechaAnalisis;
	private String puntuacionCuantitativa;
	private String puntuacionCualitativa;
	private String puntuacionCuliTotal;
	private String buro;
	private String periodo;
	private String estado;
	
	//Tipo de registro 03
	//codigo modelo
	//codigo analisis
	private String codigoEstado;
	private String facturacion;
	private String codigoCuenta;
	private String importe;
	private String indicadorAjuste;
	private String descripcionCuenta;
	
	
	
	public OutputITUC(){
		
		
		this.tipoRegistro=" ";
		this.codigoModelo=" ";
		this.codigoAnalisis=" ";
		
		//Tipo de registro 01
		this.codigoCliente=" ";
		this.anio=" ";
		this.mes=" ";
		this.tipoEmpresa=" ";

		this.inflacionActual=" ";
		this.inflacionAcumulada=" ";
		this.calificacionRating=" ";
		
		//Tipo de registro 02
		this.codigoEstudio=" ";
		this.fechaAnalisis=" ";
		this.puntuacionCuantitativa="0";
		this.puntuacionCualitativa="0";
		this.puntuacionCuliTotal="0";
		this.buro=" ";
		this.periodo=" ";
		this.estado=" ";
		
		this.codigoEstado=" ";
		this.facturacion="0";
		this.codigoCuenta=" ";
		this.importe="0";
		this.indicadorAjuste=" ";
		this.descripcionCuenta=" ";
				
	}
	
	


	public String getCalificacionRating() {
		return calificacionRating;
	}




	public void setCalificacionRating(String calificacionRating) {
		this.calificacionRating = calificacionRating;
	}




	public String getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getCodigoModelo() {
		return codigoModelo;
	}
	public void setCodigoModelo(String codigoModelo) {
		this.codigoModelo = codigoModelo;
	}
	public String getCodigoAnalisis() {
		return codigoAnalisis;
	}
	public void setCodigoAnalisis(String codigoAnalisis) {
		this.codigoAnalisis = codigoAnalisis;
	}
	public String getInflacionActual() {
		return inflacionActual;
	}
	public void setInflacionActual(String inflacionActual) {
		this.inflacionActual = inflacionActual;
	}
	public String getInflacionAcumulada() {
		return inflacionAcumulada;
	}
	public void setInflacionAcumulada(String inflacionAcumulada) {
		this.inflacionAcumulada = inflacionAcumulada;
	}
	public String getCodigoEstudio() {
		return codigoEstudio;
	}
	public void setCodigoEstudio(String codigoEstudio) {
		this.codigoEstudio = codigoEstudio;
	}
	public String getFechaAnalisis() {
		return fechaAnalisis;
	}
	public void setFechaAnalisis(String fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
	}
	public String getPuntuacionCuantitativa() {
		return puntuacionCuantitativa;
	}
	public void setPuntuacionCuantitativa(String puntuacionCuantitativa) {
		this.puntuacionCuantitativa = puntuacionCuantitativa;
	}
	public String getPuntuacionCualitativa() {
		return puntuacionCualitativa;
	}
	public void setPuntuacionCualitativa(String puntuacionCualitativa) {
		this.puntuacionCualitativa = puntuacionCualitativa;
	}
	public String getPuntuacionCuliTotal() {
		return puntuacionCuliTotal;
	}
	public void setPuntuacionCuliTotal(String puntuacionCuliTotal) {
		this.puntuacionCuliTotal = puntuacionCuliTotal;
	}
	public String getBuro() {
		return buro;
	}
	public void setBuro(String buro) {
		this.buro = buro;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getFacturacion() {
		return facturacion;
	}
	public void setFacturacion(String facturacion) {
		this.facturacion = facturacion;
	}
	public String getCodigoCuenta() {
		return codigoCuenta;
	}
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getIndicadorAjuste() {
		return indicadorAjuste;
	}
	public void setIndicadorAjuste(String indicadorAjuste) {
		this.indicadorAjuste = indicadorAjuste;
	}
	public String getDescripcionCuenta() {
		return descripcionCuenta;
	}
	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}
	
    

}
