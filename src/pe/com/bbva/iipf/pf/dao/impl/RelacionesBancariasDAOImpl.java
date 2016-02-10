package pe.com.bbva.iipf.pf.dao.impl;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.dao.RelacionesBancariasDAO;
import pe.com.bbva.iipf.pf.model.Trccmes;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;

@Service("RelacionesBancariasDAO" )
public class RelacionesBancariasDAOImpl  extends GenericDAOImpl implements	RelacionesBancariasDAO {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private StringBuilder SQL_CALIFICACION_FINANCIERA = new StringBuilder("SELECT DOCUMENTO,")
														.append(" NVL(ROUND(SUM((PORC_NOR*DEUDA_TOTAL))/SUM(DEUDA_TOTAL),2),0) NOR,")
														.append(" NVL(ROUND(SUM((PORC_CPP*DEUDA_TOTAL))/SUM(DEUDA_TOTAL),2),0) CPP,")
														.append(" NVL(ROUND(SUM((PORC_DEF*DEUDA_TOTAL))/SUM(DEUDA_TOTAL),2),0) DEF,")
														.append(" NVL(ROUND(SUM((PORC_DUD*DEUDA_TOTAL))/SUM(DEUDA_TOTAL),2),0) DUD,")
														.append(" NVL(ROUND(SUM((PORC_PER*DEUDA_TOTAL))/SUM(DEUDA_TOTAL),2),0) PER")
														.append(" FROM PROFIN.TIIPF_PFRCCMES")
														.append(" WHERE DOCUMENTO = '%s' GROUP BY DOCUMENTO");
	@Autowired	
	public RelacionesBancariasDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		 
	}
	@Override
	public List<List>  findPoolBancario(String tipoDocumento,String codigo,String tipoDeuda,String codBanco,String CodOtro,String codtipoEmpresaGrupo,String idPrograma ) throws DAOException {
		logger.info("INICIO findPoolBancario");
		Session hibernateSession = null;
		//List poolBancarios = new ArrayList();
		List poolBancarios = new ArrayList();
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		HashMap<String, String> poolBancario = new HashMap<String, String>();
		try {
			hibernateSession = (Session) getHibernateTemplate()
					.getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt = con.prepareCall("{? = call PROFIN.PFPKG.FN_IIPF_POOLBANCARIO(?,?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);			
			cstmt.setString(2, (tipoDocumento==null?"":tipoDocumento.trim()));
			cstmt.setString(3, (codigo==null?"":codigo.trim()));
			cstmt.setString(4, (tipoDeuda==null?"":tipoDeuda.trim()));
			cstmt.setString(5, (codBanco==null?"":codBanco.trim()));	
			cstmt.setString(6, (CodOtro==null?"":CodOtro.trim()));	
			
			cstmt.setString(7, (codtipoEmpresaGrupo==null?"":codtipoEmpresaGrupo.trim()));	
			cstmt.setString(8, (idPrograma==null?"":idPrograma.trim()));	
			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(1);			
			ResultSetMetaData metadata = rs.getMetaData();			
			List<HashMap<String, String>> listaColumna = new ArrayList<HashMap<String, String>>();
		    while (rs.next()) {
		    	listaColumna = new ArrayList<HashMap<String, String>>();
		      for (int i = 0; i < metadata.getColumnCount(); i++) {
		    	  poolBancario = new HashMap<String, String>();
		        Object value = rs.getObject(i + 1);
		        String valor;
		        if (value == null) {
		          valor="";
		        } else {
		          valor=value.toString().trim();
		        }	
		        poolBancario.put(metadata.getColumnLabel(i + 1), valor);
		        listaColumna.add(poolBancario);
		      }
		      poolBancarios.add(listaColumna);		      
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
		logger.info("FIN findPoolBancario");
		return poolBancarios;
	}
	
	@Override
	public List<List>  findPoolBancarioBD(String tipoDocumento,String codigo,String tipoDeuda,String codBanco,String codtipoEmpresaGrupo,String idPrograma ) throws DAOException {
		logger.info("INICIO findPoolBancario");
		Session hibernateSession = null;
		//List poolBancarios = new ArrayList();
		List poolBancarios = new ArrayList();
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		HashMap<String, String> poolBancario = new HashMap<String, String>();
		try {
			hibernateSession = (Session) getHibernateTemplate()
					.getSessionFactory().openSession();
			con = hibernateSession.connection();
			cstmt = con.prepareCall("{? = call PROFIN.PFPKG.FN_IIPF_POOLBANCARIO_BD(?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);			
			cstmt.setString(2, (tipoDocumento==null?"":tipoDocumento.trim()));
			cstmt.setString(3, (codigo==null?"":codigo.trim()));
			cstmt.setString(4, (tipoDeuda==null?"":tipoDeuda.trim()));
			cstmt.setString(5, (codBanco==null?"":codBanco.trim()));
			cstmt.setString(6, (codtipoEmpresaGrupo==null?"":codtipoEmpresaGrupo.trim()));	
			cstmt.setString(7, (idPrograma==null?"":idPrograma.trim()));	
			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(1);			
			ResultSetMetaData metadata = rs.getMetaData();			
			List<HashMap<String, String>> listaColumna = new ArrayList<HashMap<String, String>>();
		    while (rs.next()) {
		    	listaColumna = new ArrayList<HashMap<String, String>>();
		      for (int i = 0; i < metadata.getColumnCount(); i++) {
		    	  poolBancario = new HashMap<String, String>();
		        Object value = rs.getObject(i + 1);
		        String valor;
		        if (value == null) {
		          valor="";
		        } else {
		          valor=value.toString().trim();
		        }	
		        poolBancario.put(metadata.getColumnLabel(i + 1), valor);
		        listaColumna.add(poolBancario);
		      }
		      poolBancarios.add(listaColumna);		      
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
		logger.info("FIN findPoolBancario");
		return poolBancarios;
	}
	
	public List<Trccmes> findByPoolUsingFunction_Original(final String vtipoDocumento,final String vcodEmpresa,final String vcodTipoDeuda
			,final String vcodBanco,final String vtipoempresagrupo,final String vidprograma) {  
		return (List<Trccmes>) getHibernateTemplate().execute(new HibernateCallback() {    
			public Object doInHibernate(final Session session) throws HibernateException, SQLException {      
				return session.getNamedQuery("findPoolBancarioPrograma") //                    
				.setParameter("vtipoDocumento", vtipoDocumento)
				.setParameter("vcodEmpresa", vcodEmpresa) //  
				.setParameter("vcodTipoDeuda", vcodTipoDeuda) 
				.setParameter("vcodBanco", vcodBanco) 				
				.setParameter("vtipoempresagrupo", vtipoempresagrupo) 
				.setParameter("vidprograma", vidprograma) 
				.list();    }  });
		}
 

	@Override
	public List<Trccmes> findByPoolUsingFunction(String vtipoDocumento, String vcodEmpresa, String vcodTipoDeuda
			, String vcodBanco, String vtipoempresagrupo, String vidprograma) throws DAOException {
		
		logger.debug("INICIO findByPoolUsingFunction");
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt = null;
		
		ResultSet rs = null;		
		Trccmes otrccmes = null;	

		List<Trccmes> lista = new ArrayList<Trccmes>();
		try {
			hibernateSession = getHibernateTemplate().getSessionFactory()
					.openSession();
			con = hibernateSession.connection();
			cstmt = con.prepareCall("{? = call PROFIN.PFPKG.FN_IIPF_POOLBANCARIO_PROGRAMA(?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setString(2, vtipoDocumento);	
			cstmt.setString(3, vcodEmpresa);
			cstmt.setString(4, vcodTipoDeuda);			
			cstmt.setString(5, vcodBanco);	
			cstmt.setString(6, vtipoempresagrupo);
			cstmt.setString(7, vidprograma);				
			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(1);
			while (rs.next()) {
				otrccmes = new Trccmes();
				otrccmes.setId(rs.getString("ID"));
				otrccmes.setMes(rs.getString("MES"));
				otrccmes.setAnio(rs.getString("ANIO"));
				otrccmes.setCodEmpresa(rs.getString("CODEMPRESA"));	
				otrccmes.setCodbanco(rs.getString("CODBANCO"));
				otrccmes.setNombbanco(rs.getString("NOMBBANCO"));
				otrccmes.setDeudaindirecta(rs.getString("DEUDAINDIRECTA"));
				otrccmes.setDeudadirecta(rs.getString("DEUDADIRECTA"));
				otrccmes.setDeudatotal(rs.getString("DEUDATOTAL"));
				lista.add(otrccmes);	
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
		logger.debug("INICIO findByPoolUsingFunction");
		return lista;		
	}
	
	@Override
	public List<HashMap<String, BigDecimal>> findCalificacionFinanciera(String ruc) throws DAOException{
		List<HashMap<String, BigDecimal>> lista = null;
		try {
			String sql = String.format(SQL_CALIFICACION_FINANCIERA.toString(), ruc);
			lista = super.executeSQLtoMap(sql);
			logger.info(lista);
		} catch (DAOException e) {
			throw new DAOException(e);
		}
		return lista;
	}
}
