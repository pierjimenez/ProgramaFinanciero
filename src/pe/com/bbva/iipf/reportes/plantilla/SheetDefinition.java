package pe.com.bbva.iipf.reportes.plantilla;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface SheetDefinition {
	
	public void fillDataSheet(HSSFSheet hoja, Map<String,Object> datos) throws Exception;

}
