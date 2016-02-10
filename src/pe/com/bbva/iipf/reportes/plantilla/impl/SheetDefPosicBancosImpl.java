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


import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;

public class SheetDefPosicBancosImpl extends SheetDefinitionBase implements SheetDefinition{

	private List<AnexoColumna>  listaColumnas 	= new ArrayList<AnexoColumna>();
	private List<FilaAnexo> 	listaFilaAnexos = new ArrayList<FilaAnexo>();
	private FilaAnexo 		    filaTotal 		= new FilaAnexo();
	
	private HSSFCellStyle cellStyle1 = null;
	private int rowInicialY			  	= 4;
	private int numColExistentes 		=8;
	private int numColNuevas 			=0;
	int rowY			  				= 4;
	int numColRefenciaStyle			  	= 6;

	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
		cargarDatos(hoja,datos);
		
	}
	
	private void cargarDatos(HSSFSheet hoja, Map datos){
		rowY			  	  = 4;
		int colX          	   =1;
		HSSFRow row			  = null;
		HSSFCell cell		  = null;
		int numFilas		  = 0;
		
		listaColumnas   = (List<AnexoColumna>)datos.get("listaColumnas");
		listaFilaAnexos = (List<FilaAnexo>)datos.get("listaFilaAnexos");
		filaTotal 	    = (FilaAnexo)datos.get("filaTotal");
		
		numFilas = listaFilaAnexos.size()+2;  //Este Numero de Filas Para que nos servira???
		//Cabecera
//		row =hoja.getRow(rowY);
//		if(row==null) row=hoja.createRow(rowY);
//		
//		int tamanio = listaColumnas.size();
//		numColNuevas = tamanio-numColExistentes;
//		numColNuevas=(numColNuevas<0)?numColNuevas:0;
//		for(AnexoColumna acol:listaColumnas){
//
//				cell = row.getCell(colX);
//				if(cell==null) 
//					cell=row.createCell(colX);
//
//			cell.setCellValue(acol.getDescripcion());
//			colX++;
//		}
		
		//Filas Anexo
		rowY++;
		colX=1;
		int colXAux=0;
		int estilo=0;
		boolean esBold=false;
		boolean esTotal=false;
		for(FilaAnexo fAnex:listaFilaAnexos){
			row 	= hoja.getRow(rowY);
			if(row==null) row=hoja.createRow(rowY);
			
			if(fAnex.getAnexo().getTipoFila()==1){ //Ponemos un estilo Para bancos
				estilo=1;
				esBold=true;
			}else if(fAnex.getAnexo().getTipoFila()==2){ //Ponemos otro para Empresas
				estilo=2;
				esBold=true;
			}else if(fAnex.getAnexo().getTipoFila()==6){ //Ponemos otro para Total
				esTotal=true;
				estilo=3;
				esBold=true;
			}else{  //Ponemos estilo pora Operaciones
				estilo=4;
				esBold=false;
			}
			
			cell 	= row.getCell(colXAux);
			if(cell==null) 
				cell=row.createCell(colXAux);
			
			ponerStiloCelda(cell, estilo,esBold);
			if(fAnex.getAnexo().getTipoFila()==4){
				cell.setCellValue("      "+fAnex.getAnexo().getDescripcion());
			}else{
				cell.setCellValue(fAnex.getAnexo().getDescripcion());
			}
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getBureau()!=null?"":fAnex.getAnexo().getBureau());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getRating()!=null?"":fAnex.getAnexo().getRating());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getFecha()!=null?"":fAnex.getAnexo().getFecha());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getLteAutorizado()!=null?"":fAnex.getAnexo().getLteAutorizado());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getLteForm()!=null?"":fAnex.getAnexo().getLteForm());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getRgoActual()!=null?"":fAnex.getAnexo().getRgoActual());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getRgoPropBbvaBc()!=null?"":fAnex.getAnexo().getRgoPropBbvaBc());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getPropRiesgo()!=null?"":fAnex.getAnexo().getPropRiesgo());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(fAnex.getAnexo().getObservaciones()!=null?"":fAnex.getAnexo().getObservaciones());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			if (fAnex.getAnexo().getActivoCol1().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna1()!=null?"":fAnex.getAnexo().getColumna1());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			
			if (fAnex.getAnexo().getActivoCol2().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna2()!=null?"":fAnex.getAnexo().getColumna2());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol3().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna3()!=null?"":fAnex.getAnexo().getColumna3());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol4().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna4()!=null?"":fAnex.getAnexo().getColumna4());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol5().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna5()!=null?"":fAnex.getAnexo().getColumna5());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol6().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna6()!=null?"":fAnex.getAnexo().getColumna6());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol7().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna7()!=null?"":fAnex.getAnexo().getColumna7());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol8().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna8()!=null?"":fAnex.getAnexo().getColumna8());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol9().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna9()!=null?"":fAnex.getAnexo().getColumna9());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			colX++;
			if (fAnex.getAnexo().getActivoCol10().equals("1")){
				cell 	= row.getCell(colX);
				if(cell==null) cell=row.createCell(colX);	
				cell.setCellValue(fAnex.getAnexo().getColumna10()!=null?"":fAnex.getAnexo().getColumna10());			
				ponerStiloCelda(cell, estilo,esBold);
				
			}
			
			
