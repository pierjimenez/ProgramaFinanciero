package pe.com.bbva.iipf.seguridad.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.bo.OpcionPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.seguridad.model.entity.OpcionPerfil;
import pe.com.bbva.iipf.seguridad.model.entity.OpcionPermiso;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

@Service("perfilAction")
@Scope("prototype")
public class OpcionPerfilAction extends GenericAction 
{

	Logger logger = Logger.getLogger(this.getClass());
	
	private Long idPerfil;
	
	private List<Tabla> perfiles;
	
	@Resource
	private TablaBO tablaBO;
	
	@Resource
	private OpcionPerfilBO opcionPerfilBO;
	
	private List<OpcionPermiso> opcionesPermiso;
	
	private String permisos;
		
	public String getPermisos() {
		return permisos;
	}

	public void setPermisos(String permisos) {
		this.permisos = permisos;
	}

	public List<OpcionPermiso> getOpcionesPermiso() {
		return opcionesPermiso;
	}

	public void setOpcionesPermiso(List<OpcionPermiso> opcionesPermiso) {
		this.opcionesPermiso = opcionesPermiso;
	}

	public OpcionPerfilBO getOpcionPerfilBO() {
		return opcionPerfilBO;
	}

	public void setOpcionPerfilBO(OpcionPerfilBO opcionPerfilBO) {
		opcionPerfilBO = opcionPerfilBO;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}

	public List<Tabla> getPerfiles() 
	{
		if(super.getObjectSession("perfiles") != null)
			perfiles = (List<Tabla>)super.getObjectSession("perfiles");
		return perfiles;
	}

	public void setPerfiles(List<Tabla> perfiles) {
		this.perfiles = perfiles;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String listaOpcionPerfil() throws BOException
	{		
		try
		{
			this.perfiles = tablaBO.listarHijos(Constantes.ID_TABLA_PERFIL);
			Tabla comodinPerfil = new Tabla();					
			comodinPerfil.setDescripcion("SELECCIONE");
			this.perfiles.add(0, comodinPerfil);
			
			super.setObjectSession("perfiles", perfiles);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "edicionOpcionPerfil";
	}
	
	public void cargarOpcionesPorPerfil() throws BOException, IOException
	{
		
		StringBuffer sbHtml = null;
		
		try
		{
			if(this.idPerfil != null)
			{
				cargarOpciones();
				
				sbHtml = new StringBuffer();
				sbHtml.append("<ul id=\"arbol\">");
				for(int index = 0; index < this.opcionesPermiso.size(); index++)
				{
					sbHtml.append(this.opcionesPermiso.get(index).getHtml());
				}
				sbHtml.append("</ul>");
				
			}
					
		}
		catch(Exception ex)
		{	
			sbHtml = new StringBuffer();		
			sbHtml.append("ERROR : " + ex.getMessage());					       	     
		}	
		finally
		{
			if(sbHtml == null)
			{
				sbHtml = new StringBuffer();
				sbHtml.append("Seleccione perfil.");
			}
			getResponse().setContentType("text/html");   
	        PrintWriter out = getResponse().getWriter(); 
	        out.print(sbHtml.toString());
		}
		
	}
	
	public void guardarPermisos() throws BOException, IOException
	{
		StringBuffer sbHtml = null;
		
		try
		{
			if(this.permisos != null)
			{
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
				
				List<OpcionPerfil> opciones = new ArrayList<OpcionPerfil>();
				String[] grupos = permisos.split("\\|");
				OpcionPerfil opcPerfil = null;
				Opcion opc = null;
				Tabla perfil = null;
				
				for(int index = 0; index < grupos.length; index++)
				{
					String[] datos = grupos[index].toString().split("\\-");
					
					if(datos[2].equals(datos[3]))
							continue;
					else
					{
						opcPerfil = new OpcionPerfil();
						if(!datos[0].equals("0"))
						{
							opcPerfil.setId(new Long(datos[0]));
							opcPerfil.setCodUsuarioModificacion(user.getRegistroHost());
							opcPerfil.setFechaModificacion(new Date());
						}
						else
						{
							opcPerfil.setCodUsuarioCreacion(user.getRegistroHost());
							opcPerfil.setFechaCreacion(new Date());
						}
						opcPerfil.setEstado(datos[3]);
						opc = new Opcion();
						opc.setId(new Long(datos[1]));
						opcPerfil.setOpcion(opc);
						perfil = new Tabla();
						perfil.setId(this.idPerfil);
						opcPerfil.setPerfil(perfil);
						opciones.add(opcPerfil);
					}														
				}
				this.opcionPerfilBO.actualizarPermisos(opciones);
				cargarOpciones();							
			}
					
			sbHtml = new StringBuffer();
			
			sbHtml.append("<ul id=\"arbol\">");
			for(int index = 0; index < this.opcionesPermiso.size(); index++)
			{
				sbHtml.append(this.opcionesPermiso.get(index).getHtml());
			}
			sbHtml.append("</ul>");
				  	        
		}
		catch(Exception ex)
		{		
			sbHtml = new StringBuffer();		
			sbHtml.append("ERROR : " + ex.getMessage());					       	     
		}	
		finally
		{
			getResponse().setContentType("text/html");   
	        PrintWriter out = getResponse().getWriter(); 
	        out.print(sbHtml.toString());
		}
	}
	
	private void cargarOpciones() throws BOException
	{
		this.opcionesPermiso = new ArrayList<OpcionPermiso>();				
		List<OpcionPerfil> opcionesPerfil = this.opcionPerfilBO.listaPorPerfilTotal(idPerfil);
		
		for(int i = 0; i < opcionesPerfil.size(); i++)
		{
			OpcionPerfil op = opcionesPerfil.get(i);
			
			if(op.getOpcion().getSuperior() == null)
			{
				OpcionPermiso opc = new OpcionPermiso();
				opc.setCodigo((op.getId() == null ? "0" : op.getId().toString()) + "-" + op.getOpcion().getId().toString() + "-" + (op.getId() == null || (op.getId() != null && op.getEstado().equals("I")) ? "I" : "A"));
				opc.setSeleccionado((op.getId() != null && op.getEstado().equals("A")));
				opc.setDescripcion(op.getOpcion().getNombre());	
				this.opcionesPermiso.add(opc);
				cargarOpciones(opc, opcionesPerfil, op.getOpcion().getId());	
			}
		}
	}
	
	private void cargarOpciones(OpcionPermiso objPadre, List<OpcionPerfil> lista, Long idPadre)
	{
				
		List<OpcionPermiso> opciones = new ArrayList<OpcionPermiso>();
		
		for(int i = 0; i < lista.size(); i++)
		{
			OpcionPerfil op = lista.get(i);
			
			if(op.getOpcion().getSuperior() != null &&
					op.getOpcion().getSuperior().getId().equals(idPadre))
			{
				
				OpcionPermiso opc = new OpcionPermiso();
				opc.setCodigo((op.getId() == null ? "0" : op.getId().toString()) + "-" + op.getOpcion().getId().toString() + "-" + (op.getId() == null || (op.getId() != null && op.getEstado().equals("I")) ? "I" : "A"));
				opc.setSeleccionado((op.getId() != null && op.getEstado().equals("A")));
				opc.setDescripcion(op.getOpcion().getNombre());
				opciones.add(opc);
				cargarOpciones(opc, lista, op.getOpcion().getId());							
			}
		}
		
		objPadre.setHijos(opciones);
		
	}
	
}
