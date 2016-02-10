package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.CorreoBO;
import pe.com.bbva.iipf.pf.bo.LoadRenbecBO;
import pe.com.bbva.iipf.pf.dao.LoadRenbecDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRenbecBO")
public class LoadRenbecBOImpl  extends LoadGenericBO<Programa> implements LoadRenbecBO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRenbecDAO loadRenbecDAO;
	
	@Resource
	private CorreoBO correoBO;
	
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){
		
		try {
			loadRenbecDAO.executeLoadMassive(strFechaProceso,strNombreArchivo, strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRenbecDAO, Constantes.ARCHIVO_RENBEC);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
