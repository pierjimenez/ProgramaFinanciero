package pe.com.bbva.iipf.pf.action;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.SelectItem;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnalisisSectorialBO;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.AnexoGarantiaBO;
import pe.com.bbva.iipf.pf.bo.ArchivoAnexoBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.PropuestaRiesgoBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RatingBlobBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.bo.ReporteCreditoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.ArchivoReporteCredito;
import pe.com.bbva.iipf.pf.model.CabTabla;
import pe.com.bbva.iipf.pf.model.CapitalizacionBursatil;
import pe.com.bbva.iipf.pf.model.ClaseCredito;
import pe.com.bbva.iipf.pf.model.CompraVenta;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSPe21;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.Garantia;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.NegocioBeneficio;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.Participaciones;
import pe.com.bbva.iipf.pf.model.Planilla;
import pe.com.bbva.iipf.pf.model.PrincipalesEjecutivos;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.RatingBlob;
import pe.com.bbva.iipf.pf.model.RatingExterno;
import pe.com.bbva.iipf.pf.model.ReporteCredito;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.pf.model.SustentoOperacion;
import pe.com.bbva.iipf.pf.model.Tbanco;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.util.ComboUtil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.grupobbva.pf.pec6.InputPec6;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("programaAction")
@Scope("prototype")
public class ProgramaAction extends GenericAction {

	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	@Resource
	private ProgramaBO programaBO ;
	
	@Resource
	private EmpresaBO empresaBO ;
	
	//ini MCG 20120430
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private DatosBasicosBO datosBasicosBO ;
	
	@Resource
	private SintesisEconomicoBO sintesisEconomicoBO;
	
	@Resource
	private SintesisEconomicoBlobBO  sintesisEconomicoBlobBO;
	
	@Resource
	private DatosBasicosBlobBO  datosBasicosBlobBO;
	
	@Resource
	private AnalisisSectorialBO analisisSectorialBO;
	
	@Resource
	private RelacionesBancariasBO relacionesBancariasBO;
	
	
	@Resource
	private PropuestaRiesgoBO propuestaRiesgoBO;
	
	@Resource
	private PoliticasRiesgoBO politicasRiesgoBO;
	
	@Resource
	private AnexoBO anexoBO;
	
	@Resource
	private ArchivoAnexoBO archivoAnexoBO;
	
	@Resource
	private ReporteCreditoBO reporteCreditoBO;	
	
	@Resource
	private RatingBO ratingBO;
	
	@Resource
	private RatingBlobBO  ratingBlobBO;
	
	@Resource
	private AnexoGarantiaBO anexoGarantiaBO;
	
	@Resource
	private TablaBO tablaBO;
	
	//fin MCG 20120430

	private Programa programa;
	
	private String anio;
	
	private InputPec6 entradaPec6;
	/**
	 * lista para almacenar los grupos de empresas
	 */
	private List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();
	
	//////////////////DATOS BASICOS////////////////
	private String pais;
	
	private List listaPlanilla = new ArrayList();
	private Planilla totalPlanilla = new Planilla();
	private Planilla planillaAdmin = new Planilla();
	
	private Planilla planillaNoAdmin = new Planilla();
	private Empresa empresaPrincipal = new Empresa();

	private String tipoPrograma;
	private String tipoEmpresa;
	private String codigoEmpresaGrupo;
	private String tipoDocBusqueda;
	
	private String tipoDocBusquedagrupo;
	private String tipoDocBusquedaTodos;
	
	private String tipoEmpresaHidden;
	private String tipoDocBusquedaHidden;
	private String copiarsinDataHidden;
	
	private Long idPrograma;
	private String tipoCopiapf;
	private String codEmpresaPrinHidden;

	
	private Date fechaInicio;
	private Date fechaFin;
	private String idEstadoPrograma;
	
	private List<String> codigoEmpesaSelet;
	
	private String tipoMetodo;
	
    //////////////////DATOS BASICOS otros////////////////
	List<Empresa> listaEmpresasGrupoDB = new ArrayList<Empresa>();
	
	//ini MCG20130219	
	private List<CabTabla> listaCabTablaPlanilla = new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaCompra = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaVenta = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaActividad = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaNegocio = new ArrayList<CabTabla>();	
	//fin MCG20130219	
	
	
	//COPIA PF
	private String selectedItemsEmpresa;
	private String selectedItemsPrime;
	private String selectedItemsEmpresaPrograma;
			
	
	public List<String> getCodigoEmpesaSelet() {
		return Arrays.asList("00020770", "21587534");

	}

	public void setCodigoEmpesaSelet(List<String> codigoEmpesaSelet) {
		this.codigoEmpesaSelet = codigoEmpesaSelet;
	}

