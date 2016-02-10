package pe.com.bbva.iipf.pf.model;

import java.util.Map;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;

public class FilaEmpresaHoja {
	
	private int pos;
	private Empresa empresa;
	private String nombreArchivoEEFF;
	private String CodEmpresaEEFF;
	private Map<String,Object> datoAdicional;
	private Long tipoEmpresa; 
	private String fileName;
	

	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public String getNombreArchivoEEFF() {
		return nombreArchivoEEFF;
	}
	public void setNombreArchivoEEFF(String nombreArchivoEEFF) {
		this.nombreArchivoEEFF = nombreArchivoEEFF;
	}
	public String getCodEmpresaEEFF() {
		return CodEmpresaEEFF;
	}
	public void setCodEmpresaEEFF(String codEmpresaEEFF) {
		CodEmpresaEEFF = codEmpresaEEFF;
	}
	public Map<String, Object> getDatoAdicional() {
		return datoAdicional;
	}
	public void setDatoAdicional(Map<String, Object> datoAdicional) {
		this.datoAdicional = datoAdicional;
	}
	public Long getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(Long tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
