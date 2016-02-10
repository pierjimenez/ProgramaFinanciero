package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;


@Entity
@Table(schema="PROFIN", name="TIIPF_CAPITALIZACION_BURSATIL")
@SequenceGenerator(name = "SEQ_CAPITALIZACION_BURSATIL", sequenceName = "PROFIN.SEQ_CAPITALIZACION_BURSATIL", allocationSize = 1, initialValue = 20000)
public class CapitalizacionBursatil extends EntidadBase implements Comparable<CapitalizacionBursatil>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8047614997993327372L;
	private Long id; //ID_CAPITALIZACION	NUMBER
	private Tabla  divisa;//TT_ID_DIVISA	NUMBER	Y
	private String importe;//IMPORTE	VARCHAR2(40 BYTE)	
	private String fondosPropios;//FONDOS_PROPIOS	VARCHAR2(20 BYTE)
	private String fecha;//FECHA	VARCHAR2(10 BYTE)
	private String observacion;//OBSERVACION	VARCHAR2(400 BYTE)
	private Integer pos; //POS	NUMBER
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CAPITALIZACION_BURSATIL")
	@Column(name="ID_CAPITALIZACION")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_DIVISA")
	public Tabla getDivisa() {
		return divisa;
	}
	public void setDivisa(Tabla divisa) {
		this.divisa = divisa;
	}
	
	@Column(name="IMPORTE")
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	
	@Column(name="FONDOS_PROPIOS")
	public String getFondosPropios() {
		return fondosPropios;
	}
	public void setFondosPropios(String fondosPropios) {
		this.fondosPropios = fondosPropios;
	}
	
	@Column(name="FECHA")
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@Column(name="OBSERVACION")
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@Column(name="POS")
	public Integer getPos() {
		return pos;
	}
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	

	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name = "COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}
	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	
	public int compareTo(CapitalizacionBursatil o) {
		CapitalizacionBursatil ct = (CapitalizacionBursatil)o;
        if(Float.parseFloat(this.fondosPropios) < Float.parseFloat(ct.getFondosPropios()))
            return -1;
        else if(Float.parseFloat(this.fondosPropios) == Float.parseFloat(ct.getFondosPropios()))
            return 0;
        else
            return 1;
	}

	
}
