package pe.com.bbva.iipf.seguridad.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_OPCION_PERFIL")
@SequenceGenerator(name = "SEQ_OPCION_PERFIL", sequenceName = "PROFIN.SEQ_OPCION_PERFIL", allocationSize = 1, initialValue = 20000)
public class OpcionPerfil extends EntidadBase 
{
	
	private Long id;
	private Opcion opcion;	
	private Tabla perfil;
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_OPCION_PERFIL")
	@Column(name="ID_OPCION_PERFIL")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_OPCION")
	public Opcion getOpcion() {
		return opcion;
	}
	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_PERFIL")
	public Tabla getPerfil() {
		return perfil;
	}
	public void setPerfil(Tabla perfil) {
		this.perfil = perfil;
	}
	
	
}
