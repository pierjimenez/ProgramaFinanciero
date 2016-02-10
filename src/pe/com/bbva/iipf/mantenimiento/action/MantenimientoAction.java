package pe.com.bbva.iipf.mantenimiento.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;

import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;

@Service("mantenimientoAction")
@Scope("prototype")
public class MantenimientoAction extends GenericAction 
{

	Logger logger = Logger.getLogger(this.getClass());
	
	public String mantenimientos()
	{
		Map<String, Object> sessionparam = ActionContext.getContext().getSession();
		sessionparam.put(Constantes.OPCION_PADRE_ACTUAL, Constantes.ID_MENU_PADRE_MANTENIMIENTO);
		return "mantenimientos";
	}
	
}
