/*
 * Created on 08/10/2009
 *
 */
package pe.com.stefanini.core.host;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.stefanini.pe.framework.common.HelperDate;
import com.stefanini.pe.framework.common.StringUtil;

/**
 * @author Administrador
 *  
 */
public class Util { 
	public static boolean RetornaBusquedaAlfabetica(String sValor) {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("BusquedaAlfabetica");
			if(strCadena.equals(sValor)){
				return true;
			}else{
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean RetornaHabilitaAuditoria(String sValor) {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("HabilitarAuditoria");
			if(strCadena.equals(sValor)){
				return true;
			}else{
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean RetornaValidacionOficina(String sValor) {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("ValidacionOficina");
			if(strCadena.equals(sValor)){
				return true;
			}else{
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean RetornaValidacionTerritorio(String sValor) {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("ValidacionTerritorio");
			if(strCadena.equals(sValor)){
				return true;
			}else{
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean RetornaValidacionBanca(String sValor) {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("ValidacionBanca");
			if(strCadena.equals(sValor)){
				return true;
			}else{
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static String RetornaDireccionWEBService() {
		
		return Util.LeerConfig("WebService");
	} 
	
	public static String RetornaDireccionWEBServiceLDAP() {
		return Util.LeerConfig("WebServiceLDAP");
	}
	
	public static String RetornaCodigoSistema() {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("CodigoSistema");

		} catch (Exception ex) {
			strCadena = "error";
		}
		return strCadena;
	}
	
	public static boolean RetornaTestConColas() {
		try {
			if(Util.LeerConfig("TestConColas").equals("1"))
            {
				return true;
            }else{
            	return false;   	
            }
		} catch (Exception ex) {
			return false; 
		}		
	}
	
	public static boolean RetornaHabilitarDetalle() {
		try {
			if(Util.LeerConfig("HabilitarDetalle").equals("1"))
            {
				return true;
            }else{
            	return false;   	
            }
		} catch (Exception ex) {
			return false; 
		}		
	}
	
	public static String FormatoNumero(String strValor, int intEnteros, int intDecimales, boolean booCompletarEntero) throws Exception
    {
        try
        {
            String strNumero = "";
            String strDecimales = "";
            String strEntero = "";
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

	public static String RetornaAmbienteHOST() {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("Ambiente")+ Constantes.CARACTER_SEPARADOR +
					Util.LeerConfig("HostName")+ Constantes.CARACTER_SEPARADOR + 
					Util.LeerConfig("Puerto")+ Constantes.CARACTER_SEPARADOR +
					Util.LeerConfig("Canal") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("QueueManager") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaLocal") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaRemota") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaReply") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("KeyWinService") + Constantes.CARACTER_SEPARADOR+
					"1" + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("CanalMQI");

		} catch (Exception ex) {
			strCadena = "error";
		}

		return strCadena;
	}
	
	public static String RetornaAmbienteHOSTTest() {
		String strCadena = "";
		try {
			strCadena = Util.LeerConfig("Ambiente")+ Constantes.CARACTER_SEPARADOR +
					Util.LeerConfig("HostNameT")+ Constantes.CARACTER_SEPARADOR + 
					Util.LeerConfig("PuertoT")+ Constantes.CARACTER_SEPARADOR +
					Util.LeerConfig("CanalT") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("QueueManagerT") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaLocalT") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaRemotaT") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("ColaReplyT") + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("KeyWinServiceT") + Constantes.CARACTER_SEPARADOR+
					"1" + Constantes.CARACTER_SEPARADOR+
					Util.LeerConfig("CanalMQIT");

		} catch (Exception ex) {
			strCadena = "error";
		}

		return strCadena;
	}
	
	public static String LeerConfig(String strCadenaEntrada) {
		String strCadena = "";
		InputStream file = null;
		PropertiesReader reader = new PropertiesReader();
		try {

			strCadena = reader.getProperty(strCadenaEntrada);

		} catch (IOException ex) {
			strCadena = "error";
		} finally {
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException ex) {
			}
		}
		return strCadena;
	}

	/// <summary>
	/// Convierte la fecha enviada por Host (yyyy-MM-dd) al formato de la
	// aplicación web
	/// </summary>
	/// <param name="strFechaHost"></param>
	/// <returns></returns>
	public static String ConvertirFormatoFechaDesdeHost(String strFechaHost) {
		String strDiaHost = strFechaHost.substring(8, 8 + 2);
		String strMesHost = strFechaHost.substring(5, 5 + 2);
		String strAnnoHost = strFechaHost.substring(0, 4);
		;
		String strFechaAplicacion = strDiaHost + "/" + strMesHost + "/"
				+ strAnnoHost;
		return strFechaAplicacion;
	}

	public static String ConvertirFormatoFechaHaciaHost(String strFecha,
			String sTipoSeparador) {
		String[] sFecha = StringUtil.split(strFecha, "/");
		return (sFecha[2] + sTipoSeparador + sFecha[1] + sTipoSeparador + sFecha[0]);
	}

	public static void main(String args) {
		

	}
	public static Date RetornaFecha(String strCadenaFecha) {
		Date dt = null;
		try {
			dt = HelperDate.parseaDate(strCadenaFecha, "dd/MM/yyyy");
		} catch (Exception ex) {
			dt = null;
		}
		return dt;

	}
	
	public static String RetornaNombreTabla(String strFamilia) {
		String cadena = "";
		try {
			cadena = Util.LeerConfig(strFamilia);            
		} catch (Exception ex) {
			cadena = "error"; 
		}		
		return cadena;
	}
	
	public static String RetornaTecla() {
		String cadena = "";
		try {
			cadena = Util.LeerConfig("Tecla");            
		} catch (Exception ex) {
			cadena = "error"; 
		}		
		return cadena;
	}
	
	public static String formatearNumero(Number numero){		
		DecimalFormat df1 = new DecimalFormat("####.0000");
		if(numero != null){
			String numerof = df1.format(numero);
			int indice = numerof.indexOf(".");
			int contador = 0;
			String cadenaFinal="";
			for(int i=indice-1;i<=0;i--){
				cadenaFinal = numerof.charAt(i)+cadenaFinal;
				if(contador==3){
					cadenaFinal = ","+cadenaFinal;
				}
				contador++;
			}
			return numerof;
		}else{
			return "0.0000";
		}		
	}
	
	public static String ConvertirSentenciaSQL(List lstColumn, String nombreTabla, String format, BigDecimal idGrupoEconom){
		String cadena="";
        String inner="";
         for(int i=0;i<lstColumn.size();i++){
        	HashMap m = (HashMap)lstColumn.get(i);
        	if(m.get("NOMBRE_TABLA")==null){   		
	        		if(m.get("FORMATO_COLUMN")!=null){
	        			if(m.get("TIPO_COLUMN")!=null && m.get("TIPO_COLUMN").equals("NUMBER")){
	        				if(!format.equals("1"))
	        					cadena= cadena + "TO_CHAR(NC."+(String)m.get("NOMBRE_COLUMN")+"/"+format+",'"+(String)m.get("FORMATO_COLUMN")+"') AS \""+(String)m.get("NOMBRE_COLUMN")+"\" ,";
	        				else
	        					cadena= cadena + "TO_CHAR(NC."+(String)m.get("NOMBRE_COLUMN")+"/"+format+",'"+((String)m.get("FORMATO_COLUMN")).substring(0, ((String)m.get("FORMATO_COLUMN")).length()-3)+"') AS \""+(String)m.get("NOMBRE_COLUMN")+"\" ,";
	        			}else{
	        				cadena= cadena + "TO_CHAR(NC."+(String)m.get("NOMBRE_COLUMN")+",'"+(String)m.get("FORMATO_COLUMN")+"') AS \""+(String)m.get("NOMBRE_COLUMN")+"\" ,";
	        			}	            		
	            	}else{
	            		cadena = cadena + "NC."+(String)m.get("NOMBRE_COLUMN")+",";
	            	}
        	}else{
        		if(m.get("FORMATO_COLUMN")!=null){
        			if(m.get("TIPO_COLUMN")!=null && m.get("TIPO_COLUMN").equals("NUMBER")){
        				if(!format.equals("1"))
        					cadena = cadena +" TO_CHAR("+m.get("NOMBRE_TABLA")+"."+ m.get("COLUMNA_FORANEA")+"/"+format+",'"+(String)m.get("FORMATO_COLUMN")+"') AS \""+(String)m.get("COLUMNA_FORANEA")+"\",";
        				else
        					cadena = cadena +" TO_CHAR("+m.get("NOMBRE_TABLA")+"."+ m.get("COLUMNA_FORANEA")+"/"+format+",'"+((String)m.get("FORMATO_COLUMN")).substring(0, ((String)m.get("FORMATO_COLUMN")).length()-3)+"') AS \""+(String)m.get("COLUMNA_FORANEA")+"\",";
        			}else{
        				cadena = cadena +" TO_CHAR("+m.get("NOMBRE_TABLA")+"."+ m.get("COLUMNA_FORANEA")+",'"+(String)m.get("FORMATO_COLUMN")+"') AS \""+(String)m.get("COLUMNA_FORANEA")+"\",";
        			}
        		}else{
        			cadena = cadena +m.get("NOMBRE_TABLA")+"."+ m.get("COLUMNA_FORANEA")+" AS \""+(String)m.get("COLUMNA_FORANEA")+"\",";
            	}        		       		
        		inner = inner + " LEFT JOIN "+m.get("NOMBRE_TABLA")+" ON NC."+m.get("NOMBRE_COLUMN")+" = "+m.get("NOMBRE_TABLA")+"."+ m.get("NOMBRE_COLUMN");
        	}
        }
        //cadena = StringUtils.removeEnd(cadena,",");
         String query ="";
        if(idGrupoEconom!=null){
        	query = "SELECT tg.nombrecliente," + cadena + " NC.IDDIVISA FROM "+nombreTabla+" NC "+ inner+" LEFT JOIN tiipg_dimcliente_grupoeconom tg on TRIM(tg.codigocliente)= TRIM(NC.codcliente) WHERE NC.IDDIVISA!=-1 ";
        }else{
        	query = "SELECT " + cadena + " NC.IDDIVISA FROM "+nombreTabla+" NC "+ inner+" WHERE NC.IDDIVISA!=-1 ";
        }
		return query;
	}
	
	public static String formatDate(String fecha){

		String ano = fecha.substring(0,4);
		String mes = fecha.substring(5,7);
		String dia = fecha.substring(8);
		String dateout = dia+"/"+mes+"/"+ano;
		return dateout;
	}
	
	public static String formatDateDetalle(String fecha){
		
		int index = 0;
		String ano = "";
		String mes = "";
		String dia = "";
		index = fecha.indexOf("-");
		if(index == -1){
			ano = fecha.substring(0,4);
			mes = fecha.substring(4,6);
			dia = fecha.substring(6);
		}else{
			ano = fecha.substring(0,4);
			mes = fecha.substring(5,7);
			dia = fecha.substring(8);
		}
		String dateout = dia+"/"+mes+"/"+ano;
		return dateout;
	}
	
	public static String formatDateDetalle1(Date fecha){
		DateFormat formatter ; 
	    Date date ;    
	    formatter = new SimpleDateFormat("dd/MM/yyyy");    
	    String s = formatter.format(fecha);
		return s;
	}
	

	public static void acumulaTotales(String codDivisa,BigDecimal monto,HashMap rowsTotales){
		if(rowsTotales.get(codDivisa)!=null)
			rowsTotales.put(codDivisa,((new BigDecimal(rowsTotales.get(codDivisa).toString()).add(monto))));
		else
			rowsTotales.put(codDivisa,monto);
	}
	
	public static String formatCuenta(String cadena, int numCaracter){
		if(cadena.trim().length()>12){
			String resp="";
			for(int i=0;i<cadena.length();i++){			
				if(i==4){
					resp=resp+"-";
				}else if(i==8){
					resp=resp+"-";
				}else if(i==10){
					resp=resp+"-";
				}			
				resp = resp + cadena.charAt(i);
			}
			return resp;
		}
		else{
			return cadena;
		}
	}
	
	public static List cortar(String cadena){
		List cadenaArray = new ArrayList();
		if(cadena.length()>20){
			int contador = 0;
			String cadenas = "";
			cadenaArray.add(cadena.substring(0,20));
			if(cadena.length()>40){
				cadenaArray.add(cadena.substring(20,40));
				cadenaArray.add(cadena.substring(40,cadena.length()));
			}else{
				cadenaArray.add(cadena.subSequence(20,cadena.length()));
				cadenaArray.add("");
			}
		}else{
			cadenaArray.add(cadena);
			cadenaArray.add("");
			cadenaArray.add("");
		}
		return cadenaArray;
	}
	
	public static String retornaTipoDoc(String codDoc){
		String descripcion = "";
		if(codDoc.equalsIgnoreCase("D")){
			descripcion = "CARNÉT DIPLOMÁTICO";
		}else if(codDoc.equalsIgnoreCase("E")){
			descripcion = "CARNÉT EXTRANJERIA";
		}else if(codDoc.equalsIgnoreCase("L")){
			descripcion = "D.N.I";
		}else if(codDoc.equalsIgnoreCase("M")){
			descripcion = "CARNÉT DE IDENTIFICACIÓN MILITAR";
		}else if(codDoc.equalsIgnoreCase("P")){
			descripcion = "PASAPORTE";
		}else if(codDoc.equalsIgnoreCase("R")){
			descripcion = "R.U.C";
		}else if(codDoc.equalsIgnoreCase("T")){
			descripcion = "CARNÉT DE FUERZAS POLICIALES";
		}
		return descripcion;
	}
	
	public static String repeat(String str, int longitud){
		StringBuffer res = new StringBuffer();
		for(int i=0;i<longitud;i++)
			res.append(str);
		return res.toString();
	} 
	
	 public static String numeroDigitos(String numero, int digitos){
			if(numero!=null){
				if(numero.length()<digitos){
					numero = repeat("0",digitos - numero.length()) + numero;
				}
			}
			return numero;
		}
	 
	 public static boolean retornaConsultaPG6(){
		 try {
				if(Util.LeerConfig("ValidacionPG").equals("1"))
	            {
					return true;
	            }else{
	            	return false;   	
	            }
			} catch (Exception ex) {
				return false; 
			}
	 }
	 
	 public static String obtenerCadenaHTMLValidada(String mensaje,boolean escaparHTML){
			try{
				if(mensaje==null||mensaje.equals("")) return "";
				if(escaparHTML){
					StringEscapeUtils seu=new StringEscapeUtils();
					mensaje=seu.escapeHtml(mensaje);
				}
				int tamanio=mensaje.length();
				
				InputStream IS=new StringBufferInputStream(mensaje);//con.getInputStream();
				ByteArrayOutputStream OS = new ByteArrayOutputStream();
				Tidy T =new Tidy();
				T.setXHTML(true);
				T.setPrintBodyOnly(true);
				T.setShowWarnings(true);	
				//T.setShowErrors(0);
				Document document=T.parseDOM(IS,OS);
				String salida=OS.toString();
				if(salida.length()==0){ 
					throw new Exception("Problema de conversion");
				}else{
//					if (!validarRenderBlob(salida)){
//						throw new Exception("Problema de conversion. ITextRenderer..");
//					}
				}
				//System.out.print(salida);
				return trimSaltoLinea(salida);
			}catch (Exception e) {
				e.printStackTrace();
				return "<span style=\"color:red\"><h3>Problema al Exportar, esta sección del contenido no tiene el formato adecuado, por favor cambiar a un formato adecuado, sugerimos copiar el contenido a un word y volverlo a pegar<h3></span>";
			}
		}
	 
		public static boolean validarRenderBlob(String OS){			
			
			boolean booresultado = true; 	        
			try {							
				String cadenahtml ="";
				
			 	   String css= "<style type=\"text/css\">"+ 	
			 		 	   	" table.gridtable {" +
			 		 	    " width: 100%;" +
			 		 	   	" font-family: verdana,arial,sans-serif;"+
			 				" font-size:8px;" +
			 				" color:#333333;"+
			 				" border-width: 0.01em;" +
			 				" border-color: #666666;" +
			 				" border-collapse: collapse;" +	
			 				" margin: 1em 0; " +
			 				" page-break-inside:auto; page-break-inside:avoid; " +		
			 				" }" +
			 				" table.gridtable tr {" +
			 				" page-break-inside:avoid; page-break-after:auto" +
			 				" }" +
			 				" table.gridtable th {" +
			 				" border-width: 0.01em;" +
			 				" padding: 5px;" +
			 				" border-style: solid;" +
			 				" color:#333333;" +
			 				" border-color: #666666;" +
			 				" background-color: #A8C1D5;" +
			 				" border: 0.01em solid #000000;padding: .5em .5em;"+
			 				" }" +
			 				" table.gridtable td {" +
			 				" border-width: 0.01em;" +
			 				" padding: 1px;" +
			 				" border-style: solid;" +
			 				" border-color: #666666;" +	
			 				" background-color: #ffffff;" +	
			 				" border: 0.01em solid #000000;padding: .5em .5em;"+
			 				" }" +
			 				" </style>"; 				
				    
				    StringBuilder sbcomentario= new StringBuilder();				    
				    String refe="<head> " + css +" </head>";
				    
				    sbcomentario.append( " <table class=\"gridtable\" width=\"100%\">" 
			        		+ " <tr>" 		        		
			        		+ " 	<td class=\"myEditor\">" + OS.toString() + "</td>"
			        		+ " </tr>"    
			        		+ " </table>");	
				    
				     cadenahtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
					 	 	   +" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
					 		    + "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
					 		   +"<html xmlns=\"http://www.w3.org/1999/xhtml\">"	    	
					 		   + refe + " <body>" + 
					 				    "<table align=\"center\">"+
					 			    		"<tr>"+
					 			    			"<td>"+
					 			    			"<div style=\"width: 1000px; overflow:hidden margin:0;padding:0px; \">"+//div para controlar desborde
					 			    			sbcomentario.toString()+
					 			    			"</div>"+
					 			    			"</td>"+
					 		    			"</tr>"+
					 					"</table>"+
					 		    	
					 		    	" </body> </html> ";				        	
				       
				        ITextRenderer rendererTest = new ITextRenderer();        
				        rendererTest.setDocumentFromString(cadenahtml);
				        rendererTest.layout();
				        
		        } catch (Exception e) {	
		        	e.printStackTrace();
		        	booresultado=false;
				} 
			return booresultado;				        
		}
	 
		public static StringBuilder obtenerCadenaHTMLValidada(StringBuilder mensaje){
			return new StringBuilder(obtenerCadenaHTMLValidada(mensaje.toString()));
		}
		/*
		public static String trimSaltoLinea(String html){
			return trimSaltoLineaAux(html,0);
		}
		public static String trimSaltoLineaAux(String html,int i){
			System.out.println("000.-"+html);			
			String salto1="<p><br/></p>".toLowerCase();//<p><br/></p>
			String espacioBlanco="&nbsp;<br/></p>".toLowerCase();//"&nbsp;<br/></p>"
			String espacioBlanco2="&nbsp;</p>".toLowerCase();//"&nbsp;<br/></p>"
			if(html.length()<10){return html;}
			String salida="";
			String cola=html.substring(html.length()-20,html.length()).toLowerCase();
			System.out.println("1.-"+cola);
			cola=cola.replace(" ","");
			cola=cola.replace("\n","");
			cola=cola.replace("\r","");
			System.out.println("2.-"+cola);
			System.out.println("3.-"+cola+"|"+espacioBlanco);			
			if(cola.equals(espacioBlanco)){
				String aux=html.substring(0,html.length()-20)+"\r\n<br /></ p>\r";//\r\n<br /></ p>\r
				System.out.println("3.1.-"+aux);
				return trimSaltoLineaAux(aux,i+1);
			}
			
			if(i==0){
				cola=html.substring(html.length()-11,html.length()).toLowerCase();
				System.out.println("4.-"+cola);
			 }else{
				 cola=html.substring(html.length()-10,html.length()).toLowerCase();
				 System.out.println("4.1.-"+cola);
			 }
			cola=cola.replace(" ","");
			cola=cola.replace("\n","");
			cola=cola.replace("\r","");
			System.out.println("5.-"+cola+"|"+espacioBlanco2);
			
			if(cola.equals(espacioBlanco2)){
				String aux=html.substring(0,html.length()-10);//\r\n<br /></ p>\r
				System.out.println("6.-"+aux);
				String espa=aux.substring(aux.length()-6, aux.length());
				System.out.println("7.-"+espa);
				System.out.println("8.-"+espa+"|"+"&nbsp;");
				if(espa.equals("&nbsp;")){
					System.out.println("8.1.- INGRESO");
					return trimSaltoLineaAux(aux+"</p>",i+1);
				}else{
					if(i==0){						
						aux=html.substring(0,html.length()-15);
						System.out.println("8.2.-"+aux);
					}else{
						aux=html.substring(0,html.length()-11);
						System.out.println("8.3.-"+aux);
					}
					System.out.println("9.-"+aux);
					return trimSaltoLineaAux(aux,i+1);
				}
			}
			System.out.println("10.-"+html);

			if(html.length()<21){return html;}
			if(i==1){
				cola=html.substring(html.length()-19,html.length()).toLowerCase();
				System.out.println("11.-"+cola);

			}
			else{
				cola=html.substring(html.length()-21,html.length()).toLowerCase();
				System.out.println("12.-"+cola);

			}
			cola=cola.replace(" ","");
			cola=cola.replace("\n","");
			cola=cola.replace("\r","");
			System.out.println("15.-"+cola);
			System.out.println("16.-"+cola+"|"+salto1);
			if(cola.equals(salto1)){
				String aux="";
				if(i==1){
					aux=html.substring(0,html.length()-19);
					System.out.println("17.-"+aux);

				}else{
					aux=html.substring(0,html.length()-21);
					System.out.println("18.-"+aux);

				}
				return trimSaltoLineaAux(aux,0);	
			}
			System.out.println("19.-"+html);

			return html;
		}*/
		
		public static String obtenerCadenaHTMLValidada(String mensaje){
			return obtenerCadenaHTMLValidada(mensaje,false);
		}
		
		public static String[] cortarCadenaSepararEspacioBanco(String html,int i){
			String cola="";
			String cabeza="";
			if(i==0){
				cabeza=html.substring(0,html.length()-10);
				cola=html.substring(html.length()-10,html.length());
			}
			else{
				cabeza=html.substring(0,html.length()-11);
				cola=html.substring(html.length()-11,html.length());
			}
			cola=cola.replace(" ","");
			cola=cola.replace("\n","");
			cola=cola.replace("\r","");
			String[] aux=new String[2];
			aux[0]=cabeza;
			aux[1]=cola;
			return aux;
		}
		public static String[] cortarCadenaSepararSaltoLinea(String html,int i){
			String cola="";
			String cabeza="";
			if(i==0){
				cabeza=html.substring(0,html.length()-14);
				cola=html.substring(html.length()-13,html.length());
			}
			else{
				cabeza=html.substring(0,html.length()-13);
				cola=html.substring(html.length()-13,html.length());
			}
			cola=cola.replace(" ","");
			cola=cola.replace("\n","");
			cola=cola.replace("\r","");
			String[] aux=new String[2];
			aux[0]=cabeza;
			aux[1]=cola;
			return aux;
		}
		public static String trimSaltoLinea(String html){
			String salida=trimSaltoLineaAux(html,0);
			return salida;
		}
		public static String trimSaltoLineaAux(String html,int i){
			html=html.trim();
			//System.out.println("001.-"+html);
			String espacioBlanco="&nbsp;</p>".toLowerCase();//"&nbsp;<br/></p>"
			String saltoLinea="<p>&nbsp;</p>".toLowerCase();//"&nbsp;<br/></p>"
			//System.out.println("002.-"+html.length());
			if(html.length()<14){return html;}
			String[] dividido=cortarCadenaSepararSaltoLinea(html, i);;
			String cola=dividido[1];
			if(cola.equals(saltoLinea)){
				String aux=dividido[0];
				return trimSaltoLineaAux(aux.trim(),0);
			}
			dividido=cortarCadenaSepararEspacioBanco(html, i);;
			cola=dividido[1];		
			//System.out.println("003.-"+cola+"|"+espacioBlanco);		
			if(cola.equals(espacioBlanco)){
				String aux=dividido[0];//\r\n<br /></ p>\r
				//System.out.println("004.-"+aux);
				String espa=aux.substring(aux.length()-6, aux.length());
				//System.out.println("005.-"+espa);
				//System.out.println("006.-"+espa+"|"+"&nbsp;");
				if(espa.equals("&nbsp;")){//LINEA<
					//System.out.println("007.- INGRESO");
					String[] contenidoLimpio=limpiarSaltoLinea(aux,i);
					return trimSaltoLineaAux(contenidoLimpio[0],Integer.parseInt(contenidoLimpio[1]));
				}
			}
			return html;
		}

		private static String[] limpiarSaltoLinea(String html,int i) {
			String[] contenido=new String[2];
			if(html.endsWith("<p>&nbsp;")){
				contenido[0]=html.substring(0,html.length()-9).trim()+" ";
				contenido[1]="0";
				return contenido;
			}
			contenido[0]=html+" </p>";
			contenido[1]="1";
			return contenido;
		}
		
		public static int randInt(int min, int max) {

		    // NOTE: Usually this should be a field rather than a method
		    // variable so that it is not re-seeded every call.
		    Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = rand.nextInt((max - min) + 1) + min;

		    return randomNum;
		}

		
		
		
}

class PropertiesReader {

	/**
	 * Default Constructor
	 *  
	 */
	public void MyPropertiesReader() {

	}

	/**
	 * Some Method
	 * 
	 * @throws IOException
	 *  
	 */
	public String getProperty(String key) throws IOException {
		// Get the inputStream
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("websettings.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		String propValue = properties.getProperty(key);
		
		return propValue;

	}
	

	

}
