package pe.com.bbva.iipf.pf.bo;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.ArchivoReporteCredito;
import pe.com.bbva.iipf.pf.model.ClaseCredito;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.Garantia;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ReporteCredito;
import pe.com.bbva.iipf.pf.model.SustentoOperacion;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;


public interface ReporteCreditoBO {
	public void setPrograma(Programa programa);
	public void updateReporteCredito() throws BOException;
	public void updateReporteCredito(String codEmpresaGrupo) throws BOException ;
	
	public List<ClaseCredito> ObtenerListaClaseCredito(Programa oprograma) throws BOException;
	public List<ClaseCredito> ObtenerListaClaseCredito(Programa oprograma,String codEmpresaGrupo) throws BOException ;
	public void setListaClaseCredito(List<ClaseCredito> listaClaseCredito);	
	public void saveUDClaseCredito() throws BOException;
	public void saveUDClaseCredito(String codEmpresaGrupo) throws BOException;
	
	public void setListGarantia(List<Garantia> listGarantia);
	public void saveGarantia() throws BOException;	
	public void saveGarantia(String codEmpresaGrupo) throws BOException;
	public List<Garantia> findGarantiaByIdprograma(Long idPrograma) throws BOException ;
	public List<Garantia> findGarantiaByIdprograma(Long idPrograma,String codEmpresaGrupo) throws BOException ;
	
	public List<SustentoOperacion> findSustentoOperacionByIdprograma(Long idPrograma) throws BOException  ;	
	public List<SustentoOperacion> findSustentoOperacionByIdprograma(Long idPrograma,String codEmpresaGrupo) throws BOException ;
	public void setListaSustentoOperacion(List<SustentoOperacion> listaSustentoOperacion);
	public void saveUDSustentoOperacion() throws BOException;
	public void saveUDSustentoOperacion(String codEmpresaGrupo) throws BOException;
	public void copiaReporteCredito() throws BOException;
	
	public List<ReporteCredito> getListaReporteCredito(String CodEmpresaGrupo)throws BOException;
	public List<ReporteCredito> getListaReporteCredito(Programa oprograma,String CodEmpresaGrupo)throws BOException;
	
	/**vchn-20130410**/
	public List<ClaseCredito> loadReporteCreditoByAnexos(Programa oprograma, String codEmpresa) throws BOException  ;	
	public BigDecimal getTipoCambio(String fecha) throws BOException;
	/**vchn-20130410**/
	/**gmp-20130410**/
	public SustentoOperacion findSustentoById(Long id) throws BOException;
	public void save(SustentoOperacion sustento) throws BOException;
	public void delete(SustentoOperacion sustento) throws BOException;
	/**gmp-20130410**/
	public void updatePosicionSustento(SustentoOperacion so) throws BOException;
	public void setSysCodificacion (String sysCodificacion);
	public void setValorBlob(String valorBlob) ;
	
	public List<SustentoOperacion> listaSustentoOperacionByProgramaByEmpresa(Programa oprograma,String codEmpresaGrupo) throws BOException ;
	
	public List<ReporteCredito> getListaReporteCredito(Programa oprograma)throws BOException ;
	public void setListaReporteCredito(List<ReporteCredito> listaReporteCredito) ;
	public void ActualizarReporteCreditoByPrograma(Programa oprograma,List<Empresa> listaEmpresasGrupofin) throws BOException;

	public void setUsuarioSession(UsuarioSesion usuarioSession);
	
	public void refreshReporteCredito(String codEmpresaGrupo,List<Empresa> listaGrupoEmpresas,ConfiguracionWS oConfiguracionWS) throws BOException ;
	
	public void saveFileReporteCredito(File fileReporteCredito, 
			  Programa programa,
			  ArchivoReporteCredito archivoReporteCredito) throws BOException ;
	public List<ArchivoReporteCredito> findListaArchivoReporteCredito(Programa programa,String codEmpresaGrupo)throws BOException;
	public void delete(ArchivoReporteCredito t) throws BOException;
}
