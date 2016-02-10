package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadGarantiaBO;
import pe.com.bbva.iipf.pf.dao.LoadGarantiaDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadGarantiaBOImp")
public class LoadGarantiaBOImp extends LoadGenericBO<Programa> implements LoadGarantiaBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadGarantiaDAO loadGarantiaDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadGarantiaDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadGarantiaDAO, Constantes.ARCHIVO_GARANTIA);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadGarantiaDAO getLoadGarantiaDAO() {
		return loadGarantiaDAO;
	}

	public void setLoadGarantiaDAO(LoadGarantiaDAO loadGarantiaDAO) {
		this.loadGarantiaDAO = loadGarantiaDAO;
	}

}
