package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadStandByBO;
import pe.com.bbva.iipf.pf.dao.LoadStandByDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadStandByBOImp")
public class LoadStandByBOImp extends LoadGenericBO<Programa> implements LoadStandByBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadStandByDAO loadStandByDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadStandByDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadStandByDAO, Constantes.ARCHIVO_STANDBY);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadStandByDAO getLoadStandByDAO() {
		return loadStandByDAO;
	}

	public void setLoadStandByDAO(LoadStandByDAO loadStandByDAO) {
		this.loadStandByDAO = loadStandByDAO;
	}


	
}
