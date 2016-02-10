package pe.com.bbva.iipf.seguridad.bo;


import java.util.List;

import pe.com.bbva.iipf.seguridad.model.entity.OpcionPerfil;
import pe.com.stefanini.core.exceptions.BOException;

public interface OpcionPerfilBO 
{

	public List<OpcionPerfil> listaPorPerfilTotal(Long idPerfil) throws BOException;
	public void actualizarPermisos(List<OpcionPerfil> permisos) throws BOException;
	
}
