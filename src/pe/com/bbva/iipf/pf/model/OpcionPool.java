package pe.com.bbva.iipf.pf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pe.com.stefanini.core.domain.EntidadBase;

@Entity
@Table(schema="PROFIN",  name="tiipf_opcion_pool")
@SequenceGenerator(name = "SEQ_OPCION_POOL", sequenceName = "PROFIN.SEQ_OPCION_POOL", allocationSize = 1, initialValue = 20000)
public class OpcionPool extends EntidadBase {


/**
	 * 
	 */
private static final long serialVersionUID = -397354321037515425L;
	
private Long id;//	TIIPF_OPCION_POOL_ID	NUMBER	Y			
private String tipoOpcionPool;//	TT_ID_TIPO_OPCION_POOL	NUMBER	Y			TIPO OPCION POOL 1: EMPRESA 2: TIPO DEUDA 3: BANCO
private Programa programa;//	IIPF_PROGRAMA_ID	INTEGER	N			
private String codOpcionPool;//	COD_OPCION_POOL	VARCHAR2(20)	Y			CODIGO OPCION POOL
private String DescOpcionPool; //	DESC_OPCION_POOL	VARCHAR2(100)	Y			DESCRIPCION OPCION POOL


@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_OPCION_POOL")
@Column(name="TIIPF_OPCION_POOL_ID")
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

@Column(name="TT_ID_TIPO_OPCION_POOL")
public String getTipoOpcionPool() {
	return tipoOpcionPool;
}
public void setTipoOpcionPool(String tipoOpcionPool) {
	this.tipoOpcionPool = tipoOpcionPool;
}

@ManyToOne
@JoinColumn(name="IIPF_PROGRAMA_ID")
public Programa getPrograma() {
	return programa;
}
public void setPrograma(Programa programa) {
	this.programa = programa;
}


@Column(name="COD_OPCION_POOL")
public String getCodOpcionPool() {
	return codOpcionPool;
}
public void setCodOpcionPool(String codOpcionPool) {
	this.codOpcionPool = codOpcionPool;
}


@Column(name="DESC_OPCION_POOL")
public String getDescOpcionPool() {
	return DescOpcionPool;
}
public void setDescOpcionPool(String descOpcionPool) {
	DescOpcionPool = descOpcionPool;
}

	
	
}
