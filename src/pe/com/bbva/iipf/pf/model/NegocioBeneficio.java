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

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_NEGOCIO_BENEFICIO")
@NamedQueries({
@NamedQuery(name="findNegocioBeneficioByPrograma", 
		    query = "select n from NegocioBeneficio  n " +
		    		"where n.programa.id=? and n.tipo=? order by n.posicion asc"),
@NamedQuery(name="findNegocioBeneficioByProgramaCodEmpGrup", 
	    query = "select n from NegocioBeneficio  n " +
	    		"where n.programa.id=? and n.tipo=? and n.codEmpresaGrupo=? order by n.posicion asc"),
})
@SequenceGenerator(name = "SEQ_NEGOCIO_BENEFI", sequenceName = "PROFIN.SEQ_NEGOCIO_BENEFI", allocationSize = 1, initialValue = 20000)
public class NegocioBeneficio extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id; //ID_NEGOCIO_BENEFICIO	NUMBER
	private String descripcion; //DESCRIP_NEGO_BENEF	VARCHAR2(100 BYTE)
	private Integer tipo; //TIPO	NUMBER
	private String totalI1;//TOTAL_I1	VARCHAR2(10 BYTE)
	private String totalI2;//TOTAL_I2	VARCHAR2(10 BYTE)
	private String totalI3;//TOTAL_I3	VARCHAR2(10 BYTE)
	private String totalB1;//TOTAL_B1	VARCHAR2(10 BYTE)
	private String totalB2;//TOTAL_B2	VARCHAR2(10 BYTE)
	private String totalB3;//TOTAL_B3	VARCHAR2(10 BYTE)
	private Integer posicion; //POSICION NUMBER
	private Programa programa;//IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_NEGOCIO_BENEFI")
	@Column(name="ID_NEGOCIO_BENEFICIO")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DESCRIP_NEGO_BENEF", length=100)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name="TIPO")
	public Integer getTipo() {
		return tipo;
	}
	
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
	@Column(name="TOTAL_I1", length=10)
	public String getTotalI1() {
		return totalI1;
	}
	
	public void setTotalI1(String totalI1) {
		this.totalI1 = totalI1;
	}
	
	@Column(name="TOTAL_I2", length=10)
	public String getTotalI2() {
		return totalI2;
	}
	
	public void setTotalI2(String totalI2) {
		this.totalI2 = totalI2;
	}
	
	@Column(name="TOTAL_I3", length=10)
	public String getTotalI3() {
		return totalI3;
	}
	
	public void setTotalI3(String totalI3) {
		this.totalI3 = totalI3;
	}
	
	@Column(name="TOTAL_B1", length=10)
	public String getTotalB1() {
		return totalB1;
	}
	
	public void setTotalB1(String totalB1) {
		this.totalB1 = totalB1;
	}
	
	@Column(name="TOTAL_B2", length=10)
	public String getTotalB2() {
		return totalB2;
	}
	
	public void setTotalB2(String totalB2) {
		this.totalB2 = totalB2;
	}
	
	@Column(name="TOTAL_B3", length=10)
	public String getTotalB3() {
		return totalB3;
	}
	
	public void setTotalB3(String totalB3) {
		this.totalB3 = totalB3;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@Column(name="POSICION")
	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	@Column(name = "COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}

	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
	
	
	
}
