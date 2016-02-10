package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.stefanini.core.util.FormatUtil;

public class SheetDefPropRiesgoImpl extends SheetDefinitionBase implements SheetDefinition{

	private List<AnexoColumna>  listaColumnas 	= new ArrayList<AnexoColumna>();
	private List<FilaAnexo> 	listaFilaAnexos = new ArrayList<FilaAnexo>();
	private FilaAnexo 		    filaTotal 		= new FilaAnexo();
	
	private HSSFCellStyle cellStyle1 = null;
	private int rowInicialY			  	= 25;
	//private int numColExistentes 		=8;
	//private int numColNuevas 			=0;
	int rowY			  				= 4;
	int numColRefenciaStyle			  	= 6;

	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
		cargarDatos(hoja,datos);
		
	}
	
	private void cargarDatos(HSSFSheet hoja, Map datos){
		rowY			  	  = 23;
		int posComenConsidera = 0;//posiciones que se usaran para movel el comentario de cosideraciones
		int colX          	  = 1;
		HSSFRow row			  = null;
		HSSFCell cell		  = null;
		
		Double valor1 	=0d;
		Double valor2 	=0d; 
		Double total1 	=0d;
		Double total2 	=0d; 
		
        List<EstructuraLimite> listEstructuraLimite =	(List<EstructuraLimite>)datos.get("listEstructuraLimite");
        List<Empresa> listEmpresaPropRiesg 			=	(List<Empresa>)datos.get("listEmpresaPropRiesg");
        String nombreEmpresa="";
        
		String strtipomiles=(String)datos.get("tipomiles");		
		
		row 	= hoja.getRow(21);
		if(row==null) row=hoja.createRow(21);
		
		cell 	= row.getCell(1);
		if(cell==null) {
			cell=row.createCell(1);}
		cell.setCellValue(strtipomiles);
		
		for(EstructuraLimite estLimite:listEstructuraLimite){
			
				colX=0;
				row 	= hoja.getRow(rowY);
				if(row==null) row=hoja.createRow(rowY);
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				

				
				if(estLimite.getTipo().equalsIgnoreCase("TT")){
					
					cell.setCellValue(estLimite.getTipoOperacion());
					
				}else if(estLimite.getTipo().equalsIgnoreCase("TE")){
					
					//cell.setCellValue(estLimite.getEmpresa().getNombre())
					//Empresa empx=obtenerEmpresa(listEmpresaPropRiesg, estLimite.getCodEmpresatmp());
					Empresa empx=obtenerEmpresa(listEmpresaPropRiesg, estLimite.getEmpresa().getId());
					nombreEmpresa=(empx!=null)?empx.getNombre():""; 
					cell.setCellValue(nombreEmpresa);
				}
				
				ponerStiloCelda(cell, 1, false,1);
				
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getLimiteAutorizado());
				ponerStiloCelda(cell, 2, false,1);
				
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getLimitePropuesto());
				ponerStiloCelda(cell, 2, false,1);
				
				//saldo utilizado
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getSaldoUtilizado());
				ponerStiloCelda(cell, 2, false,1);

				//prop oficina
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getPropuestoOficina());
				ponerStiloCelda(cell, 2, false,1);

				//prop riesgo
				
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getPropuestaRiesgo());
				ponerStiloCelda(cell, 2, false,1);
				
				colX++;
				
				cell 	= row.getCell(colX);
				if(cell==null) 
					cell=row.createCell(colX);
				cell.setCellValue(estLimite.getObservacion());
				ponerStiloCelda(cell, 1, false,1);
				
				//Para totalizar
				
				 valor1 =Double.valueOf(FormatUtil.FormatNumeroSinComa(estLimite.getLimiteAutorizado() ));
				 valor2 =Double.valueOf(FormatUtil.FormatNumeroSinComa(estLimite.getLimitePropuesto() ));
				 total1+=valor1;
				 total2+=valor2;
			
			rowY++;
			if(rowY>28){
				posComenConsidera++;
				insertarRows(hoja,rowY,1);
			}
		}
		
		datos.put("cantPosConsiderPropRies", posComenConsidera);

		//Totales
