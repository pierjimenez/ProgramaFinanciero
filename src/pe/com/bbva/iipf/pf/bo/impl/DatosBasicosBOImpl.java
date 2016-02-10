package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.CabTabla;
import pe.com.bbva.iipf.pf.model.CapitalizacionBursatil;
import pe.com.bbva.iipf.pf.model.CompraVenta;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSPe21;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.NegocioBeneficio;
import pe.com.bbva.iipf.pf.model.Participaciones;
import pe.com.bbva.iipf.pf.model.Planilla;
import pe.com.bbva.iipf.pf.model.PrincipalesEjecutivos;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.RatingExterno;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.StringUtil;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("datosBasicosBO")
public class DatosBasicosBOImpl extends GenericBOImpl implements DatosBasicosBO{
	

	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private DatosBasicosBlobBO datosBasicosBlobBO;	
	
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private List<Planilla> listaPlanilla = new ArrayList<Planilla>();
	private Planilla totalPlanilla = new Planilla();
	private Planilla planillaAdmin = new Planilla();

	private Planilla planillaNoAdmin = new Planilla();
	private Programa programa;

	private String sintesisEmpresa;
	private String datosMatriz;
	private ProgramaBlob programaBlob = new ProgramaBlob(); 
	
	private List<Accionista> listaAccionistas = new ArrayList<Accionista>();
	private List<CapitalizacionBursatil> listaCapitalizacion = new ArrayList<CapitalizacionBursatil>();
	
	//ini MCG20111025
	private List<CabTabla> listaCabTabla= new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaPlanilla= new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaCompra= new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaVenta= new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaActividad= new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaNegocio= new ArrayList<CabTabla>();
	
	//fin MCG20111025
	
	
	private List<PrincipalesEjecutivos> listaPrinciEjecutivos = new ArrayList<PrincipalesEjecutivos>();
	
	private List<Participaciones> listaParticipaciones = new ArrayList<Participaciones>();
	
	private List<RatingExterno> listaRatingExterno = new ArrayList<RatingExterno>();
	
	private List<CompraVenta> listaCompraVenta = new ArrayList<CompraVenta>();
	
	private List<NegocioBeneficio> listaNBActividades = new ArrayList<NegocioBeneficio>();
	
	private List<NegocioBeneficio> listaNBNegocio = new ArrayList<NegocioBeneficio>();
	
	private List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();
	private List<DatosBasico> listaDatosBasicoGrupoEmpresas = new ArrayList<DatosBasico>();
	
	private String pathWebService;
	
	private ConfiguracionWS configuracionWS;
	
	/**
	 * Este metodo se ejectuta antes de realizar la validacion para el
	 * registro de datos basicos su objetivo es realizar la carga y seteo de
	 * los datos necesarios antes de ser guardados.
	 */
	public void beforeUpdateDatosBasicos(){
		listaPlanilla.clear();
		//carga planilla
		Tabla tipoPeriodo = null;
		Tabla tipoPersonal = null;
		tipoPeriodo = new Tabla();
		tipoPeriodo.setId(Constantes.ID_TIPO_PERIODO_ANUAL);
		planillaAdmin.setPrograma(programa);
		planillaAdmin.setTipoPerido(tipoPeriodo);
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_ADMINISTRATIVO);
		planillaAdmin.setTipoPersonal(tipoPersonal);	
		listaPlanilla.add(planillaAdmin);

