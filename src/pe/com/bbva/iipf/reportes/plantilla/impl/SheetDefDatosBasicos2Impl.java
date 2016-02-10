package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.util.FormatUtil;

public class SheetDefDatosBasicos2Impl extends SheetDefinitionBase implements SheetDefinition{
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
       
        cargarDatosNegocioBeneficio(hoja,datos);
        hoja.setForceFormulaRecalculation(true);
		
	}
	
	
	private void cargarDatosNegocioBeneficio(HSSFSheet hoja,Map datos){
		Programa programax = (Programa)datos.get("programax");
		String comentValorGlobal=programax.getComentvaloraGlobal();
		Double totali1 = 0d;
		Double totali2 = 0d;
		Double totali3 = 0d;
		Double totalb1 = 0d;
		Double totalb2 = 0d;
		Double totalb3 = 0d;
		int rowTot1    = 8;
		int rowTot2    = 13;

		
		
		List<Map> listaDatosNegoBenef=obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),6);
		HSSFRow row		=null;
		HSSFRow rowStyle		=null;
		Integer annioProg=(Integer)datos.get("annioProg");
		
		
		
		//ini MCG20111026
	    //Cabecera Negocio Beneficio - Actividad	
		List<Map> listaDatosSeccCab = obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),8);
	    
		String anioa1=" " + String.valueOf(annioProg-2);
		String anioa2=" " + String.valueOf(annioProg-1);
		String anioa3=" " + String.valueOf(annioProg);
		
		String anioa4=" " + String.valueOf(annioProg-2);
		String anioa5=" " + String.valueOf(annioProg-1);
		String anioa6=" " + String.valueOf(annioProg);
		String tituloActividad="Por Línea de Actividad";
		
		
		String tituloNegocio="Por Línea de Negocio";
		
		if(listaDatosSeccCab!=null && !listaDatosSeccCab.isEmpty()){
			for(Map dataa:listaDatosSeccCab){
				String idtipoTabla=(dataa.get("TT_ID_TIPO_TABLA")!=null)?dataa.get("TT_ID_TIPO_TABLA").toString():"";
				String strOrden=dataa.get("ORDEN").toString();
				String anioact=(dataa.get("NOMBRE_CABTABLA")!=null)?dataa.get("NOMBRE_CABTABLA").toString():"";
				
				if(idtipoTabla.equals(Constantes.ID_TIPO_TABLA_NEGOCIO.toString())){						
					
					if (strOrden.equals("0")) {
						tituloNegocio = anioact;
					} else if (strOrden.equals("1")) {
						anioa1 = anioact;
					} else if (strOrden.equals("2")) {
						anioa2 = anioact;
					} else if (strOrden.equals("3")) {
						anioa3 = anioact;					
					} else if (strOrden.equals("4")) {
						anioa4 = anioact;
					} else if (strOrden.equals("5")) {
						anioa5 = anioact;
					} else if (strOrden.equals("6")) {
						anioa6 = anioact;					
					} else {
						anioa6 = anioact;
					}						
				} else if (idtipoTabla.equals(Constantes.ID_TIPO_TABLA_ACTIVIDAD.toString())){
					if (strOrden.equals("0")) {
						tituloActividad = anioact;
					}
				}			
			}
		}
		
		//fin MCG20111026
		
		
		
		
		//Cargamos la fecha 
		int rowY=3, colX	= 4;
		row 			= hoja.getRow(rowY);
		row.getCell(0).setCellValue(tituloNegocio);
		row.getCell(colX).setCellValue(anioa1);
		row.getCell(colX+1).setCellValue(anioa2);
		row.getCell(colX+2).setCellValue(anioa3);
		
		colX	= 7;
		row.getCell(colX).setCellValue(anioa4);
		row.getCell(colX+1).setCellValue(anioa5);
		row.getCell(colX+2).setCellValue(anioa6);
		
		List<Map> listaDatosLinActiv 	= obtenerDataPorTipo(listaDatosNegoBenef,2);//ahora se utilizara para negocio
		List<Map> listaDatosLinNegocio  = obtenerDataPorTipo(listaDatosNegoBenef,1);//ahora se utilizara para actividad
		
		//0,4,5
		rowStyle = hoja.getRow(5);
		HSSFCellStyle styleColDes = rowStyle.getCell(0).getCellStyle();
		HSSFCellStyle styleColVal = rowStyle.getCell(4).getCellStyle();
		
		//Lineas de Actividad
		int tamanio = listaDatosLinActiv!=null?listaDatosLinActiv.size():0;
		int newRowsLineActiv= (tamanio-4) >0 ? (tamanio-4):0;
		rowY=4;
		colX=0;
		if(tamanio > 4){
			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsLineActiv);
			crearRows(hoja,rowY+1,newRowsLineActiv);
		}
		//boolean flagx=false;

		for(Map data:listaDatosLinActiv){
			
			row=hoja.getRow(rowY);
			if(row==null){
				row	= hoja.createRow(rowY);	
				crearCols(row,10);
			}else{
				if(row.getCell(colX)==null) crearCols(row,10);
			}
        		if(newRowsLineActiv > 0)
        			hoja.addMergedRegion(new CellRangeAddress(rowY,rowY,0,3));
        		 
	        	row.getCell(colX).setCellValue((String)data.get("DESCRIP_NEGO_BENEF"));
	        	row.getCell(colX).setCellStyle(styleColDes);
	        	
	    		 totali1	+=	Double.parseDouble((data.get("TOTAL_I1")!=null)?data.get("TOTAL_I1").toString():"0");
	    		 totali2	+=	Double.parseDouble((data.get("TOTAL_I2")!=null)?data.get("TOTAL_I2").toString():"0");
	    		 totali3	+=	Double.parseDouble((data.get("TOTAL_I3")!=null)?data.get("TOTAL_I3").toString():"0");
	    		 totalb1	+=	Double.parseDouble((data.get("TOTAL_B1")!=null)?data.get("TOTAL_B1").toString():"0");
	    		 totalb2	+=	Double.parseDouble((data.get("TOTAL_B2")!=null)?data.get("TOTAL_B2").toString():"0");
	    		 totalb3	+=	Double.parseDouble((data.get("TOTAL_B3")!=null)?data.get("TOTAL_B3").toString():"0");
	    		
		        colX=colX+4;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I1")!=null)?data.get("TOTAL_I1").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I2")!=null)?data.get("TOTAL_I2").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I3")!=null)?data.get("TOTAL_I3").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B1")!=null)?data.get("TOTAL_B1").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B2")!=null)?data.get("TOTAL_B2").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B3")!=null)?data.get("TOTAL_B3").toString():"");
	        	row.getCell(colX).setCellStyle(styleColVal);

        	rowY++;
        	colX=0;
        	
        	/*if(!flagx)
        		comentValorGlobal =(data.get("DB_VALORACION_GLOBAL")!=null)?data.get("DB_VALORACION_GLOBAL").toString():""; 
        	else
        		flagx=true;*/

		}
		//Llenamos los totales
		rowTot1    	 = rowTot1 + newRowsLineActiv ;
		colX		 = 4;

		row		   = hoja.getRow(rowTot1);
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali1).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali2).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali3).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb1).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb2).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb3).toString());
    	
    	
    	//TITULO DE NEGOCIO
		rowY				=	9+newRowsLineActiv;
    	colX				=	0;   	
		row 			= hoja.getRow(rowY);
		row.getCell(0).setCellValue(tituloActividad);
    	
		
		 totali1 = 0d;
		 totali2 = 0d;
		 totali3 = 0d;
		 totalb1 = 0d;
		 totalb2 = 0d;
		 totalb3 = 0d;
		
		//Lineas de Negocio
		tamanio 			= 	listaDatosLinNegocio!=null?listaDatosLinNegocio.size():0;
		int newRowsLineNeg	=	(tamanio-3) > 0?(tamanio-3):0;
		rowY				=	10+newRowsLineActiv;
    	colX				=	0;
    	if(tamanio > 3){
			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsLineNeg);
			crearRows(hoja,rowY+1,newRowsLineNeg);
		}
		for(Map data:listaDatosLinNegocio){
			
			row	=	hoja.getRow(rowY);
			if(row==null){
				row	= hoja.createRow(rowY);	
				crearCols(row,10);
			}else{
				if(row.getCell(colX)==null) crearCols(row,10);
			}
        		if(newRowsLineNeg > 0)
    				hoja.addMergedRegion(new CellRangeAddress(rowY,rowY,0,3));
        		 
        		if(row.getCell(colX)== null){
        			row = hoja.createRow(colX);
        		}
	        	row.getCell(colX).setCellValue((String)data.get("DESCRIP_NEGO_BENEF"));
	        	row.getCell(colX).setCellStyle(styleColDes);

	    		 totali1	+=	Double.parseDouble((data.get("TOTAL_I1")!=null)?data.get("TOTAL_I1").toString():"0");
	    		 totali2	+=	Double.parseDouble((data.get("TOTAL_I2")!=null)?data.get("TOTAL_I2").toString():"0");
	    		 totali3	+=	Double.parseDouble((data.get("TOTAL_I3")!=null)?data.get("TOTAL_I3").toString():"0");
	    		 totalb1	+=	Double.parseDouble((data.get("TOTAL_B1")!=null)?data.get("TOTAL_B1").toString():"0");
	    		 totalb2	+=	Double.parseDouble((data.get("TOTAL_B2")!=null)?data.get("TOTAL_B2").toString():"0");
	    		 totalb3	+=	Double.parseDouble((data.get("TOTAL_B3")!=null)?data.get("TOTAL_B3").toString():"0");
	    		 
		        colX=colX+4;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I1")!=null)?data.get("TOTAL_I1").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I2")!=null)?data.get("TOTAL_I2").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_I3")!=null)?data.get("TOTAL_I3").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B1")!=null)?data.get("TOTAL_B1").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B2")!=null)?data.get("TOTAL_B2").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);
	        	colX++;
	        	row.getCell(colX).setCellValue((data.get("TOTAL_B3")!=null)?data.get("TOTAL_B3").toString():"0");
	        	row.getCell(colX).setCellStyle(styleColVal);

        	rowY++;
        	colX=0;
		}
		//Llenamos los totales
		
		rowTot2    = rowTot2 + newRowsLineActiv + newRowsLineNeg;
		row=hoja.getRow(rowTot2);
		colX=4;
		
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali1).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali2).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totali3).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb1).toString());
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb2).toString());
    	colX++;
    	if(row.getCell(colX)!=null)
    		row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totalb3).toString());
		

		//String comentNegBenef="";
		//int y=newRowsLineNeg+newRowsLineActiv;
		//crearTextBox(hoja,comentNegBenefx, 29+y,0,29+y+7 ,7);
		
		//Mostramos valor Global
    	datos.put("nuevaPosEspacioLibre", 16 + newRowsLineActiv + newRowsLineNeg );
    	int filax=47 + newRowsLineActiv + newRowsLineNeg;
		colX=0;
		row=(hoja.getRow(filax)==null)?hoja.createRow(filax):hoja.getRow(filax);
		if(row.getCell(colX)==null) row.createCell(colX);
    	row.getCell(colX).setCellValue(comentValorGlobal);
    	
	}
	
	
	private List obtenerDataPorTipo(List<Map> listaDatosBasicos, int tipo){
		List<Map>  listaData= new ArrayList<Map> ();
		
		for(Map data:listaDatosBasicos){
			
			Object obj=data.get("TIPO");
			String bloqx= obj.toString(); // (String)data.get("BLOQUE");
			Integer bloq=Integer.parseInt(bloqx);
			if(bloq.intValue()==tipo){
				listaData.add(data);
			}
		}
		
		return listaData;
	}

	private List obtenerDataPorSeccion(List<Map> listaDatosBasicos, int bloque){
		List<Map>  listaData= new ArrayList<Map> ();
		
		for(Map data:listaDatosBasicos){
			
			Object obj=data.get("BLOQUE");
			String bloqx= obj.toString(); 
			Integer bloq=Integer.parseInt(bloqx);
			if(bloq.intValue()==bloque){
				listaData.add(data);
			}
		}
		
		return listaData;
	}


}