	public Long getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Long idPrograma) {
		this.idPrograma = idPrograma;
	}

	public String getTipoEmpresaHidden() {
		return tipoEmpresaHidden;
	}

	public void setTipoEmpresaHidden(String tipoEmpresaHidden) {
		this.tipoEmpresaHidden = tipoEmpresaHidden;
	}

	public String getTipoDocBusquedaHidden() {
		return tipoDocBusquedaHidden;
	}

	public void setTipoDocBusquedaHidden(String tipoDocBusquedaHidden) {
		this.tipoDocBusquedaHidden = tipoDocBusquedaHidden;
	}

	private String filtroProgramas;
		
	private List<Programa> programas;
	
	public List<Programa> getProgramas() {
		return programas;
	}

	public void setProgramas(List<Programa> programas) {
		this.programas = programas;
	}
	
	public String getFiltroProgramas() {
		if(super.getObjectSession("filtroProgramas") != null)
			this.filtroProgramas = String.valueOf(super.getObjectSession("filtroProgramas"));
		return filtroProgramas;
	}

	public void setFiltroProgramas(String filtroProgramas) {
		this.filtroProgramas = filtroProgramas;
	}
	
	/**
	 * variable para pasar el nombre del campo blob a registrarse
	 */
	private String campoBlob;
	/**
	 * variable que contendra el valor de campo blob
	 */
	private String valorBlob;
	
	
	private String codigoMotivoCierre;
	private String observacionCierre;
	private String idprogramaCierre;
	private List<Tabla> listaMotivoCierre = new ArrayList<Tabla>();

	private String codigoTipoOperacion;
	private List<Tabla> listaTipoOperacion= new ArrayList<Tabla>();
	
	private String idprogramaEmpresaRC;
	private String codEmpresaRC;
	
	private String idprogramaCopia;
	private String tipoCopiaCopia;
	
	//INI MCG20150817
	private List<SelectItem> itemDivisaDB;
	private List<SelectItem> itemTipoDocumento;	
	//FIN MCG20150817
	

	/**
	 * Metodo para guardar un campo blog de programa financiero
	 */
	public void saveProgramaBlob(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		
		try {
			ProgramaBlob pb = new ProgramaBlob();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));
			programaBlobBO.setPrograma(programa);
			programaBlobBO.setCampoBlob(campoBlob);
			programaBlobBO.setValorBlob(valorBlob);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			programaBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				programaBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				programaBlobBO.setPatronesEditor("");
			}
			
			
			programaBlobBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			programaBlobBO.save(pb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Busca un campo blob dentro del programa
	 * la respuesta es procesada via ajax
	 */
	public void consultarProgramaBlob(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));
			ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa);
			if(programaBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	            if(campoBlob.equals("sintesisEmpresa") &&
	               programaBlob.getSintesisEmpresa()!=null){
		            for(byte x :programaBlob.getSintesisEmpresa() ){
		            	stb.append((char)FormatUtil.getCharUTF(x));
		            }
	            }
	            if(campoBlob.equals("datosMatriz") &&
	 	               programaBlob.getDatosMatriz()!=null){
	 		        for(byte x :programaBlob.getDatosMatriz() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("espacioLibre") &&
	 	               programaBlob.getEspacioLibre()!=null){
	 		        for(byte x :programaBlob.getEspacioLibre() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("espacioLibreAS") &&
	 	               programaBlob.getEspacioLibreAS()!=null){
	 		        for(byte x :programaBlob.getEspacioLibreAS() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("campoLibrePR") &&
	 	               programaBlob.getCampoLibrePR()!=null){
	 		        for(byte x :programaBlob.getCampoLibrePR()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("estructuraLimite") &&
	 	               programaBlob.getEstructuraLimite()!=null){
	 		        for(byte x :programaBlob.getEstructuraLimite()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("consideracionPR") &&
	 	               programaBlob.getConsideracionPR()!=null){
	 		        for(byte x :programaBlob.getConsideracionPR()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("detalleOperacionGarantia") &&
	 	               programaBlob.getDetalleOperacionGarantia()!=null){
	 		        for(byte x :programaBlob.getDetalleOperacionGarantia()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("riesgoTesoreria") &&
	 	               programaBlob.getRiesgoTesoreria()!=null){
	 		        for(byte x :programaBlob.getRiesgoTesoreria()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("politicasRiesGrupo") &&
	 	               programaBlob.getPoliticasRiesGrupo()!=null){
	 		        for(byte x :programaBlob.getPoliticasRiesGrupo()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("politicasDelegacion") &&
	 	               programaBlob.getPoliticasDelegacion()!=null){
	 		        for(byte x :programaBlob.getPoliticasDelegacion()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       //factores riesgo
	 	       if(campoBlob.equals("fodaFotalezas") &&
	 	               programaBlob.getFodaFotalezas()!=null){
	 		        for(byte x :programaBlob.getFodaFotalezas()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       if(campoBlob.equals("fodaOportunidades") &&
	 	               programaBlob.getFodaOportunidades()!=null){
	 		        for(byte x :programaBlob.getFodaOportunidades()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		          	System.out.println("Amenaza stb:"+stb.toString());
	 		        }
	 	        }
	 	       if(campoBlob.equals("fodaDebilidades") &&
	 	               programaBlob.getFodaDebilidades()!=null){
	 		        for(byte x :programaBlob.getFodaDebilidades()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		          	System.out.println("Amenaza stb:"+stb.toString());
	 		        }
	 	        }
	 	       if(campoBlob.equals("fodaAmenazas") &&
	 	               programaBlob.getFodaAmenazas()!=null){
	 		        for(byte x :programaBlob.getFodaAmenazas()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		          	System.out.println("Amenaza stb:"+stb.toString());
	 		        }
	 	        }
	 	       
	 	       if(campoBlob.equals("conclucionFoda") &&
	 	               programaBlob.getConclucionFoda()!=null){
	 		        for(byte x :programaBlob.getConclucionFoda()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	       //Relaciones bancarias
	 	       if(campoBlob.equals("comenLineas") &&
	 	               programaBlob.getComenLineas()!=null){
	 		        for(byte x :programaBlob.getComenLineas()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	        if(campoBlob.equals("rentaModelGlobal") &&
		 	               programaBlob.getRentaModelGlobal()!=null){
		 		        for(byte x :programaBlob.getRentaModelGlobal()){
		 		          	stb.append((char)FormatUtil.getCharUTF(x));
		 		        }
		 	        }
		 	    if(campoBlob.equals("rentaModelBEC") &&
		 	       programaBlob.getRentaModelBEC()!=null){
		 		     for(byte x :programaBlob.getRentaModelBEC()){
		 		          	stb.append((char)FormatUtil.getCharUTF(x));
		 		     }
		 	    }
		 	    //ini MCG20140815
		 	    if(campoBlob.equals("comenIndiceTransa") &&
				 	       programaBlob.getComentIndTransaccional()!=null){
				 		     for(byte x :programaBlob.getComentIndTransaccional()){
				 		          	stb.append((char)FormatUtil.getCharUTF(x));
				 		     }
				}
		 	  //fin MCG20140815
		 	    
			  //Sintesis economico	 	        
	 	       
	 	      if(campoBlob.equals("comenSituFinanciera") &&
	 	               programaBlob.getComenSituFinanciera()!=null){
	 		        for(byte x :programaBlob.getComenSituFinanciera() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("comenSituEconomica") &&
	 	               programaBlob.getComenSituEconomica()!=null){
	 		        for(byte x :programaBlob.getComenSituEconomica() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("valoracionEconFinanciera") &&
	 	               programaBlob.getValoracionEconFinanciera()!=null){
	 		        for(byte x :programaBlob.getValoracionEconFinanciera() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("valoracionPosiBalance") &&
	 	               programaBlob.getValoracionPosiBalance()!=null){
	 		        for(byte x :programaBlob.getValoracionPosiBalance() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("valoracionRating") &&
	 	               programaBlob.getValoracionRating()!=null){
	 		        for(byte x :programaBlob.getValoracionRating()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	      }
	 	     if(campoBlob.equals("comenComprasVentas") &&
	 	               programaBlob.getComenComprasVentas()!=null){
	 		        for(byte x :programaBlob.getComenComprasVentas()){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	      }
		 	 if(campoBlob.equals("comenPoolBanc") &&
		 	            programaBlob.getComenPoolBanc()!=null){
		 	       for(byte x :programaBlob.getComenPoolBanc()){
		 	         	stb.append((char)FormatUtil.getCharUTF(x));
		 	       }
		 	  }
		 	 if(campoBlob.equals("campoLibreAnexos") &&
		 	            programaBlob.getCampoLibreAnexos()!=null){
		 	       for(byte x :programaBlob.getCampoLibreAnexos()){
		 	         	stb.append((char)FormatUtil.getCharUTF(x));
		 	       }
		 	  }
		 	 //add
	            if(campoBlob.equals("concentracion") &&
	 	               programaBlob.getConcentracion()!=null){
	 		            for(byte x :programaBlob.getConcentracion() ){
	 		            	stb.append((char)FormatUtil.getCharUTF(x));
	 		            }
	 	            }
	            //add MCG20121005
	            if(campoBlob.equals("datosBasicosAdicional") ){
	            	if( programaBlob.getDatosBasicosaddRDC()!=null){
	            		for(byte x :programaBlob.getDatosBasicosaddRDC() ){
	            			stb.append((char)FormatUtil.getCharUTF(x));
		 		        }
		 	        }
	            }
			 	 //add
	            if(campoBlob.equals("valoracion") &&
	 	               programaBlob.getValoracion()!=null){
	 		            for(byte x :programaBlob.getValoracion() ){
	 		            	stb.append((char)FormatUtil.getCharUTF(x));
	 		            }
	 	            }

	            if(campoBlob.equals("datosBasicosAdicionalSincronizar") ){
	            	if(programaBlob.getSintesisEmpresa()!=null){
		 	        	for(byte x :programaBlob.getSintesisEmpresa() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(programaBlob.getDatosMatriz()!=null){
		 	        	stb.append("<br>");
		 	        	for(byte x :programaBlob.getDatosMatriz() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(stb!=null && stb.length()>0){
	 	        		valorBlob = stb.toString();
	 	        	}
	            }
	            if(campoBlob.equals("sintesisEconFinAdicional") ){
	            	if( programaBlob.getComenSintesisEconFinaddRDC()!=null){
	            		for(byte x :programaBlob.getComenSintesisEconFinaddRDC() ){
	 		            	stb.append((char)FormatUtil.getCharUTF(x));
	 		            }
	            	}
 	            }
	            
	            if(campoBlob.equals("sintesisEconFinAdicionalSincronizar") ){
	            	if(programaBlob.getComenSituFinanciera()!=null){
            			stb.append("<b><u><font size='2'>Comentarios Situaci&oacute;n Financiera:</font></u></b><br>");
		 	        	for(byte x :programaBlob.getComenSituFinanciera() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
            		if(programaBlob.getComenSituEconomica()!=null){
            			stb.append("<br>");
            			stb.append("<b><u><font size='2'>Comentarios Situaci&oacute;n Econ&oacute;mica:</font></u></b><br>");
		 	        	for(byte x :programaBlob.getComenSituEconomica() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
            		if(programaBlob.getValoracionEconFinanciera()!=null){
            			stb.append("<br>");
            			stb.append("<b><u><font size='2'>Valoraci&oacute;n Situaci&oacute;n Econ&oacute;mica - Financiera:</font></u></b><br>");
		 	        	for(byte x :programaBlob.getValoracionEconFinanciera() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(stb!=null && stb.length()>0){
	 	        		valorBlob = stb.toString();
	 	        		//saveProgramaBlob();//Guardando
	 	        	}
 		            
 	            }
		 	 
	 	      logger.info("html blob = "+stb);
	            out.print(stb.toString());
			}
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}

	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}

	
	public String welcome()
	{
		return "welcome";		
	}

	public String programa()
	{
		return "programa";		
	}
	
	public String grupo()
	{
		setTipoDocBusqueda("2");
		setCodigoEmpresaGrupo("");
		return "grupo";		
	}		
	
	public String editgrupo()
	{
		List<Empresa> listaEmpresasini=null;
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		try{			
			programa = programaBO.findById(this.getIdPrograma());
			removeObjectSession("listaEmpresasGrupo");
			String idTipoCopiapf=this.getTipoCopiapf();
			//System.out.println("idTipoCopiapf: "+idTipoCopiapf);
			//System.out.println("idPrograma: "+this.getIdPrograma());			
			
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();			
			//si es empresa o grupo
			sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
			
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			//ini Configuracion
			String codigoUsuario=((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			
			setTipoEmpresa(programa.getTipoEmpresa().getId().toString());
			setTipoPrograma(programa.getTipoPrograma().getId().toString());
			setTipoDocBusqueda("3");
						
			
			listaEmpresasini=new ArrayList<Empresa>();
			listaEmpresasini = empresaBO.listarEmpresasPorPrograma(programa.getId());
			
			if(programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){	
				//codigoEmpresaGrupo
				setCodigoEmpresaGrupo(programa.getIdEmpresa());
				setCodEmpresaPrinHidden(programa.getIdEmpresa());
				
			}else{	
				//codigoEmpresaGrupo
				setCodigoEmpresaGrupo(programa.getIdGrupo());
				
				if (listaEmpresasini!=null && listaEmpresasini.size()>0){
					for (Empresa oempresaini:listaEmpresasini){
						if (oempresaini.getTipoGrupo()==null || oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
							empresaPrincipal.setCodigo(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
							empresaPrincipal.setNombre(oempresaini.getNombre()==null?"":oempresaini.getNombre().toString());
							setCodEmpresaPrinHidden(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
						}
						
					}				
				}				
			}	
			
			
			
			//ini
			HashMap<String,String> datosPersona = null;
			
				if(getTipoEmpresa()!= null && getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					
					if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_IaI)){
							datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
																		VAL_TIPO_DOC_COD_CENTRAL,
																	((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
																	oConfiguracionWSPe21);
						if(datosPersona !=null){
							getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));							
							getPrograma().setRuc(datosPersona.get("ruc"));							
							getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
							
							List<Empresa> olistaGrupoEmpresas= new ArrayList<Empresa>();
							Empresa oempresa = new Empresa();
							oempresa.setCodigo(datosPersona.get("codigoCentral"));
							oempresa.setRuc(datosPersona.get("ruc"));
							oempresa.setNombre(datosPersona.get("nombreEmpesa"));
							olistaGrupoEmpresas.add(oempresa);
							setObjectSession("listaEmpresasGrupo", olistaGrupoEmpresas);
							
						}else{
							addActionError("No se encontro una empresa con los datos ingresados.");
							getPrograma().setNombreGrupoEmpresa("");
						}
					}else if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_IaG)){
						// TIPO DE COPIA: GRUPO	
							sessionparam.remove(Constantes.COD_TIPO_EMPRESA_SESSION);
							sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
							setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
							
							HashMap<String,String> resulGrupo = new HashMap<String,String>();
							String codGrupo="";
							entradaPec6 = new InputPec6();
							entradaPec6.setFlagControl("");
							entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
							entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
							entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
							entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
							
							resulGrupo = QueryWS.obtenerGrupoEconomicoWS(entradaPec6);
							
							if (((String)resulGrupo.get("codError")).equals(Constantes.COD_OK_WS)){
								codGrupo= resulGrupo.get("codGrupo").toString();//"G0003333";
								
								empresaPrincipal.setCodigo(getCodigoEmpresaGrupo());							
								setCodigoEmpresaGrupo(codGrupo);

								HashMap<String,Object> resultado = new HashMap<String,Object>();
								
								entradaPec6 = new InputPec6();
								entradaPec6.setFlagControl("");
								entradaPec6.setNumeroCliente(codGrupo);
								entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
								entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
								entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
								
								resultado = QueryWS.consultarGrupoEconomico(entradaPec6,oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);				
								
								if(resultado== null){
									addActionError("No se encontro un grupo con el Código ingresado.");
								}else{
									listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");
									setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
									if(!listaGrupoEmpresas.isEmpty()){
										getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
										
									}else{
										addActionError("No se encontrón empresas para el grupo economico");
										getPrograma().setNombreGrupoEmpresa("");
									}
								}							
							}else{
								String msnError=(String)resulGrupo.get("msnError");
								addActionError(msnError);
								getPrograma().setNombreGrupoEmpresa("");
								setCodigoEmpresaGrupo("");
							}
							
						}
					
				}else if(getTipoEmpresa()!= null && getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
											
						HashMap<String,Object> resultado = new HashMap<String,Object>();						
						entradaPec6 = new InputPec6();
						entradaPec6.setFlagControl("");
						entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
						entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
						entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
						entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
						
						if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_GaI)){
							// TIPO DE COPIA: GRUPO A EMPRESA	
								sessionparam.remove(Constantes.COD_TIPO_EMPRESA_SESSION);
								sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, Constantes.ID_TIPO_EMPRESA_EMPR.toString());
								setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_EMPR.toString());								
						}
						
						resultado = QueryWS.consultarGrupoEconomico(entradaPec6,oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
						if(resultado== null){
							addActionError("No se encontro un grupo con el Código ingresado.");
						}else{
							if(resultado.get("listaEmpresas")!=null){
								listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");	
							}
							
							setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
							
							if(!listaGrupoEmpresas.isEmpty()){
								getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
								
								if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_GaI)){
									// TIPO DE COPIA: GRUPO A EMPRESA	
										setCodigoEmpresaGrupo(empresaPrincipal.getCodigo());
										getPrograma().setNombreGrupoEmpresa(empresaPrincipal.getNombre());			
										
								}
									
								if (listaEmpresasini!=null && listaEmpresasini.size()>0){
									for (Empresa oempresaini:listaEmpresasini){
										for (Empresa oempresafin:listaGrupoEmpresas){
											if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
												if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
													oempresafin.setSeleccionadoSecu("selected");
												}
											
											}else if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
												if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
													oempresafin.setSeleccionadoAnex("selected");
												}
											}
										}
										
									}				
								}
								
							}else{
								addActionError("No se encontrón empresas para el grupo economico");
								getPrograma().setNombreGrupoEmpresa("");
							}
						}											
				}				
			
			//fin
			
		}catch (WSException e) {
			setCodigoEmpresaGrupo("");			
			getPrograma().setNombreGrupoEmpresa("");
			
			e.printStackTrace();
			logger.info(e);
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception ex){	
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		return "editgrupo";	
	}
	
	//ini copia original de editgrupo no se usa
	public String editgrupo_ORIGINAL()
	{
		List<Empresa> listaEmpresasini=null;
		try{			
			programa = programaBO.findById(this.getIdPrograma());
			removeObjectSession("listaEmpresasGrupo");
			String idTipoCopiapf=this.getTipoCopiapf();
			//System.out.println("idTipoCopiapf: "+idTipoCopiapf);
			//System.out.println("idPrograma: "+this.getIdPrograma());			
			
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();			
			//si es empresa o grupo
			sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
			
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			
			setTipoEmpresa(programa.getTipoEmpresa().getId().toString());
			setTipoPrograma(programa.getTipoPrograma().getId().toString());
			setTipoEmpresa(programa.getTipoEmpresa().getId().toString());
			setTipoDocBusqueda("3");
			
			if(programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){				
				setCodigoEmpresaGrupo(programa.getIdEmpresa());	
				setCodEmpresaPrinHidden(programa.getIdEmpresa());
			}else{
				listaEmpresasini=new ArrayList<Empresa>();
				listaEmpresasini = empresaBO.listarEmpresasPorPrograma(programa.getId());
								
				setCodigoEmpresaGrupo(programa.getIdGrupo());
				if (listaEmpresasini!=null && listaEmpresasini.size()>0){
					for (Empresa oempresaini:listaEmpresasini){
						if (oempresaini.getTipoGrupo()==null || oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
							empresaPrincipal.setCodigo(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
							setCodEmpresaPrinHidden(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
						}
						
					}				
				}			
				
			}	
			
			
			
			//ini
			HashMap<String,String> datosPersona = null;
			
				if(getTipoEmpresa()!= null &&
				   getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					
					if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_IaI)){
						//S = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
						datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
																	tipoDocBusqueda,
																	((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
																	null);
						if(datosPersona !=null){
							getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));
							if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_RUC)){
								getPrograma().setRuc(datosPersona.get("ruc"));
							}else{
								getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
							}
						}else{
							addActionError("No se encontro una empresa con los datos ingresados.");
							getPrograma().setNombreGrupoEmpresa("");
						}
					}else if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_IaG)){
						// TIPO DE COPIA: GRUPO	
							sessionparam.remove(Constantes.COD_TIPO_EMPRESA_SESSION);
							sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
							setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
							
							HashMap<String,String> resulGrupo = new HashMap<String,String>();
							String codGrupo="";
							entradaPec6 = new InputPec6();
							entradaPec6.setFlagControl("");
							entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
							entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
							entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
							entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
							
							resulGrupo = QueryWS.obtenerGrupoEconomicoWS(entradaPec6);
							
							if (((String)resulGrupo.get("codError")).equals(Constantes.COD_OK_WS)){
								codGrupo= resulGrupo.get("codGrupo").toString();//"G0003333";
								
								empresaPrincipal.setCodigo(getCodigoEmpresaGrupo());							
								setCodigoEmpresaGrupo(codGrupo);

								HashMap<String,Object> resultado = new HashMap<String,Object>();
								
								entradaPec6 = new InputPec6();
								entradaPec6.setFlagControl("");
								entradaPec6.setNumeroCliente(codGrupo);
								entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
								entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
								entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
								
								resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
																			null,null);
								
								
								
								if(resultado== null){
									addActionError("No se encontro un grupo con el Código ingresado.");
								}else{
									listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");
									setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
									if(!listaGrupoEmpresas.isEmpty()){
										getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
										
									}else{
										addActionError("No se encontrón empresas para el grupo economico");
										getPrograma().setNombreGrupoEmpresa("");
									}
								}							
							}else{
								String msnError=(String)resulGrupo.get("msnError");
								addActionError(msnError);
								getPrograma().setNombreGrupoEmpresa("");
								setCodigoEmpresaGrupo("");
							}
							
						}
					
				}else if(getTipoEmpresa()!= null && getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
											
						HashMap<String,Object> resultado = new HashMap<String,Object>();						
						entradaPec6 = new InputPec6();
						entradaPec6.setFlagControl("");
						entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
						entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
						entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
						entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
						
						resultado = QueryWS.consultarGrupoEconomico(entradaPec6,null,null);
						if(resultado== null){
							addActionError("No se encontro un grupo con el Código ingresado.");
						}else{
							if(resultado.get("listaEmpresas")!=null){
								listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");	
							}
							
							setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
							
							if(!listaGrupoEmpresas.isEmpty()){
								getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
									
								if (listaEmpresasini!=null && listaEmpresasini.size()>0){
									for (Empresa oempresaini:listaEmpresasini){
										for (Empresa oempresafin:listaGrupoEmpresas){
											if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
												if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
													oempresafin.setSeleccionadoSecu("selected");
												}
											
											}else if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
												if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
													oempresafin.setSeleccionadoAnex("selected");
												}
											}
										}
										
									}				
								}
								
							}else{
								addActionError("No se encontrón empresas para el grupo economico");
								getPrograma().setNombreGrupoEmpresa("");
							}
						}											
				}				
			
			//fin
			
		}catch (WSException e) {
			setCodigoEmpresaGrupo("");			
			getPrograma().setNombreGrupoEmpresa("");
			
			e.printStackTrace();
			logger.info(e);
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception ex){	
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		return "editgrupo";	
	}
	
	//fin copia original de editgrupo
	
	
	//INI MCG20121120	
	public String editgrupoAbierto()
	{
		List<Empresa> listaEmpresasini=null;
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		try{			
			
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			setPrograma(programaBO.findById(Long.valueOf(programaId)));
			//ini Configuracion
			String codigoUsuario=((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();			
			//si es empresa o grupo
			sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
			
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			
			setTipoEmpresa(programa.getTipoEmpresa().getId().toString());
			setTipoPrograma(programa.getTipoPrograma().getId().toString());
			setTipoEmpresa(programa.getTipoEmpresa().getId().toString());
			setTipoDocBusqueda("3");
			
			if(programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){				
				setCodigoEmpresaGrupo(programa.getIdEmpresa());					
			}else{
				listaEmpresasini=new ArrayList<Empresa>();
				listaEmpresasini = empresaBO.listarEmpresasPorPrograma(programa.getId());
								
				setCodigoEmpresaGrupo(programa.getIdGrupo());
				if (listaEmpresasini!=null && listaEmpresasini.size()>0){
					for (Empresa oempresaini:listaEmpresasini){
						if (oempresaini.getTipoGrupo()==null ||oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
							empresaPrincipal.setCodigo(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
						}
						
					}				
				}			
				
			}	
			
			
			
			//ini
			HashMap<String,String> datosPersona = null;
			
				if(getTipoEmpresa()!= null &&
				   getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					//S = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
					String strTipoDocumentoDB="";
					if (tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
						strTipoDocumentoDB=VAL_TIPO_DOC_COD_CENTRAL;
					}else{
						strTipoDocumentoDB=VAL_TIPO_DOC_RUC;
					}
					
					datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
																strTipoDocumentoDB,
																((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
																oConfiguracionWSPe21);
					if(datosPersona !=null){
						getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));
						if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_RUC)){
							getPrograma().setRuc(datosPersona.get("ruc"));
						}else{
							getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
						}
					}else{
						addActionError("No se encontro una empresa con los datos ingresados.");
						getPrograma().setNombreGrupoEmpresa("");
					}
				}else if(getTipoEmpresa()!= null &&
						 getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					
					HashMap<String,Object> resultado = new HashMap<String,Object>();
					
					entradaPec6 = new InputPec6();
					entradaPec6.setFlagControl("");
					entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
					entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
					entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
					entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
					
					resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
																oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
					if(resultado== null){
						addActionError("No se encontro un grupo con el Código ingresado.");
					}else{
						
						if(resultado.get("listaEmpresas")!=null){
							listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");	
						}
						
						setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
						if(!listaGrupoEmpresas.isEmpty()){
							getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
								
							if (listaEmpresasini!=null && listaEmpresasini.size()>0){
								for (Empresa oempresaini:listaEmpresasini){
									for (Empresa oempresafin:listaGrupoEmpresas){
										if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoSecu("selected");
											}
										
										}else if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoAnex("selected");
											}
										}
									}
									
								}				
							}
							
						}else{
							addActionError("No se encontrón empresas para el grupo economico");
							getPrograma().setNombreGrupoEmpresa("");
						}
					}
				}				
			
			//fin
			
		}catch (WSException e) {
			setCodigoEmpresaGrupo("");			
			getPrograma().setNombreGrupoEmpresa("");
			
			e.printStackTrace();
			logger.info(e);
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception ex){	
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		return "editgrupoAbierto";	
	}
	
	
	//FIN MCG20121120
	
	
	
	public String consultasModificaciones()
	{
		setIdEstadoPrograma(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE+"");
		return "consultasModificaciones";
	}
	
	/**
	 * Registra informacion prelimnar del programa
	 * y sube a session los datos necesarios para el programa Financiero
	 * @return
	 */
	public String save(){
		String forward="datosBasicos";
		List<Empresa> listaEmpresas = null;
		try{
			//programa.setAnio(Integer.valueOf(anio));
//			listaBanco=new ArrayList<Tbanco>();
//			listaBancoSelect=new ArrayList<Tbanco>();
			Tabla objTipoPrograma = new Tabla();
			objTipoPrograma.setId(Long.valueOf(getTipoPrograma()));
			Tabla objTipoEmpresa = new Tabla();
			objTipoEmpresa.setId(Long.valueOf(getTipoEmpresa()));
			programa.setTipoEmpresa(objTipoEmpresa);
			programa.setTipoPrograma(objTipoPrograma);
			//MCG 20140904
			programaBO.setListaGrupoEmpresas(new ArrayList<Empresa>());
			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				//'2':'RUC','3':'Código Central'
				if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
					programa.setIdEmpresa(getCodigoEmpresaGrupo());
				}else{
					programa.setRuc(getCodigoEmpresaGrupo());
				}
				
			}else{
				listaEmpresas = new ArrayList<Empresa>();
				programa.setIdGrupo(getCodigoEmpresaGrupo().toUpperCase());
				Tabla tipoGrupo = null;// EMPRESA PRINCIPAL, SECUNDARIA O ANEXA
				List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
				if(empresaPrincipal!=null){
					empresaPrincipal = findEmpresaGrupo(listaTemp, empresaPrincipal.getCodigo());
					tipoGrupo = new Tabla();
					tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
					empresaPrincipal.setTipoGrupo(tipoGrupo);
					listaEmpresas.add(empresaPrincipal);
					//se registra el ruc de la empresa principal
					programa.setRuc(empresaPrincipal.getRuc());
				}
				if(getRequest().getParameterValues("selListaEmpresasSecundarias")!= null){
					for(String ids: getRequest().getParameterValues("selListaEmpresasSecundarias")){
						Empresa empresa =null;
						empresa = findEmpresaGrupo(listaTemp, ids);
						tipoGrupo = new Tabla();
						tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
						empresa.setTipoGrupo(tipoGrupo);
						listaEmpresas.add(empresa);
					}
				}
				//MCG 20150129 RETIDADO YA NO HA ANEXO
//				if(getRequest().getParameterValues("selListaEmpresasAnexas") != null){
//					for(String ids: getRequest().getParameterValues("selListaEmpresasAnexas")){
//						Empresa empresa =null;
//						empresa = findEmpresaGrupo(listaTemp, ids);
//						tipoGrupo = new Tabla();
//						tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_ANEXA);
//						empresa.setTipoGrupo(tipoGrupo);
//						listaEmpresas.add(empresa);
//					}
//				}
				programaBO.setListaGrupoEmpresas(listaEmpresas);
			}
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			programaBO.setPathWebService(pathWebService);
			programaBO.setConfiguracionWS(getConfiguracionWS());
			programaBO.save(programa);
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();
			sessionparam.put(Constantes.ID_PROGRAMA_SESSION, programa.getId().toString());
						
			//ini MCG20121030
			sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, programa.getNumeroSolicitud()==null?"":programa.getNumeroSolicitud().toString());
			//fin MCG20121030
			
			//si es Local o coorporativo
			sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, programa.getTipoPrograma().getId());
			//si es empresa o grupo
			sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
			//en el caso de grupos se sube a session los datos de la empresa 
			//principal en los datos de la empresa 
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, programa.getRuc());
				sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, programa.getIdEmpresa());
				sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, programa.getIdEmpresa());
				//sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
			}else{
				sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaEmpresas);
				sessionparam.put(Constantes.COD_GRUPO_SESSION, programa.getIdGrupo());
				sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
				sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
				sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
				sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
			}
			sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, programa.getNombreGrupoEmpresa());
			sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, programa.getAnio());
			loadDatosBasicos();
			sessionparam.put("LISTADIVISADB", itemDivisaDB);
			sessionparam.put("LISTATIPODOCUMENTO", itemTipoDocumento);
			//ini MCG20130219
			completarListaGrupoEmpresas();
			
			LoadCabeceraPlanillaini(programa.getId());		
			LoadCabeceraCompraini(programa.getId());	
			LoadCabeceraVentaini(programa.getId());
			LoadCabeceraCuadraNBActividadini(programa.getId());
			LoadCabeceraCuadraNBNegocioini(programa.getId());
			
			//fin MCG20130219
			
			//quitar elementos de session que ya no seran necesarios
			getRequest().getSession().removeAttribute("listaEmpresasGrupo");
		} catch (BOException e) {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "grupo";
			e.printStackTrace();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "grupo";
			e.printStackTrace();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return forward;
	}
	
	
	//ini MCG20130219
	public void LoadCabeceraPlanillaini(Long idprograma){			
		
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
								
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<4;i++) {
						intposicion=intposicion+1;
					    String stranio="12/" + String.valueOf(anio-(3-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_PLANILLA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaPlanilla=listaCabTablaini;				
			
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void LoadCabeceraCompraini(Long idprograma){			
				
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_COMPRA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaCompra=listaCabTablaini;				
				
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}	
	
	public void LoadCabeceraVentaini(Long idprograma){			
				
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
							
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_VENTA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaVenta=listaCabTablaini;				
			
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}
	
	
	public void LoadCabeceraCuadraNBActividadini(Long idprograma){			
				
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}	
		
				Collections.sort(listaCabTablaini);
				listaCabTablaActividad=listaCabTablaini;				
				
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void LoadCabeceraCuadraNBNegocioini(Long idprograma){			
				
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaNegocio=listaCabTablaini;				
				
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}	
	
	//fin MCG20130219
	
	
	/**
	 * Busca una empresa dentro de la lista de empresas que conforman el grupo.
	 * @param lista
	 * @param codigo
	 * @return
	 */
	private Empresa findEmpresaGrupo(List<Empresa> lista, String codigo){
		Empresa emp = new Empresa();
		for(Empresa empresa : lista){
			if(empresa.getCodigo().equals(codigo)){
				emp.setCodigo(empresa.getCodigo());
				emp.setNombre(empresa.getNombre());
				emp.setRuc(empresa.getRuc());
				break;
			}
		}
		return emp;
	}
	
	/**
	 * Este método retorna informacion de las empresas secundarias y anexas
	 * @return
	 */
	public void listarOtrasEmpresas(){
		try {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
			String secundarias = "<h4>Empresas Secundarias:</h4> ";
			String anexas = "<h4>Empresas Anexas:</h4> ";
			for(Empresa empresa : listaGrupoEmpresas ){
				if(empresa.getTipoGrupo()!=null && empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
					secundarias = secundarias + empresa.getNombre()+"<br/>";
				}
//				else if(empresa.getTipoGrupo()!=null && empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
//					anexas = anexas + empresa.getNombre()+"<br/>";
//				}
			}
			getResponse().setContentType("text/html");   
	        PrintWriter out = getResponse().getWriter();
			//out.print(secundarias+anexas);
			out.print(secundarias);
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} 
	}
	
	/**
	 * Busca los datos de la empresa o grupo
	 * @return
	 */
	public String buscarGrupoEmpresa(){
		//Pegt001 pgt = null;
		HashMap<String,String> datosPersona = null;
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		try {
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			//ini Configuracion
			String sUsuario=((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(sUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			if(getTipoEmpresa()!= null &&
			   getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				//S = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
				String strTipoDocumentoDB="";
				if (tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
					strTipoDocumentoDB=VAL_TIPO_DOC_COD_CENTRAL;
				}else{
					strTipoDocumentoDB=VAL_TIPO_DOC_RUC;
				}
				datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
															strTipoDocumentoDB,
															((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
															oConfiguracionWSPe21);
				if(datosPersona !=null){
					getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));
					if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_RUC)){
						getPrograma().setRuc(datosPersona.get("ruc"));
					}else{
						getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
					}
				}else{
					addActionError("No se encontro una empresa con los datos ingresados.");
					getPrograma().setNombreGrupoEmpresa("");
				}
			}else if(getTipoEmpresa()!= null &&
					 getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				//pgt = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
				HashMap<String,Object> resultado = new HashMap<String,Object>();
				
				entradaPec6 = new InputPec6();
				entradaPec6.setFlagControl("");
				entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
				entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
				entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
				entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
				
				resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
															oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
				if(resultado== null){
					addActionError("No se encontro un grupo con el Código ingresado.");
				}else{
					if(resultado.get("listaEmpresas")!=null){
						listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");
						logger.info("cantidad de empresas="+listaGrupoEmpresas.size());
					}
										
					setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
					if(!listaGrupoEmpresas.isEmpty()){
						getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
					}else{
						addActionError("No se encontrón empresas para el grupo economico");
						getPrograma().setNombreGrupoEmpresa("");
					}
				}
			}
			
		} catch ( WSException e) {
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch ( Exception e) {
				setCodigoEmpresaGrupo("");
				getPrograma().setNombreGrupoEmpresa("");
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
			}
		return "grupo";
	}
	
	public String buscarGrupoEmpresaEdit(){
		//Pegt001 pgt = null;
		HashMap<String,String> datosPersona = null;
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		String forward="";
		try {
			logger.info(getTipoPrograma());
			logger.info(getTipoEmpresa());
			logger.info(getTipoMetodo());
			forward="editgrupo";	
			
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			//ini Configuracion
			String codigoUsuario=((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			if(getTipoEmpresa()!= null &&
			   getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				//S = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
				String strTipoDocumentoDB="";
				if (tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
					strTipoDocumentoDB=VAL_TIPO_DOC_COD_CENTRAL;
				}else{
					strTipoDocumentoDB=VAL_TIPO_DOC_RUC;
				}
				datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
															strTipoDocumentoDB,
															((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
															oConfiguracionWSPe21);
				if(datosPersona !=null){
					getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));
					if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_RUC)){
						getPrograma().setRuc(datosPersona.get("ruc"));
					}else{
						getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
					}
				}else{
					addActionError("No se encontro una empresa con los datos ingresados.");
					getPrograma().setNombreGrupoEmpresa("");
				}
			}else if(getTipoEmpresa()!= null &&
					 getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				//pgt = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
				HashMap<String,Object> resultado = new HashMap<String,Object>();
				
				entradaPec6 = new InputPec6();
				entradaPec6.setFlagControl("");
				entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
				entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
				entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
				entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
				
				resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
															oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
				if(resultado== null){
					addActionError("No se encontro un grupo con el Código ingresado.");
				}else{
					
					if(resultado.get("listaEmpresas")!=null){
						listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");
					}
					
					setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
					if(!listaGrupoEmpresas.isEmpty()){
										
						getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
						try {
							List<Empresa> listaEmpresasini=new ArrayList<Empresa>();
							listaEmpresasini = empresaBO.listarEmpresasPorPrograma(programa.getId());
								
							if (listaEmpresasini!=null && listaEmpresasini.size()>0){
								for (Empresa oempresaini:listaEmpresasini){
									if (oempresaini.getTipoGrupo()==null || oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
										empresaPrincipal.setCodigo(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
									}									
									
									for (Empresa oempresafin:listaGrupoEmpresas){	
										
										if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoSecu("selected");
											}
										
										}
										/*
										else if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoAnex("selected");
											}
										}
										*/
									}
									
								}				
							}
							
						}catch(BOException ex){								
							logger.error(StringUtil.getStackTrace(ex));
						}catch(Exception ex){								
							logger.error(StringUtil.getStackTrace(ex));
						}
						
	
						
					}else{
						addActionError("No se encontrón empresas para el grupo economico");
						getPrograma().setNombreGrupoEmpresa("");
					}
				}
			}
			
		} catch ( WSException e) {
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch ( Exception e) {
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return forward;
	}
	
	//ini MCG20121128
	
	public String buscarGrupoEmpresaEditAbierta(){
		//Pegt001 pgt = null;
		HashMap<String,String> datosPersona = null;

		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		
		String forward="";
		try {
			logger.info(getTipoPrograma());
			logger.info(getTipoEmpresa());
			logger.info(getTipoMetodo());		
			forward="editgrupoAbierto";
			
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			//ini Configuracion
			String codigoUsuario=((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			if(getTipoEmpresa()!= null &&
			   getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				//S = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());

				String strTipoDocumentoDB="";
				if (tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
					strTipoDocumentoDB=VAL_TIPO_DOC_COD_CENTRAL;
				}else{
					strTipoDocumentoDB=VAL_TIPO_DOC_RUC;
				}
				datosPersona = QueryWS.consularDatosBasicos(getCodigoEmpresaGrupo(),
															strTipoDocumentoDB,
															((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost(),
															oConfiguracionWSPe21);
				if(datosPersona !=null){
					getPrograma().setNombreGrupoEmpresa(datosPersona.get("nombreEmpesa"));
					if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_RUC)){
						getPrograma().setRuc(datosPersona.get("ruc"));
					}else{
						getPrograma().setIdEmpresa(datosPersona.get("codigoCentral"));
					}
				}else{
					addActionError("No se encontro una empresa con los datos ingresados.");
					getPrograma().setNombreGrupoEmpresa("");
				}
			}else if(getTipoEmpresa()!= null &&
					 getTipoEmpresa().equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				//pgt = 	pegt001BO.findByCodigo(getCodigoEmpresaGrupo());
				HashMap<String,Object> resultado = new HashMap<String,Object>();
				
				entradaPec6 = new InputPec6();
				entradaPec6.setFlagControl("");
				entradaPec6.setNumeroCliente(getCodigoEmpresaGrupo());
				entradaPec6.setPathWebServicePEC6(pathWebServicePEC6);
				entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
				entradaPec6.setUsuario(((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION)).getRegistroHost());
				
				resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
															oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
				
				if(resultado== null){
					addActionError("No se encontro un grupo con el Código ingresado.");
				}else{
					if(resultado.get("listaEmpresas")!=null){
						listaGrupoEmpresas = (List<Empresa>)resultado.get("listaEmpresas");
					}
					
					setObjectSession("listaEmpresasGrupo", listaGrupoEmpresas);
					if(!listaGrupoEmpresas.isEmpty()){
										
						getPrograma().setNombreGrupoEmpresa(resultado.get("nombreGrupo").toString());
						try {
							List<Empresa> listaEmpresasini=new ArrayList<Empresa>();
							listaEmpresasini = empresaBO.listarEmpresasPorPrograma(programa.getId());
								
							if (listaEmpresasini!=null && listaEmpresasini.size()>0){
								for (Empresa oempresaini:listaEmpresasini){
									if (oempresaini.getTipoGrupo()==null || oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
										empresaPrincipal.setCodigo(oempresaini.getCodigo()==null?"":oempresaini.getCodigo().toString());
									}									
									
									for (Empresa oempresafin:listaGrupoEmpresas){	
										
										if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoSecu("selected");
											}
										
										}
										/*
										else if (oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_ANEXA)){
											if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
												oempresafin.setSeleccionadoAnex("selected");
											}
										}
										*/
										
									}
									
								}				
							}
							
						}catch(BOException ex)
						{								
							logger.error(StringUtil.getStackTrace(ex));
						}catch(Exception ex)
						{								
							logger.error(StringUtil.getStackTrace(ex));
						}
	
						
					}else{
						addActionError("No se encontrón empresas para el grupo economico");
						getPrograma().setNombreGrupoEmpresa("");
					}
				}
			}
			
		} catch ( WSException e) {
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch ( Exception e) {
			setCodigoEmpresaGrupo("");
			getPrograma().setNombreGrupoEmpresa("");
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return forward;
	}
	//fin MCG20121128
	
	
	
	/**
	 * Iniciara los datos extraidos de la base de datos de personas
	 * par amostrarlos en DATOS BASICOS 
	 */
	public void loadDatosBasicos(){
		try {
			

		Calendar cal = Calendar.getInstance();
		Tabla tipoPersonal = null;
		planillaAdmin = new Planilla();
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_ADMINISTRATIVO);
		planillaAdmin.setTipoPerido(tipoPersonal);
		planillaAdmin.setTotal1(0);
		planillaAdmin.setTotal2(0);
		planillaAdmin.setTotal3(0);
		planillaAdmin.setTotal4(0);
		planillaAdmin.setAnio1(cal.get(Calendar.YEAR));
		planillaAdmin.setAnio2(cal.get(Calendar.YEAR)-1);
		planillaAdmin.setAnio3(cal.get(Calendar.YEAR)-2);
		planillaAdmin.setAnio4(cal.get(Calendar.YEAR)-3);
		
		planillaNoAdmin = new Planilla();
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_ADMINISTRATIVO);
		planillaNoAdmin.setTipoPerido(tipoPersonal);
		planillaNoAdmin.setTotal1(0);
		planillaNoAdmin.setTotal2(0);
		planillaNoAdmin.setTotal3(0);
		planillaNoAdmin.setTotal4(0);
		planillaNoAdmin.setAnio1(cal.get(Calendar.YEAR));
		planillaNoAdmin.setAnio2(cal.get(Calendar.YEAR)-1);
		planillaNoAdmin.setAnio3(cal.get(Calendar.YEAR)-2);
		planillaNoAdmin.setAnio4(cal.get(Calendar.YEAR)-3);
		
		totalPlanilla= new Planilla();
		totalPlanilla.setTotal1(0);
		totalPlanilla.setTotal2(0);
		totalPlanilla.setTotal3(0);
		totalPlanilla.setTotal4(0);		
		
		List<Tabla> listaDivisaDB = new ArrayList<Tabla>();
		List<Tabla> listaTipoDocumento = new ArrayList<Tabla>();
		
		listaDivisaDB= tablaBO.listarHijos(Constantes.ID_TABLA_DIVISADB);
		itemDivisaDB= ComboUtil.getSelectItems(listaDivisaDB, 
				"id",
				"descripcion",
				Constantes.VAL_DEFAULT_SELECTION);
		
		listaTipoDocumento= tablaBO.listarHijos(Constantes.ID_TABLA_TIPODOCUMENTO);			
		itemTipoDocumento= ComboUtil.getSelectItemsConSortCodigo2(listaTipoDocumento, 
		"id",
		"descripcion",
		"codigo",
		Constantes.VAL_DEFAULT_SELECTION);
		}catch(BOException ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}

	}
	
	public List<SelectItem> getItemDivisaDB() {
		return itemDivisaDB;
	}

	public void setItemDivisaDB(List<SelectItem> itemDivisaDB) {
		this.itemDivisaDB = itemDivisaDB;
	}

	public List<SelectItem> getItemTipoDocumento() {
		return itemTipoDocumento;
	}

	public void setItemTipoDocumento(List<SelectItem> itemTipoDocumento) {
		this.itemTipoDocumento = itemTipoDocumento;
	}

	public void completarListaGrupoEmpresas(){
		String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			List<Empresa> lista = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
			//Empresa grupo = new Empresa();
			//grupo.setCodigo(getObjectSession(Constantes.COD_GRUPO_SESSION).toString());
			//grupo.setNombre(getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString());
			//listaEmpresasGrupoDB.add(grupo);
			for(Empresa emp :lista ){
				listaEmpresasGrupoDB.add(emp);
			}
		}
		
	}
	
	public String buscarProgramas()
	{
		try
		{
			
			super.setObjectSession("filtroProgramas",null);
			
//			if(!tipoEmpresa.equals(Constantes.VAL_TIPO_DOC_RUC))
//				this.tipoDocBusqueda = "";
//			
//			if(!tipoEmpresa.equals("3"))
//				this.tipoDocBusquedagrupo = "";
//			
//			if(!tipoEmpresa.equals("4"))
//				this.tipoDocBusquedaTodos = "";
			
			this.programas = programaBO.listarProgramas(this.tipoEmpresa, 
														this.tipoDocBusqueda,
														this.codigoEmpresaGrupo,
														this.fechaInicio,
														this.fechaFin,
														this.idEstadoPrograma,
														this.tipoDocBusquedagrupo,
														this.tipoDocBusquedaTodos);
			
			StringBuffer sb = new StringBuffer();
//			sb.append(this.tipoEmpresa);
//			sb.append("|");
//			sb.append((this.tipoDocBusqueda == null || this.tipoDocBusqueda.equals("")) ? " " : this.tipoDocBusqueda);
//			sb.append("|");
			sb.append((this.codigoEmpresaGrupo == null || this.codigoEmpresaGrupo.equals("")) ? " " : this.codigoEmpresaGrupo);
			sb.append("|");
			sb.append((this.fechaInicio == null || this.fechaInicio.equals("")) ? " " : this.fechaInicio.getTime());
			sb.append("|");
			sb.append((this.fechaFin == null || this.fechaFin.equals("")) ? " " : this.fechaFin.getTime());
			//sb.append("|");
			//sb.append((this.idEstadoPrograma == null || this.idEstadoPrograma.equals("")) ? " " : this.idEstadoPrograma);
//			//ini MCG 20121203
//			sb.append("|");
//			sb.append((this.tipoDocBusquedagrupo == null || this.tipoDocBusquedagrupo.equals("")) ? " " : this.tipoDocBusquedagrupo);
//			//ini MCG 20121203
//			//ini MCG 20121203
//			sb.append("|");
//			sb.append((this.tipoDocBusquedaTodos == null || this.tipoDocBusquedaTodos.equals("")) ? " " : this.tipoDocBusquedaTodos);
//			//ini MCG 20121203
			
			super.setObjectSession("filtroProgramas",sb.toString());
		}catch(BOException ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
					
		return "consultasModificaciones";
	}
	
	/**
	 * Metodo para buscar una empresa por su codigo central
	 * ruc o nombre, este metodo retorna una estructura JSON
	 * que permitira realizar sugerencias durante la búsqueda.
	 */
	public void buscarByCriterio(){
		
		String json = "{\"item\":[";
		//String forward = "consultasModificaciones";
		logger.info("Consultando");
		try {
			List<HashMap<String,String>> lista = programaBO.findByCriterio(tipoDocBusqueda, tipoEmpresa, codigoEmpresaGrupo);
			if(lista!=null &&
			   !lista.isEmpty()){
				for(HashMap item : lista){
					json+="{\"value\"=\"" +
						  item.get("EMPRESAGRUPO") +
						  "\",\"id\"=\"" +
						  item.get("EMPRESAGRUPO") +
						  "\"},";
				}
				json += "]}";
			}
			logger.info("JSON="+json.toString());
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(json.toString());
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
		//return forward; 
	}
	
	public String paginarProgramas()
	{
		try
		{
			
			String vlTipoEmpresa="";
			String vltipoDocBusqueda="";
			String vlcodigoEmpresaGrupo;
			Date vlfechaInicio;
			Date vlfechaFin;
			String vlEstadoPrograma="";
			String vltipoDocBusquedagrupo="";
			String vltipoDocBusquedaTodos="";
			
			String[] params = super.getObjectSession("filtroProgramas").toString().split("\\|");
//			vlTipoEmpresa = params[0];
//			vltipoDocBusqueda = params[1];
			vlcodigoEmpresaGrupo = params[0];//params[2];
			vlfechaInicio = params[1].trim().equals("")?null : new Date(Long.parseLong(params[1]));//params[3].trim().equals("")?null : new Date(Long.parseLong(params[3]));
			vlfechaFin = params[2].trim().equals("")?null : new Date(Long.parseLong(params[2]));//params[4].trim().equals("")?null : new Date(Long.parseLong(params[4]));
			//vlEstadoPrograma = params[3];//params[5];
//			vltipoDocBusquedagrupo = params[6];
//			vltipoDocBusquedaTodos = params[7];
			
//			if(!vlTipoEmpresa.equals("2"))
//				vltipoDocBusqueda = "";
//			
//			if(!vlTipoEmpresa.equals("3"))
//				vltipoDocBusquedagrupo = "";
//			
//			if(!vlTipoEmpresa.equals("4"))
//				vltipoDocBusquedaTodos = "";
			
			this.programas = programaBO.listarProgramas(vlTipoEmpresa.trim(), 
														vltipoDocBusqueda.trim(),
														vlcodigoEmpresaGrupo.trim(),
														vlfechaInicio,
														vlfechaFin,
														vlEstadoPrograma.trim(),
														vltipoDocBusquedagrupo,
														vltipoDocBusquedaTodos);	

		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
					
		return "consultasModificaciones";
	}
	
	public String modificarCopiarPrograma() throws BOException
	{
		String forward = "modificarPrograma";
		try
		{
			logger.info(this.getIdPrograma());
			this.programa = this.programaBO.findById(this.getIdPrograma());
			logger.info(programa);
			logger.info(programa.getEstadoPrograma());
			logger.info(programa.getEstadoPrograma().getId());
			if(this.programa.getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE))
			{
							
				List<Empresa> empresas = this.empresaBO.listarEmpresasPorPrograma(this.programa.getId());
					
				boolean existePrincipal = false;
				
				
				if(empresas != null)
				{
					for(Empresa emp : empresas ){
													
						if(emp.getTipoGrupo() == null || emp.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL))
						{
							existePrincipal = true;
							this.empresaPrincipal = emp;
						}
					}
				}
					
				
				Map<String, Object> sessionparam = ActionContext.getContext().getSession();
								
				sessionparam.put(Constantes.ID_PROGRAMA_SESSION, programa.getId().toString());
				//ini MCG20121030
				sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, programa.getNumeroSolicitud()==null?"":programa.getNumeroSolicitud().toString());
				//fin MCG20121030
				
				//si es local o corporativo
				sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, programa.getTipoPrograma().getId());
				//si es empresa o grupo
				sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
				//en el caso de grupos se sube a session los datos de la empresa 
				//principal en los datos de la empresa 
				
				//MCG 20120430 add getId al tipoempresa
				if(this.programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, programa.getRuc());
					sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, programa.getIdEmpresa());
					sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, programa.getIdEmpresa());
					if(existePrincipal)
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());

				}else{
					List<Empresa> listaEmpresas = empresaBO.listarEmpresasPorPrograma(idPrograma);
					sessionparam.put(Constantes.COD_GRUPO_SESSION, programa.getIdGrupo());
					sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaEmpresas);
					
					if(existePrincipal)
					{
						sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
						sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
						sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
					}
					
					
				}
				sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, programa.getNombreGrupoEmpresa());
				sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, programa.getAnio());
				
				
				
			}
			else if(this.programa.getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_CERRADO))
			{
				ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(this.programa);
							
				boolean existePrincipal = false;
				
				Programa copiaPrograma = new Programa();
				copiaPrograma.setActividadPrincipal(this.programa.getActividadPrincipal());
				copiaPrograma.setPais(this.programa.getPais());
				copiaPrograma.setAntiguedadNegocio(this.programa.getAntiguedadNegocio());
				copiaPrograma.setAntiguedadCliente(this.programa.getAntiguedadCliente());
				copiaPrograma.setTipoEmpresa(this.programa.getTipoEmpresa());
				copiaPrograma.setTipoPrograma(this.programa.getTipoPrograma());
				copiaPrograma.setNombreGrupoEmpresa(this.programa.getNombreGrupoEmpresa());
				copiaPrograma.setAnio(this.programa.getAnio());
				copiaPrograma.setIdEmpresa(this.programa.getIdEmpresa());
				
				Tabla estadoPrograma = new Tabla();
				estadoPrograma.setId(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE);
				copiaPrograma.setEstadoPrograma(estadoPrograma);
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);			
				copiaPrograma.setCodUsuarioCreacion(user.getRegistroHost());
				copiaPrograma.setFechaCreacion(new Date());
				
				//Datos Basicos
				copiaPrograma.setComentAccionariado(programa.getComentAccionariado());				
				copiaPrograma.setComentPartiSignificativa(programa.getComentPartiSignificativa());
				copiaPrograma.setComentRatinExterno(programa.getComentRatinExterno());
				copiaPrograma.setComentvaloraGlobal(programa.getComentvaloraGlobal());				
				
				//Relaciones Bancarias
				//copiaPrograma.setPorcentajeNormalSF(programa.getPorcentajeNormalSF());
				//copiaPrograma.setPorcentajeProblemaPotencialSF(programa.getPorcentajeProblemaPotencialSF());
				//copiaPrograma.setPorcentajeDeficienteSF(programa.getPorcentajeDeficienteSF());
				//copiaPrograma.setPorcentajeDudosoSF(programa.getPorcentajeDudosoSF());
				//copiaPrograma.setPorcentajePerdidaSF(programa.getPorcentajePerdidaSF());
				//copiaPrograma.setCalificacionBanco(programa.getCalificacionBanco());
				//copiaPrograma.setCuotaFinanciera(programa.getCuotaFinanciera());
				copiaPrograma.setComentcuotaFinanciera(programa.getComentcuotaFinanciera());
				
				//copiaPrograma.setIdmodeloRentabilidad(programa.getIdmodeloRentabilidad());
				
				//copiaPrograma.setEfectividadProm6sol(programa.getEfectividadProm6sol());
				//copiaPrograma.setEfectividadProm6dol(programa.getEfectividadProm6dol());				
				//copiaPrograma.setProtestoProm6sol(programa.getProtestoProm6sol());
				//copiaPrograma.setProtestoProm6dol(programa.getProtestoProm6dol());
				//copiaPrograma.setEfectividadUltmaniosol(programa.getEfectividadUltmaniosol());
				//copiaPrograma.setEfectividadUltmaniodol(programa.getEfectividadUltmaniodol());
				
				//Propuesta de Riesgo
				copiaPrograma.setTipoEstructura(programa.getTipoEstructura());							
				
				//Politicas de Riesgo
				copiaPrograma.setLimiteAutorizadoPRG(programa.getLimiteAutorizadoPRG());
				copiaPrograma.setProximaRevisionPRG(programa.getProximaRevisionPRG());
				copiaPrograma.setMotivoProximaPRG(programa.getMotivoProximaPRG());					
				
				copiaPrograma.setTipoMiles(programa.getTipoMiles());
				copiaPrograma.setTipoMilesPLR(programa.getTipoMilesPLR());
				copiaPrograma.setTipoMilesRB(programa.getTipoMilesRB());
				
							
				
				
						
				
				ProgramaBlob copiaProgramaBlob = null;
				if(programaBlob != null)
				{	
					
					copiaProgramaBlob = new ProgramaBlob();
					copiaProgramaBlob=programaBlob;
					copiaProgramaBlob.setId(null);
					copiaProgramaBlob.setPrograma(null);
					//copiaProgramaBlob.setFodaAmenazas(programaBlob.getFodaAmenazas());
					//copiaProgramaBlob.setFodaDebilidades(programaBlob.getFodaDebilidades());
					//copiaProgramaBlob.setFodaFotalezas(programaBlob.getFodaFotalezas());
					//copiaProgramaBlob.setFodaOportunidades(programaBlob.getFodaOportunidades());
					copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
					copiaProgramaBlob.setFechaCreacion(new Date());
					copiaProgramaBlob.setCodUsuarioModificacion(null);
					copiaProgramaBlob.setFechaModificacion(null);
					
	
				}
				

				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR))
				{

					copiaPrograma.setIdEmpresa(this.programa.getIdEmpresa());
					copiaPrograma.setRuc(this.programa.getRuc());

				}
				else
				{

					copiaPrograma.setIdGrupo(this.programa.getIdGrupo());
					copiaPrograma.setRuc(this.programa.getRuc());
					
					
					List<Empresa> listaGrupoEmpresas =  this.empresaBO.listarEmpresasPorPrograma(this.programa.getId());
										
					for(Empresa emp : listaGrupoEmpresas ){
						
						if(emp.getTipoGrupo()==null || emp.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL))
						{
							existePrincipal = true;
							this.empresaPrincipal = emp;
						}
						
						emp.setId(null);
					}
										
					programaBO.setListaGrupoEmpresas(listaGrupoEmpresas);
				}
				programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));	
				String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
				programaBO.setPathWebService(pathWebService);
				programaBO.setConfiguracionWS(getConfiguracionWS());
				programaBO.save(copiaPrograma);
				
				
				if(copiaProgramaBlob != null)
				{
									
					programaBlobBO.setPrograma(copiaPrograma);
					programaBlobBO.save(copiaProgramaBlob);
					
				}
				copiaDatosBasicos(programa.getId(), copiaPrograma,programa.getAnio());
				copiarSintesisEconomica(programa.getId(),copiaPrograma,listaGrupoEmpresas,null);
				copiarAnalisisSectorial(programa.getId(),copiaPrograma,null);
				copiarRelacionesBancarias(programa.getId(),copiaPrograma,null);
				copiarPropuestaRiesgo(programa.getId(),copiaPrograma,listaGrupoEmpresas,null);
				copiarPoliticasRiesgo(programa.getId(),copiaPrograma,null);
				copiarAnexo(programa.getId(),copiaPrograma,listaGrupoEmpresas,null);
				
				
				
			
	
				Map<String, Object> sessionparam = ActionContext.getContext().getSession();
				sessionparam.put(Constantes.ID_PROGRAMA_SESSION, copiaPrograma.getId().toString());
				
				//ini MCG20121030
				sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, copiaPrograma.getNumeroSolicitud()==null?"":copiaPrograma.getNumeroSolicitud().toString());
				//fin MCG20121030
				
				sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, copiaPrograma.getTipoPrograma().getId());
				//si es empresa o grupo
				sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, copiaPrograma.getTipoEmpresa().getId().toString());
				//en el caso de grupos se sube a session los datos de la empresa 
				//principal en los datos de la empresa 
				//MCG 20120430 add getId
				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, copiaPrograma.getRuc());
					sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, copiaPrograma.getIdEmpresa());
					sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, copiaPrograma.getIdEmpresa());
					if(existePrincipal)
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
				}else{
					sessionparam.put(Constantes.COD_GRUPO_SESSION, copiaPrograma.getIdGrupo());
					sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaGrupoEmpresas);
					if(existePrincipal)
					{
						sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
						sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
						sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
					}
				}
				sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, copiaPrograma.getNombreGrupoEmpresa());
				sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, copiaPrograma.getAnio());
			}
			
		}
		catch(Exception ex)
		{	forward = "consultasModificaciones";
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		
		return forward;
		
	}
	
	/**	 
	 * @author MCORNETERO
	 * COPIA DE PROGRAMA FINANCIERO
	 */
	//INI MCG 20120427
	
	
