package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;

@Entity
public class SaldoCliente {

	private String codcliente;				
	private String contrato;
	private String saldodeudo;
	private String saldodeudolocal;
	private String codMoneda;
	private String fechaform;
	private String fechaVencimiento;
	private String tipocambio;
	private String fechaTipocambio;
	private String indfisca;
	
	
	public String getCodcliente() {
		return codcliente;
	}
	public void setCodcliente(String codcliente) {
		this.codcliente = codcliente;
	}
	public String getContrato() {
		return contrato;
	}
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	
	public String getSaldodeudo() {
		return saldodeudo;
	}
	public void setSaldodeudo(String saldodeudo) {
		this.saldodeudo = saldodeudo;
	}
	public String getSaldodeudolocal() {
		return saldodeudolocal;
	}
	public void setSaldodeudolocal(String saldodeudolocal) {
		this.saldodeudolocal = saldodeudolocal;
	}
	public String getCodMoneda() {
		return codMoneda;
	}
	public void setCodMoneda(String codMoneda) {
		this.codMoneda = codMoneda;
	}
	public String getFechaform() {
		return fechaform;
	}
	public void setFechaform(String fechaform) {
		this.fechaform = fechaform;
	}
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getTipocambio() {
		return tipocambio;
	}
	public void setTipocambio(String tipocambio) {
		this.tipocambio = tipocambio;
	}
	public String getFechaTipocambio() {
		return fechaTipocambio;
	}
	public void setFechaTipocambio(String fechaTipocambio) {
		this.fechaTipocambio = fechaTipocambio;
	}
	public String getIndfisca() {
		return indfisca;
	}
	public void setIndfisca(String indfisca) {
		this.indfisca = indfisca;
	}
	
	
	
}
