package pe.com.grupobbva.pf.pec6;

public class SPFI_PEC6PortTypeProxy implements pe.com.grupobbva.pf.pec6.SPFI_PEC6PortType {
  private String _endpoint = null;
  private pe.com.grupobbva.pf.pec6.SPFI_PEC6PortType sPFI_PEC6PortType = null;
  
  public SPFI_PEC6PortTypeProxy() {
    _initSPFI_PEC6PortTypeProxy();
  }
  
  public SPFI_PEC6PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initSPFI_PEC6PortTypeProxy();
  }
  
  private void _initSPFI_PEC6PortTypeProxy() {
    try {
      sPFI_PEC6PortType = (new pe.com.grupobbva.pf.pec6.SPFI_PEC6SOAP_HTTP_ServiceLocator()).getSPFI_PEC6SOAP_HTTP_Port();
      if (sPFI_PEC6PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sPFI_PEC6PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sPFI_PEC6PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sPFI_PEC6PortType != null)
      ((javax.xml.rpc.Stub)sPFI_PEC6PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public pe.com.grupobbva.pf.pec6.SPFI_PEC6PortType getSPFI_PEC6PortType() {
    if (sPFI_PEC6PortType == null)
      _initSPFI_PEC6PortTypeProxy();
    return sPFI_PEC6PortType;
  }
  
  public pe.com.grupobbva.pf.pec6.CtConSpfiPEC6Rs callPEC6(pe.com.grupobbva.pf.pec6.CtConSpfiPEC6Rq conSpfiPEC6Rq) throws java.rmi.RemoteException{
    if (sPFI_PEC6PortType == null)
      _initSPFI_PEC6PortTypeProxy();
    return sPFI_PEC6PortType.callPEC6(conSpfiPEC6Rq);
  }
  
  
}