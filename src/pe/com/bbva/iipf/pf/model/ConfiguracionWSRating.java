package pe.com.bbva.iipf.pf.model;

public class ConfiguracionWSRating {
	
	//public static final String TIPO_EMPRESA ="I";//PARAMETRO DE BD PF
	private String tipoEmpresa;
	//public static final String TIPO_GRUPO ="C";//PARAMETRO DE BD PF
	private String tipoGrupo;
	//static public String codigoCliente = "04735927";//VALOR ENVIADO POR EL APLICATIVO PF
	//static public String usuario = "P016359";//VALOR ENVIADO POR EL APLICATIVO PF
	//static public String flagEmpresaGrupo  ="I"; //VALOR ENVIADO POR EL APLICATIVO PF
	
	private String codigoUsuario;
	
	//public static final String OPCION_APLICACION ="00";//PARAMETRO DE BD PF
	private String opcionAplicacion;
	//public static final String TERMINAL_LOGICO ="D90L";//PARAMETRO DE BD PF 
	private String terminalLogico;
	//public static final String TERMINAL_CONTABLE ="D90L";//PARAMETRO DE BD PF
	private String terminalContable;
	
	//public static final String ITDA_APLICATIVO ="RT";//PARAMETRO DE BD PF
	private String itdaAplicativo;
	//public static final String ITDA_TRATAMIENTO ="C";//PARAMETRO DE BD PF
	private String itdaTratamiento;
	//public static final String ITDA_IP_PORT ="http://118.180.36.26:7801";//PARAMETRO DE BD PF
	private String itdaIpPort;
	
	
	//public static final String ITCU_TRANSACCION ="ITCU";//PARAMETRO DE BD PF
	private String itcuTransaccion;
	//public static final String ITCU_TIPO_MENSAJE ="1";//PARAMETRO DE BD PF
	private String itcuTipoMensaje;
	//public static final String ITCU_TIPO_PROCESO ="O";//PARAMETRO DE BD PF
	private String itcuTipoProceso;
	//public static final String ITCU_CANAL_COMUNICACION ="PF";//PARAMETRO DE BD PF
	private String itcuCanalComunicacion;
	//public static final String ITCU_INDICADOR_PREFORMATO ="N";//PARAMETRO DE BD PF
	private String itcuIndicadorPreformato;
	//public static final String ITCU_TIPO_MENSAJE_ME ="B";//PARAMETRO DE BD PF
	private String itcuTipoMensajeMe;
	//public static final String ITCU_DIST_QNAME_IN ="QLT.GEC.RESPA";//PARAMETRO DE BD PF
	private String itcuDistQnameIn;
	//public static final String ITCU_DIST_RQNAME_OUT ="QRT.GEC.ENVIO.MPD1";//PARAMETRO DE BD PF
	private String itcuDistRqnameOut;
	//public static final String ITCU_HOST_RQNAME_OUT ="QRT.GEC.RESP.QMPCINT8";//PARAMETRO DE BD PF
	private String itcuHostRqnameOut;
	//public static final String ITCU_HOST_QMGR_NAME ="MPD1";//PARAMETRO DE BD PF
	private String itcuHostQmgrName;
	
	//public static final String ITCU_TIPO_EMPRESA ="0";//PARAMETRO DE BD PF
	private String itcuTipoEmpresa;
	//public static final String ITCU_OPCION ="1";//PARAMETRO DE BD PF
	private String itcuOpcion;
	//public static final String ITCU_IP_PORT ="http://118.180.36.26:7808";//PARAMETRO DE BD PF
	private String itcuIpPort;
	//public static final String ITCU_ENCODING ="IBM-500";//PARAMETRO DE BD PF
	private String itcuEncoding;
	
	
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}
	public String getTipoGrupo() {
		return tipoGrupo;
	}
	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}
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
	public String getItdaAplicativo() {
		return itdaAplicativo;
	}
	public void setItdaAplicativo(String itdaAplicativo) {
		this.itdaAplicativo = itdaAplicativo;
	}
	public String getItdaTratamiento() {
		return itdaTratamiento;
	}
	public void setItdaTratamiento(String itdaTratamiento) {
		this.itdaTratamiento = itdaTratamiento;
	}
	public String getItcuTransaccion() {
		return itcuTransaccion;
	}
	public void setItcuTransaccion(String itcuTransaccion) {
		this.itcuTransaccion = itcuTransaccion;
	}
	public String getItcuTipoMensaje() {
		return itcuTipoMensaje;
	}
	public void setItcuTipoMensaje(String itcuTipoMensaje) {
		this.itcuTipoMensaje = itcuTipoMensaje;
	}
	public String getItcuTipoProceso() {
		return itcuTipoProceso;
	}
	public void setItcuTipoProceso(String itcuTipoProceso) {
		this.itcuTipoProceso = itcuTipoProceso;
	}
	public String getItcuCanalComunicacion() {
		return itcuCanalComunicacion;
	}
	public void setItcuCanalComunicacion(String itcuCanalComunicacion) {
		this.itcuCanalComunicacion = itcuCanalComunicacion;
	}
	public String getItcuIndicadorPreformato() {
		return itcuIndicadorPreformato;
	}
	public void setItcuIndicadorPreformato(String itcuIndicadorPreformato) {
		this.itcuIndicadorPreformato = itcuIndicadorPreformato;
	}
	public String getItcuTipoMensajeMe() {
		return itcuTipoMensajeMe;
	}
	public void setItcuTipoMensajeMe(String itcuTipoMensajeMe) {
		this.itcuTipoMensajeMe = itcuTipoMensajeMe;
	}
	public String getItcuDistQnameIn() {
		return itcuDistQnameIn;
	}
	public void setItcuDistQnameIn(String itcuDistQnameIn) {
		this.itcuDistQnameIn = itcuDistQnameIn;
	}
	public String getItcuDistRqnameOut() {
		return itcuDistRqnameOut;
	}
	public void setItcuDistRqnameOut(String itcuDistRqnameOut) {
		this.itcuDistRqnameOut = itcuDistRqnameOut;
	}
	public String getItcuHostRqnameOut() {
		return itcuHostRqnameOut;
	}
	public void setItcuHostRqnameOut(String itcuHostRqnameOut) {
		this.itcuHostRqnameOut = itcuHostRqnameOut;
	}
	public String getItcuHostQmgrName() {
		return itcuHostQmgrName;
	}
	public void setItcuHostQmgrName(String itcuHostQmgrName) {
		this.itcuHostQmgrName = itcuHostQmgrName;
	}
	public String getItcuTipoEmpresa() {
		return itcuTipoEmpresa;
	}
	public void setItcuTipoEmpresa(String itcuTipoEmpresa) {
		this.itcuTipoEmpresa = itcuTipoEmpresa;
	}
	public String getItcuOpcion() {
		return itcuOpcion;
	}
	public void setItcuOpcion(String itcuOpcion) {
		this.itcuOpcion = itcuOpcion;
	}
	public String getItdaIpPort() {
		return itdaIpPort;
	}
	public void setItdaIpPort(String itdaIpPort) {
		this.itdaIpPort = itdaIpPort;
	}
	public String getItcuIpPort() {
		return itcuIpPort;
	}
	public void setItcuIpPort(String itcuIpPort) {
		this.itcuIpPort = itcuIpPort;
	}
	public String getItcuEncoding() {
		return itcuEncoding;
	}
	public void setItcuEncoding(String itcuEncoding) {
		this.itcuEncoding = itcuEncoding;
	}
	

	
}
