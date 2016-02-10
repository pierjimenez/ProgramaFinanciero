package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(schema="PROFIN", name="TIIPF_TABLA_DE_TABLA")
@NamedQueries({
@NamedQuery(name="obtieneTablaPadrePorCodigo", 
			query = " select o from Tabla o where o.codigo = ? and o.padre.id is null"),
@NamedQuery(name="obtieneTablaPadrePorCodigoSinActual", 
			query = " select o from Tabla o where o.codigo = ? and o.id <> ? and o.padre.id is null"),
@NamedQuery(name="obtieneTablaHijaPorCodigo", 
			query = " select o from Tabla o where o.codigo = ? and o.padre.id = ?"),
@NamedQuery(name="obtieneTablaHijaPorCodigoSinActual", 
			query = " select o from Tabla o where o.codigo = ? and o.padre.id = ? and o.id <> ?"),
@NamedQuery(name="listaTablasPadreSinActual", 
			query = " select o from Tabla o where o.estado = 'A' and o.padre.id is null and o.id <> ?"),
@NamedQuery(name="obtieneTablaPorId", 
			query = " select o from Tabla o where o.id = ?"),
@NamedQuery(name="obtieneTablaHijaPorCodigoHijoCodigoPadre", 
		query = " select o from Tabla o where o.codigo = ? and o.padre.id in (select id from Tabla o where o.codigo = ?)"),
		@NamedQuery(name="obtieneTablaHijaCodigoPadre", 
				query = " select o from Tabla o where  o.padre.id in (select id from Tabla o where o.codigo = ?)")
})
@SequenceGenerator(name = "SEQ_TABLA_TABLA", sequenceName = "PROFIN.SEQ_TABLA_TABLA", allocationSize = 1, initialValue = 20000 )
public class Tabla extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;//ID_TABLA	NUMBER
	private String codigo;//CODIGO	VARCHAR2(30 BYTE)
	private String descripcion;//DESCRIPCION	VARCHAR2(200 BYTE)
	private Tabla padre;//ID_TABLA_DE_TABLA	NUMBER
	private String abreviado; // ABREVIADO VARCHAR2(20 BYTE)
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TABLA_TABLA")
	@Column(name="ID_TABLA")
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
	
	@Column(name="DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TABLA_DE_TABLA")
	public Tabla getPadre() {
		return padre;
	}
	
	public void setPadre(Tabla padre) {
		this.padre = padre;
	}
	
	@Column(name="ABREVIADO")
	public String getAbreviado() {
		return abreviado;
	}

	public void setAbreviado(String abreviado) {
		this.abreviado = abreviado;
	}
	
}