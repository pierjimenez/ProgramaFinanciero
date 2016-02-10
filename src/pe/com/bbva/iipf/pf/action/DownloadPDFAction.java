package pe.com.bbva.iipf.pf.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import pe.com.bbva.iipf.pf.bo.DownloadPDFBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.impl.DownloadPDFBOImpl;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.threads.AdminColaHilos;
import pe.com.bbva.iipf.threads.HiloProceso;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.StringUtil;


@Service("downloadPDFAction")
@Scope("prototype") 
public class DownloadPDFAction extends GenericAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6850232020521173747L;

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private AdminColaHilos adminColaHilos;
	@Resource
	private HiloProceso hiloProceso;
	
	@Resource
	private DownloadPDFBO downloadPDFBO;
	
	@Resource
	ParametroBO parametroBO;
	@Resource
	ProgramaBO programaBO;
	
	private Long idPrograma;
	private String codigoTipoOperacion;
	
	   public void downloadPDFGeneralHilo() {
		   System.out.println("SE INICIO CREACION DE PDF");	
	    	
			String Idprograma= this.getIdPrograma().toString();
			String codigoTipoOperacion=getCodigoTipoOperacion()==null?Constantes.ID_TIPO_OPERACION_PROGRAMA_FINANCIERO:getCodigoTipoOperacion();
			System.out.println("downloadPDFGeneralHilo::codigoTipoOperacion: "+codigoTipoOperacion);	
			
			HashMap<String, String> parametros = new HashMap<String, String>();
			try {				
				
				parametros.put("IdProgramaD",Idprograma);
				parametros.put("CodigoTipoOperacionD",codigoTipoOperacion);			
				//parametros.put("proceso", Constantes.DESCARGA_PDF_HILO);	
				
				downloadPDFBO.dowloadPDFGeneral(parametros);
				
				//getHiloProceso().setParametros(parametros);
				//getHiloProceso().setProceso(Constantes.DESCARGA_PDF_HILO);
				//adminColaHilos.executeThread(getHiloProceso());
				addActionMessage("EL PDF se esta Procesando espere por favor.");
				
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.info("Error getInputStream: "+StringUtil.getStackTrace(e));
			
			}
			 System.out.println("SE FINALIZO CREACION DE PDF");	
				
	    }
	   
   
	   
		public void validarExistFile(){
			logger.info("INICIO VALIDAD EXISTE FILE");
			//System.out.println("INICIO VALIDAD EXISTE FILE");
			String Idprograma= this.getIdPrograma().toString();
			String codigoTipoOperacion=getCodigoTipoOperacion()==null?Constantes.ID_TIPO_OPERACION_PROGRAMA_FINANCIERO:getCodigoTipoOperacion();
			String estadoPDF=Constantes.REP_PROCESANDO;
			try {
				Programa oprograma=new Programa();
				oprograma =programaBO.findById(Long.valueOf(Idprograma));
				 estadoPDF=oprograma.getEstadoDescargaPDF()==null?Constantes.REP_PROCESANDO:oprograma.getEstadoDescargaPDF();
				 //System.out.println("estadoPDF"+estadoPDF);
			} catch (Exception e) {
				 
				logger.error(StringUtil.getStackTrace(e));
			}
			
			String codigoDocumento=Idprograma;
			String extencionDocumento= "pdf";		
			String nombreDocumento="PF" + codigoDocumento;
			String nombreArchivo=nombreDocumento + "." + extencionDocumento; 
	
			File file=null;			
			String stbresultado = "YES";		
	        
			try {
				getResponse().setContentType("text/html"); 
				PrintWriter out = getResponse().getWriter();
				
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_EXPPDF_PROGRAMA_FINANCIERO);
				String pathRutaPDF=parametro.getValor();
				String pathToFilePDF=parametro.getValor()+File.separator+nombreArchivo;	
			
				file = new File(pathToFilePDF);
				if (file.exists() && file.canRead()&& file.length()>0){
					if (estadoPDF.equals(Constantes.REP_TERMINO)){
						//System.out.println("estadoPDF entro"+estadoPDF);
						stbresultado="YES";
					}else{
						stbresultado="NOT";
					}					
					
				}else{
					stbresultado="NOT";
				}				
				//System.out.println("FIN VALIDAD EXISTE FILE");
				logger.info("INICIO VALIDAD EXISTE FILE");
		        out.print(stbresultado); 	        
		
			} catch (Exception e) {				 
				logger.error(StringUtil.getStackTrace(e));				
			}
			
			 
		}
		
		public HiloProceso getHiloProceso() {
			return hiloProceso;
		}

		public void setHiloProceso(HiloProceso hiloProceso) {
			this.hiloProceso = hiloProceso;
		}
		
		public String getCodigoTipoOperacion() {
			return codigoTipoOperacion;
		}
		public void setCodigoTipoOperacion(String codigoTipoOperacion) {
			this.codigoTipoOperacion = codigoTipoOperacion;
		}
			
		public Long getIdPrograma() {
			return idPrograma;
		}

		public void setIdPrograma(Long idPrograma) {
			this.idPrograma = idPrograma;
		}
}
