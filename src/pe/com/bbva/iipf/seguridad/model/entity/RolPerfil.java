package pe.com.bbva.iipf.seguridad.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(schema="PROFIN", name="TIIPF_PERFIL_ROL")
@SequenceGenerator(name = "SEQ_ROL_PERFIL", sequenceName = "PROFIN.SEQ_ROL_PERFIL", allocationSize = 1, initialValue = 20000)
public class RolPerfil extends EntidadBase {
	
	private Long id;
	private Tabla rol;	
	private Tabla perfil;
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_ROL_PERFIL")
	@Column(name="ID_PERFIL_ROL")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="ID_ROL")
	public Tabla getRol() {
		return rol;
	}
	public void setRol(Tabla rol) {
		this.rol = rol;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="ID_PERFIL")
	public Tabla getPerfil() {
		return perfil;
	}
	public void setPerfil(Tabla perfil) {
		this.perfil = perfil;
	}
	

	
	

}
