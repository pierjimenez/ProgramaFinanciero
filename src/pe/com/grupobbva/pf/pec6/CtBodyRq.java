/**
 * CtBodyRq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.pf.pec6;

public class CtBodyRq  implements java.io.Serializable {
    private java.lang.String numCliente;

    private java.lang.String control;

    public CtBodyRq() {
    }

    public CtBodyRq(
           java.lang.String numCliente,
           java.lang.String control) {
           this.numCliente = numCliente;
           this.control = control;
    }


    /**
     * Gets the numCliente value for this CtBodyRq.
     * 
     * @return numCliente
     */
    public java.lang.String getNumCliente() {
        return numCliente;
    }


    /**
     * Sets the numCliente value for this CtBodyRq.
     * 
     * @param numCliente
     */
    public void setNumCliente(java.lang.String numCliente) {
        this.numCliente = numCliente;
    }


    /**
     * Gets the control value for this CtBodyRq.
     * 
     * @return control
     */
    public java.lang.String getControl() {
        return control;
    }


    /**
     * Sets the control value for this CtBodyRq.
     * 
     * @param control
     */
    public void setControl(java.lang.String control) {
        this.control = control;
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
            ((this.numCliente==null && other.getNumCliente()==null) || 
             (this.numCliente!=null &&
              this.numCliente.equals(other.getNumCliente()))) &&
            ((this.control==null && other.getControl()==null) || 
             (this.control!=null &&
              this.control.equals(other.getControl())));
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
        if (getNumCliente() != null) {
            _hashCode += getNumCliente().hashCode();
        }
        if (getControl() != null) {
            _hashCode += getControl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtBodyRq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctBodyRq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "numCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("control");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "control"));
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
