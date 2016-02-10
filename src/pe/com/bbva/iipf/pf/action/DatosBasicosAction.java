package pe.com.bbva.iipf.pf.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.cpy.bo.PFSunatBO;
import pe.com.bbva.iipf.cpy.model.PFSunat;
import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.SelectItem;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBlobBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.CabTabla;
import pe.com.bbva.iipf.pf.model.CapitalizacionBursatil;
import pe.com.bbva.iipf.pf.model.CompraVenta;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.NegocioBeneficio;
import pe.com.bbva.iipf.pf.model.Participaciones;
import pe.com.bbva.iipf.pf.model.Planilla;
import pe.com.bbva.iipf.pf.model.PrincipalesEjecutivos;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.RatingExterno;
import pe.com.bbva.iipf.util.ComboUtil;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("datosBasicosAction")
@Scope("prototype")
public class DatosBasicosAction extends GenericAction {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private PFSunatBO sunatBO;
	
	@Resource
	private DatosBasicosBO datosBasicosBO ;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private DatosBasicosBlobBO  datosBasicosBlobBO;
	
	@Resource
	private EmpresaBO empresaBO ;
	
	@Resource
	private TablaBO tablaBO;
	
	private Programa programa;
	
	private String actividadPrincipal;
	private String sintesisEmpresa;
	private String datosMatriz;
	private String comentAccionariado;
	private String comentPartiSignificativa;
	private String comentRatinExterno;
	private String espacioLibre;
	private String comentvaloraGlobal;
	
	private String pais;
	
	private List<Planilla> listaPlanilla = new ArrayList<Planilla>();
	private List<Accionista> listaAccionistas = new ArrayList<Accionista>();
	private Accionista accionista = new Accionista();
	private Integer posiAccinista;
	private String totalAccionista;
	private String chksAccionistas;
	
	private List<CapitalizacionBursatil> listaCapitalizacion = new ArrayList<CapitalizacionBursatil>();
	private CapitalizacionBursatil capitalizacion = new CapitalizacionBursatil();
	private Integer posiCapitalizacion;	
	private String chksCapitalizacion;
	
	private List<SelectItem> itemDivisaDB;
	private List<Tabla> listaDivisaDB = new ArrayList<Tabla>();
	
	private List<SelectItem> itemTipoDocumento;
	private List<Tabla> listaTipoDocumento = new ArrayList<Tabla>();

	
	private Planilla totalPlanilla = new Planilla();

	private Planilla planillaAdmin = new Planilla();
	
	private Planilla planillaNoAdmin = new Planilla();

	private Planilla plAdministrativo;
	private Planilla plNoAdministrativo;
	private String metodo;
	
	private List<PrincipalesEjecutivos> listaPrinciEjecutivos = new ArrayList<PrincipalesEjecutivos>();
	private String chkEjecutivos;
	
	private List<Participaciones> listaParticipaciones = new ArrayList<Participaciones>();
	private String chkParticipaciones ;
	
	private List<RatingExterno> listaRatingExterno = new ArrayList<RatingExterno>();
	private String chkRatingExterno ;
	
	private CompraVenta totalImportaciones = new CompraVenta();
	private CompraVenta importacionesME = new CompraVenta();
	private CompraVenta totalExportaciones = new CompraVenta();
	private CompraVenta exportacionesME = new CompraVenta();
	
	private List<CompraVenta> listaCompraVenta = new ArrayList<CompraVenta>();
	private List<NegocioBeneficio> listaNBActividades = new ArrayList<NegocioBeneficio>();
	private String chkNBActividades;	
	private List<NegocioBeneficio> listaNBNegocio = new ArrayList<NegocioBeneficio>();
	private String chkNBNegocio;
	
	private NegocioBeneficio totalActividad = new NegocioBeneficio();
	private NegocioBeneficio totalNegocio = new NegocioBeneficio();
	
	//ini MCG20111024
	private List<CabTabla> listaCabTabla = new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaPlanilla = new ArrayList<CabTabla>();
	private List<CabTabla> listaCabTablaCompra = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaVenta = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaActividad = new ArrayList<CabTabla>();	
	private List<CabTabla> listaCabTablaNegocio = new ArrayList<CabTabla>();	
	//fin MCG20111024	

	/*
	 * flags para mantener los div abiertos
	 */
	private boolean flagPlanilla;
	private boolean flagSintesisEmpresa;
	private boolean flagDatosMatriz;
	private boolean flagAccionariado;
	private boolean flagCapitalizacion;	
	private boolean flagPrincipalesEjecutivos;
	private boolean flagParticipacionesSignificativas;
	private boolean flagRatingExterno;
	private boolean flagComprasVentas;
	private boolean flagNegocioBeneficio;
	private boolean flagValoracionGlobal;
	
	
	List<Empresa> listaEmpresasGrupoDB = new ArrayList<Empresa>();
	private String codigoEmpresa; //para el listado de las empresas del grupo(Combo).
	private String codEmpresa; //para obtener el codigo de la empresa para los editores.
	private String flagChangeEmpresa;//para saber si es un summit del change del combo.
    private String codempresagrupo;//para obtener el codigo empresa por el summit del change del combo.
    
	/**
	 * variable para pasar el nombre del campo blob a registrarse
	 */
	private String campoBlob;
	/**
	 * variable que contendra el valor de campo blob
	 */
	private String valorBlob;
	
	private String idtipoDocumento;
	private String numeroDocumento;
	
	private String btnPulsadoGrilla;

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String init(){
		try {
			String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			setPrograma(programaBO.findById(Long.valueOf(programaId)));
			datosBasicosBO.setPrograma(getPrograma());
			String tipoempresa=getPrograma().getTipoEmpresa().getId().toString();
			String codEmpresaGrupo="";
			String flagchange=getFlagChangeEmpresa();
			setBtnPulsadoGrilla("BB");
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
					codEmpresaGrupo=getPrograma().getIdEmpresa().toString();					
			}else{									
					if (flagchange!=null && flagchange.equals("C")){
						totalPlanilla = new Planilla();
						planillaAdmin = new Planilla();			
						planillaNoAdmin = new Planilla();
						
						totalImportaciones = new CompraVenta();
						importacionesME = new CompraVenta();
						totalExportaciones = new CompraVenta();
						exportacionesME = new CompraVenta();
						
						listaCompraVenta = new ArrayList<CompraVenta>();
						listaNBActividades = new ArrayList<NegocioBeneficio>();					
						listaNBNegocio = new ArrayList<NegocioBeneficio>();
						
						totalActividad = new NegocioBeneficio();
						totalNegocio = new NegocioBeneficio();	
						
						codEmpresaGrupo=getCodempresagrupo();
									
					}else{
						codEmpresaGrupo=getPrograma().getIdEmpresa().toString(); //getPrograma().getIdGrupo().toString();
					}
					Empresa empresaprin= new Empresa();	
					empresaprin=programaBO.findEmpresaByIdEmpresaPrograma(getPrograma().getId(),codEmpresaGrupo);
					
					if (empresaprin!=null && !empresaprin.getTipoGrupo().getId().equals(Constantes.ID_TIPO_EMPRESA_PRINCIPAL)){
							
						   List<DatosBasico> listDatosBasico=datosBasicosBO.getListaDatosBasico(codEmpresaGrupo);
						   if (listDatosBasico!=null && listDatosBasico.size()>0 ){
							   DatosBasico datosbas =listDatosBasico.get(0);
							    getPrograma().setActividadPrincipal(datosbas.getActividadPrincipal());
								getPrograma().setPais(datosbas.getPais());
								getPrograma().setAntiguedadNegocio(datosbas.getAntiguedadNegocio());
								getPrograma().setAntiguedadCliente(datosbas.getAntiguedadCliente());
								getPrograma().setGrupoRiesgoBuro(datosbas.getGrupoRiesgoBuro());
								
								getPrograma().setComentAccionariado(datosbas.getComentAccionariado());
								getPrograma().setComentPartiSignificativa(datosbas.getComentPartiSignificativa());
								getPrograma().setComentRatinExterno(datosbas.getComentRatinExterno());
								getPrograma().setComentvaloraGlobal(datosbas.getComentvaloraGlobal());	
						   }else{						   
								getPrograma().setActividadPrincipal("");
								getPrograma().setPais("");
								getPrograma().setAntiguedadNegocio(null);
								getPrograma().setAntiguedadCliente(null);
								getPrograma().setGrupoRiesgoBuro("");
								
								getPrograma().setComentAccionariado("");
								getPrograma().setComentPartiSignificativa("");
								getPrograma().setComentRatinExterno("");
								getPrograma().setComentvaloraGlobal("");						
						 }					
					}						
					
			}				
			
			for(Planilla pl : datosBasicosBO.getListaPlanilla(codEmpresaGrupo)){
				if(pl.getTipoPersonal()!= null && 
				   pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_ADMINISTRATIVO)){
					setPlanillaAdmin(pl);
				}else if(pl.getTipoPersonal()!= null && 
						pl.getTipoPersonal().getId().equals(Constantes.ID_PLANILLA_NO_ADMINISTRATIVO)){
					setPlanillaNoAdmin(pl);
				}else{
					setTotalPlanilla(pl);
				}
			}
			
