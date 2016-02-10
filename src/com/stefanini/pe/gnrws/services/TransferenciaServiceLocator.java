/**
 * TransferenciaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.stefanini.pe.gnrws.services;

public class TransferenciaServiceLocator extends org.apache.axis.client.Service implements com.stefanini.pe.gnrws.services.TransferenciaService {

    public TransferenciaServiceLocator() {
    }


    public TransferenciaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransferenciaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Transferencia
    private java.lang.String Transferencia_address = "http://118.180.36.103:9080/JWSGeneral/services/Transferencia";

    public java.lang.String getTransferenciaAddress() {
        return Transferencia_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TransferenciaWSDDServiceName = "Transferencia";

    public java.lang.String getTransferenciaWSDDServiceName() {
        return TransferenciaWSDDServiceName;
    }

    public void setTransferenciaWSDDServiceName(java.lang.String name) {
        TransferenciaWSDDServiceName = name;
    }

    public com.stefanini.pe.gnrws.services.Transferencia getTransferencia() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Transferencia_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransferencia(endpoint);
    }

    public com.stefanini.pe.gnrws.services.Transferencia getTransferencia(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.stefanini.pe.gnrws.services.TransferenciaSoapBindingStub _stub = new com.stefanini.pe.gnrws.services.TransferenciaSoapBindingStub(portAddress, this);
            _stub.setPortName(getTransferenciaWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransferenciaEndpointAddress(java.lang.String address) {
        Transferencia_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.stefanini.pe.gnrws.services.Transferencia.class.isAssignableFrom(serviceEndpointInterface)) {
                com.stefanini.pe.gnrws.services.TransferenciaSoapBindingStub _stub = new com.stefanini.pe.gnrws.services.TransferenciaSoapBindingStub(new java.net.URL(Transferencia_address), this);
                _stub.setPortName(getTransferenciaWSDDServiceName());
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
        if ("Transferencia".equals(inputPortName)) {
            return getTransferencia();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://services.gnrws.pe.stefanini.com", "TransferenciaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://services.gnrws.pe.stefanini.com", "Transferencia"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Transferencia".equals(portName)) {
            setTransferenciaEndpointAddress(address);
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
