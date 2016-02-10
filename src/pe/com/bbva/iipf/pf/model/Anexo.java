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

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_ANEXOPF")
@NamedQueries({
@NamedQuery(name="obtieneAnexoPorId", 
			query = " select o from Anexo o where o.id = ?"),
@NamedQuery(name="deleteAnexoByIdProgram", 
		    query = " delete from Anexo a " +
				"where a.programa.id = ?" ),
@NamedQuery(name="updateRatingAnexopf", 
	    query = " update Anexo set rating = ?,fecha = ? " +
	    		"where id = ? "),
})
@SequenceGenerator(name = "SEQ_ANEXO", sequenceName = "PROFIN.SEQ_ANEXO", allocationSize = 1, initialValue = 20000)
public class Anexo extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id; //ID_ANEXO	NUMBER
	private Integer tipoFila; //TIPO_FILA	NUMBER
	private Integer posFila;//POS_FILA	NUMBER
	private Programa programa; //IIPF_PROGRAMA_ID	NUMBER
	private String descripcion; //DESC_FILA	VARCHAR2(60 BYTE)
//	private Long padre; //PADRE	NUMBER
	
	private transient Long padretmp;
	private transient Long idtemp;
	
	private String codigoItem;
	
	
	
	private String codigoFila;		//  CODIGO_FILA               NUMBER,
	private Long idpadre; 			//  ID_ANEXO_PADRE            NUMBER,
	
	private String bureau;			//  BUREAU                    VARCHAR2(20),
	private String rating;			//  RATING                     VARCHAR2(20),
	private String fecha;			//  FECHA                      VARCHAR2(10),
	private String lteAutorizado;	//  LTE_AUTORIZADO             VARCHAR2(30),
	private String lteForm;			//  LTE_FORM                   VARCHAR2(30),
	private String rgoActual;		//  RGO_ACTUAL                 VARCHAR2(30),
	private String rgoPropBbvaBc;	//  RGO_PROP_BBVA_BC           VARCHAR2(30),
	private String propRiesgo;		//  PROP_RIESGO                VARCHAR2(30),
	private String observaciones;	//  OBSERVACIONES              VARCHAR2(4000),
	private String columna1;		//  COLUMNA1                   VARCHAR2(50),
	private String columna2;		// COLUMNA2                   VARCHAR2(50),
	private String columna3;		// COLUMNA3                   VARCHAR2(50),
	private String columna4;		// COLUMNA4                   VARCHAR2(50),
	private String columna5;		//COLUMNA5                   VARCHAR2(50),
	private String columna6;		//COLUMNA6                   VARCHAR2(50),
	private String columna7;		//  COLUMNA7                   VARCHAR2(50),
	private String columna8;		//  COLUMNA8                   VARCHAR2(50),
	private String columna9;		//  COLUMNA9                   VARCHAR2(50),
	private String columna10;		//  COLUMNA10                  VARCHAR2(50),
	private String numColumna;			//  NUM_COLUMNA                NUMBER
	private String contrato;//CONTRATO
	
	private transient String activoCol1="0";
	private transient String activoCol2="0";
	private transient String activoCol3="0";
	private transient String activoCol4="0";
	private transient String activoCol5="0";
	private transient String activoCol6="0";
	private transient String activoCol7="0";
	private transient String activoCol8="0";
	private transient String activoCol9="0";
	private transient String activoCol10="0";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ANEXO")
	@Column(name="ID_ANEXO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column(name="TIPO_FILA")
	public Integer getTipoFila() {
		return tipoFila;
	}
	
	public void setTipoFila(Integer tipoFila) {
		this.tipoFila = tipoFila;
	}
	
	@Column(name="POS_FILA")
	public Integer getPosFila() {
		return posFila;
	}
	
	public void setPosFila(Integer posFila) {
		this.posFila = posFila;
	}
	
	@ManyToOne(targetEntity = Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}
	
	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	@Column(name="DESC_FILA", length = 60)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Transient // no mapeada
	public Long getPadretmp() {
		return padretmp;
	}

	public void setPadretmp(Long padretmp) {
		this.padretmp = padretmp;
	}

	@Transient // no mapeada
	public Long getIdtemp() {
		return idtemp;
	}
	
	public void setIdtemp(Long idtemp) {
		this.idtemp = idtemp;
	}

	@Column(name="CODIGOITEM")
	public String getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}


