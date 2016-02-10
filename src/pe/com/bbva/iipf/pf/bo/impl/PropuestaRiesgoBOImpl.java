package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.PropuestaRiesgoBO;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;


@Service("propuestaRiesgoBO")
public class PropuestaRiesgoBOImpl extends GenericBOImpl implements	PropuestaRiesgoBO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	private Programa programa;	
	private ProgramaBlob programaBlob = new ProgramaBlob(); 	
	
	private String comentPropuestaRiesgo;
	private String comentEstructuraLimite;
	private String considPropuestaRiesgo;
	private List<EstructuraLimite> listEstructuraLimite = new ArrayList<EstructuraLimite>();
		
	@Override
	public List<EstructuraLimite> findEstructuraLimiteByIdprograma(Long idPrograma) throws BOException   	{
		List<Long> params = new ArrayList<Long>();
		params.add(idPrograma);
		List<EstructuraLimite> listatemp = null;
		try {	
			listatemp = executeListNamedQuery("listarEstructuraLimite", params); 	
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatemp;
	}
		
	
	
	public void beforeUpdatePropuestaRiesgo(){
		//lista.clear();		
	}
	
	public boolean validateUpdatePropuestaRiesgo(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updatePropuestaRiesgo() throws BOException {
		beforeUpdatePropuestaRiesgo();
		if(validateUpdatePropuestaRiesgo()){
			List parametros = new ArrayList();	
			parametros.add(getPrograma().getTipoEstructura()==null?"":getPrograma().getTipoEstructura());		
			parametros.add(getPrograma().getTipoMiles()==null?"":getPrograma().getTipoMiles().toString());	
			
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updatePropuestaRiesgo",parametros);
			
			saveEstructuraLimite();
			
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();

			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
			//super.saveCollection(listEstructuraLimite);	
			
//			programaBlobBO.setPrograma(programa);
//			programaBlobBO.setCampoBlob("campoLibrePR");
//			programaBlobBO.setValorBlob(comentPropuestaRiesgo);
//			programaBlobBO.save(programaBlob);	
//			
//			programaBlobBO.setCampoBlob("estructuraLimite");
//			programaBlobBO.setValorBlob(comentEstructuraLimite);
//			programaBlobBO.save(programaBlob);
//			
//			programaBlobBO.setCampoBlob("consideracionPR");
//			programaBlobBO.setValorBlob(considPropuestaRiesgo);
//			programaBlobBO.save(programaBlob);
		}
		
		//afterUpdatePropuestaRiesgo();

	}

	public void beforeEstructuraLimite(){
		int posicion=0;
		for(EstructuraLimite eli : listEstructuraLimite){
			eli.setPosicion(posicion);
			eli.setPrograma(programa);
			posicion++;
		}
	}
	
	public boolean validationEstructuraLimite(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra las participaciones
	 * siginficativas
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEstructuraLimite() throws BOException{
		try {			
			beforeEstructuraLimite();
			if(validationEstructuraLimite()){
				HashMap<String,Long> params = new HashMap<String,Long>();
				params.put("programa", programa.getId());
				List<EstructuraLimite> listatemp =  findByParams2(EstructuraLimite.class, params);
				List<EstructuraLimite> listaDel = new ArrayList<EstructuraLimite>();
				boolean flag= false;
				for(EstructuraLimite eli : listatemp ){
					for(EstructuraLimite elitemp : listEstructuraLimite){
						if(elitemp.getId()!=null && elitemp.getId().equals(eli.getId())){
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(eli);
					}
					flag= false;
				}
				//ini MCG20111026
				
					List parametroste = new ArrayList();	
					parametroste.add(getPrograma().getTipoEstructura()==null?"":getPrograma().getTipoEstructura());	
					parametroste.add(getPrograma().getTipoMiles()==null?"":getPrograma().getTipoMiles().toString());	
					
					parametroste.add(getPrograma().getId());
					super.executeNamedQuery("updatePropuestaRiesgo",parametroste);	
						
				//fin MCG20111026
				
				saveCollection(listEstructuraLimite);
				deleteCollection(listaDel);
				programaBO.actualizarFechaModificacionPrograma(programa.getId());
			}
		} catch (Exception e) {
			throw new BOException(e);//logger.error(StringUtil.getStackTrace(e));
		}	
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaEstructuraLimite(List<EstructuraLimite> olistEstructuraLimite) throws BOException{		
				saveCollection(olistEstructuraLimite);
	}
	
	
	public void delete(EstructuraLimite t) throws BOException{
		super.delete(t);
	}

	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}
	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}



	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}



	public ProgramaBlob getProgramaBlob() {
		return programaBlob;
	}
	public void setProgramaBlob(ProgramaBlob programaBlob) {
		this.programaBlob = programaBlob;
	}



	public String getComentPropuestaRiesgo() {
		return comentPropuestaRiesgo;
	}
	public void setComentPropuestaRiesgo(String comentPropuestaRiesgo) {
		this.comentPropuestaRiesgo = comentPropuestaRiesgo;
	}



	public String getComentEstructuraLimite() {
		return comentEstructuraLimite;
	}
	public void setComentEstructuraLimite(String comentEstructuraLimite) {
		this.comentEstructuraLimite = comentEstructuraLimite;
	}

	public String getConsidPropuestaRiesgo() {
		return considPropuestaRiesgo;
	}
	public void setConsidPropuestaRiesgo(String considPropuestaRiesgo) {
		this.considPropuestaRiesgo = considPropuestaRiesgo;
	}
	
	public List<EstructuraLimite> getListEstructuraLimite() {
		return listEstructuraLimite;
	}
	public void setListEstructuraLimite(List<EstructuraLimite> listEstructuraLimite) {
		this.listEstructuraLimite = listEstructuraLimite;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}
	
}
