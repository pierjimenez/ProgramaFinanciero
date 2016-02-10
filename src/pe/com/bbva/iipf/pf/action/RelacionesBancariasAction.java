package pe.com.bbva.iipf.pf.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.model.Comex;
import pe.com.bbva.iipf.pf.model.EfetividadCartera;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.PoolBancario;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.RentabilidadBEC;
import pe.com.bbva.iipf.pf.model.Tbanco;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.bbva.iipf.pf.model.rccmes;
import pe.com.bbva.iipf.pf.model.rcd;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;


@Service("relacionesBancariasAction")
@Scope("prototype")
public class RelacionesBancariasAction extends GenericAction {
	Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = -717614871157397082L;

	@Resource
	private RelacionesBancariasBO relacionesBancariasBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private Programa programa;
	
	private List<rccmes> listrccmes=new ArrayList<rccmes>();
	private List<rcd> listrcd=new ArrayList<rcd>();
	
	private List<LineasRelacionBancarias> listLineasRelacionesBancarias = new ArrayList<LineasRelacionBancarias>();
	private String chkLineas;
	private String chkIndiceTransa;
	
	
	private String tipoMiles;
	private List<Tabla> listaTipoMiles= new ArrayList<Tabla>();
	
	private List<RentabilidadBEC> listRentabilidadBEC= new ArrayList<RentabilidadBEC>();	
	private List<List> listPoolBancarioTotal = new ArrayList<List>();


	private LineasRelacionBancarias lineasRelacionBancarias;
	private List<PoolBancario> listPoolBancarioPragrama;
	private static long COUNT = 0;
	private String comenLineas;

	private String comenClientGlob;
	private String desCalifBanco;

	// ///

	private List<Tempresa> listaEmpresa;
	private List<Tempresa> listaEmpresaSelect;
	private List<Tbanco> listaBanco;
	private List<Tbanco> listaBancoSelect;

	// //////////////////////*
	private String leftSidebanco;
	private String rightSidebanco;

	private String leftSideempresa;
	private String rightSideempresa;
	
	/**
	 * variables para mostrar ocultar 
	 * las listas en el formulario
	 */
	private boolean flagPoolBancario;
	private boolean flagLineas;
	private boolean flagIndiceTransa;
	private boolean flagCuotaFinanciera;
	private boolean flagModeloRentabilidad;
	private boolean flagEfectividadCartera;
	private boolean flagEditado;
	private boolean flagComex;

	
	/*********/
	private List<String> listaModeloRentabilidad;
	private String tipoDeuda;
	private String modeloRentabilidad;

	
	/**
	 * 
	 */
	private boolean checkTipoDeudaTotal;
	private boolean checkTipoDeudaDirecta;
	private boolean checkTipoDeudaIndirecta;
	
	private String dato1;
	
	private String mensajeErrorHost="";
	
	private List<Comex> listaComexImportacion=new ArrayList<Comex>();
	private List<Comex> listaComexExportacion=new ArrayList<Comex>();
	private String periodoComex ;
	private List<Comex> listaComex=new ArrayList<Comex>();
	
	private String ratioReprocidadImp;
	private String ratioReprocidadExp;
	
	// /////////////////*
	public String init() {
		iniListado();
		loadRelacionesBancarias();
		cargaTipoMiles();
		return "relacionesbancarias";
	}

	public void iniListado() {
		listaEmpresa = new ArrayList<Tempresa>();
		listaEmpresaSelect = new ArrayList<Tempresa>();
		listaBanco = new ArrayList<Tbanco>();
		listaBancoSelect = new ArrayList<Tbanco>();
		listaModeloRentabilidad = new ArrayList<String>();
		listaComex=new ArrayList<Comex>();
	}

	public void iniciarVariablesTimeOut(){
		HttpSession session= getSession(true);
		session.setAttribute("camposEditados", 0);
	}
	public void cambiarEstadoTimeOut(){
		HttpSession session= getSession(true);
		String estado=getRequest().getParameter("camposEditados");
		session.setAttribute("camposEditados", estado);
	}
	private void cargarSessionListadoComex(){		
		setObjectSession("listaComexImportacionSession", listaComexImportacion);
		setObjectSession("listaComexExportacionSession", listaComexExportacion);		
	}
	private void AsignarListadoComexSession(){
		if(getObjectSession("listaComexImportacionSession") != null){
			listaComexImportacion = (List<Comex>)getObjectSession("listaComexImportacionSession");
		}
		if(getObjectSession("listaComexExportacionSession") != null){
			listaComexExportacion = (List<Comex>)getObjectSession("listaComexExportacionSession");
		}
	}
	
	
	public void loadRelacionesBancarias() {
		try {
			iniciarVariablesTimeOut();
			if(getObjectSession(Constantes.ID_PROGRAMA_SESSION) != null) {
				Long Idprograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
				if(Idprograma != null && !Idprograma.equals("")) {
					setPrograma(programaBO.findById(Idprograma));	
					this.setModeloRentabilidad(programa.getIdmodeloRentabilidad());
					String codCentral=programa.getIdEmpresa();
					loadClasificacionSistemaFinanciero2(getPrograma(),Constantes.TIPO_DOCUMENTO_RUC);					
					desCalifBanco = loadCalificacionBanco(getPrograma());
					loadEfectividadCartera(getPrograma(),Constantes.ID_CODIGO_SERVICIO);
					loadLineasRelacionesBancarias(Idprograma);
					CargarEmpresasRelacionesBancarias();
					cargarTipoDeuda();
					cargarBanco();	
					//habilitar para siguiente pase
					cargarComex(Idprograma,codCentral);					
					//GeneraModeloRentabilidad(Idprograma.toString(),codCentral);
					cargarModeloRentabilidad();
					setTipoMiles(getPrograma().getTipoMilesRB()==null?Constantes.ID_TABLA_TIPOMILES_MILES:getPrograma().getTipoMilesRB().toString());
				}
				String idTipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
				programa.setPeriodoArchivo(relacionesBancariasBO.obtenerFechaCuotaFinancieraEmpresa(programa));
				if(idTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					programa.setCuotaFinanciera(relacionesBancariasBO.calcularCuotaFinancieraEmpresa(programa)+"");
				}else{
					List<Empresa>  listaEmpersas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
					programa.setCuotaFinanciera(relacionesBancariasBO.calcularCuotaFinancieraGrupo(programa, listaEmpersas)+"");
				}
			}
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
//	public List<RentabilidadBEC> GeneraModeloRentabilidad(){			
//		
//		List<Rentabilidad> listRentabilidad= new ArrayList<Rentabilidad>();	
//		List<RentabilidadBEC> listRentabilidaBECtmp= new ArrayList<RentabilidadBEC>();
//		List<RentabilidadBEC> listRentabilidadBECtmpIni= new ArrayList<RentabilidadBEC>();
//		try {		
//		listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
//		String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
//			
//		listRentabilidaBECtmp =relacionesBancariasBO.findRentabilidadBEC(Idprograma);		
//		Programa programatemp=new Programa();		
//		programatemp=programaBO.findById(Long.valueOf(Idprograma));		
//		String codCentral=programatemp.getIdEmpresa();
//		
//			if (listRentabilidaBECtmp != null && listRentabilidaBECtmp.size() > 0) {			
//				listRentabilidadBEC=listRentabilidaBECtmp;
//				
//			}else{
//				listRentabilidad = relacionesBancariasBO.findRentabilidad(codCentral);
//				Rentabilidad orentabilidad=new Rentabilidad();
//				if (listRentabilidad!=null && listRentabilidad.size()>0){
//					orentabilidad=listRentabilidad.get(0);						
//				}
//					
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_INVERSIONES,orentabilidad.getSalmedioInversion()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_RECURSOS,orentabilidad.getSalmedioRecurso()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_DESINTERMED,orentabilidad.getSalmedioDesintermed()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.IMPORTE_SERVICIO,orentabilidad.getImporteServicio()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.CUENTA_CENTRAL,orentabilidad.getCuentaCentral()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_INVERSION,orentabilidad.getSpfmesInversion()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_RECURSOS,orentabilidad.getSpfmesRecurso()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_DESINTERMED,orentabilidad.getSpfmesDesintermed()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_FINANCIERO,orentabilidad.getMargenFinanciero()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COMISIONES,orentabilidad.getComisiones()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_ORDINARIO,orentabilidad.getMargenOrdinario()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.SANEAMIENTO_NETO,orentabilidad.getSaneamientoNeto()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COSTES_OPERATIVOS,orentabilidad.getCostesOperativo()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.RESULTADOS,orentabilidad.getResultado()));
//						listRentabilidadBECtmpIni.add(ObtenerRentabilidad(Idprograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.ROA,orentabilidad.getRoa()));				
//					listRentabilidadBEC=listRentabilidadBECtmpIni;
//				
//				
//			}		
//			
//		} catch (Exception e) {
//		logger.error(StringUtil.getStackTrace(e));
//		return listRentabilidadBEC;
//		}
//		return listRentabilidadBEC;
//		
//	}

//	public RentabilidadBEC ObtenerRentabilidad(String Idprograma,String tipo,String descMonto,String Monto){
//		
//		RentabilidadBEC oRentabilidadBEC=new RentabilidadBEC();
//		Programa oprograma=new Programa();
//		oprograma.setId(Long.valueOf(Idprograma));	
//		oRentabilidadBEC.setId(null);	
//		oRentabilidadBEC.setPrograma(oprograma);		
//		oRentabilidadBEC.setTipoRentabilidad(tipo);
//		oRentabilidadBEC.setDescripcionMonto(descMonto);
//		oRentabilidadBEC.setMonto(Monto==null?"":Monto);
//		return oRentabilidadBEC;
//	}
	
