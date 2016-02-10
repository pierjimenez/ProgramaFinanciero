package pe.com.grupobbva.rating.itcu;

public class HeaderITUC {
	//ih
	private String hostRqnameOut;
	private String hostQmgrName;
	private String distQnameIn;
	private String distRqnameOut;
	private String ccsidBackend;
	private String terminalLogico;
	private String terminalContable;
	private String usuario;
	private String codigoTransaccion;
	private String tipoMensaje;
	private String tipoProceso;
	private String canalComunicacion;
	private String indicadorPreformato;
	//me
	private String tipoMensajeME;
	
	//ip port
	private String ip_port;
	private String encoding;
	
	public HeaderITUC(){
		this.ccsidBackend = "500";
		this.tipoMensaje = "1";
		this.tipoProceso = "O";
		this.indicadorPreformato = "N";
	}
	
	
	public String getIp_port() {
		return ip_port;
	}


	public void setIp_port(String ip_port) {
		this.ip_port = ip_port;
	}


	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	public String getHostRqnameOut() {
		return hostRqnameOut;
	}
	public void setHostRqnameOut(String hostRqnameOut) {
		this.hostRqnameOut = hostRqnameOut;
	}
	public String getHostQmgrName() {
		return hostQmgrName;
	}
	public void setHostQmgrName(String hostQmgrName) {
		this.hostQmgrName = hostQmgrName;
	}
	public String getDistQnameIn() {
		return distQnameIn;
	}
	public void setDistQnameIn(String distQnameIn) {
		this.distQnameIn = distQnameIn;
	}
	public String getDistRqnameOut() {
		return distRqnameOut;
	}
	public void setDistRqnameOut(String distRqnameOut) {
		this.distRqnameOut = distRqnameOut;
	}
	public String getCcsidBackend() {
		return ccsidBackend;
	}
	public void setCcsidBackend(String ccsidBackend) {
		this.ccsidBackend = ccsidBackend;
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
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCodigoTransaccion() {
		return codigoTransaccion;
	}
	public void setCodigoTransaccion(String codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}
	public String getTipoMensaje() {
		return tipoMensaje;
	}
	public void setTipoMensaje(String tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	public String getTipoProceso() {
		return tipoProceso;
	}
	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	public String getCanalComunicacion() {
		return canalComunicacion;
	}
	public void setCanalComunicacion(String canalComunicacion) {
		this.canalComunicacion = canalComunicacion;
	}
	public String getIndicadorPreformato() {
		return indicadorPreformato;
	}
	public void setIndicadorPreformato(String indicadorPreformato) {
		this.indicadorPreformato = indicadorPreformato;
	}

	public String getTipoMensajeME() {
		return tipoMensajeME;
	}

	public void setTipoMensajeME(String tipoMensajeME) {
		this.tipoMensajeME = tipoMensajeME;
	}
	
	

}
