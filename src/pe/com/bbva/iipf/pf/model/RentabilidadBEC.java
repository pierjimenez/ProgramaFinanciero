package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="TIIPF_RENTABILIDAD_BEC")
@SequenceGenerator(name = "SEQ_RENTABILIDAD_BEC", sequenceName = "PROFIN.SEQ_RENTABILIDAD_BEC", allocationSize = 1, initialValue = 20000)
public class RentabilidadBEC extends EntidadBase {
	
private Long id;//	ID_RENT_BEC	NUMBER	N			
private String tipoRentabilidad;//	TIPO_RENTABILIDAD	VARCHAR2(30)	Y			
private String descripcionMonto;//	DESCRIPCION_MONTO	VARCHAR2(60)	Y			
private String monto;//	MONTO	VARCHAR2(20)	Y			
private Programa programa;//	IIPF_PROGRAMA_ID	INTEGER	N
private String montoAnual;//MONTO_ANUAL VARCHAR2(20)
private String fechaProcesoAnual;//FECHA_PROCESO VARCHAR2(20)
private String fechaProcesoMensual;//FECHA_PROCESO VARCHAR2(20)

private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)


@Id
@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_RENTABILIDAD_BEC")
@Column(name="ID_RENT_BEC")
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

@Column(name="TIPO_RENTABILIDAD")
public String getTipoRentabilidad() {
	return tipoRentabilidad;
}
public void setTipoRentabilidad(String tipoRentabilidad) {
	this.tipoRentabilidad = tipoRentabilidad;
}
@Column(name="DESCRIPCION_MONTO")
public String getDescripcionMonto() {
	return descripcionMonto;
}
public void setDescripcionMonto(String descripcionMonto) {
	this.descripcionMonto = descripcionMonto;
}

@Column(name="MONTO")
public String getMonto() {
	return monto;
}
public void setMonto(String monto) {
	this.monto = monto;
}

@ManyToOne
@JoinColumn(name="IIPF_PROGRAMA_ID")
public Programa getPrograma() {
	return programa;
}
public void setPrograma(Programa programa) {
	this.programa = programa;
}
@Column(name="MONTO_ANUAL")
public String getMontoAnual() {
	return montoAnual;
}
public void setMontoAnual(String montoAnual) {
	this.montoAnual = montoAnual;
}
@Column(name="FECHA_PROCESO_ANUAL")
public String getFechaProcesoAnual() {
	return fechaProcesoAnual;
}
public void setFechaProcesoAnual(String fechaProcesoAnual) {
	this.fechaProcesoAnual = fechaProcesoAnual;
}
@Column(name="FECHA_PROCESO_MENSUAL")
public String getFechaProcesoMensual() {
	return fechaProcesoMensual;
}
public void setFechaProcesoMensual(String fechaProcesoMensual) {
	this.fechaProcesoMensual = fechaProcesoMensual;
}

@Column(name="COD_GRUPO_EMPRESA")
public String getCodEmpresaGrupo() {
	return codEmpresaGrupo;
}
public void setCodEmpresaGrupo(String codEmpresaGrupo) {
	this.codEmpresaGrupo = codEmpresaGrupo;
}

}
