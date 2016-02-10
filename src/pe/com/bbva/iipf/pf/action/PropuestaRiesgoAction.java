package pe.com.bbva.iipf.pf.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.PropuestaRiesgoBO;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;


@Service("propuestaRiesgoAction")
@Scope("prototype") 
public class PropuestaRiesgoAction extends GenericAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3379626276769497852L;
	Logger logger = Logger.getLogger(this.getClass());

	private Programa programa;
	
	@Resource
	private ProgramaBO programaBO;
	
	private String comentPropuestaRiesgo;
	private String comentEstructuraLimite;
	private String considPropuestaRiesgo;
	private List<EstructuraLimite> listEstructuraLimite = new ArrayList<EstructuraLimite>();
	private EstructuraLimite estructuraLimite;
	
	private List<Empresa> listaEmpresa = new ArrayList<Empresa>();
	//ADD
	private List<Tabla> listaTipoMiles= new ArrayList<Tabla>();
	
	@Resource
	private PropuestaRiesgoBO propuestaRiesgoBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private String tipoEstructuraHidden;
	
	private String tipoMiles;
	
	private String chksParaEliminar;
	
	public String init(){		
		cargarEmpresas();
		loadPropuestaRiesgo();
		cargaTipoMiles();
		
		return "propuestaRiesgo";
	}
	
	
	public void loadPropuestaRiesgo(){		
		try {			
				if (getObjectSession(Constantes.ID_PROGRAMA_SESSION)!=null){
				Long Idprograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());			
					if(Idprograma != null &&
					   !Idprograma.equals("")){	
						setPrograma(programaBO.findById(Idprograma));
						setTipoMiles(getPrograma().getTipoMiles()==null?Constantes.ID_TABLA_TIPOMILES_MILES:getPrograma().getTipoMiles().toString());
						
						//ini MCG20130812
						//setTipoEstructuraHidden(programa.getTipoEstructura()==null?"1":programa.getTipoEstructura());
						setTipoEstructuraHidden("2");
						//fin MCG20130812
						LoadEstructuraLimite(Idprograma);
					}	
				}		
			} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
			}
	}
	
	public void LoadEstructuraLimite(Long idprograma){	
		try {				
			listEstructuraLimite=propuestaRiesgoBO.findEstructuraLimiteByIdprograma(idprograma);	
			if (listEstructuraLimite != null && listEstructuraLimite.size() > 0) {
				for (EstructuraLimite olistEstructuraLimite:listEstructuraLimite){
					olistEstructuraLimite.setCodEmpresatmp(olistEstructuraLimite.getEmpresa().getId().toString());					
				}
			}		
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}	
	
	
	
	public void cargarEmpresas(){
		
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		List<Empresa> listEmpresatmp= new ArrayList<Empresa>();
		listaEmpresa =new ArrayList<Empresa>(); 		
		try {
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
 			
			//buscar la actividad de la empresa
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				Empresa oTempresa=new Empresa();				
				String ruc = sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
				String strEmpresaPrincipal=sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString();
				
				listEmpresatmp =programaBO.findEmpresaByIdprogramaRuc(idprograma, ruc);	
				if (listEmpresatmp!=null && listEmpresatmp.size()>0) {
					Long idempresa= listEmpresatmp.get(0).getId();
					oTempresa.setId(idempresa);
					oTempresa.setCodigo(ruc);
					oTempresa.setNombre(strEmpresaPrincipal);
					listaEmpresa.add(oTempresa );				
				}			
				

			}else{
				//buscar las empresas del grupo
				//Simulando.				
				listaEmpresa =programaBO.findEmpresaByIdprograma(new Long(idprograma));	
		
			}	
			sessionparam.put("LISTAEMPRESA", listaEmpresa);	
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	//////
	public void cargaTipoMiles(){			
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		listaTipoMiles =new ArrayList<Tabla>(); 		
		try {					
			listaTipoMiles = tablaBO.listarHijos(Constantes.ID_TABLA_TIPOMILES);
			sessionparam.put("LISTATIPOMILES", listaTipoMiles);	
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	//////
	
	 public String addEstructuraLimite() throws Exception {
			 
			 
			 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
	 		Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
	 			
	 		 listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
	 		listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");
	 		if(listEstructuraLimite==null){
	 			listEstructuraLimite = new ArrayList<EstructuraLimite>();
			}
	 		 
			 logger.info(getListEstructuraLimite());
			 Programa programatemp=new Programa();
			 programatemp.setId(idprograma);			 
			 logger.info("size="+listEstructuraLimite.size());		
			 EstructuraLimite linealimite =new EstructuraLimite();
			 Empresa oempresat= new Empresa();	 
			 
			 oempresat.setId(new Long(1));	
			 
			 linealimite.setId(null); 
			 linealimite.setEmpresa(oempresat);
			 linealimite.setCodEmpresatmp("1");
			 linealimite.setNombEmpresa("");
			 linealimite.setTipoOperacion("");
			 linealimite.setTipo("TT");
			 linealimite.setLimiteAutorizado(null);
			 linealimite.setLimitePropuesto(null);
			 linealimite.setObservacion(null);	
			 linealimite.setPrograma(programatemp);
			 //add epomayay 07022012
			 String [] idsEliminar = null;
			 if(getChksParaEliminar()!= null){
				 idsEliminar = getChksParaEliminar().split(",");
			 }
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listEstructuraLimite.add(posicion,linealimite);
			 }else{
				 listEstructuraLimite.add(linealimite);
			 }
			 

			 logger.info("listEstructuraLimitex: " + listEstructuraLimite.size());		 
			 return "propuestaRiesgo"; 			 
	}
	
	 
	public String deleteEstructuraLimite(){	
		logger.info("Items a eliminar= "+this.getChksParaEliminar());
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		if(getChksParaEliminar()!= null){
			String [] idsEliminar = getChksParaEliminar().split(",");
			if(listEstructuraLimite!=null && listEstructuraLimite.size()>0){
				//no se debe de eliminar el primer registro por ser una empresa
				 if(Integer.parseInt(idsEliminar[0].trim())==0 && 
					listEstructuraLimite.size()>1){
					 addActionError("No se puede eliminar el primer registro por tratase de la Empresa.");
				 }else{
					//se elimina del ultimo hacia el primero para no alterar los indices
					 for(int index = idsEliminar.length-1; index>=0; index--){
						 listEstructuraLimite.remove(Integer.parseInt(idsEliminar[index].trim()));
					 }
				 }
				
			}	
		}else{
			addActionError("Debe seleccionar almenos un registro para eliminar.");
		}
		 listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
		 listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");
		return "propuestaRiesgo";
	}
	
	public String loadPropuestaRiesgoIni(){		
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
			listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");
			if (getObjectSession(Constantes.ID_PROGRAMA_SESSION)!=null){
				Long Idprograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());			
					if(Idprograma != null &&
					   !Idprograma.equals("")){
						
						LoadEstructuraLimite(Idprograma);

					}	
				}
				return "propuestaRiesgo";
			} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			return "propuestaRiesgo";
			}
	}
	
	public String deleteAllEstructuraLimite(){	
		 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			if (getTipoEstructuraHidden().equals("2")){
				listEstructuraLimite = new ArrayList<EstructuraLimite>();

			}
		 listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
		 listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");
		return "propuestaRiesgo";
	}
	 
	 public String addEmpresa() throws Exception {
		 
		 
		 Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
 		Long idprograma = Long.valueOf(sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString());
 		listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
 		listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");

		 logger.info(getListEstructuraLimite());
		 Programa programatemp=new Programa();
		 programatemp.setId(idprograma);			 
		 logger.info("size="+listEstructuraLimite.size());		
		 EstructuraLimite linealimiteEmpresa =new EstructuraLimite();
		 Empresa oempresa= new Empresa();
		 oempresa.setId(new Long(1));		 
		 linealimiteEmpresa.setId(null); 
		 linealimiteEmpresa.setEmpresa(oempresa);
		 linealimiteEmpresa.setCodEmpresatmp("1");
		 linealimiteEmpresa.setNombEmpresa("");
		 linealimiteEmpresa.setTipoOperacion("");
		 linealimiteEmpresa.setTipo("TE");
		 linealimiteEmpresa.setLimiteAutorizado(null);
		 linealimiteEmpresa.setLimitePropuesto(null);
		 linealimiteEmpresa.setObservacion(null);	
		 linealimiteEmpresa.setPrograma(programatemp);
		 
		 //add 07022012 epomayay
		 String [] idsEliminar = null;
		 if(getChksParaEliminar()!= null){
			 idsEliminar = getChksParaEliminar().split(",");
		 }
		 if(idsEliminar!=null){
			 int index = idsEliminar.length-1;
			 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
			 listEstructuraLimite.add(posicion,linealimiteEmpresa);
		 }else{
			 listEstructuraLimite.add(linealimiteEmpresa);
		 }
		 logger.info("listEstructuraLimitex: " + listEstructuraLimite.size());
		 //logger.info("listaEmpresa: " + listaEmpresa.size());
		 
		 return "propuestaRiesgo"; 
		 
	 }
	
	
	public String updatePropuestaRiesgo(){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String id = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		logger.info(getListEstructuraLimite());
		Programa programatmp = new Programa();
		programatmp.setId(Long.valueOf(id));
		programatmp.setTipoEstructura(getTipoEstructuraHidden());
		programatmp.setTipoMiles(getTipoMiles());
		
		listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
		listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");

		try {
			
			String strcodEmpresa="1";
			for (EstructuraLimite listEstruct: listEstructuraLimite){
				if (listEstruct.getTipo().equals("TE")){
					strcodEmpresa=listEstruct.getCodEmpresatmp();				
				}else if (listEstruct.getTipo().equals("TT")){
					listEstruct.setCodEmpresatmp(strcodEmpresa);
				}		
			}			
			
			for (EstructuraLimite listEstruct: listEstructuraLimite){
				Empresa empresatemp=new Empresa();
				empresatemp.setId(Long.valueOf(listEstruct.getCodEmpresatmp()));
				listEstruct.setEmpresa(empresatemp);				
			}		
						
			propuestaRiesgoBO.setPrograma(programatmp);
			propuestaRiesgoBO.setListEstructuraLimite(listEstructuraLimite);			
//			propuestaRiesgoBO.setComentPropuestaRiesgo(comentPropuestaRiesgo);
//			propuestaRiesgoBO.setComentEstructuraLimite(comentEstructuraLimite);
//			propuestaRiesgoBO.setConsidPropuestaRiesgo(considPropuestaRiesgo);
			propuestaRiesgoBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			propuestaRiesgoBO.updatePropuestaRiesgo();
			addActionMessage("Datos Actualizados");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
			return "propuestaRiesgo";
		}
		return "propuestaRiesgo";
	}
		

	public String saveEstructuraLimite(){		
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);			
			listaEmpresa=(List<Empresa>)sessionparam.get("LISTAEMPRESA");
			listaTipoMiles=(List<Tabla>)sessionparam.get("LISTATIPOMILES");

			Programa programatmp1 = new Programa();
			programatmp1.setId(Long.valueOf(idprograma));
			programatmp1.setTipoEstructura(getTipoEstructuraHidden());		
			programatmp1.setTipoMiles(getTipoMiles());
			
			propuestaRiesgoBO.setPrograma(programatmp1);
			
			String strcodEmpresa="1";
			for (EstructuraLimite listEstruct: listEstructuraLimite){
				if (listEstruct.getTipo().equals("TE")){
					strcodEmpresa=listEstruct.getCodEmpresatmp();				
				}else if (listEstruct.getTipo().equals("TT")){
					listEstruct.setCodEmpresatmp(strcodEmpresa);
				}		
			}			
			
			for (EstructuraLimite listEstruct: listEstructuraLimite){
				Empresa empresatemp=new Empresa();
				empresatemp.setId(Long.valueOf(listEstruct.getCodEmpresatmp()));
				listEstruct.setEmpresa(empresatemp);				
			}
			
			propuestaRiesgoBO.setListEstructuraLimite(listEstructuraLimite);
			propuestaRiesgoBO.saveEstructuraLimite();
			addActionMessage("Estructura Limite registrados Correctamente");
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			return "propuestaRiesgo";
		}
		
		return "propuestaRiesgo";
	}
	
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}	
	
	public String getComentEstructuraLimite() {
		return comentEstructuraLimite;
	}
	public void setComentEstructuraLimite(String comentEstructuraLimite) {
		this.comentEstructuraLimite = comentEstructuraLimite;
	}

	public String getConsidPropuestaRiesgo() {
		return considPropuestaRiesgo;
	}

	public void setConsidPropuestaRiesgo(String considPropuestaRiesgo) {
		this.considPropuestaRiesgo = considPropuestaRiesgo;
	}

	public String getComentPropuestaRiesgo() {
		return comentPropuestaRiesgo;
	}

	public void setComentPropuestaRiesgo(String comentPropuestaRiesgo) {
		this.comentPropuestaRiesgo = comentPropuestaRiesgo;
	}

	public List<EstructuraLimite> getListEstructuraLimite() {
		return listEstructuraLimite;
	}
	public void setListEstructuraLimite(List<EstructuraLimite> listEstructuraLimite) {
		this.listEstructuraLimite = listEstructuraLimite;
	}
	public EstructuraLimite getEstructuraLimite() {
		return estructuraLimite;
	}
	public void setEstructuraLimite(EstructuraLimite estructuraLimite) {
		this.estructuraLimite = estructuraLimite;
	}

	public List<Empresa> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<Empresa> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public String getTipoEstructuraHidden() {
		return tipoEstructuraHidden;
	}

	public void setTipoEstructuraHidden(String tipoEstructuraHidden) {
		this.tipoEstructuraHidden = tipoEstructuraHidden;
	}

	public List<Tabla> getListaTipoMiles() {
		return listaTipoMiles;
	}

	public void setListaTipoMiles(List<Tabla> listaTipoMiles) {
		this.listaTipoMiles = listaTipoMiles;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}

	public String getTipoMiles() {
		return tipoMiles;
	}

	public void setTipoMiles(String tipoMiles) {
		this.tipoMiles = tipoMiles;
	}

	public String getChksParaEliminar() {
		return chksParaEliminar;
	}

	public void setChksParaEliminar(String chksParaEliminar) {
		this.chksParaEliminar = chksParaEliminar;
	}
	
}
