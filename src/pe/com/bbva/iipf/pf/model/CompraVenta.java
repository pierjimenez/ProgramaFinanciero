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
@Table(schema="PROFIN", name="TIIPF_COMPRA_VENTA")
@SequenceGenerator(name = "SEQ_COMPRA_VENTA", sequenceName = "PROFIN.SEQ_COMPRA_VENTA", allocationSize = 1, initialValue = 20000)
public class CompraVenta extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ; //ID_COMPRA_VENTA	NUMBER
	
	/**
	 * Indica si son importaciones o exportaciones
	 */
	private Integer tipo; //TIPO_COMP_VENT	NUMBER
	
	/**
	 *valor que indica si la cantidad esta en porcentaje o moneda extranjera 
	 */
	private Integer tipoTotal; // TIPO_TOTAL NUMBER
	private String total1; //TOTAL1	VARCHAR2(40 BYTE)
	private String total2; //TOTAL2	VARCHAR2(40 BYTE)
	private String total3; //TOTAL3	VARCHAR2(40 BYTE)
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30);
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPRA_VENTA")
	@Column(name="ID_COMPRA_VENTA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="TIPO_COMP_VENT")
	public Integer getTipo() {
		return tipo;
	}
	
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	@Column(name="TIPO_TOTAL")
	public Integer getTipoTotal() {
		return tipoTotal;
	}

	public void setTipoTotal(Integer tipoTotal) {
		this.tipoTotal = tipoTotal;
	}

	@Column(name="TOTAL1", length=40)
	public String getTotal1() {
		return total1;
	}
	
	public void setTotal1(String total1) {
		this.total1 = total1;
	}
	
	@Column(name="TOTAL2", length=40)
	public String getTotal2() {
		return total2;
	}
	
	public void setTotal2(String total2) {
		this.total2 = total2;
	}
	
	@Column(name="TOTAL3", length=40)
	public String getTotal3() {
		return total3;
	}
	
	public void setTotal3(String total3) {
		this.total3 = total3;
	}
	
	@ManyToOne(targetEntity = Programa.class)
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

	
	
}
