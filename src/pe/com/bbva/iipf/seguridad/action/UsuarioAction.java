package pe.com.bbva.iipf.seguridad.action;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.seguridad.bo.UsuarioBO;
import pe.com.bbva.iipf.seguridad.model.entity.Usuario;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;


/**
 * 
 * @author epomayay
 *
 */
@Service("usuarioAction")
public class UsuarioAction extends GenericAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Usuario usuario ;
	
	@Resource
	UsuarioBO usuarioBO;
	

	public String registroUsuario(){
		return "usuario";
	}

	public String save(){
		//usuario.setCodigoUsuLdap("P00512");
		try {
			usuarioBO.save(usuario);
			addActionMessage("Usuario Registrado correctamente...");
		} catch (BOException e) {
			e.printStackTrace();
		}
		return "usuario";
	}

	public UsuarioBO getUsuarioBO() {
		return usuarioBO;
	}

	public void setUsuarioBO(UsuarioBO usuarioBO) {
		this.usuarioBO = usuarioBO;
	}
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
