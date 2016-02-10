package pe.com.bbva.iipf.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {
	
	 public static String isStringNull(Object o){
			if(o==null){
				return null;
			}else if(o.toString().trim().equals("")){
				return null;
			}else{
				return o.toString();
			}
		}
	 
	 public static String isString(Object o){
			if(o==null){
				return "";
			}else
				return o.toString();
		}
	 public static String addZerosToLeft(String numero,Integer valorMax){
			
			String numFormat="";
			
			if(numero.length() < valorMax.intValue()){
				//Rellenamos con ceros a la izquierda
				for(int i=numero.length();i<valorMax.intValue();i++){
					numFormat+="0";
				}
			}
			numFormat +=numero;
			
			return numFormat;
		}
	 public static  String removeCero(String obj){
		 int val=Integer.parseInt(obj);
		
		 return String.valueOf(val);
	 }
	 
	 public static List<String> separador(String valor,char separador){
		 
		 String	str="";
		 List<String> listaValor=new ArrayList<String>();
		 
		 for(int i=0;i<valor.length();i++){
			 
			 if(valor.charAt(i)!=separador){
				 str=str+valor.charAt(i);
			 }else{
				 listaValor.add(str);
				 str="";
			 }
		 }
		 
		 return listaValor;
	 }
	 
	 public static String getFormatoHora(Date date){
		 try{
			 SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			 return sdf.format(date);
		 }catch(Exception e){
			 return "00:00";
		 }
		 
	 }
	 
	 public static boolean prefijoArchivoCorrecto(String prefijoArchivo,String archivoFileName) {
		if(prefijoArchivo!=null && archivoFileName!=null && archivoFileName.indexOf(prefijoArchivo)==0)
			return true;
		return false;
	}
	 
	public static boolean extensionArchivoExcel(String archivoFileName) {
		String extension = archivoFileName.substring(archivoFileName.indexOf(".")+1,archivoFileName.length()); 
		if(extension!=null && extension.toLowerCase().equals("xlsx") || extension.toLowerCase().equals("xls")){
			return true;
		}
		return false;
	}
	
	public static boolean extensionArchivo(String archivoFileName, String ext) {
		String extension = archivoFileName.substring(archivoFileName.indexOf(".")+1,archivoFileName.length()); 
		if(extension!=null && extension.equals(ext)){
			return true;
		}
		return false;
	}
	
	public static boolean compareNameFile(String name, String nombreFile) {
		if(name!=null && name.equals(nombreFile)){
			return true;
		}
		return false;
	}
	
	public static boolean isFecha(Object valor) {
		boolean ok = false;
		if(valor!=null && !valor.toString().trim().isEmpty()){
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
			String strFecha = valor.toString();
			try {
				formatoDelTexto.setLenient(false);
				Date fech = formatoDelTexto.parse(strFecha);
				ok = true;
			} catch (ParseException e) {
				
			}
		}
		return ok;
	}
	
	public static boolean isNumero(Object valor) {
		try{
			Double doubleVal = Double.parseDouble((valor).toString());
			doubleVal.longValue();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static Locale getLocaleES(){
		return new Locale("es","PE");
	}
	public static Locale getLocaleUS(){
		return new Locale("us","US");
	}
	public static String getAnioActual() {
		Format anioFormateado = new SimpleDateFormat("yyyy");
		return anioFormateado.format(new Date());
	}
	
	public static String replaceVal(String cad,String var,String val){
		String newCad = ""; 
		newCad = cad.replace(var, val);
		return newCad; 
	}
	
}
