package pe.com.bbva.iipf.pf.action;

import static pe.com.stefanini.core.host.Util.obtenerCadenaHTMLValidada;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import pe.com.bbva.iipf.cpy.bo.PFSunatBO;
import pe.com.bbva.iipf.cpy.model.PFSunat;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.bo.ReporteCreditoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.ClaseCredito;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.Garantia;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.ReporteCredito;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.pf.model.SustentoOperacion;
import pe.com.bbva.iipf.pf.model.rcd;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;
import pe.com.stefanini.core.util.ValidateUtil;

@Service("generarPDFCreditoAction")
@Scope("prototype")
public class GenerarPDFCreditoAction extends GenericAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	ParametroBO parametroBO;
	@Resource
	private ProgramaBO programaBO;
	@Resource
	private RatingBO ratingBO;
	@Resource
	private PFSunatBO sunatBO;
	
	@Resource
	private RelacionesBancariasBO relacionesBancariasBO;
	@Resource
	private ReporteCreditoBO reporteCreditoBO;	
	
	@Resource
	private DatosBasicosBO datosBasicosBO ;	
	
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private PoliticasRiesgoBO politicasRiesgoBO;
	
	@Resource
	private TablaBO tablaBO;	
	
	@Resource
	private AnexoBO anexoBO;
	
	@Resource
	private DatosBasicosBlobBO  datosBasicosBlobBO;
	@Resource
	private SintesisEconomicoBlobBO  sintesisEconomicoBlobBO;
	
    private String inputPath;
    private String contentType;
	private String contentDisposition;
	
	private Long idPrograma;
	private Programa programa;
	private String codEmpresaRDC;
		
		
	    public InputStream getInputStream() throws Exception {
	    	
	    	
    		String Idprograma= this.getIdPrograma().toString();
    		String codEmpresaGrupo= this.getCodEmpresaRDC().toString();
    		
    		String codigoDocumento=Idprograma;
    		String extencionDocumento= "pdf";
    		String extensionDocumentoxhtml="xhtml";
    		String nombreDocumento="RDC" + codigoDocumento + "_"+ codEmpresaGrupo;
    		String nombreArchivo=nombreDocumento + "." + extencionDocumento; 
    		String nombreArchivoxhtml=nombreDocumento + "." + extensionDocumentoxhtml;
    		
    		logger.info("codigoDocumento: " + codigoDocumento);
    		logger.info("extencionDocumento: " + extencionDocumento);
    		logger.info("nombreDocumento: " + nombreDocumento);
    		logger.info("nombreArchivo: " + nombreArchivo);	
    		logger.info("codEmpresaGrupo: " + codEmpresaGrupo);
    		String d=String.valueOf("00663727");
    		File file=null;
    		try {
    			Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_EXPPDF_PROGRAMA_FINANCIERO);
    			 
    			String pathToFilePDF=parametro.getValor()+File.separator+nombreArchivo;
    			String pathToFileXHTML=parametro.getValor()+File.separator+nombreArchivoxhtml;
    			//genera Archivo PDF de xhtml
    			logger.info("Generando archivo en pathToFilePDF="+pathToFilePDF);
    			logger.info("Generando archivo en pathToFileXHTML="+pathToFileXHTML);
    			//pathToFileXHTML="C:\\@everis\\ET_Duran\\mnt\\compartido\\profin\\export\\RDC20648.pdf";
    			//pathToFilePDF="C:\\@everis\\ET_Duran\\mnt\\compartido\\profin\\export\\RDC20648.xhtml";
    			
    			ReportPDFCredito(Idprograma,pathToFileXHTML,pathToFilePDF,codEmpresaGrupo);  			
    			
    			String fileName=nombreDocumento+ "." + extencionDocumento; ;    			
    			 
    			file = new File(pathToFilePDF);
    	    	setContentType(new MimetypesFileTypeMap().getContentType(file));
    	    	setContentDisposition("attachment;filename=\""+ fileName + "\"");
    		} catch (BOException e) {			
    			logger.error(StringUtil.getStackTrace(e));
    		}catch (Exception e) {
    			logger.error(StringUtil.getStackTrace(e));
    		}
    		return new FileInputStream(file);
        }
	
	    
		public void ReportPDFCredito(String IdPrograma,String rutaxhtml,String rutaPDF, String codEmpresaGrupo){
			logger.info("INICIO ReportePDF");
			getResponse().setContentType("application/pdf");
	        
	        StringBuffer buf = new StringBuffer();
	        buf.append("<html>");
	        
	  	   String css= "<style type=\"text/css\">"+ 	
	 	   	" table.gridtable {" +
	 	    " width:100%;" +
	 	   	" font-family: verdana,arial,sans-serif;"+
			" font-size:7px;" +
			" color:#333333;"+
			" border-width: 0.01em;" +
			" border-color: #666666;" +
			" border-collapse: collapse;" +	
			" margin: 1em 0; " +
			" page-break-inside:auto; page-break-inside:avoid; " +		
			" }" +
			" table.gridtable tr {" +
			" page-break-inside:avoid; page-break-after:auto" +
			" }" +
			" table.gridtable th {" +
			" border-width: 0.01em;" +
			" padding: 5px;" +
			" border-style: solid;" +
			" color:#333333;" +
			" border-color: #666666;" +
			" background-color: #A8C1D5;" +
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtable td {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #ffffff;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;" +
			" }" +
			" table.gridtable td.alt1 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #A9D0F5;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtable td.alt2 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #CEE3F6;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtable td.alt3 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #E6E6E6;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +		
	 	   	" table.gridtableComent {" +
	 	    " width: 100%;" +
	 	   	" font-family: verdana,arial,sans-serif;"+
			" font-size:8px;" +
			" color:#333333;"+
			" border-width: 0.01em;" +
			" border-color: #666666;" +
			" border-collapse: collapse;" +	
			" margin: 1em 0; " +		
			" }" +
			" table.gridtableComent th {" +
			" border-width: 0.01em;" +
			" padding: 5px;" +
			" border-style: solid;" +
			" color:#333333;" +
			" border-color: #666666;" +
			" background-color: #A8C1D5;" +
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtableComent td {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #ffffff;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtableComent td.alt1 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #A9D0F5;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtableComent td.alt2 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #CEE3F6;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +
			" table.gridtableComent td.alt3 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #E6E6E6;" +	
			" border: 0.01em solid #eee;padding: .1em .5em;"+
			" }" +	
			" body{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:50%;" +
			" }" +	
			" h1{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:25px;" +
			" }" +	
			" h2{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:18px;" +
			" }" +	
			" h3{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:15px;" +
			" }" +
			" h4{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:10px;" +
			" }" +
			" div.saltopage" +
			" {" +
			"   PAGE-BREAK-AFTER: always" +
			"  }" +	
			"td.mydtcab {" +	
			"	color: #000000;" +	
			"	border-right: 0.05em solid #000000;" +	
			"	border-bottom: 0.05em solid #000000;" +	
			"	border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			"	letter-spacing: 2px;" +	
			"	text-transform: uppercase;" +	
			"	text-align: left;" +	
			"	padding: 1px 2px 1px 4px;" +	
			"	background: #CAE8EA no-repeat;" +	
			"	}" +	
			" td.mydtString {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 4px;" +				
			" 	color: #000000;" +
			"   text-align: left;" +
			" 	}" +	
			" td.mydtNumero {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 4px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtNumeroCab {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #CAE8EA no-repeat;" +				
			" 	padding: 1px 2px 1px 4px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtTitulo {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 4px;" +				
			" 	color: #000000;" +
			"   text-align: center;" +
			"   font-size: 8px;" +
			" 	}" +
			" td.mydtTituloCab {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 4px;" +
			"	background: #CAE8EA no-repeat;" +
			" 	color: #000000;" +
			"   text-align: center;" +
			"   font-size: 8px;" +
			" 	}" +
			" td.mydtlogo {	" +	
			" 	background: #0F0972;" +	
			" 	color: #FFFFFF;" +
			" 	font-size:10px;" +
			"   text-align: left;" +
			"   width: 140px;" +
			" 	}" +	
			" td.mydtlogorev {	" +	
			" 	background: #FFFFFF;" +	
			" 	color: #0C71B5;" +
			"   font-size:10px;" +
			"   text-align: left;" +
			" 	}" +
			"	table.titulo {width: 100%; }" +
			"	table.gridtableTitulo{width: 90%; }" +
			"" +		
			" </style>";
	 	  String cssreducido= "<style type=\"text/css\">"+ 	
		   	" table.gridtablereducido {" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:5px;" +
			" border-collapse: collapse;" +
			" border: 0;" +
			" }" +
			" </style>";   	   	

	 	   try {
	 		   
	        buf.append("<body>");       
	        
	      
	        buf.append(DatosGeneralesHTML(IdPrograma,codEmpresaGrupo));	              
	        buf.append(CuadroClaseCreditoHTML(IdPrograma,codEmpresaGrupo));	       
	        buf.append(CuadroGarantiaHTML(IdPrograma,codEmpresaGrupo));
	        buf.append(CuadroSustentoOperacionHTML(IdPrograma,codEmpresaGrupo));
	        buf.append(datosBasicoAdicionalHTML(IdPrograma,codEmpresaGrupo));
	        buf.append(posicionClienteGrupoeconomicoHTML(IdPrograma,codEmpresaGrupo));	        
	        //buf.append("<div class=saltopage>&nbsp; </div>");
	        
	        buf.append("</body>");
	        buf.append("</html>");        
	        
	        // parse our markup into an xml Document
	 	    InputStream IS=new StringBufferInputStream(buf.toString());//con.getInputStream();
		    ByteArrayOutputStream OS = new ByteArrayOutputStream();
		    Tidy T =new Tidy();
		    T.setXHTML(true);
		    T.setPrintBodyOnly(true);
		    T.setShowWarnings(true);	       
		    //T.setOutputEncoding("ISO-8859-1");
		    T.parseDOM(IS,OS); 
		    String refe="<head> " + css + cssreducido+ " </head>";
		    
		    
		    
		    String cadenahtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
		 	   +" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
			    + "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
			   +"<html xmlns=\"http://www.w3.org/1999/xhtml\">"	    	
			    	+ refe + " <body>" +
			    	"<table align=\"center\">"+
			    		"<tr>"+
			    			"<td>"+
			    			"<div style=\" width: 700px; overflow:hidden\">"+//div para controlar desborde
			    			OS.toString()+
			    			"</div>"+
			    			"</td>"+
		    			"</tr>"+
	    			"</table>"+
    			"</body> </html> ";
		    
	        //Crear un archivo xhtml
	        FileWriter fw = new FileWriter(rutaxhtml);
	        //Escribir linea en el archivo
	        BufferedWriter bw = new BufferedWriter(fw);
	        PrintWriter salida = new PrintWriter(bw);
	        salida.println(cadenahtml);
	        salida.close();
	        
	        String inputFile = rutaxhtml;
	        String url = new File(inputFile).toURI().toURL().toString();
	        String outputFile = rutaPDF;
	        OutputStream os = new FileOutputStream(outputFile);
	        
//	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	        Document doc = builder.parse(new StringBufferInputStream(buf.toString()));
	        
	        ITextRenderer renderer = new ITextRenderer();        
	        renderer.setDocument(url);	        
	        renderer.layout();
	        //renderer.getWriter().setMargins(10, 10, 10, 10);
	        //renderer.
	        renderer.createPDF(os);   
	        
	        os.close();
		    

	            
	        } catch (Exception e) {
	        	logger.error(StringUtil.getStackTrace(e));
	        }
	        logger.info("FIN ReportePDF");
		}
	
		public String DatosGeneralesHTML(String programaId,String codEmpresaGrupo){
			String datosGenerales="";
			StringBuilder sb=new StringBuilder ();
			String nombre;			
			try {			
					
						setPrograma(programaBO.findById(Long.valueOf(programaId)));
						String tipo_empresa = programa.getTipoEmpresa().getId().toString(); 						
						nombre=programa.getNombreGrupoEmpresa()==null?"":programa.getNombreGrupoEmpresa().toString();
						
						if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){								
								
								Empresa empresa= new Empresa();	
								empresa=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
								nombre=empresa.getNombre()==null?"":empresa.getNombre().toString();
								if (empresa!=null && !empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
										
									   List<ReporteCredito> listReporteCredito=reporteCreditoBO.getListaReporteCredito(getPrograma(),codEmpresaGrupo);
									   if (listReporteCredito!=null && listReporteCredito.size()>0 ){
										   
										   ReporteCredito reportCredito =listReporteCredito.get(0);								   
										   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);										   
										   						   
										   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
										   getPrograma().setGestor(odatosBasico.getGestor());
										   getPrograma().setOficina(odatosBasico.getOficina());
										   getPrograma().setSegmento(odatosBasico.getSegmento());
										   
										   getPrograma().setFechaRDC(reportCredito.getFechaRDC()==null?"":reportCredito.getFechaRDC().toString());							   
										   getPrograma().setCuentaCorriente(reportCredito.getCuentaCorriente()==null?"":reportCredito.getCuentaCorriente().toString());							   
										   getPrograma().setNumeroRVGL(reportCredito.getNumeroRVGL()==null?"":reportCredito.getNumeroRVGL().toString());
										   getPrograma().setRuc(empresa.getRuc());	
										   getPrograma().setCiiuRDC(reportCredito.getCiiuRDC());
										   getPrograma().setSalem(reportCredito.getSalem()==null?"":reportCredito.getSalem().toString());
										   							   
									   }else{
										   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
										   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
										   getPrograma().setGestor(odatosBasico.getGestor());
										   getPrograma().setOficina(odatosBasico.getOficina());
										   getPrograma().setSegmento(odatosBasico.getSegmento());
										   
										   getPrograma().setFechaRDC("");							   
										   getPrograma().setCuentaCorriente("");							   
										   getPrograma().setNumeroRVGL("");
										   getPrograma().setRuc(empresa.getRuc());
										   getPrograma().setCiiuRDC("");
										   getPrograma().setSalem("");	
									   }
								}						
								
						}
						
						DatosBasico olddatosBasico=new DatosBasico();
						olddatosBasico.setCalificacionBanco(getPrograma().getCalificacionBanco());			 
						olddatosBasico.setGestor(getPrograma().getGestor());
						olddatosBasico.setOficina(getPrograma().getOficina());
						olddatosBasico.setSegmento(getPrograma().getSegmento());	
						
						DatosBasico newdatosBasico=new DatosBasico();
						newdatosBasico=ObtenerDatosBaicosByEmpresaHost(codEmpresaGrupo,olddatosBasico);	
						programa.setCalificacionBanco(newdatosBasico.getCalificacionBanco());
						programa.setGestor(newdatosBasico.getGestor());
						programa.setOficina(newdatosBasico.getOficina());
						programa.setSegmento(newdatosBasico.getSegmento());	
						
						
						
						
						String clasificacionBanco=programa.getCalificacionBanco()==null?"":programa.getCalificacionBanco().toString();
						String gestorRDC =programa.getGestor()==null?"":programa.getGestor().toString();
						String OficinaRDC=programa.getOficina()==null?"":programa.getOficina().toString();
						String segmentoRDC=programa.getSegmento()==null?"":programa.getSegmento().toString();
						
						
						String fechaRDC=obtenerFecha(programa.getFechaRDC());
						String cuentacorrienteRDC=programa.getCuentaCorriente()==null?"":programa.getCuentaCorriente().toString();
						
						String nrvglRDC=programa.getNumeroRVGL()==null?"":programa.getNumeroRVGL().toString();
						String rucRDC=programa.getRuc()==null?"":programa.getRuc().toString();
						
						String rating=ObtenerRating (getPrograma(),codEmpresaGrupo);
						String codigociiuRDC=buscarActividadesEconomicasByRuc(getPrograma());
						String salemRDC =programa.getSalem()==null?"":programa.getSalem().toString();						
						
						String grupoEconomico=programa.getIdGrupo()==null?"":programa.getIdGrupo().toString();
						String codigoCentral=codEmpresaGrupo;						
						
			
					sb.append("<table class=\"titulo\" >");
					sb.append("<tr>");
					sb.append("<td  style=\"width:10%\"  >");	
					
					sb.append("<table>");        		
					sb.append("<tr >");
						sb.append("<td class=\"mydtlogorev\" style=\"width:5%\"  >");
						sb.append("<b>BBVA</b>");
						sb.append("</td>");
						sb.append("<td class=\"mydtlogorev\" style=\"width:15%\" >");
						sb.append("Continental");
						sb.append("</td>");				
					sb.append("</tr>"); 				
					sb.append("</table>");	
					
					sb.append("</td>");
					sb.append("<td>");
						
					sb.append("<table class=\"gridtableTitulo\" >");
					sb.append("<tr>");
					sb.append("<td align=\"center\">");
					sb.append("<h4><b>REPORTE DE CRÉDITO - Personas Jurídicas</b></h4>");
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</table>");
					
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</table>");
					
					sb.append( " <table class=\"gridtable\">"        		
			        		+ " <tr>" 
			        		+ "   <th colspan=\"6\">DATOS GENERALES</th>"			        		
			        		+ " </tr>"        
			        		+ " <tr>"         
			        		+ "   <td>Nombre </td>"
			        		+ "   <td colspan=\"2\">" + nombre + "</td>"
			        		+ "   <td>Fecha </td>"
			        		+ "   <td colspan=\"2\">" + fechaRDC + "</td>"			        		
			        		+ " </tr>"        
			        		+ " <tr>" 
			        		+ "   <td>Cta. Cte.</td>"
			        		+ "   <td colspan=\"2\">" + cuentacorrienteRDC + "</td>"
			          		+ "   <td>Oficina</td>"
			        		+ "   <td colspan=\"2\">" + OficinaRDC + "</td>"
			        		+ " </tr>"
			        		+ " <tr>" 			        	
			        		+ "   <td>N° RVGL</td>"
			        		+ "   <td colspan=\"2\">" + nrvglRDC + "</td>" 
			        		+ "   <td></td>"
			        		+ "   <td colspan=\"2\"></td>"
			        		+ " </tr>"  
			        		+ " <tr>" 
			        		+ "   <td>RUC</td>"
			        		+ "   <td colspan=\"2\">" + rucRDC + "</td>"
			        		+ "   <td>Rating</td>"
			        		+ "   <td colspan=\"2\">" + rating + "</td>"       		
			        		+ " </tr>"  
			        		+ " <tr>" 
			        		+ "   <td>CIIU</td>"
			        		+ "   <td colspan=\"2\">" + codigociiuRDC + "</td>"
			        		+ "   <td>Salem</td>"
			        		+ "   <td colspan=\"2\">" + salemRDC + "</td>"       		
			        		+ " </tr>" 
			        		+ " <tr>" 
			        		+ "   <td>Gestor</td>"
			        		+ "   <td colspan=\"2\">" + gestorRDC + "</td>"
			        		+ "   <td>Clasific. Banco</td>"
			        		+ "   <td colspan=\"2\">" + clasificacionBanco+ "</td>"       		
			        		+ " </tr>" 
			        		+ " <tr>" 
			        		+ "   <td>Grupo Económico</td>"
			        		+ "   <td colspan=\"2\">" + grupoEconomico + "</td>"
			        		+ "   <td>Cod. Central</td>"
			        		+ "   <td colspan=\"2\">" + codigoCentral + "</td>"       		
			        		+ " </tr>" 
			        		+ " <tr>" 			        		
			        		+ "   <td>Segmento</td>"
			        		+ "   <td colspan=\"5\">" + segmentoRDC + "</td>"
			        		+ " </tr>" 
			        		+ " </table>");	
									
					datosGenerales= sb.toString();
					
				} catch (BOException e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				} catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				}
				return datosGenerales;
		}
		
		public String ObtenerRating(Programa oprograma,String codEmpresa){	
			
			List<Rating> listaRatingAll = new ArrayList<Rating>(); 
			String descRating=Constantes.NOM_RATING;
			String strRatingRDC="";
			try {	
						
				listaRatingAll = ratingBO.findRating(oprograma.getId(),codEmpresa);	
				if (listaRatingAll!=null && listaRatingAll.size()>0 ){
					for(Rating orating : listaRatingAll){	
						//logger.error("ObtenerRating" + orating.getDescripcion());
						if (orating.getDescripcion()!=null && orating.getDescripcion().trim().equals(descRating)){
							strRatingRDC=orating.getTotalAnioActual();
							break;
						}				
						
					}
				}else{
					strRatingRDC="";
				}						
				
			} catch (BOException e) {
				strRatingRDC="";
				logger.error(StringUtil.getStackTrace(e));
			} catch (Exception e) {
				strRatingRDC="";
				logger.error(StringUtil.getStackTrace(e));
			}	
			return strRatingRDC;	
		}
		
		public String buscarActividadesEconomicasByRuc(Programa oprograma){		
			
			String ruc = oprograma.getRucRDC();
			
			String actividadesEconomicas = "";
			try {			
					if (oprograma.getCiiuRDC()==null){	
						PFSunat sunat = sunatBO.findByRUC(ruc);	
						if(sunat!=null){
							actividadesEconomicas = sunat.getCiiu() + " "+  sunat.getDet_activiad();
						}else{
							actividadesEconomicas="";
						}	
					}else{
						actividadesEconomicas=oprograma.getCiiuRDC();
					}				
				
			} catch (BOException e) {
				actividadesEconomicas="";
				logger.error(StringUtil.getStackTrace(e));
			} catch (Exception e) {
				actividadesEconomicas="";
				logger.error(StringUtil.getStackTrace(e));
			}
			return actividadesEconomicas;		
		}
		
		public String loadCalificacionBanco(Programa programa,String tipoDocumento) {
			String calificacion="";
			try {
				if (programa.getCalificacionBanco()==null){
					
					List<rcd> listrcdtmp=relacionesBancariasBO.findByCalificacionBanco(programa.getRuc(),tipoDocumento);
					rcd orcd=new rcd();
					if (listrcdtmp!=null && listrcdtmp.size()>0){
						orcd=listrcdtmp.get(0);					
					
						if (orcd.getCalificacionAlineada().equals(Constantes.ID_CALIFICACION_NORMAL)){
							calificacion=Constantes.CALIFICACION_NORMAL;
						}else if(orcd.getCalificacionAlineada().equals(Constantes.ID_CALIFICACION_PROBLEMAPOTENCIAL)){
							calificacion=Constantes.CALIFICACION_PROBLEMAPOTENCIAL;
						}
						else if(orcd.getCalificacionAlineada().equals(Constantes.ID_CALIFICACION_DEFICIENTE)){
							calificacion=Constantes.CALIFICACION_DEFICIENTE;
						}
						else if(orcd.getCalificacionAlineada().equals(Constantes.ID_CALIFICACION_DUDOSO)){
							calificacion=Constantes.CALIFICACION_DUDOSO;
						}
						else if(orcd.getCalificacionAlineada().equals(Constantes.ID_CALIFICACION_PERDIDA)){
							calificacion=Constantes.CALIFICACION_PERDIDA;
						}
							
					}
				}else{
					
					if (programa.getCalificacionBanco().equals(Constantes.ID_CALIFICACION_NORMAL)){
						calificacion=Constantes.CALIFICACION_NORMAL;
					}else if(programa.getCalificacionBanco().equals(Constantes.ID_CALIFICACION_PROBLEMAPOTENCIAL)){
						calificacion=Constantes.CALIFICACION_PROBLEMAPOTENCIAL;
					}
					else if(programa.getCalificacionBanco().equals(Constantes.ID_CALIFICACION_DEFICIENTE)){
						calificacion=Constantes.CALIFICACION_DEFICIENTE;
					}
					else if(programa.getCalificacionBanco().equals(Constantes.ID_CALIFICACION_DUDOSO)){
						calificacion=Constantes.CALIFICACION_DUDOSO;
					}
					else if(programa.getCalificacionBanco().equals(Constantes.ID_CALIFICACION_PERDIDA)){
						calificacion=Constantes.CALIFICACION_PERDIDA;
					}						
				}			
				
			} catch (Exception e) {
				calificacion="";
				logger.error(StringUtil.getStackTrace(e));			
			}
			return calificacion;
		}
		
		
		public String CuadroClaseCreditoHTML(String programaId,String codEmpresaGrupo){
			String cuadroclasecredito="";
			List<ClaseCredito> listaClaseCredito = new ArrayList<ClaseCredito>();
			StringBuilder sb=new StringBuilder ();
			Programa oprograman=new Programa();
			oprograman.setId(Long.valueOf(programaId));
			try {
												
						//reporteCreditoBO.setPrograma(programaBO.findById(Long.valueOf(programaId)));
						listaClaseCredito = reporteCreditoBO.ObtenerListaClaseCredito(oprograman,codEmpresaGrupo);				      

						//sb.append("<h4><b><u>2.-Cuadro de Clase de Credito</u></b></h4>");					
						//sb.append("<br/>");	
							
								StringBuilder sbpr=new StringBuilder ();		
								sbpr.append("<table class=\"gridtable\">");						
									
								sbpr.append("<tr>");
									sbpr.append("<th colspan=\"9\">CUADRO DE CLASE DE CREDITO - EN MILES</th>");
								sbpr.append("</tr>");
								sbpr.append("<tr>");							
									sbpr.append("<th>Ord/Cta</th>");	
									sbpr.append("<th>Moneda</th>");
									sbpr.append("<th>Importe</th>");
									sbpr.append("<th width=\"150\" colspan=\"3\">Clase de Crédito</th>");
									sbpr.append("<th>Tasa/Comisión</th>");
									sbpr.append("<th>Vcto/Reembolso</th>");
									sbpr.append("<th>Nota</th>");
								sbpr.append("</tr>");				
									
								if (listaClaseCredito != null && listaClaseCredito.size() > 0) {
									int contc=0;
									for (ClaseCredito occ:listaClaseCredito){
										String flagcan=occ.getFlagCancelado()==null?"":occ.getFlagCancelado().toString();
										if (flagcan.equals("CC")){
											if (contc==0){
												sbpr.append("<tr>");
												sbpr.append("<th colspan=\"9\" align=\"center\">LIMITES QUE SE CANCELAN</th>");
												sbpr.append("</tr>");												
											}
											contc++;
										}
											
										String strorden=occ.getOrden()==null?"":occ.getOrden().toString();
										String strcuenta=occ.getCuenta().getDescripcion()==null?"":occ.getCuenta().getDescripcion().toString();
										String strmoneda=occ.getMoneda().getDescripcion()==null?"":occ.getMoneda().getDescripcion().toString();
										String strImporte=occ.getImporte()==null?"":occ.getImporte().toString();
										String strclaseCredito=occ.getClaseCredito()==null?"":occ.getClaseCredito().toString();
										String strtasaComision=occ.getTasaComision()==null?"":occ.getTasaComision().toString();
										String strTipoVcto=occ.getTipoVcto()==null?"":occ.getTipoVcto().toString();
										String strTipoLimite=occ.getFlagTipoLimte()==null?"":occ.getFlagTipoLimte().toString();
										String strvencimientoReembolso="";
										if (strTipoVcto.equals("R")){											
											 if(occ.getVencimiento()!=null){
												 Tabla oreembolso=new Tabla();
												 Long idtabla=Long.valueOf(occ.getVencimiento());
												 oreembolso=tablaBO.obtienePorId(idtabla);	
												 if (oreembolso!=null){ 
													 strvencimientoReembolso=oreembolso.getDescripcion();
												  }
											 }									
											
										}else{
											strvencimientoReembolso=occ.getVencimiento()==null?"":occ.getVencimiento().toString();
											
										}
										//String strvencimiento=occ.getVencimiento()==null?"":occ.getVencimiento().toString();
										String strnota=occ.getNota()==null?"":occ.getNota().toString();
										sbpr.append("<tr>");	
										if (strTipoLimite.equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
											sbpr.append("<td>&nbsp;</td>");
										}else{
											sbpr.append("<td>" +strorden+ strcuenta + "</td>");
										}											
										sbpr.append("<td>" + strmoneda + "</td>");	
										sbpr.append("<td align=\"right\">" + strImporte + "</td>");
										sbpr.append("<td colspan=\"3\">" + strclaseCredito+ "</td>");
										sbpr.append("<td>" + strtasaComision + "</td>");
										sbpr.append("<td>" + strvencimientoReembolso + "</td>");		
										sbpr.append("<td>" + strnota + "</td>");	
										sbpr.append("</tr>");
									}
								}				
										
								sbpr.append("</table>"); 
								
							    sb.append(sbpr.toString());							
											
						cuadroclasecredito= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return cuadroclasecredito;
		}
		
		public String CuadroGarantiaHTML(String programaId,String codEmpresaGrupo){
			String cuadrogarantia="";
			List<Garantia> listGarantia = new ArrayList<Garantia>();
			StringBuilder sb=new StringBuilder ();	
			String conectorDefault=" - ";
			try {
												
						listGarantia=reporteCreditoBO.findGarantiaByIdprograma(Long.valueOf(programaId),codEmpresaGrupo);	
								
						//sb.append("<h4><b><u>2.-Cuadro de Garantia</u></b></h4>");					
						//sb.append("<br/>");	
							
								StringBuilder sbpr=new StringBuilder ();		
								sbpr.append("<table class=\"gridtable\">");						
								
								sbpr.append("<tr>");	
								 sbpr.append("<th colspan=\"6\">CUADRO GARANTIA</th>");
								sbpr.append("</tr>");
								sbpr.append("<tr>");							
									sbpr.append("<th width=\"60\" align=\"center\">Ord/Cta</th>");	
									sbpr.append("<th width=\"100\">Número Garantía</th>");								
									sbpr.append("<th width=\"290\" colspan=\"4\">Comentario</th>");
								sbpr.append("</tr>");				
								String filaAnterior="";
								String tipo="";
								if (listGarantia != null && listGarantia.size() > 0) {
									for (Garantia oGar:listGarantia){
										 filaAnterior="";
										//String strorden=oGar.getOrden()==null?"":oGar.getOrden().toString();
										//String strcuenta=oGar.getCuenta().getDescripcion()==null?"":oGar.getCuenta().getDescripcion().toString();
										String strnumGarantia=oGar.getNumeroGarantia()==null?"":oGar.getNumeroGarantia().toString();
										String strcomentario=oGar.getComentario()==null?"":oGar.getComentario().toString();
										String strnumGarantiaAnterior=oGar.getNumeroGarantiaAnterior()==null?"":oGar.getNumeroGarantiaAnterior().toString();
										String strcomentarioAnterior=oGar.getComentarioAnterior()==null?"":oGar.getComentarioAnterior().toString();
										sbpr.append("<tr>");
//										if (oGar.getTipo().equals("TC")){	
											String strOrdenFinal=oGar.getOrdenFinal()==null?"":oGar.getOrdenFinal().toString();
											String strorden=oGar.getOrden()==null?"":oGar.getOrden().toString();
											
											String strcuenta=oGar.getCuenta().getDescripcion()==null?"":oGar.getCuenta().getDescripcion().toString();
											String strcuentaFinal=oGar.getCuentaFinal().getDescripcion()==null?"":oGar.getCuentaFinal().getDescripcion().toString();
											
											//ini MCG20140610
											Long idConector=Constantes.ID_TABLA_TIPO_CONECTOR_DEFAULT;
											String strconector=conectorDefault;
											if (oGar.getConector()!=null){
												idConector=oGar.getConector().getId()==null?Constantes.ID_TABLA_TIPO_CONECTOR_DEFAULT:oGar.getConector().getId();
												strconector=oGar.getConector().getDescripcion()==null?conectorDefault:oGar.getConector().getDescripcion().toString();
											}
											
											//fin MCG20140610
											
											
											if(!strcuenta.equals("")){
												tipo=oGar.getCuenta().getId()+"";	
											}
											
											boolean rango=true;
											if(strcuenta.equals(strcuentaFinal)){
												if(strorden.equals(strOrdenFinal)){
													strOrdenFinal	="";
													strcuentaFinal	="";
													rango=false;
												}
											}
											String unionOrden="";
											if (idConector.equals(Constantes.ID_TABLA_TIPO_CONECTOR_DEFAULT)){
												unionOrden="<td align=\"center\">" +strorden+ strcuenta +((rango?conectorDefault+strOrdenFinal+strcuentaFinal:""))+ "</td>";	

											}else{
												unionOrden="<td align=\"center\">" +strorden+ strcuenta +((rango?" "+strconector+" "+strOrdenFinal+strcuentaFinal:""))+ "</td>";	

											}
											//sbpr.append("<td align=\"center\">" +strorden+ strcuenta +((rango?strconector+strOrdenFinal+strcuentaFinal:""))+ "</td>");
											sbpr.append(unionOrden);
//										}else{
//											sbpr.append("<td>&nbsp;</td>");
//										}					
										
										sbpr.append("<td align=\"right\">" + strnumGarantia + "</td>");										
										sbpr.append("<td>" + obtenerCadenaHTMLValidada(strcomentario,true) + "</td>");	
										sbpr.append("</tr>");
										if(tipo.equals(Constantes.TIPO_CUENTA_M)){
											
											filaAnterior="<tr><td>&nbsp;</td><td align=\"right\" >"+strnumGarantiaAnterior+"</td><td colspan=\"2\">"+obtenerCadenaHTMLValidada(strcomentarioAnterior,true)+" (*)Garantia Anterior</td></tr>";
										}
										sbpr.append(filaAnterior);
									}
								}				
										
								sbpr.append("</table>"); 
								
							    sb.append(sbpr.toString());							
											
						cuadrogarantia= sb.toString();
						//System.out.print(cuadrogarantia);
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return cuadrogarantia;
		}
		
		public String CuadroSustentoOperacionHTML(String programaId,String codEmpresaGrupo){
			String cuadrosustento="";
			List<SustentoOperacion> listaSustentoOperacion = new ArrayList<SustentoOperacion>();
			Programa oprograma=new Programa();
			oprograma.setId(Long.valueOf(programaId));
			StringBuilder sb=new StringBuilder ();		
			try {
												
						
						listaSustentoOperacion=reporteCreditoBO.findSustentoOperacionByIdprograma(Long.valueOf(programaId),codEmpresaGrupo);		
						//sb.append("<h4><b><u>2.-Cuadro de Garantia</u></b></h4>");					
						//sb.append("<br/>");	
							
								StringBuilder sbpr=new StringBuilder ();		
								sbpr.append("<table class=\"gridtable\">");						
								
								sbpr.append("<tr>");	
								 sbpr.append("<th colspan=\"6\">CUADRO SUSTENTO OPERACION</th>");
								sbpr.append("</tr>");
								sbpr.append("<tr>");							
									sbpr.append("<th width=\"40\">Ord/Cta</th>");																
									sbpr.append("<th width=\"400\" colspan=\"5\">Sustento</th>");
								sbpr.append("</tr>");				
									
								if (listaSustentoOperacion != null && listaSustentoOperacion.size() > 0) {
									for (SustentoOperacion osop:listaSustentoOperacion){
										String strorden=osop.getOrden()==null?"":osop.getOrden().toString();
										String strcuenta=osop.getCuenta().getDescripcion()==null?"":osop.getCuenta().getDescripcion().toString();
										
										String strOrdenFinal=osop.getOrdenFinal()==null?"":osop.getOrdenFinal().toString();
										String strcuentaFinal=osop.getCuentaFinal().getDescripcion()==null?"":osop.getCuentaFinal().getDescripcion().toString();
										
										boolean rango=true;
										if(strcuenta.equals(strcuentaFinal)){
											if(strorden.equals(strOrdenFinal)){
												strOrdenFinal	="";
												strcuentaFinal	="";
												rango=false;
											}
										}
										StringBuilder stSustentoblob = new StringBuilder();
										String strsustento="";
										if( osop.getSustento()!=null){
						            		for(byte x :osop.getSustento() ){
						            			stSustentoblob.append((char)FormatUtil.getCharUTF(x));
						 		            }
						            		strsustento=stSustentoblob.toString();
						            	}	
										
										
										sbpr.append("<tr>");											
										sbpr.append("<td align=\"center\">" +strorden+ strcuenta +((rango?" - "+strOrdenFinal+strcuentaFinal:""))+ "</td>");	
										sbpr.append("<td>" + obtenerCadenaHTMLValidada(strsustento) + "</td>");										
										sbpr.append("</tr>");
									}
								}				
										
								sbpr.append("</table>"); 
								
							    sb.append(sbpr.toString());							
											
						cuadrosustento= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return cuadrosustento;
		}
		
		public String datosBasicoAdicionalHTML(String programaId, String codEmpresaGrupo){
			String cuadroDatosbasicoadic="";
			float porcentajeMinimo=70;
			List<Accionista> listaAccionistaAll = new ArrayList<Accionista>();	
			List<Accionista> listaAccionistas = new ArrayList<Accionista>();
			String totalAccionista;
			StringBuilder sb=new StringBuilder ();		
			try {
				
				setPrograma(programaBO.findById(Long.valueOf(programaId)));								
				try {
					Parametro parametro = parametroBO.findByNombreParametro(Constantes.PAR_ACCIONISTA_PORCENTAJE_MINIMO);
					porcentajeMinimo=Float.parseFloat(parametro.getValor());
				} catch (Exception e) {
					porcentajeMinimo=70;
				}				
				listaAccionistaAll=datosBasicosBO.getListaAccionistasByOrdenPorc(Long.valueOf(programaId),codEmpresaGrupo);			
				if (listaAccionistaAll!=null && listaAccionistaAll.size()>0)   {
					Collections.sort(listaAccionistaAll,Collections.reverseOrder());
				}
				List<Accionista> listaresumen = new ArrayList<Accionista>();
				float totalacciones = 0;	
				float totalaccioneslimite = 0;
				for(Accionista acci : listaAccionistaAll){		
					totalaccioneslimite += Float.parseFloat(acci.getPorcentaje());				
					if (totalaccioneslimite>porcentajeMinimo){
						totalacciones += Float.parseFloat(acci.getPorcentaje());
						listaresumen.add(acci);
						break;					
					}else{	
					totalacciones += Float.parseFloat(acci.getPorcentaje());
					listaresumen.add(acci);
					}
				}
				listaAccionistas=listaresumen;			
				totalAccionista = ""+FormatUtil.round(totalacciones, 2);
				
				
				String tipo_empresa = programa.getTipoEmpresa().getId().toString(); 
				
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
						Empresa empresa= new Empresa();	
						empresa=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
						
						if (empresa!=null && !empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
								
							DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
							   if (odatosBasico!=null ){								   						   
								   getPrograma().setActividadPrincipal(odatosBasico.getActividadPrincipal());
								   getPrograma().setAntiguedadNegocio(odatosBasico.getAntiguedadNegocio());
								   getPrograma().setAntiguedadCliente(odatosBasico.getAntiguedadCliente());	
								   getPrograma().setGrupoRiesgoBuro(odatosBasico.getGrupoRiesgoBuro());
							   }
						}	
				}				
				
				String stractividadPrin=getPrograma().getActividadPrincipal()==null?"":getPrograma().getActividadPrincipal().toString();
				String strantiguedadNegocio=getPrograma().getAntiguedadNegocio()==null?"":getPrograma().getAntiguedadNegocio().toString();
				String strantiguedadCliente=getPrograma().getAntiguedadCliente()==null?"":getPrograma().getAntiguedadCliente().toString();
								
				String cuotaFinanciera="";//getPrograma().getCuotaFinanciera()==null?"":getPrograma().getCuotaFinanciera().toString();
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraEmpresa(programa)+"";					
				}else{
					List<Empresa>  listaEmpersas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraGrupo(programa, listaEmpersas)+"";
				}
				String boureau = getPrograma().getGrupoRiesgoBuro()==null?"":getPrograma().getGrupoRiesgoBuro();
				Empresa empresa= new Empresa();	
				empresa=programaBO.findEmpresaByIdEmpresaPrograma(Long.valueOf(programaId),codEmpresaGrupo);
				String deudaSBS = loadDeudaSBSByPrograma(programaId,empresa,programa.getTipoEmpresa().getId().toString());
				
			 	StringBuilder stbComenDatosBasicosAdicional = new StringBuilder(); 		
			 	StringBuilder stbComenAnalisisSituacionEconFin = new StringBuilder(); 			 	
			 	
	
		        
		        if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				 	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(getPrograma());
			        if (programaBlob!=null){

				    	if (programaBlob.getDatosBasicosaddRDC()!=null){
					        for(byte x :programaBlob.getDatosBasicosaddRDC()){
					        	stbComenDatosBasicosAdicional.append((char)FormatUtil.getCharUTF(x));					          	
					        }
				    	}	
				    	if (programaBlob.getComenSintesisEconFinaddRDC()!=null){
					        for(byte x :programaBlob.getComenSintesisEconFinaddRDC()){
					        	stbComenAnalisisSituacionEconFin.append((char)FormatUtil.getCharUTF(x));					          	
					        }
				    	}	
				    	
			        }			        
		        
		        }else {
						Empresa empresa1= new Empresa();	
						empresa1=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
						stbComenDatosBasicosAdicional = new StringBuilder(); 		
					 	stbComenAnalisisSituacionEconFin = new StringBuilder(); 
					 	
						if (empresa!=null && empresa1.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
								
						 	ProgramaBlob programaBlobp = programaBlobBO.findBlobByPrograma(getPrograma());
					        if (programaBlobp!=null){

						    	if (programaBlobp.getDatosBasicosaddRDC()!=null){
							        for(byte x :programaBlobp.getDatosBasicosaddRDC()){
							        	stbComenDatosBasicosAdicional.append((char)FormatUtil.getCharUTF(x));					          	
							        }
						    	}	
					        }	
							
					        
						}else{

							DatosBasicoBlob datosBasicoBlob1 = datosBasicosBlobBO.findDatosBasicoBlobByPrograma(getPrograma(),codEmpresaGrupo);
					        if (datosBasicoBlob1!=null){
	
						    	if (datosBasicoBlob1.getDatosBasicosaddRDC()!=null){
							        for(byte x :datosBasicoBlob1.getDatosBasicosaddRDC()){
							        	stbComenDatosBasicosAdicional.append((char)FormatUtil.getCharUTF(x));					          	
							        }
						    	}	
					        }
							
						}	
						
				        SintesisEconomicoBlob sintesisEconomicoBlob1 = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(getPrograma(),codEmpresaGrupo);
						if(sintesisEconomicoBlob1 !=null){
							if (sintesisEconomicoBlob1.getComenSintesisEconFinaddRDC()!=null){
						        for(byte x :sintesisEconomicoBlob1.getComenSintesisEconFinaddRDC()){
						        	stbComenAnalisisSituacionEconFin.append((char)FormatUtil.getCharUTF(x));					          	
						        }
					    	}	
						}						
				 	}				 	
		        
		        stbComenDatosBasicosAdicional=obtenerCadenaHTMLValidada(stbComenDatosBasicosAdicional); 
		        stbComenAnalisisSituacionEconFin=obtenerCadenaHTMLValidada(stbComenAnalisisSituacionEconFin);
				
				//sb.append("<h4><b><u>2.-Cuadro de Garantia</u></b></h4>");					
						//sb.append("<br/>");	
							
				StringBuilder sbpr=new StringBuilder ();	
				
				sbpr.append("<table class=\"gridtable\">");	
						sbpr.append("<tr>");	
						 sbpr.append("<th colspan=\"2\">DATOS BASICOS</th>");
						sbpr.append("</tr>");
						sbpr.append("<tr>");
							sbpr.append("<td width=\"70%\">");
								sbpr.append("<table class=\"gridtable\">");	
									sbpr.append("<tr>");
										sbpr.append("<td>Actividad Principal</td>");
										sbpr.append("<td colspan=\"3\">" + obtenerCadenaHTMLValidada(stractividadPrin,true) + "</td>");
									sbpr.append("</tr>");
									sbpr.append("<tr>");
										sbpr.append("<td >Antigüedad Negocio(Años):</td>");
										sbpr.append("<td >" + strantiguedadNegocio + "</td>");										
										sbpr.append("<td >Antigüedad Cliente(Años):</td>");
										sbpr.append("<td >" + strantiguedadCliente + "</td>");
									sbpr.append("</tr>");
									sbpr.append("<tr>");
										sbpr.append("<td>Cuota Financiera:</td>");
										sbpr.append("<td>" + cuotaFinanciera + "</td>");
										sbpr.append("<td>Deuda SBS:</td>");
										sbpr.append("<td>" + deudaSBS + "</td>");
									sbpr.append("</tr>");
									sbpr.append("<tr>");
									sbpr.append("<td>Boureau:</td>");
										sbpr.append("<td colspan=\"3\">" + boureau + "</td>");
									sbpr.append("</tr>");
								sbpr.append("</table>");
							sbpr.append("</td >");
							sbpr.append("<td width=\"30%\">");
							//sbpr.append("<table class=\"gridtable\">");	
									//sbpr.append("<tr>");	
										//sbpr.append("<td>");																		
											sbpr.append("<table class=\"gridtable\">");
											sbpr.append("<thead>");
												sbpr.append("<tr>");
													sbpr.append("<th>&nbsp;</th>");
													sbpr.append("<th>Principales Accionistas</th>");
													sbpr.append("<th>%</th>");														
												sbpr.append("</tr>");
											sbpr.append("</thead>");	
														if (listaAccionistas != null && listaAccionistas.size() > 0) {
															for (Accionista oacc:listaAccionistas){
																String straccionista=oacc.getNombre()==null?"":oacc.getNombre().toString();
																String strporcentaje=oacc.getPorcentaje()==null?"":oacc.getPorcentaje().toString();
																
																sbpr.append("<tr>");
																sbpr.append("<td>&nbsp;</td>");
																sbpr.append("<td>" + straccionista + "</td>");	
																sbpr.append("<td align=\"right\">" + strporcentaje + "</td>");	
																sbpr.append("</tr>");
															}
														}	
														
														
														sbpr.append("<tr>");
															sbpr.append("<td>&nbsp;</td>");
															sbpr.append("<td>TOTAL:</td>");
															sbpr.append("<td align=\"right\">" + totalAccionista + "</td>");
															//sbpr.append("<td>&nbsp;</td>");
															//sbpr.append("<td>&nbsp;</td>");
														sbpr.append("</tr>");
											sbpr.append("</table>");
										//sbpr.append("</td>");
									//sbpr.append("</tr>");
								//sbpr.append("</table>");
							sbpr.append("</td>");
							sbpr.append("</tr>");
							sbpr.append("</table>");
							
								
							sbpr.append( " <table class=\"gridtable\" width=\"100%\">" 
					        		+ " <tr>" 	
					        		+ " 	<td>Comentario Datos Básico Adicional</td>"		        		
					        		+ " </tr>"  
					        		+ " <tr>" 			        		
					        		+ " 	<td>" + obtenerCadenaHTMLValidada(stbComenDatosBasicosAdicional.toString()) + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");	
								
							
							sbpr.append( " <table class=\"gridtable\" width=\"100%\">" 
					        		+ " <tr>" 	
					        		+ " 	<th colspan=\"2\">ANALISIS DE LA SITUACION ECONOMICA Y FINANCIERA</th>"		        		
					        		+ " </tr>"  
					        		+ " <tr>" 			        		
					        		+ " 	<td>" + obtenerCadenaHTMLValidada(stbComenAnalisisSituacionEconFin.toString()) + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");	
						
				
			    sb.append(sbpr.toString());							
									
				cuadroDatosbasicoadic= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return cuadroDatosbasicoadic;
		}		

		private String loadDeudaSBSByPrograma(String programaId,Empresa empresa,String tipoempresa) throws BOException {
			String deudaSBS = "";
			try {		
				
				List<List> listPoolBancarioTotal = new ArrayList<List>();
				Map<String, String> hm = new HashMap<String, String>();
				listPoolBancarioTotal = relacionesBancariasBO.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, empresa.getRuc(), Constantes.ID_TIPO_DEUDA_TOTAL,
						"999", "O",Constantes.ID_TIPO_EMPRESA_EMPR.toString(),programaId);
				
				if(listPoolBancarioTotal!=null && !listPoolBancarioTotal.isEmpty()){
	//				List lista = listPoolBancarioTotal.get(0);//Obteniendo la fila con la fecha mas reciente
	//				if(lista!=null && !lista.isEmpty()){
	//					Object sbsColumna = lista.get(lista.size()-1);
	//					hm = (HashMap<String, String>) sbsColumna;
	//					deudaSBS = FormatUtil.formatMiles(hm.get("SBS")== null?"":hm.get("SBS"));//Columna SBS
	//				}
					
					//ini MCG20130710
					deudaSBS="";
					for (Object lista : listPoolBancarioTotal.get(0)) {
						hm = (HashMap<String, String>) lista;
	
						Iterator it = hm.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry e = (Map.Entry) it.next();
							// logger.info(e.getKey() + " " +
							// e.getValue());
							
							if(e.getKey().toString().substring(0, 3).equals(Constantes.ABREV_NOMB_BANCO_SBS)){
								deudaSBS = FormatUtil.formatMiles(e.getValue()== null?"":e.getValue().toString());//Columna SBS											
							break;
							}										
												
						}
					}
					//fin MCG20130710
				}
			} catch (Exception e) {
				 
				deudaSBS="";
				logger.error(StringUtil.getStackTrace(e));
			}
			return deudaSBS;
		}
		
		public String posicionClienteGrupoeconomicoHTML(String programaId,String codEmpresaGrupo){
				String posicioncliente="";
				List<LimiteFormalizado> listLimiteFormalizado= new ArrayList<LimiteFormalizado>();		
				
				StringBuilder sb=new StringBuilder ();
				String nombre;			
				try {			
						
							setPrograma(programaBO.findById(Long.valueOf(programaId)));
							
							String tipo_empresa = programa.getTipoEmpresa().getId().toString(); 						
							nombre=programa.getNombreGrupoEmpresa();
							Empresa empresa= new Empresa();	
							empresa=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
							if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){									
									if (empresa!=null && !empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
											
										   List<ReporteCredito> listReporteCredito=reporteCreditoBO.getListaReporteCredito(getPrograma(),codEmpresaGrupo);
										   if (listReporteCredito!=null && listReporteCredito.size()>0 ){
											   
											   ReporteCredito reportCredito =listReporteCredito.get(0);	
											   getPrograma().setVulnerabilidad(reportCredito.getVulnerabilidad()==null?"":reportCredito.getVulnerabilidad().toString());
											   getPrograma().setTotalInversion(reportCredito.getTotalInversion()==null?"":reportCredito.getTotalInversion().toString());
											   getPrograma().setMontoPrestamo(reportCredito.getMontoPrestamo()==null?"":reportCredito.getMontoPrestamo().toString());
											   getPrograma().setEntorno(reportCredito.getEntorno()==null?"":reportCredito.getEntorno().toString());
											   getPrograma().setPoblacionAfectada(reportCredito.getPoblacionAfectada()==null?"":reportCredito.getPoblacionAfectada().toString());
											   getPrograma().setCategorizacionAmbiental(reportCredito.getCategorizacionAmbiental()==null?"":reportCredito.getCategorizacionAmbiental().toString());
											   getPrograma().setComentarioAdmision(reportCredito.getComentarioAdmision()==null?"":reportCredito.getComentarioAdmision().toString());
											   						   
										   }else{											   
											   getPrograma().setVulnerabilidad("");
											   getPrograma().setTotalInversion("");
											   getPrograma().setMontoPrestamo("");
											   getPrograma().setEntorno("");
											   getPrograma().setPoblacionAfectada("");
											   getPrograma().setCategorizacionAmbiental("");
											   getPrograma().setComentarioAdmision("");
										   }
									}						
									
							}										
							
							String strvulnerabilidadRDC=programa.getVulnerabilidad()==null?"":programa.getVulnerabilidad().toString();
							
							String strtotalInversionRDC=programa.getTotalInversion()==null?"":programa.getTotalInversion().toString();
							String strmontoPrestamoRDC=programa.getMontoPrestamo()==null?"":programa.getMontoPrestamo().toString();
														
							String strentornoRDC=programa.getEntorno()==null?"":programa.getEntorno().toString();
							String strpoblacionAfectadaRDC=programa.getPoblacionAfectada()==null?"":programa.getPoblacionAfectada().toString();
														
							String strcategorizacionAmbientalRDC=programa.getCategorizacionAmbiental()==null?"":programa.getCategorizacionAmbiental().toString();
							Long idtipoMil=getPrograma().getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILESDEFAULT:getPrograma().getTipoMilesPLR();
							Tabla otipoMil= tablaBO.obtienePorId(idtipoMil);
							String strTipoMilesPR=otipoMil.getDescripcion();
							String strcomentarioAdmision=programa.getComentarioAdmision()==null?"":programa.getComentarioAdmision().toString();
								
								
							 String riesgovigenteRDC="";
							 String incrementoRDC="";
							 String riesgopropuestoRDC="";
							
							float riesgoVigente = 0;
							float riesgoPropuesto = 0;
							float incremento;
				
							
							String nombreEmpresa=empresa.getNombre()==null?"":empresa.getNombre().trim();
							//se cambio riesgo actual a limite autorizado
							Anexo oanexo=new Anexo();
							oanexo=anexoBO.findValorByColumnaAnexosByEmpresa(Long.valueOf(programaId),nombreEmpresa);
							//riesgopropuestoRDC = anexoBO.findValorByColumnaAnexosByEmpresa(Long.valueOf(programaId),nombreEmpresa, Constantes.COLUMN_RIESGO_PROPUESTO);
							
							if (oanexo!=null){
								riesgovigenteRDC = oanexo.getLteAutorizado()==null?"":oanexo.getLteAutorizado();//Constantes.COLUMN_LIMITE_AUTORIZADO
								riesgopropuestoRDC=oanexo.getRgoPropBbvaBc()==null?"":oanexo.getRgoPropBbvaBc();//Constantes.COLUMN_RIESGO_PROPUESTO									
							}
							
							try{
								if(riesgovigenteRDC!=null && !riesgovigenteRDC.equals("")){
									riesgoVigente = Float.valueOf(riesgovigenteRDC.replace(",", ""));
								}
								if(riesgopropuestoRDC!=null && !riesgopropuestoRDC.equals("")){
									riesgoPropuesto = Float.valueOf(riesgopropuestoRDC.replace(",", ""));
								}
								incremento = riesgoPropuesto - riesgoVigente;
								incremento = FormatUtil.round(incremento, 2);
								incrementoRDC = FormatUtil.conversion(incremento);
							}catch(Exception e){logger.error(StringUtil.getStackTrace(e));}
							
								
							String strriesgovigenteRDC=riesgovigenteRDC;
							String strIncrementoRDC=incrementoRDC;
							String strRiesgopropuestoRDC=riesgopropuestoRDC;
							
							StringBuilder sbpr=new StringBuilder ();													
						//sb.append("<h4><b><u>1.-DATOS GENERALES</u></b></h4>");
						//sb.append("<br/>");					
							sbpr.append("<table class=\"gridtable\">");	
									sbpr.append("<tr>");
										sbpr.append("<th colspan=\"5\">POSICION DEL CLIENTE Y DEL GRUPO ECONOMICO:</th>");										
									sbpr.append("</tr>");
									if(!strvulnerabilidadRDC.equals("")){
									sbpr.append("<tr>");
										sbpr.append("<td >VULNERABILIDAD:</td>");
										sbpr.append("<td style='text-align:left' colspan=\"4\">" + obtenerCadenaHTMLValidada(strvulnerabilidadRDC,true) + "</td>");
									sbpr.append("</tr>");
									}
									if(!strtotalInversionRDC.equals("") || !strmontoPrestamoRDC.equals("")
											|| !strmontoPrestamoRDC.equals("") || !strpoblacionAfectadaRDC.equals("")
											|| !strcategorizacionAmbientalRDC.equals("")){
									sbpr.append("<tr>");										
										sbpr.append("<td colspan=\"5\">IMPACTO AMBIENTAL:</td>");								
									sbpr.append("</tr>");
									sbpr.append("<tr>");										
										sbpr.append("<td></td>");
											if(!strtotalInversionRDC.equals("")){
										sbpr.append("<td >Total Inversión S/.:</td>");
										sbpr.append("<td align=\"right\">" + strtotalInversionRDC + "</td>");
											}
											if(!strmontoPrestamoRDC.equals("")){
										sbpr.append("<td >Monto Préstamo S/.:</td>");
										sbpr.append("<td align=\"right\">" + strmontoPrestamoRDC + "</td>");
											}
									sbpr.append("</tr>");
									sbpr.append("<tr>");										
										sbpr.append("<td></td>");
											if(!strentornoRDC.equals("")){
										sbpr.append("<td >Entorno(km):</td>");
										sbpr.append("<td align=\"right\">" + strentornoRDC + "</td>");
											}
											if(!strpoblacionAfectadaRDC.equals("")){
										sbpr.append("<td >Población Afectada(Hab):</td>");
										sbpr.append("<td align=\"right\">" + strpoblacionAfectadaRDC + "</td>");
											}
									sbpr.append("</tr>");
									sbpr.append("<tr>");										
										sbpr.append("<td></td>");
											if(!strcategorizacionAmbientalRDC.equals("")){
										sbpr.append("<td >Categorización Ambiental:</td>");
										sbpr.append("<td colspan=\"3\">" + strcategorizacionAmbientalRDC + "</td>");
											}
									sbpr.append("</tr>");
									}
									
									sbpr.append("<tr>");										
										sbpr.append("<td colspan=\"2\">RESPONSABILIDAD TOTAL GRUPAL</td>");
										sbpr.append("<td colspan=\"3\">OFICINA</td>");
									sbpr.append("</tr>");
									sbpr.append("<tr>");																				
										sbpr.append("<td align=\"center\" colspan=\"2\">EN MILES DE USD</td>");
										sbpr.append("<td align=\"center\" >Gerente</td>");
										sbpr.append("<td align=\"center\" >Gestor</td>");
										sbpr.append("<td align=\"center\" >Sub-Gerente</td>");								
									sbpr.append("</tr>");
									sbpr.append("<tr>");																		
										sbpr.append("<td colspan=\"2\">");
											sbpr.append("<table  class=\"gridtable\" style=\"width:100%;\" >");
												sbpr.append("<tr>");
													sbpr.append("<td width=\"50%\">Riesgo Vigente</td>");
													sbpr.append("<td width=\"50%\" align=\"right\">" + strriesgovigenteRDC +"</td>");
												sbpr.append("</tr>");
												sbpr.append("<tr>");
													sbpr.append("<td>Incremento</td>");
													sbpr.append("<td align=\"right\">" + strIncrementoRDC +"</td>");
												sbpr.append("</tr>");
												sbpr.append("<tr>");
													sbpr.append("<td>Riesgo Propuesto</td>");
													sbpr.append("<td align=\"right\">" + strRiesgopropuestoRDC + "</td>");
												sbpr.append("</tr>");										
											sbpr.append("</table>");									
										sbpr.append("</td>");
										sbpr.append("<td>&nbsp;</td>");								
										sbpr.append("<td>&nbsp;</td>");
										sbpr.append("<td>&nbsp;</td>");								
									sbpr.append("</tr>");							
								sbpr.append("</table>");
								sbpr.append( " <table class=\"gridtable\">"        		
						        		+ " <tr>" 
						        		+ "   <th colspan=\"8\">ANALISIS Y ADMISION DEL RIESGO</th>"			        		
						        		+ " </tr>"        
						        		+ " <tr>"         
						        		+ "   <td width=\"10\">&nbsp;</td>"
						        		+ "   <td border=\"#666666\" width=\"10\">&nbsp;</td>"
						        		+ "   <td width=\"50\">APROBADA </td>"
						        		+ "   <td border=\"#666666\" width=\"10\">&nbsp;</td>"	
						        		+ "   <td width=\"150\">DENEGADA </td>"	
						        		+ "   <td width=\"150\">&nbsp;</td>"
						        		+ "   <td width=\"50\">FECHA </td>"
						        		+ "   <td border=\"#666666\" width=\"100\">&nbsp;</td>"		
						        		+ " </tr>"        
						        		+ " <tr>" 
						        		+ "   <td height=\"65\">&nbsp;</td>"
						        		+ "   <td>&nbsp;</td>"
						        		+ "   <td>&nbsp;</td>"
						        		+ "   <td>&nbsp;</td>"	
						        		+ "   <td valign=\"bottom\">Firma y Sello</td>"						        		
						        		+ "   <td valign=\"bottom\">Firma y Sello</td>"
						        		+ "   <td>&nbsp;</td>"	
						        		+ "   <td>&nbsp;</td>"		
						        		+ " </tr>"		
						        		+ " <tr>" 
						        		+ "   <td>&nbsp;</td>"
						        		+ "   <td>&nbsp;</td>"
						        		+ "   <td>Area de Riesgo</td>"
						        		+ "   <td>&nbsp;</td>"	
						        		+ "   <td>&nbsp;</td>"						        		
						        		+ "   <td>&nbsp;</td>"
						        		+ "   <td>&nbsp;</td>"	
						        		+ "   <td>&nbsp;</td>"
						        		+ " </tr>"	
						        		+ " </table>");	
								sbpr.append( " <table class=\"gridtableComent\" width=\"100%\">" 
						        		+ " <tr>" 	
						        		+ " 	<th colspan=\"2\">Comentarios de Admisión</th>"		        		
						        		+ " </tr>"  
						        		+ " <tr>" 			        		
						        		+ " 	<td>" + obtenerCadenaHTMLValidada(strcomentarioAdmision) + "</td>"
						        		+ " </tr>"    
						        		+ " </table>");	
								
								
						sb.append(sbpr.toString());	
												
						posicioncliente= sb.toString();
						
					} catch (BOException e) {
						logger.error(StringUtil.getStackTrace(e));
						return "";
					}catch (Exception e) {
						logger.error(StringUtil.getStackTrace(e));
						return "";
					}
					return posicioncliente;
			}
		
		//INI MCG20130411
		public DatosBasico loadDatosBaicosByEmpresa(Programa programa,String CodEmpresa) {		
			DatosBasico odatosBasicos=new DatosBasico();
			try{
					
				String clasificacionBanco="";	
				String gestor="";
				String oficina="";
				String segmento="";	
				
				String actividadPrincipal="";
				Integer antiguedadNegocio=null;
				Integer antiguedadCliente=null;
				String  grupoRiesgoBuro=""; 
				
				List<DatosBasico> listDatosBasicos=datosBasicosBO.getListaDatosBasico(programa,CodEmpresa);
				   if (listDatosBasicos!=null && listDatosBasicos.size()>0 ){
					   DatosBasico odatosBasico =listDatosBasicos.get(0);	
					   clasificacionBanco=odatosBasico.getCalificacionBanco();
					   gestor=odatosBasico.getGestor();
					   oficina=odatosBasico.getOficina();
					   segmento=odatosBasico.getSegmento();
					   
					   actividadPrincipal=odatosBasico.getActividadPrincipal();
					   antiguedadNegocio=odatosBasico.getAntiguedadNegocio();
					   antiguedadCliente=odatosBasico.getAntiguedadCliente();
					   grupoRiesgoBuro=odatosBasico.getGrupoRiesgoBuro();
				   }
		
				odatosBasicos.setCalificacionBanco(clasificacionBanco);
				odatosBasicos.setGestor(gestor);
				odatosBasicos.setOficina(oficina);
				odatosBasicos.setSegmento(segmento);
				odatosBasicos.setActividadPrincipal(actividadPrincipal);
				odatosBasicos.setAntiguedadNegocio(antiguedadNegocio);
				odatosBasicos.setAntiguedadCliente(antiguedadCliente);
				odatosBasicos.setGrupoRiesgoBuro(grupoRiesgoBuro);
				
			}catch (Exception e) {	
				e.printStackTrace();			
			}	
			return odatosBasicos;
			
		}
		
		public DatosBasico ObtenerDatosBaicosByEmpresaHost(String CodEmpresa,DatosBasico pdatosBasicos) {		
			DatosBasico odatosBasicos=new DatosBasico();
			try{
					
				String clasificacionBanco=pdatosBasicos.getCalificacionBanco();	
				String gestor=pdatosBasicos.getGestor();
				String oficina=pdatosBasicos.getOficina();
				String segmento=pdatosBasicos.getSegmento();
				String urlRig4 = getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
				if((clasificacionBanco==null||(clasificacionBanco!=null && clasificacionBanco.equals("")))
					||(gestor==null||(gestor!=null && gestor.equals("")))
					||(oficina==null||(oficina!=null && oficina.equals("")))
					||(segmento==null||(segmento!=null && segmento.equals("")))			
					){
					HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(CodEmpresa,getConfiguracionWS(),urlRig4);					 
					String separador1="";
					String separador2="";
					String separador3=""; 
					if (datos!=null){
						if (datos.get("codRegistroGestor")!=null && !datos.get("codRegistroGestor").equals("")){separador1=" - ";}
						if (datos.get("codOficinaPrincipal")!=null && !datos.get("codOficinaPrincipal").equals("")){separador2=" - ";}
						if (datos.get("codEtiqueta")!=null && !datos.get("codEtiqueta").equals("")){separador3=" - ";}
						
						 clasificacionBanco=(datos.get("clasificacionBanco")==null?"":datos.get("clasificacionBanco"));						
						 gestor=((datos.get("codRegistroGestor")==null?"":datos.get("codRegistroGestor")) +separador1+ (datos.get("nombreGestor")==null?"":datos.get("nombreGestor")));
						 oficina=((datos.get("codOficinaPrincipal")==null?"":datos.get("codOficinaPrincipal")) +separador2+ (datos.get("descOficinaPrincipal")==null?"":datos.get("descOficinaPrincipal")) );
						 segmento=((datos.get("codEtiqueta")==null?"":datos.get("codEtiqueta"))+separador3+ (datos.get("descEtiqueta")==null?"":datos.get("descEtiqueta")));
					}else{
						clasificacionBanco="";
						gestor="";
						oficina="";
						segmento="";
					} 
					 
					
				}else{
					if (ValidateUtil.isNumeric(clasificacionBanco))	{
						HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(CodEmpresa,getConfiguracionWS(),urlRig4);
						if (datos!=null){
						clasificacionBanco=datos.get("clasificacionBanco");	
						}else{clasificacionBanco="";}			
					}	
				}				
				odatosBasicos.setCalificacionBanco(clasificacionBanco);
				odatosBasicos.setGestor(gestor);
				odatosBasicos.setOficina(oficina);
				odatosBasicos.setSegmento(segmento);
				
			}catch (Exception e) {	
				e.printStackTrace();			
			}	
			return odatosBasicos;
			
		}
		
		
		private String obtenerFecha(String strfecha){
			try {
				Calendar c = Calendar.getInstance();
				String fechaActual;
				if (strfecha==null ||strfecha.equals("")){
				
					fechaActual=FechaUtil.formatFecha(new Date(), 
	                        "dd/MM/yyyy");
				}else{
					fechaActual=strfecha;
				}
				return fechaActual;
						
			} catch (Exception e) {
				 
				return "";
			}
			
		};
		//FIN MCG20130411
		
		public String getContentDisposition() {
			return contentDisposition;
		}


		public void setContentDisposition(String contentDisposition) {
			this.contentDisposition = contentDisposition;
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
		public Long getIdPrograma() {
			return idPrograma;
		}
		public void setIdPrograma(Long idPrograma) {
			this.idPrograma = idPrograma;
		}
		public Programa getPrograma() {
			return programa;
		}
		public void setPrograma(Programa programa) {
			this.programa = programa;
		}


		public String getCodEmpresaRDC() {
			return codEmpresaRDC;
		}


		public void setCodEmpresaRDC(String codEmpresaRDC) {
			this.codEmpresaRDC = codEmpresaRDC;
		}


		
		
		
		
}
