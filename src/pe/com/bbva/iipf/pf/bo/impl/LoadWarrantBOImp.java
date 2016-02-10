package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadWarrantBO;
import pe.com.bbva.iipf.pf.dao.LoadWarrantDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadWarrantBOImp")
public class LoadWarrantBOImp extends LoadGenericBO<Programa> implements LoadWarrantBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadWarrantDAO loadWarrantDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadWarrantDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadWarrantDAO, Constantes.ARCHIVO_WARRANT);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadWarrantDAO getLoadWarrantDAO() {
		return loadWarrantDAO;
	}

	public void setLoadWarrantDAO(LoadWarrantDAO loadWarrantDAO) {
		this.loadWarrantDAO = loadWarrantDAO;
	}


	
}
