package pe.com.bbva.iipf.pf.job;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.LoadHipotecaBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;

@Service("loadHipotecaJob")
public class LoadHipotecaJob {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private LoadHipotecaBO loadHipotecaBO;

	@Resource
	private ParametroBO parametroBO;
	
	public void executeLoadMassive(){

		try {
			logger.info("INICIO LoadHipotecaJob JOB");
			Long idParametro=0L;
			 boolean TodoOk = true;
			String stroFechaVerif = getFechaYYYYMMDD(0,"0");
			idParametro=parametroBO.ValidaCargaEnProceso(Constantes.COD_MONITORJOB_HIPOTECA);	//para disparar un Job una sola vez									
			if (idParametro>0) {

				int  contador=0;				
				contador=parametroBO.ValidaCargaAutomatica(stroFechaVerif,Constantes.ARCHIVO_HIPOTECA);
				
				if (contador >0){
					 TodoOk = false;					
				 }
				 if (TodoOk) {		
		
					String strExtension;					
					Parametro parametro = parametroBO.findByNombreParametro(Constantes.DIR_FILES_CPY_HOST);
					String rutaCopy=parametro.getValor();
					Parametro oparametro = parametroBO.findByNombreParametro(Constantes.PREFIJO_ARCHHIPOTECA);
					String prefijoPrestamo=oparametro.getValor();					
					Parametro oextension = parametroBO.findByNombreParametro(Constantes.EXT_ARCH_HIPOTECA);
					strExtension=oextension.getValor()==null?"TXT":oextension.getValor();
					Parametro oformatoFecha = parametroBO.findByNombreParametro(Constantes.FORMAT_FECHA_HIPOTECA);
					String formatoFechafinal=oformatoFecha.getValor()==null?"yyMMdd":oformatoFecha.getValor();
					String activoFecha="AA";
					if (oformatoFecha.getValor()==null || (oformatoFecha.getValor()!=null && oformatoFecha.getValor().equals(""))){
						 activoFecha="NN";
					}
					String strFechaProceso = getFechaYYYYMMDD(-1,"1");
					String strFechaProcesoFormateado="";
					if (activoFecha.equals("AA")){
					 strFechaProcesoFormateado=getFechaFormateado(strFechaProceso,formatoFechafinal);					
					}
					String strNombreArchivo = prefijoPrestamo + strFechaProcesoFormateado + "." + strExtension;
					String strNombreCompletoArchivo = rutaCopy + File.separator + strNombreArchivo;				
					if (activoFecha.equals("AA")){
						deleteArchivosAntiguos(rutaCopy,prefijoPrestamo,strExtension,formatoFechafinal);				
					}
					boolean existsArchivo = (new File(strNombreCompletoArchivo)).exists();			
					if (existsArchivo)
					{			
						loadHipotecaBO.executeLoadMassive(strFechaProceso,strNombreArchivo,Constantes.TIPOCARGA_AUTOMATICO);
					}
				
				 }
			
				 updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_HIPOTECA);
		}
			
			logger.info("FIN LoadHipotecaJob JOB");
		} catch (Exception e) {
			updateParametroMonitorInactivo(Constantes.COD_MONITORJOB_HIPOTECA);
			logger.error(e.getMessage(), e);
		}
	}

	//ini MCG20121211	
	public void executeLoadManual(String fechaProceso){

		try {
			logger.info("INICIO LoadGarantiaBO JOB");	
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
			
			Parametro oparametro = parametroBO.findByNombreParametro(Constantes.PREFIJO_ARCHHIPOTECA);
			String prefijoHipoteca=oparametro.getValor();			
			Parametro oextension = parametroBO.findByNombreParametro(Constantes.EXT_ARCH_HIPOTECA);
			strExtension=oextension.getValor()==null?"TXT":oextension.getValor();			
			Parametro oformatoFecha = parametroBO.findByNombreParametro(Constantes.FORMAT_FECHA_HIPOTECA);
			String formatoFechafinal=oformatoFecha.getValor()==null?"yyMMdd":oformatoFecha.getValor();
			String activoFecha="AA";
			if (oformatoFecha.getValor()==null || (oformatoFecha.getValor()!=null && oformatoFecha.getValor().equals(""))){
				 activoFecha="NN";
			}
			String strFechaProcesoFormateado="";
			if (activoFecha.equals("AA")){
			 strFechaProcesoFormateado=getFechaFormateado(strFechaProceso,formatoFechafinal);
			}						
			String strNombreArchivo = prefijoHipoteca + strFechaProcesoFormateado + "." + strExtension;
			String strNombreCompletoArchivo = rutaCopy + File.separator + strNombreArchivo;
			boolean existsArchivo = (new File(strNombreCompletoArchivo)).exists();
			
			if (existsArchivo)
			{			
				loadHipotecaBO.executeLoadMassive(strFechaProceso,strNombreArchivo,Constantes.TIPOCARGA_MANUAL);
			}
			logger.info("FIN LoadGarantiaBO JOB");
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
	
	
	
	private String getFechaYYYYMMDD(int dias,String tipoFecha){	
		
		String strFechaYYYYMMDD = "";
		Calendar calendar = Calendar.getInstance();	
		if (tipoFecha=="1"){
			calendar.add(Calendar.DAY_OF_MONTH, dias);	
		}				
		String strDiaActual = "0" + (calendar.get(Calendar.DATE));
		String strMes = "0" + (calendar.get(Calendar.MONTH) + 1);
		String strAno = "" + calendar.get(Calendar.YEAR);		
		
		strFechaYYYYMMDD = strAno.substring(0, 4) + (strMes.substring(strMes.length() - 2))
					+ (strDiaActual.substring(strDiaActual.length() - 2));	
		
		return strFechaYYYYMMDD;
	}
	private int getdiasPermanencia(){
	
		int intMesPermanencia=90;
		try {
			Parametro ppermanencia= parametroBO.findByNombreParametro(Constantes.MES_PERMANENCIAFILE);
			intMesPermanencia=(Integer.valueOf(ppermanencia.getValor()==null?"3":ppermanencia.getValor()))*30;
			if (intMesPermanencia==0){
				intMesPermanencia=90;
			}
		} catch (Exception e) {
			intMesPermanencia=90;						
		}
		return intMesPermanencia;
		
	}
	
	private void deleteArchivosAntiguos(String rutaCopy,String prefijoPrestamo,String strExtension,String formatoFechafinal){
					
		try {
			//ini delete archivos antiguos
			File filedel=null;
		    String strFechaProcesodel = getFechaYYYYMMDD(-(getdiasPermanencia()),"1")	;	
		    strFechaProcesodel=getFechaFormateado(strFechaProcesodel,formatoFechafinal);
			
			String strNombreArchivodel = prefijoPrestamo + strFechaProcesodel + "." +strExtension;
			String strNombreCompletoArchivodel = rutaCopy + File.separator + strNombreArchivodel;
			logger.info("LoadHipotecaJob:deleteArchivosAntiguos:" +strNombreCompletoArchivodel);
			boolean existsArchivodel = (new File(strNombreCompletoArchivodel)).exists();
			
			if (existsArchivodel)
			{	
				String pathToFile=strNombreCompletoArchivodel;
				try {
					filedel = new File(pathToFile);
					filedel.delete();
				} catch (Exception ed) {
					logger.error("LoadHipotecaJob:deleteArchivosAntiguos:"+ ed.getMessage());
				}
				
			}					
			//fin delete archivos antigios
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}	

	}
	
	private String getFechaFormateado(String fechaProceso,String formatoFecha ){
		
		
	String fechaFormateada=fechaProceso;	
	SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyyMMdd");
	Date fechaPro;
	try {
		fechaPro = formatoDelTexto.parse(fechaProceso);
		String value=getEtiqueta(formatoFecha,fechaPro);
		fechaFormateada=value==null?fechaProceso:value;
	} catch (ParseException e) {
		fechaFormateada=fechaProceso;
	}		
	return fechaFormateada;
		
	}
	private Locale getDefaultLocale(){
		return new Locale("es","PE");
	}
	private String getEtiqueta(String formato,Date fecha){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat(formato,getDefaultLocale());
			return sdf.format(fecha).toUpperCase();
		}catch (Exception e) {
			return null;
		}
	}
	
}
