package pe.com.stefanini.core.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import pe.com.stefanini.core.exceptions.DAOException;

public interface GenericDAO<X> {

	/**
	 * Método para crear la session de conexiones
	 * inyectado via Spring
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory);

	/**
	 * Método que guarda y actualiza al mismo tiempo
	 * esto depende de que si el objeto a guardar o
	 * actualizar tiene un ID creado
	 * generalmente este método sera usado con objetos
	 * que tienen PK autogenerados
	 * @param t
	 * @throws DAOException
	 */
	public void saveOrUpdate(X t) throws DAOException;
	
	/**
	 * Este método sera usado solo para guardar 
	 * generalmente usado para alamacenar objetos
	 * donde su PK son pasados de forma manual
	 * @param t
	 * @throws DAOException
	 */
	public void save(X t) throws DAOException;
	
	/**
	 * Este método sera usado solo actualizar 
	 * generalmente usado para alamacenar objetos
	 * donde su PK son pasados de forma manual
	 * @param t
	 * @throws DAOException
	 */
	public void update(X t) throws DAOException;
	
	/**
	 * Método para eliminar los objetos
	 * @param <TID> Tipo de dato de la PK
	 * @param id
	 * @throws DAOException
	 */
	public void delete(X t) throws DAOException;
	
	/**
	 * M�todo para buscar un objeto por su ID
	 * @param <TID> Tipo de dato del PK
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public<TID> X findById(TID id) throws DAOException;
	
	/**
	 * Método para buscar objetos el criterio de 
	 * búsqueda esta en fución al estado en que se encuentre
	 * el objeto
	 * @param t
	 * @return
	 * @throws DAOException
	 */
	public List<X> findObjects(X t) throws DAOException;
	
	public<TID> X findById(Class c, TID id) throws DAOException;
	
	public<TD> List<X> findByParams(Class c, HashMap<String,TD> maparib_val) throws DAOException;
	
	public List executeSQL(String sql) throws DAOException;
}
