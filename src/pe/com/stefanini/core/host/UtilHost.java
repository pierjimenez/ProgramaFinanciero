/*
 * Created on 10/05/2010
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pe.com.stefanini.core.host;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.stefanini.pe.framework.collections.DataColumn;
import com.stefanini.pe.framework.collections.DataRow;
import com.stefanini.pe.framework.collections.DataTable;
import com.stefanini.pe.framework.collections.StringCollection;
import com.stefanini.pe.framework.common.Convert;
import com.stefanini.pe.framework.common.StringUtil;
import com.stefanini.pe.framework.to.FBoolean;
import com.stefanini.pe.framework.to.FString;
/**
 * @author rmatos
 *
 *  To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UtilHost {
	public static boolean ExisteErrorTrama(String sTrama, FString sError)
    {
        String[] sCadena = StringUtil.split(sTrama,Constantes.CARACTER_SEPARADOR);
        if (sCadena[1].compareTo("S") != 0)
        {
            sError.setValue(StringUtil.Empty);
            return false;
        }
        else
        {
            sError.setValue(sCadena[2]);
            return true;
        }
    }

    public static boolean ValidarTrama(String sTrama, FString sMensaje)
    {
        if (sTrama != null || sTrama.compareTo(StringUtil.Empty) != 0)
        {
            if (!ExisteErrorTrama(sTrama, sMensaje))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            sMensaje.setValue("No Hay Respuesta de Host.");
            return false;
        }
    }

    public static DataTable RetornaDataTable_Trama(ArrayList oListaTabla, StringCollection NombreCampos,boolean bGenerarNombreCampos, FString sMensaje, FBoolean bTodoOk)
    {
        DataTable dtTabla = new DataTable();
        StringCollection sFila;
        DataRow dr;
        try {
            if (oListaTabla.size() > 0)
            {
                sFila = (StringCollection)oListaTabla.get(0);
                if (sFila.size() == NombreCampos.size() || bGenerarNombreCampos==true)
                {
                    for (int i = 0; i < sFila.size(); i++)
                    {
                        if (bGenerarNombreCampos)
                        {
                            dtTabla.getColumns().add(new DataColumn("Columna"+ Convert.toString(i+1), "String"));
                        }
                        else {
                            dtTabla.getColumns().add(new DataColumn(NombreCampos.get(i), "String"));
                        }                            
                    }
                    for (int j = 0; j < oListaTabla.size(); j++)
                    {
                        dr = dtTabla.NewRow();
                        sFila = (StringCollection)oListaTabla.get(j);
                        for (int k = 0; k < NombreCampos.size(); k++)
                        {
                            dr.set(NombreCampos.get(k),sFila.get(k));
                        }
                        dtTabla.getRows().add(dr);
                    }
                    sMensaje.setValue(StringUtil.Empty);
                    bTodoOk.setValue(true);
                    return dtTabla;
                }
                else
                {
                    bTodoOk.setValue(false);
                    sMensaje.setValue("El número de columnas no coinciden. La tabla posee " + sFila.size() + ". Las columnas son " + NombreCampos.size());
                    return null;
                }
            }
            else
            {
                bTodoOk.setValue(false);
                sMensaje.setValue("La tabla se encuentra vacía.");
                return null;
            }
        }catch(Exception ex){
            bTodoOk.setValue(false);
            sMensaje.setValue(ex.getMessage());
            return null;
        }            
    }

    public static DataTable RetornaDataTable_Opciones(String strCadenaDevuelta, StringCollection NombreCampos, FBoolean blnPaginasAdicionales, FString strSiguienteRegistro, DataTable dtTablaOpciones_Acciones, FString strSesion, FString strNombreUsuario) throws Exception
    {
        String[] Campos;
        DataTable dtTabla;
        String[] Filas;
        String[] Columnas;
        DataRow drwFila;
        int i = 0;
        int j = 0;
        if (strCadenaDevuelta == null || strCadenaDevuelta.trim() == "")
        {
            throw new Exception(Constantes.MENSAJE_CADENA_DEVUELTA_VACIA);

        }
        Campos = StringUtil.split(strCadenaDevuelta,Constantes.CARACTER_SEPARADOR);
        if (Campos.length == 1)
        {
            throw new Exception(Campos[0]);
        }

        strSesion.setValue(Campos[0]);
        if (Campos[1] == "S")
        {
            //Hubo Error
            throw new Exception(Campos[2]);

        }
        //Creamos el DataTable con sus columnas
        dtTabla = new DataTable();
        for(Iterator it = NombreCampos.iterator();it.hasNext();)
        {	String strNombreColumnas = (String) it.next();
            dtTabla.getColumns().add(strNombreColumnas);
        }
        //Ahora Procesamos la data
        Filas = StringUtil.split(Campos[3],Constantes.CARACTER_SEPARADOR_FILAS);
        strNombreUsuario.setValue(Campos[5]);
        if (Campos[4] == "S")
        {
            blnPaginasAdicionales.setValue(true);
        }
        else
        {
            blnPaginasAdicionales.setValue(false);
        }
        strSiguienteRegistro.setValue(Campos[5]);
        for(int x1=0;x1<Filas.length;x1++)
        {
        	String strFila = Filas[x1];
            drwFila = dtTabla.NewRow();
            Columnas = StringUtil.split(strFila,Constantes.CARACTER_SEPARADOR_COLUMNAS);
            j = 0;
            for(int x2=0;x2<Columnas.length;x2++)
            {
            	String strColumna = Columnas[x2];
                drwFila.set(j,strColumna);
                j++;
            }
            dtTabla.getRows().add(drwFila);
        }
        ///Procesamos el dataTable Opciones y acciones
        dtTablaOpciones_Acciones = new DataTable();
        dtTablaOpciones_Acciones.getColumns().add("Opciones");
        dtTablaOpciones_Acciones.getColumns().add("Acciones");
        //Procesmos las Filas de las opciones y acciones

        Filas = StringUtil.split(Campos[6],Constantes.CARACTER_SEPARADOR_FILAS);
        for( int x1=0; x1<Filas.length;x1++)
        {	String strFila = Filas[x1];
            drwFila = dtTablaOpciones_Acciones.NewRow();
            Columnas = StringUtil.split(strFila,Constantes.CARACTER_SEPARADOR_COLUMNAS);
            j = 0;
            for(int x2=0; x2< Columnas.length;x2++)
            {
            	String strColumna = Columnas[x2];            	
                drwFila.set(j,strColumna);
                j++;
            }
            dtTablaOpciones_Acciones.getRows().add(drwFila);
        }
        return dtTabla;
    }

    public static DataTable RetornaDataTable_FormatearNumericos(DataTable dtEntrada, StringCollection ColumnasAConvertir) throws Exception
    {
        DataTable dtSalida = new DataTable();
        String nombreColumna = StringUtil.Empty;
        int posicionDecimalColumna = 0;
        String strValor = StringUtil.Empty;
        for (int k1=0;k1<dtEntrada.getRows().count();k1++)
        {
        	DataRow dr = dtEntrada.getRows().get(k1);
            for(int k2=0;k2<ColumnasAConvertir.size();k2++)
            {
            	String strColumnas = ColumnasAConvertir.get(k2);
                String[] strColumna = StringUtil.split(strColumnas,Constantes.CARACTER_SEPARADOR);
                nombreColumna = strColumna[0];
                posicionDecimalColumna = Convert.toInt32(strColumna[1]);
                strValor = Convert.toString(dr.get(strColumna[0]));
                if (strValor != "DEFECTO")
                {
                    if (strValor.length() < posicionDecimalColumna)
                    {
                        strValor = StringUtil.padLeft(strValor,posicionDecimalColumna + 1, "0");
                    }
                    strValor = StringUtil.insert(strValor,strValor.length() - posicionDecimalColumna, ".");
                    strValor = FormatoMoneda(strValor, posicionDecimalColumna, "");
                }                 
                dr.set(strColumna[0],strValor);
            }
        }
        return dtEntrada;
    }

    public static String FormatoMoneda(Object valor, int numeroDecimales, String simbolo)
    {
        String strDatoFormateado = "";

        NumberFormat nf = NumberFormat.getCurrencyInstance();

        if (IsNumeric(valor))
        {

            double numero = Convert.toDouble(valor).doubleValue();
            strDatoFormateado = nf.format(numero);
        }
        else
        {
            strDatoFormateado = Convert.toString(valor);
        }
        return strDatoFormateado;
    }

    public static boolean IsNumeric(Object Expression)
    {
        boolean isNum=false;
        double retNum;

        try{
        	retNum = Double.parseDouble(Expression.toString());
        	isNum=true;
			
        }catch(NumberFormatException ex)
		{ isNum = false;}
        
        return isNum;
    }

    public static String FormatoNumero(String strValor, int intEnteros, int intDecimales, boolean booCompletarEntero) throws Exception
    {
        try
        {
            String strNumero = StringUtil.Empty;
            String strDecimales = StringUtil.Empty;
            String strEntero = StringUtil.Empty;
            if (strValor.indexOf(".")!=-1)
            {
                strDecimales = strValor.substring(strValor.lastIndexOf(".") + 1);
                strEntero = strValor.substring(0, strValor.lastIndexOf("."));
            }
            else
            {
                strEntero = strValor;
            }

            if (strEntero.length() > intEnteros)
                throw new Exception("Error en entero");

            if (strDecimales.length() > intDecimales)
                throw new Exception("Error en decimales");

            if (intDecimales > 0)
                strDecimales = StringUtil.padRight(strDecimales,intDecimales, "0");
            if (booCompletarEntero)
            {
                strEntero = StringUtil.padLeft(strEntero,intEnteros, "0");
            }
            strNumero = strEntero + strDecimales;

            return strNumero;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public static String ConvertirFormatoFechaHaciaHost(String strFecha, String sTipoSeparador)
    {
        try
        {
            String[] sFecha = StringUtil.split(strFecha,"/");
            if (sFecha.length > 2)
            {
                return (sFecha[2] + sTipoSeparador + sFecha[1] + sTipoSeparador + sFecha[0]);
            }
            else
            {
                return "";
            }
        }
        catch (Exception ex)
        {
            return "";
        }
        
    }

    public static DataTable FormatearColumna(DataTable dtEntrada, String sColumna, int numeroDecimales) throws Exception
    {
        for(int i=0;i<dtEntrada.getRows().count();i++)
        {
        	DataRow dr = dtEntrada.getRows().get(i);
            dr.set(sColumna,QuitarDecimales(Convert.toString(dr.get(sColumna)), numeroDecimales));
        }
        return dtEntrada;
    }

    public static DataTable FormatearColumna(DataTable dtEntrada, StringCollection ColumnasAConvertir) throws Exception
    {
        String nombreColumna = StringUtil.Empty;
        int posicionDecimalColumna = 0;

        for (int k1 = 0; k1 < dtEntrada.getRows().count();k1++)        	
        {
        	DataRow dr = dtEntrada.getRows().get(k1);
            
        	for (int k2 = 0; k2 < ColumnasAConvertir.size(); k2++)
            {
        		String strColumnas = ColumnasAConvertir.get(k2);
                String[] strColumna = StringUtil.split(strColumnas,Constantes.CARACTER_SEPARADOR);
                nombreColumna = strColumna[0];
                posicionDecimalColumna = Convert.toInt32(strColumna[1]);
                dr.set(nombreColumna,QuitarDecimales(Convert.toString(dr.get(nombreColumna)), posicionDecimalColumna));
            }
        }
        return dtEntrada;
    }

    public static String QuitarDecimales(String sCadena, int nroDecimales)
    {
        try
        {
            sCadena = sCadena.trim();
            sCadena = sCadena.substring(0,sCadena.length() - (nroDecimales + 1));
            return sCadena;
        }
        catch (Exception ex)
        {
            return sCadena;
        }
    }
    
}
