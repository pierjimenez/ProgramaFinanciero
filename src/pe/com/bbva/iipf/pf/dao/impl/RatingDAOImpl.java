package pe.com.bbva.iipf.pf.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.RatingDAO;
import pe.com.bbva.iipf.pf.model.Pfrating;
import pe.com.bbva.iipf.pf.model.TcuentaRating;
import pe.com.bbva.iipf.pf.model.TmesEjercicio;
import pe.com.bbva.iipf.pf.model.Trating;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.ConexionJDBC;

@Service("ratingDAO" )
public class RatingDAOImpl extends GenericDAOImpl implements RatingDAO{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public RatingDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		 
	}
	@Override
	public List<Trating> findListaRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException {
		
		logger.debug("INICIO findListaRatingGrupo");
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt = null;
		
		ResultSet rs = null;		
		Trating otrating = null;	

		List<Trating> lista = new ArrayList<Trating>();
		try {
			hibernateSession = getHibernateTemplate().getSessionFactory()
					.openSession();
			con = hibernateSession.connection();
			cstmt = con
					.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_RATING_EMPGRU(?,?,?,?)}");

			cstmt.setString(1, idPrograma);	
			cstmt.setString(2, codCliente);
			cstmt.setString(3, TipoEmpresa);
			cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(4);
			while (rs.next()) {
				otrating = new Trating();
				otrating.setFechaPeriodoCalc(rs.getString("FECH_PERIODO_CALC"));
				otrating.setFactCuantitativo(rs.getString("FACT_CUANTITATIVOS"));
				otrating.setFactCualitativo(rs.getString("FACT_CUALITATIVOS"));	
				otrating.setFactBureau(rs.getString("FACT_BUREAU"));
				otrating.setPuntRating(rs.getString("PUNT_RATING"));
				otrating.setCalifRating(rs.getString("CALIF_RATING"));
				lista.add(otrating);
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
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			} catch (HibernateException e) {
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			}
		}
		logger.debug("INICIO findListaRatingGrupo");
		return lista;		
	}
	@Override	
public List<TcuentaRating> findListaCuentaRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException {
		
		logger.debug("INICIO findListaCuentaRatingGrupo");
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt = null;
		
		ResultSet rs = null;		
		TcuentaRating otcuentaRating = null;	

		List<TcuentaRating> lista = new ArrayList<TcuentaRating>();
		try {
			hibernateSession = getHibernateTemplate().getSessionFactory()
					.openSession();
			con = hibernateSession.connection();
			cstmt = con
					.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_RATING_CUENTA(?,?,?,?)}");

			cstmt.setString(1, idPrograma);	
			cstmt.setString(2, codCliente);
			cstmt.setString(3, TipoEmpresa);
			cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(4);
			while (rs.next()) {
				otcuentaRating = new TcuentaRating();
				otcuentaRating.setFechaPeriodoCalc(rs.getString("FECH_PERIODO_CALC"));
				otcuentaRating.setCodCuenta(rs.getString("COD_CUENTA"));
				otcuentaRating.setDescripcionCuenta(rs.getString("DESC_CUENTA"));	
				otcuentaRating.setMonto(rs.getString("MONTO"));				
				lista.add(otcuentaRating);
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
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			} catch (HibernateException e) {
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			}
		}
		logger.debug("INICIO findListaCuentaRatingGrupo");
		return lista;		
	}
	@Override
