/**
 * EnlaceBBVA_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class EnlaceBBVA_ServiceLocator extends org.apache.axis.client.Service implements grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_Service {

    public EnlaceBBVA_ServiceLocator() {
    }
    
    public EnlaceBBVA_ServiceLocator(String endpoint) {
    	this.EnlaceBBVASOAP_address = endpoint;
    }


    public EnlaceBBVA_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EnlaceBBVA_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EnlaceBBVASOAP 
    private java.lang.String EnlaceBBVASOAP_address = "http://118.180.36.26:7801/BBVASERVICIOS/EnlaceBBVA/";

    public java.lang.String getEnlaceBBVASOAPAddress() {
        return EnlaceBBVASOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EnlaceBBVASOAPWSDDServiceName = "EnlaceBBVASOAP";

    public java.lang.String getEnlaceBBVASOAPWSDDServiceName() {
        return EnlaceBBVASOAPWSDDServiceName;
    }

    public void setEnlaceBBVASOAPWSDDServiceName(java.lang.String name) {
        EnlaceBBVASOAPWSDDServiceName = name;
    }

    public grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType getEnlaceBBVASOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EnlaceBBVASOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEnlaceBBVASOAP(endpoint);
    }

    public grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType getEnlaceBBVASOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            grupobbva.pe.com.EnlaceBBVA.EnlaceBBVASOAPStub _stub = new grupobbva.pe.com.EnlaceBBVA.EnlaceBBVASOAPStub(portAddress, this);
            _stub.setPortName(getEnlaceBBVASOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEnlaceBBVASOAPEndpointAddress(java.lang.String address) {
        EnlaceBBVASOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                grupobbva.pe.com.EnlaceBBVA.EnlaceBBVASOAPStub _stub = new grupobbva.pe.com.EnlaceBBVA.EnlaceBBVASOAPStub(new java.net.URL(EnlaceBBVASOAP_address), this);
                _stub.setPortName(getEnlaceBBVASOAPWSDDServiceName());
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
        if ("EnlaceBBVASOAP".equals(inputPortName)) {
            return getEnlaceBBVASOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "EnlaceBBVA");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "EnlaceBBVASOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EnlaceBBVASOAP".equals(portName)) {
            setEnlaceBBVASOAPEndpointAddress(address);
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
