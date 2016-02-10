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
@Table( schema="PROFIN", name="TIIPF_PFRCCMES")
@SequenceGenerator(name = "SEQ_RCCMES", sequenceName = "PROFIN.SEQ_RCCMES", allocationSize = 1, initialValue = 20000)
public class rccmes  {

private Long id; //ID_PFRCCMES	INTEGER	Y			ID
private String tipoDocumento;//	TIPO_DOCUMENTO	VARCHAR2(2)	Y			R:Ruc
private String documento;//	DOCUMENTO	VARCHAR2(12)	Y			Numero de RUC
private String cliente;//	CLIENTE	VARCHAR2(8)	Y			Numero de Cliente Interno
private String aniomes; //ANIOMES	VARCHAR2(6)	Y			Mes y anio :AAAAMM
private String nroEntidadBancaria;//	NRO_ENTIDAD_BANCARIA	VARCHAR2(3)	Y			Codigo de Banco
private String nombreBanco;//	NOMBRE_BANCO	VARCHAR2(30)	Y			EJM: BBVA Banco Continental
private String porcentajeNormal;//	PORC_NOR	NUMBER	Y			Porcentaje Informado en Categoría Normal
private String porcenajePPotencial;//	PORC_CPP	NUMBER	Y			Porcentaje Informado en Categoría Con Problemas Potenciales
private String porcentajeDeficiente;//	PORC_DEF	NUMBER	Y			Porcentaje Informado en Categoría Deficiente
private String porcentajeDudoso;//	PORC_DUD	NUMBER	Y			Porcentaje Informado en Categoría Dudoso
private String porcentajePerdida;//	PORC_PER	NUMBER	Y			Porcentaje Informado en Categoría Perdida
private String deudaIndirecta;//	DEUDA_INDIRECTA	NUMBER	Y			Monto Total en Deuda Indirecta por Banco Informado
private String deudaDirecta;//	DEUDA_DIRECTA	NUMBER	Y			Monto Total en Deuda Directa por Banco Informado
private String deudaTotal;//	DEUDA_TOTAL	NUMBER	Y			Monto Total en Deuda por Banco Informado
private Date fechaProceso;//	FECHA_PROCESO	DATE	Y			Fecha Proceso



@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RCCMES")
@Column(name="ID_PFRCCMES")
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

@Column(name="TIPO_DOCUMENTO", length=2)
public String getTipoDocumento() {
	return tipoDocumento;
}
public void setTipoDocumento(String tipoDocumento) {
	this.tipoDocumento = tipoDocumento;
}

@Column(name="DOCUMENTO", length=12)
public String getDocumento() {
	return documento;
}
public void setDocumento(String documento) {
	this.documento = documento;
}


@Column(name="CLIENTE", length=8)
public String getCliente() {
	return cliente;
}
public void setCliente(String cliente) {
	this.cliente = cliente;
}


@Column(name="ANIOMES", length=6)
public String getAniomes() {
	return aniomes;
}
public void setAniomes(String aniomes) {
	this.aniomes = aniomes;
}
@Column(name="NRO_ENTIDAD_BANCARIA", length=3)
public String getNroEntidadBancaria() {
	return nroEntidadBancaria;
}
public void setNroEntidadBancaria(String nroEntidadBancaria) {
	this.nroEntidadBancaria = nroEntidadBancaria;
}


@Column(name="NOMBRE_BANCO", length=30)
public String getNombreBanco() {
	return nombreBanco;
}
public void setNombreBanco(String nombreBanco) {
	this.nombreBanco = nombreBanco;
}


@Column(name="PORC_NOR")
public String getPorcentajeNormal() {
	return porcentajeNormal;
}
public void setPorcentajeNormal(String porcentajeNormal) {
	this.porcentajeNormal = porcentajeNormal;
}


@Column(name="PORC_CPP")
public String getPorcenajePPotencial() {
	return porcenajePPotencial;
}
public void setPorcenajePPotencial(String porcenajePPotencial) {
	this.porcenajePPotencial = porcenajePPotencial;
}


@Column(name="PORC_DEF")
public String getPorcentajeDeficiente() {
	return porcentajeDeficiente;
}
public void setPorcentajeDeficiente(String porcentajeDeficiente) {
	this.porcentajeDeficiente = porcentajeDeficiente;
}


@Column(name="PORC_DUD")
public String getPorcentajeDudoso() {
	return porcentajeDudoso;
}
public void setPorcentajeDudoso(String porcentajeDudoso) {
	this.porcentajeDudoso = porcentajeDudoso;
}


@Column(name="PORC_PER")
public String getPorcentajePerdida() {
	return porcentajePerdida;
}
public void setPorcentajePerdida(String porcentajePerdida) {
	this.porcentajePerdida = porcentajePerdida;
}


@Column(name="DEUDA_INDIRECTA")
public String getDeudaIndirecta() {
	return deudaIndirecta;
}
public void setDeudaIndirecta(String deudaIndirecta) {
	this.deudaIndirecta = deudaIndirecta;
}

@Column(name="DEUDA_DIRECTA")
public String getDeudaDirecta() {
	return deudaDirecta;
}
public void setDeudaDirecta(String deudaDirecta) {
	this.deudaDirecta = deudaDirecta;
}

@Column(name="DEUDA_TOTAL")
public String getDeudaTotal() {
	return deudaTotal;
}
public void setDeudaTotal(String deudaTotal) {
	this.deudaTotal = deudaTotal;
}


@Column(name="FECHA_PROCESO")
public Date getFechaProceso() {
	return fechaProceso;
}
public void setFechaProceso(Date fechaProceso) {
	this.fechaProceso = fechaProceso;
}





}
