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

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_ARCHIVO_ANEXO")
@SequenceGenerator(name = "SEQ_ACHIVO_ANEXO", 
				   sequenceName = "PROFIN.SEQ_ACHIVO_ANEXO", 
				   allocationSize = 1, initialValue = 1)
public class ArchivoAnexo extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //	 "ID_FILE_ANEXO"    NUMBER(19,0),
	private Programa programa; //    "IIPF_PROGRAMA_ID" NUMBER(*,0) NOT NULL ENABLE,
	private String extencion; //    "EXTENCION_ARCH"   VARCHAR2(4 BYTE),
	private String nombreArchivo; //"NOMBRE_ARCHIVO"           VARCHAR2(600 BYTE),
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ACHIVO_ANEXO")
	@Column(name="ID_FILE_ANEXO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name="EXTENCION_ARCH", length=4)
	public String getExtencion() {
		return extencion;
	}
	
	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}
	
	@Column(name="NOMBRE_ARCHIVO", length=600)
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
}
