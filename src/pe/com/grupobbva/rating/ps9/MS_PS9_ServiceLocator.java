/**
 * MS_PS9_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class MS_PS9_ServiceLocator extends org.apache.axis.client.Service implements pe.com.grupobbva.rating.ps9.MS_PS9_Service {

    public MS_PS9_ServiceLocator() {
    }
    
    public MS_PS9_ServiceLocator(String ip_port) {
    	this.MS_PS9_Port_address = ip_port + "/ps9/sendhost/";
    }


    public MS_PS9_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MS_PS9_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MS_PS9_Port
    private java.lang.String MS_PS9_Port_address = "http://118.180.36.26:7808/ps9/sendhost/";

    public java.lang.String getMS_PS9_PortAddress() {
        return MS_PS9_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MS_PS9_PortWSDDServiceName = "MS_PS9_Port";

    public java.lang.String getMS_PS9_PortWSDDServiceName() {
        return MS_PS9_PortWSDDServiceName;
    }

    public void setMS_PS9_PortWSDDServiceName(java.lang.String name) {
        MS_PS9_PortWSDDServiceName = name;
    }

    public pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType getMS_PS9_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MS_PS9_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMS_PS9_Port(endpoint);
    }

    public pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType getMS_PS9_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            pe.com.grupobbva.rating.ps9.MS_PS9_TESTSOAP_HTTP_BindingStub _stub = new pe.com.grupobbva.rating.ps9.MS_PS9_TESTSOAP_HTTP_BindingStub(portAddress, this);
            _stub.setPortName(getMS_PS9_PortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMS_PS9_PortEndpointAddress(java.lang.String address) {
        MS_PS9_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (pe.com.grupobbva.rating.ps9.MS_PS9_TESTPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                pe.com.grupobbva.rating.ps9.MS_PS9_TESTSOAP_HTTP_BindingStub _stub = new pe.com.grupobbva.rating.ps9.MS_PS9_TESTSOAP_HTTP_BindingStub(new java.net.URL(MS_PS9_Port_address), this);
                _stub.setPortName(getMS_PS9_PortWSDDServiceName());
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
        if ("MS_PS9_Port".equals(inputPortName)) {
            return getMS_PS9_Port();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "MS_PS9_Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "MS_PS9_Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MS_PS9_Port".equals(portName)) {
            setMS_PS9_PortEndpointAddress(address);
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
