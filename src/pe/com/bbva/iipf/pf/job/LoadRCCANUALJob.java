package pe.com.bbva.iipf.pf.job;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRCCAnualBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRCCANUALJob")
public class LoadRCCANUALJob{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRCCAnualBO loadRCCAnualBO;
	@Resource
	private ParametroBO parametroBO;
	
	public void executeLoadMassive(){

		try {
			logger.info("INICIO loadRCCAnual JOB");
			Long idParametro=0L;
			 boolean TodoOk = true;
				Calendar calendarVerif = Calendar.getInstance();							
				String strDiaVerif = "0" + (calendarVerif.get(Calendar.DATE));
				String strMesVerif = "0" + (calendarVerif.get(Calendar.MONTH) + 1);
				String strAnoVerif = "" + calendarVerif.get(Calendar.YEAR);	
				String stroFechaVerif= "";
				stroFechaVerif = strAnoVerif.substring(0, 4) + (strMesVerif.substring(strMesVerif.length() - 2))
							+ (strDiaVerif.substring(strDiaVerif.length() - 2));
			
			idParametro=parametroBO.ValidaCargaEnProceso(Constantes.COD_MONITORJOB_RCCANUAL);	//para disparar un Job una sola vez									
			if (idParametro>0) {

				int  contador=0;				
				contador=parametroBO.ValidaCargaAutomatica(stroFechaVerif,Constantes.ARCHIVO_RCCANUAL);
				
				if (contador >0){
					 TodoOk = false;					
				 }
				 if (TodoOk) {	
					 loadRCCAnualBO.executeLoadMassive(Constantes.TIPOCARGA_AUTOMATICO);
				 }
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RCCANUAL);
			}
			logger.info("FIN loadRCCAnual JOB");
		} catch (Exception e) {
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RCCANUAL);
			logger.error(e.getMessage(), e);
		}
	}
	
	public void executeLoadManual(){

		try {
			logger.info("INICIO loadRCCAnual JOB");
			loadRCCAnualBO.executeLoadMassive(Constantes.TIPOCARGA_MANUAL);
			logger.info("FIN loadRCCAnual JOB");
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
