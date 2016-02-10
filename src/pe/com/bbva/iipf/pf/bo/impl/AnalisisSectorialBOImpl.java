package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.AnalisisSectorialBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.dao.AnalisisSectorialDAO;
import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.FileUtil;

@Service("analisisSectorialBO" )
public class AnalisisSectorialBOImpl extends GenericBOImpl<AnalisisSectorial>
		implements AnalisisSectorialBO {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	AnalisisSectorialDAO analisisSectorialDAO;
	@Resource
	ParametroBO parametroBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Override
	public List<AnalisisSectorial> findByAnalisisSectorial(Long idprograma) throws BOException{
		
		try {			
			return analisisSectorialDAO.findByAnalisisSectorial(idprograma);
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(AnalisisSectorial analisisSectorial,File uploadedFile,boolean isFlagEditFile) throws BOException {
			
		File uniqueFile = null;
		try{
		Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES);
		 
		super.save(analisisSectorial);	
		programaBO.actualizarFechaModificacionPrograma(analisisSectorial.getPrograma().getId());
		if(!isFlagEditFile){
			String extencion="";
			if(uploadedFile.getName().split("\\.").length>0){
				extencion = uploadedFile.getName().split("\\.")[(uploadedFile.getName().split("\\.").length-1)];
			}
			uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),
												analisisSectorial.getId().toString()+
											 "."+
											 analisisSectorial.getExtencionArchivo());
			byte[] serObj = getBytesFromFile(uploadedFile);
			FileUtil.write(uniqueFile,serObj);
			
		}
		} catch (IOException e) {
		throw new BOException(e.getMessage());
		}
	}
	
	
	
	///ini mcg 20120502
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void savecopiaAnalisis(AnalisisSectorial oanalisisSectorial,Long IdAnalisisini) throws BOException,IOException {
		
		File uniqueFile = null;
		File origenFile= null;	
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES);
			 					
			AnalisisSectorial t = new AnalisisSectorial ();
			
			String strNombreArchivo=oanalisisSectorial.getNombreArchivo();
	    	 int extensionIndex = strNombreArchivo.lastIndexOf(".");
	    	 String strExtension="";
	    	 strExtension=strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length()); 
	 			
			t=oanalisisSectorial;
			t.setNombreArchivo(strNombreArchivo);			
			t.setExtencionArchivo(strExtension);
			t.setCodigoArchivo(null);			
			super.onlySave(t);			
									
			origenFile = new File(parametro.getValor(), IdAnalisisini.toString()+"."+ strExtension);		        
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
		
	///fin mcg 20120502
	
	
	

	//Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {
	   
	    InputStream is = null;
	    try {
		    is = new FileInputStream(file);
		
		    // Get the size of the file
		    long length = file.length();
		
		    // You cannot create an array using a long type.
		    // It needs to be an int type.
		    // Before converting to an int type, check
		    // to ensure that file is not larger than Integer.MAX_VALUE.
		    if (length > Integer.MAX_VALUE) {
		        // File is too large
		    }
		
		    // Create the byte array to hold the data
		    byte[] bytes = new byte[(int)length];
		
		    // Read in the bytes
		    int offset = 0;
		    int numRead = 0;
		    
		//    while (offset < bytes.length
		//           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {    	
		    while (offset < bytes.length
		    			&& (numRead = is.read(bytes, offset, Math.min(bytes.length - offset, 512*1024))) >= 0) {    	
		        offset += numRead;
		    }
		
		    // Ensure all the bytes have been read in
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }
		
		    // Close the input stream and return bytes
		    is.close();
		    return bytes;
	    } finally {
	    	if (null != is) {
	    	is.close();
	    }
	    }
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}
	
	

}




