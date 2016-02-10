package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.util.FormatUtil;

public class SheetDefCaratulaImpl implements SheetDefinition{
  
  public SheetDefCaratulaImpl(){
	  //Inicalizamos

  }
  @Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) {
		
		 
		 llenarGrupo(hoja.getRow(11), datos);
		 llenarDatosLimites(hoja,datos);
		 llenarDatosCartula(hoja,datos);
	       //Actualiza Hoja, sera necesario actualizar las formulas?? 
	        hoja.setForceFormulaRecalculation(true);
		
	}
	
	private void llenarGrupo(HSSFRow fila, Map<String,Object> params){

		String nombEmprOrGrupo	= (String)params.get("nombreEmpresaGrupo");
		nombEmprOrGrupo			=(nombEmprOrGrupo!=null)?nombEmprOrGrupo:"";
		if(fila.getCell(1)==null)fila.createCell(1);
		
		fila.getCell(1).setCellValue(nombEmprOrGrupo);
	}
	private void llenarDatosLimites(HSSFSheet hoja, Map datos){
		int rowY			  = 29;
		int colX			  = 3;
		
    	Double totLimAutorizado		=(Double)datos.get("totLimAutorizado");
    	Double totLimFormalizado	=(Double)datos.get("totLimFormalizado");
    	Double totLimPropuesto		=(Double)datos.get("totLimPropuesto");
    	
    	
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimAutorizado));
    	rowY++;
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimFormalizado));
    	rowY=rowY+2;
    	hoja.getRow(rowY).getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(totLimPropuesto));
    	
	}

//ini MCG20130905
	
	private void llenarDatosCartula(HSSFSheet hoja, Map datos){
		
		Programa programax = (Programa)datos.get("programax");
		List<Rating> rating =new ArrayList<Rating>();
	
		String codEmpresaPrincipal=programax.getIdEmpresa()==null?"0":programax.getIdEmpresa().toString();
		String tipoEmpres=programax.getTipoEmpresa().getId().toString(); 
		
		rating = (List<Rating>)datos.get("rating");			
		if(rating == null || (rating != null && rating.size()==0)){
			if(tipoEmpres.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){	
				rating = (List<Rating>)datos.get("rating_"+codEmpresaPrincipal);		 
			}
		}
				
		 if(rating != null && rating.size()>0){
		 
			 //HSSFWorkbook workbook =  hoja.getWorkbook();
			 //HSSFSheet hojaEF = workbook.getSheet("Sintesis Econom-Financiero");
			 HSSFSheet hojaEF=hoja;
			 //anios
			 HSSFRow row1 =  hojaEF.getRow(19);
			 HSSFCell cell1 = row1.getCell(3);
			 HSSFCell cell2 = row1.getCell(4);
			 HSSFCell cell3 = row1.getCell(5);
			 
			 cell1.setCellValue(rating.get(0).getTotalAnio2()== null?"":rating.get(0).getTotalAnio2());
			 cell2.setCellValue(rating.get(0).getTotalAnio1()==null?"":rating.get(0).getTotalAnio1());
			 cell3.setCellValue(rating.get(0).getTotalAnioActual()==null?"":rating.get(0).getTotalAnioActual());
			 		
			 
			 //escala maestra
			 HSSFRow row6 =  hojaEF.getRow(22);
			 cell1 = row6.getCell(3);
			 cell2 = row6.getCell(4);
			 cell3 = row6.getCell(5);

			 cell1.setCellValue(rating.get(5).getTotalAnio2()== null?"":rating.get(5).getTotalAnio2());
			 cell2.setCellValue(rating.get(5).getTotalAnio1()==null?"":rating.get(5).getTotalAnio1());
			 cell3.setCellValue(rating.get(5).getTotalAnioActual()==null?"":rating.get(5).getTotalAnioActual());
		}
		 
	}
	
//fin MCG20130905

}
