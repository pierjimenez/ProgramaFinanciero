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
@Table(schema="PROFIN", name="TIIPF_ANEXO_COLUMNAS")
@NamedQueries({
	@NamedQuery(name="findColumnasByAnexo",
		    query = " from  AnexoColumna " +
		    		"where anexo.id >= ? and anexo.id <= ? " +
		    		"order by anexo.id ,posColumna"),
@NamedQuery(name="findColumnaByAnexoAndDescripcion", 
	    query = " select ac from  Anexo a, AnexoColumna ac " +
	    		"where a.id = ac.anexo.id and a.programa.id = ? and a.descripcion = ? and ac.descripcion = ?" ),
@NamedQuery(name="findColumnaByIdAndDescripcion", 
	    query = " select ac from  Anexo a, AnexoColumna ac " +
	    		"where a.id = ac.anexo.id and a.programa.id = ? and a.descripcion = ? and ac.descripcion = ? and a.id=?" ),
@NamedQuery(name="updateRatingAnexoActulizacion", 
	    query = " update AnexoColumna set valor = ? " +
	    		"where id = ? "),
@NamedQuery(name="findColumnasByAnexoidProgram", 
	    query = " select ac from AnexoColumna ac " +
	    		"where ac.anexo.id in (select a.id from Anexo a where a.programa.id = ?) order by ac.anexo.id ,posColumna" ),
@NamedQuery(name="deleteColumnasByAnexoidProgram", 
query = " delete from AnexoColumna ac " +
		"where ac.anexo.id in (select a.id from Anexo a where a.programa.id = ?)" )
})
@SequenceGenerator(name = "SEQ_ANEXO_COLUMNA", sequenceName = "PROFIN.SEQ_ANEXO_COLUMNA", allocationSize = 1, initialValue = 20000)
public class AnexoColumna extends EntidadBase implements Comparable<AnexoColumna>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id; //ID_COLUMNA	NUMBER
	private Anexo anexo ; //ID_ANEXO	NUMBER
	private Integer posColumna; //POS_COL	NUMBER
	private String descripcion; //DESCRIPCION	VARCHAR2(60 BYTE)
	private String valor; //VALOR	VARCHAR2(200 BYTE)
	private String contratos;//CONTRATO	VARCHAR2(2000)	Y			

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ANEXO_COLUMNA")
	@Column(name="ID_COLUMNA")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity = Anexo.class)
	@JoinColumn(name="ID_ANEXO")
	public Anexo getAnexo() {
		return anexo;
	}
	
	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name="POS_COL")
	public Integer getPosColumna() {
		return posColumna;
	}
	
	public void setPosColumna(Integer posColumna) {
		this.posColumna = posColumna;
	}
	
	@Column(name="DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name="VALOR", columnDefinition="varchar2(2000)")	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Column(name="CONTRATO")
	public String getContratos() {
		return contratos;
	}

	public void setContratos(String contratos) {
		this.contratos = contratos;
	}
	
	public int compareTo(AnexoColumna o) {
		AnexoColumna ct = (AnexoColumna)o;
        if(this.posColumna < ct.getPosColumna())
            return -1;
        else if(this.posColumna == ct.getPosColumna())
            return 0;
        else
            return 1;
	}
	
}
