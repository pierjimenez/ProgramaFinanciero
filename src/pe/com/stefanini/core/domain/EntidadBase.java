package pe.com.stefanini.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class EntidadBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6342089942020889560L;
	
	private String codUsuarioCreacion;
	private String codUsuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String estado;
	private transient String fechaCreacionFormato;
	private transient String fechaModificacionFormato;
	
	@Transient
	public String getFechaCreacionFormato() {
		return fechaCreacionFormato;
	}
	
	@Transient
	public void setFechaCreacionFormato(String fechaCreacionFormato) {
		this.fechaCreacionFormato = fechaCreacionFormato;
	}
	@Transient
	public String getFechaModificacionFormato() {
		return fechaModificacionFormato;
	}
	
	@Transient
	public void setFechaModificacionFormato(String fechaModificacionFormato) {
		this.fechaModificacionFormato = fechaModificacionFormato;
	}
	@Column(name="ESTADO",length=1)
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Column(name="COD_USUARIO_CREACION",length=20,updatable=false)
	public String getCodUsuarioCreacion() {
		return codUsuarioCreacion;
	}
	public void setCodUsuarioCreacion(String codUsuarioCreacion) {
		this.codUsuarioCreacion = codUsuarioCreacion;
	}
	
	@Column(name="COD_USUARIO_MODIFICACION",length=20)
	public String getCodUsuarioModificacion() {
		return codUsuarioModificacion;
	}
	public void setCodUsuarioModificacion(String codUsuarioModificacion) {
		this.codUsuarioModificacion = codUsuarioModificacion;
	}
	
	@Column(name="FECHA_CREACION",updatable=false)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@Column(name="FECHA_MODIFICACION")
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
}
