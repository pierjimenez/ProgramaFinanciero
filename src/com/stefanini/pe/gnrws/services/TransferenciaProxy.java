package com.stefanini.pe.gnrws.services;

public class TransferenciaProxy implements com.stefanini.pe.gnrws.services.Transferencia {
  private String _endpoint = null;
  private com.stefanini.pe.gnrws.services.Transferencia transferencia = null;
  
  public TransferenciaProxy() {
    _initTransferenciaProxy();
  }
  
  public TransferenciaProxy(String endpoint) {
    _endpoint = endpoint;
    _initTransferenciaProxy();
  }
  
  private void _initTransferenciaProxy() {
    try {
      transferencia = (new com.stefanini.pe.gnrws.services.TransferenciaServiceLocator()).getTransferencia();
      if (transferencia != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)transferencia)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)transferencia)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (transferencia != null)
      ((javax.xml.rpc.Stub)transferencia)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.stefanini.pe.gnrws.services.Transferencia getTransferencia() {
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia;
  }
  
  public java.lang.String validarUsuarioPorTipo(java.lang.String strIdLogin, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.validarUsuarioPorTipo(strIdLogin, strAmbienteHOST, strIpCliente, strSesion, strUsuario, strPassword, strCodigoSistema);
  }
  
  public java.lang.String listaMenuGeneral(java.lang.String codSistema, java.lang.String strPermisos) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.listaMenuGeneral(codSistema, strPermisos);
  }
  
  public java.lang.String listaMenuGenerico(java.lang.String strIdLogin, java.lang.String sIdTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.listaMenuGenerico(strIdLogin, sIdTrama, strAmbienteHOST, strIpCliente, strSesion, strUsuario, strPassword, strCodigoSistema);
  }
  
  public java.lang.String enviaTramas(java.lang.String strIdLogin, java.lang.String strIdTrama, java.lang.String strTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.enviaTramas(strIdLogin, strIdTrama, strTrama, strAmbienteHOST, strIpCliente, strSesion, strUsuario, strPassword, strCodigoSistema);
  }
  
  public java.lang.String listaMenu(java.lang.String strIdLogin, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.listaMenu(strIdLogin, strAmbienteHOST, strIpCliente, strSesion, strUsuario, strPassword, strCodigoSistema);
  }
  
  public java.lang.String listaMenuGenericoSinTecla(java.lang.String strIdLogin, java.lang.String sIdTrama, java.lang.String strTecla, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.listaMenuGenericoSinTecla(strIdLogin, sIdTrama, strTecla, strAmbienteHOST, strIpCliente, strSesion, strUsuario, strPassword, strCodigoSistema);
  }
  
  public java.lang.String soloTrama(java.lang.String strIdTrama, java.lang.String strTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strCodigoSistema) throws java.rmi.RemoteException{
    if (transferencia == null)
      _initTransferenciaProxy();
    return transferencia.soloTrama(strIdTrama, strTrama, strAmbienteHOST, strIpCliente, strCodigoSistema);
  }
  
  
}