			//ini MCG20111024
			LoadCabeceraPlanilla(Long.valueOf(programaId));		
			LoadCabeceraCompra(Long.valueOf(programaId));	
			LoadCabeceraVenta(Long.valueOf(programaId));
			LoadCabeceraCuadraNBActividad(Long.valueOf(programaId));
			LoadCabeceraCuadraNBNegocio(Long.valueOf(programaId));
			
			//fin MCG20111024
			
			//ini MCG20130214
			listaAccionistas = datosBasicosBO.getListaAccionistas(codEmpresaGrupo);
			//fin MCG20130214
			float totalacciones = 0;
			for(Accionista acci : listaAccionistas){
				totalacciones += Float.parseFloat(acci.getPorcentaje());
			}
			totalAccionista = ""+FormatUtil.round(totalacciones, 2);
			
			listaPrinciEjecutivos = datosBasicosBO.getListaPrinciEjecutivos(codEmpresaGrupo);
			listaParticipaciones = datosBasicosBO.getListaParticipaciones(codEmpresaGrupo);
			listaRatingExterno = datosBasicosBO.getListaRatingExterno(codEmpresaGrupo);
			listaCompraVenta = datosBasicosBO.getListaCompraVenta(codEmpresaGrupo);
			for(CompraVenta compraVenta :  listaCompraVenta){
				if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.IMPORTACIONES))){
					if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
						importacionesME = compraVenta;
					}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
						totalImportaciones = compraVenta;
					}
				}else if(compraVenta.getTipo().equals(Integer.valueOf(Constantes.EXPORTACIONES))){
					if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME))){
						exportacionesME = compraVenta;
					}else if(compraVenta.getTipoTotal().equals(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE))){
						totalExportaciones = compraVenta;
					}
				}
			}
			listaNBActividades = datosBasicosBO.getListaNBActividades(codEmpresaGrupo);
			float ta1 = 0;
			float ta2 = 0;
			float ta3 = 0;
			float ta4 = 0;
			float ta5 = 0;
			float ta6 = 0;
			for(NegocioBeneficio nb : listaNBActividades){
				ta1 +=nb.getTotalB1()==null?0:Float.valueOf(nb.getTotalB1().replace(",", ""));
				ta2 +=nb.getTotalB2()==null?0:Float.valueOf(nb.getTotalB2().replace(",", ""));
				ta3 +=nb.getTotalB3()==null?0:Float.valueOf(nb.getTotalB3().replace(",", ""));
				ta4 +=nb.getTotalI1()==null?0:Float.valueOf(nb.getTotalI1().replace(",", ""));
				ta5 +=nb.getTotalI2()==null?0:Float.valueOf(nb.getTotalI2().replace(",", ""));
				ta6 +=nb.getTotalI3()==null?0:Float.valueOf(nb.getTotalI3().replace(",", ""));
			}
			totalActividad.setTotalB1(""+FormatUtil.round(ta1,2));
			totalActividad.setTotalB2(""+FormatUtil.round(ta2,2));
			totalActividad.setTotalB3(""+FormatUtil.round(ta3,2));
			totalActividad.setTotalI1(""+FormatUtil.round(ta4,2));
			totalActividad.setTotalI2(""+FormatUtil.round(ta5,2));
			totalActividad.setTotalI3(""+FormatUtil.round(ta6,2));
			listaNBNegocio = datosBasicosBO.getListaNBNegocio(codEmpresaGrupo);
			ta1 = 0;
			ta2 = 0;
			ta3 = 0;
			ta4 = 0;
			ta5 = 0;
			ta6 = 0;
			for(NegocioBeneficio nb : listaNBNegocio){
				ta1 +=Float.valueOf(nb.getTotalB1()==null?"0":nb.getTotalB1().replace(",", ""));
				ta2 +=Float.valueOf(nb.getTotalB2()==null?"0":nb.getTotalB2().replace(",", ""));
				ta3 +=Float.valueOf(nb.getTotalB3()==null?"0":nb.getTotalB3().replace(",", ""));
				ta4 +=Float.valueOf(nb.getTotalI1()==null?"0":nb.getTotalI1().replace(",", ""));
				ta5 +=Float.valueOf(nb.getTotalI2()==null?"0":nb.getTotalI2().replace(",", ""));
				ta6 +=Float.valueOf(nb.getTotalI3()==null?"0":nb.getTotalI3().replace(",", ""));
			}
			totalNegocio.setTotalB1(""+FormatUtil.round(ta1,2));
			totalNegocio.setTotalB2(""+FormatUtil.round(ta2,2));
			totalNegocio.setTotalB3(""+FormatUtil.round(ta3,2));
			totalNegocio.setTotalI1(""+FormatUtil.round(ta4,2));
			totalNegocio.setTotalI2(""+FormatUtil.round(ta5,2));
			totalNegocio.setTotalI3(""+FormatUtil.round(ta6,2));
			
			//ini MCG20130725
			listaCapitalizacion = datosBasicosBO.getListaCapitalizacionBursatil(codEmpresaGrupo);
			listaDivisaDB= tablaBO.listarHijos(Constantes.ID_TABLA_DIVISADB);
			itemDivisaDB= ComboUtil.getSelectItems(listaDivisaDB, 
					"id",
					"descripcion",
					Constantes.VAL_DEFAULT_SELECTION);
			
			listaTipoDocumento= tablaBO.listarHijos(Constantes.ID_TABLA_TIPODOCUMENTO);			
			itemTipoDocumento= ComboUtil.getSelectItemsConSortCodigo2(listaTipoDocumento, 
			"id",
			"descripcion",
			"codigo",
			Constantes.VAL_DEFAULT_SELECTION);
			
			
			cargarSessionCombos();
			//fin MCG20130725
			
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}	catch(Exception e){				
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	//INI MCG20130212
	
	/**
	 * Se agrega el grupo a la lista de empresas que pertencen al grupo.
	 * 
	 */
	public void completarListaGrupoEmpresas(){
		String tipoEmpresa = getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
		if(!tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
			List<Empresa> lista = (List<Empresa>)getObjectSession(Constantes.LISTA_GRUPO_EMPRESAS_SESSION);
			//Empresa grupo = new Empresa();
			//grupo.setCodigo(getObjectSession(Constantes.COD_GRUPO_SESSION).toString());
			//grupo.setNombre(getObjectSession(Constantes.NOMBRE_EMPRESA_GRUPO_SESSION).toString());
			//listaEmpresasGrupoDB.add(grupo);
			for(Empresa emp :lista ){
				listaEmpresasGrupoDB.add(emp);
			}
		}
		
		try {
			ObtenerSessionCombos();
		} catch (Exception e) {
			 
		}
		
	}
	//FIN MCG20130212
	public void LoadCabeceraPlanilla(Long idprograma){			
		
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_PLANILLA);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				listaCabTablaPlanilla=listaCabTablatmp;				
			}else{
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<4;i++) {
						intposicion=intposicion+1;
					    String stranio="12/" + String.valueOf(anio-(3-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_PLANILLA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaPlanilla=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void LoadCabeceraCompra(Long idprograma){			
		
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_COMPRA);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				listaCabTablaCompra=listaCabTablatmp;				
			}else{
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_COMPRA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaCompra=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}	
	
	public void LoadCabeceraVenta(Long idprograma){			
		
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_VENTA);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				Collections.sort(listaCabTablatmp);
				listaCabTablaVenta=listaCabTablatmp;				
			}else{
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				int intposicion=0;
				for (int i=0;i<3;i++) {
						intposicion=intposicion+1;
					    String stranio="Dic-" + String.valueOf(anio-(2-i));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_VENTA);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);					
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaVenta=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}		
	}
	
	
	public void LoadCabeceraCuadraNBActividad(Long idprograma){			
		
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_ACTIVIDAD);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				int contcb=0;
				for (CabTabla cabtab: listaCabTablatmp ){
					if (cabtab.getPosicion()==0){
						contcb=contcb+1;
					}					
				}
				if (contcb==0){					
					CabTabla ocabTabla0=new CabTabla();
					Tabla otipoTabla0=new Tabla();
					otipoTabla0.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);
					ocabTabla0.setId(null);
					ocabTabla0.setTipoTabla(otipoTabla0);
					ocabTabla0.setPrograma(getPrograma());
					ocabTabla0.setCabeceraTabla("Por Línea de Actividad");
					ocabTabla0.setPosicion(0);						
					listaCabTablatmp.add(ocabTabla0);
				}
				
				Collections.sort(listaCabTablatmp);
				listaCabTablaActividad=listaCabTablatmp;				
			}else{
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				CabTabla ocabTabla1=new CabTabla();
				Tabla otipoTabla1=new Tabla();
				otipoTabla1.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);
				ocabTabla1.setId(null);
				ocabTabla1.setTipoTabla(otipoTabla1);
				ocabTabla1.setPrograma(getPrograma());
				ocabTabla1.setCabeceraTabla("Por Línea de Actividad");
				ocabTabla1.setPosicion(0);						
				listaCabTablaini.add(ocabTabla1);
				
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_ACTIVIDAD);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}	
				
				
		
				Collections.sort(listaCabTablaini);
				listaCabTablaActividad=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	public void LoadCabeceraCuadraNBNegocio(Long idprograma){			
		
		List<CabTabla> listaCabTablatmp= new ArrayList<CabTabla>();
		List<CabTabla> listaCabTablaini= new ArrayList<CabTabla>();	
		try {					
		
			listaCabTablatmp=datosBasicosBO.getListaCabTablaByTipoTabla( idprograma,Constantes.ID_TIPO_TABLA_NEGOCIO);
			
			if (listaCabTablatmp != null && listaCabTablatmp.size() > 0) {	
				int contcb=0;
				for (CabTabla cabtab: listaCabTablatmp ){
					if (cabtab.getPosicion()==0){
						contcb=contcb+1;
					}					
				}
				if (contcb==0) {
					CabTabla ocabTabla0=new CabTabla();
					Tabla otipoTabla0=new Tabla();
					otipoTabla0.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);
					ocabTabla0.setId(null);
					ocabTabla0.setTipoTabla(otipoTabla0);
					ocabTabla0.setPrograma(getPrograma());
					ocabTabla0.setCabeceraTabla("Por Línea de Negocio");
					ocabTabla0.setPosicion(0);						
					listaCabTablatmp.add(ocabTabla0);					
				}				
				Collections.sort(listaCabTablatmp);
				listaCabTablaNegocio=listaCabTablatmp;				
			}else{
				
				int anio = Integer.parseInt(getObjectSession(Constantes.ANIO_PROGRAMA_SESSION).toString());
				
				CabTabla ocabTabla1=new CabTabla();
				Tabla otipoTabla1=new Tabla();
				otipoTabla1.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);
				ocabTabla1.setId(null);
				ocabTabla1.setTipoTabla(otipoTabla1);
				ocabTabla1.setPrograma(getPrograma());
				ocabTabla1.setCabeceraTabla("Por Línea de Negocio");
				ocabTabla1.setPosicion(0);						
				listaCabTablaini.add(ocabTabla1);
				
				int intposicion=0;
				int intcontaux=0;
				for (int i=0;i<6;i++) {
						intposicion=intposicion+1;						
						if (intcontaux==3){
							intcontaux=0;
						}
					    String stranio="" + String.valueOf(anio-(2-intcontaux));					    
						CabTabla ocabTabla=new CabTabla();
						Tabla otipoTabla=new Tabla();
						otipoTabla.setId(Constantes.ID_TIPO_TABLA_NEGOCIO);
						ocabTabla.setId(null);
						ocabTabla.setTipoTabla(otipoTabla);
						ocabTabla.setPrograma(getPrograma());
						ocabTabla.setCabeceraTabla(stranio);
						ocabTabla.setPosicion(intposicion);						
						listaCabTablaini.add(ocabTabla);
						intcontaux=intcontaux+1;
				}				
				Collections.sort(listaCabTablaini);
				listaCabTablaNegocio=listaCabTablaini;				
			}	
			
		} catch (BOException e) {
		logger.error(StringUtil.getStackTrace(e));
		}
		
	}	
	
	/**
	 * Buscar las actividades economicas
	 */
	
	public void buscarActividadesEconomicas(){
		
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String ruc = sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
		String programaId = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
		String codigoGrupo = sessionparam.get(Constantes.COD_GRUPO_SESSION)==null?"":sessionparam.get(Constantes.COD_GRUPO_SESSION).toString();
		String actividadesEconomicas = "";
		try {
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			//buscar la actividad de la empresa
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){
				PFSunat sunat = sunatBO.findByRUC(ruc);	
				if(sunat!=null){
					actividadesEconomicas = sunat.getDet_activiad();
				}else{
					addActionMessage("No se encontró la actividad económica de la empresa");
				}
			}else{
				List lista = sunatBO.getEmpresasDelGrupo(codigoGrupo,
													     		  Long.valueOf(programaId));
				String html = "<table border=\"0\"><tr><td>";
				boolean primero = true;
				for(Object pfsunat : lista){
					if(primero){
						html= html+"<input type=\"checkbox\" name=\"chkactividades\" value=\""+((Object[])pfsunat)[3]+"\" />"+((Object[])pfsunat)[3]+"</td></tr>";
						primero = false;
					}else{
						html= html+"<tr><td><input type=\"checkbox\" name=\"chkactividades\" value=\""+((Object[])pfsunat)[3]+"\" />"+((Object[])pfsunat)[3]+"</td></tr>";
					}
				}
				html = html+"<table>";
				actividadesEconomicas = html;
				if(lista != null && lista.size()==0){
					actividadesEconomicas = "<b>No se econtraron resultados</b>";
				}
			}
			
			getResponse().setContentType("text/html");   
	        PrintWriter out = getResponse().getWriter(); 
	        out.print(actividadesEconomicas);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	/**
	 * Registra todo elformulario pero no icluye el registro de los campos blob
	 * solo las listas y los campos del programa finaciero.
	 * @return
	 */
	public String updateDatosBasicos(){
		System.out.println("llego por aca");
		Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
		String idTipoPrograma = getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString();
		Tabla tipoPrograma = new Tabla();
		tipoPrograma.setId(Long.valueOf(idTipoPrograma));
		//programa = new Programa();
		programa.setId(Long.valueOf(idprograma));
		programa.setTipoPrograma(tipoPrograma);
		String codEmpresaGrupo="";
		setBtnPulsadoGrilla("BB");
		try {
			String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
			Tabla ttipoEmpresa = new Tabla();
			ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
			programa.setTipoEmpresa(ttipoEmpresa);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
				
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}								
			
			listaPlanilla.add(planillaAdmin);
			listaPlanilla.add(planillaNoAdmin);
			listaPlanilla.add(totalPlanilla);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.setPlanillaAdmin(planillaAdmin);
			datosBasicosBO.setPlanillaNoAdmin(planillaNoAdmin);
			datosBasicosBO.setTotalPlanilla(totalPlanilla);
			datosBasicosBO.setSintesisEmpresa(sintesisEmpresa);
			datosBasicosBO.setListaAccionistas(listaAccionistas);
			datosBasicosBO.setListaCapitalizacion(listaCapitalizacion);
			//ini MCG20111025			
			datosBasicosBO.setListaCabTablaPlanilla(listaCabTablaPlanilla);
			datosBasicosBO.setListaCabTablaCompra(listaCabTablaCompra);
			datosBasicosBO.setListaCabTablaVenta(listaCabTablaVenta);
			datosBasicosBO.setListaCabTablaActividad(listaCabTablaActividad);
			datosBasicosBO.setListaCabTablaNegocio(listaCabTablaNegocio);			
			//fin MCG20111025
			
			
			datosBasicosBO.setListaPrinciEjecutivos(listaPrinciEjecutivos);
			datosBasicosBO.setListaRatingExterno(listaRatingExterno);
			//completando datos de negocio beneficio
			datosBasicosBO.setListaNBActividades(listaNBActividades);
			datosBasicosBO.setListaNBNegocio(listaNBNegocio);
			
			datosBasicosBO.setListaParticipaciones(listaParticipaciones);
			//completando datos de compra y venta
			totalImportaciones.setTipo(Integer.valueOf(Constantes.IMPORTACIONES));
			totalImportaciones.setTipoTotal(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE));
			importacionesME.setTipo(Integer.valueOf(Constantes.IMPORTACIONES));
			importacionesME.setTipoTotal(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME));
			totalExportaciones.setTipo(Integer.valueOf(Constantes.EXPORTACIONES));
			totalExportaciones.setTipoTotal(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_PORCENTAJE));
			exportacionesME.setTipo(Integer.valueOf(Constantes.EXPORTACIONES));
			exportacionesME.setTipoTotal(Integer.valueOf(Constantes.COMPRA_VENTA_TOTAL_ME));
			listaCompraVenta.add(totalImportaciones);
			listaCompraVenta.add(importacionesME);
			listaCompraVenta.add(totalExportaciones);
			listaCompraVenta.add(exportacionesME);
			datosBasicosBO.setListaCompraVenta(listaCompraVenta);
			datosBasicosBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			datosBasicosBO.updateDatosBasicos(codEmpresaGrupo);
			setPrograma(programa);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Datos Actualizados Correctamente");
		
		} catch (BOException e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			completarListaGrupoEmpresas();
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	
	//ini MCG20130405
	public String refreshDatosBasicos(){
			
		try {
			Programa oprograma=new Programa();			
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
			String idprograma = sessionparam.get(Constantes.ID_PROGRAMA_SESSION).toString();
			String idTipoPrograma = getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString();
			Tabla tipoPrograma = new Tabla();
			tipoPrograma.setId(Long.valueOf(idTipoPrograma));
			//programa = new Programa();
			programa.setId(Long.valueOf(idprograma));
			programa.setTipoPrograma(tipoPrograma);
			String codEmpresaGrupo="";
			String strruc="";
				String tipoEmpresa = sessionparam.get(Constantes.COD_TIPO_EMPRESA_SESSION).toString();
				Tabla ttipoEmpresa = new Tabla();
				ttipoEmpresa.setId(Long.valueOf(tipoEmpresa));
				programa.setTipoEmpresa(ttipoEmpresa);			
				if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
					codEmpresaGrupo=sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_CENTRAL_EMPRESA_SESSION).toString();
					strruc=sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION)==null?"":sessionparam.get(Constantes.COD_RUC_EMPRESA_SESSION).toString();
					programa.setIdEmpresa(codEmpresaGrupo);
					programa.setRuc(strruc);
					
				}else{
					codEmpresaGrupo= getCodigoEmpresa();
					
				}								
			
			List<Empresa> listaGrupoEmpresas =  empresaBO.listarEmpresasPorPrograma(this.programa.getId());
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			datosBasicosBO.setPathWebService(pathWebService);
			datosBasicosBO.setConfiguracionWS(getConfiguracionWS());
			datosBasicosBO.setListaGrupoEmpresas(listaGrupoEmpresas);
			datosBasicosBO.refreshDatosBasicos(codEmpresaGrupo);
			setPrograma(programa);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Datos de Host Actualizados Correctamente");
		} catch (BOException e) {
			completarListaGrupoEmpresas();
			logger.error(StringUtil.getStackTrace(e));
			addActionError("No se puede conectar a HOST. Error:" + e.getMessage());
		}
		return "datosBasicos";
	}
	//fin MCG20130405
	
	///////////////accionariado///////////////
	
	public String addAccionista(){
		//Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		//listaAccionistas = (List<Accionista>) sessionparam.get("listaAccionistas");
		setBtnPulsadoGrilla("AA");
		if(listaAccionistas==null){
			listaAccionistas = new ArrayList<Accionista>();
		}
		metodo = "";
		accionista = new Accionista();
		accionista.setPorcentaje("0");		
		if(getChksAccionistas()!=null){
			 String [] idsEliminar = getChksAccionistas().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaAccionistas.add(posicion, accionista);
			 }
		}else{
			 listaAccionistas.add(accionista);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//sessionparam.put("listaAccionistas", listaAccionistas);
		return "datosBasicos";
	}
	
	public String deleteAccionista(){
		metodo = "";
		setBtnPulsadoGrilla("AA");
		if(getChksAccionistas()!=null){
		 String [] idsEliminar = getChksAccionistas().split(",");
		 if(idsEliminar!=null){
			 for(int index = idsEliminar.length-1; index>=0; index--){
				 listaAccionistas.remove(Integer.parseInt(idsEliminar[index].trim()));
			 }
		 }
		}
		//listaAccionistas.remove(listaAccionistas.size()-1);
		//Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();
		//sessionparam.put("listaAccionistas", listaAccionistas);
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String saveAllAccionistas(){
		metodo = "";
		String codEmpresaGrupo="";
		try {			
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaAccionistas(listaAccionistas);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDAccionista(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Accionistas registrados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	public String editAccionista(){
		metodo = "Editar";
		return "datosBasicos";
	}
	
	//ini MCG20130725
	public String addCapitalizacion(){
		setBtnPulsadoGrilla("AA");
		if(listaCapitalizacion==null){
			listaCapitalizacion = new ArrayList<CapitalizacionBursatil>();
		}
		metodo = "";
		capitalizacion = new CapitalizacionBursatil();
		capitalizacion.setFondosPropios("0");
		if(getChksCapitalizacion()!=null){
			 String [] idsEliminar = getChksCapitalizacion().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaCapitalizacion.add(posicion, capitalizacion);
			 }
		}else{
			listaCapitalizacion.add(capitalizacion);
		}
		
		completarListaGrupoEmpresas();
		return "datosBasicos";
	}
	
	public String deleteCapitalizacion(){
		metodo = "";
		setBtnPulsadoGrilla("AA");
		if(getChksCapitalizacion()!=null){
		 String [] idsEliminar = getChksCapitalizacion().split(",");
		 if(idsEliminar!=null){
			 for(int index = idsEliminar.length-1; index>=0; index--){
				 listaCapitalizacion.remove(Integer.parseInt(idsEliminar[index].trim()));
			 }
		 }
		}
		completarListaGrupoEmpresas();
		
		return "datosBasicos";
	}
	
	public String saveAllCapitalizacion(){
		metodo = "";
		String codEmpresaGrupo="";
		try {			
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaCapitalizacion(listaCapitalizacion);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDCapitalizacion(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Capitalizacion Bursatil registrados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	//fin MCG20130725
	
	/////////////////////////////////////////
	
	
	//////////////////Principales Ejecutivos///////////////////////
	public String addEjecutivo(){
		setBtnPulsadoGrilla("AA");
		if(listaPrinciEjecutivos==null){
			listaPrinciEjecutivos = new ArrayList<PrincipalesEjecutivos>();
		}
		metodo = "";
		PrincipalesEjecutivos principalEjec = new PrincipalesEjecutivos();
		if(getChkEjecutivos()!=null){
			 String [] idsEliminar = getChkEjecutivos().split(",");
			 if(idsEliminar != null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaPrinciEjecutivos.add(posicion, principalEjec);
			 }
		}else{
			listaPrinciEjecutivos.add(principalEjec);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String deleteEjecutivo(){
		setBtnPulsadoGrilla("AA");
		if(getChkEjecutivos()!=null){
			 String [] idsEliminar = getChkEjecutivos().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaPrinciEjecutivos.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
			}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//listaPrinciEjecutivos.remove(listaPrinciEjecutivos.size()-1);
		return "datosBasicos";
	}
	
	public String saveAllEjecutivo(){
		String codEmpresaGrupo="";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//Local o corporativo
			String idTipoPrograma = getObjectSession(Constantes.ID_TIPO_PROGRAMA_SESSION).toString();
			Tabla tipoPrograma = new Tabla();
			tipoPrograma.setId(Long.valueOf(idTipoPrograma));
			//programa = new Programa();
			
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			
			programa.setId(Long.valueOf(idprograma));
			programa.setTipoPrograma(tipoPrograma);
			datosBasicosBO.setListaPrinciEjecutivos(listaPrinciEjecutivos);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDEjecutivos(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Principales Ejecutivos registrados Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	/////////////////////////////////////////
	
	///////////////////PARTICIPACIONES SIGNIFICATIVAS//////////////////////
	public String addParticipaciones(){
		setBtnPulsadoGrilla("AA");
		if(listaParticipaciones==null){
			listaParticipaciones = new ArrayList<Participaciones>();
		}
		Participaciones participaciones = new Participaciones();
		if(getChkParticipaciones()!=null){
			 String [] idsEliminar = getChkParticipaciones().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaParticipaciones.add(posicion, participaciones);
			 }
		}else{
			listaParticipaciones.add(participaciones);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String deleteParticipaciones(){
		setBtnPulsadoGrilla("AA");
		if(getChkParticipaciones()!=null){
			 String [] idsEliminar = getChkParticipaciones().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaParticipaciones.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
			}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//listaParticipaciones.remove(listaParticipaciones.size()-1);
		return "datosBasicos";
	}
	
	public String saveAllParticipaciones(){
		String codEmpresaGrupo="";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}				
			
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaParticipaciones(listaParticipaciones);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDParticipaciones(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Participaciones registradas Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	////////////////////////////////////////
	
	////////////////////DATOS BASICOS///////////////////
	
	public String addRatingExterno(){
		setBtnPulsadoGrilla("AA");
		if(listaRatingExterno==null){
			listaRatingExterno = new ArrayList<RatingExterno>();
		}
		RatingExterno ratingExterno = new RatingExterno();
		if(getChkRatingExterno()!=null){
			 String [] idsEliminar = getChkRatingExterno().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaRatingExterno.add(posicion, ratingExterno);
			 }
		}else{
			listaRatingExterno.add(ratingExterno);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String deleteRatingExterno(){
		setBtnPulsadoGrilla("AA");
		if(getChkRatingExterno()!=null){
			 String [] idsEliminar = getChkRatingExterno().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaRatingExterno.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
			}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//listaRatingExterno.remove(listaRatingExterno.size()-1);
		return "datosBasicos";
	}
	
	public String saveAllRatingExterno(){
		String codEmpresaGrupo="";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaRatingExterno(listaRatingExterno);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDRatingExerno(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Rating externo registrado Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	///////////////////////////////////////
	//////////////////LINEAS ACTIVIDAD////////////////////
	public String addLineaActividad(){
		setBtnPulsadoGrilla("AA");
		if(listaNBActividades==null){
			listaNBActividades = new ArrayList<NegocioBeneficio>();
		}
		NegocioBeneficio negocioBeneficio = new NegocioBeneficio();
		if(getChkNBActividades()!=null){
			 String [] idsEliminar = getChkNBActividades().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaNBActividades.add(posicion, negocioBeneficio);
			 }
		}else{
			listaNBActividades.add(negocioBeneficio);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String deleteLineaActividad(){
		setBtnPulsadoGrilla("AA");
		if(getChkNBActividades()!=null){
			 String [] idsEliminar = getChkNBActividades().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaNBActividades.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//listaNBActividades.remove(listaNBActividades.size()-1);
		return "datosBasicos";
	}
	
	public String saveAllLineaActividad(){
		String codEmpresaGrupo="";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}	
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaNBActividades(listaNBActividades);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDActividades(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Negocio Beneficio registrado Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	//////////////////////////////////////
	//////////////////LINEAS NEGOCIO////////////////////
	public String addLineaNegocio(){
		setBtnPulsadoGrilla("AA");
		if(listaNBNegocio==null){
			listaNBNegocio = new ArrayList<NegocioBeneficio>();
		}
		NegocioBeneficio negocioBeneficio = new NegocioBeneficio();
		if(getChkNBNegocio()!=null){
			 String [] idsEliminar = getChkNBNegocio().split(",");
			 if(idsEliminar!=null){
				 int index = idsEliminar.length-1;
				 int posicion = Integer.parseInt(idsEliminar[index].trim())+1;
				 listaNBNegocio.add(posicion, negocioBeneficio);
			 }
		}else{
			listaNBNegocio.add(negocioBeneficio);
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		return "datosBasicos";
	}
	
	public String deleteLineaNegocio(){
		setBtnPulsadoGrilla("AA");
		if(getChkNBNegocio()!=null){
			 String [] idsEliminar = getChkNBNegocio().split(",");
			 if(idsEliminar!=null){
				 for(int index = idsEliminar.length-1; index>=0; index--){
					 listaNBNegocio.remove(Integer.parseInt(idsEliminar[index].trim()));
				 }
			 }
		}
		//INI MCG20130212
		completarListaGrupoEmpresas();
		//FIN MCG20130212
		//listaNBNegocio.remove(listaNBNegocio.size()-1);
		return "datosBasicos";
	}
	
	public String saveAllLineaNegocio(){
		String codEmpresaGrupo="";
		try {
			String idprograma = (String)getObjectSession(Constantes.ID_PROGRAMA_SESSION);
			//programa = new Programa();
			String tipoEmpresa = (String)getObjectSession(Constantes.COD_TIPO_EMPRESA_SESSION);			
			if(tipoEmpresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){				
				codEmpresaGrupo=(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION)==null?"":(String)getObjectSession(Constantes.COD_CENTRAL_EMPRESA_SESSION);
			}else{
				codEmpresaGrupo= getCodigoEmpresa();				
			}
			programa.setId(Long.valueOf(idprograma));
			datosBasicosBO.setListaNBNegocio(listaNBNegocio);
			datosBasicosBO.setPrograma(programa);
			datosBasicosBO.saveUDNegocio(codEmpresaGrupo);
			//INI MCG20130212
			completarListaGrupoEmpresas();
			//FIN MCG20130212
			addActionMessage("Negocio Beneficio registrado Correctamente");
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "datosBasicos";
	}
	
	//ini MCG20130215
	public void saveDatosBasicoBlob(){
		logger.info("Guardando blog");
		logger.info("campoblob="+campoBlob);
		logger.info("valorBlob="+valorBlob);
		logger.info("valorBlob="+codEmpresa);
		
		try {
			DatosBasicoBlob seb = new DatosBasicoBlob ();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));			
			datosBasicosBlobBO.setPrograma(programa);
			datosBasicosBlobBO.setCampoBlob(campoBlob);
			datosBasicosBlobBO.setValorBlob(valorBlob);
			datosBasicosBlobBO.setCodEmpresa(codEmpresa);
			Object sysCodif = getObjectParamtrosSession(Constantes.COD_SISTEMA_CODIFICACION);
			datosBasicosBlobBO.setSysCodificacion(sysCodif== null?"":sysCodif.toString());
			try {
				Object patrones = getObjectParamtrosSession(Constantes.COD_PATRONES_EDITOR);
				datosBasicosBlobBO.setPatronesEditor(patrones== null?"":patrones.toString());
			} catch (Exception e) {
				 
				datosBasicosBlobBO.setPatronesEditor("");
			}
			datosBasicosBlobBO.setUsuarioSession((UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION));
			datosBasicosBlobBO.save(seb);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * Busca un campo blob dentro del programa
	 * la respuesta es procesada via ajax
	 */
	public void consultarDatosBasicoBlob(){
		try {
			StringBuilder stb = new StringBuilder();
			Programa programa = new Programa();
			String idPrograma = getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString();
			programa.setId(Long.valueOf(idPrograma));				
			DatosBasicoBlob datosBasicoBlob = datosBasicosBlobBO.findDatosBasicoBlobByPrograma(programa,codEmpresa);
			if(datosBasicoBlob !=null){
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	
			  //Sintesis economico	 	        
	 	       
	 	      if(campoBlob.equals("sintesisEmpresa") &&
	 	    		 datosBasicoBlob.getSintesisEmpresa()!=null){
	 		        for(byte x :datosBasicoBlob.getSintesisEmpresa() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("datosMatriz") &&
	 	    		 datosBasicoBlob.getDatosMatriz()!=null){
	 		        for(byte x :datosBasicoBlob.getDatosMatriz() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("espacioLibre") &&
	 	    		 datosBasicoBlob.getEspacioLibre()!=null){
	 		        for(byte x :datosBasicoBlob.getEspacioLibre() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("comenComprasVentas") &&
	 	    		 datosBasicoBlob.getComenComprasVentas()!=null){
	 		        for(byte x :datosBasicoBlob.getComenComprasVentas() ){
	 		          	stb.append((char)FormatUtil.getCharUTF(x));
	 		        }
	 	        }
	 	      if(campoBlob.equals("concentracion") &&
		 	    		 datosBasicoBlob.getConcentracion()!=null){
		 		        for(byte x :datosBasicoBlob.getConcentracion() ){
		 		          	stb.append((char)FormatUtil.getCharUTF(x));
		 		        }
		 	        }
	            if(campoBlob.equals("valoracion") &&
	            		datosBasicoBlob.getValoracion()!=null){
		 		            for(byte x :datosBasicoBlob.getValoracion() ){
		 		            	stb.append((char)FormatUtil.getCharUTF(x));
		 		            }
		 	            }
	 	      
	 	      logger.info("html blob = "+stb);
	            out.print(stb.toString());
			}
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//fin MCG20130215
	
	//ini MCG20140617
	public void obtenerAccionista() throws BOException, IOException{
		getResponse().setContentType("text/html");   
        PrintWriter out = getResponse().getWriter(); 
        
		String sustentoJSON = "";
		Accionista accionista=new Accionista();		
		Accionista oaccionista= null;
		Tabla otipoDocumento=new Tabla();
		String strmensajeAccionista="";
		String strJsonDefaul="{\"ruc\":\""+""+"\",\"codigoCentral\":\""+""+
		"\",\"nombreAccionista\":\""+""+
		"\",\"edadAccionista\":\""+""+
		"\",\"tipoNumeroDocumentoHost\":\""+""+
		"\",\"msnAccionista\":\""+("Ingrese tipo de documento y número de documento")+
		"\"}";
		
		try {
			

		
		if (getIdtipoDocumento()!=null && !getIdtipoDocumento().equals("")){
			 if (getNumeroDocumento()!=null && !getNumeroDocumento().equals("")){
				 	oaccionista= new Accionista();
					otipoDocumento = tablaBO.obtienePorId(Long.valueOf(getIdtipoDocumento()));	
					accionista.setTipoDocumento(otipoDocumento);
					accionista.setNumeroDocumento(getNumeroDocumento());
					
					String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
					UsuarioSesion ousuarioSession=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
					String usuarioHost=ousuarioSession.getRegistroHost();
					
					oaccionista = datosBasicosBO.obtenerAccionista(accionista,usuarioHost);
					String strcontrol=oaccionista.getControl();
					if (!strcontrol.equals("OK")){
						strmensajeAccionista=Constantes.MENS_SIN_INFORMACION_ACCIONISTA +" "+ strcontrol;
					}
				 
			 }			
		}
//		oaccionista= new Accionista();
//		oaccionista.setRuc("10000002922");
//		oaccionista.setCodigoCentral("23423424");
//		oaccionista.setNombre("Milton Cornetero");

		if(oaccionista!=null){
			sustentoJSON = "{\"ruc\":\""+(oaccionista.getRuc()==null?"":oaccionista.getRuc())+
							"\",\"codigoCentral\":\""+(oaccionista.getCodigoCentral()==null?"":oaccionista.getCodigoCentral())+
							"\",\"nombreAccionista\":\""+(oaccionista.getNombre()==null?"":oaccionista.getNombre())+
							"\",\"edadAccionista\":\""+(oaccionista.getEdad()==null?"":oaccionista.getEdad())+
							"\",\"tipoNumeroDocumentoHost\":\""+(oaccionista.getTipoNumeroDocumentoHost()==null?"":oaccionista.getTipoNumeroDocumentoHost())+
							"\",\"msnAccionista\":\""+(strmensajeAccionista)+
							"\"}";
		}else{
			sustentoJSON = strJsonDefaul;
		}
        out.print(sustentoJSON.toString());
		
		} catch (Exception e) {
			out.print(strJsonDefaul.toString());
		}
	}
	
	//fin MCG20140617
	
	public void cargarSessionCombos(){
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();	
			try {
				sessionparam.remove("LISTADIVISADB");
				sessionparam.remove("LISTATIPODOCUMENTO");
			} catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
			}

			sessionparam.put("LISTADIVISADB", itemDivisaDB);
			sessionparam.put("LISTATIPODOCUMENTO", itemTipoDocumento);
			
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	public void ObtenerSessionCombos(){
		try {
			Map<String, Object>  sessionparam =  ActionContext.getContext().getSession();			
			 itemDivisaDB=(List<SelectItem>)sessionparam.get("LISTADIVISADB");	
			 itemTipoDocumento=(List<SelectItem>)sessionparam.get("LISTATIPODOCUMENTO");	
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//////////////////////////////////////
	public String execute() throws Exception {
        //persons.addAll(personManager.getPeople());
        return SUCCESS;
    }
	
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	

	public PFSunatBO getSunatBO() {
		return sunatBO;
	}

	public void setSunatBO(PFSunatBO sunatBO) {
		this.sunatBO = sunatBO;
	}
	
	public String getActividadPrincipal() {
		return actividadPrincipal;
	}

	public void setActividadPrincipal(String actividadPrincipal) {
		this.actividadPrincipal = actividadPrincipal;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public DatosBasicosBO getDatosBasicosBO() {
		return datosBasicosBO;
	}

	public void setDatosBasicosBO(DatosBasicosBO datosBasicosBO) {
		this.datosBasicosBO = datosBasicosBO;
	}

	public Planilla getPlAdministrativo() {
		return plAdministrativo;
	}

	public void setPlAdministrativo(Planilla plAdministrativo) {
		this.plAdministrativo = plAdministrativo;
	}

	public Planilla getPlNoAdministrativo() {
		return plNoAdministrativo;
	}

	public void setPlNoAdministrativo(Planilla plNoAdministrativo) {
		this.plNoAdministrativo = plNoAdministrativo;
	}

	public List<Planilla> getListaPlanilla() {
		return listaPlanilla;
	}

	public void setListaPlanilla(List<Planilla> listaPlanilla) {
		this.listaPlanilla = listaPlanilla;
	}

	public Planilla getTotalPlanilla() {
		return totalPlanilla;
	}

	public void setTotalPlanilla(Planilla totalPlanilla) {
		this.totalPlanilla = totalPlanilla;
	}

	public Planilla getPlanillaAdmin() {
		return planillaAdmin;
	}

	public void setPlanillaAdmin(Planilla planillaAdmin) {
		this.planillaAdmin = planillaAdmin;
	}

	public Planilla getPlanillaNoAdmin() {
		return planillaNoAdmin;
	}

	public void setPlanillaNoAdmin(Planilla planillaNoAdmin) {
		this.planillaNoAdmin = planillaNoAdmin;
	}

	public String getSintesisEmpresa() {
		return sintesisEmpresa;
	}

	public void setSintesisEmpresa(String sintesisEmpresa) {
		this.sintesisEmpresa = sintesisEmpresa;
	}

	public String getDatosMatriz() {
		return datosMatriz;
	}

	public void setDatosMatriz(String datosMatriz) {
		this.datosMatriz = datosMatriz;
	}

	public List<Accionista> getListaAccionistas() {
		return listaAccionistas;
	}

	public void setListaAccionistas(List<Accionista> listaAccionistas) {
		this.listaAccionistas = listaAccionistas;
	}

	public Accionista getAccionista() {
		return accionista;
	}

	public void setAccionista(Accionista accionista) {
		this.accionista = accionista;
	}

	public Integer getPosiAccinista() {
		return posiAccinista;
	}

	public void setPosiAccinista(Integer posiAccinista) {
		this.posiAccinista = posiAccinista;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getTotalAccionista() {
		return totalAccionista;
	}

	public void setTotalAccionista(String totalAccionista) {
		this.totalAccionista = totalAccionista;
	}

	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos() {
		return listaPrinciEjecutivos;
	}

	public void setListaPrinciEjecutivos(
			List<PrincipalesEjecutivos> listaPrinciEjecutivos) {
		this.listaPrinciEjecutivos = listaPrinciEjecutivos;
	}

	public List<Participaciones> getListaParticipaciones() {
		return listaParticipaciones;
	}

	public void setListaParticipaciones(List<Participaciones> listaParticipaciones) {
		this.listaParticipaciones = listaParticipaciones;
	}

	public String getComentAccionariado() {
		return comentAccionariado;
	}

	public void setComentAccionariado(String comentAccionariado) {
		this.comentAccionariado = comentAccionariado;
	}

	public String getComentPartiSignificativa() {
		return comentPartiSignificativa;
	}

	public void setComentPartiSignificativa(String comentPartiSignificativa) {
		this.comentPartiSignificativa = comentPartiSignificativa;
	}

	public String getComentRatinExterno() {
		return comentRatinExterno;
	}

	public void setComentRatinExterno(String comentRatinExterno) {
		this.comentRatinExterno = comentRatinExterno;
	}

	public List<RatingExterno> getListaRatingExterno() {
		return listaRatingExterno;
	}

	public void setListaRatingExterno(List<RatingExterno> listaRatingExterno) {
		this.listaRatingExterno = listaRatingExterno;
	}

	public CompraVenta getTotalImportaciones() {
		return totalImportaciones;
	}

	public void setTotalImportaciones(CompraVenta totalImportaciones) {
		this.totalImportaciones = totalImportaciones;
	}

	public CompraVenta getImportacionesME() {
		return importacionesME;
	}

	public void setImportacionesME(CompraVenta importacionesME) {
		this.importacionesME = importacionesME;
	}

	public CompraVenta getTotalExportaciones() {
		return totalExportaciones;
	}

	public void setTotalExportaciones(CompraVenta totalExportaciones) {
		this.totalExportaciones = totalExportaciones;
	}

	public CompraVenta getExportacionesME() {
		return exportacionesME;
	}

	public void setExportacionesME(CompraVenta exportacionesME) {
		this.exportacionesME = exportacionesME;
	}

	public List<CompraVenta> getListaCompraVenta() {
		return listaCompraVenta;
	}

	public void setListaCompraVenta(List<CompraVenta> listaCompraVenta) {
		this.listaCompraVenta = listaCompraVenta;
	}

	public List<NegocioBeneficio> getListaNBActividades() {
		return listaNBActividades;
	}

	public void setListaNBActividades(List<NegocioBeneficio> listaNBActividades) {
		this.listaNBActividades = listaNBActividades;
	}

	public List<NegocioBeneficio> getListaNBNegocio() {
		return listaNBNegocio;
	}

	public void setListaNBNegocio(List<NegocioBeneficio> listaNBNegocio) {
		this.listaNBNegocio = listaNBNegocio;
	}

	public NegocioBeneficio getTotalActividad() {
		return totalActividad;
	}

	public void setTotalActividad(NegocioBeneficio totalActividad) {
		this.totalActividad = totalActividad;
	}

	public NegocioBeneficio getTotalNegocio() {
		return totalNegocio;
	}

	public void setTotalNegocio(NegocioBeneficio totalNegocio) {
		this.totalNegocio = totalNegocio;
	}

	public String getEspacioLibre() {
		return espacioLibre;
	}

	public void setEspacioLibre(String espacioLibre) {
		this.espacioLibre = espacioLibre;
	}

	public String getComentvaloraGlobal() {
		return comentvaloraGlobal;
	}

	public void setComentvaloraGlobal(String comentvaloraGlobal) {
		this.comentvaloraGlobal = comentvaloraGlobal;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}

	public boolean isFlagPlanilla() {
		return flagPlanilla;
	}

	public void setFlagPlanilla(boolean flagPlanilla) {
		this.flagPlanilla = flagPlanilla;
	}

	public boolean isFlagSintesisEmpresa() {
		return flagSintesisEmpresa;
	}

	public void setFlagSintesisEmpresa(boolean flagSintesisEmpresa) {
		this.flagSintesisEmpresa = flagSintesisEmpresa;
	}

	public boolean isFlagDatosMatriz() {
		return flagDatosMatriz;
	}

	public void setFlagDatosMatriz(boolean flagDatosMatriz) {
		this.flagDatosMatriz = flagDatosMatriz;
	}

	public boolean isFlagAccionariado() {
		return flagAccionariado;
	}

	public void setFlagAccionariado(boolean flagAccionariado) {
		this.flagAccionariado = flagAccionariado;
	}

	public boolean isFlagPrincipalesEjecutivos() {
		return flagPrincipalesEjecutivos;
	}

	public void setFlagPrincipalesEjecutivos(boolean flagPrincipalesEjecutivos) {
		this.flagPrincipalesEjecutivos = flagPrincipalesEjecutivos;
	}

	public boolean isFlagParticipacionesSignificativas() {
		return flagParticipacionesSignificativas;
	}

	public void setFlagParticipacionesSignificativas(
			boolean flagParticipacionesSignificativas) {
		this.flagParticipacionesSignificativas = flagParticipacionesSignificativas;
	}

	public boolean isFlagRatingExterno() {
		return flagRatingExterno;
	}

	public void setFlagRatingExterno(boolean flagRatingExterno) {
		this.flagRatingExterno = flagRatingExterno;
	}

	public boolean isFlagComprasVentas() {
		return flagComprasVentas;
	}

	public void setFlagComprasVentas(boolean flagComprasVentas) {
		this.flagComprasVentas = flagComprasVentas;
	}

	public boolean isFlagNegocioBeneficio() {
		return flagNegocioBeneficio;
	}

	public void setFlagNegocioBeneficio(boolean flagNegocioBeneficio) {
		this.flagNegocioBeneficio = flagNegocioBeneficio;
	}

	public boolean isFlagValoracionGlobal() {
		return flagValoracionGlobal;
	}

	public void setFlagValoracionGlobal(boolean flagValoracionGlobal) {
		this.flagValoracionGlobal = flagValoracionGlobal;
	}

	public List<CabTabla> getListaCabTabla() {
		return listaCabTabla;
	}

	public void setListaCabTabla(List<CabTabla> listaCabTabla) {
		this.listaCabTabla = listaCabTabla;
	}

	public List<CabTabla> getListaCabTablaPlanilla() {
		return listaCabTablaPlanilla;
	}

	public void setListaCabTablaPlanilla(List<CabTabla> listaCabTablaPlanilla) {
		this.listaCabTablaPlanilla = listaCabTablaPlanilla;
	}

	public List<CabTabla> getListaCabTablaCompra() {
		return listaCabTablaCompra;
	}

	public void setListaCabTablaCompra(List<CabTabla> listaCabTablaCompra) {
		this.listaCabTablaCompra = listaCabTablaCompra;
	}

	public List<CabTabla> getListaCabTablaVenta() {
		return listaCabTablaVenta;
	}

	public void setListaCabTablaVenta(List<CabTabla> listaCabTablaVenta) {
		this.listaCabTablaVenta = listaCabTablaVenta;
	}

	public List<CabTabla> getListaCabTablaActividad() {
		return listaCabTablaActividad;
	}

	public void setListaCabTablaActividad(List<CabTabla> listaCabTablaActividad) {
		this.listaCabTablaActividad = listaCabTablaActividad;
	}

	public List<CabTabla> getListaCabTablaNegocio() {
		return listaCabTablaNegocio;
	}

	public void setListaCabTablaNegocio(List<CabTabla> listaCabTablaNegocio) {
		this.listaCabTablaNegocio = listaCabTablaNegocio;
	}

	public String getChksAccionistas() {
		return chksAccionistas;
	}

	public void setChksAccionistas(String chksAccionistas) {
		this.chksAccionistas = chksAccionistas;
	}

	public String getChkParticipaciones() {
		return chkParticipaciones;
	}

	public void setChkParticipaciones(String chkParticipaciones) {
		this.chkParticipaciones = chkParticipaciones;
	}

	public String getChkEjecutivos() {
		return chkEjecutivos;
	}

	public void setChkEjecutivos(String chkEjecutivos) {
		this.chkEjecutivos = chkEjecutivos;
	}

	public String getChkRatingExterno() {
		return chkRatingExterno;
	}

	public void setChkRatingExterno(String chkRatingExterno) {
		this.chkRatingExterno = chkRatingExterno;
	}

	public String getChkNBActividades() {
		return chkNBActividades;
	}

	public void setChkNBActividades(String chkNBActividades) {
		this.chkNBActividades = chkNBActividades;
	}

	public String getChkNBNegocio() {
		return chkNBNegocio;
	}

	public void setChkNBNegocio(String chkNBNegocio) {
		this.chkNBNegocio = chkNBNegocio;
	}


	public DatosBasicosBlobBO getDatosBasicosBlobBO() {
		return datosBasicosBlobBO;
	}

	public void setDatosBasicosBlobBO(DatosBasicosBlobBO datosBasicosBlobBO) {
		this.datosBasicosBlobBO = datosBasicosBlobBO;
	}

	public List<Empresa> getListaEmpresasGrupoDB() {
		return listaEmpresasGrupoDB;
	}

	public void setListaEmpresasGrupoDB(List<Empresa> listaEmpresasGrupoDB) {
		this.listaEmpresasGrupoDB = listaEmpresasGrupoDB;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getFlagChangeEmpresa() {
		return flagChangeEmpresa;
	}

	public void setFlagChangeEmpresa(String flagChangeEmpresa) {
		this.flagChangeEmpresa = flagChangeEmpresa;
	}

	public String getCodempresagrupo() {
		return codempresagrupo;
	}

	public void setCodempresagrupo(String codempresagrupo) {
		this.codempresagrupo = codempresagrupo;
	}

	public String getCampoBlob() {
		return campoBlob;
	}

	public void setCampoBlob(String campoBlob) {
		this.campoBlob = campoBlob;
	}

	public String getValorBlob() {
		return valorBlob;
	}

	public void setValorBlob(String valorBlob) {
		this.valorBlob = valorBlob;
	}

	public List<CapitalizacionBursatil> getListaCapitalizacion() {
		return listaCapitalizacion;
	}

	public void setListaCapitalizacion(
			List<CapitalizacionBursatil> listaCapitalizacion) {
		this.listaCapitalizacion = listaCapitalizacion;
	}

	public CapitalizacionBursatil getCapitalizacion() {
		return capitalizacion;
	}

	public void setCapitalizacion(CapitalizacionBursatil capitalizacion) {
		this.capitalizacion = capitalizacion;
	}

	public Integer getPosiCapitalizacion() {
		return posiCapitalizacion;
	}

	public void setPosiCapitalizacion(Integer posiCapitalizacion) {
		this.posiCapitalizacion = posiCapitalizacion;
	}

	public String getChksCapitalizacion() {
		return chksCapitalizacion;
	}

	public void setChksCapitalizacion(String chksCapitalizacion) {
		this.chksCapitalizacion = chksCapitalizacion;
	}

	public boolean isFlagCapitalizacion() {
		return flagCapitalizacion;
	}

	public void setFlagCapitalizacion(boolean flagCapitalizacion) {
		this.flagCapitalizacion = flagCapitalizacion;
	}

	public List<SelectItem> getItemDivisaDB() {
		return itemDivisaDB;
	}

	public void setItemDivisaDB(List<SelectItem> itemDivisaDB) {
		this.itemDivisaDB = itemDivisaDB;
	}

	public List<Tabla> getListaDivisaDB() {
		return listaDivisaDB;
	}

	public void setListaDivisaDB(List<Tabla> listaDivisaDB) {
		this.listaDivisaDB = listaDivisaDB;
	}

	public List<SelectItem> getItemTipoDocumento() {
		return itemTipoDocumento;
	}

	public void setItemTipoDocumento(List<SelectItem> itemTipoDocumento) {
		this.itemTipoDocumento = itemTipoDocumento;
	}

	public List<Tabla> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<Tabla> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public String getIdtipoDocumento() {
		return idtipoDocumento;
	}

	public void setIdtipoDocumento(String idtipoDocumento) {
		this.idtipoDocumento = idtipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
	//controlar boton agregar y  eliminar

	public String getBtnPulsadoGrilla() {
		return btnPulsadoGrilla;
	}

	public void setBtnPulsadoGrilla(String btnPulsadoGrilla) {
		this.btnPulsadoGrilla = btnPulsadoGrilla;
	}
	
	
	
	
	
	
}
