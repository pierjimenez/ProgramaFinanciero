package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface SintesisEconomicoBlobBO {	
	
	public void save(SintesisEconomicoBlob sintesisEconomicaBlob) throws BOException;
	public SintesisEconomicoBlob findSintEcoBlobByPrograma(Programa programa,String codigoEmpresa)throws BOException;
	public List<SintesisEconomicoBlob> listaSintEcoBlobByPrograma(Programa oprograma) throws BOException;
	public void savecopia(SintesisEconomicoBlob t) throws BOException;
	public void setCampoBlob(String campoBlog) ;
	public void setValorBlob(String valorBlob) ;
	public void setCodEmpresa(String codEmpresa) ;
	public void setPrograma(Programa programa);
	public void setSysCodificacion (String sysCodificacion);
	public void delete(SintesisEconomicoBlob t) throws BOException;	
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	
	public void setPatronesEditor(String patronesEditor);

}
