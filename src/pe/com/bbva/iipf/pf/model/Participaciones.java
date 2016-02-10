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
@Table(schema="PROFIN", name = "TIIPF_PARTICIPACIONES_SIGNI")
@NamedQueries({
@NamedQuery(name="findParticipacionesByPrograma", 
		    query = "select p from Participaciones  p " +
		    		"where p.programa.id=? order by p.posicion asc"),
@NamedQuery(name="findParticipacionesByProgramaCodEmpGrup", 
		    			    query = "select p from Participaciones  p " +
		    			    		"where p.programa.id=? and p.codEmpresaGrupo=? order by p.posicion asc"),
})
@SequenceGenerator(name = "SEQ_PARTICIPACIONES", sequenceName = "PROFIN.SEQ_PARTICIPACIONES", allocationSize = 1, initialValue = 20000)
public class Participaciones extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ; //ID_PART_SIGN	NUMBER
	private String nombre ; //NOMBRE_AFILIADO	VARCHAR2(60 BYTE)
	private String porcentaje ; //PORCENTAJE	VARCHAR2(4 BYTE)
	private String consolidacion; //CONSOLIDACION	VARCHAR2(60 BYTE)
	private String sectorActividad; //SECTOR_ACTI	VARCHAR2(60 BYTE)
	private Integer posicion; //POSICION	NUMBER
	private Programa programa ; //IIPF_PROGRAMA_ID	NUMBER
	private String  codEmpresaGrupo; //COD_GRUPO_EMPRESA	VARCHAR2(30)
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PARTICIPACIONES")
	@Column(name="ID_PART_SIGN")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NOMBRE_AFILIADO" , length=60)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="PORCENTAJE" , length=7)
	public String getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	@Column(name="CONSOLIDACION" , length=60)
	public String getConsolidacion() {
		return consolidacion;
	}
	
	public void setConsolidacion(String consolidacion) {
		this.consolidacion = consolidacion;
	}
	
	@Column(name="SECTOR_ACTI" , length=60)
	public String getSectorActividad() {
		return sectorActividad;
	}
	
	public void setSectorActividad(String sectorActividad) {
		this.sectorActividad = sectorActividad;
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
