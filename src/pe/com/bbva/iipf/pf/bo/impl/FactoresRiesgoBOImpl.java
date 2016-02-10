package pe.com.bbva.iipf.pf.bo.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.FactoresRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;

@Service("FactoresRiesgoBO" )
public class FactoresRiesgoBOImpl extends GenericBOImpl implements FactoresRiesgoBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	private Programa programa;	
	private ProgramaBlob programaBlob = new ProgramaBlob(); 
	
	private String comentarioFortaleza;	
	private String comentarioOportunidades;	
	private String comentarioDebilidades;
	private String comentarioAmenaza;
	
	private String conclusionesFODA;
	
	

	public void beforeUpdateFactoresRiesgo(){
		//lista.clear();		
	}
	public boolean validateUpdateFactoresRiesgo(){
		return true;
	}
	
	/**
	 * actuliza los Relaciones Bancarias del programa financiero
	 */
	@Override
	@Transactional()
	public void updateFactoresRiesgos() throws BOException {
		beforeUpdateFactoresRiesgo();
		if(validateUpdateFactoresRiesgo()){					

			
			programaBlobBO.setPrograma(programa);
			programaBlobBO.setCampoBlob("fodaFotalezas");
			programaBlobBO.setValorBlob(comentarioFortaleza);
			programaBlobBO.save(programaBlob);	
			
			programaBlobBO.setCampoBlob("fodaOportunidades");
			programaBlobBO.setValorBlob(comentarioOportunidades);
			programaBlobBO.save(programaBlob);
			
			programaBlobBO.setCampoBlob("fodaDebilidades");
			programaBlobBO.setValorBlob(comentarioDebilidades);
			programaBlobBO.save(programaBlob);
			
			programaBlobBO.setCampoBlob("fodaAmenazas");
			programaBlobBO.setValorBlob(comentarioAmenaza);
			programaBlobBO.save(programaBlob);
			
			programaBlobBO.setCampoBlob("conclucionFoda");
			programaBlobBO.setValorBlob(conclusionesFODA);
			programaBlobBO.save(programaBlob);				
		
		}
		
		//afterUpdateRelacionesBancarias();
	}




	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}
	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}
	
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBlob getProgramaBlob() {
		return programaBlob;
	}
	public void setProgramaBlob(ProgramaBlob programaBlob) {
		this.programaBlob = programaBlob;
	}

	public String getComentarioFortaleza() {
		return comentarioFortaleza;
	}
	public void setComentarioFortaleza(String comentarioFortaleza) {
		this.comentarioFortaleza = comentarioFortaleza;
	}

	public String getComentarioOportunidades() {
		return comentarioOportunidades;
	}
	public void setComentarioOportunidades(String comentarioOportunidades) {
		this.comentarioOportunidades = comentarioOportunidades;
	}

	public String getComentarioDebilidades() {
		return comentarioDebilidades;
	}
	public void setComentarioDebilidades(String comentarioDebilidades) {
		this.comentarioDebilidades = comentarioDebilidades;
	}

	public String getComentarioAmenaza() {
		return comentarioAmenaza;
	}
	public void setComentarioAmenaza(String comentarioAmenaza) {
		this.comentarioAmenaza = comentarioAmenaza;
	}
	public String getConclusionesFODA() {
		return conclusionesFODA;
	}
	public void setConclusionesFODA(String conclusionesFODA) {
		this.conclusionesFODA = conclusionesFODA;
	}


	
	

}
