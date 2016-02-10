package pe.com.stefanini.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Helper de utilidades
 * 
 * @author epomayay
 * 
 */
public class ExcelHelper {

	private static Logger logger = Logger.getLogger(ExcelHelper.class);

	private final static String EXTENSION_HTML = ".html";
	private final static String TAG_HTML_INICIO = "<html>";
	private final static String TAG_HTML_FIN = "</html>";
	
	/**
	 * retorna el nombre la imagen generada en el directorio especificado el
	 * formato del nombre retornado constara de dos partes primero el nombre de
	 * la imagen seguido de un _ mas la fecha que se esta generando la imagen
	 * 
	 * @param html
	 * @param dir
	 * @return
	 */
	public static String convertHTMLtoIMG(String html, String dirTemporal,
			String nombreImg) {
		//html = "<div style=\"width: 700px\">" + html + "</div>";
		//logger.info("html inicial" + html);
		// html = cleanText(html);
//EPOMAYAY 04012012
//		html = ValidateUtil.pregReplace("align[\\s]*=[\\s]*[\"]{0,1}[\\s]*justify[\\s]*[\"]{0,1}",
//										html, 
//										"aling=left");
//		html = ValidateUtil.pregReplace("<[Pp][\\s]*style=[\\s]*\"[\\s]*TEXT-ALIGN[\\s]*:[\\s]*justify",
//										html, 
//										"<P style=\"text-align: left");
//EPOMAYAY 04012012
		if (!html.startsWith(TAG_HTML_INICIO))
			html = TAG_HTML_INICIO + html;
		if (!html.endsWith(TAG_HTML_FIN))
			html = html + TAG_HTML_FIN;
		//para produccion
		html = html.replace("<html>", "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head>");
		//para desarrollo
//		html = html.replace("<html>", "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/></head>");
		logger.info("html limpiado " + html);
		System.getProperties().put("http.proxyHost", "118.180.54.170");
		System.getProperties().put("http.proxyPort", "8080");
		String nombre = "";
		/*
		 * nombre = dir+File.separator+nombreImg + "_" +
		 * FechaUtil.formatFecha(new Date(), FechaUtil.YYYYMMDD_HHMMSS)+".png";
		 * HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		 * imageGenerator.loadHtml(html); imageGenerator.saveAsImage(nombre);
		 */
		logger.info("html final" + html);
		File filehtml = new File(dirTemporal + 
								 File.separator + 
								 nombreImg + 
								 EXTENSION_HTML);
		try {
			FileOutputStream fos = new FileOutputStream(filehtml);
			fos.write(html.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			 
			e.printStackTrace();
		}

		return nombre;
	}

	public static String cleanText(String html) {
		List<String> patrones = new ArrayList<String>();
		patrones.add("<\\?xml:namespace ([^>]*)>");
		patrones.add("<[/a-zA-Z0-9]*:[a-zA-Z0-9\":=\\-\\s_,@?αινσϊ\\./\\~]*>");
		//ini MCG20131010
		//patrones.add("u1:[a-zA-Z0-9\":=\\-\\s_,@?αινσϊ\\./\\~]*");
		//fin MCG20131010
		patrones.add("x:str");
		String resultado = html==null?"":html;
		for (String patron : patrones) {
			resultado = ValidateUtil.pregReplace(patron, resultado, "");
		}
		return resultado;
	}
	
	public static String cleanText(String html,String patronesEditor) {
		List<String> patrones = new ArrayList<String>();
		patrones.add("<\\?xml:namespace ([^>]*)>");
		patrones.add("<[/a-zA-Z0-9]*:[a-zA-Z0-9\":=\\-\\s_,@?αινσϊ\\./\\~]*>");
		patrones.add("x:str");
		
		//ini MCG20131010
		//patrones.add("u1:[a-zA-Z0-9\":=\\-\\s_,@?αινσϊ\\./\\~]*");		
		
		String delimiter = "\\|";
		if (patronesEditor!=null && patronesEditor.length()>0){
			String patronParam = patronesEditor;
			String[] arraypatronParam = patronParam.split(delimiter);
			for (int t = 0; t < arraypatronParam.length; t++) {
				patrones.add(arraypatronParam[t]==null?"":arraypatronParam[t].toString());
			}
		}
		//fin MCG20131010
		
		String resultado = html==null?"":html;
		for (String patron : patrones) {
			resultado = ValidateUtil.pregReplace(patron, resultado, "");
		}
		return resultado;
	}
}
