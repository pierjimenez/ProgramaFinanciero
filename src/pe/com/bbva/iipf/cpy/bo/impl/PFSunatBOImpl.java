package pe.com.bbva.iipf.cpy.bo.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;


import pe.com.bbva.iipf.cpy.bo.PFSunatBO;
import pe.com.bbva.iipf.cpy.model.Pegt001;
import pe.com.bbva.iipf.cpy.model.PFSunat;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("sunatBO")
public class PFSunatBOImpl extends GenericBOImpl<PFSunat> implements PFSunatBO  {

	public PFSunat findByRUC(String ruc) throws BOException{
		PFSunat sunat = null;
		HashMap<String,String> parametros = new HashMap<String,String>();
		parametros.put("ruc", ruc);
		try {
			List<PFSunat> lista = super.findByParams(PFSunat.class, parametros);
			if(lista !=null &&
			   !lista.isEmpty()){
				sunat = lista.get(0);
			}
		} catch (BOException e) {
			
		}
		return sunat;
	}

	/**
	 * Se busca la lista de empresas que pertencen al grupo para
	 * luego buscar las actividades economicas de cada una de ellas
	 */
	public List getEmpresasDelGrupo(String codigoGrupo, Long idPrograma) throws BOException{
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", idPrograma);
		List<Empresa> listaEmpresas = findByParams2(Empresa.class, parametros);
		String codigos = "'";
		boolean primero = true;
		for(Empresa empr : listaEmpresas){
			if(primero){
				codigos = codigos + empr.getRuc() +"'";
				primero = false;
			}else{
				codigos = codigos+
						  ",'"+
						  empr.getRuc() +
						  "'";
			}
		}
		String sql = "SELECT * FROM PROFIN.TIIPF_PFSUNAT WHERE RUC IN ("+
					 codigos+
					 ")";
		List lista = executeSQL(sql);
		return lista;
	}
}
