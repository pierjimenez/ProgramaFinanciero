package pe.com.bbva.iipf.pf.servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.model.FilaEmpresaHoja;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.reportes.bo.GeneradorReporteBO;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FileUtil;
import pe.com.stefanini.core.util.StringUtil;

/**
 * Servlet implementation class GeneradorReporteP05Excel
 */
public class GeneradorReporteP05Excel extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	private static final int BUFFER = 2048;
	
	private static HttpServletRequest requestApp = null;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private InputStream inputStream;
	private Long idPrograma;
	private Programa programa;
	private Empresa empresaPrincipal = new Empresa();
    private List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
    private String nombreTemp;
    
	//@Resource
    GeneradorReporteBO generadorReporteBO;
	
	//@Resource
	private ProgramaBO programaBO ;
	
	//@Resource
	private EmpresaBO empresaBO ;
	
	//@Resource
	private SintesisEconomicoBO sintesisEconomicoBO;
	
	private List<Empresa> listEmpresaPropRiesg = new ArrayList<Empresa>();
	
	private TreeMap<String, HashMap<String, String>> listaImg = new TreeMap<String, HashMap<String, String>>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneradorReporteP05Excel() {
        super();
         
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		generarExcel(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		this.requestApp = request;
		generarExcel(request, response);
	}
	
	public String generarExcel(HttpServletRequest request, HttpServletResponse response) 
	{
		logger.info("INICIO: GenerarExcel");
	  try {	
		
		ObjectInputStream dataInput = new ObjectInputStream(request.getInputStream());
		String cadenaDatos;
		cadenaDatos = (String)dataInput.readObject();
		//System.out.println(cadenaDatos);
		setIdPrograma(new Long(cadenaDatos));
		ApplicationContext springcontext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
		generadorReporteBO = (GeneradorReporteBO) springcontext.getBean("generadorReporteBO");
		programaBO = (ProgramaBO) springcontext.getBean("programaBO");
		empresaBO = (EmpresaBO) springcontext.getBean("empresaBO");
		sintesisEconomicoBO = (SintesisEconomicoBO) springcontext.getBean("sintesisEconomicoBO");
		UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
		
			
     	Map<String,Object> params=cargarParametros();
     	params.put("dir_temporal", getObjectParamtrosSession(Constantes.DIR_TEMPORAL));
	    String dir=(String) getObjectParamtrosSession(Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO);
	    String filename 				= dir+File.separator+Constantes.FILE_REPO_XLS_05;
	    logger.info("directorio de plantilla dir 0: "+Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO);
	    logger.info("directorio de plantilla dir 1: "+dir);
	    logger.info("directorio de plantilla dir 2: "+filename);
	    int tamnio=lstEmpresas.size();
	    if(tamnio<=6){
	    	filename=dir+File.separator+Constantes.FILE_REPO_XLS_05;
	    }else if(tamnio<10){
	    	filename=dir+File.separator+Constantes.FILE_REPO_XLS_10;	
	    }else if(tamnio<20){
	    	filename=dir+File.separator+Constantes.FILE_REPO_XLS_20;
	    }else{
	    	filename=dir+File.separator+Constantes.FILE_REPO_XLS_30;
	    }
	    params.put("dirPlantillas", dir);
	    params.put("numEpresas",new Integer(tamnio));
	    params.put("nameTemplateFinal", filename);
        FileInputStream fis = new FileInputStream(filename);
        HSSFWorkbook wbPF   = new HSSFWorkbook(fis);
        List<SintesisEconomica> lstSintEconomica = sintesisEconomicoBO.findSintesisEconomicoSQL(String.valueOf(idPrograma));
       
        
        //ini MCG20130225
        List<Empresa> listEmpresaIndex=AsingarIndexEmpresaOrigen(lstEmpresas);
        params.put("listaEmpresaByPrograma", listEmpresaIndex); 
        //fin MCG20130225
        
        //directorio donde se exportara el archivo
        String dir_export=(String) getObjectParamtrosSession(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
        params.put("listaWbProg", getHSSFWorkbook(dir_export,programa.getNombreGrupoEmpresa(),lstEmpresas,lstSintEconomica));
        
        params.put("listaWbProg2", getHSSFWorkbook2(dir_export,programa.getNombreGrupoEmpresa(),lstEmpresas,lstSintEconomica));
        
        params.put("tieneSintesisEconoDeGrupo", tieneSintesisEconoDeGrupo( programa.getNombreGrupoEmpresa(), lstSintEconomica));

        //ini MCG20111104        
        List<Map<String,Object>> lstCodigoEmpresaSEF =buscarCodigoEmpresaSEF(programa.getNombreGrupoEmpresa(),lstEmpresas, lstSintEconomica);
        params.put("listaCodigoEmpresaSEF",lstCodigoEmpresaSEF);        
        //fin MCG20111104
        
  
        
        //1 creando el directorio Temporal dir_temporal_to_export
		nombreTemp =	params.get("dir_temporal").toString()+
							File.separator + 
							usuarioSession.getRegistroHost() + "_" +
							FechaUtil.formatFecha(new Date(), FechaUtil.YYYYMMDD_HHMMSS);
		File dirTemporalToExport = new File (nombreTemp);
		dirTemporalToExport.mkdir();
		params.put("dir_temporal_to_export", nombreTemp);
        generadorReporteBO.inicializar(params);
        //LLamamos a la clase que se encargara de llenar Data y refrescar las formulas de las plantillas.
        generadorReporteBO.buildReportExcelWithTemplate(wbPF);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        wbPF.write(bos);
	    //inputStream = new ByteArrayInputStream(bos.toByteArray());
		
		//System.out.println(cadenaDatos);
		
		ObjectOutputStream dataOutput = new ObjectOutputStream(response.getOutputStream());
		//Escribiendo el archivo Excel
		dataOutput.writeObject(bos.toByteArray());
		//String nameDirectory = dir_temporal "c:/mnt/compartido/profin/export/archivostmp";
		
		//2 copiando los ficheros
		//blanco
		FileUtil.copy(new File(params.get("dirPlantillas").toString()+File.separator+Constantes.BLANCO_DOC),
					new File(nombreTemp+File.separator+Constantes.BLANCO_DOC));
		//script basic
		FileUtil.copy(new File(params.get("dirPlantillas").toString()+File.separator+Constantes.SCRIPT_BASIC), 
					new File(nombreTemp+File.separator+Constantes.SCRIPT_BASIC));
		
		String nameDirectory =nombreTemp;//params.get("dir_temporal").toString()+"/archivostmp";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream outzip = new ZipOutputStream(out);
		zipDirByteArray(outzip, nameDirectory);
		outzip.flush();
		out.close();
		byte[] arregloZip = out.toByteArray();
		//Escribiendo el directorio de los html
		dataOutput.writeObject(arregloZip);
		
		//ByteArrayOutputStream outvbs = new ByteArrayOutputStream(new File);
		
		
		dataOutput.flush();
		dataInput.close();
		dataOutput.close(); 
		
	  }catch (Exception e) {		
		logger.error(StringUtil.getStackTrace(e));
	  }finally{
		  eliminarTemporales(nombreTemp);
	  }
	  logger.info("FIN generar excel");
		return "generarExcel";
	}
	
	
	private List<Empresa> AsingarIndexEmpresaOrigen(List<Empresa> ListaEmpresa ){		 
		int cont=1;
		for (Empresa emp:ListaEmpresa){			
				cont++;				
				emp.setIndexHoja(cont);	
		}	
		return ListaEmpresa;
		
	}
	
	/**
	 * Elimina todos los archivos creados
	 * @param nombreTemp
	 */
	public void eliminarTemporales(String nombreTemp){
		logger.info("Eliminando ficheros creados");
		File directory = new File(nombreTemp);
		// Get all files in directory
		File[] files = directory.listFiles();
		if(files!=null){
			for (File file : files) {
				// Delete each file
				if (!file.delete()) {
					// Failed to delete file
					logger.info("Failed to delete " + file);
				}
			}
			directory.delete();
		}
		
	}
	
	//ini MCG20111104
	
	private  List<Map<String,Object>> buscarCodigoEmpresaSEF(String nombGrupo,List<Empresa> lstEmpresas,List<SintesisEconomica> lstSintEconomica){
				
		List<Map<String,Object>> lstCodigoEmpresas = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> lstNombresArchivEmpPrinc = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lstNombresArchivEmpOtros = new ArrayList<Map<String,Object>>();
		
//		Map<String,Object> nombArchGrupo=null;
		Map<String,Object> nombArchEmpPrin  =null;
		Map<String,Object> nombArchEmp  =null;
		
		//Si es grupo
//		if(!programa.getTipoEmpresa().getId().toString().equalsIgnoreCase(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
//			
//			for(SintesisEconomica se:lstSintEconomica){
//			     if(nombGrupo.equalsIgnoreCase(se.getNombreEmpresa())){
//					nombArchGrupo	=	new HashMap<String,Object>();
//					nombArchGrupo.put("codigoEmpresaSEF",String.valueOf(se.getCodEmpresa().toString()));
//					break;
//				}
//			}
//		
//		}
		
		for(Empresa emp:lstEmpresas){

			for(SintesisEconomica se:lstSintEconomica){
				
			  
				if(emp.getNombre().equalsIgnoreCase(se.getNombreEmpresa())){
					
						Tabla tg= emp.getTipoGrupo();
						if(tg!=null){
							if( tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_PRINCIPAL){
								
								nombArchEmpPrin=new HashMap<String,Object>();
								nombArchEmpPrin.put("codigoEmpresaSEF",String.valueOf(se.getCodEmpresa().toString()));
								lstNombresArchivEmpPrinc.add(nombArchEmpPrin);
								
							}else if(tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_ANEXA
											||tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_SECUNDARIA){
								
								nombArchEmp	=	new HashMap<String,Object>();
								nombArchEmp.put("codigoEmpresaSEF",String.valueOf(se.getCodEmpresa().toString()));
								lstNombresArchivEmpOtros.add(nombArchEmp);
								
							}
						}else{
							nombArchEmp	=	new HashMap<String,Object>();
							nombArchEmp.put("codigoEmpresaSEF",String.valueOf(se.getCodEmpresa().toString()));
							lstNombresArchivEmpPrinc.add(nombArchEmp);
						}
					break;
				}
			}

			nombArchEmp=null;
			nombArchEmpPrin=null;
		}
		
//		if(nombArchGrupo!=null)  //Ponemos en la primera ubicacion al nombre de grupo
//			lstCodigoEmpresas.add(nombArchGrupo);
		
		if(lstNombresArchivEmpPrinc!=null && !lstNombresArchivEmpPrinc.isEmpty()) 
			lstCodigoEmpresas.addAll(lstNombresArchivEmpPrinc);
		
		if(lstNombresArchivEmpOtros!=null && !lstNombresArchivEmpOtros.isEmpty()) 
			lstCodigoEmpresas.addAll(lstNombresArchivEmpOtros);
	
		return lstCodigoEmpresas;
	}
	
	private static void zipDirByteArray(ZipOutputStream out, String dir){
        File dirObj = new File(dir);        
        if(!dirObj.isDirectory()){
            System.err.println(dir + " No es  un directorio");
            return;
         }
        try{            
            addDir(dirObj, out,dirObj.getName());
               
            }
        catch (IOException e){
            e.printStackTrace();            
        }
    }
	
	private static void addDir(File dirObj, ZipOutputStream out, String strRelative) throws IOException{
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[BUFFER];
        for (int i=0; i<files.length; i++){
            if(files[i].isDirectory()){//Solo para los archivos dentro de la carpeta
                //addDir(files[i], out, strRelative+File.separator+files[i].getName());
                continue;
            }
            
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());            
            out.putNextEntry(new ZipEntry(strRelative + File.separator+ files[i].getName()));
            int len;
            while((len = in.read(tmpBuf)) > 0){
            	out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    } 
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	public Long getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Long idPrograma) {
		this.idPrograma = idPrograma;
	}
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	 private Map cargarParametros() throws BOException{
		 
		 	this.setPrograma( this.programaBO.findById(this.getIdPrograma()));
			logger.info(" this.programa.getActividadPrincipal() "+this.programa.getActividadPrincipal());
			logger.info(" programa.getAntiguedadNegocio() "+programa.getAntiguedadNegocio());
	     	Map<String,Object> params 	= new HashMap<String,Object>();

			String idPrograma			= this.getIdPrograma().toString();
			Long tipoPrograma			= programa.getTipoPrograma().getId();// (Long) getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION);
			String tipoEmpresa			= programa.getTipoEmpresa().getId().toString();//(String) getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);
			String nombreEmpresaGrupo	= null;
			String empresaPrincipalNomb		= null;
			String empresaRUC			= null;
			Integer annioProg			= programa.getAnio();//(Integer)getObjectSession(Constantes.ANIO_PROGRAMA_SESSION);
			
			//ESTO NOS PODRIA DAR PISTAS DE LA CANTIDAD DE EMPRESAA ASOCIADAS AL GRUPO.
			 lstEmpresas = this.empresaBO.listarEmpresasPorPrograma(this.programa.getId());
			logger.info(" idPrograma: "+idPrograma+" : lstEmpresas: "+lstEmpresas);
			boolean existePrincipal = false;
			
			
			if(lstEmpresas != null)
			{
				for(Empresa emp : lstEmpresas ){
												
					if(emp.getTipoGrupo() == null || emp.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL))
					{
						existePrincipal = true;
						this.empresaPrincipal = emp;
					}
				}
			}
			
			 //Si es Empresa
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				
				empresaRUC		=programa.getRuc(); //(String) getObjectSession(Constantes.COD_RUC_EMPRESA_SESSION);
				if(existePrincipal)
					empresaPrincipalNomb=empresaPrincipal.getNombre();
			}else{
				//Si es Grupo
				empresaRUC		=empresaPrincipal.getRuc();
				
				if(existePrincipal)
				{
					empresaPrincipalNomb=empresaPrincipal.getNombre();
				}
				
			}
			

			nombreEmpresaGrupo = programa.getNombreGrupoEmpresa();
			
	     	params.put("idPrograma", idPrograma);
	    	params.put("tipoPrograma", tipoPrograma);
	    	params.put("tipoEmpresa", tipoEmpresa);
	    	params.put("nombreEmpresaGrupo", nombreEmpresaGrupo);
	    	params.put("empresaPrincipal", empresaPrincipal.getNombre());
	    	params.put("empresaRUC", empresaRUC);
	    	params.put("annioProg", annioProg);
	    	
	    	params.put("programax", this.getPrograma());
	    	
	    	params.put("programaBO", this.programaBO);
	    	
			logger.info(" idPrograma: "+idPrograma+" tipoPrograma: "+tipoPrograma+" tipoEmpresa: "+tipoEmpresa+ " nombreEmpresaGrupo:  "+
					nombreEmpresaGrupo+ " empresaPrincipal: "+empresaPrincipal+" empresaRUC: "+empresaRUC);
			
			cargarEmpresasPropRiesgo();
	    	params.put("listEmpresaPropRiesg",listEmpresaPropRiesg);
	    	
			return params;
	 }
	 
	 public void cargarEmpresasPropRiesgo(){
			
			//Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			List<Empresa> listEmpresatmp= new ArrayList<Empresa>();		
			try {
				String tipoEmpresa = this.getPrograma().getTipoEmpresa().getId().toString();//.getCodigo();		// sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
				String idprograma  = String.valueOf(this.getPrograma().getId().longValue()); //sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
	 			
				//Buscar la actividad de la empresa
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					
					Empresa oTempresa=new Empresa();				
					String ruc =this.getPrograma().getRuc();// sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
					String strEmpresaPrincipal = this.empresaPrincipal.getNombre();//  sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
					
					listEmpresatmp =programaBO.findEmpresaByIdprogramaRuc(idprograma, ruc);	
					if (listEmpresatmp!=null && listEmpresatmp.size()>0) {
						Long idempresa= listEmpresatmp.get(0).getId();
						oTempresa.setId(idempresa);
						oTempresa.setCodigo(ruc);
						oTempresa.setNombre(strEmpresaPrincipal);
						listEmpresaPropRiesg.add(oTempresa );				
					}			
					

				}else{	
					
					listEmpresaPropRiesg =programaBO.findEmpresaByIdprograma(new Long(idprograma));					
				}	
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}
			
	}
	 
	 public Object getObjectParamtrosSession(String codigoParametro){
		String valor= "";
		List<Parametro> listaParametros = (List<Parametro>) getObjectSession(Constantes.PARAMETROS_SESSION);
		for(Parametro param : listaParametros){
			if(param.getCodigo().equals(codigoParametro)){
				valor = param.getValor();
				break;
			}
		}
		return valor;
	}
	 
	 public static Object getObjectSession(String nombre){
			//Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		 HttpSession sessionparam =  requestApp.getSession();
			Object  obj = sessionparam.getAttribute(nombre);
			return obj;
		}
	 
	 public List<String> getHSSFWorkbook(String dir,String nombGrupo,
			 								   List<Empresa> lstEmpresas,
			 								   List<SintesisEconomica> lstSintEconomica) throws Exception {
			
		// List<HSSFWorkbook> lista= new ArrayList<HSSFWorkbook>();
		 List<String> lista= new ArrayList<String>();		 
		 List<Map<String,Object>> lstNombresArchivos =buscarNombresArchivos(nombGrupo,lstEmpresas, lstSintEconomica);
		 		 
		// int n = Integer.parseInt(args[0]);   

		 for(Map<String,Object> mpNombArchivos:lstNombresArchivos){
			 String fileName=dir+File.separator+(String)mpNombArchivos.get("nombArchEmp")+".xls";
			 FileInputStream fis = null;
			 	try{
			 		fis = new FileInputStream(fileName);
			 	}catch(Exception e){
			 		fis = null;
			 		logger.info(StringUtil.getStackTrace(e));
			 	}
	           	if(fis!=null){
	           		HSSFWorkbook wbX = new HSSFWorkbook(fis);
	           		logger.info("Cantidad de libros ="+lista.size());
	           		lista.add(fileName); 
	           	}
		 }
		return  lista;
	}
	 
	//ini MCG20130306
	public List<FilaEmpresaHoja> getHSSFWorkbook2(String dir, String nombGrupo,
			List<Empresa> lstEmpresas, List<SintesisEconomica> lstSintEconomica)
			throws Exception {

		// List<HSSFWorkbook> lista= new ArrayList<HSSFWorkbook>();
		List<String> lista = new ArrayList<String>();
		List<FilaEmpresaHoja> listFilaEmpresaHoja = new ArrayList<FilaEmpresaHoja>();
		List<Map<String, Object>> lstNombresArchivos = buscarNombresArchivos(
				nombGrupo, lstEmpresas, lstSintEconomica);

		listFilaEmpresaHoja = buscarNombresArchivos2(nombGrupo, lstEmpresas,
				lstSintEconomica);
		// int n = Integer.parseInt(args[0]);

		for (FilaEmpresaHoja oempresaHoja : listFilaEmpresaHoja) {
			String fileName = dir + File.separator
					+  oempresaHoja.getNombreArchivoEEFF()+ ".xls";
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileName);
			} catch (Exception e) {
				fis = null;
				logger.info(StringUtil.getStackTrace(e));
			}
			if (fis != null) {
				HSSFWorkbook wbX = new HSSFWorkbook(fis);
				logger.info("Cantidad de libros =" + lista.size());
				lista.add(fileName);
			}
			oempresaHoja.setFileName(fileName);
		}
		return listFilaEmpresaHoja;
	}
	//fin MCG20130306 
	 
	 /**
	  * metodo para verificar si se logro cargar un sintesis enconomico para
	  * grupo.
	  * @param nombGrupo
	  * @param lstSintEconomica
	  * @return
	  */
	 private boolean tieneSintesisEconoDeGrupo( String nombGrupo,
			 									List<SintesisEconomica> lstSintEconomica){
		// Si es grupo
		if (programa.getTipoEmpresa().getId().toString().equalsIgnoreCase(
		    Constantes.ID_TIPO_EMPRESA_GRUPO.toString())) {
			for (SintesisEconomica se : lstSintEconomica) {
				if (nombGrupo.equalsIgnoreCase(se.getNombreEmpresa())) {
					return true;
				}
			}
		}
		return false;
	 }
	 
	 private  List<Map<String,Object>> buscarNombresArchivos(String nombGrupo,
			 												 List<Empresa> lstEmpresas,
			 												 List<SintesisEconomica> lstSintEconomica){
			
			List<Map<String,Object>> lstNombresArchivos = new ArrayList<Map<String,Object>>();
			
			List<Map<String,Object>> lstNombresArchivEmpPrinc = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lstNombresArchivEmpOtros = new ArrayList<Map<String,Object>>();			
			
			Map<String,Object> nombArchGrupo=null;
			Map<String,Object> nombArchEmpPrin  =null;
			Map<String,Object> nombArchEmp  =null;
			
			//Si es grupo
			if(!programa.getTipoEmpresa().getId().toString().equalsIgnoreCase(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				
				for(SintesisEconomica se:lstSintEconomica){
				     if(nombGrupo.equalsIgnoreCase(se.getNombreEmpresa())){
						nombArchGrupo	=	new HashMap<String,Object>();
						nombArchGrupo.put("nombArchEmp",String.valueOf(se.getId().longValue()));
						break;
					}
				}
			
			}
			
			for(Empresa emp:lstEmpresas){

				for(SintesisEconomica se:lstSintEconomica){
					
				  
					if(emp.getNombre().equalsIgnoreCase(se.getNombreEmpresa())){
						
							Tabla tg= emp.getTipoGrupo();
							if(tg!=null){
								if( tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_PRINCIPAL){
									
									nombArchEmpPrin=new HashMap<String,Object>();
									nombArchEmpPrin.put("nombArchEmp",String.valueOf(se.getId().longValue()));
									lstNombresArchivEmpPrinc.add(nombArchEmpPrin);
									
			
									
								}else if(tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_ANEXA ||
										 tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_SECUNDARIA){
									
									nombArchEmp	=	new HashMap<String,Object>();
									nombArchEmp.put("nombArchEmp",String.valueOf(se.getId().longValue()));
									lstNombresArchivEmpOtros.add(nombArchEmp);
				
									
								}									
								
							}else{
								nombArchEmp	=	new HashMap<String,Object>();
								nombArchEmp.put("nombArchEmp",String.valueOf(se.getId().longValue()));
								lstNombresArchivEmpPrinc.add(nombArchEmp);
								
					
							}
							

							
						break;
					}
				}

				nombArchEmp=null;
				nombArchEmpPrin=null;
			}
			
			if(nombArchGrupo!=null)  //Ponemos en la priemra ubicacion al nombre de grupo
				lstNombresArchivos.add(nombArchGrupo);
			
			if(lstNombresArchivEmpPrinc!=null && !lstNombresArchivEmpPrinc.isEmpty()) 
				lstNombresArchivos.addAll(lstNombresArchivEmpPrinc);
			
			if(lstNombresArchivEmpOtros!=null && !lstNombresArchivEmpOtros.isEmpty()) 
				lstNombresArchivos.addAll(lstNombresArchivEmpOtros);
			
			
		
			return lstNombresArchivos;
		}
	 
	 
	 //ini MCG20130306
	private List<FilaEmpresaHoja> buscarNombresArchivos2(String nombGrupo,List<Empresa> lstEmpresas,List<SintesisEconomica> lstSintEconomica) {

		List<Map<String, Object>> lstNombresArchivos = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> lstNombresArchivEmpPrinc = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lstNombresArchivEmpOtros = new ArrayList<Map<String, Object>>();
		List<FilaEmpresaHoja> listEmpresaHoja = new ArrayList<FilaEmpresaHoja>();

		Map<String, Object> nombArchGrupo = null;
		Map<String, Object> nombArchEmpPrin = null;
		Map<String, Object> nombArchEmp = null;

		// Si es grupo
		if (!programa.getTipoEmpresa().getId().toString().equalsIgnoreCase(
				Constantes.ID_TIPO_EMPRESA_EMPR.toString())) {

			for (SintesisEconomica se : lstSintEconomica) {
				if (nombGrupo.equalsIgnoreCase(se.getNombreEmpresa())) {
					nombArchGrupo = new HashMap<String, Object>();
					nombArchGrupo.put("nombArchEmp", String.valueOf(se.getId()
							.longValue()));
					//Para Grupo es tipoempresa =0 y posicion 0
					FilaEmpresaHoja oempresaHoja = new FilaEmpresaHoja();
					Empresa oempresa=new Empresa();
					oempresa.setCodigo("00000000");
					oempresa.setIndexHoja(0);
					oempresaHoja.setEmpresa(oempresa);					
					oempresaHoja.setPos(0);
					oempresaHoja.setTipoEmpresa(new Long(0));
					oempresaHoja.setNombreArchivoEEFF(String.valueOf(se
							.getId().longValue()));
					oempresaHoja.setCodEmpresaEEFF("00000000");
					oempresaHoja.setDatoAdicional(nombArchGrupo);
					listEmpresaHoja.add(oempresaHoja);	
					
					break;
				}
			}

		}

		for (Empresa emp : lstEmpresas) {

			for (SintesisEconomica se : lstSintEconomica) {

				if (emp.getNombre().equalsIgnoreCase(se.getNombreEmpresa())) {

					Tabla tg = emp.getTipoGrupo();
					if (tg != null) {
						if (tg.getId().longValue() == Constantes.ID_TIPO_EMPRESA_PRINCIPAL) {

							nombArchEmpPrin = new HashMap<String, Object>();
							nombArchEmpPrin.put("nombArchEmp", String
									.valueOf(se.getId().longValue()));
							lstNombresArchivEmpPrinc.add(nombArchEmpPrin);

							FilaEmpresaHoja oempresaHoja = new FilaEmpresaHoja();
							oempresaHoja.setEmpresa(emp);
							oempresaHoja.setPos(emp.getIndexHoja());
							oempresaHoja.setTipoEmpresa(tg.getId().longValue());
							oempresaHoja.setNombreArchivoEEFF(String.valueOf(se
									.getId().longValue()));
							oempresaHoja.setCodEmpresaEEFF(emp.getCodigo());
							oempresaHoja.setDatoAdicional(nombArchEmpPrin);
							listEmpresaHoja.add(oempresaHoja);

						} else if (tg.getId().longValue() == Constantes.ID_TIPO_EMPRESA_ANEXA
								|| tg.getId().longValue() == Constantes.ID_TIPO_EMPRESA_SECUNDARIA) {

							nombArchEmp = new HashMap<String, Object>();
							nombArchEmp.put("nombArchEmp", String.valueOf(se
									.getId().longValue()));
							lstNombresArchivEmpOtros.add(nombArchEmp);

							FilaEmpresaHoja oempresaHoja = new FilaEmpresaHoja();
							oempresaHoja.setEmpresa(emp);
							oempresaHoja.setTipoEmpresa(tg.getId().longValue());
							oempresaHoja.setPos(emp.getIndexHoja());
							oempresaHoja.setNombreArchivoEEFF(String.valueOf(se
									.getId().longValue()));
							oempresaHoja.setCodEmpresaEEFF(emp.getCodigo());
							oempresaHoja.setDatoAdicional(nombArchEmp);
							listEmpresaHoja.add(oempresaHoja);

						}

					} else {
						nombArchEmp = new HashMap<String, Object>();
						nombArchEmp.put("nombArchEmp", String.valueOf(se
								.getId().longValue()));
						lstNombresArchivEmpPrinc.add(nombArchEmp);

						FilaEmpresaHoja oempresaHoja = new FilaEmpresaHoja();
						oempresaHoja.setEmpresa(emp);
						//oempresaHoja.setTipoEmpresa(tg.getId().longValue());
						oempresaHoja.setTipoEmpresa(new Long(0));
						oempresaHoja.setPos(emp.getIndexHoja());
						oempresaHoja.setNombreArchivoEEFF(String.valueOf(se
								.getId().longValue()));
						oempresaHoja.setCodEmpresaEEFF(emp.getCodigo());
						oempresaHoja.setDatoAdicional(nombArchEmp);
						listEmpresaHoja.add(oempresaHoja);
					}

					break;
				}
			}

			nombArchEmp = null;
			nombArchEmpPrin = null;
		}

		if (nombArchGrupo != null) // Ponemos en la priemra ubicacion al nombre
									// de grupo
			lstNombresArchivos.add(nombArchGrupo);

		if (lstNombresArchivEmpPrinc != null
				&& !lstNombresArchivEmpPrinc.isEmpty())
			lstNombresArchivos.addAll(lstNombresArchivEmpPrinc);

		if (lstNombresArchivEmpOtros != null
				&& !lstNombresArchivEmpOtros.isEmpty())
			lstNombresArchivos.addAll(lstNombresArchivEmpOtros);
		
		return listEmpresaHoja;
	}

	 //fin MCG20130306
	 

}
