/**
 * BodyRQ.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.rating.ps9;

public class BodyRQ  implements java.io.Serializable {
    private java.lang.String OPCION;

    private java.lang.String HOST_RQ;

    private java.lang.String IH;

    private java.lang.String ME;

    public BodyRQ() {
    }

    public BodyRQ(
           java.lang.String OPCION,
           java.lang.String HOST_RQ,
           java.lang.String IH,
           java.lang.String ME) {
           this.OPCION = OPCION;
           this.HOST_RQ = HOST_RQ;
           this.IH = IH;
           this.ME = ME;
    }


    /**
     * Gets the OPCION value for this BodyRQ.
     * 
     * @return OPCION
     */
    public java.lang.String getOPCION() {
        return OPCION;
    }


    /**
     * Sets the OPCION value for this BodyRQ.
     * 
     * @param OPCION
     */
    public void setOPCION(java.lang.String OPCION) {
        this.OPCION = OPCION;
    }


    /**
     * Gets the HOST_RQ value for this BodyRQ.
     * 
     * @return HOST_RQ
     */
    public java.lang.String getHOST_RQ() {
        return HOST_RQ;
    }


    /**
     * Sets the HOST_RQ value for this BodyRQ.
     * 
     * @param HOST_RQ
     */
    public void setHOST_RQ(java.lang.String HOST_RQ) {
        this.HOST_RQ = HOST_RQ;
    }


    /**
     * Gets the IH value for this BodyRQ.
     * 
     * @return IH
     */
    public java.lang.String getIH() {
        return IH;
    }


    /**
     * Sets the IH value for this BodyRQ.
     * 
     * @param IH
     */
    public void setIH(java.lang.String IH) {
        this.IH = IH;
    }


    /**
     * Gets the ME value for this BodyRQ.
     * 
     * @return ME
     */
    public java.lang.String getME() {
        return ME;
    }


    /**
     * Sets the ME value for this BodyRQ.
     * 
     * @param ME
     */
    public void setME(java.lang.String ME) {
        this.ME = ME;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BodyRQ)) return false;
        BodyRQ other = (BodyRQ) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.OPCION==null && other.getOPCION()==null) || 
             (this.OPCION!=null &&
              this.OPCION.equals(other.getOPCION()))) &&
            ((this.HOST_RQ==null && other.getHOST_RQ()==null) || 
             (this.HOST_RQ!=null &&
              this.HOST_RQ.equals(other.getHOST_RQ()))) &&
            ((this.IH==null && other.getIH()==null) || 
             (this.IH!=null &&
              this.IH.equals(other.getIH()))) &&
            ((this.ME==null && other.getME()==null) || 
             (this.ME!=null &&
              this.ME.equals(other.getME())));
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
        if (getOPCION() != null) {
            _hashCode += getOPCION().hashCode();
        }
        if (getHOST_RQ() != null) {
            _hashCode += getHOST_RQ().hashCode();
        }
        if (getIH() != null) {
            _hashCode += getIH().hashCode();
        }
        if (getME() != null) {
            _hashCode += getME().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BodyRQ.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "BodyRQ"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OPCION");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "OPCION"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HOST_RQ");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "HOST_RQ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IH");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "IH"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ME");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "ME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
