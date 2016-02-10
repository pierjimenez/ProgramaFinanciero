/**
 * BodyRS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class BodyRS  implements java.io.Serializable {
    private java.lang.String tramaRS;

    private java.lang.String tramaRSBLOB;

    public BodyRS() {
    }

    public BodyRS(
           java.lang.String tramaRS,
           java.lang.String tramaRSBLOB) {
           this.tramaRS = tramaRS;
           this.tramaRSBLOB = tramaRSBLOB;
    }


    /**
     * Gets the tramaRS value for this BodyRS.
     * 
     * @return tramaRS
     */
    public java.lang.String getTramaRS() {
        return tramaRS;
    }


    /**
     * Sets the tramaRS value for this BodyRS.
     * 
     * @param tramaRS
     */
    public void setTramaRS(java.lang.String tramaRS) {
        this.tramaRS = tramaRS;
    }


    /**
     * Gets the tramaRSBLOB value for this BodyRS.
     * 
     * @return tramaRSBLOB
     */
    public java.lang.String getTramaRSBLOB() {
        return tramaRSBLOB;
    }


    /**
     * Sets the tramaRSBLOB value for this BodyRS.
     * 
     * @param tramaRSBLOB
     */
    public void setTramaRSBLOB(java.lang.String tramaRSBLOB) {
        this.tramaRSBLOB = tramaRSBLOB;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BodyRS)) return false;
        BodyRS other = (BodyRS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tramaRS==null && other.getTramaRS()==null) || 
             (this.tramaRS!=null &&
              this.tramaRS.equals(other.getTramaRS()))) &&
            ((this.tramaRSBLOB==null && other.getTramaRSBLOB()==null) || 
             (this.tramaRSBLOB!=null &&
              this.tramaRSBLOB.equals(other.getTramaRSBLOB())));
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
        if (getTramaRS() != null) {
            _hashCode += getTramaRS().hashCode();
        }
        if (getTramaRSBLOB() != null) {
            _hashCode += getTramaRSBLOB().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BodyRS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "BodyRS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramaRS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "tramaRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramaRSBLOB");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "tramaRSBLOB"));
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
