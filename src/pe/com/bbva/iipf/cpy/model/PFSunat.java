package pe.com.bbva.iipf.cpy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="PROFIN", name="TIIPF_PFSUNAT")
public class PFSunat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombre; //NOMBRE	VARCHAR2(100 BYTE)
	private String ruc;//DPC_RUC	VARCHAR2(12 BYTE)
	private String ciiu;//DPC_CIIU	VARCHAR2(5 BYTE)
	private String det_activiad;//DET_ACTIVIDAD	VARCHAR2(100 BYTE)
	
	@Column(name="NOMBRE",length=100)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Id
	@Column(name="RUC",length=12)
	public String getRuc() {
		return ruc;
	}
	
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	@Column(name="CIIU", length=5)
	public String getCiiu() {
		return ciiu;
	}
	
	public void setCiiu(String ciiu) {
		this.ciiu = ciiu;
	}

	@Column(name="DET_ACTIVIDAD",length=100)
	public String getDet_activiad() {
		return det_activiad;
	}

	public void setDet_activiad(String det_activiad) {
		this.det_activiad = det_activiad;
	}
	
	
}
