package pe.com.stefanini.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.com.bbva.iipf.util.Util;

public class FormatUtil {
	
	private static DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();

	/**
	 * formatea una cantidad 48512,66 al formato  48,512.66
	 * @param monto
	 */
	public static String formatImporte(String monto){
		String entero = "";
		String decimal="";
		String nuevo="";
		if(!ValidateUtil.validarImporte(monto)){
			if(monto.trim().equals("")){
				return "00.00";
			}
			if(monto.indexOf(".")!=-1){
				entero = monto.substring(0,monto.indexOf("."));
				decimal = monto.substring(monto.indexOf("."));
			}else{
				entero = monto;
				decimal=".00";
			}
			int count=0;
			for(int i = entero.length()-1;i>=0;i--){
				nuevo=entero.charAt(i)+nuevo;
				count++;
				if(count%3==0 &&
				   i!=0){
					nuevo=","+nuevo;
				}
			}
		}else{
			nuevo = monto;
		}
		return nuevo+decimal;
	}
	
	/**
	 * Redondear a dos decimales
	 * @param d
	 * @return
	 */
	public static String roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("###,###.##");
    	return (twoDForm.format(d));
	}
	public static String roundTwoDecimalsPunto(double d) {
		String valorNum="";
		try{
	    	DecimalFormat twoDForm = new DecimalFormat("###,##0.00");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.00";
		}
    	return valorNum;
	}
	
	public static String roundSinDecimalsPunto(double d) {
		String valorNum="";
		try{
	    	DecimalFormat twoDForm = new DecimalFormat("###,###,##0");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0";
		}
    	return valorNum;
	}
	//ini MCG20111206
	/**
	 * Redondear a numero de decimales requerido
	 * @param d: valor decimal
	 * @param numeroDecimales: numero de decimales
	 * @return
	 */
	public static String roundDecimalsPunto(double d,int numeroDecimal) {
		String valorNum="";
		String strFormat="###,##0.00";
		try{
	    	if (numeroDecimal>0){
	    		 strFormat="###,##0" + "."+ repetirCaracter("0",numeroDecimal);
	    	}else if (numeroDecimal==0){
	    		 strFormat="###,##0";
	    	}else{
	    		strFormat="###,##0" + "."+ repetirCaracter("0",2);
	    	}			
			
			DecimalFormat twoDForm = new DecimalFormat(strFormat);
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.00";
		}
    	return valorNum;
	}
	
	public static String roundEntero(double d,int numeroDecimal) {
		String valorNum="";
		String strFormat="#########";
		try{
			
			DecimalFormat twoDForm = new DecimalFormat(strFormat);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="";
		}
    	return valorNum;
	}
	
	public static String roundDecimalsPuntoPorcentaje(double d,int numeroDecimal) {
		String valorNum="";
		String strFormat="###,##0.0%";
		try{  	
	    	if (numeroDecimal>0){
	    		 strFormat="###,##0" + "."+ repetirCaracter("0",numeroDecimal)+"%";
	    	}else if (numeroDecimal==0){
	    		 strFormat="###,##0%";
	    	}else{
	    		strFormat="###,##0" + "."+ repetirCaracter("0",1)+"%";
	    	}			
						
			DecimalFormat twoDForm = new DecimalFormat(strFormat);
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.0%";
		}
    	return valorNum;
	}
	
	public static String roundOneDecimalsPunto(double d) {
		String valorNum="";
		try{
	    	DecimalFormat twoDForm = new DecimalFormat("###,##0.0");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.0";
		}
    	return valorNum;
	}
	
	public static String roundthreeDecimalsPunto(double d) {
		String valorNum="";
		try{
	    	DecimalFormat twoDForm = new DecimalFormat("###,##0.000");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.000";
		}
    	return valorNum;
	}
	
	public static String roundEnteroDecimalsPunto(double d) {
		String valorNum="";
		try{
	    	DecimalFormat twoDForm = new DecimalFormat("###,##0");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0";
		}
    	return valorNum;
	}
	public static String roundOneDecimalsPuntoPorcentaje(double d) {
		String valorNum="";
		try{  	
					
			DecimalFormat twoDForm = new DecimalFormat("###,##0.0%");
	    	newSymbols.setDecimalSeparator('.');
	    	newSymbols.setGroupingSeparator(',');
	    	twoDForm.setDecimalFormatSymbols(newSymbols);
	    	valorNum=twoDForm.format(d);
		}catch(Exception ex){
			valorNum="0.0%";
		}
    	return valorNum;
	}
	
	public static String repetirCaracter(String caracter,int intNumRepeticiones){
		String strCadenaRepetida="";
		try{
			
			if (intNumRepeticiones>0){
				StringBuilder sb = new StringBuilder(intNumRepeticiones);				
				for(int i = 0; i < intNumRepeticiones; i++) {
				sb.append(caracter);
				}	
				strCadenaRepetida = sb.toString();	
			}else{
				strCadenaRepetida="";
			}
					
		}catch(Exception ex){
			strCadenaRepetida="";
		}
    	return strCadenaRepetida;
	}
	
	
	//fin MCG20111206
	/**
     * Redondea u double a un numero especifico de decimales
     *
     * @param valor a ser redondeado.
     * @param numero de decimales para redondear.
     * @return valor redondeado.
     */
    public static double round(double val, int places) {
		long factor = (long)Math.pow(10,places);
		val = val * factor;
		long tmp = Math.round(val);
		return (double)tmp / factor;
    }

    /**
     *  Redondea u float a un numero especifico de decimales
     *
     * @param valor a ser redondeado.
     * @param numero de decimales para redondear.
     * @return valor redondeado.
     */
    public static float round(float val, int places) {
    	return (float)round((double)val, places);
    }

    public static int getCharUTF(byte x){
    	return  x<0?x+256:x;
    	//return  x;
    }
    
	public static String FormatNumeroSinComa(String valor) {
		String d="0";
		 if (valor!=null){
			d=valor.replace(",", "");			 
		 }else{
			 d="0";
		 }    	
    	return d;
	}
	
	public static String FormatNumeroSinComaEmpty(String valor) {
		String d="0";
		 if (valor!=null && !(valor).equals("") ){
			d=valor.replace(",", "");			 
		 }else{
			 d="0";
		 }    	
    	return d;
	}
	
	/**
	 * Conversion usado para convertir numeros con notacion cientifica 
	 * a números decimales.
	 * @param valor
	 * @return
	 */
	public static String conversion(double valor)
  	{
      Locale.setDefault(Locale.US);
      DecimalFormat num = new DecimalFormat("#,###.##");
      return num.format(valor);
  	}

	/**
	 * esta funcion formatea numeros decimales a miles 
	 * @param valor
	 * @return
	 */
	public static String formatMiles(String valor){
//		int count = ValidateUtil.countMatches("^\\d{1,3}(,\\d{3})*((\\.){1}\\d+)*$", valor);
//		if(count>0){
//			int indexPunto =0;
//			indexPunto = valor.indexOf(".");
//			if(indexPunto>0){
//				return valor.substring(0,indexPunto+3);
//			}else{
//				return valor;
//			}
//		}else{
//			return valor;
//		}
		return valor;

	}
	public static String conversionNumberFormat(BigDecimal valor)
  	{
		NumberFormat  num = NumberFormat.getInstance(Util.getLocaleUS());
		return num.format(valor.longValue());
  	}
    
	public static void main(String[] args) {
		//System.out.println(roundTwoDecimalsPunto(9999978.888));
		double x = 9999978.888;
		float y = 9.87654f;
		double z;
		float w;

		z = round(x,2);
		//System.out.println(z);
		z = round(x,5);
		//System.out.println(z);

		//System.out.println();

		w = round(y,3);
		//System.out.println(w);
		w = round(y,0);
		//System.out.println(w);

		//System.out.println(formatImporte("00.00"));
	}
}
