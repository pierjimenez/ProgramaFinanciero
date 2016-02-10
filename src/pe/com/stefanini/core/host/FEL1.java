package pe.com.stefanini.core.host;


import com.stefanini.pe.framework.common.StringUtil;

/**
 * @author rmatos
 * 
 *  To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FEL1 {
	public FEL1() {

	}

	public static String RetornarNombre() {
		return "FEL1";
	}	  

	public static String Entrada(String sOpcion,
					             String sUsuario, 
					             String sClave, 
					             String sAplicacion) {
		
		StringBuffer sblTrama = new StringBuffer();		
		sblTrama.append("001000" + StringUtil.padRight(sOpcion.toUpperCase(),2," ") + Constantes.CARACTER_SEPARADOR);
		sblTrama.append("002000" + StringUtil.padRight(sUsuario.toUpperCase(),8," ") + Constantes.CARACTER_SEPARADOR);
		sblTrama.append("003000" + StringUtil.padRight(sClave,8," ") + Constantes.CARACTER_SEPARADOR);
		sblTrama.append("004000" + StringUtil.padRight(sAplicacion.toUpperCase(),2," "));
		
		return sblTrama.toString();
	}

}
