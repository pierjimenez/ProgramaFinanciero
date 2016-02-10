package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.LoadPrestamoBO;
import pe.com.bbva.iipf.pf.dao.LoadPrestamoDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadPrestamoBOImp")
public class LoadPrestamoBOImp extends LoadGenericBO<Programa> implements LoadPrestamoBO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadPrestamoDAO loadPrestamoDAO;
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){

		try {
			loadPrestamoDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadPrestamoDAO, Constantes.ARCHIVO_PRESTAMO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public LoadPrestamoDAO getLoadPrestamoDAO() {
		return loadPrestamoDAO;
	}

	public void setLoadPrestamoDAO(LoadPrestamoDAO loadPrestamoDAO) {
		this.loadPrestamoDAO = loadPrestamoDAO;
	}


	
}
