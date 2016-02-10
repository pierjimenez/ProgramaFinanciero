package pe.com.bbva.iipf.seguridad.bo.impl;

import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.seguridad.bo.UsuarioBO;
import pe.com.bbva.iipf.seguridad.model.entity.Usuario;
import pe.com.stefanini.core.bo.GenericBOImpl;

@Service("usuarioBO" )
public class UsuarioBOImpl extends GenericBOImpl<Usuario> implements UsuarioBO {
	@Override
	public boolean validate(Usuario usuario){
		
		return true;
	}
}
