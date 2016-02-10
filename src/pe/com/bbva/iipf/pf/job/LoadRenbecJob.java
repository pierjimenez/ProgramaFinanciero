package pe.com.bbva.iipf.pf.job;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadRenbecBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadRenbecJob")
public class LoadRenbecJob{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadRenbecBO loadRenbecBO;
	
	@Resource
	private ParametroBO parametroBO;
	
	public void executeLoadMassive(){
		try {
		logger.info("INICIO LoadRenbecBO JOB");		
		logger.error("INICIO LoadRenbecBO JOB");
		Long idParametro=0L;	
		 boolean TodoOk = true;
			Calendar calendarVerif = Calendar.getInstance();							
			String strDiaVerif = "0" + (calendarVerif.get(Calendar.DATE));
			String strMesVerif = "0" + (calendarVerif.get(Calendar.MONTH) + 1);
			String strAnoVerif = "" + calendarVerif.get(Calendar.YEAR);	
			String stroFechaVerif= "";
			stroFechaVerif = strAnoVerif.substring(0, 4) + (strMesVerif.substring(strMesVerif.length() - 2))
						+ (strDiaVerif.substring(strDiaVerif.length() - 2));
		idParametro=parametroBO.ValidaCargaEnProceso(Constantes.COD_MONITORJOB_RENBECMENSUAL);	//para disparar un Job una sola vez									
		if (idParametro>0) {
			int  contador=0;				
			contador=parametroBO.ValidaCargaAutomatica(stroFechaVerif,Constantes.ARCHIVO_RENBEC);			
			if (contador >0){
				 TodoOk = false;					
			 }
			 if (TodoOk) {	
				 	String strExtension;
					Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILES_CPY_HOST);
					String rutaCopy=parametro.getValor();
					Parametro oparametro = parametroBO.findByNombreParametro(Constantes.PREFIJO_ARCHRENBEC);
					String prefijoArchRenbec=oparametro.getValor();				
					Parametro oextension = parametroBO.findByNombreParametro(Constantes.EXT_ARCH_RENBEC);
					strExtension=oextension.getValor()==null?"TXT":oextension.getValor();				
				for (int dia=1;dia<8;dia++){					
					Calendar calendar = Calendar.getInstance();	
					calendar.add(Calendar.DAY_OF_MONTH, -dia);			
					String strDiaActual = "0" + (calendar.get(Calendar.DATE));
					String strMes = "0" + (calendar.get(Calendar.MONTH) + 1);
					String strAno = "" + calendar.get(Calendar.YEAR);	
										
					String strFechaProceso = "";
					strFechaProceso = strAno.substring(0, 4) + (strMes.substring(strMes.length() - 2))
								+ (strDiaActual.substring(strDiaActual.length() - 2));	
					
					String strNombreArchivo = prefijoArchRenbec + strFechaProceso + "." + strExtension;
					String strNombreCompletoArchivo = rutaCopy + File.separator + strNombreArchivo;
						
					logger.info("LoadRenbecBO :" +strNombreCompletoArchivo);
					boolean existsArchivo = (new File(strNombreCompletoArchivo)).exists();
					logger.info("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo delete:" +strNombreCompletoArchivo);
				
					if (existsArchivo)
					{			
						loadRenbecBO.executeLoadMassive(strFechaProceso,strNombreArchivo,Constantes.TIPOCARGA_AUTOMATICO);
						eliminarFileAntiguos(prefijoArchRenbec,strExtension,rutaCopy);
						break;
					}
				}
				
			}
				
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RENBECMENSUAL);
		}
		logger.info("FIN LoadRenbecBO JOB");
		} catch (Exception e) {
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_RENBECMENSUAL);
			logger.error(e.getMessage(), e);
		}
		logger.error("FIN LoadRenbecBO JOB");
	}
	
	private void eliminarFileAntiguos(String prefijoArchRenbec,String strExtension,String rutaCopy){
		try {
		
	
			int intMesPermanencia=90;
			File filedel=null;
			try {
				Parametro ppermanencia= parametroBO.findByNombreParametro(Constantes.MES_PERMANENCIAFILE);
				intMesPermanencia=(Integer.valueOf(ppermanencia.getValor()==null?"3":ppermanencia.getValor()))*30;
				if (intMesPermanencia==0){
					intMesPermanencia=90;
				}
			} catch (Exception e) {
				intMesPermanencia=90;						
			}
	
			Calendar calendardel = Calendar.getInstance();	
			calendardel.add(Calendar.DAY_OF_MONTH, -(intMesPermanencia));			
			String strDiaActualdel = "0" + (calendardel.get(Calendar.DATE));
			String strMesdel = "0" + (calendardel.get(Calendar.MONTH) + 1);
			String strAnodel = "" + calendardel.get(Calendar.YEAR);	
			
		    String strFechaProcesodel = "";
			strFechaProcesodel = strAnodel.substring(0, 4) + (strMesdel.substring(strMesdel.length() - 2))
					+ (strDiaActualdel.substring(strDiaActualdel.length() - 2));			
			
			String strNombreArchivodel = prefijoArchRenbec + strFechaProcesodel + "." +strExtension;
			String strNombreCompletoArchivodel = rutaCopy + File.separator + strNombreArchivodel;
			logger.info("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo delete:" +strNombreCompletoArchivodel);
			////logger.error("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo delete:" +strNombreCompletoArchivodel);
			//System.out.print("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo delete:" +strNombreCompletoArchivodel);
			boolean existsArchivodel = (new File(strNombreCompletoArchivodel)).exists();
			if(!existsArchivodel){
				logger.error("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo :" +strNombreCompletoArchivodel+" no se ha encontrado..");
				logger.info("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo :" +strNombreCompletoArchivodel+" no se ha encontrado..");
			}
			
			if (existsArchivodel)
			{	
				String pathToFile=strNombreCompletoArchivodel;
				try {
					filedel = new File(pathToFile);
					filedel.delete();
				} catch (Exception ed) {
					logger.error("executeLoadMassive::Error::delete archivo historico:"+ ed.getMessage());
				}
				
			}	
		} catch (Exception e) {
			logger.error("executeLoadMassive::Error::delete archivo historico:"+ e.getMessage());
		}
	}
	
	//ini MCG20121211	
		public void executeLoadManual(String fechaProceso){

			try {
				logger.info("INICIO LoadRenbecBO JOB");	
				String strExtension;
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
				String strFecha = fechaProceso;
				Date dtFechaProceso = null;
				try {
					dtFechaProceso = formatoDelTexto.parse(strFecha);
				} catch (ParseException ex) {
					ex.printStackTrace();
				} 	
			   
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dtFechaProceso);
				String strDiaActual = "0" + calendar.get(Calendar.DATE);
				String strMes = "0" + (calendar.get(Calendar.MONTH) + 1);
				String strAno = "" + calendar.get(Calendar.YEAR);	
				String strFechaProceso = "";
				 strFechaProceso = strAno.substring(0, 4) + (strMes.substring(strMes.length() - 2))
							+ (strDiaActual.substring(strDiaActual.length() - 2));
				
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILES_CPY_HOST);
				String rutaCopy=parametro.getValor();
				Parametro oparametro = parametroBO.findByNombreParametro(Constantes.PREFIJO_ARCHRENBEC);
				String prefijoTipoCambio=oparametro.getValor();
				
				Parametro oextension = parametroBO.findByNombreParametro(Constantes.EXT_ARCH_RENBEC);
				strExtension=oextension.getValor()==null?"TXT":oextension.getValor();
										
				String strNombreArchivo = prefijoTipoCambio + strFechaProceso + "." + strExtension;
				String strNombreCompletoArchivo = rutaCopy + File.separator + strNombreArchivo;
				boolean existsArchivo = (new File(strNombreCompletoArchivo)).exists();
				if(!existsArchivo){logger.error("LoadRenbecBO JOB:executeLoadMassive: Nombre Completo Archivo :" +strNombreArchivo+" no se ha encontrado..");}
				if (existsArchivo)
				{			
					loadRenbecBO.executeLoadMassive(strFechaProceso,strNombreArchivo,Constantes.TIPOCARGA_MANUAL);
				}
				logger.info("FIN LoadRenbecBO JOB");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		//fin MCG20121211
		
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
