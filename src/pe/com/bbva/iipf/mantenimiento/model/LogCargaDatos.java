package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_LOG_DATALOAD")
@NamedQueries({
	@NamedQuery(name="listaLogCargaDatos", query=" select o from LogCargaDatos o where o.id> ?")
})
@SequenceGenerator(name = "SEQ_LOG_DATALOAD", sequenceName = "PROFIN.SEQ_LOG_DATALOAD", allocationSize = 1, initialValue = 1)
public class LogCargaDatos extends EntidadBase{

	private Long id;
	private Long idArchivo;
	private String nombreArchivo;
	private String descripcion;
	private String tipocarga;//TIPOCARGA
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_LOG_DATALOAD")
	@Column(name="ID_LOG")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="ID_ARCHIVO")
	public Long getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Long idArchivo) {
		this.idArchivo = idArchivo;
	}
	@Column(name="DESC_LOG")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Column(name="NOMB_ARCHIVO")
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	@Column(name="TIPOCARGA")
	public String getTipocarga() {
		return tipocarga;
	}

	public void setTipocarga(String tipocarga) {
		this.tipocarga = tipocarga;
	}
	
	

}
