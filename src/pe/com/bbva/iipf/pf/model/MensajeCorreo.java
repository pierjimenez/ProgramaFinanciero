package pe.com.bbva.iipf.pf.model;

public class MensajeCorreo {
//CODIGO_LOG,NOMBRE_ARCHIVO,DESCRIPCION,ESTADO
	private Integer codigoLog;
	private String nombreArchivo;
	private String descripcion;
	private String estado;
	private String fechaCreacion;
	private String usuario;
	public Integer getCodigoLog() {
		return codigoLog;
	}
	public void setCodigoLog(Integer codigoLog) {
		this.codigoLog = codigoLog;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getUsuario() {
		return usuario;
	}
	
	
}
