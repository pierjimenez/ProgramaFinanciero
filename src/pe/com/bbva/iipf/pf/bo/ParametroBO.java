package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.ArchivoMnt;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;

public interface ParametroBO 
{
	public Parametro findByIdParametro(Long id)throws BOException;
	public List<Parametro> findParametros()throws BOException;
	public Parametro findByNombreParametro(String codigo) throws BOException;
	
	public void insertar(Parametro objParametro) throws BOException;
	public void modificar(Parametro objParametro) throws BOException;
	public List<Parametro> listar(Parametro objParametro) throws BOException;
	public Parametro obtienePorCodigo(String codigo) throws BOException;
	public Parametro obtienePorCodigoSinActual(String codigo, Long id) throws BOException;
	public Parametro obtienePorId(Long id) throws BOException;
	
	public Long  ValidaCargaEnProceso(String strCodigo) throws BOException ;
	public void update(Parametro parametro) throws BOException ;
	public Integer ValidaCargaAutomatica(String fechaproceso, int idtipoArchivo) throws BOException;
	public List<List>  findResultScript(String strScript,String strTipoScript) throws BOException;
	
	public List<ArchivoMnt> findArchivosMNT(ArchivoMnt archivoMnt) throws BOException, DAOException;
	
}
