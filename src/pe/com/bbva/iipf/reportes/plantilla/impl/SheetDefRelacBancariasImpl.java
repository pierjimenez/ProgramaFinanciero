package pe.com.bbva.iipf.reportes.plantilla.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;


import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.RentabilidadBEC;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinitionBase;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

public class SheetDefRelacBancariasImpl extends SheetDefinitionBase implements SheetDefinition{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static int NUM_MAX_COLUMNAS = 11;
	private int 	 newRowsPerfSubg;
	private HSSFRow  rowAux	 = null;
	
	private HSSFCellStyle cellStyle1 = null;
	private HSSFCellStyle cellStyle2 = null;
	private HSSFCellStyle cellStyle3 = null;
	private HSSFCellStyle cellStyle4 = null;
	private HSSFCellStyle cellStyle5 = null;
	private HSSFCellStyle cellStyle6 = null;
	private HSSFCellStyle cellStyle7 = null;
	private HSSFCellStyle cellStyle8 = null;
	private HSSFCellStyle cellStyle9 = null;
	private HSSFCellStyle cellStyle10= null;
	private HSSFCellStyle cellStyle11= null;
	
	/*@Resource
	private RelacionesBancariasBO relacionesBancariasBO;
	@Resource
	private ProgramaBO 			  programaBO;
	*/
	@Override
	public void fillDataSheet(HSSFSheet hoja, Map datos) throws Exception {
		
        
        cargarDatosPerfilesSubgrupos(hoja, datos);
        cargarComentario(hoja,datos);
        cargarCuotaFinaciera(hoja,datos);
        cargarComentarioCuotaFinan(hoja, datos);
        cargarPoolBancario(hoja,datos);
        cargarRentabilidad(hoja, datos);
        efectividadCartera(hoja, datos);
        
        //hoja.setForceFormulaRecalculation(true);
        
	}
	
	private void efectividadCartera(HSSFSheet hoja, Map datos) throws Exception {
		Programa programa = (Programa)datos.get("programax");
		programa.getComentcuotaFinanciera();
		
		// MIL int rowPos= 89 +newRowsPerfSubg;
		int rowPos= 101 +newRowsPerfSubg;
		String efecprom6sol=((Programa)datos.get("programax")).getEfectividadProm6sol();
		String efecprom6dol=((Programa)datos.get("programax")).getEfectividadProm6dol();
		String protestoprom6sol=((Programa)datos.get("programax")).getProtestoProm6sol();
		String protestoprom6dol=((Programa)datos.get("programax")).getProtestoProm6dol();
		String efecultsol=((Programa)datos.get("programax")).getEfectividadUltmaniosol();
		String efecultdol=((Programa)datos.get("programax")).getEfectividadUltmaniodol();
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(efecprom6sol);
		
		rowPos++;
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(efecprom6dol);
		
		rowPos++;
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(protestoprom6sol);
		
		rowPos++;
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(protestoprom6dol);
		
		rowPos++;
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(efecultsol);
		
		rowPos++;
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(efecultdol);
	}
	
	private void cargarComentarioCuotaFinan(HSSFSheet hoja, Map datos){
		Programa programa = (Programa)datos.get("programax");
		programa.getComentcuotaFinanciera();
		
		int rowPos= 52 +newRowsPerfSubg;
		String comentario=((Programa)datos.get("programax")).getComentcuotaFinanciera();
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(0)==null) 
			hoja.getRow(rowPos).createCell(0);
		
