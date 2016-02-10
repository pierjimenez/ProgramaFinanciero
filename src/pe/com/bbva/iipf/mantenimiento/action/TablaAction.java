package pe.com.bbva.iipf.mantenimiento.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

@Service("tablaAction")
@Scope("prototype")
public class TablaAction extends GenericAction 
{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TablaBO tablaBO ;
	
	private Tabla tabla;
	
	private List<Tabla> tablasPadre;
	
	private List<Tabla> tablas;
	
	private Tabla tablaEdicion;
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tabla getTablaEdicion() {
		return tablaEdicion;
	}

	public void setTablaEdicion(Tabla tablaEdicion) {
		this.tablaEdicion = tablaEdicion;
	}

	public List<Tabla> getTablas() {
		return tablas;
	}

	public void setTablas(List<Tabla> tablas) {
		this.tablas = tablas;
	}

	public Tabla getTabla() 
	{
		if(super.getObjectSession("tabla") != null)
			tabla = (Tabla)super.getObjectSession("tabla");
		return tabla;
	}

	public List<Tabla> getTablasPadre() 
	{
		if(super.getObjectSession("tablasPadre") != null)
			tablasPadre = (List<Tabla>)super.getObjectSession("tablasPadre");
		return tablasPadre;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}
	
	public void setTablasPadre(List<Tabla> tablasPadre) {
		this.tablasPadre = tablasPadre;
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}
	
	public String listaTablas() throws BOException{
		
		try
		{
			Tabla objFiltro = new Tabla();
			objFiltro.setEstado("A");
			tablasPadre = tablaBO.listar(objFiltro);
			
			Tabla comodin = new Tabla();					
			comodin.setDescripcion("TODOS");
			comodin.setId(Long.valueOf("-1"));
			tablasPadre.add(0, comodin);
						
			super.setObjectSession("tablasPadre", tablasPadre);
					
			// ------------------------------------------------
			
			if(super.getObjectSession("tabla") == null)
			{	
				tabla = new Tabla();
				tabla.setEstado("A");
				Tabla objParamPadre = new Tabla();
				objParamPadre.setId(Long.valueOf("-1"));
				tabla.setPadre(objParamPadre);
				
				tablas = tablaBO.listar(tabla);
				
				super.setObjectSession("tabla", tabla);
			}
			else
			{
				tablas = tablaBO.listar((Tabla)getObjectSession("tabla"));
			}
											
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaTablas";
	}
		
	public String buscarTablas() throws BOException
	{
		try
		{
			super.setObjectSession("tabla", null);
			
			tablas = tablaBO.listar(this.getTabla());
									
			super.setObjectSession("tabla", tabla);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaTablas";
	}
	
	public String paginarTablas() throws BOException
	{
		try
		{
			tablas = tablaBO.listar(this.getTabla());
												
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaTablas";
	}
	
	public String agregarTabla() throws BOException
	{
		this.tablasPadre = (List<Tabla>)getObjectSession("tablasPadre");
		tablasPadre.get(0).setDescripcion("NINGUNO");
		tablasPadre.get(0).setId(null);
		
		return "edicionTabla";
	}
	
	public String modificarTabla() throws BOException
	{
		try
		{				
			this.tablasPadre = this.tablaBO.listaPadresSinActual(new Long(this.getId()));

			Tabla comodin = new Tabla();					
			comodin.setDescripcion("NINGUNO");
			comodin.setId(null);
			tablasPadre.add(0, comodin);
			
			super.setObjectSession("tablasPadre", this.tablasPadre);
					
			tablaEdicion = (Tabla)this.tablaBO.obtienePorId(Long.valueOf(this.getId()));
			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
		}
		return "edicionTabla";
	}
	
	public String guardarTabla() throws BOException
	{
		try
		{	
			
			UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
								
			if(this.getId() == null || this.getId().equals(""))
			{			
				this.tablaEdicion.setCodUsuarioCreacion(user.getRegistroHost());
				this.tablaEdicion.setFechaCreacion(new Date());
				
				if(this.tablaEdicion.getPadre().getId() == null && this.tablaBO.obtienePadrePorCodigo(this.tablaEdicion.getCodigo()) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}
				else if(this.tablaEdicion.getPadre().getId() != null && this.tablaBO.obtieneHijaPorCodigo(this.tablaEdicion.getCodigo(), this.tablaEdicion.getPadre().getId()) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}
				
				this.tablaBO.insertar(this.tablaEdicion);
				this.setId(tablaEdicion.getId().toString());
				addActionMessage("Registro exitoso.");
			}
			else
			{
				
				this.tablaEdicion.setCodUsuarioModificacion(user.getRegistroHost());
				this.tablaEdicion.setFechaModificacion(new Date());
				this.tablaEdicion.setId(Long.parseLong(this.getId()));
				
				if(this.tablaEdicion.getPadre().getId() == null && this.tablaBO.obtienePadrePorCodigoSinActual(this.tablaEdicion.getCodigo(), new Long(this.getId())) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}
				else if(this.tablaEdicion.getPadre().getId() != null && this.tablaBO.obtieneHijaPorCodigoSinActual(this.tablaEdicion.getCodigo(), this.tablaEdicion.getPadre().getId(),new Long(this.getId())) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}	
				
				this.tablaBO.modificar(tablaEdicion);
				addActionMessage("Actualización exitosa.");
			}			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
		}
		finally
		{
			if(this.getId() != null && !this.getId().equals(""))
			{
				this.tablasPadre = this.tablaBO.listaPadresSinActual(new Long(this.getId()));
				tablasPadre.get(0).setDescripcion("NINGUNO");
				tablasPadre.get(0).setId(null);	
				super.setObjectSession("tablasPadre", this.tablasPadre);
			}
			else
			{
				this.tablasPadre = (List<Tabla>)getObjectSession("tablasPadre");
				tablasPadre.get(0).setDescripcion("NINGUNO");
				tablasPadre.get(0).setId(null);
			}
		}
		return "edicionTabla";
	}
	
}
