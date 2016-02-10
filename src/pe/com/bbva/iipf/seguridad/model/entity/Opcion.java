package pe.com.bbva.iipf.seguridad.model.entity;

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

import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.stefanini.core.domain.EntidadBase;


@Entity
@Table(schema="PROFIN", name="TIIPF_OPCION")
@NamedQueries({
@NamedQuery(name="listaOpcionesSinActual", 
		    query = " select o from Opcion o where o.estado = 'A' and o.id <> ?"),
@NamedQuery(name="obtieneOpcionPorCodigo", 
			query = " select o from Opcion o where o.codigo = ?"),
@NamedQuery(name="obtieneOpcionPorCodigoSinActual", 
			query = " select o from Opcion o where o.codigo = ? and o.id <> ?"),
@NamedQuery(name="obtieneOpcionPorId", 
			query = " select o from Opcion o where o.id = ?"),
})
@SequenceGenerator(name = "SEQ_OPCION", sequenceName = "PROFIN.SEQ_OPCION", allocationSize = 1, initialValue = 20000)
public class Opcion  extends EntidadBase 
{

	private Long id;	
	private String codigo;
	private String nombre;
	private String action;
	private String reglaNavegacion;
	private String metodo;	
	private Tabla tipo;
	private String detalle;
	private Opcion superior;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_OPCION")
	@Column(name="ID_OPCION")
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
	
	@Column(name="ACTION")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name="REGLA_NAVEG")
	public String getReglaNavegacion() {
		return reglaNavegacion;
	}
	public void setReglaNavegacion(String reglaNavegacion) {
		this.reglaNavegacion = reglaNavegacion;
	}
	
	@Column(name="METODO")
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TIPO")
	public Tabla getTipo() {
		return tipo;
	}
	public void setTipo(Tabla tipo) {
		this.tipo = tipo;
	}
	
	@Column(name="DETALLE")
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OPC_SUPERIOR",nullable=true)
	public Opcion getSuperior() {
		return superior;
	}
	public void setSuperior(Opcion superior) {
		this.superior = superior;
	}
	
}
