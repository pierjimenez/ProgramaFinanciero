package pe.com.bbva.iipf.pf.dao.impl;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.LoadRVWCG010DAO;
import pe.com.bbva.iipf.pf.model.Programa;

@Service("loadRVWCG010DAO")
public class LoadRVWCG010DAOImpl extends LoadGenericDao<Programa> implements LoadRVWCG010DAO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public LoadRVWCG010DAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	@Override
	public void executeLoadMassive(String strTipoCarga){
		
		Session hSesion = null;
		Connection con = null;
		CallableStatement cstmt =null;
		try {
			
			logger.info("INICIANDO JOB LoadRVWCG010DAO");
			hSesion =getHibernateTemplate().getSessionFactory().openSession();
			con = hSesion.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_PFRVWCG010_INSERT(?)}");
			cstmt.setString(1,  strTipoCarga);
			cstmt.executeQuery();
			logger.info("FINALIZANDO JOB LoadRVWCG010DAO");
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}finally{
			try {
					if(cstmt!=null) cstmt.close();
					if(con!=null)   con.close();
					hSesion.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				} catch (HibernateException e) {
					logger.error(e.getMessage(), e);
				} 
		}
		
	}

}
