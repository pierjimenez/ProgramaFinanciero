package pe.com.bbva.iipf.seguridad.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;


/**
 * bean para la creación de usuarios
 * @author epomayay
 *
 */
@Entity
@Table(schema="PROFIN",  name="TIIPF_USUARIO")
@SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "PROFIN.SEQ_USUARIO", allocationSize = 1, initialValue = 20000)
public class Usuario extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2589599445464172090L;
	
	private Long  id;//USP_CO_USU	NUMBER
	private String codigoUsuLdap;//USP_CO_USU_LDAP	VARCHAR2(10 BYTE)
	//private Perfil perfil;//USP_TT_CO_PERF	VARCHAR2(20 BYTE)
	//private Columna nivelAcceso;//USP_NIV_ACCES NUMBER
	private String nombreUsuario;
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_USUARIO")
	@Column(name="ID_USUARIO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CODIGO", length=20)
	public String getCodigoUsuLdap() {
		return codigoUsuLdap;
	}
	
	public void setCodigoUsuLdap(String codigoUsuLdap) {
		this.codigoUsuLdap = codigoUsuLdap;
	}
	

}
