package grupobbva.pe.com.EnlaceBBVA;

public class EnlaceBBVAProxy implements grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType {
  private String _endpoint = null;
  private grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType enlaceBBVA_PortType = null;
  
  public EnlaceBBVAProxy() {
    _initEnlaceBBVAProxy();
  }
  
  public EnlaceBBVAProxy(String endpoint) {
    _endpoint = endpoint;
    _initEnlaceBBVAProxy();
  }
  
  private void _initEnlaceBBVAProxy() {
    try {
      enlaceBBVA_PortType = (new grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_ServiceLocator()).getEnlaceBBVASOAP();
      if (enlaceBBVA_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)enlaceBBVA_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)enlaceBBVA_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (enlaceBBVA_PortType != null)
      ((javax.xml.rpc.Stub)enlaceBBVA_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType getEnlaceBBVA_PortType() {
    if (enlaceBBVA_PortType == null)
      _initEnlaceBBVAProxy();
    return enlaceBBVA_PortType;
  }
  
  public grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse consultaGruposEconomico(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoRequest parameters) throws java.rmi.RemoteException{
    if (enlaceBBVA_PortType == null)
      _initEnlaceBBVAProxy();
    return enlaceBBVA_PortType.consultaGruposEconomico(parameters);
  }
  
  public grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse consultaDatosCliente(grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteRequest parameters) throws java.rmi.RemoteException{
    if (enlaceBBVA_PortType == null)
      _initEnlaceBBVAProxy();
    return enlaceBBVA_PortType.consultaDatosCliente(parameters);
  }
  
  public grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse consultaGruposRiesoBuro(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroRequest parameters) throws java.rmi.RemoteException{
    if (enlaceBBVA_PortType == null)
      _initEnlaceBBVAProxy();
    return enlaceBBVA_PortType.consultaGruposRiesoBuro(parameters);
  }
  
  
}