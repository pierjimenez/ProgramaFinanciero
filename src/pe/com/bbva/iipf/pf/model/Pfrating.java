package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema="PROFIN",  name="TIIPF_PFRATING")
@SequenceGenerator(name = "SEQ_PFRATING", sequenceName = "PROFIN.SEQ_PFRATING", allocationSize = 1, initialValue = 200)
@NamedQueries({
@NamedQuery(name="deleteRatingEmpresaWS", 
		    query = " delete from Pfrating " +		    		
		    		" where codCentralCli=? and tipEstadoFinan=? "),
@NamedQuery(name="deleteRatingGrupoWS", 
	    query = " delete from Pfrating " +		    		
	    		" where idGrupo=? "),
@NamedQuery(name="listarPFratintcm", 
		    query = " select e from Pfrating e where e.codCentralCli = ? and tipEstadoFinan=? ")	

})
public class Pfrating  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; //id_pfrating
	private String   fechPeriodoCalc;  		 //FECH_PERIODO_CALC  VARCHAR2(10), 
	private String   nombEmpresa ;		     //NOMB_EMPRESA       VARCHAR2(60), 
	private String   codCentralCli ;   		 //COD_CENTRAL_CLI    VARCHAR2(8),  
	private String   mesesEjercicios ;  	 //MESES_EJERCICIOS   VARCHAR2(2),  
	private String   inflacion         ; 	 //INFLACION          VARCHAR2(11), 
	private String   califRating      ;		 //CALIF_RATING       VARCHAR2(6),  
	private String   codCuenta         ;	 //COD_CUENTA         VARCHAR2(20), 
	private String   descCuenta       ; 	 //DESC_CUENTA        VARCHAR2(60), 
	private String   monto              ;	 //MONTO              VARCHAR2(17), 
	private String   status            ;	 //STATUS             VARCHAR2(20), 
	private String   tipEstadoFinan  ;		 //TIP_ESTADO_FINAN   VARCHAR2(1),  
	private String   factCualitativos  ;	 //FACT_CUALITATIVOS  VARCHAR2(5),  
	private String   factCuantitativos ;	 //FACT_CUANTITATIVOS VARCHAR2(5),  
	private String   factBureau        ;	 //FACT_BUREAU        VARCHAR2(5),  
	private String   puntRating        ;	 //PUNT_RATING        VARCHAR2(6) 
	private String   idGrupo; //ID_GRUPO

	  
	@Id	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PFRATING")
	@Column(name="ID_PFRATING")	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="COD_CENTRAL_CLI")
	public String getCodCentralCli() {
		return codCentralCli;
	}

	public void setCodCentralCli(String codCentralCli) {
		this.codCentralCli = codCentralCli;
	}
	
	@Column(name="FECH_PERIODO_CALC")
	public String getFechPeriodoCalc() {
		return fechPeriodoCalc;
	}
	public void setFechPeriodoCalc(String fechPeriodoCalc) {
		this.fechPeriodoCalc = fechPeriodoCalc;
	}
	@Column(name="NOMB_EMPRESA")
	public String getNombEmpresa() {
		return nombEmpresa;
	}
	public void setNombEmpresa(String nombEmpresa) {
		this.nombEmpresa = nombEmpresa;
	}

	
	@Column(name="MESES_EJERCICIOS")
	public String getMesesEjercicios() {
		return mesesEjercicios;
	}
	public void setMesesEjercicios(String mesesEjercicios) {
		this.mesesEjercicios = mesesEjercicios;
	}
	
	@Column(name="INFLACION")
	public String getInflacion() {
		return inflacion;
	}
	public void setInflacion(String inflacion) {
		this.inflacion = inflacion;
	}
	
	@Column(name="CALIF_RATING")
	public String getCalifRating() {
		return califRating;
	}
	public void setCalifRating(String califRating) {
		this.califRating = califRating;
	}
	
	
	@Column(name="COD_CUENTA")
	public String getCodCuenta() {
		return codCuenta;
	}
	public void setCodCuenta(String codCuenta) {
		this.codCuenta = codCuenta;
	}
	@Column(name="DESC_CUENTA")
	public String getDescCuenta() {
		return descCuenta;
	}
	public void setDescCuenta(String descCuenta) {
		this.descCuenta = descCuenta;
	}
	
	@Column(name="MONTO")
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="TIP_ESTADO_FINAN")
	public String getTipEstadoFinan() {
		return tipEstadoFinan;
	}
	public void setTipEstadoFinan(String tipEstadoFinan) {
		this.tipEstadoFinan = tipEstadoFinan;
	}
	
	@Column(name="FACT_CUALITATIVOS")
	public String getFactCualitativos() {
		return factCualitativos;
	}
	public void setFactCualitativos(String factCualitativos) {
		this.factCualitativos = factCualitativos;
	}
	
	@Column(name="FACT_CUANTITATIVOS")
	public String getFactCuantitativos() {
		return factCuantitativos;
	}
	public void setFactCuantitativos(String factCuantitativos) {
		this.factCuantitativos = factCuantitativos;
	}
	
	@Column(name="FACT_BUREAU")
	public String getFactBureau() {
		return factBureau;
	}
	public void setFactBureau(String factBureau) {
		this.factBureau = factBureau;
	}
	
	@Column(name="PUNT_RATING")
	public String getPuntRating() {
		return puntRating;
	}
	public void setPuntRating(String puntRating) {
		this.puntRating = puntRating;
	}

	@Column(name="ID_GRUPO")
	public String getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	
	  
	  

}
