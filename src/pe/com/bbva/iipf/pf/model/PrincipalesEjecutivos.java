package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Entity
@Table(schema="PROFIN", name = "TIIPF_PRINCIPALES_EJECUTIVOS")
@NamedQueries({
@NamedQuery(name="findPrinEjecutivosByPrograma", 
		    query = "select p from PrincipalesEjecutivos  p " +
		    		"where p.programa.id=? order by p.posicion asc"),
@NamedQuery(name="findPrinEjecutivosByProgramaCodEmpGrup", 
	    query = "select p from PrincipalesEjecutivos  p " +
	    		"where p.programa.id=? and p.codEmpresaGrupo=? order by p.posicion asc"),
})
@SequenceGenerator(name = "SEQ_PRINCIPAL_EJECUT", sequenceName = "PROFIN.SEQ_PRINCIPAL_EJECUT", allocationSize = 1, initialValue = 20000)
public class PrincipalesEjecutivos extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ; //ID_PRIN_EJEC	NUMBER
	private String nombres;//NOMBRES	VARCHAR2(60 BYTE)
	private String puesto; //PUESTO	VARCHAR2(60 BYTE)
	private String informacionAdcional ; //INFORMACION_ADIC	VARCHAR2(100 BYTE)
	private Integer posicion; //POSICION	NUMBER
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PRINCIPAL_EJECUT")
	@Column(name="ID_PRIN_EJEC")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NOMBRES" , length=60)
	public String getNombres() {
		return nombres;
	}
	
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	@Column(name="PUESTO" , length=60)
	public String getPuesto() {
		return puesto;
	}
	
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	@Column(name="INFORMACION_ADIC" , length=100)
	public String getInformacionAdcional() {
		return informacionAdcional;
	}
	
	public void setInformacionAdcional(String informacionAdcional) {
		this.informacionAdcional = informacionAdcional;
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
	
	
	@Column(name = "COD_GRUPO_EMPRESA")
	public String getCodEmpresaGrupo() {
		return codEmpresaGrupo;
	}

	public void setCodEmpresaGrupo(String codEmpresaGrupo) {
		this.codEmpresaGrupo = codEmpresaGrupo;
	}
	
}
