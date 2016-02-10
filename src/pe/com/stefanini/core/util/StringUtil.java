package pe.com.stefanini.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.apache.commons.lang.SerializationUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import pe.com.bbva.iipf.util.Constantes;

public class StringUtil {

	public boolean findStringInArray(String cadenaABuscar, String[] cadenas) {
		boolean respuesta = false;
		if (cadenas != null) {
			for (int i = 0; i < cadenas.length; i++) {
				if (cadenas[i] != null && !cadenas[i].trim().equals("")
						&& cadenas[i].trim().equals(cadenaABuscar)) {
					respuesta = true;
					break;
				}
			}
		}
		return respuesta;
	}
	
	/**
	 * Devuelve la traza de error de una excepción
	 * @param e Exception
	 * @return cadena de texto con la traza del error
	 */
	public static String getStackTrace(Exception e){
		StringWriter sr = new StringWriter(0);
        PrintWriter pw = new PrintWriter(sr,true);
        e.printStackTrace(pw);
        return sr.toString();
	}
	
	
	 public static String encrypt(String cadena,String clave) { 
	        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor(); 
	        s.setPassword(clave); 
	        return s.encrypt(cadena); 
	    } 
	 public static String encrypt(String cadena) { 
	        return encrypt(cadena,Constantes.CLAVE_ENCRYPT); 
	    }

	 public static String decrypt(String cadena,String clave) { 
	        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor(); 
	        s.setPassword(clave); 
	        String devuelve = ""; 
	        try { 
	            devuelve = s.decrypt(cadena); 
	        } catch (Exception e) { 
	        } 
	        return devuelve; 
	    } 
	 public static String decrypt(String cadena) { 
	        return decrypt(cadena,Constantes.CLAVE_ENCRYPT); 
	    }
	 
		public static void serializaObjeto(String nombreArchivo, Object objeto)
				throws Exception {
			FileOutputStream archivo = new FileOutputStream(nombreArchivo);
			SerializationUtils.serialize((Serializable) objeto, archivo);
		}
		
		public static Object deserializaObjeto(String nombreArchivo)
				throws Exception {
			FileInputStream archivo = new FileInputStream(nombreArchivo);
			return SerializationUtils.deserialize(archivo);
		}
}
