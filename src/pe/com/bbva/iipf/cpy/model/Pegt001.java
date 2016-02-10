package pe.com.bbva.iipf.cpy.model;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="PROFIN", name="IIPF_PEGT001")
public class Pegt001 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String numclien; //NUMCLIEN	VARCHAR2(8 BYTE)
	private String codident; //CODIDENT	VARCHAR2(1 BYTE)
	private String fenacimi; //FENACIMI	VARCHAR2(10 BYTE)
	private String feantig; //FEANTIG	VARCHAR2(10 BYTE)
	private String nombre;//NOMBRE	VARCHAR2(20 BYTE)
	private String tipopert;//TIPOPERT	VARCHAR2(1 BYTE)
	private String fealtcli; //FEALTCLI	VARCHAR2(10 BYTE)
	
	@Id
	@Column(name="NUMCLIEN",length=8)
	public String getNumclien() {
		return numclien;
	}
	
	public void setNumclien(String numclien) {
		this.numclien = numclien;
	}
	
	@Column(name="CODIDENT",length=1)
	public String getCodident() {
		return codident;
	}
	
	public void setCodident(String codident) {
		this.codident = codident;
	}
	
	@Column(name="FENACIMI",length=10)
	public String getFenacimi() {
		return fenacimi;
	}
	
	
	public void setFenacimi(String fenacimi) {
		this.fenacimi = fenacimi;
	}
	
	@Column(name="FEANTIG",length=10)
	public String getFeantig() {
		return feantig;
	}
	
	public void setFeantig(String feantig) {
		this.feantig = feantig;
	}
	
	@Column(name="NOMBRE",length=20)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="TIPOPERT",length=1)
	public String getTipopert() {
		return tipopert;
	}
	
	public void setTipopert(String tipopert) {
		this.tipopert = tipopert;
	}
	
	@Column(name="FEALTCLI",length=10)
	public String getFealtcli() {
		return fealtcli;
	}
	
	public void setFealtcli(String fealtcli) {
		this.fealtcli = fealtcli;
	}
}
