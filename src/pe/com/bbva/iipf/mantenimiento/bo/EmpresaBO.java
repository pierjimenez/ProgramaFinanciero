package pe.com.bbva.iipf.mantenimiento.bo;

import pe.com.stefanini.core.bo.Grid;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;

import java.util.List;

public interface EmpresaBO 
{

	public List<Empresa> listarEmpresasPorPrograma(Long idPrograma) throws BOException;
	public Grid<Empresa> findToGrid(List<Empresa> lista, String order, int page,int rows) throws BOException, DAOException;
	
}
