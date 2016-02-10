package pe.com.bbva.iipf.reportes.bo.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.PoliticasRiesgoBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBlobBO;
import pe.com.bbva.iipf.pf.bo.PropuestaRiesgoBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.bo.RatingBlobBO;
import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.bbva.iipf.pf.bo.SintesisEconomicoBlobBO;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.EstructuraLimite;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.FilaEmpresaHoja;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.ProgramaBlob;
import pe.com.bbva.iipf.pf.model.Rating;
import pe.com.bbva.iipf.pf.model.RatingBlob;
import pe.com.bbva.iipf.pf.model.SintesisEconomicoBlob;
import pe.com.bbva.iipf.reportes.bo.GeneradorReporteBO;
import pe.com.bbva.iipf.reportes.dao.impl.GeneradorReporteExcelDAOImpl;
import pe.com.bbva.iipf.reportes.plantilla.SheetDefinition;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefAnalisSectorialImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefCaratula2Impl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefCaratulaImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefDatosBasicos2Impl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefDatosBasicosImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefEstadosFinancierosImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefExtractoEstadosFinancierosImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefFactoresRiesgoImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefPoliticGrupoImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefPosicBancosImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefPropRiesgoImpl;
import pe.com.bbva.iipf.reportes.plantilla.impl.SheetDefRelacBancariasImpl;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.util.ExcelHelper;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

@Service("generadorReporteBO")
public class GeneradorReporteExcelBOImpl extends GenericBOImpl<Programa> implements GeneradorReporteBO{
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private Map<String, Object> params;
	@Resource
	private GeneradorReporteExcelDAOImpl generadorReporteExcelDAOImpl;
	@Resource
	private RelacionesBancariasBO relacionesBancariasBO;
	@Resource
	private PoliticasRiesgoBO politicasRiesgoBO;
	@Resource
	private PropuestaRiesgoBO propuestaRiesgoBO;
	@Resource
	private AnexoBO anexoBO;
	
	@Resource
	private ProgramaBlobBO programaBlobBO;
	
	@Resource
	private DatosBasicosBlobBO datosBasicosBlobBO;
	
	@Resource
	private RatingBlobBO ratingBlobBO;
	
	@Resource
	private RatingBO ratingBO;
	
	@Resource
	private TablaBO tablaBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private DatosBasicosBO datosBasicosBO;
	
	@Resource
	private SintesisEconomicoBlobBO sintesisEconomicoBlobBO;
	
	
	private TreeMap<String,HashMap<String,String>> listaImg = new TreeMap<String,HashMap<String,String>>();
	
