package pe.com.bbva.iipf.acceso.ldap;

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
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;

import com.grupobbva.bc.per.tele.ldap.comunes.IILDPeExcepcion;
import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.opensymphony.xwork2.ActionSupport;


public class AccesoAction__ extends GenericAction {
	
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
			ServiciosSeguridadBbva ssBbva; 
			String codigoUsuario = ""; 
			IILDPeUsuario user = null;
			String forward = "";
//			ApplicationContext context = null;
//			ParametroBO parametroBO = null;
			
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
					codldap=codigoUsuario;
					user = IILDPeUsuario.recuperarUsuario(codigoUsuario);
					if(user != null){
//						//genericService = new GenericService();
//						context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
//						parametroBO= (ParametroBO)context.getBean("paremetroBO");
//						
//						
						usuarioSesion.setRegistroHost(codigoUsuario);						
						usuarioSesion.setCodigoCargo(user.getCargo().getCodigo());
						usuarioSesion.setCodigoOficina(user.getBancoOficina().getCodigo());
						usuarioSesion.setNombre(user.getDatosUsuarioCompleto());
						    setObjectSession(Constantes.USUARIO_SESSIONLDAP, usuarioSesion);
						forward = "login";	
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