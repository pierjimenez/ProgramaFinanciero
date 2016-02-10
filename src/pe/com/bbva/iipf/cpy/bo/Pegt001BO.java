package pe.com.bbva.iipf.cpy.bo;

import java.util.List;

import pe.com.bbva.iipf.cpy.model.Pegt001;
import pe.com.stefanini.core.exceptions.BOException;

public interface Pegt001BO {

	public Pegt001 findByCodigo(String codigo)throws BOException;
}
