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
 * @author EPOMAYAY
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_RATING")
@SequenceGenerator(name = "SEQ_RATING", sequenceName = "PROFIN.SEQ_RATING", allocationSize = 1, initialValue = 20000)
public class Rating extends EntidadBase  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //ID_RATING	NUMBER
	private String descripcion; //DESCRIPCION	VARCHAR2(30 BYTE)
	private String totalAnio2;//TOTAL_ANIO2	NUMBER
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER
	private String totalAnio1; //TOTAL_ANIO1	NUMBER
	private String totalAnioActual ; //TOTAL_ANIO_ACT	NUMBER
	
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	private Integer pos; //POS	NUMBER
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RATING")
	@Column(name= "ID_RATING")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name= "DESCRIPCION", length=30)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name= "TOTAL_ANIO2")
	public String getTotalAnio2() {
		String cadBus="-";
		Double totalAnio2L;
		String [] arrvalores;
		try {
			if (totalAnio2!=null){
			totalAnio2L=Double.parseDouble(totalAnio2);
			totalAnio2=String.valueOf(totalAnio2L);
			}
		} catch (Exception e) {
			arrvalores=totalAnio2.split("-");
			if(arrvalores.length==3){
				totalAnio2=arrvalores[1]+"/"+arrvalores[2];
			}else{
				totalAnio2=totalAnio2;
			}
		}
		return totalAnio2;
	}
	
	public void setTotalAnio2(String totalAnio2) {
		this.totalAnio2 = totalAnio2;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name= "TOTAL_ANIO1")
	public String getTotalAnio1() {
		String cadBus="-";
		Double totalAnio1L;
		String [] arrvalores;
		try {
			if (totalAnio1!=null){
				totalAnio1L=Double.parseDouble(totalAnio1);
				totalAnio1=String.valueOf(totalAnio1L);
			}
		} catch (Exception e) {
			arrvalores=totalAnio1.split("-");
			if(arrvalores.length==3){
				totalAnio1=arrvalores[1]+"/"+arrvalores[2];
			}else{
				totalAnio1=totalAnio1;
			}
		}

		return totalAnio1;
	}
	
	public void setTotalAnio1(String totalAnio1) {
		this.totalAnio1 = totalAnio1;
	}
	
	@Column(name= "TOTAL_ANIO_ACT")
	public String getTotalAnioActual() {
		String cadBus="-";
		Double totalAnioActual1;
		String [] arrvalores;
		try {
			if (totalAnioActual!=null){
			totalAnioActual1=Double.parseDouble(totalAnioActual);
			totalAnioActual=String.valueOf(totalAnioActual1);
			}
		} catch (Exception e) {
			arrvalores=totalAnioActual.split("-");
			if(arrvalores.length==3){
				totalAnioActual=arrvalores[1]+"/"+arrvalores[2];
			}else{
				totalAnioActual=totalAnioActual;
			}
		}
		
		return totalAnioActual;
	}
	
	public void setTotalAnioActual(String totalAnioActual) {
		this.totalAnioActual = totalAnioActual;
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
