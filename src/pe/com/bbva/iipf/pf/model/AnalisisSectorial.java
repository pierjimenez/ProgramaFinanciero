package pe.com.bbva.iipf.pf.model;

import java.util.Date;

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
@Table(schema="PROFIN",  name="TIIPF_ANALISIS_SECTORIAL")
@SequenceGenerator(name = "SEQ_ANALISIS_SECTORIAL", sequenceName = "PROFIN.SEQ_ANALISIS_SECTORIAL", allocationSize = 1, initialValue = 20000)
public class AnalisisSectorial extends EntidadBase {

/**
* 
*/
private static final long serialVersionUID = 1L;

private Long  id; //Id
private Date fechaCarga;//	FECHA	DATE	Y		
private String nombreArchivo;//	NOMBRE_ARCH	VARCHAR2(100)	Y			
private String usuario;//	USUARIO	VARCHAR2(60)	Y			
private String codigoArchivo;//	CODIGO_ARCHIVO	VARCHAR2(100)	Y			
private String extencionArchivo;//	EXTENCION_ARCH	VARCHAR2(4)	Y			
private Programa programa;//	IIPF_PROGRAMA_ID	INTEGER	N	


@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ANALISIS_SECTORIAL")
@Column(name="IIPF_ANALISISSECTORIAL_ID")
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

@Column(name="FECHA")
public Date getFechaCarga() {
	return fechaCarga;
}

public void setFechaCarga(Date fechaCarga) {
	this.fechaCarga = fechaCarga;
}

@Column(name="NOMBRE_ARCH")
public String getNombreArchivo() {
	return nombreArchivo;
}
public void setNombreArchivo(String nombreArchivo) {
	this.nombreArchivo = nombreArchivo;
}

@Column(name="USUARIO")
public String getUsuario() {
	return usuario;
}
public void setUsuario(String usuario) {
	this.usuario = usuario;
}

@Column(name="CODIGO_ARCHIVO")
public String getCodigoArchivo() {
	return codigoArchivo;
}
public void setCodigoArchivo(String codigoArchivo) {
	this.codigoArchivo = codigoArchivo;
}

@Column(name="EXTENCION_ARCH")
public String getExtencionArchivo() {
	return extencionArchivo;
}
public void setExtencionArchivo(String extencionArchivo) {
	this.extencionArchivo = extencionArchivo;
}

@ManyToOne
@JoinColumn(name="IIPF_PROGRAMA_ID")
public Programa getPrograma() {
	return programa;
}
public void setPrograma(Programa programa) {
	this.programa = programa;
}


}
