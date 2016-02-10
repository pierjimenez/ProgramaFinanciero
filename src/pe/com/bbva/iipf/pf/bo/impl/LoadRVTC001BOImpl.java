package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRVTC001BO;
import pe.com.bbva.iipf.pf.dao.LoadRVTC001DAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRVTC001BO")
public class LoadRVTC001BOImpl  extends LoadGenericBO<Programa> implements LoadRVTC001BO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRVTC001DAO loadRVTC001DAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){

		try {
			loadRVTC001DAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRVTC001DAO, Constantes.ARCHIVO_RVTC001);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadRVTC001DAO getLoadRVTC001DAO() {
		return loadRVTC001DAO;
	}

	public void setLoadRVTC001DAO(LoadRVTC001DAO loadRVTC001DAO) {
		this.loadRVTC001DAO = loadRVTC001DAO;
	}
	
}
