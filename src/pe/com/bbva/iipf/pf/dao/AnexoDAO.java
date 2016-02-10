package pe.com.bbva.iipf.pf.dao;

import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.GarantiaHost;
import pe.com.bbva.iipf.pf.model.SaldoCliente;
import pe.com.stefanini.core.exceptions.DAOException;

public interface AnexoDAO {
	public List<SaldoCliente> findListaSaldoCliente(String codcliente) throws DAOException;
	public void insertLoteAnexoColumna(List<AnexoColumna> listaAnexoColumna)throws DAOException;
	
	public List<GarantiaHost> findListaGarantiaHost(Empresa oempresa) throws DAOException ;

}
