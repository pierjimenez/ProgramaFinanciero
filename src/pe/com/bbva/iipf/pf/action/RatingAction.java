package pe.com.bbva.iipf.pf.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RatingBlobBO;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.RatingBlob;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("ratingAction")
@Scope("prototype") 
public class RatingAction extends GenericAction {

	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private RatingBO ratingBO;
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private AnexoBO anexoBO;
	
	private List<Rating> listaRating = new ArrayList<Rating>(); 
	
	private String mensajeWS="";
	
	private String flagChangeEmpresa="N";
    private String codempresagrupo="0";
    List<Empresa> listaEmpresasGrupo = new ArrayList<Empresa>();


	private String codigoEmpresa="";
	
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
	
	@Resource
	private RatingBlobBO ratingBlobBO;
    
	/**
	 * Metodo para inicializar la lista del rating
	 * @return
	 */
	public String init(){
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			String idEmpresa = (String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			List<Empresa> listaempresaCompleto=new ArrayList<Empresa> ();
			TmanagerLog oLog=new TmanagerLog();
			TmanagerLog oLogRating=new TmanagerLog();
			Programa programa = new Programa();
			programa=programaBO.findById(Long.valueOf(idprograma));
			programa.setIdEmpresa(idEmpresa);
			programa.setId(Long.valueOf(idprograma));
			ratingBO.setPrograma(programa);
			setMensajeWS("");
			//ini MCG20130319
			//listaRating = ratingBO.findRating();	
			String tipoempresa=programa.getTipoEmpresa().getId().toString();
			String codEmpresaGrupo="";
			String flagchange=getFlagChangeEmpresa();
			logger.info("flagchange: "+flagchange);
			boolean flagCallWS=false;
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					codEmpresaGrupo=programa.getIdEmpresa().toString();	
					flagCallWS=true;
			}else{									
					if (flagchange!=null && flagchange.equals("C")){
						listaRating = new ArrayList<Rating>(); 	
						codEmpresaGrupo=getCodempresagrupo();
						flagCallWS=false;		
					}else{
						codEmpresaGrupo=programa.getIdGrupo().toString(); //getPrograma().getIdGrupo().toString();
						flagCallWS=true;
					}							
					
			}
			//ini MCG20140905
			try {
				if (false){
					ratingBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
//					ratingBO.setPathWebService(pathWebService);
//					ratingBO.setPathWebServicePEC6(pathWebServicePEC6);
					
					programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
					programaBO.setPathWebService(pathWebService);
					programaBO.setPathWebServicePEC6(pathWebServicePEC6);
					if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
						listaempresaCompleto =programaBO.getEmpresaGrupoWS(programa.getIdGrupo().toString(),new Long(idprograma),oLog);
						if (oLog.getCodigo()!=null && oLog.getCodigo().equals(Constantes.COD_ERROR_WS_GETEMPRESA)){
							String msnerror=oLog.getEmessage()==null?"":oLog.getEmessage();
							setMensajeWS("* "+msnerror+ "::"+Constantes.MSN_WS_GETEMPRESA);		
						}else{setMensajeWS("");	}
					}
					ratingBO.updateRatingWS(programa,true,listaempresaCompleto,oLogRating);
					
				}
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}
			//fin MCG20140905
			
