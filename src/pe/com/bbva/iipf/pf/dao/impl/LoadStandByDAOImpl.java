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

import pe.com.bbva.iipf.pf.dao.LoadStandByDAO;
import pe.com.bbva.iipf.pf.model.Programa;

@Service("loadStandByDAO")
public class LoadStandByDAOImpl extends LoadGenericDao<Programa> implements LoadStandByDAO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public LoadStandByDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	@Override
	public void executeLoadMassive(String strFechaProceso,String strNombreArchivo,String strTipoCarga){
		
		Session hSesion = null;
		Connection con = null;
		CallableStatement cstmt =null;
		try {
			
			logger.info("INICIANDO JOB StandByDAO");
			hSesion =getHibernateTemplate().getSessionFactory().openSession();
			con = hSesion.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.PR_CRDT013_STANDBY_INSERT(?,?,?)}");
			cstmt.setString(1, strFechaProceso.trim());
			cstmt.setString(2, strNombreArchivo.trim());
			cstmt.setString(3, strTipoCarga.trim());
			cstmt.executeQuery();
			logger.info("FINALIZANDO JOB StandByDAO");
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
