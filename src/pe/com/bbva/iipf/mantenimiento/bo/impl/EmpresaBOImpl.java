package pe.com.bbva.iipf.mantenimiento.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.bo.Grid;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;

@Service("empresaBO" )
public class EmpresaBOImpl extends GenericBOImpl<Empresa> implements EmpresaBO {
	@Override
	public List<Empresa> listarEmpresasPorPrograma(Long idPrograma)
			throws BOException {
		
		List parametros = new ArrayList();
		parametros.add(idPrograma);
		
		List<Empresa> empresas = super.listNamedQuery("listarEmpresaPorPrograma", parametros);
		return empresas;
		
	}
	@Override
	public Grid<Empresa> findToGrid(List<Empresa> lista, String order, int page,int rows) throws BOException, DAOException {
		
		String where="";
		return super.findToGridList(lista, where, " order by " + order, page,rows);
	}

}
