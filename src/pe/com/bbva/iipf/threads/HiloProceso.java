package pe.com.bbva.iipf.threads;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import pe.com.bbva.iipf.pf.bo.DownloadPDFBO;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.util.StringUtil;

public class HiloProceso implements Runnable {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private DownloadPDFBO downloadPDFBO;
	
	HashMap<String, String> parametros = new HashMap<String, String>();
	private int proceso;
	
	public synchronized void run() { 
		logger.info("INICIO run()  HILOPROCESO: "+this.hashCode());
		try {
			
			switch (proceso) {
			
				case Constantes.DESCARGA_PDF_HILO:
					downloadPDFBO.dowloadPDFGeneral(parametros);
					break;
				default:
					break;
			}
			
			
		} catch (Exception e) {

			logger.error(StringUtil.getStackTrace(e));
		}

		
		logger.info("FIN run()  HILOPROCESO this.hashCode():"+this.hashCode());
	}
	
	public HashMap<String, String> getParametros() {
		return parametros;
	}


	public void setParametros(HashMap<String, String> parametros) {
		this.parametros = parametros;
	}

	public int getProceso() {
		return proceso;
	}

	public void setProceso(int proceso) {
		this.proceso = proceso;
	}
	
	
	
}
