package pe.com.bbva.iipf.reportes.dao;

import java.util.List;
import java.util.Map;

import pe.com.stefanini.core.exceptions.DAOException;

public interface GeneradorReporteDAO {
	
	public List listDatosBasicos(Map<String, Object> params) throws DAOException;

}
