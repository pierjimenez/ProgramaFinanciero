package pe.com.bbva.iipf.mantenimiento.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;


@Service("tablaBO" )
public class TablaBOImpl extends GenericBOImpl<Tabla> implements TablaBO {
	@Override
	public void beforeSave(Tabla objTabla) throws BOException
	{
		if(objTabla.getPadre().getId()== null)
		{
			objTabla.setPadre(null); 
		}
	}
	@Override
	public boolean validate(Tabla objTabla) throws BOException{
		return true;
	}
	@Override
	public void afterSave(Tabla objTabla) throws BOException{
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertar(Tabla objTabla) throws BOException 
	{
		super.save(objTabla);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modificar(Tabla objTabla) throws BOException 
	{
		super.save(objTabla);
	}
	@Override
	public List<Tabla> listar(Tabla objTabla) throws BOException 
	{
		List<Tabla> tablas = null;
		Tabla hijo = null;
		Tabla padre = null;
		
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select h.id_tabla, h.codigo, h.descripcion, h.abreviado,");
			sb.append("h.id_tabla_de_tabla, h.estado, p.id_tabla as id_padre,");
			sb.append("p.codigo as cod_padre, p.descripcion as desc_padre,");
			sb.append("p.abreviado as abrev_padre, p.estado as estado_padre ");
			sb.append("from PROFIN.tiipf_tabla_de_tabla h ");
			sb.append("left join PROFIN.tiipf_tabla_de_tabla p ");
			sb.append("on h.id_tabla_de_tabla = p.id_tabla ");
						
			boolean isAnd = false;
			boolean isWhere = false;
			
			if(objTabla.getPadre() != null && objTabla.getPadre().getId() != null)
			{
				if(objTabla.getPadre().getId() != -1)
				{
					if(!isWhere)
					{
						sb.append("where");
						isWhere = true;
					}
					if(isAnd)
						sb.append(" and ");
					sb.append(" h.id_tabla_de_tabla = ");
					sb.append(objTabla.getPadre().getId());
					isAnd = true;
				}
			}	
			else
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" h.id_tabla_de_tabla is null ");
				isAnd = true;
			}
			if(objTabla.getDescripcion() != null && !objTabla.getDescripcion().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" upper(h.descripcion) like '%");
				sb.append(objTabla.getDescripcion().toUpperCase());
				sb.append("%'");
				isAnd = true;
			}
			if(objTabla.getEstado() != null && !objTabla.getEstado().equals("-1"))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" h.estado = '");
				sb.append(objTabla.getEstado());
				sb.append("' ");
				isAnd = true;
			}
			
			sb.append(" order by h.id_tabla ");
			
			List insurance = super.executeSQL(sb.toString());
			

			tablas = new ArrayList<Tabla>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				hijo = new Tabla();
				hijo.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					hijo.setCodigo(amount[1].toString());
				if(amount[2] != null)
					hijo.setDescripcion(amount[2].toString());
				if(amount[3] != null)
					hijo.setAbreviado(amount[3].toString());
				if(amount[5] != null)
					hijo.setEstado(amount[5].toString());
								
				if(amount[4] != null)
				{
					padre = new Tabla();
					padre.setId(Long.valueOf(amount[4].toString()));
					if(amount[7] != null)
						padre.setCodigo(amount[7].toString());
					if(amount[8] != null)
					padre.setDescripcion(amount[8].toString());
					if(amount[9] != null)
						padre.setAbreviado(amount[9].toString());
					if(amount[10] != null)
						padre.setEstado(amount[10].toString());
					
					hijo.setPadre(padre);
				}
				
				tablas.add(hijo);
			}
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}

		return tablas;
	}
	@Override
	public List<Tabla> listarHijos(Long idPadre) throws BOException 
	{
		
		List<Tabla> tablas = null;
		
		try 
		{
			Tabla objFiltro = new Tabla();
			objFiltro.setEstado("A");
			
			Tabla objPadre = new Tabla();
			objPadre.setId(idPadre);
			
			objFiltro.setPadre(objPadre);
			
			tablas = this.listar(objFiltro);
			
		}
		catch (Exception e) 
		{					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
				
		return tablas;
	}
	@Override
	public Tabla obtieneHijaPorCodigo(String codigo, Long idPadre)
			throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			parametros.add(idPadre);
			tablas = super.listNamedQuery("obtieneTablaHijaPorCodigo", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}
	@Override
	public Tabla obtieneHijaPorCodigoSinActual(String codigo,
			Long idPadre, Long idTabla) throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			parametros.add(idPadre);
			parametros.add(idTabla);
			tablas = super.listNamedQuery("obtieneTablaHijaPorCodigoSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}
	@Override
	public Tabla obtienePadrePorCodigo(String codigo) throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			tablas = super.listNamedQuery("obtieneTablaPadrePorCodigo", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}
	
	
	@Override
	public Tabla obtienePadrePorCodigoSinActual(String codigo, Long idTabla)
			throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			parametros.add(idTabla);
			tablas = super.listNamedQuery("obtieneTablaPadrePorCodigoSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}
	@Override
	public List<Tabla> listaPadresSinActual(Long idTabla) throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(idTabla);
			tablas = super.listNamedQuery("listaTablasPadreSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return tablas;
		
	}
	@Override
	public Tabla obtienePorId(Long id) throws BOException {
		
		List<Tabla> tablas = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(id);			
			tablas = super.listNamedQuery("obtieneTablaPorId", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}

	//ini MCG20130411
	@Override
	public Tabla obtieneHijaPorCodigoHijoCodigoPadre(String codigoHijo, String codigoPadre)
			throws BOException {

		List<Tabla> tablas = null;

		try {
			List parametros = new ArrayList();
			parametros.add(codigoHijo);
			parametros.add(codigoPadre);
			tablas = super.listNamedQuery("obtieneTablaHijaPorCodigoHijoCodigoPadre",
					parametros);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}

		if (tablas.size() == 0)
			return null;
		else
			return tablas.get(0);
	}
	//ini MCG20130411
	
	//ini MCG20140825
	@Override
	public List<Tabla> obtieneHijaCodigoPadre(String codigoPadre)	throws BOException {

		List<Tabla> tablas = new ArrayList<Tabla>();
		
		try {
			List parametros = new ArrayList();			
			parametros.add(codigoPadre);
			tablas = super.listNamedQuery("obtieneTablaHijaCodigoPadre",parametros);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if (tablas==null && tablas.size() == 0)
			return tablas = new ArrayList<Tabla>();
		else
			return tablas;
		}
	//fin MCG21040825
	@Override
	public Map<String, String> getMapTablasByCodigoPadre(String codigoPabre) {

		Map<String, String> mapResult = new HashMap<String, String>();
		try {
			List<Tabla> listatablasHijosComex=obtieneHijaCodigoPadre(codigoPabre);
			if(listatablasHijosComex != null && !listatablasHijosComex.isEmpty()){
				for (Tabla otabla : listatablasHijosComex) {
					mapResult.put((otabla.getCodigo()==null?"":otabla.getCodigo()), (otabla.getDescripcion()==null?"":otabla.getDescripcion()));
				}
			}
		} catch (Exception e) {
			mapResult = new HashMap<String, String>();
		}	
		return mapResult;
	}

}
