package pe.com.bbva.iipf.pf.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;



@Service("politicasRiesgoAction")
@Scope("prototype") 
public class PoliticasRiesgoAction extends GenericAction {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2366320533295421636L;

	Logger logger = Logger.getLogger(this.getClass());

	private Programa programa;
	
	@Resource
	private TablaBO tablaBO;
	
	@Resource
	private PoliticasRiesgoBO politicasRiesgoBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	private String comentdetOperGar;
	private String comentRiesgoTeso;
	private String comentPolRiesgoGroup;
	private String comentPolDelegacion;
	private List<LimiteFormalizado> listLimiteFormalizado= new ArrayList<LimiteFormalizado>();
	private LimiteFormalizado LimiteFormalizado;
	//AGREGADO 06022012 Epomayay
	private List<Tabla> listaTipoMiles= new ArrayList<Tabla>();
	private String tipoMiles;
	//AGREGADO 06022012 Epomayay

	
	public String init(){	
		loadPoliticasRiesgo();
		cargaTipoMiles();
		return "politicasRiesgo";
	}
	
	public void loadPoliticasRiesgo(){		
		try {			
				if (getObjectSession(Constantes.ID_PROGRAMA_SESSION)!=null){
				Long Idprograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());			
					if(Idprograma != null &&
					   !Idprograma.equals("")){	
						setPrograma(programaBO.findById(Idprograma));
						setTipoMiles(programa.getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programa.getTipoMilesPLR().toString());
						LoadLimiteFormalizar(Idprograma);
					}	
				}		
			} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
			}
	}
	


	public void LoadLimiteFormalizar(Long idprograma){			
		
		List<Tabla> listTipoRiesgo= new ArrayList<Tabla>();	
		List<LimiteFormalizado> listLimiteFormalizadotmp= new ArrayList<LimiteFormalizado>();
		List<LimiteFormalizado> listLimiteFormalizadotmpIni= new ArrayList<LimiteFormalizado>();
		List<LimiteFormalizado> listLimiteFormalizadotmp1= new ArrayList<LimiteFormalizado>();
		listLimiteFormalizado= new ArrayList<LimiteFormalizado>();
		try {		
			
		listLimiteFormalizadotmp =politicasRiesgoBO.findLimiteFormalizadoByIdprograma(idprograma);		
		Programa programatemp=new Programa();
		programatemp.setId(idprograma);	
		String Total1="";
		String Total2="";
		String Total3="";
		String Total4="";
		String Total5="";
		String Total6="";
		
		try {
	        float ta1 = 0;
			float ta2 = 0;
			float ta3 = 0;
			float ta4 = 0;
			float ta5 = 0;
			float ta6 = 0;		
			
							
			for(LimiteFormalizado olf : listLimiteFormalizadotmp){	
				
				if (!olf.getTipoRiesgo().getId().equals(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO)){
										
				ta1 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimiteAutorizado()));
				ta2 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getComprometido()));
				ta3 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getNoComprometido()));
				ta4 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getTotal()));
				ta5 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getDispuesto()));	
				ta6 += Float.valueOf(FormatUtil.FormatNumeroSinComa(olf.getLimitePropuesto()));
				
				}
				
			}
			
			 Total1=""+FormatUtil.roundTwoDecimalsPunto(ta1);
			 Total2=""+FormatUtil.roundTwoDecimalsPunto(ta2);
			 Total3=""+FormatUtil.roundTwoDecimalsPunto(ta3);
			 Total4=""+FormatUtil.roundTwoDecimalsPunto(ta4);
			 Total5=""+FormatUtil.roundTwoDecimalsPunto(ta5);
			 Total6=""+FormatUtil.roundTwoDecimalsPunto(ta6);
			
		} catch (Exception e) {
			 
		}
		
		
		if (listLimiteFormalizadotmp != null && listLimiteFormalizadotmp.size() > 0) {
			logger.info("listLimiteFormalizadotmpinix: " + listLimiteFormalizadotmp.size());
			//listLimiteFormalizado=listLimiteFormalizadotmp;
			int conttotal=0;
			for (LimiteFormalizado olimiteFormalizado: listLimiteFormalizadotmp) {				
				if (olimiteFormalizado.getTipoRiesgo().getId().equals(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO)){
					olimiteFormalizado.setTipoTotal("TT");
					conttotal+=1;
				}else{
					olimiteFormalizado.setTipoTotal("DD");
				}	
				listLimiteFormalizadotmp1.add(olimiteFormalizado);
			}
			if (conttotal==0){
				LimiteFormalizado olimiteFormalizaciontotal=new LimiteFormalizado();
				olimiteFormalizaciontotal.setId(null);
				Tabla otipoRiesgo=new Tabla();
				otipoRiesgo.setId(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO);
				otipoRiesgo.setDescripcion("TOTAL GRUPO");
				olimiteFormalizaciontotal.setTipoRiesgo(otipoRiesgo);
				olimiteFormalizaciontotal.setPrograma(programatemp);
				olimiteFormalizaciontotal.setTipoTotal("TT");
				//
				olimiteFormalizaciontotal.setLimiteAutorizado(Total1);
				olimiteFormalizaciontotal.setComprometido(Total2);
				olimiteFormalizaciontotal.setNoComprometido(Total3);
				olimiteFormalizaciontotal.setTotal(Total4);
				olimiteFormalizaciontotal.setDispuesto(Total5);	
				olimiteFormalizaciontotal.setLimitePropuesto(Total6);
				//
				
				listLimiteFormalizadotmp1.add(olimiteFormalizaciontotal);		
			}
			listLimiteFormalizado=listLimiteFormalizadotmp1;
			
			logger.info("listLimiteFormalizadotmpfinx: " + listLimiteFormalizadotmp.size());
		}else{
			listTipoRiesgo = tablaBO.listarHijos(Constantes.ID_TABLA_TIPO_RIESGO);
			
			for (Tabla tipoRiesgo: listTipoRiesgo) {
					LimiteFormalizado olimiteFormalizacion=new LimiteFormalizado();
					if (tipoRiesgo.getId().equals(Constantes.ID_TIPO_GRUPO_POLITICAS_RIESGO)){
						olimiteFormalizacion.setTipoTotal("TT");						
					}else{
						olimiteFormalizacion.setTipoTotal("DD");
					}	
					olimiteFormalizacion.setId(null);
					olimiteFormalizacion.setTipoRiesgo(tipoRiesgo);
					olimiteFormalizacion.setPrograma(programatemp);
					listLimiteFormalizadotmpIni.add(olimiteFormalizacion);					
			}				
			listLimiteFormalizado=listLimiteFormalizadotmpIni;
		}		
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}	
	
	public String updatePoliticaRiesgo(){
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String id = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		logger.info(getListLimiteFormalizado());
		//Programa programatmp = new Programa();
		programa.setId(Long.valueOf(id));
		programa.setTipoMilesPLR(Long.parseLong(getTipoMiles()));
							
		try {
			
			String strcodEmpresa="1";	
			
			programa.setNombreGrupoEmpresa("Stefanini");
			politicasRiesgoBO.setPrograma(programa);
			politicasRiesgoBO.setListLimiteFormalizado(listLimiteFormalizado);
			politicasRiesgoBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			politicasRiesgoBO.updatePoliticasRiesgo();
			
			addActionMessage("Datos Actualizados");
			cargaTipoMiles();
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		logger.info("tipos miles:>"+getListaTipoMiles());
		logger.info("tipo miles:>"+getTipoMiles());
		return "politicasRiesgo";		
	}
	
	//////Modificado 06022012 empomayay
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
	//////Modificado 06022012 empomayay
	
	
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public String getComentdetOperGar() {
		return comentdetOperGar;
	}

	public void setComentdetOperGar(String comentdetOperGar) {
		this.comentdetOperGar = comentdetOperGar;
	}

	public String getComentRiesgoTeso() {
		return comentRiesgoTeso;
	}

	public void setComentRiesgoTeso(String comentRiesgoTeso) {
		this.comentRiesgoTeso = comentRiesgoTeso;
	}

	public String getComentPolRiesgoGroup() {
		return comentPolRiesgoGroup;
	}

	public void setComentPolRiesgoGroup(String comentPolRiesgoGroup) {
		this.comentPolRiesgoGroup = comentPolRiesgoGroup;
	}

	public String getComentPolDelegacion() {
		return comentPolDelegacion;
	}

	public void setComentPolDelegacion(String comentPolDelegacion) {
		this.comentPolDelegacion = comentPolDelegacion;
	}

	public List<LimiteFormalizado> getListLimiteFormalizado() {
		return listLimiteFormalizado;
	}

	public void setListLimiteFormalizado(
			List<LimiteFormalizado> listLimiteFormalizado) {
		this.listLimiteFormalizado = listLimiteFormalizado;
	}

	public List<Tabla> getListaTipoMiles() {
		return listaTipoMiles;
	}

	public void setListaTipoMiles(List<Tabla> listaTipoMiles) {
		this.listaTipoMiles = listaTipoMiles;
	}

	public LimiteFormalizado getLimiteFormalizado() {
		return LimiteFormalizado;
	}

	public void setLimiteFormalizado(LimiteFormalizado limiteFormalizado) {
		LimiteFormalizado = limiteFormalizado;
	}

	public String getTipoMiles() {
		return tipoMiles;
	}

	public void setTipoMiles(String tipoMiles) {
		this.tipoMiles = tipoMiles;
	}
		
}
