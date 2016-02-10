package pe.com.bbva.iipf.mantenimiento.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.LogCargaDatosBO;
import pe.com.bbva.iipf.mantenimiento.model.LogCargaDatos;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.StringUtil;

@Service("mCargaDatosAction")
@Scope("prototype")
public class MonitorCargaDatosAction extends GenericAction 
{
	private static final long serialVersionUID = 6542762823081368118L;

	Logger logger = Logger.getLogger(this.getClass());
	
	private String archivoElejido;
	private Date fechaBusqueda;
	
	@Resource
	private LogCargaDatosBO logCargaDatosBO ;

	private List<LogCargaDatos> listaLogCargaDatos;
	
	public String iniciar() throws BOException{
		
		try
		{
			listaLogCargaDatos = new ArrayList<LogCargaDatos>();
			listaLogCargaDatos = logCargaDatosBO.buscarLogCargaDatos(null);
			
		}catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
		
		return "monitorLogCargaDatosIni";
	}
	public String listarLogCargas() throws BOException{
		
		try
		{
			logger.info("DESARROLLO INICIO HFQV : "+getArchivoElejido());
			HashMap<String,Object> params=new HashMap<String,Object>();
			if(this.getArchivoElejido()!=null && !"".equals(this.getArchivoElejido())){
				params.put("idArchivo", new Long(getArchivoElejido().trim()));
			}
			if(this.getFechaBusqueda()!=null){
				params.put("fecha_creacion",this.getFechaBusqueda());
			}

			if(params.isEmpty()){
				listaLogCargaDatos = logCargaDatosBO.buscarLogCargaDatos(null);
			}else{
				listaLogCargaDatos = logCargaDatosBO.buscarLogCargaDatos(params);	
			}

			logger.info("DESARROLLO params: "+params);
			
			super.setObjectSession("listaLogCargaDatos", listaLogCargaDatos);
				
			logger.info("DESARROLLO FIN HFQV listaLogCargaDatos: "+listaLogCargaDatos);
											
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		
		logger.info("DESARROLLO FIN HFQV");
		
		return "monitorLogCargaDatosList";
	}


	public List<LogCargaDatos> getListaLogCargaDatos() {
		return listaLogCargaDatos;
	}


	public void setListaLogCargaDatos(List<LogCargaDatos> listaLogCargaDatos) {
		this.listaLogCargaDatos = listaLogCargaDatos;
	}


	public String getArchivoElejido() {
		return archivoElejido;
	}


	public void setArchivoElejido(String archivoElejido) {
		this.archivoElejido = archivoElejido;
	}


	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}


	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
		
	
}
