package pe.com.bbva.iipf.pf.bo.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.dao.RelacionesBancariasDAO;
import pe.com.bbva.iipf.pf.model.Comex;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.EfetividadCartera;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.PoolBancario;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.Rentabilidad;
import pe.com.bbva.iipf.pf.model.RentabilidadBEC;
import pe.com.bbva.iipf.pf.model.Tbanco;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.pf.model.Trccmes;
import pe.com.bbva.iipf.pf.model.kc10comex;
import pe.com.bbva.iipf.pf.model.rcd;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.QueryComex;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;


@Service("relacionesBancariasBO")
public class RelacionesBancariasBOImpl extends GenericBOImpl implements RelacionesBancariasBO {
	
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private RelacionesBancariasDAO relacionesBancariasDAO;
	
	@Resource
	private TablaBO tablaBO;
	
	
	private List<LineasRelacionBancarias> listLineasRelacionesBancarias = new ArrayList<LineasRelacionBancarias>();	
	private List<RentabilidadBEC> listRentabilidadBEC = new ArrayList<RentabilidadBEC>();	
	
	private Programa programa;	
	private ProgramaBlob programaBlob = new ProgramaBlob(); 
	private String comenLineas;
	
	///
	private List<Tempresa> listaEmpresaSelect;	
	private List<Tbanco> listaBancoSelect;
	private String codTipoDeudas;	
	
	List<PoolBancario> listPoolBancario;
	List<OpcionPool> listOpcionPool;
	private List<Comex> listaComex;
	
	private ConfiguracionWS configuracionWS;
	
	private String QUERY_CUOTA = "select nvl(sum(deuda_total),0) total from PROFIN.tiipf_pfrccmes ";
	
	
	
	
	public List<OpcionPool> getListOpcionPool() {
		return listOpcionPool;
	}
	public void setListOpcionPool(List<OpcionPool> listOpcionPool) {
		this.listOpcionPool = listOpcionPool;
	}
	public List<PoolBancario> getListPoolBancario() {
		return listPoolBancario;
	}
	public void setListPoolBancario(List<PoolBancario> listPoolBancario) {
		this.listPoolBancario = listPoolBancario;
	}
	public String getCodTipoDeudas() {
		return codTipoDeudas;
	}
	public void setCodTipoDeudas(String codTipoDeudas) {
		this.codTipoDeudas = codTipoDeudas;
	}
	
	public List<Tempresa> getListaEmpresaSelect() {
		return listaEmpresaSelect;
	}
	public void setListaEmpresaSelect(List<Tempresa> listaEmpresaSelect) {
		this.listaEmpresaSelect = listaEmpresaSelect;
	}

	public List<Tbanco> getListaBancoSelect() {
		return listaBancoSelect;
	}

	public void setListaBancoSelect(List<Tbanco> listaBancoSelect) {
		this.listaBancoSelect = listaBancoSelect;
	}
	
