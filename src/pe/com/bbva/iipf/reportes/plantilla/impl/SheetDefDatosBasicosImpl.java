package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.util.StringUtil;

public class SheetDefDatosBasicosImpl extends SheetDefinitionBase implements SheetDefinition{
	Logger logger = Logger.getLogger(this.getClass());
	private int newRowsAcciona		 = 0;
	private int newRowsCapBursatil	 = 0;
	private int newRowsPartSig		 = 0;
	private int newRowsRatig		 = 0;
	private static int POS_INICIAL = 12;

	private HSSFRow  rowAux	  		 = null;
	
	private HSSFCellStyle cellStyle1= null;
	private HSSFCellStyle cellStyle2= null;
	private HSSFCellStyle cellStyle3= null;
	private HSSFCellStyle cellStyle4= null;
	private HSSFCellStyle cellStyle5= null;
	private HSSFCellStyle cellStyle6= null;
	private HSSFCellStyle cellStyle7= null;

	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
		
        
		//super.setPatriarch1(hoja.createDrawingPatriarch());
        cargarDatosIdentificacion( hoja, datos);
        cargarDatosAccionariado(hoja, datos);
        cargarDatosCapitalBursatil(hoja, datos);
        cargarDatosPartSignificativas(hoja,datos);
        cargarDatosRatingExterno(hoja,datos);
        cargarDatosCompraVenta(hoja,datos);
        
