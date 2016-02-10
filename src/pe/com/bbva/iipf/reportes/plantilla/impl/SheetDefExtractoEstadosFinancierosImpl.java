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
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.bbva.iipf.util.Constantes;

public class SheetDefExtractoEstadosFinancierosImpl extends SheetDefinitionBase implements SheetDefinition{
	Logger logger = Logger.getLogger(this.getClass());
	private FormulaEvaluator fEval=null;
	
		
	//ini MCG20130306
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) throws Exception {
		     
 	   
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
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_EXTRACTO), datos);
 		 	    }
 	    	}else{//si es grupo
 	    		if(listaWBEmpPrograma!=null &&
 	    		   Boolean.parseBoolean(datos.get("tieneSintesisEconoDeGrupo").toString())){
 		 	    	//wbPFDataGrupoOEmprPrinc = listaWBEmpPrograma.get(0);
 	    			fis = new FileInputStream(listaWBEmpPrograma.get(0).getFileName().toString());
	           		wbPFDataGrupoOEmprPrinc = new HSSFWorkbook(fis);
 		 	    	//Llenmamos data del Grupo o de la Empresa principal, en la primera hoja de Est. Finacioeros
 					fEval=wbPFDataGrupoOEmprPrinc.getCreationHelper().createFormulaEvaluator();
 			        llenarData(hoja,wbPFDataGrupoOEmprPrinc.getSheetAt(Constantes.NUM_SHEET_EXTRACTO), datos);
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
	        	String nombHojaDestino="Extracto (";
	        	int indice = 2;
	        	HSSFWorkbook wb  = null;
	        	for(FilaEmpresaHoja urlExcel : listaWBEmpPrograma){
	        		if(noEsElPrimero){
	        			
		        		if(contador < limite){
		        			fis = new FileInputStream(urlExcel.getFileName());
			           		wb = new HSSFWorkbook(fis);
			           		fEval=wb.getCreationHelper().createFormulaEvaluator();
		        		    String strindexHoja=String.valueOf(urlExcel.getEmpresa().getIndexHoja());
		        			HSSFSheet hojaEF = wbReporte.getSheet(nombHojaDestino+strindexHoja+")");  //wbReporte.getSheetAt(contador);
		        			 
		        			llenarData(hojaEF,wb.getSheetAt(Constantes.NUM_SHEET_EXTRACTO), datos);
		        		}	
		        		indice++;
		                contador++;
		                
	        		}else{
	        			noEsElPrimero=true;
	        			continue;
	        		}
	        	}
	        	
	        } 
	       
 	    }
        
	}
	//fin MCG20130306

		
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
	                if(cell.getColumnIndex() <= 13 && cellValue!=null){	                	
	                		                	
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
	           

	
		}catch(Exception ex){
			System.out.println( " SheetDefExtractoEstadosFinancierosImpl: ex.getMessage(): "+ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

}
