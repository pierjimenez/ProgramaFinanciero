package pe.com.bbva.iipf.reportes.bo;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface GeneradorReporteBO {

	public void buildReportExcelWithTemplate(HSSFWorkbook wbPF) throws Exception;
	public void inicializar(Map<String,Object> params);
	public void insertImagesToExcel(String fileNameExcel,
			 						HSSFWorkbook wb,
			 						Map<String,Object> datos);
}
