package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRCCMesBO;
import pe.com.bbva.iipf.pf.dao.LoadRCCMesDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRCCMesBO")
public class LoadRCCMesBOImpl extends  LoadGenericBO<Programa> implements LoadRCCMesBO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRCCMesDAO loadRCCMesDAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){
		try {
			loadRCCMesDAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRCCMesDAO, Constantes.ARCHIVO_RCCMES);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
