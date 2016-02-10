package pe.com.bbva.iipf.pf.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import pe.com.bbva.iipf.pf.model.Trccmes;
import pe.com.stefanini.core.exceptions.DAOException;

public interface RelacionesBancariasDAO {

	public List<List>  findPoolBancario(String tipoDocumento,String codigo,
									    String tipoDeuda,String codBanco,
									    String CodOtro,String codtipoEmpresaGrupo,
									    String idPrograma) throws DAOException ;		
	
	public List<List>  findPoolBancarioBD(String tipoDocumento,String codigo,
										  String tipoDeuda,String codBanco,
										  String codtipoEmpresaGrupo,String idPrograma) throws DAOException ;		
	
	/*public List<Trccmes> findByPoolUsingFunction(final String vtipoDocumento,final String vcodEmpresa,
												 final String vcodTipoDeuda,final String vcodBanco,
												 final String vtipoempresagrupo,final String vidprograma);*/
	
	public List<HashMap<String, BigDecimal>> findCalificacionFinanciera(String ruc) throws DAOException;
	
	public List<Trccmes> findByPoolUsingFunction(String vtipoDocumento, String vcodEmpresa, String vcodTipoDeuda
			, String vcodBanco, String vtipoempresagrupo, String vidprograma) throws DAOException;
}
