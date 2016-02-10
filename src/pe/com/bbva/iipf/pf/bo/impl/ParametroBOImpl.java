package pe.com.bbva.iipf.pf.bo.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.dao.ParametroDAO;
import pe.com.bbva.iipf.pf.model.ArchivoMnt;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.FechaUtil;

@Service("paremetroBO" )
public class ParametroBOImpl extends GenericBOImpl<Parametro> implements
		ParametroBO {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ParametroDAO parametroDAO;
	
	/**
	 * Busca un parametro por su id
	 */
	@Override
	public Parametro findByIdParametro(Long id) throws BOException {
		
		Parametro parametro = null;
		try {
			//super.findById(parametro.getClass(),id);			
			parametro = (Parametro)super.findById(Parametro.class,id);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return parametro;
	}

	/**
	 * busca todos los parametros de la base de datos
	 */
	@Override
	public List<Parametro> findParametros() throws BOException {
		
		List<Parametro> lista = null;
		try {			
			lista = super.findByParams(Parametro.class,null);
			if(lista.size()==0){
				throw new BOException("No se encontraron resultados.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return lista;
	}
	@Override
	public Parametro findByNombreParametro(String codigo) throws BOException {
			
		Parametro parametro = null;
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("codigo", codigo);
			List<Parametro> lista = super.findByParams(Parametro.class, parametros);
			if(lista !=null &&
			   !lista.isEmpty()){
				parametro = lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		return parametro;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertar(Parametro objParametro) throws BOException 
	{
		super.save(objParametro);			
	}
	@Override
	public List<Parametro> listar(Parametro objParametro) throws BOException 
	{
		
		List<Parametro> parametros = null;
		Parametro oPatametro = null;
		
		try {
			 		        		       		        		        		       		       		       		       		       		                      			     		
			StringBuffer sb = new StringBuffer();
			sb.append("select id_parametros, codigo, valor, descripcion, estado ");
			sb.append("from PROFIN.tiipf_parametros ");
			
						
			boolean isAnd = false;
			boolean isWhere = false;
						
			if(objParametro.getCodigo() != null && !objParametro.getCodigo().equals(""))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" codigo like '%");
				sb.append(objParametro.getCodigo());
				sb.append("%'");
				isAnd = true;
			}
			if(objParametro.getEstado() != null && !objParametro.getEstado().equals("-1"))
			{
				if(!isWhere)
				{
					sb.append("where");
					isWhere = true;
				}
				if(isAnd)
					sb.append(" and ");
				sb.append(" estado = '");
				sb.append(objParametro.getEstado());
				sb.append("' ");
				isAnd = true;
			}
			
			sb.append(" order by id_parametros ");
			
			List insurance = super.executeSQL(sb.toString());
						
			parametros = new ArrayList<Parametro>();
			
			for (Iterator it = insurance.iterator(); it.hasNext();) 
			{
				Object[] amount = (Object [])it.next();
				
				oPatametro = new Parametro();
				oPatametro.setId(Long.valueOf(amount[0].toString()));
				if(amount[1] != null)
					oPatametro.setCodigo(amount[1].toString());
				if(amount[2] != null)
					oPatametro.setValor(amount[2].toString());
				if(amount[3] != null)
					oPatametro.setDescripcion(amount[3].toString());
				if(amount[4] != null)
					oPatametro.setEstado(amount[4].toString());
				
				parametros.add(oPatametro);
			}
		} catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		return parametros;
		
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modificar(Parametro objParametro) throws BOException {
		super.save(objParametro);		
	}

	public void beforeSave(Tabla objTabla) throws BOException
	{
		
	}
	
	public boolean validate(Tabla objTabla) throws BOException{
		return true;
	}
	
	public void afterSave(Tabla objTabla) throws BOException{
		
	}
	@Override
	public Parametro obtienePorCodigo(String codigo) throws BOException 
	{
		List<Parametro> pars = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			pars = super.listNamedQuery("obtieneParametroPorCodigo", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(pars.size() == 0)
			return null;
		else
			return pars.get(0);
		
	}
	@Override
	public Parametro obtienePorCodigoSinActual(String codigo, Long id) throws BOException 
	{
		List<Parametro> pars = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(codigo);
			parametros.add(id);
			pars = super.listNamedQuery("obtieneParametroPorCodigoSinActual", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(pars.size() == 0)
			return null;
		else
			return pars.get(0);
	}
	@Override
	public Parametro obtienePorId(Long id) throws BOException 
	{
		List<Parametro> pars = null;
		
		try 
		{
			List parametros = new ArrayList();
			parametros.add(id);
			pars = super.listNamedQuery("obtieneParametroPorId", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(pars.size() == 0)
			return null;
		else
			return pars.get(0);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED , rollbackFor ={BOException.class})
	public Long  ValidaCargaEnProceso(String strCodigo) throws BOException {
		Long idArchivo = 0L;		
		try {
			idArchivo = parametroDAO.ValidaCargaEnProceso( strCodigo);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		return idArchivo;
	}
	@Override	
	public void update(Parametro parametro) throws BOException {			
		
		try {
			super.doSave(parametro);
		} catch (Exception e) {
		e.printStackTrace();
		throw new BOException(e.getMessage());
		}
	}
	@Override
	public Integer ValidaCargaAutomatica(String fechaproceso, int idtipoArchivo) throws BOException {
		Integer cant = 0;		
		try {
			cant = parametroDAO.ValidaCargaAutomatica(fechaproceso,idtipoArchivo);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		return cant;
	}
	
	//ini MCG20130904
	@Override
	public List<List>  findResultScript(String strScript,String strTipoScript) throws BOException {			
			List resultados = null;		
			try {
				resultados = parametroDAO.findResultScript(strScript, strTipoScript);

			} catch (DAOException e) {
			throw new BOException(e.getMessage());
			} 
			return resultados;	
	}
	
	public List<ArchivoMnt> findArchivosMNT(ArchivoMnt archivoMnt) throws BOException, DAOException {
			
		List<ArchivoMnt> listaArchivos= new ArrayList<ArchivoMnt>();		
		ArchivoMnt arch = new ArchivoMnt();		

		boolean agregar = false;
		int i = 0;
		BigDecimal kb = new BigDecimal(1024);
		BigDecimal peso = null;
		String rutaCarga=archivoMnt.getRuta();
		if (rutaCarga==null || rutaCarga!=null && rutaCarga.equals("")){
			Parametro oparametro = findByNombreParametro(Constantes.COD_PARAMETRO_DIR_FILES_SINTESIS_ECONOMICO);
			rutaCarga=oparametro.getValor();
			archivoMnt.setRuta(rutaCarga);
		}
		
		if(rutaCarga!=null){
			
			File myDir = new File(rutaCarga);				   

				    File[] contents = myDir.listFiles();
			
				    if (contents != null) {
				    	i=0;
				    	for (File file : contents) {
				    	  if(!file.isDirectory()){
					    	  agregar = true;
					    	  if(archivoMnt.getNombreArchivo()!=null && !archivoMnt.getNombreArchivo().isEmpty()){
					    		  if(!file.getName().toUpperCase().contains(archivoMnt.getNombreArchivo().toUpperCase())){
					    			  agregar = false;
					    		  }
					    	  }
					    	  if(archivoMnt.getFechaModificacion()!=null){
					    		  if(FechaUtil.compareDate(new Date(file.lastModified()),archivoMnt.getFechaModificacion())!=0 ){
					    			  agregar = false;
					    		  }
					    	  }
					    	  if(agregar){
					    		  arch = new ArchivoMnt();
					    		  peso = new BigDecimal(file.length());
					    		  arch.setId(Long.valueOf(i));
						    	  arch.setNombreArchivo(file.getName());
						    	  arch.setRutaCarpeta(rutaCarga);
						    	  arch.setRutaArchivo(file.getPath().replace(File.separator, "*"));
						    	  arch.setFechaModificacion(new Date(file.lastModified()));
						    	  arch.setPesoArchivo((peso).divide(kb).longValue()+1);
						    	  arch.setRuta(rutaCarga);
						    	  listaArchivos.add(arch);
						    	  i++;
					    	  }
				    	  }
				      }
				    }
			
	    }else{
	    	logger.error("No se ha encontrado el parametro que indica la ruta de los archivos a mostrar");
	    }
		return listaArchivos;
	}
	//INI MCG20130904
	
	
}
