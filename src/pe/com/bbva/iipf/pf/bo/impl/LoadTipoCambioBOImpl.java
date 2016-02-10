package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadTipoCambioBO;
import pe.com.bbva.iipf.pf.dao.LoadTipoCambioDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadTipoCambioBO")
public class LoadTipoCambioBOImpl   extends LoadGenericBO<Programa> implements LoadTipoCambioBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadTipoCambioDAO loadTipoCambioDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadTipoCambioDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadTipoCambioDAO, Constantes.ARCHIVO_TIPO_CAMBIO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadTipoCambioDAO getLoadTipoCambioDAO() {
		return loadTipoCambioDAO;
	}

	public void setLoadTipoCambioDAO(LoadTipoCambioDAO loadTipoCambioDAO) {
		this.loadTipoCambioDAO = loadTipoCambioDAO;
	}
	

}
