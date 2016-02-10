package pe.com.bbva.iipf.seguridad.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.seguridad.bo.RolPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.RolPerfil;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("rolPerfilBO")
public class RolPerfilBOImpl extends GenericBOImpl implements RolPerfilBO{
	
	@Resource
	private TablaBO tablaBO;
	
	@Override
	public List<RolPerfil> getListaRolPerfil(Long idPerfil)throws BOException  {
		HashMap<String,Long> params = new HashMap<String,Long>();
		params.put("perfil.id", idPerfil);
		List<RolPerfil> listatemp = null;
		try {
			listatemp = findByParams2(RolPerfil.class, params);			
		} catch (BOException e) {
			throw new BOException(e);
		}
		return listatemp;
	}
	
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void actualizarRolPerfil(List<RolPerfil> listaRolPerfil)
//			throws BOException {
//		
//		super.saveCollection(listaRolPerfil);
//		
//	}
	public void beforeRolPerfil(List<RolPerfil> listaRolPerfil,Long idPerfil){
		for(RolPerfil dbtemp : listaRolPerfil){

			Long idRol=dbtemp.getRol().getId();
			HashMap<String, Long> parametros = new HashMap<String, Long>();
			parametros.put("perfil.id", idPerfil);
			parametros.put("rol.id", idRol);
			try {
				List<RolPerfil> olista =  super.findByParams2(RolPerfil.class, parametros);	
				if(olista!=null && olista.size()>0 && !olista.isEmpty()){
					dbtemp.setId(olista.get(0).getId());											
				}
			} catch (Exception e) {
				 
			}			
		}		
	}
	
	private boolean validateRolPerfil(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarRolPerfil(List<RolPerfil> listaRolPerfil,Long idPerfil) throws BOException{
		beforeRolPerfil(listaRolPerfil,idPerfil);
		if(validateRolPerfil()){
			try {
				HashMap<String,Long> params = new HashMap<String,Long>();
				params.put("perfil.id", idPerfil);
				List<RolPerfil> listatemp =  findByParams2(RolPerfil.class, params);
				List<RolPerfil> listaDel = new ArrayList<RolPerfil>();
				boolean flag= false;
				for(RolPerfil acc : listatemp ){
					for(RolPerfil acctemp : listaRolPerfil){
						if(acctemp.getId()!=null && acctemp.getId().equals(acc.getId())){						
							flag=true;
						}
					}
					if(!flag){
						listaDel.add(acc);
					}
					flag= false;
				}
				saveCollection(listaRolPerfil);
				deleteCollection(listaDel);
			} catch (BOException e) {
				throw new BOException(e);
			}
		}
	}

}
