/**
 * DLG_RIG7_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.pfa.itda;

public class DLG_RIG7_ServiceLocator extends org.apache.axis.client.Service implements pe.com.grupobbva.rating.pfa.itda.DLG_RIG7_Service {

    public DLG_RIG7_ServiceLocator(String ip_port) {
    	this.DLG_RIG7_PortType_address = ip_port + "/pfa/itda/";
    	//this.DLG_RIG7_PortType_address = ip_port + "/it/itda/";
    }
    
    public DLG_RIG7_ServiceLocator() {
    	
    }


    public DLG_RIG7_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DLG_RIG7_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DLG_RIG7_PortType
    private java.lang.String DLG_RIG7_PortType_address = "http://118.180.36.26:7801/pfa/itda/";

    public java.lang.String getDLG_RIG7_PortTypeAddress() {
        return DLG_RIG7_PortType_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DLG_RIG7_PortTypeWSDDServiceName = "DLG_RIG7_PortType";

    public java.lang.String getDLG_RIG7_PortTypeWSDDServiceName() {
        return DLG_RIG7_PortTypeWSDDServiceName;
    }

    public void setDLG_RIG7_PortTypeWSDDServiceName(java.lang.String name) {
        DLG_RIG7_PortTypeWSDDServiceName = name;
    }

    public pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType getDLG_RIG7_PortType() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DLG_RIG7_PortType_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDLG_RIG7_PortType(endpoint);
    }

    public pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType getDLG_RIG7_PortType(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDASOAP_HTTP_BindingStub _stub = new pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDASOAP_HTTP_BindingStub(portAddress, this);
            _stub.setPortName(getDLG_RIG7_PortTypeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDLG_RIG7_PortTypeEndpointAddress(java.lang.String address) {
        DLG_RIG7_PortType_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDASOAP_HTTP_BindingStub _stub = new pe.com.grupobbva.rating.pfa.itda.MS_PFA_ITDASOAP_HTTP_BindingStub(new java.net.URL(DLG_RIG7_PortType_address), this);
                _stub.setPortName(getDLG_RIG7_PortTypeWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DLG_RIG7_PortType".equals(inputPortName)) {
            return getDLG_RIG7_PortType();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "DLG_RIG7_Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "DLG_RIG7_PortType"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DLG_RIG7_PortType".equals(portName)) {
            setDLG_RIG7_PortTypeEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
