package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRatingBO;
import pe.com.bbva.iipf.pf.dao.LoadRatingDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRatingBO")
public class LoadRatingBOImpl  extends LoadGenericBO<Programa> implements LoadRatingBO{
	Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private LoadRatingDAO loadRatingDAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){
		try {
			loadRatingDAO.executeLoadMassive( strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRatingDAO, Constantes.ARCHIVO_RATING);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
