package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadDepositoAplazoBO;
import pe.com.bbva.iipf.pf.dao.LoadDepositoAplazoDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadDepositoAplazoBOImp")
public class LoadDepositoAplazoBOImp extends LoadGenericBO<Programa> implements LoadDepositoAplazoBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadDepositoAplazoDAO loadDepositoAplazoDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadDepositoAplazoDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadDepositoAplazoDAO, Constantes.ARCHIVO_DEPOSITOAPLAZO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadDepositoAplazoDAO getLoadDepositoAplazoDAO() {
		return loadDepositoAplazoDAO;
	}

	public void setLoadDepositoAplazoDAO(LoadDepositoAplazoDAO loadDepositoAplazoDAO) {
		this.loadDepositoAplazoDAO = loadDepositoAplazoDAO;
	}


	
}
