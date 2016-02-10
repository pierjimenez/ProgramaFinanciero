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
import pe.com.bbva.iipf.seguridad.bo.RolPerfilBO;
import pe.com.bbva.iipf.seguridad.model.entity.RolPerfil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.StringUtil;


@Service("rolPerfilAction")
@Scope("prototype")
public class RolPerfilAction extends GenericAction {
	
	Logger logger = Logger.getLogger(this.getClass());
	private String idPerfil;
	
	private List<Tabla> perfiles;	
	private String idroles;
	
	
	@Resource
	private TablaBO tablaBO;
	
	@Resource
	private RolPerfilBO rolPerfilBO;
	
	
	public String listaRolPerfil() throws BOException
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
					
		return "edicionRolPerfil";
	}
	
	public void loadRolByPerfil() {
		List<RolPerfil> listaRolPerfil = new ArrayList<RolPerfil>();
		List<RolPerfil> olistaRolPerfil = new ArrayList<RolPerfil>();
		List<Tabla> olistaRolesTabla = new ArrayList<Tabla>();
		String idPerfil=getIdPerfil();
		Tabla oPerfil=new Tabla();
		oPerfil.setId(Long.valueOf(idPerfil));
		
		try {
			//idPerfil
			olistaRolPerfil = rolPerfilBO.getListaRolPerfil(Long.valueOf(idPerfil));
			
			olistaRolesTabla=tablaBO.obtieneHijaCodigoPadre(Constantes.COD_PADRE_VALOR_ROL);
			listaRolPerfil=new ArrayList<RolPerfil>();
			if (olistaRolesTabla!=null && olistaRolesTabla.size()>0){
				
				for(Tabla oroles: olistaRolesTabla){	
					RolPerfil orolPerfil=new RolPerfil();	
					orolPerfil.setId(null);
					orolPerfil.setEstado("I");
					orolPerfil.setPerfil(oPerfil);
					orolPerfil.setRol(oroles);					
					for (RolPerfil rolPerfil: olistaRolPerfil){				
						if(oroles.getId()!=null && oroles.getId().equals(rolPerfil.getRol().getId())){
							orolPerfil.setEstado("A");
							orolPerfil.setId(rolPerfil.getId());
							break;
						}						
					}
					listaRolPerfil.add(orolPerfil);
				}
			}
			
			
			retornarTablaSaldos(listaRolPerfil);
				
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public void retornarTablaSaldos(List<RolPerfil> olistaRolPerfil){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {		
	    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbRemRoles\" class=\"ui-widget ui-widget-content\" >");
			stb.append("<thead>");	
		        stb.append("<tr>");
			        stb.append("<th>#</th>");
			        stb.append("<th>Elija</th>");
			        stb.append("<th>id</th>");
			        stb.append("<th>Rol</th>");
		        stb.append("</tr>");
	        stb.append("</thead>");
        stb.append("<tbody>");
      
		for(RolPerfil orolPerfil: olistaRolPerfil){
			contador++;
			String id=orolPerfil.getRol().getId()==null?"":orolPerfil.getRol().getId().toString();
			String rol=orolPerfil.getRol().getDescripcion()==null?"":orolPerfil.getRol().getDescripcion().toString();

			stb.append("<tr>");
			stb.append("<td>"+contador+"</td>");
			if (orolPerfil.getEstado().equals("A")){
				stb.append("<td><input type=\"checkbox\" name=\"chkRoles\" id=\"idElijarol\" value="+ id+ " checked class=\"ui-button ui-widget ui-state-default ui-corner-all\"/></td>");
			}else{
				stb.append("<td><input type=\"checkbox\" name=\"chkRoles\" id=\"idElijarol\" value="+ id+ " class=\"ui-button ui-widget ui-state-default ui-corner-all\"/></td>");
			}
			stb.append("<td>"+(id)+"</td>");
			stb.append("<td align=\"left\">"+(rol)+"</td>");		
			stb.append("</tr>");

		}
		stb.append("</tbody>");
		 stb.append("</table>");
		
        out.print(stb.toString());
		
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	public void guardarRolPerfil() throws BOException, IOException
	{
		StringBuffer sbHtml = null;
		
		try
		{
			String idPerfil=getIdPerfil();
			String strRoles=this.getIdroles();
			if(strRoles != null)
			{
				
				UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
				
				List<RolPerfil> olistaRolPerfil = new ArrayList<RolPerfil>();
				
				if (strRoles.length()>0){					
					String[] grupos = strRoles.split("\\|");				
					RolPerfil rolPerfil = null;
					Tabla rol = null;
					Tabla perfil = null;
					
					for(int index = 0; index < grupos.length; index++)
					{	
							rolPerfil = new RolPerfil();				
							rolPerfil.setId(null);					
							rolPerfil.setCodUsuarioCreacion(user.getRegistroHost());
							rolPerfil.setFechaCreacion(new Date());						
							rolPerfil.setEstado("A");							
							rol = new Tabla();
							rol.setId(new Long(grupos[index]));
							rolPerfil.setRol(rol);							
							perfil = new Tabla();
							perfil.setId(Long.valueOf(idPerfil));
							rolPerfil.setPerfil(perfil);
							
							olistaRolPerfil.add(rolPerfil);																				
					}
				}
				rolPerfilBO.actualizarRolPerfil(olistaRolPerfil,Long.valueOf(idPerfil));
				//cargarROl();							
			}
					
			sbHtml = new StringBuffer();			
			sbHtml.append("OK");
				  	        
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
	        if(out !=null) out.close();
		}
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

	public String getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getIdroles() {
		return idroles;
	}

	public void setIdroles(String idroles) {
		this.idroles = idroles;
	}

	
	
	

}
