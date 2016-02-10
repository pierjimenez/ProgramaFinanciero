package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;

import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.stefanini.core.util.FormatUtil;

public class SheetDefPoliticGrupoImpl extends SheetDefinitionBase implements SheetDefinition{

	private int 	 newRowsPoliticRiesgGrup;
	private HSSFRow  rowAux	 = null;
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
        
		cargarDatosPoliticRiesgGrupo( hoja, datos);
		
		cargarDatosVarios( hoja, datos);
		
	}
	
	private void cargarDatosPoliticRiesgGrupo(HSSFSheet hoja,Map datos){
		
		List<LimiteFormalizado> listaLimForma=(List<LimiteFormalizado>)datos.get("listaLimForma");
		Double total1 = 0d;
		Double total2 = 0d;
		Double total3 = 0d;
		Double total4 = 0d;
		Double total5 = 0d;
		Double total6 = 0d;
		Double total9 = 0d;
		
		Double valor2 = 0d;
		Double valor3 = 0d;
		Double valor4 = 0d;
		Double valor5 = 0d;
		Double valor6 = 0d;
		
		int rowY			  = 6;
		int colX			  = 2;
		int rowTot1    		  = 13;
		HSSFRow row			  = null;

		int tamanio = listaLimForma!=null?listaLimForma.size():0;
		newRowsPoliticRiesgGrup= (tamanio-5)> 0?(tamanio-5):0;
	  	

		
		for(LimiteFormalizado data:listaLimForma){
			
			row=hoja.getRow(rowY);
			if(row==null){
				row	= hoja.createRow(rowY);	
				crearCols(row,8);
			}else{
				if(row.getCell(colX)==null){
					crearCols(row,8);
				}
			}
			
			//Totalizamos
			
			 valor2 = Double.valueOf(FormatUtil.FormatNumeroSinComa(data.getLimiteAutorizado())); 
			 valor3 = Double.valueOf(FormatUtil.FormatNumeroSinComa(data.getComprometido())); 
			 valor4 = Double.valueOf(FormatUtil.FormatNumeroSinComa(data.getNoComprometido()));  
			
			 total1 +=	valor2;
			 total2 +=	valor3;
			 total3 +=	valor4;
			 
			 
			 total4 =	valor3+valor4; //Solo Limite Formalizado//valor2
			 
			 total9 +=	total4;
			 
			 
			 
			 valor5 = Double.valueOf(FormatUtil.FormatNumeroSinComa(data.getDispuesto()));
			 valor6 = Double.valueOf(FormatUtil.FormatNumeroSinComa(data.getLimitePropuesto()));
			 
			 total5 +=	valor5;
			 total6 +=	valor6;
			 

	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(valor2));
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(valor3));
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(valor4));
	        	colX++;
	        	
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total4));
	        	
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(valor5));
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(valor6));
	        	
        	
	        	rowY++;
	        	colX=2;
		}
		
		//Llenamos los totales
		rowTot1    	 = rowTot1 + newRowsPoliticRiesgGrup ;
		colX		 = 2;

		row		   = hoja.getRow(rowTot1);
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total1));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total2));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total3));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total9));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total5));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total6));
    	
    	//Guardamos los totales en el Map de param, para que Caratula lo utilice.
    	List<LimiteFormalizado> listaLimFormaAnexo=(List<LimiteFormalizado>)datos.get("listaLimFormaAnexo");
		Double valorAuto = 0d;
		Double valorForma = 0d;
		Double valorProp = 0d;
		if (listaLimFormaAnexo!=null && listaLimFormaAnexo.size()>0){
    	
		valorAuto = Double.valueOf(FormatUtil.FormatNumeroSinComa(listaLimFormaAnexo.get(0).getLimiteAutorizado())); 
		valorForma = Double.valueOf(FormatUtil.FormatNumeroSinComa(listaLimFormaAnexo.get(0).getTotal())); 
		valorProp = Double.valueOf(FormatUtil.FormatNumeroSinComa(listaLimFormaAnexo.get(0).getLimitePropuesto())); 
		}
		 
    	datos.put("totLimAutorizado", valorAuto);
    	datos.put("totLimFormalizado", valorForma);
    	datos.put("totLimPropuesto", valorProp);
    	
	}
	
	private void cargarDatosVarios(HSSFSheet hoja,Map datos){
		int rowY			  = 110;
		int colX			  = 3;
		Programa programa= (Programa)datos.get("programax");
		hoja.getRow(rowY).getCell(colX).setCellValue(programa.getLimiteAutorizadoPRG());
		rowY=rowY+2;
		hoja.getRow(rowY).getCell(colX).setCellValue(programa.getProximaRevisionPRG());
		rowY=rowY+2;
		colX=2;
		hoja.getRow(rowY).getCell(colX).setCellValue(programa.getMotivoProximaPRG());
		//MILES MILLONES EN POLITICAS DE RIESGO GRUPO
		if(hoja.getRow(4).getCell(0)== null){
			hoja.getRow(4).createCell(0).setCellValue(datos.get("tipomilesPLR").toString());
		}else{
			CellStyle cstyle = hoja.getRow(4).getCell(0).getCellStyle();
			hoja.getRow(4).getCell(0).setCellValue(datos.get("tipomilesPLR").toString());
			hoja.getRow(4).getCell(0).setCellStyle(cstyle);
		}
		
	}

}
