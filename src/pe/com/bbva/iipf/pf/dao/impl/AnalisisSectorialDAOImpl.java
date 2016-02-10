package pe.com.bbva.iipf.pf.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.AnalisisSectorialDAO;
import pe.com.bbva.iipf.pf.model.AnalisisSectorial;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;


@Service("analisisSectorialDAO" )
public class AnalisisSectorialDAOImpl extends GenericDAOImpl<AnalisisSectorial> implements
		AnalisisSectorialDAO {

	@Autowired	
	public AnalisisSectorialDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);		
	}
	@Override
	public List<AnalisisSectorial> findByAnalisisSectorial(Long idprograma) throws DAOException {
		Session session = null;
		List<AnalisisSectorial> analisisSectorials= null;
 		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Criteria crit = session.createCriteria(AnalisisSectorial.class);
			crit.add( Expression.eq("programa.id", idprograma));			
			analisisSectorials = crit.list();
		}catch(HibernateException e) {
				throw new DAOException(e.getMessage());
		}finally{
			try {
				session.close();
			} catch (HibernateException e) {
				throw new DAOException(e.getMessage());
			}
		}
		return analisisSectorials;
	}

}
