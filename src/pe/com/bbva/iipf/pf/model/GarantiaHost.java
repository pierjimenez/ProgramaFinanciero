package pe.com.bbva.iipf.pf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GarantiaHost {
	
	
	private Long id	; 
	private String codCentral;//    	cod_central,
	private String itipoGara;//I_TIPO_GARA
	private String nombTipoGarantia;//NOMB_TIPOGARANTA
	private String codGarantia;//COD_GARANTIA
	private String numeroGarantia; //   numero_garantia,
	private String tipoGarantia;//    	TIPO_GARANTIA,
	private String importe;//IMPORTE
	
	private String hisubtipo;//    				HI_subtipo,
	private String hiimporte;//    				HI_importe,
	private String hirango;//    				HI_rango,
	private String hifechaTasacion;//    		HI_fecha_de_tasacion,
	private String hitasador;//    				HI_tasador,
	private String hitasacionMonto;//   		HI_tasacion_monto,
	private String hidireccionPropietario;//    HI_direccion_de_propietario,
	private String hivalorRealizacion;//    	HI_valor_de_realizacion,
	private String hinombre;//    				HI_nombre,
	private String hiclase;//    				HI_clase,
	
	private String daimporte;//    			DA_importe,
	private String danumeroDeposito;//    	DA_numero_de_deposito,
	private String daglosa;//    			DA_glosa,
	private String daoperacion;//    		DA_operacion,
	
	private String cgimporte;//    		CG_importe,
	private String cgnumeroDeposito;//  CG_numero_de_deposito,
	private String cgglosa;//    		CG_glosa,
	private String cgoperacion;//    	CG_operacion,
	
	private String fsoperacion;//    		FS_operacion,
	private String fsmoneda;//    			FS_moneda,
	private String fsimporte;//    			FS_importe,
	private String fsfiador;//    			FS_fiador,
	private String fsdocumentoIdentidad;//  FS_documento_identidad,
	private String fsglosa;//    			FS_glosa,
	
	private String fmimporte; //FM_IMPORTE
	private String fmctaPar; // FM_CTA_PAR,
	private String fmcuotas; // FM_CUOTAS,
	private String fmmoneda; // FM_MONEDA,
	private String fmvalorCotizacion; // FM_VALOR_COTIZADO,
	private String fmfechaCotizacion; // FM_F_COTIZACION,
	private String fmfechaLiberacion; // FM_F_LIBERACION,
    
	

	
	private String waimporte; //WA_IMPORTE,
	private String watipoBien;//    WA_tipo_bien,
	private String wadescripcion;// WA_descripcion,
	private String wacantidad;//    WA_cantidad,
	private String wasubtipo;//     WA_subtipo,
	private String wafechaVencimiento;//    WA_fecha_vencimiento,
	
	private String sbimporte ; //SB_IMPORTE,
	private String sbcartCred; //SB_CART_CRED,
	private String sbemisor; //SB_EMISOR,
	private String sbciudad; //SB_CIUDAD,
	private String sbcarta; //SB_CARTA,
	private String sbvcto; //SB_VCTO,
	private String sbrenovacion; //SB_RENOVACION,
	private String sbobservacion; //SB_OBSERVACIONES
    

	private String empresa;
    
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodCentral() {
		return codCentral;
	}
	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}
	
	
	
	public String getItipoGara() {
		return itipoGara;
	}
	public void setItipoGara(String itipoGara) {
		this.itipoGara = itipoGara;
	}
	

	
	public String getNombTipoGarantia() {
		return nombTipoGarantia;
	}
	public void setNombTipoGarantia(String nombTipoGarantia) {
		this.nombTipoGarantia = nombTipoGarantia;
	}
	public String getCodGarantia() {
		return codGarantia;
	}
	public void setCodGarantia(String codGarantia) {
		this.codGarantia = codGarantia;
	}
	public String getNumeroGarantia() {
		return numeroGarantia;
	}
	public void setNumeroGarantia(String numeroGarantia) {
		this.numeroGarantia = numeroGarantia;
	}
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
		
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getHisubtipo() {
		return hisubtipo;
	}
	public void setHisubtipo(String hisubtipo) {
		this.hisubtipo = hisubtipo;
	}
	public String getHiimporte() {
		return hiimporte;
	}
	public void setHiimporte(String hiimporte) {
		this.hiimporte = hiimporte;
	}
	public String getHirango() {
		return hirango;
	}
	public void setHirango(String hirango) {
		this.hirango = hirango;
	}
	public String getHifechaTasacion() {
		return hifechaTasacion;
	}
	public void setHifechaTasacion(String hifechaTasacion) {
		this.hifechaTasacion = hifechaTasacion;
	}
	public String getHitasador() {
		return hitasador;
	}
	public void setHitasador(String hitasador) {
		this.hitasador = hitasador;
	}
	public String getHitasacionMonto() {
		return hitasacionMonto;
	}
	public void setHitasacionMonto(String hitasacionMonto) {
		this.hitasacionMonto = hitasacionMonto;
	}
	public String getHidireccionPropietario() {
		return hidireccionPropietario;
	}
	public void setHidireccionPropietario(String hidireccionPropietario) {
		this.hidireccionPropietario = hidireccionPropietario;
	}
	public String getHivalorRealizacion() {
		return hivalorRealizacion;
	}
	public void setHivalorRealizacion(String hivalorRealizacion) {
		this.hivalorRealizacion = hivalorRealizacion;
	}
	public String getHinombre() {
		return hinombre;
	}
	public void setHinombre(String hinombre) {
		this.hinombre = hinombre;
	}
	public String getHiclase() {
		return hiclase;
	}
	public void setHiclase(String hiclase) {
		this.hiclase = hiclase;
	}
	public String getDaimporte() {
		return daimporte;
	}
	public void setDaimporte(String daimporte) {
		this.daimporte = daimporte;
	}
	public String getDanumeroDeposito() {
		return danumeroDeposito;
	}
	public void setDanumeroDeposito(String danumeroDeposito) {
		this.danumeroDeposito = danumeroDeposito;
	}
	public String getDaglosa() {
		return daglosa;
	}
	public void setDaglosa(String daglosa) {
		this.daglosa = daglosa;
	}
	public String getDaoperacion() {
		return daoperacion;
	}
	public void setDaoperacion(String daoperacion) {
		this.daoperacion = daoperacion;
	}
	public String getCgimporte() {
		return cgimporte;
	}
	public void setCgimporte(String cgimporte) {
		this.cgimporte = cgimporte;
	}
	public String getCgnumeroDeposito() {
		return cgnumeroDeposito;
	}
	public void setCgnumeroDeposito(String cgnumeroDeposito) {
		this.cgnumeroDeposito = cgnumeroDeposito;
	}
	public String getCgglosa() {
		return cgglosa;
	}
	public void setCgglosa(String cgglosa) {
		this.cgglosa = cgglosa;
	}
	public String getCgoperacion() {
		return cgoperacion;
	}
	public void setCgoperacion(String cgoperacion) {
		this.cgoperacion = cgoperacion;
	}
	public String getFsoperacion() {
		return fsoperacion;
	}
	public void setFsoperacion(String fsoperacion) {
		this.fsoperacion = fsoperacion;
	}
	public String getFsmoneda() {
		return fsmoneda;
	}
	public void setFsmoneda(String fsmoneda) {
		this.fsmoneda = fsmoneda;
	}
	public String getFsimporte() {
		return fsimporte;
	}
	public void setFsimporte(String fsimporte) {
		this.fsimporte = fsimporte;
	}
	public String getFsfiador() {
		return fsfiador;
	}
	public void setFsfiador(String fsfiador) {
		this.fsfiador = fsfiador;
	}
	public String getFsdocumentoIdentidad() {
		return fsdocumentoIdentidad;
	}
	public void setFsdocumentoIdentidad(String fsdocumentoIdentidad) {
		this.fsdocumentoIdentidad = fsdocumentoIdentidad;
	}
	public String getFsglosa() {
		return fsglosa;
	}
	public void setFsglosa(String fsglosa) {
		this.fsglosa = fsglosa;
	}
	
	
	
	
	public String getFmimporte() {
		return fmimporte;
	}
	public void setFmimporte(String fmimporte) {
		this.fmimporte = fmimporte;
	}
	public String getFmctaPar() {
		return fmctaPar;
	}
	public void setFmctaPar(String fmctaPar) {
		this.fmctaPar = fmctaPar;
	}
	public String getFmcuotas() {
		return fmcuotas;
	}
	public void setFmcuotas(String fmcuotas) {
		this.fmcuotas = fmcuotas;
	}
	public String getFmmoneda() {
		return fmmoneda;
	}
	public void setFmmoneda(String fmmoneda) {
		this.fmmoneda = fmmoneda;
	}
	public String getFmvalorCotizacion() {
		return fmvalorCotizacion;
	}
	public void setFmvalorCotizacion(String fmvalorCotizacion) {
		this.fmvalorCotizacion = fmvalorCotizacion;
	}
	public String getFmfechaCotizacion() {
		return fmfechaCotizacion;
	}
	public void setFmfechaCotizacion(String fmfechaCotizacion) {
		this.fmfechaCotizacion = fmfechaCotizacion;
	}
	public String getFmfechaLiberacion() {
		return fmfechaLiberacion;
	}
	public void setFmfechaLiberacion(String fmfechaLiberacion) {
		this.fmfechaLiberacion = fmfechaLiberacion;
	}

	
	
	
	public String getWaimporte() {
		return waimporte;
	}
	public void setWaimporte(String waimporte) {
		this.waimporte = waimporte;
	}
	public String getWatipoBien() {
		return watipoBien;
	}
	public void setWatipoBien(String watipoBien) {
		this.watipoBien = watipoBien;
	}
	public String getWadescripcion() {
		return wadescripcion;
	}
	public void setWadescripcion(String wadescripcion) {
		this.wadescripcion = wadescripcion;
	}
	public String getWacantidad() {
		return wacantidad;
	}
	public void setWacantidad(String wacantidad) {
		this.wacantidad = wacantidad;
	}
	public String getWasubtipo() {
		return wasubtipo;
	}
	public void setWasubtipo(String wasubtipo) {
		this.wasubtipo = wasubtipo;
	}
	public String getWafechaVencimiento() {
		return wafechaVencimiento;
	}
	public void setWafechaVencimiento(String wafechaVencimiento) {
		this.wafechaVencimiento = wafechaVencimiento;
	}
	
		
	
	
	public String getSbimporte() {
		return sbimporte;
	}
	public void setSbimporte(String sbimporte) {
		this.sbimporte = sbimporte;
	}
	public String getSbcartCred() {
		return sbcartCred;
	}
	public void setSbcartCred(String sbcartCred) {
		this.sbcartCred = sbcartCred;
	}
	public String getSbemisor() {
		return sbemisor;
	}
	public void setSbemisor(String sbemisor) {
		this.sbemisor = sbemisor;
	}
	public String getSbciudad() {
		return sbciudad;
	}
	public void setSbciudad(String sbciudad) {
		this.sbciudad = sbciudad;
	}
	public String getSbcarta() {
		return sbcarta;
	}
	public void setSbcarta(String sbcarta) {
		this.sbcarta = sbcarta;
	}
	public String getSbvcto() {
		return sbvcto;
	}
	public void setSbvcto(String sbvcto) {
		this.sbvcto = sbvcto;
	}
	public String getSbrenovacion() {
		return sbrenovacion;
	}
	public void setSbrenovacion(String sbrenovacion) {
		this.sbrenovacion = sbrenovacion;
	}
	public String getSbobservacion() {
		return sbobservacion;
	}
	public void setSbobservacion(String sbobservacion) {
		this.sbobservacion = sbobservacion;
	}
	

	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	

}
