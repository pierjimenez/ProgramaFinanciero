package pe.com.bbva.iipf.mantenimiento.action;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pe.com.bbva.iipf.pf.job.LoadCarteraJob;
import pe.com.bbva.iipf.pf.job.LoadCuentaGarantiaJob;
import pe.com.bbva.iipf.pf.job.LoadDepositoAplazoJob;
import pe.com.bbva.iipf.pf.job.LoadDetalleGarantiaJob;
import pe.com.bbva.iipf.pf.job.LoadFianzaSolidariaJob;
import pe.com.bbva.iipf.pf.job.LoadFondosMutuosJob;
import pe.com.bbva.iipf.pf.job.LoadGarantiaJob;
import pe.com.bbva.iipf.pf.job.LoadHipotecaJob;
import pe.com.bbva.iipf.pf.job.LoadPrestamoJob;
import pe.com.bbva.iipf.pf.job.LoadRCCANUALJob;
import pe.com.bbva.iipf.pf.job.LoadRCCMesJob;
import pe.com.bbva.iipf.pf.job.LoadRCDJob;
import pe.com.bbva.iipf.pf.job.LoadRVTC001Job;
import pe.com.bbva.iipf.pf.job.LoadRVWCG010Job;
import pe.com.bbva.iipf.pf.job.LoadRatingJob;
import pe.com.bbva.iipf.pf.job.LoadRenbecAnualJob;
import pe.com.bbva.iipf.pf.job.LoadRenbecJob;
import pe.com.bbva.iipf.pf.job.LoadSUNATJob;
import pe.com.bbva.iipf.pf.job.LoadStandByJob;
import pe.com.bbva.iipf.pf.job.LoadTipoCambioJob;
import pe.com.bbva.iipf.pf.job.LoadWarrantJob;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.util.StringUtil;

@Service("eCargaDatosAction")
@Scope("singleton")
public class EjecutorCargaDatosAction extends GenericAction 
{
	private static final long serialVersionUID = 6542762823081368118L;

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private LoadCarteraJob 	loadCarteraJob;
	@Resource
	private LoadRatingJob 	loadRatingJob;
	@Resource
	private LoadRCCANUALJob loadRCCANUALJob;
	@Resource
	private LoadRCCMesJob 	loadRCCMesJob;
	@Resource
	private LoadRCDJob 	  	loadRCDJob;
	@Resource
	private LoadRenbecJob 	loadRenbecJob;
	@Resource
	private LoadRenbecAnualJob 	loadRenbecAnualJob;
	@Resource
	private LoadSUNATJob 	loadSUNATJob;
	
	
	@Resource
	private LoadRVWCG010Job loadRVWCG010Job;
	@Resource
	private LoadRVTC001Job 	loadRVTC001Job;
	
	@Resource
	private LoadTipoCambioJob 	loadTipoCambioJob;
	
	@Resource
	private LoadPrestamoJob 	loadPrestamoJob;
	
	@Resource
	private LoadGarantiaJob 	loadGarantiaJob;
	@Resource
	private LoadHipotecaJob 	loadHipotecaJob;
	@Resource
	private LoadWarrantJob 	loadWarrantJob;
	@Resource
	private LoadDepositoAplazoJob 	loadDepositoAplazoJob;
	@Resource
	private LoadCuentaGarantiaJob 	loadCuentaGarantiaJob;
	@Resource
	private LoadStandByJob 	loadStandByJob;
	@Resource
	private LoadFianzaSolidariaJob 	loadFianzaSolidariaJob;
	@Resource
	private LoadDetalleGarantiaJob 	loadDetalleGarantiaJob;
	
	@Resource
	private LoadFondosMutuosJob 	loadFondosMutuosJob;
	
	
    private boolean 		enEjecucionCartera	= false;
    private boolean 		enEjecucionRating	= false;
    private boolean 		enEjecucionRCCANUAL = false;
    private boolean 		enEjecucionRCCMes	= false;
    private boolean 		enEjecucionRCD		= false;
    private boolean 		enEjecucionRenbec	= false;
    private boolean 		enEjecucionSUNAT	= false;
    
