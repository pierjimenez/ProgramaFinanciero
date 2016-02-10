package pe.com.stefanini.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;


public class FechaUtil {

	private static Logger logger = Logger.getLogger(FechaUtil.class);
	//private  Date date = new Date();
	public static  final String  YYYYMMDD_HHMMSS = "yyyyMMdd-hhmmss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	public static String getFechaActualString(){
		DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
		return dateFormat.format(new Date());
	}
	public static Date getFechaActualDate(){
		return new Date();
	}
	
	public static String formatFecha(Date fecha,
									 String format){
		Locale currentLocale = new Locale("pe","ES");
		DateFormat dateFormat = new SimpleDateFormat(format,
	    				 				 			 currentLocale);
		return dateFormat.format(fecha);
	}
	
	  public static String formatFechaActual(String format){
			Locale currentLocale = new Locale("pe","ES");
			DateFormat dateFormat = new SimpleDateFormat(format,
							 			 currentLocale);
			return dateFormat.format(new Date());
			}
	/**
	 * compara dos fechas 
	 * si la primera fecha es menor retorna 1
	 * si las dos fechas son iguales retorna 0
	 * si la segunda fecha es menor retorna -1
	 * @param firstDate
	 * @param endDate
	 * @return
	 */	
	public static int compareDate(Date firstDate, 
								  Date endDate){
		//quitar la hora minutos y segundos
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(firstDate);
		Calendar calendarFirst = new GregorianCalendar(calendar.get(Calendar.YEAR),
													   calendar.get(Calendar.MONTH),
													   calendar.get(Calendar.DATE));
		calendar.setTime(endDate);
		Calendar calendarEnd = new GregorianCalendar(calendar.get(Calendar.YEAR),
													 calendar.get(Calendar.MONTH),
													 calendar.get(Calendar.DATE));
		
		if(calendarFirst.getTime().getTime() < calendarEnd.getTime().getTime()){
			return 1;
		}else if(calendarFirst.getTime().getTime() == calendarEnd.getTime().getTime()){
			return 0;
		}else{
			return -1;
		}
	}
	
	/**
	 * obtiene la cantidad de años de una fecha con respecto a la fecha actual
	 * @param fecha
	 * @return
	 */
	public static String getNumAnios(String fecha){
		String dif = "";
		Locale currentLocale = new Locale("pe","ES");
		DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD,
	    				 				 			 currentLocale);
		try {
			if(fecha==null){
				dif = "";
			}
			if(fecha!=null && fecha.trim().equals("")){
				dif = "";
			}
			Date date = dateFormat.parse(fecha);
			Date hoy = new Date();
			if(hoy.getTime()<date.getTime()){
				throw new Exception("La fecha es mayor que la fecha actual");
			}
			Long diferencia = ((hoy.getTime()-date.getTime())/(60*60*24*365))/1000;
			dif = ""+diferencia; 
		} catch (ParseException e) {
			dif="";
			logger.error(StringUtil.getStackTrace(e));
		} catch(Exception e){
			dif="";
			logger.error(StringUtil.getStackTrace(e));
		}
		return dif;
	}
	/*vchn*/
	public  static String getPastDate(){
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	/*vchn*/
}
