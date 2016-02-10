package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRCDBO;
import pe.com.bbva.iipf.pf.dao.LoadRCDDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRCDBO")
public class LoadRCDBOImpl extends   LoadGenericBO<Programa> implements LoadRCDBO{
	Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private LoadRCDDAO loadRCDDAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){
		
		try {
			loadRCDDAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRCDDAO, Constantes.ARCHIVO_RCD);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
