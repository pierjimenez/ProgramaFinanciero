package pe.com.bbva.iipf.pf.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.ArchivoMnt;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.StringUtil;

@Service("parametroAction")
@Scope("prototype")
public class ParametroAction extends GenericAction 
{

    Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroBO parametroBO ;
	
	private Parametro parametro;
	
	private List<Parametro> parametros;
	
	private ArchivoMnt archivoMnt	;
	private List<ArchivoMnt> listArchivoMnt;
	
	private Parametro parametroEdicion;
	
	private String id;
	private String textScritp;
	private String tipoScritp;
	private String codigoAcceso;
	
	
	private File archivo;
	private String archivoFileName;
	private String contentType; 
	private String contentDisposition;	
	private InputStream fileInputStream;
	
	
	private String rutaMnt;
	private String nombreArchivo2;
	private String contentType2;
	private String contentDisposition2;
	private InputStream fileInputStream2;
	
	
	private String contentType3;
	private String contentDisposition3;
	private InputStream fileInputStream3;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Parametro getParametroEdicion() {
		return parametroEdicion;
	}

	public void setParametroEdicion(Parametro parametroEdicion) {
		this.parametroEdicion = parametroEdicion;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public Parametro getParametro() 
	{
		if(super.getObjectSession("parametro") != null)
			parametro = (Parametro)super.getObjectSession("parametro");
		return parametro;
	}

	public ParametroBO getParametroBO() {
		return parametroBO;
	}
	
	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public void setParametroBO(ParametroBO parametroBO) {
		this.parametroBO = parametroBO;
	}
	
	public String listaParametros() throws BOException{
		
		try
		{
						
			if(super.getObjectSession("parametro") == null)
			{	
				parametro = new Parametro();
				parametro.setEstado("A");
				
				parametros = parametroBO.listar(parametro);
				
				super.setObjectSession("parametro", parametro);
			}
			else
			{
				parametros = parametroBO.listar((Parametro)getObjectSession("parametro"));
			}		
											
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaParametros";
	}
		
	public String buscarParametros() throws BOException
	{
		try
		{   
			super.setObjectSession("parametro", null);
			
			parametros = parametroBO.listar(this.getParametro());
									
			super.setObjectSession("parametro", parametro);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaParametros";
	}
	
	public String paginarParametros() throws BOException
	{
		try
		{
			parametros = parametroBO.listar(this.getParametro());										
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaParametros";
	}
	
	public String agregarParametro() throws BOException
	{		
		return "edicionParametro";
	}
	
	public String modificarParametro() throws BOException
	{
		try
		{				
						
			parametroEdicion = (Parametro)this.parametroBO.obtienePorId(Long.valueOf(this.getId()));
			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
		}
		return "edicionParametro";
	}
	
	public String guardarParametro() throws BOException
	{
		try
		{	
			UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
			
			if(this.getId() == null || this.getId().equals(""))
			{			
				
				this.parametroEdicion.setCodUsuarioCreacion(user.getRegistroHost());
				this.parametroEdicion.setFechaCreacion(new Date());
				
				if(parametroBO.obtienePorCodigo(this.parametroEdicion.getCodigo()) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}
				this.parametroBO.insertar(this.parametroEdicion);
				this.setId(this.parametroEdicion.getId().toString());
				addActionMessage("Registro exitoso.");
			}
			else
			{
				this.parametroEdicion.setCodUsuarioModificacion(user.getRegistroHost());
				this.parametroEdicion.setFechaModificacion(new Date());
				parametroEdicion.setId(Long.parseLong(this.getId()));
				
				if(parametroBO.obtienePorCodigoSinActual(this.parametroEdicion.getCodigo(),new Long(this.getId())) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}				
				this.parametroBO.modificar(this.parametroEdicion);
				addActionMessage("Actualización exitosa.");
			}			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
		}
		return "edicionParametro";
	}
	
	public void loadResultScritp() {
		

		String strTextScritp =getTextScritp();
		String strTipoScrip = getTipoScritp();
		String strCodigoAcceso=getCodigoAcceso();
		List<List> listPoolBancarioTotal = new ArrayList<List>();
		
		PrintWriter out = null;
		try {
		
			
			//getResponse().setContentType("text/html");
			getResponse().setCharacterEncoding("UTF-8");			
			getResponse().setContentType("text/html;charset=UTF-8");
			out = getResponse().getWriter();
			String strencriptado=Constantes.PASWW_EXEC;
			String strdescriptado=StringUtil.decrypt(strencriptado);
			
			if (strCodigoAcceso.equals(strdescriptado)){

			out.printf("<link rel=\"stylesheet\" type=\"text/css\" href=\"/ProgramaFinanciero/css/table.css\"/>");
			out.printf("<form>");

							listPoolBancarioTotal = new ArrayList<List>();
				
							listPoolBancarioTotal = parametroBO.findResultScript(strTextScritp, strTipoScrip);
															
							if (listPoolBancarioTotal != null
									&& listPoolBancarioTotal.size() > 0) {
																
								out.printf("<table class=\"gridtable\">");
								StringBuilder sb=new StringBuilder ();
								
								sb.append("<tr>");
								Map<String, String> hm = new HashMap<String, String>();
											
								for (Object lista : listPoolBancarioTotal.get(0)) {
									hm = (HashMap<String, String>) lista;
		
									Iterator it = hm.entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry e = (Map.Entry) it.next();								
											sb.append("<th>" + e.getKey().toString() + "</th>");
																				
									}
								}	
								
								sb.append("</tr>");					
								out.printf(sb.toString());								
								
		
								for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
									out.printf("<tr>");
									Map<String, String> hmr = new HashMap<String, String>();
									for (Object x : listPoolBancarioTotal.get(i)) {
										hmr = (HashMap<String, String>) x;
		
										Iterator itr = hmr.entrySet().iterator();
										while (itr.hasNext()) {
											Map.Entry er = (Map.Entry) itr.next();

											out.printf("<td>" + 
															(er.getValue()== null?"":er.getValue().toString().replace("%", "&#37;"))
															+ "</td>");
										}
									}
		
									out.printf("</tr>");
								}
		
								out.printf("</table>");
							}else{
								out.printf("<table class=\"gridtable\">");
								out.printf("<tr>");
								out.printf("<td>No existe registros</td>");
								out.printf("</tr>");
								out.printf("</table>");								
							}
			out.printf("<form>");
		 }else{
			    out.printf("<table class=\"gridtable\">");
				out.printf("<tr>");
				out.printf("<td>No tiene acceso para ejecutar script</td>");
				out.printf("</tr>");
				out.printf("</table>");
		 }

		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
			addActionError(e.getMessage());
			out.printf("<table class=\"gridtable\">");
			out.printf("<tr>");
			out.printf("<td>"+e.getMessage()+"</td>");
			out.printf("</tr>");
			out.printf("</table>");
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			addActionError(e.getMessage());
			out.printf("<table class=\"gridtable\">");
			out.printf("<tr>");
			out.printf("<td>"+e.getMessage()+"</td>");
			out.printf("</tr>");
			out.printf("</table>");
		} finally {
			out.close();
		}
	}
	
	
	private HashMap<String, Object> cargarParametrosCarga() throws BOException {
		HashMap<String, Object> parametros = new HashMap<String, Object>() ;
		Parametro dirCargas = parametroBO.findByNombreParametro(Constantes.RUTALOG);
		if(dirCargas==null || dirCargas.getValor().length()==0){
			throw new BOException("No se encontró el parámetro que indica el directorio de cargas");
		}
		parametros.put("RUTA_LOG",dirCargas.getValor());
		return parametros;
	}
	
	private HashMap<String, Object> cargarParametrosRutaManual() throws BOException {
		HashMap<String, Object> parametros = new HashMap<String, Object>() ;
		Parametro dir = parametroBO.findByNombreParametro(Constantes.RUTAMANUALPF);
		if(dir==null || dir.getValor().length()==0){
			throw new BOException("No se encontró el parámetro que indica el directorio del manual de pf");
		}
		parametros.put("RUTAMANUALPF",dir.getValor());
		return parametros;
	}
	
	
	public String descargarLogpf() throws BOException, DAOException {
		
		String forward = SUCCESS;
		try {
			String  YYYYMMDD_HHMMSS_ = "yyyyMMdd_hhmmss";
			HashMap<String, Object> parametros = cargarParametrosCarga();
			String ruta = (String) parametros.get("RUTA_LOG");
			archivo = new File(ruta);
			if(archivo.exists()){
				archivoFileName = Constantes.NAME_APP_LOG+"_"+FechaUtil.formatFecha(new Date(),YYYYMMDD_HHMMSS_)+".log";
				setContentType(new MimetypesFileTypeMap().getContentType(archivo));
				setContentDisposition("attachment;filename=\"" + archivoFileName + "\"");
				
				fileInputStream = new FileInputStream(archivo);
				
			}else{
				addActionError("Archivo no encontrado");
			}
			
		} catch (Exception e) {
			 forward = "listaParametros";
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return forward;
	}
	
	public String buscarArchivoMnt() throws BOException
	{
		try
		{
			super.setObjectSession("archivoMnt", null);
			
			listArchivoMnt = parametroBO.findArchivosMNT(this.getArchivoMnt());
			
			super.setObjectSession("archivoMnt", archivoMnt);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaParametros";
	}
	
	public String paginarArchivosMnts() throws BOException
	{
		try
		{
			listArchivoMnt = parametroBO.findArchivosMNT(this.getArchivoMnt());										
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaParametros";
	}
	
	 public String downloadFileMnt() throws Exception { 	
			logger.info("INICIO download");
					
			String nombreDocumento=this.getNombreArchivo2();
			String ruta=getRutaMnt();
			String nombreArchivo=nombreDocumento; 	
			File file=null;
			String forward = SUCCESS;
			try {
				String dir  = ruta;
				String pathToFile=dir+File.separator+nombreArchivo;
				String fileName=nombreDocumento; 
				file = new File(pathToFile);
			   	setContentType2(new MimetypesFileTypeMap().getContentType(file));
			   	setContentDisposition2("attachment;filename=\""+ fileName + "\"");
				fileInputStream2 =  new FileInputStream(file);
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "listaParametros";
			}
			
			return forward;
	}
	
		public String eliminarFileMnt(){
			logger.info("INICIO download");
			
			String nombreDocumento=this.getNombreArchivo2();
			String ruta=getRutaMnt();
			String nombreArchivo=nombreDocumento; 	
		
			File file=null;
			String forward = "listaParametros";
			try {
			
				
				String dir  = ruta;
				String pathToFile=dir+File.separator + nombreArchivo;
				file = new File(pathToFile);
				if (file!=null && file.exists()){
			    file.delete();
				}
				setListArchivoMnt( parametroBO.findArchivosMNT(this.getArchivoMnt()));	
				addActionMessage("Eliminado Correctamente");
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "listaParametros";
			}finally{
				
			}
			return forward;
		} 
		
		 public String downloadManual() throws Exception { 	
				logger.info("INICIO download Manual");
						
				String nombreDocumento="PFA-Manual de llenado v3.0.pdf";
				String ruta="";					
				File file=null;
				String forward = SUCCESS;
				try {
					HashMap<String, Object> parametros = cargarParametrosRutaManual();
					ruta = (String) parametros.get("RUTAMANUALPF");					
					
					String pathToFile=ruta;
					String fileName=nombreDocumento; 
					file = new File(pathToFile);
				   	setContentType3(new MimetypesFileTypeMap().getContentType(file));
				   	setContentDisposition3("attachment;filename=\""+ fileName + "\"");
					fileInputStream3 =  new FileInputStream(file);
				}catch (Exception e) {
					addActionError(e.getMessage());
					logger.error(StringUtil.getStackTrace(e));
					forward = "panel";
				}
				
				return forward;
		}

	public File getArchivo() {
		return archivo;
	}

	public void setArchivo(File archivo) {
		this.archivo = archivo;
	}

	public String getArchivoFileName() {
		return archivoFileName;
	}

	public void setArchivoFileName(String archivoFileName) {
		this.archivoFileName = archivoFileName;
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

	public String getTextScritp() {
		return textScritp;
	}

	public void setTextScritp(String textScritp) {
		this.textScritp = textScritp;
	}

	public String getTipoScritp() {
		return tipoScritp;
	}

	public void setTipoScritp(String tipoScritp) {
		this.tipoScritp = tipoScritp;
	}

	public String getCodigoAcceso() {
		return codigoAcceso;
	}

	public void setCodigoAcceso(String codigoAcceso) {
		this.codigoAcceso = codigoAcceso;
	}

	public ArchivoMnt getArchivoMnt() {
		if(super.getObjectSession("archivoMnt") != null)
			archivoMnt = (ArchivoMnt)super.getObjectSession("archivoMnt");
		return archivoMnt;
		
	}
	
	public void setArchivoMnt(ArchivoMnt archivoMnt) {
		this.archivoMnt = archivoMnt;
	}

	public List<ArchivoMnt> getListArchivoMnt() {
		return listArchivoMnt;
	}

	public void setListArchivoMnt(List<ArchivoMnt> listArchivoMnt) {
		this.listArchivoMnt = listArchivoMnt;
	}

	public String getNombreArchivo2() {
		return nombreArchivo2;
	}

	public void setNombreArchivo2(String nombreArchivo2) {
		this.nombreArchivo2 = nombreArchivo2;
	}

	public String getContentType2() {
		return contentType2;
	}

	public void setContentType2(String contentType2) {
		this.contentType2 = contentType2;
	}

	public String getContentDisposition2() {
		return contentDisposition2;
	}

	public void setContentDisposition2(String contentDisposition2) {
		this.contentDisposition2 = contentDisposition2;
	}

	public InputStream getFileInputStream2() {
		return fileInputStream2;
	}

	public void setFileInputStream2(InputStream fileInputStream2) {
		this.fileInputStream2 = fileInputStream2;
	}

	public String getRutaMnt() {
		return rutaMnt;
	}

	public void setRutaMnt(String rutaMnt) {
		this.rutaMnt = rutaMnt;
	}

	public String getContentType3() {
		return contentType3;
	}

	public void setContentType3(String contentType3) {
		this.contentType3 = contentType3;
	}

	public String getContentDisposition3() {
		return contentDisposition3;
	}

	public void setContentDisposition3(String contentDisposition3) {
		this.contentDisposition3 = contentDisposition3;
	}

	public InputStream getFileInputStream3() {
		return fileInputStream3;
	}

	public void setFileInputStream3(InputStream fileInputStream3) {
		this.fileInputStream3 = fileInputStream3;
	}


	
}
