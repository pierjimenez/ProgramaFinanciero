package pe.com.bbva.iipf.pf.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


public class rccan {
	
	private Long id; //ID_PFRCCAN	INTEGER	Y			ID
	private String tipoDocumento;//	TIPO_DOCUMENTO	VARCHAR2(2)	Y			R:Ruc
	private String documento;//	DOCUMENTO	VARCHAR2(12)	Y			Numero de RUC
	private String cliente;//	CLIENTE	VARCHAR2(8)	Y			Numero de Cliente Interno
	private String nroEntidadBancaria;//	NRO_ENTIDAD_BANCARIA	VARCHAR2(3)	Y			Codigo de Banco
	private String nombreBanco;//	NOMBRE_BANCO	VARCHAR2(30)	Y			EJM: BBVA Banco Continental
	private BigDecimal porcentajeNormal;//	PORC_NOR	NUMBER	Y			Porcentaje Informado en Categoría Normal
	private BigDecimal porcenajePPotencial;//	PORC_CPP	NUMBER	Y			Porcentaje Informado en Categoría Con Problemas Potenciales
	private BigDecimal porcentajeDeficiente;//	PORC_DEF	NUMBER	Y			Porcentaje Informado en Categoría Deficiente
	private BigDecimal porcentajeDudoso;//	PORC_DUD	NUMBER	Y			Porcentaje Informado en Categoría Dudoso
	private BigDecimal porcentajePerdida;//	PORC_PER	NUMBER	Y			Porcentaje Informado en Categoría Perdida
	private BigDecimal deudaIndirecta;//	DEUDA_INDIRECTA	NUMBER	Y			Monto Total en Deuda Indirecta por Banco Informado
	private BigDecimal deudaDirecta;//	DEUDA_DIRECTA	NUMBER	Y			Monto Total en Deuda Directa por Banco Informado
	private BigDecimal deudaTotal;//	DEUDA_TOTAL	NUMBER	Y			Monto Total en Deuda por Banco Informado
	private Date fechaProceso;//	FECHA_PROCESO	DATE	Y			Fecha Proceso
	private String anio; //ANIO	VARCHAR2(4)	Y			

	@Id
	@GeneratedValue
	@Column(name="ID_PFRCCAN")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="TIPO_DOCUMENTO", length=2)
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Column(name="DOCUMENTO", length=12)
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}


	@Column(name="CLIENTE", length=8)
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}


	@Column(name="NRO_ENTIDAD_BANCARIA", length=3)
	public String getNroEntidadBancaria() {
		return nroEntidadBancaria;
	}
	public void setNroEntidadBancaria(String nroEntidadBancaria) {
		this.nroEntidadBancaria = nroEntidadBancaria;
	}


	@Column(name="NOMBRE_BANCO", length=30)
	public String getNombreBanco() {
		return nombreBanco;
	}
	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}


	@Column(name="PORC_NOR", precision = 5, scale = 2)
	public BigDecimal getPorcentajeNormal() {
		return porcentajeNormal;
	}
	public void setPorcentajeNormal(BigDecimal porcentajeNormal) {
		this.porcentajeNormal = porcentajeNormal;
	}


	@Column(name="PORC_CPP", precision = 5, scale = 2)
	public BigDecimal getPorcenajePPotencial() {
		return porcenajePPotencial;
	}
	public void setPorcenajePPotencial(BigDecimal porcenajePPotencial) {
		this.porcenajePPotencial = porcenajePPotencial;
	}


	@Column(name="PORC_DEF", precision = 5, scale = 2)
	public BigDecimal getPorcentajeDeficiente() {
		return porcentajeDeficiente;
	}
	public void setPorcentajeDeficiente(BigDecimal porcentajeDeficiente) {
		this.porcentajeDeficiente = porcentajeDeficiente;
	}


	@Column(name="PORC_DUD", precision = 5, scale = 2)
	public BigDecimal getPorcentajeDudoso() {
		return porcentajeDudoso;
	}
	public void setPorcentajeDudoso(BigDecimal porcentajeDudoso) {
		this.porcentajeDudoso = porcentajeDudoso;
	}


	@Column(name="PORC_PER", precision = 5, scale = 2)
	public BigDecimal getPorcentajePerdida() {
		return porcentajePerdida;
	}
	public void setPorcentajePerdida(BigDecimal porcentajePerdida) {
		this.porcentajePerdida = porcentajePerdida;
	}


	@Column(name="DEUDA_INDIRECTA", precision = 15, scale = 2)
	public BigDecimal getDeudaIndirecta() {
		return deudaIndirecta;
	}
	public void setDeudaIndirecta(BigDecimal deudaIndirecta) {
		this.deudaIndirecta = deudaIndirecta;
	}

	@Column(name="DEUDA_DIRECTA", precision = 15, scale = 2)
	public BigDecimal getDeudaDirecta() {
		return deudaDirecta;
	}
	public void setDeudaDirecta(BigDecimal deudaDirecta) {
		this.deudaDirecta = deudaDirecta;
	}

	@Column(name="DEUDA_TOTAL", precision = 15, scale = 2)
	public BigDecimal getDeudaTotal() {
		return deudaTotal;
	}
	public void setDeudaTotal(BigDecimal deudaTotal) {
		this.deudaTotal = deudaTotal;
	}


	@Column(name="FECHA_PROCESO")
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
	@Column(name="ANIO")
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}


}
