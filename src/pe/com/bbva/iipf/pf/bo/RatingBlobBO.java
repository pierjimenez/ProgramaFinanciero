package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.RatingBlob;
import pe.com.stefanini.core.exceptions.BOException;

public interface RatingBlobBO {
	public void save(RatingBlob ratingBlob) throws BOException;
	public RatingBlob findRatingBlobByPrograma(Programa programa,String codigoEmpresa)throws BOException;
	public List<RatingBlob> listaRatingBlobByPrograma(Programa oprograma) throws BOException;
	public void savecopia(RatingBlob t) throws BOException;
	public void setCampoBlob(String campoBlog) ;
	public void setValorBlob(String valorBlob) ;
	public void setCodEmpresa(String codEmpresa) ;
	public void setPrograma(Programa programa);
	public void setSysCodificacion (String sysCodificacion);
	public void delete(RatingBlob t) throws BOException;
	
	public void setPatronesEditor(String patronesEditor);
}
