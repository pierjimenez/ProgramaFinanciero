package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN", name="TIIPF_EMPRESA")
@NamedQueries({
@NamedQuery(name="listarEmpresaPorPrograma", 
			query = " select o from Empresa o where o.programa.id = ? order by tipoGrupo.id asc")				
})
@SequenceGenerator(name = "SEQ_EMPRESA", sequenceName = "PROFIN.SEQ_EMPRESA", allocationSize = 1, initialValue = 20000)
public class Empresa extends EntidadBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;//	ID_EMPRESA	NUMBER
	private String codigo;//	CODIGO	VARCHAR2(30 BYTE)
	private String nombre;//	NOMBRE	VARCHAR2(60 BYTE)
	private String ruc;//RUC	VARCHAR2(12 BYTE)
	/**
	 * este es el tipo de empresa del grupo que puede tener los siguietes
	 * valores:
	 * EMPRESA PRINCIPAL = 34
	 * EMPRESA SECUNDARIA = 35
	 * EMPRESA ANEXA = 36
	 */
	private Tabla tipoGrupo;//TT_ID_TIPO_GRUPO	NUMBER
	private Programa programa;//IIPF_PROGRAMA_ID	NUMBER
	
	private transient String seleccionadoSecu;
	private transient String seleccionadoAnex;
	private transient int indexHoja;
	private transient String checkedEmpSeleccionada;
	private transient String checkedEmpPrincipal;
	
	private transient String idProgramaCopia;
	private transient Long idtipoGrupoCopia;//empresa principal=34 secundaria=35
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_EMPRESA")
	@Column(name="ID_EMPRESA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="CODIGO")
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Column(name="NOMBRE")
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name="RUC")
	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	@ManyToOne(targetEntity = Tabla.class)
	@JoinColumn(name="TT_ID_TIPO_GRUPO")
	public Tabla getTipoGrupo() {
		return tipoGrupo;
	}

	public void setTipoGrupo(Tabla tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	@OneToOne(targetEntity=Programa.class)
	@JoinColumn(name="IIPF_PROGRAMA_ID")
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@Transient // no mapeada
	public String getSeleccionadoSecu() {
		return seleccionadoSecu;
	}

	public void setSeleccionadoSecu(String seleccionadoSecu) {
		this.seleccionadoSecu = seleccionadoSecu;
	}	
	
	@Transient // no mapeada
	public String getSeleccionadoAnex() {
		return seleccionadoAnex;
	}

	public void setSeleccionadoAnex(String seleccionadoAnex) {
		this.seleccionadoAnex = seleccionadoAnex;
	}

	@Transient // no mapeada
	public int getIndexHoja() {
		return indexHoja;
	}

	public void setIndexHoja(int indexHoja) {
		this.indexHoja = indexHoja;
	}



	@Transient // no mapeada
	public String getCheckedEmpPrincipal() {
		return checkedEmpPrincipal;
	}

	public void setCheckedEmpPrincipal(String checkedEmpPrincipal) {
		this.checkedEmpPrincipal = checkedEmpPrincipal;
	}

	@Transient // no mapeada
	public String getCheckedEmpSeleccionada() {
		return checkedEmpSeleccionada;
	}

	public void setCheckedEmpSeleccionada(String checkedEmpSeleccionada) {
		this.checkedEmpSeleccionada = checkedEmpSeleccionada;
	}

	@Transient // no mapeada
	public String getIdProgramaCopia() {
		return idProgramaCopia;
	}

	public void setIdProgramaCopia(String idProgramaCopia) {
		this.idProgramaCopia = idProgramaCopia;
	}

	@Transient // no mapeada
	public Long getIdtipoGrupoCopia() {
		return idtipoGrupoCopia;
	}

	public void setIdtipoGrupoCopia(Long idtipoGrupoCopia) {
		this.idtipoGrupoCopia = idtipoGrupoCopia;
	}
	
	
}
