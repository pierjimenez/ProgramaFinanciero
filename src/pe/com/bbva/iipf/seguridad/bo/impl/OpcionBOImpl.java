package pe.com.bbva.iipf.seguridad.bo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.seguridad.bo.OpcionBO;
import pe.com.bbva.iipf.seguridad.model.entity.Opcion;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("opcionBO")
public class OpcionBOImpl extends GenericBOImpl<Opcion> implements OpcionBO {
	@Override
	public void beforeSave(Opcion objOpcion) throws BOException
	{
		if(objOpcion.getSuperior().getId()== null)
		{
			objOpcion.setSuperior(null); 
		}
	}
	@Override
	public boolean validate(Opcion objOpcion) throws BOException{
		return true;
	}
	@Override
	public void afterSave(Opcion objOpcion) throws BOException{
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertar(Opcion objOpcion) throws BOException 
	{
		super.save(objOpcion);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modificar(Opcion objOpcion) throws BOException 
	{
		super.save(objOpcion);
	}
	@Override
	public List<Opcion> listar(Opcion objOpcion) throws BOException 
	{
		
		List<Opcion> opciones = null;
		Opcion opc = null;
				
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select opc.id_opcion, opc.estado, opc.codigo, opc.nombre, opc.action, ");
			sb.append("opc.regla_naveg, opc.metodo, opc.detalle, opc.opc_superior,  opc_sup.nombre as nomb_sup, ");
			sb.append("opc.tipo, tip_opc.descripcion as desc_tipo ");
			sb.append("from PROFIN.tiipf_opcion opc left join PROFIN.tiipf_tabla_de_tabla tip_opc ");
			sb.append("on opc.tipo = tip_opc.id_tabla ");
			sb.append("left join PROFIN.tiipf_opcion opc_sup on opc.opc_superior = opc_sup.id_opcion ");
										
			boolean isAnd = false;
			boolean isWhere = false;
						
			if(objOpcion.getNombre() != null && !objOpcion.getNombre().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" upper(opc.nombre) like '%");
				sb.append(objOpcion.getNombre().toUpperCase());
				sb.append("%'");
				isAnd = true;
			}
			if(objOpcion.getEstado() != null && !objOpcion.getEstado().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" opc.estado = '");
				sb.append(objOpcion.getEstado());
				sb.append("' ");
				isAnd = true;
			}
			if(objOpcion.getTipo() != null && objOpcion.getTipo().getId() != null)
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" opc.tipo = ");
				sb.append(objOpcion.getTipo().getId());
				isAnd = true;
			}
			sb.append(" order by opc.id_opcion ");
			
			List insurance = super.executeSQL(sb.toString());
			
			opciones = new ArrayList<Opcion>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				opc = new Opcion();
				opc.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					opc.setEstado(amount[1].toString());
				if(amount[2] != null)
					opc.setCodigo(amount[2].toString());
				if(amount[3] != null)
					opc.setNombre(amount[3].toString());
				if(amount[4] != null)
					opc.setAction(amount[4].toString());
				if(amount[5] != null)
					opc.setReglaNavegacion(amount[5].toString());
				if(amount[6] != null)
					opc.setMetodo(amount[6].toString());
				if(amount[7] != null)
					opc.setDetalle(amount[7].toString());
				if(amount[8] != null && amount[9] != null)
				{
					Opcion objSuperior = new Opcion();
					objSuperior.setId(new Long(amount[8].toString()));
					objSuperior.setNombre(amount[9].toString());
					opc.setSuperior(objSuperior);					
				}
				if(amount[10] != null && amount[11] != null)
				{
					Tabla objTipo = new Tabla();
					objTipo.setId(new Long(amount[10].toString()));
					objTipo.setDescripcion(amount[11].toString());
					opc.setTipo(objTipo);					
				}
				opciones.add(opc);
			}
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}

		return opciones;
	}
	@Override
	public List<Opcion> listarSinActual(Long id) throws BOException 
	{		
		List<Opcion> opciones = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(id);
			opciones = super.listNamedQuery("listaOpcionesSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		return opciones;
	}
	@Override
	public Opcion obtienePorCodigo(String codigo) throws BOException 
	{
		List<Opcion> opciones = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			opciones = super.listNamedQuery("obtieneOpcionPorCodigo", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(opciones.size() == 0)
			return null;
		else
			return opciones.get(0);
		
	}
	@Override
	public Opcion obtienePorCodigoSinActual(String codigo, Long id) throws BOException 
	{
		List<Opcion> opciones = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			parametros.add(id);
			opciones = super.listNamedQuery("obtieneOpcionPorCodigoSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(opciones.size() == 0)
			return null;
		else
			return opciones.get(0);
	}
	@Override
	public Opcion obtienePorId(Long id) throws BOException {
		
		List<Opcion> opciones = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(id);
			opciones = super.listNamedQuery("obtieneOpcionPorId", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(opciones.size() == 0)
			return null;
		else
			return opciones.get(0);
	}

}
