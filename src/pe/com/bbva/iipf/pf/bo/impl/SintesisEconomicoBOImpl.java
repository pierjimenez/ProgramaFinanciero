package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.dao.RatingDAO;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.pf.model.TcuentaRating;
import pe.com.bbva.iipf.pf.model.TmesEjercicio;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FileUtil;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("sintesisEconomicoBO")
@Scope("prototype") 
public class SintesisEconomicoBOImpl extends GenericBOImpl<SintesisEconomica> implements
		SintesisEconomicoBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroBO parametroBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private RatingDAO ratingDAO;
	
	private String SQL_RATING = " select FECH_PERIODO_CALC,COD_CUENTA, "+
								" DESC_CUENTA,MONTO from PROFIN.TIIPF_ORDEN_CUENTAS OC "+
								" LEFT JOIN PROFIN.TIIPF_PFRATING PFR "+
								" ON RTRIM(LTRIM(PFR.COD_CUENTA)) = RTRIM(LTRIM(OC.CUENTA)) "+
								" where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' and  "+
							    " TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4))>= "+
							    " (select max(TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4)))-3 "+
							    " from PROFIN.TIIPF_PFRATING where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' ) "+ 
							    " order by TO_DATE(SUBSTR(fech_periodo_calc,4,7),'MM-YYYY') desc, ORDEN ASC ";
	
	private String SQL_MESES_EJERCICIO = " select distinct fech_periodo_calc , " +
										 " meses_ejercicios , inflacion," +
										 " calif_rating,punt_rating " +
										 " from PROFIN.TIIPF_PFRATING " +
										 " where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' and " +
										 " TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4))>=" +
									     " (select max(TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4)))-3" +
									     " from PROFIN.TIIPF_PFRATING where UPPER(TIP_ESTADO_FINAN) = 'I' AND cod_central_cli = '%s' ) " +
										 " order by TO_NUMBER(SUBSTR(fech_periodo_calc, 7,4)) desc";
	private File fileExcel;
	private boolean isFlagEditFile;
	private String nombreEmpresaGrupo;
	private Programa programa;
	
	@Override
	public void beforeSave(SintesisEconomica t) throws BOException{
		t.setPrograma(programa);
		t.setFechaCarga(new Date());
		t.setUsuario(getUsuarioSession().getNombre());
	}
	@Override
	public boolean validate(SintesisEconomica t) throws BOException{
		if(fileExcel == null){
			throw new BOException("Seleccione un archivo");
		}
		if(fileExcel.getName() == null){
			throw new BOException("Seleccione un archivo");
		}
		else if(fileExcel.getName() != null &&
				fileExcel.getName().equals("")){
			throw new BOException("Seleccione un archivo");
		}
		return true;
	}
	
	
	/**
	 * registrar archivos
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save() throws BOException,IOException {
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
			File uniqueFile = null;
			SintesisEconomica t = new SintesisEconomica ();
			String nombreArchivo = nombreEmpresaGrupo + FechaUtil.formatFecha(new Date(), 
																			  FechaUtil.YYYYMMDD_HHMMSS);
			t.setNombreArchivo(nombreArchivo+".xls");
			t.setNombreEmpresa(nombreEmpresaGrupo);
			t.setExtension("xls");
			super.save(t);
			if(!isFlagEditFile){
				String extencion="";
				if(fileExcel.getName().split("\\.").length>0){
					extencion = "xls";//fileExcel.getName().split("\\.")[(fileExcel.getName().split("\\.").length-1)];
				}
				
				uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()),
												 t.getId()+
												 "."+
												 extencion);
				
				byte[] serObj = FileUtil.readBytes(fileExcel);
				FileUtil.write(uniqueFile,serObj);
				programaBO.actualizarFechaModificacionPrograma(programa.getId());
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage()); 
		}catch (BOException e) {
			throw new BOException(e.getMessage()); 
		}catch(Exception e){
			throw new BOException(e.getMessage());
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void savecopiaSintesisEconomica(SintesisEconomica osintesisEconomica,Long IdSintesisini) throws BOException,IOException {
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
			File uniqueFile = null;
			File origenFile= null;			
			
			SintesisEconomica t = new SintesisEconomica ();
			String nombreArchivo = osintesisEconomica.getNombreEmpresa()==null?"": osintesisEconomica.getNombreEmpresa().toString().trim() + FechaUtil.formatFecha(new Date(), FechaUtil.YYYYMMDD_HHMMSS);
			t=osintesisEconomica;
			t.setNombreArchivo(nombreArchivo+".xls");
			t.setNombreEmpresa(osintesisEconomica.getNombreEmpresa()==null?"":osintesisEconomica.getNombreEmpresa().toString().trim());
			t.setExtension("xls");
			super.onlySave(t);			
			String extencion="xls";	
			
			String rutaCopy=parametro.getValor();
			String strNameCompletoFileCopy = rutaCopy + File.separator + IdSintesisini.toString()+"."+ extencion;
			
			origenFile = new File(strNameCompletoFileCopy);		        
	        if (origenFile.exists()) {
	        	uniqueFile = FileUtil.uniqueFile(new File(parametro.getValor()), t.getId()+ "."+ extencion);			
				CopiaFile(origenFile,uniqueFile);				
				
	        }
			
		} catch (IOException e) {
			throw new IOException(e.getMessage()); 
		}catch (BOException e) {
			throw new BOException(e.getMessage()); 
		}catch(Exception e){
			throw new BOException(e.getMessage());
		}
	}

	
	public boolean CopiaFile(File src, File dst) throws IOException {
		logger.info("Obteniendo archivo ... ");
		InputStream in = null;
		OutputStream out = null;
		File fSrc = src;
		File fDst;
	try {	
		fDst = dst;
		if (!fDst.exists()) {
			fDst.createNewFile();
		}
		
		in = new FileInputStream(fSrc);
		out = new FileOutputStream(fDst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		logger.info("fin copiado ... ");

		
	} catch (IOException e) {
		e.printStackTrace();
		logger.error("Error entrada/salida. " + e.getMessage(), e);
		return false;
	} finally {
		try {
			in.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error cerrar file. " + ex.getMessage(), ex);
		}
	}
	return true;
}
	@Override
	public List<SintesisEconomica> listarSintesisEconomico() throws BOException {
		List<Long> parametros = new ArrayList<Long>();
		parametros.add(programa.getId());
		List<SintesisEconomica> lista = super.executeListNamedQuery("listarSintesisEconomico", parametros);
		return lista;
	}
	@Override
	public List<SintesisEconomica> listarSintesisEconomico(Programa oprograma) throws BOException {
		List<Long> parametros = new ArrayList<Long>();
		parametros.add(oprograma.getId());
		List<SintesisEconomica> lista = super.executeListNamedQuery("listarSintesisEconomico", parametros);
		return lista;
	}
	@Override
	public List<SintesisEconomica> listarSintesisEconomico(Programa oprograma,String nombreEmpresa) throws BOException {
		List<Object> parametros = new ArrayList<Object>();
		parametros.add(oprograma.getId());
		parametros.add(nombreEmpresa);	
		List<SintesisEconomica> lista = super.executeListNamedQuery("listarSintesisEconomicoByEmpresa", parametros);
		return lista;
	}
	
	/**
	 * completa el archivo plantilla.xls 
	 * con los datos del archivo PFRATING
	 * @return direccion del archivo completado
	 * @throws BOException
	 */
	
	public String llenarPlantillaExcel_orig(String dirArchivo, 
								       String nombreArchivo,
								       String dirTemporal,
								       String codigoCentralEmpresa,
								       String nombreEmpresa)throws BOException{
		logger.info("INCIO llenarPlantillaExcel");
		logger.info("codigoCentralEmpresa="+codigoCentralEmpresa);
		FileInputStream fileIn = null;
		String nombreArchivoTemporal = "";
		nombreArchivoTemporal = dirTemporal+
								File.separator+
								"temp_plantilla"+
								FechaUtil.formatFecha(new Date(), 
								FechaUtil.YYYYMMDD_HHMMSS)+
								".xls";
		Calendar calendar = Calendar.getInstance();
		//int anioInicial = calendar.get(Calendar.YEAR)-3;
		SQL_RATING = String.format(SQL_RATING, 
								   codigoCentralEmpresa,
								   codigoCentralEmpresa);
		List listaRating = executeSQL(SQL_RATING);

		if(listaRating.isEmpty()){
			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
		}
		SQL_MESES_EJERCICIO = String.format(SQL_MESES_EJERCICIO, 
											codigoCentralEmpresa,
											codigoCentralEmpresa);
		List listaMesesEjercicio = executeSQL(SQL_MESES_EJERCICIO);
		
		if(listaMesesEjercicio.isEmpty()){
			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
		}
		try {
			//String nombreEmpresa = programa.getNombreGrupoEmpresa(); //NOMB_EMPRESA	VARCHAR2(60 BYTE)
			//CORREGIR EL CODIGO CENTRAL EN EL EXCEL
			logger.info("plantilla="+dirArchivo+File.separator+nombreArchivo);
			String codigoCentralCliente = codigoCentralEmpresa;//programa.getIdEmpresa().toString(); //COD_CENTRAL_CLI	VARCHAR2(8 BYTE)
			fileIn = new FileInputStream(dirArchivo+File.separator+nombreArchivo);
			HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
			
			////////EPOMAYAY 16022012
			HSSFSheet wsIngresoDatos = workbook.getSheetAt(6);
			HSSFRow rowID = wsIngresoDatos.getRow(6);
			if(rowID != null){
				HSSFCell cellID = rowID.getCell(2);
				if(cellID != null){
					cellID.setCellValue(nombreEmpresa);
				}
			}
			////////EPOMAYAY 16022012
			
			HSSFSheet worksheet = workbook.getSheetAt(5);
			
			HSSFRow rownombreEmpresa = worksheet.getRow(1);
			HSSFCell cellC2 = rownombreEmpresa.getCell(2);
			if(cellC2 == null){
				cellC2 = rownombreEmpresa.createCell(2);
			}
			cellC2.setCellValue(nombreEmpresa.trim());
			
			HSSFRow rownrocliente = worksheet.getRow(2);
			HSSFCell cellC3 = rownrocliente.getCell(2);
			if(cellC3==null){
				cellC3 = rownrocliente.createCell(2);
			}
			cellC3.setCellValue(codigoCentralCliente);
			
			//llenando fecha y meses de ejercicio, inflacion,
			//puntuacion,califrating
			HSSFRow rowfechaperiodo = worksheet.getRow(4);
			HSSFRow rowmesesejercico = worksheet.getRow(5);
			HSSFRow rowinflacion = worksheet.getRow(6);
			HSSFRow rowpuntuacion = worksheet.getRow(7);
			HSSFRow rowcalifrating = worksheet.getRow(8);
			int pos = 14;//2;//posisicion fecha periodo
			for(Object obj : listaMesesEjercicio){
				Object[] datos = (Object[])obj;
				HSSFCell cell1 = rowfechaperiodo.getCell(pos);
				if(cell1 == null){
					cell1 = rowfechaperiodo.createCell(pos);	
				}
				cell1.setCellValue(datos[0]==null?"":datos[0].toString());
				
				HSSFCell cell2 = rowmesesejercico.getCell(pos);
				if(cell2 ==null){
					cell2 = rowmesesejercico.createCell(pos);
				}
				cell2.setCellValue(datos[1]==null?"":datos[1].toString());
				
				HSSFCell cell3 = rowinflacion.getCell(pos);
				if(cell3==null){
					cell3 = rowinflacion.createCell(pos);
				}
				cell3.setCellValue(datos[2]==null?"":datos[2].toString());
				
				HSSFCell cell4 = rowpuntuacion.getCell(pos);
				if(cell4 == null){
					cell4 = rowpuntuacion.createCell(pos);
				}
				cell4.setCellValue(datos[4]==null?"":datos[4].toString());
				
				HSSFCell cell5 = rowcalifrating.getCell(pos);
				if(cell5==null){
					cell5 = rowcalifrating.createCell(pos);
				}
				cell5.setCellValue(datos[3]==null?"":datos[3].toString());
				pos++;
			}
			//llenando las cuentas
			String fecha = "";
			String fechaTemp = ((Object[])listaRating.get(0))[0].toString();
			String periodo1 = ((Object[])listaRating.get(0))[0].toString();
			int poscuentas = 12;
			//completa codigo cuentaa, descripcion y primer monto
			for(Object fila : listaRating){
				fecha = ((Object[])fila)[0].toString();
				if(!fechaTemp.equals(fecha) ){
					fechaTemp = fecha;
					break;
				}
				HSSFRow row = worksheet.getRow(poscuentas);
				//logger.info("row="+row);
				for(int i=1; i<((Object[])fila).length;i++ ){
					//logger.info(" i-1="+(i-1));
					//logger.info("dato="+((Object[])fila)[i]);
					if(row != null){
						HSSFCell cell = row.getCell(i-1);
						if(cell == null){
							cell = row.createCell(i-1);
						}
						cell.setCellValue(((Object[])fila)[i]== null ? "":((Object[])fila)[i].toString());
					}
				}
				poscuentas++;
			}
			//completa segundo, tercer y cuarto monto de los periodos
			//2 3 4 respectivamente
			poscuentas = 12;
			int poscell = 3;
			for(Object fila : listaRating){
				fecha = ((Object[])fila)[0].toString();
				//no se considera el primer periodo
				if(!fecha.equals(periodo1)){
					if(!fechaTemp.equals(fecha) ){
						fechaTemp = fecha;
						poscuentas = 12;
						poscell++;
					}
					HSSFRow row = worksheet.getRow(poscuentas);
					if(row != null){
						HSSFCell cell = row.getCell(poscell);
						if(cell == null){
							cell = row.createCell(poscell);
						}
						cell.setCellValue(((Object[])fila)[3].toString().trim());
					}
					poscuentas++;
				}
			}

			//actulizar pesataña año 4
			//


			HSSFSheet worksheet6 = workbook.getSheetAt(5);
			worksheet6.setForceFormulaRecalculation(true);
			HSSFSheet worksheet1 = workbook.getSheetAt(6);
			worksheet1.setForceFormulaRecalculation(true); 
			HSSFSheet worksheet2 = workbook.getSheetAt(0);
			worksheet2.setForceFormulaRecalculation(true);
			HSSFSheet worksheet3 = workbook.getSheetAt(1);
			worksheet3.setForceFormulaRecalculation(true);

			HSSFSheet worksheetse = workbook.getSheetAt(4);
			worksheetse.setForceFormulaRecalculation(true); 
			
			//escribiendo la informacion sobre un archivo temporal

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

	//ini MCG20130327
	/**
	 * completa el archivo plantilla.xls 
	 * con los datos del archivo PFRATING
	 * @return direccion del archivo completado
	 * @throws BOException
	 */
	@Override
	public String llenarPlantillaExcel(String dirArchivo, 
								       String nombreArchivo,
								       String dirTemporal,
								       String codigoCentralEmpresa,
								       String nombreEmpresa)throws BOException{
		logger.info("INCIO llenarPlantillaExcel");
		logger.info("codigoCentralEmpresa="+codigoCentralEmpresa);
		FileInputStream fileIn = null;
		String nombreArchivoTemporal = "";
		nombreArchivoTemporal = dirTemporal+
								File.separator+
								"temp_plantilla"+
								FechaUtil.formatFecha(new Date(), 
								FechaUtil.YYYYMMDD_HHMMSS)+
								".xls";
		Calendar calendar = Calendar.getInstance();
		//int anioInicial = calendar.get(Calendar.YEAR)-3;
		SQL_RATING = String.format(SQL_RATING, 
								   codigoCentralEmpresa,
								   codigoCentralEmpresa);
		//List listaRating = executeSQL(SQL_RATING);
				
		//ini MCG20130327
		String idPrograma=String.valueOf(programa.getId());
		List<TcuentaRating> listTcuentaRating=new ArrayList<TcuentaRating>();
		List<TmesEjercicio> listTmesEjercicio=new ArrayList<TmesEjercicio>();
		String strTipoEmpresa= String.valueOf(programa.getTipoEmpresa().getId());
		if (strTipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			listTcuentaRating=findListaCuentaRatingGrupo(idPrograma,codigoCentralEmpresa,"E");
			listTmesEjercicio=findListaMesesEjercicioRatingGrupo(idPrograma,codigoCentralEmpresa,"E");
			
		}else{
			String idGrupo=String.valueOf(programa.getIdGrupo());
			if (idGrupo.equals(codigoCentralEmpresa)){
				listTcuentaRating=findListaCuentaRatingGrupo(idPrograma,codigoCentralEmpresa,"G");
				listTmesEjercicio=findListaMesesEjercicioRatingGrupo(idPrograma,codigoCentralEmpresa,"G");
			}else{
				listTcuentaRating=findListaCuentaRatingGrupo(idPrograma,codigoCentralEmpresa,"E");
				listTmesEjercicio=findListaMesesEjercicioRatingGrupo(idPrograma,codigoCentralEmpresa,"E");
			}
			
		}
		if(listTcuentaRating==null ||listTcuentaRating!=null && listTcuentaRating.size()<=0){
			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
		}			
		//fin MCG20130327
		
		
//		if(listaRating.isEmpty()){
//			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
//		}
		SQL_MESES_EJERCICIO = String.format(SQL_MESES_EJERCICIO, 
											codigoCentralEmpresa,
											codigoCentralEmpresa);
		//List listaMesesEjercicio = executeSQL(SQL_MESES_EJERCICIO);
		
		
		if(listTmesEjercicio==null ||listTmesEjercicio!=null && listTmesEjercicio.size()<=0){
			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
		}	
		
//		if(listaMesesEjercicio.isEmpty()){
//			throw new BOException("No existe información del Sintesis Económico de "+nombreEmpresa);
//		}
		try {
			//String nombreEmpresa = programa.getNombreGrupoEmpresa(); //NOMB_EMPRESA	VARCHAR2(60 BYTE)
			//CORREGIR EL CODIGO CENTRAL EN EL EXCEL
			logger.info("plantilla="+dirArchivo+File.separator+nombreArchivo);
			String codigoCentralCliente = codigoCentralEmpresa;//programa.getIdEmpresa().toString(); //COD_CENTRAL_CLI	VARCHAR2(8 BYTE)
			fileIn = new FileInputStream(dirArchivo+File.separator+nombreArchivo);
			HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
			
			////////EPOMAYAY 16022012
			HSSFSheet wsIngresoDatos = workbook.getSheetAt(6);
			HSSFRow rowID = wsIngresoDatos.getRow(6);
			if(rowID != null){
				HSSFCell cellID = rowID.getCell(2);
				if(cellID != null){
					cellID.setCellValue(nombreEmpresa);
				}
			}
			////////EPOMAYAY 16022012
			
			HSSFSheet worksheet = workbook.getSheetAt(5);
			
			HSSFRow rownombreEmpresa = worksheet.getRow(1);
			HSSFCell cellC2 = rownombreEmpresa.getCell(2);
			if(cellC2 == null){
				cellC2 = rownombreEmpresa.createCell(2);
			}
			cellC2.setCellValue(nombreEmpresa.trim());
			
			HSSFRow rownrocliente = worksheet.getRow(2);
			HSSFCell cellC3 = rownrocliente.getCell(2);
			if(cellC3==null){
				cellC3 = rownrocliente.createCell(2);
			}
			cellC3.setCellValue(codigoCentralCliente);
			
			//llenando fecha y meses de ejercicio, inflacion,
			//puntuacion,califrating
			HSSFRow rowfechaperiodo = worksheet.getRow(4);
			HSSFRow rowmesesejercico = worksheet.getRow(5);
			HSSFRow rowinflacion = worksheet.getRow(6);
			HSSFRow rowpuntuacion = worksheet.getRow(7);
			HSSFRow rowcalifrating = worksheet.getRow(8);
			
			//ini  MCG20140825
			HSSFRow rowfactCualitativos = worksheet.getRow(9);
			HSSFRow rowfactCuantitativos = worksheet.getRow(10);
			HSSFRow rowfactBureau = worksheet.getRow(11);
			
			//fin  MCG20140825
			
			int pos = 14;//2;//posisicion fecha periodo
			for (TmesEjercicio otmesEjercicio: listTmesEjercicio){
				//for(Object obj : listaMesesEjercicio){
				//Object[] datos = (Object[])obj;
				HSSFCell cell1 = rowfechaperiodo.getCell(pos);
				if(cell1 == null){
					cell1 = rowfechaperiodo.createCell(pos);	
				}
				cell1.setCellValue(otmesEjercicio.getFechaPeriodoCalc()==null?"":otmesEjercicio.getFechaPeriodoCalc().toString());
				
				HSSFCell cell2 = rowmesesejercico.getCell(pos);
				if(cell2 ==null){
					cell2 = rowmesesejercico.createCell(pos);
				}
				cell2.setCellValue(otmesEjercicio.getMesesEjercicio()==null?"":otmesEjercicio.getMesesEjercicio().toString());
				
				HSSFCell cell3 = rowinflacion.getCell(pos);
				if(cell3==null){
					cell3 = rowinflacion.createCell(pos);
				}
				cell3.setCellValue(otmesEjercicio.getInflacion()==null?"":otmesEjercicio.getInflacion().toString());
				
				HSSFCell cell4 = rowpuntuacion.getCell(pos);
				if(cell4 == null){
					cell4 = rowpuntuacion.createCell(pos);
				}
				cell4.setCellValue(otmesEjercicio.getPuntRating()==null?"":otmesEjercicio.getPuntRating().toString());
				
				HSSFCell cell5 = rowcalifrating.getCell(pos);
				if(cell5==null){
					cell5 = rowcalifrating.createCell(pos);
				}
				cell5.setCellValue(otmesEjercicio.getCalifRating()==null?"":otmesEjercicio.getCalifRating().toString());
				
				//ini MCG20140825
				
				HSSFCell cell6 = rowfactCualitativos.getCell(pos);
				if(cell6==null){
					cell6 = rowfactCualitativos.createCell(pos);
				}
				cell6.setCellValue(otmesEjercicio.getFactCualitativos()==null?"":otmesEjercicio.getFactCualitativos().toString());
				
				HSSFCell cell7 = rowfactCuantitativos.getCell(pos);
				if(cell7==null){
					cell7 = rowfactCuantitativos.createCell(pos);
				}
				cell7.setCellValue(otmesEjercicio.getFactCuantitativos()==null?"":otmesEjercicio.getFactCuantitativos().toString());
				
				HSSFCell cell8 = rowfactBureau.getCell(pos);
				if(cell8==null){
					cell8 = rowfactBureau.createCell(pos);
				}
				cell8.setCellValue(otmesEjercicio.getFactBureau()==null?"":otmesEjercicio.getFactBureau().toString());
							
				//fin MCG20140825
				
				pos++;
			}
			//llenando las cuentas
			String fecha = "";
			//String fechaTemp = ((Object[])listaRating.get(0))[0].toString();
			//String periodo1 = ((Object[])listaRating.get(0))[0].toString();
			
			String fechaTemp = listTcuentaRating.get(0).getFechaPeriodoCalc().toString();
			String periodo1 = listTcuentaRating.get(0).getFechaPeriodoCalc().toString();
			
			int poscuentas = 12;
			//completa codigo cuentaa, descripcion y primer monto
			//for(Object fila : listaRating){
			for(TcuentaRating fila : listTcuentaRating){
				fecha = fila.getFechaPeriodoCalc().toString();
				if(!fechaTemp.equals(fecha) ){
					fechaTemp = fecha;
					break;
				}
				HSSFRow row = worksheet.getRow(poscuentas);				
				//logger.info("row="+row);	
				
				//[0]FECH_PERIODO_CALC
				//[1]COD_CUENTA
				//[2]DESC_CUENTA
				//[3]MONTO
				for(int i=1; i<4;i++ ){
					//logger.info(" i-1="+(i-1));
					//logger.info("dato="+((Object[])fila)[i]);
					String strValor="";
					if (i==1){
						strValor=fila.getCodCuenta()== null ? "":fila.getCodCuenta().toString();
					}else if (i==2){
						strValor=fila.getDescripcionCuenta()== null ? "":fila.getDescripcionCuenta().toString();
					}else if (i==3){
						strValor=fila.getMonto()== null ? "":fila.getMonto().toString();
					}
					
					if(row != null){
						HSSFCell cell = row.getCell(i-1);
						if(cell == null){
							cell = row.createCell(i-1);
						}
						cell.setCellValue(strValor);
					}
				}
				poscuentas++;
			}
			//completa segundo, tercer y cuarto monto de los periodos
			//2 3 4 respectivamente
			poscuentas = 12;
			int poscell = 3;
			//for(Object fila : listaRating){
			for(TcuentaRating fila : listTcuentaRating){	
				//fecha = ((Object[])fila)[0].toString();
				fecha=fila.getFechaPeriodoCalc()== null ? "":String.valueOf(fila.getFechaPeriodoCalc());
				
				//no se considera el primer periodo
				if(!fecha.equals(periodo1)){
					if(!fechaTemp.equals(fecha) ){
						fechaTemp = fecha;
						poscuentas = 12;
						poscell++;
					}
					HSSFRow row = worksheet.getRow(poscuentas);
					if(row != null){
						HSSFCell cell = row.getCell(poscell);
						if(cell == null){
							cell = row.createCell(poscell);
						}
						cell.setCellValue(fila.getMonto()== null ? "":fila.getMonto().toString().trim());
					}
					poscuentas++;
				}
			}

			//actulizar pesataña año 4
			//


			HSSFSheet worksheet6 = workbook.getSheetAt(5);
			worksheet6.setForceFormulaRecalculation(true);
			HSSFSheet worksheet1 = workbook.getSheetAt(6);
			worksheet1.setForceFormulaRecalculation(true); 
			HSSFSheet worksheet2 = workbook.getSheetAt(0);
			worksheet2.setForceFormulaRecalculation(true);
			HSSFSheet worksheet3 = workbook.getSheetAt(1);
			worksheet3.setForceFormulaRecalculation(true);

			HSSFSheet worksheetse = workbook.getSheetAt(4);
			worksheetse.setForceFormulaRecalculation(true); 
			
			//escribiendo la informacion sobre un archivo temporal

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
	@Override
	public List<SintesisEconomica> findSintesisEconomicoSQL(String idPrograma) throws BOException 
	{
		
		List<SintesisEconomica> listSintesisEconomica = null;
		SintesisEconomica osintesisEconomica = null;		
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT ID_SISNTESIS_FINANCIERA,FECHA_CARG,EMPRESA,USUARIO,CODIGO_ARCHIVO,EXTENCION_ARCH,IIPF_PROGRAMA_ID,NOMBRE_ARCHIVO ");
			sb.append(",(select nvl(max(codigo),'00000000') from PROFIN.TIIPF_EMPRESA where trim(nombre)=trim(EMPRESA)) CODIGOEMPRESA, ");
			sb.append(" (select nvl(max(TT_ID_TIPO_GRUPO),'0') from PROFIN.TIIPF_EMPRESA where trim(nombre)=trim(EMPRESA) ) TIPO_EMPRESA ");
			sb.append(" FROM PROFIN.tiipf_sintesis_economica where id_sisntesis_financiera in (");
			sb.append(" select max(id_sisntesis_financiera) id_sisntesis_financiera"); 
			sb.append(" from PROFIN.tiipf_sintesis_economica");
			sb.append(" WHERE IIPF_PROGRAMA_ID=" + idPrograma);
			sb.append(" group by IIPF_PROGRAMA_ID,empresa) ");
			sb.append(" ORDER BY TIPO_EMPRESA ASC");
			
			
			List listSE= super.executeSQL(sb.toString());
						
			listSintesisEconomica = new ArrayList<SintesisEconomica>();
			
			for (Iterator it = listSE.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				osintesisEconomica= new SintesisEconomica();
				Programa oprograma =new Programa();
				
				osintesisEconomica.setId(Long.valueOf(amount[0].toString()));
				
				if(amount[1] != null){					
					osintesisEconomica.setFechaCarga(new Date());					
				}
				if(amount[2] != null){
					osintesisEconomica.setNombreEmpresa(amount[2].toString());}
				if(amount[3] != null){
					osintesisEconomica.setUsuario(amount[3].toString());}
				if(amount[4] != null)
					osintesisEconomica.setCodigoArchivo(amount[4].toString());
				if(amount[5] != null){
					osintesisEconomica.setExtension(amount[5].toString());}
				if(amount[6] != null){
					oprograma.setId(Long.valueOf(amount[6].toString()));
					osintesisEconomica.setPrograma(oprograma);}
				if(amount[7] != null){
					osintesisEconomica.setNombreArchivo(amount[7].toString());}	
				if(amount[8] != null){
					osintesisEconomica.setCodEmpresa(amount[8].toString());}	
				listSintesisEconomica.add(osintesisEconomica);
			}
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return listSintesisEconomica;
		
	}
	
	//ini MCG20121121
	
	public boolean validationdetalleSintesisEconomico(){
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void savelistaSintesisEconomico(Programa oprograma,List<SintesisEconomica> olistaSintesisEconomica) throws BOException{
		
		if(validationdetalleSintesisEconomico()){
			HashMap<String,Long> params = new HashMap<String,Long>();
			params.put("programa", oprograma.getId());
			List<SintesisEconomica> listatemp =  findByParams2(SintesisEconomica.class, params);
			List<SintesisEconomica> listaDel = new ArrayList<SintesisEconomica>();
			boolean flag= false;
			for(SintesisEconomica lrb : listatemp ){
				for(SintesisEconomica lrbtemp : olistaSintesisEconomica){
					if(lrbtemp.getId()!=null && lrbtemp.getId().equals(lrb.getId())){
						flag=true;
					}
				}
				if(!flag){
					listaDel.add(lrb);
				}
				flag= false;
			}
			saveCollection(olistaSintesisEconomica);
			deleteCollection(listaDel);		
			programaBO.actualizarFechaModificacionPrograma(programa.getId());
		}
	}
	
	//fin MCG20121121
	
	//ini MCG20130327
	public List<TcuentaRating> findListaCuentaRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws BOException {
		List<TcuentaRating> listTcuentaRating = null;	
		try{			
			listTcuentaRating = ratingDAO.findListaCuentaRatingGrupo(idPrograma,codCliente,TipoEmpresa);
		
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
		return listTcuentaRating;
	}
	
	public List<TmesEjercicio> findListaMesesEjercicioRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws BOException {
		List<TmesEjercicio> listTmesEjercicio = null;	
		try{			
			listTmesEjercicio = ratingDAO.findListaMesesEjercicioRatingGrupo(idPrograma,codCliente,TipoEmpresa);
		
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
		return listTmesEjercicio;
	}
	
	//fin MCG20130327
	

	public void setFileExcel(File fileExcel) {
		this.fileExcel = fileExcel;
	}

	public ParametroBO getParametroBO() {
		return parametroBO;
	}

	public void setParametroBO(ParametroBO parametroBO) {
		this.parametroBO = parametroBO;
	}

	public boolean isFlagEditFile() {
		return isFlagEditFile;
	}

	public void setFlagEditFile(boolean isFlagEditFile) {
		this.isFlagEditFile = isFlagEditFile;
	}

	public String getNombreEmpresaGrupo() {
		return nombreEmpresaGrupo;
	}

	public void setNombreEmpresaGrupo(String nombreEmpresaGrupo) {
		this.nombreEmpresaGrupo = nombreEmpresaGrupo;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}


	
	
}