        hoja.setForceFormulaRecalculation(true);
		
	}
	
	private void cargarDatosIdentificacion(HSSFSheet hoja,Map datos){

		Programa programax = (Programa)datos.get("programax");
		
		int rowY=0;
		int colX=0;
		Map registro	=null;
		HSSFRow row		=null;
			
		String tipoEmpres=programax.getTipoEmpresa().getId().toString(); 
		if(tipoEmpres.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){		   
			//Empresa Datos Basicos
		    
		    String nombreEmpresaDatosBasicos = datos.get("nombEmpreesaDatosBasicos")==null?"":datos.get("nombEmpreesaDatosBasicos").toString();
			String strNombreEmpresa=nombreEmpresaDatosBasicos;
			rowY=5;colX=1;
			row = hoja.getRow(rowY);
			row.getCell(colX).setCellValue(strNombreEmpresa);
		    
		} 
			//Actividad Principal
			String activPricipal=programax.getActividadPrincipal();// (String)registro.get("DB_ACTIVIDAD_PRIN");
			rowY=6;colX=1;
			row = hoja.getRow(rowY);
			row.getCell(colX).setCellValue(activPricipal);
			
			//Pais Cabecera:
			String paisCab 	=programax.getPais();// (String)registro.get("DB_T_EXT_PAIS_CODIGO");
			rowY=7; colX	=1;
			row 			= hoja.getRow(rowY);
			row.getCell(colX).setCellValue(paisCab);
			
			//Antiguedad Negocios
			String antNeg 	= (programax.getAntiguedadNegocio()!=null)?programax.getAntiguedadNegocio().toString():"";
			rowY=7;	colX	= 2;
			row 			= hoja.getRow(rowY);
			String valor	= row.getCell(colX).getStringCellValue();
			row.getCell(colX).setCellValue(valor+" "+antNeg);
			
			//Antiguedad Cliente
			String antCli	= (programax.getAntiguedadCliente()!=null)?programax.getAntiguedadCliente().toString():"";
			rowY=7; colX	= 5;
			row 			= hoja.getRow(rowY);
			valor			= row.getCell(colX).getStringCellValue();
			row.getCell(colX).setCellValue(valor+" "+antCli);

			
		//Plantilla
		List<Map> listaDatosSecc = obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),2);
				
		//Cabecera Planilla
		//ini MCG20111026
		List<Map> listaDatosSeccCab = obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),8);
	    //fin MCG20111026

		if(listaDatosSecc!=null && !listaDatosSecc.isEmpty()){
			
			Integer annioProg=programax.getAnio();
			//ini MCG20111026
			String anio1=" " + String.valueOf(annioProg-3);
			String anio2=" " + String.valueOf(annioProg-2);
			String anio3=" " + String.valueOf(annioProg-1);
			String anio4=" " + String.valueOf(annioProg);			
			
			if(listaDatosSeccCab!=null && !listaDatosSeccCab.isEmpty()){
				for(Map datap:listaDatosSeccCab){
					String idtipoTabla=(datap.get("TT_ID_TIPO_TABLA")!=null)?datap.get("TT_ID_TIPO_TABLA").toString():"";
					String strOrden=datap.get("ORDEN").toString();
					String aniop=(datap.get("NOMBRE_CABTABLA")!=null)?datap.get("NOMBRE_CABTABLA").toString():"";
					
					if(idtipoTabla.equals(Constantes.ID_TIPO_TABLA_PLANILLA.toString())){						
						if (strOrden.equals("1")) {
							anio1 = aniop;
						} else if (strOrden.equals("2")) {
							anio2 = aniop;
						} else if (strOrden.equals("3")) {
							anio3 = aniop;
						} else if (strOrden.equals("4")) {
							anio4 = aniop;
						} else {
							anio4 = aniop;
						}							
					}			
				}
			}
			//fin MCG20111026
			
			registro	   = listaDatosSecc.get(0);
			
			rowY=8; colX	= 1;
			row 			= hoja.getRow(rowY);
			row.getCell(colX).setCellValue  (anio1);
			row.getCell(colX+1).setCellValue(anio2);
			row.getCell(colX+2).setCellValue(anio3);
			row.getCell(colX+3).setCellValue(anio4);
			
			rowY=9; colX	= 1;
			row 			= hoja.getRow(rowY);
			
			row.getCell(colX).setCellValue  ((registro.get("PTOTAL1")!=null)?registro.get("PTOTAL1").toString():"");
			row.getCell(colX+1).setCellValue((registro.get("PTOTAL2")!=null)?registro.get("PTOTAL2").toString():"");
			row.getCell(colX+2).setCellValue((registro.get("PTOTAL3")!=null)?registro.get("PTOTAL3").toString():"");
			row.getCell(colX+3).setCellValue((registro.get("PTOTAL4")!=null)?registro.get("PTOTAL4").toString():"");
		}

        	
		//NO hay data para mostrar en la caja de texto que va despues de planilla.
		//crearTextBox(hoja," NO SE SABE QUE DATA VA AQUI. ", 11,0,15 ,7);
		///////////////////////////////////////////////////////////////////////////////////
		//Codigo Boureau
		rowY=11; colX	= 1;
		row 			= hoja.getRow(rowY);
		row.getCell(colX).setCellValue  ((programax.getGrupoRiesgoBuro()!=null)?programax.getGrupoRiesgoBuro().toString():"");
		
		
	}

	private void cargarDatosAccionariado(HSSFSheet hoja,Map datos){
		try {
			List<Map> listaDatosSecc=obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),3);
			Programa programax = (Programa)datos.get("programax");
			String comentAccionar ="";
			int rowY			  =19+POS_INICIAL;
			int colX			  =0;
			int contador		  =1;
			HSSFRow row			  = null;
	
			int tamanio = listaDatosSecc!=null?listaDatosSecc.size():0;
			newRowsAcciona= (tamanio-4)> 0?(tamanio-4):0;
			if(tamanio > 4){
				//Obtenemos el formato de una celda.
				 rowAux		=hoja.getRow(rowY+1);
				 cellStyle1	= rowAux.getCell(0).getCellStyle();
				 cellStyle2	= rowAux.getCell(5).getCellStyle();
				 cellStyle3	= rowAux.getCell(6).getCellStyle();
				 cellStyle4	= rowAux.getCell(7).getCellStyle();
				//Insertamos Filas nuevas.
				insertarRows(hoja,rowY+1,newRowsAcciona);
				List<HSSFRow> rowsNew =crearRows(hoja,rowY+1,newRowsAcciona);
				int cont=1;
				for(HSSFRow  rowz: rowsNew){
				  	cont++;
					crearCols(rowz,8);
					hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),0,4));
					asignarEstilosAccionariado(rowz,8);
	    		}
			}
			for(Map data:listaDatosSecc){
				
				row=hoja.getRow(rowY);
	
	        		colX=0;
	        		
	        		if(row.getCell(colX)== null){
	        			row.createCell(colX);
	        		}
		        	row.getCell(colX).setCellValue((String)data.get("NOMBRES_ACCI"));
	        			
		        	colX=colX+5;
	        		if(row.getCell(colX)== null){
	        			row.createCell(colX);
	        		}
		        	row.getCell(colX).setCellValue((data.get("PORCENTAJE")!=null)?data.get("PORCENTAJE").toString():"");
		        	colX++;
	        		if(row.getCell(colX)== null){
	        			row.createCell(colX);
	        		}
		        	row.getCell(colX).setCellValue((data.get("NACIONALIDAD")!=null)?data.get("NACIONALIDAD").toString():"");
		        	colX++;
	        		if(row.getCell(colX)== null){
	        			row.createCell(colX);
	        		}
		        	row.getCell(colX).setCellValue((data.get("CAPITALIZACION_BURS")!=null)?data.get("CAPITALIZACION_BURS").toString():"");
	
	        	if(contador ==1)
	        	comentAccionar=(String)data.get("DB_COMEN_ACCI");
	        	
	        	rowY++;
	        	colX=0;
				contador++;
				
	
			}
			comentAccionar=(programax.getComentAccionariado()!=null)?programax.getComentAccionariado().toString():"";
			
			int y=newRowsAcciona+POS_INICIAL;
			//crearTextBox(hoja,comentAccionar, 24+y,0,24+y+7 ,7);
			
			//Comentario Accionariado
			colX=0;
			row=(hoja.getRow(25+y)==null)?hoja.createRow(25+y):hoja.getRow(25+y);
			if(row.getCell(colX)==null) row.createCell(colX);
	    	row.getCell(colX).setCellValue(comentAccionar);
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	//ini MCG20130820
	private void cargarDatosCapitalBursatil(HSSFSheet hoja,Map datos){
		
		List<Map> listaDatosCapitalBursatil	=	obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),9);
		Programa programax = (Programa)datos.get("programax");
		String comentPartSignif		=	"";
		int rowY					=	34+newRowsAcciona+POS_INICIAL;
		int rowYAux					=	34+newRowsAcciona+POS_INICIAL;
		int colX					=	0;
		int contador				=	1;
		HSSFRow row					=	null;
		
		int tamanio 				= listaDatosCapitalBursatil!=null?listaDatosCapitalBursatil.size():0;
		
		newRowsCapBursatil			= (tamanio-4) > 0?(tamanio-4):0; 
		if(tamanio > 4){
			//Insertamos Filas nuevas.
			//Obtenemos el formato de una celda.
			rowAux=hoja.getRow(rowY+1);
			
			 cellStyle1	= rowAux.getCell(0).getCellStyle();
			 cellStyle2	= rowAux.getCell(1).getCellStyle();
			 cellStyle3	= rowAux.getCell(2).getCellStyle();
			 cellStyle4	= rowAux.getCell(3).getCellStyle();
			 cellStyle5	= rowAux.getCell(4).getCellStyle();
			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsCapBursatil);
			List<HSSFRow> rowsNew =crearRows(hoja,rowY+1,newRowsCapBursatil);
			int cont=1;
			for(HSSFRow  rowz: rowsNew){
			  	cont++;
				crearCols(rowz,8);
				//hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),0,3));
    			hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),4,7));
    			asignarEstilosCapitalBursatil(rowz);
    		}

		}
		for(Map data:listaDatosCapitalBursatil){
			
			row=hoja.getRow(rowY);
        		colX=0;
        		if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((String)data.get("DIVISA"));
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	           	row.getCell(colX).setCellValue((data.get("IMPORTE_CB")!=null)?data.get("IMPORTE_CB").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("FONDOS_PROPIOS")!=null)?data.get("FONDOS_PROPIOS").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	         	row.getCell(colX).setCellValue((data.get("FECHA_CB")!=null)?data.get("FECHA_CB").toString():"");
	         	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	         	row.getCell(colX).setCellValue((data.get("OBSERVACION_CB")!=null)?data.get("OBSERVACION_CB").toString():"");

        	        	
        	rowY++;
        	colX=0;
			contador++;
			

		}
	
	}
	//fin MCG20130820
	
	private void cargarDatosPartSignificativas(HSSFSheet hoja,Map datos){
		
		List<Map> listaDatosPartSig	=	obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),4);
		Programa programax = (Programa)datos.get("programax");
		String comentPartSignif		=	"";
		int rowY					=	34+newRowsCapBursatil+newRowsAcciona+POS_INICIAL +8;
		int rowYAux					=	34+newRowsCapBursatil+newRowsAcciona+POS_INICIAL+8;
		int colX					=	0;
		int contador				=	1;
		HSSFRow row					=	null;
		
		int tamanio 				= listaDatosPartSig!=null?listaDatosPartSig.size():0;
		
		newRowsPartSig			= (tamanio-4) > 0?(tamanio-4):0;
		if(tamanio > 4){
			//Insertamos Filas nuevas.
			//Obtenemos el formato de una celda.
			rowAux=hoja.getRow(rowY+1);
			
			 cellStyle1	= rowAux.getCell(0).getCellStyle();
			 cellStyle2	= rowAux.getCell(4).getCellStyle();
			 cellStyle3	= rowAux.getCell(5).getCellStyle();
			 cellStyle4	= rowAux.getCell(6).getCellStyle();
			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsPartSig);
			List<HSSFRow> rowsNew =crearRows(hoja,rowY+1,newRowsPartSig);
			int cont=1;
			for(HSSFRow  rowz: rowsNew){
			  	cont++;
				crearCols(rowz,8);
				hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),0,3));
    			hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),6,7));
				asignarEstilosPartSignific(rowz);
    		}

		}
		for(Map data:listaDatosPartSig){
			
			row=hoja.getRow(rowY);
        		colX=0;
        		if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((String)data.get("NOMBRE_AFILIADO"));
	        	colX=colX+4;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	           	row.getCell(colX).setCellValue((data.get("PORCENTAJE2")!=null)?data.get("PORCENTAJE2").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("CONSOLIDACION")!=null)?data.get("CONSOLIDACION").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	         	row.getCell(colX).setCellValue((data.get("SECTOR_ACTI")!=null)?data.get("SECTOR_ACTI").toString():"");

        	if(contador ==1)
        		comentPartSignif =(String)data.get("DB_COMEN_PART_SIGN");
        	
        	rowY++;
        	colX=0;
			contador++;
			

		}
		
		comentPartSignif=(programax.getComentPartiSignificativa()!=null)?programax.getComentPartiSignificativa().toString():"";
		//int y=newRowsAcciona+newRowsPartSig;
		//crearTextBox(hoja,comentPartSignif, 39+y,0,39+y+7 ,7);
		
		/////////////////////////
		int filax = rowYAux;
		if(contador>4){
			filax=rowY+1;
		}else{
			filax=filax+5;
		}
		
		colX=0;
		row=(hoja.getRow(filax)==null)?hoja.createRow(filax):hoja.getRow(filax);
		if(row.getCell(colX)==null) row.createCell(colX);
    	row.getCell(colX).setCellValue(comentPartSignif);
    	
    	//System.out.println(" HFQV newRowsAcciona: "+newRowsAcciona+" newRowsPartSig: "+newRowsPartSig);
    	//System.out.println(" HFQV filax: "+filax);
    	//System.out.println(" HFQV  comentPartSignif: "+comentPartSignif);

	}
	private void cargarDatosRatingExterno(HSSFSheet hoja,Map datos){
		
		List<Map> listaDatosRatExt	= obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),7);
		Programa programax = (Programa)datos.get("programax");
		String comentDatRatExt		= "";
		int rowY					= 49+newRowsPartSig+newRowsCapBursatil+newRowsAcciona+POS_INICIAL +8;
		int rowYAux					= 49+newRowsPartSig+newRowsCapBursatil+newRowsAcciona+POS_INICIAL +8;
		
		int colX					= 0;
		int contador				= 1;
		HSSFRow row					= null;
		
		int tamanio = listaDatosRatExt!=null?listaDatosRatExt.size():0;
		newRowsRatig= (tamanio-3) >0 ?(tamanio-3):0;
		if(tamanio > 3){
			rowAux=hoja.getRow(rowY+1);
			
			 cellStyle1	= rowAux.getCell(0).getCellStyle();
			 cellStyle2	= rowAux.getCell(2).getCellStyle();
			 cellStyle3	= rowAux.getCell(3).getCellStyle();
			 cellStyle4	= rowAux.getCell(4).getCellStyle();
			 cellStyle5	= rowAux.getCell(5).getCellStyle();
			 cellStyle6	= rowAux.getCell(6).getCellStyle();
			 cellStyle7	= rowAux.getCell(7).getCellStyle();
			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsRatig);
			List<HSSFRow> rowsNew =crearRows(hoja,rowY+1,newRowsRatig);
			int cont=1;
			for(HSSFRow  rowz: rowsNew){
			  	cont++;
				crearCols(rowz,8);
				hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),0,1));
			  	asignarEstilosRating(rowz);
    		}
		}
		for(Map data:listaDatosRatExt){
			
			row=hoja.getRow(rowY); 
        		colX=0;
        		if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((String)data.get("COMPANIA_GRUP"));
	        	colX=colX+2;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("AGENCIA")!=null)?data.get("AGENCIA").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("CP")!=null)?data.get("CP").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("LP")!=null)?data.get("LP").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("OUTLOOK")!=null)?data.get("OUTLOOK").toString():"");
	        	colX++;
	        	if(row.getCell(colX)== null){
        			row.createCell(colX);
        		}
	        	row.getCell(colX).setCellValue((data.get("MONEDA")!=null)?data.get("MONEDA").toString():"");
	        	colX++;
	        	String fechaDDYY=(String)data.get("FECHA");
	        	if(fechaDDYY!=null){
	        		fechaDDYY=fechaDDYY.substring(3, fechaDDYY.length());
	        		row.getCell(colX).setCellValue(fechaDDYY);
	        	}
        	/*if(contador ==1)
        		comentDatRatExt =(String)data.get("DB_COMEN_PART_SIGN");*/
        	
        	rowY++;
        	colX=0;
			contador++;
			
		}
		//int y=newRowsAcciona+newRowsPartSig+newRowsRatig;
		//crearTextBox(hoja,comentDatRatExt, 53+y,0,53+y+7 ,7);

		/////////////////////////
		
		int filax = rowYAux;
		if(contador>3){
			filax=rowY+1;
		}else{
			filax=filax+4;
		}
		
		//comentDatRatExt=programax.getComentRatinExterno();
		comentDatRatExt=(programax.getComentRatinExterno()!=null)?programax.getComentRatinExterno().toString():"";
		colX=0;
		row=(hoja.getRow(filax)==null)?hoja.createRow(filax):hoja.getRow(filax);
		if(row.getCell(colX)==null) row.createCell(colX);
    	row.getCell(colX).setCellValue(comentDatRatExt);
    	
    	//System.out.println(" HFQV newRowsAcciona: "+newRowsAcciona+" newRowsPartSig: "+newRowsPartSig+" newRowsRatig: "+newRowsRatig);
    	//System.out.println(" HFQV rowY: "+rowY);
    	//System.out.println(" xxxxxxxxxxHFQV  comentDatRatExt: "+comentDatRatExt);
	}
	
	private void cargarDatosCompraVenta(HSSFSheet hoja,Map datos){
		//System.out.println(" cargarDatosCompraVenta HFQV  newRowsAcciona: "+newRowsAcciona+" newRowsPartSig: "+newRowsPartSig+" newRowsRatig: "+newRowsRatig);
		List<Map> listaDatosCompVent= obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),5);
		int rowY					= 64 + newRowsPartSig + newRowsCapBursatil+newRowsAcciona + newRowsRatig+POS_INICIAL +8;
		//int rowY					= 67 + newRowsPartSig + newRowsAcciona+newRowsRatig;
		int colX					= 0;
		int contador				= 1;
		boolean vf					= false;
		HSSFRow row					= null;
		String tot1="";
		String tot2="";
		String tot3="";
		
	    //Cabecera Compra Venta
	    //ini MCG20111026
		List<Map> listaDatosSeccCab = obtenerDataPorSeccion((List<Map>) datos.get("listaDatosBasicos"),8);
	    //fin MCG20111026
		
		//complentado titulos de periodos 
		int anio = Integer.parseInt(datos.get("annioProg").toString());			

		//ini MCG20111026
		String anioc1="Dic-" + String.valueOf(anio-2);
		String anioc2="Dic-" + String.valueOf(anio-1);
		String anioc3="Dic-" + String.valueOf(anio);
		
		String aniov1="Dic-" + String.valueOf(anio-2);
		String aniov2="Dic-" + String.valueOf(anio-1);
		String aniov3="Dic-" + String.valueOf(anio);
		
		
		if(listaDatosSeccCab!=null && !listaDatosSeccCab.isEmpty()){
			for(Map datap:listaDatosSeccCab){
				String idtipoTabla=(datap.get("TT_ID_TIPO_TABLA")!=null)?datap.get("TT_ID_TIPO_TABLA").toString():"";
				String strOrden=datap.get("ORDEN").toString();
				String aniop=(datap.get("NOMBRE_CABTABLA")!=null)?datap.get("NOMBRE_CABTABLA").toString():"";
				//Para Compra
				if(idtipoTabla.equals(Constantes.ID_TIPO_TABLA_COMPRA.toString())){						
					if (strOrden.equals("1")) {
						anioc1 = aniop;
					} else if (strOrden.equals("2")) {
						anioc2 = aniop;
					} else if (strOrden.equals("3")) {
						anioc3 = aniop;					
					} else {
						anioc3 = aniop;
					}	
				// Para Venta
				}else if (idtipoTabla.equals(Constantes.ID_TIPO_TABLA_VENTA.toString())) {
					if (strOrden.equals("1")) {
						aniov1 = aniop;
					} else if (strOrden.equals("2")) {
						aniov2 = aniop;
					} else if (strOrden.equals("3")) {
						aniov3 = aniop;					
					} else {
						aniov3 = aniop;
					}						
				} 			
			}
		}
		
		//fin MCG20111026
		

		row=hoja.getRow(rowY-1);
		
		if(row == null){
			row = hoja.createRow(rowY-1);
		}
		
		if(row.getCell(1)== null){
			row.createCell(1);
		}
		row.getCell(1).setCellValue(anioc1);
		if(row.getCell(2)== null){
			row.createCell(2);
		}
		row.getCell(2).setCellValue(anioc2);
		if(row.getCell(3)== null){
			row.createCell(3);
		}
		row.getCell(3).setCellValue(anioc3);
		
		row=hoja.getRow(rowY+4);
		if(row.getCell(1)== null){
			row.createCell(1);
		}
		row.getCell(1).setCellValue(aniov1);
		if(row.getCell(2)== null){
			row.createCell(2);
		}
		row.getCell(2).setCellValue(aniov2);
		if(row.getCell(3)== null){
			row.createCell(3);
		}
		row.getCell(3).setCellValue(aniov3);
		
		for(Map data:listaDatosCompVent){
			//System.out.println(" cargarDatosCompraVenta HFQV  rowY: "+rowY);
			row=hoja.getRow(rowY);
			
			if(row==null){
				row	= hoja.createRow(rowY);	
				crearCols(row,8);
			}else{
				if(row.getCell(colX)==null){
					crearCols(row,8);
				}
			}
        		colX=1;
        		 tot1=(data.get("TOTAL1")!=null)?data.get("TOTAL1").toString():"";
 	        	if(row.getCell(colX)==null) row.createCell(colX);
	        	row.getCell(colX).setCellValue(tot1);
	        	colX++;
	        	tot2=(data.get("TOTAL2")!=null)?data.get("TOTAL2").toString():"";
	        	if(row.getCell(colX)==null) row.createCell(colX);
	        	row.getCell(colX).setCellValue(tot2);
	        	colX++;
	        	tot3=(data.get("TOTAL3")!=null)?data.get("TOTAL3").toString():"";
	        	if(row.getCell(colX)==null) row.createCell(colX);
	        	row.getCell(colX).setCellValue(tot3);
	        	
        	if(contador < 2){
        	 rowY++;
        	}else{
        		if(vf==false){
               	 rowY=rowY+4;
        		}else{
               	 rowY++;
        		}
        		vf=true;
        	}
        	colX=0;
			contador++;
			

		}

		//String comentConcentracion="";
		//int y=(newRowsAcciona+newRowsPartSig+newRowsRatig)>0?(newRowsAcciona+newRowsPartSig+newRowsRatig):0;
		//crearTextBox(hoja,comentConcentracion, 76+y,0,76+y+7 ,7);
		// MIL datos.put("nuevaPosComenCompVent", 85 + newRowsPartSig + newRowsAcciona + newRowsRatig);
		datos.put("nuevaPosComenCompVent", 89 + newRowsPartSig + newRowsCapBursatil + newRowsAcciona + newRowsRatig +8);
	}
	
	
	private List obtenerDataPorSeccion(List<Map> listaDatosBasicos, int bloque){
		List<Map>  listaData= new ArrayList<Map> ();
		if (listaDatosBasicos!=null && listaDatosBasicos.size()>0){
			for(Map data:listaDatosBasicos){
				
				Object obj=data.get("BLOQUE");
				String bloqx= obj.toString(); // (String)data.get("BLOQUE");
				Integer bloq=Integer.parseInt(bloqx);
				if(bloq.intValue()==bloque){
					listaData.add(data);
				}
			}
		}
		return listaData;
	}
	
  private void asignarEstilosAccionariado(HSSFRow rowz,int numCols){
	  
	  for(int i=0;i<5;i++){
		    rowz.getCell(i).setCellStyle(cellStyle1);
		    rowz.getCell(i).getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
		    rowz.getCell(i).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
	  }
			    rowz.getCell(5).setCellStyle(cellStyle2);
			    rowz.getCell(6).setCellStyle(cellStyle3);
			    rowz.getCell(7).setCellStyle(cellStyle4);
 }
  private void asignarEstilosCapitalBursatil(HSSFRow rowz){
	  
	    rowz.getCell(0).setCellStyle(cellStyle1);
	    rowz.getCell(0).getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
	    rowz.getCell(0).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    
	    rowz.getCell(1).setCellStyle(cellStyle2);
	    rowz.getCell(2).setCellStyle(cellStyle3);
	    rowz.getCell(3).setCellStyle(cellStyle4);
	    rowz.getCell(4).setCellStyle(cellStyle5);
}
  
  private void asignarEstilosPartSignific(HSSFRow rowz){
	  
	    rowz.getCell(0).setCellStyle(cellStyle1);
	    rowz.getCell(0).getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
	    rowz.getCell(0).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    
	    rowz.getCell(4).setCellStyle(cellStyle2);
	    rowz.getCell(5).setCellStyle(cellStyle3);
	    rowz.getCell(6).setCellStyle(cellStyle4);
}
  
  private void asignarEstilosRating(HSSFRow rowz){
	  
	    rowz.getCell(0).setCellStyle(cellStyle1);
	    rowz.getCell(0).getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
	    rowz.getCell(0).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    
	    rowz.getCell(2).setCellStyle(cellStyle2);
	    rowz.getCell(3).setCellStyle(cellStyle3);
	    rowz.getCell(4).setCellStyle(cellStyle4);
	    rowz.getCell(5).setCellStyle(cellStyle5);
	    rowz.getCell(6).setCellStyle(cellStyle6);
	    rowz.getCell(7).setCellStyle(cellStyle7);
}
  
}
