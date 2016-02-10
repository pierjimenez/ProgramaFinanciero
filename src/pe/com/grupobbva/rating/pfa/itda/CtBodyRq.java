/**
 * CtBodyRq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.pfa.itda;

public class CtBodyRq  implements java.io.Serializable {
    private java.lang.String codigoCliente;

    private java.lang.String tratamiento;

    private java.lang.String aplicativo;

    public CtBodyRq() {
    }

    public CtBodyRq(
           java.lang.String codigoCliente,
           java.lang.String tratamiento,
           java.lang.String aplicativo) {
           this.codigoCliente = codigoCliente;
           this.tratamiento = tratamiento;
           this.aplicativo = aplicativo;
    }


    /**
     * Gets the codigoCliente value for this CtBodyRq.
     * 
     * @return codigoCliente
     */
    public java.lang.String getCodigoCliente() {
        return codigoCliente;
    }


    /**
     * Sets the codigoCliente value for this CtBodyRq.
     * 
     * @param codigoCliente
     */
    public void setCodigoCliente(java.lang.String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }


    /**
     * Gets the tratamiento value for this CtBodyRq.
     * 
     * @return tratamiento
     */
    public java.lang.String getTratamiento() {
        return tratamiento;
    }


    /**
     * Sets the tratamiento value for this CtBodyRq.
     * 
     * @param tratamiento
     */
    public void setTratamiento(java.lang.String tratamiento) {
        this.tratamiento = tratamiento;
    }


    /**
     * Gets the aplicativo value for this CtBodyRq.
     * 
     * @return aplicativo
     */
    public java.lang.String getAplicativo() {
        return aplicativo;
    }


    /**
     * Sets the aplicativo value for this CtBodyRq.
     * 
     * @param aplicativo
     */
    public void setAplicativo(java.lang.String aplicativo) {
        this.aplicativo = aplicativo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CtBodyRq)) return false;
        CtBodyRq other = (CtBodyRq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoCliente==null && other.getCodigoCliente()==null) || 
             (this.codigoCliente!=null &&
              this.codigoCliente.equals(other.getCodigoCliente()))) &&
            ((this.tratamiento==null && other.getTratamiento()==null) || 
             (this.tratamiento!=null &&
              this.tratamiento.equals(other.getTratamiento()))) &&
            ((this.aplicativo==null && other.getAplicativo()==null) || 
             (this.aplicativo!=null &&
              this.aplicativo.equals(other.getAplicativo())));
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
        if (getCodigoCliente() != null) {
            _hashCode += getCodigoCliente().hashCode();
        }
        if (getTratamiento() != null) {
            _hashCode += getTratamiento().hashCode();
        }
        if (getAplicativo() != null) {
            _hashCode += getAplicativo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtBodyRq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "ctBodyRq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "codigoCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tratamiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "tratamiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicativo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pfa/itda/", "aplicativo"));
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
