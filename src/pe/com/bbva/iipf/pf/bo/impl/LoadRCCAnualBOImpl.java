package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRCCAnualBO;
import pe.com.bbva.iipf.pf.dao.LoadRCCAnualDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRCCAnualBO")
public class LoadRCCAnualBOImpl   extends LoadGenericBO<Programa> implements LoadRCCAnualBO{
	
	Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private LoadRCCAnualDAO loadRCCAnualDAO;
	
	@Override
	public void executeLoadMassive(String strTipoCarga){
		try {
			loadRCCAnualDAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRCCAnualDAO, Constantes.ARCHIVO_RCCANUAL);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
