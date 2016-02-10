package pe.com.bbva.iipf.seguridad.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table( schema="PROFIN", name="TIIPF_USUARIO_PERFIL")
@NamedQueries({
@NamedQuery(name="obtieneUsuarioPerfilPorId", 
		    query = " select o from UsuarioPerfil o where o.id = ?"),
@NamedQuery(name="obtieneUsuarioPerfilPorCodigo", 
		    query = " select o from UsuarioPerfil o where o.usuarioRegistro = ?"),
@NamedQuery(name="obtieneUsuarioPerfilPorCodigoSinActual", 
			query = " select o from UsuarioPerfil o where o.usuarioRegistro = ? and o.id <> ? ")
})
@SequenceGenerator(name = "SEQ_USUARIO_PERFIL", sequenceName = "PROFIN.SEQ_USUARIO_PERFIL", allocationSize = 1, initialValue = 20000)
public class UsuarioPerfil extends EntidadBase 
{

	private Long id;
	private String usuarioRegistro;
	private Tabla perfil;
	private Tabla nivelAcceso;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USUARIO_PERFIL")
	@Column(name="ID_USUARIO_PERFIL")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="USUARIO_REGISTRO")
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="ID_PERFIL")
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="ID_PERFIL")
	public Tabla getPerfil() {
		return perfil;
	}
	public void setPerfil(Tabla perfil) {
		this.perfil = perfil;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_NIVEL_ACCESO")
	public Tabla getNivelAcceso() {
		return nivelAcceso;
	}
	public void setNivelAcceso(Tabla nivelAcceso) {
		this.nivelAcceso = nivelAcceso;
	}
	
}
