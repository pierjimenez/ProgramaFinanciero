package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.stefanini.core.util.FormatUtil;

public class SheetDefCaratula2Impl implements SheetDefinition{
	private int[] fcGrupo= new int[2];
	  
	public SheetDefCaratula2Impl(){
		  //Inicalizamos
		  fcGrupo[0]=10;
		  fcGrupo[1]=1;
	}
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map<String,Object> datos) {
		 
		 
		 llenarGrupo(hoja.getRow(fcGrupo[0]), datos);
		 
		 llenarDatosLimites(hoja,datos);
	     hoja.setForceFormulaRecalculation(true);
		
	}
	
	private void llenarGrupo(HSSFRow fila, Map<String,Object> params){
		String tipoEmpresa			= (String)params.get("tipoEmpresa");
		String nombreEmpresaOrGrupo	= (String)params.get("nombreEmpresaGrupo");
		fila.getCell(fcGrupo[1]).setCellValue(nombreEmpresaOrGrupo);
	}
	
	private void llenarDatosLimites(HSSFSheet hoja, Map datos){
		int rowY			  = 29;
		int colX			  = 4;
		
    	Double totLimAutorizado		=(Double)datos.get("totLimAutorizado");
    	Double totLimFormalizado	=(Double)datos.get("totLimFormalizado");
    	Double totLimPropuesto		=(Double)datos.get("totLimPropuesto");
    	
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimAutorizado));
    	rowY++;
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimFormalizado));
    	rowY=rowY+2;
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimPropuesto));
    	
	}

}
