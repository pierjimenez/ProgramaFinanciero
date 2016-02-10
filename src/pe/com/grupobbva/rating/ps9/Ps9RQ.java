/**
 * Ps9RQ.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class Ps9RQ  implements java.io.Serializable {
    private pe.com.grupobbva.rating.ps9.HeaderRQ header;

    private pe.com.grupobbva.rating.ps9.BodyRQ body;

    public Ps9RQ() {
    }

    public Ps9RQ(
           pe.com.grupobbva.rating.ps9.HeaderRQ header,
           pe.com.grupobbva.rating.ps9.BodyRQ body) {
           this.header = header;
           this.body = body;
    }


    /**
     * Gets the header value for this Ps9RQ.
     * 
     * @return header
     */
    public pe.com.grupobbva.rating.ps9.HeaderRQ getHeader() {
        return header;
    }


    /**
     * Sets the header value for this Ps9RQ.
     * 
     * @param header
     */
    public void setHeader(pe.com.grupobbva.rating.ps9.HeaderRQ header) {
        this.header = header;
    }


    /**
     * Gets the body value for this Ps9RQ.
     * 
     * @return body
     */
    public pe.com.grupobbva.rating.ps9.BodyRQ getBody() {
        return body;
    }


    /**
     * Sets the body value for this Ps9RQ.
     * 
     * @param body
     */
    public void setBody(pe.com.grupobbva.rating.ps9.BodyRQ body) {
        this.body = body;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ps9RQ)) return false;
        Ps9RQ other = (Ps9RQ) obj;
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
        new org.apache.axis.description.TypeDesc(Ps9RQ.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "Ps9RQ"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "HeaderRQ"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("body");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "body"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "BodyRQ"));
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
