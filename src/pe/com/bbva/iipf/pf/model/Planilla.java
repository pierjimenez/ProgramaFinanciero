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

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name = "TIIPF_PLANILLA")
@SequenceGenerator(name = "SEQ_PLANILLA", sequenceName = "PROFIN.SEQ_PLANILLA", allocationSize = 1, initialValue = 20000)
public class Planilla extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //ID_PLANILLA	NUMBER
	private Tabla tipoPerido; //TT_ID_TIPO_PERIODIO	NUMBER
	private Tabla TipoPersonal; //TT_ID_TIPO_PERSONAL	NUMBER
	private Integer total1; // TOTAL1	NUMBER
	private Integer total2; // TOTAL2	NUMBER
	private Integer total3; // TOTAL3	NUMBER
	private Integer total4; // TOTAL4	NUMBER
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private Integer anio1; //ANIO1	NUMBER
	private Integer anio2; //ANIO2	NUMBER
	private Integer anio3; //ANIO3	NUMBER
	private Integer anio4; //ANIO4	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)		

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_PLANILLA")
	@Column(name = "ID_PLANILLA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ANIO1")
	public Integer getAnio1() {
		return anio1;
	}

	public void setAnio1(Integer anio1) {
		this.anio1 = anio1;
	}

	@Column(name = "ANIO2")
	public Integer getAnio2() {
		return anio2;
	}

	public void setAnio2(Integer anio2) {
		this.anio2 = anio2;
	}

	@Column(name = "ANIO3")
	public Integer getAnio3() {
		return anio3;
	}

	public void setAnio3(Integer anio3) {
		this.anio3 = anio3;
	}

	@Column(name = "ANIO4")
	public Integer getAnio4() {
		return anio4;
	}

	public void setAnio4(Integer anio4) {
		this.anio4 = anio4;
	}

	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_PERIODIO")
	public Tabla getTipoPerido() {
		return tipoPerido;
	}
	
	public void setTipoPerido(Tabla tipoPerido) {
		this.tipoPerido = tipoPerido;
	}
	
	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_PERSONAL")
	public Tabla getTipoPersonal() {
		return TipoPersonal;
	}
	
	public void setTipoPersonal(Tabla tipoPersonal) {
		TipoPersonal = tipoPersonal;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name = "TOTAL1")
	public Integer getTotal1() {
		return total1;
	}

	public void setTotal1(Integer total1) {
		this.total1 = total1;
	}

	@Column(name = "TOTAL2")
	public Integer getTotal2() {
		return total2;
	}

	public void setTotal2(Integer total2) {
		this.total2 = total2;
	}

	@Column(name = "TOTAL3")
	public Integer getTotal3() {
		return total3;
	}

	public void setTotal3(Integer total3) {
		this.total3 = total3;
	}

	@Column(name = "TOTAL4")
	public Integer getTotal4() {
		return total4;
	}

	public void setTotal4(Integer total4) {
		this.total4 = total4;
	}

	@Column(name = "COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}

	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}

	
	
}
