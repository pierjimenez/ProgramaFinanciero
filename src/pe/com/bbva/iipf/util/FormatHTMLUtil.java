package pe.com.bbva.iipf.util;

public class FormatHTMLUtil {

	public static String ObtenerNameMes(String valor){		
		String mes="Dic";
		try{		    
		     int month = Integer.parseInt(valor); 
		     
		        switch (month) {
		            case 1:  mes="Ene"; break;
		            case 2:  mes="Feb"; break;
		            case 3:  mes="Mar"; break;
		            case 4:  mes="Abr"; break;
		            case 5:  mes="May"; break;
		            case 6:  mes="Jun"; break;
		            case 7:  mes="Jul"; break;
		            case 8:  mes="Ago"; break;
		            case 9:  mes="Sep"; break;
		            case 10: mes="Oct"; break;
		            case 11: mes="Nov"; break;
		            case 12: mes="Dic"; break;
		        }		
		return mes;
		} catch (Exception e) {
		return mes;
		}		
	}
}
