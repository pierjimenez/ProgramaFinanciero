package pe.com.bbva.iipf.cpy.bo;

import java.util.List;

import pe.com.bbva.iipf.cpy.model.PFSunat;
import pe.com.stefanini.core.exceptions.BOException;

public interface PFSunatBO {

	public PFSunat findByRUC(String ruc) throws BOException;
	public List getEmpresasDelGrupo(String codigoGrupo, Long idPrograma) throws BOException;
}
