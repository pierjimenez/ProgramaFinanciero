package pe.com.bbva.iipf.pf.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.FactoresRiesgoBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;

@Service("factoresRiesgoAction")
public class FactoresRiesgoAction extends GenericAction {

	private static final long serialVersionUID = 1147016163018474381L;
	Logger logger = Logger.getLogger(this.getClass());
	
	private Programa programa;	
	private String comentarioFortaleza;	
	private String comentarioOportunidades;	
	private String comentarioDebilidades;	
	private String comentarioAmenaza;	
	private String conclusionesFODA;
	
	@Resource
	private FactoresRiesgoBO factoresRiesgoBO;
	
	
	public String init(){
		return "factoresRiesgo";
	}	
	
	public String updateFactoresRiesgo(){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String id = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();	
		Programa programa=new Programa();
		programa.setId(Long.valueOf(id));	
			
		
		try {
			programa.setNombreGrupoEmpresa("Stefanini");
			factoresRiesgoBO.setPrograma(programa);			
			factoresRiesgoBO.setComentarioFortaleza(comentarioFortaleza);
			factoresRiesgoBO.setComentarioOportunidades(comentarioOportunidades);
			factoresRiesgoBO.setComentarioDebilidades(comentarioDebilidades);
			factoresRiesgoBO.setComentarioAmenaza(comentarioAmenaza);
			factoresRiesgoBO.setConclusionesFODA(conclusionesFODA);
			
			factoresRiesgoBO.updateFactoresRiesgos();
			
			addActionMessage("Datos Actualizados");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "factoresRiesgo";
	}
	
    public String buscarPageFR() {     	       
    	return "buscarPageFR";
    } 
	
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	public String getComentarioFortaleza() {
		return comentarioFortaleza;
	}
	public void setComentarioFortaleza(String comentarioFortaleza) {
		this.comentarioFortaleza = comentarioFortaleza;
	}

	public String getComentarioOportunidades() {
		return comentarioOportunidades;
	}
	public void setComentarioOportunidades(String comentarioOportunidades) {
		this.comentarioOportunidades = comentarioOportunidades;
	}

	public String getComentarioDebilidades() {
		return comentarioDebilidades;
	}
	public void setComentarioDebilidades(String comentarioDebilidades) {
		this.comentarioDebilidades = comentarioDebilidades;
	}

	public String getComentarioAmenaza() {
		return comentarioAmenaza;
	}
	public void setComentarioAmenaza(String comentarioAmenaza) {
		this.comentarioAmenaza = comentarioAmenaza;
	}
	
	public String getConclusionesFODA() {
		return conclusionesFODA;
	}
	public void setConclusionesFODA(String conclusionesFODA) {
		this.conclusionesFODA = conclusionesFODA;
	}
	
	public FactoresRiesgoBO getFactoresRiesgoBO() {
		return factoresRiesgoBO;
	}
	public void setFactoresRiesgoBO(FactoresRiesgoBO factoresRiesgoBO) {
		this.factoresRiesgoBO = factoresRiesgoBO;
	}

	
	
	
	
	
}