//		colX=0;
//		row =hoja.getRow(rowY);
//		if(row==null) row=hoja.createRow(rowY);
//		
//		cell = row.getCell(colX);
//		if(cell==null) 
//			cell=row.createCell(colX);
//		cell.setCellValue(" TOTAL GRUPO ");
//		ponerStiloCelda(cell,3, true,1);
//
//		colX++;
//		cell 	= row.getCell(colX);
//		if(cell==null) 
//			cell=row.createCell(colX);
//		cell.setCellValue(FormatUtil.roundTwoDecimalsPunto(total1));
//		ponerStiloCelda(cell, 4, true,1);
//		
//		colX++;
//		cell 	= row.getCell(colX);
//		if(cell==null) 
//			cell=row.createCell(colX);
//		cell.setCellValue(FormatUtil.roundTwoDecimalsPunto(total2));
//		ponerStiloCelda(cell, 4, true,1);
//		
//		colX++;
//		cell 	= row.getCell(colX);
//		if(cell==null) 
//			cell=row.createCell(colX);
//		ponerStiloCelda(cell, 4, true,1);
		

	}
	
	private Empresa obtenerEmpresa(List<Empresa> listEmpresaPropRiesg, Long codEmpresatmp){
		

		Empresa empResp=null;
		if(listEmpresaPropRiesg != null)
		{
			for(Empresa emp : listEmpresaPropRiesg ){
				 if(emp.getId().longValue()==codEmpresatmp.longValue()){
					 empResp=emp;
					 break;
				 }
			}
		}
		
		return empResp;
	}
	
    private void ponerStiloCelda(HSSFCell celda, int estilo, boolean esBold,int alineacion){
    	
		HSSFFont font=null;
		HSSFCellStyle styleCol=null;
		
		switch(estilo){

			case 1: 
						styleCol = celda.getSheet().getWorkbook().createCellStyle();
						font = celda.getSheet().getWorkbook().createFont();
						if(esBold){
							font.setBoldweight((short)18);
						}
						font.setFontHeightInPoints((short)14);
						styleCol.setFont(font);
						celda.setCellStyle(styleCol);
						styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
						styleCol.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						break;
					
			case 2: 									
						styleCol = celda.getSheet().getWorkbook().createCellStyle();
						font = celda.getSheet().getWorkbook().createFont();
						if(esBold){
							font.setBoldweight((short)18);
						}
						font.setFontHeightInPoints((short)14);
						styleCol.setFont(font);
						celda.setCellStyle(styleCol);
						styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
						styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					break;
			
			case 3: 
						styleCol = celda.getSheet().getWorkbook().createCellStyle();
						styleCol.setFillBackgroundColor( HSSFColor.PALE_BLUE.index);
 						styleCol.setFillForegroundColor( HSSFColor.PALE_BLUE.index);
 	 					styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
 	 					font = celda.getSheet().getWorkbook().createFont();
 	 					if(esBold){
							font.setBoldweight((short)18);
 	 					}
						font.setFontHeightInPoints((short)15);
						styleCol.setFont(font);
						celda.setCellStyle(styleCol);
						styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
						styleCol.setAlignment(HSSFCellStyle.BORDER_MEDIUM);
					break;
			
			case 4: 
						styleCol = celda.getSheet().getWorkbook().createCellStyle();
						styleCol.setFillBackgroundColor( HSSFColor.PALE_BLUE.index);
						styleCol.setFillForegroundColor( HSSFColor.PALE_BLUE.index);
						styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						font = celda.getSheet().getWorkbook().createFont();
						if(esBold){
							font.setBoldweight((short)18);
						}
						font.setFontHeightInPoints((short)15);
						styleCol.setFont(font);
						celda.setCellStyle(styleCol);
						styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
						styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
						styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				break;

		}
	}
	
}
