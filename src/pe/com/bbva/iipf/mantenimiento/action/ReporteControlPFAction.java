package pe.com.bbva.iipf.mantenimiento.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.LogCargaDatosBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.LogCargaDatos;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlCantidadpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfestado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.StringUtil;

@Service("reporteControlPFAction")
@Scope("prototype")
public class ReporteControlPFAction extends GenericAction  {
Logger logger = Logger.getLogger(this.getClass());
	
	private String tipoReporte;
	private String contentType;
	private String contentDisposition;
	private InputStream fileInputStream;
		
	@Resource
	private LogCargaDatosBO logCargaDatosBO ;

	private List<ReporteControlpfestado> listaReporteControlpfestado;
	private List<ReporteControlpfEmpresa> listaReporteControlpfEmpresa;
	private List<ReporteControlCantidadpfEmpresa> listaReporteControlCantidadpfEmpresa;
	
	public String iniciar() throws BOException{
		
		try
		{
			listaReporteControlpfestado = new ArrayList<ReporteControlpfestado>();			
			listaReporteControlpfEmpresa = new ArrayList<ReporteControlpfEmpresa>();
			listaReporteControlCantidadpfEmpresa = new ArrayList<ReporteControlCantidadpfEmpresa>();			
			super.setObjectSession("tipoReportecpf", "999");
						
		}catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
		
		return "reporteControlpfini";
	}
	public String listarReporteControlpf() throws BOException{
		
		try
		{
			logger.info("listarReporteControlpf Ini");
			String vtipoArchivo=getTipoReporte()==null?"999":getTipoReporte().toString();
		
			if (vtipoArchivo.equals("1") ){
					listaReporteControlpfestado = logCargaDatosBO.consultaControlpfEstado();
			}else if (vtipoArchivo.equals("2")){
					listaReporteControlpfEmpresa = logCargaDatosBO.consultaControlpfEmpresa();
			}else if (vtipoArchivo.equals("3")){
					listaReporteControlCantidadpfEmpresa = logCargaDatosBO.consultaControlpfCantidadEmpresa();	
			}
			
			super.setObjectSession("tipoReportecpf", vtipoArchivo);
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}		
		logger.info("listarReporteControlpf Fin");
		
		return "reporteControlpf";
	}
	
	public String paginadoReporteControlpf() throws BOException{
		
		try
		{
			logger.info("ini paginadoReporteControlpf");				
				listaReporteControlpfestado = logCargaDatosBO.consultaControlpfEstado();															
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}		
		logger.info("fin paginadoReporteControlpf");
		
		return "reporteControlpf";
	}
	
	public String paginadoReporteControlpfEmpresa() throws BOException{
		
		try
		{
			logger.info("ini paginadoReporteControlpfEmpresa");				
			listaReporteControlpfEmpresa = logCargaDatosBO.consultaControlpfEmpresa();															
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}		
		logger.info("fin paginadoReporteControlpfEmpresa");
		
		return "reporteControlpf";
	}
	
	public String paginadoReporteControlpfCantEmpresa() throws BOException{
		
		try
		{
			logger.info("ini paginadoReporteControlpfCantEmpresa");				
			listaReporteControlCantidadpfEmpresa = logCargaDatosBO.consultaControlpfCantidadEmpresa();																
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}		
		logger.info("fin paginadoReporteControlpfCantEmpresa");
		
		return "reporteControlpf";
	}
	
	
	
	
	public String completarReporteControlpf() throws Exception { 	
		logger.info("INICIO download");
		//FileInputStream fis = null;
		String path = "";
		String dir = "";
		String fileName = ""; 
		String forward = SUCCESS;
		try {

				
			dir  = getObjectParamtrosSession(Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO).toString();
			String dirTemporal = getObjectParamtrosSession(Constantes.DIR_TEMPORAL).toString();
						
			String nombreArchivoExport = "";
			nombreArchivoExport = "ReporteControlPF";
			fileName =  nombreArchivoExport.trim() +
						FechaUtil.formatFecha(new Date(), 
						FechaUtil.YYYYMMDD_HHMMSS)+".xls";
			
			path = logCargaDatosBO.llenarPlantillaReportControlpfExcel(dir, 
														    Constantes.NOMBRE_PLANTILLA_EXCEL_REPORTECONTROLPF,
														    dirTemporal
														    );
			fileInputStream = new FileInputStream(path);
			setContentType("application/ms-excel");
		   	setContentDisposition("attachment;filename=\""+ fileName + "\"");
		}catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			//path = dir+File.separator+ Constantes.NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO;
			forward = "reporteControlpf";
		}catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			//path = dir+File.separator+ Constantes.NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO;
			forward="reporteControlpf";
		}

	   	//Eliminar los archivos.
		return forward;
}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public List<ReporteControlpfestado> getListaReporteControlpfestado() {
		return listaReporteControlpfestado;
	}

	public void setListaReporteControlpfestado(
			List<ReporteControlpfestado> listaReporteControlpfestado) {
		this.listaReporteControlpfestado = listaReporteControlpfestado;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentDisposition() {
		return contentDisposition;
	}
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public List<ReporteControlpfEmpresa> getListaReporteControlpfEmpresa() {
		return listaReporteControlpfEmpresa;
	}
	public void setListaReporteControlpfEmpresa(
			List<ReporteControlpfEmpresa> listaReporteControlpfEmpresa) {
		this.listaReporteControlpfEmpresa = listaReporteControlpfEmpresa;
	}
	public List<ReporteControlCantidadpfEmpresa> getListaReporteControlCantidadpfEmpresa() {
		return listaReporteControlCantidadpfEmpresa;
	}
	public void setListaReporteControlCantidadpfEmpresa(
			List<ReporteControlCantidadpfEmpresa> listaReporteControlCantidadpfEmpresa) {
		this.listaReporteControlCantidadpfEmpresa = listaReporteControlCantidadpfEmpresa;
	}
	
	

}
