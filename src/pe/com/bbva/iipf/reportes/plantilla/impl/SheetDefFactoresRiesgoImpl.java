package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;

public class SheetDefFactoresRiesgoImpl implements SheetDefinition{

	private int fila=0;
	private int colum=0;
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {

        
		
		setFortalezas( hoja,  datos);
		setDebilidades( hoja,  datos);
		setOportunidades( hoja,  datos);
		setAmenazas( hoja,  datos);
	}
	
	private void setFortalezas(HSSFSheet hoja, Map datos){
		fila	=7;
		colum	=8;
		int colX=0;
		/*
		hoja.addMergedRegion(new CellRangeAddress(fila,fila+8,0,colum));
		if(hoja.getRow(fila).getCell(colX)==null) hoja.getRow(fila).createCell(colX);
		hoja.getRow(fila).getCell(colX).setCellValue("datos para hcer todo tippo d FORTALEZAS");
		*/
	}
	private void setDebilidades(HSSFSheet hoja, Map datos){
		
			fila	=18;
			colum	=8;
			int colX=0;
			/*
			hoja.addMergedRegion(new CellRangeAddress(fila,fila+8,0,colum));
			if(hoja.getRow(fila)==null) hoja.createRow(fila);
			if(hoja.getRow(fila).getCell(colX)==null) hoja.getRow(fila).createCell(colX);
			hoja.getRow(fila).getCell(colX).setCellValue("datos para hcer todo tippo d DEBILIDADES");
			*/
		
	}
	private void setOportunidades(HSSFSheet hoja, Map datos){
		
		fila	=29;
		colum	=8;
		int colX=0;
		/*
		hoja.addMergedRegion(new CellRangeAddress(fila,fila+8,0,colum));
		if(hoja.getRow(fila)==null) hoja.createRow(fila);
		if(hoja.getRow(fila).getCell(colX)==null) hoja.getRow(fila).createCell(colX);
		hoja.getRow(fila).getCell(colX).setCellValue("datos para hcer todo tippo d OPORTUNIDADES");
		*/
	}
	private void setAmenazas(HSSFSheet hoja, Map datos){
		
		fila	=41;
		colum	=8;
		int colX=0;
		/*
		hoja.addMergedRegion(new CellRangeAddress(fila,fila+8,0,colum));
		if(hoja.getRow(fila)==null) hoja.createRow(fila);
		if(hoja.getRow(fila).getCell(colX)==null) hoja.getRow(fila).createCell(colX);
		hoja.getRow(fila).getCell(colX).setCellValue("datos para hcer AMENAZAS");
		*/
	}
	
}