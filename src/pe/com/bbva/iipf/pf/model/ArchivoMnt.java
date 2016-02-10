package pe.com.bbva.iipf.pf.model;

import java.math.BigDecimal;
import java.util.Date;

import pe.com.stefanini.core.domain.EntidadBase;
import pe.com.stefanini.core.util.FormatUtil;


public class ArchivoMnt  extends EntidadBase{
 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fechaModificacion;	
	private Date fechaCreacion;
	private String nombreArchivo;
	private String rutaCarpeta;
	private String rutaArchivo;
	private Long pesoArchivo;
	private String pesoArchivoCadena;
	
	private String ruta;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
	public String getRutaCarpeta() {
		return rutaCarpeta;
	}
	public void setRutaCarpeta(String rutaCarpeta) {
		this.rutaCarpeta = rutaCarpeta;
	}
	public Long getPesoArchivo() {
		return pesoArchivo;
	}
	public void setPesoArchivo(Long pesoArchivo) {
		this.pesoArchivo = pesoArchivo;
	}	
	public String getPesoArchivoCadena() {
		if(pesoArchivo!=null){
			BigDecimal bdec = new BigDecimal(pesoArchivo);
			pesoArchivoCadena = FormatUtil.conversionNumberFormat(bdec);
		}
		return pesoArchivoCadena;
	}
	public void setPesoArchivoCadena(String pesoArchivoCadena) {
		this.pesoArchivoCadena = pesoArchivoCadena;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
	
}
