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
@Table(schema="PROFIN", name = "TIIPF_DATOSBASICO_BLOBS")
@NamedQueries({
@NamedQuery(name="updateSintesisEmpresaByEmpresa", 
	    query = " update DatosBasicoBlob set sintesisEmpresa=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateDatosMatrizByEmpresa", 
	    query = " update DatosBasicoBlob set datosMatriz=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateEspacioLibreByEmpresa", 
	    query = " update DatosBasicoBlob set espacioLibre=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateComenComprasVentasByEmpresa", 
	    query = " update DatosBasicoBlob set comenComprasVentas=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateConcentracionByEmpresa", 
	    query = " update DatosBasicoBlob set concentracion=?,codigoEmpresa=? where id=?"),
@NamedQuery(name="updateDatosBasicosAddRCByEmpresa", 
	    query = " update DatosBasicoBlob set datosBasicosaddRDC=?,codigoEmpresa=? where id=?"),
	    @NamedQuery(name="updateValoracionByEmpresa", 
	    	    query = " update DatosBasicoBlob set valoracion=?,codigoEmpresa=? where id=?")	    
})
@SequenceGenerator(name = "SEQ_DATOSBASICO_BLOB", sequenceName = "PROFIN.SEQ_DATOSBASICO_BLOB", allocationSize = 1, initialValue = 20000)
public class DatosBasicoBlob extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8634105614625005512L;
	
	private Long id;//ID_DATOBASICO_BLOB	NUMBER(19)	Y	
	private Programa programa; //ID_PROGRAMA	INTEGER	N		
	private String idEmpresa  ;   //ID_EMPRESA	NUMBER	Y				
	private String codigoEmpresa ; //CODIGO_EMPRESA	VARCHAR2(30)	Y	
	private byte[] sintesisEmpresa; //DB_SINTESIS_EMPR	byte[]
	private byte[] datosMatriz; //DB_DATOS_MATR	byte[]
	private byte[] espacioLibre; //DB_ESPACIO_LIBRE	byte[]
	private byte[] comenComprasVentas; //DB_ESPACIO_LIBRE	byte[]
	private byte[] concentracion; //DB_CONCENTRACION byte[]

	private byte[] datosBasicosaddRDC;//RDC_COMEN_DATOSBASICOS_ADD	BLOB	Y			
	private byte[] valoracion; //DB_VALORACION byte[]
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DATOSBASICO_BLOB")
	@Column(name="ID_DATOBASICO_BLOB")
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
	@Column( name= "DB_SINTESIS_EMPR")
	public byte[] getSintesisEmpresa() {
		return sintesisEmpresa;
	}
	public void setSintesisEmpresa(byte[] sintesisEmpresa) {
		this.sintesisEmpresa = sintesisEmpresa;
	}
	
	@Lob
	@Column( name= "DB_DATOS_MATR")
	public byte[] getDatosMatriz() {
		return datosMatriz;
	}
	public void setDatosMatriz(byte[] datosMatriz) {
		this.datosMatriz = datosMatriz;
	}
	
	@Lob
	@Column( name= "DB_ESPACIO_LIBRE")
	public byte[] getEspacioLibre() {
		return espacioLibre;
	}
	public void setEspacioLibre(byte[] espacioLibre) {
		this.espacioLibre = espacioLibre;
	}
	
	@Lob
	@Column(name="DB_COMENTARIO_COMPR_VENT")
	public byte[] getComenComprasVentas() {
		return comenComprasVentas;
	}
	public void setComenComprasVentas(byte[] comenComprasVentas) {
		this.comenComprasVentas = comenComprasVentas;
	}
	
	@Lob
	@Column(name="DB_CONCENTRACION")
	public byte[] getConcentracion() {
		return concentracion;
	}
	public void setConcentracion(byte[] concentracion) {
		this.concentracion = concentracion;
	}
	
	@Lob
	@Column(name="RDC_COMEN_DATOSBASICOS_ADD")
	public byte[] getDatosBasicosaddRDC() {
		return datosBasicosaddRDC;
	}
	public void setDatosBasicosaddRDC(byte[] datosBasicosaddRDC) {
		this.datosBasicosaddRDC = datosBasicosaddRDC;
	}
	
	@Lob
	@Column(name="DB_VALORACION")
	public byte[] getValoracion() {
		return valoracion;
	}
	public void setValoracion(byte[] valoracion) {
		this.valoracion = valoracion;
	}
	


	
	
	
	
}
