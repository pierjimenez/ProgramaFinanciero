package pe.com.bbva.iipf.pf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




@Entity
@Table(schema="PROFIN",  name="TIIPF_PFEFCAR")
@SequenceGenerator(name = "SEQ_EFECTIVIDAD_CART", sequenceName = "PROFIN.SEQ_EFECTIVIDAD_CART", allocationSize = 1, initialValue = 20000)
public class EfetividadCartera {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1971343881847392041L;
	private Long id;//	ID_PFEFCAR	INTEGER	Y	
	private String documento;//	DOCUMENTO	VARCHAR2(12)	Y			Numero de RUC
	private String codServicio;//	CODSERV	VARCHAR2(2)	Y			Numero de Cliente Interno
	private String efectividadProm6sol;//	EFEC_PROM6SOL	NUMBER	Y			
	private String efectividadProm6dol;//	EFEC_PROM6DOL	NUMBER	Y			
	private String protestoProm6sol;//	PROP_PROM6SOL	NUMBER	Y			
	private String protestoProm6dol;//	PROP_PROM6DOL	NUMBER	Y			
//	private transient String efectividadUltmaniosol;//	EFEC_ULTM_ANIOSOLES	NUMBER	Y			
//	private transient String efectividadUltmaniodol;//	EFEC_ULTM_ANIODOLA	NUMBER	Y			
	
	private Date fechaProceso;//	FECHA_PROCESO	DATE	Y			Fecha proceso
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_EFECTIVIDAD_CART")
	@Column(name="ID_PFEFCAR")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DOCUMENTO")
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	@Column(name="CODSERV")
	public String getCodServicio() {
		return codServicio;
	}
	public void setCodServicio(String codServicio) {
		this.codServicio = codServicio;
	}
	@Column(name="EFEC_PROM6SOL")
	public String getEfectividadProm6sol() {
		return efectividadProm6sol;
	}
	public void setEfectividadProm6sol(String efectividadProm6sol) {
		this.efectividadProm6sol = efectividadProm6sol;
	}
	@Column(name="EFEC_PROM6DOL")
	public String getEfectividadProm6dol() {
		return efectividadProm6dol;
	}
	public void setEfectividadProm6dol(String efectividadProm6dol) {
		this.efectividadProm6dol = efectividadProm6dol;
	}
	@Column(name="PROP_PROM6SOL")
	public String getProtestoProm6sol() {
		return protestoProm6sol;
	}
	public void setProtestoProm6sol(String protestoProm6sol) {
		this.protestoProm6sol = protestoProm6sol;
	}
	@Column(name="PROP_PROM6DOL")
	public String getProtestoProm6dol() {
		return protestoProm6dol;
	}
	public void setProtestoProm6dol(String protestoProm6dol) {
		this.protestoProm6dol = protestoProm6dol;
	}
//	@Transient // no mapeada
//	public String getEfectividadUltmaniosol() {
//		return efectividadUltmaniosol;
//	}
//	public void setEfectividadUltmaniosol(String efectividadUltmaniosol) {
//		this.efectividadUltmaniosol = efectividadUltmaniosol;
//	}
//	@Transient // no mapeada
//	public String getEfectividadUltmaniodol() {
//		return efectividadUltmaniodol;
//	}
//	public void setEfectividadUltmaniodol(String efectividadUltmaniodol) {
//		this.efectividadUltmaniodol = efectividadUltmaniodol;
//	}
	@Column(name="FECHA_PROCESO")
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
	
	
	
}