	public void loadClasificacionSistemaFinanciero2(Programa programa,String tipoDocumento) {
		try {
			if (programa.getPorcentajeNormalSF()==null){
				List<HashMap<String, BigDecimal>> listrccmestmp=relacionesBancariasBO.findByClasificacionSistemaFinanciero2(programa.getRuc());
				logger.info(listrccmestmp);
				HashMap<String, BigDecimal> orccmes=new HashMap<String, BigDecimal>();
				if (listrccmestmp!=null && listrccmestmp.size()>0){		
					orccmes = listrccmestmp.get(0);
					logger.info("calificacion= "+orccmes);
					programa.setPorcentajeNormalSF(orccmes.get("NOR").toString().equals("0")?"":orccmes.get("NOR").toString());
					programa.setPorcentajeProblemaPotencialSF(orccmes.get("CPP").toString().equals("0")?"":orccmes.get("CPP").toString());
					programa.setPorcentajeDeficienteSF(orccmes.get("DEF").toString().equals("0")?"":orccmes.get("DEF").toString());
					programa.setPorcentajeDudosoSF(orccmes.get("DUD").toString().equals("0")?"":orccmes.get("DUD").toString());
					programa.setPorcentajePerdidaSF(orccmes.get("PER").toString().equals("0")?"":orccmes.get("PER").toString());
				}
			}
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	public String RefreshClasificacionFinanciera(){
		try {
			cargaTipoMiles();
			this.asignarListado();
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			String rucEmpresa=(String)getObjectSession(Constantes.COD_RUC_EMPRESA_SESSION);
			List<HashMap<String, BigDecimal>> listrccmestmp=relacionesBancariasBO.findByClasificacionSistemaFinanciero2((String)getObjectSession(Constantes.COD_RUC_EMPRESA_SESSION));
			logger.info(listrccmestmp);
			HashMap<String, BigDecimal> orccmes=new HashMap<String, BigDecimal>();
			if (listrccmestmp!=null && listrccmestmp.size()>0){		
				orccmes = listrccmestmp.get(0);
				logger.info("calificacion= "+orccmes);
				String nor1=orccmes.get("NOR").toString().equals("0")?"":orccmes.get("NOR").toString();
				String cpp1=orccmes.get("CPP").toString().equals("0")?"":orccmes.get("CPP").toString();
				String deff1=orccmes.get("DEF").toString().equals("0")?"":orccmes.get("DEF").toString();
				String dud1=orccmes.get("DUD").toString().equals("0")?"":orccmes.get("DUD").toString();
				String per1=orccmes.get("PER").toString().equals("0")?"":orccmes.get("PER").toString();
				programaBO.refreshClasificacionFinaciera(nor1,cpp1,deff1,dud1,per1,idprograma);
				programa.setPorcentajeNormalSF(orccmes.get("NOR").toString().equals("0")?"":orccmes.get("NOR").toString());
				programa.setPorcentajeProblemaPotencialSF(orccmes.get("CPP").toString().equals("0")?"":orccmes.get("CPP").toString());
				programa.setPorcentajeDeficienteSF(orccmes.get("DEF").toString().equals("0")?"":orccmes.get("DEF").toString());
				programa.setPorcentajeDudosoSF(orccmes.get("DUD").toString().equals("0")?"":orccmes.get("DUD").toString());
				programa.setPorcentajePerdidaSF(orccmes.get("PER").toString().equals("0")?"":orccmes.get("PER").toString());
				addActionMessage("Datos Actualizados");
			}
			
		} catch (Exception e) {
			 
		}
		return "relacionesbancarias";
	}
	
	public void loadEfectividadCartera(Programa programa,String codServicio) {
		try {
			if (programa.getEfectividadProm6sol()==null){
			
				List<EfetividadCartera> listoefetividadCarteratmp=relacionesBancariasBO.findEfectividadCartera(programa.getRuc(),codServicio);
				EfetividadCartera oefetividadCartera=new EfetividadCartera();
				if (listoefetividadCarteratmp!=null && listoefetividadCarteratmp.size()>0){
					oefetividadCartera=listoefetividadCarteratmp.get(0);			
					programa.setEfectividadProm6sol(oefetividadCartera.getEfectividadProm6sol());
					programa.setEfectividadProm6dol(oefetividadCartera.getEfectividadProm6dol());
					programa.setProtestoProm6sol(oefetividadCartera.getProtestoProm6sol());
					programa.setProtestoProm6dol(oefetividadCartera.getProtestoProm6dol());
//					programa.setEfectividadUltmaniosol(oefetividadCartera.getEfectividadUltmaniosol());
//					programa.setEfectividadUltmaniodol(oefetividadCartera.getEfectividadUltmaniodol());
				}
			}
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}

	public String loadCalificacionBanco(Programa programa) {
		try{
			String clasi=programa.getCalificacionBanco();
			String urlRig4 = getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
			if(clasi==null||clasi.equals("")){
				HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(programa.getIdEmpresa(),getConfiguracionWS(),urlRig4);
				clasi=datos.get("clasificacionBanco");	
			}else{
				try{
					double aux=Double.parseDouble(clasi.trim());
					HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(programa.getIdEmpresa(),getConfiguracionWS(),urlRig4);
					clasi=datos.get("clasificacionBanco");	
				}catch (Exception e) {
					
				}
			}
			clasi=relacionesBancariasBO.obtenerEquivClasificacionBanco(clasi);
			return clasi;
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		/*try {
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
					programa.setCalificacionBanco(orcd.getCalificacionAlineada());	
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
				//programa.setCalificacionBanco(calificacion);			
			}			
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}*/
		
		
	}
	


	public void loadLineasRelacionesBancarias(Long Idprograma) {
		try {
			logger.info("lineasize ini:");
			listLineasRelacionesBancarias = relacionesBancariasBO
					.findByLineasRelacionesBancarias(Idprograma);
			logger.info("lineasize:"
					+ listLineasRelacionesBancarias.size());
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}

	public String addLinea() throws Exception {

		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		Long idprograma = Long.valueOf(sessionparam.get(
				Constantes.ID_PROGRAMA_SESSION).toString());
		logger.info("right side list empresa: " + getRightSideempresa());

		if (listLineasRelacionesBancarias == null) {
			listLineasRelacionesBancarias = new ArrayList<LineasRelacionBancarias>();
		}

		logger.info(getListLineasRelacionesBancarias());
		Programa programatemp = new Programa();
		programatemp.setId(idprograma);
		logger.info("size=" + listLineasRelacionesBancarias.size());
		LineasRelacionBancarias linea = new LineasRelacionBancarias();

		linea.setId(null);
		linea.setLinea("");
		linea.setSgrclteform(null);
		linea.setSgrcdpto(null);
		linea.setSgrplteform(null);
		linea.setSgrpdpto(null);
		linea.setSgrflteform(null);
		linea.setSgrfdpto(null);
		linea.setSgtesolteform(null);
		linea.setSgtesodpto(null);
		linea.setSgtotallteform(null);
		linea.setSgtotaldpto(null);
		linea.setPrograma(programatemp);
		if(getChkLineas()!=null){
			 String [] idsEliminar = getChkLineas().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listLineasRelacionesBancarias.add(posicion, linea);
			 }
		}else{
			listLineasRelacionesBancarias.add(linea);
		}
		logger.info("listLineasRelacionesBancariasx: "
				+ listLineasRelacionesBancarias.size());
		asignarListado();

		return "relacionesbancarias";

	}

	public String deleteLinea() {
		if(getChkLineas()!=null){
			 String [] idsEliminar = getChkLineas().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listLineasRelacionesBancarias.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
		}
//		if (listLineasRelacionesBancarias != null
//				&& listLineasRelacionesBancarias.size() > 0) {
//			listLineasRelacionesBancarias.remove(listLineasRelacionesBancarias
//					.size() - 1);
//		}
		asignarListado();

		return "relacionesbancarias";
	}
	
	//////EPOMAYAY 16062012
	public void cargaTipoMiles(){			
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		listaTipoMiles =new ArrayList<Tabla>(); 		
		try {					
			listaTipoMiles = tablaBO.listarHijos(Constantes.ID_TABLA_TIPOMILES);
			sessionparam.put("LISTATIPOMILES", listaTipoMiles);	
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	//////EPOMAYAY 16062012

	public void asignarListado() {
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		listaEmpresa = (List<Tempresa>) sessionparam.get("LISTAEMPRESA");
		listaEmpresaSelect = (List<Tempresa>) sessionparam.get("LISTAEMPRESASELECT");
		listaBanco = (List<Tbanco>) sessionparam.get("LISTABANCO");
		listaBancoSelect = (List<Tbanco>) sessionparam.get("LISTABANCOSELECT");
//		listaTipoDeuda = (List<String>) sessionparam.get("TIPODEUDA");
		listaModeloRentabilidad = (List<String>) sessionparam.get("MODELORENTABILIDAD");
		listaTipoMiles = (List<Tabla>) sessionparam.get("LISTATIPOMILES");	
		AsignarListadoComexSession();
	}

//	public String savelinea() throws Exception {
//		logger.info("lineakkkkkk:"
//				+ this.getLineasRelacionBancarias().getLinea().toString());
//		System.out
//				.println("savelineax:" + listLineasRelacionesBancarias.size());
//		for (Iterator iter = listLineasRelacionesBancarias.iterator(); iter
//				.hasNext();) {
//			LineasRelacionBancarias linea = (LineasRelacionBancarias) iter
//					.next();
//			logger.info("linea1:" + linea.getLinea());
//			// relacionesBancariasBO.savelineas(linea);
//		}
//		return "relacionesbancarias";
//	}


	public String updateRelacionesBancarias() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		String id = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		String idEmpresa = (String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
		String codTipoEmpresaGrupo=sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		String idGrupo=sessionparam.get(Constantes.COD_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.COD_GRUPO_SESSION).toString();
		String rucEmpresaPrincipal=sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();		
				
		Tabla tipoEmpresa = new Tabla();
		tipoEmpresa.setId(Long.valueOf(codTipoEmpresaGrupo));		
		
		programa.setId(Long.valueOf(id));
		programa.setTipoEmpresa(tipoEmpresa);
		programa.setIdGrupo(idGrupo);
		programa.setRuc(rucEmpresaPrincipal);	
		programa.setIdmodeloRentabilidad(getModeloRentabilidad());
		programa.setTipoMilesRB(Long.parseLong(tipoMiles));
		
		asignarListado();
		try {			
			if (listaBancoSelect != null) {
				if (listaBancoSelect.size() > 0) {
					String strbanco = listaBancoSelect.get(0).getNombreBanco();
				}
			}
	
//			String calificacion=Constantes.ID_CALIFICACION_NORMAL;
//			if (programa.getCalificacionBanco().equals(Constantes.CALIFICACION_NORMAL)){
//				calificacion=Constantes.ID_CALIFICACION_NORMAL;
//			}else if(programa.getCalificacionBanco().equals(Constantes.CALIFICACION_PROBLEMAPOTENCIAL)){
//				calificacion=Constantes.ID_CALIFICACION_PROBLEMAPOTENCIAL;
//			}
//			else if(programa.getCalificacionBanco().equals(Constantes.CALIFICACION_DEFICIENTE)){
//				calificacion=Constantes.ID_CALIFICACION_DEFICIENTE;
//			}
//			else if(programa.getCalificacionBanco().equals(Constantes.CALIFICACION_DUDOSO)){
//				calificacion=Constantes.ID_CALIFICACION_DUDOSO;
//			}
//			else if(programa.getCalificacionBanco().equals(Constantes.CALIFICACION_PERDIDA)){
//				calificacion=Constantes.ID_CALIFICACION_PERDIDA;
//			}
//			programa.setCalificacionBanco(calificacion);	
					
			String ArraycodTipoDeudas="";
			if (checkTipoDeudaTotal){
				ArraycodTipoDeudas +="1" + ",";				
			}
			if (checkTipoDeudaDirecta){
				ArraycodTipoDeudas +="2" + ",";				
			}
			if (checkTipoDeudaIndirecta){
				ArraycodTipoDeudas +="3" + ",";				
			}
			if (ArraycodTipoDeudas.length()>0){
			int pos=ArraycodTipoDeudas.length()-1;
			ArraycodTipoDeudas=ArraycodTipoDeudas.substring(0, pos);
			}
			
			relacionesBancariasBO.setPrograma(programa);
			relacionesBancariasBO.setListLineasRelacionesBancarias(listLineasRelacionesBancarias);
			relacionesBancariasBO.setComenLineas(comenLineas);
			
			///			
			relacionesBancariasBO.setCodTipoDeudas(ArraycodTipoDeudas);
			relacionesBancariasBO.setListaEmpresaSelect(listaEmpresaSelect);
			relacionesBancariasBO.setListaBancoSelect(listaBancoSelect);
			///
			listaComex=getlistaTotalComex();
			relacionesBancariasBO.setListaComex(listaComex);
			List<RentabilidadBEC> olistaRentabilidadtempBEC=new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC> olistaRentabilidadtempBECPrev=new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC> olisRentabilidadBEC=new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC> olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
			List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();
			try {
				String valswRefresh = getObjectSession(Constantes.swRefresh).toString();
				
				if(valswRefresh.equals("1")){	
					
					listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(id.toString()));	
					
					if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {						
						for (Empresa listaempr:listEmpresaBEC){	
								olistaRentabilidadtempBECPrev=new ArrayList<RentabilidadBEC>();
								String codigoCentral=listaempr.getCodigo();	
								olistaRentabilidadtempBECPrev=relacionesBancariasBO.generaModeloRentabilidad(id.toString(),codigoCentral);
								olistaRentabilidadtempBEC.addAll(olistaRentabilidadtempBECPrev);
						}
					}
				}else{
					olistaRentabilidadtempBEC=relacionesBancariasBO.ObtenerRentabilidad(id.toString());
				}
			}catch (Exception e) {
				olistaRentabilidadtempBEC=relacionesBancariasBO.ObtenerRentabilidad(id.toString());
			}
			
			if (olistaRentabilidadtempBEC!=null && olistaRentabilidadtempBEC.size()>0){
				olisRentabilidadBEC.addAll(olistaRentabilidadtempBEC);
				if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					TmanagerLog oLog=new TmanagerLog();
					List<RentabilidadBEC>  olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
					olistRentabilidadBECTotal=getListaRentabilidadByGrupo(idGrupo, id,oLog);
					olistRentabilidadBECGrupo=relacionesBancariasBO.crearRentabilidadGrupo(olistRentabilidadBECTotal,idGrupo,id);
					if (olistRentabilidadBECGrupo!=null  && olistRentabilidadBECGrupo.size()>0){
						olisRentabilidadBEC.addAll(olistRentabilidadBECGrupo);
					}
				}				
			}
			relacionesBancariasBO.setListRentabilidadBEC(olisRentabilidadBEC);
			
			relacionesBancariasBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			relacionesBancariasBO.updateRelacionesBancarias();
			cargarComex(Long.valueOf(id),idEmpresa);
			addActionMessage("Datos Actualizados");
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "relacionesbancarias";
	}
	
	
	public String refreshPoolBancario() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		String id = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		String codTipoEmpresaGrupo=sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		String idGrupo=sessionparam.get(Constantes.COD_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.COD_GRUPO_SESSION).toString();
		String rucEmpresaPrincipal=sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();		
				
		Tabla tipoEmpresa = new Tabla();
		tipoEmpresa.setId(Long.valueOf(codTipoEmpresaGrupo));	
		Programa oprograma=new Programa();
		
		oprograma.setId(Long.valueOf(id));
		oprograma.setTipoEmpresa(tipoEmpresa);
		oprograma.setIdGrupo(idGrupo);
		oprograma.setRuc(rucEmpresaPrincipal);	
		oprograma.setIdmodeloRentabilidad(getModeloRentabilidad());
		oprograma.setTipoMilesRB(Long.parseLong(tipoMiles));
		
		asignarListado();
		
		
		
		if (!rucEmpresaPrincipal.equals("") && validarExisteRUC(listaEmpresaSelect)){
			try {			
				if (listaBancoSelect != null) {
					if (listaBancoSelect.size() > 0) {
						String strbanco = listaBancoSelect.get(0).getNombreBanco();
					}
				}
		
	
						
				String ArraycodTipoDeudas="";
				if (checkTipoDeudaTotal){
					ArraycodTipoDeudas +="1" + ",";				
				}
				if (checkTipoDeudaDirecta){
					ArraycodTipoDeudas +="2" + ",";				
				}
				if (checkTipoDeudaIndirecta){
					ArraycodTipoDeudas +="3" + ",";				
				}
				if (ArraycodTipoDeudas.length()>0){
				int pos=ArraycodTipoDeudas.length()-1;
				ArraycodTipoDeudas=ArraycodTipoDeudas.substring(0, pos);
				}
				
				relacionesBancariasBO.setPrograma(oprograma);
				
				relacionesBancariasBO.setCodTipoDeudas(ArraycodTipoDeudas);
				relacionesBancariasBO.setListaEmpresaSelect(listaEmpresaSelect);
				relacionesBancariasBO.setListaBancoSelect(listaBancoSelect);			
				relacionesBancariasBO.savePoolBancario();
	
				addActionMessage("Datos Actualizados");
				
			
			} catch (BOException e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
			}
		}else{
			addActionError("La Empresa Principal no tiene RUC. Por favor actualizar el RUC en Datos Básicos.");
		}
		return "relacionesbancarias";
	}
	
	private boolean validarExisteRUC(List<Tempresa>  olistaEmpresaSelect){
		boolean res=true;
		try {
			if (olistaEmpresaSelect!=null && olistaEmpresaSelect.size()>0){
				for (Tempresa otempresa:olistaEmpresaSelect){
					if (otempresa.getCodEmpresa()==null || (otempresa.getCodEmpresa()!=null && otempresa.getCodEmpresa().equals(""))){
						res=false;
						break;
					}				
				}
			}	
			
		} catch (Exception e) {
			res=true;
		}	
		return res;		
	}
	
	public String RefreshEfectividades() {
		
		try {
			cargaTipoMiles();
			this.asignarListado();
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			String rucEmpresa=(String)getObjectSession(Constantes.COD_RUC_EMPRESA_SESSION);
			String codServ=Constantes.ID_CODIGO_SERVICIO;
			List<EfetividadCartera> listoefetividadCarteratmpRefresh=relacionesBancariasBO.findEfectividadCartera(rucEmpresa,codServ);
			EfetividadCartera oefetividadCarteraRefresh=new EfetividadCartera();
			oefetividadCarteraRefresh=listoefetividadCarteratmpRefresh.get(0);		
			String newEfecProm6sol=oefetividadCarteraRefresh.getEfectividadProm6sol();
			String newEfecProm6dol=oefetividadCarteraRefresh.getEfectividadProm6dol();
			String newProtestoProm6sol=oefetividadCarteraRefresh.getProtestoProm6sol();
			String newProtestoProm6dol=oefetividadCarteraRefresh.getProtestoProm6dol();
			
			programaBO.refreshEfectividad(newEfecProm6sol,newEfecProm6dol,newProtestoProm6sol,newProtestoProm6dol,idprograma);
			programa.setEfectividadProm6sol(newEfecProm6sol);
			programa.setEfectividadProm6dol(newEfecProm6dol);
			programa.setProtestoProm6sol(newProtestoProm6sol);
			programa.setProtestoProm6dol(newProtestoProm6dol);
			addActionMessage("Datos Actualizados");
			
		} catch (Exception e) {
			 
		}
		return "relacionesbancarias";
	}
	
	public String saveLineasRelacionesBancarias(){		
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			programa.setId(Long.valueOf(idprograma));
			programa.setTipoMilesRB(Long.parseLong(tipoMiles));
			relacionesBancariasBO.setPrograma(programa);
			relacionesBancariasBO.setListLineasRelacionesBancarias(listLineasRelacionesBancarias);		
			relacionesBancariasBO.saveLineasRelacionesBancarias();
			addActionMessage("Lineas registrados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		asignarListado();
		return "relacionesbancarias";
	}

	public void CargarEmpresasRelacionesBancarias() {

		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		List<Empresa> listEmpresatmp= new ArrayList<Empresa>();
		List<Tempresa> listaEmpresaobj = new ArrayList<Tempresa>();
		
		listaEmpresa = new ArrayList<Tempresa>();
		listaEmpresaSelect = new ArrayList<Tempresa>();
		try {
			String tipoEmpresa = sessionparam.get(
					Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
			// buscar la actividad de la empresa
			 if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			 Tempresa oTempresa=new Tempresa();
			 String ruc = sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
			 String strEmpresaPrincipal=sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
			 oTempresa.setCodEmpresa(ruc);
			 oTempresa.setNomEmpresa(strEmpresaPrincipal);
			 listaEmpresaobj.add(oTempresa );
			 
			 }else{
			// buscar las empresas del grupo
			// Simulando.
				 
				 listEmpresatmp =programaBO.findEmpresaByIdprograma(new Long(idprograma));	
				if (listEmpresatmp!=null && listEmpresatmp.size()>0) {
					for (Empresa listaempr:listEmpresatmp){
						Tempresa oTempresa1 = new Tempresa();
						oTempresa1.setCodEmpresa(listaempr.getRuc());
						oTempresa1.setNomEmpresa(listaempr.getNombre());
						listaEmpresaobj.add(oTempresa1);
					}
				}
			 }
			 
				List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
				List<Tempresa> listaTarget = new ArrayList<Tempresa>();
				List<Tempresa> listaSource = new ArrayList<Tempresa>(); 
				
				listaopcionPool=relacionesBancariasBO.findOpcionPool(idprograma, Constantes.ID_TIPO_OPPOOL_EMPRESA);
				if (listaopcionPool!=null && listaopcionPool.size()>0){
					for (OpcionPool opcionPool: listaopcionPool){
						Tempresa otempresa=new Tempresa();
						otempresa.setCodEmpresa(opcionPool.getCodOpcionPool());
						otempresa.setNomEmpresa(opcionPool.getDescOpcionPool());
						listaTarget.add(otempresa);
					}
				}			 
				List<Tempresa> lista = listaEmpresaobj;
				
				boolean flagEncontrado = false;
				for(Tempresa empresa :lista){
					for(Tempresa asignado : listaTarget){
						if(empresa.getCodEmpresa().trim().equals(asignado.getCodEmpresa().trim())){
							flagEncontrado = true;
							break;
						}
					}
					if(!flagEncontrado){										
						listaSource.add(empresa);
					}
					flagEncontrado=false;
				}
				
				listaEmpresa=listaSource;
				listaEmpresaSelect=listaTarget;			 

			sessionparam.put("LISTAEMPRESA", listaEmpresa);
			sessionparam.put("LISTAEMPRESASELECT", listaEmpresaSelect);

		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}

	}

	public void cargarTipoDeuda() {
		
		try {
			if (getObjectSession(Constantes.ID_PROGRAMA_SESSION) != null) {
				String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
				
				listaopcionPool=relacionesBancariasBO.findOpcionPool(Idprograma, Constantes.ID_TIPO_OPPOOL_TIPODEUDA);
				setCheckTipoDeudaTotal(false);
				setCheckTipoDeudaDirecta(false);
				setCheckTipoDeudaIndirecta(false);
				
				if (listaopcionPool!=null && listaopcionPool.size()>0){
					for (OpcionPool opcionPool: listaopcionPool){						
						if(opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_TOTAL)){							
							setCheckTipoDeudaTotal(true);	
						}else if(opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_DIRECTA)){
							setCheckTipoDeudaDirecta(true);
						}else if(opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)){
							setCheckTipoDeudaIndirecta(true);
						}
					}
				}				
			}	
		
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}

	}

	public void cargarModeloRentabilidad() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		listaModeloRentabilidad = new ArrayList<String>();

		listaModeloRentabilidad.add("Banca Empresas");
		listaModeloRentabilidad.add("Clientes Globales");
		sessionparam.put("MODELORENTABILIDAD", listaModeloRentabilidad);

	}

	public void cargarBanco() {
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		List<Tbanco> listaBancotemp = new ArrayList<Tbanco>();
		listaBanco = new ArrayList<Tbanco>();
		listaBancoSelect = new ArrayList<Tbanco>();		
		try {
			if (getObjectSession(Constantes.ID_PROGRAMA_SESSION) != null) {
				String Idprograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
				String Idruc = getObjectSession(Constantes.COD_RUC_EMPRESA_SESSION).toString(); 
				String tipoEmpresa=getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
				List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
				List<Tbanco> listaTarget = new ArrayList<Tbanco>();
				List<Tbanco> listaSource = new ArrayList<Tbanco>(); 
				
				listaopcionPool=relacionesBancariasBO.findOpcionPool(Idprograma, Constantes.ID_TIPO_OPPOOL_BANCO);
				if (listaopcionPool!=null && listaopcionPool.size()>0){
					for (OpcionPool opcionPool: listaopcionPool){
						Tbanco obanco=new Tbanco();
						obanco.setCodBanco(opcionPool.getCodOpcionPool());
						obanco.setNombreBanco(opcionPool.getDescOpcionPool());
						listaTarget.add(obanco);
					}
				}
				
				listaBancotemp=relacionesBancariasBO.findBancosPoolBancarioSQL(Idprograma,Idruc,tipoEmpresa);
//				Tbanco oTbanco = new Tbanco();
//				// oTbanco.setId("01");
//				oTbanco.setCodBanco("006");
//				oTbanco.setNombreBanco("BBVA");
//				listaBancotemp.add(oTbanco);
//				
//				Tbanco oTbanco1 = new Tbanco();
//				// oTbanco1.setId("02");
//				oTbanco1.setCodBanco("001");
//				oTbanco1.setNombreBanco("BCP");
//				listaBancotemp.add(oTbanco1);
//		
//				Tbanco oTbanco2 = new Tbanco();
//				// oTbanco1.setId("02");
//				oTbanco2.setCodBanco("004");
//				oTbanco2.setNombreBanco("SCOTI");
//				listaBancotemp.add(oTbanco2);
//		
//				Tbanco oTbanco3 = new Tbanco();
//				// oTbanco1.setId("02");
//				oTbanco3.setCodBanco("126");
//				oTbanco3.setNombreBanco("HSBC");
//				listaBancotemp.add(oTbanco3);
				
				Tbanco oTbanco4 = new Tbanco();
				// oTbanco1.setId("02");
				oTbanco4.setCodBanco("999");
				oTbanco4.setNombreBanco("OTROS");
				listaBancotemp.add(oTbanco4);			
				
				List<Tbanco> lista = listaBancotemp;
				
				boolean flagEncontrado = false;
				for(Tbanco banco :lista){
					for(Tbanco asignado : listaTarget){
						if(banco.getCodBanco().equals(asignado.getCodBanco())){
							asignado.setNombreBanco(banco.getNombreBanco());
							flagEncontrado = true;
							break;
						}
					}
					if(!flagEncontrado){										
						listaSource.add(banco);
					}
					flagEncontrado=false;
				}				
				listaBanco=listaSource;
				listaBancoSelect=listaTarget;			
			}
	
			sessionparam.put("LISTABANCO", listaBanco);
			sessionparam.put("LISTABANCOSELECT", listaBancoSelect);
		
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}

	}

	public void loadPoolBancario() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		logger.info("right side list banco: " + getRightSidebanco());
		logger.info("tipo deuda: " + getTipoDeuda());
		logger.info("right side list empresa: " + getRightSideempresa());

		String codBanco = getRightSidebanco();
		String codTipoDeuda = getTipoDeuda();
		String codEmpresa = getRightSideempresa();
		List<Empresa> listEmpresat =new ArrayList<Empresa>();
		sessionparam.put("rightSidebanco", rightSidebanco);

		PrintWriter out = null;
		try {

			Long Idprograma = Long.valueOf(sessionparam.get(
					Constantes.ID_PROGRAMA_SESSION).toString());
			String strEmpresa = sessionparam.get(
					Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
			String codTipoEmpresaGrupo=sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			String oruc = sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
			
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();

			out.printf("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ProgramaFinanciero/css/table.css\"/>");
			out.printf("<form>");

			out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
			out.printf("<tr><td><b style=\"background-color: #5c9ccc; color: White;\">POOL BANCARIO</b></td></tr>");
			out.printf("<tr><td>Cuadros (Miles de Soles)</td></tr>");
			out.printf("</table>");

			String empresas = getRightSideempresa();
			String[] arrayEmpresas = empresas.split(",");

			String tipoDeuda = getTipoDeuda();
			String[] arrayTipoDeuda = tipoDeuda.split(",");
			
			//ini grupo economico  UTILIZO listPoolBancarioTotal para cualquier tipo de deuda
			if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){				
						out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
						out.printf("<tr><td><b>Deuda Grupo: " + strEmpresa + "</b></td></tr>");
						out.printf("</table>");
		
						for (int t = 0; t < arrayTipoDeuda.length; t++) {
							listPoolBancarioTotal = new ArrayList<List>();
							String CabeceraTipoDeuda="";
							if (arrayTipoDeuda[t].toString().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
								CabeceraTipoDeuda="DEUDA TOTAL";
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_TOTAL,
												codBanco, "O",Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
		
							}else if (arrayTipoDeuda[t].toString().equals(
									Constantes.ID_TIPO_DEUDA_DIRECTA )) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA;
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_DIRECTA,
												codBanco, "O",Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
		
							}else if (arrayTipoDeuda[t].toString().equals(
									Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
								listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, oruc, Constantes.ID_TIPO_DEUDA_INDIRECTA,
												codBanco, "O",Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
		
							} 
		
							// DEUDA
							if (listPoolBancarioTotal != null
									&& listPoolBancarioTotal.size() > 0) {
								out.printf("<br/>");
								out.printf(CabeceraTipoDeuda + ": </b>");
								out.printf("<table class=\"gridtable\">");
								StringBuilder sb=new StringBuilder ();
								StringBuilder sbcab=new StringBuilder ();
								
								sb.append("<tr>");
								Map<String, String> hm = new HashMap<String, String>();
								int conts=0;
								int conts1=0;
								int conts2=0;
								
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
										if(e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA) || e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA)){
											sb.append("<th style=\"background-color: yellow;color: #666666\">" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
											
										}else{
											sb.append("<th>" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
										}										
									}
								}	
								conts1=conts-conts2-1;
								sb.append("</tr>");
								
								sbcab.append("<tr>");
									sbcab.append("<th></th>");
									if (conts1>0){									
										sbcab.append("<th align=\"center\" colspan=\""+conts1+"\">IMPORTE</th>");
									}
									if (conts2>0){
										sbcab.append("<th align=\"center\" colspan=\""+conts2+"\">CUOTA</th>");
									}
								sbcab.append("</tr>");
								
								out.printf(sbcab.toString());
								out.printf(sb.toString());								
								
		
								for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
									out.printf("<tr>");
									Map<String, String> hmr = new HashMap<String, String>();
									for (Object x : listPoolBancarioTotal.get(i)) {
										hmr = (HashMap<String, String>) x;
		
										Iterator itr = hmr.entrySet().iterator();
										while (itr.hasNext()) {
											Map.Entry er = (Map.Entry) itr.next();
											// logger.info(er.getKey() + " " +
											// er.getValue());
											out.printf("<td align=\"right\">" + 
															FormatUtil.formatMiles(er.getValue()== null?"":er.getValue().toString())
															+ "</td>");
										}
									}
		
									out.printf("</tr>");
								}
		
								out.printf("</table>");
							}else{
								out.printf("<br/>");
								out.printf(CabeceraTipoDeuda + ": </b>");
								out.printf("<table class=\"gridtable\">");
								out.printf("<tr>");
								out.printf("<td>El grupo no tiene deuda de este tipo</td>");
								out.printf("</tr>");
								out.printf("</table>");								
							}
							
		
						}
				
			}	
			// fin grupo economicp
			
			
			// ini empresa
			for (int j = 0; j < arrayEmpresas.length; j++) {

				logger.info(arrayEmpresas[j]);

				if (arrayEmpresas[j].toString().equals("999999")) {
					continue;
				}
				
				String strEmpresaind="";
				listEmpresat =programaBO.findEmpresaByIdprogramaRuc(Idprograma.toString(), arrayEmpresas[j].toString());	
				if (listEmpresat!=null && listEmpresat.size()>0){
				Empresa oempresa=new Empresa();
				oempresa=listEmpresat.get(0);
				strEmpresaind=oempresa.getNombre();
				}
				out.printf("<br/>");
				out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
				out.printf("<tr><td><b>Deuda: " + strEmpresaind + "</b></td></tr>");
				out.printf("</table>");

				for (int k = 0; k < arrayTipoDeuda.length; k++) {
					listPoolBancarioTotal = new ArrayList<List>();
					String CabeceraTipoDeuda="";
					if (arrayTipoDeuda[k].toString().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
						CabeceraTipoDeuda="DEUDA TOTAL";
						listPoolBancarioTotal = relacionesBancariasBO
								.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, arrayEmpresas[j], Constantes.ID_TIPO_DEUDA_TOTAL,
										codBanco, "O",Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());

					} else if (arrayTipoDeuda[k].toString().equals(
							Constantes.ID_TIPO_DEUDA_DIRECTA )) {
						CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA;
						listPoolBancarioTotal = relacionesBancariasBO
								.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, arrayEmpresas[j], Constantes.ID_TIPO_DEUDA_DIRECTA,
										codBanco, "O",Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());

					} else if (arrayTipoDeuda[k].toString().equals(
							Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
						CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
						listPoolBancarioTotal = relacionesBancariasBO
								.findPoolBancario(Constantes.TIPO_DOCUMENTO_RUC, arrayEmpresas[j], Constantes.ID_TIPO_DEUDA_INDIRECTA,
										codBanco, "O",Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());

					} 

					// DEUDA
					if (listPoolBancarioTotal != null
							&& listPoolBancarioTotal.size() > 0) {
						out.printf("<br/>");
						out.printf(CabeceraTipoDeuda + ": </b>");
						out.printf("<table class=\"gridtable\">");
						
						StringBuilder sb=new StringBuilder ();
						StringBuilder sbcab=new StringBuilder ();

						sb.append("<tr>");
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
								
								if(e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA) || e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA)){
									sb.append("<th style=\"background-color: yellow;color: #666666\">" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
									
								}else{
									sb.append("<th>" + e.getKey().toString().replace("CTA", "&#37;") + "</th>");
								}								
								
							}
						}
						conts1=conts-conts2-1;
						sb.append("</tr>");
						
						sbcab.append("<tr>");
						sbcab.append("<th></th>");
						if (conts1>0){									
							sbcab.append("<th align=\"center\" colspan=\""+conts1+"\">IMPORTE</th>");
						}
						if (conts2>0){
							sbcab.append("<th align=\"center\" colspan=\""+conts2+"\">CUOTA</th>");
						}
					sbcab.append("</tr>");
					
					out.printf(sbcab.toString());
					out.printf(sb.toString());	

						for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
							out.printf("<tr>");
							Map<String, String> hmr = new HashMap<String, String>();
							for (Object x : listPoolBancarioTotal.get(i)) {
								hmr = (HashMap<String, String>) x;

								Iterator itr = hmr.entrySet().iterator();
								while (itr.hasNext()) {
									Map.Entry er = (Map.Entry) itr.next();
									// logger.info(er.getKey() + " " +
									// er.getValue());
									out.printf("<td align=\"right\">" + 
									FormatUtil.formatMiles(er.getValue()== null?"":er.getValue().toString())
													+ "</td>");
								}
							}
							out.printf("</tr>");
						}
						out.printf("</table>");
					}else{
						out.printf("<br/>");	
						out.printf(CabeceraTipoDeuda + ": </b>");
						out.printf("<table class=\"gridtable\">");
						out.printf("<tr>");
						out.printf("<td>La empresa no tiene deuda de este tipo</td>");
						out.printf("</tr>");
						out.printf("</table>");
						
					}

				}
			}
			//fin empresa

			out.printf("<form>");

		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		} finally {
			out.close();
		}
	}

	public void RefreshModeloRentabilidad(){
		
		logger.info("right side list modelo rentabilida: "
				+ this.getModeloRentabilidad());

		PrintWriter out = null;
		List<RentabilidadBEC>  olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
		List<RentabilidadBEC>  olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
		try {
					
			String strEmpresa = getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
			setObjectSession(Constantes.swRefresh,"1");
			
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();

			out.printf("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ProgramaFinanciero/css/table.css\"/>");
			out.printf("<form>");

			out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
			out.printf("<tr><td><b style=\"background-color: #5c9ccc; color: White;\">MODELO DE RENTABILIDAD</b></td></tr>");
			out.printf("</table>");

			String ModeloRentab = getModeloRentabilidad();	
			
			StringBuilder sbgrupo=new StringBuilder();
			StringBuilder sbempresa=new StringBuilder();
			
			String strcodigoCentral="";
			List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();
			String idprograma =getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(idprograma));
			String codGrupo=oprograma.getIdGrupo()==null?"":oprograma.getIdGrupo().toString();
			String nombGrupo=oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
			String codTipoEmpresaGrupo=oprograma.getTipoEmpresa().getId().toString();
			String empresaPrincipal=oprograma.getIdEmpresa().toString();
			
			
			if (!codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				
				out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
				out.printf("<tr><td>" + strEmpresa + "</td></tr>");
				out.printf("</table>");					
					listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
					listRentabilidadBEC=relacionesBancariasBO.ObtenerRentabilidadByEmpresa(idprograma,empresaPrincipal);
					
					if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {
			
						if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {							
							sbempresa.append(crearRentabilidad(listRentabilidadBEC,true));
							relacionesBancariasBO.saveRentabilidaBECrefresh(listRentabilidadBEC,idprograma,empresaPrincipal);	
							
						}
					}//	
				out.printf(sbempresa.toString());		
			}else{			
			
				out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
				out.printf("<tr><td>" + nombGrupo + "</td></tr>");
				out.printf("</table>");	
				
				listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idprograma));	
				if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {
					for (Empresa listaempr:listEmpresaBEC){
	
					strEmpresa=listaempr.getNombre();
					strcodigoCentral=listaempr.getCodigo();
				
						sbempresa.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
						sbempresa.append("<tr><td>" + strEmpresa + "</td></tr>");
						sbempresa.append("</table>");					
					
						listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
						listRentabilidadBEC=relacionesBancariasBO.ObtenerRentabilidadByEmpresa(idprograma,strcodigoCentral);
						
						if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {		
							sbempresa.append(crearRentabilidad(listRentabilidadBEC,false));
							if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {
								//olistRentabilidadBECTotal.addAll(listRentabilidadBEC);		
								relacionesBancariasBO.saveRentabilidaBECrefresh(listRentabilidadBEC,idprograma,strcodigoCentral);	
								
							} //listado renta
	
						}//				
					
					}//for empresa	
					TmanagerLog oLog=new TmanagerLog();
					olistRentabilidadBECTotal=getListaRentabilidadByGrupo(codGrupo, idprograma,oLog);
					olistRentabilidadBECGrupo=relacionesBancariasBO.crearRentabilidadGrupo(olistRentabilidadBECTotal,codGrupo,idprograma);
					if (oLog.getCodigo()!=null && oLog.getCodigo().equals(Constantes.COD_ERROR_WS_GETEMPRESA)){
						String msnerror=oLog.getEmessage()==null?"":oLog.getEmessage();
						sbgrupo.append("* " + msnerror);
						sbgrupo.append(" ."+Constantes.MSN_WS_GETEMPRESA);
						sbgrupo.append("<br/>");
					}
					sbgrupo.append(crearRentabilidad(olistRentabilidadBECGrupo,true));
					if (olistRentabilidadBECGrupo!=null && olistRentabilidadBECGrupo.size()>0){
						relacionesBancariasBO.saveRentabilidaBECrefresh(olistRentabilidadBECGrupo,idprograma,codGrupo);							
					}
					out.printf(sbgrupo.toString());				
					out.printf(sbempresa.toString());					
				}			
			
		}///jj
				
		out.printf("<form>");
      	
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
			out.close();
		}
		
	}
	
	
	public String refrecarClasificacion(){
		try{
			cargaTipoMiles();
			this.asignarListado();
			Long Idprograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			String idTipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			if(Idprograma != null && !Idprograma.equals("")) {			
				
				Programa oprograma=new Programa();
				oprograma=programaBO.findById(Idprograma);				
				
				String urlRig4 = getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
				HashMap<String,String> datos=ConsultaWS.consularDatosReporteCredito(oprograma.getIdEmpresa(),getConfiguracionWS(),urlRig4);			
				String clasificacion=datos.get("clasificacionBanco");
				String codigoError=datos.get("codigoError");
				clasificacion=relacionesBancariasBO.obtenerEquivClasificacionBanco(clasificacion);
				desCalifBanco=clasificacion;
				oprograma.setCalificacionBanco(clasificacion==null?"":clasificacion);					
				String cuotaFinanciera="";
				if(idTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraEmpresa(oprograma)+"";					
				}else{
					List<Empresa>  listaEmpersas = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
					cuotaFinanciera=relacionesBancariasBO.calcularCuotaFinancieraGrupo(oprograma, listaEmpersas)+"";
				}
				oprograma.setCuotaFinanciera(cuotaFinanciera);				
				programaBO.actualizarClasificacion(oprograma);
				
				programa.setCalificacionBanco(clasificacion);
				programa.setPeriodoArchivo(relacionesBancariasBO.obtenerFechaCuotaFinancieraEmpresa(oprograma));
				programa.setCuotaFinanciera(cuotaFinanciera);
				 
				if (codigoError!=null && !codigoError.equals(Constantes.COD_CORRECTO_HOST)){				
					addActionError(codigoError+": "+ datos.get("descripcionError"));
				}
			
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}		
		return "relacionesbancarias"; 
	}
	
	public void loadModeloRentabilidad() {

		logger.info("right side list modelo rentabilida: "
				+ this.getModeloRentabilidad());

		PrintWriter out = null;
		List<RentabilidadBEC>  olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
		List<RentabilidadBEC>  olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
		try {

			
			String strEmpresa = getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
			
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();

			out.printf("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ProgramaFinanciero/css/table.css\"/>");
			out.printf("<form>");

			out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
			out.printf("<tr><td><b style=\"background-color: #5c9ccc; color: White;\">MODELO DE RENTABILIDAD</b></td></tr>");
			out.printf("</table>");

			String ModeloRentab = getModeloRentabilidad();			

			StringBuilder sbgrupo=new StringBuilder();
			StringBuilder sbempresa=new StringBuilder();
			
			String strcodigoCentral="";
			List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();
			List<Empresa> listEmpresaBECGrupo=new ArrayList<Empresa>();
			String idprograma =getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(idprograma));
			String codGrupo=oprograma.getIdGrupo()==null?"":oprograma.getIdGrupo().toString();
			String nombGrupo=oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
			String codTipoEmpresaGrupo=oprograma.getTipoEmpresa().getId().toString();
			String empresaPrincipal=oprograma.getIdEmpresa().toString();

			
			
			if (!codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
				
				out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
				out.printf("<tr><td>" + strEmpresa + "</td></tr>");
				out.printf("</table>");			
				
				
					listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
					listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,empresaPrincipal);
					
					if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {
			
						if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {							
							sbempresa.append(crearRentabilidad(listRentabilidadBEC,true));
						}
					}//	
				out.printf(sbempresa.toString());		
			}else{
			
				out.printf("<table class=\"ln_formatos\" cellspacing=\"0\">");
				out.printf("<tr><td>" + nombGrupo + "</td></tr>");
				out.printf("</table>");	
				
				listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idprograma));	
				
				if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {
					
					for (Empresa listaempr:listEmpresaBEC){
							strEmpresa=listaempr.getNombre();
							strcodigoCentral=listaempr.getCodigo();	
							
							sbempresa.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
							sbempresa.append("<tr><td>" + strEmpresa + "</td></tr>");
							sbempresa.append("</table>");			
						
							listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
							listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,strcodigoCentral);
							
							if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {									
								sbempresa.append(crearRentabilidad(listRentabilidadBEC,false));
//								if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {
//									olistRentabilidadBECTotal.addAll(listRentabilidadBEC);								
//								} //listado renta
								
							}// tipo rentabilidad
										
					}// for empresa		
					TmanagerLog oLog=new TmanagerLog();
					if (ModeloRentab.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {						
						olistRentabilidadBECTotal=getListaRentabilidadByGrupo(codGrupo, idprograma,oLog);
					}
					olistRentabilidadBECGrupo=relacionesBancariasBO.crearRentabilidadGrupo(olistRentabilidadBECTotal,codGrupo,idprograma);
					if (oLog.getCodigo()!=null && oLog.getCodigo().equals(Constantes.COD_ERROR_WS_GETEMPRESA)){
						String msnerror=oLog.getEmessage()==null?"":oLog.getEmessage();
						sbgrupo.append("* " + msnerror);
						sbgrupo.append(" ."+Constantes.MSN_WS_GETEMPRESA);						
						sbgrupo.append("<br/>");
					}
					sbgrupo.append(crearRentabilidad(olistRentabilidadBECGrupo,true));
					out.printf(sbgrupo.toString());				
					out.printf(sbempresa.toString());
					
				}//lista empresa
				
			}
			out.printf("<form>");
      	
		} catch (Exception e) {

		} finally {
			out.close();
		}

	}
	
	private List<RentabilidadBEC> getListaRentabilidadByGrupo(String codGrupo,String idprograma,TmanagerLog oLog){
		
		List<Empresa> listEmpresaBECGrupo=new ArrayList<Empresa>();
		List<RentabilidadBEC> olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
		String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
		String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
		
		try {
			logger.error("pathWebService: "+ pathWebService);
			logger.error("pathWebServicePEC6: "+ pathWebServicePEC6);
			
			logger.error("ini getListaRentabilidadByGrupo");
			programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			programaBO.setPathWebService(pathWebService);
			programaBO.setPathWebServicePEC6(pathWebServicePEC6);
			listEmpresaBECGrupo =programaBO.getEmpresaGrupoWS(codGrupo,new Long(idprograma),oLog);
			if (listEmpresaBECGrupo!=null && listEmpresaBECGrupo.size()>0) {
				List<RentabilidadBEC> listRentabilidadBECG=new ArrayList<RentabilidadBEC>();
				for (Empresa empresaGrup:listEmpresaBECGrupo){							
					 String codigoCentral=empresaGrup.getCodigo();
					  logger.error("codioempresa:"+codigoCentral);
					 listRentabilidadBECG=new ArrayList<RentabilidadBEC>();	
					   //obtiene informacion de archivo host
						 listRentabilidadBECG=relacionesBancariasBO.ObtenerRentabilidadByEmpresa(idprograma,codigoCentral);
					
					 if (listRentabilidadBECG != null && listRentabilidadBECG.size() > 0) {						 
							olistRentabilidadBECTotal.addAll(listRentabilidadBECG);								
					} 
				}
			}
			
		} catch (Exception e) {
			olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
			logger.error("Error:"+e.getMessage());
		}
		logger.error("fin getListaRentabilidadByGrupo");
		return olistRentabilidadBECTotal;
		

	} 
	
	
	
	private String crearRentabilidad(List<RentabilidadBEC> listRentabilidadBEC,boolean bmostrarFecha){
		StringBuilder sbcontenidoEmpresa=new StringBuilder();
		
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
						sbcomercial.append("<td width=\"80\" align=\"right\">" + (orentabilidadbec.getMontoAnual()==null?"":orentabilidadbec.getMontoAnual()) + "</td>");
						sbcomercial.append("<td width=\"80\" align=\"right\">" + (orentabilidadbec.getMonto()==null?"":orentabilidadbec.getMonto()) + "</td>");
						sbcomercial.append("</tr>");
						
						
					}else{
						sbrentabilidad.append("<tr>");					
						sbrentabilidad.append("<td>" + orentabilidadbec.getDescripcionMonto() + "</td>");
						sbrentabilidad.append("<td width=\"80\" align=\"right\">" + (orentabilidadbec.getMontoAnual()==null?"":orentabilidadbec.getMontoAnual()) + "</td>");
						sbrentabilidad.append("<td width=\"80\" align=\"right\">" + (orentabilidadbec.getMonto()==null?"":orentabilidadbec.getMonto()) + "</td>");							
						sbrentabilidad.append("</tr>");		
		
					}	
				}
			}		
			if (bmostrarFecha){
				sbcontenidoEmpresa.append("Periodo Anual:"+sbperiodoAnual.toString()+"<br>");
				sbcontenidoEmpresa.append("Periodo Mensual:"+sbperiodoMensual.toString()+"<br>");
				sbcontenidoEmpresa.append(Constantes.TIPO_BANCAEMPRESA + ": </b>");
			}
			
			sbcontenidoEmpresa.append("<table>");
			sbcontenidoEmpresa.append("<tr>");	
			sbcontenidoEmpresa.append("<td>");
				
				sbcontenidoEmpresa.append("<table class=\"gridtable\">");
				sbcontenidoEmpresa.append("<tr>");
						sbcontenidoEmpresa.append("<th>Comercial</th>");sbcontenidoEmpresa.append("<th>Anual</th>");sbcontenidoEmpresa.append("<th>Mensual</th>");
				sbcontenidoEmpresa.append("</tr>");
						sbcontenidoEmpresa.append(sbcomercial.toString());
				sbcontenidoEmpresa.append("</table>");
			sbcontenidoEmpresa.append("</td>");
				sbcontenidoEmpresa.append("<td colspan=\"1\"></td>");
				sbcontenidoEmpresa.append("<td>");
					sbcontenidoEmpresa.append("<table class=\"gridtable\">");
						sbcontenidoEmpresa.append("<tr>");
						sbcontenidoEmpresa.append("<th>Rentabilidad</th>");sbcontenidoEmpresa.append("<th>Anual</th>");sbcontenidoEmpresa.append("<th>Mensual</th>");
						sbcontenidoEmpresa.append("</tr>");
						sbcontenidoEmpresa.append(sbrentabilidad.toString());
						sbcontenidoEmpresa.append("<tr>");
						sbcontenidoEmpresa.append("<td>&nbsp;</td>");
						sbcontenidoEmpresa.append("<td>&nbsp;</td>");
						sbcontenidoEmpresa.append("<td>&nbsp;</td>");
						sbcontenidoEmpresa.append("</tr>");									
					sbcontenidoEmpresa.append("</table>");
					
				sbcontenidoEmpresa.append("</td>");							
			sbcontenidoEmpresa.append("</tr>");
			sbcontenidoEmpresa.append("</table>");
		}else{
			sbcontenidoEmpresa.append("<table class=\"ln_formatos\" cellspacing=\"0\">");
			sbcontenidoEmpresa.append("<tr><td>" + Constantes.MENSAJE_NO_EXISTE_INFORMACION + "</td></tr>");
			sbcontenidoEmpresa.append("</table>");
		}
		return sbcontenidoEmpresa.toString();
	}
	


	public String actualizarComex(){	
		List<Comex> olistaComex=new ArrayList<Comex>();
		try {
			cargaTipoMiles();
			this.asignarListado();
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(idprograma));
			
			String codCentral=oprograma.getIdEmpresa();					
			relacionesBancariasBO.setPrograma(oprograma);
			
			olistaComex=relacionesBancariasBO.ObtenerComexOfFileHost(idprograma, codCentral);
			relacionesBancariasBO.setListaComex(olistaComex);		
			relacionesBancariasBO.saveComex();
			cargarComex(Long.valueOf(idprograma), codCentral);
			addActionMessage("Comex Actualizado Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		asignarListado();
		return "relacionesbancarias";
	}


	public List<LineasRelacionBancarias> getListLineasRelacionesBancarias() {
		return listLineasRelacionesBancarias;
	}

	public void setListLineasRelacionesBancarias(
			List<LineasRelacionBancarias> listLineasRelacionesBancarias) {
		this.listLineasRelacionesBancarias = listLineasRelacionesBancarias;
	}

	public RelacionesBancariasBO getRelacionesBancariasBO() {
		return relacionesBancariasBO;
	}

	public void setRelacionesBancariasBO(
			RelacionesBancariasBO relacionesBancariasBO) {
		this.relacionesBancariasBO = relacionesBancariasBO;
	}

	public LineasRelacionBancarias getLineasRelacionBancarias() {
		return lineasRelacionBancarias;
	}

	public void setLineasRelacionBancarias(
			LineasRelacionBancarias lineasRelacionBancarias) {
		this.lineasRelacionBancarias = lineasRelacionBancarias;
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

	public String getComenLineas() {
		return comenLineas;
	}

	public void setComenLineas(String comenLineas) {
		this.comenLineas = comenLineas;
	}

	public List<Tempresa> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<Tempresa> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public List<Tempresa> getListaEmpresaSelect() {
		return listaEmpresaSelect;
	}

	public void setListaEmpresaSelect(List<Tempresa> listaEmpresaSelect) {
		this.listaEmpresaSelect = listaEmpresaSelect;
	}

	public List<Tbanco> getListaBanco() {
		return listaBanco;
	}

	public void setListaBanco(List<Tbanco> listaBanco) {
		this.listaBanco = listaBanco;
	}

	public List<Tbanco> getListaBancoSelect() {
		return listaBancoSelect;
	}

	public void setListaBancoSelect(List<Tbanco> listaBancoSelect) {
		this.listaBancoSelect = listaBancoSelect;
	}

	public List<List> getListPoolBancarioTotal() {
		return listPoolBancarioTotal;
	}

	public void setListPoolBancarioTotal(List<List> listPoolBancarioTotal) {
		this.listPoolBancarioTotal = listPoolBancarioTotal;
	}

	public String getComenClientGlob() {
		return comenClientGlob;
	}

	public void setComenClientGlob(String comenClientGlob) {
		this.comenClientGlob = comenClientGlob;
	}

	// ///////////////

	private List<String> selectedItemsIzquierdabanco;
	private List<String> selectedItemsDerechabanco;

	private List<String> selectedItemsIzquierdaEmpresa;
	private List<String> selectedItemsDerechaEmpresa;

	public List<String> getSelectedItemsIzquierdaEmpresa() {
		return selectedItemsIzquierdaEmpresa;
	}

	public void setSelectedItemsIzquierdaEmpresa(
			List<String> selectedItemsIzquierdaEmpresa) {
		this.selectedItemsIzquierdaEmpresa = selectedItemsIzquierdaEmpresa;
	}

	public List<String> getSelectedItemsDerechaEmpresa() {
		return selectedItemsDerechaEmpresa;
	}

	public void setSelectedItemsDerechaEmpresa(
			List<String> selectedItemsDerechaEmpresa) {
		this.selectedItemsDerechaEmpresa = selectedItemsDerechaEmpresa;
	}

	public List<String> getSelectedItemsIzquierdabanco() {
		return selectedItemsIzquierdabanco;
	}

	public void setSelectedItemsIzquierdabanco(
			List<String> selectedItemsIzquierdabanco) {
		this.selectedItemsIzquierdabanco = selectedItemsIzquierdabanco;
	}

	public List<String> getSelectedItemsDerechabanco() {
		return selectedItemsDerechabanco;
	}

	public void setSelectedItemsDerechabanco(
			List<String> selectedItemsDerechabanco) {
		this.selectedItemsDerechabanco = selectedItemsDerechabanco;
	}

	// PARA EMPRESA
	// Agregar Elementos a la Derecha
	public String aDerechaEmpresa() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		Iterator iteratorSelected = getSelectedItemsIzquierdaEmpresa()
				.iterator();
		List<Tempresa> listatemp = listaEmpresa;
		while (iteratorSelected.hasNext()) {
			String item = iteratorSelected.next().toString();
			for (Tempresa ItemsIzquierda : listatemp) {
				if (ItemsIzquierda.getCodEmpresa() != null
						&& ItemsIzquierda.getCodEmpresa().equals(item)) {
					listaEmpresa.remove(ItemsIzquierda);
					// ELIMINAR SI EXISTE EN EL LADO DERECHO					
					listaEmpresaSelect.remove(ItemsIzquierda);
					listaEmpresaSelect.add(ItemsIzquierda);
					break;
				}

			}
		}
		setSelectedItemsIzquierdaEmpresa(null);
		sessionparam.put("LISTAEMPRESA", listaEmpresa);
		sessionparam.put("LISTAEMPRESASELECT", listaEmpresaSelect);
		return "relacionesbancarias";
	}

	// Agregar Todas los Elementos a la Derecha
	public String aDerechaTodosEmpresa() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		Object object;
		asignarListado();
		int size = getListaEmpresa().size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				object = getListaEmpresa().get(0);
				getListaEmpresa().remove(object);
				//ELIMINAR SI EXISTE EN EL LADO DERECHO
				getListaEmpresaSelect().remove((Tempresa) object);
				getListaEmpresaSelect().add((Tempresa) object);
			}
			setSelectedItemsIzquierdaEmpresa(null);
		}
		sessionparam.put("LISTAEMPRESA", listaEmpresa);
		sessionparam.put("LISTAEMPRESASELECT", listaEmpresaSelect);
		return "relacionesbancarias";
	}

	// Agregar Elementos a la Izquierda
	public String aIzquierdaEmpresa() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		Iterator iteratorSelected = getSelectedItemsDerechaEmpresa().iterator();
		List<Tempresa> listatemp = listaEmpresaSelect;
		while (iteratorSelected.hasNext()) {
			String item = iteratorSelected.next().toString();
			for (Tempresa ItemsDerecha : listatemp) {
				if (ItemsDerecha.getCodEmpresa() != null
						&& ItemsDerecha.getCodEmpresa().equals(item)) {
					listaEmpresaSelect.remove(ItemsDerecha);
					//ELIMINAR SI EXISTE EN EL LADO IZQUIERDO
					listaEmpresa.remove(ItemsDerecha);
					listaEmpresa.add(ItemsDerecha);
					break;
				}
			}
		}
		setSelectedItemsDerechaEmpresa(null);
		sessionparam.put("LISTAEMPRESA", listaEmpresa);
		sessionparam.put("LISTAEMPRESASELECT", listaEmpresaSelect);
		return "relacionesbancarias";
	}

	// Agregar Todas los Elementos a la Derecha
	public String aIzquierdaTodosEmpresa() {
		Object object;
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		int size = getListaEmpresaSelect().size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				object = getListaEmpresaSelect().get(0);
				getListaEmpresaSelect().remove(object);
				getListaEmpresa().remove((Tempresa) object);
				getListaEmpresa().add((Tempresa) object);
			}
			setSelectedItemsDerechaEmpresa(null);
		}
		sessionparam.put("LISTAEMPRESA", listaEmpresa);
		sessionparam.put("LISTAEMPRESASELECT", listaEmpresaSelect);
		return "relacionesbancarias";
	}

	// Para Banco
	// Agregar Elementos a la Derecha
	public String aDerechaBanco() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		Iterator iteratorSelected = getSelectedItemsIzquierdabanco().iterator();
		List<Tbanco> listatemp = listaBanco;
		while (iteratorSelected.hasNext()) {
			String item = iteratorSelected.next().toString();
			for (Tbanco ItemsIzquierda : listatemp) {
				if (ItemsIzquierda.getCodBanco() != null
						&& ItemsIzquierda.getCodBanco().equals(item)) {
					listaBanco.remove(ItemsIzquierda);
					listaBancoSelect.add(ItemsIzquierda);
					break;
				}

			}
		}
		setSelectedItemsIzquierdabanco(null);
		sessionparam.put("LISTABANCO", listaBanco);
		sessionparam.put("LISTABANCOSELECT", listaBancoSelect);
		return "relacionesbancarias";
	}

	// Agregar Todas los Elementos a la Derecha
	public String aDerechaTodosBanco() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		Object object;
		asignarListado();
		int size = getListaBanco().size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				object = getListaBanco().get(0);
				getListaBanco().remove(object);
				getListaBancoSelect().add((Tbanco) object);
			}
			setSelectedItemsIzquierdabanco(null);
		}
		sessionparam.put("LISTABANCO", listaBanco);
		sessionparam.put("LISTABANCOSELECT", listaBancoSelect);
		return "relacionesbancarias";
	}

	// Agregar Elementos a la Izquierda
	public String aIzquierdaBanco() {
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		Iterator iteratorSelected = getSelectedItemsDerechabanco().iterator();
		List<Tbanco> listatemp = listaBancoSelect;
		while (iteratorSelected.hasNext()) {
			String item = iteratorSelected.next().toString();
			for (Tbanco ItemsDerecha : listatemp) {
				if (ItemsDerecha.getCodBanco() != null
						&& ItemsDerecha.getCodBanco().equals(item)) {
					listaBancoSelect.remove(ItemsDerecha);
					listaBanco.add(ItemsDerecha);
					break;
				}
			}
		}
		setSelectedItemsDerechabanco(null);
		sessionparam.put("LISTABANCO", listaBanco);
		sessionparam.put("LISTABANCOSELECT", listaBancoSelect);
		return "relacionesbancarias";
	}

	// Agregar Todas los Elementos a la Derecha
	public String aIzquierdaTodosBanco() {
		Object object;
		Map<String, Object> sessionparam = ActionContext.getContext()
				.getSession();
		asignarListado();
		int size = getListaBancoSelect().size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				object = getListaBancoSelect().get(0);
				getListaBancoSelect().remove(object);
				getListaBanco().add((Tbanco) object);
			}
			setSelectedItemsDerechabanco(null);
		}
		sessionparam.put("LISTABANCO", listaBanco);
		sessionparam.put("LISTABANCOSELECT", listaBancoSelect);
		return "relacionesbancarias";
	}
	
	//INI MCG20141113
	private void cargarComex(Long idPrograma, String codCentral){
		List<Comex> listaComex=new ArrayList<Comex>();
		Map<String, String> mapListaDescripcionComex = new HashMap<String, String>();

		try {
			mapListaDescripcionComex = tablaBO.getMapTablasByCodigoPadre(Constantes.COD_DESC_COMEX1500);
			
			listaComex=relacionesBancariasBO.obtenerComex(String.valueOf(idPrograma), codCentral,false);
			if (listaComex!=null && listaComex.size()>0){
				String periodo=listaComex.get(0).getAnio()==null?"":listaComex.get(0).getAnio();
				setPeriodoComex(periodo);
			}
			listaComexImportacion=relacionesBancariasBO.obtenerComexByType(listaComex,"I");			
			listaComexExportacion=relacionesBancariasBO.obtenerComexByType(listaComex,"E");
			removeObjectSession("listaComexImportacionSession");
			removeObjectSession("listaComexExportacionSession");
			cargarSessionListadoComex();
			
			
			setRatioReprocidadImp(relacionesBancariasBO.ObtenerRatioReprocidadImp(listaComexImportacion,mapListaDescripcionComex));
			setRatioReprocidadExp(relacionesBancariasBO.ObtenerRatioReprocidadExp(listaComexExportacion,mapListaDescripcionComex));
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	

	
	private List<Comex> getlistaTotalComex(){
		List<Comex> olistaComex=new ArrayList<Comex>();
		if (listaComexImportacion!=null && listaComexImportacion.size()>0){
			for (Comex ocomexI:listaComexImportacion){
				olistaComex.add(ocomexI);				
			}
		}
		if (listaComexExportacion!=null && listaComexExportacion.size()>0){
			for (Comex ocomexE:listaComexExportacion){
				olistaComex.add(ocomexE);				
			}
		}
		return olistaComex;
	}
	//FIN MCG20141113
	
	public List<PoolBancario> getListPoolBancarioPragrama() {
		return listPoolBancarioPragrama;
	}

	public void setListPoolBancarioPragrama(
			List<PoolBancario> listPoolBancarioPragrama) {
		this.listPoolBancarioPragrama = listPoolBancarioPragrama;
	}

	public List<rccmes> getListrccmes() {
		return listrccmes;
	}

	public void setListrccmes(List<rccmes> listrccmes) {
		this.listrccmes = listrccmes;
	}

	public List<rcd> getListrcd() {
		return listrcd;
	}

	public void setListrcd(List<rcd> listrcd) {
		this.listrcd = listrcd;
	}

	public List<RentabilidadBEC> getListRentabilidadBEC() {
		return listRentabilidadBEC;
	}

	public void setListRentabilidadBEC(List<RentabilidadBEC> listRentabilidadBEC) {
		this.listRentabilidadBEC = listRentabilidadBEC;
	}

	public String getLeftSideempresa() {
		return leftSideempresa;
	}

	public void setLeftSideempresa(String leftSideempresa) {
		this.leftSideempresa = leftSideempresa;
	}

	public String getRightSideempresa() {
		return rightSideempresa;
	}

	public void setRightSideempresa(String rightSideempresa) {
		this.rightSideempresa = rightSideempresa;
	}

	public String getLeftSidebanco() {
		return leftSidebanco;
	}

	public void setLeftSidebanco(String leftSidebanco) {
		this.leftSidebanco = leftSidebanco;
	}

	public String getRightSidebanco() {
		return rightSidebanco;
	}

	public void setRightSidebanco(String rightSidebanco) {
		this.rightSidebanco = rightSidebanco;
	}

	public List<String> getListaModeloRentabilidad() {
		return listaModeloRentabilidad;
	}

	public void setListaModeloRentabilidad(List<String> listaModeloRentabilidad) {
		this.listaModeloRentabilidad = listaModeloRentabilidad;
	}

	public String getModeloRentabilidad() {
		return modeloRentabilidad;
	}

	public void setModeloRentabilidad(String modeloRentabilidad) {
		this.modeloRentabilidad = modeloRentabilidad;
	}

	public boolean isCheckTipoDeudaTotal() {
		return checkTipoDeudaTotal;
	}

	public void setCheckTipoDeudaTotal(boolean checkTipoDeudaTotal) {
		this.checkTipoDeudaTotal = checkTipoDeudaTotal;
	}

	public boolean isCheckTipoDeudaDirecta() {
		return checkTipoDeudaDirecta;
	}

	public void setCheckTipoDeudaDirecta(boolean checkTipoDeudaDirecta) {
		this.checkTipoDeudaDirecta = checkTipoDeudaDirecta;
	}

	public boolean isCheckTipoDeudaIndirecta() {
		return checkTipoDeudaIndirecta;
	}

	public void setCheckTipoDeudaIndirecta(boolean checkTipoDeudaIndirecta) {
		this.checkTipoDeudaIndirecta = checkTipoDeudaIndirecta;
	}

	public String getTipoDeuda() {
		return tipoDeuda;
	}

	public void setTipoDeuda(String tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}
	public boolean isFlagPoolBancario() {
		return flagPoolBancario;
	}

	public void setFlagPoolBancario(boolean flagPoolBancario) {
		this.flagPoolBancario = flagPoolBancario;
	}

	public boolean isFlagLineas() {
		return flagLineas;
	}

	public void setFlagLineas(boolean flagLineas) {
		this.flagLineas = flagLineas;
	}

	public boolean isFlagCuotaFinanciera() {
		return flagCuotaFinanciera;
	}

	public void setFlagCuotaFinanciera(boolean flagCuotaFinanciera) {
		this.flagCuotaFinanciera = flagCuotaFinanciera;
	}

	public boolean isFlagModeloRentabilidad() {
		return flagModeloRentabilidad;
	}

	public void setFlagModeloRentabilidad(boolean flagModeloRentabilidad) {
		this.flagModeloRentabilidad = flagModeloRentabilidad;
	}

	public boolean isFlagEfectividadCartera() {
		return flagEfectividadCartera;
	}

	public void setFlagEfectividadCartera(boolean flagEfectividadCartera) {
		this.flagEfectividadCartera = flagEfectividadCartera;
	}

	public String getDesCalifBanco() {
		return desCalifBanco;
	}

	public void setDesCalifBanco(String desCalifBanco) {
		this.desCalifBanco = desCalifBanco;
	}

	public String getChkLineas() {
		return chkLineas;
	}

	public void setChkLineas(String chkLineas) {
		this.chkLineas = chkLineas;
	}

	public String getTipoMiles() {
		return tipoMiles;
	}

	public void setTipoMiles(String tipoMiles) {
		this.tipoMiles = tipoMiles;
	}

	public List<Tabla> getListaTipoMiles() {
		return listaTipoMiles;
	}

	public void setListaTipoMiles(List<Tabla> listaTipoMiles) {
		this.listaTipoMiles = listaTipoMiles;
	}

	public boolean isFlagEditado() {
		return flagEditado;
	}

	public void setFlagEditado(boolean flagEditado) {
		this.flagEditado = flagEditado;
	}

	public String getMensajeErrorHost() {
		return mensajeErrorHost;
	}

	public void setMensajeErrorHost(String mensajeErrorHost) {
		this.mensajeErrorHost = mensajeErrorHost;
	}

	public String getChkIndiceTransa() {
		return chkIndiceTransa;
	}

	public void setChkIndiceTransa(String chkIndiceTransa) {
		this.chkIndiceTransa = chkIndiceTransa;
	}

	public boolean isFlagIndiceTransa() {
		return flagIndiceTransa;
	}

	public void setFlagIndiceTransa(boolean flagIndiceTransa) {
		this.flagIndiceTransa = flagIndiceTransa;
	}

	public String getDato1() {
		return dato1;
	}

	public void setDato1(String dato1) {
		this.dato1 = dato1;
	}

	public List<Comex> getListaComexImportacion() {
		return listaComexImportacion;
	}

	public void setListaComexImportacion(List<Comex> listaComexImportacion) {
		this.listaComexImportacion = listaComexImportacion;
	}

	public List<Comex> getListaComexExportacion() {
		return listaComexExportacion;
	}

	public void setListaComexExportacion(List<Comex> listaComexExportacion) {
		this.listaComexExportacion = listaComexExportacion;
	}

	public boolean isFlagComex() {
		return flagComex;
	}

	public void setFlagComex(boolean flagComex) {
		this.flagComex = flagComex;
	}

	public String getPeriodoComex() {
		return periodoComex;
	}

	public void setPeriodoComex(String periodoComex) {
		this.periodoComex = periodoComex;
	}

	public List<Comex> getListaComex() {
		return listaComex;
	}

	public void setListaComex(List<Comex> listaComex) {
		this.listaComex = listaComex;
	}

	public String getRatioReprocidadImp() {
		return ratioReprocidadImp;
	}

	public void setRatioReprocidadImp(String ratioReprocidadImp) {
		this.ratioReprocidadImp = ratioReprocidadImp;
	}

	public String getRatioReprocidadExp() {
		return ratioReprocidadExp;
	}

	public void setRatioReprocidadExp(String ratioReprocidadExp) {
		this.ratioReprocidadExp = ratioReprocidadExp;
	}

	
	
	
	
}
