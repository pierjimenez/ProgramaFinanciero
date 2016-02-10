package pe.com.grupobbva.pf.rig4;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

public class MS_PF_RIG4SOAP_HTTP_PortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private pe.com.grupobbva.pf.rig4.MSPFRIG4SOAPHTTPService _service = null;
        private pe.com.grupobbva.pf.rig4.MSPFRIG4PortType _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            _service = new pe.com.grupobbva.pf.rig4.MSPFRIG4SOAPHTTPService();
            initCommon();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new pe.com.grupobbva.pf.rig4.MSPFRIG4SOAPHTTPService(wsdlLocation, serviceName);
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getMSPFRIG4SOAPHTTPPort();
        }

        public pe.com.grupobbva.pf.rig4.MSPFRIG4PortType getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://grupobbva.com.pe/pf/rig4/", "MS_PF_RIG4SOAP_HTTP_Port");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }
    }

    public MS_PF_RIG4SOAP_HTTP_PortProxy() {
        _descriptor = new Descriptor();
    }

    public MS_PF_RIG4SOAP_HTTP_PortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public CtConDlgRIG4Rs callrig4(CtConDlgRIG4Rq conDlgRIG4Rq) {
        return _getDescriptor().getProxy().callrig4(conDlgRIG4Rq);
    }

}