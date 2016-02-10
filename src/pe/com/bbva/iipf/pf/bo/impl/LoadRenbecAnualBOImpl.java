package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.CorreoBO;
import pe.com.bbva.iipf.pf.bo.LoadRenbecAnualBO;
import pe.com.bbva.iipf.pf.dao.LoadRenbecAnualDAO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRenbecAnualBO")
public class LoadRenbecAnualBOImpl  extends LoadGenericBO<Programa> implements LoadRenbecAnualBO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRenbecAnualDAO loadRenbecAnualDAO;
	
	@Resource
	private CorreoBO correoBO;
	
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){
		
		try {
			loadRenbecAnualDAO.executeLoadMassive(strFechaProceso,strNombreArchivo,strTipoCarga);
			enviarCorreo((LoadGenericDao)loadRenbecAnualDAO, Constantes.ARCHIVO_RENBEC_ANUAL);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
