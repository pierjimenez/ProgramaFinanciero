package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("politicasRiesgoBO")
public class PoliticasRiesgoBOImpl extends GenericBOImpl implements	PoliticasRiesgoBO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;
	
	private Programa programa;	
	private List<LimiteFormalizado> listLimiteFormalizado= new ArrayList<LimiteFormalizado>();
		
	@Override
	public List<LimiteFormalizado> findLimiteFormalizadoByIdprograma(Long Idprograma) throws BOException   	{
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("programa", Idprograma);
		
		HashMap<String,String> order= new HashMap<String,String>();
		order.put("id", " asc");
		List<LimiteFormalizado> listatemp = null;
		List<LimiteFormalizado> listatempfin = new ArrayList<LimiteFormalizado>(); ;
		
		try {	
			//ini MCG20130903 listatemp = findByParams2(LimiteFormalizado.class, params); 
			listatemp = findByParamsOrder(LimiteFormalizado.class, params,order);
			if (listatemp!=null && listatemp.size()>0  ){
				int contl=0;
				for (LimiteFormalizado olimite :listatemp){
					if (contl<6){
						listatempfin.add(olimite);
					}
					contl=contl+1;
				}							
			}
			
						
		} catch (BOException  e) {
			throw new BOException(e);
		}
		return listatempfin;
	}
	
	
	
	public void beforeUpdatePoliticasRiesgo(){
		//lista.clear();		
	}
	
	public boolean validateUpdatePoliticasRiesgo(){
		return true;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updatePoliticasRiesgo() throws BOException {
		beforeUpdatePoliticasRiesgo();
		if(validateUpdatePoliticasRiesgo()){	
			List parametros = new ArrayList();			
			//limite Autorizado;
			parametros.add(getPrograma().getLimiteAutorizadoPRG()==null?"":getPrograma().getLimiteAutorizadoPRG());
			//Proxima Revision;
			parametros.add(getPrograma().getProximaRevisionPRG()==null?"":getPrograma().getProximaRevisionPRG());
			//Motivo Proxima;
			parametros.add(getPrograma().getMotivoProximaPRG()==null?"":getPrograma().getMotivoProximaPRG());
			//add 06022012 EPOMAYAY
			parametros.add(getPrograma().getTipoMilesPLR());
			//add 06022012 EPOMAYAY
			parametros.add(getPrograma().getId());
			super.executeNamedQuery("updatePoliticasRiesgo",parametros);
			
			saveLimiteFormalizado();
			
			String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();
			
			programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
			//super.saveCollection(listLimiteFormalizado);				

		}
		
		//afterUpdatePoliticasRiesgo();

	}
	
	
	public void beforeLimiteFormalizado(){
		for(LimiteFormalizado lf : listLimiteFormalizado){
			lf.setPrograma(programa);
		}
	}
	
	public boolean validationLimiteFormalizado(){
		return true;
	}
	
	/**
	 * valida y actualiza y registra las participaciones
	 * siginficativas
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveLimiteFormalizado() throws BOException{
		beforeLimiteFormalizado();
		if(validationLimiteFormalizado()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", programa.getId());
			List<LimiteFormalizado> listatemp =  findByParams2(LimiteFormalizado.class, params);
			List<LimiteFormalizado> listaDel = new ArrayList<LimiteFormalizado>();
			boolean flag= false;
			for(LimiteFormalizado lf : listatemp ){
				for(LimiteFormalizado lftemp : listLimiteFormalizado){
					if(lftemp.getId()!=null && lftemp.getId().equals(lf.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lf);
				}
				flag= false;
			}
			saveCollection(listLimiteFormalizado);
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
			deleteCollection(listaDel);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaLimiteFormalizado(List<LimiteFormalizado> olistLimiteFormalizado) throws BOException{
			saveCollection(olistLimiteFormalizado);		
	}

	public Programa getPrograma() {
		return programa;
	}	

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public List<LimiteFormalizado> getListLimiteFormalizado() {
		return listLimiteFormalizado;
	}

	public void setListLimiteFormalizado(
			List<LimiteFormalizado> listLimiteFormalizado) {
		this.listLimiteFormalizado = listLimiteFormalizado;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}
	
}