    private boolean 		enEjecucionRVWCG010	= false;
    private boolean 		enEjecucionRVTC001	= false;
    private boolean 		enEjecucionTipoCambio	= false;
    private boolean 		enEjecucionRenbecAnual	= false;
    
    private boolean 		enEjecucionPrestamo	= false;
    
    private boolean        enEjecucionGarantia=false;    
    private boolean        enEjecucionHipoteca=false;
    private boolean        enEjecucionWarrant=false;
    private boolean        enEjecucionDepositoAplazo=false;    
    private boolean        enEjecucionCuentaGarantia=false;
    private boolean        enEjecucionStandBy=false;
    private boolean        enEjecucionFianzaSolidaria=false;
    private boolean        enEjecucionDetalleGarantia=false;
    
    private boolean enEjecucionFondosMutuos=false;
   
    
    private String 			method;
    private String fechaProceso;
	public String ingresar() throws Exception{
		
		try
		{
			Map params=new HashMap();							
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
		
		
		return "cargaManualDatos";
	}
		
	public void ejecutarCargaDatos()throws Exception{
		
		logger.info("ejecutarCargaDatos");
		logger.info("method="+method);
		logger.info("fechaproceso="+fechaProceso);
		int respuesta=-1;			
		
		try {
			
		    Integer metodo = Integer.parseInt(this.getMethod());	
		    			
           switch(metodo){
           
	           case 1: if(enEjecucionCartera==false){
	        	   			ejecutarCargaCartera();
	        	   				respuesta=1;
	        	   			}else{
	        	   				respuesta=0;
	        	   			}
	           			break;
	           case 2: if(enEjecucionRCCANUAL==false){
	        	   			ejecutarCargaRCCAN();
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	        	   			break;
	           case 3: if(enEjecucionRating==false){
	        	   			ejecutarCargaRATING();
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	          			break;
	           case 4: if(enEjecucionRCCMes==false){
	        	   			ejecutarCargaRCCMES();
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	          			break;
	           case 5: if(enEjecucionRCD==false){
	        	   			ejecutarCargaRCD();
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	      				break;
	           case 6: if(enEjecucionRenbec==false){//MODIFICACIÓN
	        	   			ejecutarCargaRENBEC(fechaProceso);
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	   	    			break;
	           case 7: if(enEjecucionSUNAT==false){
	        	   			ejecutarCargaSUNAT();
	        	   			respuesta=1;
	           			}else{
	           				respuesta=0;
	           			}
	          			break;	          			
	           case 8: if(enEjecucionRVWCG010==false){
			        	   	ejecutarCargaRVWCG010();
		   	   				respuesta=1;
		      			}else{
		      				respuesta=0;
		      			}
		     			break;     			
	           case 9: if(enEjecucionRVTC001==false){
	        	   			ejecutarCargaRVTC001();
	        	   			respuesta=1;
      					}else{
      						respuesta=0;
      					}
     					break;
	           case 10: if(enEjecucionTipoCambio==false){
	        	   			ejecutarCargaTipoCambio(fechaProceso);
	        	   			respuesta=1;
		      			}else{
		      				respuesta=0;
		      			}
		     			break;
     			case 11: if(enEjecucionRenbecAnual==false){
     						ejecutarCargaRENBECAnual(fechaProceso);
     						respuesta=1;
      					}else{
      						respuesta=0;
      					}
     					break;
     			case 12: if(enEjecucionPrestamo==false){
     						ejecutarCargaPrestamo(fechaProceso);
     						respuesta=1;
		       			}else{
		       				respuesta=0;
		       			}
		      			break;
     			case 13: if(enEjecucionGarantia==false){
	  	        	   		ejecutarCargaGarantia(fechaProceso);
	  	        	   		respuesta=1;
	        			}else{
	        				respuesta=0;
	        			}
	       				break;
     			case 14: if(enEjecucionHipoteca==false){
	        	   			ejecutarCargaHipoteca(fechaProceso);
	        	   			respuesta=1;
		    			}else{
		    				respuesta=0;
		    			}
		   				break;
     			case 15: if(enEjecucionWarrant==false){
	        	   			ejecutarCargaWarrant(fechaProceso);
	        	   			respuesta=1;
		    			}else{
		    				respuesta=0;
		    			}
		   				break;
     			case 16: if(enEjecucionDepositoAplazo==false){
	        	   			ejecutarCargaDepositoAplazo(fechaProceso);
	        	   			respuesta=1;
		    			}else{
		    				respuesta=0;
		    			}
		   				break;
     			case 17: if(enEjecucionCuentaGarantia==false){
	        	   			ejecutarCargaCuentaGarantia(fechaProceso);
	        	   			respuesta=1;
		    			}else{
		    				respuesta=0;
		    			}
		   				break;
     			case 18: if(enEjecucionStandBy==false){
	        	   			ejecutarCargaStandBy(fechaProceso);
	        	   			respuesta=1;
		    			}else{
		    				respuesta=0;
		    			}
		   				break;
     			case 19: if(enEjecucionFianzaSolidaria==false){
        	   			ejecutarCargaFianzaSolidaria(fechaProceso);
        	   					respuesta=1;
						}else{
							respuesta=0;
						}
						break;
     			case 20: if(enEjecucionDetalleGarantia==false){
        	   				ejecutarCargaDetalleGarantia(fechaProceso);
        	   				respuesta=1;
						}else{
							respuesta=0;
						}
						break;
						
     			case 21: if(enEjecucionFondosMutuos==false){
	   				ejecutarCargaFondosMutuos(fechaProceso);
	   				respuesta=1;
				}else{
					respuesta=0;
				}
				break;
     			
           }
           
			
           if(respuesta==0){
        	   getResponse().setContentType("text/html"); 
        	   this.getResponse().getWriter().print("<h4><font color='red'>Este proceso est&aacute; Ejecutandose...</font></h4>");
           }else if(respuesta==1){
        	   getResponse().setContentType("text/html"); 
        	   this.getResponse().getWriter().print("<h4><font color='blue'>Ejecuci&oacute;n Finalizada...</font></h4>");
           }

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	public void ejecutarCargaCartera(){
		
		logger.info("ejecutarCargaCartera INICIO  enEjecucionCartera: "+enEjecucionCartera);
		enEjecucionCartera=true;
		try{
			loadCarteraJob.executeLoadManual();
		}finally{
			enEjecucionCartera = false;
		}
		logger.info("ejecutarCargaCartera FIN  : ");
	}
	public void ejecutarCargaRCCAN(){
		logger.info("ejecutarCargaRCCAN INICIO  enEjecucionRCCANUAL: "+enEjecucionRCCANUAL);
		enEjecucionRCCANUAL=true;
		try{
			loadRCCANUALJob.executeLoadManual();
		}finally{
			enEjecucionRCCANUAL = false;
		}
		logger.info("ejecutarCargaRCCAN FIN  : ");
	}
	public void ejecutarCargaRATING(){
		logger.info("ejecutarCargaRATING INICIO  enEjecucionRating : "+enEjecucionRating);
		enEjecucionRating=true;
		try{
			loadRatingJob.executeLoadManual();
		}finally{
			enEjecucionRating = false;
		}
		logger.info("ejecutarCargaRATING FIN  : ");
	}
	public void ejecutarCargaRCCMES(){
		logger.info("ejecutarCargaRCCMES INICIO  enEjecucionRCCMes: "+enEjecucionRCCMes);
		enEjecucionRCCMes=true;
		try{
			loadRCCMesJob.executeLoadManual();
		}finally{
			enEjecucionRCCMes = false;
		}
		logger.info("ejecutarCargaRCCMES FIN  : ");
	}
	public void ejecutarCargaRCD(){
		logger.info("ejecutarCargaRCD INICIO  enEjecucionRCD: "+enEjecucionRCD);
		enEjecucionRCD	= true;
		try{
			loadRCDJob.executeLoadManual();
		}finally{
			enEjecucionRCD = false;
		}
		logger.info("ejecutarCargaRCD FIN  : ");
	}
	public void ejecutarCargaRENBEC(String fechaProceso){
		logger.info("ejecutarCargaRENBEC INICIO  enEjecucionRenbec: "+enEjecucionRenbec);
		enEjecucionRenbec = true;

		try{
			loadRenbecJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionRenbec = false;
		}
		logger.info("ejecutarCargaRCD FIN  : ");		
	}
	public void ejecutarCargaRENBECAnual(String fechaProceso){
		logger.info("ejecutarCargaRENBECAnual INICIO  enEjecucionRenbecAnual: "+enEjecucionRenbec);
		enEjecucionRenbecAnual = true;

		try{
			loadRenbecAnualJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionRenbecAnual = false;
		}
		logger.info("ejecutarCargaRENBECAnual FIN  : ");		
	}
	public void ejecutarCargaSUNAT(){
		logger.info("ejecutarCargaSUNAT INICIO  enEjecucionSUNAT: "+enEjecucionSUNAT);
		enEjecucionSUNAT = true;
		try{
			loadSUNATJob.executeLoadManual();
		}finally{
			enEjecucionSUNAT = false;
		}
		logger.info("ejecutarCargaSUNAT FIN  : ");
	}
	
	public void ejecutarCargaRVWCG010(){
		logger.info("ejecutarCargaRVWCG010 INICIO  enEjecucionRVWCG010: "+enEjecucionRVWCG010);
		enEjecucionRVWCG010 = true;
		try{
			loadRVWCG010Job.executeLoadManual();
		}finally{
			enEjecucionRVWCG010 = false;
		}
		logger.info("ejecutarCargaRVWCG010 FIN  : ");
	}
	
	public void ejecutarCargaRVTC001(){
		logger.info("ejecutarCargaRVTC001 INICIO  enEjecucionRVTC001: "+enEjecucionRVTC001);
		enEjecucionRVTC001 = true;
		try{
			loadRVTC001Job.executeLoadManual();
		}finally{
			enEjecucionRVTC001 = false;
		}
		logger.info("ejecutarCargaRVTC001 FIN  : ");
	}
	
	public void ejecutarCargaTipoCambio(String fechaProceso){
		logger.info("ejecutarCargaTipoCambio INICIO  enEjecucionRVTC001: "+enEjecucionRVTC001);
		enEjecucionTipoCambio = true;
		try{	
			loadTipoCambioJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionTipoCambio = false;
		}
		logger.info("ejecutarCargaTipoCambio FIN  : ");
	}
	
	public void ejecutarCargaPrestamo(String fechaProceso){
		logger.info("ejecutarCargaPrestamo INICIO  enEjecucionPrestamo: "+enEjecucionPrestamo);
		enEjecucionPrestamo = true;
		try{	
			loadPrestamoJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionPrestamo = false;
		}
		logger.info("ejecutarCargaPrestamo FIN  : ");
	}
	
	public void ejecutarCargaGarantia(String fechaProceso){
		logger.info("ejecutarCargaGarantia INICIO  enEjecucionGarantia: "+enEjecucionGarantia);
		enEjecucionGarantia = true;
		try{	
			loadGarantiaJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionGarantia = false;
		}
		logger.info("ejecutarCargaGarantia FIN  : ");
	}
	
	public void ejecutarCargaHipoteca(String fechaProceso){
		logger.info("ejecutarCargaHipoteca INICIO  enEjecucionHipoteca: "+enEjecucionHipoteca);
		enEjecucionHipoteca = true;
		try{	
			loadHipotecaJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionHipoteca = false;
		}
		logger.info("ejecutarCargaHipoteca FIN  : ");
	}

	
	public void ejecutarCargaWarrant(String fechaProceso){
		logger.info("ejecutarCargaWarrant INICIO  enEjecucionWarrant: "+enEjecucionWarrant);
		enEjecucionWarrant = true;
		try{	
			loadWarrantJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionWarrant = false;
		}
		logger.info("ejecutarCargaWarrant FIN  : ");
	}
	
	public void ejecutarCargaDepositoAplazo(String fechaProceso){
		logger.info("ejecutarCargaDepositoAplazo INICIO  enEjecucionDepositoAplazo: "+enEjecucionDepositoAplazo);
		enEjecucionDepositoAplazo = true;
		try{	
			loadDepositoAplazoJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionDepositoAplazo = false;
		}
		logger.info("ejecutarCargaDepositoAplazo FIN  : ");
	}
	
	public void ejecutarCargaCuentaGarantia(String fechaProceso){
		logger.info("ejecutarCargaCuentaGarantia INICIO  enEjecucionCuentaGarantia: "+enEjecucionCuentaGarantia);
		enEjecucionCuentaGarantia = true;
		try{	
			loadCuentaGarantiaJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionCuentaGarantia = false;
		}
		logger.info("ejecutarCargaCuentaGarantia FIN  : ");
	}
	
	public void ejecutarCargaStandBy(String fechaProceso){
		logger.info("ejecutarCargaStandBy INICIO  enEjecucionStandBy: "+enEjecucionStandBy);
		enEjecucionStandBy = true;
		try{	
			loadStandByJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionStandBy = false;
		}
		logger.info("ejecutarCargaStandBy FIN  : ");
	}
	
	public void ejecutarCargaFianzaSolidaria(String fechaProceso){
		logger.info("ejecutarCargaFianzaSolidaria INICIO  enEjecucionFianzaSolidaria: "+enEjecucionFianzaSolidaria);
		enEjecucionFianzaSolidaria = true;
		try{	
			loadFianzaSolidariaJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionFianzaSolidaria = false;
		}
		logger.info("ejecutarCargaFianzaSolidaria FIN  : ");
	}
	
	public void ejecutarCargaDetalleGarantia(String fechaProceso){
		logger.info("ejecutarCargaDetalleGarantia INICIO  enEjecucionDetalleGarantia: "+enEjecucionDetalleGarantia);
		enEjecucionDetalleGarantia = true;
		try{	
			loadDetalleGarantiaJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionDetalleGarantia = false;
		}
		logger.info("ejecutarCargaDetalleGarantia FIN  : ");
	}
	
	public void ejecutarCargaFondosMutuos(String fechaProceso){
		logger.info("ejecutarCargaFondosMutuos INICIO  enEjecucionFondosMutuos: "+enEjecucionFondosMutuos);
		enEjecucionFondosMutuos = true;
		try{	
			loadFondosMutuosJob.executeLoadManual(fechaProceso);
		}finally{
			enEjecucionFondosMutuos = false;
		}
		logger.info("ejecutarCargaFondosMutuos FIN  : ");
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public LoadCarteraJob getLoadCarteraJob() {
		return loadCarteraJob;
	}

	public void setLoadCarteraJob(LoadCarteraJob loadCarteraJob) {
		this.loadCarteraJob = loadCarteraJob;
	}

	public String getFechaProceso() {
		return fechaProceso;
	}


	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
	public LoadPrestamoJob getLoadPrestamoJob() {
		return loadPrestamoJob;
	}

	public void setLoadPrestamoJob(LoadPrestamoJob loadPrestamoJob) {
		this.loadPrestamoJob = loadPrestamoJob;
	}

	public boolean isEnEjecucionPrestamo() {
		return enEjecucionPrestamo;
	}

	public void setEnEjecucionPrestamo(boolean enEjecucionPrestamo) {
		this.enEjecucionPrestamo = enEjecucionPrestamo;
	}

	public LoadGarantiaJob getLoadGarantiaJob() {
		return loadGarantiaJob;
	}

	public void setLoadGarantiaJob(LoadGarantiaJob loadGarantiaJob) {
		this.loadGarantiaJob = loadGarantiaJob;
	}

	public boolean isEnEjecucionGarantia() {
		return enEjecucionGarantia;
	}

	public void setEnEjecucionGarantia(boolean enEjecucionGarantia) {
		this.enEjecucionGarantia = enEjecucionGarantia;
	}

	
	
}
