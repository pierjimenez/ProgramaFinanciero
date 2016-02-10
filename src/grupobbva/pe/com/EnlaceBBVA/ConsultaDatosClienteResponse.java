/**
 * ConsultaDatosClienteResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class ConsultaDatosClienteResponse  implements java.io.Serializable {
    private grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera;

    private java.lang.String codigoCentral;

    private java.lang.String RUC;

    private java.lang.String nombreEmpresa;

    private java.lang.String codigoPais;

    private java.lang.String nombrePais;

    private java.lang.String fechaNacimiento;

    private java.lang.String fechaAntig;

    private java.lang.String codError;

    public ConsultaDatosClienteResponse() {
    }

    public ConsultaDatosClienteResponse(
           grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera,
           java.lang.String codigoCentral,
           java.lang.String RUC,
           java.lang.String nombreEmpresa,
           java.lang.String codigoPais,
           java.lang.String nombrePais,
           java.lang.String fechaNacimiento,
           java.lang.String fechaAntig,
           java.lang.String codError) {
           this.cabecera = cabecera;
           this.codigoCentral = codigoCentral;
           this.RUC = RUC;
           this.nombreEmpresa = nombreEmpresa;
           this.codigoPais = codigoPais;
           this.nombrePais = nombrePais;
           this.fechaNacimiento = fechaNacimiento;
           this.fechaAntig = fechaAntig;
           this.codError = codError;
    }


    /**
     * Gets the cabecera value for this ConsultaDatosClienteResponse.
     * 
     * @return cabecera
     */
    public grupobbva.pe.com.EnlaceBBVA.Cabecera getCabecera() {
        return cabecera;
    }


    /**
     * Sets the cabecera value for this ConsultaDatosClienteResponse.
     * 
     * @param cabecera
     */
    public void setCabecera(grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera) {
        this.cabecera = cabecera;
    }


    /**
     * Gets the codigoCentral value for this ConsultaDatosClienteResponse.
     * 
     * @return codigoCentral
     */
    public java.lang.String getCodigoCentral() {
        return codigoCentral;
    }


    /**
     * Sets the codigoCentral value for this ConsultaDatosClienteResponse.
     * 
     * @param codigoCentral
     */
    public void setCodigoCentral(java.lang.String codigoCentral) {
        this.codigoCentral = codigoCentral;
    }


    /**
     * Gets the RUC value for this ConsultaDatosClienteResponse.
     * 
     * @return RUC
     */
    public java.lang.String getRUC() {
        return RUC;
    }


    /**
     * Sets the RUC value for this ConsultaDatosClienteResponse.
     * 
     * @param RUC
     */
    public void setRUC(java.lang.String RUC) {
        this.RUC = RUC;
    }


    /**
     * Gets the nombreEmpresa value for this ConsultaDatosClienteResponse.
     * 
     * @return nombreEmpresa
     */
    public java.lang.String getNombreEmpresa() {
        return nombreEmpresa;
    }


    /**
     * Sets the nombreEmpresa value for this ConsultaDatosClienteResponse.
     * 
     * @param nombreEmpresa
     */
    public void setNombreEmpresa(java.lang.String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }


    /**
     * Gets the codigoPais value for this ConsultaDatosClienteResponse.
     * 
     * @return codigoPais
     */
    public java.lang.String getCodigoPais() {
        return codigoPais;
    }


    /**
     * Sets the codigoPais value for this ConsultaDatosClienteResponse.
     * 
     * @param codigoPais
     */
    public void setCodigoPais(java.lang.String codigoPais) {
        this.codigoPais = codigoPais;
    }


    /**
     * Gets the nombrePais value for this ConsultaDatosClienteResponse.
     * 
     * @return nombrePais
     */
    public java.lang.String getNombrePais() {
        return nombrePais;
    }


    /**
     * Sets the nombrePais value for this ConsultaDatosClienteResponse.
     * 
     * @param nombrePais
     */
    public void setNombrePais(java.lang.String nombrePais) {
        this.nombrePais = nombrePais;
    }


    /**
     * Gets the fechaNacimiento value for this ConsultaDatosClienteResponse.
     * 
     * @return fechaNacimiento
     */
    public java.lang.String getFechaNacimiento() {
        return fechaNacimiento;
    }


    /**
     * Sets the fechaNacimiento value for this ConsultaDatosClienteResponse.
     * 
     * @param fechaNacimiento
     */
    public void setFechaNacimiento(java.lang.String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    /**
     * Gets the fechaAntig value for this ConsultaDatosClienteResponse.
     * 
     * @return fechaAntig
     */
    public java.lang.String getFechaAntig() {
        return fechaAntig;
    }


    /**
     * Sets the fechaAntig value for this ConsultaDatosClienteResponse.
     * 
     * @param fechaAntig
     */
    public void setFechaAntig(java.lang.String fechaAntig) {
        this.fechaAntig = fechaAntig;
    }


    /**
     * Gets the codError value for this ConsultaDatosClienteResponse.
     * 
     * @return codError
     */
    public java.lang.String getCodError() {
        return codError;
    }


    /**
     * Sets the codError value for this ConsultaDatosClienteResponse.
     * 
     * @param codError
     */
    public void setCodError(java.lang.String codError) {
        this.codError = codError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaDatosClienteResponse)) return false;
        ConsultaDatosClienteResponse other = (ConsultaDatosClienteResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cabecera==null && other.getCabecera()==null) || 
             (this.cabecera!=null &&
              this.cabecera.equals(other.getCabecera()))) &&
            ((this.codigoCentral==null && other.getCodigoCentral()==null) || 
             (this.codigoCentral!=null &&
              this.codigoCentral.equals(other.getCodigoCentral()))) &&
            ((this.RUC==null && other.getRUC()==null) || 
             (this.RUC!=null &&
              this.RUC.equals(other.getRUC()))) &&
            ((this.nombreEmpresa==null && other.getNombreEmpresa()==null) || 
             (this.nombreEmpresa!=null &&
              this.nombreEmpresa.equals(other.getNombreEmpresa()))) &&
            ((this.codigoPais==null && other.getCodigoPais()==null) || 
             (this.codigoPais!=null &&
              this.codigoPais.equals(other.getCodigoPais()))) &&
            ((this.nombrePais==null && other.getNombrePais()==null) || 
             (this.nombrePais!=null &&
              this.nombrePais.equals(other.getNombrePais()))) &&
            ((this.fechaNacimiento==null && other.getFechaNacimiento()==null) || 
             (this.fechaNacimiento!=null &&
              this.fechaNacimiento.equals(other.getFechaNacimiento()))) &&
            ((this.fechaAntig==null && other.getFechaAntig()==null) || 
             (this.fechaAntig!=null &&
              this.fechaAntig.equals(other.getFechaAntig()))) &&
            ((this.codError==null && other.getCodError()==null) || 
             (this.codError!=null &&
              this.codError.equals(other.getCodError())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCabecera() != null) {
            _hashCode += getCabecera().hashCode();
        }
        if (getCodigoCentral() != null) {
            _hashCode += getCodigoCentral().hashCode();
        }
        if (getRUC() != null) {
            _hashCode += getRUC().hashCode();
        }
        if (getNombreEmpresa() != null) {
            _hashCode += getNombreEmpresa().hashCode();
        }
        if (getCodigoPais() != null) {
            _hashCode += getCodigoPais().hashCode();
        }
        if (getNombrePais() != null) {
            _hashCode += getNombrePais().hashCode();
        }
        if (getFechaNacimiento() != null) {
            _hashCode += getFechaNacimiento().hashCode();
        }
        if (getFechaAntig() != null) {
            _hashCode += getFechaAntig().hashCode();
        }
        if (getCodError() != null) {
            _hashCode += getCodError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaDatosClienteResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cabecera");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cabecera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "cabecera"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoCentral");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoCentral"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RUC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RUC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreEmpresa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreEmpresa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoPais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoPais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombrePais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombrePais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaNacimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaNacimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaAntig");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaAntig"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
