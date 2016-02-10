package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface PropuestaRiesgoBO {
	
	public List<EstructuraLimite> findEstructuraLimiteByIdprograma(Long Idprograma) throws BOException ;

	public void updatePropuestaRiesgo() throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPrograma(Programa programa) ;
	public void setComentPropuestaRiesgo(String comentPropuestaRiesgo) ;
	public void setComentEstructuraLimite(String comentEstructuraLimite); 
	public void setConsidPropuestaRiesgo(String considPropuestaRiesgo);
	public void setListEstructuraLimite(List<EstructuraLimite> listEstructuraLimite);
	
	public void saveEstructuraLimite() throws BOException;
	public void saveCopiaEstructuraLimite(List<EstructuraLimite> olistEstructuraLimite) throws BOException;
	
	public void delete(EstructuraLimite t) throws BOException;	
}
