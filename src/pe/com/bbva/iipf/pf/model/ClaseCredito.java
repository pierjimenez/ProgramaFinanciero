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
import javax.persistence.OneToOne;
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
@Table(schema="PROFIN", name = "TIIPF_CLASE_CREDITO")
@NamedQueries({
@NamedQuery(name="findClaseCreditoByPrograma", 
		    query = "select p from ClaseCredito  p " +
		    		"where p.programa.id=? order by p.posicion asc"),
@NamedQuery(name="findClaseCreditoByProgramaByEmpresa", 
	    query = "select p from ClaseCredito  p " +
	    		"where p.programa.id=? and p.codEmpresaGrupo=?  order by p.flagCancelado desc, p.posicion asc")
})
@SequenceGenerator(name = "SEQ_CLASE_CREDITO", sequenceName = "PROFIN.SEQ_CLASE_CREDITO", allocationSize = 1, initialValue = 20000)
public class ClaseCredito extends EntidadBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ; //IIPF_CLASE_CREDITO_ID	NUMBER
	
	private Integer posicion; //POSICION	NUMBER
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER
	private String orden;//ORDEN_CUENTA	NUMBER			
	private Tabla cuenta;//TT_ID_ORDEN_CUENTA	NUMBER	Y	
	private Tabla moneda;//TT_ID_MONEDA	NUMBER	Y	
	private String importe;//IMPORTE	VARCHAR2(20)	Y
	private String claseCredito	;//CLASE_CREDITO	VARCHAR2(200)	Y	
	private String tasaComision;//TASA_COMISION	VARCHAR2(200)	Y	
	private String vencimiento; //VENCIMIENTO	VARCHAR2(200)	Y	
	private String nota;//NOTA	VARCHAR2(2000)	Y	
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	private String  tipoVcto; //TIPO_VCTO	VARCHAR2(1)
	private String reembolso;
	private String flagCancelado;//FLAG_CANCELADO	VARCHAR2(2)
	private String flagTipoLimte;//FLAG_TIPOLIMITE	VARCHAR2(2)			
				
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CLASE_CREDITO")
	@Column(name="IIPF_CLASE_CREDITO_ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	@OneToOne(targetEntity = Programa.class)
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

	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_MONEDA")
	public Tabla getMoneda() {
		return moneda;
	}

	public void setMoneda(Tabla moneda) {
		this.moneda = moneda;
	}

	@Column(name="IMPORTE")
	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	@Column(name="CLASE_CREDITO")
	public String getClaseCredito() {
		return claseCredito;
	}

	public void setClaseCredito(String claseCredito) {
		this.claseCredito = claseCredito;
	}

	@Column(name="TASA_COMISION")
	public String getTasaComision() {
		return tasaComision;
	}

	public void setTasaComision(String tasaComision) {
		this.tasaComision = tasaComision;
	}

	@Column(name="VENCIMIENTO")
	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}

	@Column(name="NOTA")
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	@Column(name="COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}

	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	@Column(name="TIPO_VCTO")
	public String getTipoVcto() {
		return tipoVcto;
	}

	public void setTipoVcto(String tipoVcto) {
		this.tipoVcto = tipoVcto;
	}

	@Transient
	public String getReembolso() {
		return reembolso;
	}

	public void setReembolso(String reembolso) {
		this.reembolso = reembolso;
	}
	
	@Column(name="FLAG_CANCELADO")
	public String getFlagCancelado() {
		return flagCancelado;
	}

	public void setFlagCancelado(String flagCancelado) {
		this.flagCancelado = flagCancelado;
	}

	@Column(name="FLAG_TIPOLIMITE")
	public String getFlagTipoLimte() {
		return flagTipoLimte;
	}

	public void setFlagTipoLimte(String flagTipoLimte) {
		this.flagTipoLimte = flagTipoLimte;
	}
	
	
	
	

}
