package pe.com.bbva.iipf.pf.bo.impl;

import static pe.com.stefanini.core.host.Util.obtenerCadenaHTMLValidada;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.AnexoGarantiaBO;
import pe.com.bbva.iipf.pf.bo.ArchivoAnexoBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.DownloadPDFBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.PropuestaRiesgoBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RatingBlobBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.CabTabla;
import pe.com.bbva.iipf.pf.model.CapitalizacionBursatil;
import pe.com.bbva.iipf.pf.model.Comex;
import pe.com.bbva.iipf.pf.model.CompraVenta;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.NegocioBeneficio;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Participaciones;
import pe.com.bbva.iipf.pf.model.Planilla;
import pe.com.bbva.iipf.pf.model.PrincipalesEjecutivos;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.RatingBlob;
import pe.com.bbva.iipf.pf.model.RatingExterno;
import pe.com.bbva.iipf.pf.model.RentabilidadBEC;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.pf.model.Tcaratula;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.pf.model.TsubCaratula;
import pe.com.bbva.iipf.pf.model.Tvariable;

import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.util.FormatHTMLUtil;
import pe.com.bbva.iipf.util.GeneraTableHtml;
import pe.com.bbva.iipf.util.MergePDF;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;



@Service("downloadPDFBO")
public class DownloadPDFBOImpl extends GenericAction implements DownloadPDFBO{
	Logger logger = Logger.getLogger(this.getClass());
	

		@Resource
		private ProgramaBlobBO programaBlobBO;
		
		@Resource
		private SintesisEconomicoBlobBO  sintesisEconomicoBlobBO;	

		@Resource
		private DatosBasicosBO datosBasicosBO ;
		
		@Resource
		private ProgramaBO programaBO;
		
		@Resource
		private RatingBO ratingBO;
		
		@Resource
		private RelacionesBancariasBO relacionesBancariasBO;

		@Resource
		private PropuestaRiesgoBO propuestaRiesgoBO;
		
		@Resource
		private PoliticasRiesgoBO politicasRiesgoBO;
		
		@Resource
		ParametroBO parametroBO;
		
		@Resource
		private SintesisEconomicoBO sintesisEconomicoBO;
		
		@Resource
		private AnexoBO anexoBO;

		@Resource
		private TablaBO tablaBO;
		
		@Resource
		private DatosBasicosBlobBO  datosBasicosBlobBO;
		
		@Resource
		private EmpresaBO  empresaBO;
		
		@Resource
		private RatingBlobBO ratingBlobBO;
		
		@Resource
		private AnexoGarantiaBO anexoGarantiaBO;
		
		@Resource
		private ArchivoAnexoBO archivoAnexoBO;
		
			
	
		private String inputPath;
	    private String contentType;
		private String contentDisposition;
		private InputStream fileInputStream;
		
		private Long idPrograma;
		private Programa programa;
		//Datos Basicos
		private Planilla totalPlanilla = new Planilla();
		private Planilla planillaAdmin = new Planilla();	
		private Planilla planillaNoAdmin = new Planilla();
		private List<Accionista> listaAccionistas = new ArrayList<Accionista>();
		private List<CapitalizacionBursatil> listaCapitalizacion = new ArrayList<CapitalizacionBursatil>();
		private String totalAccionista;
		private List<PrincipalesEjecutivos> listaPrinciEjecutivos = new ArrayList<PrincipalesEjecutivos>();
		private List<Participaciones> listaParticipaciones = new ArrayList<Participaciones>();
		private List<RatingExterno> listaRatingExterno = new ArrayList<RatingExterno>();
		private List<CompraVenta> listaCompraVenta = new ArrayList<CompraVenta>();
		private CompraVenta totalImportaciones = new CompraVenta();
		private CompraVenta importacionesME = new CompraVenta();
		private CompraVenta totalExportaciones = new CompraVenta();
		private CompraVenta exportacionesME = new CompraVenta();
		private List<NegocioBeneficio> listaNBActividades = new ArrayList<NegocioBeneficio>();
		private NegocioBeneficio totalActividad = new NegocioBeneficio();
		private NegocioBeneficio totalNegocio = new NegocioBeneficio();
		private List<NegocioBeneficio> listaNBNegocio = new ArrayList<NegocioBeneficio>();	
		//Rating
		private List<Rating> listaRating = new ArrayList<Rating>(); 
		
		//relaciones bancarias
		private List<LineasRelacionBancarias> listLineasRelacionesBancarias = new ArrayList<LineasRelacionBancarias>();

		//Propuesta riesgo
		private List<EstructuraLimite> listEstructuraLimite = new ArrayList<EstructuraLimite>();
		//politica de riesgo
		List<LimiteFormalizado> listLimiteFormalizado= new ArrayList<LimiteFormalizado>();
	     
		 //Anexo
		private List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
		private List<AnexoColumna> listaColumnas = new ArrayList<AnexoColumna>();
		private FilaAnexo filaTotal = new FilaAnexo();
		

		private String codigoTipoOperacion;
		
		 
		public void ReportPDF(String IdPrograma,String rutaxhtml,String rutaPDF,String rutaPDF_Pre,String codigoTipoOperacion,String pathRutaPDF,String nombreArchivo){
			logger.info("INICIO ReportePDF");
			//getResponse().setContentType("application/pdf");
			
			String rutaPDF_Pre1=pathRutaPDF+File.separator+"pre1_"+nombreArchivo;
			String rutaPDF_Pre2=pathRutaPDF+File.separator+"pre2_"+nombreArchivo;
			
			String tipoLetra = "Arial";
			int tamanoLetra = 10;
			Parametro paramTipoLetra = null;
			Parametro paramTamanoLetra = null;
			try {
				paramTipoLetra = parametroBO
						.findByNombreParametro(Constantes.TIPO_LETRA_PF);
				paramTamanoLetra = parametroBO
						.findByNombreParametro(Constantes.TAMANO_LETRA_PF);
				if (paramTipoLetra != null) {
					tipoLetra = paramTipoLetra.getValor();
				}
				if (paramTamanoLetra != null) {
					tamanoLetra = Integer.parseInt(paramTamanoLetra.getValor());
				}
				logger.info("CREACION DEL PDF - tipoletra:" + tipoLetra);
				logger.info("CREACION DEL PDF - tamanioletra:" + tamanoLetra);
			} catch (BOException e1) {
				logger.error(StringUtil.getStackTrace(e1));
			}
	        StringBuffer buf = new StringBuffer();
	        StringBuffer bufAnexo = new StringBuffer();
//	        buf.append("<html>");
	        
	 	   String css= "<style type=\"text/css\">"+ 	
	 	   	" table.gridtable {" +
	 	    " width: 100%;" +
	 	   	" font-family: verdana,arial,sans-serif;"+
			" font-size:8px;" +
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
			" border: 0.01em solid #000000;padding: .5em .5em;"+
			" }" +
			" table.gridtable td {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #ffffff;" +	
			" border: 0.01em solid #000000;padding: .5em .5em;"+
			" }" +
			" table.gridtable td.altp {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #A8C1D5;" +	
			" border: 0.01em solid #000000;padding: .5em .5em;"+
			" }" +
			" table.gridtable td.alt1 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #A9D0F5;" +	
			" border: 0.01em solid #000000;padding: .5em .5em;"+
			" }" +
			" table.gridtable td.alt2 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #CEE3F6;" +	
			" border: 0.01em solid #000000;padding: .5em .5em;"+
			" }" +
			" table.gridtable td.alt3 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #E6E6E6;" +	
			" border: 0.01em solid #000000;padding: .5em .5em;"+
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
			" border: 0.01em solid #eee;padding: .5em .5em;"+
			" }" +
			" table.gridtableComent td {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #ffffff;" +	
			" border: 0.01em solid #eee;padding: .5em .5em;"+
			" }" +
			" table.gridtableComent td.alt1 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #A9D0F5;" +	
			" border: 0.01em solid #eee;padding: .5em .5em;"+
			" }" +
			" table.gridtableComent td.alt2 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #CEE3F6;" +	
			" border: 0.01em solid #eee;padding: .5em .5em;"+
			" }" +
			" table.gridtableComent td.alt3 {" +
			" border-width: 0.01em;" +
			" padding: 1px;" +
			" border-style: solid;" +
			" border-color: #666666;" +	
			" background-color: #E6E6E6;" +	
			" border: 0.01em solid #eee;padding: .5em .5em;"+
			" }" +	
			" body{" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:9px;" +
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
			"   PAGE-BREAK-BEFORE: always" +
			"  }" +	
			
			".banner{" +
			"	height:90px;" +
			"	width: 356px;" +		
			"}" +
			
			"td.mydtcab {" +	
			"	color: #000000;" +	
			"	border-right: 0.05em solid #000000;" +	
			"	border-bottom: 0.05em solid #000000;" +	
			"	border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			"	letter-spacing: 2px;" +	
			"	text-transform: uppercase;" +	
			"	text-align: left;" +	
			"	padding: 1px 2px 1px 2px;" +	
			"	background: #CAE8EA no-repeat;" +	
			"	}" +	
			" td.mydtString {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: left;" +
			" 	}" +
			" td.mydtStringLeftRight {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: left;" +
			" 	}" +
			" td.mydtStringLeftRightBottom {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: left;" +
			" 	}" +
			" td.mydtNumero {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtNumeroLeftRight {	" +		
			" 	border-right: 0.05em solid #000000;" +	
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtNumeroLeftRightBottom {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +			
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtNumeroCab {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #CAE8EA no-repeat;" +				
			" 	padding: 1px 2px 1px 2px;" +				
			" 	color: #000000;" +
			"   text-align: right;" +
			" 	}" +
			" td.mydtTitulo {	" +		
			" 	border-right: 0.05em solid #000000;" +				
			" 	border-bottom: 0.05em solid #000000;" +	
			"   border-top: 0.05em solid #000000;" +
			"   border-left: 0.05em solid #000000;" +
			" 	background: #fff;" +				
			" 	padding: 1px 2px 1px 2px;" +				
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
			" 	padding: 1px 2px 1px 2px;" +
			"	background: #CAE8EA no-repeat;" +
			" 	color: #000000;" +
			"   text-align: center;" +
			"   font-size: 8px;" +
			" 	}" +
			" td.myEditor div, p, span, strong{	" +		
//			"   font-size:"+tamanoLetra+"px;" +
//			"   font-family:"+tipoLetra+";" +
			"   font-size:"+tamanoLetra+"px !important;" +
			"   font-family:"+tipoLetra+" !important;" +
			" 	}"+
			" </style>";
	 	  String cssreducido= "<style type=\"text/css\">"+ 	
		   	" table.gridtablereducido {" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:9px;" +
			//" font-weight: bold;"+
			" border-collapse: collapse;" +
			" border: 1;" +
			" }" +

			" </style>";  
	 	  
	 	  String csspaginacion="<style type=\"text/css\"> "+ 
	 	 "@page { " +
//	 		 " size: 4.18in 6.88in;" +
//			 " size: 8.267in 11.692in;" +
	 		 " margin: 0.17in; " +
	 		 " -fs-flow-top: \"header\";" +
	 		 " -fs-flow-bottom: \"footer\";" +
	 		 " -fs-flow-left: \"left\";" +
	 		 " -fs-flow-right: \"right\";" +
	 		 " border: 0.01em solid #A8C1D5;" + //A8C1D5  ffffff
//	 		 " padding: 1em;" +
	 		 " }" +
//	 		 " @page land {size: landscape;}"+
//	 		 " div.landscape {page: land;}"+
	 		 " #header {" +
	 //		 " font: bold serif;" +
	 		 " position: absolute; top: 0; left: 0; " +
	 		 " -fs-move-to-flow: \"header\";" +
	 		 " }" +

	 		 " #footer {" +
//	 		 " font-size: 90%; font-style: italic; " +
	 		 " position: absolute; top: 0; left: 0;" +
	 		 " -fs-move-to-flow: \"footer\";" +
	 		 " }" +

	 		 " #pagenumber:before {" +
	 		 " content: counter(page); " +
	 		 "}" +

	 		 "#pagecount:before {" +
	 		 "content: counter(pages);  " +
	 		 "}" +

	 	 " </style>"; 
	 	  
	 	  String csscodInvisible= "<style type=\"text/css\">"+ 	
		   	" .clsinvisible {" +
			" font-family: verdana,arial,sans-serif;"+
			" font-size:6px;" +
			" 	color: #fff;" +		
			" }" +
			" </style>";  
	 	 
	  	  
		  String pageLandscape="<style type=\"text/css\">"+ 	
		 			 " @page {size: landscape;}"+
		 			 " </style>";
	 	  
	 
	 	  
	 	   try {
		 		  
		        boolean btipoGrupo=false;
		        Programa programaSE = new Programa();
	 		   try {
		 							
		 				programaSE=programaBO.findById(Long.valueOf(IdPrograma));	 		     
		 		        if(programaSE.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){ 
		 		        	btipoGrupo=true;
		 		        }
					
				} catch (Exception e) {
					 
				}
				//actualizar tipo Operacion
				try {
					programaBO.actualizarTipoOperacion(Long.valueOf(IdPrograma),Long.valueOf(codigoTipoOperacion));				
				} catch (Exception e) {
					 
					logger.error(StringUtil.getStackTrace(e));
				}
				
				
				
				
				  List<ArchivoAnexo> olistaArchivosAnexo = new ArrayList<ArchivoAnexo>();
		 		  try {
		 			olistaArchivosAnexo=archivoAnexoBO.findListaArchivos(programaSE);
		 	 		
		 		  } catch (Exception e) {
					 
		 		  }
				Tvariable oasignasalto=new Tvariable();
				oasignasalto.setDatosBasicoSLinea(false);
				oasignasalto.setSintesisEconomicoSLinea(false);
				oasignasalto.setRatingSLinea(false);          
				oasignasalto.setExtractoSLinea(false);        
				oasignasalto.setInformEmpresaSLinea(false);    
				oasignasalto.setAnalisisSectorialSLinea(false);
				oasignasalto.setRelacioBancatiasSLinea(false); 
				oasignasalto.setFactoresRiesgoSLinea(false);   
				oasignasalto.setPropuestaRiesgoSLinea(false);  
				oasignasalto.setPoliticaRiesgoSLinea(false);   
				oasignasalto.setPosicionPrincipalSLinea(false);	
				
				List<Tcaratula> listaIndiceCaratula= new ArrayList<Tcaratula>();			
				Tcaratula ocaratula=new Tcaratula();
				
			   int indcapitulo=0;
			   int indGeneral=0;
			   int totalFileArchivo=0;
			   if (olistaArchivosAnexo!=null && olistaArchivosAnexo.size()>0)  {
				   totalFileArchivo=olistaArchivosAnexo.size();
				   totalFileArchivo=totalFileArchivo+1;
			   }
	 		   String caratura=CaratulaHTML(IdPrograma,totalFileArchivo,codigoTipoOperacion);
	 		   
	 		   
	 		   //ini para grupo
	 		  String sintesisEconFinanPorEmpresaNew="";
	 		  if (btipoGrupo){
		 		  List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
				  lstEmpresas = empresaBO.listarEmpresasPorPrograma(Long.valueOf(IdPrograma));
				  StringBuilder sbGrupo=new StringBuilder ();	
				  StringBuilder sbGrupoBody=new StringBuilder ();
				  
				
				  indGeneral+=1;  
				  
				  String codigoInformGrupo=Constantes.COD_INDICE_INFORMACION_DATOS_GRUPO;
				  String tituloInformGrupo="<h4><b><u> "+indGeneral+"."+ " DATOS DE LAS EMPRESAS DEL GRUPO</u></b><span class=\"clsinvisible\"> "+ codigoInformGrupo + "</span></h4>";
				  sbGrupo.append(agregarContenido(tituloInformGrupo));			  
				   
		 		   ocaratula=new Tcaratula();
	 	 		   ocaratula.setIndice("1");
	 	 		   ocaratula.setCodTitulo(codigoInformGrupo);
	 	 		   ocaratula.setTitulo(indGeneral+".- DATOS DE LAS EMPRESAS DEL GRUPO");
	 	 		   listaIndiceCaratula.add(ocaratula);
	 	 		  			  
				  
				  for (Empresa oempresa:lstEmpresas){
					  		int indsubcapitulo=0;
					  	 	List<TsubCaratula> olistasubCaratula = new ArrayList<TsubCaratula> ();
					  	 	TsubCaratula osubcaratula=new TsubCaratula();
					  	 	
					  		indcapitulo+=1;			
							String onombreEmpresa=oempresa.getNombre().trim();	
							String strCodiEmpresa=oempresa.getCodigo().trim();
							
							String codigoTituloEmp=Constantes.COD_INDICE_INFORMACION_X_EMP+"."+ strCodiEmpresa;
							String tituloEmp="<h4><b><u> "+indGeneral+"."+ indcapitulo + ".- "+ onombreEmpresa +"</u></b><span class=\"clsinvisible\"> "+ codigoTituloEmp + "</span></h4>";
							
					 		ocaratula=new Tcaratula();
					 		ocaratula.setIndice(Constantes.IDENTIFICADOR_INFORM_X_EMPRESA);
					 		ocaratula.setTitulo("   "+indGeneral+"."+String.valueOf(indcapitulo)+".- " +onombreEmpresa);
					 		ocaratula.setCodTitulo(codigoTituloEmp);			

					 	    indsubcapitulo+=1;			 	 
				 		    osubcaratula=new TsubCaratula();
							osubcaratula.setIdSubcaratula(Long.valueOf(indsubcapitulo));
							String indDatosBasicos=indGeneral+"."+indcapitulo+"."+ indsubcapitulo;
							osubcaratula.setIndiceSubcaratula(indDatosBasicos);
							osubcaratula.setTituloSubcaratula("      "+indDatosBasicos + ".- DATOS BÁSICOS" );
							String codigoTitulo=Constantes.COD_SUBINDICE_DATOS_BASICOS+"."+strCodiEmpresa;
							osubcaratula.setCodTituloInfxEmpresa(codigoTitulo);
							olistasubCaratula.add(osubcaratula);	
							String datosBasicosEmp="";
							if (oempresa.getTipoGrupo()!=null && oempresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
								 datosBasicosEmp=DatosBasicosHTML(IdPrograma,indDatosBasicos,codigoTitulo); //Para tipo Empresa o Empresa Principal.
							}else{
								 datosBasicosEmp=DatosBasicosPorEmpresaHTML(IdPrograma,oempresa,indDatosBasicos,codigoTitulo);
							}
				 		   
				 		   indsubcapitulo+=1;
				 		   
				 		   String indSintesisEcon=indGeneral+"."+indcapitulo+"."+ indsubcapitulo;	
				 		   String codigoTituloSE=Constantes.COD_SUBINDICE_SINTESIS_ECON_FIN+"."+strCodiEmpresa;
				 		  	String sintesisEconomicaFinancieraEmp=	"";
				 		  	sintesisEconomicaFinancieraEmp=SintesisEconomicoPorEmpresaIndHTML(IdPrograma,oempresa,indSintesisEcon,oasignasalto,codigoTituloSE);

				 		   //if (oasignasalto.isSintesisEconomicoSLinea()){			 	 		   
					 		    osubcaratula=new TsubCaratula();
								osubcaratula.setIdSubcaratula(Long.valueOf(indsubcapitulo));						
								osubcaratula.setIndiceSubcaratula(indSintesisEcon);
								osubcaratula.setTituloSubcaratula("      "+indSintesisEcon + ".- SÍNTESIS ECONÓMICA-FINANCIERA");
								osubcaratula.setCodTituloInfxEmpresa(codigoTituloSE);
								olistasubCaratula.add(osubcaratula); 
								indsubcapitulo+=1;
				 		   //}		   
				 	
				 		   String indExtracto=indGeneral+"."+indcapitulo+"."+ indsubcapitulo;	
				 		   String codigoTituloEx=Constantes.COD_SUBINDICE_EXTRACTO_SINT_ECON+"."+strCodiEmpresa;
				 		   String extractoEmp="";
				 		   extractoEmp=ExtractoPorEmpresaIndHTML(IdPrograma,oempresa,indExtracto,oasignasalto,codigoTituloEx); 
			
				 		   //if (oasignasalto.isExtractoSLinea()){	 	 		   
					 		    osubcaratula=new TsubCaratula();
								osubcaratula.setIdSubcaratula(Long.valueOf(indsubcapitulo));
								osubcaratula.setIndiceSubcaratula(indExtracto);
								osubcaratula.setTituloSubcaratula("      "+indExtracto + ".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA" );
								osubcaratula.setCodTituloInfxEmpresa(codigoTituloEx);
								olistasubCaratula.add(osubcaratula);
								indsubcapitulo+=1;
				 		   //}
				 		   
				 		   String indRating=indGeneral+"."+ indcapitulo+"."+ indsubcapitulo;
				 		   String codigoTituloRa=Constantes.COD_SUBINDICE_RATING+"."+strCodiEmpresa;
				 		   String ratingEmp="";
				 		   ratingEmp=RatingPorEmpresaConSEHTML(IdPrograma,oempresa,indRating,oasignasalto,codigoTituloRa);//demas empresas			 		   
						
						   //sbGrupo.append(ratingEmp);
				 		   //if (oasignasalto.isRatingSLinea()){
			 		   		    osubcaratula=new TsubCaratula();
								osubcaratula.setIdSubcaratula(Long.valueOf(indsubcapitulo));
								osubcaratula.setIndiceSubcaratula(indRating);
								osubcaratula.setTituloSubcaratula("      "+indRating + ".- RATING" );
								osubcaratula.setCodTituloInfxEmpresa(Constantes.COD_SUBINDICE_RATING+"."+strCodiEmpresa);
								olistasubCaratula.add(osubcaratula);		 	 		   
								indsubcapitulo+=1;
				 		   //}	
				 		   
				 		  ocaratula.setListSubCaratula(olistasubCaratula);
				 		  listaIndiceCaratula.add(ocaratula);
				 		  
				 		   sbGrupo.append(agregarContenidoSinSalto(tituloEmp));
				 		   sbGrupo.append(datosBasicosEmp); 		
				 		   
				 		   	if (oasignasalto.isSintesisEconomicoSLinea()){
				 			 sbGrupo.append(agregarContenido(sintesisEconomicaFinancieraEmp));  
				 	        }else{
				 	        	sbGrupo.append(agregarContenidoSinSalto(sintesisEconomicaFinancieraEmp));  
				 	        }			 		   
				 		          
				 	        if (oasignasalto.isSintesisEconomicoSLinea() || oasignasalto.isExtractoSLinea()){
				 	        	sbGrupo.append(agregarContenido(extractoEmp));  
				 	        }else{
				 	        	sbGrupo.append(agregarContenidoSinSalto(extractoEmp));  
				 	        }
				 	        if (oasignasalto.isSintesisEconomicoSLinea() && oasignasalto.isExtractoSLinea() //|| oasignasalto.isRatingSLinea()
				 	        		){
				 	        	sbGrupo.append(agregarContenido(ratingEmp));  
				 	        }else{
				 	        	sbGrupo.append(agregarContenidoSinSalto(ratingEmp));  
				 	        }			 	      
				 		  
					}
				  	//Para Grupo
				  	//ini
				  		
				  	   int indsubcapitulo=0;
				  	   indcapitulo=indGeneral+1; 		   
			 		   indsubcapitulo+=1;
			 		   
			 		   String indSintesisEcon=indcapitulo+"";//indcapitulo+"."+ indsubcapitulo;	
			 		   String codigoTituloGrupoSE=Constantes.COD_INDICE_SINTESIS_ECON_FIN; //Constantes.COD_SUBINDICE_SINTESIS_ECON_FIN +"."+strCodiEmpresa;
			 		   String sintesisEconomicaFinancieraEmpGrupo=	"";
			 		   sintesisEconomicaFinancieraEmpGrupo=SintesisEconomicaFinancieraHTML(IdPrograma,indSintesisEcon,oasignasalto,codigoTituloGrupoSE);//Para Grupo y Empresa  segun sea el caso. 		   
					   
			 		  // if (oasignasalto.isSintesisEconomicoSLinea()){		 			   
				 	 		   ocaratula=new Tcaratula();
				 	 		   ocaratula.setIndice("2");
				 	 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_SINTESIS_ECON_FIN);
				 	 		   ocaratula.setTitulo(indSintesisEcon+".- SÍNTESIS ECONÓMICA-FINANCIERA - GRUPO");
				 	 		   listaIndiceCaratula.add(ocaratula);
				 	 		   indcapitulo+=1;			 	 		   
			 		  // }		 		   
			 		   String indExtracto=indcapitulo+""; //indcapitulo+"."+ indsubcapitulo;	
			 		   String codigoTituloGrupoEx=Constantes.COD_INDICE_EXTRACTO_SINT_ECON; //Constantes.COD_SUBINDICE_EXTRACTO_SINT_ECON+"."+strCodiEmpresa;
			 		   String extractoEmpGrupo="";
			 		   extractoEmpGrupo=ExtractoSintesisEconomicaFinancieraHTML(IdPrograma,indExtracto,oasignasalto,codigoTituloGrupoEx); //Para Grupo.
		
			 		   //if (oasignasalto.isExtractoSLinea()){	 	 		   
				 	 		ocaratula=new Tcaratula();
				 	 		ocaratula.setIndice("3");
				 	 		ocaratula.setCodTitulo(codigoTituloGrupoEx);
				 	 		ocaratula.setTitulo(indExtracto+".- EXTRACTO SÍNTESIS ECONÓMICA FINANCIERA - GRUPO");
				 	 		listaIndiceCaratula.add(ocaratula);
				 			indcapitulo+=1;			 			
			 		  // }
			 		   
			 		   
			 		   String indRating=indcapitulo+""; //indcapitulo+"."+ indsubcapitulo;
			 		   String codigoTituloGrupoRa=Constantes.COD_INDICE_RATING; //Constantes.COD_SUBINDICE_RATING+"."+strCodiEmpresa;
			 		   String ratingEmpGrupo="";
			 		   ratingEmpGrupo=RatingHTML(IdPrograma,indRating,oasignasalto,codigoTituloGrupoRa);//Para Grupo
					
			 		   //if (oasignasalto.isRatingSLinea()){
				 			   ocaratula=new Tcaratula();
				 	 		   ocaratula.setIndice("4");
				 	 		   ocaratula.setCodTitulo(codigoTituloGrupoRa);
				 	 		   ocaratula.setTitulo(indRating+".- RATING - GRUPO");
				 	 		   listaIndiceCaratula.add(ocaratula); 
				 	 		   indcapitulo+=1;						
			 		   //}	

			 	 		if (oasignasalto.isSintesisEconomicoSLinea()){
			 	 			sbGrupoBody.append(agregarContenido(sintesisEconomicaFinancieraEmpGrupo)); 
				 	    }else{
				 	        sbGrupoBody.append(agregarContenidoSinSalto(sintesisEconomicaFinancieraEmpGrupo));  
				 	    }			 	 		   
			 		  	       
			 	        if (oasignasalto.isSintesisEconomicoSLinea() || oasignasalto.isExtractoSLinea()){
			 	        	sbGrupoBody.append(agregarContenido(extractoEmpGrupo));  
			 	        }else{
			 	        	sbGrupoBody.append(agregarContenidoSinSalto(extractoEmpGrupo));  
			 	        }
				        if (oasignasalto.isSintesisEconomicoSLinea() && oasignasalto.isExtractoSLinea() //|| oasignasalto.isRatingSLinea()
				        		){
				        	sbGrupoBody.append(agregarContenido(ratingEmpGrupo));  
			 	        }else{
			 	        	sbGrupoBody.append(agregarContenidoSinSalto(ratingEmpGrupo));  
			 	        }	
				        
				        if (sbGrupoBody.toString().trim().equals("")){	
				        	indcapitulo=indcapitulo-1;
				        }else{
				        	//listaIndiceCaratula.add(ocaratula);			        	
				        	//sbGrupo.append(agregarContenido(tituloEmpGrupo)); 
				        	sbGrupo.append(sbGrupoBody.toString());
				        }			  
				  	//fin	 
				  
				  	 sintesisEconFinanPorEmpresaNew=sbGrupo.toString();
				  	
	 		   }
	 		   
	 		   //fin para grupo
	 		   
	 		 String datosBasicos="";
	 		 String sintesisEconomicaFinanciera="";
	 		 String extracto="";
	 		 String rating="";
	 		 if(!btipoGrupo){
		 		   indcapitulo+=1;
		 		   ocaratula=new Tcaratula();
		 		   ocaratula.setIndice("1");
		 		   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- DATOS BÁSICOS");
		 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_DATOS_BASICOS);
		 		   listaIndiceCaratula.add(ocaratula);
		 		    datosBasicos=DatosBasicosHTML(IdPrograma,String.valueOf(indcapitulo),Constantes.COD_INDICE_DATOS_BASICOS); //Para tipo Empresa o Empresa Principal.
		 		   
		 		   indcapitulo+=1;
		 		   sintesisEconomicaFinanciera=SintesisEconomicaFinancieraHTML(IdPrograma,String.valueOf(indcapitulo),oasignasalto,Constantes.COD_INDICE_SINTESIS_ECON_FIN);//Para Grupo y Empresa  segun sea el caso.
		 		   //if (oasignasalto.isSintesisEconomicoSLinea()){
		 	 		   ocaratula=new Tcaratula();
		 	 		   ocaratula.setIndice("2");
		 	 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_SINTESIS_ECON_FIN);
		 	 		   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- SÍNTESIS ECONÓMICA-FINANCIERA");
		 	 		   listaIndiceCaratula.add(ocaratula);
		 	 		   indcapitulo+=1;
		 		   //}	 	
		  		   
		 		    extracto=ExtractoSintesisEconomicaFinancieraHTML(IdPrograma,String.valueOf(indcapitulo),oasignasalto,Constantes.COD_INDICE_EXTRACTO_SINT_ECON); //Para Grupo y Empresa Principal segun sea el caso.
		  		   //Para Empresa Secundarias y Anexos.	
		 		   //if (oasignasalto.isExtractoSLinea()){
		 	 		   ocaratula=new Tcaratula();
		 	 		   ocaratula.setIndice("3");
		 	 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_EXTRACTO_SINT_ECON);
		 	 		   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA");
		 	 		   listaIndiceCaratula.add(ocaratula);
		 			  indcapitulo+=1;
		 		   //}
		 		   
		 		   rating=RatingHTML(IdPrograma,String.valueOf(indcapitulo),oasignasalto,Constantes.COD_INDICE_RATING);//Para Grupo y Empresa segun sea el caso.
		 		   //ini MCG20130314
		 		   //if (oasignasalto.isRatingSLinea()){
		 			   ocaratula=new Tcaratula();
		 	 		   ocaratula.setIndice("4");
		 	 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_RATING);
		 	 		   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- RATING");
		 	 		   listaIndiceCaratula.add(ocaratula); 
		 	 		   indcapitulo+=1;
		 		   //}
	 		 }else{
	 			if (!sintesisEconFinanPorEmpresaNew.equals("")){
	 				//indcapitulo+=1;
	 			}
	 		 }
	 		  
	 		   String analisisSectorial=AnalisisSectorialHTML(IdPrograma,indcapitulo,oasignasalto);
	 		   String etiquetaIndiceAS="";
	 		   if (!oasignasalto.isAnalisisSectorialSLinea()){
	 			 etiquetaIndiceAS=Constantes.MENSAJE_NO_EXISTE_INFORMACION;
	 		   }
	 	 		   ocaratula=new Tcaratula();
	 	 		   ocaratula.setIndice("6");
	 	 		   ocaratula.setCodTitulo(Constantes.COD_INDICE_ANALISIS_SECTO);
	 	 		   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- ANÁLISIS SECTORIAL "+ etiquetaIndiceAS);
	 	 		   listaIndiceCaratula.add(ocaratula);
	 			  indcapitulo+=1;  
	 		   //} 	
		 		  
	 		   ocaratula=new Tcaratula();
	 	 	   ocaratula.setIndice("7");
	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_RELACIONES_BANC);
	 	 	   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- RELACIONES BANCARIA");
	 	 	   listaIndiceCaratula.add(ocaratula);
	 		   String relacionesBancarias=RelacionesBancariasHTML(IdPrograma, indcapitulo,oasignasalto);
	 		   indcapitulo+=1;
	 		   String factoresRiesgo=FactoresRiesgoHTML(IdPrograma, indcapitulo,oasignasalto);
	 		   //if (oasignasalto.isFactoresRiesgoSLinea()){
	 	 		   ocaratula=new Tcaratula();
	 	 	 	   ocaratula.setIndice("8");
	 	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_FACTORES_RIESGO);
	 	 	 	   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- FACTORES DE RIESGO. CONCLUSIONES");
	 	 	 	   listaIndiceCaratula.add(ocaratula);
	 			   
	 			 indcapitulo+=1;  
	 		   //} 		   
	 		   String propuestaRiesgo=PropuestaRiesgoHTML(IdPrograma, indcapitulo,oasignasalto);
	 		   //if (oasignasalto.isPropuestaRiesgoSLinea()){
	 	 		   ocaratula=new Tcaratula();
	 	 	 	   ocaratula.setIndice("9");
	 	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_PROPUESTAS_RIESGO);
	 	 	 	   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- PROPUESTAS DE RIESGO");
	 	 	 	   listaIndiceCaratula.add(ocaratula);
	 			   indcapitulo+=1;
	 		   //}
	 		   String politicaRiesgo=PoliticaRiesgoHTML(IdPrograma, indcapitulo,oasignasalto);
	 		   //if (oasignasalto.isPoliticaRiesgoSLinea()){
	 	 		   ocaratula=new Tcaratula();
	 	 	 	   ocaratula.setIndice("10");
	 	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_POLITICA_RIESGO);
	 	 	 	   ocaratula.setTitulo(String.valueOf(indcapitulo)+".- POLÍTICA DE RIESGO");
	 	 	 	   listaIndiceCaratula.add(ocaratula);
	 	 	 	   indcapitulo+=1;
	 		   //}
	 		   
	 		   String anexo1=AnexoHTML(IdPrograma, indcapitulo,oasignasalto);
	 		   boolean flagAnexo = false;
	 		   if("".equals(anexo1)){
	 			  flagAnexo = true;
	 		   }
	 		   String anexo2=Anexo2HTML(IdPrograma,flagAnexo, indcapitulo);
	 		   
	 		   if (!"".equals(anexo1) ||!"".equals(anexo2)){  			   
		 		   ocaratula=new Tcaratula();
	 	 	 	   ocaratula.setIndice("11");
	 	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_POSICION_PRINC);
	 	 	 	   ocaratula.setTitulo(String.valueOf(indcapitulo)+ ".- POSICIÓN PRINCIPAL");
	 	 	 	   listaIndiceCaratula.add(ocaratula); 
	 		   }
	   	 	
	 		  if (olistaArchivosAnexo!=null && olistaArchivosAnexo.size()>0){
	 			  List<TsubCaratula> olistasubCaratula = new ArrayList<TsubCaratula> ();
		 		   ocaratula=new Tcaratula();
	 	 	 	   ocaratula.setIndice(Constantes.IDENTIFICADOR_ARCHIVO_ANEXO);
	 	 	 	   ocaratula.setCodTitulo(Constantes.COD_INDICE_ANEXOPF);
	 	 	 	   ocaratula.setTitulo("ANEXO");
	 	 	 	   
	 	 	 	   int indsubcapitulo=0;
	 	 	 	   TsubCaratula osubcaratula=new TsubCaratula();
	 	 	 	   for (ArchivoAnexo oarchivoanexo:olistaArchivosAnexo ){
	 	 	 		   String nameFile=oarchivoanexo.getNombreArchivo()==null?"":oarchivoanexo.getNombreArchivo();
	 	 	 		   String extFile=oarchivoanexo.getExtencion()==null?"":oarchivoanexo.getExtencion();
	 	 	 		   String ofile="";
	 	 	 		   indsubcapitulo+=1;
	 	 	 		   if (!nameFile.equals("")){
	 	 	 			ofile=String.valueOf(indsubcapitulo) +". " + nameFile+"."+extFile;
	 	 	 			} 	 	 		    
				 	    			 	 
			 		    osubcaratula=new TsubCaratula();
						osubcaratula.setIdSubcaratula(Long.valueOf(indsubcapitulo));					
						osubcaratula.setIndiceSubcaratula(String.valueOf(indsubcapitulo));
						osubcaratula.setTituloSubcaratula(ofile );					
						osubcaratula.setCodTituloInfxEmpresa(Constantes.COD_SUBINDICE_ARCHIVOANEXO+"."+String.valueOf(indsubcapitulo));
						olistasubCaratula.add(osubcaratula); 	 	 		   
	 	 	 	   }
	 	 	 	   ocaratula.setListSubCaratula(olistasubCaratula);
	 	 	 	   listaIndiceCaratula.add(ocaratula);
	 		  }		   
	 		   
//	        buf.append("<body>");       
	        buf.append(caratura);
	        
	        if(btipoGrupo){
	        	buf.append(agregarContenidoSinSalto(sintesisEconFinanPorEmpresaNew)); 
	        }
	        
	        if(!btipoGrupo){
		        buf.append(agregarContenido(datosBasicos)); 
		        
		        if (oasignasalto.isSintesisEconomicoSLinea() ){
		        	buf.append(agregarContenido(sintesisEconomicaFinanciera));   
		        }else{
		        	buf.append(agregarContenidoSinSalto(sintesisEconomicaFinanciera));    
		        }
		         
		        if (oasignasalto.isSintesisEconomicoSLinea() || oasignasalto.isExtractoSLinea()){
		        	buf.append(agregarContenido(extracto));  
		        }else{
		        	buf.append(agregarContenidoSinSalto(extracto));  
		        }
		        if (oasignasalto.isSintesisEconomicoSLinea() && oasignasalto.isExtractoSLinea() //|| oasignasalto.isRatingSLinea()
		        		){
		        	buf.append(agregarContenido(rating));  
		        }else{
		        	buf.append(agregarContenidoSinSalto(rating));  
		        }	       
	        }        

	        buf.append(agregarContenidoSinSalto(analisisSectorial));     	
	        buf.append(agregarContenidoSinSalto(relacionesBancarias));
	        buf.append(agregarContenidoSinSalto(factoresRiesgo));
	        buf.append(agregarContenidoSinSalto(propuestaRiesgo));
	        buf.append(agregarContenidoSinSalto(politicaRiesgo));
	        
	        
//	        if (oasignasalto.isPoliticaRiesgoSLinea()||oasignasalto.isAnexo1()){        	
	        	//bufAnexo.append("<div style=\" width: 1000px;\" class=\"landscape\">");
	        	bufAnexo.append(agregarContenidoSinSalto(anexo1));
	        	//bufAnexo.append("</div>");
//	        }else{
//	        	bufAnexo.append(agregarContenidoSinSalto(anexo1));
//	        }       
	        bufAnexo.append(agregarContenidoSinSalto(anexo2));
	      
	        logger.info("ReportePDF ini InputStream");
	        // parse our markup into an xml Document
	 	    InputStream IS=new StringBufferInputStream(buf.toString());//con.getInputStream();
		    ByteArrayOutputStream OS = new ByteArrayOutputStream();
		    Tidy T =new Tidy();
		    T.setXHTML(true);
		    T.setPrintBodyOnly(true);
		    T.setShowWarnings(false);	       
		    //T.setOutputEncoding("ISO-8859-1");
		    T.parseDOM(IS,OS); 
		    logger.info("ReportePDF fin InputStream");
		    logger.info("ReportePDF ini InputStream anexo");
		    //Para Anexos       
	 	    InputStream ISA=new StringBufferInputStream(bufAnexo.toString());//con.getInputStream();
		    ByteArrayOutputStream OSA = new ByteArrayOutputStream();
		    Tidy TA =new Tidy();
		    TA.setXHTML(true);
		    TA.setPrintBodyOnly(true);
		    TA.setShowWarnings(false);    
		    TA.parseDOM(ISA,OSA); 
		    logger.info("ReportePDF fin InputStream anexo");
		    
		    String refe="<head> " + css + cssreducido+ csspaginacion+ csscodInvisible +" </head>";
		    String refeA="<head> " + css + cssreducido+ csspaginacion+ csscodInvisible +pageLandscape+" </head>";


		    String cadenahtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
		 	   +" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
			    + "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
			   +"<html xmlns=\"http://www.w3.org/1999/xhtml\">"	    	
			    	+ refe + " <body>" + 
			    	
			    	
			        //"<div id=\"header\"></div> " +
			        //"<div id=\"footer\">  P&aacute;gina <span id=\"pagenumber\"/> de <span id=\"pagecount\"/> </div> " +
			        //"<div id=\"footer\"></div> " +
			        //OS.toString()+ 
					    "<table align=\"center\">"+
				    		"<tr>"+
				    			"<td>"+
				    			"<div style=\"width: 740px; overflow:hidden margin:0;padding:0px; \">"+//div para controlar desborde
				    			OS.toString()+
				    			"</div>"+
				    			"</td>"+
			    			"</tr>"+
						"</table>"+
			    	
			    	" </body> </html> ";
		    
		    //Para Anexos
		    String cadenahtmlAnexo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
		 	 	   +" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
		 		    + "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
		 		   +"<html xmlns=\"http://www.w3.org/1999/xhtml\">"	    	
		 		    	+ refeA + " <body>" + 
		 		    	
		 		    	
		 		        //"<div id=\"header\"></div> " +
		 		        //"<div id=\"footer\">  P&aacute;gina <span id=\"pagenumber\"/> de <span id=\"pagecount\"/> </div> " +
		 		        //"<div id=\"footer\"></div> " +
		 		        //OS.toString()+ 
		 				    "<table align=\"center\">"+
		 			    		"<tr>"+
		 			    			"<td>"+
		 			    			"<div style=\"width: 1000px; overflow:hidden margin:0;padding:0px; \">"+//div para controlar desborde
		 			    			OSA.toString()+
		 			    			"</div>"+
		 			    			"</td>"+
		 		    			"</tr>"+
		 					"</table>"+
		 		    	
		 		    	" </body> </html> ";
		    //System.out.println(cadenahtmlAnexo);
		     
		    try {
			    List<File> ofiles = new ArrayList<File>();	      
				ofiles.add(new File(rutaxhtml));         
		        ofiles.add(new File(rutaPDF));
		        ofiles.add(new File(rutaPDF_Pre));	      
		    	DeleteFilePreviosPDF(ofiles);
			} catch (Exception e) {
				logger.error("DeleteFilePreviosPDF file ini: " +StringUtil.getStackTrace(e));
			}

	    	
		    
	        //Crear un archivo xhtml
	        FileWriter fw = new FileWriter(rutaxhtml);
	        //Escribir linea en el archivo
	        BufferedWriter bw = new BufferedWriter(fw);
	        PrintWriter salida = new PrintWriter(bw);
	        salida.println(cadenahtml);
	        salida.close();
	        
	        String inputFile = rutaxhtml;
	        String url = new File(inputFile).toURI().toURL().toString();
	        //String outputFile = rutaPDF;
	        String outputFile = rutaPDF_Pre;
	        
	        logger.error("ReportePDF ini renderer");
	        OutputStream os = new FileOutputStream(rutaPDF_Pre1);  
	        try{
	            ITextRenderer renderer = new ITextRenderer(); 
	            renderer.setDocument(url);
	            //renderer.setDocumentFromString(cadenahtml);
	            renderer.layout();
	            renderer.createPDF(os);
	            os.close();
			} catch (Exception e) {			
				logger.error(StringUtil.getStackTrace(e));
	            try {
	                if (os != null){
	                	os.close();
	                    }

	            } catch (IOException ioe) {
	            	logger.error("Error renderer: "+ ioe.getMessage());
	                ioe.printStackTrace();
	                
	            }
			}
	        logger.error("ReportePDF fin renderer");
	        
	        //Para Anexo
	        logger.error("ReportePDF ini renderer anexo");
	        OutputStream osA = new FileOutputStream(rutaPDF_Pre2);  
	        try{
	        ITextRenderer rendererA = new ITextRenderer();        
	        rendererA.setDocumentFromString(cadenahtmlAnexo);
	        rendererA.layout();
	        rendererA.createPDF(osA);
	        osA.close();
	        } catch (Exception e) {			
				logger.error(StringUtil.getStackTrace(e));
	            try {
	                if (osA != null){
	                	osA.close();
	                    }

	            } catch (IOException ioe) {
	            	logger.error("Error rendererA: "+ ioe.getMessage());
	                ioe.printStackTrace();
	                
	            }
			}
	        logger.error("ReportePDF fin renderer anexo");
	        
		      try {
			      List<InputStream> pdfs = new ArrayList<InputStream>();
			      pdfs.add(new FileInputStream(rutaPDF_Pre1));
			      pdfs.add(new FileInputStream(rutaPDF_Pre2));
			      OutputStream output = new FileOutputStream(outputFile);
		      		MergePDF.concatPDFs(pdfs, output, true);
		      		
		      		
			  } catch (Exception e) {
				  logger.error("ReportePDF concatPDF" +StringUtil.getStackTrace(e));
			      e.printStackTrace();
			  }
		      
		      try {
			      List<File> ofiles2 = new ArrayList<File>(); 
					ofiles2.add(new File(rutaPDF_Pre1));
		            ofiles2.add(new File(rutaPDF_Pre2));	      
		      	 DeleteFilePreviosPDF(ofiles2);
		      } catch (Exception e) {
		    	  logger.error("DeleteFilePreviosPDF" +StringUtil.getStackTrace(e));
		      }

		      logger.error("ini LecturaPDF");
	        lecturaPDF(rutaPDF,rutaPDF_Pre,btipoGrupo,listaIndiceCaratula);  
	         logger.error("fin LecturaPDF");
	            
	        } catch (Exception e) {
	        	logger.error(StringUtil.getStackTrace(e));
	        }
	        logger.info("FIN ReportePDF");
		}
		
		private void DeleteFilePreviosPDF(List<File> ofiles){ 
			try{            
		            if (ofiles != null){
		            	for (File archivo : ofiles){
		            		if (archivo.exists()){
			            		try {
			            			archivo.delete();
								} catch (Exception e) {
									logger.error("Error eliminando: "+StringUtil.getStackTrace(e));
								}
			            		
			            	}
			            }  
		            }

				} catch (Exception ed) {
					logger.error("Error eliminando pdf previos: "+ ed.getMessage());
				}
		}
		private String agregarContenido(String contenido) {
			String salida="<div class=saltopage>&nbsp; </div>";
			if(contenido==null||contenido.equals("")){
				return "";
			}
			salida+=contenido;		
			return salida;
		}
		private String agregarContenidoSinSalto(String contenido) {
			String salida="";
			if(contenido==null||contenido.equals("")){
				return "";
			}
			salida=contenido;		
			return salida;
		}
		public String CaratulaHTML(String Idprograma,int totalFileArchivo,String codigoTipoOperacion){
			String caratula="";
			StringBuilder sb=new StringBuilder ();
			
			try {
				Programa programac =new Programa();
				programac=programaBO.findById(Long.valueOf(Idprograma));
				String strEmpresaGrupo=programac.getNombreGrupoEmpresa();
				String tituloEmpresaGrupo="EMPRESA";
				String SubtituloEmpresaGrupo="";
				
				List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
				lstEmpresas = empresaBO.listarEmpresasPorPrograma(Long.valueOf(Idprograma));
				int numeroEmpresa=lstEmpresas==null?0:lstEmpresas.size();
				
				String strTipoGrupo=programac.getTipoEmpresa().getId().toString();
				logger.info("strTipoGrupo:" + strTipoGrupo);
				if(strTipoGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					tituloEmpresaGrupo="GRUPO";
					SubtituloEmpresaGrupo="GRUPO";
					logger.info("constante:"+ Constantes.ID_TIPO_EMPRESA_GRUPO);
					}
				int anio = Integer.parseInt(programac.getAnio().toString());
						
				
				//ini MCG20130618
				String strFechaCreacion = "";
				//dd/MM/yyyy
				Calendar calendarVerif = Calendar.getInstance();							
				String strDiaVerif = "0" + (calendarVerif.get(Calendar.DATE));
				String strMesVerif = "0" + (calendarVerif.get(Calendar.MONTH) + 1);
				String strAnoVerif = "" + calendarVerif.get(Calendar.YEAR);	
				String strFechaImpresion= "";
				strFechaImpresion = (strDiaVerif.substring(strDiaVerif.length() - 2))+"/"+ 
									(strMesVerif.substring(strMesVerif.length() - 2)) +"/"+ 
									strAnoVerif.substring(0, 4) ;
							 
				
				
				Parametro parametroformatfech = parametroBO.findByNombreParametro(Constantes.FORMATFECHABD);
				String formatofechadb=parametroformatfech.getValor();
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formatofechadb);
				
				String strFecha = programac.getFechaCreacion()==null?"":programac.getFechaCreacion().toString().substring(0, 10);
				
				Long codCerrado=programac.getEstadoPrograma().getId();
				String strEstado=programac.getEstadoPrograma().getDescripcion()==null?"":programac.getEstadoPrograma().getDescripcion().toString();
				
				String strFechaModificacion="";
				
					String strFechaC=programac.getFechaModificacion()==null?"": programac.getFechaModificacion().toString().substring(0, 10);
					Date dtFechaCierre = null;
					try {
						if (strFechaC.equals("")){
							strFechaModificacion="";
						}else{
						dtFechaCierre = formatoDelTexto.parse(strFechaC);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(dtFechaCierre);
						String strDiaActual = "0" + calendar.get(Calendar.DATE);
						String strMes = "0" + (calendar.get(Calendar.MONTH) + 1);
						String strAno = "" + calendar.get(Calendar.YEAR);	
					
						strFechaModificacion=(strDiaActual.substring(strDiaActual.length() - 2))+"/"+
									   (strMes.substring(strMes.length() - 2))+"/"+
										strAno.substring(0, 4);
						}
					} catch (ParseException ex) {
						logger.error(strFecha);
						strFechaModificacion="";
						ex.printStackTrace();
					} 			   

					String strFechaCierre="";
					try {
						strFechaCierre=programac.getFechaCierre()==null?"": programac.getFechaCierre().toString().substring(0, 10);
							
					} catch (Exception e) {
						strFechaCierre="";					
					}
					String strUsuarioCierre="";
					String strUsuarioCreacion="";
					String codUsuarioCierre="";
					String codUsuarioCreacion="";
					try {
						IILDPeUsuario user = null;
						IILDPeUsuario user1 = null;
						codUsuarioCierre=programac.getCodUsuarioCierre()==null?"": programac.getCodUsuarioCierre().toString();
						user = IILDPeUsuario.recuperarUsuario(codUsuarioCierre);
						if(user != null){
							strUsuarioCierre=user.getNombreCompleto();
						}	
						codUsuarioCreacion=programac.getCodUsuarioCreacion()==null?"": programac.getCodUsuarioCreacion().toString();
						user1 = IILDPeUsuario.recuperarUsuario(codUsuarioCreacion);
						if(user1 != null){
							strUsuarioCreacion=user1.getNombreCompleto();
						}
						
					} catch (Exception e) {
						strUsuarioCierre="";
						 
					}
					
				
				Date dtFechaCreacion = null;
				try {
					if (strFecha.equals("")){
						strFechaCreacion="";
					}else{
					dtFechaCreacion = formatoDelTexto.parse(strFecha);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dtFechaCreacion);
					String strDiaActual = "0" + calendar.get(Calendar.DATE);
					String strMes = "0" + (calendar.get(Calendar.MONTH) + 1);
					String strAno = "" + calendar.get(Calendar.YEAR);	
					
					String strAnioCreacin = strAno.substring(0, 4); 
					String strMesCreacion=(strMes.substring(strMes.length() - 2));
					String sttDiaCreacion= (strDiaActual.substring(strDiaActual.length() - 2));
					strFechaCreacion=sttDiaCreacion+"/"+strMesCreacion+"/"+strAnioCreacin;
					}
				} catch (ParseException ex) {
					logger.error(strFecha);
					strFechaCreacion="";
					ex.printStackTrace();
				} 	
							   

				 
				//fin MCG20130618
				
				String anio1=String.valueOf(anio-3);
				String anio2=String.valueOf(anio-2);
				String anio3=String.valueOf(anio-1);
				String anio4=String.valueOf(anio);
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.NAMEGROUPBBVA);
				String nombreGrupobbva=parametro.getValor();
				Parametro parametro1 = parametroBO.findByNombreParametro(Constantes.NAMEGLOBAL);
				String nombreGlobal=parametro1.getValor();			
				String[] arraynombreGlobal = nombreGlobal.split(",");
				//para firma banco se utiliza la descripcion
				Parametro parametro2 = parametroBO.findByNombreParametro(Constantes.FIRMABANCO);
				String firmaBanco=parametro2.getDescripcion();			
				String[] arrayfirmaBanco = firmaBanco.split(",");
				Parametro parametro3 = parametroBO.findByNombreParametro(Constantes.PIEPAGINAPROGRAMFINANCIERO);
				String piepagina=parametro3.getDescripcion();
				
				String tipo_empresa = programac.getTipoEmpresa().getId().toString(); 
				String codEmpresaGrupo="";	
				String codEmpresaPrincipal="";
				ratingBO.setPrograma(programac);
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					codEmpresaGrupo=programac.getIdEmpresa();	
					listaRating = ratingBO.findRating(programac.getId(),codEmpresaGrupo);
				}else{ 				
					codEmpresaGrupo=programac.getIdGrupo();
					listaRating = ratingBO.findRating(programac.getId(),codEmpresaGrupo);
					if (listaRating!=null && listaRating.size()==0){
						codEmpresaPrincipal=programac.getIdEmpresa();
						listaRating = ratingBO.findRating(programac.getId(),codEmpresaPrincipal);
					}
				}	
				
				
							
				Rating oratingEM =new Rating();
				Rating oratingCAb=new Rating();			
				if (listaRating!=null && listaRating.size()>0){
					for (Rating orating: listaRating){					
						if(orating.getDescripcion()!=null && orating.getDescripcion().trim().equals(Constantes.ESCALA_MAESTRA)){
							oratingEM=orating;
							break;
						}
					}
					for (Rating orating1: listaRating){					
						if(orating1.getDescripcion()==null){						
							oratingCAb=orating1;
							break;
						}
					}
			
				}	
				
				String anio2r="";
				String anio3r="";
				String anio4r="";
				String mes2="";
				String mes3="";
				String mes4="";
				
			     //00-12-2011
					String strTotalAnio2=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().toString();
					if (!strTotalAnio2.equals("") && strTotalAnio2.length()>6){
						anio2r=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().substring(3, 7);
						mes2=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().substring(0, 2);
						mes2=FormatHTMLUtil.ObtenerNameMes(mes2);
					}			
				
					String strTotalAnio1=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().toString();
					if (!strTotalAnio1.equals("") && strTotalAnio1.length()>6){
						anio3r=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().substring(3, 7);
						mes3=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().substring(0, 2);
						mes3=FormatHTMLUtil.ObtenerNameMes(mes3);
					}
					String strTotalAnioActual=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().toString();
					if (!strTotalAnioActual.equals("") && strTotalAnioActual.length()>6){
						anio4r=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().substring(3, 7);					
						mes4=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().substring(0, 2);
						mes4=FormatHTMLUtil.ObtenerNameMes(mes4);
					}
				
				
				listLimiteFormalizado=new ArrayList<LimiteFormalizado>();			
		        //listLimiteFormalizado =politicasRiesgoBO.findLimiteFormalizadoByIdprograma(Long.valueOf(Idprograma));		
				listLimiteFormalizado =anexoBO.loadLimiteFormalizadoByAnexos(programac);	 
//		        float ta1 = 0;			
//				float ta4 = 0;			
//				float ta6 = 0;					
	//	
//							
//				for(LimiteFormalizado olf : listLimiteFormalizado){						
//					ta1 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimiteAutorizado()));
//					ta4 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getTotal()));
//					ta6 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimitePropuesto()));
//				}
//				
//				String Total1=""+FormatUtil.roundTwoDecimalsPunto(ta1);		
//				String Total4=""+FormatUtil.roundTwoDecimalsPunto(ta4);		
//				String Total6=""+FormatUtil.roundTwoDecimalsPunto(ta6);
				
				String Total1="";		
				String Total4="";		
				String Total6="";			
				if (listLimiteFormalizado!=null && listLimiteFormalizado.size()>0){				
					 Total1=""+listLimiteFormalizado.get(0).getLimiteAutorizado();		
					 Total4=""+listLimiteFormalizado.get(0).getTotal();		
					 Total6=""+listLimiteFormalizado.get(0).getLimitePropuesto();
				}
				
				String urlUploadImagenes = parametroBO.findByNombreParametro(Constantes.DIR_UPLOADFILE_URL).getValor();
				String urlLogoPeru=urlUploadImagenes+"/"+"logoperupdf.png";
				 //urlLogoPeru="http://localhost:9080/ProgramaFinanciero/image/logoperupdf.png";//borrar en produccion mcg
				
				
				String strTipoOperacion="Programa Financiero";
				
				try {
					Tabla otipoOperacion=new Tabla();
					otipoOperacion=tablaBO.obtienePorId(Long.valueOf(codigoTipoOperacion));
					strTipoOperacion=otipoOperacion.getDescripcion();
				} catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
				}
				
				sb.append("<table style=\"border:0.01em solid #D5E2EE;\" width=\"100%\">");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td></td>");
				sb.append("			<td width=\"231\"></td>");
				sb.append("			<td width=\"180\"></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td><img height=\"63\" width=\"249\" src="+ urlLogoPeru +" /></td>");
				sb.append("			<td width=\"231\"></td>");
				sb.append("			<td width=\"180\"><h3><b>"+arraynombreGlobal[0].toString()+"<br />"+arraynombreGlobal[1].toString()+"<br />"+arraynombreGlobal[2].toString()+"</b></h3></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td></td>");
				sb.append("			<td width=\"231\"></td>");
				sb.append("			<td width=\"180\"></td>");
				sb.append("			<td></td> ");
				sb.append("		</tr>");
				sb.append("</table>");
				sb.append("<br/>"); 
				sb.append("<br/>");
				
				sb.append("<table style=\"border:0.01em solid #D5E2EE;\"  width=\"100%\">");						
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><h3><b>"+tituloEmpresaGrupo+"</b></h3></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");			
				sb.append("		<tr>  ");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><h3><b>" +strEmpresaGrupo + "</b></h3></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr> ");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><h4><b>"+ strTipoOperacion+"</b></h4></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");	
				sb.append("</table>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				
				sb.append("<table class=\"gridtable\">");
				sb.append("		<tr>");
				sb.append("			<th colspan=\"4\" align=\"center\" bgcolor=\"#A8C1D5\">RATING BBVA CONTINENTAL</th>");
				sb.append("		</tr>  ");
				sb.append("		<tr>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<th align=\"center\" bgcolor=\"#5c9ccc\">"+ mes2+"-"+ anio2r +"</th> ");
				sb.append("			<th align=\"center\" bgcolor=\"#5c9ccc\">"+ mes3+"-"+ anio3r +"</th> ");
				sb.append("			<th align=\"center\" bgcolor=\"#5c9ccc\">"+ mes4+"-"+ anio4r +"</th>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"center\">" + strEmpresaGrupo + "</td>");
				sb.append("			<td align=\"center\">" + (oratingEM.getTotalAnio2()==null?"":oratingEM.getTotalAnio2())+ "</td>");
				sb.append("			<td align=\"center\">" + (oratingEM.getTotalAnio1()==null?"":oratingEM.getTotalAnio1())+ "</td>");
				sb.append("			<td align=\"center\">" + (oratingEM.getTotalAnioActual()==null?"":oratingEM.getTotalAnioActual()) + "</td>");
				sb.append("		</tr>");
				sb.append("</table>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");

				sb.append("<table class=\"gridtable\">");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<th colspan=\"2\" align=\"center\" bgcolor=\"#A8C1D5\">"+tituloEmpresaGrupo+"</th><th align=\"center\" bgcolor=\"#5c9ccc\">CE</th>");
				sb.append("		</tr>");
				sb.append("		<tr>  ");
				sb.append("			<td></td>  ");
				sb.append("			<td align=\"center\">Miles USD</td>");
				sb.append("			<td align=\"center\">Mill. EUR</td>");
				sb.append("			<td align=\"center\">Miles EUR</td>");
				sb.append("		</tr> ");
				sb.append("		<tr>");
				sb.append("			<td><b>LÍMITE AUTORIZADO</b></td>");
				sb.append("			<td align=\"center\">"+ Total1 +"</td>");
				sb.append("			<td align=\"center\"></td>");
				sb.append("			<td align=\"center\"></td> ");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td><b>LÍMITE FORMALIZADO</b></td>");
				sb.append("			<td align=\"center\">"+ Total4 +"</td>");
				sb.append("			<td align=\"center\"></td>");
				sb.append("			<td align=\"center\"></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr> ");
				sb.append("			<td><b>LÍMITE PROPUESTO</b></td>");
				sb.append("			<td align=\"center\">"+ Total6 +"</td>");
				sb.append("			<td align=\"center\"></td>");
				sb.append("			<td align=\"center\"></td> ");
				sb.append("		</tr>");
				sb.append("		<tr> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td>BBVA CONTINENTAL</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("</table>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");	
				
				sb.append("<table border=\"0\" width=\"100%\">");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b>"+arrayfirmaBanco[0]+"</b></td>");
				sb.append("			<td></td> ");
				sb.append("		</tr>");						
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b>"+arrayfirmaBanco[1]+"</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");			
				sb.append("		<tr>  ");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b>" +arrayfirmaBanco[2] + "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b>" +arrayfirmaBanco[3] + "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b> Fecha Creación: " +strFechaCreacion+ "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				if (!strUsuarioCreacion.equals("")){
					sb.append("		<tr>");
					sb.append("			<td></td>");
					sb.append("			<td align=\"center\"><b> Usuario Creación: " +strUsuarioCreacion+ "</b></td>");
					sb.append("			<td></td>");
					sb.append("		</tr>");
				}
				if (!strFechaModificacion.equals("")){
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b> Ultima Fecha Modificación: " +strFechaModificacion+ "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				}
				
				if (!strFechaCierre.equals("")){
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b> Fecha Cierre: " +strFechaCierre+ "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				}			
				
				if (!strUsuarioCierre.equals("")){
					sb.append("		<tr>");
					sb.append("			<td></td>");
					sb.append("			<td align=\"center\"><b> Usuario Cierre: " +strUsuarioCierre+ "</b></td>");
					sb.append("			<td></td>");
					sb.append("		</tr>");
					}
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b> Estado: " +strEstado+ "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr> ");
				sb.append("			<td></td>");
				sb.append("			<td align=\"center\"><b>" +arrayfirmaBanco[5] + "</b></td>");
				sb.append("			<td></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td></td> ");
				sb.append("			<td></td>");
				sb.append("		</tr>");  
				sb.append("</table>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");			
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
//				sb.append("<br/>");
//				sb.append("<br/>");
//				sb.append("<br/>");
//				sb.append("<br/>");	
//				sb.append("<br/>");
				//sb.append("<br/>");		
				sb.append("<table border=\"0\" width=\"100%\">");
				sb.append("		<tr>");
				sb.append("			<td align=\"right\"><b>Fecha Impresión: " +strFechaImpresion+ "</b></td>");
				sb.append("		</tr>"); 
	 		
				sb.append("</table>");	
				sb.append("<table style=\"border: 1px solid #eee;padding: .5em 1px;\" width=\"100%\">");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\"><b>" +piepagina+ "</b></td>");
				sb.append("		</tr>"); 		
				sb.append("</table>");
				
				int numpagInd=0;
				int totalLinea=0;
				int residuo=0;
				if (totalFileArchivo>0){
					totalFileArchivo=totalFileArchivo+1;
				}
				if(strTipoGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					totalLinea=1+(numeroEmpresa*5)+9+totalFileArchivo;
				}else{
					totalLinea=10 +totalFileArchivo;
				}
				
				if (totalLinea>47){
					residuo=totalLinea%47;
					numpagInd=new Double(totalLinea/47).intValue();
					if (residuo>0){
						numpagInd+=1;
					}
				}else{
					numpagInd=1;
				}
				
				
				
				String titulo="";
				for (int i=0; i<numpagInd;i++){
					titulo="";
					if (i==0){
						titulo="ÍNDICE";
					}
				sb.append("<div class=saltopage>&nbsp;</div>");			
				sb.append("<table border=\"0\" cellspacing=\"8\" cellpadding=\"8\" width=\"100%\">");
				sb.append("		<tr>  ");
				sb.append("			<td colspan=\"3\" align=\"center\"><h3><b><u>"+titulo+"</u></b></h3></td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td></td>");
				sb.append("			<td></td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>  ");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td></td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr> ");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>  ");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>  ");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr> ");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>    ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>  ");
				sb.append("			<td>&nbsp;</td> ");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("		<tr>");
				sb.append("			<td align=\"left\">&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("			<td>&nbsp;</td>");
				sb.append("		</tr>");
				sb.append("</table> ");
				}
				
				caratula= sb.toString();
				
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return caratula;
		}
			
		

		public String DatosBasicosHTML(String programaId,String indcapitulo,String CodigoIndice ){
			String datosbasicos="";
			StringBuilder sb=new StringBuilder ();
			String nombre="";
			String cabeceraPersonalidad="";
			String codEmpresa="";
			List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();	
			try {			
					
						setPrograma(programaBO.findById(Long.valueOf(programaId)));
						String tipo_empresa = programa.getTipoEmpresa().getId().toString();  //getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
						String idTipoPrograma =programa.getTipoPrograma().getId().toString();// getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString();
						if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
							//String grupo= programa.getNombreGrupoEmpresa(); //getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
							//nombre=grupo;
							//String empresaPrincipal= programa.getNombreGrupoEmpresa();//getObjectSession(Constantes.NOMBRE_EMPRESA_PRINCIPAL).toString();
							codEmpresa=programa.getIdEmpresa();
							Empresa empresaprin= new Empresa();	
							empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(programa.getId(),codEmpresa);
							
							if (empresaprin!=null){
								nombre=empresaprin.getNombre();
							}
					
						}else{ 
							String empresa=programa.getNombreGrupoEmpresa();//getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
							nombre=empresa;
							codEmpresa=programa.getIdEmpresa();
						}
						
						
						
						//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
						int anio = Integer.parseInt(programa.getAnio().toString());
						String anio1="12/" + String.valueOf(anio-3);
						String anio2="12/" + String.valueOf(anio-2);
						String anio3="12/" + String.valueOf(anio-1);
						String anio4="12/" + String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_PLANILLA);
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctp:listaCabTablatmp){
								if (ctp.getPosicion() == 1) {
									anio1 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion() == 2) {
									anio2 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion() == 3) {
									anio3 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion()== 4) {
									anio4 = ctp.getCabeceraTabla();
								} else {
									anio4 = ctp.getCabeceraTabla();
								}	
							}		
						}
						
						
						String anioc1="Dic-" + String.valueOf(anio-2);
						String anioc2="Dic-" + String.valueOf(anio-1);
						String anioc3="Dic-" +String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_COMPRA);
						
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctc:listaCabTablatmp){
								if (ctc.getPosicion() == 1) {
									anioc1 = ctc.getCabeceraTabla();
								} else if (ctc.getPosicion() == 2) {
									anioc2 = ctc.getCabeceraTabla();
								} else if (ctc.getPosicion() == 3) {
									anioc3 = ctc.getCabeceraTabla();							
								} else {
									anioc3 = ctc.getCabeceraTabla();
								}	
							}		
						}
						
						String aniov1="Dic-" + String.valueOf(anio-2);
						String aniov2="Dic-" + String.valueOf(anio-1);
						String aniov3="Dic-" + String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_VENTA);
						
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctv:listaCabTablatmp){
								if (ctv.getPosicion() == 1) {
									aniov1 = ctv.getCabeceraTabla();
								} else if (ctv.getPosicion() == 2) {
									aniov2 = ctv.getCabeceraTabla();
								} else if (ctv.getPosicion() == 3) {
									aniov3 = ctv.getCabeceraTabla();							
								} else {
									aniov3 = ctv.getCabeceraTabla();
								}	
							}		
						}
						
						
						String anioa1=String.valueOf(anio-2);
						String anioa2=String.valueOf(anio-1);
						String anioa3=String.valueOf(anio);
						
						String anioa4=String.valueOf(anio-2);
						String anioa5=String.valueOf(anio-1);
						String anioa6=String.valueOf(anio);
						String tituloActividad="Por Línea de Actividad";
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_ACTIVIDAD);
											
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla cta:listaCabTablatmp){
								
								if (cta.getPosicion() == 0) {
									tituloActividad = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 1) {
									anioa1 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 2) {
									anioa2 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 3) {
									anioa3 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 4) {
									anioa4 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 5) {
									anioa5 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 6) {
									anioa6 = cta.getCabeceraTabla();
								} else {
									anioa6 = cta.getCabeceraTabla();
								}	
							}		
						}
						
						String anion1=String.valueOf(anio-2);
						String anion2=String.valueOf(anio-1);
						String anion3=String.valueOf(anio);
						
						String anion4=String.valueOf(anio-2);
						String anion5=String.valueOf(anio-1);
						String anion6=String.valueOf(anio);
						String tituloNegocio="Por Línea de Negocio";
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_NEGOCIO);
											
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctn:listaCabTablatmp){
								if (ctn.getPosicion() == 0) {
									tituloNegocio = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 1) {
									anion1 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 2) {
									anion2 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 3) {
									anion3 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 4) {
									anion4 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 5) {
									anion5 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 6) {
									anion6 = ctn.getCabeceraTabla();
								} else {
									anion6 = ctn.getCabeceraTabla();
								}	
							}		
						}	
					
					datosBasicosBO.setPrograma(getPrograma());
					for(Planilla pl : datosBasicosBO.getListaPlanilla(codEmpresa)){
						if(pl.getTipoPersonal()!= null && 
						   pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_ADMINISTRATIVO)){
							setPlanillaAdmin(pl);
						}else if(pl.getTipoPersonal()!= null && 
								pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO)){
							setPlanillaNoAdmin(pl);
						}else{
							setTotalPlanilla(pl);
						}
					}
					listaAccionistas = datosBasicosBO.getListaAccionistas(codEmpresa);
					float totalacciones = 0;
					for(Accionista acci : listaAccionistas){
						totalacciones += Float.parseFloat(acci.getPorcentaje());
					}
					totalAccionista = ""+FormatUtil.round(totalacciones, 2);
					
					listaPrinciEjecutivos = datosBasicosBO.getListaPrinciEjecutivos(codEmpresa);
					listaParticipaciones = datosBasicosBO.getListaParticipaciones(codEmpresa);
					listaRatingExterno = datosBasicosBO.getListaRatingExterno(codEmpresa);
					listaCompraVenta = datosBasicosBO.getListaCompraVenta(codEmpresa);
					for(CompraVenta compraVenta :  listaCompraVenta){
						if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.IMPORTACIONES))){
							if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
								importacionesME = compraVenta;
							}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
								totalImportaciones = compraVenta;
							}
						}else if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.EXPORTACIONES))){
							if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
								exportacionesME = compraVenta;
							}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
								totalExportaciones = compraVenta;
							}
						}
					}
					listaNBActividades = datosBasicosBO.getListaNBActividades(codEmpresa);
					float ta1 = 0;
					float ta2 = 0;
					float ta3 = 0;
					float ta4 = 0;
					float ta5 = 0;
					float ta6 = 0;
					for(NegocioBeneficio nb : listaNBActividades){
						ta1 +=Float.valueOf(nb.getTotalB1()== null?"0":nb.getTotalB1());
						ta2 +=Float.valueOf(nb.getTotalB2()==null?"0":nb.getTotalB2());
						ta3 +=Float.valueOf(nb.getTotalB3()==null?"0":nb.getTotalB3());
						ta4 +=Float.valueOf(nb.getTotalI1()==null?"0":nb.getTotalI1());
						ta5 +=Float.valueOf(nb.getTotalI2()==null?"0":nb.getTotalI2());
						ta6 +=Float.valueOf(nb.getTotalI3()==null?"0":nb.getTotalI3());
					}
					totalActividad.setTotalB1(""+FormatUtil.roundTwoDecimalsPunto(ta1));
					totalActividad.setTotalB2(""+FormatUtil.roundTwoDecimalsPunto(ta2));
					totalActividad.setTotalB3(""+FormatUtil.roundTwoDecimalsPunto(ta3));
					totalActividad.setTotalI1(""+FormatUtil.roundTwoDecimalsPunto(ta4));
					totalActividad.setTotalI2(""+FormatUtil.roundTwoDecimalsPunto(ta5));
					totalActividad.setTotalI3(""+FormatUtil.roundTwoDecimalsPunto(ta6));
					listaNBNegocio = datosBasicosBO.getListaNBNegocio(codEmpresa);
					ta1 = 0;
					ta2 = 0;
					ta3 = 0;
					ta4 = 0;
					ta5 = 0;
					ta6 = 0;
					for(NegocioBeneficio nb : listaNBNegocio){
						ta1 +=Float.valueOf(nb.getTotalB1()==null?"0":nb.getTotalB1());
						ta2 +=Float.valueOf(nb.getTotalB2()==null?"0":nb.getTotalB2());
						ta3 +=Float.valueOf(nb.getTotalB3()==null?"0":nb.getTotalB3());
						ta4 +=Float.valueOf(nb.getTotalI1()==null?"0":nb.getTotalI1());
						ta5 +=Float.valueOf(nb.getTotalI2()==null?"0":nb.getTotalI2());
						ta6 +=Float.valueOf(nb.getTotalI3()==null?"0":nb.getTotalI3());
					}
					totalNegocio.setTotalB1(""+FormatUtil.roundTwoDecimalsPunto(ta1));
					totalNegocio.setTotalB2(""+FormatUtil.roundTwoDecimalsPunto(ta2));
					totalNegocio.setTotalB3(""+FormatUtil.roundTwoDecimalsPunto(ta3));
					totalNegocio.setTotalI1(""+FormatUtil.roundTwoDecimalsPunto(ta4));
					totalNegocio.setTotalI2(""+FormatUtil.roundTwoDecimalsPunto(ta5));
					totalNegocio.setTotalI3(""+FormatUtil.roundTwoDecimalsPunto(ta6));
					
					
					StringBuilder stbSintesisEmpresa = new StringBuilder(); 
			        StringBuilder stbDatosMatriz = new StringBuilder();
					StringBuilder stbEspacioLibreDB=new StringBuilder();
					StringBuilder stbComenComprVent=new StringBuilder();
					StringBuilder stbConcentracion=new StringBuilder();
					StringBuilder stbValoracion=new StringBuilder();
			    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(getPrograma());
			        if (programaBlob!=null){
//				    	if (programaBlob.getFodaFotalezas()!=null){
//						        for(byte x :programaBlob.getFodaFotalezas()){
//						          	stb.append((char)FormatUtil.getCharUTF(x));					          	
//						        }
//				        } 
				    	if (programaBlob.getSintesisEmpresa()!=null){
					        for(byte x :programaBlob.getSintesisEmpresa()){
					        	stbSintesisEmpresa.append((char)FormatUtil.getCharUTF(x));					          	
					        }
				    	}
					    if (programaBlob.getDatosMatriz()!=null){
						        for(byte x :programaBlob.getDatosMatriz()){
						        	stbDatosMatriz.append((char)FormatUtil.getCharUTF(x));					          	
						     }				        
					    } 
					    if (programaBlob.getEspacioLibre()!=null){
					        for(byte x :programaBlob.getEspacioLibre()){
					        	stbEspacioLibreDB.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (programaBlob.getComenComprasVentas()!=null){
					        for(byte x :programaBlob.getComenComprasVentas()){
					        	stbComenComprVent.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (programaBlob.getConcentracion()!=null){
					        for(byte x :programaBlob.getConcentracion()){
					        	stbConcentracion.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (programaBlob.getValoracion()!=null){
					        for(byte x :programaBlob.getValoracion()){
					        	stbValoracion.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
				    	
			        }
			        stbSintesisEmpresa 	=	obtenerCadenaHTMLValidada(stbSintesisEmpresa); 
			        stbDatosMatriz 		=	obtenerCadenaHTMLValidada(stbDatosMatriz);
					stbEspacioLibreDB	=	obtenerCadenaHTMLValidada(stbEspacioLibreDB);
					stbComenComprVent	=	obtenerCadenaHTMLValidada(stbComenComprVent);
					stbConcentracion	=	obtenerCadenaHTMLValidada(stbConcentracion);
					stbValoracion	    =	obtenerCadenaHTMLValidada(stbValoracion);
					
					String strAntiguedadNegocio=(programa.getAntiguedadNegocio()==null?"":programa.getAntiguedadNegocio().toString());
					String strAntiguedadCliente= (programa.getAntiguedadCliente()==null?"":programa.getAntiguedadCliente().toString());
					String strEtiquetaAñoNegocio="";
					String strEtiquetaAñoCliente="";
					//Se agrega el Bureau
					String strGrupoBureau = (programa.getGrupoRiesgoBuro()==null?"":programa.getGrupoRiesgoBuro());
					
					if (!strAntiguedadNegocio.equals("")){
						strEtiquetaAñoNegocio="Años";					
					}
					if (!strAntiguedadCliente.equals("")){
						strEtiquetaAñoCliente="Años";					
					}				
					
					listaCapitalizacion = datosBasicosBO.getListaCapitalizacionBursatil(codEmpresa);
					
					sb.append("<h4><b><u> "+ indcapitulo +".- DATOS BÁSICOS</u></b> <span class=\"clsinvisible\"> "+CodigoIndice+"</span></h4> ");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append( " <table class=\"gridtable\">"        		
			        		+ " <tr>" 
			        		+ "   <th colspan=\"6\">IDENTIFICACIÓN:</th>"		        		
			        		+ " </tr>"        
			        		+ " <tr>"         
			        		+ "   <td>Nombre </td>"
			        		+ "   <td colspan=\"4\">" + nombre + "</td>"
			        		+ "   <td>Buro: " + strGrupoBureau + "</td>"
			        		+ " </tr>"        
			        		+ " <tr>" 
			        		+ "   <td>Actividades principales</td>"
			        		+ "   <td colspan=\"5\">" + obtenerCadenaHTMLValidada(programa.getActividadPrincipal(),true) + "</td>"
			        		+ " </tr>"
			        		+ " <tr>" 
			        		+ "   <td>País</td>"
			        		+ "   <td>" + (programa.getPais()==null?"":programa.getPais()) + "</td>"
			        		+ "   <td colspan=\"3\">Antigüedad Negocio: " + strAntiguedadNegocio + " " + strEtiquetaAñoNegocio + "</td>"
			        		+ "   <td>Antigüedad Cliente: " + strAntiguedadCliente + " " + strEtiquetaAñoCliente + "</td>"        		
			        		+ " </tr>"  
			        		+ " </table>");	
					sb.append("<br/>");
					
					String to1=  getPlanillaAdmin().getTotal1()==null?"":getPlanillaAdmin().getTotal1().toString(); 
	        		String to2=  getPlanillaAdmin().getTotal2()==null?"":getPlanillaAdmin().getTotal2().toString() ;
	        		String to3=  getPlanillaAdmin().getTotal3()==null?"":getPlanillaAdmin().getTotal3().toString();
	        		String to4= getPlanillaAdmin().getTotal4()==null?"":getPlanillaAdmin().getTotal4().toString() ;	
	        		boolean pintapanilla=false;
	        		if (to1.length()>0 ||to2.length()>0||to3.length()>0||to4.length()>0){
	        			pintapanilla=true;
	        		}

	        		if(pintapanilla){
					sb.append( " <table class=\"gridtable\">" 	
			        		+ " 	<tr>"
			        		+ " 		<th>PLANILLA(NRO EMPLEADOS)</th>"
			        		+ " 		<th align=\"center\">"+ anio1 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio2 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio3 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio4 +"</th>"	        
			        		+ " 	</tr>"
			        		+ " 	<tr>"
			        		+ " 		<td>Planilla</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal1()==null?"":getPlanillaAdmin().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal2()==null?"":getPlanillaAdmin().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal3()==null?"":getPlanillaAdmin().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal4()==null?"":getPlanillaAdmin().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		/*
			        		+ " 	<tr>"
			        		+ " 		<td>Planilla No Administrativo </td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal1()==null?"":getPlanillaNoAdmin().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal2()==null?"":getPlanillaNoAdmin().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal3()==null?"":getPlanillaNoAdmin().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal4()==null?"":getPlanillaNoAdmin().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		+ " 	<tr>"
			        		+ " 		<td>Total </td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal1()==null?"":getTotalPlanilla().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal2()==null?"":getTotalPlanilla().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal3()==null?"":getTotalPlanilla().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal4()==null?"":getTotalPlanilla().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		*/
			        		
			        		+ " </table>");					
					sb.append("<br/>");
	        		}
					if (stbSintesisEmpresa!=null && stbSintesisEmpresa.length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<th>SÍNTESIS EMPRESA:</th>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbSintesisEmpresa.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
						sb.append("<br/>");
					}
					if (stbDatosMatriz.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>DATOS MATRIZ:</th>"		        	
				        		+ " </tr>" 
				        		+ " <tr>" 		        		
				        		+ " 	<td class=\"myEditor\">" + stbDatosMatriz.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
						
						sb.append("<br/>");
					}				

					String tableaccionista="";
					boolean blistaAcc=false;
					boolean bcomenAcc=false;
									
					if (listaAccionistas!=null && listaAccionistas.size()>0){
						blistaAcc=true;
						//si se quiero 	que se alinea a derecha reemplazar el nombre del campo por RIGHT
						String[] Formatcampos={"nombre","RIGHT","RIGHT","CENTER","capitalizacionBurs"};
						String[] campos={"nombre","edad","porcentaje","nacionalidad","capitalizacionBurs"};				
						cabeceraPersonalidad="";
						String[] cabecera={"PRINCIPALES ACCIONISTAS","EDAD / ANT.NEGOCIO ","%","NACIONALIDAD","INFORMACIÓN ADICIONAL"};
						String[] totales={"","TOTAL:",totalAccionista,"",""};
						tableaccionista =GeneraTableHtml.getTableHTML(Accionista.class,listaAccionistas,campos,Formatcampos,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabecera,totales,Constantes.ID_SIADD_TOTAL,null);
						
					}					
					String strComentAccionariadoval="";
					strComentAccionariadoval=obtenerCadenaHTMLValidada(programa.getComentAccionariado(),true);
					if (strComentAccionariadoval!=null && strComentAccionariadoval.length()>0){
						bcomenAcc=true;
					}
					if (blistaAcc  || bcomenAcc){
						//sb.append("ACCIONARIADO");
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>ACCIONARIADO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}				
					if (blistaAcc ){
						sb.append(tableaccionista);	
					}
					if ( bcomenAcc){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Accionariado:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td>" + strComentAccionariadoval + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");
					}					
					if (blistaAcc  || bcomenAcc){
					sb.append("<br/>");
					}	
									
					//ini MCG20130726
					//sb.append("CAPITALIZACION BURSATIL");			
						
					if (listaCapitalizacion != null && listaCapitalizacion.size() > 0) {
						
						sb.append(" <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>CAPITALIZACIÓN BURSATIL:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
						
						sb.append("<table class=\"gridtable\">");
						sb.append("<tr>");							
							sb.append("<th align=\"center\">DIVISA</th>");	
							sb.append("<th align=\"center\">IMPORTE</th>");	
							sb.append("<th align=\"center\">% FONDOS PROPIOS</th>");
							sb.append("<th align=\"center\">FECHA</th>");
							sb.append("<th align=\"center\">OBSERVACIÓN</th>");				
						sb.append("</tr>");	
						
						for (CapitalizacionBursatil ocapbu:listaCapitalizacion){
							sb.append("<tr>");	
							if (ocapbu.getDivisa()!=null){
								sb.append("<td align=\"left\" width=\"25\">" + (ocapbu.getDivisa().getDescripcion()==null?"":ocapbu.getDivisa().getDescripcion().toString()) + "</td>");
							}else{
								sb.append("<td align=\"left\" width=\"25\">&nbsp;</td>");
							}						
							sb.append("<td align=\"right\" width=\"90\">" + (ocapbu.getImporte()==null?"":ocapbu.getImporte()) + "</td>");
							sb.append("<td align=\"right\" width=\"90\">" + (ocapbu.getFondosPropios()==null?"":ocapbu.getFondosPropios()) + "</td>");
							sb.append("<td align=\"left\" width=\"50\">" + (ocapbu.getFecha()==null?"":ocapbu.getFecha()) + "</td>");
							sb.append("<td align=\"left\">" + (ocapbu.getObservacion()==null?"":ocapbu.getObservacion()) + "</td>");						
							sb.append("</tr>");
						}
						sb.append("</table>"); 	
						sb.append("<br/>");
					}				
					
					
				 
					
					//fin MCG20130726
					
					if(idTipoPrograma.equals(Constantes.ID_TIPO_PROGRAMA_LOCAL.toString())){
						String tableprincipalesEjec;
						if (listaPrinciEjecutivos != null && listaPrinciEjecutivos.size() > 0) {
							String[] Formatcampospe={"nombres","puesto","informacionAdcional"};
							String[] campospe={"nombres","puesto","informacionAdcional"};
							cabeceraPersonalidad="";
							String[] cabecerape={"PRINCIPALES EJECUTIVOS","PUESTO","INFORMACIÓN ADICIONAL"};
							String[] totalespe={"","",""};
							tableprincipalesEjec =GeneraTableHtml.getTableHTML(PrincipalesEjecutivos.class,listaPrinciEjecutivos,campospe,Formatcampospe,
									Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabecerape,totalespe,Constantes.ID_NOADD_TOTAL,null);
							sb.append(" <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>PRINCIPALES EJECUTIVOS:</th>"		        	
					        		+ " </tr>"  
					        		+ " </table>");							
							sb.append(tableprincipalesEjec);					
							sb.append("<br/>");
						}
					}
					
					String tableParticipaciones="";
					boolean blistaPartic=false;
					boolean bcomenPartic=false;
					if (listaParticipaciones!=null && listaParticipaciones.size()>0){
						blistaPartic=true;
						String[] Formatcamposparticipaciones={"nombre","RIGHT","consolidacion","sectorActividad"};				
						String[] camposparticipaciones={"nombre","porcentaje","consolidacion","sectorActividad"};
						cabeceraPersonalidad="";
						String[] cabeceraparticipaciones={"PRINCIPALES AFILIADAS","%","CONSOLIDACIÓN","SECTOR ACTIVIDAD"};
						String[] totalesparticipaciones={"","","",""};
						tableParticipaciones =GeneraTableHtml.getTableHTML(Participaciones.class,listaParticipaciones,camposparticipaciones,Formatcamposparticipaciones,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraparticipaciones,totalesparticipaciones,Constantes.ID_NOADD_TOTAL,null);
					}
					String strComentPartiSignificativaval="";
					strComentPartiSignificativaval=obtenerCadenaHTMLValidada(programa.getComentPartiSignificativa()==null?"":programa.getComentPartiSignificativa());
					if (strComentPartiSignificativaval!=null && strComentPartiSignificativaval.length()>0){
						bcomenPartic=true;
					}
					if (blistaPartic  || bcomenPartic){
						//sb.append("PARTICIPACIONES SIGNIFICATIVAS");
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>PARTICIPACIONES SIGNIFICATIVAS:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					if (blistaPartic ){
						sb.append(tableParticipaciones);
					}
					if ( bcomenPartic){				
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Participaciones Significativas:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td>" + strComentPartiSignificativaval + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
					}
					if (blistaPartic  || bcomenPartic){
					sb.append("<br/>");
					}
					
					
					String tableRating="";
					boolean blistaRatingEx=false;
					boolean bcomenRatingEx=false;
					if (listaRatingExterno!=null && listaRatingExterno.size()>0){
						blistaRatingEx=true;
						String[] FormatcamposRating={"companiaGrupo","agencia","cp","lp","outlook","moneda","fecha"};
						String[] camposRating={"companiaGrupo","agencia","cp","lp","outlook","moneda","fecha"};
						String[] cabeceraRating={"COMPAÑIA GRUPO","AGENCIA","C/P","L/P","OUTLOOK","MONEDA","FECHA"};
						String[] totalesRating={"","","","","","",""};
						tableRating =GeneraTableHtml.getTableHTML(RatingExterno.class,listaRatingExterno,camposRating,FormatcamposRating,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraRating,totalesRating,Constantes.ID_NOADD_TOTAL,null);
					}
					
					String strRatingExval="";
					strRatingExval=obtenerCadenaHTMLValidada(programa.getComentRatinExterno()==null?"":programa.getComentRatinExterno());
					if (strRatingExval!=null && strRatingExval.length()>0){
						bcomenRatingEx=true;
					}
					
					if (blistaRatingEx  || bcomenRatingEx){
						//sb.append("RATING EXTERNO");					
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>RATING EXTERNO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					if (blistaRatingEx ){
						sb.append(tableRating);
					}
					if ( bcomenRatingEx){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Rating Externo:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td>" + strRatingExval + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
					}
					if (blistaRatingEx  || bcomenRatingEx){
						sb.append("<br/>");
					}	
					
									
					//COMPRAS Y VENTAS
					
					boolean bcompraVenta=false;
					boolean bComenComprVent =false;
					 if (
							 (totalImportaciones.getTotal1()!=null && !totalImportaciones.getTotal1().equals(""))||
							 (totalImportaciones.getTotal2()!=null && !totalImportaciones.getTotal2().equals(""))||
							 (totalImportaciones.getTotal3()!=null && !totalImportaciones.getTotal3().equals(""))||
							 
							 (importacionesME.getTotal1()!=null && !importacionesME.getTotal1().equals(""))||
							 (importacionesME.getTotal2()!=null && !importacionesME.getTotal2().equals(""))||
							 (importacionesME.getTotal3()!=null && !importacionesME.getTotal3().equals(""))||
							 
							 (totalExportaciones.getTotal1()!=null && !totalExportaciones.getTotal1().equals(""))||
							 (totalExportaciones.getTotal2()!=null && !totalExportaciones.getTotal2().equals(""))||
							 (totalExportaciones.getTotal3()!=null && !totalExportaciones.getTotal3().equals(""))||
							 
							 (exportacionesME.getTotal1()!=null && !exportacionesME.getTotal1().equals(""))||
							 (exportacionesME.getTotal2()!=null && !exportacionesME.getTotal2().equals(""))||
							 (exportacionesME.getTotal3()!=null && !exportacionesME.getTotal3().equals(""))
							 
					 	)
					 {
						 bcompraVenta=true; 
					 }
					 
					if (stbComenComprVent.toString().length()>0){
							 bComenComprVent=true;
					}
					if (bcompraVenta  || bComenComprVent){
							//sb.append("COMPRAS Y VENTAS");
							sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>COMPRAS Y VENTAS:</th>"		        	
					        		+ " </tr>"  
					        		+ " </table>");	
					}
					if (bcompraVenta){ 	
						sb.append("<table width=\"100%\">");
						sb.append("	<tr >");
						sb.append("		<td width=\"30%\">");
						sb.append("			<table >");
						sb.append("				<tr >");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Total Importaciones</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Importaciones ME</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>(Expresado en miles de USD)</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("		<td width=\"70%\">");
						sb.append("			<table width=\"100%\" class=\"gridtable\">");
						sb.append("				<tr>");
						sb.append("					<th colspan=\"3\" align=\"center\" >");
						sb.append("						% Sobre el Total de Compras");
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr align=\"center\">");
						sb.append("					<th>");
						sb.append("						"+ anioc1);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+ anioc2);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+ anioc3);
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal1()==null?"":totalImportaciones.getTotal1()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal2()==null?"":totalImportaciones.getTotal2()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal3()==null?"":totalImportaciones.getTotal3()) + "%");
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal1()==null?"":importacionesME.getTotal1()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal2()==null?"":importacionesME.getTotal2()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal3()==null?"":importacionesME.getTotal3()));
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("	</tr>");
						sb.append("</table>");
						
						sb.append("<br/>");
						
						sb.append("<table width=\"100%\">");
						sb.append("	<tr >");
						sb.append("		<td width=\"30%\">");
						sb.append("			<table >");
						sb.append("				<tr >");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Total Exportaciones</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Exportaciones ME</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>(Expresado en miles de USD)</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("		<td width=\"70%\">");
						sb.append("			<table width=\"100%\" class=\"gridtable\">");
						sb.append("				<tr>");
						sb.append("					<th colspan=\"3\" align=\"center\" >");
						sb.append("						% Sobre el Total de Ventas");
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr align=\"center\">");
						sb.append("					<th>");
						sb.append("						"+aniov1);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+aniov2);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+aniov3);
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal1()==null?"":totalExportaciones.getTotal1()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal2()==null?"":totalExportaciones.getTotal2()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal3()==null?"":totalExportaciones.getTotal3()) + "%");
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal1()==null?"":exportacionesME.getTotal1()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal2()==null?"":exportacionesME.getTotal2()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal3()==null?"":exportacionesME.getTotal3()));
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("	</tr>");
						sb.append("</table>");
						sb.append("<br/>");
					}
					
		
					if (bComenComprVent){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Compra y Ventas:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbComenComprVent + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						sb.append("<br/>");
					}

					
					//add
					if (stbConcentracion.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 
								+ " <tr>" 	
				        		+ " 	<th>CONCENTRACIÓN:</th>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbConcentracion + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						sb.append("<br/>");
					}
					//Cifra de Negocio y Beneficio
									
					//sb.append( "CUADRO NEGOCIO BENEFICIO");
					
					boolean blistaNBNegocio=false;
					boolean blistaNBActividades=false;
					boolean bcomenNB=false;
					
					//"POR LINEA DE NEGOCIO"
					String tableCNB_pln="";
									
					if (listaNBNegocio!=null && listaNBNegocio.size()>0){
						blistaNBNegocio=true;					
						String[] cabeceraCNB_pln={tituloNegocio,anion1,anion2,anion3,anion4,anion5,anion6};
						String[] cabeceran={"DETALLE CIFRA DE NEGOCIO Y BENEFICIO","% DE INGRESOS","% DE BENEFICIOS"};//Primera fila cabecera
						String[] FormatcamposCNB_pln={"descripcion","RIGHT","RIGHT","RIGHT" ,"RIGHT","RIGHT","RIGHT"};
						String[] camposCNB_pln={"descripcion","totalI1","totalI2","totalI3" ,"totalB1","totalB2","totalB3"};				
						String[] totalesCNB_pln={"TOTAL:",totalNegocio.getTotalI1(),totalNegocio.getTotalI2(),totalNegocio.getTotalI3(),totalNegocio.getTotalB1(),totalNegocio.getTotalB2(),totalNegocio.getTotalB3()};
						tableCNB_pln =GeneraTableHtml.getTableHTML(NegocioBeneficio.class,listaNBNegocio,camposCNB_pln,FormatcamposCNB_pln,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraCNB_pln,totalesCNB_pln,Constantes.ID_SIADD_TOTAL,cabeceran);
					}
				
					
					//"POR LINEA DE ACTIVIDAD"
					String tableCNB_pla="";
					
					if (listaNBActividades!=null && listaNBActividades.size()>0){
						blistaNBActividades=true;
						String[] cabeceraCNB_pla={tituloActividad,anioa1,anioa2,anioa3,anioa4,anioa5,anioa6};
						String[] cabecera2={"DETALLE CIFRA DE NEGOCIO Y BENEFICIO","% DE INGRESOS","% DE BENEFICIOS"};//Primera fila cabecera
						String[] FormatcamposCNB_pla={"descripcion","RIGHT","RIGHT","RIGHT" ,"RIGHT","RIGHT","RIGHT"};
						String[] camposCNB_pla={"descripcion","totalI1","totalI2","totalI3" ,"totalB1","totalB2","totalB3"};				
						String[] totalesCNB_pla={"TOTAL:",totalActividad.getTotalI1(),totalActividad.getTotalI2(),totalActividad.getTotalI3(),totalActividad.getTotalB1(),totalActividad.getTotalB2(),totalActividad.getTotalB3()};
						tableCNB_pla =GeneraTableHtml.getTableHTML(NegocioBeneficio.class,listaNBActividades,camposCNB_pla,FormatcamposCNB_pla,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraCNB_pla,totalesCNB_pla,Constantes.ID_SIADD_TOTAL,cabecera2);					
					}
					
					if (stbEspacioLibreDB.toString().length()>0){
						bcomenNB=true;
					}				
					if (blistaNBNegocio  ||blistaNBActividades|| bcomenNB){					
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>CUADRO NEGOCIO BENEFICIO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					
					if (blistaNBNegocio ){
						sb.append(tableCNB_pln);
						sb.append("<br/>");
					}				
					if (blistaNBActividades ){
						sb.append(tableCNB_pla);
						sb.append("<br/>");	
					}				
					if (bcomenNB){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<td>Otros:</td>"		        	
				        		+ " </tr>" 
				        		+ " <tr>" 		        		
				        		+ " 	<td>" + stbEspacioLibreDB.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");					
						sb.append("<br/>");
					}				
					
					if (stbValoracion.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 
								+ " <tr>"
								+ " <th>VALORACIÓN GLOBAL:</th>"
								+ " </tr>"
				        		+ " <tr>" 			        		
				        		//+ " 	<td>" + obtenerCadenaHTMLValidada(programa.getComentvaloraGlobal()==null?"":programa.getComentvaloraGlobal()) + "</td>"
				        		+ " 	<td  class=\"myEditor\">" + stbValoracion + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
					}
					//sb.append("<br/>");				
					
					datosbasicos= sb.toString();
					
				} catch (BOException e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				}catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				}
				return datosbasicos;
			}
		
		//ini MCG20130318
		public String DatosBasicosPorEmpresaHTML(String programaId, Empresa oempresa,String indcapitulo,String codigoTitulo ){
			String datosbasicos="";
			StringBuilder sb=new StringBuilder ();
			String nombre="";
			String cabeceraPersonalidad="";
			String codEmpresa="";
			List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();	
			try {			
					
						setPrograma(programaBO.findById(Long.valueOf(programaId)));
						String tipo_empresa = programa.getTipoEmpresa().getId().toString();  //getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
						String idTipoPrograma =programa.getTipoPrograma().getId().toString();// getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString();
						codEmpresa=oempresa.getCodigo();
						nombre=oempresa.getNombre().trim();	
																		
						String strActividadPrincipal="";
						String strPais="";
						String strAntiguedadNegocio="";
						String strAntiguedadCliente="";
						String strGrupoRiesgoBuro="";
						
						String strComentAccionariado="";
						String strComentPartiSignificativa="";
						String strComentRatinExterno="";
						String strComentvaloraGlobal="";
						
						if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
								
							  if (oempresa.getTipoGrupo()!=null && !oempresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
								
								   List<DatosBasico> listDatosBasico=datosBasicosBO.getListaDatosBasico(codEmpresa);
								   if (listDatosBasico!=null && listDatosBasico.size()>0 ){
									   DatosBasico datosbas =listDatosBasico.get(0);
									    strActividadPrincipal=datosbas.getActividadPrincipal()==null?"":datosbas.getActividadPrincipal();
										strPais=datosbas.getPais()==null?"":datosbas.getPais();
										strAntiguedadNegocio=datosbas.getAntiguedadNegocio()==null?"":String.valueOf(datosbas.getAntiguedadNegocio());
										strAntiguedadCliente=datosbas.getAntiguedadCliente()==null?"":String.valueOf(datosbas.getAntiguedadCliente());
										strGrupoRiesgoBuro=datosbas.getGrupoRiesgoBuro()==null?"":datosbas.getGrupoRiesgoBuro();									
										
										strComentAccionariado=datosbas.getComentAccionariado()==null?"":datosbas.getComentAccionariado();
										strComentPartiSignificativa=datosbas.getComentPartiSignificativa()==null?"":datosbas.getComentPartiSignificativa();
										strComentRatinExterno=datosbas.getComentRatinExterno()==null?"":datosbas.getComentRatinExterno();
										strComentvaloraGlobal=datosbas.getComentvaloraGlobal()==null?"":datosbas.getComentvaloraGlobal();	
										
								   }
							}	
						}
						
						
						//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
						int anio = Integer.parseInt(programa.getAnio().toString());
						String anio1="12/" + String.valueOf(anio-3);
						String anio2="12/" + String.valueOf(anio-2);
						String anio3="12/" + String.valueOf(anio-1);
						String anio4="12/" + String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_PLANILLA);
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctp:listaCabTablatmp){
								if (ctp.getPosicion() == 1) {
									anio1 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion() == 2) {
									anio2 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion() == 3) {
									anio3 = ctp.getCabeceraTabla();
								} else if (ctp.getPosicion()== 4) {
									anio4 = ctp.getCabeceraTabla();
								} else {
									anio4 = ctp.getCabeceraTabla();
								}	
							}		
						}
						
						
						String anioc1="Dic-" + String.valueOf(anio-2);
						String anioc2="Dic-" + String.valueOf(anio-1);
						String anioc3="Dic-" +String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_COMPRA);
						
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctc:listaCabTablatmp){
								if (ctc.getPosicion() == 1) {
									anioc1 = ctc.getCabeceraTabla();
								} else if (ctc.getPosicion() == 2) {
									anioc2 = ctc.getCabeceraTabla();
								} else if (ctc.getPosicion() == 3) {
									anioc3 = ctc.getCabeceraTabla();							
								} else {
									anioc3 = ctc.getCabeceraTabla();
								}	
							}		
						}
						
						String aniov1="Dic-" + String.valueOf(anio-2);
						String aniov2="Dic-" + String.valueOf(anio-1);
						String aniov3="Dic-" + String.valueOf(anio);
						
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_VENTA);
						
						
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctv:listaCabTablatmp){
								if (ctv.getPosicion() == 1) {
									aniov1 = ctv.getCabeceraTabla();
								} else if (ctv.getPosicion() == 2) {
									aniov2 = ctv.getCabeceraTabla();
								} else if (ctv.getPosicion() == 3) {
									aniov3 = ctv.getCabeceraTabla();							
								} else {
									aniov3 = ctv.getCabeceraTabla();
								}	
							}		
						}
						
						
						String anioa1=String.valueOf(anio-2);
						String anioa2=String.valueOf(anio-1);
						String anioa3=String.valueOf(anio);
						
						String anioa4=String.valueOf(anio-2);
						String anioa5=String.valueOf(anio-1);
						String anioa6=String.valueOf(anio);
						String tituloActividad="Por Línea de Actividad";
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_ACTIVIDAD);
											
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla cta:listaCabTablatmp){
								if (cta.getPosicion() == 0) {
									tituloActividad = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 1) {
									anioa1 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 2) {
									anioa2 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 3) {
									anioa3 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 4) {
									anioa4 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 5) {
									anioa5 = cta.getCabeceraTabla();
								} else if (cta.getPosicion() == 6) {
									anioa6 = cta.getCabeceraTabla();
								} else {
									anioa6 = cta.getCabeceraTabla();
								}	
							}		
						}
						
						String anion1=String.valueOf(anio-2);
						String anion2=String.valueOf(anio-1);
						String anion3=String.valueOf(anio);
						
						String anion4=String.valueOf(anio-2);
						String anion5=String.valueOf(anio-1);
						String anion6=String.valueOf(anio);
						String tituloNegocio="Por Línea de Negocio";
						
						listaCabTablatmp= new ArrayList<CabTabla>();						
						listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla(  Long.valueOf(programaId),Constantes.ID_TIPO_TABLA_NEGOCIO);
											
						if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
							Collections.sort(listaCabTablatmp);
							for (CabTabla ctn:listaCabTablatmp){
								if (ctn.getPosicion() == 0) {
									tituloNegocio = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 1) {
									anion1 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 2) {
									anion2 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 3) {
									anion3 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 4) {
									anion4 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 5) {
									anion5 = ctn.getCabeceraTabla();
								} else if (ctn.getPosicion() == 6) {
									anion6 = ctn.getCabeceraTabla();
								} else {
									anion6 = ctn.getCabeceraTabla();
								}	
							}		
						}	
					
					datosBasicosBO.setPrograma(getPrograma());
					for(Planilla pl : datosBasicosBO.getListaPlanilla(codEmpresa)){
						if(pl.getTipoPersonal()!= null && 
						   pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_ADMINISTRATIVO)){
							setPlanillaAdmin(pl);
						}else if(pl.getTipoPersonal()!= null && 
								pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO)){
							setPlanillaNoAdmin(pl);
						}else{
							setTotalPlanilla(pl);
						}
					}
					listaAccionistas = datosBasicosBO.getListaAccionistas(codEmpresa);
					float totalacciones = 0;
					for(Accionista acci : listaAccionistas){
						totalacciones += Float.parseFloat(acci.getPorcentaje());
					}
					totalAccionista = ""+FormatUtil.round(totalacciones, 2);
					
					listaPrinciEjecutivos = datosBasicosBO.getListaPrinciEjecutivos(codEmpresa);
					listaParticipaciones = datosBasicosBO.getListaParticipaciones(codEmpresa);
					listaRatingExterno = datosBasicosBO.getListaRatingExterno(codEmpresa);
					listaCompraVenta = datosBasicosBO.getListaCompraVenta(codEmpresa);
					for(CompraVenta compraVenta :  listaCompraVenta){
						if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.IMPORTACIONES))){
							if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
								importacionesME = compraVenta;
							}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
								totalImportaciones = compraVenta;
							}
						}else if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.EXPORTACIONES))){
							if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
								exportacionesME = compraVenta;
							}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
								totalExportaciones = compraVenta;
							}
						}
					}
					listaNBActividades = datosBasicosBO.getListaNBActividades(codEmpresa);
					float ta1 = 0;
					float ta2 = 0;
					float ta3 = 0;
					float ta4 = 0;
					float ta5 = 0;
					float ta6 = 0;
					for(NegocioBeneficio nb : listaNBActividades){
						ta1 +=Float.valueOf(nb.getTotalB1()== null?"0":nb.getTotalB1());
						ta2 +=Float.valueOf(nb.getTotalB2()==null?"0":nb.getTotalB2());
						ta3 +=Float.valueOf(nb.getTotalB3()==null?"0":nb.getTotalB3());
						ta4 +=Float.valueOf(nb.getTotalI1()==null?"0":nb.getTotalI1());
						ta5 +=Float.valueOf(nb.getTotalI2()==null?"0":nb.getTotalI2());
						ta6 +=Float.valueOf(nb.getTotalI3()==null?"0":nb.getTotalI3());
					}
					totalActividad.setTotalB1(""+FormatUtil.roundTwoDecimalsPunto(ta1));
					totalActividad.setTotalB2(""+FormatUtil.roundTwoDecimalsPunto(ta2));
					totalActividad.setTotalB3(""+FormatUtil.roundTwoDecimalsPunto(ta3));
					totalActividad.setTotalI1(""+FormatUtil.roundTwoDecimalsPunto(ta4));
					totalActividad.setTotalI2(""+FormatUtil.roundTwoDecimalsPunto(ta5));
					totalActividad.setTotalI3(""+FormatUtil.roundTwoDecimalsPunto(ta6));
					listaNBNegocio = datosBasicosBO.getListaNBNegocio(codEmpresa);
					ta1 = 0;
					ta2 = 0;
					ta3 = 0;
					ta4 = 0;
					ta5 = 0;
					ta6 = 0;
					for(NegocioBeneficio nb : listaNBNegocio){
						ta1 +=Float.valueOf(nb.getTotalB1()==null?"0":nb.getTotalB1());
						ta2 +=Float.valueOf(nb.getTotalB2()==null?"0":nb.getTotalB2());
						ta3 +=Float.valueOf(nb.getTotalB3()==null?"0":nb.getTotalB3());
						ta4 +=Float.valueOf(nb.getTotalI1()==null?"0":nb.getTotalI1());
						ta5 +=Float.valueOf(nb.getTotalI2()==null?"0":nb.getTotalI2());
						ta6 +=Float.valueOf(nb.getTotalI3()==null?"0":nb.getTotalI3());
					}
					totalNegocio.setTotalB1(""+FormatUtil.roundTwoDecimalsPunto(ta1));
					totalNegocio.setTotalB2(""+FormatUtil.roundTwoDecimalsPunto(ta2));
					totalNegocio.setTotalB3(""+FormatUtil.roundTwoDecimalsPunto(ta3));
					totalNegocio.setTotalI1(""+FormatUtil.roundTwoDecimalsPunto(ta4));
					totalNegocio.setTotalI2(""+FormatUtil.roundTwoDecimalsPunto(ta5));
					totalNegocio.setTotalI3(""+FormatUtil.roundTwoDecimalsPunto(ta6));
					
					
					StringBuilder stbSintesisEmpresa = new StringBuilder(); 
			        StringBuilder stbDatosMatriz = new StringBuilder();
					StringBuilder stbEspacioLibreDB=new StringBuilder();
					StringBuilder stbComenComprVent=new StringBuilder();
					StringBuilder stbConcentracion=new StringBuilder();
					StringBuilder stbValoracion=new StringBuilder();
			    	//ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(getPrograma());
			    	DatosBasicoBlob datosBasicoBlob = datosBasicosBlobBO.findDatosBasicoBlobByPrograma(getPrograma(),codEmpresa);
			    	
			    	if (datosBasicoBlob!=null){
				    	if (datosBasicoBlob.getSintesisEmpresa()!=null){
					        for(byte x :datosBasicoBlob.getSintesisEmpresa()){
					        	stbSintesisEmpresa.append((char)FormatUtil.getCharUTF(x));					          	
					        }
				    	}
					    if (datosBasicoBlob.getDatosMatriz()!=null){
						        for(byte x :datosBasicoBlob.getDatosMatriz()){
						        	stbDatosMatriz.append((char)FormatUtil.getCharUTF(x));					          	
						     }				        
					    } 
					    if (datosBasicoBlob.getEspacioLibre()!=null){
					        for(byte x :datosBasicoBlob.getEspacioLibre()){
					        	stbEspacioLibreDB.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (datosBasicoBlob.getComenComprasVentas()!=null){
					        for(byte x :datosBasicoBlob.getComenComprasVentas()){
					        	stbComenComprVent.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (datosBasicoBlob.getConcentracion()!=null){
					        for(byte x :datosBasicoBlob.getConcentracion()){
					        	stbConcentracion.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    if (datosBasicoBlob.getValoracion()!=null){
					        for(byte x :datosBasicoBlob.getValoracion()){
					        	stbValoracion.append((char)FormatUtil.getCharUTF(x));					          	
					        }				        
					    } 
					    
			        }
			        stbSintesisEmpresa 	=	obtenerCadenaHTMLValidada(stbSintesisEmpresa); 
			        stbDatosMatriz 		=	obtenerCadenaHTMLValidada(stbDatosMatriz);
					stbEspacioLibreDB	=	obtenerCadenaHTMLValidada(stbEspacioLibreDB);
					stbComenComprVent	=	obtenerCadenaHTMLValidada(stbComenComprVent);
					stbConcentracion	=	obtenerCadenaHTMLValidada(stbConcentracion);
					//stbValoracion		=	obtenerCadenaHTMLValidada(stbValoracion);
					String strGrupoBureau = (programa.getGrupoRiesgoBuro()==null?"":programa.getGrupoRiesgoBuro());
					

					String strEtiquetaAñoNegocio="";
					String strEtiquetaAñoCliente="";
				
					if (!strAntiguedadNegocio.equals("")){
						strEtiquetaAñoNegocio="Años";					
					}
					if (!strAntiguedadCliente.equals("")){
						strEtiquetaAñoCliente="Años";					
					}	
					
					listaCapitalizacion = datosBasicosBO.getListaCapitalizacionBursatil(codEmpresa);
					
					sb.append("<h4><b><u>"+ indcapitulo +".- DATOS BÁSICOS - " + nombre + " </u></b><span class=\"clsinvisible\"> "+ codigoTitulo +"</span></h4>");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append( " <table class=\"gridtable\">"        		
			        		+ " <tr>" 
			        		+ "   <th colspan=\"6\">IDENTIFICACIÓN:</th>"
			        		+ " </tr>"        
			        		+ " <tr>"         
			        		+ "   <td>Nombre </td>"
			        		+ "   <td colspan=\"4\">" + nombre + "</td>"
			        		+ "   <td>Buro: " + strGrupoBureau + "</td>"
			        		+ " </tr>"        
			        		+ " <tr>" 
			        		+ "   <td>Actividades principales</td>"
			        		+ "   <td colspan=\"5\">" + obtenerCadenaHTMLValidada(strActividadPrincipal,true) + "</td>"
			        		+ " </tr>"
			        		+ " <tr>" 
			        		+ "   <td>País</td>"
			        		+ "   <td>" + strPais + "</td>"
			        		+ "   <td colspan=\"3\">Antiguedad Negocio: " + strAntiguedadNegocio + " " + strEtiquetaAñoNegocio + "</td>"
			        		+ "   <td>Antiguedad Cliente: " + strAntiguedadCliente + " " + strEtiquetaAñoCliente + "</td>"        		
			        		+ " </tr>"  
			        		+ " </table>");	
					sb.append("<br/>");
					
					String to1=  getPlanillaAdmin().getTotal1()==null?"":getPlanillaAdmin().getTotal1().toString(); 
	        		String to2=  getPlanillaAdmin().getTotal2()==null?"":getPlanillaAdmin().getTotal2().toString() ;
	        		String to3=  getPlanillaAdmin().getTotal3()==null?"":getPlanillaAdmin().getTotal3().toString();
	        		String to4= getPlanillaAdmin().getTotal4()==null?"":getPlanillaAdmin().getTotal4().toString() ;	
	        		boolean pintapanilla=false;
	        		if (to1.length()>0 ||to2.length()>0||to3.length()>0||to4.length()>0){
	        			pintapanilla=true;
	        		}

	        		if(pintapanilla){
					
					sb.append( " <table class=\"gridtable\">" 	
			        		+ " 	<tr>"
			        		+ " 		<th>PLANILLA(NRO EMPLEADOS)</th>"
			        		+ " 		<th align=\"center\">"+ anio1 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio2 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio3 +"</th>"
			        		+ " 		<th align=\"center\">"+ anio4 +"</th>"		        
			        		+ " 	</tr>"
			        		+ " 	<tr>"
			        		+ " 		<td>Planilla</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal1()==null?"":getPlanillaAdmin().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal2()==null?"":getPlanillaAdmin().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal3()==null?"":getPlanillaAdmin().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaAdmin().getTotal4()==null?"":getPlanillaAdmin().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		/*
			        		+ " 	<tr>"
			        		+ " 		<td>Planilla No Administrativo </td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal1()==null?"":getPlanillaNoAdmin().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal2()==null?"":getPlanillaNoAdmin().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal3()==null?"":getPlanillaNoAdmin().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getPlanillaNoAdmin().getTotal4()==null?"":getPlanillaNoAdmin().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		+ " 	<tr>"
			        		+ " 		<td>Total </td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal1()==null?"":getTotalPlanilla().getTotal1()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal2()==null?"":getTotalPlanilla().getTotal2()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal3()==null?"":getTotalPlanilla().getTotal3()) + "</td>"
			        		+ " 		<td align=\"center\">"+ (getTotalPlanilla().getTotal4()==null?"":getTotalPlanilla().getTotal4()) + "</td>"		        		
			        		+ " 	</tr>"
			        		*/
			        		+ " </table>");					
					sb.append("<br/>");
					}
					if (stbSintesisEmpresa!=null && stbSintesisEmpresa.length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<th>SÍNTESIS EMPRESA:</th>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbSintesisEmpresa.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
						sb.append("<br/>");
					}
					if (stbDatosMatriz.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>DATOS MATRIZ:</th>"		        	
				        		+ " </tr>" 
				        		+ " <tr>" 		        		
				        		+ " 	<td class=\"myEditor\">" + stbDatosMatriz.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");					
						sb.append("<br/>");
					}
					
					String tableaccionista="";
					boolean blistaAcc=false;
					boolean bcomenAcc=false;
					if (listaAccionistas!=null && listaAccionistas.size()>0){
						blistaAcc=true;
						//si se quiero 	que se alinea a derecha reemplazar el nombre del campo por RIGHT
						String[] Formatcampos={"nombre","RIGHT","CENTER","capitalizacionBurs"};
						String[] campos={"nombre","porcentaje","nacionalidad","capitalizacionBurs"};				
						cabeceraPersonalidad="";
						String[] cabecera={"PRINCIPALES ACCIONISTAS","%","NACIONALIDAD","INFORMACIÓN ADICIONAL"};
						String[] totales={"TOTAL:",totalAccionista,"",""};
						tableaccionista =GeneraTableHtml.getTableHTML(Accionista.class,listaAccionistas,campos,Formatcampos,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabecera,totales,Constantes.ID_SIADD_TOTAL,null);
					}
					String strComentAccionariadoval="";
					strComentAccionariadoval=obtenerCadenaHTMLValidada(strComentAccionariado,true);
					if (strComentAccionariadoval!=null && strComentAccionariadoval.length()>0){
						bcomenAcc=true;
					}
					
					if (blistaAcc  || bcomenAcc){
						//sb.append("ACCIONARIADO");
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>ACCIONARIADO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}	
					if (blistaAcc ){
						sb.append(tableaccionista);
					}
					if ( bcomenAcc){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Accionariado:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td>" + strComentAccionariadoval + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						sb.append("<br/>");
					}
					//ini MCG20130726
					//sb.append("CAPITALIZACION BURSATIL");	
				
						
					if (listaCapitalizacion != null && listaCapitalizacion.size() > 0) {
						sb.append(" <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>CAPITALIZACIÓN BURSATIL:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
						sb.append("<table class=\"gridtable\">");						
						
						sb.append("<tr>");							
							sb.append("<th align=\"center\">DIVISA</th>");	
							sb.append("<th align=\"center\">IMPORTE</th>");	
							sb.append("<th align=\"center\">% FONDOS PROPIOS</th>");
							sb.append("<th align=\"center\">FECHA</th>");
							sb.append("<th align=\"center\">OBSERVACIÓN</th>");				
						sb.append("</tr>");	
						
						for (CapitalizacionBursatil ocapbu:listaCapitalizacion){
							sb.append("<tr>");	
							if (ocapbu.getDivisa()!=null){
								sb.append("<td align=\"left\" width=\"25\">" + (ocapbu.getDivisa().getDescripcion()==null?"":ocapbu.getDivisa().getDescripcion().toString()) + "</td>");
							}else{
								sb.append("<td align=\"left\" width=\"25\">&nbsp;</td>");
							}						
							sb.append("<td align=\"right\" width=\"90\">" + (ocapbu.getImporte()==null?"":ocapbu.getImporte()) + "</td>");
							sb.append("<td align=\"right\" width=\"90\">" + (ocapbu.getFondosPropios()==null?"":ocapbu.getFondosPropios()) + "</td>");
							sb.append("<td align=\"left\" width=\"50\">" + (ocapbu.getFecha()==null?"":ocapbu.getFecha()) + "</td>");
							sb.append("<td align=\"left\">" + (ocapbu.getObservacion()==null?"":ocapbu.getObservacion()) + "</td>");						
							sb.append("</tr>");
						}
						
						sb.append("</table>"); 
						sb.append("<br/>");		
					}				
		 
					
					//fin MCG20130726
					
					if(idTipoPrograma.equals(Constantes.ID_TIPO_PROGRAMA_LOCAL.toString())){
						String tableprincipalesEjec;
						if (listaPrinciEjecutivos != null && listaPrinciEjecutivos.size() > 0) {
							String[] Formatcampospe={"nombres","puesto","informacionAdcional"};
							String[] campospe={"nombres","puesto","informacionAdcional"};
							cabeceraPersonalidad="";
							String[] cabecerape={"PRINCIPALES EJECUTIVOS","PUESTO","INFORMACIÓN ADICIONAL"};
							String[] totalespe={"","",""};
							tableprincipalesEjec =GeneraTableHtml.getTableHTML(PrincipalesEjecutivos.class,listaPrinciEjecutivos,campospe,Formatcampospe,
									Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabecerape,totalespe,Constantes.ID_NOADD_TOTAL,null);
							//sb.append("PRINCIPALES EJECUTIVOS");
							sb.append(" <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>PRINCIPALES EJECUTIVOS:</th>"		        	
					        		+ " </tr>"  
					        		+ " </table>");	
							sb.append(tableprincipalesEjec);					
							sb.append("<br/>");
						}
					}
					
					String tableParticipaciones="";
					boolean blistaPartic=false;
					boolean bcomenPartic=false;
					if (listaParticipaciones!=null && listaParticipaciones.size()>0){
							blistaPartic=true;
						String[] Formatcamposparticipaciones={"nombre","RIGHT","consolidacion","sectorActividad"};				
						String[] camposparticipaciones={"nombre","porcentaje","consolidacion","sectorActividad"};
						cabeceraPersonalidad="";
						String[] cabeceraparticipaciones={"PRINCIPALES AFILIADAS","%","CONSOLIDACIÓN","SECTOR ACTIVIDAD"};
						String[] totalesparticipaciones={"","","",""};
						tableParticipaciones =GeneraTableHtml.getTableHTML(Participaciones.class,listaParticipaciones,camposparticipaciones,Formatcamposparticipaciones,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraparticipaciones,totalesparticipaciones,Constantes.ID_NOADD_TOTAL,null);
					}
					
					String strComentPartiSignificativaval="";
					strComentPartiSignificativaval=obtenerCadenaHTMLValidada(strComentPartiSignificativa==null?"":strComentPartiSignificativa) ;
					if (strComentPartiSignificativaval!=null && strComentPartiSignificativaval.length()>0){
						bcomenPartic=true;
					}
					if (blistaPartic  || bcomenPartic){
						//sb.append("PARTICIPACIONES SIGNIFICATIVAS");
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>PARTICIPACIONES SIGNIFICATIVAS:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					if (blistaPartic ){
						sb.append(tableParticipaciones);
					}
					
					
					if ( bcomenPartic){	
					sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
			        		+ " <tr>" 	
			        		+ " 	<td>Comentario Participaciones Significativas:</td>"		        		
			        		+ " </tr>"  
			        		+ " <tr>" 			        		
			        		+ " 	<td>" + strComentPartiSignificativaval  + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");	
					}
					if (blistaPartic  || bcomenPartic){
						sb.append("<br/>");
					}
					
					String tableRating="";
					boolean blistaRatingEx=false;
					boolean bcomenRatingEx=false;
					if (listaRatingExterno!=null && listaRatingExterno.size()>0){
						blistaRatingEx=true;
						String[] FormatcamposRating={"companiaGrupo","agencia","cp","lp","outlook","moneda","fecha"};
						String[] camposRating={"companiaGrupo","agencia","cp","lp","outlook","moneda","fecha"};
						String[] cabeceraRating={"Compañia Grupo","Agencia","C/P","L/P","OutLook","Moneda","Fecha"};
						String[] totalesRating={"","","","","","",""};
						tableRating =GeneraTableHtml.getTableHTML(RatingExterno.class,listaRatingExterno,camposRating,FormatcamposRating,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraRating,totalesRating,Constantes.ID_NOADD_TOTAL,null);
						
					}
					String strRatingExval="";
					strRatingExval=obtenerCadenaHTMLValidada(strComentRatinExterno==null?"":strComentRatinExterno);
					if (strRatingExval!=null && strRatingExval.length()>0){
						bcomenRatingEx=true;
					}
					
					if (blistaRatingEx  || bcomenRatingEx){
						//sb.append("RATING EXTERNO");					
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>RATING EXTERNO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					if (blistaRatingEx ){
						sb.append(tableRating);
					}
					if ( bcomenRatingEx){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Rating Externo:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td>" + strRatingExval + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
					}
			
					if (blistaRatingEx  || bcomenRatingEx){
						sb.append("<br/>");
					}
									
					//COMPRAS Y VENTAS
					
					boolean bcompraVenta=false;
					boolean bComenComprVent=false;
					 if (
							 (totalImportaciones.getTotal1()!=null && !totalImportaciones.getTotal1().equals(""))||
							 (totalImportaciones.getTotal2()!=null && !totalImportaciones.getTotal2().equals(""))||
							 (totalImportaciones.getTotal3()!=null && !totalImportaciones.getTotal3().equals(""))||
							 
							 (importacionesME.getTotal1()!=null && !importacionesME.getTotal1().equals(""))||
							 (importacionesME.getTotal2()!=null && !importacionesME.getTotal2().equals(""))||
							 (importacionesME.getTotal3()!=null && !importacionesME.getTotal3().equals(""))||
							 
							 (totalExportaciones.getTotal1()!=null && !totalExportaciones.getTotal1().equals(""))||
							 (totalExportaciones.getTotal2()!=null && !totalExportaciones.getTotal2().equals(""))||
							 (totalExportaciones.getTotal3()!=null && !totalExportaciones.getTotal3().equals(""))||
							 
							 (exportacionesME.getTotal1()!=null && !exportacionesME.getTotal1().equals(""))||
							 (exportacionesME.getTotal2()!=null && !exportacionesME.getTotal2().equals(""))||
							 (exportacionesME.getTotal3()!=null && !exportacionesME.getTotal3().equals(""))
							 
					 	)
					 {
						 bcompraVenta=true; 
					 }
					 
					if (stbComenComprVent.toString().length()>0){
						 bComenComprVent=true;
					}
					if (bcompraVenta  || bComenComprVent){
						//sb.append("COMPRAS Y VENTAS");
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>COMPRAS Y VENTAS:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					
					if (bcompraVenta){			 

						sb.append("<table width=\"100%\">");
						sb.append("	<tr >");
						sb.append("		<td width=\"30%\">");
						sb.append("			<table >");
						sb.append("				<tr >");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Total Importaciones</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Importaciones ME</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>(Expresado en miles de USD)</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("		<td width=\"70%\">");
						sb.append("			<table width=\"100%\" class=\"gridtable\">");
						sb.append("				<tr>");
						sb.append("					<th colspan=\"3\" align=\"center\" >");
						sb.append("						% Sobre el Total de Compras");
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<th>");
						sb.append("						"+ anioc1);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+ anioc2);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+ anioc3);
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal1()==null?"":totalImportaciones.getTotal1()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal2()==null?"":totalImportaciones.getTotal2()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalImportaciones.getTotal3()==null?"":totalImportaciones.getTotal3()) + "%");
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal1()==null?"":importacionesME.getTotal1()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal2()==null?"":importacionesME.getTotal2()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((importacionesME.getTotal3()==null?"":importacionesME.getTotal3()));
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("	</tr>");
						sb.append("</table>");
						
						sb.append("<br/>");
						
						sb.append("<table width=\"100%\">");
						sb.append("	<tr >");
						sb.append("		<td width=\"30%\">");
						sb.append("			<table >");
						sb.append("				<tr >");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>&nbsp;</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Total Exportaciones</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>Exportaciones ME</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td>(Expresado en miles de USD)</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("		<td width=\"70%\">");
						sb.append("			<table width=\"100%\" class=\"gridtable\">");
						sb.append("				<tr>");
						sb.append("					<th colspan=\"3\" align=\"center\">");
						sb.append("						% Sobre el Total de Ventas");
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr align=\"center\">");
						sb.append("					<th>");
						sb.append("						"+aniov1);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+aniov2);
						sb.append("					</th>");
						sb.append("					<th>");
						sb.append("						"+aniov3);
						sb.append("					</th>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal1()==null?"":totalExportaciones.getTotal1()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal2()==null?"":totalExportaciones.getTotal2()) + "%");
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((totalExportaciones.getTotal3()==null?"":totalExportaciones.getTotal3()) + "%");
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("				<tr>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal1()==null?"":exportacionesME.getTotal1()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal2()==null?"":exportacionesME.getTotal2()));
						sb.append("					</td>");
						sb.append("					<td align=\"right\">");
						sb.append((exportacionesME.getTotal3()==null?"":exportacionesME.getTotal3()));
						sb.append("					</td>");
						sb.append("				</tr>");
						sb.append("			</table>");
						sb.append("		</td>");
						sb.append("	</tr>");
						sb.append("</table>");
						
						sb.append("<br/>");
					}
					if (bComenComprVent){
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<td>Comentario Compra y Ventas:</td>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbComenComprVent + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						sb.append("<br/>");
					}
					//add
					if (stbConcentracion.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 
				        		+ " <tr>" 	
				        		+ " 	<th>CONCENTRACIÓN:</th>"		        		
				        		+ " </tr>"  
				        		+ " <tr>" 			        		
				        		+ " 	<td class=\"myEditor\">" + stbConcentracion + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						sb.append("<br/>");
					}
					//Cifra de Negocio y Beneficio
					//sb.append( "CUADRO NEGOCIO BENEFICIO");
					boolean blistaNBNegocio=false;
					boolean blistaNBActividades=false;
					boolean bcomenNB=false;
					
					String tableCNB_pln="";
					if (listaNBNegocio!=null && listaNBNegocio.size()>0){
						blistaNBNegocio=true;	
						String[] cabeceraCNB_pln={tituloNegocio,anion1,anion2,anion3,anion4,anion5,anion6};
						String[] cabeceran={"DETALLE CIFRA DE NEGOCIO Y BENEFICIO","% DE INGRESOS","% DE BENEFICIOS"};
						String[] FormatcamposCNB_pln={"descripcion","RIGHT","RIGHT","RIGHT" ,"RIGHT","RIGHT","RIGHT"};
						String[] camposCNB_pln={"descripcion","totalI1","totalI2","totalI3" ,"totalB1","totalB2","totalB3"};				
						String[] totalesCNB_pln={"TOTAL:",totalNegocio.getTotalI1(),totalNegocio.getTotalI2(),totalNegocio.getTotalI3(),totalNegocio.getTotalB1(),totalNegocio.getTotalB2(),totalNegocio.getTotalB3()};
						tableCNB_pln =GeneraTableHtml.getTableHTML(NegocioBeneficio.class,listaNBNegocio,camposCNB_pln,FormatcamposCNB_pln,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraCNB_pln,totalesCNB_pln,Constantes.ID_SIADD_TOTAL,cabeceran);
					}

					String tableCNB_pla="";				
					if (listaNBActividades!=null && listaNBActividades.size()>0){
						String[] cabeceraCNB_pla={tituloActividad,anioa1,anioa2,anioa3,anioa4,anioa5,anioa6};
						String[] cabecera2={"DETALLE CIFRA DE NEGOCIO Y BENEFICIO","% DE INGRESOS","% DE BENEFICIOS"};//Primera fila cabecera
						String[] FormatcamposCNB_pla={"descripcion","RIGHT","RIGHT","RIGHT" ,"RIGHT","RIGHT","RIGHT"};
						String[] camposCNB_pla={"descripcion","totalI1","totalI2","totalI3" ,"totalB1","totalB2","totalB3"};				
						String[] totalesCNB_pla={"TOTAL:",totalActividad.getTotalI1(),totalActividad.getTotalI2(),totalActividad.getTotalI3(),totalActividad.getTotalB1(),totalActividad.getTotalB2(),totalActividad.getTotalB3()};
						tableCNB_pla =GeneraTableHtml.getTableHTML(NegocioBeneficio.class,listaNBActividades,camposCNB_pla,FormatcamposCNB_pla,
								Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraCNB_pla,totalesCNB_pla,Constantes.ID_SIADD_TOTAL,cabecera2);
					}
					
					if (stbEspacioLibreDB.toString().length()>0){
						bcomenNB=true;
					}
					if (blistaNBNegocio  ||blistaNBActividades|| bcomenNB){					
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>CUADRO NEGOCIO BENEFICIO:</th>"		        	
				        		+ " </tr>"  
				        		+ " </table>");	
					}
					
					if (blistaNBNegocio ){
						sb.append(tableCNB_pln);
						sb.append("<br/>");
					}				
					if (blistaNBActividades ){
						sb.append(tableCNB_pla);
						sb.append("<br/>");	
					}				
					if (bcomenNB){
					
						sb.append( " <table class=\"gridtableComent\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<td>Otros:</td>"		        	
				        		+ " </tr>" 
				        		+ " <tr>" 		        		
				        		+ " 	<td>" + stbEspacioLibreDB.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");					
						sb.append("<br/>");
					}
					
					if (stbValoracion.toString().length()>0){
						sb.append( " <table class=\"gridtable\" width=\"100%\">" 		      
				        		+ " <tr>" 			        		
								+ " <th>VALORACIÓN GLOBAL:</th>"
								+ " </tr>"
				        		+ " <tr>" 			        		
				        		//+ " 	<td>" + obtenerCadenaHTMLValidada(strComentvaloraGlobal==null?"":strComentvaloraGlobal) + "</td>"
				        		+ " 	<td class=\"myEditor\">" + stbValoracion + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");				
						//sb.append("<br/>");				
					}
					datosbasicos= sb.toString();
					
				} catch (BOException e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				}catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
					return "";
				}
				return datosbasicos;
			}

		
		//ini MCG20130318
		
		public String SintesisEconomicaFinancieraHTML(String idprograma,String indcapitulo,Tvariable oasignasalto,String codigoTitulo){
			String sintesisEF="";
			List<SintesisEconomica> listaSintesis = new ArrayList<SintesisEconomica>();
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa1 = new Programa();			
				programa1=programaBO.findById(Long.valueOf(idprograma));
				String nombreGrupoEmpresa=programa1.getNombreGrupoEmpresa();
				
				String tipo_empresa = programa1.getTipoEmpresa().getId().toString();
				String subtituloEmpGrup="";
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					subtituloEmpGrup="GRUPO";
				}
				listaSintesis=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				for (SintesisEconomica osintesiseconomica:listaSintesis ){
					if (osintesiseconomica.getNombreEmpresa().trim().equals(nombreGrupoEmpresa.trim())){
						idsintesisecon=osintesiseconomica.getId()==null?"":osintesiseconomica.getId().toString();
						extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
						break;
					}				
				}

				//Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_PLANTILLA_SINTESISECONFIN);
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				String filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
				//String filename=parametro.getValor()+File.separator +"plantilla.xls";
				if (idsintesisecon.equals("") || extension.equals("")){
					filename="";
				}
				StringBuilder stbComenSituFinanciera = new StringBuilder(); 
		        StringBuilder stbComenSituEconomica = new StringBuilder();
				StringBuilder stbValoracionEconFinanciera=new StringBuilder();
				StringBuilder stbValoracionPosiBalance=new  StringBuilder();
				
		    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa1);
		        if (programaBlob!=null){

			 	      if(programaBlob.getComenSituFinanciera()!=null){
			 		        for(byte x :programaBlob.getComenSituFinanciera() ){
			 		          	stbComenSituFinanciera.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }
			 	      if(programaBlob.getComenSituEconomica()!=null){
			 		        for(byte x :programaBlob.getComenSituEconomica() ){
			 		          	stbComenSituEconomica.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }
			 	      if(programaBlob.getValoracionEconFinanciera()!=null){
			 		        for(byte x :programaBlob.getValoracionEconFinanciera() ){
			 		          	stbValoracionEconFinanciera.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }
			 	      if(programaBlob.getValoracionPosiBalance()!=null){
			 		        for(byte x :programaBlob.getValoracionPosiBalance() ){
			 		          	stbValoracionPosiBalance.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }
		        }
		        
		        stbComenSituFinanciera 		=	obtenerCadenaHTMLValidada(stbComenSituFinanciera); 
		        stbComenSituEconomica 		= 	obtenerCadenaHTMLValidada(stbComenSituEconomica);
				stbValoracionEconFinanciera	=	obtenerCadenaHTMLValidada(stbValoracionEconFinanciera);
				stbValoracionPosiBalance	=	obtenerCadenaHTMLValidada(stbValoracionPosiBalance);
		        
		        
				boolean bComenSituFinanciera=false;
				boolean bComenSituEconomica=false;
				boolean bValoracionEconFinanciera=false;
				boolean bValoracionPosiBalance=false;
				boolean bSituacionFinancieroBD=false;
				
				String strSituacionFinancieroBDVal="";
				if (stbComenSituFinanciera.toString().length()>0){
					bComenSituFinanciera=true;
				}
				if (stbComenSituEconomica.toString().length()>0){
					bComenSituEconomica=true;
				}
				if (stbValoracionEconFinanciera.toString().length()>0){
					bValoracionEconFinanciera=true;
				}
				if (stbValoracionPosiBalance.toString().length()>0){
					bValoracionPosiBalance=true;
				}
				
				strSituacionFinancieroBDVal=SituacionFinancieroBD(filename).toString();
				
				if (strSituacionFinancieroBDVal.length()>0){
					bSituacionFinancieroBD=true;
				}

				if ( !bComenSituFinanciera &&
				     !bComenSituEconomica &&
				     !bValoracionEconFinanciera &&
				     !bValoracionPosiBalance&&
				     !bSituacionFinancieroBD){	
					oasignasalto.setSintesisEconomicoSLinea(false);
					sb.append("<h4><b><u> "+ indcapitulo +".- SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+ codigoTitulo+ "</span></h4>");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
					sb.append("<br/>");
					sb.append("<br/>");
					
				}else{	
					oasignasalto.setSintesisEconomicoSLinea(true);
					sb.append("<h4><b><u> "+ indcapitulo +".- SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+ codigoTitulo+ "</span></h4>");
					sb.append("<br/>");	
					sb.append(strSituacionFinancieroBDVal); 	
					sb.append("<br/>");
					
				}
				
				if (bComenSituFinanciera){
					sb.append("<table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>COMENTARIO DE SITUACIÓN FINANCIERA:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbComenSituFinanciera.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bComenSituEconomica){
					sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>COMENTARIO DE SITUACIÓN ECONÓMICA:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbComenSituEconomica.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bValoracionEconFinanciera){
					sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>VALORACIÓN SITUACIÓN ECONÓMICA-FINANCIERA:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbValoracionEconFinanciera.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bValoracionPosiBalance){
					sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>ANÁLISIS Y VALORACIÓN POSICIONES FUERA DE BALANCE Y RIESGO ESTRUCTURAL:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbValoracionPosiBalance.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");	
					sb.append("<br/>");	
				}
				
							
				sintesisEF= sb.toString();
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return sintesisEF;
		}
		
		//ini MCG20130313
		public String ExtractoSintesisEconomicaFinancieraHTML(String idprograma,String indcapitulo,Tvariable oasignarslto,String codigoTitulo){
			String ExtractosintesisEF="";
			List<SintesisEconomica> listaSintesis = new ArrayList<SintesisEconomica>();
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa1 = new Programa();			
				programa1=programaBO.findById(Long.valueOf(idprograma));
				String nombreGrupoEmpresa=programa1.getNombreGrupoEmpresa();
				String subtituloEmpGrup="";
				String tipo_empresa = programa1.getTipoEmpresa().getId().toString();
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					subtituloEmpGrup="GRUPO";
				}
				
				
				listaSintesis=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				for (SintesisEconomica osintesiseconomica:listaSintesis ){
					if (osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
						idsintesisecon=osintesiseconomica.getId()==null?"":osintesiseconomica.getId().toString();
						extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
						break;
					}				
				}

				//Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_PLANTILLA_SINTESISECONFIN);
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				String filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
				//String filename=parametro.getValor()+File.separator +"plantilla.xls";
				if (idsintesisecon.equals("") || extension.equals("")){
					filename="";
				}			
		        String strExtractoSituacionFinancieroBDval="";
		    	String saltolinea="";
		        boolean bExtractoSituacionFinanciero=false;
		        
		        strExtractoSituacionFinancieroBDval=ExtractoSituacionFinancieroBD(filename).toString();
		        if (strExtractoSituacionFinancieroBDval.length()>0){
		        	bExtractoSituacionFinanciero=true;	        	
		        }
		        

		        if (bExtractoSituacionFinanciero){
		        	oasignarslto.setExtractoSLinea(true);	
			        sb.append("<h4><b><u> "+ indcapitulo +".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+ codigoTitulo +"</span></h4>");
						
					sb.append(strExtractoSituacionFinancieroBDval); 	
					sb.append("<br/>");
		        }else{	
		        	oasignarslto.setExtractoSLinea(false);
		        	sb.append("<h4><b><u> "+ indcapitulo +".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+ codigoTitulo +"</span></h4>");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
					sb.append("<br/>");
					sb.append("<br/>");
		        }
				
				//sb.append("<br/>");	
							
				ExtractosintesisEF= sb.toString();
			
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return ExtractosintesisEF;
		}
		
		//fin MCG20130313
		
		
		//ini MCG20140909
		public String ExtractoPorEmpresaIndHTML(String idprograma,Empresa oempresa,String indcapitulo,Tvariable oasignarslto,String codigoTitulo){
			String ExtractosintesisEF="";
			List<SintesisEconomica> listaSintesis = new ArrayList<SintesisEconomica>();
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa1 = new Programa();			
				programa1=programaBO.findById(Long.valueOf(idprograma));
				String nombreGrupoEmpresa=programa1.getNombreGrupoEmpresa();
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				
				String subtituloEmpGrup="";
				String tipo_empresa = programa1.getTipoEmpresa().getId().toString();
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					subtituloEmpGrup="GRUPO";
				}
				

				listaSintesis=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				String codEmpresa="";
				String nombEmpresa="";
				if(programa1.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){	

					
					String onombreEmpresa=oempresa.getNombre().trim();	
					String strCodiEmpresa=oempresa.getCodigo().trim();	
								
					  SintesisEconomica osintesiseconomica=ObtenerSintesisEconFinanExisteEmpresa(onombreEmpresa,listaSintesis);
					  if (osintesiseconomica!=null){
						String filename="";				
						if (!osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
							
							idsintesisecon=osintesiseconomica.getId()==null?"": osintesiseconomica.getId().toString();
							extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
							codEmpresa=osintesiseconomica.getCodEmpresa()==null?"": osintesiseconomica.getCodEmpresa().toString();
							nombEmpresa=osintesiseconomica.getNombreEmpresa()==null?"":osintesiseconomica.getNombreEmpresa().trim();
							
							filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
							if (idsintesisecon.equals("")||extension.equals("")){
								filename="";
							}
							if (!filename.equals("")){
					  
								////////ini
				
							        String strExtractoSituacionFinancieroBDval="";
							    	String saltolinea="";
							        boolean bExtractoSituacionFinanciero=false;
							        
							        strExtractoSituacionFinancieroBDval=ExtractoSituacionFinancieroBD(filename).toString();
							        if (strExtractoSituacionFinancieroBDval.length()>0){
							        	bExtractoSituacionFinanciero=true;	        	
							        }
							        
							        sb.append("<h4><b><u> "+ indcapitulo +".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");
									
							        if (bExtractoSituacionFinanciero){
							        	oasignarslto.setExtractoSLinea(true);							        	
										sb.append(strExtractoSituacionFinancieroBDval); 	
										sb.append("<br/>");
							        }else{	
							        	oasignarslto.setExtractoSLinea(false);						        	
										sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
										sb.append("<br/>");
										sb.append("<br/>");
							        	
							        }		        
				        
							        ////////////////fin
							}
						}	 //IF SINTESIS ECONOMICA
					  }	else{
						  	oasignarslto.setExtractoSLinea(false);	
						  	sb.append("<h4><b><u> "+ indcapitulo +".- EXTRACTO SÍNTESIS ECONÓMICA - FINANCIERA "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
							sb.append("<br/>");
							sb.append("<br/>");
						  
					  }
				}			
				ExtractosintesisEF= sb.toString();
			
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return ExtractosintesisEF;
		}
		
		//fin MCG20140909
		
		
		public String SituacionFinancieroBD_ANT(String strruta) {
			
			String strSituacionFinanciero="";		
			StringBuilder sbsf=new StringBuilder ();
			FormulaEvaluator fEval=null;		
			try {
				if (!strruta.equals(""))	{
					FileInputStream fis;	
					String filename=strruta;	
					fis = new FileInputStream(filename);
				    HSSFWorkbook wbDataPlantEmpPrin = new HSSFWorkbook(fis);
				    HSSFSheet hojaOrigen=wbDataPlantEmpPrin.getSheetAt(Constantes.NUM_SHEET_FINANCIERO);
				    fEval=wbDataPlantEmpPrin.getCreationHelper().createFormulaEvaluator();
				    
					  Iterator rows = hojaOrigen.rowIterator(); 
					  sbsf.append("<table class=\"gridtablereducido\" width=\"100%\">");
				        while (rows.hasNext()) {
				        
				            HSSFRow row = (HSSFRow) rows.next();
		
				            if (row.getRowNum()==54){
				            	break;	
				            }	
				            
		
				            
				            
				            sbsf.append("<tr>");
			            			            
				            for (int i=0;i<18;i++){
				            	String valor="";
				            	String classdt="";
				            	
					            if (row.getRowNum()==14){
					            	classdt="class=\"mydtcab\"";
					            }
					            if (row.getRowNum()>=15 && row.getRowNum()<=30 && i==0){
					            	classdt="class=\"mydtString\"";
					            }
					            if (row.getRowNum()>=15 && row.getRowNum()<=30 && i>=16 && i<=18){
					            	classdt="class=\"mydtString\"";
					            }
					            
					            if (row.getRowNum()==34 && i<=7){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if (row.getRowNum()>=35 && row.getRowNum()<=43 && i==0){
					            	classdt="class=\"mydtString\"";
					            }
					            
					            if (row.getRowNum()==36 && i>=12 && i<=15 ){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if (row.getRowNum()==48 && i<=7){
					            	classdt="class=\"mydtcab\"";
					            }
					            if (row.getRowNum()>=49 && row.getRowNum()<=53 && i==0){
					            	classdt="class=\"mydtString\"";
					            }
					            
					            if (row.getRowNum()==47 && i>=13 && i<=15){
					            	classdt="class=\"mydtcab\"";
					            }
				            	
					            if (row.getCell(i)==null){			            	
					            	 valor=""; 
					            }else{
					            
						            	CellValue cellValue=fEval.evaluate(row.getCell(i));
						                if(row.getCell(i).getColumnIndex() <= 17 && cellValue!=null){
						                	
						                    switch(cellValue.getCellType())
						                    {
						                        case HSSFCell.CELL_TYPE_NUMERIC:
						                        	
						                        	//valor=FormatUtil.roundTwoDecimals(cellValue.getNumberValue());
						                        	valor=FormatUtil.roundTwoDecimalsPunto(cellValue.getNumberValue());
						                        	classdt="class=\"mydtNumero\"";			                        	
						                            break;
						                        case HSSFCell.CELL_TYPE_STRING:   
						                        	valor=cellValue.getStringValue()==null?"":cellValue.getStringValue();
						                        	break; 
						                        case HSSFCell.CELL_TYPE_BLANK: valor=""; 
						                       		break;		                        	
						                        case HSSFCell.CELL_TYPE_FORMULA:
						                        	valor="";
						                        	break;	
						                       default :
						                    	   valor=String.valueOf(cellValue.getStringValue()==null?"":cellValue.getStringValue());
						                    }	
						                }
						              
					            }
					        	sbsf.append("<td "+ classdt +">");
				            	 sbsf.append(valor); 
				            	sbsf.append("</td>");
				            }
				            sbsf.append("</tr>");
				        }
				        sbsf.append("</table>");
			        
				}else{
					sbsf.append("");
				}			        	
				strSituacionFinanciero= sbsf.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strSituacionFinanciero;
		}
		
		//ini 20111206 MIL
		
		public String SituacionFinancieroBD(String strruta) {
			
			String strSituacionFinanciero="";		
			StringBuilder sbsf=new StringBuilder ();
			FormulaEvaluator fEval=null;		
			Boolean flagEncontrado19=false;
			Boolean flagEncontrado24=false;
			FileInputStream fis=null;
			try {
				if (!strruta.equals(""))	{
					//FileInputStream fis;	
					String filename=strruta;	
					fis = new FileInputStream(filename);
				    HSSFWorkbook wbDataPlantEmpPrin = new HSSFWorkbook(fis);
				    HSSFSheet hojaOrigen=wbDataPlantEmpPrin.getSheetAt(Constantes.NUM_SHEET_FINANCIERO);
				    fEval=wbDataPlantEmpPrin.getCreationHelper().createFormulaEvaluator();
				    
					  Iterator rows = hojaOrigen.rowIterator(); 
					  sbsf.append("<table class=\"gridtablereducido\" width=\"100%\">");
				        while (rows.hasNext()) {
				        
				            HSSFRow row = (HSSFRow) rows.next();
		
				            if (row.getRowNum()==54){
				            	break;	
				            }	
				            if (row.getRowNum()==19){
				            	flagEncontrado19 = true;	
				            }
				            if (row.getRowNum()==24){
				            	flagEncontrado24 = true;	
				            }
				            sbsf.append("<tr>");
			            			            
				            for (int i=0;i<17;i++){
				            	String valor="";
				            	String classdt="";

					            if (row.getRowNum()==14){
					            	classdt="class=\"mydtcab\"";
					            	if(i==16){
					            		classdt=classdt+"colspan=\"3\"";
					            	}
					            }
					            
					            if (row.getRowNum()>=15 && row.getRowNum()<=30 && i==0){
					            	classdt="class=\"mydtString\"";
					            	classdt=classdt+" style=\"width:105px\" ";
					            }

					            if (row.getRowNum()>=15 && row.getRowNum()<=30 && i==16){
					            	classdt="class=\"mydtString\""+"width=\"100px\" colspan=\"3\"";
					            }
					            
					            if (row.getRowNum()==34 && i<=7){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if (row.getRowNum()>=35 && row.getRowNum()<=43 && i==0){
					            	classdt="class=\"mydtString\"";
					            }
					            
					            if (row.getRowNum()==36 && i>=12 && i<=15 ){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if (row.getRowNum()==48 && (i==1||i==3||i==5||i==7)){
					            	classdt="class=\"mydtcab\"";
					            }
					            if (row.getRowNum()>=49 && row.getRowNum()<=53 && i==0){
					            	classdt="class=\"mydtString\"";
					            }
					            
					            if (row.getRowNum()==47 && i>=13 && i<=15){
					            	classdt="class=\"mydtcab\"";
					            }
					            if (row.getRowNum()==1 && (i==7)){
		                        	classdt="class=\"mydtTitulo\" colspan=\"4\"";
							        //agrandamos el nombre de la empresa
		                        }
		                        if (row.getRowNum()==12 && (i==4)){
						            	classdt="class=\"mydtTituloCab\" colspan=\"9\"";
							        //quitamos el detalle Anexo 2
			                    }
		                        if (row.getRowNum()==12 && (i>4 && i<13)){
					            	continue;					        
		                        }
		                        if (row.getRowNum()==32 && (i==4)){
			                        	classdt="class=\"mydtTituloCab\" colspan=\"9\"";
			                    }
		                        if (row.getRowNum()==12 && (i>4 && i<13)){
					            	continue;
		                        }
		                        if (row.getRowNum()==45 && (i==5)){
			                        	classdt="class=\"mydtTituloCab\" colspan=\"7\"";
			                    }
		                        if (row.getRowNum()==45 && (i>6 && i<12)){
		                        	continue;
		                        }
		                        if (row.getRowNum()==35 && (i==9)){
					            	classdt="class=\"mydtTituloCab\" colspan=\"3\"";
		                        }
		                        if ((row.getRowNum()==37||row.getRowNum()==38||row.getRowNum()==39||row.getRowNum()==40||row.getRowNum()==41||row.getRowNum()==42) && (i==9)){
					            	classdt="colspan=\"3\"";
		                        }
		                        if ((row.getRowNum()==37||row.getRowNum()==38||row.getRowNum()==39||row.getRowNum()==40||row.getRowNum()==41||row.getRowNum()==42) && (i==10)){
					            	continue;
		                        }
		                        if ((row.getRowNum()==37||row.getRowNum()==38||row.getRowNum()==39||row.getRowNum()==40||row.getRowNum()==41||row.getRowNum()==42) && (i==11)){
					            	continue;
		                        }
		                        if ((row.getRowNum()==48||row.getRowNum()==49||row.getRowNum()==50||row.getRowNum()==51||row.getRowNum()==52||row.getRowNum()==53) && (i==9)){
					            	classdt="colspan=\"4\"";
		                        }
		                        if ((row.getRowNum()==48||row.getRowNum()==49||row.getRowNum()==50||row.getRowNum()==51||row.getRowNum()==52||row.getRowNum()==53) && (i==10)){
					            	continue;
		                        }
		                        if ((row.getRowNum()==48||row.getRowNum()==49||row.getRowNum()==50||row.getRowNum()==51||row.getRowNum()==52||row.getRowNum()==53) && (i==11)){
					            	continue;
		                        }
		                        if ((row.getRowNum()==48||row.getRowNum()==49||row.getRowNum()==50||row.getRowNum()==51||row.getRowNum()==52||row.getRowNum()==53) && (i==12)){
					            	continue;
		                        }
		                        
		                        if ((row.getRowNum()==35||row.getRowNum()==36||row.getRowNum()==37||row.getRowNum()==38||row.getRowNum()==39||row.getRowNum()==40||row.getRowNum()==41||row.getRowNum()==42) && (i==5)){
		                        	CellValue cellValuev=fEval.evaluate(row.getCell(i));
		                        	//logger.error("CellValue"+cellValuev);
		                        }
		                        
					            if (row.getCell(i)==null){			            	
					            	 valor=""; 
					            }else{
					            	CellValue cellValue=fEval.evaluate(row.getCell(i));
					                if(row.getCell(i).getColumnIndex() <= 17 && cellValue!=null){
				                        if (row.getRowNum()==15||row.getRowNum()==20||row.getRowNum()==21||row.getRowNum()==30){
				                        	classdt=classdt+"style=\"background: #81F7D8 no-repeat;\"";
				                        }
				                        if (row.getRowNum()==19 && (i<8)){
				                        	classdt=classdt+"style=\"background: #81F7D8 no-repeat;\"";
				                        }
				                        if (row.getRowNum()==25 && (i>8 && i<17)){
				                        	classdt=classdt+"style=\"background: #81F7D8 no-repeat;\"";
				                        }
				                        if ((row.getRowNum()==36 || row.getRowNum()==37|| row.getRowNum()==40|| row.getRowNum()==42|| row.getRowNum()==43) && (i>0 && i<8)){
				                        	classdt=classdt+"style=\"background: #81F7D8 no-repeat;\"";
				                        }
				                        if ((row.getRowNum()==37) && (i>11 && i<16)){
				                        	classdt=classdt+"style=\"background: #81F7D8 no-repeat;\"";
				                        }
				                        
					                    switch(cellValue.getCellType())
					                    {
					                        case HSSFCell.CELL_TYPE_NUMERIC:
					                        	classdt=classdt+"class=\"mydtNumero\"";	
					                        	if (row.getRowNum()==0 && (i==17)){
									            	valor="";
									            	classdt="";	
									            	//quitamos el 5
					                        	}else if (row.getRowNum()==9 && (i==3)){
						                        	//valor=FormatUtil.roundTwoDecimals(cellValue.getNumberValue());
						                        	//tipo cambio 3 decimales
									            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),3);
									            //para porcentaje con un decimal BALANCES DE SITUACION 1 decimal con porcentaje
					                        	}else if (row.getRowNum()>=15 && row.getRowNum()<=30 && (i==2 || i==4 || i==6||i==10 ||i==12||i==14)){
					        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
					        		            //para porcentaje con un decimal CUENTAS DE RESULTADOS 1 decimal con porcentaje
					                        	}else if (row.getRowNum()>=35 && row.getRowNum()<=43 && (i==2 || i==4 || i==6)){
									            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
									            //para dos decimales con formato X 00.00 COBERTURA DE INTERESES 2 decimales
					                        	}else if (row.getRowNum()==48 && (i==13 || i==14 || i==15)){
									            	valor="x " + FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
									            //para dos decimales con formato X 00.00 D. FINANC. TOTAL / F. PROPIOS y D. FINANC. NETA / E.B.I.T.D.A. 2 decimales
					                        	}else if (row.getRowNum()>=52 && row.getRowNum()<=53 && (i==13 || i==14 || i==15)){
									            	valor="x " + FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
									            //para 1 decimales PAY-BACK xxxx  1 decimal
					                        	}else if (row.getRowNum()>=49 && row.getRowNum()<=51 && (i==13 || i==14 || i==15)){
									            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),1);
									            }else{    
									            	//sin decimales: enteros									            	
									            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);
									            }
					                            break;
					                        case HSSFCell.CELL_TYPE_STRING:   
					                        	valor=cellValue.getStringValue()==null?"":cellValue.getStringValue();
					                        	if (row.getRowNum()==1 && (i==13)){
									            	valor="";
											        //quitamos el detalle Anexo 2
						                        }
					                        	if (row.getRowNum()==1 && (i==0)){
									            	valor="";
											        //quitamos titulo empresa
						                        }
					                        	break; 
					                        case HSSFCell.CELL_TYPE_BLANK: valor=""; 
					                       		break;		                        	
					                        case HSSFCell.CELL_TYPE_FORMULA:
					                        	valor="";
					                        	break;	
					                       default :
					                    	   valor=String.valueOf(cellValue.getStringValue()==null?"":cellValue.getStringValue());
					                    }	
					                }
					            }
					            if (flagEncontrado19 || flagEncontrado24){
	                        		if(i==16){
	                        			CellValue cellValueOtros=fEval.evaluate(row.getCell(17));
	                        			if (cellValueOtros==null){
	                        				valor="";
	                        			}
	                        			else{                        				
	                        				valor=cellValueOtros.getStringValue()==null?"":cellValueOtros.getStringValue();
	                        			}
	                        			
	                        		}
		                        }
					        	sbsf.append("<td "+ classdt +">");
				            	 sbsf.append(valor); 
				            	sbsf.append("</td>");            	
				            	
				            }			          
				            
				            sbsf.append("</tr>");
				            flagEncontrado19=false;
				            flagEncontrado24=false;
				        }
				        sbsf.append("</table>");
			        
				}else{
					sbsf.append("");
				}			        	
				strSituacionFinanciero= sbsf.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}finally {
			    try
			    {
			        if (fis != null)		        	
			            fis.close();
			    }
			    catch (IOException e)
			    {
			    	logger.error("Error SituacionFinancieroBD: read File SE IO: "+ e.getMessage());
	                e.printStackTrace();
			    }
			}
			return strSituacionFinanciero;
		}
		
		//fin 20111206 MIL
		
		//ini MCG20130313
	public String ExtractoSituacionFinancieroBD(String strruta) {
			
			String strExtractoSituacionFinanciero="";		
			StringBuilder sbsf=new StringBuilder ();
			FormulaEvaluator fEval=null;	
			String valorRuc="";		
			try {
				if (!strruta.equals(""))	{
					FileInputStream fis;	
					String filename=strruta;	
					fis = new FileInputStream(filename);
				    HSSFWorkbook wbDataPlantEmpPrin = new HSSFWorkbook(fis);
				    HSSFSheet hojaOrigen=wbDataPlantEmpPrin.getSheetAt(Constantes.NUM_SHEET_EXTRACTO);
				    fEval=wbDataPlantEmpPrin.getCreationHelper().createFormulaEvaluator();
				    boolean isfileNew=false;
					  Iterator rows = hojaOrigen.rowIterator(); 
					  sbsf.append("<table class=\"gridtablereducido\" width=\"100%\">");
				        while (rows.hasNext()) {
				        
				            HSSFRow row = (HSSFRow) rows.next();
				            
				            
				            if (row.getRowNum()==3){			            	
				            	
				            	int cel=2;
				            	 if (row.getCell(cel)==null){			            	
				            		 valorRuc=""; 
					             }else{				            
						            	CellValue cellValueRuc=fEval.evaluate(row.getCell(cel));
						                if(row.getCell(cel).getColumnIndex() <= 12 && cellValueRuc!=null){
						                	
						                    switch(cellValueRuc.getCellType())
						                    {
						                    	case HSSFCell.CELL_TYPE_NUMERIC: 			                        	
						                    		valorRuc=String.valueOf(FormatUtil.roundEntero(cellValueRuc.getNumberValue(),0));
						                    		break;
						                    	case HSSFCell.CELL_TYPE_STRING:   
						                        	 valorRuc=cellValueRuc.getStringValue()==null?"":cellValueRuc.getStringValue();
						                          	break;
						                    	case HSSFCell.CELL_TYPE_BLANK: valorRuc=""; 
					                       			break;		                        	
						                    	case HSSFCell.CELL_TYPE_FORMULA: valorRuc="";
					                        		break;
							                    default :
							                    	 valorRuc=String.valueOf(cellValueRuc.getStringValue()==null?"":cellValueRuc.getStringValue());
							                } 
						                }
						         }
				            	
				            	continue;
				            }
				            
				            if (row.getRowNum()==0 || row.getRowNum()==1 || row.getRowNum()==2 ){
			            	continue;
				            }
		
				            if (row.getRowNum()==163){
				            	break;	
				            }	
				            if (row.getRowNum()==84){
				            	continue;	
				            }
				            if (row.getRowNum()==85){
				            	continue;	
				            }
				            if (row.getRowNum()==86){
				            	 sbsf.append("<tr>");
				            	 sbsf.append("<td>");
				            	 sbsf.append("<div class=saltopage>&nbsp; </div>");
				            	 sbsf.append("</td>");
				            	 sbsf.append("</tr>");
				   
				            }
				            

				            sbsf.append("<tr>");
			            			            
				            for (int i=0;i<13;i++){
				            	String valor="";
				            	String classdt="";	
				            	if (i==1 ||i==4 ||i==7 ||i==10 ){
					            	continue;
						         }
				            	
				            	if (row.getRowNum()==5){					            	
					            	if(i>2 && i<9){
					            		continue;
					            	}
					            }

					            if (i==0){
					            	classdt=classdt+" width=\"200px\" ";
					            }
					            if ((row.getRowNum()==9 )&& (i==2 ||i==5 ||i==8 ||i==11)){
					            	classdt=classdt+" width=\"80px\" ";
					            }
					            if ((row.getRowNum()==9 )&& (i==3 ||i==6 ||i==9 ||i==12)){
					            	classdt=classdt+" width=\"50px\" ";
					            }
					            if ((row.getRowNum()==3 || row.getRowNum()==5 )&& i==2){
					            	classdt="class=\"mydtcab\"";				            	
						            
					            	if (row.getRowNum()==5){					            	
						            	if(i==2){
						            		classdt=classdt+" colspan=\"5\"";
						            	}
						            }
					            }

		
					           
					            if (row.getRowNum()==7 && (i==2 ||i==5 ||i==8 ||i==11)){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Activo
					            if (row.getRowNum()==10 ||row.getRowNum()==11){
					            	classdt="class=\"mydtcab\"";
					            }
					            if (row.getRowNum()>=12 && row.getRowNum()<83 && i==0){
					            	classdt="class=\"mydtStringLeftRight\"";
					            }
					            
					            if (row.getRowNum()>=86 && row.getRowNum()<=111 && i==0){
					            	classdt="class=\"mydtStringLeftRight\"";
					            }      
					            
					            if (row.getRowNum()>=113 && row.getRowNum()<=160 && i==0){
					            	classdt="class=\"mydtStringLeftRight\"";
					            }
					            if (row.getRowNum()==161 && i==0){
					            	classdt="class=\"mydtStringLeftRightBottom\"";
					            }
					         
					            //Activo Corriente
					            if (row.getRowNum()==29 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Activo N0 Corriente
					            if (row.getRowNum()==47 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //total Corriente
					            if (row.getRowNum()==48 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Pasivo
					            if (row.getRowNum()==49 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            //Pasivo Corriente
					            if (row.getRowNum()==62 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Pasivo no Corriente
					            if (row.getRowNum()==70 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //total Pasivo
					            if (row.getRowNum()==72 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Patrimonio
					            if (row.getRowNum()==80 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //total patrimonio
					            if ( row.getRowNum()==81 && i<2){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if (row.getRowNum()==83 && (i==2 ||i==5 ||i==8 ||i==11)){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            //Estado de perdidas y ganancias
					            
					            if ( row.getRowNum()==86 && i<=12){
					            	classdt="class=\"mydtcab\"";
					            }
					            
					            if ( (row.getRowNum()==87 || row.getRowNum()==89|| row.getRowNum()==93
					            		|| row.getRowNum()==98|| row.getRowNum()==106|| row.getRowNum()==109 
					            		|| row.getRowNum()==111) && i<=12){
					            	classdt="class=\"mydtcab\"";
					            }
		
					            
					            				           
				            	
					            if (row.getCell(i)==null){			            	
					            	 valor=""; 
					            }else{
					            
						            	CellValue cellValue=fEval.evaluate(row.getCell(i));
						                if(row.getCell(i).getColumnIndex() <= 12 && cellValue!=null){
						                	
						                    switch(cellValue.getCellType())
						                    {
						                        case HSSFCell.CELL_TYPE_NUMERIC:
						                        	
						                        	//valor=FormatUtil.roundTwoDecimals(cellValue.getNumberValue());
						                        	//tipo cambio 3 decimales
						                        	if (row.getRowNum()==8 && (i==2 ||i==5 ||i==8 ||i==11)){
										            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
										            	classdt=classdt+"style=\"text-align: center;\"";
										            //para porcentaje con un decimal BALANCES DE SITUACION 1 decimal con porcentaje
						                        	}else if (row.getRowNum()>=12 && row.getRowNum()<=111 && (i==3 || i==6 || i==9||i==12)){
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),2);
						                        	
						                        	}else if (row.getRowNum()>=116 && row.getRowNum()<=133 && (i==2 || i==5 || i==8||i==11)){
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
						                        	}else if (row.getRowNum()==134  && (i==2 || i==5 || i==8||i==11)){
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	}else if (row.getRowNum()>=135 && row.getRowNum()<=155 && (i==2 || i==5 || i==8||i==11)){
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
						                        	}else if (row.getRowNum()>=150 && row.getRowNum()<=153 && (i==3 || i==6 || i==9||i==12)){
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),2);
						                        	}else if (row.getRowNum()>=157 && row.getRowNum()<=160 && (i==2 || i==5 || i==8||i==11)){
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
						        		            //para porcentaje con un decimal CUENTAS DE RESULTADOS 1 decimal con porcentaje
//						                        	}else if (row.getRowNum()>=35 && row.getRowNum()<=43 && (i==2 || i==4 || i==6)){
//										            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
//										            //para dos decimales con formato X 00.00 COBERTURA DE INTERESES 2 decimales
//						                        	}else if (row.getRowNum()==48 && (i==13 || i==14 || i==15)){
//										            	valor="x " + FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
//										            //para dos decimales con formato X 00.00 D. FINANC. TOTAL / F. PROPIOS y D. FINANC. NETA / E.B.I.T.D.A. 2 decimales
//						                        	}else if (row.getRowNum()>=52 && row.getRowNum()<=53 && (i==13 || i==14 || i==15)){
//										            	valor="x " + FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);
//										            //para 1 decimales PAY-BACK xxxx  1 decimal
//						                        	}else if (row.getRowNum()>=49 && row.getRowNum()<=51 && (i==13 || i==14 || i==15)){
//										            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),1);
										            }
										            else								            
										            {    
										            	//sin decimales: enteros									            	
										            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);
										            }
						                        	classdt=classdt+"class=\"mydtNumero\"";	
						                        	
						    			            
						                        	
						                        	
						                        			                        	
						                            break;
						                        case HSSFCell.CELL_TYPE_STRING:   
						                        	valor=cellValue.getStringValue()==null?"":cellValue.getStringValue();
						                        	if ((row.getRowNum()==3 && i==2)){
						                        		classdt=classdt+"style=\"text-align: center;\"";
						                        	}
						                        	if ((row.getRowNum()==7 && (i==2||i==5 ||i==8 ||i==11))){
						                        		classdt=classdt+"style=\"text-align: center;\"";
						                        	}
						                        	if ((row.getRowNum()==9 && (i==2||i==5 ||i==8 ||i==11))){
						                        		classdt=classdt+"style=\"text-align: center;\"";
						                        	}
						                        	if ((row.getRowNum()==113 && i==0)){
						                        		if (valor!=null && valor.toString().trim().equals(Constantes.ETIQUETA_METODIZADO_RATIOS)){
						                        			isfileNew=true;
						                        		}
						                        	}
						                        	break; 
						                        case HSSFCell.CELL_TYPE_BLANK: valor=""; 
						                       		break;		                        	
						                        case HSSFCell.CELL_TYPE_FORMULA:
						                        	valor="";
						                        	break;	
						                       default :
						                    	   valor=String.valueOf(cellValue.getStringValue()==null?"":cellValue.getStringValue());
						                    }                

						                }
						                
						                  //adicional 
							            if (row.getRowNum()>=12 && row.getRowNum()<=28 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=30 && row.getRowNum()<=46 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=50 && row.getRowNum()<=61 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=63 && row.getRowNum()<=69 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=73 && row.getRowNum()<=79 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=90 && row.getRowNum()<=92 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=94 && row.getRowNum()<=97 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=99 && row.getRowNum()<=105 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            if (row.getRowNum()>=107 && row.getRowNum()<=108 && (i>=2 && i<=12)){
							            	classdt="class=\"mydtNumeroLeftRight\"";
			                        	
			                        	}
							            
							            //Pasivo
							            if (row.getRowNum()==49 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //Activo Corriente
							            if (row.getRowNum()==29 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							           //Ativo no Corriente
							            if (row.getRowNum()==47 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //total Activos
							            if (row.getRowNum()==48 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //Pasivo
							            if (row.getRowNum()==49 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            //Pasivo Corriente
							            if (row.getRowNum()==62 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //Pasivo no Corriente
							            if (row.getRowNum()==70 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //total Pasivo
							            if (row.getRowNum()==72 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //Patrimonio
							            if (row.getRowNum()==80 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //total patrimonio
							            if ( row.getRowNum()==81 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }	
							            
							            if (row.getRowNum()==83 && (i==2 ||i==5 ||i==8 ||i==11)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            //Estado de perdidas y ganancias
							            
							            if ( row.getRowNum()==86 && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            if ( (row.getRowNum()==87 || row.getRowNum()==89|| row.getRowNum()==93
							            		|| row.getRowNum()==98|| row.getRowNum()==106|| row.getRowNum()==109 
							            		|| row.getRowNum()==111) && (i>1 && i<=12)){
							            	classdt="class=\"mydtNumeroCab\"";
							            }
							            
							            if (isfileNew){	
							            	
								            //ratios nueva plantilla
		           				            
								            if (row.getRowNum()==113 && (i<=12)){
								            	classdt="class=\"mydtcab\"";
								            }
								            
								            if ( (row.getRowNum()==115 || row.getRowNum()==119|| row.getRowNum()==124|| row.getRowNum()==130
								            		|| row.getRowNum()==141|| row.getRowNum()==149 || row.getRowNum()==156) && i<=12){
								            	classdt="class=\"mydtcab\"";
								            }
								           
								            if ( (row.getRowNum()==154 && (i==2 ||i==5 ||i==8 ||i==11))){
								            	classdt="class=\"mydtNumero\"";
								            }
								            
							            	//nueva pantilla
								            if (row.getRowNum()>=116 && row.getRowNum()<=118 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=120 && row.getRowNum()<=123 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=125 && row.getRowNum()<=129 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=131 && row.getRowNum()<=140 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=142 && row.getRowNum()<=148 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=150 && row.getRowNum()<=155 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=157 && row.getRowNum()<=160 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()==161 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRightBottom\"";
				                        	
				                        	}
							            }else{
							            	
							            	//ratios vieja plantilla
		           				            
								            if (row.getRowNum()==115 && (i<=12)){
								            	classdt="class=\"mydtcab\"";
								            }
								            
								            if ( (row.getRowNum()==117||  row.getRowNum()==129
								            		|| row.getRowNum()==139|| row.getRowNum()==144 || row.getRowNum()==149) && i<=12){
								            	classdt="class=\"mydtcab\"";
								            }
								           

								            
							            	//nueva pantilla
								            if (row.getRowNum()>=118 && row.getRowNum()<=128 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=130 && row.getRowNum()<=138 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=140 && row.getRowNum()<=143 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								           
								            if (row.getRowNum()>=145 && row.getRowNum()<=148 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()>=150 && row.getRowNum()<=160 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRight\"";
				                        	
				                        	}
								            if (row.getRowNum()==161 && (i>=2 && i<=12)){
								            	classdt="class=\"mydtNumeroLeftRightBottom\"";
				                        	
				                        	}
		
							            	
							            }
						              
					            }
					            
					            if ((row.getRowNum()==5 )&& i==9){					        	
					            	valor="Ruc: ";
					            	classdt=classdt+"style=\"text-align: Right;\"";
					            }
					            if ((row.getRowNum()==5 )&& i==11){					        	
					            	valor=valorRuc;	
					            	classdt="class=\"mydtcab\"";
					            }
					            
					        	sbsf.append("<td "+ classdt +">");
				            	 sbsf.append(valor); 
				            	sbsf.append("</td>");
				            	
					            
				            }
				            sbsf.append("</tr>");
				        }
				        sbsf.append("</table>");
			        
				}else{
					sbsf.append("");
				}			        	
				strExtractoSituacionFinanciero= sbsf.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strExtractoSituacionFinanciero;
		}
		//fin MCG20130313
		

		public String RatingHTML(String idprograma,String indcapitulo,Tvariable oasignasalto,String codigoTitulo){
			String rating="";
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa2 = new Programa();
				programa2=programaBO.findById(Long.valueOf(idprograma));
				//revisar codigo central
//				String idEmpresa = programa2.getIdEmpresa();//getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
//				programa2.setIdEmpresa(idEmpresa);
//				programa2.setId(Long.valueOf(idprograma));
				int anio = Integer.parseInt(programa2.getAnio().toString());			
				
				String tipo_empresa = programa2.getTipoEmpresa().getId().toString(); 
				String codEmpresaGrupo="";
				String subtituloEmpGrup="";
				String nombreEmpresGrupo=programa2.getNombreGrupoEmpresa();
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					codEmpresaGrupo=programa2.getIdEmpresa();				
				}else{ 				
					codEmpresaGrupo=programa2.getIdGrupo();
					subtituloEmpGrup="GRUPO";
				}						
				
				ratingBO.setPrograma(programa2);
				listaRating = ratingBO.findRating(programa2.getId(),codEmpresaGrupo);			
				
				Rating oratingCAb=new Rating();
				if (listaRating!=null && listaRating.size()>0){
						for (Rating orating1: listaRating){					
						if(orating1.getDescripcion()==null){
							oratingCAb=orating1;
							listaRating.remove(orating1);
							break;
						}
					}
			
				}
				String anio2="";
				String anio3="";
				String anio4="";
				//00-12-2011
				if (oratingCAb.getTotalAnio2()!=null && oratingCAb.getTotalAnio2().length()>6){
					anio2=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().substring(0, 7);
				}
				if (oratingCAb.getTotalAnio1()!=null && oratingCAb.getTotalAnio1().length()>6){
					anio3=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().substring(0, 7);
				}
				if (oratingCAb.getTotalAnioActual()!=null && oratingCAb.getTotalAnioActual().length()>6){
					anio4=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().substring(0, 7);					
				}
				
		
		        StringBuilder stbvaloracionRating = new StringBuilder(); 
		
				
		    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa2);
		        if (programaBlob!=null){

			 	      if(programaBlob.getValoracionRating()!=null){
			 		        for(byte x :programaBlob.getValoracionRating()){
			 		        	stbvaloracionRating.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	      }

		        }
		        stbvaloracionRating = obtenerCadenaHTMLValidada(stbvaloracionRating);
		        

				
		        boolean bRating=false;
		        boolean bvaloracionRating=false;
		
				String tableRatingbbva="";
				String[] FormatcamposRatingbbva={"descripcion","CENTER","CENTER","CENTER"};
				String[] camposRatingbbva={"descripcion","totalAnio2","totalAnio1","totalAnioActual"};
				String cabeceraPersonalidad="";
				String[] cabeceraRatingbbva={"",anio2,anio3,anio4};
				String[] totalesRatingbbva={"","","",""};
				if(listaRating!=null && listaRating.size()>0){
					bRating=true;
					tableRatingbbva =GeneraTableHtml.getTableHTML(Rating.class,listaRating,camposRatingbbva,FormatcamposRatingbbva,
						Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraRatingbbva,totalesRatingbbva,Constantes.ID_NOADD_TOTAL,null);
				}
				if (stbvaloracionRating.toString().length()>0){
					bvaloracionRating=true;
				}			

				if (!bRating && !bvaloracionRating){
					oasignasalto.setRatingSLinea(false);
					sb.append("<h4><b><u> "+ indcapitulo +".- RATING BBVA CONTINENTAL "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");			
					sb.append("<h4><b><u>"+ indcapitulo +".1.- RATING: " + nombreEmpresGrupo +"</u></b></h4>");	
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
					sb.append("<br/>");
					sb.append("<br/>");
					
				}else{
					oasignasalto.setRatingSLinea(true);
			        sb.append("<h4><b><u> "+ indcapitulo +".- RATING BBVA CONTINENTAL "+subtituloEmpGrup+"</u></b> <span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");			
					//sb.append("<br/>");
					sb.append("<h4><b><u>"+ indcapitulo +".1.- RATING: " + nombreEmpresGrupo +"</u></b></h4>");				
				}
									
			
				if (bRating ){
					sb.append(tableRatingbbva);				
				}			
				if (bvaloracionRating){
					sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>VALORACIÓN DEL RATING:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbvaloracionRating.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
				}
				if (bRating  || bvaloracionRating){
					sb.append("<br/>");
				}
					
				rating= sb.toString();
		
	        } catch (Exception e) {
	        	logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return rating;
			}
		
		//ini MCG20130325
		public String RatingPorEmpresaHTML(String idprograma){
			String rating="";
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa2 = new Programa();
				programa2=programaBO.findById(Long.valueOf(idprograma));

				int anio = Integer.parseInt(programa2.getAnio().toString());			
				
				String tipo_empresa = programa2.getTipoEmpresa().getId().toString(); 
				
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					return "";			
				}					
				
				List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
				lstEmpresas = empresaBO.listarEmpresasPorPrograma(programa2.getId());			
				if (lstEmpresas!=null && lstEmpresas.size()<=0){				
					return "";
				}	
				int ind=1;
				for (Empresa oempresa:lstEmpresas){
					String codEmpresaGrupo=oempresa.getCodigo();
					String nombreEmpresGrupo=oempresa.getNombre();
					ind++;
				
				
					ratingBO.setPrograma(programa2);
					listaRating = ratingBO.findRating(programa2.getId(),codEmpresaGrupo);			
					
					Rating oratingCAb=new Rating();
					if (listaRating!=null && listaRating.size()>0){
							for (Rating orating1: listaRating){					
							if(orating1.getDescripcion()==null){
								oratingCAb=orating1;
								listaRating.remove(orating1);
								break;
							}
						}
				
					}
					String anio2="";
					String anio3="";
					String anio4="";
					//00-12-2011
					if (oratingCAb.getTotalAnio2()!=null && oratingCAb.getTotalAnio2().length()>6){
						anio2=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().substring(0, 7);
					}
					if (oratingCAb.getTotalAnio1()!=null && oratingCAb.getTotalAnio1().length()>6){
						anio3=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().substring(0, 7);
					}
					if (oratingCAb.getTotalAnioActual()!=null && oratingCAb.getTotalAnioActual().length()>6){
						anio4=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().substring(0, 7);					
					}
					
			
			        StringBuilder stbvaloracionRating = new StringBuilder(); 
			
					
			    	RatingBlob ratingBlob = ratingBlobBO.findRatingBlobByPrograma(programa2,codEmpresaGrupo);
			        if (ratingBlob!=null){
		
				 	      if(ratingBlob.getValoracionRating()!=null){
				 		        for(byte x :ratingBlob.getValoracionRating()){
				 		        	stbvaloracionRating.append((char)FormatUtil.getCharUTF(x));
				 		        }
				 	      }
		
			        }
			        stbvaloracionRating = obtenerCadenaHTMLValidada(stbvaloracionRating);
			        
			       		
					String tableRatingbbva="";
					String[] FormatcamposRatingbbva={"descripcion","RIGHT","RIGHT","RIGHT"};
					String[] camposRatingbbva={"descripcion","totalAnio2","totalAnio1","totalAnioActual"};
					String cabeceraPersonalidad="";
					String[] cabeceraRatingbbva={"",anio2,anio3,anio4};
					String[] totalesRatingbbva={"","","",""};
					if(listaRating!=null && listaRating.size()>0){
						tableRatingbbva =GeneraTableHtml.getTableHTML(Rating.class,listaRating,camposRatingbbva,FormatcamposRatingbbva,
							Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraRatingbbva,totalesRatingbbva,Constantes.ID_NOADD_TOTAL,null);
					}
					sb.append("<h4><b><u>5."+ind+ ".- RATING: " + nombreEmpresGrupo+ "</u></b></h4>");
					sb.append(tableRatingbbva);
					sb.append("<br/>");
					
					sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>VALORACION DEL RATING:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbvaloracionRating.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				
				}
				
				rating= sb.toString();
		
	        } catch (Exception e) {
	        	logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return rating;
			}
		
		//fin MCG20130325
		
		//ini MCG20130429
		public String RatingPorEmpresaConSEHTML(String idprograma,Empresa oempresa,String indcapitulo,Tvariable oasignasalto,String codigoTitulo){
			String rating="";
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa2 = new Programa();
				programa2=programaBO.findById(Long.valueOf(idprograma));

				int anio = Integer.parseInt(programa2.getAnio().toString());			
				
				String tipo_empresa = programa2.getTipoEmpresa().getId().toString(); 
				
				if(tipo_empresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					return "";			
				}		
					
				int ind=1;
			
					String codEmpresaGrupo=oempresa.getCodigo();
					String nombreEmpresGrupo=oempresa.getNombre().trim();
					ind++;
				
				
					ratingBO.setPrograma(programa2);
					listaRating = ratingBO.findRating(programa2.getId(),codEmpresaGrupo);			
					
					Rating oratingCAb=new Rating();
					if (listaRating!=null && listaRating.size()>0){
							for (Rating orating1: listaRating){					
							if(orating1.getDescripcion()==null){
								oratingCAb=orating1;
								listaRating.remove(orating1);
								break;
							}
						}
				
					}
					String anio2="";
					String anio3="";
					String anio4="";
					//00-12-2011
					if (oratingCAb.getTotalAnio2()!=null && oratingCAb.getTotalAnio2().length()>6){
						anio2=oratingCAb.getTotalAnio2()==null?"":oratingCAb.getTotalAnio2().substring(0, 7);
					}
					if (oratingCAb.getTotalAnio1()!=null && oratingCAb.getTotalAnio1().length()>6){
						anio3=oratingCAb.getTotalAnio1()==null?"":oratingCAb.getTotalAnio1().substring(0, 7);
					}
					if (oratingCAb.getTotalAnioActual()!=null && oratingCAb.getTotalAnioActual().length()>6){
						anio4=oratingCAb.getTotalAnioActual()==null?"":oratingCAb.getTotalAnioActual().substring(0, 7);					
					}
					
			
			        StringBuilder stbvaloracionRating = new StringBuilder(); 
			
					
			    	RatingBlob ratingBlob = ratingBlobBO.findRatingBlobByPrograma(programa2,codEmpresaGrupo);
			        if (ratingBlob!=null){
		
				 	      if(ratingBlob.getValoracionRating()!=null){
				 		        for(byte x :ratingBlob.getValoracionRating()){
				 		        	stbvaloracionRating.append((char)FormatUtil.getCharUTF(x));
				 		        }
				 	      }
		
			        }
			        stbvaloracionRating = obtenerCadenaHTMLValidada(stbvaloracionRating);
			        
			        boolean bRating=false;
			        boolean bvaloracionRating=false;
			        
					String tableRatingbbva="";
					String[] FormatcamposRatingbbva={"descripcion","RIGHT","RIGHT","RIGHT"};
					String[] camposRatingbbva={"descripcion","totalAnio2","totalAnio1","totalAnioActual"};
					String cabeceraPersonalidad="";
					String[] cabeceraRatingbbva={"",anio2,anio3,anio4};
					String[] totalesRatingbbva={"","","",""};
					if(listaRating!=null && listaRating.size()>0){
						bRating=true;
						tableRatingbbva =GeneraTableHtml.getTableHTML(Rating.class,listaRating,camposRatingbbva,FormatcamposRatingbbva,
							Constantes.ID_SIADD_CABECERA,cabeceraPersonalidad,cabeceraRatingbbva,totalesRatingbbva,Constantes.ID_NOADD_TOTAL,null);
					}
					if (stbvaloracionRating.toString().length()>0){
						bvaloracionRating=true;
					}			

					if (!bRating && !bvaloracionRating){
						oasignasalto.setRatingSLinea(false);
						sb.append("<h4><b><u>"+ indcapitulo +".- RATING: " + nombreEmpresGrupo+ "</u></b><span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
						sb.append("<br/>");
						sb.append("<br/>");

					}else{
						oasignasalto.setRatingSLinea(true);
						sb.append("<h4><b><u>"+ indcapitulo +".- RATING: " + nombreEmpresGrupo+ "</u></b><span class=\"clsinvisible\"> "+codigoTitulo+"</span></h4>");
									
					}
					
				
					if (bRating ){
						sb.append(tableRatingbbva);
					}
					if (bvaloracionRating){
						sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>VALORACIÓN DEL RATING:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td>" + stbvaloracionRating.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					}
					if (bRating  || bvaloracionRating){
						sb.append("<br/>");
					}			
				
					rating= sb.toString();
		
	        } catch (Exception e) {
	        	logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return rating;
			}
		
		//fin MCG20130429

		

		public String AnalisisSectorialHTML(String idprograma,int indcapitulo,Tvariable oasignarsalto){
			String analisisSectorial="";
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa3 = new Programa();
				programa3.setId(Long.valueOf(idprograma));
				boolean bespacioLibreAS =false;
				String saltolinea="";

		        StringBuilder stbespacioLibreAS = new StringBuilder(); 
				
		    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa3);
		        if (programaBlob!=null){

			 	      if(programaBlob.getEspacioLibreAS()!=null){
			 		        for(byte x :programaBlob.getEspacioLibreAS() ){
			 		        	stbespacioLibreAS.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }		 
		        }
		        stbespacioLibreAS 	= obtenerCadenaHTMLValidada(stbespacioLibreAS); 
		        
		        if (stbespacioLibreAS.toString().length()>0){
		        	bespacioLibreAS=true;
		        	        	
		        }
		        
		        
				if ( !bespacioLibreAS ){
						oasignarsalto.setAnalisisSectorialSLinea(false);
						sb.append("<h4><b><u> "+ indcapitulo +".- ANÁLISIS SECTORIAL</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_ANALISIS_SECTO+"</span></h4>");
				        
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
						sb.append("<br/>");
						sb.append("<br/>");
						
					}else{	
						oasignarsalto.setAnalisisSectorialSLinea(true);
						sb.append("<h4><b><u> "+ indcapitulo +".- ANÁLISIS SECTORIAL</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_ANALISIS_SECTO+"</span></h4>");
				        //sb.append("<br/>");
						//sb.append("<br/>");			
				    	sb.append(" <table class=\"gridtable\" width=\"100%\">"    
						 		+ " <tr>" 		
				        		+ " 	<th>COMENTARIO ANÁLISIS SECTORIAL:</th>"		        	
				        		+ " </tr>" 
				        		+ " <tr>" 		        		
				        		+ " 	<td class=\"myEditor\">" + stbespacioLibreAS.toString() + "</td>"
				        		+ " </tr>"    
				        		+ " </table>");	
				    	sb.append("<br/>");
						sb.append("<br/>");
					}
		        
					
				//sb.append("<br/>");
				
				
				analisisSectorial= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return analisisSectorial;
		}
		
		//INICIO : Metodo agrega clasificacion financiera y Clasificacion Banco
		
		public String cuadroClasificacionFinanciera(Programa programa){
			String tablaClasificacionFinanciera = "";
			StringBuilder cadenaHtml = new StringBuilder();
			
			String porNormal = programa.getPorcentajeNormalSF()==null?"":programa.getPorcentajeNormalSF();
			String porCPP = programa.getPorcentajeProblemaPotencialSF()==null?"":programa.getPorcentajeProblemaPotencialSF();
			String porDeficiente = programa.getPorcentajeDeficienteSF()==null?"":programa.getPorcentajeDeficienteSF();
			String porDudoso = programa.getPorcentajeDudosoSF()==null?"":programa.getPorcentajeDudosoSF();
			String porPerdida = programa.getPorcentajePerdidaSF()==null?"":programa.getPorcentajePerdidaSF();
			
			cadenaHtml.append("<table class=\"gridtable\" style=\" width:100%;\" >");
//				cadenaHtml.append("<tr>");
//					cadenaHtml.append("<th style=\" width:100px;\" align=\"center\">CLASIFICACION FINANCIERA</th>");			
//					cadenaHtml.append("<th style=\" width:60px;\" align=\"center\">NORMAL(0)</th>");
//					cadenaHtml.append("<th style=\" width:60px;\" align=\"center\">CPP(1)</th>");
//					cadenaHtml.append("<th style=\" width:60px;\" align=\"center\">DEFICIENTE(2)</th>");
//					cadenaHtml.append("<th style=\" width:60px;\" align=\"center\">DUDOSO(3)</th>");
//					cadenaHtml.append("<th style=\" width:60px;\" align=\"center\">PERDIDA(4)</th>");			
//				cadenaHtml.append("</tr>");
				
				cadenaHtml.append("<tr>");
				cadenaHtml.append("<th style=\" width:55px;\"  align=\"center\">CLASIFICACIÓN SBS</th>");	
				cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">NORMAL(0)</td>");
					cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">"+porNormal + addPorcentaje(porNormal)+"</td>");	
				cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">CPP(1)</td>");
					cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">"+porCPP + addPorcentaje(porCPP)+"</td>");	
				cadenaHtml.append("<td style=\" width:40px;\" align=\"center\">DEFICIENTE(2)</td>");
					cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">"+porDeficiente + addPorcentaje(porDeficiente)+"</td>");	
				cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">DUDOSO(3)</td>");
					cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">"+porDudoso + addPorcentaje(porDudoso)+"</td>");
				cadenaHtml.append("<td style=\" width:40px;\" align=\"center\">PÉRDIDA(4)</td>");
					cadenaHtml.append("<td style=\" width:30px;\" align=\"center\">"+porPerdida + addPorcentaje(porPerdida)+"</td>");
				cadenaHtml.append("</tr>");
								
					
			cadenaHtml.append("</table>");
			cadenaHtml.append("</br>");
			tablaClasificacionFinanciera = cadenaHtml.toString();
			return tablaClasificacionFinanciera;
		}
		private String addPorcentaje(String valor){
			String porcentaje="";
			if (!valor.equals("")){
				porcentaje="%";
			}		
			return porcentaje;
		}
		
		public String cuadroClasificacionBanco(Programa programa){
			String tablaClasificacionBanco = "";
			StringBuilder cadenaHtml = new StringBuilder();
			String clasificacionBanco = programa.getCalificacionBanco()==null?"":programa.getCalificacionBanco();
			clasificacionBanco=relacionesBancariasBO.obtenerEquivClasificacionBanco(clasificacionBanco);
			cadenaHtml.append("<table class=\"gridtable\" style=\" width:300px;\" >");
			cadenaHtml.append("<th colspan=\"2\" >CLASIFICACIÓN BANCO:</th>");
			cadenaHtml.append("<tr>");
			cadenaHtml.append("<td>Clasificación </td>");
			cadenaHtml.append("<td align=\"center\">"+ clasificacionBanco + "</td>");
			cadenaHtml.append("</tr>");
			cadenaHtml.append("</table>");
			cadenaHtml.append("</br>");
			tablaClasificacionBanco = cadenaHtml.toString();
			return tablaClasificacionBanco;
		}
		//FIN : Metodo agrega clasificacion financiera y Clasificacion Banco
		
		public String RelacionesBancariasHTML(String Idprograma,int  indcapitulo,Tvariable oasignasalto){
			String relacionesbancarias="";
			
			StringBuilder sb=new StringBuilder ();		
			try {
			
						//String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
						oasignasalto.setRelacioBancatiasSLinea(true);
						Programa oprogramaini=new Programa();
						oprogramaini=programaBO.findById(Long.valueOf(Idprograma));
						setPrograma(oprogramaini);						
						String strEmpresa = programa.getNombreGrupoEmpresa();
						String strTipoRentabilidad=programa.getIdmodeloRentabilidad()==null?"2":programa.getIdmodeloRentabilidad();
						
						Programa programa4 = new Programa();
						programa4.setId(Long.valueOf(Idprograma));

				        StringBuilder stbcomenLineas = new StringBuilder(); 
				        StringBuilder stbcomenClientGlob=new StringBuilder();
				        StringBuilder stbcomenBEC=new StringBuilder();
				        StringBuilder stbcomenPoolBanc=new StringBuilder();
				        StringBuilder stbcomentIndTransaccional=new StringBuilder();
				        
				    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa4);
				        if (programaBlob!=null){

					 	      if(programaBlob.getComenLineas()!=null){
					 		        for(byte x :programaBlob.getComenLineas() ){
					 		        	stbcomenLineas.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	      if(programaBlob.getRentaModelGlobal()!=null){
					 		        for(byte x :programaBlob.getRentaModelGlobal() ){
					 		        	stbcomenClientGlob.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }
					 	     if(programaBlob.getRentaModelBEC()!=null){
					 		        for(byte x :programaBlob.getRentaModelBEC() ){
					 		        	stbcomenBEC.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }
					 	    if(programaBlob.getComenPoolBanc()!=null){
				 		        for(byte x :programaBlob.getComenPoolBanc() ){
				 		        	stbcomenPoolBanc.append((char)FormatUtil.getCharUTF(x));
				 		        }
				 	        }
					 	    //ini MCG20151002
					 	    if(programaBlob.getComentIndTransaccional()!=null){
							 		     for(byte x :programaBlob.getComentIndTransaccional()){
							 		    	stbcomentIndTransaccional.append((char)FormatUtil.getCharUTF(x));
							 		     }
							}
					 	    //fin MCG20151002

				        }
				        
				        stbcomenLineas 		=	obtenerCadenaHTMLValidada(stbcomenLineas); 
				        stbcomenClientGlob	=	obtenerCadenaHTMLValidada(stbcomenClientGlob);
				        stbcomenBEC			=	obtenerCadenaHTMLValidada(stbcomenBEC);
				        stbcomenPoolBanc	=	obtenerCadenaHTMLValidada(stbcomenPoolBanc);
				        stbcomentIndTransaccional	=	obtenerCadenaHTMLValidada(stbcomentIndTransaccional);
													
//						loadClasificacionSistemaFinanciero2(getPrograma(),Constantes.TIPO_DOCUMENTO_RUC);					
//						loadCalificacionBanco(getPrograma(),Constantes.TIPO_DOCUMENTO_RUC);
//						loadEfectividadCartera(getPrograma(),Constantes.ID_CODIGO_SERVICIO);
						//loadLineasRelacionesBancarias(Idprograma);
						listLineasRelacionesBancarias = relacionesBancariasBO.findByLineasRelacionesBancarias(Long.valueOf(Idprograma));
						float ta1 = 0;
						float ta2 = 0;
						float ta3 = 0;
						float ta4 = 0;
						float ta5 = 0;
						float ta6 = 0;
						float ta7 = 0;
						float ta8 = 0;
						float ta9 = 0;
						float ta10 = 0;					
						for(LineasRelacionBancarias lrb : listLineasRelacionesBancarias){
							
							ta1 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrclteform()));
							ta2 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrcdpto()));
							ta3 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrplteform()));
							ta4 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrpdpto()));
							ta5 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrflteform()));	
							ta6 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgrfdpto()));
							ta7 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgtesolteform()));
							ta8 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgtesodpto()));
							ta9 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgtotallteform()));
							ta10 += Float.valueOf(FormatUtil.FormatNumeroSinComa(lrb.getSgtotaldpto()));
							
						}
						String Total1=""+FormatUtil.roundTwoDecimalsPunto(ta1);
						String Total2=""+FormatUtil.roundTwoDecimalsPunto(ta2);
						String Total3=""+FormatUtil.roundTwoDecimalsPunto(ta3);
						String Total4=""+FormatUtil.roundTwoDecimalsPunto(ta4);
						String Total5=""+FormatUtil.roundTwoDecimalsPunto(ta5);
						String Total6=""+FormatUtil.roundTwoDecimalsPunto(ta6);
						String Total7=""+FormatUtil.roundTwoDecimalsPunto(ta7);
						String Total8=""+FormatUtil.roundTwoDecimalsPunto(ta8);
						String Total9=""+FormatUtil.roundTwoDecimalsPunto(ta9);
						String Total10=""+FormatUtil.roundTwoDecimalsPunto(ta10);
			
						
						
//						CargarEmpresasRelacionesBancarias();
//						cargarTipoDeuda();
//						cargarBanco();
//						cargarModeloRentabilidad();
						/////////EPOMAYAY 16022012
						String idtipoMilesRB = programa.getTipoMilesRB()==null?
											 Constantes.ID_TABLA_TIPOMILES_MILES.toString():
											 programa.getTipoMilesRB().toString();
						Tabla otipoMilesRB=new Tabla();
						otipoMilesRB=tablaBO.obtienePorId(Long.valueOf(idtipoMilesRB));
						String strTipoMilesRB=otipoMilesRB.getDescripcion();
						sb.append("<h4><b><u> "+ indcapitulo +".- RELACIONES BANCARIAS</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_RELACIONES_BANC+"</span></h4>");						
						//sb.append("<br/>");
						//sb.append("<br/>");
						//INICIO :  Se agrega clasificacion financiera y clasificacion banco
						
						String strClasificacionFinanciera = cuadroClasificacionFinanciera(programa);
						sb.append(strClasificacionFinanciera);
						
						String strClasificacionBanco =cuadroClasificacionBanco(programa);
						String clasifbanc=programa.getCalificacionBanco()==null?"":programa.getCalificacionBanco();
						if (clasifbanc.length()>0 ){
							sb.append(strClasificacionBanco);
						}
						//FIN :  Se agrega clasificacion financiera y clasificacion banco
						
						if (false){
						sb.append("<br/>");//Se agrega
						sb.append(" LINEAS: CIFRAS EN " +
								  strTipoMilesRB +
								  " USD");
						/////////
						String tableLineaRB;
						String[] FormatcamposLineaRB={"linea","RIGHT","RIGHT","RIGHT","RIGHT","RIGHT",
								"RIGHT","RIGHT","RIGHT","RIGHT","RIGHT"};
						String[] camposLineaRB={"linea","sgrclteform","sgrcdpto","sgrplteform","sgrpdpto","sgrflteform",
												"sgrfdpto","sgtesolteform","sgtesodpto","sgtotallteform","sgtotaldpto"};
						String cabeceraPersonalidad="<tr>"
														+ "	<th align=\"center\" rowspan=\"3\">POR FILIAL/SUBGRUPO</th><th align=\"center\" colspan=\"2\">RIESGO COMERCIAL</th>"
														+ "	<th align=\"center\" colspan=\"2\">RIESGO PURO</th><th align=\"center\" colspan=\"2\">RIESGO DE FIRMA</th>"
														+ "	<th align=\"center\" colspan=\"2\">TESORERÍA</th><th align=\"center\" colspan=\"2\">TOTAL</th>"
													+ "</tr>"
													+ "<tr>"
													+ "	<th align=\"center\">LIMITE</th><th>&nbsp</th>"
													+ "	<th align=\"center\">LIMITE</th><th>&nbsp</th>"
												+ "	<th align=\"center\">LIMITE</th><th>&nbsp</th>"
													+ "	<th align=\"center\">LIMITE</th><th>&nbsp</th>"
													+ "	<th align=\"center\">LIMITE</th><th>&nbsp</th>"
													+ "</tr>"
													+ "<tr>"
														+ "	<th align=\"center\">FORMALIZADO</th><th align=\"center\">DPTO</th>"
														+ "	<th align=\"center\">FORMALIZADO</th><th align=\"center\">DPTO</th>"
														+ "	<th align=\"center\">FORMALIZADO</th><th align=\"center\">DPTO</th>"
														+ "	<th align=\"center\">FORMALIZADO</th><th align=\"center\">DPTO</th>"
														+ "	<th align=\"center\">FORMALIZADO</th><th align=\"center\">DPTO</th>"
													+ "</tr>";					
						String[] cabeceraLineaRB={""}; 
						String[] totalesLineaRB={"Total Grupo",Total1,Total2,Total3,Total4,Total5,Total6,Total7,Total8,Total9,Total10};
						tableLineaRB =GeneraTableHtml.getTableHTMLTamanio(LineasRelacionBancarias.class,listLineasRelacionesBancarias,camposLineaRB,FormatcamposLineaRB,
								Constantes.ID_NOADD_CABECERA,cabeceraPersonalidad,cabeceraLineaRB,totalesLineaRB,Constantes.ID_SIADD_TOTAL,"6");// 6 pixeles
						sb.append(tableLineaRB);
						}
						if (stbcomenLineas.toString().length()>0){
							//sb.append("<br/>");			
							//Detalle de Lineas y operaciones aprobadas
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>DETALLE DE LÍNEAS Y OPERACIONES APROBADAS:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomenLineas.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");
							sb.append("<br/>");
						}
						String cuotaFinanciera="";
						String periodoArchivo ="";
						try {
							periodoArchivo = relacionesBancariasBO.obtenerFechaCuotaFinancieraEmpresa(programa);
							
							if (programa.getCuotaFinanciera()!=null){
								cuotaFinanciera=programa.getCuotaFinanciera();
							}else {						
								String idTipoEmpresa = oprogramaini.getTipoEmpresa().getId().toString();
								
								if(idTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
									cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraEmpresa(oprogramaini)+"";
								}else{
									List<Empresa>  listaEmpersas=new ArrayList<Empresa> ();
									listaEmpersas = empresaBO.listarEmpresasPorPrograma(oprogramaini.getId());							
									cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraGrupo(oprogramaini, listaEmpersas)+"";
								}
							}
							
						} catch (Exception e) {
							 
						}
						boolean bcomenCuota=false;
						boolean cuotaFinan=false;
						boolean fechaArchivo=false;
						
						String strComentcuotaFinanciera="";
						strComentcuotaFinanciera=obtenerCadenaHTMLValidada(programa.getComentcuotaFinanciera()==null?"":programa.getComentcuotaFinanciera());
						if (strComentcuotaFinanciera!=null && strComentcuotaFinanciera.length()>0){
							bcomenCuota=true;
						}
						if (cuotaFinanciera!=null && cuotaFinanciera.length()>0){
							cuotaFinan=true;
						}
						if (periodoArchivo!=null && periodoArchivo.length()>0){
							fechaArchivo=true;
						}
						
						if (bcomenCuota||cuotaFinan||fechaArchivo){
							sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th colspan=\"4\">CUOTA FINANCIERA BBVA:</th>"			        					        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td style=\"width:25%\">Cuota Financiera</td>"	
					        		+ " 	<td style=\"width:25%\">" + cuotaFinanciera + "</td>"
					        		+ " 	<td style=\"width:25%\">Periodo</td>"	
					        		+ " 	<td style=\"width:25%\">" + periodoArchivo + "</td>"	
					        		+ " </tr>"  
					        		+ " <tr>" 		        		
					        		+ " 	<td colspan=\"4\">" + obtenerCadenaHTMLValidada(programa.getComentcuotaFinanciera()==null?"":programa.getComentcuotaFinanciera()) + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
							sb.append("<br/>");
						}
						
						boolean bpoolbanca=false;
						boolean bcomentpoolbanc =false;
						boolean bcomentIndTransaccional =false;
						
						String strpoolbancari=PoolBancarioBD(Idprograma);
						if (strpoolbancari!=null && strpoolbancari.length()>0){
							bpoolbanca=true;
						}
						if (stbcomenPoolBanc!=null && stbcomenPoolBanc.length()>0){
							bcomentpoolbanc=true;
						}
						if (stbcomentIndTransaccional!=null && stbcomentIndTransaccional.length()>0){
							bcomentIndTransaccional=true;
						}
						
						if (bpoolbanca){					
							sb.append( " <table class=\"gridtable\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>POOL BANCARIO: IMPORTE EXPRESADO EN MILES DE SOLES</th>"			        		
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td>" + strpoolbancari + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");
							sb.append("<br/>");	
						}
						
						if (!bpoolbanca && bcomentpoolbanc ){
							sb.append( " <table class=\"gridtable\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>POOL BANCARIO:</th>"			        		
					        		+ " </tr>" 
					        		+ " </table>");
						}
							
						if (bcomentpoolbanc){	
							sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
					        		+ " <tr>" 	
					        		+ " 	<td>COMENTARIO POOL BANCARIO:</td>"		        		
					        		+ " </tr>"  
					        		+ " <tr>" 			        		
					        		+ " 	<td class=\"myEditor\">" + stbcomenPoolBanc + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");				
							sb.append("<br/>");
						}
					
						if (strTipoRentabilidad.equals(Constantes.ID_TIPO_BANCAEMPRESA)){									
							String strRentabilidad=ModeloRentabilidadBD(Idprograma);
							
							boolean brentabi=false;
							boolean bcomentrenta =false;
							
							if (strRentabilidad!=null && strRentabilidad.length()>0){
								brentabi=true;
							}
							if (stbcomenBEC!=null && stbcomenBEC.length()>0){
								bcomentrenta=true;
							}
							
							if (brentabi){
							sb.append( " <table style=\"width:100%\" class=\"gridtable\">"    
							 		+ " <tr><th>MODELO DE RENTABILIDAD: " + Constantes.TIPO_BANCAEMPRESA + " - IMPORTE EXPRESADO EN MILES DE SOLES</th></tr>" 
							 		//+ " <tr><td>" + strEmpresa + "</td></tr>"
					        		+ " <tr>" 		        		
					        		+ " 	<td>" + strRentabilidad + "</td>"
					        		+ " </tr>"  
					        		+ " </table>");					
							sb.append("<br/>");
							}
							
							if (!brentabi && bcomentrenta){
								sb.append( " <table class=\"gridtableComent\" width=\"100%\">"    
								 		+ " <tr>" 		
						        		+ " 	<td>MODELO DE RENTABILIDAD - BEC : " + "BANCA EMPRESA"+ "</td>"		        	
						        		+ " </tr>"   
						        		+ " </table>");	
							}
							
							if (bcomentrenta){
							sb.append( " <table class=\"gridtableComent\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<td>COMENTARIO MODELO DE RENTABILIDAD - BEC : " + Constantes.TIPO_BANCAEMPRESA + "</td>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomenBEC.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
							sb.append("<br/>");
							}
							
						}else{

							if (stbcomenClientGlob!=null && stbcomenClientGlob.length()>0){
						    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
								 		+ " <tr>" 		
						        		+ " 	<th>MODELO DE RENTABILIDAD: " + Constantes.TIPO_CLIENTEGLOBAL + "</th>"		        	
						        		+ " </tr>" 
						        		+ " <tr>" 		        		
						        		+ " 	<td class=\"myEditor\">" + stbcomenClientGlob.toString() + "</td>"
						        		+ " </tr>"    
						        		+ " </table>");					
								sb.append("<br/>");
							}
						}
						
		
						
						String fechaindtrasa="";
						
						String strsaldoMedio="";
						String strcaja="";
						String strflujoTransaccional="";
						String strventaCostoVenta="";
						String strdivsaldo="";
						String strdivflujo="";
						
						try {
							
						fechaindtrasa=programa.getFechaIndiceTransa()==null?"":programa.getFechaIndiceTransa();
						
						strsaldoMedio=programa.getSaldoMedioRecGest()==null?"0":programa.getSaldoMedioRecGest().toString();
						strcaja=programa.getCaja()==null?"0":programa.getCaja().toString();
						strflujoTransaccional=programa.getFlujoTransaccional()==null?"0":programa.getFlujoTransaccional().toString();
						strventaCostoVenta=programa.getVentaCostoVenta()==null?"0":programa.getVentaCostoVenta().toString();
						
						double saldoMedio=Double.valueOf(FormatUtil.FormatNumeroSinComa(strsaldoMedio));
						double caja=Double.valueOf(FormatUtil.FormatNumeroSinComa(strcaja));
						double flujoTransaccional=Double.valueOf(FormatUtil.FormatNumeroSinComa(strflujoTransaccional));
						double ventaCosto=Double.valueOf(FormatUtil.FormatNumeroSinComa(strventaCostoVenta));
						
						double divsaldo=0;
						double divflujo;
						
						if (caja>0){
							divsaldo=saldoMedio/caja;
							strdivsaldo=""+FormatUtil.roundTwoDecimalsPunto(divsaldo);						
						}				
						if (ventaCosto>0){
							divflujo=flujoTransaccional/ventaCosto;	
							strdivflujo=""+FormatUtil.roundTwoDecimalsPunto(divflujo);
						}
						
						
						} catch (Exception e) {
							 
						}
						boolean flagindtransa=false;
						
						if (fechaindtrasa.length()>0 ||strdivsaldo.length()>0 ||strdivflujo.length()>0){
							flagindtransa=true;					
						}
						
						if (flagindtransa){
						//sb.append("<br/>");
						sb.append("<br/>");
						
						sb.append( "<table style=\"width:100%\" class=\"gridtable\">"
								+"<tr>"
								+"	<th>ÍNDICE TRANSACCIONAL</th>"							
								+"</tr>"	
								+"<tr>"
								+"	<td>");
								
							sb.append( "<table style=\"width:80%\" class=\"gridtableComent\">"
//									+"<tr>"
//									+"	<th colspan=\"4\">INDICE TRANSACCIONAL</th>"							
//									+"</tr>"
									+"<tr>"
									+"	<th colspan=\"3\">&nbsp;</th>"
									+"	<th align=\"center\">Fecha</th>"
									+"</tr>"						
									+"<tr>"
									+"	<td colspan=\"3\">&nbsp;</td>"
									+"	<td align=\"center\" valign=\"middle\">"+fechaindtrasa
									+"	</td>"									
									+"</tr>"	
									+"<tr>"
									+"	<td align=\"center\" valign=\"middle\">Saldo Medio Recursos Gestionados</td>"
									+"	<td align=\"center\" valign=\"middle\">"+ (programa.getSaldoMedioRecGest()==null?"":programa.getSaldoMedioRecGest())+"</td>"
									+"	<td align=\"center\" valign=\"middle\" rowspan=\"2\">&nbsp;&nbsp;&nbsp;=&nbsp;&nbsp;&nbsp;</td>"
									+"	<td  align=\"center\" valign=\"middle\" rowspan=\"2\">"	+ (strdivsaldo)	+"</td>"									
									+"</tr>"	
		
									+"<tr>"
									+"	<td align=\"center\" valign=\"middle\">Caja (*)</td>"
									+"	<td align=\"center\" valign=\"middle\" style=\"border-top-style:solid;border-top-color:gray\">"+(programa.getCaja()==null?"":programa.getCaja())+"</td>"
									+"</tr>	"
									+"<tr>"
									+"<td colspan=\"4\">&nbsp;</td>"
									+"</tr>"
									+"<tr>"
									+"	<td align=\"center\" valign=\"middle\">Flujos Transaccionales</td>"
									+"	<td align=\"center\" valign=\"middle\">"+(programa.getFlujoTransaccional()==null?"":programa.getFlujoTransaccional())+"</td>"
									+"	<td align=\"center\" valign=\"middle\" rowspan=\"2\">&nbsp;&nbsp;&nbsp;=&nbsp;&nbsp;&nbsp;</td>"
									+"	<td align=\"center\" valign=\"middle\" rowspan=\"2\">"+	(strdivflujo)+"	</td>"
									+"</tr>"
									+"<tr>"							
									+"	<td align=\"center\" valign=\"middle\">Ventas + Costo de Venta (*)</td>"
									+"	<td align=\"center\" valign=\"middle\" style=\"border-top-style:solid;border-top-color:gray\">"+( programa.getVentaCostoVenta()==null?"":programa.getVentaCostoVenta())+"</td>"
									+"</tr>"
									+"<tr>"
									+"  <td colspan=\"4\" >&nbsp;</td>"
									+"</tr>"								
									+"<tr>"
									+"  <td colspan=\"4\" >(*) Dato de último ejercicio</td>"
									+"</tr>"
									+"</table>"
									
							+"	</td>"							
							+"</tr>"
							+"</table>");

							
							}
						if (bcomentIndTransaccional){	
							sb.append( " <table class=\"gridtableComent\" width=\"100%\">" 
					        		+ " <tr>" 	
					        		+ " 	<td>COMENTARIO INDICE TRANSACCIONAL:</td>"		        		
					        		+ " </tr>"  
					        		+ " <tr>" 			        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentIndTransaccional + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");				
							sb.append("<br/>");
						}
						
						List<Comex> listaComex=new ArrayList<Comex>();
						List<Comex> listaComexImportacion=new ArrayList<Comex>();
						List<Comex> listaComexExportacion=new ArrayList<Comex>();
						Map<String, String> mapListaDescripcionComex = new HashMap<String, String>();
						String codCentral=oprogramaini.getIdEmpresa();
						String periodoComex="" ;
						String ratioReprocidadImp="";
						String ratioReprocidadExp="";
						boolean flagcomex=false;
						try {
							mapListaDescripcionComex = tablaBO.getMapTablasByCodigoPadre(Constantes.COD_DESC_COMEX1500);
							listaComex=relacionesBancariasBO.obtenerComex(Idprograma, codCentral,false);
							if (listaComex!=null && listaComex.size()>0){
								flagcomex=true;
								periodoComex=listaComex.get(0).getAnio()==null?"":listaComex.get(0).getAnio();
								
							}
							listaComexImportacion=relacionesBancariasBO.obtenerComexByType(listaComex,"I");
							listaComexExportacion=relacionesBancariasBO.obtenerComexByType(listaComex,"E");	
							
							ratioReprocidadImp=relacionesBancariasBO.ObtenerRatioReprocidadImp(listaComexImportacion,mapListaDescripcionComex);
							ratioReprocidadExp=relacionesBancariasBO.ObtenerRatioReprocidadExp(listaComexExportacion,mapListaDescripcionComex);
						
							
						} catch (Exception e) {
							flagcomex=false;	
							logger.error(StringUtil.getStackTrace(e));
						}
						
							if (flagcomex){
								sb.append("<br/>");
								
								sb.append( "<table style=\"width:100%\" class=\"gridtable\">"
										+"<tr>"
										+"	<th>EXPORTACIONES E IMPORTACIONES</th>"							
										+"</tr>"	
										+"<tr>"
										+"	<td>");
								
											sb.append( "<table style=\"width:90%\" class=\"gridtable\">"						
															
													+"<tr>"
													+"	<th colspan=\"3\" align=\"center\">"
													+"		&nbsp;"
													+"	</th>"												
														
													+"	<th align=\"center\">"
													+"		PERIODO"
													+"	</th>"
													+"	<th align=\"center\">"
													+ periodoComex
													+"	</th>"
													+"</tr>"			
													+"<tr>"
													+"	<th  colspan=\"2\" align=\"center\">"
													+"		&nbsp;"
													+"	</th>"												
													+"	<th align=\"center\" style=\"width: 120px;\">"
													+"		CANTIDAD"
													+"	</th>"
													+"	<th align=\"center\" style=\"width: 120px;\">"
													+"		IMPORTE ACUMULADO"
													+"	</th>"
													+"	<th align=\"center\" style=\"width: 120px;\">"
													+"		COMISIONES ACUMULADAS"
													+"	</th>"
													+"</tr>");
										   
										   	if (listaComexImportacion!=null && listaComexImportacion.size()>0){
										   		int contImp=0;
												for (Comex ocomexImp :listaComexImportacion){				
												
														if (contImp==0){
															contImp=contImp+1;
														  
														   sb.append("<tr>"
																	+"<td rowspan=\""+listaComexImportacion.size()+"\" align=\"center\" style=\"font-weight: bold;\">"
															  		+"	IMPORTACIONES"
															  		+"</td>"
															  		+"<td align=\"left\">"
															  		+(ocomexImp.getDescripcion()==null?"":ocomexImp.getDescripcion().toString())										   
															  		+"</td>"
															  		+"<td align=\"right\">"
															  		+(ocomexImp.getCantidad()==null?"":ocomexImp.getCantidad().toString())
															  		+"</td>"
															  		+"<td align=\"right\">"
															  		+(ocomexImp.getImporteAcumulado()==null?"":ocomexImp.getImporteAcumulado().toString())
															  		+"</td>"
															  		+"<td align=\"right\">"
															  		+(ocomexImp.getComisionesAcumuladas()==null?"":ocomexImp.getComisionesAcumuladas().toString())
															  		+"</td>"																				
															  		+"</tr>");
										
														}else
														{  
														
															sb.append("<tr>"
																	+"<td align=\"left\">"
																	+(ocomexImp.getDescripcion()==null?"":ocomexImp.getDescripcion().toString())										   
																	+"</td>"
																	+"<td align=\"right\">"
																	+(ocomexImp.getCantidad()==null?"":ocomexImp.getCantidad().toString())
																	+"</td>"
																	+"<td align=\"right\">"
																	+(ocomexImp.getImporteAcumulado()==null?"":ocomexImp.getImporteAcumulado().toString())
																	+"</td>"
																	+"<td align=\"right\">"
																	+(ocomexImp.getComisionesAcumuladas()==null?"":ocomexImp.getComisionesAcumuladas().toString())
																	+"</td>"																				
																	+"</tr>");	
														}							
												}
											}
											sb.append("<tr>"
													+"<td colspan=\"5\" align=\"center\">"
													+"	&nbsp;"
													+"</td>"			
													+"</tr>");
											
										
										if (listaComexImportacion!=null && listaComexImportacion.size()>0){
											int contExp=0;
											for (Comex ocomexExp :listaComexExportacion){							
													if (contExp==0){
														contExp=contExp+1;		
																				  
														sb.append("<tr>"
																+"<td rowspan=\""+ listaComexExportacion.size() +"\" style=\"font-weight: bold;\" align=\"center\">"
																+"	EXPORTACIONES"
																+"</td>"
																+"<td align=\"left\">"
																+(ocomexExp.getDescripcion()==null?"":ocomexExp.getDescripcion().toString())	
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getCantidad()==null?"":ocomexExp.getCantidad().toString())
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getImporteAcumulado()==null?"":ocomexExp.getImporteAcumulado().toString())
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getComisionesAcumuladas()==null?"":ocomexExp.getComisionesAcumuladas().toString())
																+"</td>"
																+"</tr>");
													}else{										
														sb.append("<tr>"
																+"<td align=\"left\">"
																+(ocomexExp.getDescripcion()==null?"":ocomexExp.getDescripcion().toString())	
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getCantidad()==null?"":ocomexExp.getCantidad().toString())
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getImporteAcumulado()==null?"":ocomexExp.getImporteAcumulado().toString())
																+"</td>"
																+"<td align=\"right\">"
																+(ocomexExp.getComisionesAcumuladas()==null?"":ocomexExp.getComisionesAcumuladas().toString())
																+"</td>"
																										
																+"</tr>");
													}								
											}
										}
										sb.append("</table>");
								
								sb.append("	</td>"							
									+"</tr>"
									+"</table>");
								
								
								sb.append("<br/>");							
								sb.append("<table style=\"width:250px\" class=\"gridtable\">");							
									sb.append("<tr>");								
										sb.append("<th  colspan=\"3\">Ratios de Reciprocidad</th>");
										sb.append("<th>&nbsp;</th>");
									sb.append("</tr>");	
									
									sb.append("<tr>");								
										sb.append("<td colspan=\"3\">Importaciones</td>");
										sb.append("<td align=\"center\" valign=\"middle\">");
											sb.append(ratioReprocidadImp);
										sb.append("</td>");								
									sb.append("</tr>");
									
									sb.append("<tr>");								
										sb.append("<td colspan=\"3\">Exportaciones</td>");
										sb.append("<td align=\"center\" valign=\"middle\">");
											sb.append(ratioReprocidadExp);
										sb.append("</td>");									
									sb.append("</tr>");
									
								sb.append("</table>");
								
							}	
							
							

					
				
				relacionesbancarias= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return relacionesbancarias;
		}
		
		
		
		public String PoolBancarioBD(String Idprograma) {
		
			String strPoolBancario="";
			List<List> listPoolBancarioTotal = new ArrayList<List>();
			String codBanco = "";		
			List<Empresa> listEmpresat =new ArrayList<Empresa>();		
			
			StringBuilder sb=new StringBuilder ();
			try {

				//String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				setPrograma(programaBO.findById(Long.valueOf(Idprograma)));	
				String strEmpresa = programa.getNombreGrupoEmpresa();			
				String codTipoEmpresaGrupo=programa.getTipoEmpresa().getId().toString();
				String oruc=programa.getRuc()==null?"":programa.getRuc().toString();
				List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
				List<Tempresa> listaEmpresa = new ArrayList<Tempresa>();
				
				
				listaopcionPool=relacionesBancariasBO.findOpcionPool(Idprograma, Constantes.ID_TIPO_OPPOOL_EMPRESA);
				if (listaopcionPool!=null && listaopcionPool.size()>0){
					for (OpcionPool opcionPool: listaopcionPool){
						Tempresa otempresa=new Tempresa();
						otempresa.setCodEmpresa(opcionPool.getCodOpcionPool());
						otempresa.setNomEmpresa(opcionPool.getDescOpcionPool());
						listaEmpresa.add(otempresa);
					}
				}
				
				List<OpcionPool> listaopcionPool1= new ArrayList<OpcionPool>();			
				listaopcionPool1=relacionesBancariasBO.findOpcionPool(Idprograma, Constantes.ID_TIPO_OPPOOL_BANCO);
				String strbanco="";
				if (listaopcionPool1!=null && listaopcionPool1.size()>0){				
					for (OpcionPool opcionPool: listaopcionPool1){					
						strbanco+=opcionPool.getCodOpcionPool()+",";	
					}
				}
				//EPOMAYAY 01052012
				logger.info("----------->codBanco ="+strbanco.length());
				if(strbanco.length() == 0){
					return "";
				}else{
					codBanco=strbanco.substring(0, strbanco.length()-1);
				}
				
				List<OpcionPool> listaopcionPool2= new ArrayList<OpcionPool>();			
				listaopcionPool2=relacionesBancariasBO.findOpcionPool(Idprograma, Constantes.ID_TIPO_OPPOOL_TIPODEUDA);
							
					
				//ini grupo economico
				if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){				
							sb.append("<table>");
							sb.append("<tr><td><b>Deuda Grupo: " + strEmpresa + "</b></td></tr>");
							sb.append("</table>");
							
			
							if (listaopcionPool2!=null && listaopcionPool2.size()>0){
								for (OpcionPool opcionPool: listaopcionPool2) {
									listPoolBancarioTotal = new ArrayList<List>();
									String CabeceraTipoDeuda="";
									if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_TOTAL;
										listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_TOTAL,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									}else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_DIRECTA )) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA;
										listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_DIRECTA,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									}else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
										listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_INDIRECTA,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									} 
				
									// DEUDA 
									if (listPoolBancarioTotal != null && listPoolBancarioTotal.size() > 0) {
										sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
										sb.append("<table class=\"gridtable\">");
				
										StringBuilder sbcab1=new StringBuilder ();
										StringBuilder sbcab2=new StringBuilder ();
										sbcab1.append("<tr>");
										int conts=0;
										int conts1=0;
										int conts2=0;
										Map<String, String> hm = new HashMap<String, String>();

										for (Object lista : listPoolBancarioTotal.get(0)) {
											hm = (HashMap<String, String>) lista;
				
											Iterator it = hm.entrySet().iterator();
											while (it.hasNext()) {
												Map.Entry e = (Map.Entry) it.next();
												
												conts=conts+1;
												if(e.getKey().toString().substring(0, 3).equals(Constantes.ABREV_NOMB_BANCO_CTA)){
													conts2=conts2+1;											
												}	
												
												if(e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA)||
												   e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA)){
													sbcab1.append("<th style=\"background-color: yellow;color: #666666\">" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
												}else{
													sbcab1.append("<th>" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
												}
											}
										}	
										conts1=conts-conts2-1;
										sbcab1.append("</tr>");
										
										sbcab2.append("<tr>");
											sbcab2.append("<th></th>");
											if (conts1>0){									
												sbcab2.append("<th align=\"center\" colspan=\""+conts1+"\">IMPORTE</th>");
											}
											if (conts2>0){
												sbcab2.append("<th align=\"center\" colspan=\""+conts2+"\">CUOTA</th>");
											}
										sbcab2.append("</tr>");
									
										sb.append(sbcab2.toString());
										sb.append(sbcab1.toString());	
										
				
										for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
											sb.append("<tr>");
											Map<String, String> hmr = new HashMap<String, String>();
											for (Object x : listPoolBancarioTotal.get(i)) {
												hmr = (HashMap<String, String>) x;
				
												Iterator itr = hmr.entrySet().iterator();
												while (itr.hasNext()) {
													Map.Entry er = (Map.Entry) itr.next();									
													sb.append("<td align=\"right\">" + FormatUtil.formatMiles(er == null?"":er.getValue().toString())+ "</td>");
												}
											}			
											sb.append("</tr>");
										}			
										sb.append("</table>");
									}else{
										sb.append("<br/>");
										sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
										sb.append("<table class=\"gridtable\">");
										sb.append("<tr>");
										sb.append("<td>El grupo no tiene deuda de este tipo</td>");
										sb.append("</tr>");
										sb.append("</table>");								
									}
									
				
								}
							
							}//
							
					
				}			
				// fin grupo economicp
				
				
				// ini empresa			
			if(listaEmpresa!=null && listaEmpresa.size()>0){
				for (Tempresa empresa: listaEmpresa) {

					logger.info(empresa.getCodEmpresa());

					if (empresa.getCodEmpresa().toString().equals("999999")) {
						continue;
					}
					
					String strEmpresaind="";
					listEmpresat =programaBO.findEmpresaByIdprogramaRuc(Idprograma.toString(), empresa.getCodEmpresa().toString());	
					if (listEmpresat!=null && listEmpresat.size()>0){
					Empresa oempresa=new Empresa();
					oempresa=listEmpresat.get(0);
					strEmpresaind=oempresa.getNombre();
					}
					sb.append("<br/>");
					sb.append("<table>");
					sb.append("<tr><td><b>Deuda: " + strEmpresaind + "</b></td></tr>");
					sb.append("</table>");
					if (listaopcionPool2!=null && listaopcionPool2.size()>0){
						for (OpcionPool opcionPool: listaopcionPool2) {
							listPoolBancarioTotal = new ArrayList<List>();
							String CabeceraTipoDeuda="";
							if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_TOTAL;
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_TOTAL,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							}  else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_DIRECTA )) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA ;
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_DIRECTA,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							} else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_INDIRECTA,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							}
		
							// DEUDA TOTAL
							if (listPoolBancarioTotal != null
									&& listPoolBancarioTotal.size() > 0) {
								sb.append("<br/>");
								sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
								sb.append("<table class=\"gridtable\">");
		
								
								StringBuilder sbcab1=new StringBuilder ();
								StringBuilder sbcab2=new StringBuilder ();
								sbcab1.append("<tr>");
								int conts=0;
								int conts1=0;
								int conts2=0;
								
								Map<String, String> hm = new HashMap<String, String>();
								for (Object lista : listPoolBancarioTotal.get(0)) {
									hm = (HashMap<String, String>) lista;
		
									Iterator it = hm.entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry e = (Map.Entry) it.next();
										// logger.info(e.getKey() + " " +
										// e.getValue());									
										conts=conts+1;
										if(e.getKey().toString().substring(0, 3).equals(Constantes.ABREV_NOMB_BANCO_CTA)){
											conts2=conts2+1;											
										}
										
										if(e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA)||
										   e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA)){
											sbcab1.append("<th style=\"background-color: yellow;color: #666666\">" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
										}else{
											sbcab1.append("<th>" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
										}
									}
								}
								
								conts1=conts-conts2-1;
								sbcab1.append("</tr>");
								
								sbcab2.append("<tr>");
									sbcab2.append("<th></th>");
									if (conts1>0){									
										sbcab2.append("<th align=\"center\" colspan=\""+conts1+"\">IMPORTE</th>");
									}
									if (conts2>0){
										sbcab2.append("<th align=\"center\" colspan=\""+conts2+"\">CUOTA</th>");
									}
								sbcab2.append("</tr>");
							
								sb.append(sbcab2.toString());
								sb.append(sbcab1.toString());
		
								for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
									sb.append("<tr>");
									Map<String, String> hmr = new HashMap<String, String>();
									for (Object x : listPoolBancarioTotal.get(i)) {
										hmr = (HashMap<String, String>) x;
		
										Iterator itr = hmr.entrySet().iterator();
										while (itr.hasNext()) {
											Map.Entry er = (Map.Entry) itr.next();
											// logger.info(er.getKey() + " " +
											// er.getValue());
											sb.append("<td align=\"right\">" +FormatUtil.formatMiles(er.getValue()== null?"": er.getValue().toString()) + "</td>");
										}
									}
		
									sb.append("</tr>");
								}
		
								sb.append("</table>");
							}else{
								sb.append("<br/>");	
								sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
								sb.append("<table class=\"gridtable\">");
								sb.append("<tr>");
								sb.append("<td>La empresa no tiene deuda de este tipo</td>");
								sb.append("</tr>");
								sb.append("</table>");
								
							}
		
						}
					
					}//
				}
				
			}
				//fin empresa 

				

			strPoolBancario= sb.toString();		
			
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strPoolBancario;
		}
		
		
		public String ModeloRentabilidadBD(String Idprograma) {
			
			String strRentabilidad="";	
			
			StringBuilder sb=new StringBuilder ();
			List<RentabilidadBEC> listRentabilidadBEC= new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC> listRentabilidadBECGrupoBD= new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC>  olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC>  olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
			try {
				
				//String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();				
				String ModeloRentab = Constantes.ID_TIPO_BANCAEMPRESA;	
				
				
				StringBuilder sbgrupo=new StringBuilder();
				StringBuilder sbempresa=new StringBuilder();
				
				String strcodigoCentral="";
				List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();
				String idprograma =Idprograma;
				Programa oprograma=new Programa();
				oprograma=programaBO.findById(Long.valueOf(idprograma));
				String codGrupo=oprograma.getIdGrupo()==null?"":oprograma.getIdGrupo().toString();
				String nombGrupo=oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
				String codTipoEmpresaGrupo=oprograma.getTipoEmpresa().getId().toString();
				String idempresaPrincipal=oprograma.getIdEmpresa().toString();
				
				String strEmpresa = oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
				
				
				//ini MCG20140610
				if (!codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					
					sbempresa.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
					sbempresa.append("<tr><td>" + strEmpresa + "</td></tr>");
					sbempresa.append("</table>");			
					
					
						listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
						listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,idempresaPrincipal);
					
						if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {
				
							if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {							
								sbempresa.append(crearRentabilidad(listRentabilidadBEC,true));
							}
						}//	
						sb.append(sbempresa.toString());
							
				}else {
				
					sb.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
					sb.append("<tr><td>" + nombGrupo + "</td></tr>");
					sb.append("</table>");	
					
					//	
					StringBuilder sbperiodoAnual=null;
					StringBuilder sbperiodoMensual=null;
					boolean bandera=true;
					listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idprograma));	
					if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {
						for (Empresa listaempr:listEmpresaBEC){
		
							strEmpresa=listaempr.getNombre();
							strcodigoCentral=listaempr.getCodigo();
							
							sbempresa.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
							sbempresa.append("<tr><td>" + strEmpresa + "</td></tr>");
							sbempresa.append("</table>");	
										
							listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
							listRentabilidadBEC =relacionesBancariasBO.generaModeloRentabilidad(Idprograma,strcodigoCentral);	
						
							if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {
								sbempresa.append(crearRentabilidad(listRentabilidadBEC,false));
								if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {
									olistRentabilidadBECTotal.addAll(listRentabilidadBEC);								
								} //listado renta
							}//			
						}
						// MCG20140912 Para grupo se busca en la BD
						listRentabilidadBECGrupoBD=new ArrayList<RentabilidadBEC>();					
						listRentabilidadBECGrupoBD =relacionesBancariasBO.generaModeloRentabilidad(Idprograma,codGrupo);	
						if (listRentabilidadBECGrupoBD!=null && listRentabilidadBECGrupoBD.size()>0){						
							olistRentabilidadBECGrupo.addAll(listRentabilidadBECGrupoBD);
						}else{
							olistRentabilidadBECGrupo=relacionesBancariasBO.crearRentabilidadGrupo(olistRentabilidadBECTotal,codGrupo,idprograma);
						}
						
						sbgrupo.append(crearRentabilidad(olistRentabilidadBECGrupo,true));
						sb.append(sbgrupo.toString());				
						sb.append(sbempresa.toString());
					}//lista empresa			
				//	
				}
				strRentabilidad= sb.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strRentabilidad;
		}
		
		private String crearRentabilidad(List<RentabilidadBEC> listRentabilidadBEC,boolean bmostrarFecha){
			StringBuilder sbcont=new StringBuilder();
		
				StringBuilder sbcomercial=new StringBuilder();
				StringBuilder sbrentabilidad=new StringBuilder();						
				StringBuilder sbperiodoAnual=null;
				StringBuilder sbperiodoMensual=null;
		
			if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {				
						
				for (RentabilidadBEC orentabilidadbec: listRentabilidadBEC) {
					if(sbperiodoAnual==null){
						sbperiodoAnual=new StringBuilder();
						sbperiodoAnual.append(orentabilidadbec.getFechaProcesoAnual()==null?"":orentabilidadbec.getFechaProcesoAnual());
					}
					if(sbperiodoMensual==null){
						sbperiodoMensual=new StringBuilder();
						sbperiodoMensual.append(orentabilidadbec.getFechaProcesoMensual()==null?"":orentabilidadbec.getFechaProcesoMensual());
					}
					
					if(!orentabilidadbec.getDescripcionMonto().equals(Constantes.CUENTA_CENTRAL)&& !orentabilidadbec.getDescripcionMonto().equals(Constantes.ROA)){
						if (orentabilidadbec.getTipoRentabilidad().equals(Constantes.ID_TIPO_COMERCIAL)){					
							sbcomercial.append("<tr>");					
							sbcomercial.append("<td>" + orentabilidadbec.getDescripcionMonto() + "</td>");
							
							//Se agrega para la columna Anual del reporte de rentabilidad
							sbcomercial.append("<td align=\"right\">" + 
									  (orentabilidadbec.getMontoAnual()==null?
									  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;": 
										  orentabilidadbec.getMontoAnual()) + "</td>");	
							
							sbcomercial.append("<td align=\"right\">" + 
											  (orentabilidadbec.getMonto()==null?
											  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;": 
											   orentabilidadbec.getMonto()) + "</td>");	
		
							
							sbcomercial.append("</tr>");								
							
						}else{
							sbrentabilidad.append("<tr>");					
							sbrentabilidad.append("<td>" + orentabilidadbec.getDescripcionMonto() + "</td>");
							
							//Se agrega para la columna Anual del reporte de rentabilidad
							sbrentabilidad.append("<td align=\"right\">" + 
									 (orentabilidadbec.getMontoAnual()==null?
									  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;":
									  orentabilidadbec.getMontoAnual()) + "</td>");	
							
							sbrentabilidad.append("<td align=\"right\">" + 
												 (orentabilidadbec.getMonto()==null?
												  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;":
												  orentabilidadbec.getMonto()) + "</td>");	
							
							sbrentabilidad.append("</tr>");							
						}	
					}
				}	
				
				sbcont.append("<table style=\"border: hidden\">");
					if (bmostrarFecha){
						sbcont.append("<tr>");
							sbcont.append("<td style=\"border: hidden\" colspan=\"3\">");
								sbcont.append("<table class=\"gridtableComent\">");
									sbcont.append("<tr>");
										sbcont.append("<td>Periodo Anual : " + sbperiodoAnual.toString() + "</td>");
									sbcont.append("</tr>");
									sbcont.append("<tr>");
										sbcont.append("<td>Periodo Mensual : " + sbperiodoMensual.toString() + "</td>");
									sbcont.append("</tr>");
								sbcont.append("</table>");
							sbcont.append("</td>");
						sbcont.append("</tr>");
					}				
					sbcont.append("<tr>");						
						sbcont.append("<td style=\"border: hidden\">");							
							sbcont.append("<table class=\"gridtable\">");
								sbcont.append("<tr align=\"center\">");
									sbcont.append("<th>COMERCIAL</th>");//Se Cambia sb.append("<th colspan=\"2\">Comercial</th>");
									sbcont.append("<th>&nbsp;AÑO ANTERIOR&nbsp;</th>");
									sbcont.append("<th>&nbsp;MES ACUMULADO&nbsp;</th>");
								sbcont.append("</tr>");	
								sbcont.append(sbcomercial.toString());
							sbcont.append("</table>");
						sbcont.append("</td>");
						
						sbcont.append("<td style=\"border: hidden\" colspan=\"4\">&nbsp;&nbsp;</td>");
						
						sbcont.append("<td style=\"border: hidden\">");
							sbcont.append("<table class=\"gridtable\">");
								sbcont.append("<tr align=\"center\">");
									sbcont.append("<th>RENTABILIDAD</th>");//Se Cambia sb.append("<th colspan=\"2\">Rentabilidad</th>");
									sbcont.append("<th>&nbsp;AÑO ANTERIOR&nbsp;</th>");
									sbcont.append("<th>&nbsp;MES ACUMULADO&nbsp;</th>");
								sbcont.append("</tr>");
								sbcont.append(sbrentabilidad.toString());
								sbcont.append("<tr>");
								sbcont.append("<td colspan=\"3\">&nbsp;</td>");										
								sbcont.append("</tr>");	
							sbcont.append("</table>");								
						sbcont.append("</td>");							
					sbcont.append("</tr>");
				sbcont.append("</table>");
				
			}
			
			
			return sbcont.toString();
		}
		

		
		public String FactoresRiesgoHTML(String idprograma,int  indcapitulo,Tvariable oasignasalto){
			String factoresriesgo="";
			
			StringBuilder sb=new StringBuilder ();		
			try {	
				//String idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				Programa programa5 = new Programa();
				programa5.setId(Long.valueOf(idprograma));

		        StringBuilder stbcomentfortaleza = new StringBuilder();
		        StringBuilder stbcomentOportunidades = new StringBuilder();
		        
		        StringBuilder stbcomentDebilidades = new StringBuilder();
		        StringBuilder stbcomentAmenaza = new StringBuilder();
		        StringBuilder stbconcluFODA=new StringBuilder();
				
		    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa5);
		        if (programaBlob!=null){

			 	      if(programaBlob.getFodaFotalezas()!=null){
			 		        for(byte x :programaBlob.getFodaFotalezas() ){
			 		        	stbcomentfortaleza.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }	
			 	      if(programaBlob.getFodaOportunidades()!=null){
			 		        for(byte x :programaBlob.getFodaOportunidades() ){
			 		        	stbcomentOportunidades.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }	
			 	      if(programaBlob.getFodaDebilidades()!=null){
			 		        for(byte x :programaBlob.getFodaDebilidades() ){
			 		        	stbcomentDebilidades.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }	
			 	      if(programaBlob.getFodaAmenazas()!=null){
			 		        for(byte x :programaBlob.getFodaAmenazas() ){
			 		        	stbcomentAmenaza.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }	
			 	      if(programaBlob.getConclucionFoda()!=null){
			 		        for(byte x :programaBlob.getConclucionFoda() ){
			 		        	stbconcluFODA.append((char)FormatUtil.getCharUTF(x));
			 		        }
			 	        }	
			 	      
			 	     
		        }
		        
		        
		        stbcomentfortaleza 		=	obtenerCadenaHTMLValidada(stbcomentfortaleza); 
		        stbcomentOportunidades 	= 	obtenerCadenaHTMLValidada(stbcomentOportunidades);	        
		        stbcomentDebilidades 	= 	obtenerCadenaHTMLValidada(stbcomentDebilidades);
		        stbcomentAmenaza 		= 	obtenerCadenaHTMLValidada(stbcomentAmenaza);
		        stbconcluFODA			=	obtenerCadenaHTMLValidada(stbconcluFODA);
		        
		        boolean bcomentfortaleza=false;
		        boolean bcomentOportunidades=false;
		        boolean bcomentDebilidades=false;
		        boolean bcomentAmenaza=false;
		        boolean bconcluFODA=false;
		        String saltolinea="";
		        
		        if (stbcomentfortaleza.toString().length()>0){
		        	bcomentfortaleza=true;
				}
		        if (stbcomentOportunidades.toString().length()>0){
		        	bcomentOportunidades=true;
				}
		        if (stbcomentDebilidades.toString().length()>0){
		        	bcomentDebilidades=true;
				}
		        if (stbcomentAmenaza.toString().length()>0){
		        	bcomentAmenaza=true;
				}
		        if (stbconcluFODA.toString().length()>0){
		        	bconcluFODA=true;
				}
		        


				if ( !bcomentfortaleza &&
				     !bcomentOportunidades &&
				     !bcomentDebilidades &&
				     !bcomentAmenaza&&
				     !bconcluFODA){	
					
					sb.append("<h4><b><u> "+ indcapitulo +".- FACTORES DE RIESGO. CONCLUSIONES</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_FACTORES_RIESGO+"</span></h4>");	  
			        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
//					sb.append("<br/>");
//					sb.append("<br/>");
					oasignasalto.setFactoresRiesgoSLinea(false);
						
				}else{
			        sb.append("<h4><b><u> "+ indcapitulo +".- FACTORES DE RIESGO. CONCLUSIONES</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_FACTORES_RIESGO+"</span></h4>");	  
			        //sb.append("<br/>");
					oasignasalto.setFactoresRiesgoSLinea(true);
				}
				if (bcomentfortaleza){
			    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>FORTALEZAS:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbcomentfortaleza.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bcomentOportunidades){
			    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>OPORTUNIDADES:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbcomentOportunidades.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bcomentDebilidades){
			    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>DEBILIDADES:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbcomentDebilidades.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bcomentAmenaza){
			    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>AMENAZAS:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbcomentAmenaza.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");					
					sb.append("<br/>");
				}
				if (bconcluFODA){
			    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
					 		+ " <tr>" 		
			        		+ " 	<th>CONCLUSIONES:</th>"		        	
			        		+ " </tr>" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + stbconcluFODA.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");	
				}
				sb.append("<br/>");
				sb.append("<br/>");
				factoresriesgo= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return factoresriesgo;
		}
		
		
		
		public String PropuestaRiesgoHTML(String Idprograma,int  indcapitulo,Tvariable oasignasalto){
			String propuestariesgo="";
			
			StringBuilder sb=new StringBuilder ();		
			try {
			
						//String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
						//setPrograma(programaBO.findById(Long.valueOf(Idprograma)));	
						
						Programa programa6 = new Programa();
						//programa6.setId(Long.valueOf(Idprograma));
						programa6 =programaBO.findById(Long.valueOf(Idprograma));
						
						String tipoEstructura="2";//MCG20130812 programa6.getTipoEstructura()==null?Constantes.ID_TIPO_ESTRUCTURA_LIMITE:programa6.getTipoEstructura();
						String idtipoMiles=programa6.getTipoMiles()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programa6.getTipoMiles();
						Tabla otipoMiles=new Tabla();
						otipoMiles=tablaBO.obtienePorId(Long.valueOf(idtipoMiles));
						String strTipoMiles=otipoMiles.getDescripcion();
				        StringBuilder stbcomentPropRiesgo = new StringBuilder(); 
				        StringBuilder stbcomentEstrucLimite=new StringBuilder();
				        StringBuilder stbconsidPropRiesgo =new StringBuilder();
						
				    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa6);
				        if (programaBlob!=null){

					 	      if(programaBlob.getCampoLibrePR()!=null){
					 		        for(byte x :programaBlob.getCampoLibrePR() ){
					 		        	stbcomentPropRiesgo.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	

					 	      if(programaBlob.getEstructuraLimite()!=null){
					 		        for(byte x :programaBlob.getEstructuraLimite() ){
					 		        	stbcomentEstrucLimite.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	      
					 	      if(programaBlob.getConsideracionPR()!=null){
					 		        for(byte x :programaBlob.getConsideracionPR() ){
					 		        	stbconsidPropRiesgo.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	     
				        }
				        
				        stbcomentPropRiesgo 	=	obtenerCadenaHTMLValidada(stbcomentPropRiesgo); 
				        stbcomentEstrucLimite	=	obtenerCadenaHTMLValidada(stbcomentEstrucLimite);
				        stbconsidPropRiesgo 	=	obtenerCadenaHTMLValidada(stbconsidPropRiesgo);
				        
						boolean bcomentPropRiesgo=false;
						boolean bcomentEstrucLimite=false;
						boolean bconsidPropRiesgo=false;
						boolean blistEstructuraLimite =false;
						String saltolinea="";
						
						if (stbcomentPropRiesgo.toString().length()>0){
							bcomentPropRiesgo=true;
						}
						if (stbcomentEstrucLimite.toString().length()>0){
							bcomentEstrucLimite=true;
						}
						if (stbconsidPropRiesgo.toString().length()>0){
							bconsidPropRiesgo=true;
						}
						if (listEstructuraLimite!=null && listEstructuraLimite.size()>0){
							blistEstructuraLimite=true;
						}

				        
				        listEstructuraLimite=propuestaRiesgoBO.findEstructuraLimiteByIdprograma(Long.valueOf(Idprograma));	
						float ta1 = 0;
						float ta2 = 0;
									
						for(EstructuraLimite el : listEstructuraLimite){						
							ta1 += Float.valueOf(FormatUtil.FormatNumeroSinComa(el.getLimiteAutorizado()));
							ta2 += Float.valueOf(FormatUtil.FormatNumeroSinComa(el.getLimitePropuesto()));
						}
						String Total1=""+FormatUtil.roundTwoDecimalsPunto(ta1);
						String Total2=""+FormatUtil.roundTwoDecimalsPunto(ta2);			


						
						if (!bcomentPropRiesgo &&
							     !bcomentEstrucLimite &&
							     !bconsidPropRiesgo &&
							     !blistEstructuraLimite ){	
								oasignasalto.setPropuestaRiesgoSLinea(false);
								
								sb.append("<h4><b><u> "+ indcapitulo +".- PROPUESTAS DE RIESGO</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_PROPUESTAS_RIESGO+"</span></h4>");					
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
//								sb.append("<br/>");
//								sb.append("<br/>");
								
								
						} else{
							sb.append("<h4><b><u> "+ indcapitulo +".- PROPUESTAS DE RIESGO</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_PROPUESTAS_RIESGO+"</span></h4>");					
							//sb.append("<br/>");
							oasignasalto.setPropuestaRiesgoSLinea(true);
						}
						
						if (bcomentPropRiesgo ){
							//sb.append("<br/>");					
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>PROPUESTA DE RIESGO:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentPropRiesgo.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
							sb.append("<br/>");
						}
						
						
							if (tipoEstructura.equals(Constantes.ID_TIPO_ESTRUCTURA_LIMITE)){						
								if (blistEstructuraLimite ){
									StringBuilder sbpr=new StringBuilder ();		
									sbpr.append("<table class=\"gridtable\">");						
										
									sbpr.append("<tr>");							
										sbpr.append("<th>BBVA CONTINENTAL - CIFRAS EN " + strTipoMiles + " DE DOLARES(USD)</th>");	
										sbpr.append("<th>LÍMITE AUTORIZADO</th>");	
										sbpr.append("<th>LÍMITE FORMALIZADO</th>");
										sbpr.append("<th>SALDO UTILIZADO</th>");
										sbpr.append("<th>PROPUESTO OFICINA</th>");
										sbpr.append("<th>PROPUESTO RIESGO</th>");
										sbpr.append("<th>OBSERVACIÓN</th>");
									sbpr.append("</tr>");				
										
									if (listEstructuraLimite != null && listEstructuraLimite.size() > 0) {
										for (EstructuraLimite oEl:listEstructuraLimite){
											sbpr.append("<tr>");
											if (oEl.getTipo().equals("TE")){
												sbpr.append("<td>" + oEl.getEmpresa().getNombre() + "</td>");
											}else{
												sbpr.append("<td>" + oEl.getTipoOperacion() + "</td>");
											}
											sbpr.append("<td align=\"right\">" + (oEl.getLimiteAutorizado()==null?"":oEl.getLimiteAutorizado()) + "</td>");
											sbpr.append("<td align=\"right\">" + (oEl.getLimitePropuesto()==null?"":oEl.getLimitePropuesto()) + "</td>");
											sbpr.append("<td align=\"right\">" + (oEl.getSaldoUtilizado()==null?"":oEl.getSaldoUtilizado()) + "</td>");
											sbpr.append("<td align=\"right\">" + (oEl.getPropuestoOficina()==null?"":oEl.getPropuestoOficina()) + "</td>");
											sbpr.append("<td align=\"right\">" + (oEl.getPropuestaRiesgo()==null?"":oEl.getPropuestaRiesgo()) + "</td>");
											sbpr.append("<td>" + (oEl.getObservacion()==null?"":oEl.getObservacion()) + "</td>");
											sbpr.append("</tr>");
										}
									}
									
		//							sbpr.append("<tr>");							
		//							sbpr.append("<td>Total Grupo</td>");	
		//							sbpr.append("<td align=\"right\">"+ Total1 +"</td>");	
		//							sbpr.append("<td align=\"right\">"+ Total2 +"</td>");
		//							sbpr.append("<td></td>");
		//							sbpr.append("</tr>");			
									
									sbpr.append("</table>"); 
									
								    sb.append(sbpr.toString());	
								}
							
							}else{	
								/*
						    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
								 		+ " <tr>" 		
						        		//+ " 	<th>COMENTARIO ESTRUCTURA LIMITE:</th>"		
						        		+ " 	<th>PROPUESTA DE RIESGO:</th>"	
						        		+ " </tr>" 
						        		+ " <tr>" 		        		
						        		+ " 	<td class=\"myEditor\">" + stbcomentEstrucLimite.toString() + "</td>"
						        		+ " </tr>"    
						        		+ " </table>");	
						        */
							}
						
						if (bconsidPropRiesgo){
							//sb.append("<br/>");						
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>CONSIDERACIONES:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbconsidPropRiesgo.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");	
						}
				    	//sb.append("<br/>");
						sb.append("<br/>");
						sb.append("<br/>");
						propuestariesgo= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return propuestariesgo;
		}
		
		
		public String PoliticaRiesgoHTML(String Idprograma,int  indcapitulo,Tvariable oasignasalto ){
			String politicariesgo="";
			
			StringBuilder sb=new StringBuilder ();		
			try {
			
						//String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
						//setPrograma(programaBO.findById(Long.valueOf(Idprograma)));	
						
						Programa programa6 = new Programa();
						//ADD EDWIN 06022012
						programa6 =programaBO.findById(Long.valueOf(Idprograma));
						
						String limiteAutorizado = programa6.getLimiteAutorizadoPRG()==null?"":programa6.getLimiteAutorizadoPRG();
						String proximaRevision = programa6.getProximaRevisionPRG()==null?"":programa6.getProximaRevisionPRG();
						String motivoRevision = programa6.getMotivoProximaPRG()==null?"":programa6.getMotivoProximaPRG();
						
						String idtipoMilesPLR=programa6.getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programa6.getTipoMilesPLR().toString();
						Tabla otipoMilesPLR=new Tabla();
						otipoMilesPLR=tablaBO.obtienePorId(Long.valueOf(idtipoMilesPLR));
						String strTipoMilesPLR=otipoMilesPLR.getDescripcion();
						//ADD EDWIN 06022012

				        StringBuilder stbcomentdetOperGar = new StringBuilder(); 
				        StringBuilder stbcomentRiesgoTeso=new StringBuilder();
				        StringBuilder stbcomentPolRiesgoGroup =new StringBuilder();
				        StringBuilder stbcomentPolDelegacion=new StringBuilder();
						
				    	ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa6);
				        if (programaBlob!=null){

					 	      if(programaBlob.getDetalleOperacionGarantia()!=null){
					 		        for(byte x :programaBlob.getDetalleOperacionGarantia() ){
					 		        	stbcomentdetOperGar.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	

					 	      if(programaBlob.getRiesgoTesoreria()!=null){
					 		        for(byte x :programaBlob.getRiesgoTesoreria() ){
					 		        	stbcomentRiesgoTeso.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	      
					 	      if(programaBlob.getPoliticasRiesGrupo()!=null){
					 		        for(byte x :programaBlob.getPoliticasRiesGrupo() ){
					 		        	stbcomentPolRiesgoGroup.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	      if(programaBlob.getPoliticasDelegacion()!=null){
					 		        for(byte x :programaBlob.getPoliticasDelegacion() ){
					 		        	stbcomentPolDelegacion.append((char)FormatUtil.getCharUTF(x));
					 		        }
					 	        }	
					 	     
				        }
				        
				        stbcomentdetOperGar 	=	obtenerCadenaHTMLValidada(stbcomentdetOperGar); 
				        stbcomentRiesgoTeso		=	obtenerCadenaHTMLValidada(stbcomentRiesgoTeso);	
				        stbcomentPolRiesgoGroup =	obtenerCadenaHTMLValidada(stbcomentPolRiesgoGroup);	
				        stbcomentPolDelegacion	=	obtenerCadenaHTMLValidada(stbcomentPolDelegacion);	
				    	
				        boolean bcomentdetOperGar=false;
						boolean bcomentRiesgoTeso=false;
						boolean bcomentPolRiesgoGroup=false;
						boolean bcomentPolDelegacion=false;
						boolean blistLimiteFormalizado=false;
						
						boolean blimiteAutorizado = false;
						boolean bproximaRevision = false;
						boolean bmotivoRevision = false;
						
						
						String saltolinea="";
						
						
						if (stbcomentdetOperGar.toString().length()>0){
							bcomentdetOperGar=true;
						}
						if (stbcomentRiesgoTeso.toString().length()>0){
							bcomentRiesgoTeso=true;
						}
						if (stbcomentPolRiesgoGroup.toString().length()>0){
							bcomentPolRiesgoGroup=true;
						}
						if (stbcomentPolDelegacion.toString().length()>0){
							bcomentPolDelegacion=true;
						}
						
						if (limiteAutorizado.toString().length()>0){
							blimiteAutorizado=true;
						}
						if (proximaRevision.toString().length()>0){
							bproximaRevision=true;
						}
						if (motivoRevision.toString().length()>0){
							bmotivoRevision=true;
						}
				        
				        
				        listLimiteFormalizado=new ArrayList<LimiteFormalizado>();	
				        listLimiteFormalizado =politicasRiesgoBO.findLimiteFormalizadoByIdprograma(Long.valueOf(Idprograma));		
						
				        if (listLimiteFormalizado!=null && listLimiteFormalizado.size()>0 ){
				        	blistLimiteFormalizado=true;
				        }
				        
				        float ta1 = 0;
						float ta2 = 0;
						float ta3 = 0;
						float ta4 = 0;
						float ta5 = 0;
						float ta6 = 0;					
										
						for(LimiteFormalizado olf : listLimiteFormalizado){	
							
							if (!olf.getTipoRiesgo().getId().equals(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO)){
													
							ta1 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimiteAutorizado()));
							ta2 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getComprometido()));
							ta3 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getNoComprometido()));
							ta4 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getTotal()));
							ta5 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getDispuesto()));	
							ta6 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimitePropuesto()));
							
							}
							
						}
						
						String Total1=""+FormatUtil.roundTwoDecimalsPunto(ta1);
						String Total2=""+FormatUtil.roundTwoDecimalsPunto(ta2);
						String Total3=""+FormatUtil.roundTwoDecimalsPunto(ta3);
						String Total4=""+FormatUtil.roundTwoDecimalsPunto(ta4);
						String Total5=""+FormatUtil.roundTwoDecimalsPunto(ta5);
						String Total6=""+FormatUtil.roundTwoDecimalsPunto(ta6);
						

						//sb.append("<br/>");

						
						if (!bcomentdetOperGar &&
							!bcomentRiesgoTeso &&
							!bcomentPolRiesgoGroup &&
							!bcomentPolDelegacion &&
							!blistLimiteFormalizado &&
							!blimiteAutorizado &&
							!bproximaRevision &&
							!bmotivoRevision){	
								oasignasalto.setPoliticaRiesgoSLinea(false);
								sb.append("<h4><b><u> "+ indcapitulo +".- POLÍTICA DE RIESGOS GRUPO</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_POLITICA_RIESGO+"</span></h4>");					
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
//								sb.append("<br/>");
//								sb.append("<br/>");	
								
							}else{
								sb.append("<h4><b><u> "+ indcapitulo +".- POLÍTICA DE RIESGOS GRUPO</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_POLITICA_RIESGO+"</span></h4>");					
								//sb.append("<br/>");	
								oasignasalto.setPoliticaRiesgoSLinea(true);
							}
						
						if (blistLimiteFormalizado){
							StringBuilder sbp=new StringBuilder ();		
							sbp.append("<table class=\"gridtable\">");						
								
							sbp.append("<tr>");
									sbp.append("<th colspan=\"2\">CIFRAS EN " + 
												strTipoMilesPLR+
												" DE DOLARES</th>");
									sbp.append("<th colspan=\"3\" align=\"center\" >LÍMITE FORMALIZADO</th>");
									sbp.append("<th colspan=\"2\"></th>");
									sbp.append("</tr>");
							sbp.append("<tr>");
									sbp.append("<th></th>");
									sbp.append("<th>LTE AUTORIZADO</th>");
									sbp.append("<th>COMPROMETIDO</th>");
									sbp.append("<th>NO COMPROMETIDO</th>");
									sbp.append("<th>TOTAL</th>");
									sbp.append("<th>DISPUESTO</th>");
									sbp.append("<th>LTE PROPUESTO</th>");
							sbp.append("</tr>");					
					
							int contcoltotal=0;		
							if (listLimiteFormalizado != null && listLimiteFormalizado.size() > 0) {
								for (LimiteFormalizado lf:listLimiteFormalizado){
									if (lf.getTipoRiesgo().getId().equals(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO)){	
										contcoltotal+=1;	
									}
									
									sbp.append("<tr>");	
									sbp.append("<td>" + lf.getTipoRiesgo().getDescripcion() + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getLimiteAutorizado()==null?"":lf.getLimiteAutorizado()) + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getComprometido()==null?"":lf.getComprometido()) + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getNoComprometido()==null?"":lf.getNoComprometido()) + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getTotal()==null?"":lf.getTotal()) + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getDispuesto()==null?"":lf.getDispuesto()) + "</td>");
									sbp.append("<td align=\"right\">" + (lf.getLimitePropuesto()==null?"":lf.getLimitePropuesto()) + "</td>");
									sbp.append("</tr>");
									
							
								}
							}
							
							if (contcoltotal==0){
								sbp.append("<tr>");							
								sbp.append("<td>TOTAL GRUPO</td>");	
								sbp.append("<td align=\"right\">"+ Total1 +"</td>");	
								sbp.append("<td align=\"right\">"+ Total2 +"</td>");
								sbp.append("<td align=\"right\">"+ Total3 +"</td>");	
								sbp.append("<td align=\"right\">"+ Total4 +"</td>");
								sbp.append("<td align=\"right\">"+ Total5 +"</td>");	
								sbp.append("<td align=\"right\">"+ Total6 +"</td>");
								sbp.append("</tr>");			
							}
							sbp.append("</table>"); 					
							
							//sb.append("<br/>");	
						    sb.append(sbp.toString());					
						    sb.append("<br/>");
						}
					    
						if (bcomentdetOperGar ){
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>DETALLES DE OPERACIONES Y GARANTÍAS:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentdetOperGar.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
							sb.append("<br/>");
						}
						
						if (bcomentRiesgoTeso){
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>RIESGO TESORERÍA:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentRiesgoTeso.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
							sb.append("<br/>");	
						}
						if (bcomentPolRiesgoGroup){
						
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>POLÍTICA DE RIESGOS GRUPO:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentPolRiesgoGroup.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");					
					    	sb.append("<br/>");
						}
						
						if (bcomentPolDelegacion){
					    	sb.append( " <table class=\"gridtable\" width=\"100%\">"    
							 		+ " <tr>" 		
					        		+ " 	<th>POLÍTICA DE DELEGACIÍN:</th>"		        	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td class=\"myEditor\">" + stbcomentPolDelegacion.toString() + "</td>"
					        		+ " </tr>"    
					        		+ " </table>");	
					    	
					    	sb.append("<br/>");
						}
						

						if (blimiteAutorizado ||bproximaRevision||bmotivoRevision){
					    	sb.append( " <table class=\"gridtable\" style=\"width: 300px;\" >"    
							 		+ " <tr>" 		
					        		+ " 	<td>Límites Anteriores Autorizados por:</td>"
					        		+ " 	<td>"+limiteAutorizado+"</td>"	
					        		+ " </tr>" 
					        		+ " <tr>" 		        		
					        		+ " 	<td>Nueva fecha de vencimiento PF:</td>"
					        		+ " 	<td>"+proximaRevision+"</td>"	
					        		+ " </tr>"
					        		+ " <tr>" 		        		
					        		+ " 	<td>Ámbito de sanción:</td>"
					        		+ " 	<td>"+motivoRevision+"</td>"	
					        		+ " </tr>"  
					        		+ " </table>");	
						}
						sb.append("<br/>");
						sb.append("<br/>");
				    	politicariesgo= sb.toString();
				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
			return politicariesgo;
		}
			
		public String AnexoHTML(String idprograma,int  indcapitulo,Tvariable oasignasalto ){
			String anexo="";
			StringBuilder sb=new StringBuilder ();		
			try {					
						String detalle=detallePosicionRiesgoAnexo(idprograma);
						String garantias = detalleGarantiaAnexo(idprograma);
						if(!"".equals(detalle) || !"".equals(garantias)){
							oasignasalto.setAnexo1(true);
							sb.append("<h4><b><u> "+  indcapitulo +".- POSICIÓN PRINCIPAL</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_POSICION_PRINC+"</span></h4>");
							//sb.append("<br/>");
						}else{
							oasignasalto.setAnexo1(false);
						}
						if(!detalle.equals("")){
							sb.append(detalle);
							sb.append("<br/>");
						}
						if(!garantias.equals("")){	
							sb.append(garantias);
		//					sb.append("<div class=saltopage>&nbsp; </div>");
		//					sb.append(DetalleEconomicoFinancieroGrupo(idprograma));
							//sb.append("<br/>");	
							//sb.append(SintesisEconomicoAnexo(idprograma));					
							//sb.append("<br/>");	
						}
				    	anexo= sb.toString();
			} catch (Exception e) {
				return "";
			}
			return anexo;
		}
		
		public String Anexo2HTML(String idprograma,boolean flagAnexo,int  indcapitulo){
			StringBuilder anexos2 = new StringBuilder();
			anexos2.append("");
			ProgramaBlob programaBlob;
			try {
				programaBlob = programaBlobBO.findBlobByPrograma(getPrograma());
			
				StringBuilder comenlibreAnexo = new StringBuilder();
		        if (programaBlob!=null){
				    if (programaBlob.getCampoLibreAnexos()!=null){
				        for(byte x :programaBlob.getCampoLibreAnexos()){
				        	comenlibreAnexo.append((char)FormatUtil.getCharUTF(x));					          	
				        }				        
				    } 
		        }
		        String aux=comenlibreAnexo.toString().trim();
		        comenlibreAnexo=obtenerCadenaHTMLValidada(comenlibreAnexo);
		        //String sintesisEconomico=SintesisEconomicoAnexo(idprograma);
		        if(comenlibreAnexo==null||aux.equals("")){
		        	//if(sintesisEconomico==null||sintesisEconomico.equals("")){
		        		return "";
		        	//}
		        }
		        
		        if(flagAnexo){
		        	anexos2.append("<h4><b><u>"+ indcapitulo +".- ANEXO</u></b></h4>");
		        	anexos2.append("<br/>");
		        }
		        
		        anexos2.append("<h4><b><u>INFORMACIÓN ADICIONAL</u></b></h4>");
		        anexos2.append("<br/>");
				
		        anexos2.append( " <table class=\"gridtable\" width=\"100%\">"  
		        		+ " <tr>" 			        		
		        		+ " 	<td class=\"myEditor\">" + comenlibreAnexo + "</td>"
		        		+ " </tr>"    
		        		+ " </table>");				
		        anexos2.append("<br/>");	        
		        //anexos2.append(sintesisEconomico);					
		        //anexos2.append("<br/>");	
	        
			} catch (BOException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
	        return anexos2.toString();
		}
		
		//ini MCG20130314	
		public String SintesisEconomicoPorEmpresaHTML(String idprograma,int indcapitulo,Tvariable oasignasalto,List<TsubCaratula> olistasubCaratula){
			StringBuilder sintesisEconomicoEmpresa = new StringBuilder();
			sintesisEconomicoEmpresa.append("");
			
			try {   
				
				Programa programaSE = new Programa();			
				programaSE=programaBO.findById(Long.valueOf(idprograma));
		    	String saltolinea="";
		        boolean bsintesisEconomico=false;
		        String sintesisEconomico=SintesisEconomicoAnexo(idprograma,indcapitulo,olistasubCaratula);
		        
		        if(programaSE.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){   	
				        
			        if (sintesisEconomico.length()>0){
			        	bsintesisEconomico=true;		        	
			        }				
			        if (bsintesisEconomico){	
			        	oasignasalto.setInformEmpresaSLinea(true);	
			        	sintesisEconomicoEmpresa.append("<h4><b><u> "+indcapitulo+".- INFORMACIÓN POR EMPRESA</u></b> <span class=\"clsinvisible\"> "+Constantes.COD_INDICE_INFORMACION_X_EMP+"</span></h4>");
			        	sintesisEconomicoEmpresa.append("<br/>");
			        	sintesisEconomicoEmpresa.append(sintesisEconomico); 	
			        	sintesisEconomicoEmpresa.append("<br/>");		        	
			        }else{	   
			        	oasignasalto.setInformEmpresaSLinea(false);
			
//			        	sintesisEconomicoEmpresa.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
//			        	sintesisEconomicoEmpresa.append("<br/>");
//			        	sintesisEconomicoEmpresa.append("<br/>");
			        }	        
		       } else {
		    	   oasignasalto.setInformEmpresaSLinea(false);
		       }
	        
			} catch (Exception e) {
				logger.info(StringUtil.getStackTrace(e));
			}		
	        return sintesisEconomicoEmpresa.toString();
		}
		//fin MCG20130314
		
		//ini MCG20130314	
		public String ExtractoSintesisEconomicoPorEmpresaHTML(String idprograma ){
			StringBuilder extractoSintesisEconomicoEmpresa = new StringBuilder();
			extractoSintesisEconomicoEmpresa.append("");
			
			try {
		        
		        String extractosintesisEconomico=ExtractoSintesisEconomico(idprograma);
		 	        
		        extractoSintesisEconomicoEmpresa.append("<h4><b><u>EXTRACTO SÍNTESIS ECONÓMICO - FINANCIERO POR EMPRESA</u></b></h4>");
		        extractoSintesisEconomicoEmpresa.append("<br/>");		
		        
		        extractoSintesisEconomicoEmpresa.append(extractosintesisEconomico);					
		      
	        
			} catch (Exception e) {
				logger.info(StringUtil.getStackTrace(e));
			}
			
	        return extractoSintesisEconomicoEmpresa.toString();
		}
		
		/**
		 * Inicio: Agrega Garantias al PDF
		 */
		
		public String detalleGarantiaAnexo(String Idprograma){
			
			String garantiaAnexo="";
			StringBuilder sb=new StringBuilder ();		
			try {
				Programa programa = new Programa();
				programa.setId(Long.valueOf(Idprograma));
				anexoBO.setPrograma(programa);
				listaFilaAnexos = anexoBO.findAnexos();		
				List<AnexoGarantia> lstAnexoGarantias=anexoGarantiaBO.findAnexoXPrograma(programa);
				
				if(lstAnexoGarantias == null || lstAnexoGarantias.size() < 1){return "";}
				
							sb.append("<h4><b><u>GARANTÍAS</u></b></h4>");	
							//sb.append("<br/>");	
							//INI : Cabecera de Garantias
							sb.append( " <table class=\"gridtable\">");  
							sb.append("<tr>");
							sb.append("<th colspan=\"5\" align=\"center\" >&nbsp;LISTA DE GARANTÍAS</th>");
							sb.append("<tr class=\"alt1\" >");
							sb.append("<td class=\"alt1\" >EMPRESA</td>");
							sb.append("<td class=\"alt1\" >NÚMERO GARANTÍA</td>");
							sb.append("<td class=\"alt1\" align=\"right\">IMPORTE</td>");
							sb.append("<td class=\"alt1\" >DESCRIPCIÓN</td>");
							sb.append("<td class=\"alt1\" >NOTA</td>");
							sb.append("</tr>");
							sb.append("</tr>");
							//FIN : Cabecera de Garantias
							
							//INI : Cuerpo de Garantias
							String emp  = "" ;
							String gar  = "" ;
							String importe="";
							String des  = "" ;
							String nota="";
							
							if (lstAnexoGarantias != null && lstAnexoGarantias.size() > 0) {
								for (AnexoGarantia anexoGaran:lstAnexoGarantias){
									emp = anexoGaran.getEmpresa()==null?"":anexoGaran.getEmpresa();
									gar = anexoGaran.getNumeroGarantia()==null?"":anexoGaran.getNumeroGarantia();
									importe=anexoGaran.getImporte()==null?"":anexoGaran.getImporte();
									des = anexoGaran.getDescripcionGarantia()==null?"":anexoGaran.getDescripcionGarantia();
									nota = anexoGaran.getNota()==null?"":anexoGaran.getNota();
									
									sb.append("<tr>");
										sb.append("<td>" + emp + "</td>");
										sb.append("<td>" + gar  + "</td>");
										sb.append("<td align=\"right\">" + importe  + "</td>");
										sb.append("<td>" + des  + "</td>");
										sb.append("<td>" + nota  + "</td>");
									sb.append("</tr>");
								}
							}
							sb.append("</table>");				
							//FIN : Cuerpo de Garantias
							
							garantiaAnexo= sb.toString();
			
			
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}		
			return garantiaAnexo;
		}
		
		/*
		 * Fin : Agrega Garantias al PDF
		 */
		
		
		//fin MCG20130314

		
		public String detallePosicionRiesgoAnexo(String Idprograma)
		{
			String detallePosicionRiesgoAnexo="";
			
			StringBuilder sb=new StringBuilder ();	
			boolean esTotal=false;
			try {
				Programa programa = new Programa();
				programa.setId(Long.valueOf(Idprograma));
				anexoBO.setPrograma(programa);
				listaFilaAnexos = anexoBO.findAnexos();	
				//calcularMontos(listaFilaAnexos);
				
				if(listaFilaAnexos == null || listaFilaAnexos.size() < 1){return "";}
				
							sb.append("<h4><b><u>DETALLE DE POSICIÓN</u></b></h4>");	
							//sb.append("<br/>");	
							
							sb.append( " <table class=\"gridtable\" width=\"100%\">"); 
							
							//if (listaColumnas != null && listaColumnas.size() > 0) {
//								sb.append("<tr>");
								//sb.append("<th>&nbsp;</th>");
//								sb.append("<th style=\" width: 150px;\">&nbsp;EN MILES USD</th>");
//								for (AnexoColumna lc:listaColumnas){																	
//									sb.append("<th>" + lc.getDescripcion() + "</th>");
//								}
//								sb.append("</tr>");
							//}
							if (listaFilaAnexos != null && listaFilaAnexos.size() > 0) {
								int contoplim=0;
								for (FilaAnexo lfa:listaFilaAnexos){
									
									sb.append("<tr>");
									String strclass1="";
									if (lfa.getAnexo().getTipoFila()==0){
										strclass1="class=\"altp\"";
									}else if (lfa.getAnexo().getTipoFila()==1){
										strclass1="class=\"alt1\"";
									}else if ((lfa.getAnexo().getTipoFila()==2)||(lfa.getAnexo().getTipoFila()==7)){
										contoplim=0;
										strclass1="class=\"alt2\"";
									}else if (lfa.getAnexo().getTipoFila()==6){
										contoplim=0;
										strclass1="class=\"alt3\"";
										esTotal=true;
									}else{
										strclass1="";	
									}	
									String numeracion="";
									if(lfa.getAnexo().getTipoFila()==3 || lfa.getAnexo().getTipoFila()==5){
										contoplim=contoplim+1;
										numeracion=String.valueOf(contoplim) +". ";
										//sb.append("<td "+ strclass1 + ">" + contoplim + "</td>");
									}else{
										numeracion="";
										//sb.append("<td "+ strclass1 + ">&nbsp;</td>");	
									}
									
										if(lfa.getAnexo().getTipoFila()==4){
											sb.append("<td "+ strclass1 + ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + lfa.getAnexo().getDescripcion() + "</td>");
										}else{
											if (lfa.getAnexo().getTipoFila()==0){
												sb.append("<td "+ strclass1 + " style=\" width: 150px;\">&nbsp;EN MILES USD</td>");
											}else{
											sb.append("<td "+ strclass1 + ">" + numeracion + lfa.getAnexo().getDescripcion() + "</td>");
											}
										}
										
										String strvalor="";
										
										strvalor=lfa.getAnexo().getBureau()==null?"":lfa.getAnexo().getBureau();
										sb.append("<td "+ strclass1 + " align=\"left\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getRating()==null?"":lfa.getAnexo().getRating();
										sb.append("<td "+ strclass1 + " align=\"left\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getFecha()==null?"":lfa.getAnexo().getFecha();									
										sb.append("<td "+ strclass1 + ">" + strvalor.replace(">", "&gt;").replace("<", "&lt;") + "</td>");
										
										strvalor=lfa.getAnexo().getLteAutorizado()==null?"":lfa.getAnexo().getLteAutorizado();
										sb.append("<td "+ strclass1 + " align=\"right\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getLteForm()==null?"":lfa.getAnexo().getLteForm();
										sb.append("<td "+ strclass1 + " align=\"right\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getRgoActual()==null?"":lfa.getAnexo().getRgoActual();
										sb.append("<td "+ strclass1 + " align=\"right\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getRgoPropBbvaBc()==null?"":lfa.getAnexo().getRgoPropBbvaBc();
										sb.append("<td "+ strclass1 + " align=\"right\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getPropRiesgo()==null?"":lfa.getAnexo().getPropRiesgo();
										sb.append("<td "+ strclass1 + " align=\"right\">" + strvalor + "</td>");
										
										strvalor=lfa.getAnexo().getObservaciones()==null?"":lfa.getAnexo().getObservaciones();
										sb.append("<td "+ strclass1 + ">" + strvalor.replace(">", "&gt;").replace("<", "&lt;") + "</td>");
										
										if (lfa.getAnexo().getActivoCol1().equals("1")){
											strvalor=lfa.getAnexo().getColumna1()==null?"":lfa.getAnexo().getColumna1();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol2().equals("1")){
											strvalor=lfa.getAnexo().getColumna2()==null?"":lfa.getAnexo().getColumna2();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol3().equals("1")){
											strvalor=lfa.getAnexo().getColumna3()==null?"":lfa.getAnexo().getColumna3();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol4().equals("1")){
											strvalor=lfa.getAnexo().getColumna4()==null?"":lfa.getAnexo().getColumna4();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol5().equals("1")){
											strvalor=lfa.getAnexo().getColumna5()==null?"":lfa.getAnexo().getColumna5();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol6().equals("1")){
											strvalor=lfa.getAnexo().getColumna6()==null?"":lfa.getAnexo().getColumna6();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol7().equals("1")){
											strvalor=lfa.getAnexo().getColumna7()==null?"":lfa.getAnexo().getColumna7();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol8().equals("1")){
											strvalor=lfa.getAnexo().getColumna8()==null?"":lfa.getAnexo().getColumna8();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol9().equals("1")){
											strvalor=lfa.getAnexo().getColumna9()==null?"":lfa.getAnexo().getColumna9();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										if (lfa.getAnexo().getActivoCol10().equals("1")){
											strvalor=lfa.getAnexo().getColumna10()==null?"":lfa.getAnexo().getColumna10();
											sb.append("<td "+ strclass1 + ">" + strvalor + "</td>");
										}
										
										
										
									sb.append("</tr>");
								}
								if (!esTotal){
									filaTotal = anexoBO.calcularMontoTotal(listaFilaAnexos);
								sb.append("<tr>");
								//sb.append("<td class=\"alt3\"><b>&nbsp;</b></td>");
									sb.append("<td class=\"alt3\"><b>Total</b></td>");
									Anexo oanexo = filaTotal.getAnexo();
									if (oanexo!=null){
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getBureau()==null?"":oanexo.getBureau()) + "</td>");//buro
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getRating()==null?"":oanexo.getRating()) + "</td>");//rating
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getFecha()==null?"":oanexo.getFecha()) + "</td>");//fecha
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getLteAutorizado()==null?"":oanexo.getLteAutorizado()) + "</td>");//LteAutorizado
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getLteForm()==null?"":oanexo.getLteForm()) + "</td>");//LteForm
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getRgoActual()==null?"":oanexo.getRgoActual()) + "</td>");//RgoActual
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getRgoPropBbvaBc()==null?"":oanexo.getRgoPropBbvaBc()) + "</td>");//RgoPropBbvaBc
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getPropRiesgo()==null?"":oanexo.getPropRiesgo()) + "</td>");//PropRiesgo
										sb.append("<td class=\"alt3\" align=\"right\">" + (oanexo.getObservaciones()==null?"":oanexo.getObservaciones()) + "</td>");//observaciones
									}
								sb.append("</tr>");
								}
							}
							sb.append("</table>");				
							
				detallePosicionRiesgoAnexo= sb.toString();
			
			
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}		
			return detallePosicionRiesgoAnexo;
		}
		
		public void iniciarColumas(){
			try {
				listaColumnas = anexoBO.findColumnas();
				if(listaColumnas.isEmpty()){
					//ini MCG20140820
					AnexoColumna bureau = new AnexoColumna();
					bureau.setDescripcion(Constantes.DESC_BUREAU);
					//fin MCG20140820
					AnexoColumna rating = new AnexoColumna();
					rating.setDescripcion("RATING");
					AnexoColumna fecha = new AnexoColumna();
					fecha.setDescripcion("FECHA");
					AnexoColumna lteAutorizado = new AnexoColumna();
					lteAutorizado.setDescripcion("LTE AUTORIZADO");
					AnexoColumna lteForm = new AnexoColumna();
					lteForm.setDescripcion("LTE FORM");
					AnexoColumna rgoActual = new AnexoColumna();
					rgoActual.setDescripcion("RGO ACTUAL");
					AnexoColumna rgoPropBBVABC = new AnexoColumna();
					rgoPropBBVABC.setDescripcion("RGO PROP BBVA BC");
					//ini MCG20130802
					AnexoColumna propRiesgo = new AnexoColumna();
					propRiesgo.setDescripcion(Constantes.DESC_COLUMNA_PROP_RIESGO);								
					//fin MCG20130802				
					AnexoColumna observaciones = new AnexoColumna();
					observaciones.setDescripcion("OBSERVACIONES");
					listaColumnas.add(rating);
					listaColumnas.add(fecha);
					listaColumnas.add(lteAutorizado);
					listaColumnas.add(lteForm);
					listaColumnas.add(rgoActual);
					listaColumnas.add(rgoPropBBVABC);
					listaColumnas.add(propRiesgo);
					listaColumnas.add(observaciones);
				}
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
			}
			
		}
		
		public void calcularMontos(List<FilaAnexo> lista){
			if(lista != null &&
			   !lista.isEmpty()){
				anexoBO.calcularMontosPorEmpresa(lista);
				anexoBO.calcularMontoPorBanco(lista);
				filaTotal = anexoBO.calcularMontoTotal(lista);			
			}
		}
		
		
		public String SintesisEconomicoAnexo(String idprograma,int indcapitulo,List<TsubCaratula> listsubCaratula)
		{
			String sintesisEconomicoAnexo="";	
			List<SintesisEconomica> listaSintesisAnexo = new ArrayList<SintesisEconomica>();
			StringBuilder sb=new StringBuilder ();		
			try {
				
				Programa programa6 = new Programa();			
				programa6=programaBO.findById(Long.valueOf(idprograma));
				Long idTipoEmpresa=programa6.getTipoEmpresa().getId();
				String nombreGrupoEmpresa=programa6.getNombreGrupoEmpresa().trim();
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				listaSintesisAnexo=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				String codEmpresa="";
				String nombEmpresa="";
				int cont=0;		
				
				List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
				lstEmpresas = empresaBO.listarEmpresasPorPrograma(this.programa.getId());
				int index=0;
				if(programa6.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){	

			
					if (lstEmpresas!=null && lstEmpresas.size()<=0){				
						return "";
					}
					int cntsalto=0;	
					int conti=0;
					String strindex="";
					int totalempr=lstEmpresas.size();
					for (Empresa oempresa:lstEmpresas){
						conti++;
						index=0;
						String onombreEmpresa=oempresa.getNombre().trim();	
						String strCodiEmpresa=oempresa.getCodigo().trim();
						strindex=String.valueOf(index);
						
						sb.append("<h4><b><u> "+ indcapitulo +"."+ conti + ".- "+ onombreEmpresa +"</u></b><span class=\"clsinvisible\"> "+ Constantes.COD_SUBINDICE_INFORM_X_EMPRESA +"_"+strCodiEmpresa+ "</span></h4>");
						sb.append("<br/>");
						TsubCaratula osubcaratula=new TsubCaratula();
						osubcaratula.setIdSubcaratula(Long.valueOf(indcapitulo));
						osubcaratula.setIndiceSubcaratula(indcapitulo+"."+strCodiEmpresa);
						osubcaratula.setTituloSubcaratula(indcapitulo +"."+ conti + ".- "+ onombreEmpresa );
						String codigoTitulo=Constantes.COD_SUBINDICE_INFORM_X_EMPRESA +"_"+strCodiEmpresa;
						osubcaratula.setCodTituloInfxEmpresa(codigoTitulo);
						listsubCaratula.add(osubcaratula);
						
					    if (oempresa.getTipoGrupo()!=null && !oempresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
					    	index++;
					    	strindex=String.valueOf(index);
					    	String indDatosBasico=indcapitulo+"."+conti+"." + strindex; 
					    	sb.append(DatosBasicosPorEmpresaHTML(idprograma,oempresa,indDatosBasico,codigoTitulo ));				    	
					    }		
								
					  SintesisEconomica osintesiseconomica=ObtenerSintesisEconFinanExisteEmpresa(onombreEmpresa,listaSintesisAnexo);
					  if (osintesiseconomica!=null){
						String filename="";				
						if (!osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
							index++;
							strindex=String.valueOf(index);
							idsintesisecon=osintesiseconomica.getId()==null?"": osintesiseconomica.getId().toString();
							extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
							codEmpresa=osintesiseconomica.getCodEmpresa()==null?"": osintesiseconomica.getCodEmpresa().toString();
							nombEmpresa=osintesiseconomica.getNombreEmpresa()==null?"":osintesiseconomica.getNombreEmpresa().trim();
							cont+=1;
							filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
							if (idsintesisecon.equals("")||extension.equals("")){
								filename="";
							}
							if (!filename.equals("")){
								cntsalto++;
								if (cntsalto>1){
									sb.append("<div class=saltopage>&nbsp; </div>");
								}	
								strindex=String.valueOf(index);
//								sb.append("<h4><b><u>5." + strindex + ".-SINTESIS ECONOMICO - FINANCIERO: "+ nombEmpresa +"</u></b></h4>");	
//								sb.append("<br/>");			
//								sb.append(SituacionFinancieroBD(filename).toString()); 	
//								sb.append("<br/>");	
							
								//ini MCG20111102
								
								StringBuilder stbComenSituFinanciera = new StringBuilder(); 
						        StringBuilder stbComenSituEconomica = new StringBuilder();
								StringBuilder stbValoracionEconFinanciera=new StringBuilder();
								StringBuilder stbValoracionPosiBalance=new  StringBuilder();
								
								SintesisEconomicoBlob sintesisEconomicoBlob = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(programa6,codEmpresa);
						        if (sintesisEconomicoBlob!=null){
		
							 	      if(sintesisEconomicoBlob.getComenSituFinanciera()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getComenSituFinanciera() ){
							 		          	stbComenSituFinanciera.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getComenSituEconomica()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getComenSituEconomica() ){
							 		          	stbComenSituEconomica.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getValoracionEconFinanciera()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getValoracionEconFinanciera() ){
							 		          	stbValoracionEconFinanciera.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getValoracionPosiBalance()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getValoracionPosiBalance() ){
							 		          	stbValoracionPosiBalance.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
						        }	
						        
						         
						        stbComenSituFinanciera 		=	obtenerCadenaHTMLValidada(stbComenSituFinanciera); 
						        stbComenSituEconomica 		= 	obtenerCadenaHTMLValidada(stbComenSituEconomica);
								stbValoracionEconFinanciera	=	obtenerCadenaHTMLValidada(stbValoracionEconFinanciera);
								stbValoracionPosiBalance	=	obtenerCadenaHTMLValidada(stbValoracionPosiBalance);
								
								boolean bComenSituFinanciera=false;
								boolean bComenSituEconomica=false;
								boolean bValoracionEconFinanciera=false;
								boolean bValoracionPosiBalance=false;
								boolean bSituacionFinancieroBD=false;
								
								String strSituacionFinancieroBDVal="";
								if (stbComenSituFinanciera.toString().length()>0){
									bComenSituFinanciera=true;
								}
								if (stbComenSituEconomica.toString().length()>0){
									bComenSituEconomica=true;
								}
								if (stbValoracionEconFinanciera.toString().length()>0){
									bValoracionEconFinanciera=true;
								}
								if (stbValoracionPosiBalance.toString().length()>0){
									bValoracionPosiBalance=true;
								}
								strSituacionFinancieroBDVal=SituacionFinancieroBD(filename).toString();
								
								if (strSituacionFinancieroBDVal.length()>0){
									bSituacionFinancieroBD=true;
								}
								
								sb.append("<h5><b><u>"+ indcapitulo +"."+ conti +"." + strindex + ".-SÍNTESIS ECONÓMICO - FINANCIERO: "+ nombEmpresa +"</u></b></h4>");	
								
								if ( !bComenSituFinanciera &&
									     !bComenSituEconomica &&
									     !bValoracionEconFinanciera &&
									     !bValoracionPosiBalance&&
									     !bSituacionFinancieroBD){		        				
										sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Niguna Informaci&oacute;n que mostrar"); 	
										sb.append("<br/>");
										sb.append("<br/>");	
										
									}else{	
										sb.append("<br/>");	
										sb.append(strSituacionFinancieroBDVal); 	
										sb.append("<br/>");
										
									}
								
							
								if (bComenSituFinanciera){
									sb.append("<table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>COMENTARIO DE SITUACIÓN FINANCIERA:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbComenSituFinanciera.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");
								}
								if (bComenSituEconomica){
									sb.append( " <table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>COMENTARIO DE SITUACIÓN ECONÓMICA:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbComenSituEconomica.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");
								}
								if (bValoracionEconFinanciera){
								sb.append( " <table class=\"gridtable\" width=\"100%\">"    
								 		+ " <tr>" 		
						        		+ " 	<th>VALORACIÓN SITUACIÓN ECONÓMICA-FINANCIERA:</th>"		        	
						        		+ " </tr>" 
						        		+ " <tr>" 		        		
						        		+ " 	<td class=\"myEditor\">" + stbValoracionEconFinanciera.toString() + "</td>"
						        		+ " </tr>"    
						        		+ " </table>");					
								sb.append("<br/>");
								}
								if (bValoracionPosiBalance){
									sb.append( " <table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>ANÁLISIS Y VALORACIÓN POSICIONES FUERA DE BALANCE Y RIESGO ESTRUCTURAL:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbValoracionPosiBalance.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");	
								}
								//fin MCG20111102	
								
								
								//ini MCG20130314
						    	index++;
						    	strindex=String.valueOf(index);
						    	String indrating=indcapitulo +"."+ conti +"."+strindex ;
						    	sb.append(RatingPorEmpresaConSEHTML(idprograma,oempresa,indrating,null,""));//revisar envio vacio
							    	
							    							
								index++;
								strindex=String.valueOf(index);
								
						        String strExtractoSituacionFinancieroBDval="";
						    	String saltolinea="";
						        boolean bExtractoSituacionFinanciero=false;
						        
						        strExtractoSituacionFinancieroBDval=ExtractoSituacionFinancieroBD(filename).toString();
						        if (strExtractoSituacionFinancieroBDval.length()>0){
						        	bExtractoSituacionFinanciero=true;
						        	saltolinea="<div class=saltopage>&nbsp; </div>";
						        }
						        if (bExtractoSituacionFinanciero){
						        	sb.append(saltolinea);
									sb.append("<h5><b><u>"+ indcapitulo +"."+ conti +"." + strindex + ".- EXTRACTO SÍNTESIS ECONÓMICO - FINANCIERO: "+ nombEmpresa +"</u></b></h4>");	
									sb.append("<br/>");			
									sb.append(strExtractoSituacionFinancieroBDval); 	
									sb.append("<br/>");	
									
						        }else{	        
									sb.append("<h5><b><u>"+ indcapitulo +"."+ conti +"." + strindex + ".- EXTRACTO SÍNTESIS ECONÓMICO - FINANCIERO: "+ nombEmpresa +"</u></b></h4>");	
									sb.append("<br/>");			
									sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
									sb.append("<br/>");
						        }
								
								if(totalempr!=conti){								
										sb.append("<div class=saltopage>&nbsp; </div>");								
								};
								
								//fin MCG20130314
							}
						}	 //IF SINTESIS ECONOMICA
					  }	
					}	
				
				}	
				
				sintesisEconomicoAnexo= sb.toString();
			
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
		return sintesisEconomicoAnexo;
		}
		
		
		//ini MCG20140909
		public String SintesisEconomicoPorEmpresaIndHTML(String idprograma,Empresa oempresa,String indcapitulo,Tvariable oasignasalto,String codigoTitulo)
		{
			String sintesisEconomicoAnexo="";	
			List<SintesisEconomica> listaSintesisAnexo = new ArrayList<SintesisEconomica>();
			StringBuilder sb=new StringBuilder ();
			
			try {
				
				Programa programa6 = new Programa();			
				programa6=programaBO.findById(Long.valueOf(idprograma));
				Long idTipoEmpresa=programa6.getTipoEmpresa().getId();
				String nombreGrupoEmpresa=programa6.getNombreGrupoEmpresa().trim(); //para grupo:: es nombre de grupo
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				listaSintesisAnexo=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				String codEmpresa="";
				String nombEmpresa="";
				

				if(programa6.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){	

		
					String onombreEmpresa=oempresa.getNombre().trim();	
					String strCodiEmpresa=oempresa.getCodigo().trim();	
								
					  SintesisEconomica osintesiseconomica=ObtenerSintesisEconFinanExisteEmpresa(onombreEmpresa,listaSintesisAnexo);
					  if (osintesiseconomica!=null){
						String filename="";				
						if (!osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
							
							idsintesisecon=osintesiseconomica.getId()==null?"": osintesiseconomica.getId().toString();
							extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
							codEmpresa=osintesiseconomica.getCodEmpresa()==null?"": osintesiseconomica.getCodEmpresa().toString();
							nombEmpresa=osintesiseconomica.getNombreEmpresa()==null?"":osintesiseconomica.getNombreEmpresa().trim();
							
							filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
							if (idsintesisecon.equals("")||extension.equals("")){
								filename="";
							}
							if (!filename.equals("")){

								StringBuilder stbComenSituFinanciera = new StringBuilder(); 
						        StringBuilder stbComenSituEconomica = new StringBuilder();
								StringBuilder stbValoracionEconFinanciera=new StringBuilder();
								StringBuilder stbValoracionPosiBalance=new  StringBuilder();
								
								SintesisEconomicoBlob sintesisEconomicoBlob = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(programa6,codEmpresa);
						        if (sintesisEconomicoBlob!=null){
		
							 	      if(sintesisEconomicoBlob.getComenSituFinanciera()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getComenSituFinanciera() ){
							 		          	stbComenSituFinanciera.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getComenSituEconomica()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getComenSituEconomica() ){
							 		          	stbComenSituEconomica.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getValoracionEconFinanciera()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getValoracionEconFinanciera() ){
							 		          	stbValoracionEconFinanciera.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
							 	      if(sintesisEconomicoBlob.getValoracionPosiBalance()!=null){
							 		        for(byte x :sintesisEconomicoBlob.getValoracionPosiBalance() ){
							 		          	stbValoracionPosiBalance.append((char)FormatUtil.getCharUTF(x));
							 		        }
							 	        }
						        }	
						        
						         
						        stbComenSituFinanciera 		=	obtenerCadenaHTMLValidada(stbComenSituFinanciera); 
						        stbComenSituEconomica 		= 	obtenerCadenaHTMLValidada(stbComenSituEconomica);
								stbValoracionEconFinanciera	=	obtenerCadenaHTMLValidada(stbValoracionEconFinanciera);
								stbValoracionPosiBalance	=	obtenerCadenaHTMLValidada(stbValoracionPosiBalance);
								
								boolean bComenSituFinanciera=false;
								boolean bComenSituEconomica=false;
								boolean bValoracionEconFinanciera=false;
								boolean bValoracionPosiBalance=false;
								boolean bSituacionFinancieroBD=false;
								
								String strSituacionFinancieroBDVal="";
								if (stbComenSituFinanciera.toString().length()>0){
									bComenSituFinanciera=true;
								}
								if (stbComenSituEconomica.toString().length()>0){
									bComenSituEconomica=true;
								}
								if (stbValoracionEconFinanciera.toString().length()>0){
									bValoracionEconFinanciera=true;
								}
								if (stbValoracionPosiBalance.toString().length()>0){
									bValoracionPosiBalance=true;
								}
								strSituacionFinancieroBDVal=SituacionFinancieroBD(filename).toString();
								
								if (strSituacionFinancieroBDVal.length()>0){
									bSituacionFinancieroBD=true;
								}
								
								sb.append("<h4><b><u>"+ indcapitulo +".-SÍNTESIS ECONÓMICO - FINANCIERO: "+ nombEmpresa +"</u></b><span class=\"clsinvisible\"> "+ codigoTitulo +"</span></h4>");	
								
								if ( !bComenSituFinanciera &&
									     !bComenSituEconomica &&
									     !bValoracionEconFinanciera &&
									     !bValoracionPosiBalance&&
									     !bSituacionFinancieroBD){	
										oasignasalto.setSintesisEconomicoSLinea(false);
										sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
										sb.append("<br/>");
										sb.append("<br/>");	
										
									}else{	
										sb.append("<br/>");	
										oasignasalto.setSintesisEconomicoSLinea(true);
										sb.append(strSituacionFinancieroBDVal); 	
										sb.append("<br/>");
										
									}
								
							
								if (bComenSituFinanciera){
									sb.append("<table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>COMENTARIO DE SITUACIÓN FINANCIERA:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbComenSituFinanciera.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");
								}
								if (bComenSituEconomica){
									sb.append( " <table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>COMENTARIO DE SITUACIÓN ECONÓMICA:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbComenSituEconomica.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");
								}
								if (bValoracionEconFinanciera){
								sb.append( " <table class=\"gridtable\" width=\"100%\">"    
								 		+ " <tr>" 		
						        		+ " 	<th>VALORACIÓN SITUACIÓN ECONÓMICA-FINANCIERA:</th>"		        	
						        		+ " </tr>" 
						        		+ " <tr>" 		        		
						        		+ " 	<td class=\"myEditor\">" + stbValoracionEconFinanciera.toString() + "</td>"
						        		+ " </tr>"    
						        		+ " </table>");					
								sb.append("<br/>");
								}
								if (bValoracionPosiBalance){
									sb.append( " <table class=\"gridtable\" width=\"100%\">"    
									 		+ " <tr>" 		
							        		+ " 	<th>ANÁLISIS Y VALORACIÓN POSICIONES FUERA DE BALANCE Y RIESGO ESTRUCTURAL:</th>"		        	
							        		+ " </tr>" 
							        		+ " <tr>" 		        		
							        		+ " 	<td class=\"myEditor\">" + stbValoracionPosiBalance.toString() + "</td>"
							        		+ " </tr>"    
							        		+ " </table>");					
									sb.append("<br/>");	
								}
							}
						}	 //IF SINTESIS ECONOMICA
					  }	else{
						  	oasignasalto.setSintesisEconomicoSLinea(false);
						  	sb.append("<h4><b><u>"+ indcapitulo +".-SÍNTESIS ECONÓMICO - FINANCIERO: "+ nombEmpresa +"</u></b><span class=\"clsinvisible\"> "+ codigoTitulo +"</span></h4>");	
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Constantes.MENSAJE_NO_EXISTE_INFORMACION); 	
							sb.append("<br/>");
							sb.append("<br/>");
					  }	
				}	
				
				sintesisEconomicoAnexo= sb.toString();
			
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
			return sintesisEconomicoAnexo;
		}
		
		//fin MCG20140909
		
		//ini MCG20130318
		private SintesisEconomica ObtenerSintesisEconFinanExisteEmpresa(String nombreEmpresa,List<SintesisEconomica> lista){
			SintesisEconomica osintesisEconomivo=new SintesisEconomica();
			
			//boolean bexiste=false;
			try {	
				osintesisEconomivo=null;
				if (lista!=null && lista.size()>0){
					for(SintesisEconomica osintesisEconomica : lista){
						if (nombreEmpresa.equalsIgnoreCase(osintesisEconomica.getNombreEmpresa().trim())){
							osintesisEconomivo=new SintesisEconomica();
							osintesisEconomivo=osintesisEconomica;
							//bexiste=true;
							break;
						}
					}
				}else{
					//bexiste=false;
					osintesisEconomivo=null;
				}
			} catch (Exception e) {
				//bexiste=false;
				osintesisEconomivo=null;			
			}		
			
			return osintesisEconomivo;
		}
		
		//ini MCG20130318
		
		
		//ini MCG20130314
		public String ExtractoSintesisEconomico(String idprograma)
		{
			String extractoSintesisEconomico="";	
			List<SintesisEconomica> listaSintesisAnexo = new ArrayList<SintesisEconomica>();
			StringBuilder sb=new StringBuilder ();		
			try {
				
				Programa programa6 = new Programa();			
				programa6=programaBO.findById(Long.valueOf(idprograma));
				String nombreGrupoEmpresa=programa6.getNombreGrupoEmpresa();
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				listaSintesisAnexo=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				String codEmpresa="";
				String nombEmpresa="";
				int cont=0;			
				for (SintesisEconomica osintesiseconomica:listaSintesisAnexo ){
					String filename="";				
					if (!osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
						idsintesisecon=osintesiseconomica.getId()==null?"": osintesiseconomica.getId().toString();
						extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
						codEmpresa=osintesiseconomica.getCodEmpresa()==null?"": osintesiseconomica.getCodEmpresa().toString();
						nombEmpresa=osintesiseconomica.getNombreEmpresa()==null?"":osintesiseconomica.getNombreEmpresa();
						cont+=1;
						filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
						if (idsintesisecon.equals("")||extension.equals("")){
							filename="";
						}
						if (!filename.equals("")){
							sb.append("<div class=saltopage>&nbsp; </div>");
							sb.append("<h4><b><u>Extracto Síntesis Económico - Financiero: "+ nombEmpresa +"</u></b></h4>");	
							sb.append("<br/>");			
							sb.append(ExtractoSituacionFinancieroBD(filename).toString()); 	
							sb.append("<br/>");	
							
						}
					}				
				}		
				extractoSintesisEconomico= sb.toString();
			
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
		return extractoSintesisEconomico;
		}
		
		//fin MCG20130314
		
		public String DetalleEconomicoFinancieroGrupo(String idprograma)
		{
			String detalleEconFinangrupo="";	
			List<SintesisEconomica> listaSintesisdetEconFin = new ArrayList<SintesisEconomica>();
			StringBuilder sb=new StringBuilder ();		
			try {
				
				Programa programa7 = new Programa();			
				programa7=programaBO.findById(Long.valueOf(idprograma));
				String nombreGrupoEmpresa=programa7.getNombreGrupoEmpresa();
				
				listaSintesisdetEconFin=sintesisEconomicoBO.findSintesisEconomicoSQL(idprograma);
				String idsintesisecon="";
				String extension="xls";
				for (SintesisEconomica osintesiseconomica:listaSintesisdetEconFin ){
					if (osintesiseconomica.getNombreEmpresa().equals(nombreGrupoEmpresa)){
						idsintesisecon=osintesiseconomica.getId()==null?"":osintesiseconomica.getId().toString();
						extension=osintesiseconomica.getExtension()==null?"":osintesiseconomica.getExtension();
						break;
					}				
				}
				
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
				String filename=parametro.getValor()+File.separator + idsintesisecon + "." + extension;
				
				if (idsintesisecon.equals("") || extension.equals("")){
					filename="";
				}	
				/*
				sb.append("<h4><b><u>ANEXO A2-1</u></b></h4>");	
				sb.append("<br/>");			
				sb.append(DetalleEconFinanGrupoBD(filename,Constantes.NUM_SHEET_BALANCE,117,12).toString()); 	
				sb.append("<div class=saltopage>&nbsp; </div>");
				
				sb.append("<h4><b><u>ANEXO A2-2</u></b></h4>");
				sb.append("<br/>");	
				sb.append(DetalleEconFinanGrupoBD2(filename,Constantes.NUM_SHEET_RTDOS_CASH,137,13).toString()); */
				//sb.append("<div class=saltopage>&nbsp; </div>");					
				detalleEconFinangrupo= sb.toString();
			
			
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			
		return detalleEconFinangrupo;
		}
		
		public String DetalleEconFinanGrupoBD(String strruta,int hoja,int limiterow,int limitecol) {
			
			String strdetEconFinancGrupo="";		
			StringBuilder sbsf=new StringBuilder ();
			FormulaEvaluator fEval=null;		
			try {
				if (!strruta.equals(""))	{
					FileInputStream fis;	
					String filename=strruta;	
					fis = new FileInputStream(filename);
				    HSSFWorkbook wbDataPlantEmpPrin = new HSSFWorkbook(fis);
				    HSSFSheet hojaOrigen=wbDataPlantEmpPrin.getSheetAt(hoja);
				    fEval=wbDataPlantEmpPrin.getCreationHelper().createFormulaEvaluator();
				    
					  Iterator rows = hojaOrigen.rowIterator(); 
					  sbsf.append("<table class=\"gridtablereducido\" width=\"100%\">");
				        while (rows.hasNext()) {
				        
				            HSSFRow row = (HSSFRow) rows.next();
		
				            if (row.getRowNum()==limiterow){
				            	break;	
				            }	
				            			            
				            sbsf.append("<tr>");
			            			            
				            for (int i=0;i<limitecol;i++){
				            	String valor="";
				            	String classdt="";
				            	 if (i==2 || i==3){
						            	continue;	
						          }	

				            	
					            if (row.getCell(i)==null){			            	
					            	 valor=""; 
					            }else{
					            
						            	CellValue cellValue=fEval.evaluate(row.getCell(i));
						                if(row.getCell(i).getColumnIndex() < limitecol && cellValue!=null){
						                	
						                    switch(cellValue.getCellType())
						                    {
						                        case HSSFCell.CELL_TYPE_NUMERIC:
						                        		                        	
						                        						                        	
						                        	if (row.getRowNum()>=6 && row.getRowNum()<=54 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=69 && row.getRowNum()<=99 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	}else if (row.getRowNum()>=113 && row.getRowNum()<=116 && (i==2 || i==4 || i==6||i==8||i==10)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);
						                        	}else
						                        	{
						                        		valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),1);
						                        	}			                        					                        	
						                        	
						                        	classdt="class=\"mydtNumero\"";			                        	
						                            break;
						                        case HSSFCell.CELL_TYPE_STRING:   
						                        	valor=cellValue.getStringValue()==null?"":cellValue.getStringValue();
						                        	classdt="class=\"mydtString\"";
						                        	break; 
						                        case HSSFCell.CELL_TYPE_BLANK: valor=""; 
						                       		break;		                        	
						                        case HSSFCell.CELL_TYPE_FORMULA:
						                        	valor="";
						                        	break;	
						                       default :
						                    	   valor=String.valueOf(cellValue.getStringValue()==null?"":cellValue.getStringValue());
						                    }	
						                }
						              
					            }
					            
				            	 if (row.getRowNum()==4 && i>0){
						            	classdt="class=\"mydtcab\"";
						         }
				            	 if (row.getRowNum()==67 && i>0){
						            	classdt="class=\"mydtcab\"";
						         }
				            	 if (row.getRowNum()==112 && i==1){
						            	classdt="class=\"mydtcab\"";
						            }	
					            
					        	sbsf.append("<td "+ classdt +">");
				            	 sbsf.append(valor); 
				            	sbsf.append("</td>");
				            }
				            sbsf.append("</tr>");
				        }
				        sbsf.append("</table>");
			        
				}else{
					sbsf.append("");
				}			        	
				strdetEconFinancGrupo= sbsf.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strdetEconFinancGrupo;
		}
		//ini MCG20111209
		public String DetalleEconFinanGrupoBD2(String strruta,int hoja,int limiterow,int limitecol) {
			
			String strdetEconFinancGrupo="";		
			StringBuilder sbsf=new StringBuilder ();
			FormulaEvaluator fEval=null;		
			try {
				if (!strruta.equals(""))	{
					FileInputStream fis;	
					String filename=strruta;	
					fis = new FileInputStream(filename);
				    HSSFWorkbook wbDataPlantEmpPrin = new HSSFWorkbook(fis);
				    HSSFSheet hojaOrigen=wbDataPlantEmpPrin.getSheetAt(hoja);
				    fEval=wbDataPlantEmpPrin.getCreationHelper().createFormulaEvaluator();
				    
					  Iterator rows = hojaOrigen.rowIterator(); 
					  sbsf.append("<table class=\"gridtablereducido\" width=\"100%\">");
				        while (rows.hasNext()) {
				        
				            HSSFRow row = (HSSFRow) rows.next();
		
				            if (row.getRowNum()==limiterow){
				            	break;	
				            }	
				            			            
				            sbsf.append("<tr>");
			            			            
				            for (int i=0;i<limitecol;i++){
				            	String valor="";
				            	String classdt="";
				            	 if (i==2 || i==3){
						            	continue;	
						          }	
				            	
					            if (row.getCell(i)==null){			            	
					            	 valor=""; 
					            }else{
					            
						            	CellValue cellValue=fEval.evaluate(row.getCell(i));
						                if(row.getCell(i).getColumnIndex() < limitecol && cellValue!=null){
						                	
						                    switch(cellValue.getCellType())
						                    {
						                        case HSSFCell.CELL_TYPE_NUMERIC:
						                        		                        	
						                        	//CUENTA DE RESULTADOS					                        	
						                        	if (row.getRowNum()>=7 && row.getRowNum()<=10 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=13 && row.getRowNum()<=36 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=39 && row.getRowNum()<=45 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=48 && row.getRowNum()<=49 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=52 && row.getRowNum()<=53 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);					        		            	
						                        	}else if (row.getRowNum()>=58 && row.getRowNum()<=62 && (i==3 || i==5 || i==7||i==9||i==11)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	//RATIOS
						                        	}else if (row.getRowNum()>=84 && row.getRowNum()<=86 && (i==4 ||i==6 || i==8 || i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	}else if (row.getRowNum()==91 && (i==4 ||i==6 || i==8 || i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);					                        	
						                        	}else if (row.getRowNum()>=93 && row.getRowNum()<=95 && (i==4 ||i==6 || i==8 || i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	}else if (row.getRowNum()==96 && (i==4 ||i==6 || i==8 || i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);
						                        	}else if (row.getRowNum()==97 && (i==4 ||i==6 || i==8||i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),2);						                        	
							                        }else if (row.getRowNum()==98 && (i==4 ||i==6 || i==8||i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPuntoPorcentaje(cellValue.getNumberValue(),1);
						                        	//ANALISIS CASH FLOW
						                        	}else if (row.getRowNum()>=105 && row.getRowNum()<=136&& (i==4 || i==6 || i==8||i==10||i==12)){					        		            	
						        		            	valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),0);
						                        	}else
						                        	{
						                        		valor=FormatUtil.roundDecimalsPunto(cellValue.getNumberValue(),1);
						                        	}			                        					                        	
						                        	
						                        	classdt="class=\"mydtNumero\"";			                        	
						                            break;
						                        case HSSFCell.CELL_TYPE_STRING:   
						                        	valor=cellValue.getStringValue()==null?"":cellValue.getStringValue();
						                        	classdt="class=\"mydtString\"";
						                        	break; 
						                        case HSSFCell.CELL_TYPE_BLANK: valor=""; 
						                       		break;		                        	
						                        case HSSFCell.CELL_TYPE_FORMULA:
						                        	valor="";
						                        	break;	
						                       default :
						                    	   valor=String.valueOf(cellValue.getStringValue()==null?"":cellValue.getStringValue());
						                    }	
						                }
						              
					            }
					            
				            	 if (row.getRowNum()==5 && i>0){
						            	classdt="class=\"mydtcab\"";
						         }
				            	 if (row.getRowNum()==82 && i>0){
						            	classdt="class=\"mydtcab\"";
						         }
				            	 if (row.getRowNum()==103 && i>0){
						            	classdt="class=\"mydtcab\"";
						            }	
				            	 
					        	sbsf.append("<td "+ classdt +">");
				            	 sbsf.append(valor); 
				            	sbsf.append("</td>");
				            }
				            sbsf.append("</tr>");
				        }
				        sbsf.append("</table>");
			        
				}else{
					sbsf.append("");
				}			        	
				strdetEconFinancGrupo= sbsf.toString();		
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				return "";
			}
			return strdetEconFinancGrupo;
		}	
		//fin MCG20111209
		

	    
		@Override
	    public void dowloadPDFGeneral(HashMap<String, String> parametros) throws Exception {
	    	
	    	//System.out.println("INI dowloadPDFGeneral");
	    	logger.info("INI dowloadPDFGeneral");
			String Idprograma= (String) parametros.get("IdProgramaD");
			String codigoTipoOperacion=parametros.get("CodigoTipoOperacionD")==null?Constantes.ID_TIPO_OPERACION_PROGRAMA_FINANCIERO:(String)parametros.get("CodigoTipoOperacionD");
			//System.out.println("dowloadPDFGeneral:codigoTipoOperacion"+codigoTipoOperacion);
			
			String codigoDocumento=Idprograma;
			String extencionDocumento= "pdf";
			String extensionDocumentoxhtml="xhtml";
			String nombreDocumento="PF" + codigoDocumento;
			String nombreArchivo=nombreDocumento + "." + extencionDocumento; 
			String nombreArchivoxhtml=nombreDocumento + "." + extensionDocumentoxhtml;
			
			logger.info("codigoDocumento: " + codigoDocumento);
			logger.info("extencionDocumento: " + extencionDocumento);
			logger.info("nombreDocumento: " + nombreDocumento);
			logger.info("nombreArchivo: " + nombreArchivo);		
			File file=null;  
			String forward = SUCCESS;
			try {
				try {
					String estadoDescargaPDF=Constantes.REP_PROCESANDO;
					programaBO.actualizarEstadoDescargaPDF(Long.valueOf(Idprograma),estadoDescargaPDF);
				} catch (Exception e) {
					logger.error("actualizarEstadoDescargaPDF error:"+StringUtil.getStackTrace(e));
				}
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_EXPPDF_PROGRAMA_FINANCIERO);
				String pathRutaPDF=parametro.getValor();
				String pathToFilePDF=parametro.getValor()+File.separator+nombreArchivo;
				String pathToFilePDF_Pre=parametro.getValor()+File.separator+"pre_"+nombreArchivo;
				String pathToFileXHTML=parametro.getValor()+File.separator+nombreArchivoxhtml;
				//genera Archivo PDF de xhtml
				logger.info("Generando archivo en pathToFilePDF="+pathToFilePDF);
				logger.info("Generando archivo en pathToFileXHTML="+pathToFileXHTML);
				//SE HABILITA PARA PRUEBAS LOCALES
				//pathToFileXHTML="D:\\mnt\\compartido\\profin\\export\\PF20648.xhtml";
				//pathToFilePDF="D:\\mnt\\compartido\\profin\\export\\PF20648.pdf";
				logger.info("ini ReportPDF");
				ReportPDF(Idprograma,pathToFileXHTML,pathToFilePDF,pathToFilePDF_Pre,codigoTipoOperacion,pathRutaPDF,nombreArchivo);  			
				logger.info("fin ReportPDF");
				try {
					String estadoDescargaPDF=Constantes.REP_TERMINO;
					programaBO.actualizarEstadoDescargaPDF(Long.valueOf(Idprograma),estadoDescargaPDF);
				} catch (Exception e) {
					logger.error("actualizarEstadoDescargaPDF error:"+StringUtil.getStackTrace(e));
				}
				

			} catch (BOException e) {					
				logger.error(StringUtil.getStackTrace(e));
				try {
					String estadoDescargaPDF=Constantes.REP_ERROR;
					programaBO.actualizarEstadoDescargaPDF(Long.valueOf(Idprograma),estadoDescargaPDF);
				} catch (Exception e1) {
					logger.error(StringUtil.getStackTrace(e1));
				}
				forward = "consultasModificaciones";
			}catch (Exception e) {				
				logger.info("Error getInputStream: "+StringUtil.getStackTrace(e));
				try {
					String estadoDescargaPDF=Constantes.REP_ERROR;
					programaBO.actualizarEstadoDescargaPDF(Long.valueOf(Idprograma),estadoDescargaPDF);
				} catch (Exception e2) {
					logger.error(StringUtil.getStackTrace(e2));
				}
				forward = "consultasModificaciones";
			}
			//System.out.println("FIN dowloadPDFGeneral");
			logger.info("FIN dowloadPDFGeneral");
			
			
	    }
		
		
	    //ini MCG20140530
	 
	    public void lecturaPDF(String ruta,String ruta_Pre,boolean btipoGrupo,List<Tcaratula> listaIndiceCaratula){   	
	    	
	    	int numeroPagina=0;
	    	OutputStream oss=null;
	    	PDDocument pd=null;
	              try { 
	            	   oss = new FileOutputStream(ruta);
	            	  logger.info("inicia PDDocument");
	                   pd = PDDocument.load(ruta_Pre); //CARGAR EL PDF
	                  logger.info("fin PDDocument");
	                  List allPages = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
	                  //Object[] obj = allPages.toArray();//METO EN UN OBJETO LA LISTA DE PAGINAS PARA MANIPULARLA
	                  logger.info("paso object");
	                  numeroPagina=allPages.size();

	                  PDFont font = PDType1Font.HELVETICA;
	                  float fontSize = 6.0f;
	                  
	                  for (int y=0;y<allPages.size();y++){
	                	  logger.info("entro for");
		                  PDPage page = (PDPage) allPages.get(y);//PAGE ES LA PAGINA 1 DE LA QUE CONSTA EL ARCHIVO
		                  logger.info("asigno page");
		                  int width = 792;//ANCHO
		                  int eigth=1024;//ALTO
		                  logger.info("ini stripper" );
		                  PDFTextStripperByArea stripper = new PDFTextStripperByArea();//COMPONENTE PARA ACCESO AL TEXTO
		                  logger.info("crea rect");
		                  Rectangle rect = new Rectangle(0, 0, width, eigth);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
		                  logger.info("add region");
		                  stripper.addRegion(("area"+y), rect);//REGISTRAMOS LA REGION CON UN NOMBRE
		                  logger.info("ini extrae region");
		                  stripper.extractRegions(page);//EXTRAE TEXTO DEL AREA
		                  logger.info("fin extrae region");
		                  String contenido = new String();//CONTENIDO = A LO QUE CONTENGA EL AREA O REGION
		                  logger.info("ini getTextForRegion"); 
		                  contenido=(stripper.getTextForRegion("area"+y));	                                   
		                  logger.info("obtiene contenido de pdf: " +contenido);                 
		                  
		                  //mil2014
		                  logger.info("ini contentStream ");
		                  PDPageContentStream contentStream = new PDPageContentStream(pd, page, true, true,true);
		                  contentStream.beginText();	                  
		                  contentStream.setFont( font, fontSize );	                  
		                  contentStream.setNonStrokingColor(0, 0, 0);
		                  contentStream.setTextTranslation(540, 5);
		                  contentStream.drawString("Página "+(y+1)+" de "+ (numeroPagina));
		                  contentStream.endText();
		                  contentStream.close();
		                  logger.info("fin contentStream ");
		                  //mil2014                  
		                  
		                  if (y>1){
		                	  int pag=0;
		                		  pag=y+1;
		                		  logger.info("ini buscarCodificacionPagina ");
		                		  buscarCodificacion(pag, contenido,listaIndiceCaratula);
		                		  logger.info("fin buscarCodificacionPagina ");
		                  } 	                  
	                  }//for 
	                  
	                  //ini mil2014
	                  
	                  PDPage page2 = (PDPage) allPages.get(1);                 
	                  PDPageContentStream contentStream2 = new PDPageContentStream(pd, page2, true, true,true);                  
	                  
	                  contentStream2.beginText();                  
	                  contentStream2.setFont( font, fontSize );                 
	                  contentStream2.setNonStrokingColor(0, 0, 0);
	                  int posicionY=0;
	                  posicionY=735;
	                  
	                  contentStream2.setTextTranslation(60, posicionY);
	                  contentStream2.drawString("");
	                  contentStream2.setTextTranslation(495, posicionY);               
	                  contentStream2.drawString("Página");
	                  
	                  if (listaIndiceCaratula!=null && listaIndiceCaratula.size()>0){
	                	  int contLinea=0;
	                	  int contador=1;
	                	  int residuo=0;
		                  for (Tcaratula ocaratula: listaIndiceCaratula){
			                  posicionY-=15;//25;//660	                  
			                  
			                  contLinea+=1;
			                  residuo=contLinea%47;
			                  if(residuo==0){
			                	  contador+=1;
			                	  
			                      contentStream2.endText();                  
			                      contentStream2.close();
			                      
			                	  PDPage page = (PDPage) allPages.get(contador);                 
			                      contentStream2 = new PDPageContentStream(pd, page, true, true,true);  
			                      contentStream2.beginText();                  
			                      contentStream2.setFont( font, fontSize );                 
			                      contentStream2.setNonStrokingColor(0, 0, 0);
			                      
			                      posicionY=735;
//			                      contentStream2.setTextTranslation(495, posicionY+15);               
//			                      contentStream2.drawString("Página");
			                  }
			                  
			                  contentStream2.setTextTranslation(60, posicionY);
			                  contentStream2.drawString(ocaratula.getTitulo());
			                  if (!ocaratula.getIndice().equals(Constantes.IDENTIFICADOR_ARCHIVO_ANEXO)){
				                  contentStream2.setTextTranslation(500, posicionY);		                  
				                  String opagina=ocaratula.getPagina()==null?"":ocaratula.getPagina();
				                  contentStream2.drawString(opagina);
				                  logger.info(ocaratula.getTitulo()+":::"+opagina);	
		                  	  }
	 
			                  if (ocaratula.getIndice().equals(Constantes.IDENTIFICADOR_INFORM_X_EMPRESA)){		                	  
			                	 if (ocaratula.getListSubCaratula()!=null && ocaratula.getListSubCaratula().size()>0){ 
				                	  for (TsubCaratula  osubcaratula:ocaratula.getListSubCaratula()){
				                		  
				                		  posicionY-=15;//25;
				                		  contLinea+=1;
						                  residuo=contLinea%47;
						                  if(residuo==0){
						                	  contador+=1;
						                	  
						                      contentStream2.endText();                  
						                      contentStream2.close();
						                      
						                	  PDPage page = (PDPage) allPages.get(contador);                 
						                      contentStream2 = new PDPageContentStream(pd, page, true, true,true);  
						                      contentStream2.beginText();                  
						                      contentStream2.setFont( font, fontSize );                 
						                      contentStream2.setNonStrokingColor(0, 0, 0);					                      
						                      posicionY=735;	
//						                      contentStream2.setTextTranslation(495, posicionY+15);               
//						                      contentStream2.drawString("Página");
						                  }
						                  contentStream2.setTextTranslation(70, posicionY);
						                  contentStream2.drawString(osubcaratula.getTituloSubcaratula());
						                  contentStream2.setTextTranslation(500, posicionY);
						                  String subpagina=osubcaratula.getPagina()==null?"":osubcaratula.getPagina();
						                  contentStream2.drawString(subpagina);
						                  logger.info(osubcaratula.getTituloSubcaratula()+":::"+subpagina);
				                	  }
			                	 }		                	  
			                  }else if (ocaratula.getIndice().equals(Constantes.IDENTIFICADOR_ARCHIVO_ANEXO)){		                	  
				                	 if (ocaratula.getListSubCaratula()!=null && ocaratula.getListSubCaratula().size()>0){ 
					                	  for (TsubCaratula  osubcaratula:ocaratula.getListSubCaratula()){
					                		  
					                		  posicionY-=15;//25;
					                		  contLinea+=1;
							                  residuo=contLinea%47;
							                  if(residuo==0){
							                	  contador+=1;
							                	  
							                      contentStream2.endText();                  
							                      contentStream2.close();
							                      
							                	  PDPage page = (PDPage) allPages.get(contador);                 
							                      contentStream2 = new PDPageContentStream(pd, page, true, true,true);  
							                      contentStream2.beginText();                  
							                      contentStream2.setFont( font, fontSize );                 
							                      contentStream2.setNonStrokingColor(0, 0, 0);					                      
							                      posicionY=735;	
//							                      contentStream2.setTextTranslation(495, posicionY+15);               
//							                      contentStream2.drawString("Página");
							                  }
							                  
							                  contentStream2.setTextTranslation(70, posicionY);
							                  contentStream2.drawString(osubcaratula.getTituloSubcaratula());						                  
							                  logger.info(osubcaratula.getTituloSubcaratula());
					                	  }
				                	 }		                	  
				              }
		                  }
	                  }
	            
	                  
	                  contentStream2.endText();                  
	                  contentStream2.close();
	                  
	                  //fin contenido indice
	                  logger.info("fin  allPages.get(1) ");
	                  
	                  pd.save( oss );
	                  logger.info("paso save ");
	                  oss.close();
	                  logger.info("paso ossclose ");
	                  pd.close();//CERRAMOS OBJETO ACROBAT
	                  logger.info("pdclose ");
	                  //ini mil2014
	    
	              } catch (IOException e) {            	  
	                  e.printStackTrace();                  
	                  logger.error("Error 1 PDDocument: " + e.getMessage());
	              }catch (COSVisitorException e1) {            	  
	                  e1.printStackTrace();                  
	                  logger.error("Error 2 PDDocument: " + e1.getMessage());
	              }catch (Exception e2) {            	  
	                  e2.printStackTrace();                  
	                  logger.error("Error 2 PDDocument: " + e2.getMessage());
	              }finally {
	                  if (pd!=null){
		                	  try {
		                		  pd.close();
		                	      }
		                	 catch ( IOException ignored) {
		                	      }
	                	  }
	                  try {
	                      if (oss != null){
	                    	  oss.close();
	                      }                   

	                  } catch (IOException ioe) {
	                  	logger.error("Error lecturaPDF IO: "+ ioe.getMessage());
	                      ioe.printStackTrace();
	                      
	                  }
	              }
	    }
	    

	    
	    private void buscarCodificacion(int pagina,String contenido,List<Tcaratula> olistaIndiceCaratula){
	    	if (olistaIndiceCaratula!=null && olistaIndiceCaratula.size()>0){
	    		logger.info("ini busqueda codigo titulo");
		    	for (Tcaratula ocaratula: olistaIndiceCaratula){
		    		if (!ocaratula.getIndice().equals(Constantes.IDENTIFICADOR_ARCHIVO_ANEXO)){
			    		logger.info("ocaratula:"+ocaratula.getIndice()+":codigotitulo:"+ocaratula.getCodTitulo()+":pagina:"+ocaratula.getPagina()+":titulo:"+ocaratula.getTitulo());
			   			 String codTitulo=ocaratula.getCodTitulo();
						 if (buscarCodificacionPagina(contenido,codTitulo)>0){
							 ocaratula.setPagina(String.valueOf(pagina));	
							 logger.info("codigoindice y pagina:"+codTitulo+":::"+String.valueOf(pagina));
						 } 
			    		 if (ocaratula.getIndice().equals(Constantes.IDENTIFICADOR_INFORM_X_EMPRESA)) {
			    			 if (ocaratula.getListSubCaratula()!=null && ocaratula.getListSubCaratula().size()>0) {	    				 
			    				 for (TsubCaratula osubCaratula:ocaratula.getListSubCaratula()){
			    					 logger.info("osubCaratula: indicesubcaratutla::"+osubCaratula.getIndiceSubcaratula()+":pagina:"+osubCaratula.getPagina()+":codigosubind:"+osubCaratula.getCodTituloInfxEmpresa()+":titulosubt:"+osubCaratula.getTituloSubcaratula());
			    					 String codTitulosub=osubCaratula.getCodTituloInfxEmpresa();
			    					 if (buscarCodificacionPagina(contenido,codTitulosub)>0){
			    						 osubCaratula.setPagina(String.valueOf(pagina));
			    						 logger.info("codigosubindice y pagina:"+codTitulosub+":::"+String.valueOf(pagina));
			    					 }
			    				 }
			    		 	}	    			 
			    		 }
		    		}	 
		    	}//for
	    	}    	
	    	logger.info("fin busqueda codigo titulo");
	    }
	    
	    public int buscarCodificacionPagina(String sTexto, String sTextoBuscado){
	        int contador = 0;
	    	try {
	    		if ((sTexto==null) || (sTexto!=null && sTexto.equals(""))){sTexto="x";};
	            while (sTexto.indexOf(sTextoBuscado) > -1) {
	                sTexto = sTexto.substring(sTexto.indexOf(
	                  sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
	                contador++; 
	                break;
	              }
			} catch (Exception e) {
				 
				logger.error("Error buscarCodificacionPagina: " + e.getMessage());
			}
			return contador;
	    }
	    //ini MCG20140530
		
		public ProgramaBlobBO getProgramaBlobBO() {
			return programaBlobBO;
		}

		public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
			this.programaBlobBO = programaBlobBO;
		}



		public DatosBasicosBO getDatosBasicosBO() {
			return datosBasicosBO;
		}



		public void setDatosBasicosBO(DatosBasicosBO datosBasicosBO) {
			this.datosBasicosBO = datosBasicosBO;
		}



		public ProgramaBO getProgramaBO() {
			return programaBO;
		}



		public void setProgramaBO(ProgramaBO programaBO) {
			this.programaBO = programaBO;
		}



		public Programa getPrograma() {
			return programa;
		}



		public void setPrograma(Programa programa) {
			this.programa = programa;
		}



		public Planilla getTotalPlanilla() {
			return totalPlanilla;
		}



		public void setTotalPlanilla(Planilla totalPlanilla) {
			this.totalPlanilla = totalPlanilla;
		}



		public Planilla getPlanillaAdmin() {
			return planillaAdmin;
		}



		public void setPlanillaAdmin(Planilla planillaAdmin) {
			this.planillaAdmin = planillaAdmin;
		}



		public Planilla getPlanillaNoAdmin() {
			return planillaNoAdmin;
		}



		public void setPlanillaNoAdmin(Planilla planillaNoAdmin) {
			this.planillaNoAdmin = planillaNoAdmin;
		}
		public List<Accionista> getListaAccionistas() {
			return listaAccionistas;
		}

		public void setListaAccionistas(List<Accionista> listaAccionistas) {
			this.listaAccionistas = listaAccionistas;
		}

		public String getTotalAccionista() {
			return totalAccionista;
		}

		public void setTotalAccionista(String totalAccionista) {
			this.totalAccionista = totalAccionista;
		}

		public List<PrincipalesEjecutivos> getListaPrinciEjecutivos() {
			return listaPrinciEjecutivos;
		}

		public void setListaPrinciEjecutivos(
				List<PrincipalesEjecutivos> listaPrinciEjecutivos) {
			this.listaPrinciEjecutivos = listaPrinciEjecutivos;
		}

		public List<Participaciones> getListaParticipaciones() {
			return listaParticipaciones;
		}

		public void setListaParticipaciones(List<Participaciones> listaParticipaciones) {
			this.listaParticipaciones = listaParticipaciones;
		}

		public List<RatingExterno> getListaRatingExterno() {
			return listaRatingExterno;
		}

		public void setListaRatingExterno(List<RatingExterno> listaRatingExterno) {
			this.listaRatingExterno = listaRatingExterno;
		}

		public List<CompraVenta> getListaCompraVenta() {
			return listaCompraVenta;
		}

		public void setListaCompraVenta(List<CompraVenta> listaCompraVenta) {
			this.listaCompraVenta = listaCompraVenta;
		}

		public CompraVenta getTotalImportaciones() {
			return totalImportaciones;
		}

		public void setTotalImportaciones(CompraVenta totalImportaciones) {
			this.totalImportaciones = totalImportaciones;
		}

		public CompraVenta getImportacionesME() {
			return importacionesME;
		}

		public void setImportacionesME(CompraVenta importacionesME) {
			this.importacionesME = importacionesME;
		}

		public CompraVenta getTotalExportaciones() {
			return totalExportaciones;
		}

		public void setTotalExportaciones(CompraVenta totalExportaciones) {
			this.totalExportaciones = totalExportaciones;
		}

		public CompraVenta getExportacionesME() {
			return exportacionesME;
		}

		public void setExportacionesME(CompraVenta exportacionesME) {
			this.exportacionesME = exportacionesME;
		}

		public List<NegocioBeneficio> getListaNBActividades() {
			return listaNBActividades;
		}

		public void setListaNBActividades(List<NegocioBeneficio> listaNBActividades) {
			this.listaNBActividades = listaNBActividades;
		}

		public NegocioBeneficio getTotalActividad() {
			return totalActividad;
		}

		public void setTotalActividad(NegocioBeneficio totalActividad) {
			this.totalActividad = totalActividad;
		}

		public NegocioBeneficio getTotalNegocio() {
			return totalNegocio;
		}

		public void setTotalNegocio(NegocioBeneficio totalNegocio) {
			this.totalNegocio = totalNegocio;
		}

		public List<NegocioBeneficio> getListaNBNegocio() {
			return listaNBNegocio;
		}

		public void setListaNBNegocio(List<NegocioBeneficio> listaNBNegocio) {
			this.listaNBNegocio = listaNBNegocio;
		}

		public RatingBO getRatingBO() {
			return ratingBO;
		}

		public void setRatingBO(RatingBO ratingBO) {
			this.ratingBO = ratingBO;
		}

		public List<Rating> getListaRating() {
			return listaRating;
		}

		public void setListaRating(List<Rating> listaRating) {
			this.listaRating = listaRating;
		}
		
		

		public RelacionesBancariasBO getRelacionesBancariasBO() {
			return relacionesBancariasBO;
		}

		public void setRelacionesBancariasBO(RelacionesBancariasBO relacionesBancariasBO) {
			this.relacionesBancariasBO = relacionesBancariasBO;
		}

		public List<LineasRelacionBancarias> getListLineasRelacionesBancarias() {
			return listLineasRelacionesBancarias;
		}

		public void setListLineasRelacionesBancarias(
				List<LineasRelacionBancarias> listLineasRelacionesBancarias) {
			this.listLineasRelacionesBancarias = listLineasRelacionesBancarias;
		}

		public PropuestaRiesgoBO getPropuestaRiesgoBO() {
			return propuestaRiesgoBO;
		}

		public void setPropuestaRiesgoBO(PropuestaRiesgoBO propuestaRiesgoBO) {
			this.propuestaRiesgoBO = propuestaRiesgoBO;
		}

		public List<EstructuraLimite> getListEstructuraLimite() {
			return listEstructuraLimite;
		}

		public void setListEstructuraLimite(List<EstructuraLimite> listEstructuraLimite) {
			this.listEstructuraLimite = listEstructuraLimite;
		}

		public PoliticasRiesgoBO getPoliticasRiesgoBO() {
			return politicasRiesgoBO;
		}

		public void setPoliticasRiesgoBO(PoliticasRiesgoBO politicasRiesgoBO) {
			this.politicasRiesgoBO = politicasRiesgoBO;
		}

		public List<LimiteFormalizado> getListLimiteFormalizado() {
			return listLimiteFormalizado;
		}

		public void setListLimiteFormalizado(
				List<LimiteFormalizado> listLimiteFormalizado) {
			this.listLimiteFormalizado = listLimiteFormalizado;
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

		public ParametroBO getParametroBO() {
			return parametroBO;
		}

		public void setParametroBO(ParametroBO parametroBO) {
			this.parametroBO = parametroBO;
		}

		public Long getIdPrograma() {
			return idPrograma;
		}

		public void setIdPrograma(Long idPrograma) {
			this.idPrograma = idPrograma;
		}
		public SintesisEconomicoBlobBO getSintesisEconomicoBlobBO() {
			return sintesisEconomicoBlobBO;
		}
		public void setSintesisEconomicoBlobBO(
				SintesisEconomicoBlobBO sintesisEconomicoBlobBO) {
			this.sintesisEconomicoBlobBO = sintesisEconomicoBlobBO;
		}
		public SintesisEconomicoBO getSintesisEconomicoBO() {
			return sintesisEconomicoBO;
		}
		public void setSintesisEconomicoBO(SintesisEconomicoBO sintesisEconomicoBO) {
			this.sintesisEconomicoBO = sintesisEconomicoBO;
		}
		public AnexoBO getAnexoBO() {
			return anexoBO;
		}
		public void setAnexoBO(AnexoBO anexoBO) {
			this.anexoBO = anexoBO;
		}
		public TablaBO getTablaBO() {
			return tablaBO;
		}
		public void setTablaBO(TablaBO tablaBO) {
			this.tablaBO = tablaBO;
		}

		public String getCodigoTipoOperacion() {
			return codigoTipoOperacion;
		}
		public void setCodigoTipoOperacion(String codigoTipoOperacion) {
			this.codigoTipoOperacion = codigoTipoOperacion;
		}


		
	}

