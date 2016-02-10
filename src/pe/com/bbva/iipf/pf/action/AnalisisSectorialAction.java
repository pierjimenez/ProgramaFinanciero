package pe.com.bbva.iipf.pf.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.AnalisisSectorialBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.bbva.iipf.pf.model.Archivo;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;

@Service("analisisSectorialAction")
@Scope("prototype") 
public class AnalisisSectorialAction extends GenericAction {

	
	Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;

	List<AnalisisSectorial> listAnalisisSectorial =new  ArrayList<AnalisisSectorial>();
	private Programa programa;
	AnalisisSectorial analisisSectorial;	
	@Resource
	private ProgramaBO programaBO;	
	@Resource
	AnalisisSectorialBO analisisSectorialBO;
	@Resource
	ParametroBO parametroBO;
	
	private String nombreArchivo;
	private String codigoArchivo;	
	private String extension;
	
	
	private Long id;	
	private File userArchivo;
	private String userArchivoContentType;
	private String userArchivoFileName;

    private String inputPath;
    private String contentType;
	private String contentDisposition;
	
	private String dirbuscar;
	private String filebuscar;	
	private String archivobuscar;
	
	private List<Archivo> listarchivo=new  ArrayList<Archivo>();

	
	public String init(){
		loadAnalisisSectorial();
		return "analisissectorial";
	}
	