//	public String copiarProgramaGeneral() throws BOException
//	{
//		String forward="modificarPrograma";
//		String idtipoCopia=getTipoCopiapf();
//		String copiarSinData=getCopiarsinDataHidden();
//		System.out.println(idtipoCopia);
//		System.out.println(copiarSinData);
//		
//		if (copiarSinData.equals("1")){
//			
//			forward=copiarSinPrograma();
//		}else{
//			forward=copiarPrograma();
//		}
//		return forward;
//		
//	}
	
	
	public String copiarPrograma() throws BOException
	{
		
		logger.info("INICIO COPIA PROGRAMA");
		
		List<Empresa> listaEmpresas = null;
		List<Empresa> listaEmpresaGeneral = null;
		String forward="modificarPrograma";
		String idtipoCopia=getTipoCopiapf();
		String copiarSinData=getCopiarsinDataHidden();
		//System.out.println("idtipoCopia:" +idtipoCopia);
		//System.out.println("copiasindata" + copiarSinData);
		
		String ASelectedItemsEmpresa=getSelectedItemsEmpresa();
		String ASelectedItemsPrime= getSelectedItemsPrime();
		String ASelectedItemsEmpresaPrograma=getSelectedItemsEmpresaPrograma();
		
		
		try
		{
					
			
			logger.info(programa);
			String idProgramaMother=String.valueOf(programa.getId());
			String nombregrupoEmpresaini=programa.getNombreGrupoEmpresa()==null?"":programa.getNombreGrupoEmpresa();
			this.programa = this.programaBO.findById(this.programa.getId());
			try {
				logger.error("programa:"+ programa);				
				logger.error("Estado Programa: "+ programa.getEstadoPrograma().getId());
				logger.error("Nombre Grupo Empresa: " + programa.getNombreGrupoEmpresa());
				logger.error("getCodigoEmpresaGrupo:"+getCodigoEmpresaGrupo());
			} catch (Exception e) {
				 
			}
		
			logger.info(programa.getEstadoPrograma());
			logger.info(programa.getEstadoPrograma().getId());			
			
			logger.info("getTipoEmpresa():"+getTipoEmpresa());
			logger.info("getTipoPrograma():"+getTipoPrograma());			
			logger.info("getCodigoEmpresaGrupo():"+getCodigoEmpresaGrupo());
			
			
			 //ID_TIPO_COPIA_IaI="1";
			 //ID_TIPO_COPIA_IaG="2";	 
			 //ID_TIPO_COPIA_GaI="3";
			 //ID_TIPO_COPIA_GaG="4";
			String[] arrayItemsEmpresa = ASelectedItemsEmpresa.split(",");	
			String[] arrayPrime = ASelectedItemsPrime.split(",");
			String[] arrayItemsEmpresaPrograma = ASelectedItemsEmpresaPrograma.split(",");
			List<Empresa> olistaEmpresaCopia=new ArrayList<Empresa>();
			
			try {
				logger.error("arrayItemsEmpresa: "+arrayItemsEmpresa);
				logger.error("arrayPrime: "+arrayPrime);
				logger.error("arrayItemsEmpresaPrograma: "+arrayItemsEmpresaPrograma);
			} catch (Exception e) {
				 
			}
			
			String codEmpresaPrime=arrayPrime[0];
			for(int t = 0; t < arrayItemsEmpresa.length; t++){
				String codigoEmpresa=arrayItemsEmpresa[t];
				Empresa oempresa=new Empresa();
				oempresa.setCodigo(codigoEmpresa);
				if (codigoEmpresa.equals(codEmpresaPrime)){
					oempresa.setIdtipoGrupoCopia(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
				}else{
					oempresa.setIdtipoGrupoCopia(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
				}				
				olistaEmpresaCopia.add(oempresa);
				
			}
			for(Empresa oempresa : olistaEmpresaCopia){
				String codigoEmpresa=oempresa.getCodigo();
				if (arrayItemsEmpresaPrograma.length>0){
					for (int i = 0; i < arrayItemsEmpresaPrograma.length; i++){
						String cadempresaPrograma=arrayItemsEmpresaPrograma[i];
						String[] ArrayEmpresaPrograma = cadempresaPrograma.split("-");
						String codEmpresa=ArrayEmpresaPrograma[0];
						String idPrograma=ArrayEmpresaPrograma[1]==null?"":ArrayEmpresaPrograma[1].toString();
						if (codigoEmpresa.equals(codEmpresa)){
							oempresa.setIdProgramaCopia(idPrograma)	;
							break;
						}
					}
				}
			}
			String idprogramaPrime=idProgramaMother;
			for(Empresa oempresa : olistaEmpresaCopia){
				if (oempresa.getCodigo().equals(codEmpresaPrime)){
					idprogramaPrime=oempresa.getIdProgramaCopia();
				}
			}		
			
	
			
			if(this.programa.getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_CERRADO))
			{
	
				boolean existePrincipal = false;
				
				Programa copiaPrograma = new Programa();
				
				//nueva valores
				Tabla objTipoPrograma = new Tabla();
				objTipoPrograma.setId(Long.valueOf(getTipoPrograma()));
				
				
				 //ID_TIPO_COPIA_IaI="1";
				 //ID_TIPO_COPIA_IaG="2";	 
				 //ID_TIPO_COPIA_GaI="3";
				 //ID_TIPO_COPIA_GaG="4";
				Long valTipoEmpresa=null;
				if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_IaI)){
					valTipoEmpresa=Constantes.ID_TIPO_EMPRESA_EMPR;
					idprogramaPrime=idProgramaMother;
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_GaI)){
					valTipoEmpresa=Constantes.ID_TIPO_EMPRESA_EMPR;
					
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_IaG)){
					valTipoEmpresa=Constantes.ID_TIPO_EMPRESA_GRUPO;
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_GaG)){
					valTipoEmpresa=Constantes.ID_TIPO_EMPRESA_GRUPO;
				}
				
				Programa oprogramaPrime = this.programaBO.findById(Long.valueOf(idprogramaPrime));
				String codEmpresaPrin=oprogramaPrime.getIdEmpresa();
				boolean isPrime=false;
				if (codEmpresaPrin.equals(codEmpresaPrime)){
					isPrime=true;
				}	
				
				Tabla objTipoEmpresa = new Tabla();
				objTipoEmpresa.setId(valTipoEmpresa);				
				copiaPrograma.setTipoEmpresa(objTipoEmpresa);
				
				copiaPrograma.setTipoPrograma(objTipoPrograma);			
				
				
				copiaPrograma.setNombreGrupoEmpresa(nombregrupoEmpresaini);
				copiaPrograma.setAnio(oprogramaPrime.getAnio());				
				
				Tabla estadoPrograma = new Tabla();
				estadoPrograma.setId(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE);
				copiaPrograma.setEstadoPrograma(estadoPrograma);
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);			
				copiaPrograma.setCodUsuarioCreacion(user.getRegistroHost());
				copiaPrograma.setFechaCreacion(new Date());
				
				ProgramaBlob copiaProgramaBlob = null;
				
				if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_IaI)){					
					copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,programa,programa,user,"P",true,false);
					
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_IaG )){					
					if (oprogramaPrime.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
						copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"N",false,false);
					}else{
						if (isPrime){
							copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"N",false,false);			
						}else{
							copiaProgramaBlob=asignarProgramaByEmpresa(copiaPrograma,oprogramaPrime,codEmpresaPrime,programa,user,false,false);
						}
					}					
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_GaG )){
					if (oprogramaPrime.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
						copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"G",false,true);
					}else{
						if (isPrime){
							copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"G",false,true);			
						}else{
							copiaProgramaBlob=asignarProgramaByEmpresa(copiaPrograma,oprogramaPrime,codEmpresaPrime,programa,user,false,true);
						}
					}
					
				}else if (idtipoCopia.equals(Constantes.ID_TIPO_COPIA_GaI)){
					if (oprogramaPrime.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
						copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"P",false,false);
					}else{
						if (isPrime){
							copiaProgramaBlob=asignarProgramaPrime(copiaPrograma,oprogramaPrime,programa,user,"P",false,false);			
						}else{
							copiaProgramaBlob=asignarProgramaByEmpresa(copiaPrograma,oprogramaPrime,codEmpresaPrime,programa,user,false,false);
						}
					}
				}
		
				//ini mcg
				
				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
							listaEmpresaGeneral = new ArrayList<Empresa>();
							List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
							empresaPrincipal=new Empresa();
							empresaPrincipal = findEmpresaGrupo(listaTemp, codEmpresaPrime);
							empresaPrincipal.setIdProgramaCopia(idprogramaPrime);
							
							copiaPrograma.setIdEmpresa(codEmpresaPrime);
							copiaPrograma.setNombreGrupoEmpresa(empresaPrincipal.getNombre());								
							copiaPrograma.setRuc(empresaPrincipal.getRuc());
							listaEmpresaGeneral.add(empresaPrincipal);		

				}else{
							//ini grupo
							listaEmpresas = new ArrayList<Empresa>();
							listaEmpresaGeneral = new ArrayList<Empresa>();
							copiaPrograma.setIdGrupo(getCodigoEmpresaGrupo());
							
							Tabla tipoGrupo = null;// EMPRESA PRINCIPAL, SECUNDARIA O ANEXA
							List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
								empresaPrincipal=new Empresa();
								empresaPrincipal = findEmpresaGrupo(listaTemp, codEmpresaPrime);								
								existePrincipal = true;						
								tipoGrupo = new Tabla();
								tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
								empresaPrincipal.setTipoGrupo(tipoGrupo);
								empresaPrincipal.setIdtipoGrupoCopia(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
								empresaPrincipal.setIdProgramaCopia(idprogramaPrime);
								
								listaEmpresas.add(empresaPrincipal);
								listaEmpresaGeneral.add(empresaPrincipal);
								//se registra el ruc de la empresa principal
								copiaPrograma.setIdEmpresa(codEmpresaPrime);
								copiaPrograma.setRuc(empresaPrincipal.getRuc());						
							
							if (olistaEmpresaCopia!= null){
								for(Empresa oempresa: olistaEmpresaCopia){
									if (oempresa.getIdtipoGrupoCopia().equals(Constantes.ID_TIPO_EMPRESA_SECUNDARIA)){
										Empresa empresa =null;
										empresa = findEmpresaGrupo(listaTemp, oempresa.getCodigo());
										tipoGrupo = new Tabla();
										tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
										empresa.setTipoGrupo(tipoGrupo);
										empresa.setIdtipoGrupoCopia(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
										empresa.setIdProgramaCopia(oempresa.getIdProgramaCopia());
										listaEmpresas.add(empresa);
										listaEmpresaGeneral.add(empresa);
									}
								}
							}

							programaBO.setListaGrupoEmpresas(listaEmpresas);							
							//fin grupo					
				}
				//fin mcg
				
				
				programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));	
				String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
				programaBO.setPathWebService(pathWebService);
				programaBO.setConfiguracionWS(getConfiguracionWS());
				programaBO.save(copiaPrograma);
								
				if(copiaProgramaBlob != null)
				{									
					programaBlobBO.setPrograma(copiaPrograma);
					programaBlobBO.save(copiaProgramaBlob,copiaPrograma);					
				}
				
				//if (!copiarSinData.equals(Constantes.COD_COPIA_SINDATA)){
					copiaDatosBasicos(programa.getId(), copiaPrograma,programa.getAnio(),listaEmpresaGeneral,Long.valueOf(idprogramaPrime));
					copiarSintesisEconomica(programa.getId(),copiaPrograma,listaEmpresaGeneral,Long.valueOf(idprogramaPrime));
					copiarRating(programa.getId(),copiaPrograma,listaEmpresaGeneral,Long.valueOf(idprogramaPrime));
					copiarAnalisisSectorial(programa.getId(),copiaPrograma,Long.valueOf(idprogramaPrime));
					copiarRelacionesBancarias(programa.getId(),copiaPrograma,Long.valueOf(idprogramaPrime));
					//se quito la opcion de registro copiarPropuestaRiesgo(programa.getId(),copiaPrograma,listaEmpresaGeneral,Long.valueOf(idprogramaPrime));
					copiarPoliticasRiesgo(programa.getId(),copiaPrograma,Long.valueOf(idprogramaPrime));
					copiarAnexo(programa.getId(),copiaPrograma,listaEmpresaGeneral,Long.valueOf(idprogramaPrime));
					copiaReporteCredito(programa.getId(),copiaPrograma,listaEmpresaGeneral,Long.valueOf(idprogramaPrime));			
				//}
	
				Map<String, Object> sessionparam = ActionContext.getContext().getSession();
				sessionparam.put(Constantes.ID_PROGRAMA_SESSION, copiaPrograma.getId().toString());
				
				//ini MCG20121030
				sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, copiaPrograma.getNumeroSolicitud()==null?"":copiaPrograma.getNumeroSolicitud().toString());
				//fin MCG20121030
				
				//si es Local o Corporativo
				sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, copiaPrograma.getTipoPrograma().getId());
				//si es empresa o grupo
				sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, copiaPrograma.getTipoEmpresa().getId().toString());
				//en el caso de grupos se sube a session los datos de la empresa 
				//principal en los datos de la empresa 
				//MCG 20120430 add getId
				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, copiaPrograma.getRuc());
					sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, copiaPrograma.getIdEmpresa());
					sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, copiaPrograma.getIdEmpresa());
					if(existePrincipal)
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
				}else{
					sessionparam.put(Constantes.COD_GRUPO_SESSION, copiaPrograma.getIdGrupo());
					sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaEmpresas);
					if(existePrincipal)
					{
						sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
						sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
						sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
					}
				}
				sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, copiaPrograma.getNombreGrupoEmpresa());
				sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, copiaPrograma.getAnio());							
			}
		
		} catch (BOException e) {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupo";
			e.printStackTrace();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));	
		}catch(Exception ex)	{	
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupo";
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		logger.info("FIN COPIA PROGRAMA");
		return forward;
		
		
	}
	
	private ProgramaBlob asignarProgramaPrime(Programa copiaPrograma,
									  Programa oprogramaPrime,Programa oprogramaIni,UsuarioSesion user,
									  String sSaveInfGrupo,boolean isGrabaAll,boolean isSaveBlobInfGrupo){
		ProgramaBlob copiaProgramaBlob = null;
		try {	
			//ini se obtiene de nuevo de host
			copiaPrograma.setActividadPrincipal(oprogramaPrime.getActividadPrincipal());
	//			copiaPrograma.setPais(oprogramaPrime.getPais());
	//			copiaPrograma.setAntiguedadNegocio(oprogramaPrime.getAntiguedadNegocio());
	//			copiaPrograma.setAntiguedadCliente(oprogramaPrime.getAntiguedadCliente());
				//ini se obtiene de nuevo de host
			
			//Datos Basicos
			copiaPrograma.setComentAccionariado(oprogramaPrime.getComentAccionariado());				
			copiaPrograma.setComentPartiSignificativa(oprogramaPrime.getComentPartiSignificativa());
			copiaPrograma.setComentRatinExterno(oprogramaPrime.getComentRatinExterno());
			copiaPrograma.setComentvaloraGlobal(oprogramaPrime.getComentvaloraGlobal());				
			
			if (sSaveInfGrupo.equals("G"))	{	
				//Relaciones Bancarias
				copiaPrograma.setComentcuotaFinanciera(oprogramaIni.getComentcuotaFinanciera());
				
				//Propuesta de Riesgo
				copiaPrograma.setTipoEstructura(oprogramaIni.getTipoEstructura());							
				
				//Politicas de Riesgo
				copiaPrograma.setLimiteAutorizadoPRG(oprogramaIni.getLimiteAutorizadoPRG());
				copiaPrograma.setProximaRevisionPRG(oprogramaIni.getProximaRevisionPRG());
				copiaPrograma.setMotivoProximaPRG(oprogramaIni.getMotivoProximaPRG());					
				
				copiaPrograma.setTipoMiles(oprogramaIni.getTipoMiles());
				copiaPrograma.setTipoMilesPLR(oprogramaIni.getTipoMilesPLR());
				copiaPrograma.setTipoMilesRB(oprogramaIni.getTipoMilesRB());				
			}else if (sSaveInfGrupo.equals("P")){
				//Relaciones Bancarias
				copiaPrograma.setComentcuotaFinanciera(oprogramaPrime.getComentcuotaFinanciera());
				
				//Propuesta de Riesgo
				copiaPrograma.setTipoEstructura(oprogramaPrime.getTipoEstructura());							
				
				//Politicas de Riesgo
				copiaPrograma.setLimiteAutorizadoPRG(oprogramaPrime.getLimiteAutorizadoPRG());
				copiaPrograma.setProximaRevisionPRG(oprogramaPrime.getProximaRevisionPRG());
				copiaPrograma.setMotivoProximaPRG(oprogramaPrime.getMotivoProximaPRG());					
				
				copiaPrograma.setTipoMiles(oprogramaPrime.getTipoMiles());
				copiaPrograma.setTipoMilesPLR(oprogramaPrime.getTipoMilesPLR());
				copiaPrograma.setTipoMilesRB(oprogramaPrime.getTipoMilesRB());	
			}
			//Reporte Credito
			//copiaPrograma.setNumeroSolicitud("");	
			copiaPrograma.setCuentaCorriente(oprogramaPrime.getCuentaCorriente());		
			//copiaPrograma.setDniCarnetExt(programa.getDniCarnetExt());		
			//copiaPrograma.setRucRDC(programa.getRucRDC());	
			copiaPrograma.setCiiuRDC(oprogramaPrime.getCiiuRDC());
			copiaPrograma.setGestor(oprogramaPrime.getGestor());
			copiaPrograma.setSegmento(oprogramaPrime.getSegmento());	
			//copiaPrograma.setIdsegmento(programa.getIdsegmento());	
			copiaPrograma.setFechaRDC(oprogramaPrime.getFechaRDC());
			copiaPrograma.setOficina(oprogramaPrime.getOficina());
			//copiaPrograma.setIdoficina(programa.getIdoficina());
			copiaPrograma.setNumeroRVGL(oprogramaPrime.getNumeroRVGL());
			copiaPrograma.setSalem(oprogramaPrime.getSalem());
			//copiaPrograma.setPosicionClienteGrupo(programa.getPosicionClienteGrupo());
			copiaPrograma.setVulnerabilidad(oprogramaPrime.getVulnerabilidad());	
			copiaPrograma.setTotalInversion(oprogramaPrime.getTotalInversion());
			copiaPrograma.setMontoPrestamo(oprogramaPrime.getMontoPrestamo());
			copiaPrograma.setEntorno(oprogramaPrime.getEntorno());
			copiaPrograma.setPoblacionAfectada(oprogramaPrime.getPoblacionAfectada());
			copiaPrograma.setCategorizacionAmbiental(oprogramaPrime.getCategorizacionAmbiental());
			copiaPrograma.setComentarioAdmision(oprogramaPrime.getComentarioAdmision());
			
			//copiaPrograma.setContrato(programa.getContrato());
			if (isGrabaAll)	{
				ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(oprogramaPrime);
				if(programaBlob != null){														
					copiaProgramaBlob = new ProgramaBlob();
					copiaProgramaBlob=programaBlob;
					copiaProgramaBlob.setId(null);
					copiaProgramaBlob.setPrograma(null);
					copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
					copiaProgramaBlob.setFechaCreacion(new Date());
					copiaProgramaBlob.setCodUsuarioModificacion(null);
					copiaProgramaBlob.setFechaModificacion(null);
				}
			}else{
					//grabar datos basicos blod cuando es empresa individual
					ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(oprogramaPrime);
					if(programaBlob != null){
					copiaProgramaBlob = new ProgramaBlob();
					copiaProgramaBlob.setId(null);
					copiaProgramaBlob.setPrograma(null);
	
					copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
					copiaProgramaBlob.setFechaCreacion(new Date());
					copiaProgramaBlob.setCodUsuarioModificacion(null);
					copiaProgramaBlob.setFechaModificacion(null);
					
					copiaProgramaBlob.setSintesisEmpresa( programaBlob.getSintesisEmpresa()); //DB_SINTESIS_EMPR	byte[]
					copiaProgramaBlob.setDatosMatriz(programaBlob.getDatosMatriz()); //DB_DATOS_MATR	byte[]
					copiaProgramaBlob.setEspacioLibre(programaBlob.getEspacioLibre()); //DB_ESPACIO_LIBRE	byte[]
					copiaProgramaBlob.setComenComprasVentas(programaBlob.getComenComprasVentas()); //DB_ESPACIO_LIBRE	byte[]
					copiaProgramaBlob.setConcentracion(programaBlob.getConcentracion()); //DB_CONCENTRACION byte[]
					copiaProgramaBlob.setValoracion(programaBlob.getValoracion()); //DB_VALORACION byte[]
					
					copiaProgramaBlob.setDatosBasicosaddRDC(programaBlob.getDatosBasicosaddRDC());//RDC_COMEN_DATOSBASICOS_ADD	BLOB	Y			
					copiaProgramaBlob.setComenSintesisEconFinaddRDC(programaBlob.getComenSintesisEconFinaddRDC());//RDC_COMEN_SINT_ECONFINAN_ADD	BLOB	Y	
					//ANALISIS SECTORIAL                                                                  
					copiaProgramaBlob.setEspacioLibreAS(programaBlob.getEspacioLibreAS()); 					//AS_ESPACIO_LIBRE	byte[]              
					 
					
					if (isSaveBlobInfGrupo){						
						ProgramaBlob programaBlobGrupo = programaBlobBO.findBlobByPrograma(oprogramaIni);
						if(programaBlobGrupo != null){
							
							//SINTESIS ECONOMICO
							copiaProgramaBlob.setComenSituFinanciera(programaBlobGrupo.getComenSituFinanciera()); //SF_COMENTARIO_SITU_FINA	byte[]
							copiaProgramaBlob.setComenSituEconomica(programaBlobGrupo.getComenSituEconomica()); //SF_COMENTARIO_SITU_ECON	byte[]
							copiaProgramaBlob.setValoracionEconFinanciera(programaBlobGrupo.getValoracionEconFinanciera()); //SF_VALORACION_ECON_FINA	byte[]
							copiaProgramaBlob.setValoracionPosiBalance(programaBlobGrupo.getValoracionPosiBalance()); // SF_VALORACION_POSI_BALA	byte[]	
							//RATING
							copiaProgramaBlob.setValoracionRating(programaBlobGrupo.getValoracionRating()); //RA_VALORACION_RATI	byte[]
							
							//ANALISIS SECTORIAL                                                                  
							copiaProgramaBlob.setEspacioLibreAS(programaBlobGrupo.getEspacioLibreAS()); 					//AS_ESPACIO_LIBRE	byte[]              
							                                                                                      
							//RELACIONES BANCARIAS                                                                
							copiaProgramaBlob.setComenLineas(programaBlobGrupo.getComenLineas());							//RB_COMENTARIOS_LINEAS	byte[]          
							copiaProgramaBlob.setRentaModelGlobal(programaBlobGrupo.getRentaModelGlobal());  			//RB_RENTAB_MODEL_GLOB	byte[]          
							copiaProgramaBlob.setRentaModelBEC(programaBlobGrupo.getRentaModelBEC()); 					//RB_RENTAB_MODEL_GLOB	byte[]          
							copiaProgramaBlob.setCampoLibreRB(programaBlobGrupo.getCampoLibreRB()); 						//RB_CAMPO_LIBR	byte[]                  
							copiaProgramaBlob.setComenPoolBanc(programaBlobGrupo.getComenPoolBanc()); 					//RB_COMENTARIO_POOL_BANC byte[]        
							copiaProgramaBlob.setCampoLibreAnexos(programaBlobGrupo.getCampoLibreAnexos());				//RB_CAMPO_LIBRE_ANEXO                  
							copiaProgramaBlob.setComentIndTransaccional(programaBlobGrupo.getComentIndTransaccional()) ;	//RB_COMEN_INDTRANSA                    
							                                                                                      
							//FACTORES RIESGO                                                                     
							copiaProgramaBlob.setFodaFotalezas(programaBlobGrupo.getFodaFotalezas()); 					//FR_FODA_FORTA	byte[]                  
							copiaProgramaBlob.setFodaOportunidades(programaBlobGrupo.getFodaOportunidades()); 			//FR_FODA_OPOR	byte[]                  
							copiaProgramaBlob.setFodaDebilidades(programaBlobGrupo.getFodaDebilidades()); 				//FR_FODA_DEBI	byte[]                  
							copiaProgramaBlob.setFodaAmenazas(programaBlobGrupo.getFodaAmenazas()); 						//FR_FODA_AMEN	byte[]                  
							copiaProgramaBlob.setConclucionFoda(programaBlobGrupo.getConclucionFoda()); 					//FR_CONCLUSION_FODA	byte[]            
								                                                                
							//PROPUESTA DE RIESGO                                                                 
							copiaProgramaBlob.setCampoLibrePR(programaBlobGrupo.getCampoLibrePR()); 							//PR_CAMPO_LIBR	byte[]                
							copiaProgramaBlob.setEstructuraLimite(programaBlobGrupo.getEstructuraLimite()); 					//PR_ESTR_LIMIT	byte[]                
							copiaProgramaBlob.setConsideracionPR(programaBlobGrupo.getConsideracionPR()); 					//PR_CONSIDERACION	byte[]            
							                                                                                      
							//POLITICAS DE RIESGO                                                                 
							copiaProgramaBlob.setDetalleOperacionGarantia(programaBlobGrupo.getDetalleOperacionGarantia());  //PRG_DET_OPERACI_GARANT	byte[]      
							copiaProgramaBlob.setRiesgoTesoreria(programaBlobGrupo.getRiesgoTesoreria()); 				  //PRG_RIESGO_TESO	byte[]              
							copiaProgramaBlob.setPoliticasRiesGrupo(programaBlobGrupo.getPoliticasRiesGrupo()); 				//PRG_POLITICAS_RIES_GRUP	byte[]      
							copiaProgramaBlob.setPoliticasDelegacion(programaBlobGrupo.getPoliticasDelegacion()); 			//PRG_POLITICAS_DELE	byte[]          

							
						}						
					}					
				}
			}
			return copiaProgramaBlob;
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			return null;
		}
	}

	
	private ProgramaBlob asignarProgramaByEmpresa(Programa copiaPrograma,Programa oprogramaPrime, String codEmpresaPrime,Programa oprogramaIni,UsuarioSesion user,boolean isGrabar,boolean isSaveBlobInfGrupo){
		ProgramaBlob copiaProgramaBlob=null;
		try {			

		List<DatosBasico> olistaDatosBasicos = new ArrayList<DatosBasico>();					
		olistaDatosBasicos=datosBasicosBO.getListaDatosBasico(oprogramaPrime,codEmpresaPrime);
			if (olistaDatosBasicos!=null && olistaDatosBasicos.size()>0){
						DatosBasico datosbas = olistaDatosBasicos.get(0);
						//ini se obtiene de nuevo de host
						copiaPrograma.setActividadPrincipal(datosbas.getActividadPrincipal());
//						copiaPrograma.setPais(datosbas.getPais());
//						copiaPrograma.setAntiguedadNegocio(datosbas.getAntiguedadNegocio());
//						copiaPrograma.setAntiguedadCliente(datosbas.getAntiguedadCliente());
//						copiaPrograma.setGrupoRiesgoBuro(datosbas.getGrupoRiesgoBuro());
						//Reporte Credito
//						copiaPrograma.setGestor(datosbas.getGestor());
//						copiaPrograma.setSegmento(datosbas.getSegmento());
//						copiaPrograma.setOficina(datosbas.getOficina());
						//fin se obtiene de nuevo de host
						
						copiaPrograma.setComentAccionariado(datosbas.getComentAccionariado());
						copiaPrograma.setComentPartiSignificativa(datosbas.getComentAccionariado());
						copiaPrograma.setComentRatinExterno(datosbas.getComentAccionariado());
						copiaPrograma.setComentvaloraGlobal(datosbas.getComentAccionariado());

			}
			
			List<ReporteCredito> olistaReporteCredito=reporteCreditoBO.getListaReporteCredito(oprogramaPrime,codEmpresaPrime);
			if (olistaReporteCredito!=null && olistaReporteCredito.size()>0){
				ReporteCredito orepCredito = olistaReporteCredito.get(0);
				copiaPrograma.setCuentaCorriente(orepCredito.getCuentaCorriente());		
				//copiaPrograma.setDniCarnetExt(programa.getDniCarnetExt());		
				//copiaPrograma.setRucRDC(programa.getRucRDC());	
				copiaPrograma.setCiiuRDC(orepCredito.getCiiuRDC());
				//revisar copiaPrograma.setGestor(orepCredito.getGestor());
				//revisar copiaPrograma.setSegmento(orepCredito.getSegmento());	
				//copiaPrograma.setIdsegmento(programa.getIdsegmento());	
				copiaPrograma.setFechaRDC(orepCredito.getFechaRDC());
				//revisar copiaPrograma.setOficina(orepCredito.getOficina());
				//copiaPrograma.setIdoficina(programa.getIdoficina());
				copiaPrograma.setNumeroRVGL(orepCredito.getNumeroRVGL());
				copiaPrograma.setSalem(orepCredito.getSalem());
				//copiaPrograma.setPosicionClienteGrupo(programa.getPosicionClienteGrupo());
				copiaPrograma.setVulnerabilidad(orepCredito.getVulnerabilidad());	
				copiaPrograma.setTotalInversion(orepCredito.getTotalInversion());
				copiaPrograma.setMontoPrestamo(orepCredito.getMontoPrestamo());
				copiaPrograma.setEntorno(orepCredito.getEntorno());
				copiaPrograma.setPoblacionAfectada(orepCredito.getPoblacionAfectada());
				copiaPrograma.setCategorizacionAmbiental(orepCredito.getCategorizacionAmbiental());
				copiaPrograma.setComentarioAdmision(orepCredito.getComentarioAdmision());				
			}
			
			DatosBasicoBlob odatosBasicoBlob= datosBasicosBlobBO.findDatosBasicoBlobByPrograma(oprogramaPrime,codEmpresaPrime);
			if(odatosBasicoBlob != null)
			{		
				copiaProgramaBlob = new ProgramaBlob();
				copiaProgramaBlob.setId(null);
				copiaProgramaBlob.setPrograma(null);

				copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
				copiaProgramaBlob.setFechaCreacion(new Date());
				copiaProgramaBlob.setCodUsuarioModificacion(null);
				copiaProgramaBlob.setFechaModificacion(null);
				
				copiaProgramaBlob.setSintesisEmpresa( odatosBasicoBlob.getSintesisEmpresa()); //DB_SINTESIS_EMPR	byte[]
				copiaProgramaBlob.setDatosMatriz(odatosBasicoBlob.getDatosMatriz()); //DB_DATOS_MATR	byte[]
				copiaProgramaBlob.setEspacioLibre(odatosBasicoBlob.getEspacioLibre()); //DB_ESPACIO_LIBRE	byte[]
				copiaProgramaBlob.setComenComprasVentas(odatosBasicoBlob.getComenComprasVentas()); //DB_ESPACIO_LIBRE	byte[]
				copiaProgramaBlob.setConcentracion(odatosBasicoBlob.getConcentracion()); //DB_CONCENTRACION byte[]

				copiaProgramaBlob.setDatosBasicosaddRDC(odatosBasicoBlob.getDatosBasicosaddRDC());//RDC_COMEN_DATOSBASICOS_ADD	BLOB	Y			
				copiaProgramaBlob.setValoracion(odatosBasicoBlob.getValoracion()); //DB_VALORACION byte[]
			}

			if (isGrabar){					
				SintesisEconomicoBlob osintesisEconomicoBlob= sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(oprogramaPrime,codEmpresaPrime);
				RatingBlob oRatingBlob= ratingBlobBO.findRatingBlobByPrograma(oprogramaPrime,codEmpresaPrime);
				
				if ( osintesisEconomicoBlob!=null || oRatingBlob!=null){
					if (copiaProgramaBlob==null){
						copiaProgramaBlob = new ProgramaBlob();
						copiaProgramaBlob.setId(null);
						copiaProgramaBlob.setPrograma(null);
		
						copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
						copiaProgramaBlob.setFechaCreacion(new Date());
						copiaProgramaBlob.setCodUsuarioModificacion(null);
						copiaProgramaBlob.setFechaModificacion(null);
					}	
				}			
							
				if(osintesisEconomicoBlob != null)
				{	
					copiaProgramaBlob.setComenSituFinanciera(osintesisEconomicoBlob.getComenSituFinanciera()); //SF_COMENTARIO_SITU_FINA	byte[]
					copiaProgramaBlob.setComenSituEconomica(osintesisEconomicoBlob.getComenSituEconomica()); //SF_COMENTARIO_SITU_ECON	byte[]
					copiaProgramaBlob.setValoracionEconFinanciera(osintesisEconomicoBlob.getValoracionEconFinanciera()); //SF_VALORACION_ECON_FINA	byte[]
					copiaProgramaBlob.setValoracionPosiBalance(osintesisEconomicoBlob.getValoracionPosiBalance()); // SF_VALORACION_POSI_BALA	byte[]	
					copiaProgramaBlob.setComenSintesisEconFinaddRDC(osintesisEconomicoBlob.getComenSintesisEconFinaddRDC());//RDC_COMEN_SINT_ECONFINAN_ADD	BLOB	Y	
				}
				if(oRatingBlob != null)
				{
					copiaProgramaBlob.setValoracionRating(oRatingBlob.getValoracionRating()); //RA_VALORACION_RATI	byte[]				
				}
			}
			if (isSaveBlobInfGrupo){						
				ProgramaBlob programaBlobGrupo = programaBlobBO.findBlobByPrograma(oprogramaIni);
				if(programaBlobGrupo != null){
					//SINTESIS ECONOMICO
					copiaProgramaBlob.setComenSituFinanciera(programaBlobGrupo.getComenSituFinanciera()); //SF_COMENTARIO_SITU_FINA	byte[]
					copiaProgramaBlob.setComenSituEconomica(programaBlobGrupo.getComenSituEconomica()); //SF_COMENTARIO_SITU_ECON	byte[]
					copiaProgramaBlob.setValoracionEconFinanciera(programaBlobGrupo.getValoracionEconFinanciera()); //SF_VALORACION_ECON_FINA	byte[]
					copiaProgramaBlob.setValoracionPosiBalance(programaBlobGrupo.getValoracionPosiBalance()); // SF_VALORACION_POSI_BALA	byte[]	
					//RATING
					copiaProgramaBlob.setValoracionRating(programaBlobGrupo.getValoracionRating()); //RA_VALORACION_RATI	byte[]
					
					//ANALISIS SECTORIAL                                                                  
					copiaProgramaBlob.setEspacioLibreAS(programaBlobGrupo.getEspacioLibreAS()); 					//AS_ESPACIO_LIBRE	byte[]              
					                                                                                      
					//RELACIONES BANCARIAS                                                                
					copiaProgramaBlob.setComenLineas(programaBlobGrupo.getComenLineas());							//RB_COMENTARIOS_LINEAS	byte[]          
					copiaProgramaBlob.setRentaModelGlobal(programaBlobGrupo.getRentaModelGlobal());  			//RB_RENTAB_MODEL_GLOB	byte[]          
					copiaProgramaBlob.setRentaModelBEC(programaBlobGrupo.getRentaModelBEC()); 					//RB_RENTAB_MODEL_GLOB	byte[]          
					copiaProgramaBlob.setCampoLibreRB(programaBlobGrupo.getCampoLibreRB()); 						//RB_CAMPO_LIBR	byte[]                  
					copiaProgramaBlob.setComenPoolBanc(programaBlobGrupo.getComenPoolBanc()); 					//RB_COMENTARIO_POOL_BANC byte[]        
					copiaProgramaBlob.setCampoLibreAnexos(programaBlobGrupo.getCampoLibreAnexos());				//RB_CAMPO_LIBRE_ANEXO                  
					copiaProgramaBlob.setComentIndTransaccional(programaBlobGrupo.getComentIndTransaccional()) ;	//RB_COMEN_INDTRANSA                    
					                                                                                      
					//FACTORES RIESGO                                                                     
					copiaProgramaBlob.setFodaFotalezas(programaBlobGrupo.getFodaFotalezas()); 					//FR_FODA_FORTA	byte[]                  
					copiaProgramaBlob.setFodaOportunidades(programaBlobGrupo.getFodaOportunidades()); 			//FR_FODA_OPOR	byte[]                  
					copiaProgramaBlob.setFodaDebilidades(programaBlobGrupo.getFodaDebilidades()); 				//FR_FODA_DEBI	byte[]                  
					copiaProgramaBlob.setFodaAmenazas(programaBlobGrupo.getFodaAmenazas()); 						//FR_FODA_AMEN	byte[]                  
					copiaProgramaBlob.setConclucionFoda(programaBlobGrupo.getConclucionFoda()); 					//FR_CONCLUSION_FODA	byte[]            
						                                                                
					//PROPUESTA DE RIESGO                                                                 
					copiaProgramaBlob.setCampoLibrePR(programaBlobGrupo.getCampoLibrePR()); 							//PR_CAMPO_LIBR	byte[]                
					copiaProgramaBlob.setEstructuraLimite(programaBlobGrupo.getEstructuraLimite()); 					//PR_ESTR_LIMIT	byte[]                
					copiaProgramaBlob.setConsideracionPR(programaBlobGrupo.getConsideracionPR()); 					//PR_CONSIDERACION	byte[]            
					                                                                                      
					//POLITICAS DE RIESGO                                                                 
					copiaProgramaBlob.setDetalleOperacionGarantia(programaBlobGrupo.getDetalleOperacionGarantia());  //PRG_DET_OPERACI_GARANT	byte[]      
					copiaProgramaBlob.setRiesgoTesoreria(programaBlobGrupo.getRiesgoTesoreria()); 				  //PRG_RIESGO_TESO	byte[]              
					copiaProgramaBlob.setPoliticasRiesGrupo(programaBlobGrupo.getPoliticasRiesGrupo()); 				//PRG_POLITICAS_RIES_GRUP	byte[]      
					copiaProgramaBlob.setPoliticasDelegacion(programaBlobGrupo.getPoliticasDelegacion()); 			//PRG_POLITICAS_DELE	byte[]          

					
				}						
			}
			
			return copiaProgramaBlob;
			
		} catch (Exception e) {			
			logger.error(StringUtil.getStackTrace(e));
			return null;
		}
		
	}
	
	//ini copia pf original
	public String copiarPrograma_ORIGEN() throws BOException
	{
		List<Empresa> listaEmpresas = null;
		String forward="modificarPrograma";
		String idtipoCopia=getTipoCopiapf();
		String copiarSinData=getCopiarsinDataHidden();
		//System.out.println("idtipoCopia:" +idtipoCopia);
		//System.out.println("copiasindata" + copiarSinData);
		
		try
		{
			
			logger.info(programa);
			String nombregrupoEmpresaini=programa.getNombreGrupoEmpresa()==null?"":programa.getNombreGrupoEmpresa();
			this.programa = this.programaBO.findById(this.programa.getId());
			logger.info(programa);
			logger.info(programa.getEstadoPrograma());
			logger.info(programa.getEstadoPrograma().getId());
			
			
			logger.info("getTipoEmpresa():"+getTipoEmpresa());
			logger.info("getTipoPrograma():"+getTipoPrograma());			
			logger.info("getCodigoEmpresaGrupo():"+getCodigoEmpresaGrupo());
			
			
			 //ID_TIPO_COPIA_IaI="1";
			 //ID_TIPO_COPIA_IaG="2";	 
			 //ID_TIPO_COPIA_GaI="3";
			 //ID_TIPO_COPIA_GaG="4";
							
			
			
			if(this.programa.getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_CERRADO))
			{
				ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(this.programa);
							
				boolean existePrincipal = false;
				
				Programa copiaPrograma = new Programa();
				
				//nueva valores
				Tabla objTipoPrograma = new Tabla();
				objTipoPrograma.setId(Long.valueOf(getTipoPrograma()));
				Tabla objTipoEmpresa = new Tabla();
				objTipoEmpresa.setId(Long.valueOf(getTipoEmpresa()));
				
				copiaPrograma.setTipoEmpresa(objTipoEmpresa);
				copiaPrograma.setTipoPrograma(objTipoPrograma);			
				
				if (!copiarSinData.equals(Constantes.COD_COPIA_SINDATA)){ 
					copiaPrograma.setActividadPrincipal(this.programa.getActividadPrincipal());
					copiaPrograma.setPais(this.programa.getPais());
					copiaPrograma.setAntiguedadNegocio(this.programa.getAntiguedadNegocio());
					copiaPrograma.setAntiguedadCliente(this.programa.getAntiguedadCliente());
				}
				copiaPrograma.setNombreGrupoEmpresa(nombregrupoEmpresaini);
				copiaPrograma.setAnio(this.programa.getAnio());				
				
				Tabla estadoPrograma = new Tabla();
				estadoPrograma.setId(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE);
				copiaPrograma.setEstadoPrograma(estadoPrograma);
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);			
				copiaPrograma.setCodUsuarioCreacion(user.getRegistroHost());
				copiaPrograma.setFechaCreacion(new Date());
				
				ProgramaBlob copiaProgramaBlob = null;
				if (!copiarSinData.equals(Constantes.COD_COPIA_SINDATA)){
					//Datos Basicos
					copiaPrograma.setComentAccionariado(programa.getComentAccionariado());				
					copiaPrograma.setComentPartiSignificativa(programa.getComentPartiSignificativa());
					copiaPrograma.setComentRatinExterno(programa.getComentRatinExterno());
					copiaPrograma.setComentvaloraGlobal(programa.getComentvaloraGlobal());				
					
					//Relaciones Bancarias
					copiaPrograma.setComentcuotaFinanciera(programa.getComentcuotaFinanciera());
					
					//Propuesta de Riesgo
					copiaPrograma.setTipoEstructura(programa.getTipoEstructura());							
					
					//Politicas de Riesgo
					copiaPrograma.setLimiteAutorizadoPRG(programa.getLimiteAutorizadoPRG());
					copiaPrograma.setProximaRevisionPRG(programa.getProximaRevisionPRG());
					copiaPrograma.setMotivoProximaPRG(programa.getMotivoProximaPRG());					
					
					copiaPrograma.setTipoMiles(programa.getTipoMiles());
					copiaPrograma.setTipoMilesPLR(programa.getTipoMilesPLR());
					copiaPrograma.setTipoMilesRB(programa.getTipoMilesRB());
					
					
					//Reporte Credito
					//copiaPrograma.setNumeroSolicitud("");	
					copiaPrograma.setCuentaCorriente(programa.getCuentaCorriente());		
					//copiaPrograma.setDniCarnetExt(programa.getDniCarnetExt());		
					//copiaPrograma.setRucRDC(programa.getRucRDC());	
					copiaPrograma.setCiiuRDC(programa.getCiiuRDC());
					copiaPrograma.setGestor(programa.getGestor());
					copiaPrograma.setSegmento(programa.getSegmento());	
					//copiaPrograma.setIdsegmento(programa.getIdsegmento());	
					copiaPrograma.setFechaRDC(programa.getFechaRDC());
					copiaPrograma.setOficina(programa.getOficina());
					//copiaPrograma.setIdoficina(programa.getIdoficina());
					copiaPrograma.setNumeroRVGL(programa.getNumeroRVGL());
					copiaPrograma.setSalem(programa.getSalem());
					//copiaPrograma.setPosicionClienteGrupo(programa.getPosicionClienteGrupo());
					copiaPrograma.setVulnerabilidad(programa.getVulnerabilidad());	
					copiaPrograma.setTotalInversion(programa.getTotalInversion());
					copiaPrograma.setMontoPrestamo(programa.getMontoPrestamo());
					copiaPrograma.setEntorno(programa.getEntorno());
					copiaPrograma.setPoblacionAfectada(programa.getPoblacionAfectada());
					copiaPrograma.setCategorizacionAmbiental(programa.getCategorizacionAmbiental());
					copiaPrograma.setComentarioAdmision(programa.getComentarioAdmision());
					
					//copiaPrograma.setContrato(programa.getContrato());
											
					if(programaBlob != null)
					{							
						copiaProgramaBlob = new ProgramaBlob();
						copiaProgramaBlob=programaBlob;
						copiaProgramaBlob.setId(null);
						copiaProgramaBlob.setPrograma(null);
						copiaProgramaBlob.setCodUsuarioCreacion(user.getRegistroHost());
						copiaProgramaBlob.setFechaCreacion(new Date());
						copiaProgramaBlob.setCodUsuarioModificacion(null);
						copiaProgramaBlob.setFechaModificacion(null);	
					}				
				}
			
			
				//ini mcg
				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					//'2':'RUC','3':'Código Central'
					if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
						copiaPrograma.setIdEmpresa(getCodigoEmpresaGrupo());
					}else{
						copiaPrograma.setRuc(getCodigoEmpresaGrupo());
					}
					
				}else{
						if (getTipoCopiapf().equals(Constantes.ID_TIPO_COPIA_GaI)){		//3								
								Tabla oTipoEmpresa = new Tabla();
								oTipoEmpresa.setId(Constantes.ID_TIPO_EMPRESA_EMPR);							
								copiaPrograma.setTipoEmpresa(oTipoEmpresa);							
								
								List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
								if(empresaPrincipal!=null){
									empresaPrincipal = findEmpresaGrupo(listaTemp, empresaPrincipal.getCodigo());
									copiaPrograma.setIdEmpresa(empresaPrincipal.getCodigo());
									copiaPrograma.setNombreGrupoEmpresa(empresaPrincipal.getNombre());								
									copiaPrograma.setRuc(empresaPrincipal.getRuc());
								}						
							
						}else{
							//ini grupo
							listaEmpresas = new ArrayList<Empresa>();
							copiaPrograma.setIdGrupo(getCodigoEmpresaGrupo());
							Tabla tipoGrupo = null;// EMPRESA PRINCIPAL, SECUNDARIA O ANEXA
							List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
							if(empresaPrincipal!=null){
								empresaPrincipal = findEmpresaGrupo(listaTemp, empresaPrincipal.getCodigo());
								
								existePrincipal = true;						
								tipoGrupo = new Tabla();
								tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
								empresaPrincipal.setTipoGrupo(tipoGrupo);
								listaEmpresas.add(empresaPrincipal);
								//se registra el ruc de la empresa principal
								copiaPrograma.setRuc(empresaPrincipal.getRuc());
							}
							if(getRequest().getParameterValues("selListaEmpresasSecundarias")!= null){
								for(String ids: getRequest().getParameterValues("selListaEmpresasSecundarias")){
									Empresa empresa =null;
									empresa = findEmpresaGrupo(listaTemp, ids);
									tipoGrupo = new Tabla();
									tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
									empresa.setTipoGrupo(tipoGrupo);
									listaEmpresas.add(empresa);
								}
							}
							if(getRequest().getParameterValues("selListaEmpresasAnexas") != null){
								for(String ids: getRequest().getParameterValues("selListaEmpresasAnexas")){
									Empresa empresa =null;
									empresa = findEmpresaGrupo(listaTemp, ids);
									tipoGrupo = new Tabla();
									tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_ANEXA);
									empresa.setTipoGrupo(tipoGrupo);
									listaEmpresas.add(empresa);
								}
							}
							programaBO.setListaGrupoEmpresas(listaEmpresas);
							
							//fin grupo
						}
					
				}
				//fin mcg
				
				
				programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));	
				String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
				programaBO.setPathWebService(pathWebService);
				programaBO.setConfiguracionWS(getConfiguracionWS());
				programaBO.save(copiaPrograma);
								
				if(copiaProgramaBlob != null)
				{									
					programaBlobBO.setPrograma(copiaPrograma);
					programaBlobBO.save(copiaProgramaBlob);
					
				}
				
				if (!copiarSinData.equals(Constantes.COD_COPIA_SINDATA)){
					copiaDatosBasicos(programa.getId(), copiaPrograma,programa.getAnio(),listaEmpresas,null);
					copiarSintesisEconomica(programa.getId(),copiaPrograma,listaEmpresas,null);
					copiarRating(programa.getId(),copiaPrograma,listaEmpresas,null);
					copiarAnalisisSectorial(programa.getId(),copiaPrograma,null);
					copiarRelacionesBancarias(programa.getId(),copiaPrograma,null);
					copiarPropuestaRiesgo(programa.getId(),copiaPrograma,listaEmpresas,null);
					copiarPoliticasRiesgo(programa.getId(),copiaPrograma,null);
					copiarAnexo(programa.getId(),copiaPrograma,listaEmpresas,null);
					copiaReporteCredito(programa.getId(),copiaPrograma,listaEmpresas,null);			
				}
	
				Map<String, Object> sessionparam = ActionContext.getContext().getSession();
				sessionparam.put(Constantes.ID_PROGRAMA_SESSION, copiaPrograma.getId().toString());
				
				//ini MCG20121030
				sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, copiaPrograma.getNumeroSolicitud()==null?"":copiaPrograma.getNumeroSolicitud().toString());
				//fin MCG20121030
				
				//si es Local o Corporativo
				sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, copiaPrograma.getTipoPrograma().getId());
				//si es empresa o grupo
				sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, copiaPrograma.getTipoEmpresa().getId().toString());
				//en el caso de grupos se sube a session los datos de la empresa 
				//principal en los datos de la empresa 
				//MCG 20120430 add getId
				if(copiaPrograma.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, copiaPrograma.getRuc());
					sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, copiaPrograma.getIdEmpresa());
					sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, copiaPrograma.getIdEmpresa());
					if(existePrincipal)
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
				}else{
					sessionparam.put(Constantes.COD_GRUPO_SESSION, copiaPrograma.getIdGrupo());
					sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaEmpresas);
					if(existePrincipal)
					{
						sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
						sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
						sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
					}
				}
				sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, copiaPrograma.getNombreGrupoEmpresa());
				sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, copiaPrograma.getAnio());
							
			}
		
		} catch (BOException e) {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupo";
			e.printStackTrace();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));	
		}catch(Exception ex)	{	
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupo";
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		
		return forward;
		
	}
	//fin copia pf original
	
	
	
	
	public void copiaDatosBasicos(Long idprogramaini,Programa copiaPrograma,int annioini){
				
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);	
		int anio=copiaPrograma.getAnio();
		List<Planilla> oListaPlanilla =null;		
	
		try {			
						
			datosBasicosBO.setPrograma(copiaPrograma);			
			oListaPlanilla = new ArrayList<Planilla>();
			oListaPlanilla= datosBasicosBO.getListaPlanilla(programaini);
			if (annioini<anio){
				for(Planilla oplanilla :oListaPlanilla){
					oplanilla.setTotal1(oplanilla.getTotal2());
					oplanilla.setTotal2(oplanilla.getTotal3());
					oplanilla.setTotal3(oplanilla.getTotal4());
					oplanilla.setTotal4(null);
				}
				
			}
			for(Planilla pl : oListaPlanilla){
				if(pl.getTipoPersonal()!= null && 
				   pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_ADMINISTRATIVO)){					
					pl.setId(null);					
					datosBasicosBO.setPlanillaAdmin(pl);
				}else if(pl.getTipoPersonal()!= null && 
						pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO)){
					
					pl.setId(null);
					datosBasicosBO.setPlanillaNoAdmin(pl);
				}else{					
					pl.setId(null);
					datosBasicosBO.setTotalPlanilla(pl);	
				}
			}		
			
			List<Accionista> listaAccionistaCopia=new ArrayList<Accionista>();
			List<Accionista> olistaAccionistas=datosBasicosBO.getListaAccionistas(programaini);
			if (olistaAccionistas!=null && olistaAccionistas.size()>0){
				for (Accionista oaccionista: olistaAccionistas){
					oaccionista.setId(null);
					oaccionista.setPrograma(null);
					listaAccionistaCopia.add(oaccionista);
				}				
			}			
			datosBasicosBO.setListaAccionistas( listaAccionistaCopia);
			
								
			datosBasicosBO.setListaCabTablaPlanilla(LoadCabeceraPlanilla(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaCompra(LoadCabeceraCompra(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaVenta(LoadCabeceraVenta(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaActividad(LoadCabeceraCuadraNBActividad(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaNegocio(LoadCabeceraCuadraNBNegocio(idprogramaini,anio,annioini));			
		
		
			List<PrincipalesEjecutivos> listaPrincipalesEjecutivosCopia=new ArrayList<PrincipalesEjecutivos>();
			List<PrincipalesEjecutivos> olistaPrincipalesEjecutivos=datosBasicosBO.getListaPrinciEjecutivos(programaini);
			if (olistaPrincipalesEjecutivos!=null && olistaPrincipalesEjecutivos.size()>0){
				for (PrincipalesEjecutivos oprincipalesEjecutivos: olistaPrincipalesEjecutivos){
					oprincipalesEjecutivos.setId(null);
					oprincipalesEjecutivos.setPrograma(null);
					listaPrincipalesEjecutivosCopia.add(oprincipalesEjecutivos);
				}				
			}
			datosBasicosBO.setListaPrinciEjecutivos(listaPrincipalesEjecutivosCopia);
			
			
			List<RatingExterno> listaRatingExternoCopia=new ArrayList<RatingExterno>();
			List<RatingExterno> olistaRatingExterno=datosBasicosBO.getListaRatingExterno(programaini);
			if (olistaRatingExterno!=null && olistaRatingExterno.size()>0){
				for (RatingExterno oratingExterno: olistaRatingExterno){
					oratingExterno.setId(null);	
					oratingExterno.setPrograma(null);
					listaRatingExternoCopia.add(oratingExterno);
				}				
			}			
			datosBasicosBO.setListaRatingExterno(listaRatingExternoCopia);
			//completando datos de negocio beneficio
			
			List<NegocioBeneficio> listaNBActividadesCopia=new ArrayList<NegocioBeneficio>();
			List<NegocioBeneficio> olistaNBActividades=datosBasicosBO.getListaNBActividades(programaini);
			if (olistaNBActividades!=null && olistaNBActividades.size()>0){
				if (annioini<anio){
					for(NegocioBeneficio oNBActividades:olistaNBActividades){
						oNBActividades.setTotalI1(oNBActividades.getTotalI2());
						oNBActividades.setTotalI2(oNBActividades.getTotalI3());
						oNBActividades.setTotalI3(null);
						oNBActividades.setTotalB1(oNBActividades.getTotalB2());
						oNBActividades.setTotalB2(oNBActividades.getTotalB3());
						oNBActividades.setTotalB3(null);
						
					}					
				}					
				for (NegocioBeneficio oNBActividades: olistaNBActividades){
					oNBActividades.setId(null);	
					oNBActividades.setPrograma(null);
					listaNBActividadesCopia.add(oNBActividades);
				}				
			}
			datosBasicosBO.setListaNBActividades(listaNBActividadesCopia);
			
			List<NegocioBeneficio> listaNBNegocioCopia=new ArrayList<NegocioBeneficio>();
			List<NegocioBeneficio> olistaNBNegocio=datosBasicosBO.getListaNBNegocio(programaini);
			if (olistaNBNegocio!=null && olistaNBNegocio.size()>0){
				
				if (annioini<anio){
					for(NegocioBeneficio oNBNegocio:olistaNBNegocio){
						oNBNegocio.setTotalI1(oNBNegocio.getTotalI2());
						oNBNegocio.setTotalI2(oNBNegocio.getTotalI3());
						oNBNegocio.setTotalI3(null);
						oNBNegocio.setTotalB1(oNBNegocio.getTotalB2());
						oNBNegocio.setTotalB2(oNBNegocio.getTotalB3());
						oNBNegocio.setTotalB3(null);						
					}					
				}				
				
				for (NegocioBeneficio oNBNegocio: olistaNBNegocio){
					oNBNegocio.setId(null);	
					oNBNegocio.setPrograma(null);
					listaNBNegocioCopia.add(oNBNegocio);
				}				
			}
			datosBasicosBO.setListaNBNegocio(listaNBNegocioCopia);
			
			List<Participaciones> listaParticipacionesCopia=new ArrayList<Participaciones>();
			List<Participaciones> olistaParticipaciones=datosBasicosBO.getListaParticipaciones(programaini);
			if (olistaParticipaciones!=null && olistaParticipaciones.size()>0){
				for (Participaciones oparticipaciones: olistaParticipaciones){
					oparticipaciones.setId(null);
					oparticipaciones.setPrograma(null);
					listaParticipacionesCopia.add(oparticipaciones);
				}				
			}
			datosBasicosBO.setListaParticipaciones(listaParticipacionesCopia);
			//completando datos de compra y venta
			
			List<CompraVenta> listaCompraVentaCopia=new ArrayList<CompraVenta>();
			List<CompraVenta> olistaCompraVenta=datosBasicosBO.getListaCompraVenta(programaini);
			if (olistaCompraVenta!=null && olistaCompraVenta.size()>0){	
				if (annioini<anio){
					for(CompraVenta ocompraVenta:olistaCompraVenta){
						ocompraVenta.setTotal1(ocompraVenta.getTotal2());
						ocompraVenta.setTotal2(ocompraVenta.getTotal3());
						ocompraVenta.setTotal3(null);						
					}					
				}	
				
				for (CompraVenta ocompraVenta: olistaCompraVenta){
					ocompraVenta.setId(null);
					ocompraVenta.setPrograma(null);
					listaCompraVentaCopia.add(ocompraVenta);
				}				
			}
			datosBasicosBO.setListaCompraVenta(listaCompraVentaCopia);
			datosBasicosBO.updateDatosBasicos();
			//setPrograma(programa);
			//addActionMessage("Datos Actualizados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}		
	}
	
	//INI MCG20130221
	

	public void copiaDatosBasicos(Long idprogramaini,Programa copiaPrograma,int annioini,List<Empresa> listaEmpresas,Long idprogramaPrime){
		
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);	
		int anio=copiaPrograma.getAnio();
		List<Planilla> oListaPlanilla =null;
		
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();
		
		try {			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			String tipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			//para tipo empresa inclue la empresa para tipo grupo inclue las empresas que pertenecen al grupo
			listaEmpresasGrupofin=listaEmpresas;					
			
			datosBasicosBO.setPrograma(copiaPrograma);	
			//se asigna los valores de la cabecera del programa mother
			datosBasicosBO.setListaCabTablaPlanilla(LoadCabeceraPlanilla(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaCompra(LoadCabeceraCompra(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaVenta(LoadCabeceraVenta(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaActividad(LoadCabeceraCuadraNBActividad(idprogramaini,anio,annioini));
			datosBasicosBO.setListaCabTablaNegocio(LoadCabeceraCuadraNBNegocio(idprogramaini,anio,annioini));			
			
						
			for(Empresa oempresa :listaEmpresasGrupofin){
				String codigoEmpresafin=oempresa.getCodigo();
				String idProgramafin =oempresa.getIdProgramaCopia();
				if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
					continue;
				}				
				Programa oprogramafin=new Programa();
				oprogramafin.setId(Long.valueOf(idProgramafin));
				
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					
					if (oempresa!=null && ( oempresa.getTipoGrupo()!=null && !oempresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)) ){							
						List<DatosBasico> olistaDatosBasicos = new ArrayList<DatosBasico>();					
						olistaDatosBasicos=datosBasicosBO.getListaDatosBasico(oprogramafin,codigoEmpresafin);
							if (olistaDatosBasicos!=null && olistaDatosBasicos.size()>0){
										DatosBasico datosbas = olistaDatosBasicos.get(0);
										copiaPrograma.setActividadPrincipal(datosbas.getActividadPrincipal());
//										copiaPrograma.setPais(datosbas.getPais());
//										copiaPrograma.setAntiguedadNegocio(datosbas.getAntiguedadNegocio());
//										copiaPrograma.setAntiguedadCliente(datosbas.getAntiguedadCliente());
//										copiaPrograma.setGrupoRiesgoBuro(datosbas.getGrupoRiesgoBuro());
										
										copiaPrograma.setComentAccionariado(datosbas.getComentAccionariado());
										copiaPrograma.setComentPartiSignificativa(datosbas.getComentAccionariado());
										copiaPrograma.setComentRatinExterno(datosbas.getComentAccionariado());
										copiaPrograma.setComentvaloraGlobal(datosbas.getComentAccionariado());
									
								 datosBasicosBO.setPrograma(copiaPrograma);
							}						
					}	
				}
				   					
					oListaPlanilla = new ArrayList<Planilla>();
					oListaPlanilla= datosBasicosBO.getListaPlanilla(oprogramafin,codigoEmpresafin);			
					if (oListaPlanilla!=null && oListaPlanilla.size()>0){			
							if (annioini<anio){
								for(Planilla oplanilla :oListaPlanilla){
									oplanilla.setTotal1(oplanilla.getTotal2());
									oplanilla.setTotal2(oplanilla.getTotal3());
									oplanilla.setTotal3(oplanilla.getTotal4());
									oplanilla.setTotal4(null);
								}
								
							}
							for(Planilla pl : oListaPlanilla){						
									if(pl.getTipoPersonal()!= null && 
									   pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_ADMINISTRATIVO)){					
										pl.setId(null);					
										datosBasicosBO.setPlanillaAdmin(pl);
									}else if(pl.getTipoPersonal()!= null && 
											pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO)){
										
										pl.setId(null);
										datosBasicosBO.setPlanillaNoAdmin(pl);
									}else{					
										pl.setId(null);
										datosBasicosBO.setTotalPlanilla(pl);	
									}							
							}	
					
					}
					
					List<Accionista> listaAccionistaCopia=new ArrayList<Accionista>();
					List<Accionista> olistaAccionistas=datosBasicosBO.getListaAccionistas(oprogramafin,codigoEmpresafin);
					if (olistaAccionistas!=null && olistaAccionistas.size()>0){
						for (Accionista oaccionista: olistaAccionistas){								
								oaccionista.setId(null);
								oaccionista.setPrograma(null);
								//para soportar null de tipo documento
								if (oaccionista.getTipoDocumento()!=null && oaccionista.getTipoDocumento().getId()==null){		
									oaccionista.setTipoDocumento(null);
								}
								listaAccionistaCopia.add(oaccionista);
							 
						}				
					}			
					datosBasicosBO.setListaAccionistas( listaAccionistaCopia);
					
					//ini MCG20130822
					List<CapitalizacionBursatil> listaCapitalizacionCopia = new ArrayList<CapitalizacionBursatil>();
					
					List<CapitalizacionBursatil> olistaCapitalizacion = datosBasicosBO.getListaCapitalizacionBursatil(oprogramafin,codigoEmpresafin);
					
					if (olistaCapitalizacion!=null && olistaCapitalizacion.size()>0){
						for (CapitalizacionBursatil ocapitalizacionBursatil: olistaCapitalizacion){								
								ocapitalizacionBursatil.setId(null);
								ocapitalizacionBursatil.setPrograma(null);
								//para soportar  null de divisa
								if (ocapitalizacionBursatil.getDivisa()!=null && ocapitalizacionBursatil.getDivisa().getId()==null){		
									ocapitalizacionBursatil.setDivisa(null);
								}
								listaCapitalizacionCopia.add(ocapitalizacionBursatil);
							 
						}				
					}			
					datosBasicosBO.setListaCapitalizacion( listaCapitalizacionCopia);
					
					//fin MCG20130822
					
		
				
					List<PrincipalesEjecutivos> listaPrincipalesEjecutivosCopia=new ArrayList<PrincipalesEjecutivos>();
					List<PrincipalesEjecutivos> olistaPrincipalesEjecutivos=datosBasicosBO.getListaPrinciEjecutivos(oprogramafin,codigoEmpresafin);
					if (olistaPrincipalesEjecutivos!=null && olistaPrincipalesEjecutivos.size()>0){
						for (PrincipalesEjecutivos oprincipalesEjecutivos: olistaPrincipalesEjecutivos){
								oprincipalesEjecutivos.setId(null);
								oprincipalesEjecutivos.setPrograma(null);
								listaPrincipalesEjecutivosCopia.add(oprincipalesEjecutivos);
							
						}				
					}
					datosBasicosBO.setListaPrinciEjecutivos(listaPrincipalesEjecutivosCopia);
					
					
					List<RatingExterno> listaRatingExternoCopia=new ArrayList<RatingExterno>();
					List<RatingExterno> olistaRatingExterno=datosBasicosBO.getListaRatingExterno(oprogramafin,codigoEmpresafin);
					if (olistaRatingExterno!=null && olistaRatingExterno.size()>0){
						for (RatingExterno oratingExterno: olistaRatingExterno){
								oratingExterno.setId(null);	
								oratingExterno.setPrograma(null);
								listaRatingExternoCopia.add(oratingExterno);
							
						}				
					}			
					datosBasicosBO.setListaRatingExterno(listaRatingExternoCopia);
					//completando datos de negocio beneficio
					
					List<NegocioBeneficio> listaNBActividadesCopia=new ArrayList<NegocioBeneficio>();
					List<NegocioBeneficio> olistaNBActividades=datosBasicosBO.getListaNBActividades(oprogramafin,codigoEmpresafin);
					if (olistaNBActividades!=null && olistaNBActividades.size()>0){
						if (annioini<anio){
							for(NegocioBeneficio oNBActividades:olistaNBActividades){	
								oNBActividades.setTotalI1(oNBActividades.getTotalI2());
								oNBActividades.setTotalI2(oNBActividades.getTotalI3());
								oNBActividades.setTotalI3(null);
								oNBActividades.setTotalB1(oNBActividades.getTotalB2());
								oNBActividades.setTotalB2(oNBActividades.getTotalB3());
								oNBActividades.setTotalB3(null);						
							}					
						}					
						for (NegocioBeneficio oNBActividades: olistaNBActividades){							
								oNBActividades.setId(null);	
								oNBActividades.setPrograma(null);
								listaNBActividadesCopia.add(oNBActividades);
							
						}				
					}
					datosBasicosBO.setListaNBActividades(listaNBActividadesCopia);
					
					List<NegocioBeneficio> listaNBNegocioCopia=new ArrayList<NegocioBeneficio>();
					List<NegocioBeneficio> olistaNBNegocio=datosBasicosBO.getListaNBNegocio(oprogramafin,codigoEmpresafin);
					if (olistaNBNegocio!=null && olistaNBNegocio.size()>0){
						
						if (annioini<anio){
							for(NegocioBeneficio oNBNegocio:olistaNBNegocio){
								oNBNegocio.setTotalI1(oNBNegocio.getTotalI2());
								oNBNegocio.setTotalI2(oNBNegocio.getTotalI3());
								oNBNegocio.setTotalI3(null);
								oNBNegocio.setTotalB1(oNBNegocio.getTotalB2());
								oNBNegocio.setTotalB2(oNBNegocio.getTotalB3());
								oNBNegocio.setTotalB3(null);						
							}					
						}				
						
						for (NegocioBeneficio oNBNegocio: olistaNBNegocio){							
								oNBNegocio.setId(null);	
								oNBNegocio.setPrograma(null);
								listaNBNegocioCopia.add(oNBNegocio);
							
						}				
					}
					datosBasicosBO.setListaNBNegocio(listaNBNegocioCopia);
					
					List<Participaciones> listaParticipacionesCopia=new ArrayList<Participaciones>();
					List<Participaciones> olistaParticipaciones=datosBasicosBO.getListaParticipaciones(oprogramafin,codigoEmpresafin);
					if (olistaParticipaciones!=null && olistaParticipaciones.size()>0){
						for (Participaciones oparticipaciones: olistaParticipaciones){
								oparticipaciones.setId(null);
								oparticipaciones.setPrograma(null);
								listaParticipacionesCopia.add(oparticipaciones);
						}				
					}
					datosBasicosBO.setListaParticipaciones(listaParticipacionesCopia);
					//completando datos de compra y venta
					
					List<CompraVenta> listaCompraVentaCopia=new ArrayList<CompraVenta>();
					List<CompraVenta> olistaCompraVenta=datosBasicosBO.getListaCompraVenta(oprogramafin,codigoEmpresafin);
					if (olistaCompraVenta!=null && olistaCompraVenta.size()>0){	
						if (annioini<anio){
							for(CompraVenta ocompraVenta:olistaCompraVenta){
								ocompraVenta.setTotal1(ocompraVenta.getTotal2());
								ocompraVenta.setTotal2(ocompraVenta.getTotal3());
								ocompraVenta.setTotal3(null);						
							}					
						}	
						
						for (CompraVenta ocompraVenta: olistaCompraVenta){							
								ocompraVenta.setId(null);
								ocompraVenta.setPrograma(null);
								listaCompraVentaCopia.add(ocompraVenta);
							
						}				
					}
					datosBasicosBO.setListaCompraVenta(listaCompraVentaCopia);
					datosBasicosBO.updateDatosBasicos(codigoEmpresafin);
					
					if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
						
						if (oempresa!=null && ( oempresa.getTipoGrupo()!=null && !oempresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)) ){	
					
							DatosBasicoBlob odatosBasicoBlob= datosBasicosBlobBO.findDatosBasicoBlobByPrograma(oprogramafin,codigoEmpresafin);
							if (odatosBasicoBlob!=null){
								odatosBasicoBlob.setId(null);
								odatosBasicoBlob.setPrograma(copiaPrograma);
								odatosBasicoBlob.setFechaCreacion(new Date());
								odatosBasicoBlob.setCodUsuarioCreacion(ousurio.getRegistroHost());
								datosBasicosBlobBO.savecopia(odatosBasicoBlob);
							}else{
							
								Programa oprogramaByEmpresa = this.programaBO.findById(Long.valueOf(idProgramafin));
								String idcodEmpresa=oprogramaByEmpresa.getIdEmpresa();
								if (idcodEmpresa!=null && idcodEmpresa.equals(codigoEmpresafin)){
									ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(oprogramafin);					
									if(programaBlob != null)
									{
										DatosBasicoBlob odatosBasicoBlobPrin =new DatosBasicoBlob();
										odatosBasicoBlobPrin.setId(null);
										odatosBasicoBlobPrin.setPrograma(copiaPrograma);
										odatosBasicoBlobPrin.setCodigoEmpresa(codigoEmpresafin);
										odatosBasicoBlobPrin.setFechaCreacion(new Date());
										odatosBasicoBlobPrin.setCodUsuarioCreacion(ousurio.getRegistroHost());
										
										odatosBasicoBlobPrin.setSintesisEmpresa( programaBlob.getSintesisEmpresa()); //DB_SINTESIS_EMPR	byte[]
										odatosBasicoBlobPrin.setDatosMatriz(programaBlob.getDatosMatriz()); //DB_DATOS_MATR	byte[]
										odatosBasicoBlobPrin.setEspacioLibre(programaBlob.getEspacioLibre()); //DB_ESPACIO_LIBRE	byte[]
										odatosBasicoBlobPrin.setComenComprasVentas(programaBlob.getComenComprasVentas()); //DB_ESPACIO_LIBRE	byte[]
										odatosBasicoBlobPrin.setConcentracion(programaBlob.getConcentracion()); //DB_CONCENTRACION byte[]
				
										odatosBasicoBlobPrin.setDatosBasicosaddRDC(programaBlob.getDatosBasicosaddRDC());//RDC_COMEN_DATOSBASICOS_ADD	BLOB	Y			
										odatosBasicoBlobPrin.setValoracion(programaBlob.getValoracion()); //DB_VALORACION byte[]
										datosBasicosBlobBO.savecopia(odatosBasicoBlob);
									}
								}
							}
						}
					}
					
			}
			
//			List<DatosBasicoBlob> olistadatosBasicoBlob= datosBasicosBlobBO.listaDatosBasicoBlobByPrograma(programaini);
//			if (olistadatosBasicoBlob!=null && olistadatosBasicoBlob.size()>0){
//				for (DatosBasicoBlob odatosBasicoBlob: olistadatosBasicoBlob){
//					String codigoEmp=odatosBasicoBlob.getCodigoEmpresa();
//					if (validarEmpresaAGuardar(codigoEmp,listaEmpresasGrupofin)){
//						odatosBasicoBlob.setId(null);
//						odatosBasicoBlob.setPrograma(copiaPrograma);
//						odatosBasicoBlob.setFechaCreacion(new Date());
//						odatosBasicoBlob.setCodUsuarioCreacion(ousurio.getRegistroHost());
//						datosBasicosBlobBO.savecopia(odatosBasicoBlob);
//					}
//				}
//			}
			
			//setPrograma(programa);
			//addActionMessage("Datos Actualizados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
		
	}
		
	private boolean validarEmpresaAGuardar(String codigoEmpresa,List<Empresa> lista){
		
		boolean bexiste=false;
		try {			
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getCodigo().trim().equals(codigoEmpresa.trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		} catch (Exception e) {
			bexiste=false;
		}
		return bexiste;
	}
	
	private boolean validarEmpresaAGuardarByNombre(String nombreEmpresa,List<Empresa> lista){
		
		boolean bexiste=false;
		try {			
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getNombre().trim().equals(nombreEmpresa.trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		} catch (Exception e) {
			bexiste=false;
		}
		return bexiste;
	}
	//FIN MCG20130221
	
	public void copiarSintesisEconomica(Long idprogramaini,Programa copiaPrograma,List<Empresa> listaEmpresas,Long idprogramaPrime){
		
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();
		try {		
			
			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);

			String tipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				List<Empresa> lista = listaEmpresas;
				
				Empresa grupo = new Empresa();
				grupo.setCodigo(copiaPrograma.getIdGrupo());
				grupo.setNombre(copiaPrograma.getNombreGrupoEmpresa());
				try {
					grupo.setIdProgramaCopia(String.valueOf(idprogramaini));
				} catch (Exception e) {
					// TODO: handle exception
				}				
				listaEmpresasGrupofin.add(grupo);
				for(Empresa emp :lista ){
					listaEmpresasGrupofin.add(emp);
				}
				
			}else{			
//				Empresa oempresaPrincipal = new Empresa();
//				oempresaPrincipal.setCodigo(copiaPrograma.getIdEmpresa());
//				oempresaPrincipal.setNombre(copiaPrograma.getNombreGrupoEmpresa());
//				listaEmpresasGrupofin.add(oempresaPrincipal);
				listaEmpresasGrupofin=listaEmpresas;
				
			}
			
			for(Empresa oempresa :listaEmpresasGrupofin){
				String codigoEmpresafin=oempresa.getCodigo();
				String nombreEmpresa=oempresa.getNombre().trim();
				String idProgramafin =oempresa.getIdProgramaCopia();
				if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
					continue;
				}				
				Programa oprogramafin=new Programa();
				oprogramafin.setId(Long.valueOf(idProgramafin));
				
				List<SintesisEconomica> olistaSintesis=sintesisEconomicoBO.listarSintesisEconomico(oprogramafin,nombreEmpresa) ;
				if (olistaSintesis!=null && olistaSintesis.size()>0){					
					for (SintesisEconomica osintesisEconomica:olistaSintesis){						
						Long IdSintesisini=osintesisEconomica.getId();	
							osintesisEconomica.setId(null);
							osintesisEconomica.setFechaCarga(new Date());
							osintesisEconomica.setUsuario(ousurio.getNombre());
							osintesisEconomica.setFechaCreacion(new Date());
							osintesisEconomica.setCodUsuarioCreacion(ousurio.getRegistroHost());
							osintesisEconomica.setPrograma(copiaPrograma);
							sintesisEconomicoBO.savecopiaSintesisEconomica(osintesisEconomica,IdSintesisini);
					}
				}
				
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					if (!copiaPrograma.getIdGrupo().equals(codigoEmpresafin)){					
						SintesisEconomicoBlob osintesisEconomicoBlob= sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(oprogramafin,codigoEmpresafin);
						if (osintesisEconomicoBlob!=null){					
							String codigoEmp=osintesisEconomicoBlob.getCodigoEmpresa();						
							osintesisEconomicoBlob.setId(null);
							osintesisEconomicoBlob.setPrograma(copiaPrograma);						
							osintesisEconomicoBlob.setFechaCreacion(new Date());
							osintesisEconomicoBlob.setCodUsuarioCreacion(ousurio.getRegistroHost());
							sintesisEconomicoBlobBO.savecopia(osintesisEconomicoBlob);					
						}else{
							
							Programa oprogramaByEmpresa = this.programaBO.findById(Long.valueOf(idProgramafin));
							String idcodEmpresa=oprogramaByEmpresa.getIdEmpresa();
							if (idcodEmpresa!=null && idcodEmpresa.equals(codigoEmpresafin)){
								ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(oprogramafin);					
								if(programaBlob != null)
								{
									
									//SINTESIS ECONOMICO
									SintesisEconomicoBlob osintesisEconomicoBlobP=new SintesisEconomicoBlob();
									osintesisEconomicoBlobP.setId(null);
									osintesisEconomicoBlobP.setPrograma(copiaPrograma);
									osintesisEconomicoBlobP.setCodigoEmpresa(codigoEmpresafin);
									osintesisEconomicoBlobP.setFechaCreacion(new Date());
									osintesisEconomicoBlobP.setCodUsuarioCreacion(ousurio.getRegistroHost());
									osintesisEconomicoBlobP.setComenSituFinanciera(programaBlob.getComenSituFinanciera()); //SF_COMENTARIO_SITU_FINA	byte[]
									osintesisEconomicoBlobP.setComenSituEconomica(programaBlob.getComenSituEconomica()); //SF_COMENTARIO_SITU_ECON	byte[]
									osintesisEconomicoBlobP.setValoracionEconFinanciera(programaBlob.getValoracionEconFinanciera()); //SF_VALORACION_ECON_FINA	byte[]
									osintesisEconomicoBlobP.setValoracionPosiBalance(programaBlob.getValoracionPosiBalance()); // SF_VALORACION_POSI_BALA	byte[]	
									
									sintesisEconomicoBlobBO.savecopia(osintesisEconomicoBlobP);	
									
								}
							}
						}
						
						
					}
				}
			}
			

			
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	//ini MCG20130326
	public void copiarRating(Long idprogramaini,Programa copiaPrograma,List<Empresa> listaEmpresas,Long idprogramaPrime){
		
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();
		try {		
			
			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);

			String tipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				List<Empresa> lista = listaEmpresas;
				Empresa grupo = new Empresa();
				grupo.setCodigo(copiaPrograma.getIdGrupo());
				grupo.setNombre(copiaPrograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(grupo);
				for(Empresa emp :lista ){
					listaEmpresasGrupofin.add(emp);
				}
			}else{	
				listaEmpresasGrupofin=listaEmpresas;		
			}
			
			for(Empresa oempresa :listaEmpresasGrupofin){
				String codigoEmpresafin=oempresa.getCodigo();
				String nombreEmpresa=oempresa.getNombre().trim();
				String idProgramafin =oempresa.getIdProgramaCopia();
				if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
					continue;
				}				
				Programa oprogramafin=new Programa();
				oprogramafin.setId(Long.valueOf(idProgramafin));
			
				List<Rating> olistaRating=ratingBO.findRating(Long.valueOf(idProgramafin),codigoEmpresafin);
				List<Rating> listRatingCopia = new ArrayList<Rating>();			
				if (olistaRating!=null && olistaRating.size()>0){					
					for(Rating orating:olistaRating){
						String codEmpresa=orating.getCodEmpresaGrupo();							
						orating.setId(null);						
						orating.setFechaCreacion(new Date());
						orating.setCodUsuarioCreacion(ousurio.getRegistroHost());
						orating.setPrograma(copiaPrograma);
						listRatingCopia.add(orating);
					}
					ratingBO.saveCopiaRating(listRatingCopia);
				}
				
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					if (!copiaPrograma.getIdGrupo().equals(codigoEmpresafin)){
						RatingBlob oratingBlob= ratingBlobBO.findRatingBlobByPrograma(oprogramafin,codigoEmpresafin);
						if (oratingBlob!=null ){					
									String codEmpresa=oratingBlob.getCodigoEmpresa();					
									oratingBlob.setId(null);
									oratingBlob.setPrograma(copiaPrograma);
									oratingBlob.setFechaCreacion(new Date());
									oratingBlob.setCodUsuarioCreacion(ousurio.getRegistroHost());
									ratingBlobBO.savecopia(oratingBlob);			
						}else{
							
							Programa oprogramaByEmpresa = this.programaBO.findById(Long.valueOf(idProgramafin));
							String idcodEmpresa=oprogramaByEmpresa.getIdEmpresa();
							if (idcodEmpresa!=null && idcodEmpresa.equals(codigoEmpresafin)){
								ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(oprogramafin);					
								if(programaBlob != null)
								{
									
									//RATING
									RatingBlob oratingBlobP=new RatingBlob();
									oratingBlobP.setId(null);
									oratingBlobP.setPrograma(copiaPrograma);
									oratingBlobP.setCodigoEmpresa(codigoEmpresafin);
									oratingBlobP.setFechaCreacion(new Date());
									oratingBlobP.setCodUsuarioCreacion(ousurio.getRegistroHost());
									oratingBlobP.setValoracionRating(programaBlob.getValoracionRating()); //RA_VALORACION_RATI	byte[]
									ratingBlobBO.savecopia(oratingBlobP);
									
								}
							}
						}
					}
				}
			}
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	//fin MCG20130326
	
	private boolean validarEmpresasfinalesaGuardar(SintesisEconomica osintesisEconomica,List<Empresa> lista){
		
		boolean bexiste=false;
		try {
			
			
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getNombre().trim().equals(osintesisEconomica.getNombreEmpresa().trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		} catch (Exception e) {
			bexiste=false;
		}
		return bexiste;
	}
	
	public void copiarAnalisisSectorial(Long idprogramaini,Programa copiaPrograma,Long idprogramaPrime){
		
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);
		
		try {		
			
			
			
			List<AnalisisSectorial> olistAnalisisSectorial =new  ArrayList<AnalisisSectorial>();
			
			olistAnalisisSectorial=analisisSectorialBO.findByAnalisisSectorial(idprogramaini);
			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			
			if (olistAnalisisSectorial!=null && olistAnalisisSectorial.size()>0){
				for (AnalisisSectorial oanalisisSectorial: olistAnalisisSectorial){
					Long IdAnalisis=oanalisisSectorial.getId();
					oanalisisSectorial.setId(null);
					oanalisisSectorial.setFechaCarga(new Date());
					oanalisisSectorial.setUsuario(ousurio.getRegistroHost());
					oanalisisSectorial.setFechaCreacion(new Date());
					oanalisisSectorial.setCodUsuarioCreacion(ousurio.getRegistroHost());
					oanalisisSectorial.setPrograma(copiaPrograma);
					analisisSectorialBO.savecopiaAnalisis(oanalisisSectorial,IdAnalisis);
				}				
			}
			
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	public void copiarRelacionesBancarias(Long idprogramaini,Programa copiaPrograma,Long idprogramaPrime){
					
		try {		
			
			List<LineasRelacionBancarias> listLineasRelacionesCopia = new ArrayList<LineasRelacionBancarias>();
			List<LineasRelacionBancarias> olistLineasRelacionesBancarias = relacionesBancariasBO.findByLineasRelacionesBancarias(idprogramaini);
			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			
			if (olistLineasRelacionesBancarias!=null && olistLineasRelacionesBancarias.size()>0){
				for (LineasRelacionBancarias olineasRelacionBancarias: olistLineasRelacionesBancarias){
					
					olineasRelacionBancarias.setId(null);					
					olineasRelacionBancarias.setFechaCreacion(new Date());
					olineasRelacionBancarias.setCodUsuarioCreacion(ousurio.getRegistroHost());
					olineasRelacionBancarias.setFechaModificacion(null);
					olineasRelacionBancarias.setCodUsuarioModificacion(null);					
					olineasRelacionBancarias.setPrograma(copiaPrograma);					
					listLineasRelacionesCopia.add(olineasRelacionBancarias);
				}
				relacionesBancariasBO.saveCopiaLineasRelaciones(listLineasRelacionesCopia);
			}	
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	public void copiarPropuestaRiesgo(Long idprogramaini,Programa copiaPrograma,List<Empresa> listaEmpresas,Long idprogramaPrime){
		Empresa oEmpresaini=null;
		Empresa oEmpresafin=null;	
		Empresa oempresaEstructura=null;
		List<Empresa> listaEmpresasfin = new ArrayList<Empresa>();
		try {		
			
			List<EstructuraLimite> listEstructuraLimiteCopia = new ArrayList<EstructuraLimite>();
			List<EstructuraLimite> olistEstructuraLimite = propuestaRiesgoBO.findEstructuraLimiteByIdprograma(idprogramaini);
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
				
			String ptipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			if(ptipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){			
	
				listaEmpresasfin=listaEmpresas;
			}else{
				listaEmpresasfin= listaEmpresas;
			}
			
			
			
			if (olistEstructuraLimite!=null && olistEstructuraLimite.size()>0){
				for (EstructuraLimite oestructuraLimite: olistEstructuraLimite){
					
					if (validarEmpresasaGuardarPR(oestructuraLimite,listaEmpresasfin)){	
						oempresaEstructura= new Empresa();
												
						
							oEmpresaini =programaBO.findEmpresaByIdEmpresa(oestructuraLimite.getEmpresa().getId());	
							if (oEmpresaini!=null ) {
								oEmpresafin=programaBO.findEmpresaByIdEmpresaPrograma(copiaPrograma.getId() ,oEmpresaini.getCodigo()) ;
							}
							if (oEmpresafin!=null){						
								oempresaEstructura.setId(oEmpresafin.getId());
								oestructuraLimite.setEmpresa(oempresaEstructura);
							}
						
						oestructuraLimite.setId(null);					
						oestructuraLimite.setFechaCreacion(new Date());
						oestructuraLimite.setCodUsuarioCreacion(ousurio.getRegistroHost());
						oestructuraLimite.setFechaModificacion(null);
						oestructuraLimite.setCodUsuarioModificacion(null);					
						oestructuraLimite.setPrograma(copiaPrograma);						
						listEstructuraLimiteCopia.add(oestructuraLimite);
					}
				}
				propuestaRiesgoBO.saveCopiaEstructuraLimite(listEstructuraLimiteCopia);
			}	
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	private boolean validarEmpresasaGuardarPR(EstructuraLimite oestructuraLimite,List<Empresa> lista){
		
	boolean bexiste=false;
	try {	
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getNombre().trim().equals(oestructuraLimite.getEmpresa().getNombre().trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		
	} catch (Exception e) {
		bexiste=false;
	}		
		return bexiste;
	}
	
	public void copiarPoliticasRiesgo(Long idprogramaini,Programa copiaPrograma,Long idprogramaPrime){
	
		try {		
						
			List<LimiteFormalizado> listLimiteFormalizadoCopia= new ArrayList<LimiteFormalizado>();
			List<LimiteFormalizado> olistLimiteFormalizado=politicasRiesgoBO.findLimiteFormalizadoByIdprograma(idprogramaini);		
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
				
			if (olistLimiteFormalizado!=null && olistLimiteFormalizado.size()>0){
				for (LimiteFormalizado olimiteFormalizado: olistLimiteFormalizado){	
					
					olimiteFormalizado.setId(null);					
					olimiteFormalizado.setFechaCreacion(new Date());
					olimiteFormalizado.setCodUsuarioCreacion(ousurio.getRegistroHost());
					olimiteFormalizado.setFechaModificacion(null);
					olimiteFormalizado.setCodUsuarioModificacion(null);					
					olimiteFormalizado.setPrograma(copiaPrograma);						
					listLimiteFormalizadoCopia.add(olimiteFormalizado);
				}
				politicasRiesgoBO.saveCopiaLimiteFormalizado(listLimiteFormalizadoCopia);
			}	
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}	
	
	//copiarAnexo	
	public void copiarAnexo(Long idprogramaini,Programa copiaPrograma,List<Empresa> listaEmpresas,Long idprogramaPrime){
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);
		List<Empresa> listaEmpresasfin = new ArrayList<Empresa>();
		try {		
					
					
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
				
			String ptipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			if(ptipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){			
	
				listaEmpresasfin=listaEmpresas;				
			}else{
				listaEmpresasfin= listaEmpresas;
			}
							
//			List<FilaAnexo> olistaFilaAnexos=anexoBO.findAnexosByPrograma(programaini);		
//			if(olistaFilaAnexos != null && olistaFilaAnexos.size()>0){
//					
//					Long lpadrebanco=null;
//					Long lpadreempresa=null;
//					Long lpadreoperacion=null;
//					
//					for(FilaAnexo filaAnexo : olistaFilaAnexos){
//						
//						if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
//							filaAnexo.getAnexo().setPadretmp(null);
//							lpadrebanco=filaAnexo.getAnexo().getId();
//						}else if (filaAnexo.getAnexo().getTipoFila().equals(2)){						
//							filaAnexo.getAnexo().setPadretmp(lpadrebanco);
//							lpadreempresa=filaAnexo.getAnexo().getId();
//						}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
//							filaAnexo.getAnexo().setPadretmp(lpadreempresa);
//							lpadreoperacion=filaAnexo.getAnexo().getId();
//						}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
//							filaAnexo.getAnexo().setPadretmp(lpadreoperacion);						
//						}
//						logger.info("Asignacion de padre a Anexos:");
//						logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getId() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	
//					}
//					
//					List<FilaAnexo> olistaFilaAnexosexcl=new ArrayList<FilaAnexo>();
//					List<FilaAnexo> olistaFilaAnexosexcltfin=new ArrayList<FilaAnexo>();
//					
//					olistaFilaAnexosexcl=listadoEmpresaExcluidas(olistaFilaAnexos,listaEmpresasfin);
//					if (olistaFilaAnexosexcl!=null && olistaFilaAnexosexcl.size()>0){
//						for (FilaAnexo xfilaAnexo: olistaFilaAnexosexcl){
//							olistaFilaAnexosexcltfin.add(xfilaAnexo);
//							buscarHijos(xfilaAnexo.getAnexo().getId(),olistaFilaAnexos,olistaFilaAnexosexcltfin);					
//						}					
//					}
//					for (FilaAnexo afilaAnexo: olistaFilaAnexosexcltfin){					
//						logger.info("copiarAnexo:::Anexo Excluidos:");
//						logger.info("Descripcion Anexo:"+ afilaAnexo.getAnexo().getDescripcion() +"::" 
//								+ "id:"+ afilaAnexo.getAnexo().getId() +"::"
//								+ "idpadre:"+ afilaAnexo.getAnexo().getPadretmp());	
//					}
//					List<FilaAnexo> olistFilaAnexoCopia = new ArrayList<FilaAnexo>();
//					int posicion=0;
//					List<Empresa> olistaEmpresaFaltaIncluir=new ArrayList<Empresa>();
//					for(FilaAnexo filaAnexo : olistaFilaAnexos){					
//						if (!validarEmpresaAGuardarAnexo(filaAnexo,olistaFilaAnexosexcltfin)){
//							Anexo anexo = filaAnexo.getAnexo();
//							anexo.setId(null);
//							anexo.setPosFila(posicion);
//							anexo.setPrograma(copiaPrograma);					
//							olistFilaAnexoCopia.add(filaAnexo);
//							posicion=posicion+1;
//						}						
//					}				
//					anexoBO.saveCopiaAnexos(olistFilaAnexoCopia);					
//			}
			
			//ini nuevo carga anexo
			
			List<FilaAnexo> olistaFilaAnexosCopiaN=new ArrayList<FilaAnexo>();
			FilaAnexo filaAnexo=new FilaAnexo();
			Anexo anexo = new Anexo();
			anexo.setId(null);
			anexo.setPosFila(0);
			anexo.setTipoFila(1);
			anexo.setDescripcion(Constantes.NOM_BANCO_DEFAULT);
			anexo.setPrograma(copiaPrograma);
			filaAnexo.setAnexo(anexo);
			olistaFilaAnexosCopiaN.add(filaAnexo);
			
			int posicion=1;
			for(Empresa oempresa :listaEmpresasfin){
				String codigoEmpresafin=oempresa.getCodigo();
				String nombreEmpresa=oempresa.getNombre().trim();
				String idProgramafin =oempresa.getIdProgramaCopia();
				if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
					continue;
				}				
				Programa oprogramafin=new Programa();
				oprogramafin.setId(Long.valueOf(idProgramafin));
				
				List<FilaAnexo> olistaFilaAnexosPFini=new ArrayList<FilaAnexo>();
				olistaFilaAnexosPFini = anexoBO.obtenerListAnexosHijosByEmpresaInclueEmpresa(idprogramaini,nombreEmpresa);
				if (olistaFilaAnexosPFini==null ||olistaFilaAnexosPFini!=null && olistaFilaAnexosPFini.size()==0){
					olistaFilaAnexosPFini=new ArrayList<FilaAnexo>();
					olistaFilaAnexosPFini = anexoBO.obtenerListAnexosHijosByEmpresaInclueEmpresa(Long.valueOf(idProgramafin),nombreEmpresa);
				}
				if (olistaFilaAnexosPFini!=null & olistaFilaAnexosPFini.size()>0){
					for (FilaAnexo ofilaAnexo:olistaFilaAnexosPFini){
						Anexo anexoN = ofilaAnexo.getAnexo();
						anexoN.setId(null);
						anexoN.setPosFila(posicion);
						anexoN.setPrograma(copiaPrograma);					
						olistaFilaAnexosCopiaN.add(ofilaAnexo);
						posicion=posicion+1;
					}
					
				}				
			}
			
			if (olistaFilaAnexosCopiaN!=null & olistaFilaAnexosCopiaN.size()>0){
				anexoBO.saveCopiaAnexos(olistaFilaAnexosCopiaN);
			}
			//fin nuevo carga anexo	
			
				
				
				for(Empresa oempresa :listaEmpresasfin){
					String codigoEmpresafin=oempresa.getCodigo();
					String nombreEmpresafin=oempresa.getNombre().trim();
					String idProgramafin =oempresa.getIdProgramaCopia();
					if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
						continue;
					}				
					Programa oprogramafin=new Programa();
					oprogramafin.setId(Long.valueOf(idProgramafin));
				
				List<AnexoGarantia> olistaGarantiaAnexo=anexoGarantiaBO.findAnexoXPrograma(oprogramafin,nombreEmpresafin);
				
				List<AnexoGarantia> listAnexoGarantiaCopia = new ArrayList<AnexoGarantia>();			
				if (olistaGarantiaAnexo!=null && olistaGarantiaAnexo.size()>0){
					for (AnexoGarantia oanexoGarantia: olistaGarantiaAnexo){
						String nombreEmpresa=oanexoGarantia.getEmpresa();							
							oanexoGarantia.setId(null);						
							oanexoGarantia.setFechaCreacion(new Date());
							oanexoGarantia.setCodUsuarioCreacion(ousurio.getRegistroHost());
							oanexoGarantia.setPrograma(copiaPrograma);
							listAnexoGarantiaCopia.add(oanexoGarantia);						
									
					}
					anexoGarantiaBO.saveCopiaAnexoGarantia(listAnexoGarantiaCopia);
				}			
			}			
			
			List<ArchivoAnexo> olistaArchivosAnexo = archivoAnexoBO.findListaArchivos(programaini);
				
			if (olistaArchivosAnexo!=null && olistaArchivosAnexo.size()>0){
				for (ArchivoAnexo oarchivoAnexo: olistaArchivosAnexo){
					Long IdarchivoAnexo=oarchivoAnexo.getId();
					oarchivoAnexo.setId(null);					
					oarchivoAnexo.setFechaCreacion(new Date());
					oarchivoAnexo.setCodUsuarioCreacion(ousurio.getRegistroHost());
					oarchivoAnexo.setPrograma(copiaPrograma);
					archivoAnexoBO.saveCopiaFileAnexo(oarchivoAnexo,IdarchivoAnexo);
				}				
			}		
	
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	private void buscarHijos(Long idfila,List<FilaAnexo> olistaFilaAnexos,List<FilaAnexo> olistaFilaAnexosexcl){
					
			for (FilaAnexo filaAnexo : olistaFilaAnexos){
				if (filaAnexo.getAnexo().getPadretmp()!=null && filaAnexo.getAnexo().getPadretmp().equals(idfila)){					
					olistaFilaAnexosexcl.add(filaAnexo);
					buscarHijos(filaAnexo.getAnexo().getId(),olistaFilaAnexos,olistaFilaAnexosexcl);
				}
			}		
		
	}
	
	private List<FilaAnexo> listadoEmpresaExcluidas(List<FilaAnexo> olistaFilaAnexo,List<Empresa> lista){
		
		List<FilaAnexo> olistaFilaAnexoExcl=new ArrayList<FilaAnexo>();
			if (lista!=null && lista.size()>0){	
				boolean flag= false;
				for (FilaAnexo ofilaAnexo : olistaFilaAnexo){
					if (ofilaAnexo.getAnexo().getTipoFila().equals(2)){
						for(Empresa empresa : lista){
							if(empresa.getNombre().trim().equals(ofilaAnexo.getAnexo().getDescripcion().trim())){
								
								flag=true;
							}
						}
						if(!flag){
							olistaFilaAnexoExcl.add(ofilaAnexo);
						}						
					}		
					flag= false;
				}
			}		
		return olistaFilaAnexoExcl;
	}
	
	private boolean validarEmpresaAGuardarAnexo(FilaAnexo ofilaAnexo,List<FilaAnexo> listaExcluidos){
		
		boolean bexiste=false;
		try {	
				if (listaExcluidos!=null && listaExcluidos.size()>0){
					for(FilaAnexo ofilaAnexoexc : listaExcluidos){
						if(ofilaAnexoexc.getAnexo().getId().equals(ofilaAnexo.getAnexo().getId())){
							bexiste=true;
							break;
						}
					}
				}else{
					bexiste=false;
				}
			
		} catch (Exception e) {
			bexiste=false;
		}		
			return bexiste;
		}
	
	
	public List<CabTabla> LoadCabeceraPlanilla(Long idprograma,int anio,int annioini){			
		List<CabTabla> listaCabTablaPlanilla = new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_PLANILLA);
					
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {
				Collections.sort(listaCabTablatmp);				
				int fila=0;
				int numerofila=0;
				numerofila=listaCabTablatmp.size();
				String stranio="12/" + String.valueOf(anio);
				String[] strnombrecabecera = new String[numerofila+2];
				if (annioini<anio){
					int n=0;
					for (CabTabla oCabTabla: listaCabTablatmp){
						strnombrecabecera[n]=oCabTabla.getCabeceraTabla();
						n=n+1;
					}	
					fila=0;					
					for (CabTabla oCabTabla: listaCabTablatmp){	
						fila=fila+1;	
					    oCabTabla.setCabeceraTabla(strnombrecabecera[fila]==null?stranio:strnombrecabecera[fila]);
					}					
				}
				
				for (CabTabla oCabTabla: listaCabTablatmp){		
					oCabTabla.setId(null);
					oCabTabla.setPrograma(null);	
				}
				listaCabTablaPlanilla=listaCabTablatmp;				
			}else{
				
				//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<4;i++) {
						intposicion=intposicion+1;
					    String stranio="12/" + String.valueOf(anio-(3-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_PLANILLA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaPlanilla=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		return listaCabTablaPlanilla;
	}
	
	public List<CabTabla> LoadCabeceraCompra(Long idprograma,int anio,int annioini){			
		List<CabTabla> listaCabTablaCompra = new ArrayList<CabTabla>();	
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_COMPRA);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				
				//ini mcg rearmando cabecera
				int fila=0;
				int numerofila=0;
				numerofila=listaCabTablatmp.size();
				String stranio="Dic-" + String.valueOf(anio);
				String[] strnombrecabecera = new String[numerofila+2];
				if (annioini<anio){
					int n=0;
					for (CabTabla oCabTabla: listaCabTablatmp){
						strnombrecabecera[n]=oCabTabla.getCabeceraTabla();
						n=n+1;
					}	
					fila=0;					
					for (CabTabla oCabTabla: listaCabTablatmp){	
						fila=fila+1;	
					    oCabTabla.setCabeceraTabla(strnombrecabecera[fila]==null?stranio:strnombrecabecera[fila]);
					}					
				}
				//fin 					
				
				for (CabTabla oCabTabla: listaCabTablatmp){
					oCabTabla.setId(null);
					oCabTabla.setPrograma(null);
				}
				listaCabTablaCompra=listaCabTablatmp;				
			}else{
				
				//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_COMPRA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaCompra=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}	
		return listaCabTablaCompra;
	}	
	
	public List<CabTabla> LoadCabeceraVenta(Long idprograma,int anio,int annioini){			
		List<CabTabla> listaCabTablaVenta = new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_VENTA);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				
				
				//ini mcg rearmando cabecera
				int fila=0;
				int numerofila=0;
				numerofila=listaCabTablatmp.size();
				String stranio="Dic-" + String.valueOf(anio);
				String[] strnombrecabecera = new String[numerofila+2];
				if (annioini<anio){
					int n=0;
					for (CabTabla oCabTabla: listaCabTablatmp){
						strnombrecabecera[n]=oCabTabla.getCabeceraTabla();
						n=n+1;
					}	
					fila=0;					
					for (CabTabla oCabTabla: listaCabTablatmp){	
						fila=fila+1;	
					    oCabTabla.setCabeceraTabla(strnombrecabecera[fila]==null?stranio:strnombrecabecera[fila]);
					}					
				}
				//fin 
				
				
				
				for (CabTabla oCabTabla: listaCabTablatmp){
					oCabTabla.setId(null);
					oCabTabla.setPrograma(null);
				}
				listaCabTablaVenta=listaCabTablatmp;				
			}else{
				
				//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_VENTA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaVenta=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}	
		return listaCabTablaVenta;
	}
	
	
	public List<CabTabla> LoadCabeceraCuadraNBActividad(Long idprograma,int anio,int annioini ){			
		List<CabTabla> listaCabTablaActividad = new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_ACTIVIDAD);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				
				//ini mcg rearmando cabecera
				int fila=0;
				int numerofila=0;
				numerofila=listaCabTablatmp.size();
				String stranio="" + String.valueOf(anio);
				String[] strnombrecabecera = new String[numerofila+2];
				if (annioini<anio){
					int n=0;
					for (CabTabla oCabTabla: listaCabTablatmp){
						strnombrecabecera[n]=oCabTabla.getCabeceraTabla();
						n=n+1;
					}	
					fila=0;					
					for (CabTabla oCabTabla: listaCabTablatmp){	
						fila=fila+1;
						if (fila==3){
							oCabTabla.setCabeceraTabla(stranio);
						}else{
							oCabTabla.setCabeceraTabla(strnombrecabecera[fila]==null?stranio:strnombrecabecera[fila]);
						}	
					}					
				}
				//fin 
									
				for (CabTabla oCabTabla: listaCabTablatmp){
					oCabTabla.setId(null);
					oCabTabla.setPrograma(null);
				}
				listaCabTablaActividad=listaCabTablatmp;				
			}else{
				
				//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}	
		
				Collections.sort(listaCabTablaini);
				listaCabTablaActividad=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		return listaCabTablaActividad;
	}
	
	public List<CabTabla> LoadCabeceraCuadraNBNegocio(Long idprograma,int anio,int annioini){			
		List<CabTabla> listaCabTablaNegocio = new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_NEGOCIO);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				//ini mcg rearmando cabecera
				int fila=0;
				int numerofila=0;
				numerofila=listaCabTablatmp.size();
				String stranio="" + String.valueOf(anio);
				String[] strnombrecabecera = new String[numerofila+2];
				if (annioini<anio){
					int n=0;
					for (CabTabla oCabTabla: listaCabTablatmp){
						strnombrecabecera[n]=oCabTabla.getCabeceraTabla();
						n=n+1;
					}	
					fila=0;					
					for (CabTabla oCabTabla: listaCabTablatmp){	
						fila=fila+1;
						if (fila==3){
							oCabTabla.setCabeceraTabla(stranio);
						}else{
							oCabTabla.setCabeceraTabla(strnombrecabecera[fila]==null?stranio:strnombrecabecera[fila]);
						}	
					}					
				}
				//fin 
				
				for (CabTabla oCabTabla: listaCabTablatmp){
					oCabTabla.setId(null);
					oCabTabla.setPrograma(null);
				}
				listaCabTablaNegocio=listaCabTablatmp;				
			}else{
				
				//int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaNegocio=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		return listaCabTablaNegocio;
	}
	
	public void copiaReporteCredito(Long idprogramaini,Programa copiaPrograma,List<Empresa> listaEmpresas,Long idprogramaPrime){
		
		Programa programaini = new Programa();
		programaini.setId(idprogramaini);
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();
		try {	
			
				
			reporteCreditoBO.setPrograma(copiaPrograma);			
		
			String tipoEmpresa = copiaPrograma.getTipoEmpresa().getId().toString();
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){		
					listaEmpresasGrupofin=listaEmpresas;				
			}else{			
				listaEmpresasGrupofin=listaEmpresas;
			}	
			
			for(Empresa oempresa :listaEmpresasGrupofin){
				String codigoEmpresafin=oempresa.getCodigo();
				String nombreEmpresa=oempresa.getNombre().trim();
				String idProgramafin =oempresa.getIdProgramaCopia();
				if (idProgramafin==null || (idProgramafin!=null && idProgramafin.equals(""))){
					continue;
				}				
				Programa oprogramafin=new Programa();
				oprogramafin.setId(Long.valueOf(idProgramafin));
			
			
					List<ReporteCredito> listaReporteCreditoCopia=new ArrayList<ReporteCredito>();
					List<ReporteCredito> olistaReporteCredito=reporteCreditoBO.getListaReporteCredito(oprogramafin,codigoEmpresafin);
					
					if (olistaReporteCredito!=null && olistaReporteCredito.size()>0){
						ReporteCredito oreporteCredito=olistaReporteCredito.get(0);
							String codiEmpresarc=oreporteCredito.getCodigoEmpresa();							
								oreporteCredito.setId(null);
								oreporteCredito.setFechaCreacion(new Date());
								//oclaseCredito.setCodUsuarioCreacion(ousurio.getRegistroHost());
								oreporteCredito.setPrograma(copiaPrograma);
								listaReporteCreditoCopia.add(oreporteCredito);
							
										
					}	
					reporteCreditoBO.setListaReporteCredito(listaReporteCreditoCopia);
					
					List<ClaseCredito> listaClaseCreditoCopia=new ArrayList<ClaseCredito>();
					List<ClaseCredito> olistaClaseCredito=reporteCreditoBO.ObtenerListaClaseCredito(oprogramafin,codigoEmpresafin);
															
					if (olistaClaseCredito!=null && olistaClaseCredito.size()>0){
						for (ClaseCredito oclaseCredito: olistaClaseCredito){
							String codiEmpresa=oclaseCredito.getCodEmpresaGrupo();
							
							oclaseCredito.setId(null);
							oclaseCredito.setFechaCreacion(new Date());
							//oclaseCredito.setCodUsuarioCreacion(ousurio.getRegistroHost());
							oclaseCredito.setPrograma(copiaPrograma);
							listaClaseCreditoCopia.add(oclaseCredito);
							
						}				
					}		
					
					reporteCreditoBO.setListaClaseCredito(listaClaseCreditoCopia);
			
					List<Garantia> listaGarantiaCopia=new ArrayList<Garantia>();
					List<Garantia> olistaGarantia=reporteCreditoBO.findGarantiaByIdprograma(Long.valueOf(idProgramafin),codigoEmpresafin);				
					if (olistaGarantia!=null && olistaGarantia.size()>0){
						for (Garantia oGarantia: olistaGarantia){
							String codiEmpresa1=oGarantia.getCodEmpresaGrupo();
							
							oGarantia.setId(null);
							oGarantia.setPrograma(null);
							oGarantia.setFechaCreacion(new Date());
							listaGarantiaCopia.add(oGarantia);						
						}				
					}
					reporteCreditoBO.setListGarantia(listaGarantiaCopia);	
					
					List<SustentoOperacion> listaSustentoOperacionCopia = new ArrayList<SustentoOperacion>();
					List<SustentoOperacion> olistaSustentoOperacion=reporteCreditoBO.findSustentoOperacionByIdprograma(Long.valueOf(idProgramafin),codigoEmpresafin);	
					if (olistaSustentoOperacion!=null && olistaSustentoOperacion.size()>0){
						for (SustentoOperacion osustentoOperacion: olistaSustentoOperacion){
							String codiEmpresa2=osustentoOperacion.getCodEmpresaGrupo();
							
								osustentoOperacion.setId(null);	
								osustentoOperacion.setPrograma(null);
								osustentoOperacion.setFechaCreacion(new Date());
								listaSustentoOperacionCopia.add(osustentoOperacion);							
						}				
					}			
					reporteCreditoBO.setListaSustentoOperacion(listaSustentoOperacionCopia);		
					
					reporteCreditoBO.copiaReporteCredito();
					
			}		
					
			
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			 
		}		
	}
		
	//FIN MCG 20120427
	
	//ini MCG20121121
	public String actualizarProgramaAbierto() throws BOException	
	{
		List<Empresa> listaEmpresas = null;
		String forward="modificarPrograma";
		try
		{
			
			logger.info(programa);
			String nombregrupoEmpresaini=programa.getNombreGrupoEmpresa()==null?"":programa.getNombreGrupoEmpresa();
			this.programa = this.programaBO.findById(this.programa.getId());
			logger.info(programa);
			logger.info(programa.getEstadoPrograma());
			logger.info(programa.getEstadoPrograma().getId());
			
			
			logger.info("getTipoEmpresa():"+getTipoEmpresa());
			logger.info("getTipoPrograma():"+getTipoPrograma());			
			logger.info("getCodigoEmpresaGrupo():"+getCodigoEmpresaGrupo());		
							
			
			
			if(this.programa.getEstadoPrograma().getId().equals(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE))
			{
						
				boolean existePrincipal = false;
				
				
				//nueva valores
				Tabla objTipoPrograma = new Tabla();
				objTipoPrograma.setId(Long.valueOf(getTipoPrograma()));
				Tabla objTipoEmpresa = new Tabla();
				objTipoEmpresa.setId(Long.valueOf(getTipoEmpresa()));
				
				programa.setTipoEmpresa(objTipoEmpresa);
				programa.setTipoPrograma(objTipoPrograma);	

				programa.setNombreGrupoEmpresa(nombregrupoEmpresaini);
					
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);			
				programa.setCodUsuarioModificacion(user.getRegistroHost());
				programa.setFechaModificacion(new Date());
				
		
				//ini mcg
				if(programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					//'2':'RUC','3':'Código Central'
					if(tipoDocBusqueda.equals(Constantes.VAL_TIPO_DOC_COD_CENTRAL)){
						programa.setIdEmpresa(getCodigoEmpresaGrupo());
					}else{
						programa.setRuc(getCodigoEmpresaGrupo());
					}
					
				}else{
					listaEmpresas = new ArrayList<Empresa>();
					programa.setIdGrupo(getCodigoEmpresaGrupo());
					Tabla tipoGrupo = null;// EMPRESA PRINCIPAL, SECUNDARIA O ANEXA
					List<Empresa> listaTemp = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
					if(empresaPrincipal!=null){
						empresaPrincipal = findEmpresaGrupo(listaTemp, empresaPrincipal.getCodigo());
						
						existePrincipal = true;						
						tipoGrupo = new Tabla();
						tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
						empresaPrincipal.setTipoGrupo(tipoGrupo);
						listaEmpresas.add(empresaPrincipal);
						//se registra el ruc de la empresa principal
						programa.setRuc(empresaPrincipal.getRuc());
					}
					if(getRequest().getParameterValues("selListaEmpresasSecundarias")!= null){
						for(String ids: getRequest().getParameterValues("selListaEmpresasSecundarias")){
							Empresa empresa =null;
							empresa = findEmpresaGrupo(listaTemp, ids);
							tipoGrupo = new Tabla();
							tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_SECUNDARIA);
							empresa.setTipoGrupo(tipoGrupo);
							listaEmpresas.add(empresa);
						}
					}
					//MCG20150129 RETIRADO YA NO EXISTE ANEXO
					/*
					if(getRequest().getParameterValues("selListaEmpresasAnexas") != null){
						for(String ids: getRequest().getParameterValues("selListaEmpresasAnexas")){
							Empresa empresa =null;
							empresa = findEmpresaGrupo(listaTemp, ids);
							tipoGrupo = new Tabla();
							tipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_ANEXA);
							empresa.setTipoGrupo(tipoGrupo);
							listaEmpresas.add(empresa);
						}
					}
					*/
									
					Empresa oEmpresaOriginal=null;					
					for(Empresa oempresa:listaEmpresas){
						oEmpresaOriginal=programaBO.findEmpresaByIdEmpresaPrograma(programa.getId() ,oempresa.getCodigo()) ;
						if (oEmpresaOriginal!=null){
							oempresa.setId(oEmpresaOriginal.getId());
						}
						
					}
					programaBO.setListaGrupoEmpresas(listaEmpresas);
				}
				//fin mcg
							
				
				
				programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));	
				String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
				programaBO.setPathWebService(pathWebService);
				programaBO.setConfiguracionWS(getConfiguracionWS());
				programa.setActivoValida("Activo");
																
				if(!programaBO.validate(programa)){
					return "editgrupoAbierto";
				}			
				actualizarDatosBasicos(programa,listaEmpresas);
				actualizarSintesisEconomica(programa,listaEmpresas);
				actualizarRating(programa,listaEmpresas);
				actualizarRelacionesBancarias(programa,listaEmpresas);
				actualizarPropuestaRiesgo(programa,listaEmpresas);				
				actualizarcopiarAnexo(programa,listaEmpresas);	
				actualizarReporteCredito(programa,listaEmpresas);
				programaBO.save(programa);
	
				Map<String, Object> sessionparam = ActionContext.getContext().getSession();
				sessionparam.put(Constantes.ID_PROGRAMA_SESSION, programa.getId().toString());
				
				//ini MCG20121030
				sessionparam.put(Constantes.NUM_SOLICITUD_SESSION, programa.getNumeroSolicitud()==null?"":programa.getNumeroSolicitud().toString());
				//fin MCG20121030
				
				//si es Local o Corporativo
				sessionparam.put(Constantes.ID_TIPO_PROGRAMA_SESSION, programa.getTipoPrograma().getId());
				//si es empresa o grupo
				sessionparam.put(Constantes.COD_TIPO_EMPRESA_SESSION, programa.getTipoEmpresa().getId().toString());
				//en el caso de grupos se sube a session los datos de la empresa 
				//principal en los datos de la empresa 
				//MCG 20120430 add getId
				if(programa.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
					sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, programa.getRuc());
					sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, programa.getIdEmpresa());
					sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, programa.getIdEmpresa());
					if(existePrincipal)
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
				}else{
					sessionparam.put(Constantes.COD_GRUPO_SESSION, programa.getIdGrupo());
					sessionparam.put(Constantes.LISTA_GRUPO_EMPRESAS_SESSION, listaEmpresas);
					if(existePrincipal)
					{
						sessionparam.put(Constantes.COD_RUC_EMPRESA_SESSION, empresaPrincipal.getRuc());
						sessionparam.put(Constantes.COD_CENTRAL_EMPRESA_SESSION, empresaPrincipal.getCodigo());
						sessionparam.put(Constantes.NOMBRE_EMPRESA_PRINCIPAL, empresaPrincipal.getNombre());
						sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, empresaPrincipal.getCodigo());
					}
				}
				sessionparam.put(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION, programa.getNombreGrupoEmpresa());
				sessionparam.put(Constantes.ANIO_PROGRAMA_SESSION, programa.getAnio());
			
			}
		
		} catch (BOException e) {
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupoAbierto";
			e.printStackTrace();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));	
		}catch(Exception ex)	{	
			listaGrupoEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			forward = "editgrupoAbierto";
			ex.printStackTrace();
			logger.info(ex);
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		
		return forward;
		
	}
	
	public void actualizarSintesisEconomica(Programa oprograma,List<Empresa> listaEmpresas){
		
	
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();		
		try {		
			
			List<SintesisEconomica> olistaSintesis=sintesisEconomicoBO.listarSintesisEconomico(oprograma) ;
			
			String tipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				List<Empresa> lista = listaEmpresas;
				Empresa grupo = new Empresa();
				grupo.setCodigo(oprograma.getIdGrupo());
				grupo.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(grupo);
				for(Empresa emp :lista ){
					listaEmpresasGrupofin.add(emp);
				}
			}else{			
				Empresa oempresaPrincipal = new Empresa();
				oempresaPrincipal.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipal.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(oempresaPrincipal);
				
			}
			
			if (olistaSintesis!=null && olistaSintesis.size()>0){
				for (SintesisEconomica osintesisEconomica: olistaSintesis){										
					if (!validarEmpresasfinalesaUpdate(osintesisEconomica,listaEmpresasGrupofin)){						
						sintesisEconomicoBO.delete(osintesisEconomica);
					}
				
				}				
			}	
			
			
			List<SintesisEconomicoBlob> olistasintesisEconomicoBlob= sintesisEconomicoBlobBO.listaSintEcoBlobByPrograma(oprograma);
			if (olistasintesisEconomicoBlob!=null && olistasintesisEconomicoBlob.size()>0){
				for (SintesisEconomicoBlob osintesisEconomicoBlob: olistasintesisEconomicoBlob){					
					if (!validarEmpresasfinalesaUpdate2(osintesisEconomicoBlob,listaEmpresasGrupofin)){						
						sintesisEconomicoBlobBO.delete(osintesisEconomicoBlob);
					}
				}
			}
			
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	//ini MCG20130326
	public void actualizarRating(Programa oprograma,List<Empresa> listaEmpresas){
				
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();		
		try {		
			
			List<Rating> olistaRating=ratingBO.findRating(oprograma.getId()) ;
			
			String tipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				List<Empresa> lista = listaEmpresas;
				Empresa grupo = new Empresa();
				grupo.setCodigo(oprograma.getIdGrupo());
				grupo.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(grupo);
				for(Empresa emp :lista ){
					listaEmpresasGrupofin.add(emp);
				}
			}else{			
				Empresa oempresaPrincipal = new Empresa();
				oempresaPrincipal.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipal.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(oempresaPrincipal);
				
			}
			
			if (olistaRating!=null && olistaRating.size()>0){
				for (Rating orating: olistaRating){	
					String codEmpresa=orating.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codEmpresa,listaEmpresasGrupofin)){						
						ratingBO.delete(orating);
					}
				
				}				
			}				
			
			List<RatingBlob> olistaRatingBlob= ratingBlobBO.listaRatingBlobByPrograma(oprograma);
			if (olistaRatingBlob!=null && olistaRatingBlob.size()>0){
				for (RatingBlob oratingBlob: olistaRatingBlob){	
					String codEmpresa=oratingBlob.getCodigoEmpresa();
					if (!validarEmpresaAGuardar(codEmpresa,listaEmpresasGrupofin)){						
						ratingBlobBO.delete(oratingBlob);
					}
				}
			}			
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}	
	//fin MCG20130326
	
	//ini MCG20130222
	public void actualizarDatosBasicos(Programa oprograma,List<Empresa> listaEmpresas){
				
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();		
		try {		
									
			String tipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					listaEmpresasGrupofin=listaEmpresas;				
			}else{			
				Empresa oempresaPrincipal = new Empresa();
				oempresaPrincipal.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipal.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(oempresaPrincipal);				
			}
			datosBasicosBO.ActualizarDatosBasicosByPrograma(oprograma,listaEmpresasGrupofin);				
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	//fin MCG20130222
	
	
	//ini MCG20130222
	public void actualizarReporteCredito(Programa oprograma,List<Empresa> listaEmpresas){
				
		List<Empresa> listaEmpresasGrupofin = new ArrayList<Empresa>();		
		try {		
									
			String tipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					listaEmpresasGrupofin=listaEmpresas;				
			}else{			
				Empresa oempresaPrincipal = new Empresa();
				oempresaPrincipal.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipal.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasGrupofin.add(oempresaPrincipal);				
			}
			reporteCreditoBO.ActualizarReporteCreditoByPrograma(oprograma,listaEmpresasGrupofin);				
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	//fin MCG20130222
	
	
	private boolean validarEmpresasfinalesaUpdate(SintesisEconomica osintesisEconomica,List<Empresa> lista){
		
		boolean bexiste=false;
		try {
			
			
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getNombre().trim().equals(osintesisEconomica.getNombreEmpresa().trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		} catch (Exception e) {
			bexiste=false;
		}
		return bexiste;
	}
	private boolean validarEmpresasfinalesaUpdate2(SintesisEconomicoBlob osintesisEconomicoBlob,List<Empresa> lista){
		
		boolean bexiste=false;
		try {
			
			
			if (lista!=null && lista.size()>0){
				for(Empresa empresa : lista){
					if(empresa.getCodigo().trim().equals(osintesisEconomicoBlob.getCodigoEmpresa().trim())){
						bexiste=true;
						break;
					}
				}
			}else{
				bexiste=false;
			}
		} catch (Exception e) {
			bexiste=false;
		}
		return bexiste;
	}
	
	public void actualizarRelacionesBancarias(Programa oprograma,List<Empresa> listaEmpresas){
		
		try {		
			
			List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
			List<Tempresa> listaEmpresaSelect = new ArrayList<Tempresa>();
			List<Tbanco> listaBancoSelect = new ArrayList<Tbanco>();
			List<Empresa> listEmpresatmp= new ArrayList<Empresa>();
			List<Tempresa> listaEmpresaobj = new ArrayList<Tempresa>();
			String idprograma=oprograma.getId().toString();
			listaopcionPool=relacionesBancariasBO.findOpcionPool(idprograma, Constantes.ID_TIPO_OPPOOL_TIPODEUDA);
			
			String ArraycodTipoDeudas="";
			for (OpcionPool opcionPool: listaopcionPool){				
					ArraycodTipoDeudas +=opcionPool.getCodOpcionPool() + ",";
			}
			if (ArraycodTipoDeudas.length()>0){
			int pos=ArraycodTipoDeudas.length()-1;
			ArraycodTipoDeudas=ArraycodTipoDeudas.substring(0, pos);
			}										
				 
			String tipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			
			 if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				 Tempresa oTempresa=new Tempresa();
				 String ruc = programa.getRuc();
				 String strEmpresaPrincipal=programa.getNombreGrupoEmpresa();
				 oTempresa.setCodEmpresa(ruc);
				 oTempresa.setNomEmpresa(strEmpresaPrincipal);
				 listaEmpresaobj.add(oTempresa );
				 
			}else{
					listEmpresatmp =listaEmpresas;	
					if (listEmpresatmp!=null && listEmpresatmp.size()>0) {
						for (Empresa listaempr:listEmpresatmp){
							Tempresa oTempresa1 = new Tempresa();
							oTempresa1.setCodEmpresa(listaempr.getRuc());
							oTempresa1.setNomEmpresa(listaempr.getNombre());
							listaEmpresaobj.add(oTempresa1);
						}
					}
			}
							
			List<OpcionPool> listaopcionPoolEmp= new ArrayList<OpcionPool>();
			List<Tempresa> listaTarget = new ArrayList<Tempresa>();
			List<Tempresa> listaSource = new ArrayList<Tempresa>(); 
			
			listaopcionPoolEmp=relacionesBancariasBO.findOpcionPool(idprograma, Constantes.ID_TIPO_OPPOOL_EMPRESA);
			if (listaopcionPoolEmp!=null && listaopcionPoolEmp.size()>0){
				for (OpcionPool opcionPool: listaopcionPoolEmp){
					Tempresa otempresa=new Tempresa();
					otempresa.setCodEmpresa(opcionPool.getCodOpcionPool());
					otempresa.setNomEmpresa(opcionPool.getDescOpcionPool());
					listaTarget.add(otempresa);
				}
			}			 
			
			boolean flagEncontrado = false;
			for(Tempresa  asignado  :listaTarget ){
				for(Tempresa empresa : listaEmpresaobj){
					if(asignado.getCodEmpresa().equals(empresa.getCodEmpresa())){
						flagEncontrado = true;
						break;
					}
				}
				if(flagEncontrado){										
					listaSource.add(asignado);
				}
				flagEncontrado=false;
			}		
			listaEmpresaSelect=listaSource;	
			
			List<Tbanco> listaTargetbanco = new ArrayList<Tbanco>();			
			
			listaopcionPool=relacionesBancariasBO.findOpcionPool(idprograma, Constantes.ID_TIPO_OPPOOL_BANCO);
			if (listaopcionPool!=null && listaopcionPool.size()>0){
				for (OpcionPool opcionPool: listaopcionPool){
					Tbanco obanco=new Tbanco();
					obanco.setCodBanco(opcionPool.getCodOpcionPool());
					obanco.setNombreBanco(opcionPool.getDescOpcionPool());
					listaTargetbanco.add(obanco);
				}
			}
			listaBancoSelect=listaTargetbanco;
			
			relacionesBancariasBO.setPrograma(oprograma);
			relacionesBancariasBO.setCodTipoDeudas(ArraycodTipoDeudas);
			relacionesBancariasBO.setListaEmpresaSelect(listaEmpresaSelect);
			relacionesBancariasBO.setListaBancoSelect(listaBancoSelect);			
			relacionesBancariasBO.savePoolBancario();
			
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	public void actualizarPropuestaRiesgo(Programa oprograma,List<Empresa> listaEmpresas){	
		List<Empresa> listaEmpresasfin = new ArrayList<Empresa>();
		try {						
			List<EstructuraLimite> olistEstructuraLimite = propuestaRiesgoBO.findEstructuraLimiteByIdprograma(oprograma.getId());
			String ptipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(ptipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){			
				listaEmpresas = new ArrayList<Empresa>();
				Empresa oempresaPrincipalPR = new Empresa();
				oempresaPrincipalPR.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipalPR.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasfin.add(oempresaPrincipalPR);				
			}else{
				listaEmpresasfin= listaEmpresas;
			}			
			if (olistEstructuraLimite!=null && olistEstructuraLimite.size()>0){
				for (EstructuraLimite oestructuraLimite: olistEstructuraLimite){					
					if (!validarEmpresasUpdatePR(oestructuraLimite,listaEmpresasfin)){	
						propuestaRiesgoBO.delete(oestructuraLimite);
					}
				}				
			}	
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	private boolean validarEmpresasUpdatePR(EstructuraLimite oestructuraLimite,List<Empresa> lista){
		boolean bexiste=false;
		try {	
				if (lista!=null && lista.size()>0){
					for(Empresa empresa : lista){
						if(empresa.getNombre().trim().equals(oestructuraLimite.getEmpresa().getNombre().trim())){
							bexiste=true;
							break;
						}
					}
				}else{
					bexiste=false;
				}
			
		} catch (Exception e) {
			bexiste=false;
		}		
			return bexiste;
	}
	
	public void actualizarcopiarAnexo(Programa oprograma,List<Empresa> listaEmpresas){
	
		List<Empresa> listaEmpresasfin = new ArrayList<Empresa>();
		try {		
					
			List<FilaAnexo> olistaFilaAnexos=anexoBO.findAnexosByPrograma(oprograma);			
			UsuarioSesion ousurio=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
				
			String ptipoEmpresa = oprograma.getTipoEmpresa().getId().toString();
			if(ptipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){			
				listaEmpresas = new ArrayList<Empresa>();
				Empresa oempresaPrincipalPR = new Empresa();
				oempresaPrincipalPR.setCodigo(oprograma.getIdEmpresa());
				oempresaPrincipalPR.setNombre(oprograma.getNombreGrupoEmpresa());
				listaEmpresasfin.add(oempresaPrincipalPR);				
			}else{
				listaEmpresasfin= listaEmpresas;
			}
			
			
			if(olistaFilaAnexos != null && olistaFilaAnexos.size()>0){
				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadreoperacion=null;
				
				for(FilaAnexo filaAnexo : olistaFilaAnexos){
					
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						lpadrebanco=filaAnexo.getAnexo().getId();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(2)){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getId();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3)) || (filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadreoperacion=filaAnexo.getAnexo().getId();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadreoperacion);						
					}
					logger.info("Asignacion de padre a Anexos:");
					logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getId() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	
				}
				
				List<FilaAnexo> olistaFilaAnexosexcl=new ArrayList<FilaAnexo>();
				List<FilaAnexo> olistaFilaAnexosexcltfin=new ArrayList<FilaAnexo>();
				
				olistaFilaAnexosexcl=listadoEmpresaExcluidas(olistaFilaAnexos,listaEmpresasfin);
				if (olistaFilaAnexosexcl!=null && olistaFilaAnexosexcl.size()>0){
					for (FilaAnexo xfilaAnexo: olistaFilaAnexosexcl){
						olistaFilaAnexosexcltfin.add(xfilaAnexo);
						buscarHijos(xfilaAnexo.getAnexo().getId(),olistaFilaAnexos,olistaFilaAnexosexcltfin);					
					}	
					
				}
				for (FilaAnexo afilaAnexo: olistaFilaAnexosexcltfin){					
					logger.info("actualizarAnexo:::Anexo Excluidos:");
					logger.info("Descripcion Anexo:"+ afilaAnexo.getAnexo().getDescripcion() +"::" 
							+ "id:"+ afilaAnexo.getAnexo().getId() +"::"
							+ "idpadre:"+ afilaAnexo.getAnexo().getPadretmp());	
				}
				List<FilaAnexo> olistFilaAnexoCopia = new ArrayList<FilaAnexo>();
				int posicion=0;
				for(FilaAnexo filaAnexo : olistaFilaAnexos){
					if (!validarEmpresaAGuardarAnexo(filaAnexo,olistaFilaAnexosexcltfin)){
						Anexo anexo = filaAnexo.getAnexo();
						anexo.setId(null);
						anexo.setPosFila(posicion);
						anexo.setPrograma(oprograma);					
						for(AnexoColumna ac : filaAnexo.getListaAnexoColumna()){
							ac.setId(null);
							ac.setAnexo(anexo);						
						}
						olistFilaAnexoCopia.add(filaAnexo);
						posicion=posicion+1;
					}
					
				}			
								
				anexoBO.setPrograma(oprograma);
				anexoBO.setUsuarioSession(ousurio);				
				anexoBO.setListaFilaAnexos(olistFilaAnexoCopia);
				anexoBO.saveAnexos();	
			}	
			
			List<AnexoGarantia> olistaGarantiaAnexo=anexoGarantiaBO.findAnexoXPrograma(oprograma);
			if (olistaGarantiaAnexo!=null && olistaGarantiaAnexo.size()>0){
				for (AnexoGarantia oanexoGarantia: olistaGarantiaAnexo){	
					String nombreEmpresa=oanexoGarantia.getEmpresa();
					if (!validarEmpresaAGuardarByNombre(nombreEmpresa,listaEmpresasfin)){						
						anexoGarantiaBO.delete(oanexoGarantia);
					}
				
				}				
			}		
			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));		
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}			
		
	}
	
	//fin MCG20121121
	
	
	
	
	/**
	 * Esta opcion es usada para poner en estado concluido un programa
	 * financiero
	 */
	public String conluirPrograma(){
		String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
		try {
			Long codmotivo=Long.parseLong(getCodigoMotivoCierre());			 
			String strobservacion=getObservacionCierre();
		
			
			Calendar ocalendar = Calendar.getInstance();							
			String strDia = "0" + (ocalendar.get(Calendar.DATE));
			String strMes = "0" + (ocalendar.get(Calendar.MONTH) + 1);
			String strAno = "" + ocalendar.get(Calendar.YEAR);	
			int hora24 = ocalendar.get(Calendar.HOUR_OF_DAY);			
			int minutos = ocalendar.get(Calendar.MINUTE);
			int segundos = ocalendar.get(Calendar.SECOND);
			String hora="";
			String minuto="";
			String segundo="";
		
			String meridiano = " AM";
			if(hora24 > 12){
				hora24 -= 12; 
				meridiano = " PM";
				hora=String.valueOf(hora24);
			}else if(hora24 == 12){				
				meridiano = " PM";
				hora=String.valueOf(hora24);
			}
			if (hora24 < 10) {hora = "0" + hora24;}else {hora=""+hora24;}
			if (minutos < 10) {minuto = "0" + minutos;}else {minuto=""+minutos;}
			if (segundos < 10) {segundo = "0" + segundos;}else {segundo=""+segundos;}
			String puntos = ":";
			String horita = hora + puntos + 
							minuto +puntos+
							segundo+ 
							meridiano;
			
			String strFechaCierre= "";
			strFechaCierre = (strDia.substring(strDia.length() - 2))+"/"+ 
								(strMes.substring(strMes.length() - 2)) +"/"+ 
								strAno.substring(0, 4) +" "+ horita ;
			
			String usuarioCierre="";
			if (getObjectSession(Constantes.USUARIO_SESSION)!=null){				
				UsuarioSesion usuarioSession =(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
				usuarioCierre=usuarioSession.getRegistroHost();
			}
						
			programaBO.conluirPrograma2(Long.parseLong(idPrograma),codmotivo,strobservacion,strFechaCierre,usuarioCierre);
			addActionMessage("Se cambio el estado del Programa a CERRADO");
		} catch (NumberFormatException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "consultasModificaciones";
	}
	
	//ini MCG20130703
	
	public boolean validarRVGL(String idprograma){
		
		Programa oprograma=new Programa();
		boolean res=true;
		try {
			oprograma = programaBO.findById(Long.valueOf(idprograma));
			List<ReporteCredito> olistaReporteCredito=new ArrayList<ReporteCredito>();
			olistaReporteCredito=reporteCreditoBO.getListaReporteCredito(oprograma);
			
			if (olistaReporteCredito!=null && olistaReporteCredito.size()>0){
				for (ReporteCredito oreporteCredito: olistaReporteCredito){
					if ((oreporteCredito.getNumeroRVGL()==null)
					|| (oreporteCredito.getNumeroRVGL()!=null && oreporteCredito.getNumeroRVGL().toString().trim().equals(""))){
						res=false;
					}
				}
			}
			
			if ((oprograma.getNumeroRVGL()==null)
					|| (oprograma.getNumeroRVGL()!=null && oprograma.getNumeroRVGL().toString().trim().equals(""))){
						res=false;
			}
	
		} catch (BOException e) {
			res=true;
		}	
		return res;
		
	}
	
	private boolean validadRolConcluir(){
		boolean activo=false;
		try {
			String activoconcluir=(String)getObjectRolSession(Constantes.COD_ROL_CONCLUIR); 
			if (activoconcluir.equals("1")){
				activo=true;
			}
		} catch (Exception e) {
			activo=true;
			logger.error(StringUtil.getStackTrace(e));
		}
		return activo;
		
	}
	
	public void loadListaMotivo(){			
		try {	
				listaMotivoCierre = tablaBO.listarHijos(Constantes.ID_TABLA_TIPO_MOTIVO);
				String idPrograma = this.getIdprogramaCierre();
				retornarMotivos(listaMotivoCierre,idPrograma);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
	}

	
	public void retornarMotivos(List<Tabla> olistaMotivoCierre,String idPrograma){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbRemmotivos\">");
        stb.append("<tbody>");        
        stb.append("<select name=\"codMotivoCierre\" id=\"codMotivoCierre\">");
		for(Tabla omotivo: olistaMotivoCierre){			
			String codmotivo=omotivo.getId()==null?"":omotivo.getId().toString();
			String motivo=omotivo.getDescripcion()==null?"":omotivo.getDescripcion().toString();		
			stb.append("<option value="+codmotivo+" >"+ motivo +"</option>");
		}
		stb.append("</select>");
		stb.append("</tbody>");
		stb.append("</table>");
		if (validarRVGL(idPrograma)){			
			if (validadRolConcluir()){
				out.print(stb.toString());
			}else{
				out.print("NOROL");
			}
		}else{
			out.print("NO");        	
        }
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	
	//fin MCG20130703
	
	
	//ini MCG20141023
	public void loadListaTipoOperacion(){			
		try {	
				listaTipoOperacion = tablaBO.listarHijos(Constantes.ID_TABLA_TIPO_OPERACION);
				
				retornarTipoOperacion(listaTipoOperacion);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
	}

	
	public void retornarTipoOperacion(List<Tabla> olistaTipoOperacion){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbtipoOperacion\">");
        stb.append("<tbody>");        
        stb.append("<select name=\"codTipoOperacion\" id=\"codTipoOperacion\">");
		for(Tabla otipoOperacicon: olistaTipoOperacion){			
			String codtipoOperacion=otipoOperacicon.getId()==null?"":otipoOperacicon.getId().toString();
			String tipoOperacion=otipoOperacicon.getDescripcion()==null?"":otipoOperacicon.getDescripcion().toString();		
			stb.append("<option value="+codtipoOperacion+" >"+ tipoOperacion +"</option>");
		}
		stb.append("</select>");
		stb.append("</tbody>");
		stb.append("</table>");		
		out.print(stb.toString());
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	
	public void loadListaEmpresaRC(){			
		try {	
				String idPrograma=getIdprogramaEmpresaRC();
				List<Empresa> listaEmpresas=new ArrayList<Empresa>();
				listaEmpresas = empresaBO.listarEmpresasPorPrograma(Long.valueOf(idPrograma));			
				retornarEmpresaRC(listaEmpresas);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
	}

	
	public void retornarEmpresaRC(List<Empresa> listaEmpresa){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbempresaRC\">");
        stb.append("<tbody>");        
        stb.append("<select name=\"codEmpresaRC\" onchange=\"obtenerFileRC()\" id=\"selcodEmpresaRC\">");
		for(Empresa oempresa: listaEmpresa){			
			String codigoCentral=oempresa.getCodigo()==null?"":oempresa.getCodigo().toString();
			String nombreEmpresa=oempresa.getNombre()==null?"":oempresa.getNombre().toString();		
			stb.append("<option value="+codigoCentral+" >"+ nombreEmpresa +"</option>");
		}
		stb.append("</select>");
		stb.append("</tbody>");
		stb.append("</table>");		
		out.print(stb.toString());
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	
	public void loadTablaArchivoRC(){			
		try {	
				String idPrograma=getIdprogramaEmpresaRC();
				Programa oprograma=new Programa();
				oprograma.setId(Long.valueOf(idPrograma));
				String codEmpresaRC=getCodEmpresaRC();
				List<ArchivoReporteCredito> listaArchivosReporteCredito = new ArrayList<ArchivoReporteCredito>();
				listaArchivosReporteCredito=reporteCreditoBO.findListaArchivoReporteCredito(oprograma,codEmpresaRC);
				
				retornarTablaArchivoRC(listaArchivosReporteCredito);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
	}
	
	public void retornarTablaArchivoRC(List<ArchivoReporteCredito> listaArchivosRC){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {
    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbRemEmpresaRC\">");
			stb.append("<thead>");
		        stb.append("<tr>");
			        stb.append("<th>#</th>");
			        stb.append("<th>Nombre</th>");
			        stb.append("<th>Extensión</th>");
			        stb.append("<th>Descarga</th>");
		        stb.append("</tr>");
	        stb.append("</thead>");
        stb.append("<tbody>");
       
		for(ArchivoReporteCredito oarchivoReporteCredito: listaArchivosRC){
			contador++;
			String idReporteCredito=oarchivoReporteCredito.getId()==null?"":oarchivoReporteCredito.getId().toString();
			String nombre=oarchivoReporteCredito.getNombreArchivo()==null?"":oarchivoReporteCredito.getNombreArchivo().toString();
			String extension=oarchivoReporteCredito.getExtencion()==null?"":oarchivoReporteCredito.getExtencion().toString();
		
			stb.append("<tr>");
				stb.append("<td>"+contador+"</td>");
				stb.append("<td align=\"left\">"+(nombre)+"</td>");
				stb.append("<td align=\"center\">"+(extension)+"</td>");
				stb.append("<td align=\"center\">");
					stb.append("<a href=\"javascript:descargarArchivoRC('"+(idReporteCredito)+"','"+(extension)+"','"+(nombre)+"');\">");
						stb.append(" <img src=\"imagentabla/bbva.documentoAzul24.png\" border=\"0\" alt=\"download\">" );
					stb.append("</a>");
				stb.append("</td>");
			stb.append("</tr>");

		}
		stb.append("</tbody>");
		 stb.append("</table>");
		
        out.print(stb.toString());
		
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	

	//ini MCG20150112
	@Action(value = "findEmpresaHostByGrupo")
	public void findEmpresaHostByGrupo(){			
		try {	
			List<Empresa> listaEmpresas = new ArrayList<Empresa>();
			if (getObjectSession("listaEmpresasGrupo")!=null){
				listaEmpresas = (List<Empresa>)getObjectSession("listaEmpresasGrupo");
			}
			String idprogramaIni=getIdprogramaCopia();
			String tipoCopia=getTipoCopiaCopia();
			List<Empresa> listaEmpresasini=empresaBO.listarEmpresasPorPrograma(Long.valueOf(idprogramaIni));
			if (listaEmpresasini!=null && listaEmpresasini.size()>0){
				for (Empresa oempresaini:listaEmpresasini){
					for (Empresa oempresafin:listaEmpresas){
						if ((oempresaini.getTipoGrupo()==null) ||(oempresaini.getTipoGrupo()!=null && oempresaini.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL))){
							
							if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
								oempresafin.setCheckedEmpPrincipal("checked");
							}
						
						} 						
						if (oempresaini.getCodigo().equals(oempresafin.getCodigo())){
							oempresafin.setCheckedEmpSeleccionada("checked");
						}						
					}
					
				}				
			}
			
			
				retornarEmpresaHost(listaEmpresas,idprogramaIni,tipoCopia);
		
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}	
	}
	public void retornarEmpresaHost(List<Empresa> listaEmpresas,String idprogramaIni,String tipoCopia ){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {
    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbRemEmpresaHost\">");
			stb.append("<thead>");
		        stb.append("<tr>");
		        	stb.append("<th width='30px'></th>");
			        stb.append("<th width='50px' >#</th>");
			        stb.append("<th>Codigo</th>");
			        stb.append("<th>Ruc</th>");
			        stb.append("<th width='400px'>Nombre</th>");
			        stb.append("<th>Emp.Principal</th>");
			        stb.append("<th>Seleccionar</th>");
		        stb.append("</tr>");
	        stb.append("</thead>");
        stb.append("<tbody>");
       
		for(Empresa oempresa: listaEmpresas){
			contador++;
			String idEmpresa=oempresa.getId()==null?"":oempresa.getId().toString();
			String codigo=oempresa.getCodigo()==null?"":oempresa.getCodigo().toString();
			String ruc=oempresa.getRuc()==null?"":oempresa.getRuc().toString();
			String nombre=oempresa.getNombre()==null?"":oempresa.getNombre().toString();
		
			String checkedEmpresa=oempresa.getCheckedEmpSeleccionada()==null?"":oempresa.getCheckedEmpSeleccionada().toString();
			String checkedEmpresaPrincipal=oempresa.getCheckedEmpPrincipal()==null?"":oempresa.getCheckedEmpPrincipal().toString();
			
			List<Programa> olistaPrograma =new ArrayList<Programa>();
			olistaPrograma=programaBO.listarProgramasByCodigoCentral(codigo,idprogramaIni);
			String addAccionRadio="";
			if (tipoCopia.equals("3")){
				addAccionRadio="onclick='setEmpresaPrincipal(\""+(codigo)+"\",\""+(nombre)+"\");'";
			}
			if (checkedEmpresa.equals("checked")){
			stb.append("<tr bgcolor='#E0ECF8'>");
			}else{
				stb.append("<tr>");
			}
				stb.append("<td width='30px' align=\"center\">");
				
					if (!checkedEmpresa.equals("checked")){
						stb.append("<a style=\"text-decoration: none;\" id='idAver"+(codigo)+"' href='#' onclick='toggle_visibility(\"idDivEmpresa"+codigo+"\",\"idAver"+codigo+"\");'><font size='5'>+</font></a>" );
					}
				
				stb.append("</td>");
				stb.append("<td width='50px' align=\"center\">"+contador+"</td>");
				stb.append("<td align=\"left\">"+(codigo)+"</td>");
				stb.append("<td align=\"left\">"+(ruc)+"</td>");
				stb.append("<td width='400px' align=\"left\">"+(nombre)+"</td>");
				stb.append("<td align=\"center\">");
					stb.append("<input type='radio'  "+addAccionRadio+"  id='idRadioEmpresaHost"+codigo+"' name='radioSelectEmpresa' "+ (checkedEmpresaPrincipal)+" value='"+(codigo)+"'/>");
				stb.append("</td>");
				stb.append("<td align=\"center\">");
					if (!tipoCopia.equals("3")){
					stb.append("<input id='idCheckEmpresaHost"+codigo+"' name='chksSelectEmpresa' type='checkbox' "+ (checkedEmpresa)+" value='"+(codigo)+"'/>");
					}
				stb.append("</td>");
			stb.append("</tr>");
			stb.append("<tr>");	
			stb.append("<td>");
			stb.append("</td>");
			stb.append("<td colspan='6'>");
			//ini detalle
			  stb.append("<div  style=\"display:none\" id=\"idDivEmpresa"+(codigo)+"\">");
			        stb.append("<table  id=\"tbRemEmpresaHost\">");
					stb.append("<thead>");
				        stb.append("<tr>");
					        stb.append("<th width='50px'>Programa</th>");
					        stb.append("<th width='100px'>Numero solicitud</th>");
					        stb.append("<th width='80px'>fecha creacion</th>");
					        stb.append("<th width='150px'>NombreGrupoEmpresa</th>");
					        stb.append("<th width='80px'>Id Grupo</th>");
					        stb.append("<th width='80px'>Emp.Principal</th>");	
					        stb.append("<th width='80px'>Tipo Empresa</th>");
					        stb.append("<th>Seleccionar</th>");
				        stb.append("</tr>");
			        stb.append("</thead>");
			        stb.append("<tbody>");
			        for(Programa oprograma: olistaPrograma){						
						String idprograma=oprograma.getId()==null?"":oprograma.getId().toString();
						String nsolicitud=oprograma.getNumeroSolicitud()==null?"":oprograma.getNumeroSolicitud().toString();
						String fechacreacion=oprograma.getFechaCreacionFormato()==null?"":oprograma.getFechaCreacionFormato().toString();
						String nombreGrupoEmpresa=oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
						String idGrupo=oprograma.getIdGrupo()==null?"":oprograma.getIdGrupo().toString();
						String empresaPrin=oprograma.getIdEmpresa()==null?"":oprograma.getIdEmpresa().toString();
						String tipoEmpresa=oprograma.getTipoEmpresa().getDescripcion()==null?"":oprograma.getTipoEmpresa().getDescripcion().toString();
						String radiocheckedPrograma="";
						if (idprograma.equals(idprogramaIni)){
							radiocheckedPrograma="checked";
						}
							if (radiocheckedPrograma.equals("checked")){
							stb.append("<tr bgcolor='#E0ECF8'>");
							}else{
								stb.append("<tr>");
							}
						
							stb.append("<td>"+idprograma+"</td>");
							stb.append("<td align=\"left\">"+(nsolicitud)+"</td>");
							stb.append("<td align=\"left\">"+(fechacreacion)+"</td>");
							stb.append("<td align=\"left\">"+(nombreGrupoEmpresa)+"</td>");
							stb.append("<td align=\"left\">"+(idGrupo)+"</td>");
							stb.append("<td align=\"left\">"+(empresaPrin)+"</td>");
							stb.append("<td align=\"left\">"+(tipoEmpresa)+"</td>");							
							stb.append("<td align=\"center\">");
								stb.append("<input type='radio' id='idRadioPrograma"+idprograma+"' name='radioSelectPrograma"+codigo+"' "+ (radiocheckedPrograma)+" value='"+(idprograma)+"'/>");
							stb.append("</td>");
						stb.append("</tr>");
			        
			        }
			        stb.append("</tbody>");
					stb.append("</table>");
			stb.append("</div>");       
			//fin detalle
			stb.append("</td>");
			stb.append("<tr>");

		}
		stb.append("</tbody>");
		 stb.append("</table>");
		
        out.print(stb.toString());
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	//fin MCG20150112
	private ConfiguracionWSPe21 getConfiguracionWSPE21(String codigoUsuario){
		ConfiguracionWSPe21 oConfiguracion=new ConfiguracionWSPe21();
		List<Tabla> tablasHijos = new ArrayList<Tabla>();
		
		try {	
			
		tablasHijos=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_VALOR_WSPE21);		
		oConfiguracion.setCodigoUsuario(codigoUsuario);
		oConfiguracion.setOpcionAplicacion(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_OPCION_APLICACION			,tablasHijos)	);
		oConfiguracion.setTerminalLogico(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TERMINAL_LOGICO			,tablasHijos)	);
		oConfiguracion.setTerminalContable(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TERMINAL_CONTABLE			,tablasHijos)	);
		
		oConfiguracion.setPe21Transaccion(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TRANSACCION			,tablasHijos)	);
		oConfiguracion.setPe21TipoMensaje(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TIPO_MENSAJE			,tablasHijos)	);
		oConfiguracion.setPe21TipoProceso(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TIPO_PROCESO			,tablasHijos)	);
		oConfiguracion.setPe21CanalComunicacion(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_CANAL_COMUNICACION	,tablasHijos)	);
		oConfiguracion.setPe21IndicadorPreformato(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_INDICADOR_PREFORMATO	,tablasHijos)	);
		oConfiguracion.setPe21TipoMensajeMe(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_TIPO_MENSAJE_ME		,tablasHijos)	);
		oConfiguracion.setPe21DistQnameIn(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_DIST_QNAME_IN		,tablasHijos)	);
		oConfiguracion.setPe21DistRqnameOut(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_DIST_RQNAME_OUT		,tablasHijos)	);
		oConfiguracion.setPe21HostRqnameOut(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_HOST_RQNAME_OUT		,tablasHijos)	);
		oConfiguracion.setPe21HostQmgrName(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_HOST_QMGR_NAME		,tablasHijos)	);

		oConfiguracion.setPe21IpPort(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_IP_PORT				,tablasHijos)	);
		oConfiguracion.setPe21Encoding(obtenerValorHijo(Constantes.COD_HIJO_WSPE21_ENCODING				,tablasHijos)	);
		 

		} catch (Exception e) {
			 
		}
		
		return oConfiguracion;
		
	}
	
	public String obtenerValorHijo(String codigoHijo,List<Tabla> tablasHijos){	
		
		String valorHijo="";				
		try {				
			if(tablasHijos!=null && tablasHijos.size()>0){
				for (Tabla otabla:tablasHijos){
					if (codigoHijo!=null && codigoHijo.toString().trim().equals(otabla.getCodigo().toString().trim())){
						valorHijo=otabla.getAbreviado()==null?"":otabla.getAbreviado();
						break;
					}				
				}	
			}		
		} catch (Exception e) {
			valorHijo="";
		}
		return valorHijo;
	}
	
	
	public void validarRenderBlob(){
		logger.info("Guardando blog");
		
		logger.info("valorBlob="+valorBlob);
		StringBuilder stbvalor = new StringBuilder(); 
		String stbresultado = "OK"; 
		
        
		try {
			getResponse().setContentType("text/html"); 
			PrintWriter out = getResponse().getWriter(); 	
			String patronesEditor="";
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				patronesEditor=(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				patronesEditor="";
			}
			String texto = ExcelHelper.cleanText(valorBlob,patronesEditor );
			stbvalor.append(texto);
			
			String cadenahtml ="";
			
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
		 				" </style>"; 
			try {
		 	    InputStream IS=new StringBufferInputStream(stbvalor.toString());
		 	   ByteArrayOutputStream OS = new ByteArrayOutputStream();
			    Tidy T =new Tidy();
			    T.setXHTML(true);
			    T.setPrintBodyOnly(true);
			    T.setShowWarnings(false);	       
			    T.parseDOM(IS,OS); 
			    String salida=OS.toString();
				if(salida.length()==0){ throw new Exception("Problema de conversion");}
			    
			    StringBuilder sbcomentario= new StringBuilder();
			    
			    String refe="<head> " + css +" </head>";
			    
			    sbcomentario.append( " <table class=\"gridtable\" width=\"100%\">" 
		        		+ " <tr>" 		        		
		        		+ " 	<td class=\"myEditor\">" + OS.toString() + "</td>"
		        		+ " </tr>"    
		        		+ " </table>");	
			    
			     cadenahtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
				 	 	   +" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
				 		    + "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
				 		   +"<html xmlns=\"http://www.w3.org/1999/xhtml\">"	    	
				 		   + refe + " <body>" + 
				 				    "<table align=\"center\">"+
				 			    		"<tr>"+
				 			    			"<td>"+
				 			    			"<div style=\"width: 1000px; overflow:hidden margin:0;padding:0px; \">"+//div para controlar desborde
				 			    			sbcomentario.toString()+
				 			    			"</div>"+
				 			    			"</td>"+
				 		    			"</tr>"+
				 					"</table>"+
				 		    	
				 		    	" </body> </html> ";
			     
			     //cadenahtml= OS.toString(); 
			     
			     //logger.error("cadenahtml Valida:"+cadenahtml);
			} catch (Exception e) {					
				logger.error(StringUtil.getStackTrace(e));
				stbresultado="ERROR999999";
			}
	    
		    if (!cadenahtml.equals("")){	
			        try{
			        ITextRenderer rendererV = new ITextRenderer();        
			        rendererV.setDocumentFromString(cadenahtml);
			        rendererV.layout();
			        } catch (Exception e) {			
						logger.error(StringUtil.getStackTrace(e));
						stbresultado="ERROR999999";
					}      	
		    }
	        
	        out.print(stbresultado);
	        
	        
	
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));			
			
		}
		
		 
	}

	
	public void activarSession(){
		logger.info("Activando la session");
	}
	
	/**
	 * Metodo que sera usado para consultar un campo blog mediante ajax
	 * 
	 */
	public void consultarBlob(){
		logger.info("Metodo consultando datos del blob");
		logger.info(getRequest().getParameterMap());
	}
	
	public String findSIIUs(){
		return "programa";
	}
	
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
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

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getTipoPrograma() {
		return tipoPrograma;
	}

	public void setTipoPrograma(String tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

	public String getCodigoEmpresaGrupo() {
		return codigoEmpresaGrupo;
	}

	public void setCodigoEmpresaGrupo(String codigoEmpresaGrupo) {
		this.codigoEmpresaGrupo = codigoEmpresaGrupo;
	}


	public String getPais() {
		return pais;
	}

	
	public void setPais(String pais) {
		this.pais = pais;
	}
	

	public List getListaPlanilla() {
		return listaPlanilla;
	}

	public void setListaPlanilla(List listaPlanilla) {
		this.listaPlanilla = listaPlanilla;
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

	
	public String execute() throws Exception{
 
		return SUCCESS;
	}
 
	public String display() {
		return NONE;
	}

	public String getCampoBlob() {
		return campoBlob;
	}

	public void setCampoBlob(String campoBlob) {
		this.campoBlob = campoBlob;
	}

	public String getValorBlob() {
		return valorBlob;
	}

	public void setValorBlob(String valorBlob) {
		this.valorBlob = valorBlob;
	}

	public Empresa getEmpresaPrincipal() {
		return empresaPrincipal;
	}

	public void setEmpresaPrincipal(Empresa empresaPrincipal) {
		this.empresaPrincipal = empresaPrincipal;
	}

	public String getTipoDocBusqueda() {
		return tipoDocBusqueda;
	}

	public void setTipoDocBusqueda(String tipoDocBusqueda) {
		this.tipoDocBusqueda = tipoDocBusqueda;
	}

	public List<Empresa> getListaGrupoEmpresas() {
		return listaGrupoEmpresas;
	}

	public void setListaGrupoEmpresas(List<Empresa> listaGrupoEmpresas) {
		this.listaGrupoEmpresas = listaGrupoEmpresas;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getIdEstadoPrograma() {
		return idEstadoPrograma;
	}

	public void setIdEstadoPrograma(String idEstadoPrograma) {
		this.idEstadoPrograma = idEstadoPrograma;
	}

	public String getTipoMetodo() {
		return tipoMetodo;
	}

	public void setTipoMetodo(String tipoMetodo) {
		this.tipoMetodo = tipoMetodo;
	}

	public String getTipoDocBusquedagrupo() {
		return tipoDocBusquedagrupo;
	}

	public void setTipoDocBusquedagrupo(String tipoDocBusquedagrupo) {
		this.tipoDocBusquedagrupo = tipoDocBusquedagrupo;
	}

	public List<Empresa> getListaEmpresasGrupoDB() {
		return listaEmpresasGrupoDB;
	}

	public void setListaEmpresasGrupoDB(List<Empresa> listaEmpresasGrupoDB) {
		this.listaEmpresasGrupoDB = listaEmpresasGrupoDB;
	}

	public List<CabTabla> getListaCabTablaPlanilla() {
		return listaCabTablaPlanilla;
	}

	public void setListaCabTablaPlanilla(List<CabTabla> listaCabTablaPlanilla) {
		this.listaCabTablaPlanilla = listaCabTablaPlanilla;
	}

	public List<CabTabla> getListaCabTablaCompra() {
		return listaCabTablaCompra;
	}

	public void setListaCabTablaCompra(List<CabTabla> listaCabTablaCompra) {
		this.listaCabTablaCompra = listaCabTablaCompra;
	}

	public List<CabTabla> getListaCabTablaVenta() {
		return listaCabTablaVenta;
	}

	public void setListaCabTablaVenta(List<CabTabla> listaCabTablaVenta) {
		this.listaCabTablaVenta = listaCabTablaVenta;
	}

	public List<CabTabla> getListaCabTablaActividad() {
		return listaCabTablaActividad;
	}

	public void setListaCabTablaActividad(List<CabTabla> listaCabTablaActividad) {
		this.listaCabTablaActividad = listaCabTablaActividad;
	}

	public List<CabTabla> getListaCabTablaNegocio() {
		return listaCabTablaNegocio;
	}

	public void setListaCabTablaNegocio(List<CabTabla> listaCabTablaNegocio) {
		this.listaCabTablaNegocio = listaCabTablaNegocio;
	}

	public String getCodigoMotivoCierre() {
		return codigoMotivoCierre;
	}

	public void setCodigoMotivoCierre(String codigoMotivoCierre) {
		this.codigoMotivoCierre = codigoMotivoCierre;
	}

	public String getObservacionCierre() {
		return observacionCierre;
	}

	public void setObservacionCierre(String observacionCierre) {
		this.observacionCierre = observacionCierre;
	}

	public List<Tabla> getListaMotivoCierre() {
		return listaMotivoCierre;
	}

	public void setListaMotivoCierre(List<Tabla> listaMotivoCierre) {
		this.listaMotivoCierre = listaMotivoCierre;
	}

	public String getIdprogramaCierre() {
		return idprogramaCierre;
	}

	public void setIdprogramaCierre(String idprogramaCierre) {
		this.idprogramaCierre = idprogramaCierre;
	}

	public String getTipoDocBusquedaTodos() {
		return tipoDocBusquedaTodos;
	}

	public void setTipoDocBusquedaTodos(String tipoDocBusquedaTodos) {
		this.tipoDocBusquedaTodos = tipoDocBusquedaTodos;
	}

	public String getTipoCopiapf() {
		return tipoCopiapf;
	}

	public void setTipoCopiapf(String tipoCopiapf) {
		this.tipoCopiapf = tipoCopiapf;
	}

	public String getCodEmpresaPrinHidden() {
		return codEmpresaPrinHidden;
	}

	public void setCodEmpresaPrinHidden(String codEmpresaPrinHidden) {
		this.codEmpresaPrinHidden = codEmpresaPrinHidden;
	}

	public String getCopiarsinDataHidden() {
		return copiarsinDataHidden;
	}

	public void setCopiarsinDataHidden(String copiarsinDataHidden) {
		this.copiarsinDataHidden = copiarsinDataHidden;
	}
	

	public List<Tabla> getListaTipoOperacion() {
		return listaTipoOperacion;
	}

	public void setListaTipoOperacion(List<Tabla> listaTipoOperacion) {
		this.listaTipoOperacion = listaTipoOperacion;
	}

	public String getCodigoTipoOperacion() {
		return codigoTipoOperacion;
	}

	public void setCodigoTipoOperacion(String codigoTipoOperacion) {
		this.codigoTipoOperacion = codigoTipoOperacion;
	}

	public String getIdprogramaEmpresaRC() {
		return idprogramaEmpresaRC;
	}

	public void setIdprogramaEmpresaRC(String idprogramaEmpresaRC) {
		this.idprogramaEmpresaRC = idprogramaEmpresaRC;
	}

	public String getCodEmpresaRC() {
		return codEmpresaRC;
	}

	public void setCodEmpresaRC(String codEmpresaRC) {
		this.codEmpresaRC = codEmpresaRC;
	}

	
	public String getIdprogramaCopia() {
		return idprogramaCopia;
	}

	public void setIdprogramaCopia(String idprogramaCopia) {
		this.idprogramaCopia = idprogramaCopia;
	}

	public String getTipoCopiaCopia() {
		return tipoCopiaCopia;
	}

	public void setTipoCopiaCopia(String tipoCopiaCopia) {
		this.tipoCopiaCopia = tipoCopiaCopia;
	}

	public String getSelectedItemsEmpresa() {
		return selectedItemsEmpresa;
	}

	public void setSelectedItemsEmpresa(String selectedItemsEmpresa) {
		this.selectedItemsEmpresa = selectedItemsEmpresa;
	}

	public String getSelectedItemsPrime() {
		return selectedItemsPrime;
	}

	public void setSelectedItemsPrime(String selectedItemsPrime) {
		this.selectedItemsPrime = selectedItemsPrime;
	}

	public String getSelectedItemsEmpresaPrograma() {
		return selectedItemsEmpresaPrograma;
	}

	public void setSelectedItemsEmpresaPrograma(String selectedItemsEmpresaPrograma) {
		this.selectedItemsEmpresaPrograma = selectedItemsEmpresaPrograma;
	}


	
	
	
	
}
