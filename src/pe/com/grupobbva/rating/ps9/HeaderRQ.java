/**
 * HeaderRQ.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class HeaderRQ  implements java.io.Serializable {
    private java.lang.String dist_qname_in;

    private java.lang.String dist_qname_out;

    private java.lang.String host_qmanager;

    private java.lang.String host_qname_out;

    public HeaderRQ() {
    }

    public HeaderRQ(
           java.lang.String dist_qname_in,
           java.lang.String dist_qname_out,
           java.lang.String host_qmanager,
           java.lang.String host_qname_out) {
           this.dist_qname_in = dist_qname_in;
           this.dist_qname_out = dist_qname_out;
           this.host_qmanager = host_qmanager;
           this.host_qname_out = host_qname_out;
    }


    /**
     * Gets the dist_qname_in value for this HeaderRQ.
     * 
     * @return dist_qname_in
     */
    public java.lang.String getDist_qname_in() {
        return dist_qname_in;
    }


    /**
     * Sets the dist_qname_in value for this HeaderRQ.
     * 
     * @param dist_qname_in
     */
    public void setDist_qname_in(java.lang.String dist_qname_in) {
        this.dist_qname_in = dist_qname_in;
    }


    /**
     * Gets the dist_qname_out value for this HeaderRQ.
     * 
     * @return dist_qname_out
     */
    public java.lang.String getDist_qname_out() {
        return dist_qname_out;
    }


    /**
     * Sets the dist_qname_out value for this HeaderRQ.
     * 
     * @param dist_qname_out
     */
    public void setDist_qname_out(java.lang.String dist_qname_out) {
        this.dist_qname_out = dist_qname_out;
    }


    /**
     * Gets the host_qmanager value for this HeaderRQ.
     * 
     * @return host_qmanager
     */
    public java.lang.String getHost_qmanager() {
        return host_qmanager;
    }


    /**
     * Sets the host_qmanager value for this HeaderRQ.
     * 
     * @param host_qmanager
     */
    public void setHost_qmanager(java.lang.String host_qmanager) {
        this.host_qmanager = host_qmanager;
    }


    /**
     * Gets the host_qname_out value for this HeaderRQ.
     * 
     * @return host_qname_out
     */
    public java.lang.String getHost_qname_out() {
        return host_qname_out;
    }


    /**
     * Sets the host_qname_out value for this HeaderRQ.
     * 
     * @param host_qname_out
     */
    public void setHost_qname_out(java.lang.String host_qname_out) {
        this.host_qname_out = host_qname_out;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderRQ)) return false;
        HeaderRQ other = (HeaderRQ) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dist_qname_in==null && other.getDist_qname_in()==null) || 
             (this.dist_qname_in!=null &&
              this.dist_qname_in.equals(other.getDist_qname_in()))) &&
            ((this.dist_qname_out==null && other.getDist_qname_out()==null) || 
             (this.dist_qname_out!=null &&
              this.dist_qname_out.equals(other.getDist_qname_out()))) &&
            ((this.host_qmanager==null && other.getHost_qmanager()==null) || 
             (this.host_qmanager!=null &&
              this.host_qmanager.equals(other.getHost_qmanager()))) &&
            ((this.host_qname_out==null && other.getHost_qname_out()==null) || 
             (this.host_qname_out!=null &&
              this.host_qname_out.equals(other.getHost_qname_out())));
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
        if (getDist_qname_in() != null) {
            _hashCode += getDist_qname_in().hashCode();
        }
        if (getDist_qname_out() != null) {
            _hashCode += getDist_qname_out().hashCode();
        }
        if (getHost_qmanager() != null) {
            _hashCode += getHost_qmanager().hashCode();
        }
        if (getHost_qname_out() != null) {
            _hashCode += getHost_qname_out().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderRQ.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "HeaderRQ"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dist_qname_in");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "dist_qname_in"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dist_qname_out");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "dist_qname_out"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("host_qmanager");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "host_qmanager"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("host_qname_out");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "host_qname_out"));
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
