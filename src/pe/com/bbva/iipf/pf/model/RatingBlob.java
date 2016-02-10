package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;


@Entity
@Table(schema="PROFIN", name = "TIIPF_RATING_BLOBS")
@NamedQueries({
@NamedQuery(name="updateValoracionRatingByEmpresa", 
	    query = " update RatingBlob set valoracionRating=?,codigoEmpresa=? where id=?")
})
@SequenceGenerator(name = "SEQ_RATING_BLOB", sequenceName = "PROFIN.SEQ_RATING_BLOB", allocationSize = 1, initialValue = 20000)

public class RatingBlob extends EntidadBase{
	
	private Long id;//ID_RATING_BLOB NUMERIC
	private Programa programa; //ID_PROGRAMA	NUMBER
	private String idEmpresa  ;   //ID_EMPRESA	NUMBER	Y			
	private String codigoEmpresa ; //CODIGO_EMPRESA	VARCHAR2(30)	Y	
	private byte[] valoracionRating; //RA_VALORACION_RATI	byte[]
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RATING_BLOB")
	@Column(name="ID_RATING_BLOB")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="ID_PROGRAMA")
	public Programa getPrograma() {
		return programa;
	}
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name="ID_EMPRESA")
	public String getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	@Column(name="CODIGO_EMPRESA", length = 30)
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	
	@Lob
	@Column( name= "RA_VALORACION_RATI")
	public byte[] getValoracionRating() {
		return valoracionRating;
	}
	public void setValoracionRating(byte[] valoracionRating) {
		this.valoracionRating = valoracionRating;
	}
	


	
	

}
