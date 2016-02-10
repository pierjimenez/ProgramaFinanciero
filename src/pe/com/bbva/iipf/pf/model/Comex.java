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

/**
 * 
 * @author MCornetero
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_COMEX")
@SequenceGenerator(name = "SEQ_COMEX", sequenceName = "PROFIN.SEQ_COMEX", allocationSize = 1, initialValue = 20000)
public class Comex extends EntidadBase  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //ID_COMEX	NUMBER
	private String descripcion; //DESCRIPCION	VARCHAR2(30 BYTE)
	private String cantidad; //cantidad
	private String importeAcumulado; //importe_Acumulado
	private String comisionesAcumuladas; //comisiones_Acumuladas
	private String anio; //anio
	private String tipoComex;//TIPO_COMEX
	
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER	
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	private Integer pos; //POS	NUMBER
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMEX")
	@Column(name= "ID_COMEX")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name= "DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name= "CANTIDAD")
	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name= "IMPORTE_ACUMULADO")
	public String getImporteAcumulado() {
		return importeAcumulado;
	}

	public void setImporteAcumulado(String importeAcumulado) {
		this.importeAcumulado = importeAcumulado;
	}

	@Column(name= "COMISIONES_ACUMULADAS")
	public String getComisionesAcumuladas() {
		return comisionesAcumuladas;
	}

	public void setComisionesAcumuladas(String comisionesAcumuladas) {
		this.comisionesAcumuladas = comisionesAcumuladas;
	}

	@Column(name= "ANIO")
	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
	

	@Column(name= "TIPO_COMEX")
	public String getTipoComex() {
		return tipoComex;
	}

	public void setTipoComex(String tipoComex) {
		this.tipoComex = tipoComex;
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

	@Column(name="POS")
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
	
	
	
}

