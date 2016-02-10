/**
 * CtBodyRq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.pf.kc10;

public class CtBodyRq  implements java.io.Serializable {
    private java.lang.String numeroDocumento;

    private java.lang.String anio;

    private java.lang.String expimp;

    public CtBodyRq() {
    }

    public CtBodyRq(
           java.lang.String numeroDocumento,
           java.lang.String anio,
           java.lang.String expimp) {
           this.numeroDocumento = numeroDocumento;
           this.anio = anio;
           this.expimp = expimp;
    }


    /**
     * Gets the numeroDocumento value for this CtBodyRq.
     * 
     * @return numeroDocumento
     */
    public java.lang.String getNumeroDocumento() {
        return numeroDocumento;
    }


    /**
     * Sets the numeroDocumento value for this CtBodyRq.
     * 
     * @param numeroDocumento
     */
    public void setNumeroDocumento(java.lang.String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }


    /**
     * Gets the anio value for this CtBodyRq.
     * 
     * @return anio
     */
    public java.lang.String getAnio() {
        return anio;
    }


    /**
     * Sets the anio value for this CtBodyRq.
     * 
     * @param anio
     */
    public void setAnio(java.lang.String anio) {
        this.anio = anio;
    }


    /**
     * Gets the expimp value for this CtBodyRq.
     * 
     * @return expimp
     */
    public java.lang.String getExpimp() {
        return expimp;
    }


    /**
     * Sets the expimp value for this CtBodyRq.
     * 
     * @param expimp
     */
    public void setExpimp(java.lang.String expimp) {
        this.expimp = expimp;
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
            ((this.numeroDocumento==null && other.getNumeroDocumento()==null) || 
             (this.numeroDocumento!=null &&
              this.numeroDocumento.equals(other.getNumeroDocumento()))) &&
            ((this.anio==null && other.getAnio()==null) || 
             (this.anio!=null &&
              this.anio.equals(other.getAnio()))) &&
            ((this.expimp==null && other.getExpimp()==null) || 
             (this.expimp!=null &&
              this.expimp.equals(other.getExpimp())));
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
        if (getNumeroDocumento() != null) {
            _hashCode += getNumeroDocumento().hashCode();
        }
        if (getAnio() != null) {
            _hashCode += getAnio().hashCode();
        }
        if (getExpimp() != null) {
            _hashCode += getExpimp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtBodyRq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/kc10/", "ctBodyRq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/kc10/", "numeroDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/kc10/", "anio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expimp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/kc10/", "expimp"));
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
