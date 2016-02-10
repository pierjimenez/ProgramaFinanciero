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

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",name="TIIPF_PARAMETROS")
@NamedQueries({
@NamedQuery(name="obtieneParametroPorCodigo", 
			query = " select o from Parametro o where o.codigo = ?"),
@NamedQuery(name="obtieneParametroPorCodigoSinActual", 
					query = " select o from Parametro o where o.codigo = ? and o.id <> ?"),
@NamedQuery(name="obtieneParametroPorId", 
					query = " select o from Parametro o where o.id = ?")					
})
@SequenceGenerator(name = "SEQ_PARAMETRO", sequenceName = "PROFIN.SEQ_PARAMETRO", allocationSize = 1, initialValue = 20000)
public class Parametro extends EntidadBase
{

	private Long id;
	private String codigo;
	private String valor;
	private String descripcion;
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_PARAMETRO")
	@Column(name="ID_PARAMETROS")
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
	
	@Column(name="VALOR")
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Column(name="DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
			
}
