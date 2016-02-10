/**
 * CtBodyRs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package pe.com.grupobbva.pf.pec6;

public class CtBodyRs  implements java.io.Serializable {
    private java.lang.String numCliente;

    private java.lang.String nombre;

    private java.lang.String codigo;

    private java.lang.String apellidoPaterno;

    private java.lang.String apellidoMaterno;

    private pe.com.grupobbva.pf.pec6.CtRela[] relaciones;

    private java.lang.String control;

    public CtBodyRs() {
    }

    public CtBodyRs(
           java.lang.String numCliente,
           java.lang.String nombre,
           java.lang.String codigo,
           java.lang.String apellidoPaterno,
           java.lang.String apellidoMaterno,
           pe.com.grupobbva.pf.pec6.CtRela[] relaciones,
           java.lang.String control) {
           this.numCliente = numCliente;
           this.nombre = nombre;
           this.codigo = codigo;
           this.apellidoPaterno = apellidoPaterno;
           this.apellidoMaterno = apellidoMaterno;
           this.relaciones = relaciones;
           this.control = control;
    }


    /**
     * Gets the numCliente value for this CtBodyRs.
     * 
     * @return numCliente
     */
    public java.lang.String getNumCliente() {
        return numCliente;
    }


    /**
     * Sets the numCliente value for this CtBodyRs.
     * 
     * @param numCliente
     */
    public void setNumCliente(java.lang.String numCliente) {
        this.numCliente = numCliente;
    }


    /**
     * Gets the nombre value for this CtBodyRs.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this CtBodyRs.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the codigo value for this CtBodyRs.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this CtBodyRs.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the apellidoPaterno value for this CtBodyRs.
     * 
     * @return apellidoPaterno
     */
    public java.lang.String getApellidoPaterno() {
        return apellidoPaterno;
    }


    /**
     * Sets the apellidoPaterno value for this CtBodyRs.
     * 
     * @param apellidoPaterno
     */
    public void setApellidoPaterno(java.lang.String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }


    /**
     * Gets the apellidoMaterno value for this CtBodyRs.
     * 
     * @return apellidoMaterno
     */
    public java.lang.String getApellidoMaterno() {
        return apellidoMaterno;
    }


    /**
     * Sets the apellidoMaterno value for this CtBodyRs.
     * 
     * @param apellidoMaterno
     */
    public void setApellidoMaterno(java.lang.String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }


    /**
     * Gets the relaciones value for this CtBodyRs.
     * 
     * @return relaciones
     */
    public pe.com.grupobbva.pf.pec6.CtRela[] getRelaciones() {
        return relaciones;
    }


    /**
     * Sets the relaciones value for this CtBodyRs.
     * 
     * @param relaciones
     */
    public void setRelaciones(pe.com.grupobbva.pf.pec6.CtRela[] relaciones) {
        this.relaciones = relaciones;
    }


    /**
     * Gets the control value for this CtBodyRs.
     * 
     * @return control
     */
    public java.lang.String getControl() {
        return control;
    }


    /**
     * Sets the control value for this CtBodyRs.
     * 
     * @param control
     */
    public void setControl(java.lang.String control) {
        this.control = control;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CtBodyRs)) return false;
        CtBodyRs other = (CtBodyRs) obj;
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
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.apellidoPaterno==null && other.getApellidoPaterno()==null) || 
             (this.apellidoPaterno!=null &&
              this.apellidoPaterno.equals(other.getApellidoPaterno()))) &&
            ((this.apellidoMaterno==null && other.getApellidoMaterno()==null) || 
             (this.apellidoMaterno!=null &&
              this.apellidoMaterno.equals(other.getApellidoMaterno()))) &&
            ((this.relaciones==null && other.getRelaciones()==null) || 
             (this.relaciones!=null &&
              java.util.Arrays.equals(this.relaciones, other.getRelaciones()))) &&
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
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getApellidoPaterno() != null) {
            _hashCode += getApellidoPaterno().hashCode();
        }
        if (getApellidoMaterno() != null) {
            _hashCode += getApellidoMaterno().hashCode();
        }
        if (getRelaciones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelaciones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelaciones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getControl() != null) {
            _hashCode += getControl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CtBodyRs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctBodyRs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "numCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellidoPaterno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "apellidoPaterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellidoMaterno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "apellidoMaterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "relaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "ctRela"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://grupobbva.com.pe/spfi/pec6/", "relacion"));
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
