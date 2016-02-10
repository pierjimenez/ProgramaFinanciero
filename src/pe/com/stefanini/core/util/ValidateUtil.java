package pe.com.stefanini.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

	/**
	 * Valida montos con formato 12,222.00
	 * @param monto
	 * @return
	 */
	public static boolean validarImporte(String monto){
		int totalRes =  countMatches("^\\d{1,3}(,\\d{3})*((\\.){1}\\d{1,2})*$", 
						monto) ;
		if(totalRes == 0){
			return false;
		}
		return true;
	}
	/**
	 * Valida que solo se esten ingresando numeros
	 * @param numero
	 * @param longitud
	 * @return
	 */
	public boolean validarNumeroDigitos(String numero, int longitud){
		
		int totalRes =  countMatches("^\\d[^a-zA-Z\\W]*\\d*$", 
									numero) ;
		if(totalRes == 0){
			return false;
		}
		if(numero.length() != longitud){
			return false;
		}
		return true;
	}
	/**
	 * Método para validar el DNI retorna
	 * true si el numero de documento tiene un formato correcto
	 * y false en caso contrario
	 * @param numeroDNI
	 * @return
	 */
	public boolean validarDNI(String numeroDNI){
		
		int totalRes =  countMatches("^\\d[^a-zA-Z\\W]*\\d*$", 
									 numeroDNI) ;
		if(totalRes == 0){
			return false;
		}
		if(numeroDNI.length() != 8){
			return false;
		}
		return true;
	}
	
	public boolean validarRUC(String numeroRUC){
		
		int totalRes =  countMatches("^\\d[^a-zA-Z\\W]*\\d*$", 
									 numeroRUC) ;
		if(totalRes == 0){
			return false;
		}
		if(numeroRUC.length() != 11){
			return false;
		}
		return true;
	}
	/**
	 * Método que retorna en un array de Objetos todas las coincidencias 
	 * encontradas dentro de un texto
	 * E. Pomayay
	 * @param patron
	 * @param cad
	 * @return
	 */
	public static Object[] pregMatchs(String regex, 
							          String input){
		Pattern pattern = Pattern.compile(regex);
		Matcher matchers = pattern.matcher(input);
		List<String> items = new ArrayList<String>();
	    while(matchers.find()){	
	    	items.add(matchers.group());
	    }
		return items.toArray();
	}
	
	/**
	 * Método para encontrar la cantidad de coincidencias en función
	 * a un patron dado
	 * 
	 * @param cadCount
	 * @param texto
	 * @return
	 */
	public static int  countMatches(String regex, 
						            String input) {
		Pattern patron = Pattern.compile(regex);
		Matcher matcher = patron.matcher(input);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Metodo para remplazar todas las ocurencias encontradas
	 * dentro de un texto dado un patron
	 * 
	 * @param regex
	 * @param input
	 * @param replace
	 * @return
	 */
	public static String pregReplace(String regex, 
									 String input,
								 	 String replace){
		Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input); // get a matcher object
        input = m.replaceAll(replace);
		return input;
	}
	
	public static boolean isNumeric(String cadena) {
		try {
			//Integer.parseInt(cadena);
			Double.parseDouble(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
