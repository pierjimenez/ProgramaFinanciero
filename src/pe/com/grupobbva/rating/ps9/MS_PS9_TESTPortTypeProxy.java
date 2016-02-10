package pe.com.grupobbva.rating.ps9;

public class MS_PS9_TESTPortTypeProxy implements pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType {
  private String _endpoint = null;
  private pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType mS_PS9_TESTPortType = null;
  
  public MS_PS9_TESTPortTypeProxy() {
    _initMS_PS9_TESTPortTypeProxy();
  }
  
  public MS_PS9_TESTPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initMS_PS9_TESTPortTypeProxy();
  }
  
  private void _initMS_PS9_TESTPortTypeProxy() {
    try {
      mS_PS9_TESTPortType = (new pe.com.grupobbva.rating.ps9.MS_PS9_ServiceLocator()).getMS_PS9_Port();
      if (mS_PS9_TESTPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mS_PS9_TESTPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mS_PS9_TESTPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mS_PS9_TESTPortType != null)
      ((javax.xml.rpc.Stub)mS_PS9_TESTPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType getMS_PS9_TESTPortType() {
    if (mS_PS9_TESTPortType == null)
      _initMS_PS9_TESTPortTypeProxy();
    return mS_PS9_TESTPortType;
  }
  
  public pe.com.grupobbva.rating.ps9.Ps9RS sendHost(pe.com.grupobbva.rating.ps9.Ps9RQ ps9Rq) throws java.rmi.RemoteException{
    if (mS_PS9_TESTPortType == null)
      _initMS_PS9_TESTPortTypeProxy();
    return mS_PS9_TESTPortType.sendHost(ps9Rq);
  }
  
  
}