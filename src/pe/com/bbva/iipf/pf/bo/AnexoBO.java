package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.GarantiaHost;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SaldoCliente;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface AnexoBO {

	public void calcularMontosPorEmpresa(List<FilaAnexo> lista);
	public void calcularMontosPorAccionista(List<FilaAnexo> lista) ;
	public void calcularMontoPorBanco(List<FilaAnexo> lista);
	public FilaAnexo calcularMontoTotal(List<FilaAnexo> lista);	
	public boolean validarFilas(List<FilaAnexo> lista, 
						  FilaAnexo filaAnexo,String posicion,String strAccion) throws BOException;
	public void saveAnexos()throws BOException;	
	public void setListaFilaAnexos(List<FilaAnexo> listaFilaAnexos);
	
	public void setPrograma(Programa programa);
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public List<FilaAnexo> findAnexos()throws BOException;	
	public List<FilaAnexo> findAnexosByPrograma(Programa oprograma)throws BOException;
	public List<AnexoColumna> findColumnas()throws BOException;
	public boolean deleteFila(int posAnexo,  List<FilaAnexo> listaFilaAnexosTemp)throws BOException;
	public List<AnexoColumna> findByAnexoId(Long idAnexo)throws BOException;
	
	public void saveCopiaAnexos(List<FilaAnexo> olistaFilaAnexos)throws BOException;	
	public List<SaldoCliente> findListaSaldoCliente(String codCliente) throws BOException ;
	public String findValorByColumnaAnexos(Long idprograma, String descEmpresa, String nombreColumna)throws BOException;
	/**vchn***/
	public Long findIdAnexoBancoLocal(List<FilaAnexo> listaAllAnexos,Long idEmpresa)  throws BOException;
	public List<FilaAnexo> listaAnexosByIdPadre(List<FilaAnexo> olistaFilaAnexo, Long idPadre);
	public void buscarHijos(Long idfila,List<FilaAnexo> olistaFilaAnexos,List<FilaAnexo> olistaFilaAnexosFinal);
	public Long findIdAnexoByEmpresa(List<FilaAnexo> olistaFilaAnexo,String nombreEmpresa);
	public Anexo findFilaAnexoById(Long idAnexo) throws BOException;
	public String findValorColumnaById(Long idprograma, String descEmpresa, String nombreColumna, Long idAnexo)throws BOException;
	/**vchn***/
	public String ObtenerCodigoEmpresa(List<FilaAnexo> lista,List<Empresa> oListaEmpresa,String posicion,List<Accionista> olistaAccionista);
	
	public Integer ObtenerTipoFilaPadre(List<FilaAnexo> lista,String posicion);
	public void actualizarAnexoRating(Long idColumnaAnexo,String strRating) throws BOException;
	public void actualizarAnexoRatingFecha(Long idColumnaAnexo,String strFechaRating) throws BOException;
	
	public void actualizarAnexoRatingpf(Long idAnexo,String strRating,String strFechaRating) throws BOException;
	
	public List<LimiteFormalizado> loadLimiteFormalizadoByAnexos(Programa oprograma) throws BOException;
	public String ObtenerContratosLimitePadre(List<FilaAnexo> lista,String posicion);
	public List<FilaAnexo> ObtenerHijosByIdPabre(List<FilaAnexo> lista,String posicion);
	public List<FilaAnexo> listaAnexosByIdPadreIdTemp(List<FilaAnexo> olistaFilaAnexo, Long idPadre) ;
	
	public List<FilaAnexo> findAnexosTotal()throws BOException;
	public void setListaFilaAnexosTotal(List<FilaAnexo> listaFilaAnexosTotal);
	public Anexo findValorByColumnaAnexosByEmpresa(Long idprograma, String descEmpresa)throws BOException;
	
	public List<FilaAnexo> obtenerListAnexosHijosByEmpresa(Long idprograma,String nombreEmpresa)throws BOException;
	public void asignarActivoColumna(List<FilaAnexo> listaFilaAnexo);
	
	public List<GarantiaHost> findListaGarantiaHost(Empresa oempresa) throws BOException;
	public List<FilaAnexo> obtenerListAnexosHijosByEmpresaInclueEmpresa(Long idprograma,String nombreEmpresa)throws BOException;
	
}
