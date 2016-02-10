package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

/**
 * 
 * @author MCORNETERO
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_SUSTENTO_OPERACION")
@NamedQueries({
@NamedQuery(name="findSustentoOperacionByPrograma", 
		    query = "select p from SustentoOperacion  p " +
		    		"where p.programa.id=? order by p.posicion asc"),
@NamedQuery(name="findSustentoOperacionByProgramaByEmpresa", 
	    query = "select p from SustentoOperacion  p " +
	    		"where p.programa.id=? and p.codEmpresaGrupo=? order by p.posicion asc"),
@NamedQuery(name="updatePosicionSustento", 
	    query = " update SustentoOperacion set posicion = ? where id = ? "),
})
@SequenceGenerator(name = "SEQ_SUSTENTO_OPERACION", sequenceName = "PROFIN.SEQ_SUSTENTO_OPERACION", allocationSize = 1, initialValue = 20000)

public class SustentoOperacion extends EntidadBase{
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ; //IIPF_SUSTENTO_OPERA_ID	NUMBER	Y			
	private Programa programa ;//IIPF_PROGRAMA_ID	INTEGER	Y			
	private String orden;//ORDEN_CUENTA	NUMBER	Y			
	private Tabla cuenta;//TT_ID_ORDEN_CUENTA	NUMBER	Y			
	private byte[] sustento;//SUSTENTO	BLOB	Y
	private Integer posicion; //POSICION	NUMBER	
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)	
	private String comentario;//COMENTARIO	VARCHAR2(2000)	Y
	private String ordenFinal;//ORDEN_CUENTA_FIN	NUMBER	Y
	private Tabla cuentaFinal;//TT_ID_ORDEN_CUENTA_FIN	NUMBER	Y
	private String sustentoString;

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SUSTENTO_OPERACION")
	@Column(name="IIPF_SUSTENTO_OPERA_ID")
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
	
	@Column(name="ORDEN_CUENTA")
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_ORDEN_CUENTA")
	public Tabla getCuenta() {
		return cuenta;
	}
	public void setCuenta(Tabla cuenta) {
		this.cuenta = cuenta;
	}
	
	@Lob
	@Column( name= "SUSTENTO")
	public byte[] getSustento() {
		return sustento;
	}
	public void setSustento(byte[] sustento) {
		this.sustento = sustento;
	}
		
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	
	@Column(name="COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}
	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	/**gmp-20130410**/
	@Column(name="ORDEN_CUENTA_FIN")
	public String getOrdenFinal() {
		return ordenFinal;
	}
	public void setOrdenFinal(String ordenFinal) {
		this.ordenFinal = ordenFinal;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_ORDEN_CUENTA_FIN")
	public Tabla getCuentaFinal() {
		return cuentaFinal;
	}
	public void setCuentaFinal(Tabla cuentaFinal) {
		this.cuentaFinal = cuentaFinal;
	}

	@Column(name="COMENTARIO")
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	/**gmp-20130410**/
	
	@Transient
	public String getSustentoString() {
		return sustentoString;
	}
	public void setSustentoString(String sustentoString) {
		this.sustentoString = sustentoString;
	}
	

}
