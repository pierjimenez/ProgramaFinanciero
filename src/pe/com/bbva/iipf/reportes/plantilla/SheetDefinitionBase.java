package pe.com.bbva.iipf.reportes.plantilla;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFTextbox;

public class SheetDefinitionBase {
	
	private HSSFPatriarch patriarch1 = null;
	
	protected HSSFTextbox crearTextBox(HSSFSheet hojaX,String texto, int rowPos,int colPos,int finRowPos ,int finColPos){
        /*
		 patriarch1 =hojaX.createDrawingPatriarch();
		 
		 HSSFTextbox textbox1 = patriarch1.createTextbox(new HSSFClientAnchor(0,0,0,0,(short)colPos,rowPos,(short)finColPos,finRowPos));

		 HSSFFont fuente1 = hojaX.getWorkbook().createFont();
		 fuente1.setFontHeightInPoints((short)22);
		 fuente1.setFontName(HSSFFont.FONT_ARIAL);
		 HSSFRichTextString rs1=new HSSFRichTextString(texto);
		 rs1.applyFont(fuente1);
		 textbox1.setString(rs1);
		
		 return textbox1;*/
		
		return null;
	}
	protected void insertarRows(HSSFSheet hoja,int fIni,int cantidad){
		hoja.shiftRows(fIni, hoja.getLastRowNum(), cantidad);
	}
	protected List<HSSFRow> crearRows(HSSFSheet hoja,int fIni,int cantidad){
	  	int fFin=fIni+cantidad;
	  	List<HSSFRow> newRows= new ArrayList<HSSFRow>();
		for(int i=fIni;i<fFin;i++){
			HSSFRow rx = hoja.createRow(i);
			newRows.add(rx);
		}
		
		return newRows;
	}

	protected void crearCols(HSSFRow row,int numCols){
       if(row!=null){
	        for(int i=0;i<numCols;i++){
	        	if(row.getCell(i)==null)
	        	row.createCell(i);
	        }
       }
	}
	public HSSFPatriarch getPatriarch1() {
		return patriarch1;
	}
	public void setPatriarch1(HSSFPatriarch patriarch1) {
		this.patriarch1 = patriarch1;
	}

}
