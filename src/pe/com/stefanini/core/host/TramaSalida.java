/*
 * Created on 10/05/2010
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pe.com.stefanini.core.host;

import java.util.ArrayList;

import com.stefanini.pe.framework.collections.StringCollection;
import com.stefanini.pe.framework.common.StringUtil;
import com.stefanini.pe.framework.to.FBoolean;
import com.stefanini.pe.framework.to.FString;

/**
 * @author rmatos
 *
 *  To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TramaSalida {
	private String sSession;        
    private int iTamanioIdentificadorOne;
    private int iTamanioIdentificadorTwo;
    private StringCollection sCampo;
    private ArrayList sDetalle;
    private ArrayList sDetalleGeneral;

    public TramaSalida(int iTamanioIdentificadorOne, int iTamanioIdentificadorTwo)
    {
        sSession = StringUtil.Empty;
        this.iTamanioIdentificadorOne = iTamanioIdentificadorOne;
        this.iTamanioIdentificadorTwo = iTamanioIdentificadorTwo;            
        sDetalleGeneral = new ArrayList();
        sCampo = null;
        sDetalle = null;
    }

    public StringCollection ObtenerCampos() {
        return sCampo;
    }       

    public ArrayList ObtenerDetalleGeneral()
    {
        return sDetalleGeneral;
    }

    public String ObtenerSession()
    {
        return sSession; 
    }

    public void ObtenerFormateadoValidarUsuario(String sTramaRecibida, FString sMensaje, FBoolean bTodoOk)
    {
        try
        {
            if (UtilHost.ValidarTrama(sTramaRecibida, sMensaje))
            {
                String[] sCadena = StringUtil.split(sTramaRecibida,Constantes.CARACTER_SEPARADOR);
                if (sCadena.length == 4)
                {
                    this.sSession = sCadena[3];
                    bTodoOk.setValue(true);
                }
                else
                {
                    bTodoOk.setValue(false);
                    sMensaje.setValue("La trama no contiene datos de salida.");
                }
            }
        }
        catch (Exception ex)
        {
            bTodoOk.setValue(false);
            sMensaje.setValue(ex.getMessage());
        }
    }
            
    public void ObtenerFormateado(String sTramaRecibida, FString sMensaje, FBoolean bTodoOk)
    {
        try
        {
            if (UtilHost.ValidarTrama(sTramaRecibida, sMensaje))
            {
                String[] sCadena = StringUtil.split(sTramaRecibida,Constantes.CARACTER_SEPARADOR);
                if (sCadena.length == 4)
                {
                    String[] sContenido = StringUtil.split(sCadena[3],Constantes.CARACTER_SEPARADOR_FILAS);
                    this.sSession = sCadena[0];
                    int contadorDetalle = 0;
                    sCampo = new StringCollection();

                    String sElememtoOne = StringUtil.Empty;
                    String sElememtoOneAnterior = StringUtil.Empty;
                    String sElememtoTwo = StringUtil.Empty;
                    boolean bArrayNuevo = false;
                    bArrayNuevo = true;
                    StringCollection sItemDetalle = null;

                    for (int i = 0; i < sContenido.length; i++)
                    {
                        sElememtoOne = sContenido[i].substring(0, this.iTamanioIdentificadorOne);
                        sElememtoTwo = sContenido[i].substring(this.iTamanioIdentificadorOne, this.iTamanioIdentificadorOne + this.iTamanioIdentificadorTwo);
                        if (sElememtoTwo.equals(StringUtil.padLeft("0",this.iTamanioIdentificadorTwo, "0")))
                        {
                            //Elemento Simple
                            if (contadorDetalle > 0)
                            {
                                sDetalle.add(sItemDetalle);
                                contadorDetalle = 0;
                                bArrayNuevo = true;
                                if (sItemDetalle != null)
                                {
                                    if (sDetalle != null)
                                    {
                                        this.sDetalleGeneral.add(sDetalle);
                                    }
                                }
                            }                              
                            sCampo.add(sContenido[i].substring(this.iTamanioIdentificadorOne + this.iTamanioIdentificadorTwo).trim());
                            sDetalle = null;
                            sItemDetalle = null;
                        }
                        else
                        {
                            //Elemento Array                                                                
                            if (sElememtoTwo.equals(StringUtil.padLeft("1",this.iTamanioIdentificadorTwo, "0")))
                            {
                                if (sElememtoOneAnterior.equals(sElememtoOne))
                                {
                                    bArrayNuevo = false;
                                    if (sItemDetalle != null && contadorDetalle != 0)
                                    {
                                        contadorDetalle = 0;
                                        if (sDetalle == null)
                                        {
                                            sDetalle = new ArrayList();
                                        }
                                        sDetalle.add(sItemDetalle);                                          
                                    } 
                                }
                                else
                                {
                                    bArrayNuevo = true;
                                    if (sDetalle != null)
                                    {
                                        if (sItemDetalle != null && contadorDetalle != 0)
                                        {
                                            sDetalle.add(sItemDetalle);
                                        } 
                                        this.sDetalleGeneral.add(sDetalle);
                                        sDetalle = null;
                                    }                                        
                                }
                                contadorDetalle = 0;
                                sItemDetalle = new StringCollection();
                                contadorDetalle += 1;
                                sItemDetalle.add(sContenido[i].substring(this.iTamanioIdentificadorOne + this.iTamanioIdentificadorTwo).trim());
                                //Primer Elemento del Array   
                                sElememtoOneAnterior = sElememtoOne;
                            }
                            else
                            {
                                //No es el primer Elemento del Array
                                contadorDetalle += 1;
                                sItemDetalle.add(sContenido[i].substring(this.iTamanioIdentificadorOne + this.iTamanioIdentificadorTwo).trim());
                            }
                        }
                    }
                    if (contadorDetalle > 0)
                    {
                        if (sItemDetalle!=null)
                        {
                            if (sDetalle == null)
                            {
                                sDetalle = new ArrayList();                             
                            }   
                            sDetalle.add(sItemDetalle);                            
                        }                            
                        contadorDetalle = 0;
                        bArrayNuevo = true;                            
                        if (sDetalle != null)
                        {
                            this.sDetalleGeneral.add(sDetalle);
                        }                            
                    }                  
                    bTodoOk.setValue(true);
                }
                else
                {
                    bTodoOk.setValue(false);
                    sMensaje.setValue("La trama no contiene datos de salida.");
                }
            }
        }
        catch (Exception ex)
        {
            bTodoOk.setValue(false);
            sMensaje.setValue(ex.getMessage());
        }
    }
}
