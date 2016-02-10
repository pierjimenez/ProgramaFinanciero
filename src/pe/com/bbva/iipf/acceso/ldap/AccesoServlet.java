package pe.com.bbva.iipf.acceso.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.stefanini.core.util.StringUtil;

//import pe.com.bbva.core.GenericService;
//import pe.com.bbva.core.util.StringUtil;
//import pe.com.bbva.model.seguridad.entity.Opcion;
//import pe.com.bbva.model.seguridad.entity.OpcionPerfil;
//import pe.com.bbva.model.seguridad.entity.Usuario;
//import pe.com.bbva.service.gestion.controller.BandejaController;
//import pe.com.bbva.service.seguridad.bo.PerfilBO;
//import pe.com.bbva.service.seguridad.controller.LoginController;
//import pe.com.bbva.service.seguridad.controller.MenuController;
//import pe.com.bbva.util.Constantes;

import com.grupobbva.bc.per.tele.ldap.comunes.IILDPeExcepcion;
import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;

/**
 * Servlet implementation class for Servlet: AccesoServlet
 * Valida el acceso del usuario con el LDAP del banco
 * Si el usuario es encontrado iniciara la aplicacion
 * @author epomayay
 */
 public class AccesoServlet extends HttpServlet implements Servlet {
	
	 Logger logger = Logger.getLogger(this.getClass());
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6283748594951454118L;
	
	
	//private GenericService genericService; 
	
	public AccesoServlet() {
		super();
	}   	
	
	/**
	 * Obtiene el contenido de una invocación 
	 * GET O POST
	 * @param request
	 * @param response
	 */
	public void execute(HttpServletRequest request, 
			  			HttpServletResponse response){
		logger.info("INICIO execute");
		logger.info("incio servlet");
		ServiciosSeguridadBbva ssBbva; 
		String codigoUsuario = ""; 
		IILDPeUsuario user = null;
		String forward = "";
		ApplicationContext context = null;
//		LoginController loginCll = null;
//		MenuController menuCll = null;
//		PerfilBO perfilBO = null;
//		BandejaController bandejaCll = null;
//		Usuario usuario  = null;
		try {
			try {
				String desa = request.getParameter("desa");
				if(desa!=null){
					logger.info("Obteniendo parametro de desarrollo");
					codigoUsuario = request.getParameter("codldap").toUpperCase();
				}else{
					logger.info("Conectandose al ldap");
					ssBbva = new ServiciosSeguridadBbva(request);
					if(ssBbva != null) {
						ssBbva.obtener_ID();
						codigoUsuario  =  ssBbva.getUsuario().toUpperCase();	
					} 
				}
				logger.info("USUARIO:"+codigoUsuario);
				user = IILDPeUsuario.recuperarUsuario(codigoUsuario);
				if(user != null){
					//genericService = new GenericService();
					context = WebApplicationContextUtils.getRequiredWebApplicationContext((getServletContext()));
//					loginCll = (LoginController)context.getBean("loginController");
//					menuCll = (MenuController)context.getBean("menuController");
//					perfilBO = (PerfilBO)context.getBean("perfilBO");
//					bandejaCll = (BandejaController)context.getBean("bandejaController");
//
//					loginCll.setCodigoRegistro(codigoUsuario);
//					logger.info("nombre="+user.getNombreCompleto());
//					logger.info("cargo="+user.getCargo().getCodigo());
//					logger.info("oficina="+user.getBancoOficina().getCodigo());
//					logger.info("user="+user);
//					loginCll.setCodigoCargo(user.getCargo().getCodigo());
//					loginCll.setCodigoOficina(user.getBancoOficina().getCodigo());
//					loginCll.setNombreCompleto(user.getNombreCompleto());
//
//					usuario = loginCll.login();
//					if(usuario != null){
//						List<OpcionPerfil> opcionesPerfil = perfilBO.findOpcionesPerfil(usuario.getPerfil().getId());
//						List<Opcion> opciones = new ArrayList<Opcion>();
//						for(OpcionPerfil op : opcionesPerfil){
//							opciones.add(op.getOpcion());
//						}
//						request.getSession(true).setAttribute(Constantes.SESSION_OPCIONES, 
//															  opciones);
//						request.getSession(true).setAttribute(Constantes.SESSION_USER, 
//															  usuario);
//						logger.info("perfil del usuario as = "+usuario.getPerfil());
//						menuCll.setNodoSeleccionado(Constantes.COD_OPCION_MENU_BANDEJA);
//						bandejaCll.setCodigoPerfilUsuario(usuario.getPerfil().getId().toString());
//						bandejaCll.loadListaExpediente(usuario);
//						//menuCll.loadMenu(usuario);
//						forward = "./index.jsp";	
//					}else{
//						logger.info("No se encontro del usuario en el Sistema");
//						forward = "./pages/error.jsp";
//					}
					forward = "./index.jsp";	
				}else{
					logger.info("No se encontro del usuario en el LDAP");
					forward = "./pages/error.jsp";
				}
	
			} catch (IILDPeExcepcion e) {
				logger.error(StringUtil.getStackTrace(e));
				forward = "./pages/error.jsp";
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				forward = "./pages/error.jsp";
			}

			response.sendRedirect(forward);
		} catch (Exception e) {
			logger.info("Error general");
			logger.error(StringUtil.getStackTrace(e));
		}
		logger.info("FIN execute");
	}
	
	protected void doGet(HttpServletRequest request, 
			 HttpServletResponse response) throws ServletException, IOException {
		execute(request,
				response);
	}  	
	
	protected void doPost(HttpServletRequest request, 
				  HttpServletResponse response) throws ServletException, IOException {
		execute(request,
				response);
	}   	 

}