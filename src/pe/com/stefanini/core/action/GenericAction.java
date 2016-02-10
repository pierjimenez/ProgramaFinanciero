package pe.com.stefanini.core.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.Grid;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GenericAction extends ActionSupport {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private String scrollX ;
	private String scrollY;
	
	//Variables usadas para la generacion de las grillas con jquery
	private int page;
	private int rows;
	private int records;
	private String sord ;
	private String sidx;
	private Grid<?> grid;
	
	/**
	 * Metodo para obtener un bean de spring
	 * @param name
	 * @return
	 */
	public static Object getSpringBean(String name){
		WebApplicationContext context =
			WebApplicationContextUtils.getRequiredWebApplicationContext(
                                    ServletActionContext.getServletContext()
                        );
		return context.getBean(name);
	}
	
	/**
	 * Metodo para obtener datos de session
	 * @param nombre
	 * @return
	 */
	public static Object getObjectSession(String nombre){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		Object  obj = sessionparam.get(nombre);
		return obj;
	}
	
	/**
	 * Metodos para enviar datos a session
	 * @param nombre
	 * @param valor
	 */
	public static void  setObjectSession(String nombre, Object valor){
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		sessionparam.put(nombre, valor);
	}
	
	/**
	 * Método para obtener algun valor de los parametros subidos a 
	 * session
	 * @param codigoParametro
	 * @return
	 */
	public static Object getObjectParamtrosSession(String codigoParametro){
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
	
	public static Object getObjectRolSession(String codigoRol){
	
		String activo= "0";
		try {
			List<Tabla> listaRoles = (List<Tabla>) getObjectSession(Constantes.ROLES_PERFIL_SESSION);
			
			for(Tabla rol : listaRoles){
				if(rol.getCodigo().equals(codigoRol) ){
					activo = "1";
					break;
				}
			}
			
		} catch (Exception e) {
			activo = "0";
		}
		return activo;
		
		
		
	}
	
	/**
	 * Metodo para definir el tiempo máximo de duración de la session
	 * @param time
	 */
	public void setMaxInactiveInterval(int time){
		getRequest().getSession().setMaxInactiveInterval(time);
	}
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	public ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
	
	public HttpSession getSession(boolean create){
		return getRequest().getSession(create);
	}

	public String getScrollX() {
		return scrollX;
	}

	public void setScrollX(String scrollX) {
		this.scrollX = scrollX;
	}

	public String getScrollY() {
		return scrollY;
	}

	public void setScrollY(String scrollY) {
		this.scrollY = scrollY;
	}
	
	/**
	 * Asigna los valores de auditoria como fecha de creacion, usuario creador
	 * fecha de modificacion y usuario modificador dependiendo si el objeto
	 * tiene o no asignana una id
	 * para poder usar este metodo los objetos tiene que eredar de EntidadBase
	 * 
	 * @param obj
	 * @param usuarioSession
	 */
	public void setCamposAuditoria(Object obj,UsuarioSesion usuarioSession){
		Method method = null;
		Object id = null;
	
			try {
				method = obj.getClass().getMethod("getId", null);
				id = method.invoke(obj, null);
				if(id==null){
					BeanUtils.setProperty(obj,
							  			  "codUsuarioCreacion", 
							  			  usuarioSession.getRegistroHost());
					BeanUtils.setProperty(obj,
							  			  "fechaCreacion", 
							  			  FechaUtil.getFechaActualDate());
					BeanUtils.setProperty(obj,
							  			  "fechaModificacion", 
							  			  FechaUtil.getFechaActualDate());
				}else{
					BeanUtils.setProperty(obj,
							  			  "codUsuarioModificacion", 
							  			  usuarioSession.getRegistroHost());
					BeanUtils.setProperty(obj,
							  			  "fechaModificacion", 
							  			  FechaUtil.getFechaActualDate());
				}
			} catch (SecurityException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (NoSuchMethodException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (IllegalAccessException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (InvocationTargetException e) {
				logger.info(StringUtil.getStackTrace(e));
			} catch (Exception e) {
				logger.info(StringUtil.getStackTrace(e));
			}
			
	}
	
	public ConfiguracionWS getConfiguracionWS(){
		ConfiguracionWS configuracionWS=new ConfiguracionWS();
		String terminal=(String)getObjectParamtrosSession(Constantes.TERMINAL_CONTABLE);
		String logico=(String)getObjectParamtrosSession(Constantes.TERMINAL_LOGICO);
		String aplicaicon=(String)getObjectParamtrosSession(Constantes.CODIGO_APLICACION);
		UsuarioSesion user=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
		
		
		configuracionWS.setCodigoEjecutivoCuenta((String)getObjectParamtrosSession(Constantes.CODIGO_EJECUTIVO_CUENTA));
		configuracionWS.setCodigoOficinaCuenta((String)getObjectParamtrosSession(Constantes.CODIGO_OFICINA_CUENTA));		
		configuracionWS.setCodigoUsuario(user.getRegistroHost());
		//configuracionWS.setCodigoUsuario("P016653");
		configuracionWS.setCodigoAplicacion((aplicaicon));
		configuracionWS.setTerminalContable(terminal);
		configuracionWS.setTerminalLogico(logico);
		return configuracionWS;
	}
	
	/**
	 * Metodo que permite remover un objeto de la Session
	 * @param nombre
	 */
	public static void  removeObjectSession(String nombre){
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		sessionparam.remove(nombre);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public Grid<?> getGrid() {
		return grid;
	}

	public void setGrid(Grid<?> grid) {
		this.grid = grid;
	}
	
	

}