//adicionado
	

	
	@Column(name="CODIGO_FILA")
	public String getCodigoFila() {
		return codigoFila;
	}
	public void setCodigoFila(String codigoFila) {
		this.codigoFila = codigoFila;
	}
	

	@Column(name="ID_ANEXO_PADRE")
	public Long getIdpadre() {
		return idpadre;
	}
	public void setIdpadre(Long idpadre) {
		this.idpadre = idpadre;
	}
	
	@Column(name="BUREAU")
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	
	@Column(name="RATING")
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	@Column(name="FECHA")
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@Column(name="LTE_AUTORIZADO")
	public String getLteAutorizado() {
		return lteAutorizado;
	}
	public void setLteAutorizado(String lteAutorizado) {
		this.lteAutorizado = lteAutorizado;
	}
	
	@Column(name="LTE_FORM")
	public String getLteForm() {
		return lteForm;
	}
	public void setLteForm(String lteForm) {
		this.lteForm = lteForm;
	}
	
	@Column(name="RGO_ACTUAL")
	public String getRgoActual() {
		return rgoActual;
	}
	public void setRgoActual(String rgoActual) {
		this.rgoActual = rgoActual;
	}
	@Column(name="RGO_PROP_BBVA_BC")
	public String getRgoPropBbvaBc() {
		return rgoPropBbvaBc;
	}
	public void setRgoPropBbvaBc(String rgoPropBbvaBc) {
		this.rgoPropBbvaBc = rgoPropBbvaBc;
	}
	
	@Column(name="PROP_RIESGO")
	public String getPropRiesgo() {
		return propRiesgo;
	}
	public void setPropRiesgo(String propRiesgo) {
		this.propRiesgo = propRiesgo;
	}
	
	@Column(name="OBSERVACIONES")
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Column(name="COLUMNA1")
	public String getColumna1() {
		return columna1;
	}
	public void setColumna1(String columna1) {
		this.columna1 = columna1;
	}
	
	@Column(name="COLUMNA2")
	public String getColumna2() {
		return columna2;
	}
	public void setColumna2(String columna2) {
		this.columna2 = columna2;
	}
	
	@Column(name="COLUMNA3")
	public String getColumna3() {
		return columna3;
	}
	public void setColumna3(String columna3) {
		this.columna3 = columna3;
	}
	
	@Column(name="COLUMNA4")
	public String getColumna4() {
		return columna4;
	}
	public void setColumna4(String columna4) {
		this.columna4 = columna4;
	}
	
	@Column(name="COLUMNA5")
	public String getColumna5() {
		return columna5;
	}
	public void setColumna5(String columna5) {
		this.columna5 = columna5;
	}
	
	@Column(name="COLUMNA6")
	public String getColumna6() {
		return columna6;
	}
	public void setColumna6(String columna6) {
		this.columna6 = columna6;
	}
	
	@Column(name="COLUMNA7")
	public String getColumna7() {
		return columna7;
	}
	public void setColumna7(String columna7) {
		this.columna7 = columna7;
	}
	
	@Column(name="COLUMNA8")
	public String getColumna8() {
		return columna8;
	}
	public void setColumna8(String columna8) {
		this.columna8 = columna8;
	}
	
	@Column(name="COLUMNA9")
	public String getColumna9() {
		return columna9;
	}
	public void setColumna9(String columna9) {
		this.columna9 = columna9;
	}
	
	@Column(name="COLUMNA10")
	public String getColumna10() {
		return columna10;
	}
	public void setColumna10(String columna10) {
		this.columna10 = columna10;
	}
	
	@Column(name="NUM_COLUMNA")
	public String getNumColumna() {
		return numColumna;
	}
	public void setNumColumna(String numColumna) {
		this.numColumna = numColumna;
	}

	@Column(name="CONTRATO")
	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	@Transient // no mapeada
	public String getActivoCol1() {
		return activoCol1;
	}

	public void setActivoCol1(String activoCol1) {
		this.activoCol1 = activoCol1;
	}
	
	@Transient // no mapeada
	public String getActivoCol2() {
		return activoCol2;
	}

	public void setActivoCol2(String activoCol2) {
		this.activoCol2 = activoCol2;
	}
	
	@Transient // no mapeada
	public String getActivoCol3() {
		return activoCol3;
	}

	public void setActivoCol3(String activoCol3) {
		this.activoCol3 = activoCol3;
	}
	
	@Transient // no mapeada
	public String getActivoCol4() {
		return activoCol4;
	}

	public void setActivoCol4(String activoCol4) {
		this.activoCol4 = activoCol4;
	}
	
	@Transient // no mapeada
	public String getActivoCol5() {
		return activoCol5;
	}

	public void setActivoCol5(String activoCol5) {
		this.activoCol5 = activoCol5;
	}
	
	@Transient // no mapeada
	public String getActivoCol6() {
		return activoCol6;
	}

	public void setActivoCol6(String activoCol6) {
		this.activoCol6 = activoCol6;
	}
	
	@Transient // no mapeada
	public String getActivoCol7() {
		return activoCol7;
	}

	public void setActivoCol7(String activoCol7) {
		this.activoCol7 = activoCol7;
	}
	
	@Transient // no mapeada
	public String getActivoCol8() {
		return activoCol8;
	}

	public void setActivoCol8(String activoCol8) {
		this.activoCol8 = activoCol8;
	}
	
	@Transient // no mapeada
	public String getActivoCol9() {
		return activoCol9;
	}

	public void setActivoCol9(String activoCol9) {
		this.activoCol9 = activoCol9;
	}
	
	@Transient // no mapeada
	public String getActivoCol10() {
		return activoCol10;
	}

	public void setActivoCol10(String activoCol10) {
		this.activoCol10 = activoCol10;
	}
	

	


	

	
	
	
	
}
