/**
 * ConsultaDatosClienteRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class ConsultaDatosClienteRequest  implements java.io.Serializable {
    private grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera;

    private java.lang.String tipoDocumentoIdentidad;

    private java.lang.String numeroDocumentoIdentidad;

    private java.lang.String codigoCentral;

    public ConsultaDatosClienteRequest() {
    }

    public ConsultaDatosClienteRequest(
           grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera,
           java.lang.String tipoDocumentoIdentidad,
           java.lang.String numeroDocumentoIdentidad,
           java.lang.String codigoCentral) {
           this.cabecera = cabecera;
           this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
           this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
           this.codigoCentral = codigoCentral;
    }


    /**
     * Gets the cabecera value for this ConsultaDatosClienteRequest.
     * 
     * @return cabecera
     */
    public grupobbva.pe.com.EnlaceBBVA.Cabecera getCabecera() {
        return cabecera;
    }


    /**
     * Sets the cabecera value for this ConsultaDatosClienteRequest.
     * 
     * @param cabecera
     */
    public void setCabecera(grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera) {
        this.cabecera = cabecera;
    }


    /**
     * Gets the tipoDocumentoIdentidad value for this ConsultaDatosClienteRequest.
     * 
     * @return tipoDocumentoIdentidad
     */
    public java.lang.String getTipoDocumentoIdentidad() {
        return tipoDocumentoIdentidad;
    }


    /**
     * Sets the tipoDocumentoIdentidad value for this ConsultaDatosClienteRequest.
     * 
     * @param tipoDocumentoIdentidad
     */
    public void setTipoDocumentoIdentidad(java.lang.String tipoDocumentoIdentidad) {
        this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
    }


    /**
     * Gets the numeroDocumentoIdentidad value for this ConsultaDatosClienteRequest.
     * 
     * @return numeroDocumentoIdentidad
     */
    public java.lang.String getNumeroDocumentoIdentidad() {
        return numeroDocumentoIdentidad;
    }


    /**
     * Sets the numeroDocumentoIdentidad value for this ConsultaDatosClienteRequest.
     * 
     * @param numeroDocumentoIdentidad
     */
    public void setNumeroDocumentoIdentidad(java.lang.String numeroDocumentoIdentidad) {
        this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
    }


    /**
     * Gets the codigoCentral value for this ConsultaDatosClienteRequest.
     * 
     * @return codigoCentral
     */
    public java.lang.String getCodigoCentral() {
        return codigoCentral;
    }


    /**
     * Sets the codigoCentral value for this ConsultaDatosClienteRequest.
     * 
     * @param codigoCentral
     */
    public void setCodigoCentral(java.lang.String codigoCentral) {
        this.codigoCentral = codigoCentral;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaDatosClienteRequest)) return false;
        ConsultaDatosClienteRequest other = (ConsultaDatosClienteRequest) obj;
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
            ((this.tipoDocumentoIdentidad==null && other.getTipoDocumentoIdentidad()==null) || 
             (this.tipoDocumentoIdentidad!=null &&
              this.tipoDocumentoIdentidad.equals(other.getTipoDocumentoIdentidad()))) &&
            ((this.numeroDocumentoIdentidad==null && other.getNumeroDocumentoIdentidad()==null) || 
             (this.numeroDocumentoIdentidad!=null &&
              this.numeroDocumentoIdentidad.equals(other.getNumeroDocumentoIdentidad()))) &&
            ((this.codigoCentral==null && other.getCodigoCentral()==null) || 
             (this.codigoCentral!=null &&
              this.codigoCentral.equals(other.getCodigoCentral())));
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
        if (getTipoDocumentoIdentidad() != null) {
            _hashCode += getTipoDocumentoIdentidad().hashCode();
        }
        if (getNumeroDocumentoIdentidad() != null) {
            _hashCode += getNumeroDocumentoIdentidad().hashCode();
        }
        if (getCodigoCentral() != null) {
            _hashCode += getCodigoCentral().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaDatosClienteRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaDatosClienteRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cabecera");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cabecera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "cabecera"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumentoIdentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumentoIdentidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroDocumentoIdentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroDocumentoIdentidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoCentral");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoCentral"));
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
