package pe.com.bbva.iipf.pf.bo;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;

public interface FactoresRiesgoBO {
	
	public void updateFactoresRiesgos() throws BOException;	
	
	public void setPrograma(Programa programa) ;
	
	public void setComentarioFortaleza(String comentarioFortaleza) ;	
	public void setComentarioOportunidades(String comentarioOportunidades) ;	
	public void setComentarioDebilidades(String comentarioDebilidades);	 
	public void setComentarioAmenaza(String comentarioAmenaza) ;
	
	public void setConclusionesFODA(String conclusionesFODA);

}
