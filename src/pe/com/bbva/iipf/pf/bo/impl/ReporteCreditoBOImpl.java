package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ReporteCreditoBO;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.ArchivoReporteCredito;
import pe.com.bbva.iipf.pf.model.ClaseCredito;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.Garantia;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ReporteCredito;
import pe.com.bbva.iipf.pf.model.SustentoOperacion;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.ConsultaWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.FileUtil;

/**
 * 
 * @author MCORNETERO
 * 
 */
@Service("reporteCreditoBO")
public class ReporteCreditoBOImpl extends GenericBOImpl implements
		ReporteCreditoBO {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroBO parametroBO;
	
	@Resource
	private ProgramaBO programaBO;
	private Programa programa;
	private List<ClaseCredito> listaClaseCredito = new ArrayList<ClaseCredito>();	
	private List<Garantia> listGarantia = new ArrayList<Garantia>();	
	private List<SustentoOperacion> listaSustentoOperacion= new ArrayList<SustentoOperacion>();
	
	private List<ReporteCredito> listaReporteCredito = new ArrayList<ReporteCredito>();	
	private List<DatosBasico> listaDatosBasicoGrupoEmpresas = new ArrayList<DatosBasico>();
	
	/**vchn-20130410**/
	@Resource
	private AnexoBO anexoBO;
	@Resource
	private TablaBO tablaBO;
	/**vchn-20130410**/
	
	private String sysCodificacion;
	private String valorBlob;
	
	@Override
	public List<ClaseCredito> ObtenerListaClaseCredito(Programa oprograma)
			throws BOException {
		List<Long> params = new ArrayList<Long>();
		params.add(oprograma.getId());
		List<ClaseCredito> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery(
					"findClaseCreditoByPrograma", params);
			for (ClaseCredito claseCredito : listatemp) {
				if(claseCredito.getTipoVcto()!= null &&  claseCredito.getTipoVcto().equals("R")){
					claseCredito.setReembolso(claseCredito.getVencimiento());
				}
			}
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130409
	@Override
	public List<ClaseCredito> ObtenerListaClaseCredito(Programa oprograma,String codEmpresaGrupo)
			throws BOException {
		List<Object> params = new ArrayList<Object>();
		params.add(oprograma.getId());
		params.add(codEmpresaGrupo);
		List<ClaseCredito> listatemp = null;
		try {
			listatemp = super.executeListNamedQuery(
					"findClaseCreditoByProgramaByEmpresa", params);
			for (ClaseCredito claseCredito : listatemp) {
				if(claseCredito.getTipoVcto()!= null && claseCredito.getTipoVcto().equals("R")){
					claseCredito.setReembolso(claseCredito.getVencimiento());
				}
			}
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG20130409
	
	public void beforeUpdateReporteCredito(){
		
	}
	
	public boolean validateUpdateReporteCredito(){
		return true;
	}
	/**
	 * actuliza los datos basicos del programa financiero
	 * no incluye actualizacion de los campos blob
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateReporteCredito() throws BOException {
		beforeUpdateReporteCredito();
		if(validateUpdateReporteCredito()){
			List parametros = new ArrayList();
			parametros.add(getPrograma().getCuentaCorriente()==null?"":getPrograma().getCuentaCorriente());
			parametros.add(getPrograma().getFechaRDC()==null?"":getPrograma().getFechaRDC());			
			parametros.add(getPrograma().getNumeroRVGL()==null?"":getPrograma().getNumeroRVGL());
			parametros.add(getPrograma().getSalem()==null?"":getPrograma().getSalem());
			parametros.add(getPrograma().getVulnerabilidad()==null?"":getPrograma().getVulnerabilidad());
			parametros.add(getPrograma().getTotalInversion()==null?"":getPrograma().getTotalInversion());
			
			parametros.add(getPrograma().getMontoPrestamo()==null?"":getPrograma().getMontoPrestamo());
			parametros.add(getPrograma().getEntorno()==null?"":getPrograma().getEntorno());
			parametros.add(getPrograma().getPoblacionAfectada()==null?"":getPrograma().getPoblacionAfectada());
			parametros.add(getPrograma().getCategorizacionAmbiental()==null?"":getPrograma().getCategorizacionAmbiental());
			parametros.add(getPrograma().getComentarioAdmision()==null?"":getPrograma().getComentarioAdmision());
			parametros.add(getPrograma().getCiiuRDC()==null?"":getPrograma().getCiiuRDC());			
									
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updateReporteCredito",parametros);
			
			//guardando la lista de clase credito
			saveUDClaseCredito();
			//guardando la lista de garantias
			saveGarantia();
			//guardando la lista de sustento operacion
			saveUDSustentoOperacion();			
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		
		//afterUpdateDatosBasicos();
	}
	
	
	public void beforeUpdateReporteCredito(String codEmpresaGrupo){
		
	}
	
	//ini MCG 20130409
	/**
	 * actuliza los datos basicos del programa financiero
	 * no incluye actualizacion de los campos blob
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateReporteCredito(String codEmpresaGrupo) throws BOException {
		beforeUpdateReporteCredito(codEmpresaGrupo);
		if(validateUpdateReporteCredito()){
			List parametros = new ArrayList();
			parametros.add(getPrograma().getCuentaCorriente()==null?"":getPrograma().getCuentaCorriente());	
			parametros.add(getPrograma().getFechaRDC()==null?"":getPrograma().getFechaRDC());			
			parametros.add(getPrograma().getNumeroRVGL()==null?"":getPrograma().getNumeroRVGL());
			parametros.add(getPrograma().getSalem()==null?"":getPrograma().getSalem());
			parametros.add(getPrograma().getVulnerabilidad()==null?"":getPrograma().getVulnerabilidad());
			parametros.add(getPrograma().getTotalInversion()==null?"":getPrograma().getTotalInversion());
			
			parametros.add(getPrograma().getMontoPrestamo()==null?"":getPrograma().getMontoPrestamo());
			parametros.add(getPrograma().getEntorno()==null?"":getPrograma().getEntorno());
			parametros.add(getPrograma().getPoblacionAfectada()==null?"":getPrograma().getPoblacionAfectada());
			parametros.add(getPrograma().getCategorizacionAmbiental()==null?"":getPrograma().getCategorizacionAmbiental());
			parametros.add(getPrograma().getComentarioAdmision()==null?"":getPrograma().getComentarioAdmision());
			parametros.add(getPrograma().getCiiuRDC()==null?"":getPrograma().getCiiuRDC());
									
			if(getPrograma().getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){	
				parametros.add(getPrograma().getId());
				super.executeNamedQuery("updateReporteCredito",parametros);					
			}else{
				Empresa empresaprin= new Empresa();	
				empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
				if (empresaprin!=null){
					if (empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
						parametros.add(getPrograma().getId());
						super.executeNamedQuery("updateReporteCredito",parametros);
					}else{
						parametros.add(getPrograma().getId());
						parametros.add(codEmpresaGrupo);					
						
						
						HashMap<String, Object> parametrosbusc = new HashMap<String, Object>();
						parametrosbusc.put("programa", programa.getId());
						parametrosbusc.put("codigoEmpresa", codEmpresaGrupo);
						List<ReporteCredito> olista =  super.findByParams2(ReporteCredito.class, parametrosbusc);	
						if(olista!=null && olista.size()>0 && !olista.isEmpty()){
							super.executeNamedQuery("updateReporteCreditoByEmpresa",parametros);
						}else{
							List<ReporteCredito> listReporteCreditotmp = new ArrayList<ReporteCredito>();	
							ReporteCredito oreporteCredito=new ReporteCredito();
							
							oreporteCredito.setPrograma(getPrograma());
							oreporteCredito.setCodigoEmpresa(codEmpresaGrupo);
							oreporteCredito.setCuentaCorriente(getPrograma().getCuentaCorriente()==null?"":getPrograma().getCuentaCorriente());							
							
							oreporteCredito.setFechaRDC(getPrograma().getFechaRDC()==null?"":getPrograma().getFechaRDC());			
							oreporteCredito.setNumeroRVGL(getPrograma().getNumeroRVGL()==null?"":getPrograma().getNumeroRVGL());
							oreporteCredito.setSalem(getPrograma().getSalem()==null?"":getPrograma().getSalem());
							oreporteCredito.setVulnerabilidad(getPrograma().getVulnerabilidad()==null?"":getPrograma().getVulnerabilidad());
							oreporteCredito.setTotalInversion(getPrograma().getTotalInversion()==null?"":getPrograma().getTotalInversion());
							
							oreporteCredito.setMontoPrestamo(getPrograma().getMontoPrestamo()==null?"":getPrograma().getMontoPrestamo());
							oreporteCredito.setEntorno(getPrograma().getEntorno()==null?"":getPrograma().getEntorno());
							oreporteCredito.setPoblacionAfectada(getPrograma().getPoblacionAfectada()==null?"":getPrograma().getPoblacionAfectada());
							oreporteCredito.setCategorizacionAmbiental(getPrograma().getCategorizacionAmbiental()==null?"":getPrograma().getCategorizacionAmbiental());
							oreporteCredito.setComentarioAdmision(getPrograma().getComentarioAdmision()==null?"":getPrograma().getComentarioAdmision());
							oreporteCredito.setCiiuRDC(getPrograma().getCiiuRDC()==null?"":getPrograma().getCiiuRDC());
							listReporteCreditotmp.add(oreporteCredito);							
							saveReporteCredito(listReporteCreditotmp);
							//listaReporteCredito=listReporteCreditotmp;
							
						}
							
					}	
				}
			}			
			
			//guardando la lista de clase credito
			saveUDClaseCredito(codEmpresaGrupo);
			//guardando la lista de garantias
			saveGarantia(codEmpresaGrupo);
			//guardando la lista de sustento operacion
			//saveUDSustentoOperacion(codEmpresaGrupo);			
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();

			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
		}		
		
	}
	//fin MCG 20130409
	
	//ini MCG20130410	
	
	public boolean validationReporteCredito(){
		return true;
	}
	//fin MCG20130410
	
	//ini MCG20130410
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveReporteCredito(List<ReporteCredito> olistReporteCredito) throws BOException {		
		if(validationReporteCredito()){	
			saveCollection(olistReporteCredito);			
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130410
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void copiaReporteCredito() throws BOException {
		beforeUpdateReporteCredito();
		if(validateUpdateReporteCredito()){			
			
			//guardando la lista de clase credito
			saveUDReporteCredito();
			
			//guardando la lista de clase credito
			saveUDClaseCredito();
			//guardando la lista de garantias
			saveGarantia();
			//guardando la lista de sustento operacion
			saveUDSustentoOperacion();			
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		
		//afterUpdateDatosBasicos();
	}

		
	
	public void beforeUDClaseCredito(){
		int posicion = 0;
		for(ClaseCredito acc : listaClaseCredito){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			acc.setOrden(String.valueOf(posicion+1));
			posicion++;
		}
	}
	
	public void beforeUDReporteCredito(){		
		for(ReporteCredito acc : listaReporteCredito){
			acc.setPrograma(programa);			
			
		}
	}
	
	public void beforeUDClaseCredito(String codEmpresaGrupo){
		int posicion = 0;
		int cont=0;
		for(ClaseCredito acc : listaClaseCredito){
			acc.setPrograma(programa);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setPosicion(posicion);
			if (acc.getFlagTipoLimte()!=null && acc.getFlagTipoLimte().equals(Constantes.VALOR_FLAGTIPO_SUBLIMITE)){
				acc.setOrden("0");
			}else{
				cont=cont+1;				
				acc.setOrden(String.valueOf(cont));
			}
			
			posicion++;
		}
	}
	
	public boolean validationUDClaseCredito(){
		return true;
	}
	
	/**
	 * Registra valida la clase credito
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDClaseCredito() throws BOException{
		beforeUDClaseCredito();
		if(validationUDClaseCredito()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<ClaseCredito> listatemp =  findByParams2(ClaseCredito.class, params);
			List<ClaseCredito> listaDel = new ArrayList<ClaseCredito>();
			boolean flag= false;
			for(ClaseCredito acc : listatemp ){
				for(ClaseCredito acctemp : listaClaseCredito){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			for (ClaseCredito claseCredito : listaClaseCredito) {
				if(claseCredito.getTipoVcto()!= null &&  claseCredito.getTipoVcto().equals("R")){
					claseCredito.setVencimiento(claseCredito.getReembolso());
				}
			}
			saveCollection(listaClaseCredito);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	public boolean validationUDReporteCredito(){
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDReporteCredito() throws BOException{
		beforeUDReporteCredito();
		if(validationUDReporteCredito()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<ReporteCredito> listatemp =  findByParams2(ReporteCredito.class, params);
			List<ReporteCredito> listaDel = new ArrayList<ReporteCredito>();
			boolean flag= false;
			for(ReporteCredito rdc : listatemp ){
				for(ReporteCredito rdctemp : listaReporteCredito){
					if(rdctemp.getId()!=null && rdctemp.getId().equals(rdc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(rdc);
				}
				flag= false;
			}
			saveCollection(listaReporteCredito);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	
	//ini MCG20130409
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDClaseCredito(String codEmpresaGrupo) throws BOException{
		beforeUDClaseCredito(codEmpresaGrupo);
		if(validationUDClaseCredito()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<ClaseCredito> listatemp =  findByParams2(ClaseCredito.class, params);
			List<ClaseCredito> listaDel = new ArrayList<ClaseCredito>();
			boolean flag= false;
			for(ClaseCredito acc : listatemp ){
				for(ClaseCredito acctemp : listaClaseCredito){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			for (ClaseCredito claseCredito : listaClaseCredito) {
				if(claseCredito.getTipoVcto()!= null &&  claseCredito.getTipoVcto().equals("R")){
					claseCredito.setVencimiento(claseCredito.getReembolso());
				}
			}
			saveCollection(listaClaseCredito);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//fin MCG20130409
	
	public void beforeGarantia(){
		int posicion=0;
		for(Garantia eli : listGarantia){
			eli.setPosicion(posicion);			
			eli.setPrograma(programa);
			posicion++;
		}
	}
	
	public void beforeGarantia(String codEmpresaGrupo){
		int posicion=0;
		for(Garantia eli : listGarantia){
			eli.setPosicion(posicion);			
			eli.setPrograma(programa);
			eli.setCodEmpresaGrupo(codEmpresaGrupo);
			posicion++;
		}
	}
	
	public boolean validationGarantia(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra las garantias
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveGarantia() throws BOException{
		try {			
			beforeGarantia();
			if(validationGarantia()){
				HashMap<String,Long> params = new HashMap<String,Long>();
				params.put("programa", programa.getId());
				List<Garantia> listatemp =  findByParams2(Garantia.class, params);
				List<Garantia> listaDel = new ArrayList<Garantia>();
				boolean flag= false;
				for(Garantia eli : listatemp ){
					for(Garantia elitemp : listGarantia){
						if(elitemp.getId()!=null && elitemp.getId().equals(eli.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(eli);
					}
					flag= false;
				}
				
				saveCollection(listGarantia);
				deleteCollection(listaDel);
				programaBO.actualizarFechaModificacionPrograma(programa.getId());
			}
		} catch (Exception e) {
			throw new BOException(e);//logger.error(StringUtil.getStackTrace(e));
		}	
	}
	
	//ini MCG20130409
	
	/**
	 * valida y actualiza y registra las garantias
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveGarantia(String codEmpresaGrupo) throws BOException{
		try {			
			beforeGarantia(codEmpresaGrupo);
			if(validationGarantia()){
				HashMap<String,Object> params = new HashMap<String,Object>();
				params.put("programa", programa.getId());
				params.put("codEmpresaGrupo", codEmpresaGrupo);
				List<Garantia> listatemp =  findByParams2(Garantia.class, params);
				List<Garantia> listaDel = new ArrayList<Garantia>();
				boolean flag= false;
				for(Garantia eli : listatemp ){
					for(Garantia elitemp : listGarantia){
						if(elitemp.getId()!=null && elitemp.getId().equals(eli.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(eli);
					}
					flag= false;
				}
				
				saveCollection(listGarantia);
				deleteCollection(listaDel);
				programaBO.actualizarFechaModificacionPrograma(programa.getId());
			}
		} catch (Exception e) {
			throw new BOException(e);//logger.error(StringUtil.getStackTrace(e));
		}	
	}
	
	//fin MCG20130409
	
	
	@Override
	public List<Garantia> findGarantiaByIdprograma(Long idPrograma) throws BOException   	{
		List<Long> params = new ArrayList<Long>();
		params.add(idPrograma);
		List<Garantia> listatemp = null;
		try {	
			listatemp = executeListNamedQuery("listarGarantia", params); 	
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//ini MCG20130409
	@Override
	public List<Garantia> findGarantiaByIdprograma(Long idPrograma,String codEmpresaGrupo) throws BOException   	{
		List<Object> params = new ArrayList<Object>();
		params.add(idPrograma);
		params.add(codEmpresaGrupo);	
		List<Garantia> listatemp = null;
		try {	
			listatemp = executeListNamedQuery("listarGarantiabyEmpresa", params); 	
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//fin MCG2013409
	@Override
	public List<SustentoOperacion> findSustentoOperacionByIdprograma(Long idPrograma) throws BOException   	{
		List<Long> params = new ArrayList<Long>();
		params.add(idPrograma);
		List<SustentoOperacion> listatemp = null;
		try {	
			listatemp = executeListNamedQuery("findSustentoOperacionByPrograma", params); 	
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	//ini MCG20130409
	@Override
	public List<SustentoOperacion> findSustentoOperacionByIdprograma(Long idPrograma,String codEmpresaGrupo) throws BOException   	{
		List<Object> params = new ArrayList<Object>();
		params.add(idPrograma);
		params.add(codEmpresaGrupo);	
		List<SustentoOperacion> listatemp = null;
		try {	
			listatemp = executeListNamedQuery("findSustentoOperacionByProgramaByEmpresa", params); 	
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//fin MCG20130409
	
	public void beforeUDSustentoOperacion(){
		int posicion = 0;
		for(SustentoOperacion acc : listaSustentoOperacion){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			acc.setOrden(String.valueOf(posicion+1));
			posicion++;
		}
	}
	//ini MCG20130409
	
	public void beforeUDSustentoOperacion(String codEmpresaGrupo){
		int posicion = 0;
		for(SustentoOperacion acc : listaSustentoOperacion){
			acc.setPrograma(programa);
			acc.setPosicion(posicion);
			acc.setCodEmpresaGrupo(codEmpresaGrupo);
			acc.setOrden(String.valueOf(posicion+1));
			posicion++;
		}
	}
	//fin MCG20130409
	
	
	public boolean validationUDSustentoOperacion(){
		return true;
	}
	
	/**
	 * Registra valida los Sustento Operacion
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDSustentoOperacion() throws BOException{
		beforeUDSustentoOperacion();
		if(validationUDSustentoOperacion()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<SustentoOperacion> listatemp =  findByParams2(SustentoOperacion.class, params);
			List<SustentoOperacion> listaDel = new ArrayList<SustentoOperacion>();
			boolean flag= false;
			for(SustentoOperacion acc : listatemp ){
				for(SustentoOperacion acctemp : listaSustentoOperacion){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaSustentoOperacion);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//ini MCG20130409
	/**
	 * Registra valida los Sustento Operacion
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUDSustentoOperacion(String codEmpresaGrupo) throws BOException{
		beforeUDSustentoOperacion(codEmpresaGrupo);
		if(validationUDSustentoOperacion()){
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("programa", programa.getId());
			params.put("codEmpresaGrupo", codEmpresaGrupo);
			List<SustentoOperacion> listatemp =  findByParams2(SustentoOperacion.class, params);
			List<SustentoOperacion> listaDel = new ArrayList<SustentoOperacion>();
			boolean flag= false;
			for(SustentoOperacion acc : listatemp ){
				for(SustentoOperacion acctemp : listaSustentoOperacion){
					if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(acc);
				}
				flag= false;
			}
			saveCollection(listaSustentoOperacion);
			deleteCollection(listaDel);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	//fin MCG20130409
	
	//ini MCG20130410
	@Override
	public List<ReporteCredito> getListaReporteCredito(String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", programa.getId());
		params.put("codigoEmpresa", CodEmpresaGrupo);
		List<ReporteCredito> listatemp = null;
		try {
			listatemp = findByParams2(ReporteCredito.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<ReporteCredito> getListaReporteCredito(Programa oprograma,String CodEmpresaGrupo)throws BOException  {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("programa", oprograma.getId());
		params.put("codigoEmpresa", CodEmpresaGrupo);
		List<ReporteCredito> listatemp = null;
		try {
			listatemp = findByParams2(ReporteCredito.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	@Override
	public List<ReporteCredito> getListaReporteCredito(Programa oprograma)throws BOException  {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", oprograma.getId());		
		List<ReporteCredito> listatemp = null;
		try {
			listatemp = findByParams2(ReporteCredito.class, params);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
	//fin MCG20130410
	/**gmp-20130410**/
	@Override
	public SustentoOperacion findSustentoById(Long id) throws BOException {
		SustentoOperacion sustento = (SustentoOperacion) super.findById(SustentoOperacion.class, id);
		return sustento;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(SustentoOperacion sustento) throws BOException {
		try{
			logger.info("blob limpio sin filtro = "+valorBlob);
			String texto = ExcelHelper.cleanText(valorBlob );
			logger.info("blob limpio = "+texto);
			sustento.setSustento(texto.getBytes(sysCodificacion== null?"":sysCodificacion.trim()));
			super.doSave(sustento);
		}catch(Exception e){
			logger.error(e);
			throw new BOException(e);
		}
	}

	public void delete(SustentoOperacion sustento) throws BOException {
		super.delete(sustento);
	}
	
	public void delete(ArchivoReporteCredito archivoReporteCredito) throws BOException {
		super.delete(archivoReporteCredito);
	}
	/**gmp-20130410**/
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public void setListaClaseCredito(List<ClaseCredito> listaClaseCredito) {
		this.listaClaseCredito = listaClaseCredito;
	}
	
	
	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}

	public void setListGarantia(List<Garantia> listGarantia) {
		this.listGarantia = listGarantia;
	}
	

	public void setListaSustentoOperacion(
			List<SustentoOperacion> listaSustentoOperacion) {
		this.listaSustentoOperacion = listaSustentoOperacion;
	}
	/**vchn-20130410**/
	@Override
	public List<ClaseCredito> loadReporteCreditoByAnexos(Programa oprograma,String codEmpresa) throws BOException {
		List<ClaseCredito> listaReporteCredito = new ArrayList<ClaseCredito>();
		Long idEmpresa = null;
		String tipoVenc = "";
		try {
		
		
		Empresa empresa= new Empresa();	
		empresa=programaBO.findEmpresaByIdEmpresaPrograma(oprograma.getId(),codEmpresa);

			List<FilaAnexo> olistaFilaAnexosxEmp=new ArrayList<FilaAnexo>();
			olistaFilaAnexosxEmp = anexoBO.obtenerListAnexosHijosByEmpresa(oprograma.getId(),empresa.getNombre().trim());
			if(olistaFilaAnexosxEmp != null && olistaFilaAnexosxEmp.size()>0){
				Tabla moneda = new Tabla();
				moneda = tablaBO.obtieneHijaPorCodigoHijoCodigoPadre(Constantes.CODIGO_MONEDAD_USA,Constantes.CODIGO_TIPOMONEDA);
				//moneda = tablaBO.obtienePorId(new Long(20402));//USD corregir obtener por codigo de la tabla
				int cont = 1;
				Tabla ocuenta=new Tabla();
				ocuenta.setId(Constantes.ID_TIPO_CUENTA_M);
				for (FilaAnexo afilaAnexo: olistaFilaAnexosxEmp){	
					String descripcion;
					String observaciones;
					String riesgo;
					ClaseCredito claseCredito = new ClaseCredito();
					claseCredito.setPosicion(cont);
					claseCredito.setPrograma(oprograma);
					claseCredito.setOrden(""+cont);
					claseCredito.setFlagTipoLimte(Constantes.VALOR_FLAGTIPO_LIMITEOPERACION);
					claseCredito.setMoneda(moneda);
					//claseCredito.setImporte(anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_RIESGO_PROPUESTO,afilaAnexo.getAnexo().getId()));
					claseCredito.setImporte(afilaAnexo.getAnexo().getRgoPropBbvaBc()==null?"":afilaAnexo.getAnexo().getRgoPropBbvaBc());
					if(afilaAnexo.getAnexo().getTipoFila().equals(3)||afilaAnexo.getAnexo().getTipoFila().equals(5)){
						descripcion = afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion();
						//observaciones = anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_OBSERVACIONES,afilaAnexo.getAnexo().getId())==null?"":anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_OBSERVACIONES,afilaAnexo.getAnexo().getId());
						observaciones=afilaAnexo.getAnexo().getObservaciones()==null?"":afilaAnexo.getAnexo().getObservaciones();
						
						claseCredito.setClaseCredito(descripcion.toUpperCase()+": "+observaciones);
						cont++;
					}else if(afilaAnexo.getAnexo().getTipoFila().equals(4)){
						descripcion = afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion();
						//observaciones = anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_OBSERVACIONES,afilaAnexo.getAnexo().getId())==null?"":anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_OBSERVACIONES,afilaAnexo.getAnexo().getId());
						//riesgo = anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_RIESGO_PROPUESTO,afilaAnexo.getAnexo().getId())==null?"":anexoBO.findValorColumnaById(oprograma.getId(), afilaAnexo.getAnexo().getDescripcion()==null?"":afilaAnexo.getAnexo().getDescripcion(), Constantes.COLUMN_RIESGO_PROPUESTO,afilaAnexo.getAnexo().getId());
						
						observaciones=afilaAnexo.getAnexo().getObservaciones()==null?"":afilaAnexo.getAnexo().getObservaciones();
						riesgo=afilaAnexo.getAnexo().getRgoPropBbvaBc()==null?"":afilaAnexo.getAnexo().getRgoPropBbvaBc();
						claseCredito.setOrden("0");					
						claseCredito.setCuenta(ocuenta);
						claseCredito.setFlagTipoLimte(Constantes.VALOR_FLAGTIPO_SUBLIMITE);
						claseCredito.setClaseCredito(descripcion.toUpperCase()+": "+riesgo+" "+observaciones);
					}else{
						cont++;	
					}
					claseCredito.setTasaComision("A Pactar");
					claseCredito.setTipoVcto("V");
					String fechaVencimiento=afilaAnexo.getAnexo().getFecha()==null?"":afilaAnexo.getAnexo().getFecha().toString();
					claseCredito.setVencimiento(fechaVencimiento);
					claseCredito.setFlagCancelado("NC");
					listaReporteCredito.add(claseCredito);
					
				}			
			}
		} catch (Exception e) {
			logger.error(e);
			listaReporteCredito = new ArrayList<ClaseCredito>();			
		}
		return listaReporteCredito;
	}
	/**vchn-20130410**/
	@Override
	public BigDecimal getTipoCambio(String fecha) throws BOException {
		String sql;
		List<BigDecimal> lista;
		BigDecimal tipoCambio = new BigDecimal(1);

		try {
			sql = "SELECT TCFIXIN FROM PROFIN.TIIPF_PFTIPOCAMBIO"
					+ " WHERE FECCAMB='" + fecha + "'"
					+ " AND TIPCAMB='S' AND DIVISA='USD'";
			lista = executeSQL(sql);

			if (lista.size() > 0 || !lista.isEmpty()) {
				tipoCambio = lista.get(0);
			} else {
				sql = " SELECT TCFIXIN FROM PROFIN.TIIPF_PFTIPOCAMBIO"
						+ " WHERE FECCAMB in (SELECT max(FECCAMB) FROM PROFIN.TIIPF_PFTIPOCAMBIO)"
						+ " AND TIPCAMB='S' AND DIVISA='USD'";
				lista = executeSQL(sql);
				if (lista.size() > 0 || !lista.isEmpty()) {
					tipoCambio = lista.get(0);
				}
			}

		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		return tipoCambio;
	}
	
	//INI MCG20130411
	@Override
	public List<SustentoOperacion> listaSustentoOperacionByProgramaByEmpresa(Programa oprograma,String codEmpresaGrupo) throws BOException {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("programa",oprograma.getId());
		parametros.put("codEmpresaGrupo", codEmpresaGrupo);
		List<SustentoOperacion> lista = null ;
		try {
			lista=super.findByParams(SustentoOperacion.class, parametros);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return lista;
	}
	//FIN MCG20130411
	
	
	//ini MCG20130411
	@Override
	public void ActualizarReporteCreditoByPrograma(Programa oprograma,List<Empresa> listaEmpresasGrupofin) throws BOException{
		try {	
			
			List<ReporteCredito> oListaReporteCreditoDelete  = new ArrayList<ReporteCredito>();
			List<ReporteCredito> oListaReporteCredito = this.getListaReporteCredito(oprograma);			
			if (oListaReporteCredito!=null && oListaReporteCredito.size()>0){
				for (ReporteCredito oReporteCredito: oListaReporteCredito){	
					String codempre=oReporteCredito.getCodigoEmpresa();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){						
						oListaReporteCreditoDelete.add(oReporteCredito);
					}				
				}	
				deleteCollection(oListaReporteCreditoDelete);
			}	
			
			List<ClaseCredito> listaClaseCreditoCopia=new ArrayList<ClaseCredito>();
			List<ClaseCredito> olistaClaseCredito=this.ObtenerListaClaseCredito(oprograma);
			if (olistaClaseCredito!=null && olistaClaseCredito.size()>0){
				for (ClaseCredito oclaseCredito: olistaClaseCredito){
					String codempre=oclaseCredito.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){						
						listaClaseCreditoCopia.add(oclaseCredito);
					}
				}	
				deleteCollection(listaClaseCreditoCopia);
			}	
			
			List<Garantia> listaGarantiaCopia=new ArrayList<Garantia>();
			List<Garantia> olistaGarantia=this.findGarantiaByIdprograma(oprograma.getId());
			if (olistaGarantia!=null && olistaGarantia.size()>0){
				for (Garantia ogarantia: olistaGarantia){
					String codempre=ogarantia.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){					
						listaGarantiaCopia.add(ogarantia);
					}
				}	
				deleteCollection(listaGarantiaCopia);
			}
			
			List<SustentoOperacion> listaSustentoOperacionCopia=new ArrayList<SustentoOperacion>();
			List<SustentoOperacion> olistaSustentoOperacion=findSustentoOperacionByIdprograma(oprograma.getId());
			if (olistaSustentoOperacion!=null && olistaSustentoOperacion.size()>0){
				for (SustentoOperacion osustentoOperacion: olistaSustentoOperacion){
					String codempre=osustentoOperacion.getCodEmpresaGrupo();
					if (!validarEmpresaAGuardar(codempre,listaEmpresasGrupofin)){	
						listaSustentoOperacionCopia.add(osustentoOperacion);
					}
				}	
				deleteCollection(listaSustentoOperacionCopia);
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
	
	//fin MCG20130411
	
	
	//INI MCG20131125
	
	public void beforeRefreshReporteCredito(Programa t,String codEmpresaGrupo,List<Empresa> listaGrupoEmpresas,ConfiguracionWS oConfiguracionWS) throws BOException{
		
		HashMap<String,String> datosBasicos = null;
		HashMap<String,String> datosBasicostmp = null;		
		List<DatosBasico> listDatosBasico=new ArrayList<DatosBasico>();
		
		HashMap<String,String> datosBasicosRC = null;
		HashMap<String,String> datosBasicosRC1 = null;
		
		try {

			if(t.getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_GRUPO)){
				for(Empresa acctemp : listaGrupoEmpresas){
					DatosBasico datosBasicoEmpresa=new DatosBasico();
					String codEmpresaDB=acctemp.getCodigo();				
					if (codEmpresaDB.equals(codEmpresaGrupo)){	
						t.setIdEmpresa(acctemp.getCodigo());	
						t.setRuc(acctemp.getRuc());						
					}				

					
						String urlRig4 = GenericAction.getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
						datosBasicosRC1 = ConsultaWS.consularDatosReporteCredito(acctemp.getCodigo(), oConfiguracionWS,urlRig4);
						
						if (datosBasicosRC1 != null) {
							String separador1="";
							String separador2="";
							String separador3="";
							if (datosBasicosRC1.get("codRegistroGestor")!=null && !datosBasicosRC1.get("codRegistroGestor").equals("")){separador1=" - ";}
							if (datosBasicosRC1.get("codOficinaPrincipal")!=null && !datosBasicosRC1.get("codOficinaPrincipal").equals("")){separador2=" - ";}
							if (datosBasicosRC1.get("codEtiqueta")!=null && !datosBasicosRC1.get("codEtiqueta").equals("")){separador3=" - ";}						
							
							datosBasicoEmpresa.setCalificacionBanco(datosBasicosRC1.get("clasificacionBanco")==null?"":datosBasicosRC1.get("clasificacionBanco"));
							
							datosBasicoEmpresa.setGestor((datosBasicosRC1.get("codRegistroGestor")==null?"":datosBasicosRC1.get("codRegistroGestor")) + separador1 + (datosBasicosRC1.get("nombreGestor")==null?"":datosBasicosRC1.get("nombreGestor")) );
							datosBasicoEmpresa.setOficina((datosBasicosRC1.get("codOficinaPrincipal")==null?"":datosBasicosRC1.get("codOficinaPrincipal")) + separador2 + (datosBasicosRC1.get("descOficinaPrincipal")==null?"":datosBasicosRC1.get("descOficinaPrincipal")) );
						    datosBasicoEmpresa.setSegmento((datosBasicosRC1.get("codEtiqueta")==null?"":datosBasicosRC1.get("codEtiqueta") )+ separador3 + (datosBasicosRC1.get("descEtiqueta")==null?"":datosBasicosRC1.get("descEtiqueta")) );
							
							
						}

										
					// fin MCG20130218
					datosBasicoEmpresa.setPrograma(t);
					datosBasicoEmpresa.setCodigoEmpresa(acctemp.getCodigo());
					datosBasicoEmpresa.setRuc(acctemp.getRuc());
					datosBasicoEmpresa.setNombreGrupoEmpresa(acctemp.getNombre());				
					
					listDatosBasico.add(datosBasicoEmpresa);					
					
				}
			}
			
			//ini MCG20130401
			
				String urlRig4 = GenericAction.getObjectParamtrosSession(Constantes.URL_RIG4_PF).toString();
				datosBasicosRC = ConsultaWS.consularDatosReporteCredito(t.getIdEmpresa(), oConfiguracionWS,urlRig4);
				String codigoError=datosBasicosRC.get("codigoError");
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
				if (!codigoError.equals(Constantes.COD_CORRECTO_HOST)){				
					throw new BOException(codigoError+": "+ datosBasicosRC.get("descripcionError"));
				}
							
			//fin MCG20130401				

			
			listaDatosBasicoGrupoEmpresas=listDatosBasico;		
			
		} catch (Exception e) {
			throw new BOException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void refreshReporteCredito(String codEmpresaGrupo,List<Empresa> listaGrupoEmpresas,ConfiguracionWS oConfiguracionWS) throws BOException {
		beforeRefreshReporteCredito(programa,codEmpresaGrupo,listaGrupoEmpresas,oConfiguracionWS);
		
		try {
			

		if(true){	
			List parametros = new ArrayList();		
					
			if(getPrograma().getTipoEmpresa().getId().equals(Constantes.ID_TIPO_EMPRESA_EMPR)){		
				parametros = new ArrayList();				
		
			    parametros.add(getPrograma().getCalificacionBanco());
				parametros.add(getPrograma().getGestor());
				parametros.add(getPrograma().getOficina());
				parametros.add(getPrograma().getSegmento());
				
				parametros.add(getPrograma().getId());
				
				String sqlqueryp = " UPDATE PROFIN.TIIPF_PROGRAMA " +								
			    " set RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
				" RDC_OFICINA = ?, RDC_SEGMENTO = ? " +
				" where id_programa = ? ";								
				super.executeSQLQuery(sqlqueryp,parametros);
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
							
						    String sqlquery = " update PROFIN.tiipf_datos_basico " +								
						   	" set RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
							" RDC_OFICINA = ?, RDC_SEGMENTO = ? " +
							" where id_programa = ? AND codigo_empresa=?";									
							parametros.add(odatosBasicos.getCalificacionBanco()==null?"":odatosBasicos.getCalificacionBanco().toString());
							parametros.add(odatosBasicos.getGestor()==null?"":odatosBasicos.getGestor().toString());
							parametros.add(odatosBasicos.getOficina()==null?"":odatosBasicos.getOficina().toString());
							parametros.add(odatosBasicos.getSegmento()==null?"":odatosBasicos.getSegmento().toString());
							parametros.add(getPrograma().getId());
						
							if (empresaprin!=null && empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){								
								String sqlquery1 = " UPDATE PROFIN.TIIPF_PROGRAMA " +								    	
								" set RB_CALIFICACION_BANC = ?, RDC_GESTOR = ?," +
								" RDC_OFICINA = ?, RDC_SEGMENTO = ?" +								
								" where id_programa = ? ";								
								super.executeSQLQuery(sqlquery1,parametros);	
							}								
							parametros.add(codEmpresa);
							super.executeSQLQuery(sqlquery,parametros);	
														
					}
				}
			}						
	
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
		
		} catch (Exception e) {
			throw new BOException(e.getMessage());
		}
	}
	
	//FIN MCG20131125
	
	//ini MCG20141024
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveFileReporteCredito(File fileReporteCredito, 
							  Programa programa,
							  ArchivoReporteCredito archivoReporteCredito) throws BOException {
		Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILE_REPORTE_CREDITO);
		File uniqueFile = null;
		archivoReporteCredito.setPrograma(programa);
		//archivoReporteCredito.setNombreArchivo(archivoAnexo.getNombreArchivo());
		//archivoReporteCredito.setExtencion(archivoAnexo.getExtencion());
		super.save(archivoReporteCredito);			
			
		try {
			uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),
												 archivoReporteCredito.getId()+
												 "."+
												 archivoReporteCredito.getExtencion());
		
			byte[] serObj = FileUtil.readBytes(fileReporteCredito);
			FileUtil.write(uniqueFile,serObj);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		} catch (IOException e) {
			throw new BOException(e);
		} catch (Exception e) {
			throw new BOException(e);
		}
	}
	@Override
	public List<ArchivoReporteCredito> findListaArchivoReporteCredito(Programa programa,String codEmpresaGrupo)throws BOException{
		HashMap<String,String> parametros = new HashMap<String,String>();
		parametros.put("programa", String.valueOf(programa.getId()));
		parametros.put("codEmpresaGrupo", codEmpresaGrupo);		
		List<ArchivoReporteCredito> listaArchivoReporteCredito = null;
		try {
			listaArchivoReporteCredito = super.findByParams(ArchivoReporteCredito.class, 
											   parametros);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listaArchivoReporteCredito;
	}
	
	//fin MCG20141024
	
	public String getSysCodificacion() {
		return sysCodificacion;
	}

	public void setSysCodificacion(String sysCodificacion) {
		this.sysCodificacion = sysCodificacion;
	}

	public String getValorBlob() {
		return valorBlob;
	}

	public void setValorBlob(String valorBlob) {
		this.valorBlob = valorBlob;
	}
	@Override
	public void updatePosicionSustento(SustentoOperacion so) throws BOException {
		List parametros = new ArrayList();
		parametros.add(so.getPosicion());
		parametros.add(so.getId());

		super.executeNamedQuery("updatePosicionSustento" ,parametros);
	}

	public void setListaReporteCredito(List<ReporteCredito> listaReporteCredito) {
		this.listaReporteCredito = listaReporteCredito;
	}

	public List<DatosBasico> getListaDatosBasicoGrupoEmpresas() {
		return listaDatosBasicoGrupoEmpresas;
	}

	public void setListaDatosBasicoGrupoEmpresas(
			List<DatosBasico> listaDatosBasicoGrupoEmpresas) {
		this.listaDatosBasicoGrupoEmpresas = listaDatosBasicoGrupoEmpresas;
	}


	
	
	
}


