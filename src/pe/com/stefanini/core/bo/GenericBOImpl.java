package pe.com.stefanini.core.bo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FechaUtil;

/**
 * 
 * @author epomayay
 *
 * @param <T>
 */
@Service("genericBO")
public  class GenericBOImpl <T>{


	@Resource
	private GenericDAOImpl<T> genericDAO;
	
	/**
	 * Usuario loggeado actualmente este usuario 
	 * es verificado y validado contra Host
	 * este usuario debe ser pasaso por los action hacia
	 * cada BO
	 */
	private UsuarioSesion usuarioSession = new UsuarioSesion();
	

	/**
	 * 
	 * @param t
	 * @throws BOException
	 */
	public void beforeSave(T t) throws BOException{
		Method method = null;
		Object id = null;
	
			try {
				method = t.getClass().getMethod("getId", null);
				id = method.invoke(t, null);
				if(id==null){
					BeanUtils.setProperty(t,
							  			  "codUsuarioCreacion", 
							  			  usuarioSession.getRegistroHost());
					BeanUtils.setProperty(t,
							  			  "fechaCreacion", 
							  			  FechaUtil.getFechaActualDate());
					BeanUtils.setProperty(t,
							  			  "fechaModificacion", 
							  			  FechaUtil.getFechaActualDate());
				}else{
					BeanUtils.setProperty(t,
							  			  "codUsuarioModificacion", 
							  			  usuarioSession.getRegistroHost());
					BeanUtils.setProperty(t,
							  			  "fechaModificacion", 
							  			  FechaUtil.getFechaActualDate());
				}
			} catch (SecurityException e) {
				throw new BOException(e);
			} catch (NoSuchMethodException e) {
				throw new BOException(e);
			} catch (IllegalArgumentException e) {
				throw new BOException(e);
			} catch (IllegalAccessException e) {
				throw new BOException(e);
			} catch (InvocationTargetException e) {
				throw new BOException(e);
			}
			
	}
	
	public boolean validate(T t) throws BOException{
		return true;
	}
	
	public void afterSave(T t) throws BOException{
		
	}
	
