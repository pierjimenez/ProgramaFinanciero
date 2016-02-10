package pe.com.bbva.iipf.reportes.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.reportes.dao.GeneradorReporteDAO;
import pe.com.stefanini.core.dao.GenericDAOImpl;
import pe.com.stefanini.core.exceptions.DAOException;
@Service("generadorReporteExcelDAOImpl")
public class GeneradorReporteExcelDAOImpl extends GenericDAOImpl<Programa> implements GeneradorReporteDAO{
	
	Logger logger = Logger.getLogger(this.getClass());
	
	
	@Autowired	
	public GeneradorReporteExcelDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	@Override
	public List<Map> listDatosBasicos(Map<String, Object> params) throws DAOException {
	
			List <Map> listaDatosBasicos= new ArrayList<Map>();
	        Session objSession = null;
	        
		 try{ 
			 
	        StringBuilder sqlQuery = new StringBuilder("");

	        sqlQuery.append(" SELECT '2' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	               sqlQuery.append("PL.TOTAL1 AS PTOTAL1,PL.TOTAL2 AS PTOTAL2,PL.TOTAL3 AS PTOTAL3,PL.TOTAL4 AS PTOTAL4,");
	               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
	               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
	               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
	               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
	               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
	               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
	               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	        sqlQuery.append(" FROM   PROFIN.TIIPF_PROGRAMA  PR INNER JOIN PROFIN.TIIPF_PLANILLA PL  ON PR.ID_PROGRAMA=PL.IIPF_PROGRAMA_ID");
	        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	        sqlQuery.append(" AND PL.ID_PLANILLA=(SELECT MAX(PLX.ID_PLANILLA) FROM PROFIN.TIIPF_PLANILLA PLX WHERE PLX.IIPF_PROGRAMA_ID=PR.ID_PROGRAMA) ");
	        
	        
	        sqlQuery.append(" UNION ");

	        sqlQuery.append(" SELECT '3' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
	               sqlQuery.append("AC.ID_ACCIONISTA,AC.NOMBRES_ACCI,AC.NACIONALIDAD,AC.PORCENTAJE,AC.CAPITALIZACION_BURS,");
	               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
	               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
	               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
	               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
	               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
	               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_ACCIONISTA AC ON PR.ID_PROGRAMA=AC.IIPF_PROGRAMA_ID");
	        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	        
	        sqlQuery.append(" UNION ");
	        
	        sqlQuery.append(" SELECT '4' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
	               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS, ");
	               sqlQuery.append("PS.ID_PART_SIGN,PS.NOMBRE_AFILIADO,PS.PORCENTAJE AS PORCENTAJE2,PS.CONSOLIDACION,PS.SECTOR_ACTI,");
	               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
	               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
	               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
	               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
	               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_PARTICIPACIONES_SIGNI PS ON PR.ID_PROGRAMA=PS.IIPF_PROGRAMA_ID");
	        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	        
	        sqlQuery.append(" UNION ");
	        
	        sqlQuery.append(" SELECT '5' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
	               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
	               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
	               sqlQuery.append("CV.ID_COMPRA_VENTA,CV.TIPO_COMP_VENT,CV.TOTAL1,CV.TOTAL2,CV.TOTAL3,CV.TIPO_TOTAL,");
	               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
	               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
	               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
	               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_COMPRA_VENTA CV ON PR.ID_PROGRAMA=CV.IIPF_PROGRAMA_ID");
	        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	        
	        sqlQuery.append(" UNION");
	        
	        sqlQuery.append(" SELECT '6' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
	               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
	               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
	               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
	               sqlQuery.append("NB.ID_NEGOCIO_BENEFICIO,NB.TIPO,NB.DESCRIP_NEGO_BENEF,NB.TOTAL_I1,NB.TOTAL_I2,NB.TOTAL_I3,");
	               sqlQuery.append("NB.TOTAL_B1,NB.TOTAL_B2,NB.TOTAL_B3,");
	               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
	               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_NEGOCIO_BENEFICIO NB ON PR.ID_PROGRAMA=NB.IIPF_PROGRAMA_ID");
	        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	        
	        sqlQuery.append(" UNION ");
	        
	        sqlQuery.append("SELECT '7' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
	        sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
	        sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
	        sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
	        sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
	        sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
	        sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
	        sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
	        sqlQuery.append("RE.ID_RATI_EXTE,RE.COMPANIA_GRUP,RE.AGENCIA,RE.CP,RE.LP,RE.OUTLOOK,RE.MONEDA,RE.FECHA,");
	        sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
	        sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
	 sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_RATING_EXTERNO RE ON pr.id_programa=RE.iipf_programa_id");
	 sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
	 
	 
     sqlQuery.append(" UNION ");
     
     sqlQuery.append("SELECT '8' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
     sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
     sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
     sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
     sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
     sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
     sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
     sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
     sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
     sqlQuery.append("CT.IIPF_CABTABLA_ID,CT.TT_ID_TIPO_TABLA,CT.NOMBRE_CABTABLA,CT.ORDEN,");
     sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");
     sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_CABTABLA CT ON pr.id_programa=CT.iipf_programa_id");
     sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
     
     sqlQuery.append(" UNION ");
     
     sqlQuery.append("SELECT '9' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
     sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
     sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
     sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
     sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
     sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
     sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
     sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
     sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
     sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
     sqlQuery.append(" CB.ID_CAPITALIZACION,CB.TT_ID_DIVISA, TT.DESCRIPCION AS DIVISA , CB.IMPORTE AS IMPORTE_CB,CB.FONDOS_PROPIOS,CB.FECHA AS FECHA_CB,CB.OBSERVACION AS OBSERVACION_CB");   
     sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_CAPITALIZACION_BURSATIL CB ON PR.ID_PROGRAMA=CB.IIPF_PROGRAMA_ID");
     sqlQuery.append(" LEFT JOIN PROFIN.TIIPF_TABLA_DE_TABLA TT ON CB.TT_ID_DIVISA=TT.ID_TABLA");
     sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");    	
			
				  
		        objSession = getSession();
		        Query objQuery = objSession.createSQLQuery(sqlQuery.toString());
				if(params.get("idPrograma")!=null){
					String idProg=(String)params.get("idPrograma");
					objQuery.setParameter("idProg",Long.valueOf(idProg), Hibernate.LONG);
				}
				
			
	        objQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        listaDatosBasicos = objQuery.list();
	        
		}catch(HibernateException e) {
			logger.error("[GeneradorReporteExcelDAOImpl :: listDatosBasicos] ");
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}catch(Exception e) {
			logger.error("[GeneradorReporteExcelDAOImpl :: listDatosBasicos] ");
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}finally{
			try {
				releaseSession(objSession);
			} catch (HibernateException e) {
				throw new DAOException(e.getMessage());
			}
		}
		
		return listaDatosBasicos;
	}
	
	public List<Map> listDatosBasicos(Map<String, Object> params,String CodEmpresaGrupo) throws DAOException {
		
		List <Map> listaDatosBasicos= new ArrayList<Map>();
        Session objSession = null;
        
	 try{ 
		 
        StringBuilder sqlQuery = new StringBuilder("");

        sqlQuery.append(" SELECT '2' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
               sqlQuery.append("PL.TOTAL1 AS PTOTAL1,PL.TOTAL2 AS PTOTAL2,PL.TOTAL3 AS PTOTAL3,PL.TOTAL4 AS PTOTAL4,");
               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");                
        sqlQuery.append(" FROM   PROFIN.TIIPF_PROGRAMA  PR INNER JOIN PROFIN.TIIPF_PLANILLA PL  ON PR.ID_PROGRAMA=PL.IIPF_PROGRAMA_ID");
        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg");
        sqlQuery.append(" AND PL.ID_PLANILLA=(SELECT MAX(PLX.ID_PLANILLA) FROM PROFIN.TIIPF_PLANILLA PLX WHERE PLX.IIPF_PROGRAMA_ID=PR.ID_PROGRAMA AND (PLX.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(PLX.COD_GRUPO_EMPRESA,'CODEP')='CODEP')) ");
        
        
        sqlQuery.append(" UNION ");

        sqlQuery.append(" SELECT '3' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
               sqlQuery.append("AC.ID_ACCIONISTA,AC.NOMBRES_ACCI,AC.NACIONALIDAD,AC.PORCENTAJE,AC.CAPITALIZACION_BURS,");
               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");                
        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_ACCIONISTA AC ON PR.ID_PROGRAMA=AC.IIPF_PROGRAMA_ID");
        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg and (AC.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(AC.COD_GRUPO_EMPRESA,'CODEP')='CODEP' )");
        
        sqlQuery.append(" UNION ");
        
        sqlQuery.append(" SELECT '4' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS, ");
               sqlQuery.append("PS.ID_PART_SIGN,PS.NOMBRE_AFILIADO,PS.PORCENTAJE AS PORCENTAJE2,PS.CONSOLIDACION,PS.SECTOR_ACTI,");
               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");                
        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_PARTICIPACIONES_SIGNI PS ON PR.ID_PROGRAMA=PS.IIPF_PROGRAMA_ID");
        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND (PS.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(PS.COD_GRUPO_EMPRESA,'CODEP')='CODEP' )");
        
        sqlQuery.append(" UNION ");
        
        sqlQuery.append(" SELECT '5' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
               sqlQuery.append("CV.ID_COMPRA_VENTA,CV.TIPO_COMP_VENT,CV.TOTAL1,CV.TOTAL2,CV.TOTAL3,CV.TIPO_TOTAL,");
               sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
               sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");                
        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_COMPRA_VENTA CV ON PR.ID_PROGRAMA=CV.IIPF_PROGRAMA_ID");
        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND (CV.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(CV.COD_GRUPO_EMPRESA,'CODEP')='CODEP')");
        
        sqlQuery.append(" UNION");
        
        sqlQuery.append(" SELECT '6' AS BLOQUE,PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
               sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
               sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
               sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
               sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
               sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
               sqlQuery.append("NB.ID_NEGOCIO_BENEFICIO,NB.TIPO,NB.DESCRIP_NEGO_BENEF,NB.TOTAL_I1,NB.TOTAL_I2,NB.TOTAL_I3,");
               sqlQuery.append("NB.TOTAL_B1,NB.TOTAL_B2,NB.TOTAL_B3,");
               sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
               sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
               sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");                
        sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  PR  INNER JOIN PROFIN.TIIPF_NEGOCIO_BENEFICIO NB ON PR.ID_PROGRAMA=NB.IIPF_PROGRAMA_ID");
        sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND (NB.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(NB.COD_GRUPO_EMPRESA,'CODEP')='CODEP')");
        
        sqlQuery.append(" UNION ");
        
        sqlQuery.append("SELECT '7' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
        sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
        sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
        sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
        sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
        sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
        sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
        sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
        sqlQuery.append("RE.ID_RATI_EXTE,RE.COMPANIA_GRUP,RE.AGENCIA,RE.CP,RE.LP,RE.OUTLOOK,RE.MONEDA,RE.FECHA,");
        sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
        sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB");        
 sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_RATING_EXTERNO RE ON pr.id_programa=RE.iipf_programa_id");
 sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND (RE.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(RE.COD_GRUPO_EMPRESA,'CODEP')='CODEP' )");
 
 
 sqlQuery.append(" UNION ");
 
 sqlQuery.append("SELECT '8' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
 sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
 sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
 sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
 sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
 sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
 sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
 sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
 sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
 sqlQuery.append("CT.IIPF_CABTABLA_ID,CT.TT_ID_TIPO_TABLA,CT.NOMBRE_CABTABLA,CT.ORDEN,");
 sqlQuery.append(" 0 AS ID_CAPITALIZACION,0 AS TT_ID_DIVISA, '' AS DIVISA , '' AS IMPORTE_CB,'' AS FONDOS_PROPIOS,'' AS FECHA_CB,'' AS OBSERVACION_CB"); 
 sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_CABTABLA CT ON pr.id_programa=CT.iipf_programa_id");
 sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND :codEmpresa=:codEmpresa");
 
 sqlQuery.append(" UNION ");
 
 sqlQuery.append("SELECT '9' AS BLOQUE, PR.DB_ACTIVIDAD_PRIN, PR.DB_T_EXT_PAIS_CODIGO,PR.DB_ANTIGUEDAD_NEGO,PR.DB_ANTIGUEDAD_CLIE,PR.DB_VALORACION_GLOBAL,");
 sqlQuery.append("PR.DB_COMEN_ACCI,PR.DB_COMEN_PART_SIGN,");
 sqlQuery.append("0 AS PTOTAL1 ,0 AS PTOTAL2,0 AS PTOTAL3,0 AS PTOTAL4,");
 sqlQuery.append("0 AS ID_ACCIONISTA,'' AS NOMBRES_ACCI,'' AS NACIONALIDAD,'' AS PORCENTAJE,'' AS CAPITALIZACION_BURS,");
 sqlQuery.append("0 AS ID_PART_SIGN,'' AS NOMBRE_AFILIADO,'' AS PORCENTAJE2,'' AS CONSOLIDACION,'' AS SECTOR_ACTI,");
 sqlQuery.append("0 AS ID_COMPRA_VENTA,0 AS TIPO_COMP_VENT,'' AS TOTAL1,'' AS TOTAL2,'' AS TOTAL3,0 AS TIPO_TOTAL,");
 sqlQuery.append("0 AS ID_NEGOCIO_BENEFICIO,0 AS TIPO,'' AS DESCRIP_NEGO_BENEF,'' AS TOTAL_I1,'' AS TOTAL_I2,'' AS TOTAL_I3,");
 sqlQuery.append("'' AS TOTAL_B1,'' AS TOTAL_B2,'' AS TOTAL_B3,");
 sqlQuery.append("0 AS ID_RATI_EXTE,'' AS COMPANIA_GRUP,'' AS AGENCIA,'' AS CP,'' AS LP,'' AS OUTLOOK,'' AS MONEDA,'' AS FECHA,");
 sqlQuery.append("0 AS IIPF_CABTABLA_ID,0 AS TT_ID_TIPO_TABLA,'' AS NOMBRE_CABTABLA,0 AS ORDEN,");
 sqlQuery.append(" CB.ID_CAPITALIZACION,CB.TT_ID_DIVISA, TT.DESCRIPCION AS DIVISA , CB.IMPORTE AS IMPORTE_CB,CB.FONDOS_PROPIOS,CB.FECHA AS FECHA_CB,CB.OBSERVACION AS OBSERVACION_CB");   
 sqlQuery.append(" FROM  PROFIN.TIIPF_PROGRAMA  pr  INNER JOIN PROFIN.TIIPF_CAPITALIZACION_BURSATIL CB ON PR.ID_PROGRAMA=CB.IIPF_PROGRAMA_ID");
 sqlQuery.append(" LEFT JOIN PROFIN.TIIPF_TABLA_DE_TABLA TT ON CB.TT_ID_DIVISA=TT.ID_TABLA");
 sqlQuery.append(" WHERE PR.ID_PROGRAMA=:idProg AND (CB.COD_GRUPO_EMPRESA=:codEmpresa OR NVL(CB.COD_GRUPO_EMPRESA,'CODEP')='CODEP' )");
		
			  
	        objSession = getSession();
	        Query objQuery = objSession.createSQLQuery(sqlQuery.toString());
			if(params.get("idPrograma")!=null){
				String idProg=(String)params.get("idPrograma");
				objQuery.setParameter("idProg",Long.valueOf(idProg), Hibernate.LONG);
				objQuery.setParameter("codEmpresa",CodEmpresaGrupo, Hibernate.STRING);
			}
			
		
        objQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        listaDatosBasicos = objQuery.list();
        
	}catch(HibernateException e) {
		logger.error("[GeneradorReporteExcelDAOImpl :: listDatosBasicos] ");
		e.printStackTrace();
		throw new DAOException(e.getMessage());
	}catch(Exception e) {
		logger.error("[GeneradorReporteExcelDAOImpl :: listDatosBasicos] ");
		e.printStackTrace();
		throw new DAOException(e.getMessage());
	}finally{
		try {
			releaseSession(objSession);
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	return listaDatosBasicos;
}

}
