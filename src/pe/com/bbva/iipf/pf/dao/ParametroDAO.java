package pe.com.bbva.iipf.pf.dao;

import java.util.List;

import pe.com.stefanini.core.exceptions.DAOException;

public interface ParametroDAO {
	public Long ValidaCargaEnProceso(String strCodigo) throws DAOException ;
	public Integer ValidaCargaAutomatica(String fechaproceso, int idtipoArchivo) throws DAOException;
	
	public List<List>  findResultScript(String strScript,String strTipoScript ) throws DAOException ;
}
