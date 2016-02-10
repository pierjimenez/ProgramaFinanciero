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
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.StringUtil;

@Service("sintesisEconomicoBlobBO")
public class SintesisEconomicoBlobBOImpl extends GenericBOImpl<SintesisEconomicoBlob> implements
SintesisEconomicoBlobBO  {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;
	/**
	 * nombre del campo que se actualizara de la tabla parametroblob
	 */
	private String campoBlob;
	private String valorBlob;
	
	private String codEmpresa;
	
	private Programa programa;
	private String sysCodificacion;
	private String patronesEditor;
	
	/**
	 * Verificando que el programa ya tenga un registro blob creado
	 * 
	 */
	@Override
	public void beforeSave(SintesisEconomicoBlob t) throws BOException{
	
		t.setPrograma(programa);
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("programa", t.getPrograma().getId());
		parametros.put("codigoEmpresa", getCodEmpresa());
		List<SintesisEconomicoBlob> lista =  super.findByParams(SintesisEconomicoBlob.class, parametros);
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
	public void save(SintesisEconomicoBlob t) throws BOException{
		try {
		beforeSave(t);
		if(validate(t)){
			//creamos el registro de blob para el programa si no existe
			
			if(t.getId() == null){
				doSave(t);
			}			
			
			String cadSQL = "";
			if(campoBlob != null && campoBlob.equals("comenSituFinanciera")){
				cadSQL = "updateComenSituFinancieraByEmpresa";
			}else if(campoBlob != null && campoBlob.equals("comenSituEconomica")){
				cadSQL = "updateComenSituEconomicaByEmpresa";
			}else if(campoBlob != null && campoBlob.equals("valoracionEconFinanciera")){
				cadSQL = "updateValoracionEconFinancieraByEmpresa";
			}else if(campoBlob != null && campoBlob.equals("valoracionPosiBalance")){
				cadSQL = "updateValoracionPosiBalanceByEmpresa";
			}else if(campoBlob != null && campoBlob.equals("sintesisEconFinAdicional")){
				cadSQL = "updateSintesisEconFinanAddRCByEmpresa";
			}
			//limpiando codificaciones 
			String texto = ExcelHelper.cleanText(valorBlob,patronesEditor );
			if(cadSQL != null && !cadSQL.equals(""))
			{
				List parametros = new ArrayList();
				
				parametros.add(texto.getBytes(sysCodificacion== null?"":sysCodificacion.trim()));
				
				parametros.add(codEmpresa);
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
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void savecopia(SintesisEconomicoBlob t) throws BOException{	
		if(t.getId() == null){
				doSave(t);	
		}			
	}

	/**
	 * Buscar blob
	 */
	@Override
	public SintesisEconomicoBlob findSintEcoBlobByPrograma(Programa programa,String codigoEmpresa) throws BOException {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("programa",programa.getId());
		parametros.put("codigoEmpresa", codigoEmpresa);
		List<SintesisEconomicoBlob> lista =  super.findByParams(SintesisEconomicoBlob.class, parametros);
		SintesisEconomicoBlob pb = null;
		if(!lista.isEmpty()){
			pb = lista.get(0);
		}
		return pb;
	}
	
	@Override
	public List<SintesisEconomicoBlob> listaSintEcoBlobByPrograma(Programa oprograma) throws BOException {
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa",oprograma.getId());
		List<SintesisEconomicoBlob> lista = null ;
		try {
			lista=super.findByParams(SintesisEconomicoBlob.class, parametros);
		} catch (BOException e) {
			throw new BOException(e);
		}
		return lista;
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

	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}


	public void setPatronesEditor(String patronesEditor) {
		this.patronesEditor = patronesEditor;
	}

	
	

	
}
