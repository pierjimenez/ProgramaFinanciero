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
@Table( schema="PROFIN", name="TIIPF_PFRCD")
@SequenceGenerator(name = "SEQ_RCD", sequenceName = "PROFIN.SEQ_RCD", allocationSize = 1, initialValue = 20000)
public class rcd {

private Long id;//	ID_PFRCD	INTEGER	Y			ID	
private String cliente;//	CLIENTE	VARCHAR2(8)	Y			CODIGO O NUMERO DE CLIENTE INTERNO
private String ruc;//	RUC	VARCHAR2(12)	Y			NUMERO DE RUC
private String calificacionAlineada;//	CALIFICACION_ALINEADA	VARCHAR2(1)	Y			0: NORMAL 1: CON PROBLEMAS POTENCIALES  2:DEFICIENTE 3:DUDOSO 4:PERDIDA
private Date fechaProceso;//	FECHA_PROCESO	DATE	Y			Fech a Proceso

@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RCD")
@Column(name="ID_PFRCD")
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

@Column(name="CLIENTE")
public String getCliente() {
	return cliente;
}
public void setCliente(String cliente) {
	this.cliente = cliente;
}

@Column(name="RUC", length=12)
public String getRuc() {
	return ruc;
}
public void setRuc(String ruc) {
	this.ruc = ruc;
}

@Column(name="CALIFICACION_ALINEADA")
public String getCalificacionAlineada() {
	return calificacionAlineada;
}
public void setCalificacionAlineada(String calificacionAlineada) {
	this.calificacionAlineada = calificacionAlineada;
}

@Column(name="FECHA_PROCESO")
public Date getFechaProceso() {
	return fechaProceso;
}
public void setFechaProceso(Date fechaProceso) {
	this.fechaProceso = fechaProceso;
}

	
}
