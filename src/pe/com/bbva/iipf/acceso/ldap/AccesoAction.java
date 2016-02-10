package pe.com.bbva.iipf.acceso.ldap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.seguridad.bo.UsuarioPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.UsuarioPerfil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.util.Utilities;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;

import com.grupobbva.bc.per.tele.ldap.comunes.IILDPeExcepcion;
import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;

/**
 * 
 * @author epomayay
 *
 */
@Service("accesoAction")
@Scope("prototype")
public class AccesoAction extends GenericAction {
	
	public String codldap;
	public String desa;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(this.getClass());
		
		  
	  public String execute() throws Exception{
		    logger.info("INICIO execute");
		    logger.info("incio servletwwww");
		    ApplicationContext context = null;
			ParametroBO parametroBO = null;
			UsuarioPerfilBO usuarioPerfilBO = null;
			ServiciosSeguridadBbva ssBbva; 
			String codigoUsuario = ""; 
			IILDPeUsuario user = null;
			String forward = "";
		//FBoolean bRefTodoOk = new FBoolean(false) ;
			//FString sRefMensaje  = new FString("");
			//StringCollection sRefCampos = new StringCollection();
//			ApplicationContext context = null;
//			ParametroBO parametroBO = null;
			String strNivelAcceso=Constantes.NIVELACCESO_REGISTRO;
			UsuarioSesion usuarioSesion = new UsuarioSesion();
			try {
				
				try {
					//String desa = getDesa();
					String desa=getRequest().getParameter("desa");
					if(desa!=null){
						logger.info("Obteniendo parametro de desarrollo");
						//codigoUsuario = getCodldap().toUpperCase();
						codigoUsuario=getRequest().getParameter("codldap").toUpperCase();
					}else{
						logger.info("Conectandose al ldap");
						//request = ServletActionContext.getRequest();
						ssBbva = new ServiciosSeguridadBbva(getRequest());
						if(ssBbva != null) {
							ssBbva.obtener_ID();
							codigoUsuario  =  ssBbva.getUsuario().toUpperCase();	
						} 
					}
					logger.info("USUARIO:"+codigoUsuario);
					user = IILDPeUsuario.recuperarUsuario(codigoUsuario);
					if(user != null){
						usuarioSesion.setRegistroHost(codigoUsuario);						
						usuarioSesion.setCodigoCargo(user.getCargo().getCodigo());
						usuarioSesion.setCodigoOficina(user.getBancoOficina().getCodigo());
						usuarioSesion.setNombre(user.getDatosUsuarioCompleto());
						setObjectSession(Constantes.USUARIO_SESSIONLDAP, usuarioSesion);			
						context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
						usuarioPerfilBO = (UsuarioPerfilBO)context.getBean("usuarioPerfilBO");	
						UsuarioPerfil usuarioPerfil= new UsuarioPerfil();
						UsuarioPerfil usuarioPerfilRegistro = usuarioPerfilBO.obtienePorCodigo(codigoUsuario);
						if(usuarioPerfilRegistro == null){
							UsuarioPerfil usuarioPerfilCargo = usuarioPerfilBO.obtienePorCodigo(usuarioSesion.getCodigoCargo());
								if (usuarioPerfilCargo==null){							
									UsuarioPerfil usuarioPerfilOficina = usuarioPerfilBO.obtienePorCodigo(usuarioSesion.getCodigoOficina());
										if (usuarioPerfilOficina==null){
												usuarioPerfil=null;								
										}else{
											usuarioPerfil=usuarioPerfilOficina;
											strNivelAcceso=Constantes.NIVELACCESO_OFICINA;
										}							
								}else{
									usuarioPerfil=usuarioPerfilCargo;
									strNivelAcceso=Constantes.NIVELACCESO_CARGO;
								}						
						}else{
							usuarioPerfil=usuarioPerfilRegistro;
							strNivelAcceso=Constantes.NIVELACCESO_REGISTRO;
						}	
						//Valida si el usuario tiene un perfil creado.
						if(usuarioPerfil != null){
								parametroBO= (ParametroBO)context.getBean("paremetroBO");
								setObjectSession(Constantes.PARAMETROS_SESSION, 
												parametroBO.findParametros());		
								List<Opcion> opciones=new  ArrayList<Opcion>();
								if (strNivelAcceso.equals(Constantes.NIVELACCESO_REGISTRO)) {
										opciones = usuarioPerfilBO.listaOpcionesPermiso(usuarioSesion.getRegistroHost());
								}else if (strNivelAcceso.equals(Constantes.NIVELACCESO_CARGO)){
										opciones = usuarioPerfilBO.listaOpcionesPermiso(usuarioSesion.getCodigoCargo());									
								}else if (strNivelAcceso.equals(Constantes.NIVELACCESO_OFICINA)){
										opciones = usuarioPerfilBO.listaOpcionesPermiso(usuarioSesion.getCodigoOficina());								
								}else{
										opciones = usuarioPerfilBO.listaOpcionesPermiso(usuarioSesion.getRegistroHost());	
								}
								//fin mcg	
								setObjectSession(Constantes.TIEMPO_AUTOGUARDADO, getObjectParamtrosSession(Constantes.TIEMPO_AUTOGUARDADO).toString());
								setObjectSession(Constantes.TIEMPO_SESSION_TIMEOUT, getObjectParamtrosSession(Constantes.TIEMPO_SESSION_TIMEOUT).toString());
								setObjectSession(Constantes.PATH_URL_CERRARSESSION, getObjectParamtrosSession(Constantes.PATH_URL_CERRARSESSION).toString());
															
								setObjectSession(Constantes.PERMISO_OPCIONES_SESSION, opciones);
								
								List<Tabla> roles=new  ArrayList<Tabla>();
								roles=usuarioPerfilBO.listaRolesByPerfiles(usuarioSesion.getRegistroHost());
								setObjectSession(Constantes.ROLES_PERFIL_SESSION, roles);
								
								// ----------------			
								setObjectSession(Constantes.USUARIO_SESSION, usuarioSesion);
								Utilities.DIR_UPLOADFILE_URL_IMG = getObjectParamtrosSession(Constantes.DIR_UPLOADFILE_URL).toString();
//								setMaxInactiveInterval(Integer.parseInt(getObjectParamtrosSession(
//													   Constantes.MAX_TIME_SESSION).toString()));
								forward =  "panel";		
						}else{
								logger.info("El usuario no esta registrado en el sistema");
								forward =  "error";
						}    
					}else{
						logger.info("No se encontro del usuario en el LDAP");
						forward = "error";
					}		
				} catch (IILDPeExcepcion e) {
					logger.error(StringUtil.getStackTrace(e));
					forward = "error";
				} catch (Exception e) {
					logger.error(StringUtil.getStackTrace(e));
					forward = "error";
				}
			} catch (Exception e) {
				logger.info("Error general");
				logger.error(StringUtil.getStackTrace(e));
				forward = "error";
			}
			logger.info("FIN execute");
	    return forward;
	  }
	
		public String getCodldap() {
			return codldap;
		}
		public void setCodldap(String codldap) {
			this.codldap = codldap;
		}
		public String getDesa() {
			return desa;
		}
		public void setDesa(String desa) {
			this.desa = desa;
		}
}