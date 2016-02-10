package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadCuentaGarantiaBO;
import pe.com.bbva.iipf.pf.dao.LoadCuentaGarantiaDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadCuentaGarantiaBOImp")
public class LoadCuentaGarantiaBOImp extends LoadGenericBO<Programa> implements LoadCuentaGarantiaBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadCuentaGarantiaDAO loadCuentaGarantiaDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadCuentaGarantiaDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadCuentaGarantiaDAO, Constantes.ARCHIVO_CUENTAGARANTIA);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadCuentaGarantiaDAO getLoadCuentaGarantiaDAO() {
		return loadCuentaGarantiaDAO;
	}

	public void setLoadCuentaGarantiaDAO(LoadCuentaGarantiaDAO loadCuentaGarantiaDAO) {
		this.loadCuentaGarantiaDAO = loadCuentaGarantiaDAO;
	}


	
}
