package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;

@Entity
public class Trating {
		
	private String fechaPeriodoCalc ;//FECH_PERIODO_CALC,
	private String factCuantitativo; // FACT_CUANTITATIVOS,
	private String factCualitativo;// FACT_CUALITATIVOS,
	private String factBureau; // FACT_BUREAU,
	private String puntRating ;//PUNT_RATING,
	private String califRating ; //CALIF_RATING
	
	
	public String getFechaPeriodoCalc() {
		return fechaPeriodoCalc;
	}
	public void setFechaPeriodoCalc(String fechaPeriodoCalc) {
		this.fechaPeriodoCalc = fechaPeriodoCalc;
	}
	public String getFactCuantitativo() {
		return factCuantitativo;
	}
	public void setFactCuantitativo(String factCuantitativo) {
		this.factCuantitativo = factCuantitativo;
	}
	public String getFactCualitativo() {
		return factCualitativo;
	}
	public void setFactCualitativo(String factCualitativo) {
		this.factCualitativo = factCualitativo;
	}
	public String getFactBureau() {
		return factBureau;
	}
	public void setFactBureau(String factBureau) {
		this.factBureau = factBureau;
	}
	public String getPuntRating() {
		return puntRating;
	}
	public void setPuntRating(String puntRating) {
		this.puntRating = puntRating;
	}
	public String getCalifRating() {
		return califRating;
	}
	public void setCalifRating(String califRating) {
		this.califRating = califRating;
	}
	
	
	
	
	
}
