/**
 * CtConSpfiPEC6Rs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.pf.pec6;

public class CtConSpfiPEC6Rs  implements java.io.Serializable {
    private pe.com.grupobbva.pf.pec6.CtHeaderRs header;

    private pe.com.grupobbva.pf.pec6.CtBodyRs data;

    public CtConSpfiPEC6Rs() {
    }

    public CtConSpfiPEC6Rs(
           pe.com.grupobbva.pf.pec6.CtHeaderRs header,
           pe.com.grupobbva.pf.pec6.CtBodyRs data) {
           this.header = header;
           this.data = data;
    }


    /**
     * Gets the header value for this CtConSpfiPEC6Rs.
     * 
     * @return header
     */
    public pe.com.grupobbva.pf.pec6.CtHeaderRs getHeader() {
        return header;
    }


    /**
     * Sets the header value for this CtConSpfiPEC6Rs.
     * 
     * @param header
     */
    public void setHeader(pe.com.grupobbva.pf.pec6.CtHeaderRs header) {
        this.header = header;
    }


    /**
     * Gets the data value for this CtConSpfiPEC6Rs.
     * 
     * @return data
     */
    public pe.com.grupobbva.pf.pec6.CtBodyRs getData() {
        return data;
    }


    /**
     * Sets the data value for this CtConSpfiPEC6Rs.
     * 
     * @param data
     */
    public void setData(pe.com.grupobbva.pf.pec6.CtBodyRs data) {
        this.data = data;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CtConSpfiPEC6Rs)) return false;
        CtConSpfiPEC6Rs other = (CtConSpfiPEC6Rs) obj;
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
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData())));
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
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtConSpfiPEC6Rs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctConSpfiPEC6Rs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "Header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctHeaderRs"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctBodyRs"));
        elemField.setMinOccurs(0);
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
