package pe.com.bbva.iipf.seguridad.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;

import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;

@Service("seguridadAction")
@Scope("prototype")
public class SeguridadAction extends GenericAction{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public String seguridad()
	{
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		
		//Setear el padre actual de las opciones
		sessionparam.put(Constantes.OPCION_PADRE_ACTUAL, Constantes.ID_MENU_PADRE_SEGURIDAD);
		
		return "seguridad";
	}
	
	public String home(){
		getRequest().getSession().removeAttribute(Constantes.OPCION_PADRE_ACTUAL);
		return "panel";
	}
	
	public String closeSession(){
		try{
			getSession(false).setAttribute(Constantes.USUARIO_SESSION, null);
			getSession(false).invalidate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return "logout";
	}
}
