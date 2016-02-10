package pe.com.bbva.iipf.pf.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;


public interface ProgramaBO {

	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPathWebService(String pathWebService);
	public void setConfiguracionWS(ConfiguracionWS configuracionWS);	
	public void save(Programa programa) throws BOException;
	public Programa findById(Long id)throws BOException;
	public void setListaGrupoEmpresas(List<Empresa> listaGrupoEmpresas) ;
	
	public List<Empresa> findEmpresaByIdprogramaRuc(String Idprograma,String ruc) throws BOException;	
	public List<Empresa> findEmpresaByIdprograma(Long Idprograma) throws BOException;
	public List<Programa> listarProgramas(String tipoEmpresa, 
										  String tipoDocumento, 
										  String codigo,
										  Date fechaInicio,
										  Date fechaFin,
										  String idEstadoPrograma) throws BOException;
	public void actualizarFechaModificacionPrograma(Long idPrograma) throws BOException;
	public void actualizarFechaModificacionPrograma(Long idPrograma,String usuarioModicacion) throws BOException;
	public void conluirPrograma(Long idPrograma)throws BOException;
	public void conluirPrograma2(Long idPrograma,Long codMotivo,String observacionCierre, String fechaCierre,String codUsuarioCierre)throws BOException;
	public List<HashMap<String,String>> findByCriterio(String tipoDoc, 
										   String tipoEmpresa, 
										   String codigo)throws BOException;
	public Empresa findEmpresaByIdEmpresa(Long idEmpresa) throws BOException  ;
	public Empresa findEmpresaByIdEmpresaPrograma(Long idPrograma , String codigoEmpresa) throws BOException ;
	
	public boolean validate(Programa t) throws BOException;
	
	public List<Programa> listarProgramas(String tipoEmpresa, 
			  String tipoDocumento, 
			  String codigo,
			  Date fechaInicio,
			  Date fechaFin,
			  String idEstadoPrograma,
			  String tipoDocumentogrupo) throws BOException;
			  
	public void refreshEfectividad(String newEfecProm6sol,
			String newEfecProm6dol, String newProtestoProm6sol,
			String newProtestoProm6dol, String idprograma) throws BOException;
	
	public void refreshClasificacionFinaciera(String nor1, String cpp1,
			String deff1, String dud1, String per1, String idprograma) throws BOException;
	
	public void actualizarClasificacion(Programa t) throws BOException;
	
	public List<Programa> listarProgramas(String tipoEmpresa, 
			  String tipoDocumento, 
			  String codigo,
			  Date fechaInicio,
			  Date fechaFin,
			  String idEstadoPrograma,
			  String tipoDocumentogrupo,
			  String tipoDocumentoTodos) throws BOException ;
	public void setPathWebServicePEC6(String pathWebServicePEC6) ;
	public List<Empresa> getEmpresaWS(Long idPrograma);
	public List<Empresa> getEmpresaGrupoWS(String idGrupo,Long idPrograma,TmanagerLog oLog);
	public void actualizarTipoOperacion(Long idPrograma,Long codigotipoOperacion) throws BOException;
	public List<Programa> listarProgramasByCodigoCentral(String codigo,String idprogramaIni ) throws BOException;
	public void actualizarEstadoDescargaPDF(Long idPrograma,String estadoDescargaPDF) throws BOException;
}
