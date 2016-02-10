package pe.com.bbva.iipf.acceso.host;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.model.Archivo;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.seguridad.bo.UsuarioPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.UsuarioPerfil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;


import com.stefanini.pe.framework.collections.StringCollection;
import com.stefanini.pe.framework.to.FBoolean;
import com.stefanini.pe.framework.to.FString;

import com.grupobbva.bc.per.tele.ldap.comunes.IILDPeExcepcion;
import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;

/**
 * Clase para realizar una conexion a HOST
 * busca el usuario lo valida y sube a session la información necesaria
 * de los perfiles y datos del usuario
 * @author EPOMAYAY
 *
 */
@Service("loginAction")
@Scope("prototype")
public class LoginAction extends GenericAction {

	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoUsuario;
	private String password;
	private String desa;
	

	/**
	 * Realiza una conexion con host y verifica que el usuario tenga acceso a host.
	 * esta consulta se realiza mediante un web Service
	 * @return
	 */
	public String login(){
		logger.info("El usuario con código "+codigoUsuario+" esta ingresando al sistema");
		String forward = "";
		FBoolean bRefTodoOk = new FBoolean(false) ;
		FString sRefMensaje  = new FString("");
		StringCollection sRefCampos = new StringCollection();
		UsuarioSesion oRefUsuarioSesion = new UsuarioSesion();
		ApplicationContext context = null;
		ParametroBO parametroBO = null;
		UsuarioPerfilBO usuarioPerfilBO = null;
		codigoUsuario = codigoUsuario.toUpperCase();
		
		//INI MCG
		String strNivelAcceso=Constantes.NIVELACCESO_REGISTRO;
		UsuarioSesion ousuarioSesion = new UsuarioSesion();
		try {
	
				if(super.getObjectSession(Constantes.USUARIO_SESSIONLDAP) != null){
				ousuarioSesion = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSIONLDAP);
				}else{
					ousuarioSesion=null;
				}			
			if(ousuarioSesion != null){
		
		//FIN MCG
		
				try {					
					context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
					usuarioPerfilBO = (UsuarioPerfilBO)context.getBean("usuarioPerfilBO");	
					UsuarioPerfil usuarioPerfil= new UsuarioPerfil();
					UsuarioPerfil usuarioPerfilRegistro = usuarioPerfilBO.obtienePorCodigo(codigoUsuario);
					if(usuarioPerfilRegistro == null){
						UsuarioPerfil usuarioPerfilCargo = usuarioPerfilBO.obtienePorCodigo(ousuarioSesion.getCodigoCargo());
						if (usuarioPerfilCargo==null){							
							UsuarioPerfil usuarioPerfilOficina = usuarioPerfilBO.obtienePorCodigo(ousuarioSesion.getCodigoOficina());
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
						QueryWS.LoginFeloCopy00(getRequest(), 
												getResponse(), 
												codigoUsuario, 
												password, 
												bRefTodoOk, 
												sRefMensaje, 
												sRefCampos, 
												oRefUsuarioSesion);
						if(bRefTodoOk.getValue()){
							
							parametroBO= (ParametroBO)context.getBean("paremetroBO");
							setObjectSession(Constantes.PARAMETROS_SESSION, 
										     parametroBO.findParametros());
							
							
							// --- HARDCODE ---
							
							oRefUsuarioSesion.setRegistroHost(codigoUsuario);
							//ini mcg
							oRefUsuarioSesion.setCodigoCargo(ousuarioSesion.getCodigoCargo());
							oRefUsuarioSesion.setCodigoOficina(ousuarioSesion.getCodigoOficina());
							oRefUsuarioSesion.setNombre(ousuarioSesion.getNombre());
							
							List<Opcion> opciones=new  ArrayList<Opcion>();
							if (strNivelAcceso.equals(Constantes.NIVELACCESO_REGISTRO)) {
								opciones = usuarioPerfilBO.listaOpcionesPermiso(oRefUsuarioSesion.getRegistroHost());
							}else if (strNivelAcceso.equals(Constantes.NIVELACCESO_CARGO)){
								opciones = usuarioPerfilBO.listaOpcionesPermiso(ousuarioSesion.getCodigoCargo());									
							}else if (strNivelAcceso.equals(Constantes.NIVELACCESO_OFICINA)){
								opciones = usuarioPerfilBO.listaOpcionesPermiso(ousuarioSesion.getCodigoOficina());								
							}else{
								opciones = usuarioPerfilBO.listaOpcionesPermiso(oRefUsuarioSesion.getRegistroHost());	
							}
							//fin mcg
							
							setObjectSession(Constantes.PERMISO_OPCIONES_SESSION, opciones);
							// ----------------
							
							setObjectSession(Constantes.USUARIO_SESSION, oRefUsuarioSesion);
							forward =  "panel";
						}else{
							addActionError(sRefMensaje.getValue());
							forward =  "login";
							logger.error(sRefMensaje.getValue());
						}
					}else{
						addActionError("Usted no tiene acceso al sistema solicite un acceso al Administrador del sistema.");
						forward =  "login";
						logger.error("Usted no tiene acceso al sistema solicite un acceso al Administrador del sistema.");
					}
				} catch (WSException e) {
					addActionError(e.getMessage());
					forward =  "login";
					logger.error(StringUtil.getStackTrace(e));			
		
			//INI MCG					
				}	
			}else{				
				//addActionError("Usted no tiene acceso al sistema");
				//forward = "login";
				//logger.info("Usted no tiene acceso al sistema");
				logger.error("la Session a caducado");
				forward = "error";
			}
//			
//		} catch (IILDPeExcepcion e) {
//			addActionError(e.getMessage());
//			forward =  "login";
//			logger.error(StringUtil.getStackTrace(e));			
					
					
		//FIN MCG			
			
			
		}catch (BOException e) {
			addActionError(e.getMessage());
			forward =  "login";
			logger.error(StringUtil.getStackTrace(e));
		}catch (Exception e) {
			addActionError(e.getMessage());
			forward =  "login";
			logger.error(StringUtil.getStackTrace(e));
		}
		return forward;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDesa() {
		return desa;
	}

	public void setDesa(String desa) {
		this.desa = desa;
	}

	
	
}