		hoja.getRow(rowPos).getCell(0).setCellValue(comentario);
	}
	private void cargarDatosPerfilesSubgrupos(HSSFSheet hoja,Map datos){
		
		List<LineasRelacionBancarias> listaDatosperfSubgrupo=(List<LineasRelacionBancarias>)datos.get("listaFilSubgrupo");
		
		Double total1 = 0d;
		Double total2 = 0d;
		Double total3 = 0d;
		Double total4 = 0d;
		Double total5 = 0d;
		Double total6 = 0d;
		Double total7 = 0d;
		Double total8 = 0d;
		Double total9 = 0d;
		Double total10 = 0d;
		Double total11 = 0d;
		Double total12 = 0d;
		
		Double valor1 = 0d;
		Double valor2 = 0d;
		Double valor3 = 0d;
		Double valor4 = 0d;
		Double valor5 = 0d;
		Double valor6 = 0d;
		Double valor7 = 0d;
		Double valor8 = 0d;
		
		int rowY			  = 21;
		int colX			  = 0;
		int rowTot1    		  = 24;
		HSSFRow row			  = null;
		HSSFCell cell		  = null;
		
		////////EPOMAYAY 16022012
		String strtipomiles=(String)datos.get("tipomilesRB");		
		
		row = hoja.getRow(3);
		if(row==null) {
			row=hoja.createRow(3);
		}
		cell 	= row.getCell(0);
		if(cell==null) {
			cell=row.createCell(0);
		}
		CellStyle cstyletm = hoja.getRow(3).getCell(0).getCellStyle();
		cell.setCellValue(strtipomiles);
		cell.setCellStyle(cstyletm);
		//////////EPOMAYAY 16022012
		
		int tamanio = listaDatosperfSubgrupo!=null?listaDatosperfSubgrupo.size():0;
		newRowsPerfSubg= (tamanio-3)> 0?(tamanio-3):0;
		if(tamanio > 3){
			//Obtenemos el formato de una celda.
			rowAux=hoja.getRow(rowY+1);
			
			 cellStyle1	= rowAux.getCell(0).getCellStyle();
			 cellStyle2	= rowAux.getCell(1).getCellStyle();
			 cellStyle3	= rowAux.getCell(2).getCellStyle();
			 cellStyle4	= rowAux.getCell(3).getCellStyle();
			 cellStyle5	= rowAux.getCell(4).getCellStyle();
			 cellStyle6	= rowAux.getCell(5).getCellStyle();
			 cellStyle7	= rowAux.getCell(6).getCellStyle();
			 cellStyle8	= rowAux.getCell(7).getCellStyle();
			 cellStyle9	= rowAux.getCell(8).getCellStyle();
			 cellStyle10= rowAux.getCell(9).getCellStyle();
			 cellStyle11= rowAux.getCell(10).getCellStyle();

			//Insertamos Filas nuevas.
			insertarRows(hoja,rowY+1,newRowsPerfSubg);
			List<HSSFRow> rowsNew = crearRows(hoja,rowY+1,newRowsPerfSubg);

			for(HSSFRow  rowz: rowsNew){
				crearCols(rowz,11);
				asignarEstilosFiliales(rowz,11);
			}
		}
		
		for(LineasRelacionBancarias relaBanc:listaDatosperfSubgrupo){

			row=hoja.getRow(rowY);
			//cargamos
			
			valor1 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrclteform() ));
			valor2 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrcdpto() ));
			valor3 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrplteform() ));
			valor4 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrpdpto()));
			valor5 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrflteform() ));
			valor6 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgrfdpto() ));
			valor7 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgtesolteform() ));
			valor8 	= Double.valueOf(FormatUtil.FormatNumeroSinComa(relaBanc.getSgtesodpto() ));
			
			//Totalizamos
			
			 total1 +=	valor1;
			 total2 +=	valor2;
			 total3 +=	valor3;
			 total4 +=	valor4;
			 total5 +=	valor5;
			 total6 +=	valor6;
			 total7 +=	valor7;
			 total8 +=	valor8;
			 
			 total11 = valor1+valor3+valor5+valor7;
			 total12 = valor2+valor4+valor6+valor8;
			 
			 total9 +=	total11;
			 total10 +=	total12;

	        	row.getCell(colX).setCellValue(relaBanc.getLinea());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrclteform());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrcdpto());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrplteform());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrpdpto());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrflteform());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgrfdpto());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgtesolteform());
	        	colX++;
	        	row.getCell(colX).setCellValue(relaBanc.getSgtesodpto()); 
	        	
	        	
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total11) ); //Total LTE FORM
	        	colX++;
	        	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total12)); //Total DPTO
        	
	        	rowY++;
	        	colX=0;
		}
		
		//Llenamos los totales
		rowTot1    	 = rowTot1 + newRowsPerfSubg ;
		colX		 = 1;

		row		   = hoja.getRow(rowTot1);
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total1));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total2));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total3));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total4));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total5));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total6));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total7));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total8));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total9));
    	colX++;
    	row.getCell(colX).setCellValue(FormatUtil.roundTwoDecimalsPunto(total10));
    	//ini MCG20111108
    	datos.put("nuevaPosComenLineaRB",28+ newRowsPerfSubg);
    	//fin MCG20111108
	}
	
	private void cargarComentario(HSSFSheet hoja,Map datos){
		int rowPos= 16 +newRowsPerfSubg;
		
		//Insertamos una region con data.

	}
	private void cargarCuotaFinaciera(HSSFSheet hoja,Map datos){
		
		int rowPos= 51 +newRowsPerfSubg;
		String cfbbva=((Programa)datos.get("programax")).getCuotaFinanciera();
		if(hoja.getRow(rowPos)==null )
			hoja.createRow(rowPos);
		
		if(hoja.getRow(rowPos).getCell(2)==null) 
			hoja.getRow(rowPos).createCell(2);
		
		hoja.getRow(rowPos).getCell(2).setCellValue(cfbbva);
	}
	
	private void cargarPoolBancario(HSSFSheet hoja,Map datos) throws Exception{
		
			//int cantTotalColumnas = 11;//cantidad de columnas que se pueden mostrar en la impresion
			//int numColumnas = 0;//
			int grupo = 1;
			boolean flagPrint = false;
			int rowPosini=59 +newRowsPerfSubg;
			int rowPos= 59 +newRowsPerfSubg;
			String valorx="";
			List<List> listPoolBancarioTotal = new ArrayList<List>();
			String codBanco = "";		
			List<Empresa> listEmpresat =new ArrayList<Empresa>();	
			List<OpcionPool> listaopcionPool= new ArrayList<OpcionPool>();
			List<Tempresa> listaEmpresa = new ArrayList<Tempresa>();
			int rowsinsert=0;
			
			//StringBuilder sb=new StringBuilder ();
	    try {

				String Idprograma 			= (String)datos.get("idPrograma");
				Programa programa 			= (Programa)datos.get("programax");
				String strEmpresa 			= programa.getNombreGrupoEmpresa();			
				String codTipoEmpresaGrupo	= programa.getTipoEmpresa().getId().toString();
				
				RelacionesBancariasBO relacionesBancariasBO =(RelacionesBancariasBO)datos.get("relacionesBancariasBO");
				ProgramaBO programaBO =(ProgramaBO)datos.get("programaBO");

				listaopcionPool=(List<OpcionPool>)datos.get("listaopcionPool");
				if (listaopcionPool!=null && listaopcionPool.size()>0){
					for (OpcionPool opcionPool: listaopcionPool){
						Tempresa otempresa=new Tempresa();
						otempresa.setCodEmpresa(opcionPool.getCodOpcionPool());
						otempresa.setNomEmpresa(opcionPool.getDescOpcionPool());
						listaEmpresa.add(otempresa);
					}
				}
				List<OpcionPool> listaopcionPool1= new ArrayList<OpcionPool>();			
				listaopcionPool1=(List<OpcionPool>)datos.get("listaopcionPool1");
				String strbanco="";
				if (listaopcionPool1!=null && listaopcionPool1.size()>0){				
					for (OpcionPool opcionPool: listaopcionPool1){					
						strbanco+=opcionPool.getCodOpcionPool()+",";	
					}
				}
				 if(!strbanco.equals(""))
					 codBanco=strbanco.substring(0, strbanco.length()-1);
				
				List<OpcionPool> listaopcionPool2= new ArrayList<OpcionPool>();			
				listaopcionPool2=(List<OpcionPool>)datos.get("listaopcionPool2");
							
					
				//ini grupo economico
				if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_GRUPO.toString())){
					
							///////DATOS GRUPO
							configurarFila(hoja,rowPos,1,11);
							rowsinsert++;
							hoja.getRow(rowPos).getCell(0).setCellValue("Deuda Grupo: " + strEmpresa);
							ponerStiloCelda(hoja.getRow(rowPos).getCell(0), 1, true,0,false);
							rowPos++;
							//sb.append("<table>");
							//sb.append("<tr><td>Deuda Grupo: " + strEmpresa + "</td></tr>");
							//sb.append("</table>");
							do{
							if (listaopcionPool2!=null && listaopcionPool2.size()>0){
								for (OpcionPool opcionPool: listaopcionPool2) {
									listPoolBancarioTotal = new ArrayList<List>();
									String CabeceraTipoDeuda="";
									if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_TOTAL;
									   if(!codBanco.equals(""))
										listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, "", Constantes.ID_TIPO_DEUDA_TOTAL,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									}else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_DIRECTA )) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA ;
										 if(!codBanco.equals(""))
										  listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, "", Constantes.ID_TIPO_DEUDA_DIRECTA,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									} else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
										CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
										 if(!codBanco.equals(""))
										  listPoolBancarioTotal = relacionesBancariasBO.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, "", Constantes.ID_TIPO_DEUDA_INDIRECTA,
														codBanco, Constantes.ID_TIPO_EMPRESA_GRUPO.toString(),Idprograma.toString());
				
									} 

									// DEUDA 
									if (listPoolBancarioTotal != null && listPoolBancarioTotal.size() > 0) {
										///////CABECERA TIPO DE DEUDA
										configurarFila(hoja,rowPos,1,11);
										rowsinsert++;
										if(hoja.getRow(rowPos)==null){ hoja.createRow(rowPos);rowsinsert++;}
										if(hoja.getRow(rowPos).getCell(0)==null) hoja.getRow(rowPos).createCell(0);
										
										ponerStiloCelda(hoja.getRow(rowPos).getCell(0), 2, true,0, false);
										hoja.getRow(rowPos).getCell(0).setCellValue(CabeceraTipoDeuda);
										rowPos++;
										////////////////////////
										
										//sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
										//sb.append("<table class=\"gridtable\">");
				
										//TITULO DE CABECERA
										int contColx=0;
										configurarFila(hoja,rowPos,1,11);
										rowsinsert++;
										//sb.append("<tr>");
										Map<String, String> hm = new HashMap<String, String>();
										for (Object lista : listPoolBancarioTotal.get(0)) {
											hm = (HashMap<String, String>) lista;
				
											Iterator it = hm.entrySet().iterator();
											while (it.hasNext()) {
												flagPrint = false;
												Map.Entry e = (Map.Entry) it.next();	
												if(grupo == 1){
													flagPrint = true;
												}else if(grupo == 2 && (contColx == 0 || contColx >= NUM_MAX_COLUMNAS)){
													flagPrint = true;
												}
												if(flagPrint){													
													//sb.append("<th>" + e.getKey() + "</th>");
													if(hoja.getRow(rowPos)==null){ hoja.createRow(rowPos);rowsinsert++;}
													if(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx)==null) hoja.getRow(rowPos).createCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx);
													if((e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA)||
														e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA))){
														ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx), 3, true,1,true);
													}else{
														ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx), 3, true,1,false);
													}
													hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx).setCellValue((String)e.getKey());
												}
												contColx=contColx+1;
											}
											if(contColx == NUM_MAX_COLUMNAS && grupo==1){
												break;
											}
										}	
										rowPos++;
										
										//sb.append("</tr>");
				
										int numCol=0;
										configurarFila(hoja,rowPos,listPoolBancarioTotal.size(),11);
										rowsinsert=rowsinsert+listPoolBancarioTotal.size();
										for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
											//sb.append("<tr>");
											
											numCol=0;

											Map<String, String> hmr = new HashMap<String, String>();
											for (Object x : listPoolBancarioTotal.get(i)) {
												hmr = (HashMap<String, String>) x;

												Iterator itr = hmr.entrySet().iterator();
												while (itr.hasNext()) {
													flagPrint = false;
													Map.Entry er = (Map.Entry) itr.next();									
													//sb.append("<td>" + er.getValue()+ "</td>");
													
													//CREAMOS COLUMANS PARA LA HOJA
													if(grupo == 1){
														flagPrint = true;
													}else if(grupo == 2 && (numCol == 0 || numCol >= NUM_MAX_COLUMNAS)){
														flagPrint = true;
													}
													if(flagPrint){
														if(hoja.getRow(rowPos)==null){ hoja.createRow(rowPos);rowsinsert++;};
														if(hoja.getRow(rowPos).getCell(grupo==2 && numCol > 0?(numCol-NUM_MAX_COLUMNAS+1):numCol)==null) hoja.getRow(rowPos).createCell(grupo==2 && numCol > 0?(numCol-NUM_MAX_COLUMNAS+1):numCol);
														
														if(numCol==0){
															ponerStiloCelda(hoja.getRow(rowPos).getCell(numCol), 4, true,1,false);
														}else{
															ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && numCol > 0?(numCol-NUM_MAX_COLUMNAS+1):numCol), 4, true,3,false);
														}
														
														if(er!=null){
															valorx=FormatUtil.formatMiles((er.getValue()!=null)?er.getValue().toString():"");
															hoja.getRow(rowPos).getCell(grupo==2 && numCol > 0?(numCol-NUM_MAX_COLUMNAS+1):numCol).setCellValue(valorx);
														}
													}
													numCol++;

												}
												if(numCol == NUM_MAX_COLUMNAS && grupo==1){
													break;
												}
											}
											rowPos++;
											//////EPOMAYAY 20202012
											configurarFila(hoja,rowPos,1,11);
											rowsinsert++;
											//////EPOMAYAY 20202012
											//sb.append("</tr>");
										}			
										//sb.append("</table>");
									}
				
								}
							
							}//
							grupo++;
							if(grupo==1){
								rowPos++;
							}
							}while(grupo<3 && getNumColumnas(listPoolBancarioTotal)>NUM_MAX_COLUMNAS);
					
				}			
				// fin grupo economicp
				
				
				// ini empresa			
			if(listaEmpresa!=null && listaEmpresa.size()>0){
				
				for (Tempresa empresa: listaEmpresa) {

					if (empresa.getCodEmpresa().toString().equals("999999")) {
						continue;
					}
					
					String strEmpresaind="";
					listEmpresat =programaBO.findEmpresaByIdprogramaRuc(Idprograma.toString(), empresa.getCodEmpresa().toString());	
					if (listEmpresat!=null && listEmpresat.size()>0){
					Empresa oempresa=new Empresa();
					oempresa=listEmpresat.get(0);
					strEmpresaind=oempresa.getNombre();
					}
					
					//TITULO TIPO NOMBRE DE GRUPO////////////////////////////
					configurarFila(hoja,rowPos,1,11);
					rowsinsert++;
					hoja.getRow(rowPos).getCell(0).setCellValue("Deuda: " + strEmpresaind);
					ponerStiloCelda(hoja.getRow(rowPos).getCell(0), 1, true,0,false);
					rowPos++;
					
					//sb.append("<br/>");
					//sb.append("<table>");
					//sb.append("<tr><td>Deuda: " + strEmpresaind + "</td></tr>");
					//sb.append("</table>");
					
					if (listaopcionPool2!=null && listaopcionPool2.size()>0){
						for (OpcionPool opcionPool: listaopcionPool2) {
							grupo=1;
							listPoolBancarioTotal = new ArrayList<List>();
							String CabeceraTipoDeuda="";
							if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_TOTAL)) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_TOTAL;
								 if(!codBanco.equals(""))
								  listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_TOTAL,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							}else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_DIRECTA )) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_DIRECTA ;
								 if(!codBanco.equals(""))
								  listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_DIRECTA,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							}else if (opcionPool.getCodOpcionPool().equals(Constantes.ID_TIPO_DEUDA_INDIRECTA)) {
								CabeceraTipoDeuda=Constantes.TIPO_DEUDA_INDIRECTA;
								 if(!codBanco.equals(""))
								  listPoolBancarioTotal = relacionesBancariasBO
										.findPoolBancarioBD(Constantes.TIPO_DOCUMENTO_RUC, empresa.getCodEmpresa(), Constantes.ID_TIPO_DEUDA_INDIRECTA,
												codBanco, Constantes.ID_TIPO_EMPRESA_EMPR.toString(),Idprograma.toString());
		
							} 
		
							// DEUDA TOTAL
							
							do{
								//4 HFQV CREAMOS FILAS PARA LA HOJA
								if (listPoolBancarioTotal != null
									&& listPoolBancarioTotal.size() > 0) {
									
									//TITULO TIPO DE DEUDA////////////////////////////
									configurarFila(hoja,rowPos,1,11);
									rowsinsert++;
									hoja.getRow(rowPos).getCell(0).setCellValue(CabeceraTipoDeuda);
									ponerStiloCelda(hoja.getRow(rowPos).getCell(0), 2, true,0,false);
									rowPos++;
									////////////////////////////////////////////////////////////
									//sb.append("<br/>");
									//sb.append("<b>" + CabeceraTipoDeuda + ": </b>");
									//TITULO DE CABECERA
									int contColx=0;
									configurarFila(hoja,rowPos,1,11);
									rowsinsert++;
									//sb.append("<table class=\"gridtable\">");
									//sb.append("<tr>");
									Map<String, String> hm = new HashMap<String, String>();
									for (Object lista : listPoolBancarioTotal.get(0)) {
										
										hm = (HashMap<String, String>) lista;
										Iterator it = hm.entrySet().iterator();
										while (it.hasNext()) {
											flagPrint = false;
											Map.Entry e = (Map.Entry) it.next();
											//sb.append("<th>" + e.getKey() + "</th>");
											if(grupo == 1){
												flagPrint = true;
											}else if(grupo == 2 && (contColx == 0 || contColx >= NUM_MAX_COLUMNAS)){
												flagPrint = true;
											}
											if(flagPrint){
												if(hoja.getRow(rowPos)==null) {hoja.createRow(rowPos);rowsinsert++;}
												if(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx)==null) hoja.getRow(rowPos).createCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx);
												if(e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_BBVA)||
												   e.getKey().toString().equals(Constantes.ABREV_NOMB_BANCO_CTABBVA)){
													ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx), 3, true,1,true);
												}else{
													ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx), 3, true,1,false);	
												}
												hoja.getRow(rowPos).getCell(grupo==2 && contColx > 0?(contColx-NUM_MAX_COLUMNAS+1):contColx).setCellValue((String)e.getKey());
											}
											contColx=contColx+1;
										}
										if(contColx == NUM_MAX_COLUMNAS && grupo==1){
											break;
										}
									}
									rowPos++;

									//sb.append("</tr>");
									
									//3 HFQV CREAMOS FILAS PARA LA HOJA
									int contCol=0;
									configurarFila(hoja,rowPos,listPoolBancarioTotal.size(),11);
									rowsinsert=rowsinsert+listPoolBancarioTotal.size();
									//4 HFQV CREAMOS FILAS PARA LA HOJA
			
									for (int i = 0; i < listPoolBancarioTotal.size(); i++) {
										
										//sb.append("<tr>");
										Map<String, String> hmr = new HashMap<String, String>();
										contCol=0;
										for (Object x : listPoolBancarioTotal.get(i)) {
											
											hmr = (HashMap<String, String>) x;
											Iterator itr = hmr.entrySet().iterator();
											while (itr.hasNext()) {
												flagPrint = false;
												Map.Entry er = (Map.Entry) itr.next();
												//sb.append("<td>" + er.getValue() + "</td>");
												if(grupo == 1){
													flagPrint = true;
												}else if(grupo == 2 && (contCol == 0 || contCol >= NUM_MAX_COLUMNAS)){
													flagPrint = true;
												}
												if(flagPrint){
													if(hoja.getRow(rowPos)==null){ hoja.createRow(rowPos);rowsinsert++;}
													if(hoja.getRow(rowPos).getCell(grupo==2 && contCol > 0?(contCol-NUM_MAX_COLUMNAS+1):contCol)==null) hoja.getRow(rowPos).createCell(grupo==2 && contCol > 0?(contCol-NUM_MAX_COLUMNAS+1):contCol);
													
													if(contCol==0){
														ponerStiloCelda(hoja.getRow(rowPos).getCell(contCol), 4, true,1,false);
													}else{
														ponerStiloCelda(hoja.getRow(rowPos).getCell(grupo==2 && contCol > 0?(contCol - NUM_MAX_COLUMNAS+1):contCol), 4, true,3,false);
													}
													if(er!=null){
														valorx=FormatUtil.formatMiles((er.getValue()!=null)?er.getValue().toString():"");
														hoja.getRow(rowPos).getCell(grupo==2 && contCol > 0?(contCol - NUM_MAX_COLUMNAS+1):contCol).setCellValue(valorx);
													}
												}
												contCol=contCol+1;
											}
											if(contCol == NUM_MAX_COLUMNAS && grupo==1){
												break;
											}
										}
										
										rowPos++;
										//////EPOMAYAY 20202012
										//configurarFila(hoja,rowPos,1,11);
										//rowsinsert++;
										//////EPOMAYAY 20202012
										//.append("</tr>");
										
									}
			
									//sb.append("</table>");
								}
								
								grupo++;
								if(grupo==1){
									rowPos++;
								}
							}while(grupo<3 && getNumColumnas(listPoolBancarioTotal) > NUM_MAX_COLUMNAS);
							rowPos++;
							//configurarFila(hoja,rowPos,listPoolBancarioTotal.size(),11);
						}
					}//
					
				}
				
			}
			
			//ini MCG20111108
			int filainsert=rowsinsert;		
			newRowsPerfSubg=newRowsPerfSubg + filainsert;	
			//fin MCG20111108						
				
				//fin empresa
			datos.put("nuevaPosComenPoolBanc",62+ newRowsPerfSubg);
			logger.info("rowPos="+rowPos);
			logger.info("newRowsPerfSubg="+newRowsPerfSubg);
			logger.info("nuevaPosComenPoolBanc="+(newRowsPerfSubg+62));
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
				throw e;
			}
	}

	private int getNumColumnas(List<List> listPoolBancarioTotal){
		
		int contCol =0;
		if(listPoolBancarioTotal.size()>0){
			for (Object lista : listPoolBancarioTotal.get(0)) {
				Map<String, String> hm = (HashMap<String, String>) lista;
				Iterator it = hm.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					contCol++;
				}
			}
		}
		return contCol;
	}
	 private void configurarFila(HSSFSheet hoja,int rowPosx,int numRows,int numCols){
			insertarRows(hoja,rowPosx,numRows);
			List<HSSFRow> rowsNew =crearRows(hoja,rowPosx,numRows);
			for(HSSFRow  rowz: rowsNew){
				crearCols(rowz,numCols);
    		}
	 }
	 
	 private void configurarFilaRent(HSSFSheet hoja,int rowPosx,int numRows,int numCols){
			insertarRows(hoja,rowPosx,numRows);
			List<HSSFRow> rowsNew =crearRows(hoja,rowPosx,numRows);
			for(HSSFRow  rowz: rowsNew){
				crearCols(rowz,numCols);
				hoja.addMergedRegion(new CellRangeAddress(rowz.getRowNum(),rowz.getRowNum(),4,5));
 		}
	 }
	 
	 
	 private void ponerStiloCelda(HSSFCell celda, int estilo, 
			 					  boolean esBold,int alineacion, 
			 					  boolean flgBBVA){
		HSSFFont font=null;
		HSSFCellStyle styleCol=null;
		switch(estilo){

			case 1: 
				
				 styleCol = celda.getSheet().getWorkbook().createCellStyle();
				 styleCol.setFillBackgroundColor( HSSFColor.BLUE.index);
				 font = celda.getSheet().getWorkbook().createFont();
				if(esBold){
					font.setFontHeightInPoints((short)18);
					font.setBoldweight((short)18);
				}
				styleCol.setFont(font);
				celda.setCellStyle(styleCol);
					break;
					
			case 2: 				styleCol = celda.getSheet().getWorkbook().createCellStyle();
			 						styleCol.setFillBackgroundColor( HSSFColor.CORAL.index);
			 						//styleCol.setFillForegroundColor( HSSFColor.CORAL.index);
			 						font = celda.getSheet().getWorkbook().createFont();
									if(esBold){
										font.setFontHeightInPoints((short)16);
										font.setBoldweight((short)18);
									}
									styleCol.setFont(font);
									celda.setCellStyle(styleCol);
									break;
			
			case 3: 
					styleCol = celda.getSheet().getWorkbook().createCellStyle();
					if(flgBBVA){
						styleCol.setFillBackgroundColor( HSSFColor.YELLOW.index);
 						styleCol.setFillForegroundColor( HSSFColor.YELLOW.index);
					}else{
						styleCol.setFillBackgroundColor( HSSFColor.PALE_BLUE.index);
 						styleCol.setFillForegroundColor( HSSFColor.PALE_BLUE.index);
					}
 	 					styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					 font = celda.getSheet().getWorkbook().createFont();
					if(esBold){
						font.setFontHeightInPoints((short)15);
						font.setBoldweight((short)18);
					}
					styleCol.setFont(font);
					celda.setCellStyle(styleCol);
					styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					break;
			
			case 4: 
					styleCol = celda.getSheet().getWorkbook().createCellStyle();
				 	font = celda.getSheet().getWorkbook().createFont();
				 	font.setFontHeightInPoints((short)14);
					styleCol.setFont(font);
					celda.setCellStyle(styleCol);
					styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
					if(alineacion==3)
						styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					break;

		}
	}
	 
	  private void asignarEstilosFiliales(HSSFRow rowz,int numCols){
		  
			    /*rowz.getCell(0).setCellStyle(cellStyle1);
			    rowz.getCell(0).getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
			    rowz.getCell(0).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);*/
		    rowz.getCell(0).setCellStyle(cellStyle1);
		    rowz.getCell(1).setCellStyle(cellStyle2);
		    rowz.getCell(2).setCellStyle(cellStyle3);
		    rowz.getCell(3).setCellStyle(cellStyle4);
		    rowz.getCell(4).setCellStyle(cellStyle5);
		    rowz.getCell(5).setCellStyle(cellStyle6);
			rowz.getCell(6).setCellStyle(cellStyle7);
			rowz.getCell(7).setCellStyle(cellStyle8);
			rowz.getCell(8).setCellStyle(cellStyle9);
			rowz.getCell(9).setCellStyle(cellStyle10);
			rowz.getCell(10).setCellStyle(cellStyle11);
	 }
	  
	  
	 //INI MCG20140702
	  	 private void ponerStiloCeldaRentabilidad(HSSFCell celda, int estilo, 
			 					  boolean esBold,int alineacion){
		HSSFFont font=null;
		HSSFCellStyle styleCol=null;
		switch(estilo){

			case 1: 
				
				 styleCol = celda.getSheet().getWorkbook().createCellStyle();
				 styleCol.setFillBackgroundColor( HSSFColor.BLUE.index);
				 font = celda.getSheet().getWorkbook().createFont();
				if(esBold){
					font.setFontHeightInPoints((short)18);
					font.setBoldweight((short)18);
				}
				styleCol.setFont(font);
				celda.setCellStyle(styleCol);
					break;
					
			case 2: 				styleCol = celda.getSheet().getWorkbook().createCellStyle();
			 						styleCol.setFillBackgroundColor( HSSFColor.CORAL.index);
			 						//styleCol.setFillForegroundColor( HSSFColor.CORAL.index);
			 						font = celda.getSheet().getWorkbook().createFont();
									if(esBold){
										font.setFontHeightInPoints((short)16);
										font.setBoldweight((short)18);
									}
									styleCol.setFont(font);
									celda.setCellStyle(styleCol);
									break;
			
			case 3: 
					styleCol = celda.getSheet().getWorkbook().createCellStyle();
				
					styleCol.setFillBackgroundColor( HSSFColor.BLUE.index);
 					styleCol.setFillForegroundColor( HSSFColor.BLUE.index);
 					
 	 				styleCol.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					 font = celda.getSheet().getWorkbook().createFont();
					 font.setColor(HSSFColor.WHITE.index);
					if(esBold){
						font.setFontHeightInPoints((short)15);
						font.setBoldweight((short)18);						
					}
					styleCol.setFont(font);
					celda.setCellStyle(styleCol);
					
					styleCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
					styleCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					break;
			
			case 4: 
				
				
					styleCol = celda.getSheet().getWorkbook().createCellStyle();
				 	font = celda.getSheet().getWorkbook().createFont();
				 	font.setFontHeightInPoints((short)14);
					styleCol.setFont(font);
					celda.setCellStyle(styleCol);
					styleCol.setBorderTop(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					styleCol.setBorderRight(HSSFCellStyle.BORDER_THIN);
					if(alineacion==3)
						styleCol.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					break;

		}
	}
	  
	 //FIN mcg20140702
	  
	 private void cargarRentabilidad( HSSFSheet hoja, Map datos){
		 int rowPos= 78+newRowsPerfSubg;		 
		 int poscomenRentabilidad=0;
		
		 int rowPosInsert=0;
		 
		 //INI MCG20140701
		List<RentabilidadBEC> listRentabilidadBEC= new ArrayList<RentabilidadBEC>();
		List<RentabilidadBEC> listRentabilidadBECGrupoBD= new ArrayList<RentabilidadBEC>();
		List<RentabilidadBEC>  olistRentabilidadBECTotal=new ArrayList<RentabilidadBEC>();
		List<RentabilidadBEC>  olistRentabilidadBECGrupo=new ArrayList<RentabilidadBEC>();
		String strcodigoCentral="";
		List<Empresa> listEmpresaBEC=new ArrayList<Empresa>();
		
		try {

			String idprograma =datos.get("idPrograma").toString();	
			Programa oprograma 			= (Programa)datos.get("programax");	
			ProgramaBO programaBO =(ProgramaBO)datos.get("programaBO");
			RelacionesBancariasBO relacionesBancariasBO =(RelacionesBancariasBO)datos.get("relacionesBancariasBO");
			String tipoModeloRenta=datos.get("tipoModeloRentabilidad")==null?Constantes.ID_TIPO_CLIENTEGLOBAL:datos.get("tipoModeloRentabilidad").toString().toString();
			
			
			String codGrupo=oprograma.getIdGrupo()==null?"":oprograma.getIdGrupo().toString();
			String nombGrupo=oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
			String codTipoEmpresaGrupo=oprograma.getTipoEmpresa().getId().toString();
			String idempresaPrincipal=oprograma.getIdEmpresa().toString();		
			String strEmpresa = oprograma.getNombreGrupoEmpresa()==null?"":oprograma.getNombreGrupoEmpresa().toString();
		 
			if (codTipoEmpresaGrupo.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				if (tipoModeloRenta.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {	
					//strEmpresa			
					 listRentabilidadBEC= new ArrayList<RentabilidadBEC>();				 
					 listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,idempresaPrincipal);	 
							
					 if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {							
						 crearRentabilidad(hoja,tipoModeloRenta,rowPos,listRentabilidadBEC,strEmpresa);
					 }
				}						
			}else {
			
				
				if (tipoModeloRenta.equals(Constantes.ID_TIPO_BANCAEMPRESA)) {						
					
					listEmpresaBEC =programaBO.findEmpresaByIdprograma(new Long(idprograma));				
					if (listEmpresaBEC!=null && listEmpresaBEC.size()>0) {				
						for (Empresa listaempr:listEmpresaBEC){								
							strcodigoCentral=listaempr.getCodigo();								
							listRentabilidadBEC=new ArrayList<RentabilidadBEC>();					
							listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,strcodigoCentral);						
								if (listRentabilidadBEC != null && listRentabilidadBEC.size() > 0) {
									olistRentabilidadBECTotal.addAll(listRentabilidadBEC);								
								} //listado renta
						}	
						// MCG20140912 Para grupo se busca en la BD
						listRentabilidadBECGrupoBD=new ArrayList<RentabilidadBEC>();					
						listRentabilidadBECGrupoBD =relacionesBancariasBO.generaModeloRentabilidad(idprograma,codGrupo);	
						if (listRentabilidadBECGrupoBD!=null && listRentabilidadBECGrupoBD.size()>0){						
							olistRentabilidadBECGrupo.addAll(listRentabilidadBECGrupoBD);
						}else{
							olistRentabilidadBECGrupo=relacionesBancariasBO.crearRentabilidadGrupo(olistRentabilidadBECTotal,codGrupo,idprograma);
						}						
						//nombGrupo
						crearRentabilidad(hoja,tipoModeloRenta,rowPos,olistRentabilidadBECGrupo,nombGrupo);
						rowPos=rowPos+9;
						
						/////NNNN
						
						for (Empresa listaempr:listEmpresaBEC){		
							strEmpresa=listaempr.getNombre();
							strcodigoCentral=listaempr.getCodigo();							
							//strEmpresa									
							listRentabilidadBEC=new ArrayList<RentabilidadBEC>();						
							configurarFilaRent(hoja,rowPos,10,11);							
							listRentabilidadBEC=relacionesBancariasBO.generaModeloRentabilidad(idprograma,strcodigoCentral);									
							crearRentabilidad(hoja,tipoModeloRenta,rowPos+1,listRentabilidadBEC,strEmpresa);
							rowPos=rowPos+10;
							rowPosInsert=rowPosInsert+10;
										
						}
						////NNNNN	
						
					}//lista empresa
				}
			//	
			}	
			newRowsPerfSubg=newRowsPerfSubg + rowPosInsert;
			poscomenRentabilidad=78+newRowsPerfSubg+12;
			 datos.put("nuevaPosRentabilidad", poscomenRentabilidad);
			//FIN MCG20140701
		
		} catch (Exception e) {
			 
			logger.error("error cargarentabilida"+e.getMessage());
		}	 

			
			
	 }	 
	 
	 

	 
	 //ini MCG20140701
	 private void crearRentabilidad( HSSFSheet hoja, String tipoModeloRentabilidad,int orowPos,List<RentabilidadBEC> listRentabilidadBEC,String nombGrupoEmpresa){
		 int rowPos= orowPos;
		 int rowPosD=orowPos;
		 
		 try {	 
		
			 if(listRentabilidadBEC != null &&	 listRentabilidadBEC.size()>0){
				 
				 
				 if(hoja.getRow(rowPos-1).getCell(0)== null){
						hoja.getRow(rowPos-1).createCell(0);
					}
				 hoja.getRow(rowPos-1).getCell(0).setCellValue(nombGrupoEmpresa);
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPos-1).getCell(0), 2, true,1);	
	
				 
				 if(hoja.getRow(rowPos).getCell(0)== null){
						hoja.getRow(rowPos).createCell(0);
					}
				 hoja.getRow(rowPos).getCell(0).setCellValue("Comercial");
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(0), 3, true,1);	
				 
				 if(hoja.getRow(rowPos).getCell(1)== null){
						hoja.getRow(rowPos).createCell(1);
					}
				 hoja.getRow(rowPos).getCell(1).setCellValue("Año Ant.");
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(1), 3, true,1);
				 
				 if(hoja.getRow(rowPos).getCell(2)== null){
						hoja.getRow(rowPos).createCell(2);
					}
				 hoja.getRow(rowPos).getCell(2).setCellValue("Mes Acum.");
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(2), 3, true,1);
				 
				 
				 
				 if(hoja.getRow(rowPosD).getCell(4)== null){
						hoja.getRow(rowPosD).createCell(4);
					}
				 hoja.getRow(rowPosD).getCell(4).setCellValue("Rentabilidad");	
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(4), 3, true,1);
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(5), 3, true,1);
				 
				 if(hoja.getRow(rowPosD).getCell(6)== null){
						hoja.getRow(rowPosD).createCell(6);
					}
				 hoja.getRow(rowPosD).getCell(6).setCellValue("Año Ant.");	
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(6), 3, true,1);
				 
				 if(hoja.getRow(rowPosD).getCell(7)== null){
						hoja.getRow(rowPosD).createCell(7);
					}
				 hoja.getRow(rowPosD).getCell(7).setCellValue("Mes Acum.");	
				 ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(7), 3, true,1);
				 
				 
				 for (RentabilidadBEC orentabilidadbec: listRentabilidadBEC) {
		
						if (orentabilidadbec.getTipoRentabilidad().equals(Constantes.ID_TIPO_COMERCIAL)){
							rowPos++;
							if(hoja.getRow(rowPos).getCell(0)== null){
								hoja.getRow(rowPos).createCell(0);
							}
							hoja.getRow(rowPos).getCell(0).setCellValue(orentabilidadbec.getDescripcionMonto());
							 ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(0), 4, true,1);
							
							if(hoja.getRow(rowPos).getCell(1)== null){
								hoja.getRow(rowPos).createCell(1);
							}							
							hoja.getRow(rowPos).getCell(1).setCellValue(orentabilidadbec.getMontoAnual()==null?"":orentabilidadbec.getMontoAnual().trim());
							ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(1), 4, true,3);
							
							//ini MCG20130823
							if(hoja.getRow(rowPos).getCell(2)== null){
								hoja.getRow(rowPos).createCell(2);
							}
												
							hoja.getRow(rowPos).getCell(2).setCellValue(orentabilidadbec.getMonto()==null?"":orentabilidadbec.getMonto().trim());
							ponerStiloCeldaRentabilidad(hoja.getRow(rowPos).getCell(2), 4, true,3);

							//fin MCG20130823
		
						}
				 }
				for (RentabilidadBEC orentabilidadbec2: listRentabilidadBEC) {
						if (orentabilidadbec2.getTipoRentabilidad().equals(Constantes.ID_TIPO_RENTABILIDAD)){
					
								rowPosD++;
								if(hoja.getRow(rowPosD).getCell(4)== null){
									hoja.getRow(rowPosD).createCell(4);
								}
								hoja.getRow(rowPosD).getCell(4).setCellValue(orentabilidadbec2.getDescripcionMonto());
								ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(4), 4, true,1);
								ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(5), 4, true,1);
								
								if(hoja.getRow(rowPosD).getCell(6)== null){
									hoja.getRow(rowPosD).createCell(6);
								}
								hoja.getRow(rowPosD).getCell(6).setCellValue(orentabilidadbec2.getMontoAnual()==null?"":orentabilidadbec2.getMontoAnual().trim());
								ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(6), 4, true,3);
								
								//ini MCG20130823
								if(hoja.getRow(rowPosD).getCell(7)== null){
									hoja.getRow(rowPosD).createCell(7);
								}
								hoja.getRow(rowPosD).getCell(7).setCellValue(orentabilidadbec2.getMonto()==null?"":orentabilidadbec2.getMonto().trim());
								ponerStiloCeldaRentabilidad(hoja.getRow(rowPosD).getCell(7), 4, true,3);
								//fin MCG20130823										
						}	
					}//FOR
	
			 }//LISTA
		 } catch (Exception e) {
		 
			 logger.error("error crearrentabilida"+e.getMessage());
		 }
		 
	 }
	 
	 //fin MCG20140701
	 
	 
	 
}
