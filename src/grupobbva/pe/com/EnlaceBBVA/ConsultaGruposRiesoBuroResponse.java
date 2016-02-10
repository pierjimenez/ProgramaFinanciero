/**
 * ConsultaGruposRiesoBuroResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package grupobbva.pe.com.EnlaceBBVA;

public class ConsultaGruposRiesoBuroResponse  implements java.io.Serializable {
    private grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera;

    private java.lang.String codigoGrupoRiesgoBuro;

    private java.lang.String descripcionGrupoRiesgoBuro;

    private java.lang.String codError;

    public ConsultaGruposRiesoBuroResponse() {
    }

    public ConsultaGruposRiesoBuroResponse(
           grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera,
           java.lang.String codigoGrupoRiesgoBuro,
           java.lang.String descripcionGrupoRiesgoBuro,
           java.lang.String codError) {
           this.cabecera = cabecera;
           this.codigoGrupoRiesgoBuro = codigoGrupoRiesgoBuro;
           this.descripcionGrupoRiesgoBuro = descripcionGrupoRiesgoBuro;
           this.codError = codError;
    }


    /**
     * Gets the cabecera value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @return cabecera
     */
    public grupobbva.pe.com.EnlaceBBVA.Cabecera getCabecera() {
        return cabecera;
    }


    /**
     * Sets the cabecera value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @param cabecera
     */
    public void setCabecera(grupobbva.pe.com.EnlaceBBVA.Cabecera cabecera) {
        this.cabecera = cabecera;
    }


    /**
     * Gets the codigoGrupoRiesgoBuro value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @return codigoGrupoRiesgoBuro
     */
    public java.lang.String getCodigoGrupoRiesgoBuro() {
        return codigoGrupoRiesgoBuro;
    }


    /**
     * Sets the codigoGrupoRiesgoBuro value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @param codigoGrupoRiesgoBuro
     */
    public void setCodigoGrupoRiesgoBuro(java.lang.String codigoGrupoRiesgoBuro) {
        this.codigoGrupoRiesgoBuro = codigoGrupoRiesgoBuro;
    }


    /**
     * Gets the descripcionGrupoRiesgoBuro value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @return descripcionGrupoRiesgoBuro
     */
    public java.lang.String getDescripcionGrupoRiesgoBuro() {
        return descripcionGrupoRiesgoBuro;
    }


    /**
     * Sets the descripcionGrupoRiesgoBuro value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @param descripcionGrupoRiesgoBuro
     */
    public void setDescripcionGrupoRiesgoBuro(java.lang.String descripcionGrupoRiesgoBuro) {
        this.descripcionGrupoRiesgoBuro = descripcionGrupoRiesgoBuro;
    }


    /**
     * Gets the codError value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @return codError
     */
    public java.lang.String getCodError() {
        return codError;
    }


    /**
     * Sets the codError value for this ConsultaGruposRiesoBuroResponse.
     * 
     * @param codError
     */
    public void setCodError(java.lang.String codError) {
        this.codError = codError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaGruposRiesoBuroResponse)) return false;
        ConsultaGruposRiesoBuroResponse other = (ConsultaGruposRiesoBuroResponse) obj;
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
            ((this.codigoGrupoRiesgoBuro==null && other.getCodigoGrupoRiesgoBuro()==null) || 
             (this.codigoGrupoRiesgoBuro!=null &&
              this.codigoGrupoRiesgoBuro.equals(other.getCodigoGrupoRiesgoBuro()))) &&
            ((this.descripcionGrupoRiesgoBuro==null && other.getDescripcionGrupoRiesgoBuro()==null) || 
             (this.descripcionGrupoRiesgoBuro!=null &&
              this.descripcionGrupoRiesgoBuro.equals(other.getDescripcionGrupoRiesgoBuro()))) &&
            ((this.codError==null && other.getCodError()==null) || 
             (this.codError!=null &&
              this.codError.equals(other.getCodError())));
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
        if (getCodigoGrupoRiesgoBuro() != null) {
            _hashCode += getCodigoGrupoRiesgoBuro().hashCode();
        }
        if (getDescripcionGrupoRiesgoBuro() != null) {
            _hashCode += getDescripcionGrupoRiesgoBuro().hashCode();
        }
        if (getCodError() != null) {
            _hashCode += getCodError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaGruposRiesoBuroResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", ">ConsultaGruposRiesoBuroResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cabecera");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cabecera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.pe.grupobbva/EnlaceBBVA/", "cabecera"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoGrupoRiesgoBuro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoGrupoRiesgoBuro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionGrupoRiesgoBuro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionGrupoRiesgoBuro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codError"));
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
