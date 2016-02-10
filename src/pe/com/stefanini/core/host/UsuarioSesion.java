/*
 * Created on 22/09/2009
 *
 */
package pe.com.stefanini.core.host;


/**
 * @author jlezama
 * 
 */
public class UsuarioSesion {
	public UsuarioSesion() {

	}

	private String codigoUsuario;

	private String perfilHost;

	private String nombre;

	private String registroHost;

	private String apellidoPaterno;

	private String apellidoMaterno;

	private String sesion;

	private String password;
	
	private String tipoBusqueda;
	
	private String valorBusqueda;
	
	//INI MCG
	private String codigoCargo;
	private String codigoOficina;
	//FIN MCG

	/**
	 * @return Returns the apellidoMaterno.
	 */
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	/**
	 * @param apellidoMaterno
	 *            The apellidoMaterno to set.
	 */
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	/**
	 * @return Returns the apellidoPaterno.
	 */
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	/**
	 * @param apellidoPaterno
	 *            The apellidoPaterno to set.
	 */
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	/**
	 * @return Returns the codigoUsuario.
	 */
	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	/**
	 * @param codigoUsuario
	 *            The codigoUsuario to set.
	 */
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the perfilHost.
	 */
	public String getPerfilHost() {
		return perfilHost;
	}

	/**
	 * @param perfilHost
	 *            The perfilHost to set.
	 */
	public void setPerfilHost(String perfilHost) {
		this.perfilHost = perfilHost;
	}

	/**
	 * @return Returns the registroHost.
	 */
	public String getRegistroHost() {
		return registroHost;
	}

	/**
	 * @param registroHost
	 *            The registroHost to set.
	 */
	public void setRegistroHost(String registroHost) {
		this.registroHost = registroHost;
	}

	/**
	 * @return Returns the sesion.
	 */
	public String getSesion() {
		return sesion;
	}

	/**
	 * @param sesion
	 *            The sesion to set.
	 */
	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	/**
	 * @return Returns the tipoBusqueda.
	 */
	public String getTipoBusqueda() {
		return tipoBusqueda;
	}
	/**
	 * @param tipoBusqueda The tipoBusqueda to set.
	 */
	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	/**
	 * @return Returns the valorBusqueda.
	 */
	public String getValorBusqueda() {
		return valorBusqueda;
	}
	/**
	 * @param valorBusqueda The valorBusqueda to set.
	 */
	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public String getCodigoOficina() {
		return codigoOficina;
	}

	public void setCodigoOficina(String codigoOficina) {
		this.codigoOficina = codigoOficina;
	}
	
	
}
