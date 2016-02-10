package pe.com.bbva.iipf.seguridad.bo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.bo.UsuarioPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.RolPerfil;
import pe.com.bbva.iipf.seguridad.model.entity.UsuarioPerfil;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("usuarioPerfilBO")
public class UsuarioPerfilBOImpl extends GenericBOImpl<UsuarioPerfil> implements UsuarioPerfilBO {
	@Override
	public void beforeSave(UsuarioPerfil objUsuarioPerfil) throws BOException
	{

	}
	@Override
	public boolean validate(UsuarioPerfil objUsuarioPerfil) throws BOException{
		return true;
	}
	@Override
	public void afterSave(UsuarioPerfil objUsuarioPerfil) throws BOException{
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertar(UsuarioPerfil objUsuarioPerfil) throws BOException {
		super.save(objUsuarioPerfil);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modificar(UsuarioPerfil objUsuarioPerfil) throws BOException {
		super.save(objUsuarioPerfil);
	}
	@Override
	public List<UsuarioPerfil> listar(UsuarioPerfil objUsuarioPerfil) throws BOException {
		
		List<UsuarioPerfil> usuariosPerfil = null;
		UsuarioPerfil usuPerfil = null;
				
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select usu_per.id_usuario_perfil, usu_per.estado, usu_per.cod_usuario_creacion, ");
			sb.append("usu_per.cod_usuario_modificacion, usu_per.fecha_creacion, usu_per.fecha_modificacion, ");
			sb.append("usu_per.usuario_registro, usu_per.id_perfil, tab_per.descripcion as perfil, ");
			sb.append("usu_per.id_nivel_acceso, tab_niv.descripcion as nivelacceso ");
			sb.append("from PROFIN.tiipf_usuario_perfil usu_per inner join PROFIN.tiipf_tabla_de_tabla tab_per ");
			sb.append("on usu_per.id_perfil = tab_per.id_tabla inner join PROFIN.tiipf_tabla_de_tabla tab_niv ");
			sb.append("on usu_per.id_nivel_acceso = tab_niv.id_tabla ");
										
			boolean isAnd = false;
			boolean isWhere = false;
						
			if(objUsuarioPerfil.getUsuarioRegistro() != null && !objUsuarioPerfil.getUsuarioRegistro().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" upper(usu_per.usuario_registro) like '%");
				sb.append(objUsuarioPerfil.getUsuarioRegistro().toUpperCase());
				sb.append("%'");
				isAnd = true;
			}
			if(objUsuarioPerfil.getEstado() != null && !objUsuarioPerfil.getEstado().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" usu_per.estado = '");
				sb.append(objUsuarioPerfil.getEstado());
				sb.append("'");
				isAnd = true;
			}
			if(objUsuarioPerfil.getPerfil() != null && objUsuarioPerfil.getPerfil().getId() != null)
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" usu_per.id_perfil = ");
				sb.append(objUsuarioPerfil.getPerfil().getId());
				isAnd = true;
			}
			if(objUsuarioPerfil.getNivelAcceso() != null && objUsuarioPerfil.getNivelAcceso().getId() != null)
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" usu_per.id_nivel_acceso = ");
				sb.append(objUsuarioPerfil.getNivelAcceso().getId());
				isAnd = true;
			}
			
			sb.append(" order by usu_per.id_usuario_perfil ");
			
			List insurance = super.executeSQL(sb.toString());
			
			usuariosPerfil = new ArrayList<UsuarioPerfil>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				usuPerfil = new UsuarioPerfil();
				usuPerfil.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					usuPerfil.setEstado(amount[1].toString());
				if(amount[6] != null)
					usuPerfil.setUsuarioRegistro(amount[6].toString());
							
				if(amount[7] != null && amount[8] != null)
				{
					Tabla objPerfil = new Tabla();
					objPerfil.setId(new Long(amount[7].toString()));
					objPerfil.setDescripcion(amount[8].toString());
					usuPerfil.setPerfil(objPerfil);					
				}
				if(amount[9] != null && amount[10] != null)
				{
					Tabla objNivelAcceso = new Tabla();
					objNivelAcceso.setId(new Long(amount[9].toString()));
					objNivelAcceso.setDescripcion(amount[10].toString());
					usuPerfil.setNivelAcceso(objNivelAcceso);					
				}
				usuariosPerfil.add(usuPerfil);
			}
		} catch (Exception e) {					
			throw new BOException(e.getMessage());
		}

		return usuariosPerfil;
		
	}

	
	@Override
	public UsuarioPerfil obtienePorCodigo(String codigoRegistro)
			throws BOException {
		
		List<UsuarioPerfil> usuariosPerfil = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigoRegistro);
			usuariosPerfil = super.listNamedQuery("obtieneUsuarioPerfilPorCodigo", parametros);
		} 
		catch (Exception e) {					
			throw new BOException(e.getMessage());
		}
		
		if(usuariosPerfil.size() == 0)
			return null;
		else
			return usuariosPerfil.get(0);
	}
	
	
	public List<UsuarioPerfil> getPerfilesPorCodigo(String codigoRegistro)
			throws BOException {

		List<UsuarioPerfil> listaUsuarioPerfil = new ArrayList<UsuarioPerfil>();

		try {
			List parametros = new ArrayList();
			parametros.add(codigoRegistro);
			listaUsuarioPerfil = super.listNamedQuery(
					"obtieneUsuarioPerfilPorCodigo", parametros);
		} catch (Exception e) {
			listaUsuarioPerfil = new ArrayList<UsuarioPerfil>();
		}
		return listaUsuarioPerfil;
	}
	
	@Override
	public UsuarioPerfil obtienePorCodigoSinActual(
			String codigoRegistro, Long id) throws BOException {
		
		List<UsuarioPerfil> usuariosPerfil = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigoRegistro);
			parametros.add(id);
			usuariosPerfil = super.listNamedQuery("obtieneUsuarioPerfilPorCodigoSinActual", parametros);
		} 
		catch (Exception e) {					

			throw new BOException(e.getMessage());
		}
		
		if(usuariosPerfil.size() == 0)
			return null;
		else
			return usuariosPerfil.get(0);
	}
	@Override
	public UsuarioPerfil obtienePorId(Long id) throws BOException 
	{
		List<UsuarioPerfil> usuariosPerfil = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(id);
			usuariosPerfil = super.listNamedQuery("obtieneUsuarioPerfilPorId", parametros);
		} 
		catch (Exception e) {					
			throw new BOException(e.getMessage());
		}
		
		if(usuariosPerfil.size() == 0)
			return null;
		else
			return usuariosPerfil.get(0);
	}
	@Override
	public List<Opcion> listaOpcionesPermiso(String codigoRegistro) throws BOException 
	{
		List<Opcion> opciones = null;
		Opcion opc = null;
		Opcion opcSup = null;
		
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select opc.id_opcion, opc.codigo, opc.nombre, opc.action, opc.opc_superior ");
			sb.append("from PROFIN.tiipf_opcion opc inner join PROFIN.tiipf_opcion_perfil opc_perf ");
			sb.append("on opc.id_opcion = opc_perf.id_opcion ");
			sb.append("inner join PROFIN.tiipf_usuario_perfil usu_perf ");
			sb.append("on opc_perf.id_perfil = usu_perf.id_perfil ");
			sb.append("where opc.estado = 'A' and opc_perf.estado = 'A' and ");
			sb.append("usu_perf.usuario_registro = '");
			sb.append(codigoRegistro);
			sb.append("'");
																		
			
			List insurance = super.executeSQL(sb.toString());
			
			opciones = new ArrayList<Opcion>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				opc = new Opcion();
				opc.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					opc.setCodigo(amount[1].toString());	
				if(amount[2] != null)
					opc.setNombre(amount[2].toString());
				if(amount[3] != null)
					opc.setAction(amount[3].toString());
				if(amount[4] != null)
				{
					opcSup = new Opcion();
					opcSup.setId(Long.valueOf(amount[4].toString()));
					opc.setSuperior(opcSup);
				}
					
				opciones.add(opc);
			}
		} catch (Exception e) {					
			throw new BOException(e.getMessage());
		}

		return opciones;
	}
	
	@Override
	public List<Tabla> listaRolesByPerfiles(String codigoRegistro) throws BOException 
	{
		List<UsuarioPerfil> listaUsuarioPerfil = new ArrayList<UsuarioPerfil>();
		List<Tabla> roles = new ArrayList<Tabla>();
		Tabla rol = null;
		
		try {
			
			listaUsuarioPerfil= getPerfilesPorCodigo(codigoRegistro);
			String cadenaIdPerfiles="";
			boolean primero=true;
			if (listaUsuarioPerfil!=null && listaUsuarioPerfil.size()>0){
				for (UsuarioPerfil operfil: listaUsuarioPerfil){
					String idPerfil=operfil.getPerfil().getId()==null?"":operfil.getPerfil().getId().toString();
					if (primero){
						cadenaIdPerfiles="'"+idPerfil+"'";
						primero=false;
					}else{
						cadenaIdPerfiles=cadenaIdPerfiles +","+"'"+idPerfil+"'";
					}
				}
				
			}
			        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT PR.ID_ROL IDROL,RO.CODIGO CODIGOROL,RO.DESCRIPCION  ROL"); 
			sb.append(" FROM profin.tiipf_perfil_rol  PR,");
			sb.append(" profin.tiipf_tabla_de_tabla PE,profin.tiipf_tabla_de_tabla RO"); 
			sb.append(" WHERE PR.ID_PERFIL=PE.ID_TABLA");
			sb.append(" AND PR.ID_ROL=RO.ID_TABLA ");
			sb.append(" AND PE.ID_TABLA IN ("+ cadenaIdPerfiles +")");
			sb.append(" GROUP BY PR.ID_ROL,RO.CODIGO ,RO.DESCRIPCION ");		
														
			
			List insurance = super.executeSQL(sb.toString());			
			roles = new ArrayList<Tabla>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();				
				rol = new Tabla();
				rol.setId(Long.valueOf(amount[0].toString()));
				
				if(amount[1] != null)
					rol.setCodigo(amount[1].toString());	
				if(amount[2] != null)
					rol.setDescripcion(amount[2].toString());				
					
				roles.add(rol);
			}
		} catch (Exception e) {		
			roles = new ArrayList<Tabla>();
			throw new BOException(e.getMessage());
			
		}

		return roles;
	}
	
	
}
