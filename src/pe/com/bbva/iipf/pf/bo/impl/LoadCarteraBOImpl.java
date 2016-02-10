package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadCarteraBO;
import pe.com.bbva.iipf.pf.dao.LoadCarteraDAO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.bo.GenericBOImpl;
@Service("loadCarteraBO")
public class LoadCarteraBOImpl extends GenericBOImpl<Programa> implements LoadCarteraBO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource 
	private LoadCarteraDAO loadCarteraDAO;
	@Override
	public void executeLoadMassive(String strTipoCarga){
		try {
			loadCarteraDAO.executeLoadMassive(strTipoCarga);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