	public void doSave(T t) throws BOException{
		try {
			genericDAO.saveOrUpdate(t);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public void save(T t) throws BOException{
		beforeSave(t);
		if(validate(t)){
			doSave(t);
		}
		afterSave(t);
	}
	
	public void onlySave(T t) throws BOException{		
			doSave(t);		
	}
	
	public void saveOrUpdate(T t) throws BOException{
		try {
			genericDAO.saveOrUpdate(t);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public GenericDAOImpl<T> getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDAOImpl<T> genericDAO) {
		this.genericDAO = genericDAO;
	}
	
	/**
	 * busca un objeto por su ID
	 * @param <TD> tipo de dato
	 * @param c clase a buscar
	 * @param id id del objeto
	 * @return
	 * @throws BOException
	 */
	public<TD> T findById(Class c,
						  TD id)throws BOException{
		T t = null;
		try {
			t = this.genericDAO.findById(c,
										 id);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return t;
	}
	
	public<TD> List<T> findByParams(Class c, 
									HashMap<String,TD> maparib_val) throws BOException{
		try {
			return this.genericDAO.findByParams(c, 
												maparib_val);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public<TD> List<T> findByParamsOrder(Class c, 
										 HashMap<String,TD> maparib_val, 
										 HashMap<String,String> order) throws BOException{
		try {
			return this.genericDAO.findByParamsOrder(c, 
													 maparib_val, 
													 order);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public <TD> List<T> findByParamsOrder2(Class c,
			HashMap<String, TD> maparib_val, HashMap<String, String> order)
			throws BOException {
		try {
			return this.genericDAO.findByParamsOrder2(c, maparib_val, order);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	
	
	public<TD> List findByParams2(Class c, 
								  HashMap<String,TD> maparib_val) throws BOException{
		try {
			return this.genericDAO.findByParams2(c,
												 maparib_val);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public List executeQueryHql(String hql,List<Map> listParam) throws BOException{
		try {
			return genericDAO.executeHqlParam(hql, listParam);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public List executeSQL(String sql) throws BOException{
		List lista = null;
		try {
			lista = genericDAO.executeSQL(sql);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return lista;
	}
	
	public List executeSQL(String sql, Class entity )  throws BOException{
		List lista = null;
		try {
			lista = genericDAO.executeSQL(sql, entity);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return lista;
	}
	
	public List<HashMap<String,String>> executeSQLtoMap(String sql) throws BOException{
		List lista = null;
		try {
			lista = genericDAO.executeSQLtoMap(sql);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return lista;
	}
	
	public int executeNamedQuery(String nameQuery,
			 					 List parametros)  throws BOException{
		int res = 0;
		try {
			res = genericDAO.executeNamedQuery(nameQuery, parametros);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return res;
	}
	
	//INI MCG20130409
	public int executeSQLQuery(String sqlQuery, List parametros)
			throws BOException {
		int res = 0;
		try {
			res = genericDAO.executeSQLQuery(sqlQuery, parametros);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return res;
	}
	
	//FIN MCG20130409
	
	public List executeListNamedQuery(String nameQuery,
			 						  List parametros) throws BOException{
		List lista = null;
		try {
			lista = genericDAO.executeListNamedQuery(nameQuery, parametros);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return lista;
	}
			 
	public List<T> listNamedQuery(String nameQuery, List parametros)  throws BOException{
		List<T> lista = null;
		try {
			lista = (List<T>)genericDAO.listNamedQuery(nameQuery, parametros);
		} catch (DAOException e) {
		throw new BOException(e);
		}
		return lista;
	}
		
	public void saveCollection(Collection<?> collection)throws BOException{
		try {
			genericDAO.saveCollection(collection);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public void deleteCollection(Collection<?> collection)throws BOException{
		try {
			genericDAO.deleteCollection(collection);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	public void update(T t) throws BOException{
		try {
			genericDAO.update(t);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	public UsuarioSesion getUsuarioSession() {
		return usuarioSession;
	}

	public void setUsuarioSession(UsuarioSesion usuarioSession) {
		this.usuarioSession = usuarioSession;
	}
	
   public List<?> findObjects(Class clase,Map<String, Object> parameters)throws BOException{
		List<?> lista = null;
		try {
			lista = (List<?>)genericDAO.findObjects(clase, parameters);
		} catch (DAOException e) {
			throw new BOException(e);
		}
		return lista;
   }
   
	/**
	 * Elimina de forma permanente una entidad
	 * @param t
	 * @throws BOException
	 */
	public void delete(T t) throws BOException{
		try {
			genericDAO.delete(t);
		} catch (DAOException e) {
			throw new BOException(e);
		}catch (Exception e) {
			if(e.getCause() != null &&
			   e.getCause() instanceof ConstraintViolationException){
				throw new BOException("El registro esta relacionado no es posible eliminarlo.");
			}
		}
	}
	
	//ini para jqGrid
	public Grid<T> findToGrid(T t,String where,String order,int page,int rows,int records) throws BOException, DAOException{
		Grid<T> grid=new Grid<T>();
		grid.setPage(page);
		grid.setRows(rows);
		double count;
		if(records==0){
		 count=genericDAO.executeQuery(t.getClass(), where,"").size();
		}else{
			count=records;
		}
		double pages=(count>0?Math.ceil( count/rows):0); 
		grid.setTotal((int)pages);
		grid.setRecords((int)count);
		List<T> dataModel=null;
		
		int firstResult=(page-1)*rows;
		dataModel=genericDAO.executeQuery(t.getClass(), where,order,firstResult,rows);
		grid.setDataModel(dataModel);
		return grid;
	}
	
	
	
	public Grid<T> findToGridList(List<T> list ,String where,String order,int page,int rows) throws BOException, DAOException{
		Grid<T> grid=new Grid<T>();
		List<T> newList=null;
		grid.setPage(page);
		grid.setRows(rows);
		double count;
		count=list.size();
		double pages=(count>0?Math.ceil( count/rows):0); 
		grid.setTotal((int)pages);
		grid.setRecords((int)count);
		int firstResult=(page-1)*rows;
		newList=divListGrid(list, firstResult, rows);
		grid.setDataModel(newList);
		return grid;
	}

	public Grid<T> findToGridListTotal(List<T> list ,String where,String order,int page,int rows, int total) throws BOException, DAOException{
		Grid<T> grid=new Grid<T>();
		List<T> newList=null;
		grid.setPage(page);
		grid.setRows(rows);
		double count;
		count=total;
		double pages=(count>0?Math.ceil( count/rows):0); 
		grid.setTotal((int)pages);
		grid.setRecords((int)count);
		int firstResult=(0)*rows;
		newList=divListGrid(list, firstResult, rows);
		grid.setDataModel(newList);
		return grid;
	}
	
	public List<T> divListGrid(List<T> list,int firstResult,int rows){
		List<T> newList=new ArrayList<T>();
		
		for(int i=firstResult;i<rows+firstResult;i++){
			if(i<list.size()){
			T t=list.get(i);
			newList.add(t);
			}
		}
		
		return newList;
	}
	//fin jqGrid
	
}
