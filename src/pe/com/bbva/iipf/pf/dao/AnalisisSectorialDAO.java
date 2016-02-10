package pe.com.bbva.iipf.pf.dao;

import java.util.List;

import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.stefanini.core.exceptions.DAOException;

public interface AnalisisSectorialDAO {

	public List<AnalisisSectorial> findByAnalisisSectorial(Long idprograma) throws DAOException;	
	
}
