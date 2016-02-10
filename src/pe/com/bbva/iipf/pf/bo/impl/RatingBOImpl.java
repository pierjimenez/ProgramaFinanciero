package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;



import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.dao.RatingDAO;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSRating;
import pe.com.bbva.iipf.pf.model.Pfrating;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.bbva.iipf.pf.model.Trating;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.QueryRatingWS;
import pe.com.bbva.pfa.rating.Ratingws;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;


/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("ratingBO")
@Scope("prototype") 
public class RatingBOImpl extends GenericBOImpl<Rating> implements RatingBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private RatingDAO ratingDAO;
	
	@Resource
	private TablaBO tablaBO;
	

	
	private String SQL_RATING = "select distinct FECH_PERIODO_CALC,FACT_CUANTITATIVOS," +
										 " FACT_CUALITATIVOS,FACT_BUREAU," +
										 " PUNT_RATING,CALIF_RATING " +
										 " from PROFIN.TIIPF_PFRATING " +
										 " where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' and" +
										 " TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4))>=" +
									     " (select max(TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4)))-3" +
									     " from PROFIN.TIIPF_PFRATING where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' ) " +
										 " order by TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4)) desc";
		
	private Programa programa;
	private List<Rating> listaRating = new ArrayList<Rating>(); 
	
	private String pathWebServicePEC6;
	private String pathWebService;
	
	/**
	 * Extrae la información del archivo de HOST del Rating 
	 * LA DATA POR CADA FILA SE ENCUENTRA DE LA SIGUIENTE FORMA
	 * FACT_CUANTITATIVOS
	 * FACT_CUALITATIVOS
	 * FACT_BUREAU
	 * PUNT_RATING
	 * CALIF_RATING
	 * CADA FILA ES UN PERIODO INICIANDO POR EL PERIDODO ACTUAL 
	 * Y TERMNANDO EN EL ANIO ANTEPENULTIMO
	 * 
	 * IMPORTANTE
	 * Este metodo debe ser usado solo cuando se desee actualizar el rating
	 * de la empresa.
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Rating> actualizarRating()throws BOException{
		try {
			listaRating = new ArrayList<Rating>();
			Calendar calendar = Calendar.getInstance();
			//int anioInicial = calendar.get(Calendar.YEAR)-2;
			SQL_RATING = String.format(SQL_RATING, 
									   programa.getIdEmpresa(),
									   programa.getIdEmpresa());
		
			List listaDataRating = executeSQL(SQL_RATING);			
			
			if(listaDataRating.isEmpty()){
				throw new BOException("No existen datos en la tabla del Rating");
			}
					
			
			List<Rating> listaRatingBD = findRating();
			Object[] fila1 = {"-","-","-","-","-","-"};//ANIO ACTUAL
			Object[] fila2 = {"-","-","-","-","-","-"};//PENULTIMO AÑO
			Object[] fila3 = {"-","-","-","-","-","-"};//ANTEPENULTIMO AÑO
			if(listaDataRating.size()>0){
				fila1 = (Object[])listaDataRating.get(0);//ANIO ACTUAL
			}
			if(listaDataRating.size()>1){
				fila2 = (Object[])listaDataRating.get(1);//PENULTIMO AÑO
			}
			if(listaDataRating.size()>2){
				fila3 = (Object[])listaDataRating.get(2);//ANTEPENULTIMO AÑO
			}
			
			
			Rating ratingFecha = null;//fecha
			Rating ratingFCT = null;//factores cuantitativos
			Rating ratingFCL = null;//factores cualitativos
			Rating ratingB = null;//escala buro
			Rating ratingR = null;//escala rating
			Rating ratingEM = null;//escala maestra
			if(listaRatingBD.isEmpty()){
				ratingFecha  = new Rating();
				ratingFCT = new Rating();
				ratingFCL = new Rating();
				ratingB = new Rating();
				ratingR = new Rating();
				ratingEM = new Rating();
			}else{
				ratingFecha  = listaRatingBD.get(0);
				ratingFCT = listaRatingBD.get(1);
				ratingFCL = listaRatingBD.get(2);
				ratingB = listaRatingBD.get(3);
				ratingR = listaRatingBD.get(4);
				ratingEM = listaRatingBD.get(5);
			}
			
			ratingFecha.setTotalAnio2(fila3[0].toString());
			ratingFecha.setTotalAnio1(fila2[0].toString());
			ratingFecha.setTotalAnioActual(fila1[0].toString());
			ratingFecha.setDescripcion(null);
			ratingFecha.setPrograma(programa);
			ratingFecha.setPos(0);
			
			ratingFCT.setTotalAnio2(fila3[1].toString());
			ratingFCT.setTotalAnio1(fila2[1].toString());
			ratingFCT.setTotalAnioActual(fila1[1].toString());
			ratingFCT.setDescripcion("FACTORES CUANTITATIVOS");
			ratingFCT.setPrograma(programa);
			ratingFCT.setPos(1);	
			
			ratingFCL.setTotalAnio2(fila3[2].toString());
			ratingFCL.setTotalAnio1(fila2[2].toString());
			ratingFCL.setTotalAnioActual(fila1[2].toString());
			ratingFCL.setDescripcion("FACTORES CUALITATIVOS ");
			ratingFCL.setPrograma(programa);
			ratingFCL.setPos(2);
			
			ratingB.setTotalAnio2(fila3[3].toString());
			ratingB.setTotalAnio1(fila2[3].toString());
			ratingB.setTotalAnioActual(fila1[3].toString());
			ratingB.setDescripcion("FACTORES BURO ");
			ratingB.setPrograma(programa);
			ratingB.setPos(3);
			
			ratingR.setTotalAnio2(fila3[4].toString());
			ratingR.setTotalAnio1(fila2[4].toString());
			ratingR.setTotalAnioActual(fila1[4].toString());
			ratingR.setDescripcion("FACTORES RATING ");
			ratingR.setPrograma(programa);
			ratingR.setPos(4);	
			
			ratingEM.setTotalAnio2(fila3[5]!= null?fila3[5].toString():"");
			ratingEM.setTotalAnio1(fila2[5]!= null?fila2[5].toString():"");
			ratingEM.setTotalAnioActual(fila1[5]!=null?fila1[5].toString():"");
			ratingEM.setDescripcion("ESCALA MAESTRA ");
			ratingEM.setPrograma(programa);
			ratingEM.setPos(5);

			listaRating.add(ratingFecha);
			listaRating.add(ratingFCT);
			listaRating.add(ratingFCL);
			listaRating.add(ratingB);
			listaRating.add(ratingR);
			listaRating.add(ratingEM);
			
			//actualizar los datos
			//mil saveCollection(listaRating);
			//mil programaBO.actualizarFechaModificacionPrograma(programa.getId());
			
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}catch(Exception e){
			throw new BOException(e.getMessage());
		}
		return listaRating;
	}
	
	
	//ini MCG20130320
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Rating> actualizarRating(String codEmpresaGrupo)throws BOException{
		try {
			listaRating = new ArrayList<Rating>();
			Calendar calendar = Calendar.getInstance();
			//int anioInicial = calendar.get(Calendar.YEAR)-2;
			
			

			String idPrograma=String.valueOf(programa.getId());
			List<Trating> listTrating=new ArrayList<Trating>();
			String strTipoEmpresa= String.valueOf(programa.getTipoEmpresa().getId());
			if (strTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				listTrating=findRatinGrupoEmpresa(idPrograma,codEmpresaGrupo,"E");
			}else{
				String idGrupo=String.valueOf(programa.getIdGrupo());
				if (idGrupo.equals(codEmpresaGrupo)){
					listTrating=findRatinGrupoEmpresa(idPrograma,codEmpresaGrupo,"G");
				}else{
					listTrating=findRatinGrupoEmpresa(idPrograma,codEmpresaGrupo,"E");
				}
				
			}
			
			if(listTrating==null ||listTrating!=null && listTrating.size()<=0){
				throw new BOException("No existen datos en la tabla del Rating");
			}		
			
			List<Rating> listaRatingBD = findRating(programa.getId(),codEmpresaGrupo);
			Trating fila1 = new Trating();//ANIO ACTUAL
			Trating fila2 = new Trating();//PENULTIMO AÑO
			Trating fila3 = new Trating();//ANTEPENULTIMO AÑO
			if(listTrating.size()>0){
				fila1 = listTrating.get(0);//ANIO ACTUAL
			}
			if(listTrating.size()>1){
				fila2 = listTrating.get(1);//PENULTIMO AÑO
			}
			if(listTrating.size()>2){
				fila3 = listTrating.get(2);//ANTEPENULTIMO AÑO
			}
			
			
			Rating ratingFecha = null;//fecha
			Rating ratingFCT = null;//factores cuantitativos
			Rating ratingFCL = null;//factores cualitativos
			Rating ratingB = null;//escala buro
			Rating ratingR = null;//escala rating
			Rating ratingEM = null;//escala maestra
			if(listaRatingBD.isEmpty()){
				ratingFecha  = new Rating();
				ratingFCT = new Rating();
				ratingFCL = new Rating();
				ratingB = new Rating();
				ratingR = new Rating();
				ratingEM = new Rating();
			}else{
				ratingFecha  = listaRatingBD.get(0);
				ratingFCT = listaRatingBD.get(1);
				ratingFCL = listaRatingBD.get(2);
				ratingB = listaRatingBD.get(3);
				ratingR = listaRatingBD.get(4);
				ratingEM = listaRatingBD.get(5);
			}
			
			
			ratingFecha.setTotalAnio2(fila3.getFechaPeriodoCalc());
			ratingFecha.setTotalAnio1(fila2.getFechaPeriodoCalc());
			ratingFecha.setTotalAnioActual(fila1.getFechaPeriodoCalc());
			ratingFecha.setDescripcion(null);
			ratingFecha.setPrograma(programa);
			ratingFecha.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingFecha.setPos(0);
			
			ratingFCT.setTotalAnio2(fila3.getFactCuantitativo());
			ratingFCT.setTotalAnio1(fila2.getFactCuantitativo());
			ratingFCT.setTotalAnioActual(fila1.getFactCuantitativo());
			ratingFCT.setDescripcion("FACTORES CUANTITATIVOS");
			ratingFCT.setPrograma(programa);
			ratingFCT.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingFCT.setPos(1);
			
			ratingFCL.setTotalAnio2(fila3.getFactCualitativo());
			ratingFCL.setTotalAnio1(fila2.getFactCualitativo());
			ratingFCL.setTotalAnioActual(fila1.getFactCualitativo());
			ratingFCL.setDescripcion("FACTORES CUALITATIVOS ");
			ratingFCL.setPrograma(programa);
			ratingFCL.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingFCL.setPos(2);
			
			ratingB.setTotalAnio2(fila3.getFactBureau());
			ratingB.setTotalAnio1(fila2.getFactBureau());
			ratingB.setTotalAnioActual(fila1.getFactBureau());
			ratingB.setDescripcion("FACTORES BURO ");
			ratingB.setPrograma(programa);
			ratingB.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingB.setPos(3);
			
			ratingR.setTotalAnio2(fila3.getPuntRating());
			ratingR.setTotalAnio1(fila2.getPuntRating());
			ratingR.setTotalAnioActual(fila1.getPuntRating());
			ratingR.setDescripcion("FACTORES RATING ");
			ratingR.setPrograma(programa);
			ratingR.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingR.setPos(4);
			
			ratingEM.setTotalAnio2(fila3.getCalifRating()!= null?fila3.getCalifRating():"");
			ratingEM.setTotalAnio1(fila2.getCalifRating()!= null?fila2.getCalifRating():"");
			ratingEM.setTotalAnioActual(fila1.getCalifRating()!=null?fila1.getCalifRating():"");
			ratingEM.setDescripcion("ESCALA MAESTRA ");
			ratingEM.setPrograma(programa);
			ratingEM.setCodEmpresaGrupo(codEmpresaGrupo);
			ratingEM.setPos(5);

			listaRating.add(ratingFecha);
			listaRating.add(ratingFCT);
			listaRating.add(ratingFCL);
			listaRating.add(ratingB);
			listaRating.add(ratingR);
			listaRating.add(ratingEM);
			
			//actualizar los datos
			saveCollection(listaRating);
			//saveListRating(listaRating,programa.getId(),codEmpresaGrupo);
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();

			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
			
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}catch(Exception e){
			throw new BOException(e.getMessage());
		}
		return listaRating;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveListRating(List<Rating> olistRating,Long oidProgramao,String codEmpresaGrupo) throws BOException{
		try {			
			
			if(true){
				HashMap<String,Object> params = new HashMap<String,Object>();
				params.put("programa", oidProgramao);
				params.put("codEmpresaGrupo", codEmpresaGrupo);
				List<Rating> listatemp =  findByParams2(Rating.class, params);
				List<Rating> listaDel = new ArrayList<Rating>();
				boolean flag= false;
				for(Rating eli : listatemp ){
					for(Rating elitemp : olistRating){
						if(elitemp.getId()!=null && elitemp.getId().equals(eli.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(eli);
					}
					flag= false;
				}
				
				saveCollection(olistRating);
				deleteCollection(listaDel);
				//programaBO.actualizarFechaModificacionPrograma(oidProgramao);
			}
		} catch (Exception e) {
			throw new BOException(e);//logger.error(StringUtil.getStackTrace(e));
		}	
	}
	
	//fin MCG20130320

	/**
	 * Este metodo extrae informacion de la tabla rating registrada para el
	 * programa financiero, 
	 * IMPORTANTE!
	 * este metodo debe ser usado para consultar el rating del
	 * programa 
	 */
	@Override
	public List<Rating> findRating()throws BOException{
		List<Rating> listaRatingBD = null;
		try {
			HashMap<String, Long> parametros = new HashMap<String, Long> ();
			parametros.put("programa", programa.getId());
			HashMap<String,String> order= new HashMap<String,String>();
			order.put("pos", " asc");
			//listaRatingBD = findByParams(Rating.class, parametros);
			listaRatingBD = findByParamsOrder(Rating.class, parametros,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaRatingBD;
	}
	@Override
	public List<Rating> findRating(Long idPrograma)throws BOException{
		List<Rating> listaRatingBD = null;
		try {
			HashMap<String, Long> parametros = new HashMap<String, Long> ();
			parametros.put("programa", idPrograma);
			HashMap<String,String> order= new HashMap<String,String>();
			order.put("pos", " asc");
			//listaRatingBD = findByParams(Rating.class, parametros);
			listaRatingBD = findByParamsOrder(Rating.class, parametros,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaRatingBD;
	}
	@Override
	public List<Rating> findRating(Long idPrograma , String codigoEmpresa)throws BOException{
		List<Rating> listaRatingBD = null;
		try {
			HashMap<String, Object> parametros = new HashMap<String, Object> ();
			parametros.put("programa", idPrograma);
			parametros.put("codEmpresaGrupo", codigoEmpresa);
			HashMap<String,String> order= new HashMap<String,String>();
			order.put("pos", " asc");
			//listaRatingBD = findByParams(Rating.class, parametros);
			listaRatingBD = findByParamsOrder(Rating.class, parametros,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaRatingBD;
	}
	
	public List<Trating> findRatinGrupoEmpresa(String idPrograma,String codCliente,String TipoEmpresa) throws BOException {
		List<Trating> listTrating = null;	
		try{			
			listTrating = ratingDAO.findListaRatingGrupo(idPrograma,codCliente,TipoEmpresa);
		
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
		return listTrating;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaRating(List<Rating> olistRating) throws BOException{		
				saveCollection(olistRating);
	}
	
	//ini MCG20130708
	@Override
	public String ObtenerRating(Programa oprograma,String codEmpresa){	
		
		List<Rating> listaRatingAll = new ArrayList<Rating>(); 
		String descRating=Constantes.NOM_RATING;// "ESCALA MAESTRA";
		String strRatingRDC="";
		try {			
									
			listaRatingAll = findRating(oprograma.getId(),codEmpresa);	
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
		}	
		return strRatingRDC;	
	}
	//fin MCG20130708
	
	//ini MCG20130812
	@Override
	public Map  ObtenerRatingConFecha(Programa oprograma,String codEmpresa){	
		
		List<Rating> listaRatingAll = new ArrayList<Rating>(); 
		Map<String,String> mRatingFecha 	= new HashMap<String,String>();
		String descRating=Constantes.NOM_RATING;// "ESCALA MAESTRA";
		String strResul="";
		String strRatingRDC="";
		String strRatingFecha="";
		try {			
									
			listaRatingAll = findRating(oprograma.getId(),codEmpresa);	
			if (listaRatingAll!=null && listaRatingAll.size()>0 ){
				for(Rating orating : listaRatingAll){	
					if (orating.getDescripcion()!=null && orating.getDescripcion().trim().equals(descRating)){
						strRatingRDC=orating.getTotalAnioActual()==null?"":orating.getTotalAnioActual();						
						break;
					}				
					
				}
				//obtener fecha rating
				for(Rating oratingf : listaRatingAll){	
					if (oratingf.getDescripcion()==null){
						strRatingFecha=oratingf.getTotalAnioActual()==null?"":oratingf.getTotalAnioActual();						
						break;
					}				
					
				}
				mRatingFecha.put("mratingAnexo", strRatingRDC);
				mRatingFecha.put("mfechagAnexo", strRatingFecha);				
			}else{
				mRatingFecha.put("mratingAnexo", "");
				mRatingFecha.put("mfechagAnexo", "");	
			}					
			
		} catch (BOException e) {
			mRatingFecha 	= new HashMap<String,String>();
			mRatingFecha.put("mratingAnexo", "");
			mRatingFecha.put("mfechagAnexo", "");			
		}	
		return mRatingFecha;	
	}
	//fin MCG20130812
	
	//ini MCG20140903

	
	private ConfiguracionWSRating getConfiguracionWSRating(String codigoUsuario){
		ConfiguracionWSRating oConfiguracion=new ConfiguracionWSRating();
		List<Tabla> tablasHijos = new ArrayList<Tabla>();
		
		try {	
		tablasHijos=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_VALOR_WSRATING);		
		oConfiguracion.setCodigoUsuario(codigoUsuario);
		oConfiguracion.setTipoEmpresa(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_TIPO_EMPRESA				,tablasHijos)	);
		oConfiguracion.setTipoGrupo(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_TIPO_GRUPO				,tablasHijos)	);
		oConfiguracion.setOpcionAplicacion(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_OPCION_APLICACION			,tablasHijos)	);
		oConfiguracion.setTerminalLogico(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_TERMINAL_LOGICO			,tablasHijos)	);
		oConfiguracion.setTerminalContable(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_TERMINAL_CONTABLE			,tablasHijos)	);
		oConfiguracion.setItdaAplicativo(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITDA_APLICATIVO			,tablasHijos)	);
		oConfiguracion.setItdaTratamiento(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITDA_TRATAMIENTO			,tablasHijos)	);
		//oConfiguracion.setXgr7Tratamiento(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_XGR7_TRATAMIENTO			,tablasHijos)	);
		oConfiguracion.setItcuTransaccion(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_TRANSACCION			,tablasHijos)	);
		oConfiguracion.setItcuTipoMensaje(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_TIPO_MENSAJE			,tablasHijos)	);
		oConfiguracion.setItcuTipoProceso(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_TIPO_PROCESO			,tablasHijos)	);
		oConfiguracion.setItcuCanalComunicacion(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_CANAL_COMUNICACION	,tablasHijos)	);
		oConfiguracion.setItcuIndicadorPreformato(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_INDICADOR_PREFORMATO	,tablasHijos)	);
		oConfiguracion.setItcuTipoMensajeMe(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_TIPO_MENSAJE_ME		,tablasHijos)	);
		oConfiguracion.setItcuDistQnameIn(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_DIST_QNAME_IN		,tablasHijos)	);
		oConfiguracion.setItcuDistRqnameOut(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_DIST_RQNAME_OUT		,tablasHijos)	);
		oConfiguracion.setItcuHostRqnameOut(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_HOST_RQNAME_OUT		,tablasHijos)	);
		oConfiguracion.setItcuHostQmgrName(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_HOST_QMGR_NAME		,tablasHijos)	);
		oConfiguracion.setItcuTipoEmpresa(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_TIPO_EMPRESA			,tablasHijos)	);
		oConfiguracion.setItcuOpcion(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_OPCION				,tablasHijos)	);
		
		oConfiguracion.setItdaIpPort(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITDA_IP_PORT				,tablasHijos)	);
		oConfiguracion.setItcuIpPort(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_IP_PORT				,tablasHijos)	);
		oConfiguracion.setItcuEncoding(obtenerValorHijo(Constantes.COD_HIJO_WSRATING_ITCU_ENCODING				,tablasHijos)	);
		 

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
	
	private List<Ratingws> findRatinWSGrupoEmpresa(String codigoUsuario,List<Empresa> olistaEmpresa,String flagEmpresaGrupo) throws BOException {
		List<Ratingws> listRatingws = new ArrayList<Ratingws>();	
		
		try{		
			ConfiguracionWSRating oConfiguracion=getConfiguracionWSRating(codigoUsuario);
			listRatingws = QueryRatingWS.obtenerRatingWS( oConfiguracion, olistaEmpresa, flagEmpresaGrupo);
		
		} catch (Exception e) {
			throw new BOException(e.getMessage());
		} 
		return listRatingws;
	}
	//fin MCF20140903
	


	@Override
	public void updateRatingWS(Programa oprograma,boolean bactivo,List<Empresa> listaempresaCompleto,TmanagerLog oLogRating){
		
		try {
			List<Empresa> listaempresa =programaBO.getEmpresaWS(oprograma.getId());
			String codGrupo=oprograma.getIdGrupo();
			String strTipoEmpresa= String.valueOf(oprograma.getTipoEmpresa().getId());
			String codigoUsuario=getUsuarioSession().getRegistroHost();
			String codEmpresa="";
			if (strTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				codEmpresa=listaempresa.get(0).getCodigo();
				
				List<Ratingws> listaRatingWS=findRatinWSGrupoEmpresa(codigoUsuario,listaempresa,Constantes.COD_TIPO_INDIVIDUAL_RATINGWS);
				actualizaEmpresaRatingWS(null,codEmpresa,listaRatingWS,Constantes.COD_TIPO_INDIVIDUAL_RATINGWS);
				if (bactivo && listaRatingWS!=null && listaRatingWS.size()>0){
					try {
						actualizarRating(codEmpresa);
					} catch (Exception e) {
						logger.error("error::"+e.getMessage());
					}
					
				}
			}else{
				for(Empresa empresa: listaempresa){
					codEmpresa=empresa.getCodigo();
					List<Empresa> listaempresaIndiv=new ArrayList<Empresa>();
					listaempresaIndiv.add(empresa);
					List<Ratingws> listaRatingWS=findRatinWSGrupoEmpresa(codigoUsuario,listaempresaIndiv,Constantes.COD_TIPO_INDIVIDUAL_RATINGWS);
					actualizaEmpresaRatingWS(null,codEmpresa,listaRatingWS,Constantes.COD_TIPO_INDIVIDUAL_RATINGWS);					
					if (bactivo && listaRatingWS!=null && listaRatingWS.size()>0){
						try {
							actualizarRating(codEmpresa);	
						} catch (Exception e) {
							logger.error("error::"+e.getMessage());
						}						
					}
				}			
				//Para grupo
				
				//List<Empresa> listaempresaCompleto=programaBO.getEmpresaGrupoWS( codGrupo, oprograma.getId(),oLog);
				List<Ratingws> listaRatingWS=findRatinWSGrupoEmpresa(codigoUsuario,listaempresaCompleto,Constantes.COD_TIPO_CONSOLIDADO_RATINGWS);
				String codigoempresaCon=listaRatingWS.get(0).getCodigoCentralCliente();
				actualizaEmpresaRatingWS(codGrupo,codigoempresaCon,listaRatingWS,Constantes.COD_TIPO_CONSOLIDADO_RATINGWS);					
				if (bactivo && listaRatingWS!=null && listaRatingWS.size()>0){
					try {
						actualizarRating(codGrupo);
					} catch (Exception e) {
						logger.error("error::"+e.getMessage());
					}					
				}			
			}				
			
		} catch (Exception e) {
			logger.error("updateRatingWS error::"+e.getMessage());
		}

	}
	
	
	
	private void actualizaEmpresaRatingWS(String idGrupo,String codigoEmpresa,List<Ratingws> listaRatingWS,String tipEstadoFinan){
		try {
			List parametros = new ArrayList();
			parametros.add(codigoEmpresa);
			parametros.add(tipEstadoFinan);
			
			
			
			if (listaRatingWS!=null && listaRatingWS.size()>0){
				super.executeNamedQuery("deleteRatingEmpresaWS",parametros);
				if (tipEstadoFinan.equals(Constantes.COD_TIPO_CONSOLIDADO_RATINGWS)){
					List parametrosGrupo = new ArrayList();
					logger.error("actualizaEmpresaRatingWS:para consilidado::"+idGrupo);
					logger.error("actualizaEmpresaRatingWS: idgrupo::"+idGrupo);
					parametrosGrupo.add(idGrupo);
					super.executeNamedQuery("deleteRatingGrupoWS",parametrosGrupo);
				}
				List<Pfrating> listaPfrating=getListaPFRating(idGrupo,codigoEmpresa,listaRatingWS,tipEstadoFinan);
				ratingDAO.insertLotepfRating(listaPfrating);
				
			}		
			
		} catch (Exception e) {
			logger.error("error::"+e.getMessage());
		}		
	}
	@Override
	public void saveSQLPFRating2(List<Pfrating> listapfRating) throws BOException{
		
		try {
			ratingDAO.insertLotepfRating(null);
		} catch (DAOException e) {
			 
			e.printStackTrace();
		}
	}
	
	private void saveSQLPFRating(List<Pfrating> listapfRating) throws BOException{
		try {
				for ( Pfrating oPfrating:listapfRating) {
					List parametros = new ArrayList();
					parametros.add(null);
					
					parametros.add(oPfrating.getFechPeriodoCalc());  		 //FECH_PERIODO_CALC  VARCHAR2(10), 
					parametros.add(oPfrating.getNombEmpresa()) ;		     //NOMB_EMPRESA       VARCHAR2(60), 
					parametros.add(oPfrating.getCodCentralCli()) ;   		 //COD_CENTRAL_CLI    VARCHAR2(8),  
					parametros.add(oPfrating.getMesesEjercicios()) ;  	 //MESES_EJERCICIOS   VARCHAR2(2),  
					parametros.add(oPfrating.getInflacion() )        ; 	 //INFLACION          VARCHAR2(11), 
					parametros.add(oPfrating.getCalifRating() )    ;		 //CALIF_RATING       VARCHAR2(6),  
					parametros.add(oPfrating.getCodCuenta())         ;	 //COD_CUENTA         VARCHAR2(20), 
					parametros.add(oPfrating.getDescCuenta() )      ; 	 //DESC_CUENTA        VARCHAR2(60), 
					parametros.add(oPfrating.getMonto() )           ;	 //MONTO              VARCHAR2(17), 
					parametros.add(oPfrating.getStatus())            ;	 //STATUS             VARCHAR2(20), 
					parametros.add(oPfrating.getTipEstadoFinan() ) ;		 //TIP_ESTADO_FINAN   VARCHAR2(1),  
					parametros.add(oPfrating.getFactCualitativos() ) ;	 //FACT_CUALITATIVOS  VARCHAR2(5),  
					parametros.add(oPfrating.getFactCuantitativos()) ;	 //FACT_CUANTITATIVOS VARCHAR2(5),  
					parametros.add(oPfrating.getFactBureau() )      ;	 //FACT_BUREAU        VARCHAR2(5),  
					parametros.add(oPfrating.getPuntRating())        ;	 //PUNT_RATING        VARCHAR2(6) 
					parametros.add(oPfrating.getId()); //id_pfrating
					parametros.add(oPfrating.getIdGrupo()); //ID_GRUPO
									
				
					
					
					String sqlqueryp = "insert into PROFIN.TIIPF_PFRATING (FECH_PERIODO_CALC, NOMB_EMPRESA, COD_CENTRAL_CLI, MESES_EJERCICIOS, INFLACION, CALIF_RATING, " +
							"COD_CUENTA, DESC_CUENTA, MONTO, STATUS, TIP_ESTADO_FINAN, FACT_CUALITATIVOS, FACT_CUANTITATIVOS, FACT_BUREAU, PUNT_RATING, ID_PFRATING, ID_GRUPO)" +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, PROFIN.seq_pfrating.NEXTVAL, ?)";

					
					super.executeSQLQuery(sqlqueryp,parametros);				
				}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new BOException(e.getMessage());
		}
		
	}
	
	private List<Pfrating> getListaPFRating(String idGrupo,String codigoEmprea,List<Ratingws> listaRatingWS,String tipoEstadoFinan){
		
		List<Pfrating> listpfrating=new ArrayList<Pfrating>();
		Pfrating opfrating=new Pfrating();
		for (Ratingws oratingws:listaRatingWS){
			opfrating=new Pfrating();
			
			opfrating.setFechPeriodoCalc(oratingws.getFechaPeriodoCalculado());
			opfrating.setNombEmpresa(oratingws.getNombreEmpresa());
			//MCG opfrating.setCodCentralCli(oratingws.getCodigoCentralCliente());
			opfrating.setCodCentralCli(codigoEmprea);
			opfrating.setIdGrupo(idGrupo);
			opfrating.setMesesEjercicios(oratingws.getMesEjercicio());
			opfrating.setInflacion(oratingws.getInflacion());
			opfrating.setCalifRating(oratingws.getCalificacionRating());
			opfrating.setCodCuenta(oratingws.getCodigoCuenta());
			opfrating.setDescCuenta(oratingws.getDescripcionCuenta());
			//try {
			//	Double monto1=Double.valueOf(FormatUtil.FormatNumeroSinComaEmpty(oratingws.getMonto()));
			//	System.out.println("monto original:"+oratingws.getMonto() +"::monto final:"+monto1);
			//} catch (Exception e) {
				 
			//}
			
			
			opfrating.setMonto(oratingws.getMonto());
			opfrating.setStatus(oratingws.getStatus());
			//MCG opfrating.setTipEstadoFinan(oratingws.getTipoEstadoFinanciero());
			opfrating.setTipEstadoFinan(tipoEstadoFinan);
			opfrating.setFactCualitativos(oratingws.getFactorCualitativo());
			opfrating.setFactCuantitativos(oratingws.getFactorCuantitativo());
			opfrating.setFactBureau(oratingws.getFactorBureau());
			opfrating.setPuntRating(oratingws.getPuntuacionRating());
			listpfrating.add(opfrating);
		}
		
		
		
		//ini Simulando	
//		List<String> params = new ArrayList<String>();
//		params.add("00000655");
//		params.add("I");
//		
//		
//		List<Pfrating> listatemp=new ArrayList<Pfrating>();
//		try {
//			//listatemp = findByParams2(Pfrating.class, params);
//			listatemp = executeListNamedQuery("listarPFratintcm", params); 
//			for (Pfrating or:listatemp){
//				or.setCodCentralCli(codigoEmprea);
//				or.setId(null);
//			}
//			
//		} catch (BOException e) {
//			 
//			e.printStackTrace();
//		}
		//fin Simulando
		
		
		//return listatemp;
		return listpfrating;
		
	}
	
	

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}


	public String getPathWebServicePEC6() {
		return pathWebServicePEC6;
	}


	public void setPathWebServicePEC6(String pathWebServicePEC6) {
		this.pathWebServicePEC6 = pathWebServicePEC6;
	}


	public String getPathWebService() {
		return pathWebService;
	}


	public void setPathWebService(String pathWebService) {
		this.pathWebService = pathWebService;
	}	
	
	
}
