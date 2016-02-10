/**
 * EnlaceBBVASOAPStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class EnlaceBBVASOAPStub extends org.apache.axis.client.Stub implements grupobbva.pe.com.EnlaceBBVA.EnlaceBBVA_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ConsultaGruposEconomico");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaGruposEconomicoRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposEconomicoRequest"), grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposEconomicoReponse"));
        oper.setReturnClass(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaGruposEconomicoReponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ConsultaDatosCliente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaDatosClienteRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteRequest"), grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteResponse"));
        oper.setReturnClass(grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaDatosClienteResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ConsultaGruposRiesoBuro");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaGruposRiesoBuroRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposRiesoBuroRequest"), grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposRiesoBuroResponse"));
        oper.setReturnClass(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "ConsultaGruposRiesoBuroResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

    }

    public EnlaceBBVASOAPStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public EnlaceBBVASOAPStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public EnlaceBBVASOAPStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteRequest");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteResponse");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposEconomicoReponse");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposEconomicoRequest");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposRiesoBuroRequest");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposRiesoBuroResponse");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">EnlaceBBVAFault");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.EnlaceBBVAFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "cabecera");
            cachedSerQNames.add(qName);
            cls = grupobbva.pe.com.EnlaceBBVA.Cabecera.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse consultaGruposEconomico(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://com.pe.grupobbva/EnlaceBBVA/ConsultaGruposEconomico");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ConsultaGruposEconomico"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse) org.apache.axis.utils.JavaUtils.convert(_resp, grupobbva.pe.com.EnlaceBBVA.ConsultaGruposEconomicoReponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse consultaDatosCliente(grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://com.pe.grupobbva/EnlaceBBVA/ConsultaDatosCliente");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ConsultaDatosCliente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse) org.apache.axis.utils.JavaUtils.convert(_resp, grupobbva.pe.com.EnlaceBBVA.ConsultaDatosClienteResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse consultaGruposRiesoBuro(grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://com.pe.grupobbva/EnlaceBBVA/ConsultaGruposRiesoBuro");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ConsultaGruposRiesoBuro"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse) org.apache.axis.utils.JavaUtils.convert(_resp, grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
