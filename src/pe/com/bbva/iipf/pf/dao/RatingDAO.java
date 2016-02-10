package pe.com.bbva.iipf.pf.dao;

import java.util.List;

import pe.com.bbva.iipf.pf.model.Pfrating;
import pe.com.bbva.iipf.pf.model.TcuentaRating;
import pe.com.bbva.iipf.pf.model.TmesEjercicio;
import pe.com.bbva.iipf.pf.model.Trating;
import pe.com.stefanini.core.exceptions.DAOException;

public interface RatingDAO {
	public List<Trating> findListaRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException ;
	public List<TcuentaRating> findListaCuentaRatingGrupo(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException;
	public List<TmesEjercicio> findListaMesesEjercicioRatingGrupo
	(String idPrograma,String codCliente,String TipoEmpresa) throws DAOException ;
	public void insertLotepfRating(List<Pfrating> listapfRating)throws DAOException;

}
