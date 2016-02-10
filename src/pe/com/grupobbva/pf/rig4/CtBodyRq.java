/**
 * CtBodyRq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.pf.rig4;

public class CtBodyRq  implements java.io.Serializable {
    private java.lang.String codCentral;

    private java.lang.String codEjecutivoCuenta;

    private java.lang.String codOficinaAlta;

    public CtBodyRq() {
    }

    public CtBodyRq(
           java.lang.String codCentral,
           java.lang.String codEjecutivoCuenta,
           java.lang.String codOficinaAlta) {
           this.codCentral = codCentral;
           this.codEjecutivoCuenta = codEjecutivoCuenta;
           this.codOficinaAlta = codOficinaAlta;
    }


    /**
     * Gets the codCentral value for this CtBodyRq.
     * 
     * @return codCentral
     */
    public java.lang.String getCodCentral() {
        return codCentral;
    }


    /**
     * Sets the codCentral value for this CtBodyRq.
     * 
     * @param codCentral
     */
    public void setCodCentral(java.lang.String codCentral) {
        this.codCentral = codCentral;
    }


    /**
     * Gets the codEjecutivoCuenta value for this CtBodyRq.
     * 
     * @return codEjecutivoCuenta
     */
    public java.lang.String getCodEjecutivoCuenta() {
        return codEjecutivoCuenta;
    }


    /**
     * Sets the codEjecutivoCuenta value for this CtBodyRq.
     * 
     * @param codEjecutivoCuenta
     */
    public void setCodEjecutivoCuenta(java.lang.String codEjecutivoCuenta) {
        this.codEjecutivoCuenta = codEjecutivoCuenta;
    }


    /**
     * Gets the codOficinaAlta value for this CtBodyRq.
     * 
     * @return codOficinaAlta
     */
    public java.lang.String getCodOficinaAlta() {
        return codOficinaAlta;
    }


    /**
     * Sets the codOficinaAlta value for this CtBodyRq.
     * 
     * @param codOficinaAlta
     */
    public void setCodOficinaAlta(java.lang.String codOficinaAlta) {
        this.codOficinaAlta = codOficinaAlta;
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
            ((this.codCentral==null && other.getCodCentral()==null) || 
             (this.codCentral!=null &&
              this.codCentral.equals(other.getCodCentral()))) &&
            ((this.codEjecutivoCuenta==null && other.getCodEjecutivoCuenta()==null) || 
             (this.codEjecutivoCuenta!=null &&
              this.codEjecutivoCuenta.equals(other.getCodEjecutivoCuenta()))) &&
            ((this.codOficinaAlta==null && other.getCodOficinaAlta()==null) || 
             (this.codOficinaAlta!=null &&
              this.codOficinaAlta.equals(other.getCodOficinaAlta())));
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
        if (getCodCentral() != null) {
            _hashCode += getCodCentral().hashCode();
        }
        if (getCodEjecutivoCuenta() != null) {
            _hashCode += getCodEjecutivoCuenta().hashCode();
        }
        if (getCodOficinaAlta() != null) {
            _hashCode += getCodOficinaAlta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtBodyRq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/rig4/", "ctBodyRq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codCentral");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/rig4/", "codCentral"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codEjecutivoCuenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/rig4/", "codEjecutivoCuenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codOficinaAlta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/pf/rig4/", "codOficinaAlta"));
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