	/**
	 * 
	 */
	@Override
	public List<rcd> findByCalificacionBanco(String codEmpresa,String tipoDocumento) throws Exception	{
		try {
			HashMap<String,String> params = new HashMap<String,String>();			
			params.put("ruc", codEmpresa);
			params.put("rownum", "1");	
			List<rcd> lista = findByParams2(rcd.class, params); 		
			logger.info("cantidad calificacion financieera="+lista.size());
			return lista;			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	
	/**
	 * Método que calcula los porcentajes Normal, CPP, Deficiente, Dudoso, Perdida de 
	 * la calificación financiera. para el caso de grupos se toma la calificación de la empresa 
	 * 
	 * @param codEmpesa ruc de la empresa
	 */
	@Override
	public List<HashMap<String,BigDecimal>> findByClasificacionSistemaFinanciero2(String codEmpresa) throws BOException {
		List<HashMap<String,BigDecimal>> lista = null;
		try {	
//			HashMap<String,String> params = new HashMap<String,String>();
//			params.put("tipoDocumento", tipoDocumento);
//			params.put("documento", codEmpresa);
//			params.put("rownum", "1");	
//			List<rccmes> lista = findByParams2(rccmes.class, params);
			lista = relacionesBancariasDAO.findCalificacionFinanciera(codEmpresa);
			logger.info("findByClasificacionSistemaFinanciero2 cantidad  ="+lista.size());
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		}
		return lista;
	}
	
	/**
	 * 
	 */
	@Override
	public List<EfetividadCartera> findEfectividadCartera(String codEmpresa,String codServicio) throws Exception {
		try {	
			HashMap<String,String> params = new HashMap<String,String>();
			
			//params.put("tipoDocumento", tipoDocumento);
			params.put("documento", codEmpresa);
			params.put("codServicio", codServicio);
			params.put("rownum", "1");	
			List<EfetividadCartera> lista = findByParams2(EfetividadCartera.class, params);
			logger.info("findEfectividadCartera"+lista);
			return lista;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public List<Rentabilidad> findRentabilidad(String codCentral,String tipoarchivo) throws Exception {
		try {	
			HashMap<String,String> params = new HashMap<String,String>();		
			params.put("codCentral", codCentral);			
			params.put("rownum", "1");
			params.put("tipoarchivo",tipoarchivo);
			//tipoarchivo
			List<Rentabilidad> lista = findByParams2(Rentabilidad.class, params);
			logger.info("cantidad rentabilidad = "+lista.size());
			return lista;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public List<RentabilidadBEC> findRentabilidadBEC(String Idprograma) throws Exception {
		try {	
			HashMap<String,String> params = new HashMap<String,String>();		
			params.put("programa", Idprograma);			
			List<RentabilidadBEC> lista = findByParams2(RentabilidadBEC.class, params);
			logger.info("cantidad Rentabilidad BEC= "+lista.size());
			return lista;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<RentabilidadBEC> findRentabilidadBEC(String Idprograma,String codigoCentral) throws Exception {
		try {	
			HashMap<String,String> params = new HashMap<String,String>();		
			params.put("programa", Idprograma);	
			params.put("codEmpresaGrupo",codigoCentral);
			List<RentabilidadBEC> lista = findByParams2(RentabilidadBEC.class, params);
			logger.info("cantidad Rentabilidad BEC= "+lista.size());
			return lista;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public List<OpcionPool> findOpcionPool(String Idprograma,String tipoOpcionPool) throws Exception  	{
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("programa", Idprograma);
		params.put("tipoOpcionPool", tipoOpcionPool);
		List<OpcionPool> listatemp = null;
		
		try {	
			listatemp = findByParams2(OpcionPool.class, params); 
			logger.info("cantidad opocion pool="+listatemp.size());			
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	
	/**
	 * 
	 */
	@Override
	public List<LineasRelacionBancarias> findByLineasRelacionesBancarias(Long Idprograma) throws BOException  	{
		List<Long> params = new ArrayList<Long>();
		params.add(Idprograma);
		List<LineasRelacionBancarias> listatemp = null;
		
		try {	
			listatemp = super.executeListNamedQuery("findLineasRelaBancByPrograma", params); 
			logger.info("cantidad relaciones bancarias="+listatemp.size());					
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	
//	public String savelineas(LineasRelacionBancarias lineasRelacionBancarias) throws Exception{
//		
//		try {	
//			super.save(lineasRelacionBancarias);
//					
//			return "OK";			
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		}
//	
//	}
//	public String deletelineas(LineasRelacionBancarias lineasRelacionBancarias) throws Exception{
//		
//		try {	
//			//super.delete(lineasRelacionBancarias);
//					
//			return "OK";			
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		}
//	
//	}
	
	
	@Override
	public List<List>  findPoolBancario(String tipoDocumento,String codigo,
										String tipoDeuda,String codBanco,
										String CodOtro,String codtipoEmpresaGrupo,
										String idPrograma) throws BOException

	{
		
		logger.info("INICIO findPoolBancario");
		List poolBancarios = null;		
		try {
			poolBancarios = relacionesBancariasDAO.findPoolBancario(tipoDocumento, codigo,
																	tipoDeuda,codBanco,
																	CodOtro,codtipoEmpresaGrupo,
																	idPrograma);
			logger.info("cantidad opocion pool="+poolBancarios.size());		
			if (poolBancarios == null
					|| (poolBancarios != null && poolBancarios.size() == 0)) {
				//throw new BOException("No se econtraron Pool Bancarios");
			}
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
//			catch (BOException e) {
//			throw new BOException(e.getMessage());
//		}
		logger.info("FIN findPoolBancario");
		return poolBancarios;		
		
	}
	@Override
	public List<List>  findPoolBancarioBD(String tipoDocumento,String codigo,
										  String tipoDeuda,String codBanco,
										  String codtipoEmpresaGrupo,String idPrograma) throws BOException

	{
		
		logger.info("INICIO findPoolBancario");
		List poolBancarios = null;		
		try {
			poolBancarios = relacionesBancariasDAO.findPoolBancarioBD(tipoDocumento, codigo,
																	  tipoDeuda,codBanco,
																	  codtipoEmpresaGrupo,idPrograma);
			logger.info("cantidad opocion pool="+poolBancarios.size());	
			if (poolBancarios == null
					|| (poolBancarios != null && poolBancarios.size() == 0)) {
				//throw new BOException("No se econtraron Pool Bancarios");
			}
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
//			catch (BOException e) {
//			throw new BOException(e.getMessage());
//		}
		logger.info("FIN findPoolBancario");
		return poolBancarios;		
		
	}
	
	public void beforeUpdateRelacionesBancarias()throws BOException {		
					
	}
	
	
	
	public boolean validateUpdateRelacionesBancarias(){
		return true;
	}
	
	/**
	 * actuliza los Relacion Bancaria del programa financiero
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateRelacionesBancarias() throws BOException {
		beforeUpdateRelacionesBancarias();
		if(validateUpdateRelacionesBancarias()){
			List parametros = new ArrayList();			
			//Cuota Financiera;
			parametros.add(getPrograma().getCuotaFinanciera()==null?"":getPrograma().getCuotaFinanciera());
			//porcentajeNormalSF=?,porcentajeProblemaPotencialSF=?,porcentajeDeficienteSF=?,porcentajeDudosoSF=?,porcentajePerdidaSF=?,calificacionBanco=?
			//Calificacion Sistema Financiero;
			parametros.add(getPrograma().getPorcentajeNormalSF()==null?"":getPrograma().getPorcentajeNormalSF());			
			parametros.add(getPrograma().getPorcentajeProblemaPotencialSF()==null?"":getPrograma().getPorcentajeProblemaPotencialSF());
			parametros.add(getPrograma().getPorcentajeDeficienteSF()==null?"":getPrograma().getPorcentajeDeficienteSF());
			parametros.add(getPrograma().getPorcentajeDudosoSF()==null?"":getPrograma().getPorcentajeDudosoSF());
			parametros.add(getPrograma().getPorcentajePerdidaSF()==null?"":getPrograma().getPorcentajePerdidaSF());		
			//Calificaicon Banco
			parametros.add(getPrograma().getCalificacionBanco()==null?"":getPrograma().getCalificacionBanco());
			
			//Rentabilidad cartera
			parametros.add(getPrograma().getEfectividadProm6sol()==null?"":getPrograma().getEfectividadProm6sol());			
			parametros.add(getPrograma().getEfectividadProm6dol()==null?"":getPrograma().getEfectividadProm6dol());
			parametros.add(getPrograma().getProtestoProm6sol()==null?"":getPrograma().getProtestoProm6sol());
			parametros.add(getPrograma().getProtestoProm6dol()==null?"":getPrograma().getProtestoProm6dol());
			parametros.add(getPrograma().getEfectividadUltmaniosol()==null?"":getPrograma().getEfectividadUltmaniosol());		
			parametros.add(getPrograma().getEfectividadUltmaniodol()==null?"":getPrograma().getEfectividadUltmaniodol());		
			//comentario cuentafinanciera
			parametros.add(getPrograma().getComentcuotaFinanciera()==null?"":getPrograma().getComentcuotaFinanciera());		
			//tipo de modelo rentabilidad
			parametros.add(getPrograma().getIdmodeloRentabilidad()==null?"":getPrograma().getIdmodeloRentabilidad());		
				
			//indice Transaccional
			parametros.add(getPrograma().getSaldoMedioRecGest()==null?"":getPrograma().getSaldoMedioRecGest());		
			parametros.add(getPrograma().getCaja()==null?"":getPrograma().getCaja());		
			parametros.add(getPrograma().getFlujoTransaccional()==null?"":getPrograma().getFlujoTransaccional());		
			parametros.add(getPrograma().getVentaCostoVenta()==null?"":getPrograma().getVentaCostoVenta());		
			parametros.add(getPrograma().getFechaIndiceTransa()==null?"":getPrograma().getFechaIndiceTransa());		
									
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updateRelacionesBancarias",parametros);
			
			saveLineasRelacionesBancarias();
			savePoolBancario();
			saveRentabilidaBEC();
			saveComex();
			
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();

			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
			//super.saveCollection(listLineasRelacionesBancarias);
			
//			programaBlob.setComenLineas(comenLineas.getBytes());
//			programaBlob.setPrograma(programa);
			
//			programaBlobBO.setPrograma(programa);
//			programaBlobBO.setCampoBlob("comenLineas");
//			programaBlobBO.setValorBlob(comenLineas);
//			programaBlobBO.save(programaBlob);		
					
		}
		
		//afterUpdateRelacionesBancarias();
	}
	
	public void beforeLineasRelacionesBancarias(){
		int posicion = 0;
		for(LineasRelacionBancarias lrb : listLineasRelacionesBancarias){
			lrb.setPrograma(programa);
			lrb.setPosicion(posicion);
			posicion++;
		}
	}
	
	public boolean validationLineasRelacionesBancarias(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra las lineas de relaciones bancarias
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveLineasRelacionesBancarias() throws BOException{
		beforeLineasRelacionesBancarias();
		if(validationLineasRelacionesBancarias()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<LineasRelacionBancarias> listatemp =  findByParams2(LineasRelacionBancarias.class, params);
			List<LineasRelacionBancarias> listaDel = new ArrayList<LineasRelacionBancarias>();
			boolean flag= false;
			for(LineasRelacionBancarias lrb : listatemp ){
				for(LineasRelacionBancarias lrbtemp : listLineasRelacionesBancarias){
					if(lrbtemp.getId()!=null && lrbtemp.getId().equals(lrb.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lrb);
				}
				flag= false;
			}
			saveCollection(listLineasRelacionesBancarias);
			deleteCollection(listaDel);
			
			
			List parametros = new ArrayList();	
			parametros.add(getPrograma().getTipoMilesRB());	
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updateLineasRelBancarias",parametros);	
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaLineasRelaciones(List<LineasRelacionBancarias> olistLineasRelaciones ) throws BOException{
		saveCollection(olistLineasRelaciones);
	}
	
	public void beforeRentabilidaBEC(){
		if (listRentabilidadBEC!=null && listRentabilidadBEC.size()>0){
			for(RentabilidadBEC lrbec : listRentabilidadBEC){
				lrbec.setPrograma(programa);
			}
		}
	}
	
	public void beforeComex(){
		if (listaComex!=null && listaComex.size()>0){
			int cont=0;
			for(Comex ocomex : listaComex){
				cont=cont+1;
				ocomex.setPrograma(programa);
				ocomex.setPos(cont);				
			}
		}
	}
	
	public void beforeRentabilidaBECRefresh(Long idprograma ){
		Programa oprograma=new Programa();
		oprograma.setId(idprograma);
		if (listRentabilidadBEC!=null && listRentabilidadBEC.size()>0){
			for(RentabilidadBEC lrbec : listRentabilidadBEC){
				lrbec.setPrograma(oprograma);
			}
		}
	}
	public boolean validationRentabilidaBEC(){
		if (listRentabilidadBEC!=null && listRentabilidadBEC.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveRentabilidaBEC() throws BOException{
		beforeRentabilidaBEC();
		if(validationRentabilidaBEC()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<RentabilidadBEC> listatemp =  findByParams2(RentabilidadBEC.class, params);
			List<RentabilidadBEC> listaDel = new ArrayList<RentabilidadBEC>();
			boolean flag= false;
			for(RentabilidadBEC lrbec : listatemp ){
				for(RentabilidadBEC lrbectemp : listRentabilidadBEC){
					if(lrbectemp.getId()!=null && lrbectemp.getId().equals(lrbec.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lrbec);
				}
				flag= false;
			}
			saveCollection(listRentabilidadBEC);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//MCG20141103
	public boolean validationComex(){
		if (listaComex!=null && listaComex.size()>0){
			return true;
		}else{
			return false;
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveComex() throws BOException{
		beforeComex();
		if(validationComex()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<Comex> listatemp =  findByParams2(Comex.class, params);
			List<Comex> listaDel = new ArrayList<Comex>();
			boolean flag= false;
			for(Comex lcomex : listatemp ){
				for(Comex lcomextemp : listaComex){
					if(lcomextemp.getId()!=null && lcomextemp.getId().equals(lcomex.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lcomex);
				}
				flag= false;
			}
			saveCollection(listaComex);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//MCG20131103
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveRentabilidaBECrefresh(List<RentabilidadBEC> listRentabilidadBEC, String idPrograma) throws BOException{
		beforeRentabilidaBECRefresh(Long.parseLong(idPrograma));
		if(validationRentabilidaBEC()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", Long.parseLong(idPrograma));
			List<RentabilidadBEC> listatemp =  findByParams2(RentabilidadBEC.class, params);
			List<RentabilidadBEC> listaDel = new ArrayList<RentabilidadBEC>();
			boolean flag= false;
			for(RentabilidadBEC lrbec : listatemp ){
				for(RentabilidadBEC lrbectemp : listRentabilidadBEC){
					if(lrbectemp.getId()!=null && lrbectemp.getId().equals(lrbec.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lrbec);
				}
				flag= false;
			}			
			saveCollection(listRentabilidadBEC);	
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(Long.parseLong(idPrograma));
		}
	}
	
	//ini MCG20140611
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveRentabilidaBECrefresh(List<RentabilidadBEC> listRentabilidadBEC, String idPrograma,String codigoCentral) throws BOException{
		beforeRentabilidaBECRefresh(Long.parseLong(idPrograma));
		if(validationRentabilidaBEC()){
			HashMap<String,String> params = new HashMap<String,String>();
			//params.put("programa", Long.parseLong(idPrograma));
			params.put("programa", idPrograma);
			params.put("codEmpresaGrupo", codigoCentral);
			List<RentabilidadBEC> listatemp =  findByParams2(RentabilidadBEC.class, params);
			List<RentabilidadBEC> listaDel = new ArrayList<RentabilidadBEC>();
			boolean flag= false;
			for(RentabilidadBEC lrbec : listatemp ){
				for(RentabilidadBEC lrbectemp : listRentabilidadBEC){
					if(lrbectemp.getId()!=null && lrbectemp.getId().equals(lrbec.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lrbec);
				}
				flag= false;
			}			
			saveCollection(listRentabilidadBEC);	
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(Long.parseLong(idPrograma));
		}
	}
	//fin MCG20140611
	
	//Pool Bancario	
	
	
	
	public void beforePoolBancario() throws BOException{	
		
			String idBancos = "";
			String idprograma="";	
			String codtipoDeudas="";
			String codTipoEmpresaGrupo="";
			String idGrupo="";
			String rucEmpresaPrincipal="";				
			
			StringBuffer sb = new StringBuffer();
			listPoolBancario=new ArrayList<PoolBancario>();
			listOpcionPool=new ArrayList<OpcionPool>();
			try {
				codtipoDeudas = this.getCodTipoDeudas();
				
				String[] arrayTipoDeuda = codtipoDeudas.split(",");
				
				idprograma=getPrograma().getId().toString();
				codTipoEmpresaGrupo=getPrograma().getTipoEmpresa().getId().toString();
				idGrupo=getPrograma().getIdGrupo();			
				rucEmpresaPrincipal= getPrograma().getRuc()==null?"":getPrograma().getRuc().toString();
				Programa oprograma=new Programa();
				oprograma.setId(getPrograma().getId());
				
				if (listaBancoSelect != null && listaBancoSelect.size() > 0) {
					for (Tbanco bancoSelect : listaBancoSelect) {
						OpcionPool opcionPool=new OpcionPool();
						
						opcionPool.setTipoOpcionPool(Constantes.ID_TIPO_OPPOOL_BANCO);
						opcionPool.setPrograma(oprograma);
						opcionPool.setCodOpcionPool(bancoSelect.getCodBanco().toString());
						opcionPool.setDescOpcionPool(bancoSelect.getNombreBanco());
						listOpcionPool.add(opcionPool);
						idBancos = idBancos + bancoSelect.getCodBanco().toString()
								+ ",";
						
					}
					
					int pos = idBancos.length() - 1;
					idBancos = idBancos.substring(0, pos);
				}
				
				if (codtipoDeudas.length()>0){
					for (int m = 0; m < arrayTipoDeuda.length; m++) {
						OpcionPool opcionPool=new OpcionPool();	
						opcionPool.setTipoOpcionPool(Constantes.ID_TIPO_OPPOOL_TIPODEUDA);
						opcionPool.setPrograma(oprograma);
						//deuda total
						if (arrayTipoDeuda[m].toString().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {							
							opcionPool.setCodOpcionPool(arrayTipoDeuda[m].toString());
							opcionPool.setDescOpcionPool(Constantes.TIPO_DEUDA_TOTAL);																
						//deuda directa	
						} else if (arrayTipoDeuda[m].toString().equals(Constantes.ID_TIPO_DEUDA_DIRECTA)) {											
							opcionPool.setCodOpcionPool(arrayTipoDeuda[m].toString());
							opcionPool.setDescOpcionPool(Constantes.TIPO_DEUDA_DIRECTA);												
						//deuda indirecta	
						} else if (arrayTipoDeuda[m].toString().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {												
							opcionPool.setCodOpcionPool(arrayTipoDeuda[m].toString());
							opcionPool.setDescOpcionPool(Constantes.TIPO_DEUDA_INDIRECTA);
						}						
						listOpcionPool.add(opcionPool);
					}
				}
				
				//add deuda por Grupo si el programa es para Grupo
				if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
									
					if (idBancos.length()>0){
						if (codtipoDeudas.length()>0){
							List<Trccmes> listrrcmes= null;							
							listrrcmes=relacionesBancariasDAO.findByPoolUsingFunction(Constantes.TIPO_DOCUMENTO_RUC,rucEmpresaPrincipal,"",idBancos,Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),idprograma);
							if (listrrcmes!=null && listrrcmes.size()>0){
								
								//Obtencion de ID de empresa
								List<Empresa> listEmpresatmpg= null;
								Empresa oTempresa=new Empresa();
								listEmpresatmpg =programaBO.findEmpresaByIdprogramaRuc(idprograma,rucEmpresaPrincipal);	
								if (listEmpresatmpg!=null && listEmpresatmpg.size()>0) {									
									Long idempresa= listEmpresatmpg.get(0).getId();
									oTempresa.setId(idempresa);
								}				
								
								String  deuda="0";
								for (Trccmes otrccmes:listrrcmes){	
									for (int k = 0; k < arrayTipoDeuda.length; k++) {
										PoolBancario poolBancario=new PoolBancario();
										poolBancario.setEmpresa(oTempresa);
										poolBancario.setIdbanco(otrccmes.getCodbanco());
										poolBancario.setPrograma(oprograma);
										poolBancario.setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
										poolBancario.setIdGrupo(idGrupo);
										poolBancario.setMes(otrccmes.getMes());
										poolBancario.setAnio(otrccmes.getAnio());
										//deuda total
										if (arrayTipoDeuda[k].toString().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {		
											poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_TOTAL);
											deuda=(otrccmes.getDeudatotal()!=null&&!"".equals(otrccmes.getDeudatotal()))?otrccmes.getDeudatotal():"0";
											poolBancario.setTotal(new Double(deuda));
										//deuda directa	
										} else if (arrayTipoDeuda[k].toString().equals(Constantes.ID_TIPO_DEUDA_DIRECTA)) {	
											poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_DIRECTA);
											deuda=(otrccmes.getDeudadirecta()!=null&&!"".equals(otrccmes.getDeudadirecta()))?otrccmes.getDeudadirecta():"0";
											poolBancario.setTotal(new Double(deuda));												
										//deuda indirecta	
										} else if (arrayTipoDeuda[k].toString().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {	
											poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_INDIRECTA);
											deuda=(otrccmes.getDeudaindirecta()!=null&&!"".equals(otrccmes.getDeudaindirecta()))?otrccmes.getDeudaindirecta():"0";
											poolBancario.setTotal(new Double(deuda));	
										}
										listPoolBancario.add(poolBancario);
									}							
								}
							}
						}						
					}
				}		
				
				//hacer unico las lista de empresa
				listaEmpresaSelect=getlistaEmpresaSelectUnico(getListaEmpresaSelect());
								
				//Add Deuda por Empresa			
				if (listaEmpresaSelect != null && listaEmpresaSelect.size() > 0) {
				
					for (Tempresa empresaSelect : listaEmpresaSelect) {						
					    OpcionPool opcionPool=new OpcionPool();						
						opcionPool.setTipoOpcionPool(Constantes.ID_TIPO_OPPOOL_EMPRESA);
						opcionPool.setPrograma(oprograma);
						opcionPool.setCodOpcionPool(empresaSelect.getCodEmpresa().toString());
						opcionPool.setDescOpcionPool(empresaSelect.getNomEmpresa());
						listOpcionPool.add(opcionPool);
						
							if (idBancos.length()>0){
								if (codtipoDeudas.length()>0){
									List<Trccmes> listrrcmese= null;									
									List<Empresa> listEmpresatmp= null;	
									
									//Obtencion de ID de empresa
									Empresa oTempresa=new Empresa();	
									listEmpresatmp =programaBO.findEmpresaByIdprogramaRuc(idprograma, empresaSelect.getCodEmpresa());	
									if (listEmpresatmp!=null && listEmpresatmp.size()>0) {									
										Long idempresa= listEmpresatmp.get(0).getId();
										oTempresa.setId(idempresa);
									}	
									logger.info("ini findByPoolUsingFunction: ");
									logger.info("findByPoolUsingFunction:getCodEmpresa " + empresaSelect.getCodEmpresa());
									logger.info("findByPoolUsingFunction:idBancos " + idBancos);
									logger.info("findByPoolUsingFunction:idprograma " + idprograma);
									listrrcmese=relacionesBancariasDAO.findByPoolUsingFunction(Constantes.TIPO_DOCUMENTO_RUC,empresaSelect.getCodEmpresa(),"",idBancos,Constantes.ID_TIPO_EMPRESA_EMPR.toString(),idprograma);
									logger.info("fin findByPoolUsingFunction: ");
									if (listrrcmese!=null && listrrcmese.size()>0){
										for (Trccmes otrccmese:listrrcmese){	
											for (int j = 0; j < arrayTipoDeuda.length; j++) {
												PoolBancario poolBancario=new PoolBancario();
												poolBancario.setIdbanco(otrccmese.getCodbanco());
												poolBancario.setEmpresa(oTempresa);
												poolBancario.setPrograma(oprograma);
												poolBancario.setMes(otrccmese.getMes());
												poolBancario.setAnio(otrccmese.getAnio());
												
//												if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
//													poolBancario.setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_GRUPO.toString());
//													//poolBancario.setIdGrupo(idGrupo);
//												}else{
													poolBancario.setTipoEmpresa(Constantes.ID_TIPO_EMPRESA_EMPR.toString());												
//												}
												//deuda total
												if (arrayTipoDeuda[j].toString().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {												
													poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_TOTAL);
													poolBancario.setTotal(new Double(otrccmese.getDeudatotal()));												
												//deuda directa	
												} else if (arrayTipoDeuda[j].toString().equals(Constantes.ID_TIPO_DEUDA_DIRECTA)) {											
													poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_DIRECTA);
													poolBancario.setTotal(new Double(otrccmese.getDeudadirecta()));												
												//deuda indirecta	
												} else if (arrayTipoDeuda[j].toString().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {												
													poolBancario.setTipoDeuda(Constantes.ID_TIPO_DEUDA_INDIRECTA);
													poolBancario.setTotal(new Double(otrccmese.getDeudaindirecta()));	
												}
												listPoolBancario.add(poolBancario);
											}
											logger.info("documento: " + listrrcmese.size());
											logger.info("documento: " + listrrcmese.get(0).getCodEmpresa());
											logger.info("deuda total: " + listrrcmese.get(0).getDeudatotal().toString());
										}
									}
								}						
							}					
					}				
				}
				 if (listPoolBancario!=null && listPoolBancario.size()>0){					 
					logger.info("listPoolBancario: " + listPoolBancario.size());
					//logger.info("listPoolBancario getIdbanco: " + listPoolBancario.get(0).getIdbanco().toString());
				}
				 
				 
			} catch (BOException e) {
				throw new BOException(e.getMessage());			
			} catch (Exception e) {					
				e.printStackTrace();
				throw new BOException(e.getMessage());
			}	
	}
	
	private List<Tempresa>  getlistaEmpresaSelectUnico(List<Tempresa> listaEmpresaSelect ){
		List<Tempresa> olistaFiltro=new ArrayList<Tempresa>();
		try {
			
			if (listaEmpresaSelect!=null && listaEmpresaSelect.size()>0){
				for (Tempresa empresaSelect : listaEmpresaSelect) {	
					if (olistaFiltro.size()==0){
						olistaFiltro.add(empresaSelect);				
					}else{
						if (!ExisteEmpresaFiltro(empresaSelect,olistaFiltro)){
							olistaFiltro.add(empresaSelect);
						}
					}	
				}			
			}else{
				olistaFiltro=listaEmpresaSelect;
			}
		} catch (Exception e) {
			olistaFiltro=listaEmpresaSelect;
		}
		
		return olistaFiltro;
	}
	
	
	private boolean ExisteEmpresaFiltro(Tempresa oempresa,List<Tempresa> olistaFiltro){
		boolean existe=false;
		try {
			for (Tempresa empresafiltro: olistaFiltro) {	
				if (oempresa.getCodEmpresa()!=null && oempresa.getCodEmpresa().equals(empresafiltro.getCodEmpresa())){
					existe=true;
				}				
			}
		} catch (Exception e) {
			existe=false;
		}
		return existe;
	}
	
	public boolean validationPoolBancario(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra el pool bancario
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void savePoolBancario() throws BOException{
		beforePoolBancario();
		if(validationPoolBancario()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<PoolBancario> listatemp =  findByParams2(PoolBancario.class, params);
			List<PoolBancario> listaDel = new ArrayList<PoolBancario>();
			
			List<OpcionPool> listatemppool =  findByParams2(OpcionPool.class, params);
			List<OpcionPool> listaDelpool = new ArrayList<OpcionPool>();
			
			if (listatemp!=null && listatemp.size()>0) {
				listaDel=listatemp;
			}
			
			if (listatemppool!=null && listatemppool.size()>0) {
				listaDelpool=listatemppool;
			}
			
			saveCollection(listPoolBancario);
			deleteCollection(listaDel);
			
			saveCollection(listOpcionPool);
			deleteCollection(listaDelpool);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	///BANCOS
	@Override
	public List<Tbanco> findBancosPoolBancarioSQL(String idPrograma,String codEmpresa,String tipoEmpresa) throws BOException 
	{
		logger.info("inicio findBancosPoolBancarioSQL");
		List<Tbanco> listBancosPool = new ArrayList<Tbanco>();
		Tbanco otbanco = null;		
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();	
			if (tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				
				sb.append(" SELECT nro_entidad_bancaria CODIGO,T.ABREVIADO   ||' ('||to_char(sum(RCC.DEUDA_TOTAL), '999,999,999,999.99')||')' ABREVIADO");
				sb.append("  FROM PROFIN.tiipf_pfrccmes RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T ");
				sb.append("   ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
				sb.append("   where tipo_documento='R' ");
				sb.append("   and documento = '"+ codEmpresa +"' and RCC.DEUDA_TOTAL >0");
				sb.append("   group by nro_entidad_bancaria,T.ABREVIADO ");
				sb.append("   UNION ");
				sb.append("   SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ||' ('||to_char(0)||')' ABREVIADO ");
				sb.append("   FROM PROFIN.tiipf_pfrccan RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T ");
				sb.append("   ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
				sb.append("   where tipo_documento='R'  ");
				sb.append("   and documento ='"+ codEmpresa+"'" );
				sb.append("   and RCC.nro_entidad_bancaria not in (SELECT DISTINCT nro_entidad_bancaria ");
				sb.append("   FROM PROFIN.tiipf_pfrccmes RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T ");
				sb.append("   ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
				sb.append("   where tipo_documento='R' ");
				sb.append("   and documento ='"+ codEmpresa + "' and RCC.DEUDA_TOTAL >0)  and RCC.deuda_total>0 ");
				
				
			}else{
				
				sb.append(" SELECT nro_entidad_bancaria CODIGO,T.ABREVIADO ||' ('||to_char(sum(RCC.DEUDA_TOTAL),'999,999,999,999.99')||')' ABREVIADO");
				sb.append(" FROM PROFIN.tiipf_pfrccmes RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T ");
				sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
				sb.append(" where tipo_documento='R' "); 
				sb.append(" and documento in (select distinct ruc from PROFIN.tiipf_empresa where iipf_programa_id=" + idPrograma + ")  and RCC.DEUDA_TOTAL >0 "); 
				sb.append(" group by nro_entidad_bancaria,T.ABREVIADO ");
				sb.append(" UNION  ");
				sb.append(" SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ||' ('||to_char(0)||')' ABREVIADO ");
				sb.append(" FROM PROFIN.tiipf_pfrccan RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T  ");
				sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16  ");
				sb.append(" where tipo_documento='R' ");
				sb.append(" and documento in (select distinct ruc from PROFIN.tiipf_empresa where iipf_programa_id=" + idPrograma + ")  ");
				sb.append(" and RCC.nro_entidad_bancaria not in (SELECT distinct nro_entidad_bancaria ");
				sb.append(" FROM PROFIN.tiipf_pfrccmes RCC INNER JOIN PROFIN.tiipf_tabla_de_tabla T  ");
				sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
				sb.append(" where tipo_documento='R'   ");
			    sb.append(" and documento in (select distinct ruc from PROFIN.tiipf_empresa where iipf_programa_id=" + idPrograma + ") and RCC.DEUDA_TOTAL >0)  and RCC.DEUDA_TOTAL >0 ");
				
			}
//			sb.append(" SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ABREVIADO ");
//			sb.append(" FROM PROFIN.iipf_pfrccmes RCC INNER JOIN PROFIN.iipf_tabla_de_tabla T ");
//			sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
//			sb.append(" where tipo_documento='R'  ");
//			sb.append(" and documento = "+ codEmpresa );
//			sb.append(" UNION ");
//			sb.append(" SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ABREVIADO ");
//			sb.append(" FROM PROFIN.iipf_pfrccan RCC INNER JOIN PROFIN.iipf_tabla_de_tabla T ");
//			sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
//			sb.append(" where tipo_documento='R' "); 
//			sb.append(" and documento = "+ codEmpresa);
//			sb.append(" UNION ");
//			sb.append(" SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ABREVIADO ");
//			sb.append(" FROM PROFIN.iipf_pfrccmes RCC INNER JOIN PROFIN.iipf_tabla_de_tabla T ");
//			sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
//			sb.append(" where tipo_documento='R'  ");
//			sb.append(" and documento in (select distinct ruc from PROFIN.iipf_empresa where iipf_programa_id=" + idPrograma + ")  ");
//			sb.append(" UNION ");
//			sb.append(" SELECT DISTINCT nro_entidad_bancaria CODIGO,T.ABREVIADO ABREVIADO ");
//			sb.append(" FROM PROFIN.iipf_pfrccan RCC INNER JOIN PROFIN.iipf_tabla_de_tabla T ");
//			sb.append(" ON   RCC.nro_entidad_bancaria=T.CODIGO AND id_tabla_de_tabla=16 ");
//			sb.append(" where tipo_documento='R' ");
//			sb.append(" and documento in (select distinct ruc from PROFIN.iipf_empresa where iipf_programa_id=" + idPrograma + ") ");
//			
			
			
			logger.info(" LISTBP sb.toString(): "+ sb.toString());
			List listbp= super.executeSQL(sb.toString());
			logger.info("cantidad de resultados POOL BANCARIO="+listbp.size());			
			listBancosPool = new ArrayList<Tbanco>();
			
			for (Iterator it = listbp.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				logger.info("amount="+amount);
				otbanco= new Tbanco();				
				if(amount[0] != null){
					otbanco.setCodBanco(amount[0].toString());}				
				if(amount[1] != null){
					otbanco.setNombreBanco(amount[1].toString());}
							
				listBancosPool.add(otbanco);
			}
		} catch (Exception e) {					
			logger.error(StringUtil.getStackTrace(e));
			listBancosPool = new ArrayList<Tbanco>();
			//throw new BOException(e.getMessage());
		}
		logger.info("fin findBancosPoolBancarioSQL");
		return listBancosPool;
		
	}
	@Override
	public String obtenerFechaCuotaFinancieraEmpresa(Programa programa) throws BOException{
		String fechaArchivo="";
		StringBuilder queryFechaCuotaEmpresa = new StringBuilder();
		
		queryFechaCuotaEmpresa.append("select distinct aniomes from PROFIN.tiipf_pfrccmes where documento = '");
		queryFechaCuotaEmpresa.append(programa.getRuc());
		queryFechaCuotaEmpresa.append("'");
		
		List listaFecha = null;
		listaFecha = super.executeSQL(queryFechaCuotaEmpresa.toString());
		if(listaFecha!=null && !listaFecha.isEmpty()){
			try{
				fechaArchivo = listaFecha.get(0).toString();
				fechaArchivo = fechaArchivo.substring(4, 6)+"/"+fechaArchivo.substring(0, 4);
			}catch (Exception e) {
				fechaArchivo = "";
			}
		}
		
		
		return fechaArchivo;
	}
	@Override
	public double calcularCuotaFinancieraEmpresa(Programa programa) throws BOException{
		double totalEmpresa = 0;
		double totalGeneral = 0;
		try {
			
			String rucEmpresa="";
			
			if (programa.getRuc()!=null){
				rucEmpresa=programa.getRuc();
			}else{
				rucEmpresa="1";
			}
			
			String queryTotalEmpresa = QUERY_CUOTA + 
			   "where to_number( nro_entidad_bancaria) =to_number( '" +
			   Constantes.COD_BBVA +
			   "') and documento = '" +
			   rucEmpresa +
			   "'";
			String queryTotalGeneral = QUERY_CUOTA + 
			   "where  documento = '" +
			   rucEmpresa +
			   "'";
			List listaEmpre = null;
			List listaGeneral = null;
			listaEmpre = super.executeSQL(queryTotalEmpresa);
			if(listaEmpre != null && !listaEmpre.isEmpty()){
				totalEmpresa = Double.parseDouble(listaEmpre.get(0).toString());
			}
			listaGeneral = super.executeSQL(queryTotalGeneral);
			if(listaGeneral != null && !listaGeneral.isEmpty()){
				totalGeneral = Double.parseDouble(listaGeneral.get(0).toString());
			}
		} catch (BOException e) {
			throw new BOException(e);
		}
		return FormatUtil.round(totalEmpresa/totalGeneral,2);
	}
	@Override
	public double calcularCuotaFinancieraGrupo(Programa programa, 
											   List<Empresa> listaEmpresas) throws BOException{
		double totalGrupo = 0;
		double totalGeneral = 0;
		String codEmpresas = "";
		boolean flagPrimero = true;
		for(Empresa empresa : listaEmpresas){
			if(flagPrimero){
				codEmpresas	= codEmpresas+
							  "'"+ 
							  empresa.getRuc()+
							  "'";
				flagPrimero = false;
			}else{
				codEmpresas	= codEmpresas+
							  ",'"+
							  empresa.getRuc()+
							  "'";
			}
		}
		try {
			String queryTotalEmpresa = QUERY_CUOTA + 
									   " where to_number( nro_entidad_bancaria) = to_number( '" +
									   Constantes.COD_BBVA +
									   "') and documento in(" +
									   codEmpresas+
									   ")";
			String queryTotalGeneral = QUERY_CUOTA + 
									   " where  documento in(" +
									   codEmpresas+
									   ")";
			List listaEmpre = null;
			List listaGeneral = null;
			listaEmpre = super.executeSQL(queryTotalEmpresa);
			if(listaEmpre != null && !listaEmpre.isEmpty()){
				totalGrupo = Double.parseDouble(listaEmpre.get(0).toString());
			}
			listaGeneral = super.executeSQL(queryTotalGeneral);
			if(listaGeneral != null && !listaGeneral.isEmpty()){
				totalGeneral = Double.parseDouble(listaGeneral.get(0).toString());
			}
		} catch (BOException e) {
			throw new BOException(e);
		}
		return FormatUtil.round(totalGrupo/totalGeneral,2);		
	}
	@Override
	public List<RentabilidadBEC> generaModeloRentabilidad(String idPrograma){			
			
			List<RentabilidadBEC> listRentabilidaBECtmp= new ArrayList<RentabilidadBEC>();
			
			try {		
			listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
			
			//iyeccion de variable listRentabilidaBECtmp
			//listRentabilidaBECtmp = null;
			listRentabilidaBECtmp =findRentabilidadBEC(idPrograma);		
//			Programa programatemp=new Programa();		
//			programatemp=programaBO.findById(Long.valueOf(idPrograma));		
//			String codCentral=programatemp.getIdEmpresa();
			String codCentral="";
			List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();		
			List<RentabilidadBEC> olistaRentabilidadEmpresa=new ArrayList<RentabilidadBEC>();
			listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idPrograma));	

			
				if (listRentabilidaBECtmp != null && listRentabilidaBECtmp.size() > 0) {			
					listRentabilidadBEC=listRentabilidaBECtmp;
					
				}else{
					//codCentral="00698440";
					if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {						
						for (Empresa oEmpresa:listEmpresaBEC){							
							codCentral=oEmpresa.getCodigo();
							olistaRentabilidadEmpresa=new ArrayList<RentabilidadBEC>();
							
							olistaRentabilidadEmpresa=ObtenerRentabilidadOfFileHost(idPrograma,codCentral);	
							if (olistaRentabilidadEmpresa!=null && olistaRentabilidadEmpresa.size()>0) {
								listRentabilidadBEC.addAll(olistaRentabilidadEmpresa);
							}
						}
					}
				}		
				
			} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			return listRentabilidadBEC;
			}
			return listRentabilidadBEC;
			
	}
	
	//Ini MCG20140610
	@Override
	public List<RentabilidadBEC> generaModeloRentabilidad(String idPrograma,String codigoCentral){			
		
		List<RentabilidadBEC> listRentabilidaBECtmp= new ArrayList<RentabilidadBEC>();
		
		try {		
		listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
		
		listRentabilidaBECtmp =findRentabilidadBEC(idPrograma,codigoCentral);		
//		Programa programatemp=new Programa();		
//		programatemp=programaBO.findById(Long.valueOf(idPrograma));		
		String codCentral=codigoCentral;
		
			if (listRentabilidaBECtmp != null && listRentabilidaBECtmp.size() > 0) {			
				listRentabilidadBEC=listRentabilidaBECtmp;
				
			}else{
				//codCentral="00698440";
				listRentabilidadBEC=ObtenerRentabilidadOfFileHost(idPrograma,codCentral);
			}		
			
		} catch (Exception e) {
		logger.error(StringUtil.getStackTrace(e));
		return listRentabilidadBEC;
		}
		return listRentabilidadBEC;
		
	}
	//Fin MCG20140610
	@Override
	public List<RentabilidadBEC> ObtenerRentabilidad(String idPrograma){
		try{
//		Programa programatemp=new Programa();
//		programatemp=programaBO.findById(Long.valueOf(idPrograma));		
//		String codCentral=programatemp.getIdEmpresa();
			
			String codCentral="";
			List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();		
			List<RentabilidadBEC> listRentabilidadBEC=new ArrayList<RentabilidadBEC>();
			List<RentabilidadBEC> olistaRentabilidadEmpresa=new ArrayList<RentabilidadBEC>();
			listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idPrograma));
			if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {						
				for (Empresa oEmpresa:listEmpresaBEC){							
					codCentral=oEmpresa.getCodigo();
					olistaRentabilidadEmpresa=new ArrayList<RentabilidadBEC>();
					
					olistaRentabilidadEmpresa=ObtenerRentabilidadOfFileHost(idPrograma, codCentral);
					if (olistaRentabilidadEmpresa!=null && olistaRentabilidadEmpresa.size()>0) {
						listRentabilidadBEC.addAll(olistaRentabilidadEmpresa);
					}
				}
			}
			
			//return ObtenerRentabilidad(idPrograma, codCentral);
			return listRentabilidadBEC;
			
			
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
		return null;
	}
	
	//ini MCG20140611
	@Override
	public List<RentabilidadBEC> ObtenerRentabilidadByEmpresa(String idPrograma,String codCentral){
		try{

			return ObtenerRentabilidadOfFileHost(idPrograma, codCentral);
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
		return null;
	}
	//fin MCG20140611
	
	private List<RentabilidadBEC> ObtenerRentabilidadOfFileHost(String idPrograma,String codCentral){
			try{
			List<Rentabilidad> listRentabilidadMensual= new ArrayList<Rentabilidad>();
			List<Rentabilidad> listRentabilidadAnual= new ArrayList<Rentabilidad>();
			List<RentabilidadBEC> listRentabilidadBECtmpIni= new ArrayList<RentabilidadBEC>();
			
			listRentabilidadMensual = findRentabilidad(codCentral,"M");
			listRentabilidadAnual = findRentabilidad(codCentral,"A");
			Rentabilidad orentabilidadMensual=new Rentabilidad();
			Rentabilidad orentabilidadAnual=new Rentabilidad();
			listRentabilidadBEC=new ArrayList<RentabilidadBEC>(); 

			if ((listRentabilidadMensual!=null && listRentabilidadMensual.size()>0)&&(listRentabilidadAnual!=null && listRentabilidadAnual.size()>0)){
				
				orentabilidadMensual=listRentabilidadMensual.get(0);
				orentabilidadAnual=listRentabilidadAnual.get(0);
				
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_INVERSIONES	,orentabilidadAnual.getSalmedioInversionAcu()	,orentabilidadMensual.getSalmedioInversionAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_RECURSOS	,orentabilidadAnual.getSalmedioRecursoAcu()		,orentabilidadMensual.getSalmedioRecursoAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_DESINTERMED	,orentabilidadAnual.getSalmedioDesintermedAcu() ,orentabilidadMensual.getSalmedioDesintermedAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.IMPORTE_SERVICIO		,orentabilidadAnual.getImporteServicioAcu()		,orentabilidadMensual.getImporteServicioAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.CUENTA_CENTRAL			,orentabilidadAnual.getCuentaCentralAcu()		,orentabilidadMensual.getCuentaCentralAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_INVERSION		,orentabilidadAnual.getSpfmesInversion()		,orentabilidadMensual.getSpfmesInversion(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_RECURSOS		,orentabilidadAnual.getSpfmesRecurso()			,orentabilidadMensual.getSpfmesRecurso(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_DESINTERMED		,orentabilidadAnual.getSpfmesDesintermed()		,orentabilidadMensual.getSpfmesDesintermed(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_FINANCIERO	,orentabilidadAnual.getMargenFinancieroAcu()	,orentabilidadMensual.getMargenFinancieroAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COMISIONES			,orentabilidadAnual.getComisionesAcu()			,orentabilidadMensual.getComisionesAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_ORDINARIO	,orentabilidadAnual.getMargenOrdinarioAcu()		,orentabilidadMensual.getMargenOrdinarioAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.SANEAMIENTO_NETO	,orentabilidadAnual.getSaneamientoNetoAcu()		,orentabilidadMensual.getSaneamientoNetoAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COSTES_OPERATIVOS	,orentabilidadAnual.getCostesOperativoAcu()		,orentabilidadMensual.getCostesOperativoAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.RESULTADOS			,orentabilidadAnual.getResultadoAcu()			,orentabilidadMensual.getResultadoAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.ROA					,orentabilidadAnual.getRoaAcu()					,orentabilidadMensual.getRoaAcu(),orentabilidadAnual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBEC=listRentabilidadBECtmpIni;	
				
			}else if(listRentabilidadMensual!=null && listRentabilidadMensual.size()>0){
				
				orentabilidadMensual=listRentabilidadMensual.get(0);
				
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_INVERSIONES	,null		,orentabilidadMensual.getSalmedioInversionAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_RECURSOS	,null		,orentabilidadMensual.getSalmedioRecursoAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_DESINTERMED	,null 		,orentabilidadMensual.getSalmedioDesintermedAcu()			,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.IMPORTE_SERVICIO		,null		,orentabilidadMensual.getImporteServicioAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.CUENTA_CENTRAL			,null		,orentabilidadMensual.getCuentaCentralAcu()					,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_INVERSION		,null		,orentabilidadMensual.getSpfmesInversion()					,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_RECURSOS		,null		,orentabilidadMensual.getSpfmesRecurso()					,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_DESINTERMED		,null		,orentabilidadMensual.getSpfmesDesintermed()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_FINANCIERO	,null		,orentabilidadMensual.getMargenFinancieroAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COMISIONES			,null		,orentabilidadMensual.getComisionesAcu()					,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_ORDINARIO	,null		,orentabilidadMensual.getMargenOrdinarioAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.SANEAMIENTO_NETO	,null		,orentabilidadMensual.getSaneamientoNetoAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COSTES_OPERATIVOS	,null		,orentabilidadMensual.getCostesOperativoAcu()				,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.RESULTADOS			,null		,orentabilidadMensual.getResultadoAcu()						,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.ROA					,null		,orentabilidadMensual.getRoaAcu()							,orentabilidadMensual.getFechaProceso(),orentabilidadMensual.getFechaProceso(),codCentral));
				listRentabilidadBEC=listRentabilidadBECtmpIni;	
				
			}else if(listRentabilidadAnual!=null && listRentabilidadAnual.size()>0){
				
				orentabilidadAnual=listRentabilidadAnual.get(0);
				
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_INVERSIONES	,orentabilidadAnual.getSalmedioInversionAcu()	,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_RECURSOS	,orentabilidadAnual.getSalmedioRecursoAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_DESINTERMED	,orentabilidadAnual.getSalmedioDesintermedAcu() ,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.IMPORTE_SERVICIO		,orentabilidadAnual.getImporteServicioAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.CUENTA_CENTRAL			,orentabilidadAnual.getCuentaCentralAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_INVERSION		,orentabilidadAnual.getSpfmesInversion()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_RECURSOS		,orentabilidadAnual.getSpfmesRecurso()			,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_DESINTERMED		,orentabilidadAnual.getSpfmesDesintermed()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_FINANCIERO	,orentabilidadAnual.getMargenFinancieroAcu()	,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COMISIONES			,orentabilidadAnual.getComisionesAcu()			,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_ORDINARIO	,orentabilidadAnual.getMargenOrdinarioAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.SANEAMIENTO_NETO	,orentabilidadAnual.getSaneamientoNetoAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.COSTES_OPERATIVOS	,orentabilidadAnual.getCostesOperativoAcu()		,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.RESULTADOS			,orentabilidadAnual.getResultadoAcu()			,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBECtmpIni.add(ObtenerRentabilidad(idPrograma,Constantes.ID_TIPO_RENTABILIDAD,Constantes.ROA					,orentabilidadAnual.getRoaAcu()					,null,orentabilidadAnual.getFechaProceso(),orentabilidadAnual.getFechaProceso(),codCentral));
				listRentabilidadBEC=listRentabilidadBECtmpIni;	
			}
			
						
			}catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}
			return listRentabilidadBEC;
	}
	
	public RentabilidadBEC ObtenerRentabilidad(String idPrograma,String tipo,String descMonto,String MontoAnual, String Monto, Date fechaProcesoAnual, Date fechaProcesoMensual,String codigoCentral){
		
		RentabilidadBEC oRentabilidadBEC=new RentabilidadBEC();
		Programa oprograma=new Programa();
		oprograma.setId(Long.valueOf(idPrograma));	
		oRentabilidadBEC.setId(null);	
		oRentabilidadBEC.setPrograma(oprograma);	
		oRentabilidadBEC.setCodEmpresaGrupo(codigoCentral);
		oRentabilidadBEC.setTipoRentabilidad(tipo);
		oRentabilidadBEC.setDescripcionMonto(descMonto);
		oRentabilidadBEC.setMontoAnual(MontoAnual==null?"":MontoAnual);
		oRentabilidadBEC.setMonto(Monto==null?"":Monto);
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		oRentabilidadBEC.setFechaProcesoAnual(sdf.format(fechaProcesoAnual));
		oRentabilidadBEC.setFechaProcesoMensual(sdf.format(fechaProcesoMensual));
		
		return oRentabilidadBEC;
	}
	
	@Override
	public List<RentabilidadBEC> crearRentabilidadGrupo(List<RentabilidadBEC> listRentabilidadBECTodoEmp,String codGrupo,String idPrograma){
		
		List<RentabilidadBEC> olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
		
		//
		float totalAnualSalmedioInversionAcu=0; 	float totalMensualSalmedioInversionAcu=0;
		float totalAnualSalmedioRecursoAcu=0; 		float totalMensualSalmedioRecursoAcu=0;
		float totalAnualSalmedioDesintermedAcu=0; 	float totalMensualSalmedioDesintermedAcu=0;
		float totalAnualImporteServicioAcu=0; 		float totalMensualImporteServicioAcu=0;
		float totalAnualCuentaCentralAcu=0; 		float totalMensualCuentaCentralAcu=0;
		float totalAnualSpfmesInversion=0; 			float totalMensualSpfmesInversion=0;
		float totalAnualSpfmesRecurso=0; 			float totalMensualSpfmesRecurso=0;
		float totalAnualSpfmesDesintermed=0; 		float totalMensualSpfmesDesintermed=0;
		float totalAnualMargenFinancieroAcu=0; 		float totalMensualMargenFinancieroAcu=0;
		float totalAnualComisionesAcu=0; 			float totalMensualComisionesAcu=0;
		float totalAnualMargenOrdinarioAcu=0; 		float totalMensualMargenOrdinarioAcu=0;
		float totalAnualSaneamientoNetoAcu=0; 		float totalMensualSaneamientoNetoAcu=0;
		float totalAnualCostesOperativoAcu=0; 		float totalMensualCostesOperativoAcu=0;
		float totalAnualResultadoAcu=0; 			float totalMensualResultadoAcu=0;
		float totalAnualRoaAcu=0; 					float totalMensualRoaAcu=0;	
	 
		//
	
		int  existeGrupo=0;
		String strperiodoAnual=null;
		String strperiodoMensual=null;
		
		try {
			
			if (listRentabilidadBECTodoEmp != null && listRentabilidadBECTodoEmp.size() > 0) {			
				for (RentabilidadBEC orentabilidadbec: listRentabilidadBECTodoEmp) {
					if ( orentabilidadbec.getCodEmpresaGrupo().equals(codGrupo)){					
						existeGrupo++;
						olistRentabilidadBECGrupo.add(orentabilidadbec);
					}				
				}
				if (existeGrupo==0){
					for (RentabilidadBEC orentabilidadbec: listRentabilidadBECTodoEmp) {
						
						if(strperiodoAnual==null){						
							strperiodoAnual=orentabilidadbec.getFechaProcesoAnual()==null?"":orentabilidadbec.getFechaProcesoAnual();
						}
						if(strperiodoMensual==null){						
							strperiodoMensual=orentabilidadbec.getFechaProcesoMensual()==null?"":orentabilidadbec.getFechaProcesoMensual();
						}
						
						if (Constantes.SALDOMEDIO_INVERSIONES.equals(orentabilidadbec.getDescripcionMonto())){					
							totalAnualSalmedioInversionAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 	
							totalMensualSalmedioInversionAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));						
							
						}else if (Constantes.SALDOMEDIO_RECURSOS.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualSalmedioRecursoAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		 
							totalMensualSalmedioRecursoAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.SALDOMEDIO_DESINTERMED.equals(orentabilidadbec.getDescripcionMonto())){	  
							totalAnualSalmedioDesintermedAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));	 
							totalMensualSalmedioDesintermedAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.IMPORTE_SERVICIO.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualImporteServicioAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));
							totalMensualImporteServicioAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.CUENTA_CENTRAL.equals(orentabilidadbec.getDescripcionMonto())){	
							totalAnualCuentaCentralAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		
							totalMensualCuentaCentralAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.SPFMES_INVERSION.equals(orentabilidadbec.getDescripcionMonto())){
	
							totalAnualSpfmesInversion+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 			
							totalMensualSpfmesInversion+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.SPFMES_RECURSOS.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualSpfmesRecurso+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 			
							totalMensualSpfmesRecurso+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.SPFMES_DESINTERMED.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualSpfmesDesintermed+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		
							totalMensualSpfmesDesintermed+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.MARGEN_FINANCIERO.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualMargenFinancieroAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		
							totalMensualMargenFinancieroAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.COMISIONES.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualComisionesAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));
							totalMensualComisionesAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.MARGEN_ORDINARIO.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualMargenOrdinarioAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		
							totalMensualMargenOrdinarioAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.SANEAMIENTO_NETO.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualSaneamientoNetoAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual())); 		
							totalMensualSaneamientoNetoAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.COSTES_OPERATIVOS.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualCostesOperativoAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));		
							totalMensualCostesOperativoAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.RESULTADOS.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualResultadoAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));			
							totalMensualResultadoAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						
						}else if (Constantes.ROA.equals(orentabilidadbec.getDescripcionMonto())){
							totalAnualRoaAcu+=orentabilidadbec.getMontoAnual()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMontoAnual()));					
							totalMensualRoaAcu+=orentabilidadbec.getMonto()==null?0:Float.valueOf(FormatUtil.FormatNumeroSinComaEmpty(orentabilidadbec.getMonto()));
						}
					}
					
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_INVERSIONES	,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSalmedioInversionAcu)	  	,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSalmedioInversionAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_RECURSOS		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSalmedioRecursoAcu)	 		,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSalmedioRecursoAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SALDOMEDIO_DESINTERMED	,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSalmedioDesintermedAcu) 	,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSalmedioDesintermedAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.IMPORTE_SERVICIO			,""+ FormatUtil.roundSinDecimalsPunto(totalAnualImporteServicioAcu)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualImporteServicioAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.CUENTA_CENTRAL			,""+ FormatUtil.roundSinDecimalsPunto(totalAnualCuentaCentralAcu)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualCuentaCentralAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_INVERSION			,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSpfmesInversion)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSpfmesInversion)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_RECURSOS			,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSpfmesRecurso)				,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSpfmesRecurso)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_COMERCIAL,Constantes.SPFMES_DESINTERMED		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSpfmesDesintermed)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSpfmesDesintermed)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_FINANCIERO		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualMargenFinancieroAcu)		,""+ FormatUtil.roundSinDecimalsPunto(totalMensualMargenFinancieroAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.COMISIONES				,""+ FormatUtil.roundSinDecimalsPunto(totalAnualComisionesAcu)				,""+ FormatUtil.roundSinDecimalsPunto(totalMensualComisionesAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.MARGEN_ORDINARIO		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualMargenOrdinarioAcu)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualMargenOrdinarioAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.SANEAMIENTO_NETO		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualSaneamientoNetoAcu)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualSaneamientoNetoAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.COSTES_OPERATIVOS		,""+ FormatUtil.roundSinDecimalsPunto(totalAnualCostesOperativoAcu)			,""+ FormatUtil.roundSinDecimalsPunto(totalMensualCostesOperativoAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.RESULTADOS				,""+ FormatUtil.roundSinDecimalsPunto(totalAnualResultadoAcu)				,""+ FormatUtil.roundSinDecimalsPunto(totalMensualResultadoAcu)));
					olistRentabilidadBECGrupo.add(AsignarRentabilidad(Constantes.ID_TIPO_RENTABILIDAD,Constantes.ROA					,"",""));
					
					Programa oprograma=new Programa();
					oprograma.setId(Long.valueOf(idPrograma));
					for (RentabilidadBEC oRentagrupo:olistRentabilidadBECGrupo ){
						oRentagrupo.setPrograma(oprograma);
						oRentagrupo.setCodEmpresaGrupo(codGrupo);
						oRentagrupo.setFechaProcesoAnual(strperiodoAnual);
						oRentagrupo.setFechaProcesoMensual(strperiodoMensual);
						
					}
					
				}
								                                                                                                                                                                         		
				
			}
		return olistRentabilidadBECGrupo;
		
		} catch (Exception e) {
			olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
			return olistRentabilidadBECGrupo;
		}
	}

	private RentabilidadBEC AsignarRentabilidad(String tipoRentabilidad,String descripcionMOnto,String montoAnual,String monto){
		RentabilidadBEC orentabilidadBEC =new RentabilidadBEC();
		orentabilidadBEC.setDescripcionMonto(descripcionMOnto);
		orentabilidadBEC.setMontoAnual(montoAnual.equals("0")?"":montoAnual);
		orentabilidadBEC.setMonto(monto.equals("0")?"":monto);
		orentabilidadBEC.setTipoRentabilidad(tipoRentabilidad);
		return orentabilidadBEC;
	}
	@Override	
	public String obtenerEquivClasificacionBanco(String clasificacion){	
		String equivalencia=clasificacion;
		List<Tabla> tablasHijos = new ArrayList<Tabla>();
				
		try {
			tablasHijos=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_EQUIV_CLASBANCO);	
			if(tablasHijos!=null && tablasHijos.size()>0){
				for (Tabla otabla:tablasHijos){
					if (clasificacion!=null && clasificacion.toUpperCase().trim().equals(otabla.getAbreviado().toUpperCase().trim())){
						equivalencia=otabla.getDescripcion();
						break;
					}				
				}	
			}		
		} catch (BOException e) {
			equivalencia=clasificacion;
		}
		return (equivalencia==null?"":equivalencia);
	}
	
	//Ini MCG20141030
	@Override
	public List<Comex> obtenerComex(String idPrograma,String codigoCentral,boolean flagHost){			
		
		List<Comex> listaComexBD= new ArrayList<Comex>();
		List<Comex> listComexResul= new ArrayList<Comex>();
		
		try {		
			listaComexBD =findComexBD(idPrograma,codigoCentral);		
			if (listaComexBD != null && listaComexBD.size() > 0) {			
				listComexResul=listaComexBD;				
			}else{	
				if (flagHost){
				    listComexResul=ObtenerComexOfFileHost(idPrograma,codigoCentral);
				}
			}		
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			return listComexResul;
		}
		return listComexResul;
		
	}
	
	public List<Comex> findComexBD(String idPrograma , String codigoEmpresa)throws BOException{
		List<Comex> listaComexBD = null;
		try {
			HashMap<String, String> parametros = new HashMap<String, String> ();
			parametros.put("programa", idPrograma);
			parametros.put("codEmpresaGrupo", codigoEmpresa);
			HashMap<String,String> order= new HashMap<String,String>();
			order.put("pos", " asc");			
			listaComexBD = findByParamsOrder(Comex.class, parametros,order);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaComexBD;
	}
	
	private boolean validarComexCero(List<kc10comex> olistkc10comex){
		boolean resul=false;
		if (olistkc10comex!=null && olistkc10comex.size()>0){
			for (kc10comex okc10comex :olistkc10comex){
				String valorComision=okc10comex.getComisionesAcumuladas()==null?"0":okc10comex.getComisionesAcumuladas();
			    Double CERO=Double.parseDouble(FormatUtil.FormatNumeroSinComa("0"));
				Double	dvalorComision = Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorComision));	
				
				String valorImporte=okc10comex.getImporteAcumulado()==null?"0":okc10comex.getImporteAcumulado();
				Double	dvalorImporte = Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorImporte));
				if (dvalorComision.compareTo(CERO)!=0 ||dvalorImporte.compareTo(CERO)!=0){
					resul= true;	
					break;
				}
			}
		}		
		return resul;
		
	}
	@Override
	public List<Comex> ObtenerComexOfFileHost(String idPrograma,String codCentral){
		List<Comex> listComexofFileHost= new ArrayList<Comex>();
		List<kc10comex> listkc10comex= new ArrayList<kc10comex>();
		Calendar fecha = Calendar.getInstance();
        int anioactual = fecha.get(Calendar.YEAR); 		
		int anioAnterior=anioactual-1;
		try{	
			
			listkc10comex = findComexOfFileHost(codCentral,anioactual);
			if (!validarComexCero(listkc10comex)){
				listkc10comex= new ArrayList<kc10comex>();
				listkc10comex = findComexOfFileHost(codCentral,anioAnterior);
			}
			
			Programa oprograma =new Programa();
			oprograma.setId(Long.valueOf(idPrograma));
			
		if (listkc10comex!=null && listkc10comex.size()>0){
			int cont=0;
			for (kc10comex okc10:listkc10comex){
				cont=cont+1;
				Comex ocomex=new Comex();
				ocomex.setId(null);
				ocomex.setDescripcion(okc10.getDescripcion());
				ocomex.setCantidad(okc10.getCantidad());
				ocomex.setImporteAcumulado(okc10.getImporteAcumulado());
				ocomex.setComisionesAcumuladas(okc10.getComisionesAcumuladas());
				ocomex.setAnio(okc10.getAnio());
				ocomex.setTipoComex(okc10.getTipoComex());
				ocomex.setPrograma(oprograma);
				ocomex.setCodEmpresaGrupo(okc10.getCodigoCentral());
				ocomex.setPos(cont);			
				listComexofFileHost.add(ocomex);
			}
		}
					
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			listComexofFileHost= new ArrayList<Comex>();
		}
		return listComexofFileHost;
	}
	
	
//	public List<kc10comex> findComexOfFileHost_BD(String codCentral) throws BOException{
//
//		List<kc10comex> listakc10comex = null;
//		kc10comex okc10comex = null;
//
//		try {
//			 //--DD/MM/YYYY HH12:MI:SS PM		        		       		        		        		       		       		       		       		       		                      			     		
//			StringBuffer sb = new StringBuffer();
//			sb.append("SELECT ID_KC10COMEX,CODIGO_CENTRAL,DESCRIPCION,");
//			sb.append("CANTIDAD,IMPORTE_ACUMULADO,COMISIONES_ACUMULADAS,ANIO,TIPO_COMEX");                 
//			sb.append(" FROM PROFIN.TIIPF_KC10COMEX");
//			sb.append(" WHERE 1=1 ");
//			sb.append(" AND CODIGO_CENTRAL='"+codCentral+"'");
//			sb.append(" ORDER BY ID_KC10COMEX DESC");
//			logger.info("find programas = "+sb.toString());
//			List insurance = super.executeSQL(sb.toString());
//			
//			listakc10comex = new ArrayList<kc10comex>();
//			
//			for (Iterator it = insurance.iterator(); it.hasNext();) 
//			{
//				Object[] amount = (Object [])it.next();
//				
//				okc10comex = new kc10comex();
//				
//				okc10comex.setId(Long.valueOf(amount[0].toString()));
//				okc10comex.setCodigoCentral(amount[1]==null?"":amount[1].toString());				
//				okc10comex.setDescripcion(amount[2]==null?"":amount[2].toString());
//				okc10comex.setCantidad(amount[3]==null?"":amount[3].toString());
//				okc10comex.setImporteAcumulado(amount[4]==null?"":amount[4].toString());			
//				okc10comex.setComisionesAcumuladas(amount[5]==null?"":amount[5].toString());
//				okc10comex.setAnio(amount[6]==null?"":amount[6].toString());
//				okc10comex.setAnio(amount[6]==null?"":amount[6].toString());
//				okc10comex.setTipoComex(amount[7]==null?"":amount[7].toString());
//											
//				listakc10comex.add(okc10comex);
//			}
//				
//		} catch (Exception e) {					
//			e.printStackTrace();
//			throw new BOException(e.getMessage());
//		}
//		
//		return listakc10comex;
//	}
	
	
	//ini MCG20150511
	
	public List<kc10comex> findComexOfFileHost(String codCentral,int anio) throws BOException{

		List<kc10comex> listakc10comex = null;
		List<kc10comex> listakc10comexTotal = new ArrayList<kc10comex>();
		
		HashMap<String,Object> datosComex = null;
		Calendar fecha = Calendar.getInstance();
        //int anio = fecha.get(Calendar.YEAR);     
        
		try {
				boolean ONCOMEX=true;	   
				String urlComex = GenericAction.getObjectParamtrosSession(Constantes.URL_COMEX_PF).toString();
				//for(int i=0;i<2;i++){
					//anio=anio-i;
					String strAnio=String.valueOf(anio);						
					listakc10comex = new ArrayList<kc10comex>();				
					datosComex = QueryComex.consularDatosComex(codCentral, strAnio,Constantes.VAL_TIPO_COMEX_IMP, getConfiguracionWS(),urlComex);
					if (datosComex != null) {	
						if (datosComex.get("codigoError").equals("0000")){
							listakc10comex=(List<kc10comex>)datosComex.get("ListaComexHost");
							for (kc10comex mkc10comex :listakc10comex){								
								listakc10comexTotal.add(mkc10comex);
							}
							ONCOMEX=true;
							//break;
						}else{
							String codErrorComex=(String)datosComex.get("descripcionError");
							codErrorComex=StringUtils.rightPad(codErrorComex, 7, " ");
							codErrorComex=codErrorComex.substring(0, 7);							
							if (codErrorComex.equals(Constantes.COD_ERROR_COMEX)){
								ONCOMEX=false;
								//break;								
							}
						}
					}
				//}
				
				if (ONCOMEX){				
					//for(int i=0;i<2;i++){
						//anio=anio-i;
						String strAnioe=String.valueOf(anio);
						listakc10comex = new ArrayList<kc10comex>();
						datosComex = QueryComex.consularDatosComex(codCentral,strAnioe,Constantes.VAL_TIPO_COMEX_EXP, getConfiguracionWS(),urlComex);
						if (datosComex != null) {	
							if (datosComex.get("codigoError").equals("0000")){;
								listakc10comex=(List<kc10comex>)datosComex.get("ListaComexHost");
								for (kc10comex mkc10comex :listakc10comex){
									listakc10comexTotal.add(mkc10comex);
								}
								//break;
							}else{
								String codErrorComex=(String)datosComex.get("descripcionError");
								codErrorComex=StringUtils.rightPad(codErrorComex, 7, " ");
								codErrorComex=codErrorComex.substring(0, 7);							
								if (codErrorComex.equals(Constantes.COD_ERROR_COMEX)){
									//break;
								}
							}
						}	
					}
				//}
			
				
		} catch (Exception e) {					
			e.printStackTrace();	
			listakc10comexTotal = new ArrayList<kc10comex>();
			//throw new BOException(e.getMessage());
		}
		
		return listakc10comexTotal;
	}
	//fin MCG20150511
	@Override
	public List<Comex> obtenerComexByType(List<Comex> olistaComex,String TipoComex){
		List<Comex> listaComex=new ArrayList<Comex>();
		if (olistaComex!=null && olistaComex.size()>0){
			for (Comex ocomex:olistaComex){
				if (TipoComex.equals(ocomex.getTipoComex())){
					listaComex.add(ocomex);
				}				
			}
		}
		return listaComex;		
	}
	@Override
	public String ObtenerRatioReprocidadImp(List<Comex> olistaComexImportacion,Map<String, String> mapListaDescripcionComex){
		String strratioImp="";
		try {			
			if(olistaComexImportacion!=null && olistaComexImportacion.size()>0){
				float cobranzaImp = 0;
				float cartaImp = 0;
				float transfImp = 0;
				float finanImp = 0;
				float ratioImp=0;
				 
				for (Comex ocomer:olistaComexImportacion){
					if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.COBRANZA_DE_IMPORTACION))){
						cobranzaImp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));					
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.CARTAS_DE_CREDITO_IMP))){
						cartaImp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.TRANSFERENCIA_EMITIDA))){
						transfImp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.FINANCIMIENTO_IMP))){
						finanImp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}
				}	
				if (finanImp!=0){
					ratioImp=(cobranzaImp+cartaImp+transfImp)/finanImp;					
				}
				logger.error("ratioExp:"+ratioImp);
				strratioImp=""+FormatUtil.roundTwoDecimalsPunto(ratioImp);		
			}		
		
		} catch (Exception e) {
			strratioImp="";
			logger.error(StringUtil.getStackTrace(e));
		}
		return strratioImp;
	}

	@Override
	public String ObtenerRatioReprocidadExp(List<Comex> olistaComexExportacion,Map<String, String> mapListaDescripcionComex){
		String strratioExp="";
		try {			
			if(olistaComexExportacion!=null && olistaComexExportacion.size()>0){
				float cobranzaExp = 0;
				float cartaExp = 0;
				float transfExp = 0;
				float finanExp = 0;
				float ratioExp=0;
				 
				for (Comex ocomer:olistaComexExportacion){
					if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.COBRANZA_DE_EXPORTACION))){
						cobranzaExp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));					
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.CARTAS_DE_CREDITO_EXP))){
						cartaExp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.TRANSFERANCIA_RECIBIDAS))){
						transfExp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}else if (ocomer.getDescripcion().equals(mapListaDescripcionComex.get(Constantes.FINANCIMIENTO_EXPORT))){
						finanExp +=ocomer.getImporteAcumulado()==null?0:Float.valueOf(ocomer.getImporteAcumulado().replace(",", ""));
					}
				}	
				if (finanExp!=0){
				ratioExp=(cobranzaExp+cartaExp+transfExp)/finanExp;		
				}
				logger.error("ratioExp:"+ratioExp);
				strratioExp=""+FormatUtil.roundTwoDecimalsPunto(ratioExp);		
			}		
		
		} catch (Exception e) {
			strratioExp="";
			logger.error(StringUtil.getStackTrace(e));
		}
		return strratioExp;
	}
	
	//Fin MCG20141030

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}

	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}
	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}
	public void setComenLineas(String comenLineas) {
		this.comenLineas = comenLineas;
	}

	public List<LineasRelacionBancarias> getListLineasRelacionesBancarias() {
		return listLineasRelacionesBancarias;
	}

	public void setListLineasRelacionesBancarias(
			List<LineasRelacionBancarias> listLineasRelacionesBancarias) {
		this.listLineasRelacionesBancarias = listLineasRelacionesBancarias;
	}
	
	public List<RentabilidadBEC> getListRentabilidadBEC() {
		return listRentabilidadBEC;
	}
	public void setListRentabilidadBEC(List<RentabilidadBEC> listRentabilidadBEC) {
		this.listRentabilidadBEC = listRentabilidadBEC;
	}

	public void setListaComex(List<Comex> listaComex) {
		this.listaComex = listaComex;
	}

	public void setConfiguracionWS(ConfiguracionWS configuracionWS) {
		this.configuracionWS = configuracionWS;
	}
	
	public ConfiguracionWS getConfiguracionWS(){
		ConfiguracionWS configuracionWS=new ConfiguracionWS();
		String terminal=(String)GenericAction.getObjectParamtrosSession(Constantes.TERMINAL_CONTABLE);
		String logico=(String)GenericAction.getObjectParamtrosSession(Constantes.TERMINAL_LOGICO);
		String aplicaicon=(String)GenericAction.getObjectParamtrosSession(Constantes.CODIGO_APLICACION);
		UsuarioSesion user=(UsuarioSesion)GenericAction.getObjectSession(Constantes.USUARIO_SESSION);
		
		
		configuracionWS.setCodigoEjecutivoCuenta((String)GenericAction.getObjectParamtrosSession(Constantes.CODIGO_EJECUTIVO_CUENTA));
		configuracionWS.setCodigoOficinaCuenta((String)GenericAction.getObjectParamtrosSession(Constantes.CODIGO_OFICINA_CUENTA));		
		configuracionWS.setCodigoUsuario(user.getRegistroHost());
		//configuracionWS.setCodigoUsuario("P016653");
		configuracionWS.setCodigoAplicacion((aplicaicon));
		configuracionWS.setTerminalContable(terminal);
		configuracionWS.setTerminalLogico(logico);
		return configuracionWS;
	}
	
	

}
