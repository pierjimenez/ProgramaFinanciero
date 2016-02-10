package pe.com.bbva.iipf.pf.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FileUtil;
import pe.com.stefanini.core.util.StringUtil;

@Service("uploadFileAction")
@Scope("prototype") 
public class UploadFileAction extends GenericAction {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	ParametroBO parametroBO;

	private File file;
	private String fileContentType;
	private String fileFileName;
	private String direccionURL;
	private String htmlURL;
			

	public String getHtmlURL() {
		return htmlURL;
	}

	public void setHtmlURL(String htmlURL) {
		this.htmlURL = htmlURL;
	}

	public String getDireccionURL() {
		return direccionURL;
	}

	public void setDireccionURL(String direccionURL) {
		this.direccionURL = direccionURL;
	}

	public ParametroBO getParametroBO() {
		return parametroBO;
	}

	public void setParametroBO(ParametroBO parametroBO) {
		this.parametroBO = parametroBO;
	}
	
	

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String verUploadFile() {     	       
    	return "verUploadFile";
    }     
    
	
	public String UploadFile() {	
		logger.error("INICIO UploadFile");
		String strURLhtml="";		
	     try{

	    	 String strNombreArchivo=fileFileName;
	    	 String StrNombreArchivoGuardado="";
	    	 setDireccionURL("");
	    	 setHtmlURL("");
	    	 int extensionIndex = strNombreArchivo.lastIndexOf(".");
	    	 String strExtension="";
	    	 strExtension=strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length()); 
	    	 
	    	 	File uploadedFile=file;	    		
	    	 
	    		File uniqueFile = null;	    		
	    		Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_UPLOAD_IMAGEN_PDF);
	    		Parametro parametro1 = parametroBO.findByNombreParametro(Constantes.DIR_UPLOADFILE_URL);
	    		
	    			String extencion="";
	    			if(uploadedFile.getName().split("\\.").length>0){
	    				extencion = uploadedFile.getName().split("\\.")[(uploadedFile.getName().split("\\.").length-1)];
	    			}
	    			uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),strNombreArchivo);
	    			StrNombreArchivoGuardado=uniqueFile.getName();
	    			byte[] serObj = getBytesFromFile(uploadedFile);
	    			FileUtil.write(uniqueFile,serObj); 	    			
	    			setDireccionURL(parametro1.getValor()+StrNombreArchivoGuardado);	    			
	    			strURLhtml="<p><img alt=\"Imagen\" src=\"" + parametro1.getValor() + StrNombreArchivoGuardado+ "\"/></p>";	    			
	    			setHtmlURL(strURLhtml);

	     } catch (BOException e) {	 
	    	 e.printStackTrace();
	    	 logger.error(StringUtil.getStackTrace(e));
	    	 return "verUploadFile";
	    	 
	     }catch(Exception e){
	    	 e.printStackTrace();
	    	 logger.error(StringUtil.getStackTrace(e));
	    	 return "verUploadFile";
	     }	    
	     return "verUploadFile";
	}   
	
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

	}


