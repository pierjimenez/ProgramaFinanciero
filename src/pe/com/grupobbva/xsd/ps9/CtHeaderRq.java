/**
 * CtHeaderRq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.xsd.ps9;

public class CtHeaderRq  implements java.io.Serializable {
    private java.lang.String terminalLogico;

    private java.lang.String terminalContable;

    private java.lang.String usuario;

    private java.lang.String opcionAplicacion;

    public CtHeaderRq() {
    }

    public CtHeaderRq(
           java.lang.String terminalLogico,
           java.lang.String terminalContable,
           java.lang.String usuario,
           java.lang.String opcionAplicacion) {
           this.terminalLogico = terminalLogico;
           this.terminalContable = terminalContable;
           this.usuario = usuario;
           this.opcionAplicacion = opcionAplicacion;
    }


    /**
     * Gets the terminalLogico value for this CtHeaderRq.
     * 
     * @return terminalLogico
     */
    public java.lang.String getTerminalLogico() {
        return terminalLogico;
    }


    /**
     * Sets the terminalLogico value for this CtHeaderRq.
     * 
     * @param terminalLogico
     */
    public void setTerminalLogico(java.lang.String terminalLogico) {
        this.terminalLogico = terminalLogico;
    }


    /**
     * Gets the terminalContable value for this CtHeaderRq.
     * 
     * @return terminalContable
     */
    public java.lang.String getTerminalContable() {
        return terminalContable;
    }


    /**
     * Sets the terminalContable value for this CtHeaderRq.
     * 
     * @param terminalContable
     */
    public void setTerminalContable(java.lang.String terminalContable) {
        this.terminalContable = terminalContable;
    }


    /**
     * Gets the usuario value for this CtHeaderRq.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this CtHeaderRq.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the opcionAplicacion value for this CtHeaderRq.
     * 
     * @return opcionAplicacion
     */
    public java.lang.String getOpcionAplicacion() {
        return opcionAplicacion;
    }


    /**
     * Sets the opcionAplicacion value for this CtHeaderRq.
     * 
     * @param opcionAplicacion
     */
    public void setOpcionAplicacion(java.lang.String opcionAplicacion) {
        this.opcionAplicacion = opcionAplicacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CtHeaderRq)) return false;
        CtHeaderRq other = (CtHeaderRq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.terminalLogico==null && other.getTerminalLogico()==null) || 
             (this.terminalLogico!=null &&
              this.terminalLogico.equals(other.getTerminalLogico()))) &&
            ((this.terminalContable==null && other.getTerminalContable()==null) || 
             (this.terminalContable!=null &&
              this.terminalContable.equals(other.getTerminalContable()))) &&
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario()))) &&
            ((this.opcionAplicacion==null && other.getOpcionAplicacion()==null) || 
             (this.opcionAplicacion!=null &&
              this.opcionAplicacion.equals(other.getOpcionAplicacion())));
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
        if (getTerminalLogico() != null) {
            _hashCode += getTerminalLogico().hashCode();
        }
        if (getTerminalContable() != null) {
            _hashCode += getTerminalContable().hashCode();
        }
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        if (getOpcionAplicacion() != null) {
            _hashCode += getOpcionAplicacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtHeaderRq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "ctHeaderRq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalLogico");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "terminalLogico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalContable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "terminalContable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opcionAplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/xsd/ps9/", "opcionAplicacion"));
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