//			List<AnexoColumna> listaAnexCol = fAnex.getListaAnexoColumna();
//			for(AnexoColumna aValCol:listaAnexCol){
//
//					cell 	= row.getCell(colX);
//					if(cell==null) 
//						cell=row.createCell(colX);
//
//				cell.setCellValue(aValCol.getValor());
//				
//				ponerStiloCelda(cell, estilo,esBold);
//					colX++;
//			}

			colX=1;
			rowY++;
		}
		
		//Totales
		if (!esTotal){
			colX=1;
			row =hoja.getRow(rowY);
			if(row==null) row=hoja.createRow(rowY);
			
			//Para el Titulo:
			cell = row.getCell(colX-1);
			if(cell==null) 
				cell=row.createCell(colX-1);
			cell.setCellValue(" TOTAL ");
			esBold=true;
			ponerStiloCelda(cell, 3,esBold);
			
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getBureau()!=null?"":filaTotal.getAnexo().getBureau());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getRating()!=null?"":filaTotal.getAnexo().getRating());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getFecha()!=null?"":filaTotal.getAnexo().getFecha());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getLteAutorizado()!=null?"":filaTotal.getAnexo().getLteAutorizado());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getLteForm()!=null?"":filaTotal.getAnexo().getLteForm());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getRgoActual()!=null?"":filaTotal.getAnexo().getRgoActual());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getRgoPropBbvaBc()!=null?"":filaTotal.getAnexo().getRgoPropBbvaBc());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getPropRiesgo()!=null?"":filaTotal.getAnexo().getPropRiesgo());			
			ponerStiloCelda(cell, estilo,esBold);
			colX++;
			
			cell 	= row.getCell(colX);
			if(cell==null) cell=row.createCell(colX);	
			cell.setCellValue(filaTotal.getAnexo().getObservaciones()!=null?"":filaTotal.getAnexo().getObservaciones());			
			ponerStiloCelda(cell, estilo,esBold);
			
			
		
			
