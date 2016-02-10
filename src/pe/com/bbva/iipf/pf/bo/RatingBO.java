package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.Pfrating;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.TmanagerLog;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

import java.util.Map;

public interface RatingBO {

	public List<Rating> actualizarRating()throws BOException;
	public List<Rating> actualizarRating(String codEmpresaGrupo)throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPrograma(Programa programa) ;
	public List<Rating> findRating()throws BOException;
	public List<Rating> findRating(Long idPrograma)throws BOException;
	public List<Rating> findRating(Long idPrograma , String codigoEmpresa)throws BOException;
	public void saveCopiaRating(List<Rating> olistRating) throws BOException;
	public void delete(Rating t) throws BOException;	
	public String ObtenerRating(Programa oprograma,String codEmpresa);
	public Map  ObtenerRatingConFecha(Programa oprograma,String codEmpresa);
	
	public void updateRatingWS(Programa oprograma,boolean bactivo,List<Empresa> listaempresaCompleto,TmanagerLog oLogRating);
	
	public void setPathWebServicePEC6(String pathWebServicePEC6) ;
	public void setPathWebService(String pathWebService);
	public void saveSQLPFRating2(List<Pfrating> listapfRating) throws BOException;
	
}
