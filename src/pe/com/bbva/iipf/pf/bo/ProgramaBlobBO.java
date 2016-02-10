package pe.com.bbva.iipf.pf.bo;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

/**
 * 
 * @author EPOMAYAY
 *
 */
public interface ProgramaBlobBO {

	public void save(ProgramaBlob programaBlob) throws BOException;
	public ProgramaBlob findBlobByPrograma(Programa programa)throws BOException;
	public void setCampoBlob(String campoBlog) ;
	public void setValorBlob(String valorBlob) ;
	public void setPrograma(Programa programa);
	public void setSysCodificacion (String sysCodificacion);
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPatronesEditor(String patronesEditor);
	public void save(ProgramaBlob t,Programa oprograma) throws BOException;
}
