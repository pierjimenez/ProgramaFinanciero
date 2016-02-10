package pe.com.grupobbva.pf.kc10;

public class MS_KC_KC10PortTypeProxy implements pe.com.grupobbva.pf.kc10.MS_KC_KC10PortType {
  private String _endpoint = null;
  private pe.com.grupobbva.pf.kc10.MS_KC_KC10PortType mS_KC_KC10PortType = null;
  
  public MS_KC_KC10PortTypeProxy() {
    _initMS_KC_KC10PortTypeProxy();
  }
  
  public MS_KC_KC10PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initMS_KC_KC10PortTypeProxy();
  }
  
  private void _initMS_KC_KC10PortTypeProxy() {
    try {
      mS_KC_KC10PortType = (new pe.com.grupobbva.pf.kc10.KC_KC10_ServiceLocator()).getKC_KC10_Port();
      if (mS_KC_KC10PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mS_KC_KC10PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mS_KC_KC10PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mS_KC_KC10PortType != null)
      ((javax.xml.rpc.Stub)mS_KC_KC10PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public pe.com.grupobbva.pf.kc10.MS_KC_KC10PortType getMS_KC_KC10PortType() {
    if (mS_KC_KC10PortType == null)
      _initMS_KC_KC10PortTypeProxy();
    return mS_KC_KC10PortType;
  }
  
  public pe.com.grupobbva.pf.kc10.CtConKC10Rs consultaPresupuestoCliente(pe.com.grupobbva.pf.kc10.CtConKC10Rq conKC10Rq) throws java.rmi.RemoteException{
    if (mS_KC_KC10PortType == null)
      _initMS_KC_KC10PortTypeProxy();
    return mS_KC_KC10PortType.consultaPresupuestoCliente(conKC10Rq);
  }
  
  
}