	private List<AnexoColumna> listaColumnas = new ArrayList<AnexoColumna>();
	private FilaAnexo filaTotal = new FilaAnexo();
	private List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
	private List<AnexoColumna> columnasTotales = new ArrayList<AnexoColumna>();
	@Override
	public void buildReportExcelWithTemplate(HSSFWorkbook wbPF) throws Exception {
		logger.info("INICIO buildReportExcelWithTemplate");
        Map<String, Object> paramDatos = new HashMap<String, Object>();
        paramDatos.putAll(this.getParams());
        Programa programax = (Programa)paramDatos.get("programax");
        List<Empresa> olistEmpresa=(List<Empresa>)paramDatos.get("listaEmpresaByPrograma");      
       
        SheetDefinition sd=  new SheetDefDatosBasicosImpl();        
        String tipoEmpres=programax.getTipoEmpresa().getId().toString();    
        
			if(tipoEmpres.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				 String codEmpresa=programax.getIdEmpresa().toString();
				 List listaDatosBasicos=generadorReporteExcelDAOImpl.listDatosBasicos(paramDatos,codEmpresa);
			     paramDatos.put("listaDatosBasicos", listaDatosBasicos);  
			     sd=  new SheetDefDatosBasicosImpl();		        
			     sd.fillDataSheet(wbPF.getSheet("Datos Basicos"), paramDatos);	
			     
			     int posComenComp = Integer.parseInt(paramDatos.get("nuevaPosComenCompVent").toString());				        
			        paramDatos.put("nuevaPosComenCompVent_prin", posComenComp); 
			     //DATOS BASICOS 2
			     
		        sd=  new SheetDefDatosBasicos2Impl();
		        sd.fillDataSheet(wbPF.getSheet("Datos Basicos 2"), paramDatos);
		        
		        int posPosEspacioLibre = Integer.parseInt(paramDatos.get("nuevaPosEspacioLibre").toString());				        
		        paramDatos.put("nuevaPosEspacioLibre_prin", posPosEspacioLibre);
		        
		        
		        //Rating Empresa
		       
		        try{				
		        List<Rating> rating = ratingBO.findRating(programax.getId(),codEmpresa);
		        paramDatos.put("rating", rating);
		        }catch(Exception e){
		        	logger.error(StringUtil.getStackTrace(e));
		        }
				
			}else{	
							
		        //Rating Grupo			       
		        try{
		        String idGrupo=String.valueOf(programax.getIdGrupo());				
		        List<Rating> rating = ratingBO.findRating(programax.getId(),idGrupo);
		        paramDatos.put("rating", rating);
		        }catch(Exception e){
		        	logger.error(StringUtil.getStackTrace(e));
		        }
								 
				 int cont=2;
				 for(Empresa oempresa:olistEmpresa){					 
					  String codEmpresa=oempresa.getCodigo();
					  String NombreEmpresa=oempresa.getNombre()==null?"":oempresa.getNombre().toString().trim();
					  String strIndexHoja=String.valueOf(oempresa.getIndexHoja());
					    //Hacemos llamadas a base de datos para obtener lo necesario.
				        List listaDatosBasicos=generadorReporteExcelDAOImpl.listDatosBasicos(paramDatos,codEmpresa);
				        paramDatos.remove("listaDatosBasicos");
				        paramDatos.put("listaDatosBasicos", listaDatosBasicos);  
				        paramDatos.put("nombEmpreesaDatosBasicos", NombreEmpresa);  
				        
						Empresa empresaprin= new Empresa();	
						empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(programax.getId(),codEmpresa);						
						
						if (empresaprin!=null && !empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){ 
							cont=cont+1;	
							
								List<DatosBasico> listdatosbas=datosBasicosBO.getListaDatosBasico(programax,codEmpresa);
								if (listdatosbas!=null && listdatosbas.size()>0){
									DatosBasico datosbas =listdatosbas.get(0);
									programax.setActividadPrincipal(datosbas.getActividadPrincipal());
									programax.setPais(datosbas.getPais());
									programax.setAntiguedadNegocio(datosbas.getAntiguedadNegocio());
									programax.setAntiguedadCliente(datosbas.getAntiguedadCliente());
									programax.setGrupoRiesgoBuro(datosbas.getGrupoRiesgoBuro());
									
									programax.setComentAccionariado(datosbas.getComentAccionariado());
									programax.setComentPartiSignificativa(datosbas.getComentPartiSignificativa());
									programax.setComentRatinExterno(datosbas.getComentRatinExterno());
									programax.setComentvaloraGlobal(datosbas.getComentvaloraGlobal());									
								}else{									
									programax.setActividadPrincipal("");
									programax.setPais("");
									programax.setAntiguedadNegocio(null);
									programax.setAntiguedadCliente(null);
									programax.setGrupoRiesgoBuro("");
									
									programax.setComentAccionariado("");
									programax.setComentPartiSignificativa("");
									programax.setComentRatinExterno("");
									programax.setComentvaloraGlobal("");
								}
						        sd=  new SheetDefDatosBasicosImpl();		        
						        sd.fillDataSheet(wbPF.getSheet("Datos Basicos ("+strIndexHoja+")"), paramDatos);				        
						        int posComenComp = Integer.parseInt(paramDatos.get("nuevaPosComenCompVent").toString());				        
						        paramDatos.put("nuevaPosComenCompVent_" + codEmpresa, posComenComp); 
						        
						        sd=  new SheetDefDatosBasicos2Impl();
						        sd.fillDataSheet(wbPF.getSheet("Datos Basicos 2 ("+strIndexHoja+")"), paramDatos);
						        int posPosEspacioLibre = Integer.parseInt(paramDatos.get("nuevaPosEspacioLibre").toString());				        
						        paramDatos.put("nuevaPosEspacioLibre_" + codEmpresa, posPosEspacioLibre); 						       
								//Rating Empresa Secundaria-Anexo
						        try{	
							        List<Rating> rating = ratingBO.findRating(programax.getId(),codEmpresa);
							        paramDatos.put("rating_" + codEmpresa, rating);
							        }catch(Exception e){
							        	logger.error(StringUtil.getStackTrace(e));
							        }
						        
						        
						        
						}else{					
					        sd=  new SheetDefDatosBasicosImpl();		        
					        sd.fillDataSheet(wbPF.getSheet("Datos Basicos"), paramDatos);
					        int posComenComp = Integer.parseInt(paramDatos.get("nuevaPosComenCompVent").toString());				        
					        paramDatos.put("nuevaPosComenCompVent_prin", posComenComp); 
					        
					        sd=  new SheetDefDatosBasicos2Impl();
					        sd.fillDataSheet(wbPF.getSheet("Datos Basicos 2"), paramDatos);
					        int posPosEspacioLibre = Integer.parseInt(paramDatos.get("nuevaPosEspacioLibre").toString());				        
					        paramDatos.put("nuevaPosEspacioLibre_prin", posPosEspacioLibre);
					        
					        //Rating Empresa Principal
					        try{	
						        List<Rating> rating = ratingBO.findRating(programax.getId(),codEmpresa);
						        paramDatos.put("rating_" + codEmpresa, rating);
						        }catch(Exception e){
						        	logger.error(StringUtil.getStackTrace(e));
						}						
					 
				}	
				
			}			
		}

        
//        sd=  new SheetDefDatosBasicos2Impl();
//        sd.fillDataSheet(wbPF.getSheet("Datos Basicos 2"), paramDatos);
        
        
//        logger.info("inicio carga sintesis economico financiero");
//        try{
//		ratingBO.setPrograma((Programa)paramDatos.get("programax"));
//        List<Rating> rating = ratingBO.findRating();
//        paramDatos.put("rating", rating);
//        }catch(Exception e){
//        	logger.error(StringUtil.getStackTrace(e));
//        }
        
        sd=  new SheetDefEstadosFinancierosImpl();
        sd.fillDataSheet(wbPF.getSheet("Sintesis Econom-Financiero"), paramDatos);
        logger.info("fin carga sintesis economico financiero");
        
        logger.info("ini Extracto");
        sd=  new SheetDefExtractoEstadosFinancierosImpl();
        sd.fillDataSheet(wbPF.getSheet("Extracto"), paramDatos);
        logger.info("fin Extracto");

        //Aqui verificar si La organizacion analizada es Grupo Economico, por lo cual se debera  buscar los estados 
        sd=  new SheetDefAnalisSectorialImpl();
        sd.fillDataSheet(wbPF.getSheet("Analisis Sectorial"), paramDatos);
        
        List<LineasRelacionBancarias> listaFilSubgr = relacionesBancariasBO.findByLineasRelacionesBancarias(Long.parseLong((String)paramDatos.get("idPrograma")));
        paramDatos.put("listaFilSubgrupo",listaFilSubgr);
		List<OpcionPool> listaopcionPool = relacionesBancariasBO.findOpcionPool((String)paramDatos.get("idPrograma"), Constantes.ID_TIPO_OPPOOL_EMPRESA);
        paramDatos.put("listaopcionPool",listaopcionPool);
        List<OpcionPool> listaopcionPool1=relacionesBancariasBO.findOpcionPool((String)paramDatos.get("idPrograma"), Constantes.ID_TIPO_OPPOOL_BANCO);
        paramDatos.put("listaopcionPool1",listaopcionPool1);
        
        List<OpcionPool> listaopcionPool2=relacionesBancariasBO.findOpcionPool((String)paramDatos.get("idPrograma"), Constantes.ID_TIPO_OPPOOL_TIPODEUDA);
        paramDatos.put("listaopcionPool2",listaopcionPool2);
        
        
        paramDatos.put("relacionesBancariasBO",relacionesBancariasBO);
        paramDatos.put("tipoModeloRentabilidad", programax.getIdmodeloRentabilidad());
        
        //ADD 060122012 EPOMAYAY
        String idtipoMilesRB=programax.getTipoMilesRB()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programax.getTipoMilesRB().toString();
		Tabla otipoMilesRB=new Tabla();
		otipoMilesRB=tablaBO.obtienePorId(Long.valueOf(idtipoMilesRB));
		String strtipoMilesRB="CIFRAS EN " + otipoMilesRB.getDescripcion() +" USD";
	    paramDatos.put("tipomilesRB",strtipoMilesRB);	
        //ADD 060122012 EPOMAYAY
	    
        sd= new SheetDefRelacBancariasImpl();;
        sd.fillDataSheet(wbPF.getSheet("Relaciones Bancarias"), paramDatos);
	
  	   
        paramDatos.remove("relacionesBancariasBO");
        sd=  new SheetDefFactoresRiesgoImpl();
        sd.fillDataSheet(wbPF.getSheet("Factores Riesgo"), paramDatos);
        
        List<EstructuraLimite> listEstructuraLimite = new ArrayList<EstructuraLimite>();
		listEstructuraLimite=propuestaRiesgoBO.findEstructuraLimiteByIdprograma(programax.getId());	
	    paramDatos.put("listEstructuraLimite",listEstructuraLimite);
        
	    //ini MCG20111107
	    paramDatos.put("tipoEstructura",programax.getTipoEstructura());	    
		String idtipoMiles=programax.getTipoMiles()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programax.getTipoMiles();
		Tabla otipoMiles=new Tabla();
		otipoMiles=tablaBO.obtienePorId(Long.valueOf(idtipoMiles));
		String strtipoMiles="Cifras en " + otipoMiles.getDescripcion() +" de Dolares(USD)";
	    paramDatos.put("tipomiles",strtipoMiles);	    
	    //fin MCG20111107
	    
	    sd=  new SheetDefPropRiesgoImpl();
        sd.fillDataSheet(wbPF.getSheet("Prop. Riesgo"), paramDatos);

        List<LimiteFormalizado>  listaLimForma = politicasRiesgoBO.findLimiteFormalizadoByIdprograma(Long.parseLong((String)paramDatos.get("idPrograma")));
        //ADD 060122012 EPOMAYAY
        String idtipoMilesPLR=programax.getTipoMilesPLR()==null?Constantes.ID_TABLA_TIPOMILES_MILES:programax.getTipoMilesPLR().toString();
		Tabla otipoMilesPLR=new Tabla();
		otipoMilesPLR=tablaBO.obtienePorId(Long.valueOf(idtipoMilesPLR));
		String strtipoMilesPLR="CIFRAS EN " + otipoMilesPLR.getDescripcion() +" DE DÓLARES";
	    paramDatos.put("tipomilesPLR",strtipoMilesPLR);	
	    //ini MCG20130805
	    
		
	    List<LimiteFormalizado> listLimiteFormalizadoAnexo=new ArrayList<LimiteFormalizado>();        		
	    listLimiteFormalizadoAnexo =anexoBO.loadLimiteFormalizadoByAnexos(programax);
	    paramDatos.put("listaLimFormaAnexo",listLimiteFormalizadoAnexo); 
	    //ini MCG20130805
	    
	    
        //ADD 060122012 EPOMAYAY
        paramDatos.put("listaLimForma",listaLimForma); 
        sd=  new SheetDefPoliticGrupoImpl();
        sd.fillDataSheet(wbPF.getSheet("Politica Grupo"), paramDatos);

  	   //Aqui la fuente es los anexos
    	listaColumnas = new ArrayList<AnexoColumna>();
    	listaFilaAnexos = new ArrayList<FilaAnexo>();
    	columnasTotales = new ArrayList<AnexoColumna>();
		Long  idPrograma = Long.valueOf((String)paramDatos.get("idPrograma"));
		Programa programaz = new Programa();
		programaz.setId(idPrograma);
		anexoBO.setPrograma(programaz);

		listaFilaAnexos = anexoBO.findAnexos();
//		iniciarColumas();
//		
//		for(AnexoColumna acol : listaColumnas){
//			columnasTotales.add(new AnexoColumna());
//		}
//		filaTotal.setListaAnexoColumna(columnasTotales);
//		calcularMontos(listaFilaAnexos);
		filaTotal = anexoBO.calcularMontoTotal(listaFilaAnexos);
		
		
		paramDatos.put("listaColumnas",listaColumnas);
		paramDatos.put("listaFilaAnexos",listaFilaAnexos);
		paramDatos.put("filaTotal",filaTotal);
		
        sd=  new SheetDefPosicBancosImpl();
        sd.fillDataSheet(wbPF.getSheet("Posiciones Por Bancos"), paramDatos);
        
        //Los psamos al fina porque se alimenta de data generada en : Politica Grupo
        sd=new SheetDefCaratulaImpl();
        sd.fillDataSheet(wbPF.getSheet("Caratula"), paramDatos);
  
        sd=new SheetDefCaratula2Impl();
        sd.fillDataSheet(wbPF.getSheet("Caratula (2)"), paramDatos);
        
       // POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("c:/temporal.xls"));
		
		//FileOutputStream fos = new FileOutputStream("c:/reporteeeeeeee___________.xls");
	      
		//HSSFWorkbook wb = new HSSFWorkbook(fs);
		//insertImagesToExcel("", wb);
	   // wb.write(fos);
	  //  fos.close();
        //completar con las imagenes de los editores de texto
        //epomayay
//        crearLista();
//        generarImagenes();
        
        
        insertImagesToExcel(params.get("nameTemplateFinal").toString(),
      			  			wbPF,
      			  			paramDatos);
        limpiarImagenes();
   	   logger.info("  GeneradorReporteExcelBOImpl FIN ");
	}
	
