package pe.com.bbva.iipf.pf.model;

import java.io.Serializable;

public class ConfiguracionWSPe21 implements Serializable {
	
	private String codigoUsuario;
	//public static final String OPCION_APLICACION ="00";//PARAMETRO DE BD PF
	private String opcionAplicacion;
	//public static final String TERMINAL_LOGICO ="D90L";//PARAMETRO DE BD PF 
	private String terminalLogico;
	//public static final String TERMINAL_CONTABLE ="D90L";//PARAMETRO DE BD PF
	private String terminalContable;
	
	
	//public static final String PE21_IP_PORT ="http://118.180.36.26:7808";//PARAMETRO DE BD PF
	private String pe21IpPort;
	//public static final String PE21_ENCODING ="IBM-500";//PARAMETRO DE BD PF
	private String pe21Encoding;
	
	
	//public static final String PE21_TRANSACCION ="PE21";//PARAMETRO DE BD PF
	private String pe21Transaccion;
	//public static final String PE21_TIPO_MENSAJE ="7";//PARAMETRO DE BD PF
	private String pe21TipoMensaje;
	//public static final String PE21_TIPO_PROCESO ="O";//PARAMETRO DE BD PF
	private String pe21TipoProceso;
	//public static final String PE21_CANAL_COMUNICACION ="PF";//PARAMETRO DE BD PF
	private String pe21CanalComunicacion;
	//public static final String PE21_INDICADOR_PREFORMATO ="N";//PARAMETRO DE BD PF
	private String pe21IndicadorPreformato;
	//public static final String PE21_TIPO_MENSAJE_ME ="B";//PARAMETRO DE BD PF
	private String pe21TipoMensajeMe;
	//public static final String PE21_DIST_QNAME_IN ="QLT.GEC.RESPA";//PARAMETRO DE BD PF
	private String pe21DistQnameIn;
	//public static final String PE21_DIST_RQNAME_OUT ="QRT.GEC.ENVIO.MPD1";//PARAMETRO DE BD PF
	private String pe21DistRqnameOut;
	//public static final String PE21_HOST_RQNAME_OUT ="QRT.GEC.RESP.QMPCINT8";//PARAMETRO DE BD PF
	private String pe21HostRqnameOut;
	//public static final String PE21_HOST_QMGR_NAME ="MPD1";//PARAMETRO DE BD PF
	private String pe21HostQmgrName;
	
	
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getOpcionAplicacion() {
		return opcionAplicacion;
	}
	public void setOpcionAplicacion(String opcionAplicacion) {
		this.opcionAplicacion = opcionAplicacion;
	}
	public String getTerminalLogico() {
		return terminalLogico;
	}
	public void setTerminalLogico(String terminalLogico) {
		this.terminalLogico = terminalLogico;
	}
	public String getTerminalContable() {
		return terminalContable;
	}
	public void setTerminalContable(String terminalContable) {
		this.terminalContable = terminalContable;
	}
	public String getPe21IpPort() {
		return pe21IpPort;
	}
	public void setPe21IpPort(String pe21IpPort) {
		this.pe21IpPort = pe21IpPort;
	}
	public String getPe21Encoding() {
		return pe21Encoding;
	}
	public void setPe21Encoding(String pe21Encoding) {
		this.pe21Encoding = pe21Encoding;
	}
	public String getPe21Transaccion() {
		return pe21Transaccion;
	}
	public void setPe21Transaccion(String pe21Transaccion) {
		this.pe21Transaccion = pe21Transaccion;
	}
	public String getPe21TipoMensaje() {
		return pe21TipoMensaje;
	}
	public void setPe21TipoMensaje(String pe21TipoMensaje) {
		this.pe21TipoMensaje = pe21TipoMensaje;
	}
	public String getPe21TipoProceso() {
		return pe21TipoProceso;
	}
	public void setPe21TipoProceso(String pe21TipoProceso) {
		this.pe21TipoProceso = pe21TipoProceso;
	}
	public String getPe21CanalComunicacion() {
		return pe21CanalComunicacion;
	}
	public void setPe21CanalComunicacion(String pe21CanalComunicacion) {
		this.pe21CanalComunicacion = pe21CanalComunicacion;
	}
	public String getPe21IndicadorPreformato() {
		return pe21IndicadorPreformato;
	}
	public void setPe21IndicadorPreformato(String pe21IndicadorPreformato) {
		this.pe21IndicadorPreformato = pe21IndicadorPreformato;
	}
	public String getPe21TipoMensajeMe() {
		return pe21TipoMensajeMe;
	}
	public void setPe21TipoMensajeMe(String pe21TipoMensajeMe) {
		this.pe21TipoMensajeMe = pe21TipoMensajeMe;
	}
	public String getPe21DistQnameIn() {
		return pe21DistQnameIn;
	}
	public void setPe21DistQnameIn(String pe21DistQnameIn) {
		this.pe21DistQnameIn = pe21DistQnameIn;
	}
	public String getPe21DistRqnameOut() {
		return pe21DistRqnameOut;
	}
	public void setPe21DistRqnameOut(String pe21DistRqnameOut) {
		this.pe21DistRqnameOut = pe21DistRqnameOut;
	}
	public String getPe21HostRqnameOut() {
		return pe21HostRqnameOut;
	}
	public void setPe21HostRqnameOut(String pe21HostRqnameOut) {
		this.pe21HostRqnameOut = pe21HostRqnameOut;
	}
	public String getPe21HostQmgrName() {
		return pe21HostQmgrName;
	}
	public void setPe21HostQmgrName(String pe21HostQmgrName) {
		this.pe21HostQmgrName = pe21HostQmgrName;
	}
	
	
	

}
