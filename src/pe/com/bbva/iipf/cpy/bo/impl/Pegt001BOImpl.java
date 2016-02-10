package pe.com.bbva.iipf.cpy.bo.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.cpy.bo.Pegt001BO;
import pe.com.bbva.iipf.cpy.model.Pegt001;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;


public class Pegt001BOImpl extends GenericBOImpl<Pegt001> implements Pegt001BO {

	public Pegt001 findByCodigo(String codigo) throws BOException {
		Pegt001 pegt = null;
		HashMap<String, String> parametros = new HashMap<String, String>();
		parametros.put("numclien", codigo);
		List<Pegt001> lista = super.findByParams(Pegt001.class, parametros);
		if(lista !=null &&
		   !lista.isEmpty()){
			pegt = lista.get(0);
		}
		return pegt;
	}



}
