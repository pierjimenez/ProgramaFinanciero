package pe.com.bbva.iipf.pf.model;

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

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="TIIPF_GARANTIA")
@NamedQueries({
@NamedQuery(name="listarGarantia", 
			query = " select e from Garantia e where e.programa.id = ? order by e.posicion, e.cuenta.id, e.tipo"),	
@NamedQuery(name="listarGarantiabyEmpresa", 
		query = " select e from Garantia e where e.programa.id = ? and e.codEmpresaGrupo=?  order by e.posicion, e.cuenta.id, e.tipo")	
})
@SequenceGenerator(name = "SEQ_GARANTIA", sequenceName = "PROFIN.SEQ_GARANTIA", allocationSize = 1, initialValue = 20000)
public class Garantia extends EntidadBase{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;//IIPF_GARANTIA_ID	NUMBER	Y			
	private Programa programa ; //IIPF_PROGRAMA_ID	INTEGER	Y			
	private String orden;//ORDEN_CUENTA	NUMBER	Y			
	private Tabla cuenta;//TT_ID_ORDEN_CUENTA	NUMBER	Y
	private transient String idcuentatmp; // ID_EMPRESA	NUMBER	N
	private String numeroGarantia;//NUMERO_GARANTIA	VARCHAR2(20)	Y			
	private String comentario;//COMENTARIO	VARCHAR2(2000)	Y			
	private String tipo;//TIPO	VARCHAR2(2)	Y	
	private Integer posicion;//POSICION	NUMBER	Y	
	private Long codigoAnexoGarantia;
	private String ordenFinal;
	private Tabla cuentaFinal;
	private transient String idcuentatmpFinal; // ID_EMPRESA	NUMBER	N
	private String numeroGarantiaAnterior;
	private String comentarioAnterior;
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	private Tabla conector;
	private transient String idconectortmp; // ID_EMPRESA	NUMBER	N
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GARANTIA")
	@Column(name="IIPF_GARANTIA_ID")
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
	
	@Column(name="NUMERO_GARANTIA")
	public String getNumeroGarantia() {
		return numeroGarantia;
	}
	public void setNumeroGarantia(String numeroGarantia) {
		this.numeroGarantia = numeroGarantia;
	}
	
	@Column(name="COMENTARIO")
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	@Column(name="TIPO")
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}
	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
	
	@Transient // no mapeada
	public String getIdcuentatmp() {
		return idcuentatmp;
	}
	public void setIdcuentatmp(String idcuentatmp) {
		this.idcuentatmp = idcuentatmp;
	}
	
	@Column(name="ID_ANEXO_GARANTIA")
	public Long getCodigoAnexoGarantia() {
		return codigoAnexoGarantia;
	}
	
	public void setCodigoAnexoGarantia(Long codigoAnexoGarantia) {
		this.codigoAnexoGarantia = codigoAnexoGarantia;
	}

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
	@Transient
	public String getIdcuentatmpFinal() {
		return idcuentatmpFinal;
	}
	public void setIdcuentatmpFinal(String idcuentatmpFinal) {
		this.idcuentatmpFinal = idcuentatmpFinal;
	}
	@Column(name="NUMERO_GARANTIA_ANTERIOR")
	public String getNumeroGarantiaAnterior() {
		return numeroGarantiaAnterior;
	}
	public void setNumeroGarantiaAnterior(String numeroGarantiaAnterior) {
		this.numeroGarantiaAnterior = numeroGarantiaAnterior;
	}
	@Column(name="COMENTARIO_ANTERIOR")
	public String getComentarioAnterior() {
		return comentarioAnterior;
	}
	public void setComentarioAnterior(String comentarioAnterior) {
		this.comentarioAnterior = comentarioAnterior;
	}
	
	@Column(name="COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}
	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_CONECTOR")
	public Tabla getConector() {
		return conector;
	}
	public void setConector(Tabla conector) {
		this.conector = conector;
	}
	
	@Transient
	public String getIdconectortmp() {
		return idconectortmp;
	}
	public void setIdconectortmp(String idconectortmp) {
		this.idconectortmp = idconectortmp;
	}

	
	
	

}
