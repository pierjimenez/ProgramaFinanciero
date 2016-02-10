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

/**
 * 
 * @author MCORNETERO
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_SINTESISECONOMICA_BLOBS")
@NamedQueries({
@NamedQuery(name="updateComenSituFinancieraByEmpresa", 
	    query = " update SintesisEconomicoBlob set comenSituFinanciera=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateComenSituEconomicaByEmpresa", 
	    query = " update SintesisEconomicoBlob set comenSituEconomica=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateValoracionEconFinancieraByEmpresa", 
	    query = " update SintesisEconomicoBlob set valoracionEconFinanciera=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateValoracionPosiBalanceByEmpresa", 
	    query = " update SintesisEconomicoBlob set valoracionPosiBalance=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateSintesisEconFinanAddRCByEmpresa", 
	    query = " update SintesisEconomicoBlob set comenSintesisEconFinaddRDC=?,codigoEmpresa=? where id=?")
})
@SequenceGenerator(name = "SEQ_SINTECON_BLOB", sequenceName = "PROFIN.SEQ_SINTECON_BLOB", allocationSize = 1, initialValue = 20000)
public class SintesisEconomicoBlob extends EntidadBase {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 160381649952434837L;
	
	private Long id;//ID_SINTECON_BLOB NUMERIC
	private Programa programa; //IIPF_PROGRAMA_ID_PROGRAMA	NUMBER
	private String idEmpresa  ;   //ID_EMPRESA	NUMBER	Y			
	private String codigoEmpresa ; //CODIGO_EMPRESA	VARCHAR2(30)	Y	
	private byte[] comenSituFinanciera; //SF_COMENTARIO_SITU_FINA	byte[]
	private byte[] comenSituEconomica; //SF_COMENTARIO_SITU_ECON	byte[]
	private byte[] valoracionEconFinanciera; //SF_VALORACION_ECON_FINA	byte[]
	private byte[] valoracionPosiBalance; // SF_VALORACION_POSI_BALA	byte[]	
	
	private byte[] comenSintesisEconFinaddRDC;//RDC_COMEN_SINT_ECONFINAN_ADD	BLOB	Y	
			
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SINTECON_BLOB")
	@Column(name="ID_SINTECON_BLOB")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID_PROGRAMA")
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
	@Column( name= "SF_COMENTARIO_SITU_FINA")
	public byte[] getComenSituFinanciera() {
		return comenSituFinanciera;
	}
	
	public void setComenSituFinanciera(byte[] comenSituFinanciera) {
		this.comenSituFinanciera = comenSituFinanciera;
	}
	
	@Lob
	@Column( name= "SF_COMENTARIO_SITU_ECON")
	public byte[] getComenSituEconomica() {
		return comenSituEconomica;
	}
	
	public void setComenSituEconomica(byte[] comenSituEconomica) {
		this.comenSituEconomica = comenSituEconomica;
	}
	
	@Lob
	@Column( name= "SF_VALORACION_ECON_FINA")
	public byte[] getValoracionEconFinanciera() {
		return valoracionEconFinanciera;
	}
	
	public void setValoracionEconFinanciera(byte[] valoracionEconFinanciera) {
		this.valoracionEconFinanciera = valoracionEconFinanciera;
	}
	
	@Lob
	@Column( name= "SF_VALORACION_POSI_BALA")
	public byte[] getValoracionPosiBalance() {
		return valoracionPosiBalance;
	}
	
	public void setValoracionPosiBalance(byte[] valoracionPosiBalance) {
		this.valoracionPosiBalance = valoracionPosiBalance;
	}

	@Lob
	@Column(name="RDC_COMEN_SINT_ECONFINAN_ADD")
	public byte[] getComenSintesisEconFinaddRDC() {
		return comenSintesisEconFinaddRDC;
	}
	public void setComenSintesisEconFinaddRDC(byte[] comenSintesisEconFinaddRDC) {
		this.comenSintesisEconFinaddRDC = comenSintesisEconFinaddRDC;
	}
	


}
