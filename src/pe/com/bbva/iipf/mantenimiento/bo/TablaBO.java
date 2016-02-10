package pe.com.bbva.iipf.mantenimiento.bo;

import java.util.List;
import java.util.Map;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.exceptions.BOException;

public interface TablaBO 
{

	public void insertar(Tabla objTabla) throws BOException;
	public void modificar(Tabla objTabla) throws BOException;
	public List<Tabla> listar(Tabla objTabla) throws BOException;
	public List<Tabla> listarHijos(Long idPadre) throws BOException;
	public Tabla obtienePadrePorCodigo(String codigo) throws BOException;
	public Tabla obtienePadrePorCodigoSinActual(String codigo, Long idTabla) throws BOException;
	public Tabla obtieneHijaPorCodigo(String codigo, Long idPadre) throws BOException;
	public Tabla obtieneHijaPorCodigoSinActual(String codigo, Long idPadre, Long idTabla) throws BOException;
	public List<Tabla> listaPadresSinActual(Long idTabla) throws BOException;
	public Tabla obtienePorId(Long id) throws BOException;
	public Tabla obtieneHijaPorCodigoHijoCodigoPadre(String codigoHijo, String codigoPadre)	throws BOException;
	public List<Tabla> obtieneHijaCodigoPadre(String codigoPadre)	throws BOException;
	public Map<String, String> getMapTablasByCodigoPadre(String codigoPabre) ;
}
