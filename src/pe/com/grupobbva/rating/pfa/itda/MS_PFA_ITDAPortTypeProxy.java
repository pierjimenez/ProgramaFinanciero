package pe.com.grupobbva.rating.pfa.itda;

public class MS_PFA_ITDAPortTypeProxy implements pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType {
  private String _endpoint = null;
  private pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType mS_PFA_ITDAPortType = null;
  
  public MS_PFA_ITDAPortTypeProxy() {
    _initMS_PFA_ITDAPortTypeProxy();
  }
  
  public MS_PFA_ITDAPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initMS_PFA_ITDAPortTypeProxy();
  }
  
  private void _initMS_PFA_ITDAPortTypeProxy() {
    try {
      mS_PFA_ITDAPortType = (new pe.com.grupobbva.rating.pfa.itda.DLG_RIG7_ServiceLocator()).getDLG_RIG7_PortType();
      if (mS_PFA_ITDAPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mS_PFA_ITDAPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mS_PFA_ITDAPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mS_PFA_ITDAPortType != null)
      ((javax.xml.rpc.Stub)mS_PFA_ITDAPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType getMS_PFA_ITDAPortType() {
    if (mS_PFA_ITDAPortType == null)
      _initMS_PFA_ITDAPortTypeProxy();
    return mS_PFA_ITDAPortType;
  }
  
  public pe.com.grupobbva.rating.pfa.itda.CtConPFaITDaRs obtenerEjercicios(pe.com.grupobbva.rating.pfa.itda.CtConPFaITDaRq ctConPFaITDaRq) throws java.rmi.RemoteException{
    if (mS_PFA_ITDAPortType == null)
      _initMS_PFA_ITDAPortTypeProxy();
    return mS_PFA_ITDAPortType.obtenerEjercicios(ctConPFaITDaRq);
  }
  
  
}