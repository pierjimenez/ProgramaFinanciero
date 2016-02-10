package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_CABTABLA")
@SequenceGenerator(name = "SEQ_CABTABLA", sequenceName = "PROFIN.SEQ_CABTABLA", allocationSize = 1, initialValue = 20000)
public class CabTabla extends EntidadBase implements Comparable<CabTabla>{
	
	private static final long serialVersionUID = 7165775574667854503L;
	
	private Long id;//IIPF_CABTABLA_ID	NUMBER	Y			
	private Tabla tipoTabla;//TT_ID_TIPO_TABLA	NUMBER	Y			
	private Programa programa;//IIPF_PROGRAMA_ID	NUMBER	Y	
	private String  cabeceraTabla;//NOMBRE_CABTABLA	VARCHAR2(250)	Y
	
	private Integer posicion;//ORDEN	NUMBER	Y	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CABTABLA")
	@Column(name="IIPF_CABTABLA_ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_TABLA")
	public Tabla getTipoTabla() {
		return tipoTabla;
	}
	public void setTipoTabla(Tabla tipoTabla) {
		this.tipoTabla = tipoTabla;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
		
	@Column(name="NOMBRE_CABTABLA")
	public String getCabeceraTabla() {
		return cabeceraTabla;
	}
	public void setCabeceraTabla(String cabeceraTabla) {
		this.cabeceraTabla = cabeceraTabla;
	}
	
	@Column(name = "ORDEN")
	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	

	public int compareTo(CabTabla o) {
		CabTabla ct = (CabTabla)o;
        if(this.posicion < ct.getPosicion())
            return -1;
        else if(this.posicion == ct.getPosicion())
            return 0;
        else
            return 1;
	}



	
}
