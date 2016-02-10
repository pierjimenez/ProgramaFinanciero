package pe.com.bbva.iipf.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import pe.com.bbva.iipf.mantenimiento.model.SelectItem;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.exceptions.UtilException;

/**
 * 
 * @author EPOMAYAY
 *
 */
public class ComboUtil{

	Logger logger = Logger.getLogger(this.getClass());
	
	
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems
	 * 
	 * @param list
	 * @param value
	 * @param label
	 * @param idTabladeTabla
	 * @return
	 * @throws UtilException
	 */
	public static  List<SelectItem> getSelectItems(Collection list, 
													String value, 
													String label,
													Long idTabladeTabla)throws UtilException{
			List<SelectItem> items = new ArrayList<SelectItem>();
			SelectItem opcionSeleccionar = new SelectItem(Constantes.COD_DEFAULT_SELECTION,
				 	  										   Constantes.VAL_DEFAULT_SELECTION);
			try {
				items.add(opcionSeleccionar);
				for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
						 BeanUtils.getSimpleProperty(object, label)));
				}
				items = sortSelectItemList(items);
			} catch (IllegalAccessException e) {
				throw new UtilException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new UtilException(e.getMessage());
			} catch (NoSuchMethodException e) {
				throw new UtilException(e.getMessage());
			} 
			return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems
	 * 
	 * @param list
	 * @param value
	 * @param label
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItems(Collection list, 
												  String value, 
												  String label,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar = new SelectItem(Constantes.COD_DEFAULT_SELECTION,
													  descripcionDefault);
		try {
			items.add(opcionSeleccionar);
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label)));
			}
			items = sortSelectItemList(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems sin SortTable
	 * 
	 * @param list
	 * @param value
	 * @param label
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItemsSinSort(Collection list, 
												  String value, 
												  String label,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar = new SelectItem(Constantes.COD_DEFAULT_SELECTION,
													  descripcionDefault);
		try {
			items.add(opcionSeleccionar);
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label)));
			}
			
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems
	 * 
	 * @param list
	 * @param value
	 * @param label
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItems(Collection list, 
												  String value, 
												  String label)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		try {
			
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label)));
			}
			items = sortSelectItemList(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems
	 * sin Sortable
	 * @param list
	 * @param value
	 * @param label
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItemsSinSort(Collection list, 
												  String value, 
												  String label)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		try {
			
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label)));
			}
			items = sortSelectItemList(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems con 2 label
	 * 
	 * @param list
	 * @param value
	 * @param label1
	 * @param label2
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItems(Collection list, 
												  String value, 
												  String label1,
												  String label2,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar = new SelectItem(Constantes.COD_DEFAULT_SELECTION,
													  descripcionDefault);
		try {
			items.add(opcionSeleccionar);
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label1)+" - "+
										 BeanUtils.getSimpleProperty(object, label2)));
			}
			items = sortSelectItemList(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems con 2 label
	 * 
	 * @param list
	 * @param value
	 * @param label1
	 * @param label2
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItemsSinSort(Collection list, 
												  String value, 
												  String label1,
												  String label2,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar =null;
		
		
		try {
			
			if(descripcionDefault!=null){
				opcionSeleccionar=new SelectItem(Constantes.COD_DEFAULT_SELECTION,descripcionDefault);
				items.add(opcionSeleccionar);
			}
			
			
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label1)+" - "+
										 BeanUtils.getSimpleProperty(object, label2)));
			}
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Convierte y ordena una colecion en un array de SelectItems con 2 label
	 * 
	 * @param list
	 * @param value
	 * @param label1
	 * @param label2
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItemsConSortCodigo(Collection list, 
												  String value, 
												  String label1,
												  String label2,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar =null;
		
		
		try {
			
			if(descripcionDefault!=null){
				opcionSeleccionar=new SelectItem(Constantes.COD_DEFAULT_SELECTION,descripcionDefault,"");
				items.add(opcionSeleccionar);
			}
			
			
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label1)+" - "+
										 BeanUtils.getSimpleProperty(object, label2),BeanUtils.getSimpleProperty(object, label1)));
			}
			
			items = sortSelectItemListParam(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	//ini MCG20130812
	/**
	 * Convierte y ordena una colecion en un array de SelectItems con 2 label
	 * 
	 * @param list
	 * @param value
	 * @param label
	 * @param codigo
	 * @return
	 * @throws UtilException
	 */
	public static List<SelectItem> getSelectItemsConSortCodigo2(Collection list, 
												  String value, 
												  String label,
												  String codigo,
												  String descripcionDefault)throws UtilException{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem opcionSeleccionar =null;
		
		
		try {
			
			if(descripcionDefault!=null){
				opcionSeleccionar=new SelectItem(Constantes.COD_DEFAULT_SELECTION,descripcionDefault,"");
				items.add(opcionSeleccionar);
			}
			
			
			for(Object object: list){
				items.add(new SelectItem(BeanUtils.getSimpleProperty(object, value),
										 BeanUtils.getSimpleProperty(object, label),
										 BeanUtils.getSimpleProperty(object, codigo)));
			}
			
			items = sortSelectItemListParam(items);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UtilException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UtilException(e.getMessage());
		}
		return items;
	}
	
	//fin MCG20130812
	
	/**
	 * Ordena una lista, que contiene objetos SelectItem, según el label de cada SelectItem.<br/>
	 * (El label y el value deben ser del tipo String)
	 * @param lista List de SelectItems
	 */
	public static List<SelectItem> sortSelectItemList(List<SelectItem> lista){
		Iterator iterador = lista.iterator();
		Set<String> ids = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		Hashtable hashContent = new Hashtable();
		while(iterador.hasNext()){
			SelectItem item = (SelectItem)iterador.next();
			ids.add(item.getLabel());
			hashContent.put(item.getLabel(), item.getValue());
		}
		lista = new ArrayList<SelectItem>();
		SelectItem newItem = null;
		for(String elm : ids){
			newItem = new SelectItem(hashContent.get(elm).toString(),elm);
			lista.add(newItem);
		}
		return lista;
	}
	
	/**
	 * Ordena una lista, que contiene objetos SelectItem, según el label de cada SelectItem.<br/>
	 * (El label y el value deben ser del tipo String)
	 * @param lista List de SelectItems
	 */
	public static List<SelectItem> sortSelectItemListParam(List<SelectItem> lista){
		
		
		List<SelectItem> listParam=new ArrayList<SelectItem>();
		Iterator iterador = lista.iterator();
		Set<String> ids = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		while(iterador.hasNext()){
			SelectItem item = (SelectItem)iterador.next();
			ids.add(item.getCodigo());
			
		}
		
		SelectItem newItem = null;
		for(String elm : ids){
			for(SelectItem obj:lista){
				if(elm.equals(obj.getCodigo())){
					
					listParam.add(obj);	
				}
				
				
			}
			
			
		}
		
		
		return listParam;
	}
	
	
	
	public static Long validateOption(String value){
		if(value != null && 
		   value.equals("")){
			return null;
		}else if(value == null){
			return null;
		}
		return Long.valueOf(value);
	}

}
