package pe.com.bbva.iipf.seguridad.bo;

import java.util.List;

import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.stefanini.core.exceptions.BOException;

public interface OpcionBO 
{
	
	public void insertar(Opcion objOpcion) throws BOException;
	public void modificar(Opcion objOpcion) throws BOException;
	public List<Opcion> listar(Opcion objOpcion) throws BOException;
	public List<Opcion> listarSinActual(Long id) throws BOException;
	public Opcion obtienePorCodigo(String codigo) throws BOException;
	public Opcion obtienePorCodigoSinActual(String codigo, Long id) throws BOException;
	public Opcion obtienePorId(Long id) throws BOException;
	
}
