package pe.com.bbva.iipf.mantenimiento.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.LogCargaDatosBO;
import pe.com.bbva.iipf.mantenimiento.model.LogCargaDatos;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlCantidadpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfEmpresa;
import pe.com.bbva.iipf.mantenimiento.model.ReporteControlpfestado;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.FechaUtil;


@Service("logCargaDatosBO" )
public class LogCargaDatosBOImpl extends GenericBOImpl<LogCargaDatos> implements LogCargaDatosBO {
	Logger logger = Logger.getLogger(this.getClass());
	
	private String SQL_MAX_ID_CARG = " SELECT idx FROM("+
                                        				" SELECT id_archivo, max(id_log) AS idx"+
                                        				" FROM PROFIN.TIIPF_LOG_DATALOAD T"+
                                        				" WHERE T.id_archivo IN(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,18,19,20,21)"+
                                        				" GROUP BY T.id_archivo"+
                                        			 ") ";
	@Override
	public List<LogCargaDatos> listarLogCargaDatos(HashMap<String,Object> params)throws BOException {
		
		List<LogCargaDatos> listaLogCargaDatos = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(new Long(0));
			listaLogCargaDatos = super.listNamedQuery("listaLogCargaDatos", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return listaLogCargaDatos;
		
	}
	@Override
	public List<LogCargaDatos> buscarLogCargaDatos(HashMap<String,Object> params) throws BOException{
		
		List<LogCargaDatos> logCargaDatos = new ArrayList<LogCargaDatos>();
				
		try {
			
			if(params==null){
				params= new HashMap();
				Map subQuery1= new HashMap();
				subQuery1.put(" IN ", SQL_MAX_ID_CARG);
				params.put("id_log", subQuery1);
			}
			
			logCargaDatos = (List<LogCargaDatos>) super.findObjects(LogCargaDatos.class,params);
			
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return logCargaDatos;
		
	}
	
	//ini MCG20131025
	@Override
	public List<ReporteControlpfestado> consultaControlpfEstado() throws BOException {

		List<ReporteControlpfestado> reporteControlpfestados = null;
		ReporteControlpfestado reporteControlpfestado = null;

		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT ID_PROGRAMA,NUMERO_SOLICITUD,T.DESCRIPCION,FECHA_CREACION, ");
			sb.append("COD_USUARIO_CREACION,FECHA_MODIFICACION,COD_USUARIO_MODIFICACION ");
			sb.append("FROM PROFIN.TIIPF_PROGRAMA P ");
			sb.append("LEFT JOIN (SELECT ID_TABLA,DESCRIPCION FROM PROFIN.TIIPF_TABLA_DE_TABLA T ");
			sb.append("WHERE ID_TABLA_DE_TABLA='8201') T ");
			sb.append("ON P.TT_ID_ESTADO_PROGRAMA=T.ID_TABLA ");
			sb.append("GROUP BY ID_PROGRAMA,NUMERO_SOLICITUD,T.DESCRIPCION,FECHA_CREACION, ");
			sb.append("COD_USUARIO_CREACION,FECHA_MODIFICACION,COD_USUARIO_MODIFICACION ");
			sb.append("ORDER BY T.DESCRIPCION,ID_PROGRAMA,NUMERO_SOLICITUD");
																	
	
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			reporteControlpfestados = new ArrayList<ReporteControlpfestado>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				reporteControlpfestado = new ReporteControlpfestado();
				
				reporteControlpfestado.setIdprograma(amount[0].toString());
				if(amount[1] != null)
					reporteControlpfestado.setNumeroSolicitud(amount[1].toString());
				if(amount[2] != null)
					reporteControlpfestado.setEstado(amount[2].toString());
				if(amount[3] != null)
					reporteControlpfestado.setFechaCreacion(amount[3].toString());
				if(amount[4] != null)
					reporteControlpfestado.setCodusuarioCreacion(amount[4].toString());
				
				if(amount[5] != null){
					reporteControlpfestado.setFechaModificacion(amount[5]==null?"":amount[5].toString());
				}
				
				if(amount[6] != null){  
					reporteControlpfestado.setCodusuarioModificacion(amount[6]==null?"":amount[6].toString());
				}
								
											
				reporteControlpfestados.add(reporteControlpfestado);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return reporteControlpfestados;
	}
	@Override
	public List<ReporteControlpfEmpresa> consultaControlpfEmpresa() throws BOException {

		List<ReporteControlpfEmpresa> reporteControlpfEmpresas = null;
		ReporteControlpfEmpresa reporteControlpfEmpresa = null;

		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT COD_CENTRAL_EMP_PRINCIPAL,RUC_EMP_PRINCIPAL,");
					sb.append("EMPRESA_PRINCIPAL,  NUMERO_SOLICITUD,  TIPO, "); 
					sb.append("CODIGO_GRUPO,  NOMBRE_GRUPO,  ");
					sb.append("FECHA_CREACION,  FECHA_MODIFICACION,  ESTADO,");
					sb.append("USUARIO_CREACION,");
					sb.append("USUARIO_MODIFICACION, ");
					sb.append("USUARIO_CIERRE,");
					sb.append("FECHA_CIERRE,");
					sb.append("MOTIVO_CIERRE,");
					sb.append("RDC_GESTOR,");
					sb.append("RDC_OFICINA,");
					sb.append("PRG_PROXIMA_REVI_RIES, ");					
					sb.append("RGO_PROP_BBVA_BC,");
					sb.append("PROP_RIESGO, ");
					sb.append("RVGL_PRINCIPAL,");
					sb.append("RGVL_OTRAS_EMPRESA");
					sb.append(" FROM (SELECT P.NUMERO_SOLICITUD,");
					sb.append("DECODE(P.TT_ID_TIPO_EMPRESA,2,'EMPRESA',3,'GRUPO','OTROS') TIPO,");
					sb.append("P.ID_GRUPO CODIGO_GRUPO,");
					sb.append("DECODE(P.TT_ID_TIPO_EMPRESA,3,DB_NOMBRE_GRUP_EMPR,'') NOMBRE_GRUPO,");
					sb.append("P.ID_EMPRESA COD_CENTRAL_EMP_PRINCIPAL,");
					sb.append("P.DB_RUC RUC_EMP_PRINCIPAL ,");
					sb.append("E.NOMBRE EMPRESA_PRINCIPAL,");
					sb.append("P.FECHA_CREACION,");
					sb.append("P.FECHA_MODIFICACION,");
					sb.append("T.DESCRIPCION ESTADO,");
					sb.append("P.COD_USUARIO_CREACION USUARIO_CREACION,");
					sb.append("P.COD_USUARIO_MODIFICACION USUARIO_MODIFICACION, ");
					sb.append("P.COD_USUARIO_CIERRE USUARIO_CIERRE,");
					sb.append("P.FECHA_CIERRE,");
			        sb.append("M.DESCRIPCION MOTIVO_CIERRE,");
					sb.append("P.RDC_GESTOR,");
					sb.append("P.RDC_OFICINA,");
					sb.append("P.PRG_PROXIMA_REVI_RIES, ");
					sb.append("RP.VALOR RGO_PROP_BBVA_BC, ");
					sb.append("PR.VALOR PROP_RIESGO, ");	
					sb.append("P.RDC_NUMERO_RVGL RVGL_PRINCIPAL,");
					sb.append("(SELECT PROFIN.pfpkg.FN_ROWCONCAT ('SELECT EM.nombre||'': ''||RC.rdc_numero_rvgl FROM tiipf_reporte_credito RC,tiipf_empresa EM WHERE RC.codigo_empresa=EM.codigo AND RC.id_programa=EM.iipf_programa_id AND RC.id_programa= '||P.ID_PROGRAMA)  FROM DUAL ) RGVL_OTRAS_EMPRESA "); 
			        
					sb.append("FROM PROFIN.TIIPF_PROGRAMA P ");
					sb.append("LEFT JOIN PROFIN.TIIPF_EMPRESA E ");
					sb.append("ON P.ID_PROGRAMA=E.IIPF_PROGRAMA_ID ");
					sb.append("AND  P.ID_EMPRESA=E.CODIGO ");
					sb.append("LEFT JOIN PROFIN.TIIPF_TABLA_DE_TABLA T ");
					sb.append("ON T.ID_TABLA=P.TT_ID_ESTADO_PROGRAMA ");
					sb.append("LEFT JOIN PROFIN.TIIPF_TABLA_DE_TABLA M ");
					sb.append("ON M.ID_TABLA=P.TT_ID_MOTIVO_CIERRE ");
					
					sb.append("LEFT JOIN ");
					sb.append(" (SELECT IIPF_PROGRAMA_ID,DESCRIPCION,VALOR FROM (SELECT A.IIPF_PROGRAMA_ID,A.ID_ANEXO, C.DESCRIPCION,C.VALOR , ");
					sb.append(" ROW_NUMBER() OVER (PARTITION BY A.IIPF_PROGRAMA_ID ORDER BY A.ID_ANEXO) AS CORRELATIVO" );
					sb.append(" FROM PROFIN.TIIPF_ANEXO A, PROFIN.TIIPF_ANEXO_COLUMNAS C ");
					sb.append(" WHERE A.ID_ANEXO=C.ID_ANEXO");
					sb.append(" AND TIPO_FILA='6' AND DESCRIPCION ='PROP RIESGO' ");
					sb.append(" ) WHERE CORRELATIVO=1) PR");
					sb.append(" ON P.ID_PROGRAMA=PR.IIPF_PROGRAMA_ID ");

					sb.append(" LEFT JOIN ");
					sb.append(" (SELECT IIPF_PROGRAMA_ID,DESCRIPCION,VALOR FROM (SELECT A.IIPF_PROGRAMA_ID,A.ID_ANEXO, C.DESCRIPCION,C.VALOR , ");
					sb.append(" ROW_NUMBER() OVER (PARTITION BY A.IIPF_PROGRAMA_ID ORDER BY A.ID_ANEXO) AS CORRELATIVO ");
					sb.append(" FROM PROFIN.TIIPF_ANEXO A, PROFIN.TIIPF_ANEXO_COLUMNAS C ");
					sb.append(" WHERE A.ID_ANEXO=C.ID_ANEXO ");
					sb.append(" AND TIPO_FILA='6' AND DESCRIPCION ='RGO PROP BBVA BC' ");
					sb.append(" ) WHERE CORRELATIVO=1) RP ");
					sb.append(" ON P.ID_PROGRAMA=RP.IIPF_PROGRAMA_ID ");
										
					sb.append(") PF ");
					sb.append("ORDER BY PF.EMPRESA_PRINCIPAL,PF.NUMERO_SOLICITUD ");
																	
	
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			reporteControlpfEmpresas = new ArrayList<ReporteControlpfEmpresa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				reporteControlpfEmpresa = new ReporteControlpfEmpresa();
				
				if(amount[0] != null)
					reporteControlpfEmpresa.setCodcentralEmpprincipal(amount[0].toString());				
				if(amount[1] != null)
					reporteControlpfEmpresa.setRucEmpprincipal(amount[1].toString());
				if(amount[2] != null)
					reporteControlpfEmpresa.setEmpresaprincipal(amount[2].toString());
				if(amount[3] != null)
					reporteControlpfEmpresa.setNumeroSolicitud(amount[3].toString());
				if(amount[4] != null)
					reporteControlpfEmpresa.setTipo(amount[4].toString());				
				if(amount[5] != null){
					reporteControlpfEmpresa.setCodigoGrupo(amount[5]==null?"":amount[5].toString());
				}				
				if(amount[6] != null){  
					reporteControlpfEmpresa.setNombreGrupo(amount[6]==null?"":amount[6].toString());
				}				
				if(amount[7] != null){  
					reporteControlpfEmpresa.setFechaCreacion(amount[7]==null?"":amount[7].toString());
				}
				if(amount[8] != null){  
					reporteControlpfEmpresa.setFechaModificacion(amount[8]==null?"":amount[8].toString());
				}
				if(amount[9] != null){  
					reporteControlpfEmpresa.setEstado(amount[9]==null?"":amount[9].toString());
				}
				if(amount[10] != null){  
					reporteControlpfEmpresa.setUsuarioCreacion(amount[10]==null?"":amount[10].toString());
				}
				if(amount[11] != null){  
					reporteControlpfEmpresa.setUsuarioModificacion(amount[11]==null?"":amount[11].toString());
				}	
				
				//
				if(amount[12] != null){  
					reporteControlpfEmpresa.setUsuarioCierre(amount[12]==null?"":amount[12].toString());
				}	
				if(amount[13] != null){  
					reporteControlpfEmpresa.setFechaCierre(amount[13]==null?"":amount[13].toString());
				}	
				if(amount[14] != null){  
					reporteControlpfEmpresa.setMotivoCierre(amount[14]==null?"":amount[14].toString());
				}	
				if(amount[15] != null){  
					reporteControlpfEmpresa.setGestor(amount[15]==null?"":amount[15].toString());
				}	
				if(amount[16] != null){  
					reporteControlpfEmpresa.setOficina(amount[16]==null?"":amount[16].toString());
				}	
				if(amount[17] != null){  
					reporteControlpfEmpresa.setProximaRevision(amount[17]==null?"":amount[17].toString());
				}	
				if(amount[18] != null){  
					reporteControlpfEmpresa.setRgopropbbvabc(amount[18]==null?"":amount[18].toString());
				}				
				if(amount[19] != null){  
					reporteControlpfEmpresa.setPropRiesgo(amount[19]==null?"":amount[19].toString());
				}	
				
				if(amount[20] != null){  
					reporteControlpfEmpresa.setRvglEPrincipal(amount[20]==null?"":amount[20].toString());
				}				
				if(amount[21] != null){  
					reporteControlpfEmpresa.setRvglESecundaria(amount[21]==null?"":amount[21].toString());
				}	
				
				
															
				reporteControlpfEmpresas.add(reporteControlpfEmpresa);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return reporteControlpfEmpresas;
	}
	@Override
	public List<ReporteControlCantidadpfEmpresa> consultaControlpfCantidadEmpresa() throws BOException {

		List<ReporteControlCantidadpfEmpresa> reporteControlCantidadpfEmpresas = null;
		ReporteControlCantidadpfEmpresa reporteControlCantidadpfEmpresa = null;

		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			
				sb.append("SELECT EMPRESA_PRINCIPAL,");
					sb.append("RUC_EMP_PRINCIPAL RUC_EMPPRINCIPAL,");
					sb.append("COD_CENTRAL_EMP_PRINCIPAL CODCENTRAL_EMPPRINCIPAL,");
					sb.append("COUNT(NUMERO_SOLICITUD) CANT_PROGRAMA ");
					sb.append(" FROM (SELECT P.NUMERO_SOLICITUD, ");
					sb.append("DECODE(P.TT_ID_TIPO_EMPRESA,2,'EMPRESA',3,'GRUPO','OTROS') TIPO,");
					sb.append("P.ID_GRUPO CODIGO_GRUPO,");
					sb.append("DECODE(P.TT_ID_TIPO_EMPRESA,3,DB_NOMBRE_GRUP_EMPR,'') NOMBRE_GRUPO,");
					sb.append("P.ID_EMPRESA COD_CENTRAL_EMP_PRINCIPAL,");
					sb.append("P.DB_RUC RUC_EMP_PRINCIPAL ,");
					sb.append("E.NOMBRE EMPRESA_PRINCIPAL,");
					sb.append("P.FECHA_CREACION,");
					sb.append("P.FECHA_MODIFICACION,");
					sb.append("T.DESCRIPCION ESTADO,");
					sb.append("P.COD_USUARIO_CREACION USUARIO_CREACION,");
					sb.append("P.COD_USUARIO_MODIFICACION USUARIO_MODIFICACION ");
					sb.append("FROM PROFIN.TIIPF_PROGRAMA P ");
					sb.append("LEFT JOIN PROFIN.TIIPF_EMPRESA E ");
					sb.append("ON P.ID_PROGRAMA=E.IIPF_PROGRAMA_ID ");
					sb.append("AND  P.ID_EMPRESA=E.CODIGO ");
					sb.append("LEFT JOIN PROFIN.TIIPF_TABLA_DE_TABLA T ");
					sb.append("ON T.ID_TABLA=P.TT_ID_ESTADO_PROGRAMA ) PF ");
					sb.append("GROUP BY PF.EMPRESA_PRINCIPAL ,RUC_EMP_PRINCIPAL,COD_CENTRAL_EMP_PRINCIPAL ");
					sb.append("ORDER BY PF.EMPRESA_PRINCIPAL");
																	
	
			logger.info("find programas = "+sb.toString());
			List insurance = super.executeSQL(sb.toString());
			
			reporteControlCantidadpfEmpresas = new ArrayList<ReporteControlCantidadpfEmpresa>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				reporteControlCantidadpfEmpresa = new ReporteControlCantidadpfEmpresa();
				
				if(amount[0] != null)
					reporteControlCantidadpfEmpresa.setEmpresaPrincipal(amount[0].toString());				
				if(amount[1] != null)
					reporteControlCantidadpfEmpresa.setRucEmpprincipal(amount[1].toString());
				if(amount[2] != null)
					reporteControlCantidadpfEmpresa.setCodcentralEmpprincipal(amount[2].toString());
				if(amount[3] != null)
					reporteControlCantidadpfEmpresa.setCantPrograma(amount[3].toString());
								
												
											
				reporteControlCantidadpfEmpresas.add(reporteControlCantidadpfEmpresa);
			}
				
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
					
		return reporteControlCantidadpfEmpresas;
	}
	//fin MCG20131025
	@Override
	public String llenarPlantillaReportControlpfExcel(String dirArchivo, String nombreArchivo,
			String dirTemporal) throws BOException {
		logger.info("INCIO llenarPlantillaExcel");
		
		FileInputStream fileIn = null;
		String nombreArchivoTemporal = "";
		nombreArchivoTemporal = dirTemporal + File.separator + "temp_plantillaReportcpf"
				+ FechaUtil.formatFecha(new Date(), FechaUtil.YYYYMMDD_HHMMSS)
				+ ".xls";
		

		List<ReporteControlpfestado> listaReporteControlpfestado= consultaControlpfEstado();
		List<ReporteControlCantidadpfEmpresa> listaReporteControlCantidadpfEmpresa= consultaControlpfCantidadEmpresa();
		
		List<ReporteControlpfEmpresa> listaReporteControlpfEmpresa=consultaControlpfEmpresa();
		
	
		if (listaReporteControlpfestado == null || listaReporteControlpfestado != null
				&& listaReporteControlpfestado.size() <= 0) {
			throw new BOException(
					"No existe información "
							);
		}
		// fin MCG20130327


		try {
			// String nombreEmpresa = programa.getNombreGrupoEmpresa();
			// //NOMB_EMPRESA VARCHAR2(60 BYTE)
			// CORREGIR EL CODIGO CENTRAL EN EL EXCEL
			logger.info("plantilla=" + dirArchivo + File.separator
					+ nombreArchivo);
			
			fileIn = new FileInputStream(dirArchivo + File.separator
					+ nombreArchivo);
			HSSFWorkbook workbook = new HSSFWorkbook(fileIn);

			// //////EPOMAYAY 16022012
//			HSSFSheet wsIngresoDatos = workbook.getSheetAt(6);
//			HSSFRow rowID = wsIngresoDatos.getRow(6);
//			if (rowID != null) {
//				HSSFCell cellID = rowID.getCell(2);
//				if (cellID != null) {
//					cellID.setCellValue(nombreEmpresa);
//				}
//			}
			// //////EPOMAYAY 16022012

			HSSFSheet worksheet = workbook.getSheetAt(0);
			
			
			int contCerrado=0;
			int contPendiente=0;
			for (ReporteControlpfestado fila : listaReporteControlpfestado) {			
				String strEstado = fila.getEstado() == null ? "" : fila.getEstado().toString();
				if (strEstado.equals("CERRADO"))   {
					contCerrado=contCerrado+1;
				}else if(strEstado.equals("PENDIENTE")){
					contPendiente=contPendiente+1;
				}		
			}
			
			HSSFRow rowc = worksheet.getRow(2);
			HSSFRow rowp = worksheet.getRow(3);
			if(rowc==null){
				rowc	= worksheet.createRow(2);						
			}
			if (rowc != null) {
				HSSFCell cell = rowc.getCell(2);
				if (cell == null) {
					cell = rowc.createCell(2);
				}
				cell.setCellValue(contCerrado);
			}
						
			if(rowp==null){
				rowp	= worksheet.createRow(3);						
			}
			if (rowp != null) {
				HSSFCell cell = rowp.getCell(2);
				if (cell == null) {
					cell = rowp.createCell(2);
				}
				cell.setCellValue(contPendiente);
			}			
			int poscuentas = 7;
			for (ReporteControlpfestado fila : listaReporteControlpfestado) {	
				HSSFRow row = worksheet.getRow(poscuentas);				
				if(row==null){
					row	= worksheet.createRow(poscuentas);						
				}	
						
				for (int i = 1; i < 8; i++) {
					String strValor = "";
					if (i == 1) {
						strValor = fila.getIdprograma() == null ? "" : fila
								.getIdprograma().toString();
					} else if (i == 2) {
						strValor = fila.getNumeroSolicitud() == null ? ""
								: fila.getNumeroSolicitud().toString();
					} else if (i == 3) {
						strValor = fila.getEstado() == null ? "" : fila
								.getEstado().toString();
					}else if (i == 4) {
						strValor = fila.getFechaCreacion() == null ? "" : fila
								.getFechaCreacion().toString();
					}else if (i == 5) {
						strValor = fila.getCodusuarioCreacion() == null ? "" : fila
								.getCodusuarioCreacion().toString();
					}else if (i == 6) {
						strValor = fila.getFechaModificacion() == null ? "" : fila
								.getFechaModificacion().toString();
					}else if (i == 7) {
						strValor = fila.getCodusuarioModificacion() == null ? "" : fila
								.getCodusuarioModificacion().toString();
					}
					
					if (row != null) {
						HSSFCell cell = row.getCell(i - 1);
						if (cell == null) {
							cell = row.createCell(i - 1);
						}
						cell.setCellValue(strValor);
					}
				}
				poscuentas++;
			}
			
			//Para cantidad programa por cliente
			HSSFSheet worksheet2 = workbook.getSheetAt(1);
			int poscuentas2 = 4;

			for (ReporteControlCantidadpfEmpresa fila : listaReporteControlCantidadpfEmpresa) {	
				HSSFRow row = worksheet2.getRow(poscuentas2);				
				if(row==null){
					row	= worksheet2.createRow(poscuentas2);						
				}	
				for (int i = 1; i < 5; i++) {
					String strValor = "";
					if (i == 1) {
						strValor = fila.getEmpresaPrincipal() == null ? "" : fila
								.getEmpresaPrincipal().toString();
					} else if (i == 2) {
						strValor = fila.getRucEmpprincipal() == null ? ""
								: fila.getRucEmpprincipal().toString();
					} else if (i == 3) {
						strValor = fila.getCodcentralEmpprincipal() == null ? "" : fila
								.getCodcentralEmpprincipal().toString();
					}else if (i == 4) {
						strValor = fila.getCantPrograma() == null ? "" : fila
								.getCantPrograma().toString();
					}
					
					if (row != null) {
						HSSFCell cell = row.getCell(i - 1);
						if (cell == null) {
							cell = row.createCell(i - 1);
						}
						cell.setCellValue(strValor);
					}
				}
				poscuentas2++;
			}
			
			
			//para detalle Programa por cliente
			
			HSSFSheet worksheet3 = workbook.getSheetAt(2);
			int poscuentas3 = 4;
			// completa codigo cuentaa, descripcion y primer monto
			// for(Object fila : listaRating){
			for (ReporteControlpfEmpresa fila : listaReporteControlpfEmpresa) {	
				HSSFRow row = worksheet3.getRow(poscuentas3);				
				if(row==null){
					row	= worksheet3.createRow(poscuentas3);						
				}	
				for (int i = 1; i < 23; i++) {
					String strValor = "";
					if (i == 1) {
						strValor = fila.getCodcentralEmpprincipal() == null ? "" : fila
								.getCodcentralEmpprincipal().toString();
					} else if (i == 2) {
						strValor = fila.getRucEmpprincipal() == null ? ""
								: fila.getRucEmpprincipal().toString();
					} else if (i == 3) {
						strValor = fila.getEmpresaprincipal() == null ? "" : fila
								.getEmpresaprincipal().toString();
					}else if (i == 4) {
						strValor = fila.getNumeroSolicitud() == null ? "" : fila
								.getNumeroSolicitud().toString();
					}else if (i == 5) {
						strValor = fila.getTipo() == null ? "" : fila
								.getTipo().toString();
					}else if (i == 6) {
						strValor = fila.getCodigoGrupo() == null ? "" : fila
								.getCodigoGrupo().toString();
					}else if (i == 7) {
						strValor = fila.getNombreGrupo() == null ? "" : fila
								.getNombreGrupo().toString();
					}else if (i == 8) {
						strValor = fila.getFechaCreacion() == null ? "" : fila
								.getFechaCreacion().toString();
					}else if (i == 9) {
						strValor = fila.getFechaModificacion() == null ? "" : fila
								.getFechaModificacion().toString();
					}else if (i == 10) {
						strValor = fila.getEstado() == null ? "" : fila
								.getEstado().toString();
					}else if (i == 11) {
						strValor = fila.getUsuarioCreacion() == null ? "" : fila
								.getUsuarioCreacion().toString();
					}else if (i == 12) {
						strValor = fila.getUsuarioModificacion() == null ? "" : fila
								.getUsuarioModificacion().toString();
					}else if (i == 13) {
						strValor = fila.getUsuarioCierre() == null ? "" : fila
								.getUsuarioCierre().toString();
					}else if (i == 14) {
						strValor = fila.getFechaCierre() == null ? "" : fila
								.getFechaCierre().toString();
					}else if (i == 15) {
						strValor = fila.getMotivoCierre() == null ? "" : fila
								.getMotivoCierre().toString();
					}else if (i == 16) {
						strValor = fila.getGestor() == null ? "" : fila
								.getGestor().toString();
					}else if (i == 17) {
						strValor = fila.getOficina() == null ? "" : fila
								.getOficina().toString();
					}else if (i == 18) {
						strValor = fila.getProximaRevision() == null ? "" : fila
								.getProximaRevision().toString();
					}else if (i == 19) {
						strValor = fila.getPropRiesgo() == null ? "" : fila
								.getPropRiesgo().toString();
					}else if (i == 20) {
						strValor = fila.getRgopropbbvabc() == null ? "" : fila
								.getRgopropbbvabc().toString();
					}else if (i == 21) {
						strValor = fila.getRvglEPrincipal() == null ? "" : fila
								.getRvglEPrincipal().toString();
					}else if (i == 22) {
						strValor = fila.getRvglESecundaria() == null ? "" : fila
								.getRvglESecundaria().toString();
					}
					
					
					if (row != null) {
						HSSFCell cell = row.getCell(i - 1);
						if (cell == null) {
							cell = row.createCell(i - 1);
						}
						cell.setCellValue(strValor);
					}
				}
				poscuentas3++;
			}
			
			HSSFSheet worksheetestado = workbook.getSheetAt(0);
			worksheetestado.setForceFormulaRecalculation(true);

			// escribiendo la informacion sobre un archivo temporal

			FileOutputStream out = new FileOutputStream(nombreArchivoTemporal);
			workbook.write(out);
			out.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			throw new BOException(e);
		} catch (IOException e) {
			throw new BOException(e);
		}
		logger.info("FIN llenarPlantillaExcel");
		return nombreArchivoTemporal;
	}
//fin MCG20130327

}