	public boolean ExisteEmpresaenEEFF(String Codigoempresa,List<Map<String,Object>> listaEEFF){
		boolean bexiste=false;
		try {
			 for(Map<String,Object> mpcodEmpresEEFF:listaEEFF){					
				 String codempresaEEFF=(String)mpcodEmpresEEFF.get("codigoEmpresaSEF");
				 if (codempresaEEFF.equals(Codigoempresa)){
					 bexiste=true;
					 break;
				 }
				
			 }		
			return bexiste;
		} catch (Exception e) {
			return bexiste;
		}
	}

	/**
	 * Este metodo transforma un codigo HTML a una imagen
	 * para luego insertarlo dentro de una hoja excel
	 * @param html
	 * @param fileNameExcel
	 * @param nameSheet
	 * @param rutTempToGenerate
	 * @param posx
	 * @param posy
	 * @param weight
	 * @param height
	 */
	@Override
	public  void insertImagesToExcel(String fileNameExcel,
									 HSSFWorkbook wb,
									 Map<String,Object> datos){
		try {
			listaImg = new TreeMap<String,HashMap<String,String>>();
	        crearLista(datos);
	        generarImagenes();
	        addLogos();
	        logger.info("cantidad de imagenes = " + listaImg.size());
	        Programa programatr = (Programa)datos.get("programax");		
	        setDatosPosicionExcel(wb.getSheet("Parametro"),
	        					  (programatr.getTipoEstructura()==null?"1":programatr.getTipoEstructura()),
	        					  datos.get("numEpresas").toString());
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public void setDatosPosicionExcel(HSSFSheet hoja,
									  String tipoEstructura,
									  String numeroEmpresas){
		int rowY			  = 1;
		int colX			  = 0;
		for(HashMap<String,String> img : listaImg.values()){
			if(hoja.getRow(rowY)==null){
				hoja.createRow(rowY);
			}
			if(hoja.getRow(rowY).getCell(colX)==null){
				hoja.getRow(rowY).createCell(colX);
			}
			
			String indice= img.get("indiceSEF");
			String tipoespecif=img.get("tipoSEF");
			String strcampo=img.get("campo");
			if (tipoespecif.equals(Constantes.TIPO_ESPECIFICODB) && !indice.equals("")){
				 strcampo=img.get("campo") +indice;
			}else{
				 strcampo=img.get("campo");
			}			
			hoja.getRow(rowY).getCell(colX).setCellValue(strcampo);
			if(hoja.getRow(rowY).getCell(colX+1)==null){
				hoja.getRow(rowY).createCell(colX+1);
			}
			hoja.getRow(rowY).getCell(colX+1).setCellValue(img.get("y"));
			rowY++;
		}
		if(hoja.getRow(rowY)==null){
			hoja.createRow(rowY);
		}
		if(hoja.getRow(rowY).getCell(colX)==null){
			hoja.getRow(rowY).createCell(colX);
		}
		hoja.getRow(rowY).getCell(colX).setCellValue("tipoEstructura");
		if(hoja.getRow(rowY).getCell(colX+1)==null){
			hoja.getRow(rowY).createCell(colX+1);
		}
		hoja.getRow(rowY).getCell(colX+1).setCellValue(tipoEstructura);
		
		rowY++;
		if(hoja.getRow(rowY)==null){
			hoja.createRow(rowY);
		}
		if(hoja.getRow(rowY).getCell(colX)==null){
			hoja.getRow(rowY).createCell(colX);
		}
		hoja.getRow(rowY).getCell(colX).setCellValue("numeroEmpresas");
		if(hoja.getRow(rowY).getCell(colX+1)==null){
			hoja.getRow(rowY).createCell(colX+1);
		}
		hoja.getRow(rowY).getCell(colX+1).setCellValue(numeroEmpresas);
		
	}
	
	/**
	 * Método que genere las imagenes a partir del codigo html
	 * de los editores de texto.
	 */
	public synchronized void generarImagenes(){
		//se obtiene el id del programa de session
		Long idPrograma = Long.valueOf(params.get("idPrograma").toString());
		Programa programa = new Programa();
		programa.setId(idPrograma);
		try {
			String directorioTemporal = params.get("dir_temporal_to_export").toString();
			ProgramaBlob programaBlob = programaBlobBO.findBlobByPrograma(programa);
			StringBuilder stb = null;
			Method method = null;
			if(programaBlob != null){
				for(HashMap<String,String> imagen : listaImg.values()){
					String codEmpresa="";
					try {
						
						if (imagen.get("tipoSEF").equals(Constantes.TIPO_NORMAL)){
							method = programaBlob.getClass().getMethod("get"+imagen.get("campo"), null);
							byte[] html = (byte[])method.invoke(programaBlob, null);
							if (html!=null){
								stb = new StringBuilder();
							    for(byte x :html){
							      	stb.append((char)FormatUtil.getCharUTF(x));					          	
							    }
							    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
							      											 directorioTemporal,
							       											 imagen.get("campo"));
							    imagen.put("dirimagen", dirImg);
							}
						
						}else if(imagen.get("tipoSEF").equals(Constantes.TIPO_ESPECIFICO)){
							
							codEmpresa=imagen.get("codigEmpresaSEF").toString()	;					
							String strIndice=imagen.get("indiceSEF").toString()	;
							SintesisEconomicoBlob sintesisEconomicoBlob = sintesisEconomicoBlobBO.findSintEcoBlobByPrograma(programa,codEmpresa);
							if(sintesisEconomicoBlob !=null){
								method = sintesisEconomicoBlob.getClass().getMethod("get"+imagen.get("campo"), null);
								byte[] html = (byte[])method.invoke(sintesisEconomicoBlob, null);
								if (html!=null){
									stb = new StringBuilder();
								    for(byte x :html){
								      	stb.append((char)FormatUtil.getCharUTF(x));					          	
								    }
//								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
//								      											 directorioTemporal,
//								       											 imagen.get("campo")+codEmpresa);
								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
 											 									directorioTemporal,
 											 									imagen.get("campo")+ strIndice);
								    imagen.put("dirimagen", dirImg);
								}	
							}							
						}else if(imagen.get("tipoSEF").equals(Constantes.TIPO_ESPECIFICODB)){
							
							codEmpresa=imagen.get("codigEmpresaSEF").toString()	;					
							String strIndice=imagen.get("indiceSEF").toString()	;
							logger.info("ini TIPO_ESPECIFICODB");
							logger.info("cantidad de imagenes = " + codEmpresa);
							logger.info("cantidad de imagenes = " + strIndice);
							logger.info("fin TIPO_ESPECIFICODB");
							DatosBasicoBlob odatosBasicoBlob = datosBasicosBlobBO.findDatosBasicoBlobByPrograma(programa,codEmpresa);
							if(odatosBasicoBlob !=null){
								method = odatosBasicoBlob.getClass().getMethod("get"+imagen.get("campo"), null);
								byte[] html = (byte[])method.invoke(odatosBasicoBlob, null);
								if (html!=null){
									stb = new StringBuilder();
								    for(byte x :html){
								      	stb.append((char)FormatUtil.getCharUTF(x));					          	
								    }
//								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
//								      											 directorioTemporal,
// 								       											 imagen.get("campo")+codEmpresa);
								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
 											 									directorioTemporal,
 											 									imagen.get("campo")+ strIndice);
								    imagen.put("dirimagen", dirImg);
								}	
							}
						}else if(imagen.get("tipoSEF").equals(Constantes.TIPO_ESPECIFICORATING)){
							
							codEmpresa=imagen.get("codigEmpresaSEF").toString()	;					
							String strIndice=imagen.get("indiceSEF").toString()	;
							logger.info("ini TIPO_ESPECIFICORATING");
							logger.info("cantidad de imagenes = " + codEmpresa);
							logger.info("cantidad de imagenes = " + strIndice);
							logger.info("fin TIPO_ESPECIFICORATING");
							RatingBlob oratingBlob = ratingBlobBO.findRatingBlobByPrograma(programa,codEmpresa);
							if(oratingBlob !=null){
								method = oratingBlob.getClass().getMethod("get"+imagen.get("campo"), null);
								byte[] html = (byte[])method.invoke(oratingBlob, null);
								if (html!=null){
									stb = new StringBuilder();
								    for(byte x :html){
								      	stb.append((char)FormatUtil.getCharUTF(x));					          	
								    }
//								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
//								      											 directorioTemporal,
//								       											 imagen.get("campo")+codEmpresa);
								    String dirImg = ExcelHelper.convertHTMLtoIMG(stb.toString(), 
 											 									directorioTemporal,
 											 									imagen.get("campo")+ strIndice);
								    imagen.put("dirimagen", dirImg);
								}	
							}								
						}	
						
						
						
					} catch (SecurityException e) {
						logger.error(StringUtil.getStackTrace(e));
					} catch (NoSuchMethodException e) {
						logger.error(StringUtil.getStackTrace(e));
					} catch (IllegalArgumentException e) {
						logger.error(StringUtil.getStackTrace(e));
					} catch (IllegalAccessException e) {
						logger.error(StringUtil.getStackTrace(e));
					} catch (InvocationTargetException e) {
						logger.error(StringUtil.getStackTrace(e));
					}
		    	}
			}
			
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void limpiarImagenes(){
		//quitar los logos y la imagen default
		listaImg.remove("logo1");
		listaImg.remove("logo2");
		String imgDefault = params.get("dirPlantillas").toString()+File.separator+"Blanco.png";
		for(HashMap<String,String> imagen : listaImg.values()){
			if(!imagen.get("dirimagen").toString().equals(imgDefault)){
				File file = new File(imagen.get("dirimagen"));
				file.delete();
			}
		}
	}
	
	public void addLogos(){
		HashMap<String,String> posicion = new HashMap<String,String>();
		String codEmpresaDefault="";
		String strIndiceDefault="";
		posicion.put("alto", "3");
		posicion.put("largo", "3");
		posicion.put("x", "0");
		posicion.put("y", "1");
		posicion.put("sheet", "Caratula");
		posicion.put("campo", "logo1");
		posicion.put("dirimagen", params.get("dirPlantillas").toString()+File.separator+"bbva.png");
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("logo1", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "3");
		posicion.put("largo", "3");
		posicion.put("x", "0");
		posicion.put("y", "1");
		posicion.put("sheet", "Caratula (2)");
		posicion.put("campo", "logo2");
		posicion.put("dirimagen", params.get("dirPlantillas").toString()+File.separator+"bbva.png");
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("logo2", posicion);
	}
	
	/**
	 * Método para obtener la posicion y la hoja donde se insertara una 
	 * imagen dentro del excel
	 * @param dirImg
	 */
	public void crearLista(Map<String,Object> datos){		
		String imgDefault = params.get("dirPlantillas").toString()+File.separator+"Blanco.png";
		String codEmpresaDefault="";
		String strIndiceDefault="";
		HashMap<String,String> posicion = new HashMap<String,String>();
		
		posicion.put("alto", "8");
		posicion.put("largo", "8");
		posicion.put("x", "0");
		posicion.put("y", ""+datos.get("nuevaPosComenCompVent_prin"));//se obtiene de SheetDefDatosBasicosImpl
		posicion.put("sheet", "Datos Basicos");
		posicion.put("campo", "ComenComprasVentas");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ComenComprasVentas", posicion);
		
		//ini MCG20111107
		posicion = new HashMap<String,String>();
		posicion.put("alto", "8");
		posicion.put("largo", "8");
		posicion.put("x", "0");		
		//MIL posicion.put("y", ""+(Integer.parseInt("9")+Integer.parseInt(datos.get("nuevaPosComenCompVent").toString())));		
		posicion.put("y", ""+(Integer.parseInt("6")+Integer.parseInt(datos.get("nuevaPosComenCompVent_prin").toString())));		
		posicion.put("sheet", "Datos Basicos");
		posicion.put("campo", "Concentracion");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("Concentracion", posicion);
		//fin MCG20111107		
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "10");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		//posicion.put("y", "16");
		posicion.put("y", ""+datos.get("nuevaPosEspacioLibre_prin"));
		posicion.put("sheet", "Datos Basicos 2");
		posicion.put("campo", "EspacioLibre");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("EspacioLibre", posicion);
		
		//ini MCG20130801
		posicion = new HashMap<String,String>();
		posicion.put("alto", "10");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", ""+(Integer.parseInt("31")+Integer.parseInt(datos.get("nuevaPosEspacioLibre_prin").toString())));
		posicion.put("sheet", "Datos Basicos 2");
		posicion.put("campo", "Valoracion");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("Valoracion", posicion);
		
		//fin MCG20130801
		
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "18");
		posicion.put("largo", "20");
		posicion.put("x", "0");
		posicion.put("y", "57");
		posicion.put("sheet", "Sintesis Econom-Financiero");
		posicion.put("campo", "ComenSituFinanciera");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ComenSituFinanciera", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "18");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "95");
		posicion.put("sheet", "Sintesis Econom-Financiero");
		posicion.put("campo", "ComenSituEconomica");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ComenSituEconomica", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "18");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "124");
		posicion.put("sheet", "Sintesis Econom-Financiero");
		posicion.put("campo", "ValoracionEconFinanciera");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ValoracionEconFinanciera", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "18");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "141");
		posicion.put("sheet", "Sintesis Econom-Financiero");
		posicion.put("campo", "ValoracionPosiBalance");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ValoracionPosiBalance", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "18");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "171");
		posicion.put("sheet", "Sintesis Econom-Financiero");
		posicion.put("campo", "ValoracionRating");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ValoracionRating", posicion);
//		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "36");
		posicion.put("x", "0");
		posicion.put("y", "7");
		posicion.put("sheet", "Analisis Sectorial");
		posicion.put("campo", "EspacioLibreAS");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("EspacioLibreAS", posicion);
		