public List<TmesEjercicio> findListaMesesEjercicioRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException {
	
	logger.debug("INICIO findListaMesesEjercicioRatingGrupo");
	Session hibernateSession = null;
	Connection con = null;
	CallableStatement cstmt = null;
	
	ResultSet rs = null;		
	TmesEjercicio otmesEjercicio = null;	

	List<TmesEjercicio> lista = new ArrayList<TmesEjercicio>();
	try {
		hibernateSession = getHibernateTemplate().getSessionFactory()
				.openSession();
		con = hibernateSession.connection();
		cstmt = con
				.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_RATING_MESEJE(?,?,?,?)}");

		cstmt.setString(1, idPrograma);	
		cstmt.setString(2, codCliente);
		cstmt.setString(3, TipoEmpresa);
		cstmt.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);			
		cstmt.executeQuery();			
		rs = (ResultSet) cstmt.getObject(4);
		while (rs.next()) {
			otmesEjercicio = new TmesEjercicio();
			otmesEjercicio.setFechaPeriodoCalc(rs.getString("FECH_PERIODO_CALC"));
			otmesEjercicio.setMesesEjercicio(rs.getString("MESES_EJERCICIOS"));
			otmesEjercicio.setInflacion(rs.getString("INFLACION"));	
			otmesEjercicio.setCalifRating(rs.getString("CALIF_RATING"));	
			otmesEjercicio.setPuntRating(rs.getString("PUNT_RATING"));	
			
			otmesEjercicio.setFactCualitativos(rs.getString("FACT_CUALITATIVOS"));
			otmesEjercicio.setFactCuantitativos(rs.getString("FACT_CUANTITATIVOS"));
			otmesEjercicio.setFactBureau(rs.getString("FACT_BUREAU"));
			
			
			lista.add(otmesEjercicio);
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
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
	}
	logger.debug("INICIO findListaCuentaRatingGrupo");
	return lista;		
}

	public void insertLotepfRating_Orig(List<Pfrating> listapfRating)throws DAOException{
		Session hibernateSession = null;		
		try{
				hibernateSession = getHibernateTemplate().getSessionFactory().openSession();	
				Transaction tx = hibernateSession.beginTransaction();
				 int i=0 ; 
				for ( Pfrating oPfrating:listapfRating) {
				    i++;
				    hibernateSession.save(oPfrating);
				    if ( i % 20 == 0 ) { //20, same as the JDBC batch size
				        //flush a batch of inserts and release memory:
				    	hibernateSession.flush();
				    	hibernateSession.clear();
				    }
				}	   
				tx.commit();
				//hibernateSession.close();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} finally {
			hibernateSession.close();
	
		}
	}
	@Override
	public void insertLotepfRating(List<Pfrating> listapfRating)throws DAOException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = ConexionJDBC.getConnection();		
			String sql = " INSERT INTO PROFIN.TIIPF_PFRATING (ID_PFRATING,FECH_PERIODO_CALC,NOMB_EMPRESA,COD_CENTRAL_CLI,MESES_EJERCICIOS,INFLACION "+          
															  ",CALIF_RATING,COD_CUENTA,DESC_CUENTA,MONTO,STATUS,TIP_ESTADO_FINAN,FACT_CUALITATIVOS "+  
															  ",FACT_CUANTITATIVOS,FACT_BUREAU,PUNT_RATING,ID_GRUPO)	"+							
															  " VALUES(PROFIN.SEQ_PFRATING.NEXTVAL, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";	
			
			ps = conn.prepareStatement(sql);			 
			final int batchSize = 200;
			int count = 0;
			 
			for (Pfrating oPfrating: listapfRating) {
											 
			    ps.setString(1,oPfrating.getFechPeriodoCalc()==null?"":oPfrating.getFechPeriodoCalc()); 		 
			    ps.setString(2,oPfrating.getNombEmpresa()==null?"":oPfrating.getNombEmpresa());		     
			    ps.setString(3,oPfrating.getCodCentralCli()==null?"":oPfrating.getCodCentralCli());  		 
			    ps.setString(4,oPfrating.getMesesEjercicios()==null?"":oPfrating.getMesesEjercicios());	 
			    ps.setString(5,oPfrating.getInflacion()==null?"":oPfrating.getInflacion());	 
			    ps.setString(6,oPfrating.getCalifRating()==null?"":oPfrating.getCalifRating());	 
			    ps.setString(7,oPfrating.getCodCuenta()==null?"":oPfrating.getCodCuenta()); 
			    ps.setString(8,oPfrating.getDescCuenta()==null?"":oPfrating.getDescCuenta()); 
			    ps.setString(9,oPfrating.getMonto()==null?"":oPfrating.getMonto());	 
			    ps.setString(10,oPfrating.getStatus()==null?"":oPfrating.getStatus());	 
			    ps.setString(11,oPfrating.getTipEstadoFinan()==null?"":oPfrating.getTipEstadoFinan()); 
			    ps.setString(12,oPfrating.getFactCualitativos()==null?"":oPfrating.getFactCualitativos()); 
			    ps.setString(13,oPfrating.getFactCuantitativos()==null?"":oPfrating.getFactCuantitativos());
			    ps.setString(14,oPfrating.getFactBureau()==null?"":oPfrating.getFactBureau());
			    ps.setString(15,oPfrating.getPuntRating()==null?"":oPfrating.getPuntRating()); 
			    ps.setString(16,oPfrating.getIdGrupo()==null?"":oPfrating.getIdGrupo());			    
			    ps.addBatch();
			     
			    if(++count % batchSize == 0) {
			        ps.executeBatch();
			    }
			}
			ps.executeBatch(); // insert remaining records
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}				
			} catch (SQLException e) {
				throw new DAOException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			}
		}
	}

}
