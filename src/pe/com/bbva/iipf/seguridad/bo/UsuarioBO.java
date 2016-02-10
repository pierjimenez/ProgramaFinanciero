package pe.com.bbva.iipf.seguridad.bo;

import pe.com.bbva.iipf.seguridad.model.entity.Usuario;
import pe.com.stefanini.core.exceptions.BOException;

public interface UsuarioBO {

	public void save(Usuario usuario) throws BOException;
}