	public void loadAnalisisSectorial(){
		try {
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
						
			if(programaId != null && !programaId.equals("")){
					setPrograma(programaBO.findById(Long.valueOf(programaId)));
					loadDocumentosAdjuntos(Long.valueOf(programaId));
			}		
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Elimina los archivos 
	 * @return
	 */
	public String eliminar(){
		String codigoDocumento=getCodigoArchivo();
		String extencionDocumento= this.getExtension();
		String nombreArchivo=codigoDocumento + "." + extencionDocumento; 
		File file=null;
		String forward = "analisissectorial";
		try {
			Long idprograma = Long.valueOf( getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString()); 	
			AnalisisSectorial analisisSectorial = new AnalisisSectorial();
			analisisSectorial.setId(Long.valueOf(codigoDocumento));
			analisisSectorialBO.delete(analisisSectorial);
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES);
			String pathToFile=parametro.getValor()+File.separator+nombreArchivo;
			file = new File(pathToFile);
			file.delete();
			setListAnalisisSectorial(analisisSectorialBO.findByAnalisisSectorial(idprograma));
			addActionMessage("Eliminado Correctamente");
		} catch (BOException e) {	
			forward = "analisissectorial";
			logger.info(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			forward = "analisissectorial";
			logger.info(StringUtil.getStackTrace(e));
		}
		return forward;
	}
	
	    
    
//	public String UploadArchivo() {
//		try {
//			
//			Long Idprograma = new Long(3362);		
//			if(Idprograma != null &&
//			   !Idprograma.equals("")){
//			loadDocumentosAdjuntos(Idprograma);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();			
//
//			return "analisissectorial";
//		}
//		return "analisissectorial";
//	}
    
	public String getDirbuscar() {
		return dirbuscar;
	}
	
	public void setDirbuscar(String dirbuscar) {
		this.dirbuscar = dirbuscar;
	}
	
	public String getFilebuscar() {
		return filebuscar;
	}
	
	public void setFilebuscar(String filebuscar) {
		this.filebuscar = filebuscar;
	}
	
	public String save() {		
	     try{	         	
    		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
    		Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
    			    		
	    	 Programa programa1 =new Programa();	    	     	
	    	 programa1.setId(new Long(idprograma));	   	
	    	 AnalisisSectorial analisisSectorial1=new AnalisisSectorial();
	    	 UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
	    	 String strNombreArchivo=userArchivoFileName;
	    	 int extensionIndex = strNombreArchivo.lastIndexOf(".");
	    	 String strExtension="";
	    	 strExtension=strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length()); 
	    	 
	    	 analisisSectorial1.setFechaCarga(new Date());
	    	 analisisSectorial1.setNombreArchivo(userArchivoFileName); 
	    	 analisisSectorial1.setExtencionArchivo(strExtension);
	    	 analisisSectorial1.setCodigoArchivo("");
	    	 analisisSectorial1.setPrograma(programa1);
	    	 analisisSectorial1.setUsuario(usuarioSession.getRegistroHost());
	    	 analisisSectorial1.setId(null);
	    	 analisisSectorialBO.save(analisisSectorial1,userArchivo,false);	
	    	 loadDocumentosAdjuntos(idprograma);	
	    	 addActionMessage("Archivo adjuntado correctamente.");
	         
	     } catch (BOException e) {	    	
	    	 return "analisissectorial";
	     }catch(Exception e){	    	
	    	 return "analisissectorial";
	     }
	     return "analisissectorial";
	}
	
	public String adjuntar() {	
		 logger.info("adjuntarcvv");
	     try{	
	    	 UsuarioSesion usuarioSession=new UsuarioSesion();
	    	 String strUsuario="";
	    	 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
	    	 Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
	   		try {
	   			usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
	   			strUsuario=usuarioSession.getRegistroHost();
			} catch (Exception e) {
				strUsuario="P000001";
			}   		
   			    		
	    	 Programa programa1 =new Programa();	    	     	
	    	 programa1.setId(new Long(idprograma));	   	
	    	 AnalisisSectorial analisisSectorial1=new AnalisisSectorial();	    	 	    	 
	    	 
	    	 String strNombreArchivo=this.getFilebuscar();
	    	 String strDirectory=this.getDirbuscar();	
	    	 logger.info("strNombreArchivo" + strNombreArchivo);
	    	 logger.info("strDirectory: " + strDirectory);
	    	 File fileadjunto=new File(strDirectory, strNombreArchivo);
	    	 
	    	 int extensionIndex = strNombreArchivo.lastIndexOf(".");
	    	 String strExtension="";
	    	 strExtension=strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length()); 
	    	 
	    	 analisisSectorial1.setFechaCarga(new Date());
	    	 analisisSectorial1.setNombreArchivo(strNombreArchivo); 
	    	 analisisSectorial1.setExtencionArchivo(strExtension);
	    	 analisisSectorial1.setCodigoArchivo("");
	    	 analisisSectorial1.setPrograma(programa1);
	    	 analisisSectorial1.setUsuario(strUsuario);
	    	 analisisSectorial1.setId(null);
	    	 analisisSectorialBO.save(analisisSectorial1,fileadjunto,false);	
	    	 loadDocumentosAdjuntos(idprograma);	    	
	    	 addActionMessage("Archivo adjuntado correctamente.");
	     } catch (BOException e) {	    	
	    	 return "analisissectorial";
	     }catch(Exception e){	    	
	    	 return "analisissectorial";
	     }
	     return "analisissectorial";
	}   
	
	
	
    public InputStream getInputStream() throws Exception {
    	    	
	logger.info("INICIO download");
		
		String codigoDocumento=getCodigoArchivo();
		String extencionDocumento= this.getExtension();
		String nombreDocumento=this.getNombreArchivo();
		String nombreArchivo=codigoDocumento + "." + extencionDocumento; 
		
		logger.info("codigoDocumento: " + codigoDocumento);
		logger.info("extencionDocumento: " + extencionDocumento);
		logger.info("nombreDocumento: " + nombreDocumento);
		logger.info("nombreArchivo: " + nombreArchivo);		
		File file=null;
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES);
			 
			String pathToFile=parametro.getValor()+File.separator+nombreArchivo;
			String fileName=nombreDocumento;
			String extencion=extencionDocumento;
			 
			file = new File(pathToFile);
	    	setContentType(new MimetypesFileTypeMap().getContentType(file));
	    	setContentDisposition("attachment;filename=\""+ fileName + "\"");
		} catch (BOException e) {			
			e.printStackTrace();	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new FileInputStream(file);
    }
    
    public String execute() throws Exception{
        return "success";
    }
    
	
	
	
