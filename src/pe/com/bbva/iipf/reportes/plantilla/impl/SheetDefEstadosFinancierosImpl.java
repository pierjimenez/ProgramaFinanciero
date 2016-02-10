package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import pe.com.bbva.iipf.pf.model.FilaEmpresaHoja;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.bbva.iipf.util.Constantes;

public class SheetDefEstadosFinancierosImpl extends SheetDefinitionBase implements SheetDefinition{

	Logger logger = Logger.getLogger(this.getClass());
	private FormulaEvaluator fEval=null;
	
	
	public void fillDataSheet_ORIG(HSSFSheet hoja, Map datos) throws Exception {
		
        
 	    List<String> listaWBEmpPrograma	= (List<String>)datos.get("listaWbProg");
 	    Integer numEpresas=(Integer)datos.get("numEpresas");
 	    int tamanio=(listaWBEmpPrograma!=null)?listaWBEmpPrograma.size():0;
 	    logger.info("tamanio>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+tamanio);
 	    logger.info("dtipoPrograma"+datos.get("tipoEmpresa").toString());
 	    if(tamanio >0){
 	    	//Epomayay 12/032012
 	    	boolean noEsElPrimero = false;
 	    	HSSFWorkbook wbPFDataGrupoOEmprPrinc=null;
 	    	FileInputStream fis = null;
 	    	if(Long.parseLong(datos.get("tipoEmpresa").toString())==Constantes.ID_TIPO_EMPRESA_EMPR){
 		 	    if(listaWBEmpPrograma!=null){
 		 	    	fis = new FileInputStream(listaWBEmpPrograma.get(0).toString());
	           		wbPFDataGrupoOEmprPrinc = new HSSFWorkbook(fis);
 		 	    	//Llenmamos data del Grupo o de la Empresa principal, en la primera hoja de Est. Finacioeros
 					fEval=wbPFDataGrupoOEmprPrinc.getCreationHelper().createFormulaEvaluator();
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
 		 	    }
 	    	}else{//si es grupo
 	    		if(listaWBEmpPrograma!=null &&
 	    		   Boolean.parseBoolean(datos.get("tieneSintesisEconoDeGrupo").toString())){
 		 	    	//wbPFDataGrupoOEmprPrinc = listaWBEmpPrograma.get(0);
 	    			fis = new FileInputStream(listaWBEmpPrograma.get(0).toString());
	           		wbPFDataGrupoOEmprPrinc = new HSSFWorkbook(fis);
 		 	    	//Llenmamos data del Grupo o de la Empresa principal, en la primera hoja de Est. Finacioeros
 					fEval=wbPFDataGrupoOEmprPrinc.getCreationHelper().createFormulaEvaluator();
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
 		 	    }else{
 		 	    	noEsElPrimero = true;
 		 	    }
 	    	}
 	    	// fin Epomayay 12/032012
	        
	        
	        if(tamanio >=1){
	        	
		 	    HSSFWorkbook wbReporte = hoja.getWorkbook();
	        	int contador = Constantes.INDEX_HOJA_INI_EMP_EF; //Posicion de la Hoja de Est. Financieros Empresas.
	        	int adicion= numEpresas>30?30:numEpresas;
	        	int limite = contador+adicion; 
	        	
	        	
	        	//Utilizaremos los nombres de hojas porque parece que hay hojas escondidas.
	        	String nombHojaDestino="Sintesis Econom-Financiero (";
	        	int indice = 2;
	        	HSSFWorkbook wb  = null;
	        	for(String urlExcel : listaWBEmpPrograma){
	        		if(noEsElPrimero){
	        			
		        		if(contador < limite){
		        			fis = new FileInputStream(urlExcel);
			           		wb = new HSSFWorkbook(fis);
			           		fEval=wb.getCreationHelper().createFormulaEvaluator();
		        		
		        			HSSFSheet hojaEF = wbReporte.getSheet(nombHojaDestino+indice+")");  //wbReporte.getSheetAt(contador);
		        			 
		        			llenarData(hojaEF,wb.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
		        		}	
		        		indice++;
		                contador++;
		                
	        		}else{
	        			noEsElPrimero=true;
	        			continue;
	        		}
	        	}
	        	
	        } 
	        completarRating( hoja,  datos);
 	    }
        
	}
	
	//ini MCG20130306
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) throws Exception {
		
        
 	   // List<String> listaWBEmpPrograma	= (List<String>)datos.get("listaWbProg");
 	    List<FilaEmpresaHoja> listaWBEmpPrograma	= (List<FilaEmpresaHoja>)datos.get("listaWbProg2");
 	    Integer numEpresas=(Integer)datos.get("numEpresas");
 	    int tamanio=(listaWBEmpPrograma!=null)?listaWBEmpPrograma.size():0;
 	    logger.info("tamanio>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+tamanio);
 	    logger.info("dtipoPrograma"+datos.get("tipoEmpresa").toString());
 	    if(tamanio >0){
 	    	//Epomayay 12/032012
 	    	boolean noEsElPrimero = false;
 	    	HSSFWorkbook wbPFDataGrupoOEmprPrinc=null;
 	    	FileInputStream fis = null;
 	    	if(Long.parseLong(datos.get("tipoEmpresa").toString())==Constantes.ID_TIPO_EMPRESA_EMPR){
 		 	    if(listaWBEmpPrograma!=null){
 		 	    	;
 		 	    	fis = new FileInputStream(listaWBEmpPrograma.get(0).getFileName().toString());
	           		wbPFDataGrupoOEmprPrinc = new HSSFWorkbook(fis);
 		 	    	//Llenmamos data del Grupo o de la Empresa principal, en la primera hoja de Est. Finacioeros
 					fEval=wbPFDataGrupoOEmprPrinc.getCreationHelper().createFormulaEvaluator();
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
 			        completarRating( hoja,  datos);
 			        
 		 	    }
 	    	}else{//si es grupo
 	    		if(listaWBEmpPrograma!=null &&
 	    		   Boolean.parseBoolean(datos.get("tieneSintesisEconoDeGrupo").toString())){
 		 	    	//wbPFDataGrupoOEmprPrinc = listaWBEmpPrograma.get(0);
 	    			fis = new FileInputStream(listaWBEmpPrograma.get(0).getFileName().toString());
	           		wbPFDataGrupoOEmprPrinc = new HSSFWorkbook(fis);
 		 	    	//Llenmamos data del Grupo o de la Empresa principal, en la primera hoja de Est. Finacioeros
 					fEval=wbPFDataGrupoOEmprPrinc.getCreationHelper().createFormulaEvaluator();
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
 			        completarRating( hoja,  datos);
 			        
 		 	    }else{
 		 	    	noEsElPrimero = true;
 		 	    }
 	    	}
 	    	// fin Epomayay 12/032012
	        
	        
	        if(tamanio >=1){
	        	
		 	    HSSFWorkbook wbReporte = hoja.getWorkbook();
	        	int contador = Constantes.INDEX_HOJA_INI_EMP_EF; //Posicion de la Hoja de Est. Financieros Empresas.
	        	int adicion= numEpresas>30?30:numEpresas;
	        	int limite = contador+adicion; 
	        	
	        	
	        	//Utilizaremos los nombres de hojas porque parece que hay hojas escondidas.
	        	String nombHojaDestino="Sintesis Econom-Financiero (";
	        	int indice = 2;
	        	HSSFWorkbook wb  = null;
	        	for(FilaEmpresaHoja urlExcel : listaWBEmpPrograma){
	        		if(noEsElPrimero){
	        			
		        		if(contador < limite){
		        			fis = new FileInputStream(urlExcel.getFileName());
			           		wb = new HSSFWorkbook(fis);
			           		fEval=wb.getCreationHelper().createFormulaEvaluator();
		        		    String strindexHoja=String.valueOf(urlExcel.getEmpresa().getIndexHoja());
		        		    String codEmpresa=String.valueOf(urlExcel.getEmpresa().getCodigo());
		        			HSSFSheet hojaEF = wbReporte.getSheet(nombHojaDestino+strindexHoja+")");  //wbReporte.getSheetAt(contador);
		        			 
		        			llenarData(hojaEF,wb.getSheetAt(Constantes.NUM_SHEET_FINANCIERO), datos);
		        			
		        		    List<Rating> rating = (List<Rating>)datos.get("rating_"+codEmpresa);
		        		    datos.remove("rating");
		        		    datos.put("rating", rating);
		        			completarRating( hojaEF,  datos);
		        			
		        		}	
		        		indice++;
		                contador++;
		                
	        		}else{
	        			noEsElPrimero=true;
	        			continue;
	        		}
	        	}
	        	
	        } 
	        //completarRating( hoja,  datos);
 	    }
        
	}
	//fin MCG20130306

	/**
	 * completa el rating registrado
	 */
	private void completarRating(HSSFSheet hoja, Map datos){
		
		 List<Rating> rating = (List<Rating>)datos.get("rating");
		 logger.info("listado Rating = "+rating);
		 if(rating != null &&
			rating.size()>0){
		 
			 //HSSFWorkbook workbook =  hoja.getWorkbook();
			 //HSSFSheet hojaEF = workbook.getSheet("Sintesis Econom-Financiero");
			 HSSFSheet hojaEF=hoja;
			 //anios
			 HSSFRow row1 =  hojaEF.getRow(161);
			 HSSFCell cell1 = row1.getCell(2);
			 HSSFCell cell2 = row1.getCell(4);
			 HSSFCell cell3 = row1.getCell(6);
			 
			 logger.info("0anio 2="+rating.get(0).getTotalAnio2());
			 logger.info("0anio 1="+rating.get(0).getTotalAnio1());
			 logger.info("0anio actual = "+rating.get(0).getTotalAnioActual());
			 
			 cell1.setCellValue(rating.get(0).getTotalAnio2()== null?"":rating.get(0).getTotalAnio2());
			 cell2.setCellValue(rating.get(0).getTotalAnio1()==null?"":rating.get(0).getTotalAnio1());
			 cell3.setCellValue(rating.get(0).getTotalAnioActual()==null?"":rating.get(0).getTotalAnioActual());
			 
			 //cuantitavo
			 HSSFRow row2 =  hojaEF.getRow(162);
			 cell1 = row2.getCell(2);
			 cell2 = row2.getCell(4);
			 cell3 = row2.getCell(6);
			 
			 logger.info("1anio 2="+rating.get(1).getTotalAnio2());
			 logger.info("1anio 1="+rating.get(1).getTotalAnio1());
			 logger.info("1anio actual = "+rating.get(1).getTotalAnioActual());
			 cell1.setCellValue(rating.get(1).getTotalAnio2()== null?"":rating.get(1).getTotalAnio2());
			 cell2.setCellValue(rating.get(1).getTotalAnio1()==null?"":rating.get(1).getTotalAnio1());
			 cell3.setCellValue(rating.get(1).getTotalAnioActual()==null?"":rating.get(1).getTotalAnioActual());
			 
			 //cualitativo
			 HSSFRow row3 =  hojaEF.getRow(163);
			 cell1 = row3.getCell(2);
			 cell2 = row3.getCell(4);
			 cell3 = row3.getCell(6);
			 
			 logger.info("2anio 2="+rating.get(2).getTotalAnio2());
			 logger.info("2anio 1="+rating.get(2).getTotalAnio1());
			 logger.info("2anio actual = "+rating.get(2).getTotalAnioActual());
			 cell1.setCellValue(rating.get(2).getTotalAnio2()== null?"":rating.get(2).getTotalAnio2());
			 cell2.setCellValue(rating.get(2).getTotalAnio1()==null?"":rating.get(2).getTotalAnio1());
			 cell3.setCellValue(rating.get(2).getTotalAnioActual()==null?"":rating.get(2).getTotalAnioActual());
			//bueradu
			 HSSFRow row4 =  hojaEF.getRow(164);
			 cell1 = row4.getCell(2);
			 cell2 = row4.getCell(4);
			 cell3 = row4.getCell(6);
			 
			 logger.info("3anio 2="+rating.get(3).getTotalAnio2());
			 logger.info("3anio 1="+rating.get(3).getTotalAnio1());
			 logger.info("3anio actual = "+rating.get(3).getTotalAnioActual());
			 cell1.setCellValue(rating.get(3).getTotalAnio2()== null?"":rating.get(3).getTotalAnio2());
			 cell2.setCellValue(rating.get(3).getTotalAnio1()==null?"":rating.get(3).getTotalAnio1());
			 cell3.setCellValue(rating.get(3).getTotalAnioActual()==null?"":rating.get(3).getTotalAnioActual());
			 
			 //rating
			 HSSFRow row5 =  hojaEF.getRow(165);
			 cell1 = row5.getCell(2);
			 cell2 = row5.getCell(4);
			 cell3 = row5.getCell(6);
			 
			 logger.info("4anio 2="+rating.get(4).getTotalAnio2());
			 logger.info("4anio 1="+rating.get(4).getTotalAnio1());
			 logger.info("4anio actual = "+rating.get(4).getTotalAnioActual());
			 cell1.setCellValue(rating.get(4).getTotalAnio2()== null?"":rating.get(4).getTotalAnio2());
			 cell2.setCellValue(rating.get(4).getTotalAnio1()==null?"":rating.get(4).getTotalAnio1());
			 cell3.setCellValue(rating.get(4).getTotalAnioActual()==null?"":rating.get(4).getTotalAnioActual());
			 
			 //escala maestra
			 HSSFRow row6 =  hojaEF.getRow(167);
			 cell1 = row6.getCell(2);
			 cell2 = row6.getCell(4);
			 cell3 = row6.getCell(6);
			 
			 logger.info("5anio 2="+rating.get(5).getTotalAnio2());
			 logger.info("5anio 1="+rating.get(5).getTotalAnio1());
			 logger.info("5anio actual = "+rating.get(5).getTotalAnioActual());
			 cell1.setCellValue(rating.get(5).getTotalAnio2()== null?"":rating.get(5).getTotalAnio2());
			 cell2.setCellValue(rating.get(5).getTotalAnio1()==null?"":rating.get(5).getTotalAnio1());
			 cell3.setCellValue(rating.get(5).getTotalAnioActual()==null?"":rating.get(5).getTotalAnioActual());
		}
		 
	}
	
	private void llenarData(HSSFSheet hojaDestino,HSSFSheet hojaOrigen, Map datos) throws Exception{
		

		try{
            
		  Iterator rows = hojaOrigen.rowIterator(); // hojaPlatillaData.rowIterator();
	        
	        while (rows.hasNext()) {

	            HSSFRow row = (HSSFRow) rows.next();
	            HSSFRow rowNew = (HSSFRow) hojaDestino.getRow(row.getRowNum());

                if(rowNew==null)  rowNew = hojaDestino.createRow(row.getRowNum());
                
	            Iterator cells = row.cellIterator();

	            while (cells.hasNext()) {
	            	
	                HSSFCell cell = (HSSFCell) cells.next();
	                HSSFCell cellNew= rowNew.getCell(cell.getColumnIndex());
	                
	                if(cellNew==null) cellNew=rowNew.createCell(cell.getColumnIndex());

	                CellValue cellValue=fEval.evaluate(cell);
	               /* if(cell!=null){

	                	int datformaint=cell.getCellStyle().getDataFormat();
	                	
	                	String datformastring1=cell.getCellStyle().getDataFormatString();
	                	System.out.println("datformastring1: "+datformastring1);
	                }*/
	                if(cell.getColumnIndex() <= 17 && cellValue!=null){
	                	
	                	
	                	/*String cellValueFormatAsString=cellValue.formatAsString();
	                	
		                String datformastring1=cell.getCellStyle().getDataFormatString();
		                System.out.println("datformastring1xx: "+datformastring1);*/
						//System.out.println("HFQV   buscarNombresArchivos: cellValueFormatAsString"+cellValueFormatAsString);
	                    switch(cellValue.getCellType())
	                    {
	                        case HSSFCell.CELL_TYPE_NUMERIC:
	                        	cellNew.setCellValue(cellValue.getNumberValue());
	                            break;
	                        case HSSFCell.CELL_TYPE_STRING:   
	                        		cellNew.setCellValue(cellValue.getStringValue());
	                        	break; 
	                        case HSSFCell.CELL_TYPE_BLANK: break;
	                        
	                        case HSSFCell.CELL_TYPE_FORMULA:
	                        	break;	
	                       default :
	                    	   cellNew.setCellValue(cellValue.getStringValue());
	                    }

	                }   
	                
	                
	            }

	        }
	        
	        //
	        /* EXPERIMENTO PARA ACCEDER A LOS TEXTBOX
	        HSSFPatriarch patriarch1 = hojaDestino.getDrawingPatriarch();
	    	System.out.println("HOla Mundo patriarch1: "+patriarch1);
	        if(patriarch1!= null){ 
		        List<HSSFShape> lista=patriarch1.getChildren();
		        for(HSSFShape objeto:lista){
		        	
		        	if(objeto instanceof HSSFTextbox){
		        		HSSFTextbox objetoTB=(HSSFTextbox)objeto;
		        		HSSFRichTextString rsz =objetoTB.getString();
		        		System.out.println("HOla Mundo rsz.toString(): "+rsz.toString());
		        		HSSFRichTextString rszy = new HSSFRichTextString("NUEVO TEXTO PARA TI");
		        		//objetoTB.setString(rszy);
		        	}
		        }
	        } 
	        */
	        //Creamos los TextBoxs Y los Cargamos con  la data Respectiva.

	         int col=6,row=4;
			 //HSSFPatriarch patriarch1 = hojaDestino.createDrawingPatriarch();
			 //HSSFTextbox textbox1 = patriarch1.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,10));
			 //Podemos sobreescribir la creacion de textBox solo para esta clase
	         
	         //HSSFTextbox textbox1 = crearTextBox(hojaDestino,"", row, col, 10, 18);
			 
			 /*HSSFFont fuente1 = hojaDestino.getWorkbook().createFont();
			 fuente1.setFontHeightInPoints((short)22);
			 fuente1.setFontName(HSSFFont.FONT_ARIAL);*/
			 //HSSFRichTextString rs1 = new HSSFRichTextString(" 1  12/12/2010 yyyyyyyyyy     despejando todas la dudas quese puedan encontrar");
			 //rs1.applyFont(fuente1);
			 
			// textbox1.setString(rs1);
			 
			 //EXPERIMENTO CREAMOS UNION DE FILAS.
			 /*
			 col=0;row=57;
			 hojaDestino.addMergedRegion(new CellRangeAddress(row,84,col,17));
			 hojaDestino.getRow(row).getCell(col).setCellValue("ESTO ES DATADE PRUEBA QUE IRAEN LA UNION DE CELDAS JSJSJJSJSJSJ");
			 
			 col=0;row=96;
			 hojaDestino.addMergedRegion(new CellRangeAddress(row,120,col,17));
	        	HSSFCell celx=hojaDestino.getRow(row).getCell(col);
	        	if(celx == null) celx=hojaDestino.getRow(row).createCell(0);
	        	
				 HSSFRichTextString rsz=new HSSFRichTextString(" PERO NO TE LA CREAS El presente documento es de uso exclusivamente interno de" +
							" las entidades pertenecientes al Grupo BBVA. Los datos, opiniones" +
							" y conclusiones que contiene son estrictamente confidenciales. Queda " +
							"prohibida su cesión a terceros por cualquier medio sin la autorización expresa" +
							" y por escrito de BBVA, S.A.");
					HSSFFont fuente2 = hojaDestino.getWorkbook().createFont();
					fuente2.setFontHeightInPoints((short)19);
					fuente2.setFontName(HSSFFont.FONT_ARIAL);
					fuente2.setItalic(true);
					rsz.applyFont(fuente2);
					HSSFCellStyle cst=celx.getCellStyle();
					//cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY); 
					cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
					cst.setAlignment(HSSFCellStyle.ALIGN_LEFT);

	        		celx.setCellValue(rsz);
				*/
			 /*  

			 col=0;row=57;
			 //HSSFPatriarch patriarch2 = hojaDestino.createDrawingPatriarch();
			 //HSSFTextbox textbox2 = patriarch2.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,84));
	         HSSFTextbox textbox2 = crearTextBox(hojaDestino,"", row, col, 18, 84);
			 HSSFRichTextString rs2=new HSSFRichTextString("A AAA Huber Fernando Quinto Vargas 12/12/2010 xxxxxx");
			 textbox2.setString(rs2);
			 
			 col=0;row=95;
			// HSSFPatriarch patriarch3 = hojaDestino.createDrawingPatriarch();
			 //HSSFTextbox textbox3 = patriarch3.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,122));
	         HSSFTextbox textbox3 = crearTextBox(hojaDestino,"", row, col, 18, 122);
			 HSSFRichTextString rs3=new HSSFRichTextString("B BBBBB hola mundo Huber Fernando Quinto Vargas 12/12/2010 xxxxxx");
			 textbox3.setString(rs3);
 	 
			 col=0;row=125;
			 //HSSFPatriarch patriarch4 = hojaDestino.createDrawingPatriarch();
			 //HSSFTextbox textbox4 = patriarch4.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,130));
	         HSSFTextbox textbox4 = crearTextBox(hojaDestino,"", row, col, 18, 130);
			 HSSFRichTextString rs4=new HSSFRichTextString("C CCCC Huber Fernando Quinto Vargas 12/12/2010 xxxxxx");
			 textbox4.setString(rs4);
			 
			 col=0;row=142;

			 //HSSFTextbox textbox5 = patriarch2.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,153));
	         HSSFTextbox textbox5 	= crearTextBox(hojaDestino,"", row, col, 18, 153);
			 HSSFRichTextString rs5	= new HSSFRichTextString("D DDDD Huber Fernando Quinto Vargas 12/12/2010 xxxxxx");
			 textbox5.setString(rs5);
			 
			 
			 col=0;row=171;

			 //HSSFTextbox textbox6 = patriarch2.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,186));
	         HSSFTextbox textbox6 	= crearTextBox(hojaDestino,"", row, col, 18, 186);
			 HSSFRichTextString rs6=new HSSFRichTextString("E EEEE Huber Fernando Quinto Vargas 12/12/2010 xxxxxx");
			 textbox6.setString(rs6);
			 

			 //Colocamos los CopyRights.
			 
			 col=0;row=89;

			 //HSSFTextbox textbox7 = patriarch2.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,91));
	         HSSFTextbox textbox7 	= crearTextBox(hojaDestino,"", row, col, 18, 91);
			 HSSFRichTextString rs7=new HSSFRichTextString("El presente documento es de uso exclusivamente interno de" +
			 												" las entidades pertenecientes al Grupo BBVA. Los datos, opiniones" +
			 												" y conclusiones que contiene son estrictamente confidenciales. Queda " +
			 												"prohibida su cesión a terceros por cualquier medio sin la autorización expresa" +
			 												" y por escrito de BBVA, S.A.");
			 HSSFFont fuente2 = hojaDestino.getWorkbook().createFont();
			 fuente2.setFontHeightInPoints((short)14);
			 fuente2.setFontName(HSSFFont.FONT_ARIAL);
			 fuente2.setItalic(true);
			 rs7.applyFont(fuente2);
			 textbox7.setString(rs7);
			 
			 col=0;row=190;
			 //HSSFTextbox textbox8 = patriarch2.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)col,row,(short)18,192));
	         HSSFTextbox textbox8 	= crearTextBox(hojaDestino,"", row, col, 18, 190);
			 HSSFRichTextString rs8=new HSSFRichTextString("El presente documento es de uso exclusivamente interno de" +
			 												" las entidades pertenecientes al Grupo BBVA. Los datos, opiniones" +
			 												" y conclusiones que contiene son estrictamente confidenciales. Queda " +
			 												"prohibida su cesión a terceros por cualquier medio sin la autorización expresa" +
			 												" y por escrito de BBVA, S.A.");
			 rs8.applyFont(fuente2);
			 textbox8.setString(rs8);  
			 
			 */
	
		}catch(Exception ex){
			System.out.println( " SheetDefEstadosFinancierosImpl: ex.getMessage(): "+ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

	
}
