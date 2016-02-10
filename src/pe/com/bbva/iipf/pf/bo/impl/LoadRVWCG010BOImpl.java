package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRVWCG010BO;
import pe.com.bbva.iipf.pf.dao.LoadRVWCG010DAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRVWCG010BO")
public class LoadRVWCG010BOImpl  extends LoadGenericBO<Programa> implements LoadRVWCG010BO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRVWCG010DAO loadRVWCG010DAO;	
	@Override
	public void executeLoadMassive(String strTipoCarga){

		try {
			loadRVWCG010DAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRVWCG010DAO, Constantes.ARCHIVO_RVWCG010);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadRVWCG010DAO getLoadRVWCG010DAO() {
		return loadRVWCG010DAO;
	}

	public void setLoadRVWCG010DAO(LoadRVWCG010DAO loadRVWCG010DAO) {
		this.loadRVWCG010DAO = loadRVWCG010DAO;
	}
	
	
}
