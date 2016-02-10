package pe.com.bbva.iipf.pf.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.cpy.bo.PFSunatBO;
import pe.com.bbva.iipf.cpy.model.PFSunat;
import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.SelectItem;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.AnexoGarantiaBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.bo.ReporteCreditoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.ArchivoReporteCredito;
import pe.com.bbva.iipf.pf.model.ClaseCredito;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.Garantia;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.ReporteCredito;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.pf.model.SustentoOperacion;
import pe.com.bbva.iipf.util.ComboUtil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;
import pe.com.stefanini.core.util.ValidateUtil;

import com.opensymphony.xwork2.ActionContext;

@Service("reporteCreditoAction")
@Scope("prototype")
public class ReporteCreditoAction extends GenericAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;	
	@Resource
	private ReporteCreditoBO reporteCreditoBO;	
	@Resource
	private TablaBO tablaBO;	
	@Resource
	private DatosBasicosBO datosBasicosBO ;	
	@Resource
	ParametroBO parametroBO;	
	@Resource
	private RelacionesBancariasBO relacionesBancariasBO;	
	@Resource
	private PFSunatBO sunatBO;
	@Resource
	private RatingBO ratingBO;
	@Resource
	private PoliticasRiesgoBO politicasRiesgoBO;
	@Resource
	private AnexoBO anexoBO;
	
	@Resource
	private AnexoGarantiaBO anexoGarantiaBO;
	
	@Resource
	private DatosBasicosBlobBO  datosBasicosBlobBO;
	
	@Resource
	private SintesisEconomicoBlobBO  sintesisEconomicoBlobBO;
	
	@Resource
	private EmpresaBO empresaBO ;
	
	
	private Programa programa;
	/**gmp-20130410**/
	private SustentoOperacion sustento;
	private Long idSustento;
	/**gmp-20130410**/
	
	private List<SelectItem> itemSegmento;
	private List<Tabla> listaSegmentos = new ArrayList<Tabla>();
	private List<SelectItem> itemOficinaBEC;
	private List<Tabla> listaOficinaBEC = new ArrayList<Tabla>();
	
	private List<SelectItem> itemCuentaRDC;
	private List<Tabla> listaCuentaRDC = new ArrayList<Tabla>();
	
	private List<SelectItem> itemCuentaGarantiaRDC;
	private List<Tabla> listaCuentaGarantiaRDC = new ArrayList<Tabla>();
	
	private List<SelectItem> itemConectorGarantiaRDC;
	private List<Tabla> listaConectorGarantiaRDC = new ArrayList<Tabla>();
	
	private List<SelectItem> itemCuentaCreditoRDC;
	private List<Tabla> listaCuentaCreditoRDC = new ArrayList<Tabla>();
	
	private List<SelectItem> itemMonedaRDC;
	private List<Tabla> listaMonedaRDC = new ArrayList<Tabla>();
	/*vchn*/
	private List<SelectItem> itemReembolso;
	private List<Tabla> listaReembolso = new ArrayList<Tabla>();
	/*vchn*/
	private String ciiuRDC;
	private String ratingRDC;
	private String clasificacionBancoRDC;
	private String codigoCentralRDC;
	private String contratoRDC;
	
	private String idOficina;//eliminar luego
	private String idSegmento;//eliminar luego
	
	private List<ClaseCredito> listaClaseCredito = new ArrayList<ClaseCredito>();
	private String chkClaseCredito;	
	private boolean flagClaseCredito;
	
	
	
	private boolean flagGarantia;	
	private List<Garantia> listGarantia = new ArrayList<Garantia>();
	private String chksParaEliminar;
	
	private boolean flagSustento;
	private String chkSustento;	
	private List<SustentoOperacion> listaSustentoOperacion = new ArrayList<SustentoOperacion>();
		

	private List<Accionista> listaAccionistas = new ArrayList<Accionista>();
	private String totalAccionista;
	
	private boolean  flagDatosBasicosAdicionalesPrin;
	private boolean  flagSintesisEconFinAdicional;
	
	private boolean  flagPosicionCliente;	

	
	private String riesgovigenteRDC;
	private String incrementoRDC;
	private String riesgopropuestoRDC;
	private String tipoMilesPR;
	
	//add para multiple empresa
	List<Empresa> listaEmpresasGrupoRC = new ArrayList<Empresa>();
	private String codigoEmpresa; //para el listado de las empresas del grupo(Combo).
	private String codEmpresa; //para obtener el codigo de la empresa para los editores.
	private String flagChangeEmpresa;//para saber si es un submit del change del combo.
    private String codempresagrupo;//para obtener el codigo empresa por el submit del change del combo.
    
	/**
	 * variable para pasar el nombre del campo blob a registrarse
	 */
	private String campoBlob;
	/**
	 * variable que contendra el valor de campo blob
	 */
	private String valorBlob;
	
	private String deudaSBS;
	/**vchn-20130410**/
	private List<Tabla> listaVctoRblso= new ArrayList<Tabla>();
	private List<SelectItem> itemVctoRblso;
	private String tipoCambio;
	private String cancelado;
	/**vchn-20130410**/
	
	//mlj 20130411
	private String codEmpresaGrupoS ; // Para el guardar sustento 
	private String valorSustento;
	
	
	private Long ideSustento;
	private Long idCuentaSustento;
	private Long idCuentaFinalSustento;
	private String ordenSustento;
	private String ordenFinalSustento;
	private String nombreEmpresaRDC;
	
	private boolean flagArchivoReporteCredito;
	private File fileReporteCredito;
	private String fileReporteCreditoFileName;
	
	private String codigoArchivo;
	private String extension;
	private String nombreArchivo;

	private String contentType;
	private String contentDisposition;
	private InputStream fileInputStream;
	
	
	private List<ArchivoReporteCredito> listaArchivosReporteCredito = new ArrayList<ArchivoReporteCredito>();
	
	public String init(){
		try {
						
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			setPrograma(programaBO.findById(Long.valueOf(programaId)));
			//datosBasicosBO.setPrograma(getPrograma());
			
			//ini MCG20130402
			String tipoempresa=getPrograma().getTipoEmpresa().getId().toString();
			String codEmpresaGrupo="";			
			String flagchange=getFlagChangeEmpresa();
			
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					codEmpresaGrupo=getPrograma().getIdEmpresa().toString();				
					 						
			}else{									
					if (flagchange!=null && flagchange.equals("C")){
						listaSegmentos = new ArrayList<Tabla>();
						codEmpresaGrupo=getCodempresagrupo();									
					}else{
						codEmpresaGrupo=getPrograma().getIdEmpresa().toString();
					}
					Empresa empresa= new Empresa();	
					empresa=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);					
					if (empresa!=null && !empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
							
						   List<ReporteCredito> listReporteCredito=reporteCreditoBO.getListaReporteCredito(getPrograma(),codEmpresaGrupo);
						   if (listReporteCredito!=null && listReporteCredito.size()>0 ){
							   
							   ReporteCredito reportCredito =listReporteCredito.get(0);								   
							   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
							   
							   //desCalifBanco=odatosBasico.getCalificacionBanco();							   
							   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
							   getPrograma().setGestor(odatosBasico.getGestor());
							   getPrograma().setOficina(odatosBasico.getOficina());
							   getPrograma().setSegmento(odatosBasico.getSegmento());
							   
							   getPrograma().setActividadPrincipal(odatosBasico.getActividadPrincipal());
							   getPrograma().setAntiguedadNegocio(odatosBasico.getAntiguedadNegocio());
							   getPrograma().setAntiguedadCliente(odatosBasico.getAntiguedadCliente());
							   getPrograma().setGrupoRiesgoBuro(odatosBasico.getGrupoRiesgoBuro());
							   
							   getPrograma().setFechaRDC(reportCredito.getFechaRDC()==null?"":reportCredito.getFechaRDC().toString());							   
							   getPrograma().setCuentaCorriente(reportCredito.getCuentaCorriente()==null?"":reportCredito.getCuentaCorriente().toString());							   
							   getPrograma().setNumeroRVGL(reportCredito.getNumeroRVGL()==null?"":reportCredito.getNumeroRVGL().toString());
							   getPrograma().setRuc(empresa.getRuc());	
							   getPrograma().setCiiuRDC(reportCredito.getCiiuRDC());
							   getPrograma().setSalem(reportCredito.getSalem()==null?"":reportCredito.getSalem().toString());
							   	
							   
							   getPrograma().setVulnerabilidad(reportCredito.getVulnerabilidad()==null?"":reportCredito.getVulnerabilidad().toString());
							   getPrograma().setTotalInversion(reportCredito.getTotalInversion()==null?"":reportCredito.getTotalInversion().toString());
							   getPrograma().setMontoPrestamo(reportCredito.getMontoPrestamo()==null?"":reportCredito.getMontoPrestamo().toString());
							   getPrograma().setEntorno(reportCredito.getEntorno()==null?"":reportCredito.getEntorno().toString());
							   getPrograma().setPoblacionAfectada(reportCredito.getPoblacionAfectada()==null?"":reportCredito.getPoblacionAfectada().toString());
							   getPrograma().setCategorizacionAmbiental(reportCredito.getCategorizacionAmbiental()==null?"":reportCredito.getCategorizacionAmbiental().toString());
							   getPrograma().setComentarioAdmision(reportCredito.getComentarioAdmision()==null?"":reportCredito.getComentarioAdmision().toString());
							   

						   }else{
							   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
							   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
							   getPrograma().setGestor(odatosBasico.getGestor());
							   getPrograma().setOficina(odatosBasico.getOficina());
							   getPrograma().setSegmento(odatosBasico.getSegmento());							   
							   
							   getPrograma().setActividadPrincipal(odatosBasico.getActividadPrincipal());
							   getPrograma().setAntiguedadNegocio(odatosBasico.getAntiguedadNegocio());
							   getPrograma().setAntiguedadCliente(odatosBasico.getAntiguedadCliente());
							   getPrograma().setGrupoRiesgoBuro(odatosBasico.getGrupoRiesgoBuro());
							   
							   getPrograma().setFechaRDC("");							   
							   getPrograma().setCuentaCorriente("");							   
							   getPrograma().setNumeroRVGL("");
							   getPrograma().setRuc(empresa.getRuc());
							   getPrograma().setCiiuRDC("");
							   getPrograma().setSalem("");	
							   
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
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();
			sessionparam.remove(Constantes.COD_GRUPOEMPRESA_RDC_SESSION);
			sessionparam.put(Constantes.COD_GRUPOEMPRESA_RDC_SESSION, codEmpresaGrupo);
			//fin MCG20130402
			
			
			listaCuentaRDC= tablaBO.listarHijos(Constantes.ID_TABLA_CUENTA);
			itemCuentaRDC= ComboUtil.getSelectItems(listaCuentaRDC, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);
			
			listaCuentaGarantiaRDC= tablaBO.listarHijos(Constantes.ID_TABLA_CUENTA);
			ListIterator<Tabla> iter = listaCuentaGarantiaRDC.listIterator();
			while(iter.hasNext()){
				Tabla tablaTemp = iter.next();
			    if(tablaTemp.getId().equals(Constantes.ID_TIPO_CUENTA_E)||tablaTemp.getId().equals(Constantes.ID_TIPO_CUENTA_P)||tablaTemp.getId().equals(Constantes.ID_TIPO_CUENTA_R)){
			        iter.remove();
			    }
			}
			itemCuentaGarantiaRDC= ComboUtil.getSelectItems(listaCuentaGarantiaRDC, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);
			
			//INI MCG20140610
			listaConectorGarantiaRDC= tablaBO.listarHijos(Constantes.ID_TABLA_TIPO_CONECTOR);

			itemConectorGarantiaRDC= ComboUtil.getSelectItems(listaConectorGarantiaRDC, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);
			//FIN MCG20140610
			
			
			listaCuentaCreditoRDC= tablaBO.listarHijos(Constantes.ID_TABLA_CUENTA);
			ListIterator<Tabla> iterCredito = listaCuentaCreditoRDC.listIterator();
			while(iterCredito.hasNext()){
				Tabla tablaTemp = iterCredito.next();
			    if(tablaTemp.getId().equals(Constantes.ID_TIPO_CUENTA_E)){
			    	iterCredito.remove();
			    }
			}
			itemCuentaCreditoRDC= ComboUtil.getSelectItems(listaCuentaCreditoRDC, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);
			
			listaMonedaRDC= tablaBO.listarHijos(Constantes.ID_TABLA_MONEDA);
			itemMonedaRDC= ComboUtil.getSelectItems(listaMonedaRDC, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);	
			/*vchn*/
			listaReembolso= tablaBO.listarHijos(Constantes.ID_TABLA_REEMBLSO);
			//ini MCG20130812
//			itemReembolso= ComboUtil.getSelectItems(listaReembolso, 
//					"id",
//					"descripcion",
//					Constantes.VAL_DEFAULT_SELECTION);	
			
			itemReembolso= ComboUtil.getSelectItemsConSortCodigo2(listaReembolso, 
			"id",
			"descripcion",
			"codigo",
			Constantes.VAL_DEFAULT_SELECTION);
			
			//fin MCG20130812
			
			
			listaVctoRblso = tablaBO.listarHijos(Constantes.ID_TABLA_VCTO_RMBLSO);			
			
			itemVctoRblso= ComboUtil.getSelectItems(listaVctoRblso, 
					"abreviado",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);			
			try {
				tipoCambio = (reporteCreditoBO.getTipoCambio(FechaUtil.getPastDate())).toString();
			} catch (Exception e) {
				tipoCambio="1";
			}			
			
			/*vchn*/			
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
		
			
			programa.setFechaRDC(obtenerFecha(getPrograma().getFechaRDC()));			
			setClasificacionBancoRDC(newdatosBasico.getCalificacionBanco());
			setCodigoCentralRDC(codEmpresaGrupo);			
			setCiiuRDC(buscarActividadesEconomicasByRuc(getPrograma()));
			
			Long idtipoMil=getPrograma().getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILESDEFAULT:getPrograma().getTipoMilesPLR();
			Tabla otipoMil= tablaBO.obtienePorId(idtipoMil);
			setTipoMilesPR(otipoMil.getDescripcion());
			setRatingRDC(ObtenerRating(getPrograma(),codEmpresaGrupo));
			
			reporteCreditoBO.setPrograma(getPrograma());
			/**vchn-20130410**/
			//listaClaseCredito = reporteCreditoBO.ObtenerListaClaseCredito(getPrograma());
			
			List<ClaseCredito> listaTemp = reporteCreditoBO.ObtenerListaClaseCredito(getPrograma(), codEmpresaGrupo);
			if(listaTemp.size()>0 && !listaTemp.isEmpty()){
				listaClaseCredito = reporteCreditoBO.ObtenerListaClaseCredito(getPrograma(), codEmpresaGrupo);
			}else{
				listaClaseCredito = reporteCreditoBO.loadReporteCreditoByAnexos(getPrograma(), codEmpresaGrupo);
			}
			/**vchn-20130410**/
			cargaTipoVcto(listaClaseCredito);
			LoadGarantias(Long.valueOf(programaId),codEmpresaGrupo);
			LoadSustentoOperacion(Long.valueOf(programaId),codEmpresaGrupo);
			LoadAccionista(Long.valueOf(programaId),codEmpresaGrupo);			
			Empresa empresa= new Empresa();	
			empresa=programaBO.findEmpresaByIdEmpresaPrograma(Long.valueOf(programaId),codEmpresaGrupo);
			setNombreEmpresaRDC(empresa.getNombre()==null?"":empresa.getNombre().toString());
			LoadLimiteFormalizar(Long.valueOf(programaId),codEmpresaGrupo,empresa);

			setListaArchivosReporteCredito(reporteCreditoBO.findListaArchivoReporteCredito(getPrograma(),codEmpresaGrupo));
			sessionparam.remove("listaArchivosReporteCredito");
			sessionparam.put("listaArchivosReporteCredito", getListaArchivosReporteCredito());
			cargarSessionCombos();	
			
			codEmpresaGrupoS=codEmpresaGrupo; //mlj 20130411
			
			String cuotaFinanciera="";
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraEmpresa(programa)+"";					
			}else{
				List<Empresa>  listaEmpersas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
				cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraGrupo(programa, listaEmpersas)+"";
			}
			programa.setCuotaFinanciera(cuotaFinanciera);
			//init mlj 20130405
			loadDeudaSBSByPrograma(programaId,empresa,tipoempresa);
			//fin  mlj 20130405
		} catch (BOException e) {
			cargarSessionCombos();	
			logger.error(StringUtil.getStackTrace(e));
		}	catch(Exception e){
			cargarSessionCombos();	
		logger.error(StringUtil.getStackTrace(e));
		}
		completarListaGrupoEmpresas();
		return "reporteCreditoPF";
	}
	
	private void loadDeudaSBSByPrograma(String programaId, Empresa empresa, String tipoempresa) throws BOException {
		List<List> listPoolBancarioTotal = new ArrayList<List>();
		Map<String, String> hm = new HashMap<String, String>();
		try {			
	
			listPoolBancarioTotal = relacionesBancariasBO.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, empresa.getRuc(), Constantes.ID_TIPO_DEUDA_TOTAL,
					"999", "O",Constantes.ID_TIPO_EMPRESA_EMPR.toString(),programaId);			
			
			if(listPoolBancarioTotal!=null && !listPoolBancarioTotal.isEmpty()){
	//			List lista = listPoolBancarioTotal.get(0);//Obteniendo la fila con la fecha mas reciente
	//			if(lista!=null && !lista.isEmpty()){
	//				Object sbsColumna = lista.get(lista.size()-1);
	//				hm = (HashMap<String, String>) sbsColumna;
	//				deudaSBS = FormatUtil.formatMiles(hm.get("SBS")== null?"":hm.get("SBS"));//Columna SBS
	//			}
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
	
	//INI MCG20130212
	
	/**
	 * Se agrega el grupo a la lista de empresas que pertencen al grupo.
	 * 
	 */
	public void completarListaGrupoEmpresas(){
		String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			List<Empresa> lista = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
			for(Empresa emp :lista ){
				listaEmpresasGrupoRC.add(emp);
			}
		}
		
	}
	//FIN MCG20130212
	
	public void LoadGarantias(Long idprograma){	
		try {				
			listGarantia=reporteCreditoBO.findGarantiaByIdprograma(idprograma);	
			if (listGarantia != null && listGarantia.size() > 0) {
				for (Garantia oGarantia:listGarantia){
					oGarantia.setIdcuentatmp(oGarantia.getCuenta().getId().toString());					
					oGarantia.setIdcuentatmpFinal(oGarantia.getCuentaFinal().getId().toString());
				}
			}
			if(listGarantia.isEmpty()){
				precargarDatos(false);	
			}
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}	
	
	//ini MCG20130409
	public void LoadGarantias(Long idprograma,String codEmpresaGrupo){	
		try {	
			
			Programa oprograma =new Programa();			
			oprograma=programaBO.findById(idprograma);
			listGarantia=reporteCreditoBO.findGarantiaByIdprograma(idprograma,codEmpresaGrupo);	
			if (listGarantia != null && listGarantia.size() > 0) {
				for (Garantia oGarantia:listGarantia){
					if (oGarantia.getCuenta()==null){
						oGarantia.setIdcuentatmp("");	
					}else{
						oGarantia.setIdcuentatmp(oGarantia.getCuenta().getId().toString());	
					}
					if (oGarantia.getCuentaFinal()==null){
						oGarantia.setIdcuentatmpFinal("");	
					}else{
						oGarantia.setIdcuentatmpFinal(oGarantia.getCuentaFinal().getId().toString());
					}
					
					if (oGarantia.getConector()==null){
						oGarantia.setIdconectortmp("");	
					}else{
						oGarantia.setIdconectortmp(oGarantia.getConector().getId().toString());
					}
										
					
				}
			}
			if(listGarantia.isEmpty()){
				precargarDatos(false,oprograma,codEmpresaGrupo);
			}
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		}			
	}	
	//fin MCG20130409
	
	
	public void precargarDatos(boolean sincronizar){
		try{
			List<AnexoGarantia> lstAnexoGarantias=anexoGarantiaBO.findAnexoXPrograma(programa);
			List<AnexoGarantia> lstAnexoGarantiasNuevas=new ArrayList<AnexoGarantia>();
			if(sincronizar){listGarantia.clear();}
			for(AnexoGarantia anexoGarantia:lstAnexoGarantias){
				boolean encontrado=false;
				for(Garantia garantia:listGarantia){
					if(garantia.getCodigoAnexoGarantia()!=null){
						if(anexoGarantia.getId().equals(garantia.getCodigoAnexoGarantia())){
							encontrado=true;
							break;
						}
					}
				}
				if(!encontrado){
					lstAnexoGarantiasNuevas.add(anexoGarantia);
				}
			}
			int indiceGarantia=listGarantia.size();
			for(AnexoGarantia anexoGarantia:lstAnexoGarantiasNuevas){
				//addNumeroGarantia();				
				Garantia garantia=new Garantia();
				garantia.setTipo("TC");
				garantia.setComentario(anexoGarantia.getDescripcionGarantia());
				garantia.setNumeroGarantia(anexoGarantia.getNumeroGarantia());
				garantia.setCodigoAnexoGarantia(anexoGarantia.getId());
				//garantia.setOrden(""+(listGarantia.size()+1));
				listGarantia.add(garantia);
			}
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}	
	
	
	//ini MCG20130409
	public void precargarDatos(boolean sincronizar,Programa oprograma,String codEmpresaGrupo){
		try{
			
			Empresa empresa= new Empresa();	
			empresa=programaBO.findEmpresaByIdEmpresaPrograma(oprograma.getId(),codEmpresaGrupo);
			String nombreEmpresa=empresa.getNombre().trim();
			List<AnexoGarantia> lstAnexoGarantias=anexoGarantiaBO.findAnexoXPrograma(oprograma,nombreEmpresa);
			List<AnexoGarantia> lstAnexoGarantiasNuevas=new ArrayList<AnexoGarantia>();
			if(sincronizar){listGarantia.clear();}
			for(AnexoGarantia anexoGarantia:lstAnexoGarantias){
				boolean encontrado=false;
				for(Garantia garantia:listGarantia){
					if(garantia.getCodigoAnexoGarantia()!=null){
						if(anexoGarantia.getId().equals(garantia.getCodigoAnexoGarantia())){
							encontrado=true;
							break;
						}
					}
				}
				if(!encontrado){
					lstAnexoGarantiasNuevas.add(anexoGarantia);
				}
			}
			int indiceGarantia=listGarantia.size();
			for(AnexoGarantia anexoGarantia:lstAnexoGarantiasNuevas){
				//addNumeroGarantia();				
				Garantia garantia=new Garantia();
				garantia.setTipo("TC");
				garantia.setComentario(anexoGarantia.getDescripcionGarantia());
				garantia.setNumeroGarantia(anexoGarantia.getNumeroGarantia());
				garantia.setCodigoAnexoGarantia(anexoGarantia.getId());
				//garantia.setOrden(""+(listGarantia.size()+1));
				listGarantia.add(garantia);
			}
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}	
	
	//fin MCG20130409
	
	
	
	public String sincronizarDatos(){
		try{
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			ObtenerSessionCombos();		
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			Programa oprograma =new Programa();
			
			oprograma=programaBO.findById(Long.valueOf(programaId));
			String codEmpresaGrupo="";			
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			Tabla ttipoEmpresa = new Tabla();
			ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
			oprograma.setTipoEmpresa(ttipoEmpresa);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
				
			}else{
				codEmpresaGrupo= getCodigoEmpresa();
				
			}								
			
			precargarDatos(true,oprograma,codEmpresaGrupo);
			cargarSessionCombos();
			completarListaGrupoEmpresas();
		}catch (Exception e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
		}	
		return "reporteCreditoPF";
	}
	
	public void LoadSustentoOperacion(Long idprograma){	
		try {				
			listaSustentoOperacion=reporteCreditoBO.findSustentoOperacionByIdprograma(idprograma);	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}	
	
	//ini MCG20130509
	public void LoadSustentoOperacion(Long idprograma,String codEmpresaGrupo){	
		try {				
			StringBuilder stb = new StringBuilder();
			listaSustentoOperacion=reporteCreditoBO.findSustentoOperacionByIdprograma(idprograma,codEmpresaGrupo);
			if(listaSustentoOperacion!=null){
				for (SustentoOperacion sustentoOperacion : listaSustentoOperacion) {
					stb = new StringBuilder();
					if(sustentoOperacion.getSustento()!=null){
						for(byte x :sustentoOperacion.getSustento() ){
							stb.append((char)FormatUtil.getCharUTF(x));
			            }
						sustentoOperacion.setSustentoString(stb.toString());
					}
				}
			}
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}
	//fin MCG20130509
	
	public void LoadAccionista(Long idprograma){	
		float porcentajeMinimo=70;
		List<Accionista> listaAccionistaAll = new ArrayList<Accionista>();
		try {				
			
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.PAR_ACCIONISTA_PORCENTAJE_MINIMO);
				porcentajeMinimo=Float.parseFloat(parametro.getValor());
			} catch (Exception e) {
				porcentajeMinimo=70;
			}				
			listaAccionistaAll=datosBasicosBO.getListaAccionistasByOrdenPorc(idprograma);			
			List<Accionista> listaresumen = new ArrayList<Accionista>();
			float totalacciones = 0;			
			for(Accionista acci : listaAccionistaAll){
				logger.info("Nombre:" +acci.getNombre() + " Porcentaje:"+ acci.getPorcentaje() );
				totalacciones += Float.parseFloat(acci.getPorcentaje());
				listaresumen.add(acci);
				if (totalacciones>porcentajeMinimo){
					break;					
				}					
			}
			listaAccionistas=listaresumen;			
			totalAccionista = ""+FormatUtil.round(totalacciones, 2);
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}
	//ini MCG20130409
	public void LoadAccionista(Long idprograma,String codEmpresaGrupo){	
		float porcentajeMinimo=70;
		List<Accionista> listaAccionistaAll = new ArrayList<Accionista>();
		try {				
			
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.PAR_ACCIONISTA_PORCENTAJE_MINIMO);
				porcentajeMinimo=Float.parseFloat(parametro.getValor());
			} catch (Exception e) {
				porcentajeMinimo=70;
			}				
			listaAccionistaAll=datosBasicosBO.getListaAccionistasByOrdenPorc(idprograma,codEmpresaGrupo);			
			if (listaAccionistaAll!=null && listaAccionistaAll.size()>0)   {
				Collections.sort(listaAccionistaAll,Collections.reverseOrder());
			}
			List<Accionista> listaresumen = new ArrayList<Accionista>();
			float totalacciones = 0;
			float totalaccioneslimite = 0;
			for(Accionista acci : listaAccionistaAll){
				logger.info("Nombre:" +acci.getNombre() + " Porcentaje:"+ acci.getPorcentaje() );
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
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}
	
	//fin MCG20130409
	
	
	public String ObtenerRating(Programa oprograma,String codEmpresa){	
		
		List<Rating> listaRatingAll = new ArrayList<Rating>(); 
		String descRating=Constantes.NOM_RATING;// "ESCALA MAESTRA";
		String strRatingRDC="";
		try {			
									
			listaRatingAll = ratingBO.findRating(oprograma.getId(),codEmpresa);	
			if (listaRatingAll!=null && listaRatingAll.size()>0 ){
				for(Rating orating : listaRatingAll){	
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
		}	
		return strRatingRDC;	
	}
	
	
	
	
	
	public String loadCalificacionBanco(Programa programa) {		
		try{
			String clasi=programa.getCalificacionBanco();
			String urlRig4 = getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
			if(clasi==null||clasi.equals("")){
				HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(programa.getIdEmpresa(),getConfiguracionWS(),urlRig4);
				clasi=datos.get("clasificacionBanco");	
			}else{				
				if (ValidateUtil.isNumeric(clasi))	{
					HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(programa.getIdEmpresa(),getConfiguracionWS(),urlRig4);
					clasi=datos.get("clasificacionBanco");					
				}				
			
			}
			return clasi;
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}	
		
	}
	
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
			
			String actividadPrincipal=pdatosBasicos.getActividadPrincipal();
			Integer antiguedadNegocio=pdatosBasicos.getAntiguedadNegocio();
			Integer antiguedadCliente=pdatosBasicos.getAntiguedadCliente();
			String grupoRiesgoBuro=pdatosBasicos.getGrupoRiesgoBuro();
			
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
					 gestor=((datos.get("codRegistroGestor")==null?"":datos.get("codRegistroGestor")) + separador1+(datos.get("nombreGestor")==null?"":datos.get("nombreGestor")));
					 oficina=((datos.get("codOficinaPrincipal")==null?"":datos.get("codOficinaPrincipal")) + separador2 + (datos.get("descOficinaPrincipal")==null?"":datos.get("descOficinaPrincipal")));				 
					 segmento=((datos.get("codEtiqueta")==null?"":datos.get("codEtiqueta")) + separador3 + (datos.get("descEtiqueta")==null?"":datos.get("descEtiqueta")));
				}else{
					clasificacionBanco="";
					gestor="";
					oficina="";
					segmento="";
				}
				
			}else{
				if (ValidateUtil.isNumeric(clasificacionBanco))	{
					HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(CodEmpresa,getConfiguracionWS(),urlRig4);
					if(datos!=null){
						clasificacionBanco=datos.get("clasificacionBanco");		
					}else{clasificacionBanco="";}
									
				}	
			}	

			
			odatosBasicos.setCalificacionBanco(clasificacionBanco);
			odatosBasicos.setGestor(gestor);
			odatosBasicos.setOficina(oficina);
			odatosBasicos.setSegmento(segmento);
			
		}catch (Exception e) {
			odatosBasicos.setCalificacionBanco("");
			odatosBasicos.setGestor("");
			odatosBasicos.setOficina("");
			odatosBasicos.setSegmento("");
			e.printStackTrace();			
		}	
		return odatosBasicos;
		
	}
	
	   //ini MCG20131125
		public String refreshReporteCredito(){
				
			try {
				ObtenerSessionCombos();
						
				Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
				String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
				
				programa.setId(Long.valueOf(idprograma));				
				String codEmpresaGrupo="";
				String strruc="";
					String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
					Tabla ttipoEmpresa = new Tabla();
					ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
					programa.setTipoEmpresa(ttipoEmpresa);			
					if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
						codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
						strruc=sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
						programa.setIdEmpresa(codEmpresaGrupo);
						programa.setRuc(strruc);						
					}else{
						codEmpresaGrupo= getCodigoEmpresa();						
					}								
				
				List<Empresa> listaGrupoEmpresas =  empresaBO.listarEmpresasPorPrograma(this.programa.getId());
				
				reporteCreditoBO.setPrograma(programa);
				reporteCreditoBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));							
				reporteCreditoBO.refreshReporteCredito(codEmpresaGrupo,listaGrupoEmpresas,getConfiguracionWS());
				setPrograma(programa);
				setClasificacionBancoRDC(programa.getCalificacionBanco()==null?"":programa.getCalificacionBanco().toString());
				completarListaGrupoEmpresas();
				addActionMessage("Datos de Host Actualizados Correctamente");
			} catch (BOException e) {
				completarListaGrupoEmpresas();
				logger.error(StringUtil.getStackTrace(e));
				addActionError("No se puede conectar a HOST. Error:" + e.getMessage());
			}
			return "reporteCreditoPF";
		}
		//fin MCG20131125
	
	public String buscarActividadesEconomicasByRuc(Programa oprograma){
		
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String ruc = oprograma.getRuc();
		String actividadesEconomicas = "";
		try {						
				if (oprograma.getCiiuRDC()==null ||(oprograma.getCiiuRDC()!=null && oprograma.getCiiuRDC().equals(""))){				
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
	
	public void LoadLimiteFormalizar(Long idprograma, String codEmpresaGrupo, Empresa empresa){			
		List<LimiteFormalizado> listLimiteFormalizado= new ArrayList<LimiteFormalizado>();		
		double riesgoVigente = 0;
		double riesgoPropuesto = 0;
		double  incremento;
		try {		
			
			/*listLimiteFormalizado =politicasRiesgoBO.findLimiteFormalizadoByIdprograma(idprograma);	
			float totalRiegoVigente = 0;
			float totalRiegoPropuesto = 0;
			//float totalRiegoComprometido = 0;
			float totalIncremento = 0;
			
			if (listLimiteFormalizado != null && listLimiteFormalizado.size() > 0) {				
				for (LimiteFormalizado olimite:listLimiteFormalizado){					
					totalRiegoVigente +=olimite.getLimiteAutorizado()==null?0:Float.valueOf(olimite.getLimiteAutorizado().replace(",", ""));
					totalRiegoPropuesto+=olimite.getLimitePropuesto()==null?0:Float.valueOf(olimite.getLimitePropuesto().replace(",", ""));
					//totalRiegoComprometido+=olimite.getComprometido()==null?0:Float.valueOf(olimite.getComprometido().replace(",", ""));
				}
				totalIncremento=totalRiegoPropuesto - totalRiegoVigente;
			}
			setRiesgovigenteRDC(""+FormatUtil.round(totalRiegoVigente, 2));
			setIncrementoRDC(""+FormatUtil.round(totalIncremento, 2));
			setRiesgopropuestoRDC(""+FormatUtil.round(totalRiegoPropuesto, 2));*/
			// MLJ 20130404

			
			// Se cambio de riesgo actual a limiteAutorizado
			
			//riesgovigenteRDC = anexoBO.findValorByColumnaAnexosByEmpresa(idprograma,empresa.getNombre()==null?"":empresa.getNombre().trim(), Constantes.COLUMN_LIMITE_AUTORIZADO);
			//riesgopropuestoRDC = anexoBO.findValorByColumnaAnexosByEmpresa(idprograma,empresa.getNombre()==null?"":empresa.getNombre().trim(), Constantes.COLUMN_RIESGO_PROPUESTO);
			Anexo oanexo=new Anexo();
			oanexo=anexoBO.findValorByColumnaAnexosByEmpresa(idprograma,empresa.getNombre()==null?"":empresa.getNombre().trim());
			if (oanexo!=null){
				riesgovigenteRDC = oanexo.getLteAutorizado()==null?"":oanexo.getLteAutorizado();//Constantes.COLUMN_LIMITE_AUTORIZADO
				riesgopropuestoRDC=oanexo.getRgoPropBbvaBc()==null?"":oanexo.getRgoPropBbvaBc();//Constantes.COLUMN_RIESGO_PROPUESTO									
			}
			
			try{
				if(riesgovigenteRDC!=null && !riesgovigenteRDC.equals("")){
					riesgoVigente = Double.valueOf(riesgovigenteRDC.replace(",", ""));
				}
				if(riesgopropuestoRDC!=null && !riesgopropuestoRDC.equals("")){
					riesgoPropuesto = Double.valueOf(riesgopropuestoRDC.replace(",", ""));
				}
				incremento = riesgoPropuesto - riesgoVigente;
				incremento = FormatUtil.round(incremento, 2);
				incrementoRDC = FormatUtil.conversion(incremento);
			}catch(Exception e){logger.error(StringUtil.getStackTrace(e));}
			
									
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void cargarSessionCombos(){
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			sessionparam.put("LISTASEGMENTO", itemSegmento);	
			sessionparam.put("LISTAOFICINA", itemOficinaBEC);
			sessionparam.put("LISTACUENTA", itemCuentaRDC);	
			sessionparam.put("LISTAMONEDA", itemMonedaRDC);
			/*vchn*/
			sessionparam.put("LISTAREEMBLSO", itemReembolso);
			sessionparam.put("LISTATIPOREEMBLSO", itemVctoRblso);
			sessionparam.put("LISTACUENTAGARANTIAS", itemCuentaGarantiaRDC);
			sessionparam.put("LISTACUENTACREDITO", itemCuentaCreditoRDC);
			/*vchn*/
			sessionparam.put("LISTACONECTORGARANTIAS", itemConectorGarantiaRDC);
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public void ObtenerSessionCombos(){
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			 itemSegmento=(List<SelectItem>)sessionparam.get("LISTASEGMENTO");
			 itemOficinaBEC=(List<SelectItem>)sessionparam.get("LISTAOFICINA");
			 itemCuentaRDC=(List<SelectItem>)sessionparam.get("LISTACUENTA");
			 itemMonedaRDC=(List<SelectItem>)sessionparam.get("LISTAMONEDA");
			 /*vchn*/
			 itemReembolso=(List<SelectItem>)sessionparam.get("LISTAREEMBLSO");
			 itemVctoRblso=(List<SelectItem>)sessionparam.get("LISTATIPOREEMBLSO");
			 itemCuentaGarantiaRDC=(List<SelectItem>)sessionparam.get("LISTACUENTAGARANTIAS");
			 itemCuentaCreditoRDC=(List<SelectItem>)sessionparam.get("LISTACUENTACREDITO");
			 /*vchn*/
			 itemConectorGarantiaRDC=(List<SelectItem>)sessionparam.get("LISTACONECTORGARANTIAS");
			 
			 listaArchivosReporteCredito=(List<ArchivoReporteCredito>)sessionparam.get("listaArchivosReporteCredito");
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public String addClaseCredito(){
		ObtenerSessionCombos();
		if(listaClaseCredito==null){
			listaClaseCredito = new ArrayList<ClaseCredito>();
		}
		int contflagcan=0;
		for(ClaseCredito oClaseCredito:listaClaseCredito){			
			if (oClaseCredito.getFlagCancelado()!=null && oClaseCredito.getFlagCancelado().equals("CC")){
				break;
			}
			contflagcan++;
		}
		
		ClaseCredito claseCredito = new ClaseCredito();
		claseCredito.setTasaComision("A Pactar");
		claseCredito.setFlagCancelado("NC");
		claseCredito.setFlagTipoLimte(Constantes.VALOR_FLAGTIPO_LIMITEOPERACION);
		if(getChkClaseCredito()!=null){
			 String [] idsEliminar = getChkClaseCredito().split(",");
			 if(idsEliminar != null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;				 
				 listaClaseCredito.add(posicion, claseCredito);
			 }
		}else{
			//listaClaseCredito.add(claseCredito);
			listaClaseCredito.add(contflagcan, claseCredito);
		}
		int cont=0;
		int pos=0;
		Tabla ocuenta=new Tabla();
		ocuenta.setId(Constantes.ID_TIPO_CUENTA_M);
		for(ClaseCredito oClaseCredito:listaClaseCredito){
			
			if (oClaseCredito.getFlagTipoLimte()!=null && oClaseCredito.getFlagTipoLimte().equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
				oClaseCredito.setOrden("0");
				oClaseCredito.setCuenta(ocuenta);
			}else{
				cont=cont+1;
				oClaseCredito.setOrden(String.valueOf(cont));
			}
			
			oClaseCredito.setPosicion(pos);
			if(oClaseCredito.getTipoVcto()!= null && oClaseCredito.getTipoVcto().equals("R")){
				oClaseCredito.setReembolso(oClaseCredito.getVencimiento());
			}
			pos++;
		}
		completarListaGrupoEmpresas();
		return "reporteCreditoPF";
	}
	
	public String addClaseCreditoSublimite(){
		ObtenerSessionCombos();
		if(listaClaseCredito==null){
			listaClaseCredito = new ArrayList<ClaseCredito>();
		}
		int contflagcan=0;
		for(ClaseCredito oClaseCredito:listaClaseCredito){			
			if (oClaseCredito.getFlagCancelado()!=null && oClaseCredito.getFlagCancelado().equals("CC")){
				break;
			}
			contflagcan++;
		}
		
		ClaseCredito claseCredito = new ClaseCredito();
		claseCredito.setTasaComision("A Pactar");
		claseCredito.setFlagCancelado("NC");
		claseCredito.setFlagTipoLimte(Constantes.VALOR_FLAGTIPO_SUBLIMITE);
		
		if(getChkClaseCredito()!=null){
			 String [] idsEliminar = getChkClaseCredito().split(",");
			 if(idsEliminar != null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;				 
				 listaClaseCredito.add(posicion, claseCredito);
			 }
		}else{
			//listaClaseCredito.add(claseCredito);
			listaClaseCredito.add(contflagcan, claseCredito);
		}
		int cont=0;
		int pos=0;
		Tabla ocuenta=new Tabla();
		ocuenta.setId(Constantes.ID_TIPO_CUENTA_M);
		for(ClaseCredito oClaseCredito:listaClaseCredito){
			
			if (oClaseCredito.getFlagTipoLimte()!=null && oClaseCredito.getFlagTipoLimte().equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
				oClaseCredito.setOrden("0");
				oClaseCredito.setCuenta(ocuenta);
			}else{
				cont=cont+1;
				oClaseCredito.setOrden(String.valueOf(cont));
			}
			
			oClaseCredito.setPosicion(pos);
			if(oClaseCredito.getTipoVcto()!= null && oClaseCredito.getTipoVcto().equals("R")){
				oClaseCredito.setReembolso(oClaseCredito.getVencimiento());
			}
			pos++;
		}
		completarListaGrupoEmpresas();
		return "reporteCreditoPF";
	}
	
	public String deleteClaseCredito(){
		ObtenerSessionCombos();
		if(getChkClaseCredito()!=null){
			 String [] idsEliminar = getChkClaseCredito().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaClaseCredito.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
			}
		int cont=0;
		Tabla ocuenta=new Tabla();
		ocuenta.setId(Constantes.ID_TIPO_CUENTA_M);
		for(ClaseCredito oClaseCredito:listaClaseCredito){

			if (oClaseCredito.getFlagTipoLimte()!=null && oClaseCredito.getFlagTipoLimte().equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
				oClaseCredito.setOrden("0");
				oClaseCredito.setCuenta(ocuenta);
			}else{
				cont=cont+1;
				oClaseCredito.setOrden(String.valueOf(cont));
			}
						
			if(oClaseCredito.getTipoVcto()!= null && oClaseCredito.getTipoVcto().equals("R")){
				oClaseCredito.setReembolso(oClaseCredito.getVencimiento());
			}
		}
		completarListaGrupoEmpresas();
		//listaPrinciEjecutivos.remove(listaPrinciEjecutivos.size()-1);
		return "reporteCreditoPF";
	}
	
	public String saveAllClaseCredito(){
		try {
			ObtenerSessionCombos();
			String codEmpresaGrupo="";
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			programa.setId(Long.valueOf(idprograma));
			for (ClaseCredito claseCredito : listaClaseCredito) {
				if(claseCredito.getTipoVcto()!= null &&  claseCredito.getTipoVcto().equals("R")){
					claseCredito.setVencimiento(claseCredito.getReembolso());
				}
			}
			reporteCreditoBO.setListaClaseCredito(listaClaseCredito);
			reporteCreditoBO.setPrograma(programa);
			reporteCreditoBO.saveUDClaseCredito(codEmpresaGrupo);
			completarListaGrupoEmpresas();
			cargaTipoVcto(listaClaseCredito);
			addActionMessage("Clase de Credito registrados Correctamente");
		} catch (BOException e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			completarListaGrupoEmpresas();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "reporteCreditoPF";
	}
	
	public String addCanceledRows(){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		List<ClaseCredito> tempList = new ArrayList<ClaseCredito>();
		List<ClaseCredito> tempListdel = new ArrayList<ClaseCredito>();
		ObtenerSessionCombos();
		if(listaClaseCredito==null){
			listaClaseCredito = new ArrayList<ClaseCredito>();
		}
		for(ClaseCredito objClaseCredito: listaClaseCredito){
			if (objClaseCredito.getFlagCancelado()!=null && objClaseCredito.getFlagCancelado().equals("NC")){
				ClaseCredito claseCredito = new ClaseCredito();
				claseCredito.setTasaComision("A Pactar");
				claseCredito.setMoneda(objClaseCredito.getMoneda());
				claseCredito.setOrden(objClaseCredito.getOrden());
				claseCredito.setFlagTipoLimte(objClaseCredito.getFlagTipoLimte());
				claseCredito.setTipoVcto(objClaseCredito.getTipoVcto());
				claseCredito.setVencimiento(objClaseCredito.getVencimiento());
				claseCredito.setClaseCredito(objClaseCredito.getClaseCredito());
				if(objClaseCredito.getTipoVcto()!= null && objClaseCredito.getTipoVcto().equals("R")){
					claseCredito.setReembolso(objClaseCredito.getVencimiento());
				}
				claseCredito.setFlagCancelado("CC");
				tempList.add(claseCredito);
			}else{
				ClaseCredito claseCredito = new ClaseCredito();
				claseCredito=objClaseCredito;
				tempListdel.add(claseCredito);
			}
		}
		cancelado = String.valueOf(listaClaseCredito.size()-1);
		listaClaseCredito.removeAll(tempListdel);
		listaClaseCredito.addAll(tempList);
		int cont=0;
		Tabla ocuenta=new Tabla();
		ocuenta.setId(Constantes.ID_TIPO_CUENTA_M);
		for(ClaseCredito oClaseCredito:listaClaseCredito){				
			
			if (oClaseCredito.getFlagTipoLimte()!=null && oClaseCredito.getFlagTipoLimte().equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
				oClaseCredito.setOrden("0");
				oClaseCredito.setCuenta(ocuenta);
				
			}else{				
				cont=cont+1;
				oClaseCredito.setOrden(String.valueOf(cont));
			}
			
			
		}
			
		completarListaGrupoEmpresas();
		return "reporteCreditoPF";
	}

	public String sincronizarClaseCredito(){
		try{
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			ObtenerSessionCombos();		
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(programaId));
			String codEmpresaGrupo="";			
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			Tabla ttipoEmpresa = new Tabla();
			ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
			oprograma.setTipoEmpresa(ttipoEmpresa);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
				
			}else{
				codEmpresaGrupo= getCodigoEmpresa();
				
			}								
			precargarClaseCredito(true,oprograma,codEmpresaGrupo);
			cargarSessionCombos();
			completarListaGrupoEmpresas();
		}catch (Exception e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
		}	
		return "reporteCreditoPF";
	}

	public void precargarClaseCredito(boolean sincronizar,Programa oprograma,String codEmpresaGrupo){
		try{
			if(sincronizar){listaClaseCredito.clear();}
			listaClaseCredito=reporteCreditoBO.loadReporteCreditoByAnexos(oprograma,codEmpresaGrupo);

		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}	

	
	 public String addCuentaGarantia() throws Exception {
		 
		 
		 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
 		Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
 		ObtenerSessionCombos();

		 logger.info(getListGarantia());
		 Programa programatemp=new Programa();
		 programatemp.setId(idprograma);			 
		 logger.info("size="+listGarantia.size());		
		 Garantia garantiaCuenta =new Garantia();
		 
		 Tabla ocuenta= new Tabla();
		 ocuenta.setId(new Long(1));
		 
		 Tabla ocuentaFinal= new Tabla();
		 ocuentaFinal.setId(new Long(1));
		//Ini MCG20140610
		 Tabla oconector= new Tabla();
		 oconector.setId(new Long(1));
		//Fin MCG20140610
		 
		 garantiaCuenta.setId(null); 
		 garantiaCuenta.setCuenta(ocuenta);
		 garantiaCuenta.setCuentaFinal(ocuentaFinal);
		//Ini MCG20140610
		 garantiaCuenta.setConector(oconector);
		//Fin MCG20140610
		 garantiaCuenta.setIdcuentatmp("1");
		 garantiaCuenta.setIdcuentatmpFinal("1");
		//Ini MCG20140610
		 garantiaCuenta.setIdconectortmp("1");
		//Fin MCG20140610
		
		 garantiaCuenta.setNumeroGarantia("");
		 garantiaCuenta.setTipo("TG");		 
		 garantiaCuenta.setComentario(null);	
		 garantiaCuenta.setPrograma(programatemp);		 
		 
		 String [] idsEliminar = null;
		 if(getChksParaEliminar()!= null){
			 idsEliminar = getChksParaEliminar().split(",");
		 }
		 if(idsEliminar!=null){
			 int index = idsEliminar.length-1;
			 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
			 listGarantia.add(posicion,garantiaCuenta);
		 }else{
			 listGarantia.add(garantiaCuenta);
		 }
		 logger.info("listGarantia: " + listGarantia.size());
		 int cont=0;
			for(Garantia oGarantia:listGarantia){
				if (oGarantia.getTipo().equals("TC")){
					cont=cont+1;
					oGarantia.setOrden(String.valueOf(cont));
				}
			}
		 
		 completarListaGrupoEmpresas();
		 return "reporteCreditoPF"; 
		 
	 }
	 public String addNumeroGarantia() throws Exception {
		 
		 
		 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
 		Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
 		ObtenerSessionCombos();	
 		
 		if(listGarantia==null){
 			listGarantia = new ArrayList<Garantia>();
		}
 		 
		 logger.info(getListGarantia());
		 Programa programatemp=new Programa();
		 programatemp.setId(idprograma);			 
		 logger.info("size="+listGarantia.size());		
		 Garantia lineagarantia =new Garantia();
		 Tabla ocuenta= new Tabla();		 
		 ocuenta.setId(new Long(1));	
		 
		 Tabla ocuentaFinal= new Tabla(); 		 
		 ocuentaFinal.setId(new Long(1));
		//Ini MCG20140610
		 Tabla oconector= new Tabla(); 		 
		 oconector.setId(new Long(1));
		//Fin MCG20140610
		 
		 lineagarantia.setId(null); 
		 lineagarantia.setCuenta(ocuenta);
		 lineagarantia.setCuentaFinal(ocuentaFinal);
		 lineagarantia.setConector(oconector);
		 lineagarantia.setIdcuentatmp("1");
		 lineagarantia.setIdcuentatmpFinal("1");
		 lineagarantia.setIdconectortmp("1");
		 lineagarantia.setNumeroGarantia("");		
		 lineagarantia.setTipo("TG");		
		 lineagarantia.setComentario(null);	
		 lineagarantia.setPrograma(programatemp);
		
		 String [] idsEliminar = null;
		 if(getChksParaEliminar()!= null){
			 idsEliminar = getChksParaEliminar().split(",");
		 }
		 if(idsEliminar!=null){
			 int index = idsEliminar.length-1;
			 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
			 listGarantia.add(posicion,lineagarantia);
		 }else{
			 listGarantia.add(lineagarantia);
		 }
		 
		 completarListaGrupoEmpresas();
		 logger.info("listGarantia: " + listGarantia.size());		 
		 return "reporteCreditoPF"; 			 
}
	 
		public String delNumeroGarantia(){	
			logger.info("Items a eliminar= "+this.getChksParaEliminar());
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			if(getChksParaEliminar()!= null){
				String [] idsEliminar = getChksParaEliminar().split(",");
				if(listGarantia!=null && listGarantia.size()>0){
					//no se debe de eliminar el primer registro por ser una Cuenta
					// MCG20130905 if(Integer.parseInt(idsEliminar[0].trim())==0 && listGarantia.size()>1){
						 // MCG20130905 addActionError("No se puede eliminar el primer registro por tratase de la Cuenta.");
					// MCG20130905}else{
						//se elimina del ultimo hacia el primero para no alterar los indices
						if(idsEliminar!=null){
							 for(int index = idsEliminar.length-1; index>=0; index--){
								 listGarantia.remove(Integer.parseInt(idsEliminar[index].trim()));
							 }
						}
					// MCG20130905 }
					
				}	
			}else{
				addActionError("Debe seleccionar al menos un registro para eliminar.");
			}
			 int cont=0;
				for(Garantia oGarantia:listGarantia){
					if (oGarantia.getTipo().equals("TC")){
						cont=cont+1;
						oGarantia.setOrden(String.valueOf(cont));
					}
				}
			ObtenerSessionCombos();
			completarListaGrupoEmpresas();
			return "reporteCreditoPF";
		}
		
		public String saveGarantia(){		
			try {
				String codEmpresaGrupo="";
				Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
				String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);	
				String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
				}else{
					codEmpresaGrupo= getCodigoEmpresa();				
				}
				ObtenerSessionCombos();

				Programa programatmp1 = new Programa();
				programatmp1.setId(Long.valueOf(idprograma));
				reporteCreditoBO.setPrograma(programatmp1);
				
				String stridcuentatmp="1";
				String stridcuentatmpFinal="1";
				
				String stridconectortmp="1";
				
		 		if(listGarantia==null){
		 			listGarantia = new ArrayList<Garantia>();
				}
				
				for (Garantia listGaran: listGarantia){
//					if (listGaran.getTipo().equals("TC")){
						stridcuentatmp=listGaran.getIdcuentatmp();
						stridcuentatmpFinal=listGaran.getIdcuentatmpFinal();
						stridconectortmp=listGaran.getIdconectortmp();
//					}else if (listGaran.getTipo().equals("TG")){
//						listGaran.setIdcuentatmp(stridcuentatmp);
//						listGaran.setIdcuentatmpFinal(stridcuentatmpFinal);						
//					}		
				}		
				
				for (Garantia listGaran: listGarantia){
					Tabla cuenta=new Tabla();
					cuenta.setId(Long.valueOf(listGaran.getIdcuentatmp()));
					listGaran.setCuenta(cuenta);	
					Tabla cuentaFinal=new Tabla();
					cuentaFinal.setId(Long.valueOf(listGaran.getIdcuentatmpFinal()));
					listGaran.setCuentaFinal(cuentaFinal);
					//Ini MCG20140610
					Tabla oconector=new Tabla();
					oconector.setId(Long.valueOf(listGaran.getIdconectortmp()));
					listGaran.setConector(oconector);
					//Fin MCG20140610
				}
				
				reporteCreditoBO.setListGarantia(listGarantia);
				reporteCreditoBO.saveGarantia(codEmpresaGrupo);
				completarListaGrupoEmpresas();
				addActionMessage("Garantia registrados Correctamente");
			} catch (BOException e) {
				completarListaGrupoEmpresas();
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				return "reporteCreditoPF";
			}
			
			return "reporteCreditoPF";
		}
		
		
		public String addSustento(){
			ObtenerSessionCombos();
			if(listaSustentoOperacion==null){
				listaSustentoOperacion = new ArrayList<SustentoOperacion>();
			}
			SustentoOperacion sustentoOperacion= new SustentoOperacion();			
			if(getChkSustento()!=null){
				 String [] idsEliminar = getChkSustento().split(",");
				 if(idsEliminar != null){
					 int index = idsEliminar.length-1;
					 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;				 
					 listaSustentoOperacion.add(posicion, sustentoOperacion);
				 }
			}else{
				listaSustentoOperacion.add(sustentoOperacion);
			}
			int cont=0;
			for(SustentoOperacion oSustentoOperacion:listaSustentoOperacion){
				cont=cont+1;
				oSustentoOperacion.setOrden(String.valueOf(cont));
				
			}
			completarListaGrupoEmpresas();
			return "reporteCreditoPF";
		}
		
		/**gmp-20130410**/
		/**
		 * Consulta el sustento que se mostrará en el modal, para la actualización de datos
		 * @throws IOException 
		 * */
		public void updateSustento() throws BOException, IOException{
			String sustentoJSON = "";
			sustento = reporteCreditoBO.findSustentoById(idSustento);
			/*StringBuilder stb = new StringBuilder();
			if(sustento.getSustento()!=null){
				for(byte x :sustento.getSustento() ){
					stb.append((char)FormatUtil.getCharUTF(x));
	            }
				sustento.setSustentoString(stb.toString());
			}
			if(sustento.getSustentoString()!=null)
				sustento.setSustentoString(sustento.getSustentoString().replace("\"", "*"));*/
			if(sustento!=null){
				sustentoJSON = "{\"rowId\":\""+sustento.getId()+"\",\"sustentoId\":\""+sustento.getId()+
								"\",\"sustentoProgramaId\":\""+sustento.getPrograma().getId()+
								"\",\"sustentoPosicion\":\""+sustento.getPosicion()+
								//"\",\"sustentoComentario\":\""+sustento.getSustentoString()+
								"\",\"sustentoOrden\":\""+sustento.getOrden()+
								"\",\"sustentoCuentaId\":\""+(sustento.getCuenta()==null?"":sustento.getCuenta().getId())+
								"\",\"sustentoOrdenFinal\":\""+sustento.getOrdenFinal()+
								"\",\"sustentoCuentaIdFinal\":\""+(sustento.getCuentaFinal()==null?"":sustento.getCuentaFinal().getId())+
								"\"}";
			}
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(sustentoJSON.toString());
		}
		
		public void consultarSustento() throws BOException, IOException{
			StringBuilder stb = new StringBuilder();
			sustento = reporteCreditoBO.findSustentoById(idSustento);
			if(sustento!=null){
				if(sustento.getSustento()!=null){
					for(byte x :sustento.getSustento() ){
						stb.append((char)FormatUtil.getCharUTF(x));
		            }
					sustento.setSustentoString(stb.toString());
				}
			}
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(stb.toString());
		}
		/**gmp-20130410
		 * @throws BOException 
		 * @throws NumberFormatException **/
		
		public String deleteSustento() throws NumberFormatException, BOException{
			ObtenerSessionCombos();
			Long idSo = 0L;
			SustentoOperacion so = new SustentoOperacion();
			if(getChkSustento()!=null){
				 String [] idsEliminar = getChkSustento().split(",");
				 if(idsEliminar!=null){
					 for(int index = idsEliminar.length-1; index>=0; index--){
						 idSo = listaSustentoOperacion.get(Integer.parseInt(idsEliminar[index].trim())).getId();
						 so = reporteCreditoBO.findSustentoById(idSo);
						 reporteCreditoBO.delete(so);
						 listaSustentoOperacion.remove(Integer.parseInt(idsEliminar[index].trim()));
					 }
				 }
			}
			completarListaGrupoEmpresas();
			return "reporteCreditoPF";
		}
			/**mlj-20130410**/
		public String saveAllSustento(){
			try {				
				int posicion = 1;
				boolean updateLista = false;
				ObtenerSessionCombos();
				String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
				Programa programa = programaBO.findById(Long.valueOf(idprograma));
				LoadSustentoOperacion(Long.valueOf(idprograma),codEmpresaGrupoS);
				SustentoOperacion sustento = new SustentoOperacion();
				if(ideSustento==null){
					
					if(getChkSustento()!=null && !chkSustento.equals("")){
						 String [] idsEliminar = getChkSustento().split(",");
						 if(idsEliminar != null){
							 int index = idsEliminar.length-1;
							 posicion = Integer.parseInt(idsEliminar[index].trim())+2;
							 updateLista = true;
						}
					}else{
						if(listaSustentoOperacion!=null && !listaSustentoOperacion.isEmpty()){
							posicion = listaSustentoOperacion.size()+1;
						}
					}
					
					sustento.setPosicion(posicion);
				}else{
					sustento = reporteCreditoBO.findSustentoById(ideSustento);
				}
				sustento.setPrograma(programa);
				sustento.setOrden(ordenSustento);
				sustento.setOrdenFinal(ordenFinalSustento);
				sustento.setCuenta(tablaBO.obtienePorId(idCuentaSustento));
				sustento.setCuentaFinal(tablaBO.obtienePorId(idCuentaFinalSustento));
				sustento.setCodEmpresaGrupo(codEmpresaGrupoS);
				Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
				reporteCreditoBO.setValorBlob(valorSustento);
				reporteCreditoBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
				reporteCreditoBO.save(sustento);
				if(updateLista){
					 for (int i = 0; i < listaSustentoOperacion.size(); i++) {
						 if(posicion-1<=i){
							 SustentoOperacion oSustentoOperacion = listaSustentoOperacion.get(i);
							 oSustentoOperacion.setPosicion(i+2);
							 reporteCreditoBO.updatePosicionSustento(oSustentoOperacion);
						 }
					 }
				}
				LoadSustentoOperacion(Long.valueOf(idprograma),codEmpresaGrupoS);
				completarListaGrupoEmpresas();
				flagSustento = true;
				addActionMessage("Sustento de Operaciones registrado Correctamente");
			} catch (BOException e) {
				completarListaGrupoEmpresas();
				logger.error(StringUtil.getStackTrace(e));
			}
			return "reporteCreditoPF";
		}
		private void cargarDatos() {
			try{
				String programaId = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
				String codEmpresaGrupo = codEmpresaGrupoS;
				Empresa empresa= new Empresa();	
				empresa=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
				String tipoempresa=getPrograma().getTipoEmpresa().getId().toString();
				
				if (empresa!=null && !empresa.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
						
					   List<ReporteCredito> listReporteCredito=reporteCreditoBO.getListaReporteCredito(getPrograma(),codEmpresaGrupo);
					   if (listReporteCredito!=null && listReporteCredito.size()>0 ){
						   
						   ReporteCredito reportCredito =listReporteCredito.get(0);								   
						   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
						   
						   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
						   getPrograma().setGestor(odatosBasico.getGestor());
						   getPrograma().setOficina(odatosBasico.getOficina());
						   getPrograma().setSegmento(odatosBasico.getSegmento());
						   
						   getPrograma().setActividadPrincipal(odatosBasico.getActividadPrincipal());
						   getPrograma().setAntiguedadNegocio(odatosBasico.getAntiguedadNegocio());
						   getPrograma().setAntiguedadCliente(odatosBasico.getAntiguedadCliente());
						   getPrograma().setGrupoRiesgoBuro(odatosBasico.getGrupoRiesgoBuro());
						   
						   getPrograma().setFechaRDC(reportCredito.getFechaRDC()==null?"":reportCredito.getFechaRDC().toString());							   
						   getPrograma().setCuentaCorriente(reportCredito.getCuentaCorriente()==null?"":reportCredito.getCuentaCorriente().toString());							   
						   getPrograma().setNumeroRVGL(reportCredito.getNumeroRVGL()==null?"":reportCredito.getNumeroRVGL().toString());
						   getPrograma().setRuc(empresa.getRuc());	
						   getPrograma().setCiiuRDC(reportCredito.getCiiuRDC());
						   getPrograma().setSalem(reportCredito.getSalem()==null?"":reportCredito.getSalem().toString());
						   	
						   
						   getPrograma().setVulnerabilidad(reportCredito.getVulnerabilidad()==null?"":reportCredito.getVulnerabilidad().toString());
						   getPrograma().setTotalInversion(reportCredito.getTotalInversion()==null?"":reportCredito.getTotalInversion().toString());
						   getPrograma().setMontoPrestamo(reportCredito.getMontoPrestamo()==null?"":reportCredito.getMontoPrestamo().toString());
						   getPrograma().setEntorno(reportCredito.getEntorno()==null?"":reportCredito.getEntorno().toString());
						   getPrograma().setPoblacionAfectada(reportCredito.getPoblacionAfectada()==null?"":reportCredito.getPoblacionAfectada().toString());
						   getPrograma().setCategorizacionAmbiental(reportCredito.getCategorizacionAmbiental()==null?"":reportCredito.getCategorizacionAmbiental().toString());
						   getPrograma().setComentarioAdmision(reportCredito.getComentarioAdmision()==null?"":reportCredito.getComentarioAdmision().toString());
						   

					   }else{
						   DatosBasico odatosBasico=loadDatosBaicosByEmpresa(getPrograma(),codEmpresaGrupo);
						   getPrograma().setCalificacionBanco(odatosBasico.getCalificacionBanco());
						   getPrograma().setGestor(odatosBasico.getGestor());
						   getPrograma().setOficina(odatosBasico.getOficina());
						   getPrograma().setSegmento(odatosBasico.getSegmento());							   
						   
						   getPrograma().setActividadPrincipal(odatosBasico.getActividadPrincipal());
						   getPrograma().setAntiguedadNegocio(odatosBasico.getAntiguedadNegocio());
						   getPrograma().setAntiguedadCliente(odatosBasico.getAntiguedadCliente());
						   getPrograma().setGrupoRiesgoBuro(odatosBasico.getGrupoRiesgoBuro());
						   
						   getPrograma().setFechaRDC("");							   
						   getPrograma().setCuentaCorriente("");							   
						   getPrograma().setNumeroRVGL("");
						   getPrograma().setRuc(empresa.getRuc());
						   getPrograma().setCiiuRDC("");
						   getPrograma().setSalem("");	
						   
						   getPrograma().setVulnerabilidad("");
						   getPrograma().setTotalInversion("");
						   getPrograma().setMontoPrestamo("");
						   getPrograma().setEntorno("");
						   getPrograma().setPoblacionAfectada("");
						   getPrograma().setCategorizacionAmbiental("");
						   getPrograma().setComentarioAdmision("");
						   
						   
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
				
				programa.setFechaRDC(obtenerFecha(getPrograma().getFechaRDC()));			
				setClasificacionBancoRDC(newdatosBasico.getCalificacionBanco());
				setCodigoCentralRDC(codEmpresaGrupo);			
				setCiiuRDC(buscarActividadesEconomicasByRuc(getPrograma()));
				
				Long idtipoMil=getPrograma().getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILESDEFAULT:getPrograma().getTipoMilesPLR();
				Tabla otipoMil= tablaBO.obtienePorId(idtipoMil);
				setTipoMilesPR(otipoMil.getDescripcion());
				setRatingRDC(ObtenerRating(getPrograma(),codEmpresaGrupo));
				
				reporteCreditoBO.setPrograma(getPrograma());
				List<ClaseCredito> listaTemp = reporteCreditoBO.ObtenerListaClaseCredito(getPrograma(), codEmpresaGrupo);
				if(listaTemp.size()>0 && !listaTemp.isEmpty()){
					listaClaseCredito = reporteCreditoBO.ObtenerListaClaseCredito(getPrograma(), codEmpresaGrupo);
				}else{
					listaClaseCredito = reporteCreditoBO.loadReporteCreditoByAnexos(getPrograma(), codEmpresaGrupo);
				}
				
				cargaTipoVcto(listaClaseCredito);
				LoadGarantias(Long.valueOf(programaId),codEmpresaGrupo);
				LoadAccionista(Long.valueOf(programaId),codEmpresaGrupo);			
				
				LoadLimiteFormalizar(Long.valueOf(programaId),codEmpresaGrupo,empresa);
				loadDeudaSBSByPrograma(programaId,empresa,tipoempresa);
				
				String cuotaFinanciera="";
				if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraEmpresa(programa)+"";					
				}else{
					List<Empresa>  listaEmpersas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraGrupo(programa, listaEmpersas)+"";
				}
				programa.setCuotaFinanciera(cuotaFinanciera);
			}
			catch(Exception e){
				logger.error(StringUtil.getStackTrace(e));
			}
		}

		/**mlj-20130410**/
		
	
	public String updateReporteCredito(){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		programa.setId(Long.valueOf(idprograma));
		String codEmpresaGrupo="";
		ObtenerSessionCombos();
		try {			
			
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			Tabla ttipoEmpresa = new Tabla();
			ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
			programa.setTipoEmpresa(ttipoEmpresa);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
				
			}else{
				codEmpresaGrupo= getCodigoEmpresa();
				
			}			
			
								
//			String stridCuenta="1";
//			for (Garantia listGaran: listGarantia){
//				if (listGaran.getTipo().equals("TC")){
//					stridCuenta=listGaran.getIdcuentatmp();				
//				}else if (listGaran.getTipo().equals("TG")){
//					listGaran.setIdcuentatmp(stridCuenta);
//				}		
//				if(!listGaran.getIdcuentatmp().equals(Constantes.TIPO_CUENTA_M)){
//					listGaran.setNumeroGarantiaAnterior(null);
//					listGaran.setComentarioAnterior(null);
//				}
//			}	
//			for (Garantia listGarant: listGarantia){
//				Tabla cuentatmp=new Tabla();
//				cuentatmp.setId(Long.valueOf(listGarant.getIdcuentatmp()));
//				listGarant.setCuenta(cuentatmp);				
//			}				
			
			String stridcuentatmp="1";
			String stridcuentatmpFinal="1";
			String stridconectortmp="1";
	 		if(listGarantia==null){
	 			listGarantia = new ArrayList<Garantia>();
			}
			for (Garantia listGaran: listGarantia){
//				if (listGaran.getTipo().equals("TC")){
					stridcuentatmp=listGaran.getIdcuentatmp();
					stridcuentatmpFinal=listGaran.getIdcuentatmpFinal();
					stridconectortmp=listGaran.getIdconectortmp();
//				}else if (listGaran.getTipo().equals("TG")){
//					listGaran.setIdcuentatmp(stridcuentatmp);
//					listGaran.setIdcuentatmpFinal(stridcuentatmpFinal);						
//				}		
			}		
			
			for (Garantia listGaran: listGarantia){
				Tabla cuenta=new Tabla();
				cuenta.setId(Long.valueOf(listGaran.getIdcuentatmp()));
				listGaran.setCuenta(cuenta);	
				Tabla cuentaFinal=new Tabla();
				cuentaFinal.setId(Long.valueOf(listGaran.getIdcuentatmpFinal()));
				listGaran.setCuentaFinal(cuentaFinal);
				//Ini MCG20140610
				Tabla oconector=new Tabla();
				oconector.setId(Long.valueOf(listGaran.getIdconectortmp()));
				listGaran.setConector(oconector);
				//Fin MCG20140610
			}
			
			programa.setCiiuRDC(getCiiuRDC());
			reporteCreditoBO.setPrograma(programa);
			reporteCreditoBO.setListaClaseCredito(listaClaseCredito);	
			reporteCreditoBO.setListGarantia(listGarantia);
			//reporteCreditoBO.setListaSustentoOperacion(listaSustentoOperacion);
			
			reporteCreditoBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));

			reporteCreditoBO.updateReporteCredito(codEmpresaGrupo);
			setPrograma(programa);
			completarListaGrupoEmpresas();
			cargaTipoVcto(listaClaseCredito);
			addActionMessage("Datos Actualizados Correctamente");

		}	catch (Exception e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
		}
		return "reporteCreditoPF";
	}
	
	
	//ini MCG20130403
	public void saveDatosBasicoBlobRC(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		logger.info("valorBlob="+codEmpresa);
		
		try {
			DatosBasicoBlob seb = new DatosBasicoBlob ();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));			
			datosBasicosBlobBO.setPrograma(programa);
			datosBasicosBlobBO.setCampoBlob(campoBlob);//datosBasicosAddRCB
			datosBasicosBlobBO.setValorBlob(valorBlob);
			datosBasicosBlobBO.setCodEmpresa(codEmpresa);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			datosBasicosBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				datosBasicosBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				datosBasicosBlobBO.setPatronesEditor("");
			}
			datosBasicosBlobBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			datosBasicosBlobBO.save(seb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Busca un campo blob dentro del programa
	 * la respuesta es procesada via ajax
	 */
	public void consultarDatosBasicoBlobRC(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));				
			DatosBasicoBlob datosBasicoBlob = datosBasicosBlobBO.findDatosBasicoBlobByPrograma(programa,codEmpresa);
			if(datosBasicoBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	
			  //Sintesis economico	 	        
	 	       
	 	      if(campoBlob.equals("sintesisEmpresa") &&
	 	    		 datosBasicoBlob.getSintesisEmpresa()!=null){
	 		        for(byte x :datosBasicoBlob.getSintesisEmpresa() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("datosMatriz") &&
	 	    		 datosBasicoBlob.getDatosMatriz()!=null){
	 		        for(byte x :datosBasicoBlob.getDatosMatriz() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("espacioLibre") &&
	 	    		 datosBasicoBlob.getEspacioLibre()!=null){
	 		        for(byte x :datosBasicoBlob.getEspacioLibre() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("comenComprasVentas") &&
	 	    		 datosBasicoBlob.getComenComprasVentas()!=null){
	 		        for(byte x :datosBasicoBlob.getComenComprasVentas() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("concentracion") &&
		 	    		 datosBasicoBlob.getConcentracion()!=null){
		 		        for(byte x :datosBasicoBlob.getConcentracion() ){
		 		          	stb.append((char)FormatUtil.getCharUTF(x));
		 		        }
		 	        }
	 	      //add MLJ
	            if(campoBlob.equals("datosBasicosAdicional") ){
	            	if( datosBasicoBlob.getDatosBasicosaddRDC()!=null){
	            		for(byte x :datosBasicoBlob.getDatosBasicosaddRDC() ){
	            			stb.append((char)FormatUtil.getCharUTF(x));
		 		        }
		 	        }
	            }
	            
	            if(campoBlob.equals("datosBasicosAdicionalSincronizar") ){
	 	        	if(datosBasicoBlob.getSintesisEmpresa()!=null){
		 	        	for(byte x :datosBasicoBlob.getSintesisEmpresa() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(datosBasicoBlob.getDatosMatriz()!=null){
		 	        	stb.append("<br>");
		 	        	for(byte x :datosBasicoBlob.getDatosMatriz() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(stb!=null && stb.length()>0){
	 	        		valorBlob = stb.toString();
	 	        		//saveDatosBasicoBlobRC();//Guardando si es la primera vez que se ve datos basicos
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
	
	//fin MCG20130403
	
	public void consultarSintesisEconBlobRC(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));				
			SintesisEconomicoBlob sintesisEconomicoBlob = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(programa,codEmpresa);
			if(sintesisEconomicoBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	
	            if(campoBlob.equals("sintesisEconFinAdicional") ){
	            	if( sintesisEconomicoBlob.getComenSintesisEconFinaddRDC()!=null){
	            		for(byte x :sintesisEconomicoBlob.getComenSintesisEconFinaddRDC() ){
	 		            	stb.append((char)FormatUtil.getCharUTF(x));
	 		            }
	            	}
 	            }
	            
	            if(campoBlob.equals("sintesisEconFinAdicionalSincronizar") ){
	            	if(sintesisEconomicoBlob.getComenSituFinanciera()!=null){
            			stb.append("<b><u><font size='2'>Comentarios Situaci&oacute;n Financiera:</font></u></b><br>");
		 	        	for(byte x :sintesisEconomicoBlob.getComenSituFinanciera() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
            		if(sintesisEconomicoBlob.getComenSituEconomica()!=null){
            			stb.append("<br>");
            			stb.append("<b><u><font size='2'>Comentarios Situaci&oacute;n Econ&oacute;mica:</font></u></b><br>");
		 	        	for(byte x :sintesisEconomicoBlob.getComenSituEconomica() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
            		if(sintesisEconomicoBlob.getValoracionEconFinanciera()!=null){
            			stb.append("<br>");
            			stb.append("<b><u><font size='2'>Valoraci&oacute;n Situaci&oacute;n Econ&oacute;mica - Financiera:</u></font></b><br>");
		 	        	for(byte x :sintesisEconomicoBlob.getValoracionEconFinanciera() ){
			            	stb.append((char)FormatUtil.getCharUTF(x));
			            }
	 	        	}
	 	        	if(stb!=null && stb.length()>0){
	 	        		valorBlob = stb.toString();
	 	        		//saveSintesisEconBlobRC();//Guardando
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
	
	public void saveSintesisEconBlobRC(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		
		try {
			SintesisEconomicoBlob seb = new SintesisEconomicoBlob();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));			
			sintesisEconomicoBlobBO.setPrograma(programa);
			sintesisEconomicoBlobBO.setCampoBlob(campoBlob);
			sintesisEconomicoBlobBO.setValorBlob(valorBlob);
			sintesisEconomicoBlobBO.setCodEmpresa(codEmpresa);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			sintesisEconomicoBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				sintesisEconomicoBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				sintesisEconomicoBlobBO.setPatronesEditor("");
			}
			sintesisEconomicoBlobBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			sintesisEconomicoBlobBO.save(seb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**vchn-20130410**/
	public void cargaTipoVcto(List<ClaseCredito> lista){			
		int contCancelados=0;
		try {					
			if(lista.size()>0 && !lista.isEmpty()){
				for(ClaseCredito obj:lista){
					if(obj.getFlagCancelado()!= null && obj.getFlagCancelado().equals("CC")){
						contCancelados++;
					}
				}
				//tipoVctoRblso=lista.get(0).getTipoVcto();
			}
			
			cancelado= String.valueOf(contCancelados-1);

		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	/**vchn-20130410**/
	
	
	//ini MCG20141024
	public String saveFileReporteCredito(){
		
		Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
		ObtenerSessionCombos();
		String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);	
		String codEmpresaGrupo="";
		if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
			codEmpresaGrupo=getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
		}else{
			codEmpresaGrupo= getCodigoEmpresa();				
		}		
		Programa programa = new Programa();
		programa.setId(idPrograma);
		UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
		ArchivoReporteCredito archivoReporteCredito= new ArchivoReporteCredito();
		String strNombreArchivo = fileReporteCreditoFileName;
		if(fileReporteCreditoFileName!=null && !fileReporteCreditoFileName.equals("")){
			int extensionIndex = strNombreArchivo.lastIndexOf(".");
			String strExtension="";
	    	if(extensionIndex>0){
	    		strExtension = strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length());
	    	}
			if(fileReporteCreditoFileName.split("\\.").length>0){
				strNombreArchivo = fileReporteCreditoFileName.split("\\.")[0];
			}
	    	
			//setCamposAuditoria(archivoReporteCredito,  usuarioSession);
			archivoReporteCredito.setExtencion(strExtension);
			archivoReporteCredito.setNombreArchivo(strNombreArchivo);
			archivoReporteCredito.setCodEmpresaGrupo(codEmpresaGrupo);
			try {			
				reporteCreditoBO.setUsuarioSession(usuarioSession);
				reporteCreditoBO.saveFileReporteCredito(fileReporteCredito, 
											 programa, 
											 archivoReporteCredito);
				
				setListaArchivosReporteCredito(reporteCreditoBO.findListaArchivoReporteCredito(programa,codEmpresaGrupo));	
				removeObjectSession("listaArchivosReporteCredito");
				setObjectSession("listaArchivosReporteCredito", getListaArchivosReporteCredito());				
				
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
			}
			
		}else{
			addActionError("Seleccione un archivo");
		}
		completarListaGrupoEmpresas();
		return "reporteCreditoPF";
	}
	
	 public String downloadFileReporteCredito() throws Exception { 	
			logger.info("INICIO download");
			String codigoDocumento=getCodigoArchivo();
			String extencionDocumento= getExtension();
			String nombreDocumento=this.getNombreArchivo();
			String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
			File file=null;
			String forward = SUCCESS;
			try {
				String dir  = getObjectParamtrosSession(Constantes.DIR_FILE_REPORTE_CREDITO).toString();
				String pathToFile=dir+File.separator+nombreArchivo;
				String fileName=nombreDocumento; 
				file = new File(pathToFile);
			   	setContentType(new MimetypesFileTypeMap().getContentType(file));
			   	setContentDisposition("attachment;filename=\""+ fileName + "." + extencionDocumento + "\"");
				fileInputStream =  new FileInputStream(file);
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "reporteCreditoPF";
			}
			return forward;
	}
	 
		public String eliminarArchiReporteCredito(){
			String forward = "reporteCreditoPF";
			String codigoDocumento=getCodigoArchivo();
			String extencionDocumento= getExtension();
			String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
			File file = null;
			try {
				Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
				ObtenerSessionCombos();
				String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);	
				String codEmpresaGrupo="";
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					codEmpresaGrupo=getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
				}else{
					codEmpresaGrupo= getCodigoEmpresa();				
				}
				Programa programa = new Programa();
				programa.setId(idPrograma);
				ArchivoReporteCredito archivoReporteCredito = new ArchivoReporteCredito();
				archivoReporteCredito.setId(Long.valueOf(codigoDocumento));
				reporteCreditoBO.delete(archivoReporteCredito);
				String dir  = getObjectParamtrosSession(Constantes.DIR_FILE_REPORTE_CREDITO).toString();
				String pathToFile=dir+File.separator+nombreArchivo;
				file = new File(pathToFile);
				file.delete();
				addActionMessage("Eliminado Correctamente");
				setListaArchivosReporteCredito(reporteCreditoBO.findListaArchivoReporteCredito(programa,codEmpresaGrupo));
				
				removeObjectSession("listaArchivosReporteCredito");
				setObjectSession("listaArchivosReporteCredito", getListaArchivosReporteCredito());
				completarListaGrupoEmpresas();
				
			}catch (Exception e) {
				completarListaGrupoEmpresas();
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "reporteCreditoPF";
			}
			return forward;
		} 
	//fin MCG20141024

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

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}

	public List<SelectItem> getItemSegmento() {
		return itemSegmento;
	}

	public void setItemSegmento(List<SelectItem> itemSegmento) {
		this.itemSegmento = itemSegmento;
	}

	public List<Tabla> getListaSegmentos() {
		return listaSegmentos;
	}

	public void setListaSegmentos(List<Tabla> listaSegmentos) {
		this.listaSegmentos = listaSegmentos;
	}

	public List<SelectItem> getItemOficinaBEC() {
		return itemOficinaBEC;
	}

	public void setItemOficinaBEC(List<SelectItem> itemOficinaBEC) {
		this.itemOficinaBEC = itemOficinaBEC;
	}

	public List<Tabla> getListaOficinaBEC() {
		return listaOficinaBEC;
	}

	public void setListaOficinaBEC(List<Tabla> listaOficinaBEC) {
		this.listaOficinaBEC = listaOficinaBEC;
	}

	public String getRatingRDC() {
		return ratingRDC;
	}

	public void setRatingRDC(String ratingRDC) {
		this.ratingRDC = ratingRDC;
	}
	
	public String getClasificacionBancoRDC() {
		return clasificacionBancoRDC;
	}

	public void setClasificacionBancoRDC(String clasificacionBancoRDC) {
		this.clasificacionBancoRDC = clasificacionBancoRDC;
	}

	public String getCodigoCentralRDC() {
		return codigoCentralRDC;
	}

	public void setCodigoCentralRDC(String codigoCentralRDC) {
		this.codigoCentralRDC = codigoCentralRDC;
	}

	public String getContratoRDC() {
		return contratoRDC;
	}

	public void setContratoRDC(String contratoRDC) {
		this.contratoRDC = contratoRDC;
	}

	public ReporteCreditoBO getReporteCreditoBO() {
		return reporteCreditoBO;
	}

	public void setReporteCreditoBO(ReporteCreditoBO reporteCreditoBO) {
		this.reporteCreditoBO = reporteCreditoBO;
	}

	public List<SelectItem> getItemCuentaRDC() {
		return itemCuentaRDC;
	}

	public void setItemCuentaRDC(List<SelectItem> itemCuentaRDC) {
		this.itemCuentaRDC = itemCuentaRDC;
	}

	public List<Tabla> getListaCuentaRDC() {
		return listaCuentaRDC;
	}

	public void setListaCuentaRDC(List<Tabla> listaCuentaRDC) {
		this.listaCuentaRDC = listaCuentaRDC;
	}

	public List<SelectItem> getItemMonedaRDC() {
		return itemMonedaRDC;
	}

	public void setItemMonedaRDC(List<SelectItem> itemMonedaRDC) {
		this.itemMonedaRDC = itemMonedaRDC;
	}

	public List<Tabla> getListaMonedaRDC() {
		return listaMonedaRDC;
	}

	public void setListaMonedaRDC(List<Tabla> listaMonedaRDC) {
		this.listaMonedaRDC = listaMonedaRDC;
	}

	public List<ClaseCredito> getListaClaseCredito() {
		return listaClaseCredito;
	}

	public void setListaClaseCredito(List<ClaseCredito> listaClaseCredito) {
		this.listaClaseCredito = listaClaseCredito;
	}

	public String getChkClaseCredito() {
		return chkClaseCredito;
	}

	public void setChkClaseCredito(String chkClaseCredito) {
		this.chkClaseCredito = chkClaseCredito;
	}


	public String getIdOficina() {
		return idOficina;
	}


	public void setIdOficina(String idOficina) {
		this.idOficina = idOficina;
	}


	public String getIdSegmento() {
		return idSegmento;
	}


	public void setIdSegmento(String idSegmento) {
		this.idSegmento = idSegmento;
	}

	public boolean isFlagClaseCredito() {
		return flagClaseCredito;
	}

	public void setFlagClaseCredito(boolean flagClaseCredito) {
		this.flagClaseCredito = flagClaseCredito;
	}

	public boolean isFlagGarantia() {
		return flagGarantia;
	}

	public void setFlagGarantia(boolean flagGarantia) {
		this.flagGarantia = flagGarantia;
	}

	public List<Garantia> getListGarantia() {
		return listGarantia;
	}

	public void setListGarantia(List<Garantia> listGarantia) {
		this.listGarantia = listGarantia;
	}

	public String getChksParaEliminar() {
		return chksParaEliminar;
	}

	public void setChksParaEliminar(String chksParaEliminar) {
		this.chksParaEliminar = chksParaEliminar;
	}

	public boolean isFlagSustento() {
		return flagSustento;
	}

	public void setFlagSustento(boolean flagSustento) {
		this.flagSustento = flagSustento;
	}

	public String getChkSustento() {
		return chkSustento;
	}

	public void setChkSustento(String chkSustento) {
		this.chkSustento = chkSustento;
	}

	public List<SustentoOperacion> getListaSustentoOperacion() {
		return listaSustentoOperacion;
	}

	public void setListaSustentoOperacion(
			List<SustentoOperacion> listaSustentoOperacion) {
		this.listaSustentoOperacion = listaSustentoOperacion;
	}

	public DatosBasicosBO getDatosBasicosBO() {
		return datosBasicosBO;
	}

	public void setDatosBasicosBO(DatosBasicosBO datosBasicosBO) {
		this.datosBasicosBO = datosBasicosBO;
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

	public ParametroBO getParametroBO() {
		return parametroBO;
	}

	public void setParametroBO(ParametroBO parametroBO) {
		this.parametroBO = parametroBO;
	}

	public boolean isFlagDatosBasicosAdicionalesPrin() {
		return flagDatosBasicosAdicionalesPrin;
	}

	public void setFlagDatosBasicosAdicionalesPrin(
			boolean flagDatosBasicosAdicionalesPrin) {
		this.flagDatosBasicosAdicionalesPrin = flagDatosBasicosAdicionalesPrin;
	}

	public boolean isFlagSintesisEconFinAdicional() {
		return flagSintesisEconFinAdicional;
	}

	public void setFlagSintesisEconFinAdicional(boolean flagSintesisEconFinAdicional) {
		this.flagSintesisEconFinAdicional = flagSintesisEconFinAdicional;
	}

	public RelacionesBancariasBO getRelacionesBancariasBO() {
		return relacionesBancariasBO;
	}

	public void setRelacionesBancariasBO(RelacionesBancariasBO relacionesBancariasBO) {
		this.relacionesBancariasBO = relacionesBancariasBO;
	}

	public PFSunatBO getSunatBO() {
		return sunatBO;
	}

	public void setSunatBO(PFSunatBO sunatBO) {
		this.sunatBO = sunatBO;
	}

	public RatingBO getRatingBO() {
		return ratingBO;
	}

	public void setRatingBO(RatingBO ratingBO) {
		this.ratingBO = ratingBO;
	}

	public String getCiiuRDC() {
		return ciiuRDC;
	}

	public void setCiiuRDC(String ciiuRDC) {
		this.ciiuRDC = ciiuRDC;
	}

	public boolean isFlagPosicionCliente() {
		return flagPosicionCliente;
	}

	public void setFlagPosicionCliente(boolean flagPosicionCliente) {
		this.flagPosicionCliente = flagPosicionCliente;
	}

	public PoliticasRiesgoBO getPoliticasRiesgoBO() {
		return politicasRiesgoBO;
	}

	public void setPoliticasRiesgoBO(PoliticasRiesgoBO politicasRiesgoBO) {
		this.politicasRiesgoBO = politicasRiesgoBO;
	}

	public String getRiesgovigenteRDC() {
		return riesgovigenteRDC;
	}

	public void setRiesgovigenteRDC(String riesgovigenteRDC) {
		this.riesgovigenteRDC = riesgovigenteRDC;
	}

	public String getIncrementoRDC() {
		return incrementoRDC;
	}

	public void setIncrementoRDC(String incrementoRDC) {
		this.incrementoRDC = incrementoRDC;
	}

	public String getRiesgopropuestoRDC() {
		return riesgopropuestoRDC;
	}

	public void setRiesgopropuestoRDC(String riesgopropuestoRDC) {
		this.riesgopropuestoRDC = riesgopropuestoRDC;
	}

	public String getTipoMilesPR() {
		return tipoMilesPR;
	}

	public void setTipoMilesPR(String tipoMilesPR) {
		this.tipoMilesPR = tipoMilesPR;
	}

	public AnexoGarantiaBO getAnexoGarantiaBO() {
		return anexoGarantiaBO;
	}

	public void setAnexoGarantiaBO(AnexoGarantiaBO anexoGarantiaBO) {
		this.anexoGarantiaBO = anexoGarantiaBO;
	}

	public List<Empresa> getListaEmpresasGrupoRC() {
		return listaEmpresasGrupoRC;
	}

	public void setListaEmpresasGrupoRC(List<Empresa> listaEmpresasGrupoRC) {
		this.listaEmpresasGrupoRC = listaEmpresasGrupoRC;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getFlagChangeEmpresa() {
		return flagChangeEmpresa;
	}

	public void setFlagChangeEmpresa(String flagChangeEmpresa) {
		this.flagChangeEmpresa = flagChangeEmpresa;
	}

	public String getCodempresagrupo() {
		return codempresagrupo;
	}

	public void setCodempresagrupo(String codempresagrupo) {
		this.codempresagrupo = codempresagrupo;
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

	public String getDeudaSBS() {
		return deudaSBS;
	}

	public void setDeudaSBS(String deudaSBS) {
		this.deudaSBS = deudaSBS;
	}
	/**vchn-20130410**/
	public List<Tabla> getListaVctoRblso() {
		return listaVctoRblso;
	}

	public void setListaVctoRblso(List<Tabla> listaVctoRblso) {
		this.listaVctoRblso = listaVctoRblso;
	}

	/**vchn-20130410**/
	/**gmp-20130410**/
	public void setSustento(SustentoOperacion sustento) {
		this.sustento = sustento;
	}

	public SustentoOperacion getSustento() {
		return sustento;
	}

	public void setIdSustento(Long idSustento) {
		this.idSustento = idSustento;
	}

	public Long getIdSustento() {
		return idSustento;
	}
	
	/**
	 * Metodo para definir el tiempo máximo de duración de la session
	 * @param time
	 */
	public void setMaxInactiveInterval(int time){
		getRequest().getSession().setMaxInactiveInterval(time);
	}
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	public ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
	
	public HttpSession getSession(boolean create){
		return getRequest().getSession(create);
	}
		/**gmp-20130410**/

	public String getCodEmpresaGrupoS() {
		return codEmpresaGrupoS;
	}

	public void setCodEmpresaGrupoS(String codEmpresaGrupoS) {
		this.codEmpresaGrupoS = codEmpresaGrupoS;
	}

	public String getValorSustento() {
		return valorSustento;
	}

	public void setValorSustento(String valorSustento) {
		this.valorSustento = valorSustento;
	}
	/*vchn*/
	public List<SelectItem> getItemReembolso() {
		return itemReembolso;
	}

	public void setItemReembolso(List<SelectItem> itemReembolso) {
		this.itemReembolso = itemReembolso;
	}

	public List<Tabla> getListaReembolso() {
		return listaReembolso;
	}

	public void setListaReembolso(List<Tabla> listaReembolso) {
		this.listaReembolso = listaReembolso;
	}

	public String getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	/*vchn*/

	public String getCancelado() {
		return cancelado;
	}

	public void setCancelado(String cancelado) {
		this.cancelado = cancelado;
	}

	public List<SelectItem> getItemVctoRblso() {
		return itemVctoRblso;
	}

	public void setItemVctoRblso(List<SelectItem> itemVctoRblso) {
		this.itemVctoRblso = itemVctoRblso;
	}

	public List<SelectItem> getItemCuentaGarantiaRDC() {
		return itemCuentaGarantiaRDC;
	}

	public void setItemCuentaGarantiaRDC(List<SelectItem> itemCuentaGarantiaRDC) {
		this.itemCuentaGarantiaRDC = itemCuentaGarantiaRDC;
	}

	public List<Tabla> getListaCuentaGarantiaRDC() {
		return listaCuentaGarantiaRDC;
	}

	public void setListaCuentaGarantiaRDC(List<Tabla> listaCuentaGarantiaRDC) {
		this.listaCuentaGarantiaRDC = listaCuentaGarantiaRDC;
	}

	public List<SelectItem> getItemCuentaCreditoRDC() {
		return itemCuentaCreditoRDC;
	}

	public void setItemCuentaCreditoRDC(List<SelectItem> itemCuentaCreditoRDC) {
		this.itemCuentaCreditoRDC = itemCuentaCreditoRDC;
	}
	//

	public Long getIdeSustento() {
		return ideSustento;
	}

	public void setIdeSustento(Long ideSustento) {
		this.ideSustento = ideSustento;
	}

	public Long getIdCuentaSustento() {
		return idCuentaSustento;
	}

	public void setIdCuentaSustento(Long idCuentaSustento) {
		this.idCuentaSustento = idCuentaSustento;
	}

	public Long getIdCuentaFinalSustento() {
		return idCuentaFinalSustento;
	}

	public void setIdCuentaFinalSustento(Long idCuentaFinalSustento) {
		this.idCuentaFinalSustento = idCuentaFinalSustento;
	}

	public String getOrdenSustento() {
		return ordenSustento;
	}

	public void setOrdenSustento(String ordenSustento) {
		this.ordenSustento = ordenSustento;
	}

	public String getOrdenFinalSustento() {
		return ordenFinalSustento;
	}

	public void setOrdenFinalSustento(String ordenFinalSustento) {
		this.ordenFinalSustento = ordenFinalSustento;
	}

	public String getNombreEmpresaRDC() {
		return nombreEmpresaRDC;
	}

	public void setNombreEmpresaRDC(String nombreEmpresaRDC) {
		this.nombreEmpresaRDC = nombreEmpresaRDC;
	}

	public List<SelectItem> getItemConectorGarantiaRDC() {
		return itemConectorGarantiaRDC;
	}

	public void setItemConectorGarantiaRDC(List<SelectItem> itemConectorGarantiaRDC) {
		this.itemConectorGarantiaRDC = itemConectorGarantiaRDC;
	}

	public List<Tabla> getListaConectorGarantiaRDC() {
		return listaConectorGarantiaRDC;
	}

	public void setListaConectorGarantiaRDC(List<Tabla> listaConectorGarantiaRDC) {
		this.listaConectorGarantiaRDC = listaConectorGarantiaRDC;
	}


	public boolean isFlagArchivoReporteCredito() {
		return flagArchivoReporteCredito;
	}

	public void setFlagArchivoReporteCredito(boolean flagArchivoReporteCredito) {
		this.flagArchivoReporteCredito = flagArchivoReporteCredito;
	}

	public File getFileReporteCredito() {
		return fileReporteCredito;
	}

	public void setFileReporteCredito(File fileReporteCredito) {
		this.fileReporteCredito = fileReporteCredito;
	}



	public String getFileReporteCreditoFileName() {
		return fileReporteCreditoFileName;
	}

	public void setFileReporteCreditoFileName(String fileReporteCreditoFileName) {
		this.fileReporteCreditoFileName = fileReporteCreditoFileName;
	}

	public List<ArchivoReporteCredito> getListaArchivosReporteCredito() {
		return listaArchivosReporteCredito;
	}

	public void setListaArchivosReporteCredito(
			List<ArchivoReporteCredito> listaArchivosReporteCredito) {
		this.listaArchivosReporteCredito = listaArchivosReporteCredito;
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

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
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

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
		
	
	
}
