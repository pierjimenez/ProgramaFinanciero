package pe.com.bbva.iipf.pf.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.dao.AnexoDAO;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.GarantiaHost;
import pe.com.bbva.iipf.pf.model.SaldoCliente;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;


@Service("anexoDAO" )
public class AnexoDAOImpl extends GenericDAOImpl implements	AnexoDAO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired	
	public AnexoDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		
	}
	@Override	
	public List<SaldoCliente> findListaSaldoCliente(String codcliente) throws DAOException {
		
		logger.debug("INICIO findListaSaldoCliente");
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt = null;
		
		ResultSet rs = null;		
		SaldoCliente saldocliente = null;	

		List<SaldoCliente> lista = new ArrayList<SaldoCliente>();
		try {
			hibernateSession = getHibernateTemplate().getSessionFactory()
					.openSession();
			con = hibernateSession.connection();
			cstmt = con
					.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_SALDODEUDOR(?,?)}");

			cstmt.setString(1, codcliente);			
			cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(2);
			while (rs.next()) {
				saldocliente = new SaldoCliente();
				saldocliente.setCodcliente(rs.getString("CODCLIENTE"));
				saldocliente.setContrato(rs.getString("CONTRATO"));
				saldocliente.setSaldodeudo(rs.getString("SALDO"));
				saldocliente.setSaldodeudolocal(rs.getString("SALDOMONEDALOCAL"));
				saldocliente.setCodMoneda(rs.getString("CODMONEDA"));
				saldocliente.setFechaform(rs.getString("FECHAFORM"));
				saldocliente.setFechaVencimiento(rs.getString("FECHAVENC"));
				saldocliente.setTipocambio(rs.getString("TIPOCAMBIO"));
				saldocliente.setFechaTipocambio(rs.getString("FECHATIPOCAMBIO"));
				saldocliente.setIndfisca(rs.getString("INDFISCA"));
				lista.add(saldocliente);
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
		logger.debug("INICIO findListaSaldoCliente");
		return lista;		
	}
	@Override
	public void insertLoteAnexoColumna(List<AnexoColumna> listaAnexoColumna)throws DAOException{
		Session hibernateSession = null;		
		try{
				hibernateSession = getHibernateTemplate().getSessionFactory().openSession();	
				Transaction tx = hibernateSession.beginTransaction();
				 int i=0 ; 
				for ( AnexoColumna oanexoColumna:listaAnexoColumna) {
				    i++;
				    hibernateSession.save(oanexoColumna);
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
	public List<GarantiaHost> findListaGarantiaHost(Empresa oempresa) throws DAOException {
		
		logger.debug("INICIO findListaGarantiaHost");
		Session hibernateSession = null;
		Connection con = null;
		CallableStatement cstmt = null;
		
		ResultSet rs = null;		
		GarantiaHost garantiaHost = null;	

		List<GarantiaHost> lista = new ArrayList<GarantiaHost>();
		try {
			hibernateSession = getHibernateTemplate().getSessionFactory()
					.openSession();
			con = hibernateSession.connection();
			cstmt = con
					.prepareCall("{call PROFIN.PFPKG.PR_TIIPF_OBTENER_GARANTIA(?,?)}");

			cstmt.setString(1, oempresa.getCodigo());			
			cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);			
			cstmt.executeQuery();			
			rs = (ResultSet) cstmt.getObject(2);
			while (rs.next()) {
				garantiaHost = new GarantiaHost();
				  garantiaHost.setEmpresa(oempresa.getNombre()==null?"":oempresa.getNombre());
			      garantiaHost.setCodCentral(rs.getString("COD_CENTRAL")==null?"":rs.getString("COD_CENTRAL")); 
			    
			      garantiaHost.setItipoGara(rs.getString("I_TIPO_GARA")==null?"":rs.getString("I_TIPO_GARA")); 				    
			      garantiaHost.setNombTipoGarantia(rs.getString("NOMB_TIPOGARANTA")==null?"":rs.getString("NOMB_TIPOGARANTA"));				    
			      garantiaHost.setCodGarantia(rs.getString("COD_GARANTIA")==null?"":rs.getString("COD_GARANTIA")); 
				    
      
			      garantiaHost.setNumeroGarantia(rs.getString("NUMERO_GARANTIA")==null?"":rs.getString("NUMERO_GARANTIA"));
			      garantiaHost.setTipoGarantia(rs.getString("TIPO_GARANTIA")==null?"":rs.getString("TIPO_GARANTIA"));
			      garantiaHost.setImporte(rs.getString("IMPORTE")==null?"":rs.getString("IMPORTE"));
			      
			      
			      //HIPOTECA
			      String strSubTipo=(rs.getString("HI_SUBTIPO")==null?"":rs.getString("HI_SUBTIPO")) + 
			    		  			(rs.getString("HI_DES_SUBTIPO")==null?"":" - ")+
			    		  			(rs.getString("HI_DES_SUBTIPO")==null?"":rs.getString("HI_DES_SUBTIPO"));//HI_DES_SUBTIPO			      
			      garantiaHost.setHisubtipo(strSubTipo);
			      	
			      garantiaHost.setHiimporte(rs.getString("HI_IMPORTE")==null?"":rs.getString("HI_IMPORTE"));
			      String strRango=(rs.getString("HI_RANGO")==null?"":rs.getString("HI_RANGO")) + 
	    		  				  (rs.getString("HI_DESC_RANGO")==null?"":" - ")+
	    		  				  (rs.getString("HI_DESC_RANGO")==null?"":rs.getString("HI_DESC_RANGO"));//HI_DESC_RANGO
			      garantiaHost.setHirango(strRango);
			      	
			      garantiaHost.setHifechaTasacion(rs.getString("HI_FECHA_DE_TASACION")==null?"":rs.getString("HI_FECHA_DE_TASACION"));
			      garantiaHost.setHitasador(rs.getString("HI_TASADOR")==null?"":rs.getString("HI_TASADOR"));
			      garantiaHost.setHitasacionMonto(rs.getString("HI_TASACION_MONTO")==null?"":rs.getString("HI_TASACION_MONTO"));
			      garantiaHost.setHidireccionPropietario(rs.getString("HI_DIRECCION_DE_PROPIETARIO")==null?"":rs.getString("HI_DIRECCION_DE_PROPIETARIO"));
			      garantiaHost.setHivalorRealizacion(rs.getString("HI_VALOR_DE_REALIZACION")==null?"":rs.getString("HI_VALOR_DE_REALIZACION"));
			      garantiaHost.setHinombre(rs.getString("HI_NOMBRE")==null?"":rs.getString("HI_NOMBRE"));
			      
			      String strClase=(rs.getString("HI_CLASE")==null?"":rs.getString("HI_CLASE")) + 
		  				  		  (rs.getString("HI_DESCRIPCION_CLASE")==null?"":" - ")+
		  				  		  (rs.getString("HI_DESCRIPCION_CLASE")==null?"":rs.getString("HI_DESCRIPCION_CLASE"));//HI_DESCRIPCION_CLASE
			      garantiaHost.setHiclase(strClase);
			       
			      
			      //DEPOSITO A PLAZO
			      garantiaHost.setDaimporte(rs.getString("DA_IMPORTE")==null?"":rs.getString("DA_IMPORTE"));
			      garantiaHost.setDanumeroDeposito(rs.getString("DA_NUMERO_DE_DEPOSITO")==null?"":rs.getString("DA_NUMERO_DE_DEPOSITO"));
			      garantiaHost.setDaglosa(rs.getString("DA_GLOSA")==null?"":rs.getString("DA_GLOSA"));
			      
			      String strDaOperacion=(rs.getString("DA_OPERACION")==null?"":rs.getString("DA_OPERACION")) + 
  				  		  			    (rs.getString("DA_DESCRIPCION_OPERACION")==null?"":" - ")+
  				  		  			    (rs.getString("DA_DESCRIPCION_OPERACION")==null?"":rs.getString("DA_DESCRIPCION_OPERACION"));//DA_DESCRIPCION_OPERACION
			      garantiaHost.setDaoperacion(strDaOperacion);			        
			      
			      //CUENTA GARANTIA			      
			      garantiaHost.setCgimporte(rs.getString("CG_IMPORTE")==null?"":rs.getString("CG_IMPORTE"));
			      garantiaHost.setCgnumeroDeposito(rs.getString("CG_NUMERO_DE_DEPOSITO")==null?"":rs.getString("CG_NUMERO_DE_DEPOSITO"));
			      garantiaHost.setCgglosa(rs.getString("CG_GLOSA")==null?"":rs.getString("CG_GLOSA"));
			      
			      String strCgOperacion=(rs.getString("CG_OPERACION")==null?"":rs.getString("CG_OPERACION")) + 
		  		  			  			(rs.getString("CG_DESCRIPCION_OPERACION")==null?"":" - ")+
		  		  			  			(rs.getString("CG_DESCRIPCION_OPERACION")==null?"":rs.getString("CG_DESCRIPCION_OPERACION"));//CG_DESCRIPCION_OPERACION
			      garantiaHost.setCgoperacion(strCgOperacion);
			       
			      
			      //FIANZA SOLIDADRIA
			      String strFsOperacion=(rs.getString("FS_OPERACION")==null?"":rs.getString("FS_OPERACION")) + 
	  		  			  				(rs.getString("FS_DESCRIPCION_OPERACION")==null?"":" - ")+
	  		  			  				(rs.getString("FS_DESCRIPCION_OPERACION")==null?"":rs.getString("FS_DESCRIPCION_OPERACION"));//FS_DESCRIPCION_OPERACION
			      garantiaHost.setFsoperacion(strFsOperacion);
			      	
			      garantiaHost.setFsmoneda(rs.getString("FS_MONEDA")==null?"":rs.getString("FS_MONEDA"));
			      garantiaHost.setFsimporte(rs.getString("FS_IMPORTE")==null?"":rs.getString("FS_IMPORTE"));
			      garantiaHost.setFsfiador(rs.getString("FS_FIADOR")==null?"":rs.getString("FS_FIADOR"));
			      garantiaHost.setFsdocumentoIdentidad(rs.getString("FS_DOCUMENTO_IDENTIDAD")==null?"":rs.getString("FS_DOCUMENTO_IDENTIDAD"));
			      garantiaHost.setFsglosa(rs.getString("FS_GLOSA")==null?"":rs.getString("FS_GLOSA"));
			      
			      //FONDOS MUTUOS
			    
			      garantiaHost.setFmimporte(rs.getString("FM_IMPORTE")==null?"":rs.getString("FM_IMPORTE"));
			      garantiaHost.setFmctaPar(rs.getString("FM_CTA_PAR")==null?"":rs.getString("FM_CTA_PAR"));
			      garantiaHost.setFmcuotas(rs.getString("FM_CUOTAS")==null?"":rs.getString("FM_CUOTAS"));
			      garantiaHost.setFmmoneda(rs.getString("FM_MONEDA")==null?"":rs.getString("FM_MONEDA"));
			      garantiaHost.setFmvalorCotizacion(rs.getString("FM_VALOR_COTIZADO")==null?"":rs.getString("FM_VALOR_COTIZADO"));
			      garantiaHost.setFmfechaCotizacion(rs.getString("FM_F_COTIZACION")==null?"":rs.getString("FM_F_COTIZACION"));
			      garantiaHost.setFmfechaLiberacion(rs.getString("FM_F_LIBERACION")==null?"":rs.getString("FM_F_LIBERACION"));
			      
			      //WARRANT
			      
			      garantiaHost.setWaimporte(rs.getString("WA_IMPORTE")==null?"":rs.getString("WA_IMPORTE"));
			      String strTipoBien=(rs.getString("WA_TIPO_BIEN")==null?"":rs.getString("WA_TIPO_BIEN")) + 
	  		  			  				(rs.getString("WA_DESC_TIPO_BIEN")==null?"":" - ")+
	  		  			  				(rs.getString("WA_DESC_TIPO_BIEN")==null?"":rs.getString("WA_DESC_TIPO_BIEN"));//WA_DESC_TIPO_BIEN
			      garantiaHost.setWatipoBien(strTipoBien);
			      	
			      garantiaHost.setWadescripcion(rs.getString("WA_DESCRIPCION")==null?"":rs.getString("WA_DESCRIPCION"));
			      garantiaHost.setWacantidad(rs.getString("WA_CANTIDAD")==null?"":rs.getString("WA_CANTIDAD"));
			      String strWaSubTipo=(rs.getString("WA_SUBTIPO")==null?"":rs.getString("WA_SUBTIPO")) + 
	  		  			  			  (rs.getString("WA_DES_SUBTIPO")==null?"":" - ")+
	  		  			  			  (rs.getString("WA_DES_SUBTIPO")==null?"":rs.getString("WA_DES_SUBTIPO"));//WA_DES_SUBTIPO
			      garantiaHost.setWasubtipo(strWaSubTipo);
			      
			      garantiaHost.setWafechaVencimiento(rs.getString("WA_FECHA_VENCIMIENTO")==null?"":rs.getString("WA_FECHA_VENCIMIENTO"));
			      			      
			      garantiaHost.setSbimporte(rs.getString("SB_IMPORTE")==null?"":rs.getString("SB_IMPORTE"));
			      garantiaHost.setSbcartCred(rs.getString("SB_CART_CRED")==null?"":rs.getString("SB_CART_CRED"));
			      garantiaHost.setSbemisor(rs.getString("SB_EMISOR")==null?"":rs.getString("SB_EMISOR"));
			      garantiaHost.setSbciudad(rs.getString("SB_CIUDAD")==null?"":rs.getString("SB_CIUDAD"));
			      garantiaHost.setSbcarta(rs.getString("SB_CARTA")==null?"":rs.getString("SB_CARTA"));
			      garantiaHost.setSbvcto(rs.getString("SB_VCTO")==null?"":rs.getString("SB_VCTO"));
			      garantiaHost.setSbrenovacion(rs.getString("SB_RENOVACION")==null?"":rs.getString("SB_RENOVACION"));
			      garantiaHost.setSbobservacion(rs.getString("SB_OBSERVACIONES")==null?"":rs.getString("SB_OBSERVACIONES"));
			      
				lista.add(garantiaHost);
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
		logger.debug("INICIO findListaGarantiaHost");
		return lista;		
	}
	
	
}
