package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface PoliticasRiesgoBO {
	
	public List<LimiteFormalizado> findLimiteFormalizadoByIdprograma(Long Idprograma) throws BOException ;
	
	
	public void updatePoliticasRiesgo() throws BOException;	
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPrograma(Programa programa);
	public void setListLimiteFormalizado(List<LimiteFormalizado> listLimiteFormalizado);
	public void saveCopiaLimiteFormalizado(List<LimiteFormalizado> olistLimiteFormalizado) throws BOException;

	
}
