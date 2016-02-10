package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadDetalleGarantiaBO;
import pe.com.bbva.iipf.pf.dao.LoadDetalleGarantiaDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadDetalleGarantiaBOImp")
public class LoadDetalleGarantiaBOImp extends LoadGenericBO<Programa> implements LoadDetalleGarantiaBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadDetalleGarantiaDAO loadDetalleGarantiaDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadDetalleGarantiaDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadDetalleGarantiaDAO, Constantes.ARCHIVO_DETALLEGARANTIA);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadDetalleGarantiaDAO getLoadDetalleGarantiaDAO() {
		return loadDetalleGarantiaDAO;
	}

	public void setLoadDetalleGarantiaDAO(LoadDetalleGarantiaDAO loadDetalleGarantiaDAO) {
		this.loadDetalleGarantiaDAO = loadDetalleGarantiaDAO;
	}


	
}
