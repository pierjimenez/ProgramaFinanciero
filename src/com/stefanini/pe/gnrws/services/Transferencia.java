/**
 * Transferencia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.stefanini.pe.gnrws.services;

public interface Transferencia extends java.rmi.Remote {
    public java.lang.String validarUsuarioPorTipo(java.lang.String strIdLogin, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
    public java.lang.String listaMenuGeneral(java.lang.String codSistema, java.lang.String strPermisos) throws java.rmi.RemoteException;
    public java.lang.String listaMenuGenerico(java.lang.String strIdLogin, java.lang.String sIdTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
    public java.lang.String enviaTramas(java.lang.String strIdLogin, java.lang.String strIdTrama, java.lang.String strTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
    public java.lang.String listaMenu(java.lang.String strIdLogin, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
    public java.lang.String listaMenuGenericoSinTecla(java.lang.String strIdLogin, java.lang.String sIdTrama, java.lang.String strTecla, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strSesion, java.lang.String strUsuario, java.lang.String strPassword, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
    public java.lang.String soloTrama(java.lang.String strIdTrama, java.lang.String strTrama, java.lang.String strAmbienteHOST, java.lang.String strIpCliente, java.lang.String strCodigoSistema) throws java.rmi.RemoteException;
}
