package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.ArchivoAnexoBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FileUtil;

@Service("archivoAnexoBO")
public class ArchivoAnexoBOImpl extends GenericBOImpl<ArchivoAnexo> implements ArchivoAnexoBO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroBO parametroBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	public void beforeSave(ArchivoAnexo t) throws BOException{
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveFileAnexo(File fileAnexo, 
							  Programa programa,
							  ArchivoAnexo archivoAnexo) throws BOException {
		Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILES_ANEXOS);
		File uniqueFile = null;
		archivoAnexo.setPrograma(programa);
		archivoAnexo.setNombreArchivo(archivoAnexo.getNombreArchivo());
		archivoAnexo.setExtencion(archivoAnexo.getExtencion());
		super.save(archivoAnexo);			
			
		try {
			uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),
												 archivoAnexo.getId()+
												 "."+
												 archivoAnexo.getExtencion());
		
			byte[] serObj = FileUtil.readBytes(fileAnexo);
			FileUtil.write(uniqueFile,serObj);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		} catch (IOException e) {
			throw new BOException(e);
		} catch (Exception e) {
			throw new BOException(e);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaFileAnexo(ArchivoAnexo archivoAnexo,Long IdarchivoAnexo) throws BOException,IOException {
		
		File uniqueFile = null;
		File origenFile= null;	
					
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILES_ANEXOS);
			
			ArchivoAnexo t = new ArchivoAnexo ();
			
			String strNombreArchivo=archivoAnexo.getNombreArchivo();	    	 
	    	 String strExtension="";
	    	 strExtension=archivoAnexo.getExtencion()==null?"xls":archivoAnexo.getExtencion(); 
	 			
	    	 t=archivoAnexo;
	    	 t.setNombreArchivo(strNombreArchivo);			
	    	 t.setExtencion(strExtension);						
			super.onlySave(t);				
									
			origenFile = new File(parametro.getValor(), IdarchivoAnexo.toString()+"."+ strExtension);		        
	        if (origenFile.exists()) {
	        	uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),
	        								  t.getId()+
											 "."+
											 strExtension);			
				CopiaFile(origenFile,uniqueFile);				
				
	        }			
			
		} catch (IOException e) {
			throw new IOException(e.getMessage()); 
		}catch (BOException e) {
			throw new BOException(e.getMessage()); 
		}catch(Exception e){
			throw new BOException(e.getMessage());
		}
	}
	
	public boolean CopiaFile(File src, File dst) throws IOException {
			logger.info("Obteniendo archivo ... ");
			InputStream in = null;
			OutputStream out = null;
			File fSrc = src;
			File fDst;
		try {	
			fDst = dst;
			if (!fDst.exists()) {
				fDst.createNewFile();
			}
			
			in = new FileInputStream(fSrc);
			out = new FileOutputStream(fDst);
	
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			logger.info("fin copiado ... ");
	
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error entrada/salida. " + e.getMessage(), e);
			return false;
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Error cerrar file. " + ex.getMessage(), ex);
			}
		}
		return true;
	}

	/**
	 * Metodo para obtener la lista de archivos adjuntos al programa financiero
	 * @param programa
	 * @return
	 * @throws BOException
	 */
	@Override
	public List<ArchivoAnexo> findListaArchivos(Programa programa)throws BOException{
		HashMap<String,Long> parametros = new HashMap<String,Long>();
		parametros.put("programa", programa.getId());
		List<ArchivoAnexo> listaArchivos = null;
		try {
			listaArchivos = super.findByParams(ArchivoAnexo.class, 
											   parametros);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaArchivos;
	}
	@Override
	public ArchivoAnexo findById(Long idArchivoAnexo) throws BOException {
		ArchivoAnexo archivoAnexo = super.findById(ArchivoAnexo.class, idArchivoAnexo);
		return archivoAnexo;
	}
}
