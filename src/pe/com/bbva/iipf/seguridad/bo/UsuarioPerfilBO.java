package pe.com.bbva.iipf.seguridad.bo;

import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.UsuarioPerfil;
import pe.com.stefanini.core.exceptions.BOException;

public interface UsuarioPerfilBO 
{
	
	public void insertar(UsuarioPerfil objUsuarioPerfil) throws BOException;
	public void modificar(UsuarioPerfil objUsuarioPerfil) throws BOException;
	public List<UsuarioPerfil> listar(UsuarioPerfil objUsuarioPerfil) throws BOException;
	public UsuarioPerfil obtienePorId(Long id) throws BOException;
	public UsuarioPerfil obtienePorCodigo(String codigoRegistro) throws BOException;
	public UsuarioPerfil obtienePorCodigoSinActual(String codigoRegistro, Long id) throws BOException;
	public List<Opcion> listaOpcionesPermiso(String codigoRegistro) throws BOException;
	public List<Tabla> listaRolesByPerfiles(String codigoRegistro) throws BOException ;
		
}
