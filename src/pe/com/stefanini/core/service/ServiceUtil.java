package pe.com.stefanini.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pe.com.bbva.iipf.mantenimiento.model.SelectItem;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.ComboUtil;
import pe.com.bbva.iipf.util.Constantes;

import pe.com.stefanini.core.exceptions.UtilException;

/**
 * Clase utilitaria para obtener variables
 * subidas a session
 * con el fin de evitar consultas masivas a la base
 * de datos para obtener detalle de las tablas tipo
 * 
 * @author epomayay
 *
 */
@Component("serviceUtil")
//@Scope("session")
public class ServiceUtil {

	private static Logger logger = Logger.getLogger(ServiceUtil.class);
	
	private static List<Tabla> listaTablas = new ArrayList<Tabla>();

	/**
	 * Este metodo solo retornar el detalle de las tablas 
	 * que esten con estado ACTIVO
	 */
	public static List<Tabla> findDetalleTabla(Long id){
		List<Tabla> lista = new ArrayList<Tabla>();
		for(Tabla tabla : listaTablas){
			if(tabla.getPadre() != null &&
			   tabla.getPadre().getId().equals(id) && 
			   tabla.getEstado().equals(Constantes.VAL_ACTIVO) ){
				lista.add(tabla);
			}
		}
		return lista;
	}
	
	/**
	 * Buscar padre
	 * @param id
	 * @return
	 */
	public Tabla findPadre(Long id){
		Tabla tablaObj = null;
		for(Tabla tabla : listaTablas){
			if(tabla.getId().equals(id)){
				tablaObj = tabla;
				break;
			}
		}
		return tablaObj;
	}
	
	/**
	 * 
	 * Metodo para obtener de forma directa el listado de los
	 * combos
	 * 
	 * @param idTipo
	 * @return
	 */
	public static List<SelectItem> getTipos(Long idTipo, 
										    String descripcionDefault){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items =  ComboUtil.getSelectItems(findDetalleTabla(idTipo), 
											  "id", 
											  "descripcion",
											  descripcionDefault);
		} catch (UtilException e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	
	/**
	 * 
	 * Metodo para obtener de forma directa el listado de los
	 * combos
	 * 
	 * @param idTipo
	 * @return
	 */
	public static List<SelectItem> getTipos(Long idTipo,String value,String label, 
										    String descripcionDefault){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items =  ComboUtil.getSelectItems(findDetalleTabla(idTipo), 
											  value, 
											  label,
											  descripcionDefault);
		} catch (UtilException e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	/**
	 * 
	 * Metodo para obtener de forma directa el listado de los
	 * combos sin valor por defecto
	 * 
	 * @param idTipo
	 * @return
	 */
	public static List<SelectItem> getTipos(Long idTipo){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items =  ComboUtil.getSelectItems(findDetalleTabla(idTipo), 
											  "id", 
											  "descripcion");
		} catch (UtilException e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	/**
	 * 
	 * Metodo para obtener de forma detallada los combos con el valor 
	 * de tabal de tablas
	 * combos sin valor por defecto
	 * 
	 * @param idTipo
	 * @return
	 */
	public static List<SelectItem> getTipos(Long idTipo,String value,String label){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items =  ComboUtil.getSelectItems(findDetalleTabla(idTipo), 
												value, 
											    label);
		} catch (UtilException e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	public static List<SelectItem> listToListOfSelectItems(List list, String label) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		try {
			for(Object object: list){
				items.add(new SelectItem((String)object,BeanUtils.getSimpleProperty(object, label)));
			}
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			logger.debug(e);
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			logger.debug(e);
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			logger.debug(e);
		}
		return items;
	}
	
	public List<Tabla> getListaTablas() {
		return listaTablas;
	}

	public void setListaTablas(List<Tabla> listaTablas) {
		this.listaTablas = listaTablas;
	}
	
	public static List<SelectItem> getTipos2(Long idTipo){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			items =  ComboUtil.getSelectItems(findDetalleTabla(idTipo), 
											  "id", 
											  "codigo");
		} catch (UtilException e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	public static String  findValueParam(Long id, List<Parametro>  listaParam){
		String valueParam=null;
		for(Parametro parametro : listaParam){
			if(parametro.getId() != null &&
					parametro.getEstado().equals(Constantes.VAL_ACTIVO)
					&& parametro.getId().longValue()==id.longValue()){
				
					valueParam=parametro.getValor();
					break;
			}
		}
		return valueParam;
	}
	public static Parametro  findParametro(Long id, List<Parametro>  listaParam){
		Parametro parametro=null;
		for(Parametro paramx : listaParam){
			if(paramx.getId() != null &&
					paramx.getEstado().equals(Constantes.VAL_ACTIVO)
					&& paramx.getId().longValue()==id.longValue()){
					parametro=paramx;
					break;
			}
		}
		return parametro;
	}
}
