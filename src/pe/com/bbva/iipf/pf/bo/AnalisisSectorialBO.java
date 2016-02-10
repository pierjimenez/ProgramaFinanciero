package pe.com.bbva.iipf.pf.bo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.stefanini.core.exceptions.BOException;

public interface AnalisisSectorialBO {	
	public List<AnalisisSectorial> findByAnalisisSectorial(Long idprograma) throws BOException;
	public void save(AnalisisSectorial analisisSectorial,
			File userArchivo,boolean isFlagEditFile) throws BOException;
	public void savecopiaAnalisis(AnalisisSectorial oanalisisSectorial,Long IdAnalisisini) throws BOException,IOException;
	public void delete(AnalisisSectorial t) throws BOException;
}
