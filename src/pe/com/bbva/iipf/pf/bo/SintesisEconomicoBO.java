package pe.com.bbva.iipf.pf.bo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

/**
 * 
 * @author EPOMAYAY
 *
 */
public interface SintesisEconomicoBO {

	public void setFileExcel(File fileExcel);
	public void save()throws BOException,IOException;
	public void savecopiaSintesisEconomica(SintesisEconomica osintesisEconomica,Long IdSintesisini) throws BOException,IOException;
	public void setNombreEmpresaGrupo(String nombreEmpresaGrupo);
	public void setPrograma(Programa programa) ;
	public List<SintesisEconomica> listarSintesisEconomico()throws BOException;
	public List<SintesisEconomica> listarSintesisEconomico(Programa oprograma) throws BOException;
	public List<SintesisEconomica> findSintesisEconomicoSQL(String idPrograma) throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession) ;
	public String llenarPlantillaExcel(String dirArchivo, 
								       String nombreArchivo,
								       String dirTemporal,
								       String codigoCentralEmpresa,
								       String nombreEmpresa)throws BOException;
	
	public void savelistaSintesisEconomico(Programa oprograma,List<SintesisEconomica> olistaSintesisEconomica) throws BOException;

	/**
	 * Elimina de forma permanente una entidad
	 * @param t
	 * @throws BOException
	 */
	public void delete(SintesisEconomica t) throws BOException;	
	public List<SintesisEconomica> listarSintesisEconomico(Programa oprograma,String nombreEmpresa) throws BOException ;
	
}
