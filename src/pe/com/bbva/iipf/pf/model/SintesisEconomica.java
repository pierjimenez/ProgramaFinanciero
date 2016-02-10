package pe.com.bbva.iipf.pf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import pe.com.stefanini.core.domain.EntidadBase;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Entity
@Table(schema="PROFIN", name="TIIPF_SINTESIS_ECONOMICA")
@NamedQueries({
@NamedQuery(name="listarSintesisEconomico", 
		    query = "from  SintesisEconomica " +
		    		"where programa.id=? order by fechaCarga desc "),
@NamedQuery(name="listarSintesisEconomicoByEmpresa", 
	    query = "from  SintesisEconomica " +
	    		"where programa.id=? and nombreEmpresa=?  order by fechaCarga desc "),
})
@SequenceGenerator(name = "SEQ_SINTESIS_ECONOMICO", sequenceName = "PROFIN.SEQ_SINTESIS_ECONOMICO", allocationSize = 1, initialValue = 20000)
public class SintesisEconomica extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; // ID_SISNTESIS_FINANCIERA;
	private Date fechaCarga; //FECHA_CARG	DATE
	private String nombreEmpresa; //EMPRESA	VARCHAR2(60 BYTE)
	private String usuario; //USUARIO	VARCHAR2(30 BYTE)
	private String nombreArchivo;//NOMBRE_ARCHIVO VARCHAR2(70 BYTE)
	private String codigoArchivo;//CODIGO_ARCHIVO	VARCH70 BYTE)
	private String extension; //EXTENCION_ARCH	VARCHAR2(4 BYTE)
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private transient String codEmpresa;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SINTESIS_ECONOMICO")
	@Column(name = "ID_SISNTESIS_FINANCIERA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FECHA_CARG")
	public Date getFechaCarga() {
		return fechaCarga;
	}
	
	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	
	@Column(name = "EMPRESA")
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	
	@Column(name = "USUARIO")
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	@Column(name = "CODIGO_ARCHIVO")
	public String getCodigoArchivo() {
		return codigoArchivo;
	}
	
	public void setCodigoArchivo(String codigoArchivo) {
		this.codigoArchivo = codigoArchivo;
	}
	
	@Column(name = "EXTENCION_ARCH", length=4)
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	@Column(name = "NOMBRE_ARCHIVO", length=200)
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Transient // no mapeada
	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	
	

}
