
package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadFondosMutuosBO;
import pe.com.bbva.iipf.pf.dao.LoadFondosMutuosDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadFondosMutuosBOImp")
public class LoadFondosMutuosBOImp extends LoadGenericBO<Programa> implements LoadFondosMutuosBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadFondosMutuosDAO loadFondosMutuosDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadFondosMutuosDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadFondosMutuosDAO, Constantes.ARCHIVO_FONDOSMUTUOS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadFondosMutuosDAO getLoadFondosMutuosDAO() {
		return loadFondosMutuosDAO;
	}

	public void setLoadFondosMutuosDAO(LoadFondosMutuosDAO loadFondosMutuosDAO) {
		this.loadFondosMutuosDAO = loadFondosMutuosDAO;
	}


	
}
