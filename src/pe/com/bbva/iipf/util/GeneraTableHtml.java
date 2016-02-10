package pe.com.bbva.iipf.util;

import java.lang.reflect.Field;
import java.util.List;

public class GeneraTableHtml {
	
	
	public static String getTableHTML(Class c,List list,String[] campos,String[] Formatcampos,String addcabecera,String cabPersonalidad,String[] cabecera,String[] totales,String addtotal,String[] cabecera2){
		try {
			
	
		StringBuilder sb=new StringBuilder ();		
		sb.append("<table class=\"gridtable\">");		
			
			if (addcabecera.equals("1")){
				// cabecera2 para el cuadro de negocio y beneficio
				if(cabecera2!=null && cabecera2.length>0){
					sb.append("<tr>");
					sb.append("<th align=\"center\">" + cabecera2[0] + "</th>");
					for (int n = 1; n < cabecera2.length; n++){
						sb.append("<th colspan=\"3\" align=\"center\">" + cabecera2[n] + "</th>");					
					}
					sb.append("</tr>");
				}
				sb.append("<tr>");
				for (int n = 0; n < cabecera.length; n++){
					sb.append("<th align=\"center\">" + cabecera[n] + "</th>");					
				}
				sb.append("</tr>");				
			}
			else{
				sb.append(cabPersonalidad);
			}			
			
			for (int i=0;i<list.size();i++){
				sb.append("<tr>");
				Object obj=list.get(i);				
				for (int m = 0; m < campos.length; m++){
					 String valor="";
					  Field campo = c.getDeclaredField(campos[m]);
					   campo.setAccessible(true); //por el tema de ser el atributo privado
					   if (campo.get(obj)!=null){
					    valor = campo.get(obj).toString(); 					   
					   }
					   
					   if (Formatcampos[m].equals("CENTER")){
						   sb.append("<td align=\"center\">" + valor + "</td>");					   
					   }else{
						   if (campos[m].equals(Formatcampos[m])){
							   sb.append("<td>" + valor + "</td>");					   
						   }else{
							   sb.append("<td align=\"right\">" + valor + "</td>");
						   }
					   }
//					   if (m==0) {
//						   sb.append("<td>" + valor + "</td>");
//					   }else{
//						   sb.append("<td align=\"right\">" + valor + "</td>");
//					   }
					
					
				}
			
				sb.append("</tr>");
			}
			
			if (addtotal.equals("1")){
				sb.append("<tr>");
				for (int p = 0; p < totales.length; p++){
					sb.append("<td align=\"right\">" + totales[p] + "</td>");					
				}
				sb.append("</tr>");	
			}    
		
		sb.append("</table>");  
		
		return sb.toString();
		
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param c
	 * @param list
	 * @param campos
	 * @param Formatcampos
	 * @param addcabecera
	 * @param cabPersonalidad
	 * @param cabecera
	 * @param totales
	 * @param addtotal
	 * @param tamanioLetra en pixeles!!!
	 * @return
	 */
	
	public static String getTableHTMLTamanio(Class c,List list,String[] campos,String[] Formatcampos,String addcabecera,
			String cabPersonalidad,String[] cabecera,String[] totales,String addtotal, String tamanioLetra){
		try {
			
		String estiloTamanioL = "style=\"font-size: "+tamanioLetra+"px;\"";
		StringBuilder sb=new StringBuilder ();		
		sb.append("<table class=\"gridtable\">");		
			
			if (addcabecera.equals("1")){
				sb.append("<tr>");
				for (int n = 0; n < cabecera.length; n++){
					sb.append("<th align=\"center\">" + cabecera[n] + "</th>");					
				}
				sb.append("</tr>");				
			}
			else{
				sb.append(cabPersonalidad);
			}			
			
			for (int i=0;i<list.size();i++){
				sb.append("<tr>");
				Object obj=list.get(i);				
				for (int m = 0; m < campos.length; m++){
					 String valor="";
					  Field campo = c.getDeclaredField(campos[m]);
					   campo.setAccessible(true); //por el tema de ser el atributo privado
					   if (campo.get(obj)!=null){
					    valor = campo.get(obj).toString(); 					   
					   }
					   
					   if (campos[m].equals(Formatcampos[m])){
						   sb.append("<td>" + valor + "</td>");					   
					   }else{
					   sb.append("<td "+estiloTamanioL+"align=\"right\">" + valor + "</td>");
					   }
					   
				}
			
				sb.append("</tr>");
			}
			
			if (addtotal.equals("1")){
				sb.append("<tr>");
				for (int p = 0; p < totales.length; p++){
					if(p==0){
						sb.append("<td align=\"right\">" + totales[p] + "</td>");
					}else{
						sb.append("<td "+estiloTamanioL+"align=\"right\">" + totales[p] + "</td>");
					}
				}
				sb.append("</tr>");	
			}    
		
		sb.append("</table>");  
		
		return sb.toString();
		
		} catch (Exception e) {
			return "";
		}
	}
}
