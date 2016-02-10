package pe.com.bbva.iipf.pf.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.ParametroDAO;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;


@Service("parametroDAO" )
public class ParametroDAOImpl extends GenericDAOImpl implements	ParametroDAO{
Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public ParametroDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		
	}
	@Override
	public Long ValidaCargaEnProceso(String strCodigo) throws DAOException {
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt =null;
		ResultSet rs  = null;		
		Integer cant = 0;
		Long IdCarga=0L;
		
		try {
			hibernateSession = (Session) getHibernateTemplate().getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_VALIDAR_ENPROCESO(?,?)}");			
			cstmt.registerOutParameter(1,oracle.jdbc.OracleTypes.VARCHAR);		
			cstmt.setString(2, strCodigo);
			
			cstmt.executeQuery();			
			IdCarga = Long.parseLong((String)cstmt.getObject(1));			
			
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
		
		return IdCarga;
	}
	
	@Override
	public Integer ValidaCargaAutomatica(String fechaproceso, int idtipoArchivo) throws DAOException {
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt =null;
		ResultSet rs  = null;		
		Integer cant = 0;
		try {
			hibernateSession = (Session) getHibernateTemplate().getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_VALIDAR_FECHAPROCESO(?,?,?)}");			
			cstmt.registerOutParameter(1,oracle.jdbc.OracleTypes.INTEGER);			
			cstmt.setString(2, fechaproceso);
			cstmt.setInt(3, idtipoArchivo);
			cstmt.executeQuery();			
			cant = (Integer)cstmt.getObject(1);
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
		
		return cant;
	}
	
	
	//ini  MCG20130904
	@Override
	public List<List>  findResultScript(String strScript,String strTipoScript ) throws DAOException {
		logger.info("INICIO findResultScript");
		Session hibernateSession = null;
		
		List resultado = new ArrayList();
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		HashMap<String, String> listaResultado = new HashMap<String, String>();
		try {
			hibernateSession = (Session) getHibernateTemplate()
					.getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt = con.prepareCall("{call PROFIN.PFPKG.PR_IIPF_EXECUTE_SQL(?,?,?,?)}");
			
			cstmt.setString(1, strScript);	
			cstmt.setString(2, strTipoScript);
			cstmt.registerOutParameter(3,oracle.jdbc.OracleTypes.NUMBER);
			cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(4);			
			ResultSetMetaData metadata = rs.getMetaData();			
			List<HashMap<String, String>> listaColumna = new ArrayList<HashMap<String, String>>();
		    while (rs.next()) {
		    	listaColumna = new ArrayList<HashMap<String, String>>();
		      for (int i = 0; i < metadata.getColumnCount(); i++) {
		    	  listaResultado = new HashMap<String, String>();
		        Object value = rs.getObject(i + 1);
		        String valor;
		        if (value == null) {
		          valor="";
		        } else {
		          valor=value.toString().trim();
		        }	
		        listaResultado.put(metadata.getColumnLabel(i + 1), valor);
		        listaColumna.add(listaResultado);
		      }
		      resultado.add(listaColumna);		      
		    }    
		
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cstmt != null) {
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
		logger.info("FIN findResultScript");
		return resultado;
	}
	
	
	//fin MCG20130904
	
	
}
