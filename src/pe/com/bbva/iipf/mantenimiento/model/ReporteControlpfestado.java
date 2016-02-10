package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Entity;

@Entity
public class ReporteControlpfestado {
	private String idprograma;
	private String numeroSolicitud;
	private String estado;
	private String fechaCreacion;
	private String codusuarioCreacion;
	private String fechaModificacion;
	private String codusuarioModificacion;
	public String getIdprograma() {
		return idprograma;
	}
	public void setIdprograma(String idprograma) {
		this.idprograma = idprograma;
	}
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getCodusuarioCreacion() {
		return codusuarioCreacion;
	}
	public void setCodusuarioCreacion(String codusuarioCreacion) {
		this.codusuarioCreacion = codusuarioCreacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getCodusuarioModificacion() {
		return codusuarioModificacion;
	}
	public void setCodusuarioModificacion(String codusuarioModificacion) {
		this.codusuarioModificacion = codusuarioModificacion;
	}
	
	
	
	
}
