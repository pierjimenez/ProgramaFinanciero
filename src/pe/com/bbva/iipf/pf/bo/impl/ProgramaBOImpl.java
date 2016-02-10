package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.dao.ProgramaDAO;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSPe21;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.grupobbva.pf.pec6.InputPec6;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.StringUtil;


/**
 * 
 * @author epomayay
 *
 */
@Service("programaBO")
public class ProgramaBOImpl extends GenericBOImpl<Programa> implements ProgramaBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	ProgramaDAO programaDAO;
	
	
	
	@Resource
	EmpresaBO empresaBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();
	private List<DatosBasico> listaDatosBasicoGrupoEmpresas = new ArrayList<DatosBasico>();
	
	private String pathWebService;

	
	private String pathWebServicePEC6;
	
	
	private ConfiguracionWS configuracionWS;
	
	@Override
	public Programa findById(Long id) throws BOException {
		Programa programa = super.findById(Programa.class, id);
		return programa;
	}
	
	/**
	 * la lista de listaGrupoEmpresas contiene a las empresas principal, segundarias y anexas
	 */
	@Override
	public void beforeSave(Programa t) throws BOException{
		//registro de los campos de auditoria
		super.beforeSave(t);
		String anio = FechaUtil.formatFecha(new Date(), "yyyy");
		Tabla estadoPrograma = new Tabla();
		estadoPrograma.setId(Constantes.ID_ESTADO_PROGRAMA_PENDIENTE);
		t.setEstadoPrograma(estadoPrograma);
		t.setAnio(Integer.valueOf(anio));
		HashMap<String,String> datosBasicos = null;
		HashMap<String,String> datosBasicostmp = null;
		HashMap<String,String> datosBasicosNoPrincip = null;
		List<DatosBasico> listDatosBasico=new ArrayList<DatosBasico>();
		
		HashMap<String,String> datosBasicosRC = null;
		HashMap<String,String> datosBasicosRC1 = null;
		
		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";
		
		try {
			//En el caso de grupo se toma a la empresa principal
			//como dato base 
			//definir el codigo de la empresa principal en 
			//el programa
			//ini Configuracion
			String codigoUsuario=getUsuarioSession().getRegistroHost();
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			
			String tipDocRuc = GenericAction.getObjectParamtrosSession(Constantes.EQUIV_RUC).toString();
			if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
				for(Empresa acctemp : listaGrupoEmpresas){
					DatosBasico datosBasicoEmpresa=new DatosBasico();
					try{
						datosBasicostmp = QueryWS.consularDatosBasicos(acctemp.getCodigo()
								, VAL_TIPO_DOC_COD_CENTRAL,
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
					}
					
					if(acctemp.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
						t.setIdEmpresa(acctemp.getCodigo());	
						t.setRuc(acctemp.getRuc());
						//t.setNombreGrupoEmpresa(acctemp.getNombre());
						//se consulta los datos basicos de la empresa principal
						datosBasicos=datosBasicostmp;						
					}	
					// ini MCG20130218
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
						String buro1="";
						try {
							 buro1 = QueryWS.consultarGrupoBuro(acctemp
									.getRuc(),tipDocRuc, getUsuarioSession()
									.getRegistroHost(), getPathWebService());
						} catch (Exception e) {
							buro1="";
						}
						
						if (buro1 != null) {
							datosBasicoEmpresa.setGrupoRiesgoBuro(buro1);
						}
					}
					try {
						String urlRig4 = GenericAction.getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
						datosBasicosRC1 = ConsultaWS.consularDatosReporteCredito(acctemp.getCodigo(), getConfiguracionWS(),urlRig4);
						if (datosBasicosRC1 != null && datosBasicosRC1.size()>0) {
							String separador1="";
							String separador2="";
							String separador3="";
							if (datosBasicosRC1.get("codRegistroGestor")!=null &&!datosBasicosRC1.get("codRegistroGestor").equals("") ){separador1=" - ";}
							if (datosBasicosRC1.get("codOficinaPrincipal")!=null && !datosBasicosRC1.get("codOficinaPrincipal").equals("") ){separador2=" - ";}
							if (datosBasicosRC1.get("codEtiqueta")!=null && !datosBasicosRC1.get("codEtiqueta").equals("") ){separador3=" - ";}		
							
							datosBasicoEmpresa.setCalificacionBanco(datosBasicosRC1.get("clasificacionBanco")==null?"":datosBasicosRC1.get("clasificacionBanco"));
							datosBasicoEmpresa.setGestor((datosBasicosRC1.get("codRegistroGestor")==null?"":datosBasicosRC1.get("codRegistroGestor") )+separador1+ (datosBasicosRC1.get("nombreGestor")==null?"":datosBasicosRC1.get("nombreGestor")));
							datosBasicoEmpresa.setOficina((datosBasicosRC1.get("codOficinaPrincipal")==null?"":datosBasicosRC1.get("codOficinaPrincipal")) +separador2+ (datosBasicosRC1.get("descOficinaPrincipal")==null?"": datosBasicosRC1.get("descOficinaPrincipal")));
							datosBasicoEmpresa.setSegmento((datosBasicosRC1.get("codEtiqueta")==null?"":datosBasicosRC1.get("codEtiqueta")) +separador3+ (datosBasicosRC1.get("descEtiqueta")==null?"":datosBasicosRC1.get("descEtiqueta")) );
							
						}
					} catch (Exception e) {

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
						if (!datosBasicos.get("ruc").trim().equals("")){
							t.setRuc(datosBasicos.get("ruc"));
						}

				}else{					
					datosBasicos = QueryWS.consularDatosBasicos(t.getRuc(),
							VAL_TIPO_DOC_RUC,
							getUsuarioSession().getRegistroHost(),
							oConfiguracionWSPe21);
					if (!datosBasicos.get("codigoCentral").trim().equals("")){
						t.setIdEmpresa(datosBasicos.get("codigoCentral"));
					}
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
				//23/05/2012 EDWIN POMAYAY
				//if(buro != null && !buro.trim().equals("")){
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

			}				
			//fin MCG20130401
			
			/* 
			 * para calcular la antiguedad del cliente y antiguedad del negocio 
			 * con canda una de las fechas retornadas por HOST.
			 * Seguiendo las indicaciones del documento P036 v1.1
			 */
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
			
			//INI MCG20121030
			if (t.getId()==null){				
				t.setNumeroSolicitud(generarNumeroSolicitud());				
			}
			//FIN MCG20121030
			
		} catch (WSException e) {
			throw new BOException(e.getMessage());
		}
	}
	@Override
	public boolean validate(Programa t) throws BOException{
		logger.info("Validando Registro de Programa");
		List<String> parametros = new ArrayList<String>();
		List resEstPent = null;
		if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){
			
			if (t.getActivoValida()==null){	
				parametros.add(t.getIdEmpresa());
				resEstPent = super.executeListNamedQuery("buscarProgramaEmprEstPend", parametros);
				if(resEstPent!= null && 
				   !resEstPent.isEmpty()){
					throw new BOException("La Empresa tiene un programa creado en estado PENDIENTE");
				}
			}			
			
		}else{
			if(getListaGrupoEmpresas().size()>30){
				throw new BOException("El sistema puede trabajar hasta un máximo de 30 empresas.");
			}
			if (t.getActivoValida()==null){				
			
				parametros.add(t.getIdGrupo());
				resEstPent = super.executeListNamedQuery("buscarProgramaGrupEstPend", parametros);	
				if(resEstPent!= null && 
				   !resEstPent.isEmpty()){
					throw new BOException("El Grupo tiene un programa creado en estado PENDIENTE");
				}
			}
			
			//ini MCG20140904 cambio de ubicacion solo para grupo
			TreeSet<String> listaDuplicados  = new TreeSet<String>();
			for(Empresa emp : listaGrupoEmpresas){
				List<Empresa> listaTemporal = listaGrupoEmpresas;
				int  pos =0;
				for(Empresa temp : listaTemporal){
					if(emp.getCodigo().equals(temp.getCodigo()) && 
					   !emp.getTipoGrupo().getId().equals(temp.getTipoGrupo().getId())){
						listaDuplicados.add(temp.getNombre());
						//listaTemporal.remove(pos);
					}
					pos++;
				}
			}
			if(listaDuplicados.size()>0){
				throw new BOException("La(s) siguiente(s) empresa(s): " + listaDuplicados + " se encuentran en más de una selección");
			}
			//fin MCG20140904			
			
		}
		
	
		if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
			if(listaGrupoEmpresas.isEmpty()){
				throw new BOException("Selecccione por lo menos una empresa para el Grupo.");
			}
		}
	
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Programa t) throws BOException{
		try{
			//falta registrar el estado pendiente o cerrado
			
			super.save(t);
			if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
				saveUDListaEmpresas(t);	
			}else{
				//Se registra la empresa en la tabla TIIPF_EMPRESA
				//esto servira para tener el dato de la empresa en 
				//Estructura de limites
				Empresa empresa = new Empresa();
				empresa.setCodigo(t.getIdEmpresa());
				empresa.setPrograma(t);
				empresa.setRuc(t.getRuc());
				empresa.setNombre(t.getNombreGrupoEmpresa());
				List<Empresa> lista = new ArrayList<Empresa>();
				lista.add(empresa);
				saveCollection(lista);
			}
			if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
				saveUDListaDatosBasicoEmpresas(t);	
			}
			
		}catch(BOException e){
			throw new BOException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarClasificacion(Programa t) throws BOException{
		try{
			List parametros = new ArrayList();
			parametros.add(t.getCalificacionBanco());
			parametros.add(t.getCuotaFinanciera());			
			parametros.add(t.getId());
			super.executeNamedQuery("updateClasificacionBancariaCuotaFinanciera", parametros);
		}catch(BOException e){
			throw new BOException(e.getMessage());
		}
	}
	
	//INI MCG20121030
	
	public String generarNumeroSolicitud()throws BOException {
		String numeroSolicitud = "";		
		try {
			numeroSolicitud = programaDAO.generarNumeroSolicitud();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		return numeroSolicitud;
	}
	//FIN MCG20121030

	public void beforeUDListaEmpresas(Programa t){
		for(Empresa acctemp : listaGrupoEmpresas){
			acctemp.setPrograma(t);
		}
	}
	
	public void beforeUDListaDatosBasicoEmpresas(Programa t){
		for(DatosBasico dbtemp : listaDatosBasicoGrupoEmpresas){
			String codemp=dbtemp.getCodigoEmpresa();
			dbtemp.setPrograma(t);			
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("programa", t.getId());
			parametros.put("codigoEmpresa", codemp);
			try {
				List<DatosBasico> olista =  super.findByParams2(DatosBasico.class, parametros);	
				if(olista!=null && olista.size()>0 && !olista.isEmpty()){
					dbtemp.setId(olista.get(0).getId());
					dbtemp.setActividadPrincipal(olista.get(0).getActividadPrincipal());
					
					dbtemp.setComentAccionariado(olista.get(0).getComentAccionariado());
					dbtemp.setComentPartiSignificativa(olista.get(0).getComentPartiSignificativa());
					dbtemp.setComentRatinExterno(olista.get(0).getComentRatinExterno());
					dbtemp.setComentvaloraGlobal(olista.get(0).getComentvaloraGlobal());										
				}
			} catch (Exception e) {
				 
			}			
		}		
	}
	
	public boolean validateUDListaEmpresas(){
		return true;
	}
	public boolean validateUDListaDatosBasicoEmpresas(){
		return true;
	}
	
	public void saveUDListaEmpresas(Programa t) throws BOException{
		beforeUDListaEmpresas( t);
		if(validateUDListaEmpresas()){
			try {
				HashMap<String,Long> params = new HashMap<String,Long>();
				params.put("programa", t.getId());
				List<Empresa> listatemp =  findByParams2(Empresa.class, params);
				List<Empresa> listaDel = new ArrayList<Empresa>();
				boolean flag= false;
				for(Empresa acc : listatemp ){
					for(Empresa acctemp : listaGrupoEmpresas){
						if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(acc);
					}
					flag= false;
				}
				saveCollection(listaGrupoEmpresas);
				deleteCollection(listaDel);
			} catch (BOException e) {
				throw new BOException(e);
			}
		}
	}
	
	//ini MCG20130218
	public void saveUDListaDatosBasicoEmpresas(Programa t) throws BOException{
		beforeUDListaDatosBasicoEmpresas( t);
		if(validateUDListaDatosBasicoEmpresas()){
			try {
				HashMap<String,Long> params = new HashMap<String,Long>();
				params.put("programa", t.getId());
				List<DatosBasico> listatemp =  findByParams2(DatosBasico.class, params);
				List<DatosBasico> listaDel = new ArrayList<DatosBasico>();
				boolean flag= false;
				for(DatosBasico acc : listatemp ){
					for(DatosBasico acctemp : listaDatosBasicoGrupoEmpresas){
						if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(acc);
					}
					flag= false;
				}
				saveCollection(listaDatosBasicoGrupoEmpresas);
				deleteCollection(listaDel);
			} catch (BOException e) {
				throw new BOException(e);
			}
		}
	}
	//ini MCG20130218
	@Override
	public List<Empresa> findEmpresaByIdprogramaRuc(String Idprograma,String ruc) throws BOException   	{
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("programa", Idprograma);
		params.put("ruc", ruc);
		List<Empresa> listatemp = null;
		
		try {	
			listatemp = findByParams2(Empresa.class, params); 
						
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public Empresa findEmpresaByIdEmpresa(Long idEmpresa) throws BOException   	{
		HashMap<String,Long> params = new HashMap<String,Long>();		
		params.put("id", idEmpresa);
		List<Empresa> listatemp = null;
		Empresa oempresa=null;
		
		try {	
			listatemp = findByParams2(Empresa.class, params); 
			if (listatemp!=null && listatemp.size()>0) {
				oempresa = listatemp.get(0);
			}
						
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return oempresa;
	}
	@Override
	public Empresa findEmpresaByIdEmpresaPrograma(Long idPrograma , String codigoEmpresa) throws BOException   	{
		HashMap<String,Object> params = new HashMap<String,Object>();		
		params.put("programa", idPrograma);
		params.put("codigo", codigoEmpresa);
		List<Empresa> listatemp = null;
		Empresa oempresa=null;
		
		try {	
			listatemp = findByParams2(Empresa.class, params); 
			if (listatemp!=null && listatemp.size()>0) {
				oempresa = listatemp.get(0);
			}
						
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return oempresa;
	}
	
	
	@Override
	public List<Empresa> findEmpresaByIdprograma(Long Idprograma) throws BOException   	{
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", Idprograma);		
		List<Empresa> listatemp = null;
		
		try {	
			listatemp = findByParams2(Empresa.class, params); 
						
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	
	public List<Empresa> getListaGrupoEmpresas() {
		return listaGrupoEmpresas;
	}

	public void setListaGrupoEmpresas(List<Empresa> listaGrupoEmpresas) {
		this.listaGrupoEmpresas = listaGrupoEmpresas;
	}
	@Override
	public List<Programa> listarProgramas(String tipoEmpresa, 
										  String tipoDocumento, 
										  String codigo,
										  Date fechaInicio,
										  Date fechaFin,
										  String idEstadoPrograma) throws BOException {
		
		List<Programa> programas = null;
		Programa datoPrograma = null;
		if(fechaInicio != null &&
		   fechaFin != null &&
		   FechaUtil.compareDate(fechaInicio, fechaFin)<0){
			throw new BOException("Rango de fechas Incorrecto");
		}
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select pro.id_programa, to_char(pro.fecha_creacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("to_char(pro.fecha_modificacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("pro.db_nombre_grup_empr, pro.cod_usuario_creacion, ");
			sb.append("pro.tt_id_estado_programa, tab.descripcion as  estado_descrip,pro.numero_solicitud ");
			sb.append("from profin.tiipf_programa pro inner join profin.tiipf_tabla_de_tabla tab ");
			sb.append("on pro.tt_id_estado_programa = tab.id_tabla ");
			sb.append("where ");
																	
			if(tipoEmpresa.equals("2"))//Empresa
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_EMPR);
				
				if(tipoDocumento.equals("2"))//RUC
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" DB_RUC like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("3"))//CODIGO CENTRAL
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" ID_EMPRESA like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("4"))//NOMBRE EMPRESA O GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumento.equals("5"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
			}
			else if(tipoEmpresa.equals("3"))//GRUPO
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_GRUPO);
				
				if(codigo != null && !codigo.trim().equals(""))
				{
					sb.append(" and (");
					sb.append(" upper(ID_GRUPO) like upper('%");
					sb.append(codigo);
					sb.append("%')");
					
					sb.append(" or ");
					sb.append(" NUMERO_SOLICITUD like '%");
					sb.append(codigo);
					sb.append("%')");
				}
			}
			if(fechaInicio != null){
				if(fechaFin == null){
					throw new BOException("Ingrese la fecha final");
				}
				sb.append(" AND pro.FECHA_CREACION BETWEEN to_date('" +
						FechaUtil.formatFecha(fechaInicio, "dd/MM/yyyy") +
						"','DD/MM/YYYY') AND to_date('" +
						FechaUtil.formatFecha(fechaFin, "dd/MM/yyyy") +
						"','DD/MM/YYYY')");
			}
			if(!idEstadoPrograma.equals("")){
				sb.append(" AND pro.tt_id_estado_programa = "+idEstadoPrograma);
			}
			sb.append(" order by pro.FECHA_CREACION desc");
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			programas = new ArrayList<Programa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				datoPrograma = new Programa();
				
				datoPrograma.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					datoPrograma.setFechaCreacionFormato(amount[1].toString());
				if(amount[2] != null)
					datoPrograma.setFechaModificacionFormato(amount[2].toString());
				if(amount[3] != null)
					datoPrograma.setNombreGrupoEmpresa(amount[3].toString());
				if(amount[4] != null)
					datoPrograma.setCodUsuarioCreacion(amount[4].toString());
				if(amount[5] != null && amount[6] != null)
				{
					Tabla estadoPrograma = new Tabla();
					estadoPrograma.setId(new Long(amount[5].toString()));
					estadoPrograma.setDescripcion(amount[6].toString());					
					datoPrograma.setEstadoPrograma(estadoPrograma);
				}
				if(amount[7] != null){
					datoPrograma.setNumeroSolicitud(amount[7]==null?"":amount[7].toString());
				}
											
				programas.add(datoPrograma);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return programas;
		
	}
	
	//ini MCG20121203
	@Override
	public List<Programa> listarProgramas(String tipoEmpresa, 
			  String tipoDocumento, 
			  String codigo,
			  Date fechaInicio,
			  Date fechaFin,
			  String idEstadoPrograma,
			  String tipoDocumentogrupo) throws BOException {

		List<Programa> programas = null;
		Programa datoPrograma = null;
		if(fechaInicio != null &&
		   fechaFin != null &&
		   FechaUtil.compareDate(fechaInicio, fechaFin)<0){
			throw new BOException("Rango de fechas Incorrecto");
		}
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select pro.id_programa, to_char(pro.fecha_creacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("to_char(pro.fecha_modificacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("pro.db_nombre_grup_empr, pro.cod_usuario_creacion, ");
			sb.append("pro.tt_id_estado_programa, tab.descripcion as  estado_descrip,pro.numero_solicitud, ");
			sb.append("pro.tt_id_motivo_cierre, tabmotivo.descripcion as  motivo,pro.observacion_cierre,pro.fecha_cierre,pro.cod_usuario_cierre ");			
			sb.append("from profin.tiipf_programa pro inner join profin.tiipf_tabla_de_tabla tab ");
			sb.append("on pro.tt_id_estado_programa = tab.id_tabla ");
			sb.append("left join  profin.tiipf_tabla_de_tabla tabmotivo ");
			sb.append("on pro.tt_id_motivo_cierre = tabmotivo.id_tabla ");
			sb.append("where ");
																	
			if(tipoEmpresa.equals("2"))//Empresa
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_EMPR);
				
				if(tipoDocumento.equals("2"))//RUC
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" DB_RUC like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("3"))//CODIGO CENTRAL
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" ID_EMPRESA like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("4"))//NOMBRE EMPRESA O GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumento.equals("5"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
			}
			else if(tipoEmpresa.equals("3"))//GRUPO
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_GRUPO);
				//ini MCG20121203
//				if(codigo != null && !codigo.trim().equals(""))
//				{
//					sb.append(" and (");
//					sb.append(" upper(ID_GRUPO) like upper('%");
//					sb.append(codigo);
//					sb.append("%')");
//					
//					sb.append(" or ");
//					sb.append(" NUMERO_SOLICITUD like '%");
//					sb.append(codigo);
//					sb.append("%')");
//				}				
				
				if(tipoDocumentogrupo.equals("2"))//CODIGO GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper(ID_GRUPO) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumentogrupo.equals("3"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%' ");
					}
				}
				else if(tipoDocumentogrupo.equals("4"))//NOMBRE GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				//fin MCG20121203				
				
				
			}
			if(fechaInicio != null){
				if(fechaFin == null){
					throw new BOException("Ingrese la fecha final");
				}
				sb.append(" AND pro.FECHA_CREACION BETWEEN to_date('" +
						FechaUtil.formatFecha(fechaInicio, "dd/MM/yyyy") +
						"','DD/MM/YYYY') AND to_date('" +
						FechaUtil.formatFecha(fechaFin, "dd/MM/yyyy") +
						"','DD/MM/YYYY')");
			}
			if(!idEstadoPrograma.equals("")){
				sb.append(" AND pro.tt_id_estado_programa = "+idEstadoPrograma);
			}
			sb.append(" order by pro.FECHA_CREACION desc");
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			programas = new ArrayList<Programa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				datoPrograma = new Programa();
				
				datoPrograma.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					datoPrograma.setFechaCreacionFormato(amount[1].toString());
				if(amount[2] != null)
					datoPrograma.setFechaModificacionFormato(amount[2].toString());
				if(amount[3] != null)
					datoPrograma.setNombreGrupoEmpresa(amount[3].toString());
				if(amount[4] != null)
					datoPrograma.setCodUsuarioCreacion(amount[4].toString());
				if(amount[5] != null && amount[6] != null)
				{
					Tabla estadoPrograma = new Tabla();
					estadoPrograma.setId(new Long(amount[5].toString()));
					estadoPrograma.setDescripcion(amount[6].toString());					
					datoPrograma.setEstadoPrograma(estadoPrograma);
				}
				if(amount[7] != null){
					datoPrograma.setNumeroSolicitud(amount[7]==null?"":amount[7].toString());
				}
				if(amount[8] != null && amount[9] != null)
				{
					Tabla motivoPrograma = new Tabla();
					motivoPrograma.setId(new Long(amount[8].toString()));
					motivoPrograma.setDescripcion(amount[9].toString());					
					datoPrograma.setMotivoCierre(motivoPrograma);
				}
				if(amount[10] != null){  
					datoPrograma.setObservacionCierre(amount[10]==null?"":amount[10].toString().replaceAll("\\r?\\n","<br/>"));
				}
				if(amount[11] != null){
					datoPrograma.setFechaCierre(amount[11]==null?"":amount[11].toString());
					
				}
				if(amount[12] != null){
					datoPrograma.setCodUsuarioCierre(amount[12]==null?"":amount[12].toString());
				}
				
				
											
				programas.add(datoPrograma);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return programas;
	}
	//fin MCG20121203
	
	
	////
	
	//ini MCG20140523
	public List<Programa> listarProgramas_old(String tipoEmpresa, 
			  String tipoDocumento, 
			  String codigo,
			  Date fechaInicio,
			  Date fechaFin,
			  String idEstadoPrograma,
			  String tipoDocumentogrupo,
			  String tipoDocumentoTodos) throws BOException {

		List<Programa> programas = null;
		Programa datoPrograma = null;
		if(fechaInicio != null &&
		   fechaFin != null &&
		   FechaUtil.compareDate(fechaInicio, fechaFin)<0){
			throw new BOException("Rango de fechas Incorrecto");
		}
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select pro.id_programa, to_char(pro.fecha_creacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("to_char(pro.fecha_modificacion,'DD/MM/YYYY HH12:MI:SS PM'), ");
			sb.append("pro.db_nombre_grup_empr, pro.cod_usuario_creacion, ");
			sb.append("pro.tt_id_estado_programa, tab.descripcion as  estado_descrip,pro.numero_solicitud, ");
			sb.append("pro.tt_id_motivo_cierre, tabmotivo.descripcion as  motivo,pro.observacion_cierre,pro.fecha_cierre,pro.cod_usuario_cierre ");			
			sb.append(",profin.PFPKG.FN_ROWCONCAT('SELECT nvl(trim(NOMBRE),''-'') FROM profin.tiipf_empresa WHERE rownum<=10 and iipf_programa_id ='|| pro.id_programa) empresasxgrupo ");
			sb.append("from profin.tiipf_programa pro inner join profin.tiipf_tabla_de_tabla tab ");
			sb.append("on pro.tt_id_estado_programa = tab.id_tabla ");
			sb.append("left join  profin.tiipf_tabla_de_tabla tabmotivo ");
			sb.append("on pro.tt_id_motivo_cierre = tabmotivo.id_tabla ");
			sb.append("where ");
																	
			if(tipoEmpresa.equals("2"))//Empresa
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_EMPR);
				
				if(tipoDocumento.equals("2"))//RUC
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" DB_RUC like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("3"))//CODIGO CENTRAL
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" ID_EMPRESA like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
				else if(tipoDocumento.equals("4"))//NOMBRE EMPRESA O GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumento.equals("5"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%'");
					}
				}
			}
			else if(tipoEmpresa.equals("3"))//GRUPO
			{
				
				sb.append(" TT_ID_TIPO_EMPRESA = ");
				sb.append(Constantes.ID_TIPO_EMPRESA_GRUPO);
			
				
				if(tipoDocumentogrupo.equals("2"))//CODIGO GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper(ID_GRUPO) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumentogrupo.equals("3"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%' ");
					}
				}
				else if(tipoDocumentogrupo.equals("4"))//NOMBRE GRUPO
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}								
			}
			else if(tipoEmpresa.equals("4"))//Todos
			{
				
				sb.append(" 1 = 1 ");
				
				String strQueryEmpresa=" and pro.id_programa in (select distinct iipf_programa_id from profin.tiipf_empresa where 1=1 ";
				
				if(tipoDocumentoTodos.equals("2"))//RUC
				{
					
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(strQueryEmpresa);	
						sb.append(" and ");
						sb.append(" upper(RUC) like upper('%");
						sb.append(codigo);
						sb.append("%') )");
					}
				}
				else if(tipoDocumentoTodos.equals("3"))//Codigo Central
				{		
					
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(strQueryEmpresa);	
						sb.append(" and ");
						sb.append(" upper(CODIGO) like upper('%");
						sb.append(codigo);
						sb.append("%') )");
					}
				}
				else if(tipoDocumentoTodos.equals("4"))//NOMBRE EMPRESA
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(strQueryEmpresa);	
						sb.append(" and ");
						sb.append(" upper( trim(NOMBRE)) like upper('%");
						sb.append(codigo);
						sb.append("%') )");
					}
				}				
				else if(tipoDocumentoTodos.equals("5"))//CODIGO GRUPO
				{
					sb.append(" and TT_ID_TIPO_EMPRESA = ");
					sb.append(Constantes.ID_TIPO_EMPRESA_GRUPO);
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper(ID_GRUPO) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumentoTodos.equals("6"))//NOMBRE GRUPO
				{
					sb.append(" and TT_ID_TIPO_EMPRESA = ");
					sb.append(Constantes.ID_TIPO_EMPRESA_GRUPO);
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" upper( trim(db_nombre_grup_empr)) like upper('%");
						sb.append(codigo);
						sb.append("%') ");
					}
				}
				else if(tipoDocumentoTodos.equals("7"))//NUMERO SOLICITUD
				{
					if(codigo != null && !codigo.trim().equals(""))
					{
						sb.append(" and ");
						sb.append(" NUMERO_SOLICITUD like '%");
						sb.append(codigo);
						sb.append("%' ");
					}
				}
				//fin MCG20121203				
				
				
			}
			if(fechaInicio != null){
				if(fechaFin == null){
					throw new BOException("Ingrese la fecha final");
				}
				sb.append(" AND pro.FECHA_CREACION BETWEEN to_date('" +
						FechaUtil.formatFecha(fechaInicio, "dd/MM/yyyy") +
						"','DD/MM/YYYY') AND to_date('" +
						FechaUtil.formatFecha(fechaFin, "dd/MM/yyyy") +
						"','DD/MM/YYYY')");
			}
			if(!idEstadoPrograma.equals("")){
				sb.append(" AND pro.tt_id_estado_programa = "+idEstadoPrograma);
			}
			sb.append(" order by pro.FECHA_CREACION desc");
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			programas = new ArrayList<Programa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				datoPrograma = new Programa();
				
				datoPrograma.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					datoPrograma.setFechaCreacionFormato(amount[1].toString());
				if(amount[2] != null)
					datoPrograma.setFechaModificacionFormato(amount[2].toString());
				if(amount[3] != null)
					datoPrograma.setNombreGrupoEmpresa(amount[3].toString());
				if(amount[4] != null)
					datoPrograma.setCodUsuarioCreacion(amount[4].toString());
				if(amount[5] != null && amount[6] != null)
				{
					Tabla estadoPrograma = new Tabla();
					estadoPrograma.setId(new Long(amount[5].toString()));
					estadoPrograma.setDescripcion(amount[6].toString());					
					datoPrograma.setEstadoPrograma(estadoPrograma);
				}
				if(amount[7] != null){
					datoPrograma.setNumeroSolicitud(amount[7]==null?"":amount[7].toString());
				}
				if(amount[8] != null && amount[9] != null)
				{
					Tabla motivoPrograma = new Tabla();
					motivoPrograma.setId(new Long(amount[8].toString()));
					motivoPrograma.setDescripcion(amount[9].toString());					
					datoPrograma.setMotivoCierre(motivoPrograma);
				}
				if(amount[10] != null){  
					datoPrograma.setObservacionCierre(amount[10]==null?"":amount[10].toString().replaceAll("\\r?\\n","<br/>"));
				}
				if(amount[11] != null){
					datoPrograma.setFechaCierre(amount[11]==null?"":amount[11].toString());
					
				}
				if(amount[12] != null){
					datoPrograma.setCodUsuarioCierre(amount[12]==null?"":amount[12].toString());
				}
				if(amount[13] != null){
					datoPrograma.setCadEmpresaxGrupo(amount[13]==null?"":amount[13].toString());
				}
				
											
				programas.add(datoPrograma);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return programas;
	}
	//fin MCG20140523
	
	
	//ini MCG20140804
	@Override
	public List<Programa> listarProgramas(String tipoEmpresa, 
			  String tipoDocumento, 
			  String codigo,
			  Date fechaInicio,
			  Date fechaFin,
			  String idEstadoPrograma,
			  String tipoDocumentogrupo,
			  String tipoDocumentoTodos) throws BOException{

		List<Programa> programas = null;
		Programa datoPrograma = null;
		if(fechaInicio != null &&
		   fechaFin != null &&
		   FechaUtil.compareDate(fechaInicio, fechaFin)<0){
			throw new BOException("Rango de fechas Incorrecto");
		}
		try {
			 //--DD/MM/YYYY HH12:MI:SS PM		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select pro.id_programa, to_char(pro.fecha_creacion,'DD/MM/YYYY'), ");
			sb.append("to_char(pro.fecha_modificacion,'DD/MM/YYYY'), ");
			sb.append("pro.db_nombre_grup_empr, pro.cod_usuario_creacion, ");
			sb.append("pro.tt_id_estado_programa, tab.descripcion as  estado_descrip,pro.numero_solicitud, ");
			sb.append("pro.tt_id_motivo_cierre, tabmotivo.descripcion as  motivo,pro.observacion_cierre,substr(pro.fecha_cierre,1,10),pro.cod_usuario_cierre ");			
			sb.append(",profin.PFPKG.FN_ROWCONCAT('SELECT nvl(trim(NOMBRE),''-'') FROM profin.tiipf_empresa WHERE rownum<=10 and iipf_programa_id ='|| pro.id_programa) empresasxgrupo ");
			sb.append(",pro.tt_id_tipo_empresa ");
			sb.append("from profin.tiipf_programa pro inner join profin.tiipf_tabla_de_tabla tab ");
			sb.append("on pro.tt_id_estado_programa = tab.id_tabla ");
			sb.append("left join  profin.tiipf_tabla_de_tabla tabmotivo ");
			sb.append("on pro.tt_id_motivo_cierre = tabmotivo.id_tabla ");
			sb.append("where 1=1 ");
			if(codigo!=null && !codigo.equals("")){
				sb.append("and id_programa in (");			
				sb.append("select id_programa from ");
				sb.append("(");    
				sb.append(" select");
				sb.append("  id_programa");
				sb.append("	,INSTR(decode(tipoEmpresa,'3',id_grupo||'-'||db_nombre_grup_empr||' + '||empresasxgrupo,'2',empresasxgrupo) ||' + '|| ");                      
				sb.append("  numero_solicitud ,'" + codigo.toUpperCase()+ "') INDICADOR ");                   
				sb.append("  from(");     
				sb.append("   select ");
				sb.append("   prog.TT_ID_TIPO_EMPRESA tipoEmpresa,");
				sb.append("   nvl(prog.id_grupo,'-') id_grupo,");
				sb.append("   prog.id_programa, ");
				sb.append("   nvl(prog.db_nombre_grup_empr,'-') db_nombre_grup_empr, ");
				sb.append("   prog.numero_solicitud  ");      
				sb.append("  ,profin.PFPKG.FN_ROWCONCAT('SELECT nvl(trim(CODIGO),''-'')||''-''|| nvl(trim(RUC),''-'')||''-''|| nvl(trim(NOMBRE),''-'') FROM profin.tiipf_empresa WHERE rownum<=30 and iipf_programa_id ='|| prog.id_programa) empresasxgrupo ");
				sb.append("   from profin.tiipf_programa prog");
				sb.append("   ) GEN");
			    sb.append(")FI");
			    sb.append(" WHERE FI.INDICADOR<>0");
			    sb.append(")");
			}
																	

			if(fechaInicio != null){
				if(fechaFin == null){
					throw new BOException("Ingrese la fecha final");
				}
				sb.append(" AND to_char(pro.FECHA_CREACION,'YYYYMMDD') BETWEEN ('" +
						FechaUtil.formatFecha(fechaInicio, "yyyyMMdd") +
						"') AND ('" +
						FechaUtil.formatFecha(fechaFin, "yyyyMMdd") +
						"')");
			}
//			if(!idEstadoPrograma.equals("")){
//				sb.append(" AND pro.tt_id_estado_programa = "+idEstadoPrograma);
//			}
			sb.append(" order by pro.FECHA_CREACION desc");
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			programas = new ArrayList<Programa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				datoPrograma = new Programa();
				
				datoPrograma.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					datoPrograma.setFechaCreacionFormato(amount[1].toString());
				if(amount[2] != null)
					datoPrograma.setFechaModificacionFormato(amount[2].toString());
				if(amount[3] != null)
					datoPrograma.setNombreGrupoEmpresa(amount[3].toString());
				if(amount[4] != null)
					datoPrograma.setCodUsuarioCreacion(amount[4].toString());
				if(amount[5] != null && amount[6] != null)
				{
					Tabla estadoPrograma = new Tabla();
					estadoPrograma.setId(new Long(amount[5].toString()));
					estadoPrograma.setDescripcion(amount[6].toString());					
					datoPrograma.setEstadoPrograma(estadoPrograma);
				}
				if(amount[7] != null){
					datoPrograma.setNumeroSolicitud(amount[7]==null?"":amount[7].toString());
				}
				if(amount[8] != null && amount[9] != null)
				{
					Tabla motivoPrograma = new Tabla();
					motivoPrograma.setId(new Long(amount[8].toString()));
					motivoPrograma.setDescripcion(amount[9].toString());					
					datoPrograma.setMotivoCierre(motivoPrograma);
				}
				if(amount[10] != null){  
					datoPrograma.setObservacionCierre(amount[10]==null?"":amount[10].toString().replaceAll("\\r?\\n","<br/>"));
				}
				if(amount[11] != null){
					datoPrograma.setFechaCierre(amount[11]==null?"":amount[11].toString());
					
				}
				if(amount[12] != null){
					datoPrograma.setCodUsuarioCierre(amount[12]==null?"":amount[12].toString());
				}
				if(amount[13] != null){
					datoPrograma.setCadEmpresaxGrupo(amount[13]==null?"":amount[13].toString());
				}
				if(amount[14] != null)
				{
					Tabla otipoEmpresa = new Tabla();
					String descTipoEmpresa="";
					
					if (amount[14].toString().equals("2")) {
						descTipoEmpresa="EMPRESA";
					}else if (amount[14].toString().equals("3")){
						descTipoEmpresa="GRUPO";
					}else{
						descTipoEmpresa="-";
					}
					otipoEmpresa.setId(new Long(amount[14].toString()));
					otipoEmpresa.setDescripcion(descTipoEmpresa);					
					datoPrograma.setTipoEmpresa(otipoEmpresa);
				}
				
											
				programas.add(datoPrograma);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return programas;
	}
	//fin MCG20140804
	
	////
	
	/**
	 * Metodo para actualizar la fecha de modifacion del programa
	 * este metodo debe ser usado para cualquier tipo de actualizacion de la tabla 
	 * Programa o TIIPF_PROGRAMA TIIPF_PROGRAMA
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarFechaModificacionPrograma(Long idPrograma) throws BOException{
		List parametros = new ArrayList();
		parametros.add(idPrograma);
		try {
			super.executeNamedQuery("updateFechaActualizacion",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarFechaModificacionPrograma(Long idPrograma,String usuarioModicacion) throws BOException{
		List parametros = new ArrayList();		
		parametros.add(usuarioModicacion);
		parametros.add(idPrograma);
		try {
			super.executeNamedQuery("updateFechaUsuarioActualizacion",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	@Override
	public void conluirPrograma(Long idPrograma)throws BOException{
		List parametros = new ArrayList();
		parametros.add(idPrograma);
		try {
			super.executeNamedQuery("concluirPrograma",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	//ini MCG20130704
	@Override
	public void conluirPrograma2(Long idPrograma,Long codMotivo,String observacionCierre, String fechaCierre,String codUsuarioCierre)throws BOException{
		List parametros = new ArrayList();
		parametros.add(codMotivo);
		parametros.add(observacionCierre);
		parametros.add(fechaCierre);
		parametros.add(codUsuarioCierre);
		parametros.add(idPrograma);
		try {
			super.executeNamedQuery("concluirPrograma2",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	//fin MCG20130704
	@Override
	public List<HashMap<String,String>> findByCriterio(String tipoDoc, 
										   String tipoEmpresa, 
										   String codigo) throws BOException {
		StringBuilder stb = new StringBuilder()
		.append(" SELECT DISTINCT  EMPRESAGRUPO  FROM ( " )
		.append(" SELECT  CASE  WHEN  '4'='" +tipoDoc+
				"' THEN TRIM(DB_NOMBRE_GRUP_EMPR)  " )
	    .append(" WHEN  '2'='" + tipoDoc +	"' THEN DB_RUC || '  ' ||TRIM(DB_NOMBRE_GRUP_EMPR)  " )
	    .append(" WHEN  '3'='" + tipoDoc +	"' THEN ID_EMPRESA || '  ' ||TRIM(DB_NOMBRE_GRUP_EMPR)  " )
	    .append(" WHEN  '4'='" + tipoDoc +	"' THEN TRIM(DB_NOMBRE_GRUP_EMPR)  " )
	    .append(" WHEN  '3'='" + tipoDoc +	"' THEN ID_GRUPO || '  ' ||TRIM(DB_NOMBRE_GRUP_EMPR) " ) 
	    .append(" ELSE ''  END EMPRESAGRUPO FROM PROFIN.TIIPF_PROGRAMA " )
	    .append(" WHERE  TT_ID_TIPO_EMPRESA = " + tipoEmpresa +
	    		" AND ( UPPER(ID_GRUPO) LIKE UPPER('" +	codigo + "%') OR " ) 
	    .append(" UPPER(ID_EMPRESA) LIKE UPPER('" +	codigo + "%') OR " )
	    .append(" UPPER(TRIM(DB_NOMBRE_GRUP_EMPR)) LIKE UPPER('%" +	codigo + "%') OR " ) 
	    .append(" UPPER(DB_RUC) LIKE UPPER('" + codigo + "%') ) ) " )
	    .append(" ORDER BY EMPRESAGRUPO");
		logger.info("query = " + stb.toString());
		List<HashMap<String,String>> lista = super.executeSQLtoMap(stb.toString());
		return lista;
	}

	public String getPathWebService() {
		return pathWebService;
	}

	public void setPathWebService(String pathWebService) {
		this.pathWebService = pathWebService;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshEfectividad(String newEfecProm6sol, String newEfecProm6dol, String newProtestoProm6sol, String newProtestoProm6dol , String idprograma) throws BOException {
		
		List parametros = new ArrayList();
		parametros.add(newEfecProm6sol);
		parametros.add(newEfecProm6dol);
		parametros.add(newProtestoProm6sol);
		parametros.add(newProtestoProm6dol);
		parametros.add(Long.parseLong(idprograma));
		try {
			super.executeNamedQuery("updateEfectividadPrograma",parametros);
		} catch (Exception e) {
			throw new BOException(e.getMessage());
		}
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshClasificacionFinaciera(String nor1, String cpp1,	String deff1, String dud1, String per1, String idprograma) throws BOException {
		
		List parametros = new ArrayList();
		parametros.add(nor1);
		parametros.add(cpp1);
		parametros.add(deff1);
		parametros.add(dud1);
		parametros.add(per1);
		parametros.add(Long.parseLong(idprograma));
		
		try {
			super.executeNamedQuery("updateClasificacionFinanciera",parametros);
		} catch (Exception e) {
			throw new BOException(e.getMessage());
		}
	}
	@Override
	public List<Empresa> getEmpresaWS(Long idPrograma){
		List<Empresa> listaEmpresaWS=new ArrayList<Empresa> () ;
		try {
			listaEmpresaWS = empresaBO.listarEmpresasPorPrograma(idPrograma);
		} catch (BOException e) {
			 
			logger.error("getEmpresaWS::"+StringUtil.getStackTrace(e));
		}
		return listaEmpresaWS;
		
	}
	@Override
	public List<Empresa> getEmpresaGrupoWS(String idGrupo,Long idPrograma,TmanagerLog oLog){
		List<Empresa> listaEmpresaWS=new ArrayList<Empresa> () ;
		List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();

		ConfiguracionWSPe21 oConfiguracionWSPe21=new ConfiguracionWSPe21();
		List<Tabla> tablasHijosTD=new ArrayList<Tabla>();
		String VAL_TIPO_DOC_COD_CENTRAL="";
		String VAL_TIPO_DOC_RUC="";

		logger.info("ini getEmpresaGrupoWS");
		try {
			
			String codigoUsuario=getUsuarioSession().getRegistroHost();
			//ini Configuracion
			
			oConfiguracionWSPe21=getConfiguracionWSPE21(codigoUsuario);
			tablasHijosTD=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_TIPO_DOCUMENTO);
			VAL_TIPO_DOC_COD_CENTRAL=obtenerValorHijo(Constantes.COD_HIJO_COD_CODIGO_CENTRAL,tablasHijosTD);
			VAL_TIPO_DOC_RUC=obtenerValorHijo(Constantes.COD_HIJO_COD_RUC,tablasHijosTD);
			//fin configuracion
			HashMap<String,Object> resultado = new HashMap<String,Object>();
			logger.info("QueryWS.consultarGrupoEconomico = idGrupo::" + idGrupo+"codigoUsuario::"+codigoUsuario+"getPathWebServicePEC6::"+getPathWebServicePEC6()+"getPathWebService::"+getPathWebService());
			
			InputPec6 entradaPec6 = new InputPec6();
			entradaPec6.setFlagControl("");
			entradaPec6.setNumeroCliente(idGrupo);
			entradaPec6.setPathWebServicePEC6(getPathWebServicePEC6());
			entradaPec6.setTeclaPulsada(Constantes.TECLA_HOST_CTRL);
			entradaPec6.setUsuario(codigoUsuario);
			
			resultado = QueryWS.consultarGrupoEconomico(entradaPec6,
														oConfiguracionWSPe21,VAL_TIPO_DOC_COD_CENTRAL);
			if(resultado== null){				
				listaEmpresaWS=getEmpresaWS(idPrograma);//por defecto las empresa seleccionada
				oLog.setCodigo(Constantes.COD_ERROR_WS_GETEMPRESA);
				oLog.setEmessage("No se ha podido obtener todas Empresas para el Grupo.");
				logger.info("No se obtuvo empresa para el grupo");
			}else{
				
				if(resultado.get("listaEmpresas")!=null){
					listaEmpresaWS = (List<Empresa>)resultado.get("listaEmpresas");	
				}
					
				if (listaEmpresaWS==null ||( listaEmpresaWS!=null &&listaEmpresaWS.size()==0)){
					listaEmpresaWS=getEmpresaWS(idPrograma);					
				}
			}
		}catch (WSException e) {
			listaEmpresaWS=getEmpresaWS(idPrograma);
			oLog.setCodigo(Constantes.COD_ERROR_WS_GETEMPRESA);
			oLog.setEmessage(e.getMessage());
			logger.error("error::"+e.getMessage());
		} catch (Exception e) {
			listaEmpresaWS=getEmpresaWS(idPrograma);
			oLog.setCodigo(Constantes.COD_ERROR_WS_GETEMPRESA);
			oLog.setEmessage(e.getMessage());
			logger.error("error::"+e.getMessage());
		}
		logger.error("fin getEmpresaGrupoWS");
		return listaEmpresaWS;		
	}
	@Override
	public void actualizarTipoOperacion(Long idPrograma,Long codigotipoOperacion) throws BOException{
		try{
			List parametros = new ArrayList();
			parametros.add(codigotipoOperacion);				
			parametros.add(idPrograma);
			super.executeNamedQuery("updateTipoOperacion", parametros);
		}catch(BOException e){
			throw new BOException(e.getMessage());
		}
	}
	
	//ini MCG20150112
	@Override
	public List<Programa> listarProgramasByCodigoCentral(String codigo,String idprogramaIni ) throws BOException{

		List<Programa> programas = null;
		Programa datoPrograma = null;
		String numeroTop=Constantes.NUMTOPDEFAULT;
		try {
			numeroTop = GenericAction.getObjectParamtrosSession(Constantes.LIMITEPF_COPIA).toString();			
		} catch (Exception e) {
			numeroTop=Constantes.NUMTOPDEFAULT;
		}
			
		try {
			 //--DD/MM/YYYY HH12:MI:SS PM		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select pro.id_programa, to_char(pro.fecha_creacion,'DD/MM/YYYY'), ");
			sb.append("to_char(pro.fecha_modificacion,'DD/MM/YYYY'), ");
			sb.append("pro.db_nombre_grup_empr, pro.cod_usuario_creacion, ");
			sb.append("pro.tt_id_estado_programa, tab.descripcion as  estado_descrip,pro.numero_solicitud, ");
			sb.append(" substr(pro.fecha_cierre,1,10),pro.cod_usuario_cierre ");			
			sb.append(",profin.PFPKG.FN_ROWCONCAT('SELECT nvl(trim(NOMBRE),''-'') FROM profin.tiipf_empresa WHERE rownum<=10 and iipf_programa_id ='|| pro.id_programa) empresasxgrupo ");
			sb.append(",pro.tt_id_tipo_empresa ");
			sb.append(",pro.id_grupo ");
			sb.append(",pro.id_empresa ");
			sb.append("from profin.tiipf_programa pro inner join profin.tiipf_tabla_de_tabla tab ");
			sb.append("on pro.tt_id_estado_programa = tab.id_tabla ");
			sb.append("left join  profin.tiipf_tabla_de_tabla tabmotivo ");
			sb.append("on pro.tt_id_motivo_cierre = tabmotivo.id_tabla ");
			sb.append("where 1=1 ");
			if(codigo!=null && !codigo.equals("")){
				sb.append("and id_programa in (");
				
				sb.append(" select prog.id_programa from profin.tiipf_programa prog");
				sb.append(" where prog.id_programa=" + idprogramaIni+ "");
				sb.append(" and exists (select 1 from profin.tiipf_empresa e where e.iipf_programa_id=prog.id_programa and e.codigo='" + codigo.toUpperCase()+ "')");
			       
				sb.append(" union ");
				sb.append(" select id_programa from ");				
					sb.append("(");
					sb.append("select id_programa,rownum item from ");
					sb.append("(");    
					sb.append(" select");
					sb.append("  id_programa");
					sb.append("	,INSTR(empresasxgrupo ,'" + codigo.toUpperCase()+ "') INDICADOR ");                   
					sb.append("  from(");     
					sb.append("   select ");
					sb.append("   prog.id_programa ");
					sb.append("  ,profin.PFPKG.FN_ROWCONCAT('SELECT nvl(trim(CODIGO),''-'') FROM profin.tiipf_empresa WHERE rownum<=60 and iipf_programa_id ='|| prog.id_programa) empresasxgrupo ");
					sb.append("   from profin.tiipf_programa prog");
					sb.append("   where id_programa<>" + idprogramaIni+ "");
					sb.append("   ) GEN");
				    sb.append(")FI");
				    sb.append(" WHERE FI.INDICADOR<>0");
				    sb.append(")");			    
			    sb.append(" where item <" + numeroTop+ "");
			    
			    sb.append(")");
			}																	
			sb.append(" order by pro.FECHA_CREACION desc");
			
			//logger.error("listarProgramasByCodigoCentral = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			programas = new ArrayList<Programa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				datoPrograma = new Programa();
				
				datoPrograma.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					datoPrograma.setFechaCreacionFormato(amount[1].toString());
				if(amount[2] != null)
					datoPrograma.setFechaModificacionFormato(amount[2].toString());
				if(amount[3] != null)
					datoPrograma.setNombreGrupoEmpresa(amount[3].toString());
				if(amount[4] != null)
					datoPrograma.setCodUsuarioCreacion(amount[4].toString());
				if(amount[5] != null && amount[6] != null)
				{
					Tabla estadoPrograma = new Tabla();
					estadoPrograma.setId(new Long(amount[5].toString()));
					estadoPrograma.setDescripcion(amount[6].toString());					
					datoPrograma.setEstadoPrograma(estadoPrograma);
				}
				if(amount[7] != null){
					datoPrograma.setNumeroSolicitud(amount[7]==null?"":amount[7].toString());
				}
//				if(amount[8] != null && amount[9] != null)
//				{
//					Tabla motivoPrograma = new Tabla();
//					motivoPrograma.setId(new Long(amount[8].toString()));
//					motivoPrograma.setDescripcion(amount[9].toString());					
//					datoPrograma.setMotivoCierre(motivoPrograma);
//				}
//				if(amount[10] != null){  
//					datoPrograma.setObservacionCierre(amount[10]==null?"":amount[10].toString().replaceAll("\\r?\\n","<br/>"));
//				}
				if(amount[8] != null){
					datoPrograma.setFechaCierre(amount[8]==null?"":amount[8].toString());
					
				}
				if(amount[9] != null){
					datoPrograma.setCodUsuarioCierre(amount[9]==null?"":amount[9].toString());
				}
				if(amount[10] != null){
					datoPrograma.setCadEmpresaxGrupo(amount[10]==null?"":amount[10].toString());
				}
				if(amount[11] != null)
				{
					Tabla otipoEmpresa = new Tabla();
					String descTipoEmpresa="";
					
					if (amount[11].toString().equals("2")) {
						descTipoEmpresa="EMPRESA";
					}else if (amount[11].toString().equals("3")){
						descTipoEmpresa="GRUPO";
					}else{
						descTipoEmpresa="-";
					}
					otipoEmpresa.setId(new Long(amount[11].toString()));
					otipoEmpresa.setDescripcion(descTipoEmpresa);					
					datoPrograma.setTipoEmpresa(otipoEmpresa);
				}
				if(amount[12] != null){
					datoPrograma.setIdGrupo(amount[12]==null?"":amount[12].toString());
				}
				if(amount[13] != null){
					datoPrograma.setIdEmpresa(amount[13]==null?"":amount[13].toString());
				}
											
				programas.add(datoPrograma);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return programas;
	}
	//fin MCG20140804
	
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
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarEstadoDescargaPDF(Long idPrograma,String estadoDescargaPDF) throws BOException{
		List parametros = new ArrayList();
		parametros.add(estadoDescargaPDF);
		parametros.add(idPrograma);
				
		try {
			super.executeNamedQuery("updateEstadoDescargaPDF",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	
	public ConfiguracionWS getConfiguracionWS() {
		return configuracionWS;
	}

	public void setConfiguracionWS(ConfiguracionWS configuracionWS) {
		this.configuracionWS = configuracionWS;
	}

	public String getPathWebServicePEC6() {
		return pathWebServicePEC6;
	}

	public void setPathWebServicePEC6(String pathWebServicePEC6) {
		this.pathWebServicePEC6 = pathWebServicePEC6;
	}


	
	
	
}
