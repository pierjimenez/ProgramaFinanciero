/**
 * ConsultaGruposEconomicoRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class ConsultaGruposEconomicoRequest  implements java.io.Serializable {
    private grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera;

    private java.lang.String codigoGrupoEconomico;

    public ConsultaGruposEconomicoRequest() {
    }

    public ConsultaGruposEconomicoRequest(
           grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera,
           java.lang.String codigoGrupoEconomico) {
           this.cabecera = cabecera;
           this.codigoGrupoEconomico = codigoGrupoEconomico;
    }


    /**
     * Gets the cabecera value for this ConsultaGruposEconomicoRequest.
     * 
     * @return cabecera
     */
    public grupobbva.pe.com.EnlaceBBVA.Cabecera getCabecera() {
        return cabecera;
    }


    /**
     * Sets the cabecera value for this ConsultaGruposEconomicoRequest.
     * 
     * @param cabecera
     */
    public void setCabecera(grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera) {
        this.cabecera = cabecera;
    }


    /**
     * Gets the codigoGrupoEconomico value for this ConsultaGruposEconomicoRequest.
     * 
     * @return codigoGrupoEconomico
     */
    public java.lang.String getCodigoGrupoEconomico() {
        return codigoGrupoEconomico;
    }


    /**
     * Sets the codigoGrupoEconomico value for this ConsultaGruposEconomicoRequest.
     * 
     * @param codigoGrupoEconomico
     */
    public void setCodigoGrupoEconomico(java.lang.String codigoGrupoEconomico) {
        this.codigoGrupoEconomico = codigoGrupoEconomico;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaGruposEconomicoRequest)) return false;
        ConsultaGruposEconomicoRequest other = (ConsultaGruposEconomicoRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cabecera==null && other.getCabecera()==null) || 
             (this.cabecera!=null &&
              this.cabecera.equals(other.getCabecera()))) &&
            ((this.codigoGrupoEconomico==null && other.getCodigoGrupoEconomico()==null) || 
             (this.codigoGrupoEconomico!=null &&
              this.codigoGrupoEconomico.equals(other.getCodigoGrupoEconomico())));
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
        if (getCabecera() != null) {
            _hashCode += getCabecera().hashCode();
        }
        if (getCodigoGrupoEconomico() != null) {
            _hashCode += getCodigoGrupoEconomico().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaGruposEconomicoRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposEconomicoRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cabecera");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cabecera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "cabecera"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoGrupoEconomico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoGrupoEconomico"));
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
