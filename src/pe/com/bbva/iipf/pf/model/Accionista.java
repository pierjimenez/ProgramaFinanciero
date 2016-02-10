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
import javax.persistence.Transient;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_ACCIONISTA")
@SequenceGenerator(name = "SEQ_ACCIONISTA", sequenceName = "PROFIN.SEQ_ACCIONISTA", allocationSize = 1, initialValue = 20000)
public class Accionista extends EntidadBase implements Comparable<Accionista>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //ID_ACCIONISTA	NUMBER
	private String nombre;//NOMBRES_ACCI	VARCHAR2(60 BYTE)
	private String porcentaje;//PORCENTAJE	VARCHAR2(4 BYTE)
	private String nacionalidad;//NACIONALIDAD	VARCHAR2(60 BYTE)
	private String capitalizacionBurs;//CAPITALIZACION_BURS	VARCHAR2(60 BYTE)
	private Integer pos; //POS	NUMBER
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	
	
	private  Tabla tipoDocumento;
	private  String numeroDocumento;
	private  String ruc;
	private  String codigoCentral;
	private String edad;
	private String tipoNumeroDocumentoHost;
	
	
	private transient String control;
	
	
	

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ACCIONISTA")
	@Column(name="ID_ACCIONISTA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NOMBRES_ACCI")
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="PORCENTAJE", length = 7)
	public String getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	@Column(name="NACIONALIDAD")
	public String getNacionalidad() {
		return nacionalidad;
	}
	
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	@Column(name="CAPITALIZACION_BURS")
	public String getCapitalizacionBurs() {
		return capitalizacionBurs;
	}
	
	public void setCapitalizacionBurs(String capitalizacionBurs) {
		this.capitalizacionBurs = capitalizacionBurs;
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
	
	public int compareTo(Accionista o) {
		Accionista ct = (Accionista)o;
        if(Float.parseFloat(this.porcentaje) < Float.parseFloat(ct.getPorcentaje()))
            return -1;
        else if(Float.parseFloat(this.porcentaje) == Float.parseFloat(ct.getPorcentaje()))
            return 0;
        else
            return 1;
	}

	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPODOCUMENTO")
	public Tabla getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Tabla tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	
	@Column(name = "NUMERO_DOCUMENTO")
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Column(name = "RUC")
	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	@Column(name = "CODIGO_CENTRAL") 
	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}
	@Column(name = "EDAD") 
	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}
	
	@Transient // no mapeada
	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	@Column(name = "TIP_NUM_DOCUMENTO_HOST") 
	public String getTipoNumeroDocumentoHost() {
		return tipoNumeroDocumentoHost;
	}

	public void setTipoNumeroDocumentoHost(String tipoNumeroDocumentoHost) {
		this.tipoNumeroDocumentoHost = tipoNumeroDocumentoHost;
	}


	
	
}
