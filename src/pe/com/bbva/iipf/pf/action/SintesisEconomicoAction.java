package pe.com.bbva.iipf.pf.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomica;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("sintesisEconoAction")
@Scope("prototype")
public class SintesisEconomicoAction extends GenericAction {

	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Resource
	private SintesisEconomicoBO sintesisEconomicoBO;
	
	@Resource
	private SintesisEconomicoBlobBO  sintesisEconomicoBlobBO;
	
	@Resource
	private ProgramaBO  programaBO;	
	
	private File archivoExcel;
	private String codigoArchivo;
	private String extension;
	private String nombreArchivo;
	private String contentType;
	private String contentDisposition;
	private List<SintesisEconomica> listaSintesis = new ArrayList<SintesisEconomica>();
	List<Empresa> listaEmpresasGrupo = new ArrayList<Empresa>();
	private String codigoEmpresa;
	private InputStream fileInputStream;

	
	/**
	 * variable para pasar el nombre del campo blob a registrarse
	 */
	private String campoBlob;
	/**
	 * variable que contendra el valor de campo blob
	 */
	private String valorBlob;

	/**
	 * variable que contendra el valor de campo codigoEmpresa
	 */
	private String codEmpresa;

	/**
	 * Metodo para iniciar Sintesis Economico
	 * @return
	 */
	public String init(){
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			Programa programa = new Programa();
			programa.setId(Long.valueOf(idprograma));
			sintesisEconomicoBO.setPrograma(programa);
			setListaSintesis(sintesisEconomicoBO.listarSintesisEconomico());
			completarListaGrupoEmpresas();
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "sintesisEconomico";
	}
	
