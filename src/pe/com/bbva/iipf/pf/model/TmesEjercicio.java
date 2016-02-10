package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;

@Entity
public class TmesEjercicio {

	private String fechaPeriodoCalc ;//FECH_PERIODO_CALC
	private String mesesEjercicio ;//MESES_EJERCICIOS 
	private String inflacion ;//INFLACION
	private String califRating ;//CALIF_RATING
	private String puntRating ;//PUNT_RATING
	
	private String factCualitativos ;//FACT_CUALITATIVOS,
	private String factCuantitativos ;//FACT_CUANTITATIVOS,
	private String factBureau ;//FACT_BUREAU
	
	
	
	
	public String getFechaPeriodoCalc() {
		return fechaPeriodoCalc;
	}
	public void setFechaPeriodoCalc(String fechaPeriodoCalc) {
		this.fechaPeriodoCalc = fechaPeriodoCalc;
	}
	public String getMesesEjercicio() {
		return mesesEjercicio;
	}
	public void setMesesEjercicio(String mesesEjercicio) {
		this.mesesEjercicio = mesesEjercicio;
	}
	public String getInflacion() {
		return inflacion;
	}
	public void setInflacion(String inflacion) {
		this.inflacion = inflacion;
	}
	public String getCalifRating() {
		return califRating;
	}
	public void setCalifRating(String califRating) {
		this.califRating = califRating;
	}
	public String getPuntRating() {
		return puntRating;
	}
	public void setPuntRating(String puntRating) {
		this.puntRating = puntRating;
	}
	public String getFactCualitativos() {
		return factCualitativos;
	}
	public void setFactCualitativos(String factCualitativos) {
		this.factCualitativos = factCualitativos;
	}
	public String getFactCuantitativos() {
		return factCuantitativos;
	}
	public void setFactCuantitativos(String factCuantitativos) {
		this.factCuantitativos = factCuantitativos;
	}
	public String getFactBureau() {
		return factBureau;
	}
	public void setFactBureau(String factBureau) {
		this.factBureau = factBureau;
	}
	
	

}
