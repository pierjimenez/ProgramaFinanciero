package pe.com.bbva.iipf.seguridad.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.bo.OpcionBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

@Service("opcionAction")
@Scope("prototype")
public class OpcionAction extends GenericAction 
{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private OpcionBO opcionBO;
	
	@Resource
	private TablaBO tablaBO;
	
	private Opcion opcion;
	
	private List<Opcion> opciones;
	
	private Opcion opcionEdicion;
	
	private String id;
	
	private List<Tabla> opcionesTipo;
	
	private List<Opcion> opcionesEdicion;
	
	public List<Opcion> getOpcionesEdicion() {
		return opcionesEdicion;
	}

	public void setOpcionesEdicion(List<Opcion> opcionesEdicion) {
		this.opcionesEdicion = opcionesEdicion;
	}
	
	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}
	
	public List<Tabla> getOpcionesTipo() {
		if(super.getObjectSession("opcionesTipo") != null)
			opcionesTipo = (List<Tabla>)super.getObjectSession("opcionesTipo");
		return opcionesTipo;
	}

	public void setOpcionesTipo(List<Tabla> opcionesTipo) 
	{
		this.opcionesTipo = opcionesTipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Opcion getOpcionEdicion() {
		return opcionEdicion;
	}

	public void setOpcionEdicion(Opcion opcionEdicion) {
		this.opcionEdicion = opcionEdicion;
	}

	public List<Opcion> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<Opcion> opcions) {
		this.opciones = opcions;
	}

	public Opcion getOpcion() 
	{
		if(super.getObjectSession("opcion") != null)
			opcion = (Opcion)super.getObjectSession("opcion");
		return opcion;
	}

	public OpcionBO getOpcionBO() {
		return opcionBO;
	}
	
	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}

	public void setOpcionBO(OpcionBO opcionBO) {
		this.opcionBO = opcionBO;
	}
	
	public String listaOpciones() throws BOException{
		
		try
		{
			
			this.opcionesTipo = tablaBO.listarHijos(Constantes.ID_TABLA_TIPO_MENU);
			
			Tabla comodin = new Tabla();					
			comodin.setDescripcion("TODOS");
			this.opcionesTipo.add(0, comodin);
				
			super.setObjectSession("opcionesTipo", opcionesTipo);
			
			// ---------------------------------------------------
								
			if(super.getObjectSession("opcion") == null)
			{	
				opcion = new Opcion();
				opcion.setEstado("A");
				
				opciones = opcionBO.listar(opcion);
				
				super.setObjectSession("opcion", opcion);
			}
			else
			{
				opciones = opcionBO.listar((Opcion)getObjectSession("opcion"));
			}
											
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaOpciones";
	}
		
	public String buscarOpciones() throws BOException
	{
		try
		{
			super.setObjectSession("opcion", null);
			
			opciones = opcionBO.listar(this.opcion);
									
			super.setObjectSession("opcion", this.opcion);
			
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaOpciones";
	}
	
	public String paginarOpciones() throws BOException
	{
		try
		{
			opciones = opcionBO.listar(this.getOpcion());										
		}
		catch(Exception ex)
		{			
			super.addActionError(ex.getMessage());
		}
					
		return "listaOpciones";
	}
	
	public String agregarOpcion() throws BOException
	{		
		
		this.getOpcionesTipo().get(0).setDescripcion("SELECCIONE");
					
		Opcion objFiltro = new Opcion();
		objFiltro.setEstado("A");
		this.opcionesEdicion = opcionBO.listar(objFiltro);
		Opcion objComodin = new Opcion();
		objComodin.setNombre("NINGUNO");
		this.opcionesEdicion.add(0,objComodin);
				
		return "edicionOpcion";
	}
	
	public String modificarOpcion() throws BOException
	{
		try
		{				
			this.getOpcionesTipo().get(0).setDescripcion("SELECCIONE");
			
			this.opcionesEdicion = opcionBO.listarSinActual(new Long(this.getId()));
			Opcion objComodin = new Opcion();
			objComodin.setNombre("NINGUNO");
			this.opcionesEdicion.add(0,objComodin);
										
			opcionEdicion = (Opcion)this.opcionBO.obtienePorId(Long.valueOf(this.getId()));
			
		}
		catch(Exception ex)
		{			
			addActionError(ex.getMessage());
		}
		return "edicionOpcion";
	}
	
	public String guardarOpcion() throws BOException
	{
		try
		{	
			
			UsuarioSesion user = (UsuarioSesion)super.getObjectSession(Constantes.USUARIO_SESSION);
			
			if(this.getId() == null || this.getId().equals(""))
			{		
				
				this.opcionEdicion.setCodUsuarioCreacion(user.getRegistroHost());
				this.opcionEdicion.setFechaCreacion(new Date());
				
				if(opcionBO.obtienePorCodigo(this.opcionEdicion.getCodigo()) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}
				
				this.opcionBO.insertar(this.opcionEdicion);
				this.setId(this.opcionEdicion.getId().toString());
				addActionMessage("Registro exitoso.");
			}
			else
			{
				this.opcionEdicion.setCodUsuarioModificacion(user.getRegistroHost());
				this.opcionEdicion.setFechaModificacion(new Date());
				opcionEdicion.setId(Long.parseLong(this.getId()));
				
				if(opcionBO.obtienePorCodigoSinActual(this.opcionEdicion.getCodigo(),new Long(this.getId())) != null)
				{
					throw new Exception("El código ingresado ya existe, ingrese uno diferente.");
				}				
				this.opcionBO.modificar(this.opcionEdicion);
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
				this.opcionesEdicion = opcionBO.listarSinActual(new Long(this.getId()));
				Opcion objComodin = new Opcion();
				objComodin.setNombre("NINGUNO");
				this.opcionesEdicion.add(0,objComodin);
			}
			else
			{
				Opcion objFiltro = new Opcion();
				objFiltro.setEstado("A");
				this.opcionesEdicion = opcionBO.listar(objFiltro);
				Opcion objComodin = new Opcion();
				objComodin.setNombre("NINGUNO");
				this.opcionesEdicion.add(0,objComodin);
			}
		}
		
		return "edicionOpcion";
	}
	
}
