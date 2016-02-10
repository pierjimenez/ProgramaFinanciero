package pe.com.bbva.iipf.seguridad.bo;

import java.util.List;

import pe.com.bbva.iipf.seguridad.model.entity.RolPerfil;
import pe.com.stefanini.core.exceptions.BOException;

public interface RolPerfilBO {
	public List<RolPerfil> getListaRolPerfil(Long idPerfil)throws BOException;
	//public void actualizarRolPerfil(List<RolPerfil> listaRolPerfil)	throws BOException;
	public void actualizarRolPerfil(List<RolPerfil> listaRolPerfil,Long idPerfil) throws BOException;

}
