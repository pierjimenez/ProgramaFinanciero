package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadHipotecaBO;
import pe.com.bbva.iipf.pf.dao.LoadHipotecaDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadHipotecaBOImp")
public class LoadHipotecaBOImp extends LoadGenericBO<Programa> implements LoadHipotecaBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadHipotecaDAO loadHipotecaDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadHipotecaDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadHipotecaDAO, Constantes.ARCHIVO_HIPOTECA);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadHipotecaDAO getLoadHipotecaDAO() {
		return loadHipotecaDAO;
	}

	public void setLoadHipotecaDAO(LoadHipotecaDAO loadHipotecaDAO) {
		this.loadHipotecaDAO = loadHipotecaDAO;
	}


	
}
