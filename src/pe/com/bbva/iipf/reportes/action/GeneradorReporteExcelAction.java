package pe.com.bbva.iipf.reportes.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.reportes.bo.GeneradorReporteBO;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.StringUtil;

@Service("generadorReporteExcelAction")
@Scope("prototype")
public class GeneradorReporteExcelAction extends GenericAction {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	
	private InputStream inputStream;
	private Long idPrograma;
	private Programa programa;
	private Empresa empresaPrincipal = new Empresa();
    private List<Empresa> lstEmpresas = new ArrayList<Empresa> ();
	@Resource
    GeneradorReporteBO generadorReporteBO;
	@Resource
	private ProgramaBO programaBO ;
	@Resource
	private EmpresaBO empresaBO ;
	@Resource
	private SintesisEconomicoBO sintesisEconomicoBO;
	
	private List<Empresa> listEmpresaPropRiesg = new ArrayList<Empresa>();
	
	private TreeMap<String, HashMap<String, String>> listaImg = new TreeMap<String, HashMap<String, String>>();
	
    /**
     * Metodo que generara un reporte excel a partir de una platilla exisitente.
     * @return
     * @throws Exception 
     */
	public String generarExcel() 
	{
		
	  try {	
     	Map<String,Object> params=cargarParametros();
     	params.put("dir_temporal", getObjectParamtrosSession(Constantes.DIR_TEMPORAL));
     	//params.put("dir_temporal", "C:\\@everis\\ET_Duran\\mnt\\compartido\\profin\\export");
	    String dir=(String) getObjectParamtrosSession(Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO);
	    String filename 				= dir+File.separator+Constantes.FILE_REPO_XLS_05;
	    logger.info("directorio de plantilla dir 0: "+Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO);
	    logger.info("directorio de plantilla dir 1: "+dir);
	    logger.info("directorio de plantilla dir 2: "+filename);
	    int tamnio=lstEmpresas.size();
	    if(tamnio<5){
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
        List<SintesisEconomica> lstSintEconomica= sintesisEconomicoBO.findSintesisEconomicoSQL(String.valueOf(idPrograma));
        //directorio donde se exportara el archivo
        String dir_export=(String) getObjectParamtrosSession(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
        params.put("listaWbProg", getHSSFWorkbook(dir_export,programa.getNombreGrupoEmpresa(),lstEmpresas,lstSintEconomica));

        //ini MCG20111104        
        List<Map<String,Object>> lstCodigoEmpresaSEF =buscarCodigoEmpresaSEF(programa.getNombreGrupoEmpresa(),lstEmpresas, lstSintEconomica);
        params.put("listaCodigoEmpresaSEF",lstCodigoEmpresaSEF); 
        //fin MCG20111104
        
        
        generadorReporteBO.inicializar(params);
        //LLamamos a la clase que se encargara de llenar Data y refrescar las formulas de las plantillas.
        generadorReporteBO.buildReportExcelWithTemplate(wbPF);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        wbPF.write(bos);
	    inputStream = new ByteArrayInputStream(bos.toByteArray());

	  }catch (Exception e) {
		addActionError(e.getMessage());
		logger.error(StringUtil.getStackTrace(e));
	  }
		return "generarExcel";
	}
	
	public List<HSSFWorkbook> getHSSFWorkbook(String dir,String nombGrupo,List<Empresa> lstEmpresas,List<SintesisEconomica> lstSintEconomica) throws Exception {
		
		 List<HSSFWorkbook> lista= new ArrayList<HSSFWorkbook>();
		 
		 List<Map<String,Object>> lstNombresArchivos =buscarNombresArchivos(nombGrupo,lstEmpresas, lstSintEconomica);
		 
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
	           		lista.add(wbX);
	           	}
		 }
		return  lista;
	}
	
	private  List<Map<String,Object>> buscarNombresArchivos(String nombGrupo,List<Empresa> lstEmpresas,List<SintesisEconomica> lstSintEconomica){
		
		List<Map<String,Object>> lstNombresArchivos = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> lstNombresArchivEmpPrinc = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lstNombresArchivEmpOtros = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> nombArchGrupo=null;
		Map<String,Object> nombArchEmpPrin  =null;
		Map<String,Object> nombArchEmp  =null;
		
		//Si es grupo
		if(!programa.getTipoEmpresa().getId().toString().equalsIgnoreCase(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			
			for(SintesisEconomica se:lstSintEconomica){
			     if(nombGrupo.trim().equalsIgnoreCase(se.getNombreEmpresa().trim())){
					nombArchGrupo	=	new HashMap<String,Object>();
					nombArchGrupo.put("nombArchEmp",String.valueOf(se.getId().longValue()));
					break;
				}
			}
		
		}
		
		for(Empresa emp:lstEmpresas){

			for(SintesisEconomica se:lstSintEconomica){
				
			  
				if(emp.getNombre().trim().equalsIgnoreCase(se.getNombreEmpresa().trim())){
					
						Tabla tg= emp.getTipoGrupo();
						if(tg!=null){
							if( tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_PRINCIPAL){
								
								nombArchEmpPrin=new HashMap<String,Object>();
								nombArchEmpPrin.put("nombArchEmp",String.valueOf(se.getId().longValue()));
								lstNombresArchivEmpPrinc.add(nombArchEmpPrin);
								
							}else if(tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_ANEXA
											||tg.getId().longValue()==Constantes.ID_TIPO_EMPRESA_SECUNDARIA){
								
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
				
			  
				if(emp.getNombre().trim().equalsIgnoreCase(se.getNombreEmpresa().trim())){
					
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
	
	//fin MCG20111104
	
	 
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
	 
	 /**
	  * Metodo para alimentar a propuesta de Riesgo.
	  */
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
	 
	/**
	 * incluido para la nueva exportacion del excel
	 * @return
	 */
	public String descargarExcel2(){
		//System.out.println("Generando el Excel");
		return "descargarExcel2";
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
	
}
