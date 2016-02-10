package pe.com.bbva.iipf.seguridad.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.bo.UsuarioPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.UsuarioPerfil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;

@Service("usuarioPerfilAction")
@Scope("prototype")
public class UsuarioPerfilAction extends GenericAction 
{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private UsuarioPerfilBO usuarioPerfilBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private UsuarioPerfil usuarioPerfil;
	
	private List<UsuarioPerfil> usuarioPerfiles;
	
	private UsuarioPerfil usuarioPerfilEdicion;
	
	private String id;
	
	private List<Tabla> perfiles;
	
	private List<Tabla> nivelesAcceso;
		
	public List<Tabla> getPerfiles() 
	{
		if(super.getObjectSession("perfiles") != null)
			perfiles = (List<Tabla>)super.getObjectSession("perfiles");
		return perfiles;
	}

	public void setPerfiles(List<Tabla> perfiles) {
		this.perfiles = perfiles;
	}

	public List<Tabla> getNivelesAcceso() {
		if(super.getObjectSession("nivelesAcceso") != null)
			nivelesAcceso = (List<Tabla>)super.getObjectSession("nivelesAcceso");
		return nivelesAcceso;
	}

	public void setNivelesAcceso(List<Tabla> nivelesAcceso) {
		this.nivelesAcceso = nivelesAcceso;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UsuarioPerfil getUsuarioPerfilEdicion() {
		return usuarioPerfilEdicion;
	}

	public void setUsuarioPerfilEdicion(UsuarioPerfil usuarioPerfilEdicion) {
		this.usuarioPerfilEdicion = usuarioPerfilEdicion;
	}

	public List<UsuarioPerfil> getUsuarioPerfiles() {
		return usuarioPerfiles;
	}

	public void setUsuarioPerfiles(List<UsuarioPerfil> usuarioPerfils) {
		this.usuarioPerfiles = usuarioPerfils;
	}

	public UsuarioPerfil getUsuarioPerfil() 
	{
		if(super.getObjectSession("usuarioPerfil") != null)
			usuarioPerfil = (UsuarioPerfil)super.getObjectSession("usuarioPerfil");
		return usuarioPerfil;
	}

	public UsuarioPerfilBO getUsuarioPerfilBO() {
		return usuarioPerfilBO;
	}
	
	public void setUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
		this.usuarioPerfil = usuarioPerfil;
	}

	public void setUsuarioPerfilBO(UsuarioPerfilBO usuarioPerfilBO) {
		this.usuarioPerfilBO = usuarioPerfilBO;
	}
	
	public String listaUsuarioPerfiles() throws BOException{
		
		try
		{
			
			this.perfiles = tablaBO.listarHijos(Constantes.ID_TABLA_PERFIL);
			this.nivelesAcceso = tablaBO.listarHijos(Constantes.ID_TABLA_NIVEL_ACCESO);
			
			Tabla comodinPerfil = new Tabla();					
			comodinPerfil.setDescripcion("TODOS");
			this.perfiles.add(0, comodinPerfil);
			
			Tabla comodinNivel = new Tabla();					
			comodinNivel.setDescripcion("TODOS");
			this.nivelesAcceso.add(0, comodinNivel);
			
			super.setObjectSession("perfiles", this.perfiles);
			super.setObjectSession("nivelesAcceso", this.nivelesAcceso);
			
			// ---------------------------------------------------
								
			if(super.getObjectSession("usuarioPerfil") == null)
			{	
				usuarioPerfil = new UsuarioPerfil();
				usuarioPerfil.setEstado("A");
				
				usuarioPerfiles = usuarioPerfilBO.listar(usuarioPerfil);
				
				super.setObjectSession("usuarioPerfil", usuarioPerfil);
			}
			else
			{
				usuarioPerfiles = usuarioPerfilBO.listar((UsuarioPerfil)getObjectSession("usuarioPerfil"));
			}
											
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
					
		return "listaUsuarioPerfiles";
	}
		
	public String buscarUsuarioPerfiles() throws BOException
	{
		try
		{
			super.setObjectSession("usuarioPerfil", null);
			
			usuarioPerfiles = usuarioPerfilBO.listar(this.usuarioPerfil);
									
			super.setObjectSession("usuarioPerfil", this.usuarioPerfil);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
					
		return "listaUsuarioPerfiles";
	}
	
	public String paginarUsuarioPerfiles() throws BOException
	{
		try
		{
			usuarioPerfiles = usuarioPerfilBO.listar(this.getUsuarioPerfil());										
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
					
		return "listaUsuarioPerfiles";
	}
	
	public String agregarUsuarioPerfil() throws BOException
	{		
		
		this.getPerfiles().get(0).setDescripcion("SELECCIONE");
		this.getNivelesAcceso().get(0).setDescripcion("SELECCIONE");
									
		return "edicionUsuarioPerfil";
		
	}
	
	public String modificarUsuarioPerfil() throws BOException
	{
		try
		{				
			this.getPerfiles().get(0).setDescripcion("SELECCIONE");
			this.getNivelesAcceso().get(0).setDescripcion("SELECCIONE");
													
			usuarioPerfilEdicion = (UsuarioPerfil)this.usuarioPerfilBO.obtienePorId(Long.valueOf(this.getId()));
			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		return "edicionUsuarioPerfil";
	}
	
	public String guardarUsuarioPerfil() throws BOException
	{
		try
		{	
			
			UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
			
			if(this.getId() == null || this.getId().equals(""))
			{		
				this.usuarioPerfilEdicion.setCodUsuarioCreacion(user.getRegistroHost());
				this.usuarioPerfilEdicion.setFechaCreacion(new Date());
				
				if(usuarioPerfilBO.obtienePorCodigo(this.usuarioPerfilEdicion.getUsuarioRegistro()) != null)
				{
					throw new Exception("El código de registro ingresado ya existe, ingrese uno diferente.");
				}
				
				this.usuarioPerfilBO.insertar(this.usuarioPerfilEdicion);
				this.setId(this.usuarioPerfilEdicion.getId().toString());
				addActionMessage("Registro exitoso.");
			}
			else
			{
				this.usuarioPerfilEdicion.setCodUsuarioModificacion(user.getRegistroHost());
				this.usuarioPerfilEdicion.setFechaModificacion(new Date());
				usuarioPerfilEdicion.setId(Long.parseLong(this.getId()));
				
				if(usuarioPerfilBO.obtienePorCodigoSinActual(this.usuarioPerfilEdicion.getUsuarioRegistro(),new Long(this.getId())) != null)
				{
					throw new Exception("El código de registro ingresado ya existe, ingrese uno diferente.");
				}			
				this.usuarioPerfilBO.modificar(this.usuarioPerfilEdicion);
				addActionMessage("Actualización exitosa.");
			}	
						
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
			logger.error(StringUtil.getStackTrace(ex));
		}
		
		return "edicionUsuarioPerfil";
		
	}
	
}
