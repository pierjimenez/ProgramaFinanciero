package pe.com.bbva.iipf.mantenimiento.bo;

import java.util.HashMap;
import java.util.List;


import pe.com.bbva.iipf.mantenimiento.model.LogCargaDatos;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlCantidadpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfestado;
import pe.com.stefanini.core.exceptions.BOException;

public interface LogCargaDatosBO 
{
	public List<LogCargaDatos> listarLogCargaDatos(HashMap<String,Object> params) throws BOException;
	
	public List<LogCargaDatos> buscarLogCargaDatos(HashMap<String,Object> params) throws BOException;
	
	public List<ReporteControlpfestado> consultaControlpfEstado() throws BOException ;
	public List<ReporteControlpfEmpresa> consultaControlpfEmpresa() throws BOException ;	
	public List<ReporteControlCantidadpfEmpresa> consultaControlpfCantidadEmpresa() throws BOException;
	
	public String llenarPlantillaReportControlpfExcel(String dirArchivo, String nombreArchivo,
			String dirTemporal) throws BOException;
	

	

}
