package pe.com.stefanini.core.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.StringUtil;

public class ConexionJDBC {
	static Logger logger = Logger.getLogger(ConexionJDBC.class.getName());

	public static Connection getConnection() throws SQLException, DAOException {
		InitialContext context = null;
		DataSource dataSource = null;
		Connection con = null;
		try {
			context = new InitialContext();
			 dataSource = (DataSource)
			 //context.lookup("java:/comp/env/jdbc/PROFIN");//TOMCAT
			 context.lookup("jdbc/PROFIN");//WAS
			/*dataSource = (DataSource) context
					.lookup("java:/comp/env/jdbc/ORA_Biiwx001");*/
			con = dataSource.getConnection();
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			throw new DAOException(e.getMessage(), e);
		}
		return con;
	}

	public static void close(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}

	}
}
