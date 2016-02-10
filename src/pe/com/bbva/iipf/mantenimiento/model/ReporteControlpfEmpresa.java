package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Entity;

@Entity
public class ReporteControlpfEmpresa {

	private String codcentralEmpprincipal;
	private String rucEmpprincipal;
	private String empresaprincipal;  
	private String numeroSolicitud; 
	private String tipo;
	private String codigoGrupo; 
	private String nombreGrupo;  
	private String fechaCreacion;  
	private String fechaModificacion;  
	private String estado;
	private String usuarioCreacion;
	private String usuarioModificacion;
	
    private String usuarioCierre;
    private String fechaCierre;
    private String motivoCierre;
    private String gestor;
    private String oficina;
    private String proximaRevision;
    private String rgopropbbvabc;
    private String propRiesgo;
    
    private String rvglEPrincipal;
    private String rvglESecundaria;
    

	
	public String getCodcentralEmpprincipal() {
		return codcentralEmpprincipal;
	}
	public void setCodcentralEmpprincipal(String codcentralEmpprincipal) {
		this.codcentralEmpprincipal = codcentralEmpprincipal;
	}
	public String getRucEmpprincipal() {
		return rucEmpprincipal;
	}
	public void setRucEmpprincipal(String rucEmpprincipal) {
		this.rucEmpprincipal = rucEmpprincipal;
	}
	public String getEmpresaprincipal() {
		return empresaprincipal;
	}
	public void setEmpresaprincipal(String empresaprincipal) {
		this.empresaprincipal = empresaprincipal;
	}
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCodigoGrupo() {
		return codigoGrupo;
	}
	public void setCodigoGrupo(String codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}
	public String getNombreGrupo() {
		return nombreGrupo;
	}
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public String getUsuarioCierre() {
		return usuarioCierre;
	}
	public void setUsuarioCierre(String usuarioCierre) {
		this.usuarioCierre = usuarioCierre;
	}
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public String getMotivoCierre() {
		return motivoCierre;
	}
	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getProximaRevision() {
		return proximaRevision;
	}
	public void setProximaRevision(String proximaRevision) {
		this.proximaRevision = proximaRevision;
	}
	public String getRgopropbbvabc() {
		return rgopropbbvabc;
	}
	public void setRgopropbbvabc(String rgopropbbvabc) {
		this.rgopropbbvabc = rgopropbbvabc;
	}
	
	public String getPropRiesgo() {
		return propRiesgo;
	}
	public void setPropRiesgo(String propRiesgo) {
		this.propRiesgo = propRiesgo;
	}
	public String getRvglEPrincipal() {
		return rvglEPrincipal;
	}
	public void setRvglEPrincipal(String rvglEPrincipal) {
		this.rvglEPrincipal = rvglEPrincipal;
	}
	public String getRvglESecundaria() {
		return rvglESecundaria;
	}
	public void setRvglESecundaria(String rvglESecundaria) {
		this.rvglESecundaria = rvglESecundaria;
	}

	
	
	
}