		Programa programa = (Programa)datos.get("programax");
		int nuevaPosRentabilidad = Integer.parseInt(datos.get("nuevaPosRentabilidad").toString());
		if(programa.getIdmodeloRentabilidad() != null &&
		   programa.getIdmodeloRentabilidad().equals(Constantes.ID_TIPO_BANCAEMPRESA)){
			posicion = new HashMap<String,String>();
			posicion.put("alto", "11");
			posicion.put("largo", "10");
			posicion.put("x", "6");
			posicion.put("y", ""+nuevaPosRentabilidad);
			posicion.put("sheet", "Relaciones Bancarias");
			posicion.put("campo", "RentaModelBEC");
			posicion.put("dirimagen", imgDefault);
			posicion.put("codigEmpresaSEF", codEmpresaDefault);
			posicion.put("indiceSEF", strIndiceDefault);
			posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
			listaImg.put("RentaModelBEC", posicion);
		}else{
			posicion = new HashMap<String,String>();
			posicion.put("alto", "11");
			posicion.put("largo", "10");
			posicion.put("x", "0");
			posicion.put("y", ""+nuevaPosRentabilidad);
			posicion.put("sheet", "Relaciones Bancarias");
			posicion.put("campo", "RentaModelGlobal");
			posicion.put("dirimagen", imgDefault);
			posicion.put("codigEmpresaSEF", codEmpresaDefault);
			posicion.put("indiceSEF", strIndiceDefault);
			posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
			listaImg.put("RentaModelGlobal", posicion);
		}
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "11");
		posicion.put("largo", "7");
		posicion.put("x", "0");
		posicion.put("y", ""+datos.get("nuevaPosComenPoolBanc"));//se obtiene de SheetDefRelacBancariasImpl
		posicion.put("sheet", "Relaciones Bancarias");
		posicion.put("campo", "ComenPoolBanc");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ComenPoolBanc", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "11");
		posicion.put("largo", "13");
		posicion.put("x", "0");
		//posicion.put("y", "28");
		posicion.put("y",""+datos.get("nuevaPosComenLineaRB"));
		posicion.put("sheet", "Relaciones Bancarias");
		posicion.put("campo", "ComenLineas");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ComenLineas", posicion);
