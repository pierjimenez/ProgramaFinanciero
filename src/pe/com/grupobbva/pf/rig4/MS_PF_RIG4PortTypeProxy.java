package pe.com.grupobbva.pf.rig4;

public class MS_PF_RIG4PortTypeProxy implements pe.com.grupobbva.pf.rig4.MS_PF_RIG4PortType {
  private String _endpoint = null;
  private pe.com.grupobbva.pf.rig4.MS_PF_RIG4PortType mS_PF_RIG4PortType = null;
  
  public MS_PF_RIG4PortTypeProxy() {
    _initMS_PF_RIG4PortTypeProxy();
  }
  
  public MS_PF_RIG4PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initMS_PF_RIG4PortTypeProxy();
  }
  
  private void _initMS_PF_RIG4PortTypeProxy() {
    try {
      mS_PF_RIG4PortType = (new pe.com.grupobbva.pf.rig4.MS_PF_RIG4SOAP_HTTP_ServiceLocator(_endpoint)).getMS_PF_RIG4SOAP_HTTP_Port();
      if (mS_PF_RIG4PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mS_PF_RIG4PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mS_PF_RIG4PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mS_PF_RIG4PortType != null)
      ((javax.xml.rpc.Stub)mS_PF_RIG4PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public pe.com.grupobbva.pf.rig4.MS_PF_RIG4PortType getMS_PF_RIG4PortType() {
    if (mS_PF_RIG4PortType == null)
      _initMS_PF_RIG4PortTypeProxy();
    return mS_PF_RIG4PortType;
  }
  
  public pe.com.grupobbva.pf.rig4.CtConDlgRIG4Rs callrig4(pe.com.grupobbva.pf.rig4.CtConDlgRIG4Rq conDlgRIG4Rq) throws java.rmi.RemoteException{
    if (mS_PF_RIG4PortType == null)
      _initMS_PF_RIG4PortTypeProxy();
    return mS_PF_RIG4PortType.callrig4(conDlgRIG4Rq);
  }
  
  
}