package pe.com.bbva.iipf.pf.bo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;

public interface ArchivoAnexoBO {

	public void saveFileAnexo(File fileAnexo, Programa programa , ArchivoAnexo archivoAnexo)throws BOException;
	public List<ArchivoAnexo> findListaArchivos(Programa programa)throws BOException;
	public ArchivoAnexo findById(Long idArchivoAnexo)throws BOException;
	public void delete(ArchivoAnexo t) throws BOException;
	
	public void saveCopiaFileAnexo(ArchivoAnexo archivoAnexo,Long IdarchivoAnexo) throws BOException,IOException;
}
