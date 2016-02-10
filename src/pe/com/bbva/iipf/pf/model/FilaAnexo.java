package pe.com.bbva.iipf.pf.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

/**
 * 
 * @author EPOMAYAY
 *
 */
public class FilaAnexo{
	
	private int pos;
	private Anexo anexo;
	private List<AnexoColumna> listaAnexoColumna = new ArrayList<AnexoColumna>();
	
	private String activosaldo;
	private transient String tipoTotal; 
	
	public Anexo getAnexo() {
		return anexo;
	}
	
	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	public List<AnexoColumna> getListaAnexoColumna() {
		return listaAnexoColumna;
	}

	public void setListaAnexoColumna(List<AnexoColumna> listaAnexoColumna) {
		this.listaAnexoColumna = listaAnexoColumna;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	
	public String getActivosaldo() {
		return activosaldo;
	}

	public void setActivosaldo(String activosaldo) {
		this.activosaldo = activosaldo;
	}

	@Transient // no mapeada
	public String getTipoTotal() {
		return tipoTotal;
	}

	public void setTipoTotal(String tipoTotal) {
		this.tipoTotal = tipoTotal;
	}
	
	
	
}