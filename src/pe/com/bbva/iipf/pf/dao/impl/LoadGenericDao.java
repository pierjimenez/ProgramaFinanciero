package pe.com.bbva.iipf.pf.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pe.com.bbva.iipf.pf.model.MensajeCorreo;
import pe.com.stefanini.core.dao.GenericDAOImpl;

public class LoadGenericDao <T> extends GenericDAOImpl<T> {

	Logger logger = Logger.getLogger(this.getClass());
	
	public LoadGenericDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	
	public MensajeCorreo obtenerDatosCorreo(Integer tipoArchivo){
		
		Session hSesion = null;
		Connection con = null;
		CallableStatement cstmt =null;
		ResultSet rs=null;
		try {
			
			logger.info("INICIANDO CONSULTA DE LOG");
			hSesion =getHibernateTemplate().getSessionFactory().openSession();
			con = hSesion.connection();
			cstmt =con.prepareCall("{call PROFIN.PFPKG.SP_DATOS_EMAIL(?,?)}");
			cstmt.setInt(1, tipoArchivo);
			cstmt.registerOutParameter(2,oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.executeQuery();
			rs = (ResultSet) cstmt.getObject(2);			
			if(rs.next()){
				MensajeCorreo mensajeCorreo=new MensajeCorreo();
				mensajeCorreo.setCodigoLog(rs.getInt("CODIGO_LOG"));
				mensajeCorreo.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				mensajeCorreo.setDescripcion(rs.getString("DESCRIPCION"));
				mensajeCorreo.setEstado(rs.getString("ESTADO"));
				mensajeCorreo.setFechaCreacion(rs.getString("FECHA"));
				return mensajeCorreo;
			}
			logger.info("FINALIZANDO CONSULTA DE LOG");			
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			try {
					if (rs != null) {rs.close();}
					if(cstmt!=null) cstmt.close();
					if(con!=null)   con.close();
					hSesion.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				} catch (HibernateException e) {
					logger.error(e.getMessage(), e);
				} 
		}
		return null;
	}

	
}