//	public String loadArchivosAS(){
//		Long Idprograma = new Long(3362);		
//		if(Idprograma != null &&
//		   !Idprograma.equals("")){
//			loadDocumentosAdjuntos(Idprograma);			
//		}
//		return "analisissectorial"; 
//	}
	
	
	public void loadDocumentosAdjuntos(Long Idprograma){
		try {
			setListAnalisisSectorial(analisisSectorialBO.findByAnalisisSectorial(Idprograma));
		} catch (BOException e) {
			
		}
	}
	
	public String buscarArchAvanzado() throws Exception{
	       	try {
	       		Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_BUSCAR_ANALISISSECTORIAL);
	       	
	       		String strArchivo=this.getArchivobuscar();
	       		//String dir="//118.247.4.245/xcobranzas/DATAAS";
	       		String dir=parametro.getValor();
	       		
	       	 if (dir == null) {
	         	return "buscarArchivosUpload";
	         }
	     	
	     	if (dir.charAt(dir.length()-1) == '\\') {
	         	dir = dir.substring(0, dir.length()-1) + "/";
	     	} else if (dir.charAt(dir.length()-1) != '/') {
	     	    dir += "/";
	     	}
	     	
	     	dir = java.net.URLDecoder.decode(dir, "UTF-8");		       		
	     	File dir1= new File(dir);       		
	    	
	     	search(dir1,strArchivo);
	     	
			} catch (Exception e) {
				
			}
			 return "buscarArchivosUpload";
	    }
	    	
	
	
	public void search(File target,String fileName){
		
		  
	    if(target != null && target.isDirectory())        
	    {            
	    	logger.info("di:  " +target.getPath());
	    	for(File f:target.listFiles())            
		    {                
		    	logger.info("F:  " +f.getPath());
		    	
		    	Object String;
				if(f.isFile())               
		     	{                   
		    		Pattern p = Pattern.compile(fileName);                    
		    		Matcher m = p.matcher(f.getName());                    
		      		if(m.find())  {
		      			Archivo arch=new Archivo();
		      			String strfile=f.getName() ;
		      			int dotIndex = strfile.lastIndexOf('.');
						String ext = dotIndex > 0 ? strfile.substring(dotIndex + 1) : "";
		      			arch.setNombre(f.getName());		      			
		      			String directorio=f.getAbsolutePath();
		      			int dirIndex = directorio.lastIndexOf('\\');
						String strdirectorio = dirIndex > 0 ? directorio.substring(0,dirIndex + 1) : "";		      					
		      			arch.setDirectory(strdirectorio);		      			
		      			arch.setTipo(ext);		      			
		      			listarchivo.add(arch);
		      			logger.info("File Found:  "+f.getAbsolutePath()+ "Filed:" + f.getName() ); 
		      		//else                        
		      		//logger.infoln("File not found: " + f.getName() + f.getAbsolutePath() + fileName );                
		      		}
		      	}               
		      	else  {
		      
		       	search(f,fileName);  
				logger.info("direct: " + f.getPath());}
		    }        
	     }
	    
	}
	
	public String SubStringRight(String Cad, int NroCar){
		String Valor = Cad;
		int LongCad = Cad.length();
		if(NroCar<=LongCad){   
			int Inicio = (LongCad - NroCar );   
		Valor = Valor.substring(Inicio, LongCad);   
		} 
		else{   
			Valor = Cad;  
			}   
		return Valor;  
	}
	
	public void directoryFile() {

		PrintWriter out = null;
		try {
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();		        
		        String dir = getRequest().getParameter("directoryCode");       
		              
			        if (dir == null) {
			        	return;
			        }	    	
			    	if (dir.charAt(dir.length()-1) == '\\') {
			        	dir = dir.substring(0, dir.length()-1) + "/";
			    	} else if (dir.charAt(dir.length()-1) != '/') {
			    	    dir += "/";
			    	}	    	
			    	dir = java.net.URLDecoder.decode(dir, "UTF-8");		    	
			        if (new File(dir).exists()) {
			    		String[] files = new File(dir).list(new FilenameFilter() {
			    		    public boolean accept(File dir, String name) {
			    				return name.charAt(0) != '.';
			    		    }
			    		});
			    		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER); 
			    		out.printf("<select id=\"directoryCodeSelect\" name=\"directory\" onchange=\"showFiles()\">");
			    		 out.printf("<option value=''>Seleccione Directorio</option>");
			    		// All dirs
			    		for (int i = 0; i < files.length; i++) {				
			    				String file=files[i];
			    			
			    		    if (new File(dir, file).isDirectory()) {
			    		    	 out.printf("<option value='%1s'>%2s</option>", dir+file, file);
			    		    }
			    		}	
			    		out.printf("</select>");
			        }  			
		
		} catch (Exception e) {

		} finally {
			out.close();
		}
	}
	
	
	///
	
	public void File() {

		PrintWriter out = null;
		try {
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();
			
		        String fileCode = getRequest().getParameter("fileCode"); //"//118.247.4.245/xcobranzas/DATAAS/AnalisisSecorial"; 
	
		        logger.info("fileCode: " + fileCode);
		        
	        	out.printf("fileCode:" + fileCode);
	            String dir = fileCode;	
	            if (dir == null) {
	            	return;
	            }
	        	
	        	if (dir.charAt(dir.length()-1) == '\\') {
	            	dir = dir.substring(0, dir.length()-1) + "/";
	        	} else if (dir.charAt(dir.length()-1) != '/') {
	        	    dir += "/";
	        	}
	        	
	        	dir = java.net.URLDecoder.decode(dir, "UTF-8");	 
	        		
	        		if (new File(dir).exists()) { 
	        			String[] files = new File(dir).list(new FilenameFilter() {
	        			    public boolean accept(File dir, String name) {
	        					return name.charAt(0) != '.';
	        			    }
	        			});
	        			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
	        			out.printf("<table id=\"tfile\" name=\"tdirectory\" class=\"ui-widget ui-widget-content\" cellspacing=\"0\">");
	        			
	        			
	            		out.printf("<tr class=\"ui-widget-header\">");
	            		out.printf("<th>Tipo</th>");
	            		//out.printf("<th>Directory</th>"); 
	            		out.printf("<th>Nombre</th>"); 
	            		out.printf("<th>Accion</th>");
	            		out.printf("<th>Adjuntar</th>"); 
	            		
	            		out.printf("</tr>");
	    	        	for (int i = 0; i < files.length; i++) {				
	    					String file=files[i];
	    				    if (!new File(dir, file).isDirectory()) {
	    						int dotIndex = file.lastIndexOf('.');
	    						String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
	    						out.printf("<tr>");
	    						out.printf("<td>" + ext + "</td>");
	    						//out.printf("<td>" + dir +  "</td>");
	    						out.printf("<td width=\"300px\">" + file + "</td>");										
	    						out.printf("<td><img src=\"image/pdf.png\" alt=\"download\"><a href=\"#\" onClick=\"donwload1('" + dir + "','"+ file +"')\">Abrir</a></td>");
	    						out.printf("<td><img src=\"image/Attach.png\" alt=\"download\"><a href=\"doadjuntar.do?dirbuscar=" + dir + "&filebuscar=" + file + "\">Adjuntar</a></td>");
	    						
	    						out.printf("</tr>");
	    				    	}
	    	        	}
	    	        	
	    	        	out.printf("</table>");
	                
	        		}          
		        
		
		} catch (Exception e) {

		} finally {
			out.close();
		}
	}
	
	
	public void DownloadFile() {
		   String fileName="";
		    String ruta="";
		
			try {
			    OutputStream outStream = getResponse().getOutputStream();
		        ruta=getRequest().getParameter("ruta");
		        fileName=getRequest().getParameter("fileName");
		        logger.info("ruta:"+ ruta);
		        logger.info("Filename:"+ fileName);
		        if (ruta == null) {
		        	return;
		        }
		        ruta = java.net.URLDecoder.decode(ruta, "UTF-8");	
		                
		        
		       logger.info("Filename3:"+fileName);
		       
		        String filePath =ruta ;
		        File f=new File(filePath, fileName);
		        String fileType = fileName.substring(fileName.indexOf(".")+1,fileName.length());
		//        log.debug("Filetype:"+fileType+";"+f.length());
		
		        if (fileType.trim().equalsIgnoreCase("txt")) {
		        	getResponse().setContentType( "text/plain" );
		        } else if (fileType.trim().equalsIgnoreCase("doc")) {
		        	getResponse().setContentType( "application/msword" );
		        } else if (fileType.trim().equalsIgnoreCase("xls")) {
		        	getResponse().setContentType( "application/vnd.ms-excel" );
		        } else if (fileType.trim().equalsIgnoreCase("pdf")) {
		        	getResponse().setContentType( "application/pdf" );
		//            log.debug("content type set to pdf");
		        } else {
		        	getResponse().setContentType( "application/octet-stream" );
		        }
		        
		        getResponse().setContentLength((int)f.length());
		        getResponse().setHeader("Content-Disposition"," attachment; filename=\"" + fileName + "\"  ");
		
		        getResponse().setHeader("Cache-Control", "max-age=60"); 
		        //response.setHeader("Cache-Control", "no-cache");
		        byte[] buf = new byte[8192];
		        FileInputStream inStream = new FileInputStream(f);
		        int sizeRead = 0;
		        while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
		//            log.debug("size:"+sizeRead);
		            outStream.write(buf, 0, sizeRead);
		        }
		 	    inStream.close();
		        outStream.close();
	        
		} catch (Exception e) {
	
		} finally {
	
		}
		
	}
	
    
    public String delete() { 
    	return "analisissectorial";
    	} 
    
    public String buscarArchivos() {     	       
    	return "buscarArchivosUpload";
    } 


    
    
    public String regresar() {   
    	loadAnalisisSectorial();
    	return "analisissectorial";
    } 
	
	
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	public AnalisisSectorial getAnalisisSectorial() {
		return analisisSectorial;
	}
	public void setAnalisisSectorial(AnalisisSectorial analisisSectorial) {
		this.analisisSectorial = analisisSectorial;
	}
	public AnalisisSectorialBO getAnalisisSectorialBO() {
		return analisisSectorialBO;
	}
	public void setAnalisisSectorialBO(AnalisisSectorialBO analisisSectorialBO) {
		this.analisisSectorialBO = analisisSectorialBO;
	}
	public List<AnalisisSectorial> getListAnalisisSectorial() {
		return listAnalisisSectorial;
	}
	public void setListAnalisisSectorial(
			List<AnalisisSectorial> listAnalisisSectorial) {
		this.listAnalisisSectorial = listAnalisisSectorial;
	}

	
	public File getUserArchivo() {
		return userArchivo;
	}

	public void setUserArchivo(File userArchivo) {
		this.userArchivo = userArchivo;
	}

	public String getUserArchivoContentType() {
		return userArchivoContentType;
	}

	public void setUserArchivoContentType(String userArchivoContentType) {
		this.userArchivoContentType = userArchivoContentType;
	}

	public String getUserArchivoFileName() {
		return userArchivoFileName;
	}

	public void setUserArchivoFileName(String userArchivoFileName) {
		this.userArchivoFileName = userArchivoFileName;
		
	}


	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentDisposition() {
		return contentDisposition;
	}
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	public List<Archivo> getListarchivo() {
		return listarchivo;
	}
	public void setListarchivo(List<Archivo> listarchivo) {
		this.listarchivo = listarchivo;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}

	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	

	public String getArchivobuscar() {
		return archivobuscar;
	}
	
	public void setArchivobuscar(String archivobuscar) {
		this.archivobuscar = archivobuscar;
	}
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	public String getCodigoArchivo() {
		return codigoArchivo;
	}
	
	public void setCodigoArchivo(String codigoArchivo) {
		this.codigoArchivo = codigoArchivo;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void Clear(){		
		this.setListAnalisisSectorial(new  ArrayList<AnalisisSectorial>());
	}	 
	
	
}
