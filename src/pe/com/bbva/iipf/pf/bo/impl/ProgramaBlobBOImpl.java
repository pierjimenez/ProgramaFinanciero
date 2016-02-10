package pe.com.bbva.iipf.pf.bo.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.StringUtil;

/**
 * Clase para guardar los campos blob del programa financiero
 * @author EPOMAYAY
 *
 */
@Service("programaBlobBO")
public class ProgramaBlobBOImpl extends GenericBOImpl<ProgramaBlob> implements
		ProgramaBlobBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;
	/**
	 * nombre del campo que se actualizara de la tabla parametroblob
	 */
	private String campoBlob;
	private String valorBlob;
	private Programa programa;
	private String sysCodificacion;
	private String patronesEditor;
	

	/**
	 * Verificando que el programa ya tenga un registro blob creado
	 * 
	 */
	@Override
	public void beforeSave(ProgramaBlob t) throws BOException{
	
		t.setPrograma(programa);
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", t.getPrograma().getId());
		List<ProgramaBlob> lista =  super.findByParams(ProgramaBlob.class, parametros);
		if(!lista.isEmpty()){
			t.setId(lista.get(0).getId());
		}
	
	}	
	
	/**
	 * Inserta un nuevo registro en la tabla programablob sino no existe 
	 * en caso contrario actualiza un campo blob especifico dentro de la
	 * tabla programablob
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(ProgramaBlob t) throws BOException{
		try {
		beforeSave(t);
		if(validate(t)){
			//creamos el registro de blob para el programa si no existe
			
			if(t.getId() == null){
				doSave(t);
			}
			
			
			String cadSQL = "";
			if(campoBlob != null && campoBlob.equals("sintesisEmpresa")){
				cadSQL = "updateSintesisEmpresa";
			}else if(campoBlob != null && campoBlob.equals("datosMatriz")){
				cadSQL = "updateDatosMatriz";
			}else if(campoBlob != null && campoBlob.equals("espacioLibre")){
				cadSQL = "updateEspacioLibre";
			}else if(campoBlob != null && campoBlob.equals("comenSituFinanciera")){
				cadSQL = "updateComenSituFinanciera";
			}else if(campoBlob != null && campoBlob.equals("comenSituEconomica")){
				cadSQL = "updateComenSituEconomica";
			}else if(campoBlob != null && campoBlob.equals("valoracionEconFinanciera")){
				cadSQL = "updateValoracionEconFinanciera";
			}else if(campoBlob != null && campoBlob.equals("valoracionPosiBalance")){
				cadSQL = "updateValoracionPosiBalance";
			}else if(campoBlob != null && campoBlob.equals("valoracionRating")){
				cadSQL = "updateValoracionRating";
			}else if(campoBlob != null && campoBlob.equals("espacioLibreAS")){
				cadSQL = "updateEspacioLibreAS";
			}else if(campoBlob != null && campoBlob.equals("comenLineas")){
				cadSQL = "updateComenLineas";
			}else if(campoBlob != null && campoBlob.equals("rentaModelGlobal")){
				cadSQL = "updateRentaModelGlobal";
			}else if(campoBlob != null && campoBlob.equals("campoLibreRB")){
				cadSQL = "updateCampoLibreRB";
			}else if(campoBlob != null && campoBlob.equals("fodaFotalezas")){
				cadSQL = "updateFodaFotalezas";
			}else if(campoBlob != null && campoBlob.equals("fodaOportunidades")){
				cadSQL = "updateFodaOportunidades";
			}else if(campoBlob != null && campoBlob.equals("fodaDebilidades")){
				cadSQL = "updateFodaDebilidades";
			}else if(campoBlob != null && campoBlob.equals("fodaAmenazas")){
				cadSQL = "updateFodaAmenazas";
			}else if(campoBlob != null && campoBlob.equals("conclucionFoda")){
				cadSQL = "updateConclucionFoda";
			}else if(campoBlob != null && campoBlob.equals("campoLibrePR")){
				cadSQL = "updateCampoLibrePR";
			}else if(campoBlob != null && campoBlob.equals("estructuraLimite")){
				cadSQL = "updateEstructuraLimite";
			}else if(campoBlob != null && campoBlob.equals("consideracionPR")){
				cadSQL = "updateConsideracionPR";
			}else if(campoBlob != null && campoBlob.equals("detalleOperacionGarantia")){
				cadSQL = "updateDetalleOperacionGarantia";
			}else if(campoBlob != null && campoBlob.equals("riesgoTesoreria")){
				cadSQL = "updateRiesgoTesoreria";
			}else if(campoBlob != null && campoBlob.equals("politicasRiesGrupo")){
				cadSQL = "updatePoliticasRiesGrupo";
			}else if(campoBlob != null && campoBlob.equals("politicasDelegacion")){
				cadSQL = "updatePoliticasDelegacion";
			}else if(campoBlob != null && campoBlob.equals("rentaModelBEC")){
				cadSQL = "updateRentaModelBEC";
			}else if(campoBlob != null && campoBlob.equals("comenComprasVentas")){
				cadSQL = "updateComenComprasVentas";
			}else if(campoBlob != null && campoBlob.equals("comenPoolBanc")){
				cadSQL = "updateComenPoolBanc";
			}else if(campoBlob != null && campoBlob.equals("campoLibreAnexos")){
				cadSQL = "updateCampoLibreAnexos";
			}else if(campoBlob != null && campoBlob.equals("concentracion")){
				cadSQL = "updateConcentracion";
			}else if(campoBlob != null && campoBlob.equals("datosBasicosAdicional")){
				cadSQL = "updateDatosBasicosRCD";
			}else if(campoBlob != null && campoBlob.equals("sintesisEconFinAdicional")){
				cadSQL = "updateSituacionEconFinRCD";
			}else if(campoBlob != null && campoBlob.equals("valoracion")){
				cadSQL = "updateValoracion";
			}else if(campoBlob != null && campoBlob.equals("comenIndiceTransa")){
				cadSQL = "updateIndTransaccional";
			}
			
			//limpiando codificaciones 
			logger.info("blob limpio sin filtro = "+valorBlob);
			String texto = ExcelHelper.cleanText(valorBlob,patronesEditor );
			logger.info("blob limpio = "+texto);
			if(cadSQL != null && !cadSQL.equals(""))
			{
				List parametros = new ArrayList();
				
				parametros.add(texto.getBytes(sysCodificacion== null?"":sysCodificacion.trim()));
				
				parametros.add(t.getId());

				super.executeNamedQuery(cadSQL ,parametros);
				String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();
				programaBO.actualizarFechaModificacionPrograma(programa.getId(),codUsuarioModificacion);
			}

			
		}
				
		afterSave(t);
		} catch (UnsupportedEncodingException e) {
			logger.info(StringUtil.getStackTrace(e));
		}
	
			
	}
	
	public void beforeSave(ProgramaBlob t,Programa oprograma) throws BOException{
		
		t.setPrograma(oprograma);
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", t.getPrograma().getId());
		List<ProgramaBlob> lista =  super.findByParams(ProgramaBlob.class, parametros);
		if(!lista.isEmpty()){
			t.setId(lista.get(0).getId());
		}
	
	} 
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(ProgramaBlob t,Programa oprograma) throws BOException{
		try {
		beforeSave(t,oprograma);
		if(validate(t)){
			//creamos el registro de blob para el programa si no existe
			
			if(t.getId() == null){
				doSave(t);
			}
			//PRUEBA DE COPIA DE BLOBS
			/*
			String cadSQL = "";
			if(campoBlob != null && campoBlob.equals("sintesisEmpresa")){
				cadSQL = "updateSintesisEmpresa";
			}else if(campoBlob != null && campoBlob.equals("datosMatriz")){
				cadSQL = "updateDatosMatriz";
			}else if(campoBlob != null && campoBlob.equals("espacioLibre")){
				cadSQL = "updateEspacioLibre";
			}else if(campoBlob != null && campoBlob.equals("comenSituFinanciera")){
				cadSQL = "updateComenSituFinanciera";
			}else if(campoBlob != null && campoBlob.equals("comenSituEconomica")){
				cadSQL = "updateComenSituEconomica";
			}else if(campoBlob != null && campoBlob.equals("valoracionEconFinanciera")){
				cadSQL = "updateValoracionEconFinanciera";
			}else if(campoBlob != null && campoBlob.equals("valoracionPosiBalance")){
				cadSQL = "updateValoracionPosiBalance";
			}else if(campoBlob != null && campoBlob.equals("valoracionRating")){
				cadSQL = "updateValoracionRating";
			}else if(campoBlob != null && campoBlob.equals("espacioLibreAS")){
				cadSQL = "updateEspacioLibreAS";
			}else if(campoBlob != null && campoBlob.equals("comenLineas")){
				cadSQL = "updateComenLineas";
			}else if(campoBlob != null && campoBlob.equals("rentaModelGlobal")){
				cadSQL = "updateRentaModelGlobal";
			}else if(campoBlob != null && campoBlob.equals("campoLibreRB")){
				cadSQL = "updateCampoLibreRB";
			}else if(campoBlob != null && campoBlob.equals("fodaFotalezas")){
				cadSQL = "updateFodaFotalezas";
			}else if(campoBlob != null && campoBlob.equals("fodaOportunidades")){
				cadSQL = "updateFodaOportunidades";
			}else if(campoBlob != null && campoBlob.equals("fodaDebilidades")){
				cadSQL = "updateFodaDebilidades";
			}else if(campoBlob != null && campoBlob.equals("fodaAmenazas")){
				cadSQL = "updateFodaAmenazas";
			}else if(campoBlob != null && campoBlob.equals("conclucionFoda")){
				cadSQL = "updateConclucionFoda";
			}else if(campoBlob != null && campoBlob.equals("campoLibrePR")){
				cadSQL = "updateCampoLibrePR";
			}else if(campoBlob != null && campoBlob.equals("estructuraLimite")){
				cadSQL = "updateEstructuraLimite";
			}else if(campoBlob != null && campoBlob.equals("consideracionPR")){
				cadSQL = "updateConsideracionPR";
			}else if(campoBlob != null && campoBlob.equals("detalleOperacionGarantia")){
				cadSQL = "updateDetalleOperacionGarantia";
			}else if(campoBlob != null && campoBlob.equals("riesgoTesoreria")){
				cadSQL = "updateRiesgoTesoreria";
			}else if(campoBlob != null && campoBlob.equals("politicasRiesGrupo")){
				cadSQL = "updatePoliticasRiesGrupo";
			}else if(campoBlob != null && campoBlob.equals("politicasDelegacion")){
				cadSQL = "updatePoliticasDelegacion";
			}else if(campoBlob != null && campoBlob.equals("rentaModelBEC")){
				cadSQL = "updateRentaModelBEC";
			}else if(campoBlob != null && campoBlob.equals("comenComprasVentas")){
				cadSQL = "updateComenComprasVentas";
			}else if(campoBlob != null && campoBlob.equals("comenPoolBanc")){
				cadSQL = "updateComenPoolBanc";
			}else if(campoBlob != null && campoBlob.equals("campoLibreAnexos")){
				cadSQL = "updateCampoLibreAnexos";
			}else if(campoBlob != null && campoBlob.equals("concentracion")){
				cadSQL = "updateConcentracion";
			}else if(campoBlob != null && campoBlob.equals("datosBasicosAdicional")){
				cadSQL = "updateDatosBasicosRCD";
			}else if(campoBlob != null && campoBlob.equals("sintesisEconFinAdicional")){
				cadSQL = "updateSituacionEconFinRCD";
			}else if(campoBlob != null && campoBlob.equals("valoracion")){
				cadSQL = "updateValoracion";
			}else if(campoBlob != null && campoBlob.equals("comenIndiceTransa")){
				cadSQL = "updateIndTransaccional";
			}
			
			//limpiando codificaciones 
			logger.info("blob limpio sin filtro = "+valorBlob);
			String texto = ExcelHelper.cleanText(valorBlob,patronesEditor );
			logger.info("blob limpio = "+texto);
			if(cadSQL != null && !cadSQL.equals(""))
			{
				List parametros = new ArrayList();
				
				parametros.add(texto.getBytes(sysCodificacion== null?"":sysCodificacion.trim()));
				
				parametros.add(t.getId());

				super.executeNamedQuery(cadSQL ,parametros);
				String codUsuarioModificacion=getUsuarioSession().getRegistroHost()==null?"":getUsuarioSession().getRegistroHost().toString();
				programaBO.actualizarFechaModificacionPrograma(oprograma.getId(),codUsuarioModificacion);
			}
			*/
			
		}
				
		//afterSave(t);
		} catch (Exception e) {
			logger.info(StringUtil.getStackTrace(e));
		}
	
			
	}

	/**
	 * Buscar blob
	 */
	@Override
	public ProgramaBlob findBlobByPrograma(Programa programa) throws BOException {
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa",programa.getId());
		List<ProgramaBlob> lista =  super.findByParams(ProgramaBlob.class, parametros);
		ProgramaBlob pb = null;
		if(!lista.isEmpty()){
			pb = lista.get(0);
		}
		return pb;
	}

	public String getCampoBlob() {
		return campoBlob;
	}

	public void setCampoBlob(String campoBlob) {
		this.campoBlob = campoBlob;
	}

	public String getValorBlob() {
		return valorBlob;
	}

	public void setValorBlob(String valorBlob) {
		this.valorBlob = valorBlob;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}

	public String getSysCodificacion() {
		return sysCodificacion;
	}

	public void setSysCodificacion(String sysCodificacion) {
		this.sysCodificacion = sysCodificacion;
	}

	public void setPatronesEditor(String patronesEditor) {
		this.patronesEditor = patronesEditor;
	}

	
}
