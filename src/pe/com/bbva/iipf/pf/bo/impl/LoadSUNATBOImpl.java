package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadSUNATBO;
import pe.com.bbva.iipf.pf.dao.LoadSUNATDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadSUNATBO")
public class LoadSUNATBOImpl  extends LoadGenericBO<Programa> implements LoadSUNATBO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadSUNATDAO loadSUNATDAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){

		try {
			loadSUNATDAO.executeLoadMassive(strTipoCarga);
			enviarCorreo((LoadGenericDao)loadSUNATDAO, Constantes.ARCHIVO_SUNAT);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadSUNATDAO getLoadSUNATDAO() {
		return loadSUNATDAO;
	}

	public void setLoadSUNATDAO(LoadSUNATDAO loadSUNATDAO) {
		this.loadSUNATDAO = loadSUNATDAO;
	}

}
