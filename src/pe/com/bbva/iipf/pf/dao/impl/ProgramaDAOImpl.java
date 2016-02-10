package pe.com.bbva.iipf.pf.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.ProgramaDAO;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;

@Service("programaDAO" )
public class ProgramaDAOImpl extends GenericDAOImpl implements ProgramaDAO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public ProgramaDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		 
	}
	
	/**
	 * genera el Numero de solicitud
	 */
	@Override
	public synchronized String generarNumeroSolicitud() throws DAOException {
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt =null;
		ResultSet rs  = null;
		String codigo = "";
		try {
			hibernateSession = (Session) getHibernateTemplate().getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_NUM_SOLICITUD(?)}");
			cstmt.registerOutParameter(1,
									   oracle.jdbc.driver.OracleTypes.VARCHAR);			
			cstmt.executeQuery();
			codigo = (String)cstmt.getObject(1);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}finally{
			try {
					if(rs!=null){
						rs.close();
					}
					if(cstmt!=null){
						cstmt.close();
					}
					hibernateSession.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new DAOException(e.getMessage());
				} 
		}
		
		return codigo;
	}

}