	/**
	 * Se agrega el grupo a la lista de empresas que pertencen al grupo.
	 * 
	 */
	public void completarListaGrupoEmpresas(){
		String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			List<Empresa> lista = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
			Empresa grupo = new Empresa();
			grupo.setCodigo(getObjectSession(Constantes.COD_GRUPO_SESSION).toString());
			grupo.setNombre(getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString());
			listaEmpresasGrupo.add(grupo);
			for(Empresa emp :lista ){
				listaEmpresasGrupo.add(emp);
			}
		}
		
	}

	/**
	 * Guarda el archivo Excel 
	 * @return
	 */
	public String saveExcel(){
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			Programa programa = new Programa();
			programa.setId(Long.valueOf(idprograma));
			String nombreEmpresaGrupo = getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
			String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				List<Empresa> listaEmpresasGrupo = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
				for(Empresa empresa :listaEmpresasGrupo){
					if(empresa.getCodigo().equals(codigoEmpresa)){
						nombreEmpresaGrupo = empresa.getNombre().trim();
						break;
					}
				}
			}
			sintesisEconomicoBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			sintesisEconomicoBO.setPrograma(programa);
			sintesisEconomicoBO.setFileExcel(archivoExcel);
			sintesisEconomicoBO.setNombreEmpresaGrupo(nombreEmpresaGrupo);
			sintesisEconomicoBO.save();
			setListaSintesis(sintesisEconomicoBO.listarSintesisEconomico());
			addActionMessage("Registrado Correctamente.");
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}catch(Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		completarListaGrupoEmpresas();
		return "sintesisEconomico";
	}
	
	/**
	 * Este metodo sera invocado al momento de descargar los
	 * archivos excel
	 * @return
	 * @throws Exception
	 */
	 public String descargarSinstesisEconomico() throws Exception { 	
		logger.info("INICIO download");
		String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
		Programa programa = new Programa();
		programa.setId(Long.valueOf(idprograma));
		
		String codigoDocumento=getCodigoArchivo();
		String extencionDocumento= getExtension();
		String nombreDocumento=this.getNombreArchivo();
		String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
		File file=null;
		String forward = SUCCESS;
		try {
			String dir  = getObjectParamtrosSession(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO).toString();
			String pathToFile=dir+File.separator+nombreArchivo;
			String fileName=nombreDocumento; 
			file = new File(pathToFile);
		   	setContentType(new MimetypesFileTypeMap().getContentType(file));
		   	setContentDisposition("attachment;filename=\""+ fileName + "\"");
			fileInputStream =  new FileInputStream(file);
		}catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "sintesisEconomico";
		}
		sintesisEconomicoBO.setPrograma(programa);
		setListaSintesis(sintesisEconomicoBO.listarSintesisEconomico());
		completarListaGrupoEmpresas();
		return forward;
	}
	 
	 /**
	 * Este metodo sera invocado al momento de descargar los
	 * archivos excel
	 * @return
	 * @throws Exception
	 */
	public String completarPlantilla() throws Exception { 	
			logger.info("INICIO download");
			//FileInputStream fis = null;
			String path = "";
			String dir = "";
			String fileName = ""; 
			String forward = SUCCESS;
			try {
				String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);				
				String idEmpresa = (String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
				String nombreGrupoEmpresa = (String)getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION);
				String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
				
				Programa programa = new Programa();
				programa=programaBO.findById(Long.valueOf(idprograma));
				programa.setId(Long.valueOf(idprograma));
				programa.setIdEmpresa(idEmpresa);
				
				programa.setNombreGrupoEmpresa(nombreGrupoEmpresa);
				sintesisEconomicoBO.setPrograma(programa);
				dir  = getObjectParamtrosSession(Constantes.DIR_PLANTILLA_EXCEL_SINTESIS_ECONOMICO).toString();
				String dirTemporal = getObjectParamtrosSession(Constantes.DIR_TEMPORAL).toString();
							
				String nombreEmpresa = "";
				nombreEmpresa = nombreGrupoEmpresa.trim();
				logger.info("codigo central="+codigoEmpresa);
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					codigoEmpresa = getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
				}else{
					List<Empresa> listaEmpresasGrupo = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
					for(Empresa empresa :listaEmpresasGrupo){
						if(empresa.getCodigo().equals(codigoEmpresa)){
							nombreEmpresa = empresa.getNombre().trim();
							break;
						}
					}
				}
				fileName =  nombreEmpresa.trim() +
							FechaUtil.formatFecha(new Date(), 
							FechaUtil.YYYYMMDD_HHMMSS)+".xls";
				
				path = sintesisEconomicoBO.llenarPlantillaExcel(dir, 
															    Constantes.NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO,
															    dirTemporal,
															    codigoEmpresa,
															    nombreEmpresa);
				fileInputStream = new FileInputStream(path);
				setContentType("application/ms-excel");
			   	setContentDisposition("attachment;filename=\""+ fileName + "\"");
			}catch (BOException e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				//path = dir+File.separator+ Constantes.NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO;
				forward = "sintesisEconomico";
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				//path = dir+File.separator+ Constantes.NOMBRE_PLANTILLA_EXCEL_SINTESIS_ECONOMICO;
				forward="sintesisEconomico";
			}
			setListaSintesis(sintesisEconomicoBO.listarSintesisEconomico());
			completarListaGrupoEmpresas();
		   	//Eliminar los archivos.
			return forward;
	}
		 
	public String eliminar(){
		logger.info("INICIO download");
		String codigoDocumento=getCodigoArchivo();
		String extencionDocumento= getExtension();
		String nombreDocumento=this.getNombreArchivo();
		String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
		File file=null;
		String forward = "sintesisEconomico";
		try {
			SintesisEconomica sintesisEconomica = new SintesisEconomica();
			sintesisEconomica.setId(Long.parseLong(codigoDocumento));
			sintesisEconomicoBO.delete(sintesisEconomica);
			String dir  = getObjectParamtrosSession(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO).toString();
			String pathToFile=dir+File.separator + nombreArchivo;
			file = new File(pathToFile);
			if (file!=null && file.exists()){
		    file.delete();
			}
		    String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			Programa programa = new Programa();
			programa.setId(Long.valueOf(idprograma));
			sintesisEconomicoBO.setPrograma(programa);
			setListaSintesis(sintesisEconomicoBO.listarSintesisEconomico());
			completarListaGrupoEmpresas();
			addActionMessage("Eliminado Correctamente");
		}catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "sintesisEconomico";
		}finally{
			
		}
		return forward;
	}
	
	//ini MCG20111031
	public void saveSintesisEconBlob(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		
		try {
			SintesisEconomicoBlob seb = new SintesisEconomicoBlob();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));			
			sintesisEconomicoBlobBO.setPrograma(programa);
			sintesisEconomicoBlobBO.setCampoBlob(campoBlob);
			sintesisEconomicoBlobBO.setValorBlob(valorBlob);
			sintesisEconomicoBlobBO.setCodEmpresa(codEmpresa);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			sintesisEconomicoBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				sintesisEconomicoBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				sintesisEconomicoBlobBO.setPatronesEditor("");
			}
			
			sintesisEconomicoBlobBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			sintesisEconomicoBlobBO.save(seb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Busca un campo blob dentro del programa
	 * la respuesta es procesada via ajax
	 */
	public void consultarSintesisEconBlob(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));				
			SintesisEconomicoBlob sintesisEconomicoBlob = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(programa,codEmpresa);
			if(sintesisEconomicoBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	
			  //Sintesis economico	 	        
	 	       
	 	      if(campoBlob.equals("comenSituFinanciera") &&
	 	    		 sintesisEconomicoBlob.getComenSituFinanciera()!=null){
	 		        for(byte x :sintesisEconomicoBlob.getComenSituFinanciera() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("comenSituEconomica") &&
	 	    		 sintesisEconomicoBlob.getComenSituEconomica()!=null){
	 		        for(byte x :sintesisEconomicoBlob.getComenSituEconomica() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("valoracionEconFinanciera") &&
	 	    		 sintesisEconomicoBlob.getValoracionEconFinanciera()!=null){
	 		        for(byte x :sintesisEconomicoBlob.getValoracionEconFinanciera() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("valoracionPosiBalance") &&
	 	    		 sintesisEconomicoBlob.getValoracionPosiBalance()!=null){
	 		        for(byte x :sintesisEconomicoBlob.getValoracionPosiBalance() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      
	 	      logger.info("html blob = "+stb);
	            out.print(stb.toString());
			}
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//fin MCG20111031
	
	
	
	public File getArchivoExcel() {
		return archivoExcel;
	}

	public void setArchivoExcel(File archivoExcel) {
		this.archivoExcel = archivoExcel;
	}

	public SintesisEconomicoBO getSintesisEconomicoBO() {
		return sintesisEconomicoBO;
	}

	public void setSintesisEconomicoBO(SintesisEconomicoBO sintesisEconomicoBO) {
		this.sintesisEconomicoBO = sintesisEconomicoBO;
	}

	public List<SintesisEconomica> getListaSintesis() {
		return listaSintesis;
	}

	public void setListaSintesis(List<SintesisEconomica> listaSintesis) {
		this.listaSintesis = listaSintesis;
	}

	public String getCodigoArchivo() {
		return codigoArchivo;
	}

	public void setCodigoArchivo(String codigoArchivo) {
		this.codigoArchivo = codigoArchivo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
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

	public List<Empresa> getListaEmpresasGrupo() {
		return listaEmpresasGrupo;
	}

	public void setListaEmpresasGrupo(List<Empresa> listaEmpresasGrupo) {
		this.listaEmpresasGrupo = listaEmpresasGrupo;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getCampoBlob() {
		return campoBlob;
	}

	public void setCampoBlob(String campoBlob) {
		this.campoBlob = campoBlob;
	}

	public String getValorBlob() {
		return valorBlob;
	}

	public void setValorBlob(String valorBlob) {
		this.valorBlob = valorBlob;
	}

	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public SintesisEconomicoBlobBO getSintesisEconomicoBlobBO() {
		return sintesisEconomicoBlobBO;
	}

	public void setSintesisEconomicoBlobBO(
			SintesisEconomicoBlobBO sintesisEconomicoBlobBO) {
		this.sintesisEconomicoBlobBO = sintesisEconomicoBlobBO;
	}
	
	
	
}