//	
		
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "6");
		posicion.put("sheet", "Factores Riesgo");
		posicion.put("campo", "FodaFotalezas");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("FodaFotalezas", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "17");
		posicion.put("sheet", "Factores Riesgo");
		posicion.put("campo", "FodaDebilidades");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("FodaDebilidades", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "28");
		posicion.put("sheet", "Factores Riesgo");
		posicion.put("campo", "FodaOportunidades");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("FodaOportunidades", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "39");
		posicion.put("sheet", "Factores Riesgo");
		posicion.put("campo", "FodaAmenazas");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("FodaAmenazas", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "53");
		posicion.put("sheet", "Factores Riesgo");
		posicion.put("campo", "ConclucionFoda");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ConclucionFoda", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "17");
		posicion.put("x", "0");
		posicion.put("y", "4");
		posicion.put("sheet", "Prop. Riesgo");
		posicion.put("campo", "CampoLibrePR");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("CampoLibrePR", posicion);
//		
		
		//ini MCG20111107
		Programa programatr = (Programa)datos.get("programax");		
		if(programatr.getTipoEstructura() != null &&
		   programatr.getTipoEstructura().equals(Constantes.ID_TIPO_ESTRUCTURA_COMENTARIO)){
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "11");
		posicion.put("x", "0");
		posicion.put("y", "23");
		posicion.put("sheet", "Prop. Riesgo");
		posicion.put("campo", "EstructuraLimite");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("EstructuraLimite", posicion);
		}
		//fin MCG20111107
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", ""+(Integer.parseInt("32")+Integer.parseInt(datos.get("cantPosConsiderPropRies").toString())));
		posicion.put("sheet", "Prop. Riesgo");
		posicion.put("campo", "ConsideracionPR");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("ConsideracionPR", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "44");
		posicion.put("sheet", "Politica Grupo");
		posicion.put("campo", "DetalleOperacionGarantia");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("DetalleOperacionGarantia", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "7");
		posicion.put("x", "0");
		posicion.put("y", "83");
		posicion.put("sheet", "Politica Grupo");
		posicion.put("campo", "RiesgoTesoreria");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("RiesgoTesoreria", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "66");
		posicion.put("sheet", "Politica Grupo");
		posicion.put("campo", "PoliticasRiesGrupo");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("PoliticasRiesGrupo", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "9");
		posicion.put("largo", "10");
		posicion.put("x", "0");
		posicion.put("y", "92");
		posicion.put("sheet", "Politica Grupo");
		posicion.put("campo", "PoliticasDelegacion");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("PoliticasDelegacion", posicion);
		
		posicion = new HashMap<String,String>();
		posicion.put("alto", "11");
		posicion.put("largo", "9");
		posicion.put("x", "0");
		posicion.put("y", "3");
		posicion.put("sheet", "Anexos");
		posicion.put("campo", "CampoLibreAnexos");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("CampoLibreAnexos", posicion);
		//imgPosicion.put("detalle_cifra_negocio_beneficio", value);
		
		///Sintesis Empresa 
		//rowY=11; colX	= 1;
		posicion = new HashMap<String,String>();
		posicion.put("alto", "8");
		posicion.put("largo", "6");
		posicion.put("x", "0");
		posicion.put("y", "15");
		posicion.put("sheet", "Datos Basicos");
		posicion.put("campo", "SintesisEmpresa");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("SintesisEmpresa", posicion);
		
		//Datos Matriz.
		posicion = new HashMap<String,String>();
		posicion.put("alto", "8");
		posicion.put("largo", "6");
		posicion.put("x", "0");
		posicion.put("y", "23");
		posicion.put("sheet", "Datos Basicos");
		posicion.put("campo", "DatosMatriz");
		posicion.put("dirimagen", imgDefault);
		posicion.put("codigEmpresaSEF", codEmpresaDefault);
		posicion.put("indiceSEF", strIndiceDefault);
		posicion.put("tipoSEF", Constantes.TIPO_NORMAL);
		listaImg.put("DatosMatriz", posicion);	
		
		//ini MCG20111104
		//List<Map<String,Object>> lstCodigoEmpresaSEF = new ArrayList<Map<String,Object>>();		
		//lstCodigoEmpresaSEF=(List<Map<String,Object>>)datos.get("listaCodigoEmpresaSEF");
		
		List<FilaEmpresaHoja> lstCodigoEmpresaSEF=new ArrayList<FilaEmpresaHoja>();
		lstCodigoEmpresaSEF=(List<FilaEmpresaHoja>)datos.get("listaWbProg2");
		
		String nombHojaDestino="Sintesis Econom-Financiero (";
    	int indice=2;
		for(FilaEmpresaHoja ofilaEmpresaHoja:lstCodigoEmpresaSEF){
			 //String ocodigoEmpresa=(String)mpCodigoEmpresaSEF.get("codigoEmpresaSEF");
			 String ocodigoEmpresa=ofilaEmpresaHoja.getEmpresa().getCodigo();
			 String strIndexHoja=String.valueOf(ofilaEmpresaHoja.getEmpresa().getIndexHoja());
			 String hojaEF = "";
			 if (!ocodigoEmpresa.equals("00000000")){
				 hojaEF = nombHojaDestino+strIndexHoja+")";
				    String strIndice=strIndexHoja;
					posicion = new HashMap<String,String>();
					posicion.put("alto", "18");
					posicion.put("largo", "20");
					posicion.put("x", "0");
					posicion.put("y", "57");
					posicion.put("sheet", hojaEF);
					posicion.put("campo", "ComenSituFinanciera");
					posicion.put("dirimagen", imgDefault);					
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICO);					
					//listaImg.put("ComenSituFinanciera"+ ocodigoEmpresa, posicion);
					listaImg.put("ComenSituFinanciera"+ strIndice, posicion);
					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "18");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					posicion.put("y", "95");
					posicion.put("sheet", hojaEF);
					posicion.put("campo", "ComenSituEconomica");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICO);
					//listaImg.put("ComenSituEconomica"+ocodigoEmpresa, posicion);
					listaImg.put("ComenSituEconomica"+ strIndice, posicion);
					
					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "18");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					posicion.put("y", "124");
					posicion.put("sheet", hojaEF);
					posicion.put("campo", "ValoracionEconFinanciera");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICO);
					//listaImg.put("ValoracionEconFinanciera"+ocodigoEmpresa, posicion);
					listaImg.put("ValoracionEconFinanciera"+ strIndice, posicion);
					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "18");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					posicion.put("y", "141");
					posicion.put("sheet", hojaEF);
					posicion.put("campo", "ValoracionPosiBalance");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF",  ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICO);
					//listaImg.put("ValoracionPosiBalance"+ocodigoEmpresa, posicion);
					listaImg.put("ValoracionPosiBalance"+ strIndice, posicion);					
					
					
					//ini MCG20130325 RATING
					posicion = new HashMap<String,String>();
					posicion.put("alto", "18");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					posicion.put("y", "171");
					posicion.put("sheet", hojaEF);
					posicion.put("campo", "ValoracionRating");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICORATING);
					listaImg.put("ValoracionRating"+ strIndice, posicion);
					//fin MCG20130325
			 }		 
			 indice++;
	    }		
		//fin MCG20111104
		
		
		
		
		//ini MCG20130226
		
        List<Empresa> olistEmpresa=(List<Empresa>)datos.get("listaEmpresaByPrograma");
        Programa programadb = (Programa)datos.get("programax");	
        List<Empresa> olistEmpresafinal=new ArrayList<Empresa>();
        
        	String tipoEmpres=programadb.getTipoEmpresa().getId().toString();        	
			if(tipoEmpres.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				Empresa oempresa=new Empresa();				
				oempresa.setCodigo(programadb.getIdEmpresa().toString());
				oempresa.setNombre(programadb.getNombreGrupoEmpresa().toString());				
				Tabla otipoGrupo=new Tabla();
				otipoGrupo.setId(Constantes.ID_TIPO_EMPRESA_PRINCIPAL);
				oempresa.setTipoGrupo(otipoGrupo);
				olistEmpresafinal.add(oempresa);
			}else{	
				olistEmpresafinal=olistEmpresa;			
			}
		
		String nombHojaDestinoDB="Datos Basicos (";
		String nombHojaDestinoDB2="Datos Basicos 2 (";
    	int indiceDB=2;
		for(Empresa oEmpresaBD:olistEmpresafinal){
			 String ocodigoEmpresa=oEmpresaBD.getCodigo();
			 
			 String hojaDB = "";
			 String hojaDB2 = "";
			 if (!oEmpresaBD.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
				 String strIndexHoja=String.valueOf(oEmpresaBD.getIndexHoja());
				 indiceDB++;
				 hojaDB = nombHojaDestinoDB+strIndexHoja+")";
				 String strIndice=strIndexHoja;				 
				   int posComenComp = Integer.parseInt(datos.get("nuevaPosComenCompVent_"+ocodigoEmpresa).toString());
				   String strposComenComp=String.valueOf(posComenComp);
				   //""+(Integer.parseInt("32")+Integer.parseInt(datos.get("cantPosConsiderPropRies").toString())));
				   
					///Sintesis Empresa					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "8");
					posicion.put("largo", "6");
					posicion.put("x", "0");
					posicion.put("y", "15");
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "SintesisEmpresa");					
					posicion.put("dirimagen", imgDefault);					
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICODB);					
					//listaImg.put("ComenSituFinanciera"+ ocodigoEmpresa, posicion);
					listaImg.put("SintesisEmpresa"+ strIndice, posicion);
					
					//Datos Matriz.
					posicion = new HashMap<String,String>();
					posicion.put("alto", "8");
					posicion.put("largo", "6");
					posicion.put("x", "0");
					posicion.put("y", "23");
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "DatosMatriz");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF",Constantes.TIPO_ESPECIFICODB);					
					listaImg.put("DatosMatriz"+ strIndice, posicion);
				   
				   
					posicion = new HashMap<String,String>();
					posicion.put("alto", "8");
					posicion.put("largo", "8");
					posicion.put("x", "0");
					posicion.put("y", ""+ strposComenComp);//se obtiene de SheetDefDatosBasicosImpl
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "ComenComprasVentas");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF",  ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICODB);					
					listaImg.put("ComenComprasVentas"+ strIndice, posicion);
					
					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "8");
					posicion.put("largo", "8");
					posicion.put("x", "0");		
					//MIL posicion.put("y", ""+(Integer.parseInt("9")+Integer.parseInt(datos.get("nuevaPosComenCompVent").toString())));		
					posicion.put("y", ""+(Integer.parseInt("6")+posComenComp));		
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "Concentracion");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICODB);					
					listaImg.put("Concentracion"+ strIndice, posicion);		
					
					
					
					//DATOS BASICOS 2
										 
					 hojaDB2 = nombHojaDestinoDB2+strIndexHoja+")";					 				 
					 int posComenComp2 = Integer.parseInt(datos.get("nuevaPosEspacioLibre_"+ocodigoEmpresa).toString());
					 String strposComenComp2=String.valueOf(posComenComp2);
					
					posicion = new HashMap<String,String>();
					posicion.put("alto", "10");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					//posicion.put("y", "16");
					posicion.put("y", ""+strposComenComp2);
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "EspacioLibre");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICODB);
					listaImg.put("EspacioLibre"+ strIndice, posicion);
					
					//ini MCG20130801
					posicion = new HashMap<String,String>();
					posicion.put("alto", "10");
					posicion.put("largo", "10");
					posicion.put("x", "0");
					//posicion.put("y", "16");					
					posicion.put("y", ""+(Integer.parseInt("31")+posComenComp2));	
					posicion.put("sheet", hojaDB);
					posicion.put("campo", "Valoracion");
					posicion.put("dirimagen", imgDefault);
					posicion.put("codigEmpresaSEF", ocodigoEmpresa.toString());
					posicion.put("indiceSEF", strIndice);
					posicion.put("tipoSEF", Constantes.TIPO_ESPECIFICODB);
					listaImg.put("Valoracion"+ strIndice, posicion);
					
					//fin MCG20130801
					
					
			 }		 
			
	    }		
		//fin MCG20111104
		
	}
	
	public void inicializar(Map<String, Object> params) {
    	  this.params=params;  	
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public GeneradorReporteExcelDAOImpl getGeneradorReporteExcelDAOImpl() {
		return generadorReporteExcelDAOImpl;
	}

	public void setGeneradorReporteExcelDAOImpl(
			GeneradorReporteExcelDAOImpl generadorReporteExcelDAOImpl) {
		this.generadorReporteExcelDAOImpl = generadorReporteExcelDAOImpl;
	}
	
	/////Utiles
	public void iniciarColumas(){
		try {
			listaColumnas = anexoBO.findColumnas();
			if(listaColumnas.isEmpty()){
				AnexoColumna rating = new AnexoColumna();
				rating.setDescripcion("RATING");
				AnexoColumna fecha = new AnexoColumna();
				fecha.setDescripcion("FECHA");
				AnexoColumna lteAutorizado = new AnexoColumna();
				lteAutorizado.setDescripcion("LTE AUTORIZADO");
				AnexoColumna lteForm = new AnexoColumna();
				lteForm.setDescripcion("LTE FORM");
				AnexoColumna rgoActual = new AnexoColumna();
				rgoActual.setDescripcion("RGO ACTUAL");
				AnexoColumna rgoPropBBVABC = new AnexoColumna();
				rgoPropBBVABC.setDescripcion("RGO PROP BBVA BC");
				AnexoColumna observaciones = new AnexoColumna();
				observaciones.setDescripcion("OBSERVACIONES");
				listaColumnas.add(rating);
				listaColumnas.add(fecha);
				listaColumnas.add(lteAutorizado);
				listaColumnas.add(lteForm);
				listaColumnas.add(rgoActual);
				listaColumnas.add(rgoPropBBVABC);
				listaColumnas.add(observaciones);
			}
			
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	/**
	 * Se realiza un calculo por empresa, banco y totales por bancos.
	 * @param lista
	 */
	public void calcularMontos(List<FilaAnexo> lista){
		if(lista != null &&
		   !lista.isEmpty()){
			anexoBO.calcularMontosPorEmpresa(lista);
			anexoBO.calcularMontoPorBanco(lista);
			filaTotal = anexoBO.calcularMontoTotal(lista);
		}
	}

	public ProgramaBlobBO getProgramaBlobBO() {
		return programaBlobBO;
	}

	public void setProgramaBlobBO(ProgramaBlobBO programaBlobBO) {
		this.programaBlobBO = programaBlobBO;
	}

	public RatingBO getRatingBO() {
		return ratingBO;
	}

	public void setRatingBO(RatingBO ratingBO) {
		this.ratingBO = ratingBO;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}


	
	
}
