package pe.com.bbva.iipf.pf.model;


import javax.persistence.Entity;



/**
 * 
 * @author MCornetero
 *
 */
@Entity
//@Table(schema="PROFIN", name = "TIIPF_KC10COMEX")
//@SequenceGenerator(name = "SEQ_KC10COMEX", sequenceName = "PROFIN.SEQ_KC10COMEX", allocationSize = 1, initialValue = 20000)
public class kc10comex  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //ID_KC10COMEX	NUMBER
	private String codigoCentral;//CODIGO_CENTRAL
	private String descripcion; //DESCRIPCION	VARCHAR2(30 BYTE)
	private String cantidad; //cantidad
	private String importeAcumulado; //importe_Acumulado
	private String comisionesAcumuladas; //comisiones_Acumuladas
	private String anio; //anio
	private String tipoComex;//TIPO_COMEX


	
//	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_KC10COMEX")
//	@Column(name= "ID_KC10COMEX")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

//	@Column(name= "DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
//	@Column(name= "CANTIDAD")
	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

//	@Column(name= "IMPORTE_ACUMULADO")
	public String getImporteAcumulado() {
		return importeAcumulado;
	}

	public void setImporteAcumulado(String importeAcumulado) {
		this.importeAcumulado = importeAcumulado;
	}

//	@Column(name= "COMISIONES_ACUMULADAS")
	public String getComisionesAcumuladas() {
		return comisionesAcumuladas;
	}

	public void setComisionesAcumuladas(String comisionesAcumuladas) {
		this.comisionesAcumuladas = comisionesAcumuladas;
	}

//	@Column(name= "ANIO")
	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

//	@Column(name= "TIPO_COMEX")
	public String getTipoComex() {
		return tipoComex;
	}

	public void setTipoComex(String tipoComex) {
		this.tipoComex = tipoComex;
	}
	
	
	
}


