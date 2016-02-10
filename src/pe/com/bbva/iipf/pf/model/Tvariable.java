package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tvariable {
	private Long id	; 	
	private boolean datosBasicoSLinea;				
	private boolean sintesisEconomicoSLinea;
	private boolean ratingSLinea;
	private boolean extractoSLinea;
	private boolean informEmpresaSLinea;
	private boolean analisisSectorialSLinea;
	private boolean relacioBancatiasSLinea;
	private boolean factoresRiesgoSLinea;
	private boolean propuestaRiesgoSLinea;
	private boolean politicaRiesgoSLinea;
	private boolean posicionPrincipalSLinea;
	private boolean anexo1;
	
//	private String indiceDatosBasico;				
//	private String indiceSintesisEconomico;
//	private String indiceRating;
//	private String indiceExtracto;
//	private String indiceInformEmpresa;
//	private String indiceAnalisisSectorial;
//	private String indiceRelacioBancatias;
//	private String indiceFactoresRiesgo;
//	private String indicePropuestaRiesgo;
//	private String indicePoliticaRiesgo;
//	private String indicePosicionPrincipal;

	
	private String codigo;
	private String valor1;
	private String valor2;
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public boolean isDatosBasicoSLinea() {
		return datosBasicoSLinea;
	}
	public void setDatosBasicoSLinea(boolean datosBasicoSLinea) {
		this.datosBasicoSLinea = datosBasicoSLinea;
	}
	public boolean isSintesisEconomicoSLinea() {
		return sintesisEconomicoSLinea;
	}
	public void setSintesisEconomicoSLinea(boolean sintesisEconomicoSLinea) {
		this.sintesisEconomicoSLinea = sintesisEconomicoSLinea;
	}
	public boolean isRatingSLinea() {
		return ratingSLinea;
	}
	public void setRatingSLinea(boolean ratingSLinea) {
		this.ratingSLinea = ratingSLinea;
	}
	public boolean isExtractoSLinea() {
		return extractoSLinea;
	}
	public void setExtractoSLinea(boolean extractoSLinea) {
		this.extractoSLinea = extractoSLinea;
	}
	public boolean isInformEmpresaSLinea() {
		return informEmpresaSLinea;
	}
	public void setInformEmpresaSLinea(boolean informEmpresaSLinea) {
		this.informEmpresaSLinea = informEmpresaSLinea;
	}
	public boolean isAnalisisSectorialSLinea() {
		return analisisSectorialSLinea;
	}
	public void setAnalisisSectorialSLinea(boolean analisisSectorialSLinea) {
		this.analisisSectorialSLinea = analisisSectorialSLinea;
	}
	public boolean isRelacioBancatiasSLinea() {
		return relacioBancatiasSLinea;
	}
	public void setRelacioBancatiasSLinea(boolean relacioBancatiasSLinea) {
		this.relacioBancatiasSLinea = relacioBancatiasSLinea;
	}
	public boolean isFactoresRiesgoSLinea() {
		return factoresRiesgoSLinea;
	}
	public void setFactoresRiesgoSLinea(boolean factoresRiesgoSLinea) {
		this.factoresRiesgoSLinea = factoresRiesgoSLinea;
	}
	public boolean isPropuestaRiesgoSLinea() {
		return propuestaRiesgoSLinea;
	}
	public void setPropuestaRiesgoSLinea(boolean propuestaRiesgoSLinea) {
		this.propuestaRiesgoSLinea = propuestaRiesgoSLinea;
	}
	public boolean isPoliticaRiesgoSLinea() {
		return politicaRiesgoSLinea;
	}
	public void setPoliticaRiesgoSLinea(boolean politicaRiesgoSLinea) {
		this.politicaRiesgoSLinea = politicaRiesgoSLinea;
	}
	public boolean isPosicionPrincipalSLinea() {
		return posicionPrincipalSLinea;
	}
	public void setPosicionPrincipalSLinea(boolean posicionPrincipalSLinea) {
		this.posicionPrincipalSLinea = posicionPrincipalSLinea;
	}
	
	
	public boolean isAnexo1() {
		return anexo1;
	}
	public void setAnexo1(boolean anexo1) {
		this.anexo1 = anexo1;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getValor1() {
		return valor1;
	}
	public void setValor1(String valor1) {
		this.valor1 = valor1;
	}
	public String getValor2() {
		return valor2;
	}
	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

}