		planillaNoAdmin.setPrograma(programa);
		planillaNoAdmin.setTipoPerido(tipoPeriodo);
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO);
		planillaNoAdmin.setTipoPersonal(tipoPersonal);	
		listaPlanilla.add(planillaNoAdmin);
		totalPlanilla.setPrograma(programa);
		listaPlanilla.add(totalPlanilla);
		
		//carga accionistas
		beforeSUDAccionistas();
		
		//ini MCG20111025
		listaCabTabla.clear();
		if (listaCabTablaPlanilla != null && listaCabTablaPlanilla.size() > 0) {
			for(CabTabla ctp : listaCabTablaPlanilla){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_PLANILLA);			
				ctp.setTipoTabla(otipoTabla);
				ctp.setPrograma(programa);
				listaCabTabla.add(ctp);			
			} 
		}
		if (listaCabTablaCompra != null && listaCabTablaCompra.size() > 0) {
			for(CabTabla ctc : listaCabTablaCompra){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_COMPRA);			
				ctc.setTipoTabla(otipoTabla);
				ctc.setPrograma(programa);
				listaCabTabla.add(ctc);			
			}
		}
		
		if (listaCabTablaVenta != null && listaCabTablaVenta.size() > 0) {
			for(CabTabla ctv : listaCabTablaVenta){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_VENTA);			
				ctv.setTipoTabla(otipoTabla);
				ctv.setPrograma(programa);
				listaCabTabla.add(ctv);			
			}
		}
		
		if (listaCabTablaActividad != null && listaCabTablaActividad.size() > 0) {
			for(CabTabla cta : listaCabTablaActividad){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);			
				cta.setTipoTabla(otipoTabla);
				cta.setPrograma(programa);
				listaCabTabla.add(cta);			
			}
		}
		
		if (listaCabTablaNegocio != null && listaCabTablaNegocio.size() > 0) {
			for(CabTabla ctn : listaCabTablaNegocio){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);			
				ctn.setTipoTabla(otipoTabla);
				ctn.setPrograma(programa);
				listaCabTabla.add(ctn);			
			}
		}
		
		//fin MCG20111025
	}
	
	
	//INI MCG20130212
	/**
	 * Este metodo se ejectuta antes de realizar la validacion para el
	 * registro de datos basicos su objetivo es realizar la carga y seteo de
	 * los datos necesarios antes de ser guardados.
	 */
	public void beforeUpdateDatosBasicos(String codEmpresaGrupo){
		listaPlanilla.clear();
		//carga planilla
		Tabla tipoPeriodo = null;
		Tabla tipoPersonal = null;
		tipoPeriodo = new Tabla();
		tipoPeriodo.setId(Constantes.ID_TIPO_PERIODO_ANUAL);
		planillaAdmin.setPrograma(programa);
		planillaAdmin.setCodEmpresaGrupo(codEmpresaGrupo);
		planillaAdmin.setTipoPerido(tipoPeriodo);
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_ADMINISTRATIVO);
		planillaAdmin.setTipoPersonal(tipoPersonal);	
		listaPlanilla.add(planillaAdmin);

		planillaNoAdmin.setPrograma(programa);
		planillaNoAdmin.setCodEmpresaGrupo(codEmpresaGrupo);
		planillaNoAdmin.setTipoPerido(tipoPeriodo);
		tipoPersonal = new Tabla();
		tipoPersonal.setId(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO);
		planillaNoAdmin.setTipoPersonal(tipoPersonal);	
		listaPlanilla.add(planillaNoAdmin);
		totalPlanilla.setPrograma(programa);
		totalPlanilla.setCodEmpresaGrupo(codEmpresaGrupo);
		
		listaPlanilla.add(totalPlanilla);
		
		//carga accionistas
		beforeSUDAccionistas(codEmpresaGrupo);
		
		//ini MCG20111025
		listaCabTabla.clear();
		if (listaCabTablaPlanilla != null && listaCabTablaPlanilla.size() > 0) {
			for(CabTabla ctp : listaCabTablaPlanilla){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_PLANILLA);			
				ctp.setTipoTabla(otipoTabla);
				ctp.setPrograma(programa);
				listaCabTabla.add(ctp);			
			} 
		}
		if (listaCabTablaCompra != null && listaCabTablaCompra.size() > 0) {
			for(CabTabla ctc : listaCabTablaCompra){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_COMPRA);			
				ctc.setTipoTabla(otipoTabla);
				ctc.setPrograma(programa);
				listaCabTabla.add(ctc);			
			}
		}
		
		if (listaCabTablaVenta != null && listaCabTablaVenta.size() > 0) {
			for(CabTabla ctv : listaCabTablaVenta){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_VENTA);			
				ctv.setTipoTabla(otipoTabla);
				ctv.setPrograma(programa);
				listaCabTabla.add(ctv);			
			}
		}
		
		if (listaCabTablaActividad != null && listaCabTablaActividad.size() > 0) {
			for(CabTabla cta : listaCabTablaActividad){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);			
				cta.setTipoTabla(otipoTabla);
				cta.setPrograma(programa);
				listaCabTabla.add(cta);			
			}
		}
		
		if (listaCabTablaNegocio != null && listaCabTablaNegocio.size() > 0) {
			for(CabTabla ctn : listaCabTablaNegocio){
				Tabla otipoTabla=new Tabla();			
				otipoTabla.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);			
				ctn.setTipoTabla(otipoTabla);
				ctn.setPrograma(programa);
				listaCabTabla.add(ctn);			
			}
		}
		
		//fin MCG20111025
	}
	//FIN MCG20130212
	
	/**
	 * Validar cualquier inconsistencia en datos basicos antes de ser registrado.
	 * 
	 * @return
	 */
	public boolean validateUpdateDatosBasicos(){
		return true;
	}

	/**
	 * Recupera los datos de la planilla para mostararlos en el formulario 
	 * @throws BOException
	 */
	public void afterUpdateDatosBasicos() throws BOException{
		planillaAdmin = listaPlanilla.get(0);
		planillaNoAdmin = listaPlanilla.get(1);
		totalPlanilla = listaPlanilla.get(2);
	}
	
	/**
	 * actuliza los datos basicos del programa financiero
	 * no incluye actualizacion de los campos blob
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateDatosBasicos() throws BOException {
		beforeUpdateDatosBasicos();
		if(validateUpdateDatosBasicos()){
			List parametros = new ArrayList();
			parametros.add(getPrograma().getComentAccionariado());
			parametros.add(getPrograma().getComentPartiSignificativa());
			parametros.add(getPrograma().getComentRatinExterno());
			parametros.add(getPrograma().getComentvaloraGlobal());
			parametros.add(getPrograma().getActividadPrincipal());
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updateDatosBasicos",parametros);
			//guardadno la planilla
			saveUDPlanilla(listaPlanilla);
			//guardando la lista de accionistas
			saveUDAccionista();
			//guardando la lista de cabecera de tablas
			saveUDCabeceraTabla();
			
			//guardando lista de Principales Ejecutivos
			if(programa.getTipoPrograma().getId().equals(Constantes.ID_TIPO_PROGRAMA_LOCAL)){
				saveUDEjecutivos();
			}
			//guardando las participaciones significativas
			saveUDParticipaciones();
			//guardando rating externo
			saveUDRatingExerno();
			//guardando Compras Ventas
			saveUDCompraVenta();
			//guardando Negocio Beneficio
			saveUDActividades();
			saveUDNegocio();
			
//			programaBlob.setPrograma(programa);
//			if(sintesisEmpresa != null){
//			programaBlob.setSintesisEmpresa(sintesisEmpresa.getBytes());
//			}
//			
//			programaBlobBO.save(programaBlob);
			
			//List<Usuario> lista = findByParams2(Usuario.class, null);
			//guardando la planilla
			List<Planilla> listaPlanilla = new ArrayList<Planilla>();
			//listaPlanilla.addAll(dbaction.getListaPlanilla());
			//savePlanilla(dbaction.);
			//actualizando la ultima modificacion al programa
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		
		//afterUpdateDatosBasicos();
	}
	
	//INI MCG20130212
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateDatosBasicos(String codEmpresaGrupo) throws BOException {
		beforeUpdateDatosBasicos(codEmpresaGrupo);
		if(validateUpdateDatosBasicos()){
			List parametros = new ArrayList();
			parametros.add(getPrograma().getComentAccionariado());
			parametros.add(getPrograma().getComentPartiSignificativa());
			parametros.add(getPrograma().getComentRatinExterno());
			parametros.add(getPrograma().getComentvaloraGlobal());
			parametros.add(getPrograma().getActividadPrincipal());
			if(getPrograma().getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){	
				parametros.add(getPrograma().getId());
				super.executeNamedQuery("updateDatosBasicos",parametros);					
			}else{
				Empresa empresaprin= new Empresa();	
				empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
				if (empresaprin!=null){
					if (empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
						parametros.add(getPrograma().getId());
						super.executeNamedQuery("updateDatosBasicos",parametros);	
					}else{
						parametros.add(getPrograma().getId());
						parametros.add(codEmpresaGrupo);
						super.executeNamedQuery("updateDatosBasicosByEmpresa",parametros);	
					}	
				}
			}
			
			
			//guardadno la planilla
			saveUDPlanilla(listaPlanilla,codEmpresaGrupo);
			//guardando la lista de accionistas
			saveUDAccionista(codEmpresaGrupo);
			//guardando la lista de cabecera de tablas
			saveUDCabeceraTabla();
			
			//guardando lista de Principales Ejecutivos
			if(programa.getTipoPrograma().getId().equals(Constantes.ID_TIPO_PROGRAMA_LOCAL)){
				saveUDEjecutivos(codEmpresaGrupo);
			}
			//guardando las participaciones significativas
			saveUDParticipaciones(codEmpresaGrupo);
			//guardando rating externo
			saveUDRatingExerno(codEmpresaGrupo);
			//guardando Compras Ventas
			saveUDCompraVenta(codEmpresaGrupo);
			//guardando Negocio Beneficio
			saveUDActividades(codEmpresaGrupo);
			saveUDNegocio(codEmpresaGrupo);
			
//			programaBlob.setPrograma(programa);
//			if(sintesisEmpresa != null){
//			programaBlob.setSintesisEmpresa(sintesisEmpresa.getBytes());
//			}
//			
//			programaBlobBO.save(programaBlob);
			
			//List<Usuario> lista = findByParams2(Usuario.class, null);
			//guardando la planilla
			List<Planilla> listaPlanilla = new ArrayList<Planilla>();
			//listaPlanilla.addAll(dbaction.getListaPlanilla());
			//savePlanilla(dbaction.);
			//actualizando la ultima modificacion al programa
			saveUDCapitalizacion(codEmpresaGrupo);
			
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
		}
		
		//afterUpdateDatosBasicos();
	}
	
	//FIN MCG20130212
	
	
	//INI MCG20130405
	
	public void beforeRefreshDatosBasicos(Programa t,String codEmpresaGrupo) throws BOException{
		//registro de los campos de auditoria		
		
		HashMap<String,String> datosBasicos = null;
		HashMap<String,String> datosBasicostmp = null;		
		List<DatosBasico> listDatosBasico=new ArrayList<DatosBasico>();
		
		HashMap<String,String> datosBasicosRC = null;
		HashMap<String,String> datosBasicosRC1 = null;
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		try {
			//En el caso de grupo se toma a la empresa principal
			//como dato base 
			//definir el codigo de la empresa principal en 
			//el programa
			String tipDocRuc = GenericAction.getObjectParamtrosSession(Constantes.EQUIV_RUC).toString();
			oConfiguracionWSPe21=getConfiguracionWSPE21(getUsuarioSession().getRegistroHost());
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			String VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			String VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			
			if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
				for(Empresa acctemp : listaGrupoEmpresas){
					DatosBasico datosBasicoEmpresa=new DatosBasico();
					String codEmpresaDB=acctemp.getCodigo();
					try{
						datosBasicostmp = QueryWS.consularDatosBasicos(acctemp.getCodigo(), 
								VAL_TIPO_DOC_COD_CENTRAL,
								getUsuarioSession().getRegistroHost(),
								oConfiguracionWSPe21);
					} catch (Exception e) {
						datosBasicostmp= new HashMap<String,String>();
						datosBasicostmp.put("codigoCentral", acctemp.getCodigo());
						datosBasicostmp.put("ruc", acctemp.getRuc());
						datosBasicostmp.put("nombreEmpesa", acctemp.getNombre());
						datosBasicostmp.put("pais", "");
						datosBasicostmp.put("codigoPais", "");
						datosBasicostmp.put("fechaNacimiento", "");
						datosBasicostmp.put("fechaAntig","");
						logger.error(StringUtil.getStackTrace(e));
					}
					
					if (codEmpresaDB.equals(codEmpresaGrupo)){	
						t.setIdEmpresa(acctemp.getCodigo());	
						t.setRuc((acctemp.getRuc()==null?datosBasicostmp.get("ruc"):acctemp.getRuc()));
						datosBasicos=datosBasicostmp;
					}
					
					
				   // ini MCG20130218
					datosBasicoEmpresa.setRuc(datosBasicostmp.get("ruc"));
					datosBasicoEmpresa.setPais(datosBasicostmp.get("pais"));
					String antiguedadCliente = FechaUtil
							.getNumAnios(datosBasicostmp.get("fechaAntig"));// datosBasicos[5]
					String antiguedadNegocio = FechaUtil
							.getNumAnios(datosBasicostmp.get("fechaNacimiento"));// datosBasicos[6]
					if (antiguedadCliente != null
							&& !antiguedadCliente.trim().equals("")) {
						datosBasicoEmpresa.setAntiguedadCliente(Integer
								.parseInt(antiguedadCliente));
					} else {
						datosBasicoEmpresa.setAntiguedadCliente(null);
					}
					if (antiguedadNegocio != null
							&& !antiguedadNegocio.trim().equals("")) {
						datosBasicoEmpresa.setAntiguedadNegocio(Integer
								.parseInt(antiguedadNegocio));
					} else {
						datosBasicoEmpresa.setAntiguedadNegocio(null);
					}

					if (t.getTipoPrograma().getId().equals(
							Constantes.ID_TIPO_PROGRAMA_LOCAL)) {
						String buro1 ="";
						try {
							
							buro1 = QueryWS.consultarGrupoBuro(acctemp
									.getRuc(),tipDocRuc, getUsuarioSession()
									.getRegistroHost(), getPathWebService());
						} catch (Exception e) {
							logger.error(StringUtil.getStackTrace(e));
							buro1 ="";
						}
						
						if (buro1 != null) {
							datosBasicoEmpresa.setGrupoRiesgoBuro(buro1);
						}
					}
					try {
						String urlRig4 = GenericAction.getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
						datosBasicosRC1 = ConsultaWS.consularDatosReporteCredito(acctemp.getCodigo(), getConfiguracionWS(),urlRig4);
						if (datosBasicosRC1 != null) {
							String separador1="";
							String separador2="";
							String separador3="";
							if (datosBasicosRC1.get("codRegistroGestor")!=null && !datosBasicosRC1.get("codRegistroGestor").equals("")){separador1=" - ";}
							if (datosBasicosRC1.get("codOficinaPrincipal")!=null && !datosBasicosRC1.get("codOficinaPrincipal").equals("")){separador2=" - ";}
							if (datosBasicosRC1.get("codEtiqueta")!=null && !datosBasicosRC1.get("codEtiqueta").equals("")){separador3=" - ";}						
							
							datosBasicoEmpresa.setCalificacionBanco(datosBasicosRC1.get("clasificacionBanco")==null?"":datosBasicosRC1.get("clasificacionBanco"));
							
							datosBasicoEmpresa.setGestor((datosBasicosRC1.get("codRegistroGestor")==null?"":datosBasicosRC1.get("codRegistroGestor")) +separador1+ (datosBasicosRC1.get("nombreGestor")==null?"":datosBasicosRC1.get("nombreGestor")) );
							datosBasicoEmpresa.setOficina((datosBasicosRC1.get("codOficinaPrincipal")==null?"":datosBasicosRC1.get("codOficinaPrincipal")) +separador2+ (datosBasicosRC1.get("descOficinaPrincipal")==null?"":datosBasicosRC1.get("descOficinaPrincipal")) );
						    datosBasicoEmpresa.setSegmento((datosBasicosRC1.get("codEtiqueta")==null?"":datosBasicosRC1.get("codEtiqueta") )+separador3+ (datosBasicosRC1.get("descEtiqueta")==null?"":datosBasicosRC1.get("descEtiqueta")) );
							
							
						}
					} catch (Exception e) {
						logger.error(StringUtil.getStackTrace(e));
					}					
					// fin MCG20130218		
					

					datosBasicoEmpresa.setPrograma(t);
					datosBasicoEmpresa.setCodigoEmpresa(acctemp.getCodigo());
					datosBasicoEmpresa.setRuc(acctemp.getRuc());
					datosBasicoEmpresa.setNombreGrupoEmpresa(acctemp.getNombre());	
					
	
					
					
					listDatosBasico.add(datosBasicoEmpresa);					
					
				}
			}else{
				
				if(t.getIdEmpresa() != null &&
				   !t.getIdEmpresa().trim().equals("")){
					datosBasicos = QueryWS.consularDatosBasicos(t.getIdEmpresa(),
							VAL_TIPO_DOC_COD_CENTRAL,
							getUsuarioSession().getRegistroHost(),
							oConfiguracionWSPe21);
					t.setRuc(datosBasicos.get("ruc"));

				}else{					
					datosBasicos = QueryWS.consularDatosBasicos(t.getRuc(),
							VAL_TIPO_DOC_RUC,
							getUsuarioSession().getRegistroHost(),
							oConfiguracionWSPe21);
					t.setIdEmpresa(datosBasicos.get("codigoCentral"));
				}		
				
			}	
			//cambiado 16/05/2012 el codigo buro no es oblitatorio
			if(t.getTipoPrograma().getId().equals(Constantes.ID_TIPO_PROGRAMA_LOCAL)){
				String buro="";
				try {
				 buro = QueryWS.consultarGrupoBuro(t.getRuc(),tipDocRuc,
														 getUsuarioSession().getRegistroHost(),
														 getPathWebService());
				} catch (Exception e) {
					buro="";
				}
				if(buro != null){
					t.setGrupoRiesgoBuro(buro);
				}
			}
			
			//ini MCG20130401
			try {
				String urlRig4 = GenericAction.getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
				datosBasicosRC = ConsultaWS.consularDatosReporteCredito(t.getIdEmpresa(), getConfiguracionWS(),urlRig4);
				if (datosBasicosRC != null) {
					String separador1="";
					String separador2="";
					String separador3="";
					
					if (datosBasicosRC.get("codRegistroGestor")!=null && !datosBasicosRC.get("codRegistroGestor").equals("")){separador1=" - ";}
					if (datosBasicosRC.get("codOficinaPrincipal")!=null && !datosBasicosRC.get("codOficinaPrincipal").equals("")){separador2=" - ";}
					if (datosBasicosRC.get("codEtiqueta")!=null && !datosBasicosRC.get("codEtiqueta").equals("")){separador3=" - ";}	
					
					t.setCalificacionBanco(datosBasicosRC.get("clasificacionBanco")==null?"":datosBasicosRC.get("clasificacionBanco"));
						
					t.setGestor((datosBasicosRC.get("codRegistroGestor")==null?"":datosBasicosRC.get("codRegistroGestor")) +separador1+(datosBasicosRC.get("nombreGestor")==null?"":datosBasicosRC.get("nombreGestor")) );					
					t.setOficina((datosBasicosRC.get("codOficinaPrincipal")==null?"":datosBasicosRC.get("codOficinaPrincipal")) +separador2+(datosBasicosRC.get("descOficinaPrincipal")==null?"":datosBasicosRC.get("descOficinaPrincipal")) );					
					t.setSegmento((datosBasicosRC.get("codEtiqueta")==null?"":datosBasicosRC.get("codEtiqueta")) +separador3+ (datosBasicosRC.get("descEtiqueta")==null?"":datosBasicosRC.get("descEtiqueta")) );
						
					
				}
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}				
			//fin MCG20130401				

			String antiguedadCliente = FechaUtil.getNumAnios(datosBasicos.get("fechaAntig"));//datosBasicos[5] 
			String antiguedadNegocio = FechaUtil.getNumAnios(datosBasicos.get("fechaNacimiento"));//datosBasicos[6]
			if(antiguedadCliente != null &&
				!antiguedadCliente.trim().equals("")){
				t.setAntiguedadCliente(Integer.parseInt(antiguedadCliente));
			}else{
				t.setAntiguedadCliente(null);
			}
			if(antiguedadNegocio != null &&
			   !antiguedadNegocio.trim().equals("")){
				t.setAntiguedadNegocio(Integer.parseInt(antiguedadNegocio));
			}else{
				t.setAntiguedadNegocio(null);
			}
			t.setPais(datosBasicos.get("pais"));				
			
			listaDatosBasicoGrupoEmpresas=listDatosBasico;		
			
		} catch (WSException e) {
			logger.error(StringUtil.getStackTrace(e));
			throw new BOException(e.getMessage());
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshDatosBasicos(String codEmpresaGrupo) throws BOException {
		beforeRefreshDatosBasicos(programa,codEmpresaGrupo);
		if(validateUpdateDatosBasicos()){	
			List parametros = new ArrayList();		
					
			if(getPrograma().getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){		
				parametros = new ArrayList();
				
				parametros.add(getPrograma().getPais());				
				//por ser integer						
			    String antNegocio=" DB_ANTIGUEDAD_NEGO =null";
				String antCliente=" DB_ANTIGUEDAD_CLIE =null";
			    if (getPrograma().getAntiguedadNegocio()!=null){
			    	antNegocio= " DB_ANTIGUEDAD_NEGO = ?";
			    	parametros.add(getPrograma().getAntiguedadNegocio());
			    }
			    if (getPrograma().getAntiguedadCliente()!=null){
			    	antCliente= " DB_ANTIGUEDAD_CLIE = ?";
			    	parametros.add(getPrograma().getAntiguedadCliente());
			    }			    				
				//parametros.add(getPrograma().getAntiguedadNegocio()==null?java.sql.Types.INTEGER:getPrograma().getAntiguedadNegocio());
				//parametros.add(getPrograma().getAntiguedadCliente()==null?java.sql.Types.INTEGER:getPrograma().getAntiguedadCliente());
				
			    parametros.add(getPrograma().getCalificacionBanco());
				parametros.add(getPrograma().getGestor());
				parametros.add(getPrograma().getOficina());
				parametros.add(getPrograma().getSegmento());
				parametros.add(getPrograma().getGrupoRiesgoBuro());
				//add ruc
				parametros.add(getPrograma().getRuc()==null?"":getPrograma().getRuc());
				parametros.add(getPrograma().getId());
				
				String sqlqueryp = " UPDATE PROFIN.TIIPF_PROGRAMA set DB_T_EXT_PAIS_CODIGO = ? ," +								
			    antNegocio + "," + antCliente + "," +	
				" RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
				" RDC_OFICINA = ?, RDC_SEGMENTO = ?," +
				" DB_GRUP_RIESGO = ?," +
				" DB_RUC = ? " +
				" where id_programa = ? ";								
				super.executeSQLQuery(sqlqueryp,parametros);
				//Actualizando tabla Empresa.
				try {
					List parametrosEmpr = new ArrayList();	
					parametrosEmpr.add(getPrograma().getRuc()==null?"":getPrograma().getRuc());
					parametrosEmpr.add(getPrograma().getIdEmpresa()==null?"":getPrograma().getIdEmpresa());
					parametrosEmpr.add(getPrograma().getId());
					String sqlUpdateEmpresa="UPDATE PROFIN.TIIPF_EMPRESA "+
											" SET RUC=?" +
											" where CODIGO=?"+
											" AND IIPF_PROGRAMA_ID=?";
					super.executeSQLQuery(sqlUpdateEmpresa,parametrosEmpr);
					
				} catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
				}
				
				//super.executeNamedQuery("refreshDatosBasicosHost",parametros);					
			}else{						
				
				if (listaDatosBasicoGrupoEmpresas!=null && listaDatosBasicoGrupoEmpresas.size()>0){
					for (DatosBasico odatosBasicos:listaDatosBasicoGrupoEmpresas){					
						String codEmpresa=odatosBasicos.getCodigoEmpresa();	
						Empresa empresaprin= new Empresa();	
						empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresa);
						
						HashMap<String, Object> parametrossdb = new HashMap<String, Object>();
						parametrossdb.put("programa", getPrograma().getId());
						parametrossdb.put("codigoEmpresa", codEmpresa);
						List<DatosBasico> olista =  super.findByParams2(DatosBasico.class, parametrossdb);	
						if((olista==null  )|| (olista!=null && olista.size()==0) ){
							odatosBasicos.setId(null);
							odatosBasicos.setPrograma(getPrograma());
							odatosBasicos.setCodigoEmpresa(codEmpresa);
							save(odatosBasicos);
						}	
						
							parametros = new ArrayList();						
							parametros.add(odatosBasicos.getPais()==null?"":odatosBasicos.getPais().toString());
							//por ser integer						
						    String antNegocio=" DB_ANTIGUEDAD_NEGO =null";
							String antCliente=" DB_ANTIGUEDAD_CLIE =null";
						    if (odatosBasicos.getAntiguedadNegocio()!=null){
						    	antNegocio= " DB_ANTIGUEDAD_NEGO = ?";
						    	parametros.add(odatosBasicos.getAntiguedadNegocio());
						    }
						    if (odatosBasicos.getAntiguedadCliente()!=null){
						    	antCliente= " DB_ANTIGUEDAD_CLIE = ?";
						    	parametros.add(odatosBasicos.getAntiguedadCliente());
						    }
						    String sqlquery = " update PROFIN.tiipf_datos_basico set db_t_ext_pais_codigo = ? ," +								
						    antNegocio + "," + antCliente + "," +	
							" RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
							" RDC_OFICINA = ?, RDC_SEGMENTO = ?," +
							" DB_GRUP_RIESGO = ?, " +
							" DB_RUC = ? " +
							" where id_programa = ? AND codigo_empresa=?";									
							parametros.add(odatosBasicos.getCalificacionBanco()==null?"":odatosBasicos.getCalificacionBanco().toString());
							parametros.add(odatosBasicos.getGestor()==null?"":odatosBasicos.getGestor().toString());
							parametros.add(odatosBasicos.getOficina()==null?"":odatosBasicos.getOficina().toString());
							parametros.add(odatosBasicos.getSegmento()==null?"":odatosBasicos.getSegmento().toString());
							parametros.add(odatosBasicos.getGrupoRiesgoBuro()==null?"":odatosBasicos.getGrupoRiesgoBuro().toString());
							//add ruc
							parametros.add(odatosBasicos.getRuc()==null?"":odatosBasicos.getRuc().toString());
							parametros.add(getPrograma().getId());
						
							if (empresaprin!=null && empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){								
								String sqlquery1 = " UPDATE PROFIN.TIIPF_PROGRAMA set DB_T_EXT_PAIS_CODIGO = ? ," +								
							    antNegocio + "," + antCliente + "," +	
								" RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
								" RDC_OFICINA = ?, RDC_SEGMENTO = ?," +
								" DB_GRUP_RIESGO = ?, " +
								" DB_RUC = ? " +
								" where id_programa = ? ";								
								super.executeSQLQuery(sqlquery1,parametros);	
							}								
							parametros.add(codEmpresa);
							super.executeSQLQuery(sqlquery,parametros);	
							//Actualizando tabla Empresa.
							try {
								List parametrosEmpr = new ArrayList();	
								parametrosEmpr.add(odatosBasicos.getRuc()==null?"":odatosBasicos.getRuc().toString());
								parametrosEmpr.add(codEmpresa);
								parametrosEmpr.add(getPrograma().getId());
								String sqlUpdateEmpresa="UPDATE PROFIN.TIIPF_EMPRESA "+
														" SET RUC=?" +
														" where CODIGO=?"+
														" AND IIPF_PROGRAMA_ID=?";
								super.executeSQLQuery(sqlUpdateEmpresa,parametrosEmpr);
								
							} catch (Exception e) {
								logger.error(StringUtil.getStackTrace(e));
							}
							
							
														
					}
				}
			}						
	
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		
		//afterUpdateDatosBasicos();
	}
	
	//FIN MCG20130405
	
	public boolean validarPlanillas(){
		return true;
	}

	/**
	 * valida y gudarda la planilla
	 * @throws BOException
	 */
	public void saveUDPlanilla(List<Planilla> planilla)throws BOException {
		saveCollection(planilla);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDPlanilla(List<Planilla> planilla,String codEmpresaGrupo)throws BOException {
		//ini MCG20130909
		//saveCollection(planilla);				
		if(validarPlanillas()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			
			List<Planilla> listatemp =  findByParams2(Planilla.class, params);
			List<Planilla> listaDel = new ArrayList<Planilla>();
			boolean flag= false;
			for(Planilla pla : listatemp ){
				for(Planilla acctemp : planilla){
					if(acctemp.getId()!=null && acctemp.getId().equals(pla.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(pla);
				}
				flag= false;
			}
			saveCollection(planilla);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		//fin MCG20130909
	}
	
	public void beforeSUDAccionistas(){
		int pos=0;
		for(Accionista acc : listaAccionistas){
			acc.setPrograma(programa);
			acc.setPos(pos);
			pos++;
		}
	}
	//ini MCG20130214
	public void beforeSUDAccionistas(String codEmpresaGrupo){
		int pos=0;
		for(Accionista acc : listaAccionistas){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPos(pos);
			pos++;
		}
	}
	//fin MCG20130214
	
	//ini MCG20130214
	public void beforeSUDCapitalizacion(String codEmpresaGrupo){
		int pos=0;
		for(CapitalizacionBursatil cb : listaCapitalizacion){
			cb.setPrograma(programa);
			cb.setCodEmpresaGrupo(codEmpresaGrupo);
			cb.setPos(pos);
			pos++;
		}
	}
	//fin MCG20130214
	public boolean validarAccionistas(){
		return true;
	}
	
	public boolean validarCapitalizacion(){
		return true;
	}
	
	/**
	 * valida y registra los principales accionistas de las empresas
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDAccionista()throws BOException {
		beforeSUDAccionistas();
		if(validarAccionistas()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<Accionista> listatemp =  findByParams2(Accionista.class, params);
			List<Accionista> listaDel = new ArrayList<Accionista>();
			boolean flag= false;
			for(Accionista acc : listatemp ){
				for(Accionista acctemp : listaAccionistas){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaAccionistas);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130214
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDAccionista(String codEmpresaGrupo)throws BOException {
		beforeSUDAccionistas(codEmpresaGrupo);
		if(validarAccionistas()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			
			List<Accionista> listatemp =  findByParams2(Accionista.class, params);
			List<Accionista> listaDel = new ArrayList<Accionista>();
			boolean flag= false;
			
			//controlando id null
			for(Accionista acctemp : listaAccionistas){
				if (acctemp.getTipoDocumento()!=null && acctemp.getTipoDocumento().getId()==null){		
					acctemp.setTipoDocumento(null);
				}
			}
			
			
			for(Accionista acc : listatemp ){
				for(Accionista acctemp : listaAccionistas){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaAccionistas);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130214
	
	//ini MCG20130725
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDCapitalizacion(String codEmpresaGrupo)throws BOException {
		beforeSUDCapitalizacion(codEmpresaGrupo);
		if(validarCapitalizacion()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			
			List<CapitalizacionBursatil> listatemp =  findByParams2(CapitalizacionBursatil.class, params);
			List<CapitalizacionBursatil> listaDel = new ArrayList<CapitalizacionBursatil>();
			boolean flag= false;
			
			//controlando id null
			for(CapitalizacionBursatil cctemp : listaCapitalizacion){
				if (cctemp.getDivisa()!=null && cctemp.getDivisa().getId()==null){		
					cctemp.setDivisa(null);
				}
			}			
			
			for(CapitalizacionBursatil acc : listatemp ){
				for(CapitalizacionBursatil acctemp : listaCapitalizacion){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaCapitalizacion);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130725
	
	//ini MCG20111022
	public void beforeSUDCabeceraTabla(){
		for(CabTabla ct : listaCabTabla){
			ct.setPrograma(programa);
		}
	}
	
	public boolean validarCabeceraTabla(){
		return true;
	}
	
	/**
	 * valida y registra los principales accionistas de las empresas
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDCabeceraTabla()throws BOException {
		beforeSUDCabeceraTabla();
		if(validarCabeceraTabla()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<CabTabla> listatemp =  findByParams2(CabTabla.class, params);
			List<CabTabla> listaDel = new ArrayList<CabTabla>();
			boolean flag= false;
			for(CabTabla cttmp : listatemp ){
				for(CabTabla ct : listaCabTabla){
					if(ct.getId()!=null && ct.getId().equals(cttmp.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(cttmp);
				}
				flag= false;
			}
			saveCollection(listaCabTabla);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin NCG20111022
	

	public void beforeUDEjecutivos(){
		int posicion = 0;
		for(PrincipalesEjecutivos acc : listaPrinciEjecutivos){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	
	//ini MCG20130220
	public void beforeUDEjecutivos(String codEmpresaGrupo){
		int posicion = 0;
		for(PrincipalesEjecutivos acc : listaPrinciEjecutivos){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			posicion++;
		}
	}	
	//fin MCG20130220
	
	
	public boolean validationUDEjecutivos(){
		return true;
	}
	
	/**
	 * Registra valida los principales ejecutivos
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDEjecutivos() throws BOException{
		beforeUDEjecutivos();
		if(validationUDEjecutivos()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<PrincipalesEjecutivos> listatemp =  findByParams2(PrincipalesEjecutivos.class, params);
			List<PrincipalesEjecutivos> listaDel = new ArrayList<PrincipalesEjecutivos>();
			boolean flag= false;
			for(PrincipalesEjecutivos acc : listatemp ){
				for(PrincipalesEjecutivos acctemp : listaPrinciEjecutivos){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaPrinciEjecutivos);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130220
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDEjecutivos(String codEmpresaGrupo) throws BOException{
		beforeUDEjecutivos(codEmpresaGrupo);
		if(validationUDEjecutivos()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<PrincipalesEjecutivos> listatemp =  findByParams2(PrincipalesEjecutivos.class, params);
			List<PrincipalesEjecutivos> listaDel = new ArrayList<PrincipalesEjecutivos>();
			boolean flag= false;
			for(PrincipalesEjecutivos acc : listatemp ){
				for(PrincipalesEjecutivos acctemp : listaPrinciEjecutivos){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaPrinciEjecutivos);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fni MCG20130220
	public void beforeUDParticipaciones(){
		int posicion=0;
		for(Participaciones acc : listaParticipaciones){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	
	//ini MCG20130214
	public void beforeUDParticipaciones(String codEmpresaGrupo){
		int posicion=0;
		for(Participaciones acc : listaParticipaciones){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	//fin MCG20130214
	
	public boolean validationUDParticipaciones(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra las participaciones
	 * siginficativas
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDParticipaciones() throws BOException{
		beforeUDParticipaciones();
		if(validationUDParticipaciones()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<Participaciones> listatemp =  findByParams2(Participaciones.class, params);
			List<Participaciones> listaDel = new ArrayList<Participaciones>();
			boolean flag= false;
			for(Participaciones acc : listatemp ){
				for(Participaciones acctemp : listaParticipaciones){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaParticipaciones);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130214
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDParticipaciones(String codEmpresaGrupo) throws BOException{
		beforeUDParticipaciones(codEmpresaGrupo);
		if(validationUDParticipaciones()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<Participaciones> listatemp =  findByParams2(Participaciones.class, params);
			List<Participaciones> listaDel = new ArrayList<Participaciones>();
			boolean flag= false;
			for(Participaciones acc : listatemp ){
				for(Participaciones acctemp : listaParticipaciones){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaParticipaciones);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130214
	
	private boolean validationUDRatingExerno() {
		
		return true;
	}

	private void beforeUDRatingExerno() {
		int posicion =0;
		for(RatingExterno acc : listaRatingExterno){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	
	//ini MCG20130214
	
	private void beforeUDRatingExerno(String codEmpresaGrupo) {
		int posicion =0;
		for(RatingExterno acc : listaRatingExterno){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	//fin MCG20130214
	
	/**
	 * valida registra y actualiza el rating externo
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDRatingExerno() throws BOException {
		beforeUDRatingExerno();
		if(validationUDRatingExerno()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<RatingExterno> listatemp =  findByParams2(RatingExterno.class, params);
			List<RatingExterno> listaDel = new ArrayList<RatingExterno>();
			boolean flag= false;
			for(RatingExterno acc : listatemp ){
				for(RatingExterno acctemp : listaRatingExterno){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaRatingExterno);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130214
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDRatingExerno(String codEmpresaGrupo) throws BOException {
		beforeUDRatingExerno(codEmpresaGrupo);
		if(validationUDRatingExerno()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<RatingExterno> listatemp =  findByParams2(RatingExterno.class, params);
			List<RatingExterno> listaDel = new ArrayList<RatingExterno>();
			boolean flag= false;
			for(RatingExterno acc : listatemp ){
				for(RatingExterno acctemp : listaRatingExterno){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaRatingExterno);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130214

	private void beforeUDCompraVenta() {
		for(CompraVenta acc : listaCompraVenta){
			acc.setPrograma(programa);
		}
	}
	//ini MCG20130214
	private void beforeUDCompraVenta(String codEmpresaGrupo) {
		for(CompraVenta acc : listaCompraVenta){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			
		}
	}
	//fin MCG20130214
	
	private boolean validationCompraVenta(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDCompraVenta() throws BOException {
		beforeUDCompraVenta();
		if(validationCompraVenta()){
			saveCollection(listaCompraVenta);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDCompraVenta(String codEmpresaGrupo) throws BOException {
		beforeUDCompraVenta(codEmpresaGrupo);
		if(validationCompraVenta()){
			//ini MCG20130909
			//saveCollection(listaCompraVenta);			
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			
			List<CompraVenta> listatemp =  findByParams2(CompraVenta.class, params);
			List<CompraVenta> listaDel = new ArrayList<CompraVenta>();
			boolean flag= false;
			for(CompraVenta cv : listatemp ){
				for(CompraVenta cvtemp : listaCompraVenta){
					if(cvtemp.getId()!=null && cvtemp.getId().equals(cv.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(cv);
				}
				flag= false;
			}
			saveCollection(listaCompraVenta);
			deleteCollection(listaDel);			
			//fin MCG20130909
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	public void beforeActividades()throws BOException{
		int posicion = 0;
		for(NegocioBeneficio acc : listaNBActividades){
			acc.setPrograma(programa);
			acc.setTipo(Integer.valueOf(Constantes.POR_LINEA_ACTIVIDAD));
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	
	//ini MCG20130415
	public void beforeActividades(String codEmpresaGrupo)throws BOException{
		int posicion = 0;
		for(NegocioBeneficio acc : listaNBActividades){
			acc.setPrograma(programa);
			acc.setTipo(Integer.valueOf(Constantes.POR_LINEA_ACTIVIDAD));
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	//ini MCG20130415
	
	public boolean validationActividades(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDActividades() throws BOException {
		beforeActividades();
		if(validationActividades()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("tipo", Integer.valueOf(Constantes.POR_LINEA_ACTIVIDAD));
			List<NegocioBeneficio> listatemp =  findByParams2(NegocioBeneficio.class, params);
			List<NegocioBeneficio> listaDel = new ArrayList<NegocioBeneficio>();
			boolean flag= false;
			for(NegocioBeneficio acc : listatemp ){
				for(NegocioBeneficio acctemp : listaNBActividades){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaNBActividades);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130215
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDActividades(String codEmpresaGrupo) throws BOException {
		beforeActividades(codEmpresaGrupo);
		if(validationActividades()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("tipo", Integer.valueOf(Constantes.POR_LINEA_ACTIVIDAD));
			params.put("codEmpresaGrupo",codEmpresaGrupo );
			List<NegocioBeneficio> listatemp =  findByParams2(NegocioBeneficio.class, params);
			List<NegocioBeneficio> listaDel = new ArrayList<NegocioBeneficio>();
			boolean flag= false;
			for(NegocioBeneficio acc : listatemp ){
				for(NegocioBeneficio acctemp : listaNBActividades){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaNBActividades);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130215

	public void beforeNegocio(){
		int posicion = 0;
		for(NegocioBeneficio acc : listaNBNegocio){
			acc.setPrograma(programa);
			acc.setTipo(Integer.valueOf(Constantes.POR_LINEA_NEGOCIO));
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	
	//ini MCG20130215
	public void beforeNegocio(String codEmpresaGrupo){
		int posicion = 0;
		for(NegocioBeneficio acc : listaNBNegocio){
			acc.setPrograma(programa);
			acc.setTipo(Integer.valueOf(Constantes.POR_LINEA_NEGOCIO));
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			posicion++;
		}
	}
	//fin MCG20130215
	
	public boolean validationNegocio(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDNegocio() throws BOException {
		beforeNegocio();
		if(validationNegocio()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("tipo", Integer.valueOf(Constantes.POR_LINEA_NEGOCIO));
			List<NegocioBeneficio> listatemp =  findByParams2(NegocioBeneficio.class, params);
			List<NegocioBeneficio> listaDel = new ArrayList<NegocioBeneficio>();
			boolean flag= false;			
			for(NegocioBeneficio acc : listatemp ){
				for(NegocioBeneficio acctemp : listaNBNegocio){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaNBNegocio);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130215
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDNegocio(String codEmpresaGrupo) throws BOException {
		beforeNegocio(codEmpresaGrupo);
		if(validationNegocio()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("tipo", Integer.valueOf(Constantes.POR_LINEA_NEGOCIO));
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<NegocioBeneficio> listatemp =  findByParams2(NegocioBeneficio.class, params);
			List<NegocioBeneficio> listaDel = new ArrayList<NegocioBeneficio>();
			boolean flag= false;			
			for(NegocioBeneficio acc : listatemp ){
				for(NegocioBeneficio acctemp : listaNBNegocio){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaNBNegocio);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//ini MCG20130215
	@Override
	public List<Planilla> getListaPlanilla()throws BOException  {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", programa.getId());
		List<Planilla> listatemp = null;
		try {
			listatemp = findByParams2(Planilla.class, params);
			Long valIdPro=programa.getId();
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<Planilla> getListaPlanilla(String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);		
		List<Planilla> listatemp = null;
		try {
			listatemp = findByParams2(Planilla.class, params);			
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130219
	@Override
	public List<DatosBasico> getListaDatosBasico(String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codigoEmpresa", CodEmpresaGrupo);
		List<DatosBasico> listatemp = null;
		try {
			listatemp = findByParams2(DatosBasico.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<DatosBasico> getListaDatosBasico(Programa oprograma,String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", oprograma.getId());
		params.put("codigoEmpresa", CodEmpresaGrupo);
		List<DatosBasico> listatemp = null;
		try {
			listatemp = findByParams2(DatosBasico.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//fin MCG20130219
	@Override
	public List<Planilla> getListaPlanilla(Programa oprograma)throws BOException  {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", oprograma.getId());
		List<Planilla> listatemp = null;
		try {
			listatemp = findByParams2(Planilla.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<Planilla> getListaPlanilla(Programa oprograma,String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", oprograma.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		List<Planilla> listatemp = null;
		try {
			listatemp = findByParams2(Planilla.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130221

	public void setListaPlanilla(List<Planilla> listaPlanilla) {
		this.listaPlanilla = listaPlanilla;
	}
	public Planilla getTotalPlanilla() {
		return totalPlanilla;
	}

	public void setTotalPlanilla(Planilla totalPlanilla) {
		this.totalPlanilla = totalPlanilla;
	}
	
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
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

	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}

	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}

	public void setSintesisEmpresa(String sintesisEmpresa) {
		this.sintesisEmpresa = sintesisEmpresa;
		
	}

	public void setDatosMatriz(String datosMatriz) {
		this.datosMatriz = datosMatriz;
	}

	public void setListaAccionistas(List<Accionista> listaAccionistas) {
		this.listaAccionistas = listaAccionistas;
	}
	
	
	public void setListaCapitalizacion(
			List<CapitalizacionBursatil> listaCapitalizacion) {
		this.listaCapitalizacion = listaCapitalizacion;
	}


	public void setListaPrinciEjecutivos(
			List<PrincipalesEjecutivos> listaPrinciEjecutivos) {
		this.listaPrinciEjecutivos = listaPrinciEjecutivos;
	}

	public void setListaParticipaciones(List<Participaciones> listaParticipaciones) {
		this.listaParticipaciones = listaParticipaciones;
	}

	public void setListaRatingExterno(List<RatingExterno> listaRatingExterno) {
		this.listaRatingExterno = listaRatingExterno;
	}

	public void setListaCompraVenta(List<CompraVenta> listaCompraVenta) {
		this.listaCompraVenta = listaCompraVenta;
	}

	public void setListaNBActividades(List<NegocioBeneficio> listaNBActividades) {
		this.listaNBActividades = listaNBActividades;
	}

	public void setListaNBNegocio(List<NegocioBeneficio> listaNBNegocio) {
		this.listaNBNegocio = listaNBNegocio;
	}
	@Override
	public List<Accionista> getListaAccionistas() throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", programa.getId());
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//ini MCG20130214
	@Override
	public List<Accionista> getListaAccionistas(String CodEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);			
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130214
	
	//ini MCG20130725
	@Override
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(String CodEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<CapitalizacionBursatil> listatemp = null;
		try {
			listatemp = findByParamsOrder(CapitalizacionBursatil.class, params,order);			
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();		
		params.put("programa", oprograma.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<CapitalizacionBursatil> listatemp = null;
		try {
			listatemp = findByParamsOrder(CapitalizacionBursatil.class, params,order);			
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(Programa oprograma) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();		
		params.put("programa", oprograma.getId());	
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<CapitalizacionBursatil> listatemp = null;
		try {
			listatemp = findByParamsOrder(CapitalizacionBursatil.class, params,order);			
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130725
	@Override
	public List<Accionista> getListaAccionistasByOrdenPorc(Long idprograma) throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", idprograma);
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("porcentaje", " desc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130409
	@Override
	public List<Accionista> getListaAccionistasByOrdenPorc(Long idprograma,String codEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", idprograma);
		params.put("codEmpresaGrupo", codEmpresaGrupo);
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("porcentaje", " desc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130409
	@Override
	public List<Accionista> getListaAccionistas(Programa oprograma) throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", oprograma.getId());
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<Accionista> getListaAccionistas(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", oprograma.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("pos", " asc");
		List<Accionista> listatemp = null;
		try {
			listatemp = findByParamsOrder(Accionista.class, params,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130221
	
	public List<CabTabla> getListaCabTabla() {
		return listaCabTabla;
	}

	public void setListaCabTabla(List<CabTabla> listaCabTabla) {
		this.listaCabTabla = listaCabTabla;
	}
	
	//mcg 20111024
	@Override
	public List<CabTabla> getListaCabTablaByTipoTabla(Long Idprograma,Long idTipoTabla) throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		Tabla tipoTabla = null;		
		tipoTabla = new Tabla();
		tipoTabla.setId(idTipoTabla);		
		params.put("programa", Idprograma);
		params.put("tipoTabla", tipoTabla.getId());
		
		List<CabTabla> listatemp = null;
		try {
			listatemp = findByParams2(CabTabla.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//
	@Override
	public List<CompraVenta> getListaCompraVenta() throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", programa.getId());
		List<CompraVenta> listatemp = null;
		try {
			listatemp = findByParams2(CompraVenta.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130214	
	@Override
	public List<CompraVenta> getListaCompraVenta(String codEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codEmpresaGrupo", codEmpresaGrupo);
		List<CompraVenta> listatemp = null;
		try {
			listatemp = findByParams2(CompraVenta.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130214
	@Override
	public List<CompraVenta> getListaCompraVenta(Programa oprograma) throws BOException {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", oprograma.getId());
		List<CompraVenta> listatemp = null;
		try {
			listatemp = findByParams2(CompraVenta.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20120221
	@Override
	public List<CompraVenta> getListaCompraVenta(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", oprograma.getId());
		params.put("codEmpresaGrupo", CodEmpresaGrupo);
		List<CompraVenta> listatemp = null;
		try {
			listatemp = findByParams2(CompraVenta.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20120221
	@Override
	public List<NegocioBeneficio> getListaNBActividades() throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_ACTIVIDAD));
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130215
	@Override
	public List<NegocioBeneficio> getListaNBActividades(String codEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_ACTIVIDAD));
		params.add(codEmpresaGrupo);
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130215
	@Override
	public List<NegocioBeneficio> getListaNBActividades(Programa oprograma) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_ACTIVIDAD));
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//ini MCG20130221
	@Override
	public List<NegocioBeneficio> getListaNBActividades(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_ACTIVIDAD));
		params.add(CodEmpresaGrupo);
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130221
	@Override
	public List<NegocioBeneficio> getListaNBNegocio() throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_NEGOCIO));
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130215
	@Override
	public List<NegocioBeneficio> getListaNBNegocio(String codEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_NEGOCIO));
		params.add(codEmpresaGrupo);
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130215
	@Override
	public List<NegocioBeneficio> getListaNBNegocio(Programa oprograma) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_NEGOCIO));
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<NegocioBeneficio> getListaNBNegocio(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(Integer.parseInt(Constantes.POR_LINEA_NEGOCIO));
		params.add(CodEmpresaGrupo);
		List<NegocioBeneficio> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findNegocioBeneficioByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130221
	@Override
	public List<Participaciones> getListaParticipaciones() throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(programa.getId());
		List<Participaciones> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findParticipacionesByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130214
	@Override
	public List<Participaciones> getListaParticipaciones(String codEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());	
		params.add(codEmpresaGrupo);	
		List<Participaciones> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findParticipacionesByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130214
	@Override
	public List<Participaciones> getListaParticipaciones(Programa oprograma) throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(oprograma.getId());
		List<Participaciones> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findParticipacionesByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<Participaciones> getListaParticipaciones(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(CodEmpresaGrupo);	
		List<Participaciones> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findParticipacionesByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130221
	@Override
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos()
			throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(programa.getId());
		List<PrincipalesEjecutivos> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findPrinEjecutivosByPrograma", params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130220
	@Override
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(String codEmpresaGrupo)
			throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(codEmpresaGrupo);
		List<PrincipalesEjecutivos> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findPrinEjecutivosByProgramaCodEmpGrup", params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130220
	@Override
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(Programa oprograma)
	throws BOException {
	List<Long> params = new ArrayList<Long>();
	params.add(oprograma.getId());
	List<PrincipalesEjecutivos> listatemp = null;
	try {
		listatemp = super.executeListNamedQuery("findPrinEjecutivosByPrograma", params);
	} catch (BOException e) {
		throw new BOException(e);
	}
	return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(Programa oprograma,String CodEmpresaGrupo)	throws BOException {
	List<Object> params = new ArrayList<Object>();
	params.add(oprograma.getId());
	params.add(CodEmpresaGrupo);
	List<PrincipalesEjecutivos> listatemp = null;
	try {
		listatemp = super.executeListNamedQuery("findPrinEjecutivosByProgramaCodEmpGrup", params);
	} catch (BOException e) {
		throw new BOException(e);
	}
	return listatemp;
	}
	//fin MCG20130221
	@Override
	public List<RatingExterno> getListaRatingExterno() throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(programa.getId());
		List<RatingExterno> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findRatingExternoByPrograma",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//ini MCG20130214
	@Override
	public List<RatingExterno> getListaRatingExterno(String codEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(programa.getId());
		params.add(codEmpresaGrupo);
		List<RatingExterno> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findRatingExternoByProgramaCodEmpGrup",params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130214
	
	@Override
	public List<RatingExterno> getListaRatingExterno(Programa oprograma) throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(oprograma.getId());
		List<RatingExterno> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findRatingExternoByPrograma",params);
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130221
	@Override
	public List<RatingExterno> getListaRatingExterno(Programa oprograma,String CodEmpresaGrupo) throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(CodEmpresaGrupo);
		List<RatingExterno> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery("findRatingExternoByProgramaCodEmpGrup",params);
			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//ini MCG20130221
	
	
	//ini MCG20120222
	@Override
	public void ActualizarDatosBasicosByPrograma(Programa oprograma,List<Empresa> listaEmpresasGrupofin) throws BOException{
		try {	
			
			List<Planilla> oListaPlanillaDelete  = new ArrayList<Planilla>();
			List<Planilla> oListaPlanilla = getListaPlanilla(oprograma);			
			if (oListaPlanilla!=null && oListaPlanilla.size()>0){
				for (Planilla oPlanilla: oListaPlanilla){	
					String codempre=oPlanilla.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){						
						oListaPlanillaDelete.add(oPlanilla);
					}				
				}	
				deleteCollection(oListaPlanillaDelete);
			}	
			
			List<Accionista> listaAccionistaCopia=new ArrayList<Accionista>();
			List<Accionista> olistaAccionistas=getListaAccionistas(oprograma);
			if (olistaAccionistas!=null && olistaAccionistas.size()>0){
				for (Accionista oaccionista: olistaAccionistas){
					String codempre=oaccionista.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){						
						listaAccionistaCopia.add(oaccionista);
					}
				}	
				deleteCollection(listaAccionistaCopia);
			}	
			
			//ini MCG20130822
			List<CapitalizacionBursatil> listaCapitalizacionCopia=new ArrayList<CapitalizacionBursatil>();
			List<CapitalizacionBursatil> olistaCapitalizacion=getListaCapitalizacionBursatil(oprograma);
			if (olistaCapitalizacion!=null && olistaCapitalizacion.size()>0){
				for (CapitalizacionBursatil ocapitalizacionBursatil: olistaCapitalizacion){
					String codempre=ocapitalizacionBursatil.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){						
						listaCapitalizacionCopia.add(ocapitalizacionBursatil);
					}
				}	
				deleteCollection(listaCapitalizacionCopia);
			}
			
			//fin MCG20130822
			
			List<PrincipalesEjecutivos> listaPrincipalesEjecutivosCopia=new ArrayList<PrincipalesEjecutivos>();
			List<PrincipalesEjecutivos> olistaPrincipalesEjecutivos=getListaPrinciEjecutivos(oprograma);
			if (olistaPrincipalesEjecutivos!=null && olistaPrincipalesEjecutivos.size()>0){
				for (PrincipalesEjecutivos oprincipalesEjecutivos: olistaPrincipalesEjecutivos){
					String codempre=oprincipalesEjecutivos.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){					
						listaPrincipalesEjecutivosCopia.add(oprincipalesEjecutivos);
					}
				}	
				deleteCollection(listaPrincipalesEjecutivosCopia);
			}
			
			List<RatingExterno> listaRatingExternoCopia=new ArrayList<RatingExterno>();
			List<RatingExterno> olistaRatingExterno=getListaRatingExterno(oprograma);
			if (olistaRatingExterno!=null && olistaRatingExterno.size()>0){
				for (RatingExterno oratingExterno: olistaRatingExterno){
					String codempre=oratingExterno.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){	
						listaRatingExternoCopia.add(oratingExterno);
					}
				}	
				deleteCollection(listaRatingExternoCopia);
			}	
			
			List<NegocioBeneficio> listaNBActividadesCopia=new ArrayList<NegocioBeneficio>();
			List<NegocioBeneficio> olistaNBActividades=getListaNBActividades(oprograma);
			if (olistaNBActividades!=null && olistaNBActividades.size()>0){				
				for (NegocioBeneficio oNBActividades: olistaNBActividades){						
					String codempre=oNBActividades.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){
						listaNBActividadesCopia.add(oNBActividades);
					}
				}
				deleteCollection(listaNBActividadesCopia);
			}
			
			List<NegocioBeneficio> listaNBNegocioCopia=new ArrayList<NegocioBeneficio>();
			List<NegocioBeneficio> olistaNBNegocio=getListaNBNegocio(oprograma);
			if (olistaNBNegocio!=null && olistaNBNegocio.size()>0){
				for (NegocioBeneficio oNBNegocio: olistaNBNegocio){							
					String codempre=oNBNegocio.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){	
						listaNBNegocioCopia.add(oNBNegocio);
					}
				}	
				deleteCollection(listaNBNegocioCopia);
			}
			
			List<Participaciones> listaParticipacionesCopia=new ArrayList<Participaciones>();
			List<Participaciones> olistaParticipaciones=getListaParticipaciones(oprograma);
			if (olistaParticipaciones!=null && olistaParticipaciones.size()>0){
				for (Participaciones oparticipaciones: olistaParticipaciones){
					String codempre=oparticipaciones.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){
						listaParticipacionesCopia.add(oparticipaciones);
					}
				}	
				deleteCollection(listaParticipacionesCopia);
			}
			
			List<CompraVenta> listaCompraVentaCopia=new ArrayList<CompraVenta>();
			List<CompraVenta> olistaCompraVenta=getListaCompraVenta(oprograma);
			if (olistaCompraVenta!=null && olistaCompraVenta.size()>0){	
		
				for (CompraVenta ocompraVenta: olistaCompraVenta){							
					String codempre=ocompraVenta.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){
						listaCompraVentaCopia.add(ocompraVenta);
					}
				}	
				deleteCollection(listaCompraVentaCopia);
			}
			List<DatosBasicoBlob> olistadatosBasicoBlobDelete =new ArrayList<DatosBasicoBlob>();
			List<DatosBasicoBlob> olistadatosBasicoBlob= datosBasicosBlobBO.listaDatosBasicoBlobByPrograma(oprograma);
			if (olistadatosBasicoBlob!=null && olistadatosBasicoBlob.size()>0){
				for (DatosBasicoBlob odatosBasicoBlob: olistadatosBasicoBlob){
					String codigoEmp=odatosBasicoBlob.getCodigoEmpresa();
					if (!validarEmpresaAGuardar(codigoEmp,listaEmpresasGrupofin)){
						olistadatosBasicoBlobDelete.add(odatosBasicoBlob);
					}
				}
				deleteCollection(olistadatosBasicoBlobDelete);
			}			
			
									
		} catch (BOException  e) {
			throw new BOException(e);
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
	
	
	//ini CCA
	
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
	
	
	//fin CCA
	//fin MCG20120222
	@Override
	public Accionista obtenerAccionista(Accionista oaccionista,String strUsuarioHost) throws BOException{
		HashMap<String,String> datosAccionista = null;	
		Accionista accionista=new Accionista();
		
		try {
			
		String tipoDocumento=oaccionista.getTipoDocumento().getAbreviado();		
		if(oaccionista.getNumeroDocumento() != null &&  !oaccionista.getNumeroDocumento().trim().equals("")){			
			
			datosAccionista = QueryWS.consularDatosAccionista(oaccionista.getNumeroDocumento(),
					tipoDocumento,
					strUsuarioHost,
					getConfiguracionWSPE21(strUsuarioHost));
			
			accionista.setNumeroDocumento(oaccionista.getNumeroDocumento());
			accionista.setTipoDocumento(oaccionista.getTipoDocumento());
			
				accionista.setRuc(datosAccionista.get("ruc"));
				accionista.setCodigoCentral(datosAccionista.get("codigoCentral"));
				accionista.setNombre(datosAccionista.get("nombreEmpesa"));
				accionista.setControl(datosAccionista.get("codError"));	
				accionista.setTipoNumeroDocumentoHost(datosAccionista.get("tipNumDocumento"));
				String antiguedadNegocio = FechaUtil.getNumAnios(datosAccionista.get("fechaNacimiento"));// datosBasicos[6]
				
					if (antiguedadNegocio != null
							&& !antiguedadNegocio.trim().equals("")) {
						accionista.setEdad(antiguedadNegocio);
					} else {
						accionista.setEdad("");
					}
		
		}
			return accionista;
		
		} catch (WSException e) {
			return accionista=new Accionista();
		}
		
	}

	//ini MCG20140616
	
	
	//fin MCG20140616
	
	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
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


	public List<Empresa> getListaGrupoEmpresas() {
		return listaGrupoEmpresas;
	}


	public void setListaGrupoEmpresas(List<Empresa> listaGrupoEmpresas) {
		this.listaGrupoEmpresas = listaGrupoEmpresas;
	}


	public List<DatosBasico> getListaDatosBasicoGrupoEmpresas() {
		return listaDatosBasicoGrupoEmpresas;
	}


	public void setListaDatosBasicoGrupoEmpresas(
			List<DatosBasico> listaDatosBasicoGrupoEmpresas) {
		this.listaDatosBasicoGrupoEmpresas = listaDatosBasicoGrupoEmpresas;
	}


	public String getPathWebService() {
		return pathWebService;
	}


	public void setPathWebService(String pathWebService) {
		this.pathWebService = pathWebService;
	}


	public ConfiguracionWS getConfiguracionWS() {
		return configuracionWS;
	}


	public void setConfiguracionWS(ConfiguracionWS configuracionWS) {
		this.configuracionWS = configuracionWS;
	}

	

}
