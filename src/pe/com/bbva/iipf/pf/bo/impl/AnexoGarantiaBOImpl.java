package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.AnexoGarantiaBO;
import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("anexoGarantiaBO")
public class AnexoGarantiaBOImpl extends GenericBOImpl<AnexoGarantia> implements AnexoGarantiaBO {

	private Programa programa;
	private List<AnexoGarantia> listaGarantiaAnexo=new ArrayList<AnexoGarantia>();

	public void beforeSUDAnexoGarantias(){
		int pos=0;
		for(AnexoGarantia ag : listaGarantiaAnexo){			
			ag.setPos(pos);
			pos++;
		}
	}
	@Override
	public void saveAnexosGarantia() throws BOException {
		List<AnexoGarantia> lstGarantiaAnexoBD=new ArrayList<AnexoGarantia>();
		beforeSUDAnexoGarantias();
		lstGarantiaAnexoBD=findAnexoXPrograma(getPrograma());	
		//eliminamos
		boolean flagEliminar=false;
		List<AnexoGarantia> lstEliminar=new ArrayList<AnexoGarantia>();
		for(AnexoGarantia garantiaAnexoBD:lstGarantiaAnexoBD){
			flagEliminar=true;
			for(AnexoGarantia garantiaAnexo:listaGarantiaAnexo){
				if(garantiaAnexoBD.getId().equals(garantiaAnexo.getId())){					
					flagEliminar=false;
					break;
				}
			}
			if(flagEliminar){
				lstEliminar.add(garantiaAnexoBD);
			}
		}
		super.deleteCollection(lstEliminar);
		for(AnexoGarantia garantiaAnexo:listaGarantiaAnexo){
			garantiaAnexo.setPrograma(programa);			
			if(garantiaAnexo.getId()==null){
				garantiaAnexo.setCodUsuarioCreacion(getUsuarioSession().getRegistroHost());
				garantiaAnexo.setFechaCreacion(new Date());	
				garantiaAnexo.setEstado("A");
				super.saveOrUpdate(garantiaAnexo);
			}else{
				garantiaAnexo.setCodUsuarioModificacion(getUsuarioSession().getRegistroHost());
				garantiaAnexo.setFechaModificacion(new Date());
				garantiaAnexo.setEstado("A");				
				super.save(garantiaAnexo);
			}
		}		
	}
	@Override
	public List<AnexoGarantia> findAnexoXPrograma(Programa programa) throws  BOException{
		HashMap<String, Long> parametros=new HashMap<String, Long>();
		parametros.put("programa", programa.getId());
		HashMap<String, String> order=new HashMap<String, String>();
		order.put("1", "empresa asc");
		order.put("2", "numeroGarantia asc");
		order.put("3","pos asc");
		order.put("4","id asc");
		List<AnexoGarantia> lstAnexoGarantia= super.findByParamsOrder2(AnexoGarantia.class,parametros,order);
		int i=1;
		for(AnexoGarantia aux:lstAnexoGarantia){
			aux.setCodigoGarantiaAnexo(i++);
		}
		return lstAnexoGarantia;
	}
	
	//ini MCG20130409
	@Override
	public List<AnexoGarantia> findAnexoXPrograma(Programa programa,String EmpresaGrupo) throws  BOException{
		HashMap<String, Object> parametros=new HashMap<String, Object>();
		parametros.put("programa", programa.getId());
		parametros.put("empresa", EmpresaGrupo);		
		HashMap<String, String> order=new HashMap<String, String>();
		//ini MCG20130918
		order.put("1", "numeroGarantia asc");
		order.put("2","pos asc");
		order.put("3","id asc");
		//List<AnexoGarantia> lstAnexoGarantia= super.findByParams(AnexoGarantia.class,parametros);
		List<AnexoGarantia> lstAnexoGarantia= super.findByParamsOrder2(AnexoGarantia.class,parametros,order);
		//fin MCG20130918
		int i=1;
		for(AnexoGarantia aux:lstAnexoGarantia){
			aux.setCodigoGarantiaAnexo(i++);
		}
		return lstAnexoGarantia;
	}
	//fin MCG20130409
	
	//ini MCG20130415
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaAnexoGarantia(List<AnexoGarantia> olistAnexoGarantia) throws BOException{		
				saveCollection(olistAnexoGarantia);
	}
	//fin MCG20130415
	
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	} 
	public List<AnexoGarantia> getListaGarantiaAnexo() {
		return listaGarantiaAnexo;
	}

	public void setListaGarantiaAnexo(List<AnexoGarantia> listaGarantiaAnexo) {
		this.listaGarantiaAnexo = listaGarantiaAnexo;
	}
	
}
