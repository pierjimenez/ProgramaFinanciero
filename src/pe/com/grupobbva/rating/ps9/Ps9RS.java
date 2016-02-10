/**
 * Ps9RS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class Ps9RS  implements java.io.Serializable {
    private pe.com.grupobbva.rating.ps9.HeaderRS header;

    private pe.com.grupobbva.rating.ps9.BodyRS body;

    public Ps9RS() {
    }

    public Ps9RS(
           pe.com.grupobbva.rating.ps9.HeaderRS header,
           pe.com.grupobbva.rating.ps9.BodyRS body) {
           this.header = header;
           this.body = body;
    }


    /**
     * Gets the header value for this Ps9RS.
     * 
     * @return header
     */
    public pe.com.grupobbva.rating.ps9.HeaderRS getHeader() {
        return header;
    }


    /**
     * Sets the header value for this Ps9RS.
     * 
     * @param header
     */
    public void setHeader(pe.com.grupobbva.rating.ps9.HeaderRS header) {
        this.header = header;
    }


    /**
     * Gets the body value for this Ps9RS.
     * 
     * @return body
     */
    public pe.com.grupobbva.rating.ps9.BodyRS getBody() {
        return body;
    }


    /**
     * Sets the body value for this Ps9RS.
     * 
     * @param body
     */
    public void setBody(pe.com.grupobbva.rating.ps9.BodyRS body) {
        this.body = body;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ps9RS)) return false;
        Ps9RS other = (Ps9RS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.header==null && other.getHeader()==null) || 
             (this.header!=null &&
              this.header.equals(other.getHeader()))) &&
            ((this.body==null && other.getBody()==null) || 
             (this.body!=null &&
              this.body.equals(other.getBody())));
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
        if (getHeader() != null) {
            _hashCode += getHeader().hashCode();
        }
        if (getBody() != null) {
            _hashCode += getBody().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ps9RS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "Ps9RS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "HeaderRS"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("body");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "body"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "BodyRS"));
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
