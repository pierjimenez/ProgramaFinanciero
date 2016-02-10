package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadFianzaSolidariaBO;
import pe.com.bbva.iipf.pf.dao.LoadFianzaSolidariaDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadFianzaSolidariaBOImp")
public class LoadFianzaSolidariaBOImp extends LoadGenericBO<Programa> implements LoadFianzaSolidariaBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadFianzaSolidariaDAO loadFianzaSolidariaDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadFianzaSolidariaDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadFianzaSolidariaDAO, Constantes.ARCHIVO_FIANZASOLIDARIA);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadFianzaSolidariaDAO getLoadFianzaSolidariaDAO() {
		return loadFianzaSolidariaDAO;
	}

	public void setLoadFianzaSolidariaDAO(LoadFianzaSolidariaDAO loadFianzaSolidariaDAO) {
		this.loadFianzaSolidariaDAO = loadFianzaSolidariaDAO;
	}


	
}