//			List<AnexoColumna> listAnexColTot=filaTotal.getListaAnexoColumna();
//			for(AnexoColumna aColTot:listAnexColTot){
//				cell = row.getCell(colX);
//				if(cell==null) 	cell=row.createCell(colX);
//				cell.setCellValue(aColTot.getValor());
//				
//				ponerStiloCelda(cell, 3,esBold);
//				
//				colX++;
//			}
		}
		//Formateamos las celdas de las nueva columnas
		formatearCeldasNuevas(hoja);
		
	}
	
	private void ponerStiloCelda(HSSFCell celda, int estilo, boolean esBold){
		HSSFFont font=null;
		HSSFCellStyle styleCol=null;
		int numCol;
		switch(estilo){

			case 1: 
				
				 	styleCol = celda.getSheet().getWorkbook().createCellStyle();
					styleCol.setFillForegroundColor(HSSFColor.BLUE.index);
					styleCol.setFillBackgroundColor(HSSFColor.BLUE.index);
					styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				 font = celda.getSheet().getWorkbook().createFont();
				 font.setColor(HSSFColor.WHITE.index);
				if(esBold){
					font.setFontHeightInPoints((short)17);
					font.setBoldweight((short)17);
				}
				styleCol.setFont(font);

				styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
				styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
				styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
				styleCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
				
				//Alineamos
				numCol=celda.getColumnIndex();
				if(numCol==0 ||numCol==8){ //MCG20130823 cambio de 7 por 8
					styleCol.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				}else if(numCol==2){ 
					styleCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				}else{
					styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				}
				
				celda.setCellStyle(styleCol);
					break;
					
			case 2: 				styleCol = celda.getSheet().getWorkbook().createCellStyle();
									styleCol.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			 						styleCol.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
			 						styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 						
			 						font = celda.getSheet().getWorkbook().createFont();
									if(esBold){
										font.setFontHeightInPoints((short)16);
										font.setBoldweight((short)16);
									}
									styleCol.setFont(font);
									styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
									styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
									styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
									styleCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
									
									//Alineamos
									numCol=celda.getColumnIndex();
									if(numCol==0 ||numCol==8){//MCG20130823 cambio de 7 por 8
										styleCol.setAlignment(HSSFCellStyle.ALIGN_LEFT);
									}else if(numCol==2){ 
										styleCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
									}else{
										styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
									}
									celda.setCellStyle(styleCol);
									break;
			
			case 3: 
					styleCol = celda.getSheet().getWorkbook().createCellStyle();
					styleCol.setFillForegroundColor(HSSFColor.YELLOW.index);
					styleCol.setFillBackgroundColor(HSSFColor.YELLOW.index);
 					styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
 					
					font = celda.getSheet().getWorkbook().createFont();
					if(esBold){
						font.setFontHeightInPoints((short)17);
						font.setBoldweight((short)17);
					}
					styleCol.setFont(font);
					
					styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setFillBackgroundColor( HSSFColor.CORAL.index);
					//Alineamos
					numCol=celda.getColumnIndex();
					if(numCol > 0){
						styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					}
					celda.setCellStyle(styleCol);

					break;
			
			case 4: 		styleCol = celda.getSheet().getWorkbook().createCellStyle();
			 				font = celda.getSheet().getWorkbook().createFont();
								font.setFontHeightInPoints((short)14);
							styleCol.setFont(font);
							styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
							styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
							styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
							styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);

							//Alineamos
							numCol=celda.getColumnIndex();
							if(numCol==0 ||numCol==8){//MCG20130823 cambio de 7 por 8
								styleCol.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							}else if(numCol==2){ 
								styleCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
							}else{
								styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
							}
							celda.setCellStyle(styleCol);
							break;
		}
	}
	
	private void formatearCeldasNuevas(HSSFSheet hoja){
		HSSFCellStyle cellStyle=null;
		int inicio= this.rowInicialY;
		int fin=this.rowY;
		int posIniCol=numColExistentes;
		int posFinCol=numColExistentes+numColNuevas;
       while(inicio<=fin){
    	   
    	   cellStyle=hoja.getRow(inicio).getCell(numColRefenciaStyle).getCellStyle(); //Tomamos como patron las celdas dela columna 6
    	   
    	   for(int i=posIniCol;i<posFinCol;i++){
    		   hoja.getRow(inicio).getCell(i).setCellStyle(cellStyle);
    	   }
    	   
    	   inicio++;
       }
		
	}

}
