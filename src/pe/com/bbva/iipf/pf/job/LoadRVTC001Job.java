package pe.com.bbva.iipf.pf.job;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRVTC001BO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;


@Service("loadRVTC001Job")
public class LoadRVTC001Job {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRVTC001BO loadRVTC001BO;
	
	@Resource
	private ParametroBO parametroBO;
	
	public void executeLoadMassive(){

		try {
			logger.info("INICIO loadRVTC001BO JOB");
			Long idParametro=0L;	
			 boolean TodoOk = true;
				Calendar calendarVerif = Calendar.getInstance();							
				String strDiaVerif = "0" + (calendarVerif.get(Calendar.DATE));
				String strMesVerif = "0" + (calendarVerif.get(Calendar.MONTH) + 1);
				String strAnoVerif = "" + calendarVerif.get(Calendar.YEAR);	
				String stroFechaVerif= "";
				stroFechaVerif = strAnoVerif.substring(0, 4) + (strMesVerif.substring(strMesVerif.length() - 2))
							+ (strDiaVerif.substring(strDiaVerif.length() - 2));
			idParametro=parametroBO.ValidaCargaEnProceso(Constantes.COD_MONITORJOB_RVTC001);	//para disparar un Job una sola vez									
			if (idParametro>0) {
				int  contador=0;				
				contador=parametroBO.ValidaCargaAutomatica(stroFechaVerif,Constantes.ARCHIVO_RVTC001);
				
				if (contador >0){
					 TodoOk = false;					
				 }
				 if (TodoOk) {	
					 loadRVTC001BO.executeLoadMassive(Constantes.TIPOCARGA_AUTOMATICO);
				 }
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RVTC001);
			}
			logger.info("FIN loadRVTC001BO JOB");
		} catch (Exception e) {
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RVTC001);
			logger.error(e.getMessage(), e);
			
		}
	}
	
	
	public void executeLoadManual(){

		try {
			logger.info("INICIO loadRVTC001BO JOB");
			loadRVTC001BO.executeLoadMassive(Constantes.TIPOCARGA_MANUAL);	
			logger.info("FIN loadRVTC001BO JOB");
		} catch (Exception e) {			
			logger.error(e.getMessage(), e);
			
		}
	}
	
	private void updateParametroMonitorInactivo(String strCodigo){
		try {
			Parametro parametroMonitor =parametroBO.findByNombreParametro(strCodigo);	
			parametroMonitor.setValor("0");
			parametroBO.update(parametroMonitor);
	
		} catch (Exception ex) {				
			logger.error("Error al actualizar parametro monitor. " + ex.getMessage(), ex);
		}
	}	

}