			listaRating = ratingBO.findRating(programa.getId(),codEmpresaGrupo);			
			//fin MCG20130319		
			
			
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		completarListaGrupoEmpresas();
		return "rating";
	}
	
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
	 * Acatualiza el rating de la empresa con la ultima informacion
	 * registrada en la tabla de Rating
	 * @return
	 */
	public String actualizarRating(){
		String forward = "rating";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			String idEmpresa = (String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			String pathWebServicePEC6 = getObjectParamtrosSession(Constantes.COD_SPFI_PEC6SOAP_HTTP_PORT_ADDRESS).toString();
			List<Empresa> listaempresaCompleto=new ArrayList<Empresa> ();
			TmanagerLog oLog=new TmanagerLog();
			TmanagerLog oLogRating=new TmanagerLog();
			Programa programa = new Programa();
			programa=programaBO.findById(Long.valueOf(idprograma));
//			programa.setIdEmpresa(idEmpresa);
//			programa.setId(Long.valueOf(idprograma));
			ratingBO.setPrograma(programa);
			setMensajeWS("");
			
			//ini MCG20130321
			//listaRating = ratingBO.actualizarRating();
			String tipoempresa=programa.getTipoEmpresa().getId().toString();
			String codEmpresaGrupo="";
			String flagchange=getFlagChangeEmpresa();
			logger.info("flagchange: "+flagchange);
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					codEmpresaGrupo=programa.getIdEmpresa().toString();					
			}else{			
					codEmpresaGrupo=getCodigoEmpresa();		
			}
			ratingBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			//ratingBO.setPathWebService(pathWebService);
			//ratingBO.setPathWebServicePEC6(pathWebServicePEC6);
			try {
				programaBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
				programaBO.setPathWebService(pathWebService);
				programaBO.setPathWebServicePEC6(pathWebServicePEC6);
				if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					listaempresaCompleto =programaBO.getEmpresaGrupoWS(programa.getIdGrupo().toString(),new Long(idprograma),oLog);
					if (oLog.getCodigo()!=null && oLog.getCodigo().equals(Constantes.COD_ERROR_WS_GETEMPRESA)){
						String msnerror=oLog.getEmessage()==null?"":oLog.getEmessage();
						setMensajeWS("* "+msnerror+ "::"+Constantes.MSN_WS_GETEMPRESA);	
					}else{setMensajeWS("");	}
				}
				ratingBO.updateRatingWS(programa,true,listaempresaCompleto,oLogRating);
				
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}		
			
			listaRating = ratingBO.actualizarRating(codEmpresaGrupo);
			//fin MCG20130321
			
			actualizarRatingAnexo(listaRating,programa,codEmpresaGrupo);
			
			addActionMessage("Rating Actualizado Correctamente.");
		} catch (BOException e) {
			forward = "rating";
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		completarListaGrupoEmpresas();
		return forward;
	}
	
		
	//ini MCG20130709
	public void actualizarRatingAnexo(List<Rating> olistaRating,Programa oprograma, String strcodempresa){
		try {
			List<FilaAnexo> olistaFilaAnexos = new ArrayList<FilaAnexo>();
			olistaFilaAnexos = anexoBO.findAnexos();			
			if(olistaFilaAnexos !=null && olistaFilaAnexos.size()>0){
				Empresa oempresa= new Empresa();	
				oempresa=programaBO.findEmpresaByIdEmpresaPrograma(oprograma.getId(),strcodempresa);
				if (oempresa!=null){	
					
					String ofecharating ="";
					String orating="";
					//String orating= ratingBO.ObtenerRating(oprograma,strcodempresa);
					try {
						 Map<String,Object> oratingFecha= ratingBO.ObtenerRatingConFecha(oprograma,strcodempresa);
						 orating = oratingFecha.get("mratingAnexo")==null?"":oratingFecha.get("mratingAnexo").toString();
						 ofecharating=oratingFecha.get("mfechagAnexo")==null?"":oratingFecha.get("mfechagAnexo").toString();	
							
					} catch (Exception e) {
						 
					}
					
					for(FilaAnexo filaAnexo : olistaFilaAnexos){											
						if (filaAnexo.getAnexo().getTipoFila().equals(2) && 
								filaAnexo.getAnexo().getDescripcion().trim().equals(oempresa.getNombre().trim())) {
							
							Long idAnexo=filaAnexo.getAnexo().getId();
							try {
								anexoBO.actualizarAnexoRatingpf(idAnexo,orating,ofecharating);
							} catch (Exception e) {
								 
							}							
						}
					}				
				}
			}
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	//fin MCg20130709
	
	
	//ini MCG20130325
	public void saveRatingBlob(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		logger.info("codEmpresa="+codEmpresa);
		
		try {
			RatingBlob seb = new RatingBlob();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));			
			ratingBlobBO.setPrograma(programa);
			ratingBlobBO.setCampoBlob(campoBlob);
			ratingBlobBO.setValorBlob(valorBlob);
			ratingBlobBO.setCodEmpresa(codEmpresa);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			ratingBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				ratingBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				ratingBlobBO.setPatronesEditor("");
			}
			ratingBlobBO.save(seb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Busca un campo blob dentro del programa
	 * la respuesta es procesada via ajax
	 */
	public void consultarRatingBlob(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));				
			RatingBlob ratingBlob = ratingBlobBO.findRatingBlobByPrograma(programa,codEmpresa);
			if(ratingBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	
			  //rating	 	        
	 	       
	 	      if(campoBlob.equals("valoracionRating") &&
	 	    		 ratingBlob.getValoracionRating()!=null){
	 		        for(byte x :ratingBlob.getValoracionRating() ){
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
	
	//fin MCG20130325
	
	public RatingBO getRatingBO() {
		return ratingBO;
	}

	public void setRatingBO(RatingBO ratingBO) {
		this.ratingBO = ratingBO;
	}

	public List<Rating> getListaRating() {
		return listaRating;
	}

	public void setListaRating(List<Rating> listaRating) {
		this.listaRating = listaRating;
	}

	public String getFlagChangeEmpresa() {
		return flagChangeEmpresa;
	}

	public void setFlagChangeEmpresa(String flagChangeEmpresa) {
		this.flagChangeEmpresa = flagChangeEmpresa;
	}

	public String getCodempresagrupo() {
		return codempresagrupo;
	}

	public void setCodempresagrupo(String codempresagrupo) {
		this.codempresagrupo = codempresagrupo;
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

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
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

	public String getMensajeWS() {
		return mensajeWS;
	}

	public void setMensajeWS(String mensajeWS) {
		this.mensajeWS = mensajeWS;
	}
	
	
	
	
}
