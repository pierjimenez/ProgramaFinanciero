package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;

@Entity
public class TcuentaRating {

	
	private String fechaPeriodoCalc ;//FECH_PERIODO_CALC
	private String codCuenta ; //COD_CUENTA
	private String descripcionCuenta ; //DESC_CUENTA
	private String monto ; //MONTO
	
	
	public String getFechaPeriodoCalc() {
		return fechaPeriodoCalc;
	}
	public void setFechaPeriodoCalc(String fechaPeriodoCalc) {
		this.fechaPeriodoCalc = fechaPeriodoCalc;
	}
	public String getCodCuenta() {
		return codCuenta;
	}
	public void setCodCuenta(String codCuenta) {
		this.codCuenta = codCuenta;
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
	
	
	
	
}
