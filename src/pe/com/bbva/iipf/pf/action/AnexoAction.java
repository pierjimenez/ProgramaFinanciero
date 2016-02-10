package pe.com.bbva.iipf.pf.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.mantenimiento.bo.EmpresaBO;
import pe.com.bbva.iipf.mantenimiento.bo.TablaBO;
import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.mantenimiento.model.Tabla;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.AnexoGarantiaBO;
import pe.com.bbva.iipf.pf.bo.ArchivoAnexoBO;
import pe.com.bbva.iipf.pf.bo.DatosBasicosBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.bo.RatingBO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.ArchivoAnexo;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.GarantiaHost;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SaldoCliente;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.stefanini.core.action.GenericAction;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.util.FormatUtil;
import pe.com.stefanini.core.util.StringUtil;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author EPOMAYAY
 *
 */

@Service("anexosAction")
@Scope("prototype")
public class AnexoAction extends GenericAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private TablaBO tablaBO;
	
	@Resource
	private AnexoBO anexoBO;
	
	@Resource
	private EmpresaBO empresaBO;
	
	@Resource
	private ArchivoAnexoBO archivoAnexoBO;
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	ParametroBO parametroBO;
	
	@Resource
	private AnexoGarantiaBO anexoGarantiaBO;
	
	@Resource
	private RatingBO ratingBO;
	
	@Resource
	private DatosBasicosBO datosBasicosBO ;
	
	private List<Tabla> listaBancos = new ArrayList<Tabla>();
	private List<Empresa> listaEmpresas =	new ArrayList<Empresa>();
	private List<Tabla> listaOperaciones = new ArrayList<Tabla>();
	private List<Tabla> listaLimites = new ArrayList<Tabla>();
	

	
	private List<AnexoColumna> listaColumnas = new ArrayList<AnexoColumna>();
	private List<AnexoColumna> listaColumnasFormulario = new ArrayList<AnexoColumna>();
	private List<Anexo> listaAnexos = new ArrayList<Anexo>();
	private List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
	private static int pos;
	private String idAnexo;
	private String posAnexo;
	private String editAnexo;
	private String addcontratos;
	private String indiceGarantia;
	

//	private List<SelectItem> itemAccionistaAnexo;	
	private List<Accionista> listaAccionistaAnexo =	new ArrayList<Accionista>();
	private String idAccionistaAnexo;
	
	/**
	 * El tipo fila almacenara un valor que corresponde con la seleccion 
	 * de que se agrega un banco, empresa o operacion
	 */
	private Integer tipoFila ;
	
	/**
	 * En esa variable se almacenara el valor del codigo del banco , empresa
	 * o operacion seleccionado
	 */
	private String codigoBanco;
	private String idEmpresaAnexo;
	private String limite;
	private String operacion;	
	private String subLimite;
	private String fechaRatingA;
	
	private String columnBureau;
	private String columnBuroAccionista;
	private String columnRating;				
	private String columnFecha;					
	private String columnLteAutorizado;	
	private String columnLteForm;				
	private String columnRgoActual;			
	private String columnRgoPropBbvaBc;	
	private String columnPropRiesgo;		
	private String columnObservaciones;	
	private String columnColumna1;			
	private String columnColumna2;	
	private String columnColumna3;	
	private String columnColumna4;	
	private String columnColumna5;	
	private String columnColumna6;	
	private String columnColumna7;	
	private String columnColumna8;	
	private String columnColumna9;	
	private String columnColumna10;	
	private String columnContrato;
	
	
	

	private String nombreColumna;
	
	private boolean flagAnexo1;
	private boolean flagAnexo2;
	private boolean flagAnexo3;
	private boolean flagAnexo4;
	
	private String flagActivar;
	
	enum TiposFila{
		TIPO_CABECERA(0),
		TIPO_BANCO(1),
		TIPO_EMPRESA(2),
		TIPO_LIMITES(3),
		TIPO_SUB_LIMITE(4),
		TIPO_OPERACION(5),
		TIPO_TOTAL(6),
		TIPO_ACCIONISTA(7);
		Integer tipo;
		TiposFila(Integer tipo){
			this.tipo = tipo;
		}
	}
	enum EditFila{
		EDITAR(1),
		NUEVO(0);
		Integer estado;
		EditFila(Integer estado){
			this.estado = estado;
		}
	}

	private FilaAnexo filaTotal = new FilaAnexo();
	
	private File fileAnexo;
	private String fileAnexoFileName;
	private List<ArchivoAnexo> listaArchivosAnexo = new ArrayList<ArchivoAnexo>();
	private List<AnexoGarantia> listaGarantiaAnexo=new ArrayList<AnexoGarantia>();
	
	private String codigoArchivo;
	private String extension;
	private String nombreArchivo;
	private String contentType;
	private String contentDisposition;
	private InputStream fileInputStream;
	
	private String codempresaRating;
	
	private String codempresaAccionista;


	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}


	public String getColumnBuroAccionista() {
		return columnBuroAccionista;
	}

	public void setColumnBuroAccionista(String columnBuroAccionista) {
		this.columnBuroAccionista = columnBuroAccionista;
	}

	public String init(){
		try {
			setFlagAnexo1(false);
			setFlagAnexo2(false);
			setFlagAnexo3(false);
			iniflagGrabar();
			getRequest().getSession().removeAttribute("listaBancos");
			getRequest().getSession().removeAttribute("listaEmpresas");
			getRequest().getSession().removeAttribute("listaOperaciones");
			getRequest().getSession().removeAttribute("listaFilaAnexosSession");
			getRequest().getSession().removeAttribute("listaLimites");
			getRequest().getSession().removeAttribute("listaGarantiaAnexo");	
			getRequest().getSession().removeAttribute("listaAccionistaAnexo");
			
		
			Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa programa = new Programa();
			programa.setId(idPrograma);
			anexoBO.setPrograma(programa);
			setListaArchivosAnexo(archivoAnexoBO.findListaArchivos(programa));
			listaGarantiaAnexo=anexoGarantiaBO.findAnexoXPrograma(programa);
			Collections.sort(listaGarantiaAnexo, AnexoGarantia.GarantiaComparator);
			cargarAnexos(idPrograma);
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	public void cargarAnexos(Long idPrograma){
		try{
			String bancolocal;
			boolean flagactiva=false;
			//ini MCG20121126
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
				bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
			} catch (Exception e) {
				bancolocal=Constantes.NOM_BANCO_DEFAULT;
			}	
			//fin MCG20121126
			List<FilaAnexo> listaFilaAnexosgen = new ArrayList<FilaAnexo>();
			
			Programa programaA = new Programa();
			programaA.setId(idPrograma);
			anexoBO.setPrograma(programaA);
			listaFilaAnexosgen=anexoBO.findAnexos();
			
			listaFilaAnexos = removerFilaTotalFilaAnexo(listaFilaAnexosgen,true);
			pos = 0;			
			if(listaFilaAnexos!= null && 
			   !listaFilaAnexos.isEmpty()){
				String activosaldo="NN";
				String banco="";
				int m=0;
				for(FilaAnexo filaAnexo : listaFilaAnexos){
					filaAnexo.setPos(pos++);
					
					//ini MCG20121126	Para activar solo al banco local: BBVA 
					//para los otros banco no se activa.. ver saldo
					if (filaAnexo.getAnexo().getTipoFila().equals(1) && 
							filaAnexo.getAnexo().getDescripcion().equals(bancolocal)) {
						banco=filaAnexo.getAnexo().getDescripcion();
						activosaldo="AA";
						m++;
					}
					if (m>0){
						if (filaAnexo.getAnexo().getTipoFila().equals(1) && 
								!filaAnexo.getAnexo().getDescripcion().equals(banco)) {						
							activosaldo="NN";
						}
					}
					
					filaAnexo.setActivosaldo(activosaldo);
					//logger.info("Saldo activo:" + filaAnexo.getAnexo().getDescripcion() + " Saldo activo:" + filaAnexo.getActivosaldo());
					//fin MCG20121126				
					
				}
				setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
				pos = listaFilaAnexos.size();
			}
			listaBancos = tablaBO.listarHijos(Constantes.ID_TABLA_BANCOS_EXTERNOS);
			listaEmpresas =empresaBO.listarEmpresasPorPrograma(idPrograma);
			
			listaOperaciones = tablaBO.listarHijos(Constantes.ID_TABLA_OPERACIONES);
			setListaLimites(tablaBO.listarHijos(Constantes.ID_TABLA_LISTA_LIMITES));

			List<Accionista> olistaAccionista=new ArrayList<Accionista>();
			olistaAccionista = datosBasicosBO.getListaAccionistas(programaA);
			Accionista oaccionista=new Accionista();
			oaccionista.setId(0L);
			oaccionista.setNombre(Constantes.VAL_DEFAULT_SELECTION);
			listaAccionistaAnexo.add(oaccionista);
			
			if (olistaAccionista!=null && olistaAccionista.size()>0){
				for(Accionista oacci:olistaAccionista ){
					listaAccionistaAnexo.add(oacci);
				}
			}	
			
			
			setObjectSession("listaBancos"		, listaBancos);
			setObjectSession("listaEmpresas"	, listaEmpresas);
			setObjectSession("listaOperaciones"	, listaOperaciones);
			setObjectSession("listaLimites"		, getListaLimites());
			setObjectSession("listaAccionistaAnexo",listaAccionistaAnexo);
			iniflagGrabar();
			//iniciarColumas();
			
			//List<AnexoColumna> columnasTotales = new ArrayList<AnexoColumna>();
			
			List<FilaAnexo> listaFilaAnexostotal = new ArrayList<FilaAnexo>();
			listaFilaAnexostotal=ObtenerFilaTotalFilaAnexo(listaFilaAnexosgen);
				
		
			if(listaFilaAnexostotal!=null && listaFilaAnexostotal.size()>0){
				flagactiva=false;	
			}else{
				flagactiva=true;				
			}
			//ini MCG20130815			
			if (flagactiva){			
				Anexo oanexoTot=new Anexo();			
				oanexoTot.setDescripcion("Total");
				oanexoTot.setTipoFila(TiposFila.TIPO_TOTAL.tipo);
				oanexoTot.setPrograma(programaA);
				filaTotal.setAnexo(oanexoTot);
			}else{				
				filaTotal=listaFilaAnexostotal.get(0);
			}
			//fin MCG20130815
			
			calcularMontos(listaFilaAnexos,flagactiva);
			//asignarCodigoItem(listaFilaAnexos);
			
			//setObjectSession("filaTotalSession", filaTotal);
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e){
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//ini MCG20130920
	
	//fin MCG20130920
	
	public List<FilaAnexo> removerFilaTotalFilaAnexo(List<FilaAnexo> listaFilaAnexos,boolean activaRemovetotal){	
		List<FilaAnexo> listaFilaAnexosfin = new ArrayList<FilaAnexo>();
		try {
			if (activaRemovetotal){
				if(!listaFilaAnexos.isEmpty()){
					for (FilaAnexo ofilaAnexo:listaFilaAnexos){
						if (ofilaAnexo.getAnexo().getTipoFila()!=null && ofilaAnexo.getAnexo().getTipoFila()!=6){
							listaFilaAnexosfin.add(ofilaAnexo);
						}					
					}					
				}
			}
		} catch (Exception e) {
			listaFilaAnexosfin = new ArrayList<FilaAnexo>();
			 
		}
		return listaFilaAnexosfin;
	}
	
	public List<FilaAnexo> ObtenerNameColumnFilaAnexo(List<FilaAnexo> listaFilaAnexos,boolean activaRemovetotal){	
		List<FilaAnexo> listaFilaAnexosfin = new ArrayList<FilaAnexo>();
		try {
			if (activaRemovetotal){
				if(!listaFilaAnexos.isEmpty()){
					for (FilaAnexo ofilaAnexo:listaFilaAnexos){
						if (ofilaAnexo.getAnexo().getTipoFila()!=null && ofilaAnexo.getAnexo().getTipoFila()==0){
							listaFilaAnexosfin.add(ofilaAnexo);
						}					
					}					
					
				}
			}
		} catch (Exception e) {
			listaFilaAnexosfin = new ArrayList<FilaAnexo>();
			 
		}
		return listaFilaAnexosfin;
	}
	public List<FilaAnexo> ObtenerFilaTotalFilaAnexo(List<FilaAnexo> listaFilaAnexos){	
		List<FilaAnexo> listaFilaAnexosfin = new ArrayList<FilaAnexo>();
		try {			
				if(!listaFilaAnexos.isEmpty()){
					for (FilaAnexo ofilaAnexo:listaFilaAnexos){
						if (ofilaAnexo.getAnexo().getTipoFila()!=null && ofilaAnexo.getAnexo().getTipoFila()==6){
							listaFilaAnexosfin.add(ofilaAnexo);
							break;
						}					
					}					
				}
			
		} catch (Exception e) {
			listaFilaAnexosfin = new ArrayList<FilaAnexo>();
			 
		}
		return listaFilaAnexosfin;
	}

	/**
	 * 
	 */
	public void iniciarColumas(){
		try {
			listaColumnas = anexoBO.findColumnas();
			if(listaColumnas.isEmpty()){
				//ini MCG20140818 add +1 a las demas columan
				AnexoColumna bureau = new AnexoColumna();
				bureau.setDescripcion(Constantes.DESC_BUREAU);
				bureau.setPosColumna(0);
				//fin MCG20140818
				AnexoColumna rating = new AnexoColumna();
				rating.setDescripcion("RATING");
				rating.setPosColumna(1);
				AnexoColumna fecha = new AnexoColumna();
				fecha.setDescripcion("FECHA");
				fecha.setPosColumna(2);
				AnexoColumna lteAutorizado = new AnexoColumna();
				lteAutorizado.setDescripcion("LTE AUTORIZADO");
				lteAutorizado.setPosColumna(3);
				AnexoColumna lteForm = new AnexoColumna();
				lteForm.setDescripcion("LTE FORM");
				lteForm.setPosColumna(4);
				AnexoColumna rgoActual = new AnexoColumna();
				rgoActual.setDescripcion("RGO ACTUAL");
				rgoActual.setPosColumna(5);
				AnexoColumna rgoPropBBVABC = new AnexoColumna();
				rgoPropBBVABC.setDescripcion("RGO PROP BBVA BC");
				rgoPropBBVABC.setPosColumna(6);
				
				//ini MCG20130802
				AnexoColumna propRiesgo = new AnexoColumna();
				propRiesgo.setDescripcion(Constantes.DESC_COLUMNA_PROP_RIESGO);
				propRiesgo.setPosColumna(7);
				
				//fin MCG20130802
				
				AnexoColumna observaciones = new AnexoColumna();
				observaciones.setDescripcion("OBSERVACIONES");
				observaciones.setPosColumna(8);
				
				listaColumnas.add(bureau);
				listaColumnas.add(rating);
				listaColumnas.add(fecha);
				listaColumnas.add(lteAutorizado);
				listaColumnas.add(lteForm);
				listaColumnas.add(rgoActual);
				listaColumnas.add(rgoPropBBVABC);
				listaColumnas.add(propRiesgo);
				listaColumnas.add(observaciones);
		}
			listaColumnasFormulario = listaColumnas;
			setObjectSession("listaColumnasSession", listaColumnas);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	
	public void limpiarColumas(){
		try {
			
				//ini MCG20140818 add +1 a los demas columnas
				AnexoColumna bureau = new AnexoColumna();
				bureau.setDescripcion(Constantes.DESC_BUREAU);
				bureau.setPosColumna(0);
				//fin MCG20140818 
				AnexoColumna rating = new AnexoColumna();
				rating.setDescripcion("RATING");
				rating.setPosColumna(1);
				AnexoColumna fecha = new AnexoColumna();
				fecha.setDescripcion("FECHA");
				fecha.setPosColumna(2);
				AnexoColumna lteAutorizado = new AnexoColumna();
				lteAutorizado.setDescripcion("LTE AUTORIZADO");
				lteAutorizado.setPosColumna(3);
				AnexoColumna lteForm = new AnexoColumna();
				lteForm.setDescripcion("LTE FORM");
				lteForm.setPosColumna(4);
				AnexoColumna rgoActual = new AnexoColumna();
				rgoActual.setDescripcion("RGO ACTUAL");
				rgoActual.setPosColumna(5);
				AnexoColumna rgoPropBBVABC = new AnexoColumna();
				rgoPropBBVABC.setDescripcion("RGO PROP BBVA BC");
				rgoPropBBVABC.setPosColumna(6);
				
				//ini MCG20130802
				AnexoColumna propRiesgo = new AnexoColumna();
				propRiesgo.setDescripcion(Constantes.DESC_COLUMNA_PROP_RIESGO);
				propRiesgo.setPosColumna(7);
				
				//fin MCG20130802
				
				AnexoColumna observaciones = new AnexoColumna();
				observaciones.setDescripcion("OBSERVACIONES");
				observaciones.setPosColumna(8);
				
				listaColumnas.add(bureau);
				listaColumnas.add(rating);
				listaColumnas.add(fecha);
				listaColumnas.add(lteAutorizado);
				listaColumnas.add(lteForm);
				listaColumnas.add(rgoActual);
				listaColumnas.add(rgoPropBBVABC);
				listaColumnas.add(propRiesgo);
				listaColumnas.add(observaciones);
		
			listaColumnasFormulario = listaColumnas;
			setObjectSession("listaColumnasSession", listaColumnas);
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
	}
	
	/**
	 * agrega filas a los anexos
	 * @return
	 */	
	public String addFila(){
			try {
				
				iniflagGrabar();
				boolean flagLimpiahijos=false;
				boolean flagtieneContratos=false;
				listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
				
				//para obtener las garantias del objeto
				Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
				Programa programa = new Programa();
				programa.setId(idPrograma);
				anexoBO.setPrograma(programa);
				listaGarantiaAnexo=anexoGarantiaBO.findAnexoXPrograma(programa);
				//fn
				
				logger.info("editAnexo="+editAnexo);
				logger.info("posAnexo="+posAnexo);
				FilaAnexo filaAnexo = new FilaAnexo();
				if(EditFila.EDITAR.estado.equals(Integer.valueOf(editAnexo))){
					filaAnexo.setPos(Integer.valueOf(posAnexo));
				}else{
					if(posAnexo!=null &&
					   !posAnexo.equals("")){
						filaAnexo.setPos(Integer.valueOf(posAnexo+1));
					}else{
						filaAnexo.setPos(pos);
					}
				}
				
				Anexo anexo = new Anexo();
	
				boolean flagValidaEmpresa=false;
				
							
				anexo.setRating(getColumnRating()==null?"":getColumnRating());				
				anexo.setFecha(getColumnFecha()==null?"":getColumnFecha());
				
				if(tipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){
					anexo.setBureau(getColumnBuroAccionista()==null?"":getColumnBuroAccionista());
				}
				
				if(tipoFila.equals(TiposFila.TIPO_EMPRESA.tipo)){				
					anexo.setFecha(getFechaRatingA()==null?"":getFechaRatingA());
					anexo.setBureau(getColumnBureau()==null?"":getColumnBureau());	
				}
				
				
				if (getColumnLteAutorizado()!=null){
				String valorLteAutorizado = getColumnLteAutorizado().equals("")?"0":getColumnLteAutorizado();
				valorLteAutorizado = FormatUtil.conversion(FormatUtil.round(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorLteAutorizado)),2));				
				anexo.setLteAutorizado(valorLteAutorizado);
				}
				if (getColumnLteForm()!=null){
				String valorLteForm = getColumnLteForm().equals("")?"0":getColumnLteForm();
				valorLteForm = FormatUtil.conversion(FormatUtil.round(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorLteForm)),2));	
				anexo.setLteForm(valorLteForm);
				}
				if (getColumnRgoActual()!=null){
				String valorRgoActual = getColumnRgoActual().equals("")?"0":getColumnRgoActual();
				valorRgoActual = FormatUtil.conversion(FormatUtil.round(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorRgoActual)),2));	
				anexo.setRgoActual(valorRgoActual);
				}
				if (getColumnRgoPropBbvaBc()!=null){
				String valorRgoPropBbvaBc = getColumnRgoPropBbvaBc().equals("")?"0":getColumnRgoPropBbvaBc();
				valorRgoPropBbvaBc = FormatUtil.conversion(FormatUtil.round(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorRgoPropBbvaBc)),2));	
				anexo.setRgoPropBbvaBc(valorRgoPropBbvaBc);
				}
				if (getColumnPropRiesgo()!=null){
				String valorPropRiesgo = getColumnPropRiesgo().equals("")?"0":getColumnPropRiesgo();
				valorPropRiesgo = FormatUtil.conversion(FormatUtil.round(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorPropRiesgo)),2));	
				anexo.setPropRiesgo(valorPropRiesgo);	
				}
				anexo.setObservaciones(getColumnObservaciones()==null?"":getColumnObservaciones());	
				anexo.setColumna1(getColumnColumna1()==null?"":getColumnColumna1());			
				anexo.setColumna2(getColumnColumna2()==null?"":getColumnColumna2());	
				anexo.setColumna3(getColumnColumna3()==null?"":getColumnColumna3());	
				anexo.setColumna4(getColumnColumna4()==null?"":getColumnColumna4());	
				anexo.setColumna5(getColumnColumna5()==null?"":getColumnColumna5());	
				anexo.setColumna6(getColumnColumna6()==null?"":getColumnColumna6());	
				anexo.setColumna7(getColumnColumna7()==null?"":getColumnColumna7());	
				anexo.setColumna8(getColumnColumna8()==null?"":getColumnColumna8());	
				anexo.setColumna9(getColumnColumna9()==null?"":getColumnColumna9());	
				anexo.setColumna10(getColumnColumna10()==null?"":getColumnColumna10());
				anexo.setContrato(getColumnContrato()==null?"":getColumnContrato());
				
				if(tipoFila.equals(TiposFila.TIPO_BANCO.tipo)){
					Tabla tabla = findBanco(codigoBanco);
					anexo.setDescripcion(tabla.getDescripcion());
					anexo.setCodigoItem(String.valueOf(tabla.getId()));
					anexo.setTipoFila(TiposFila.TIPO_BANCO.tipo);
					filaAnexo.setAnexo(anexo);

				}else if(tipoFila.equals(TiposFila.TIPO_EMPRESA.tipo)){
					Empresa empresa = findEmpresa(idEmpresaAnexo,idPrograma);	
					if (empresa!=null){
						anexo.setDescripcion(empresa.getNombre()==null?"":empresa.getNombre().trim());	
						anexo.setCodigoItem(String.valueOf(empresa.getId()));
						anexo.setFecha(getFechaRatingA()==null?"":getFechaRatingA());					
					
						logger.error("Anexo:Descripcion Empresa: "+ anexo.getDescripcion());
					}else{
						flagValidaEmpresa=true;
						anexo.setDescripcion("");
						anexo.setCodigoItem("");
						
					}								
					anexo.setTipoFila(TiposFila.TIPO_EMPRESA.tipo);
					filaAnexo.setAnexo(anexo);

				}else if(tipoFila.equals(TiposFila.TIPO_LIMITES.tipo)){					
					Tabla tabla = findOperaciones(operacion);
					anexo.setDescripcion(tabla.getDescripcion());
					anexo.setCodigoItem(String.valueOf(tabla.getId()));
					anexo.setTipoFila(TiposFila.TIPO_LIMITES.tipo);
					filaAnexo.setAnexo(anexo);
					flagLimpiahijos=true;
				}else if(tipoFila.equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
					//Tabla tabla = findOperaciones(subLimite);
					anexo.setDescripcion(subLimite);
					anexo.setCodigoItem("");
					anexo.setTipoFila(TiposFila.TIPO_SUB_LIMITE.tipo);
					filaAnexo.setAnexo(anexo);	
				}else if(tipoFila.equals(TiposFila.TIPO_OPERACION.tipo)){
					Tabla tabla = findLimites(limite);
					anexo.setDescripcion(tabla.getDescripcion());
					anexo.setCodigoItem(String.valueOf(tabla.getId()));
					anexo.setTipoFila(TiposFila.TIPO_OPERACION.tipo);
					filaAnexo.setAnexo(anexo);
				}else if(tipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){					
					Accionista oaccionista = findAccionista(idAccionistaAnexo);
					if (oaccionista!=null){
						anexo.setDescripcion(oaccionista.getNombre()==null?"":oaccionista.getNombre());
						anexo.setCodigoItem(String.valueOf(oaccionista.getId()));
					}else{
						anexo.setDescripcion("");
						anexo.setCodigoItem("");
					}
					anexo.setTipoFila(TiposFila.TIPO_ACCIONISTA.tipo);
					filaAnexo.setAnexo(anexo);
				}			
				
				if(getObjectSession("listaFilaAnexosSession") != null){
					listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
				}
				
				if (flagValidaEmpresa){
					throw new BOException("La Empresa no Pertenece al Grupo.Ingrese Nuevamente los datos");
				}			
				if(anexoBO.validarFilas(listaFilaAnexos, filaAnexo,posAnexo,editAnexo)){
					int posicionH=0;
					if(EditFila.EDITAR.estado.equals(Integer.valueOf(editAnexo))){
						int posicion =0;
						for(FilaAnexo item: listaFilaAnexos){
							if(item.getPos() == Integer.valueOf(posAnexo)){								
								listaFilaAnexos.set(posicion, filaAnexo);
								posicionH=posicion;
								break;
							}
							posicion++;
						}					
					}else{
						if(posAnexo!=null &&
						   !posAnexo.equals("")){
							listaFilaAnexos.add(Integer.valueOf(posAnexo)+1,filaAnexo);
							posicionH=Integer.valueOf(posAnexo)+1;
						}else{
							listaFilaAnexos.add(filaAnexo);
							posicionH=pos;
							pos++;
						}
						
					}

					limpirHijosLimite(listaFilaAnexos,String.valueOf(posicionH),flagLimpiahijos);
						
					anexoBO.asignarActivoColumna(listaFilaAnexos);
					actualizarPosicionesListaAnexos();
					setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
				}
				
			} catch (BOException e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
			}
			

			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			setListaAccionistaAnexo((List<Accionista>)getObjectSession("listaAccionistaAnexo"));
			
			calcularMontos(listaFilaAnexos,true);
			setFlagAnexo1(true);
			finflagGrabar();
			
			return "anexos";
		}

	
	public void limpirHijosLimite(List<FilaAnexo> listaFilaAnexos,String posicionAnexo,boolean flaglimpiahijos ){
		try {
			

		if (flaglimpiahijos){
			List<FilaAnexo> olistaFilaAnexoHijos=new ArrayList<FilaAnexo>();
			olistaFilaAnexoHijos=anexoBO.ObtenerHijosByIdPabre(listaFilaAnexos,posicionAnexo);
				if (olistaFilaAnexoHijos!=null && olistaFilaAnexoHijos.size()>0){
					for (FilaAnexo oFilaAnexoHijos:olistaFilaAnexoHijos){
						int hposicion =0;
						for(FilaAnexo item:listaFilaAnexos){
							if(oFilaAnexoHijos.getPos()==item.getPos()){
								if (item.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){
									item.getAnexo().setLteAutorizado("");
									item.getAnexo().setLteForm("");
								}
								item.getAnexo().setRgoActual("");
								listaFilaAnexos.set(hposicion, item);
								break;
							}
							hposicion++;	
						}
					}
					
				}					
			}
		} catch (Exception e) {
			 
		}
		
	}
	
	/**
	 * Agrega una columna a la tabla anexos
	 * @return
	 */
	public String addColumna(){
		iniflagGrabar();		
		listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
		if(listaFilaAnexos!= null &&
		  !listaFilaAnexos.isEmpty()){
			FilaAnexo ofilaAnexo=listaFilaAnexos.get(0);
			String numcol=ofilaAnexo.getAnexo().getNumColumna()==null?"0":ofilaAnexo.getAnexo().getNumColumna();
			int icontcol=Integer.valueOf(numcol);			
				icontcol+=1;				
				if (icontcol==1){
					ofilaAnexo.getAnexo().setColumna1(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol1("1");
				}	
				if (icontcol==2){
					ofilaAnexo.getAnexo().setColumna2(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol2("1");
				}
				if (icontcol==3){
					ofilaAnexo.getAnexo().setColumna3(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol3("1");
				}
				if (icontcol==4){
					ofilaAnexo.getAnexo().setColumna4(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol4("1");
				}
				if (icontcol==5){
					ofilaAnexo.getAnexo().setColumna5(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol5("1");
				}
				if (icontcol==6){
					ofilaAnexo.getAnexo().setColumna6(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol6("1");
				}
				if (icontcol==7){
					ofilaAnexo.getAnexo().setColumna7(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol7("1");
				}
				if (icontcol==8){
					ofilaAnexo.getAnexo().setColumna8(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol8("1");
				}
				if (icontcol==9){
					ofilaAnexo.getAnexo().setColumna9(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol9("1");
				}
				if (icontcol==10){
					ofilaAnexo.getAnexo().setColumna10(nombreColumna);
					ofilaAnexo.getAnexo().setActivoCol10("1");
				}
				ofilaAnexo.getAnexo().setNumColumna(String.valueOf(icontcol));
				anexoBO.asignarActivoColumna(listaFilaAnexos)	;
				
			setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
		}
		listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
		listaBancos = (List<Tabla>)getObjectSession("listaBancos");
		listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
		listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
		setListaLimites((List<Tabla>)getObjectSession("listaLimites"));
		setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
		listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");

		calcularMontos(listaFilaAnexos,true);
		setFlagAnexo1(true);
		finflagGrabar();
		return "anexos";
	}
	
	public void actualizarPosicionesListaAnexos(){
		int nuevaPos=0;
		String activosaldo="0";
		String banco="";
		int m=0;
		
		//ini MCG20121126
		String bancolocal;
		try {
			Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
			bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
		} catch (Exception e) {
			bancolocal=Constantes.NOM_BANCO_DEFAULT;
		}
		//fin MCG20121126
		
		for(FilaAnexo filaAnexo : listaFilaAnexos){
			filaAnexo.setPos(nuevaPos++);
			
			//ini MCG20121126			
			if (filaAnexo.getAnexo().getTipoFila().equals(1) && 
					filaAnexo.getAnexo().getDescripcion().equals(bancolocal)) {
				banco=filaAnexo.getAnexo().getDescripcion();
				activosaldo="AA";
				m++;
			}
			if (m>0){
				if (filaAnexo.getAnexo().getTipoFila().equals(1) && 
						!filaAnexo.getAnexo().getDescripcion().equals(banco)) {						
					activosaldo="NN";
				}
			}
			
			filaAnexo.setActivosaldo(activosaldo);
			//logger.info("Saldo activo:" + filaAnexo.getAnexo().getDescripcion() + " Saldo activo:" + filaAnexo.getActivosaldo());
			//ini MCG20121126
		}
	}
	
	public Tabla findBanco(String id){
		List<Tabla> lista = (List<Tabla>)getObjectSession("listaBancos");
		for(Tabla tabla: lista){
			if(tabla.getId().toString().equals(id)){
				return tabla;
			}
		}
		return null;
	}

	// es considerado para obtener los limites. se cambio la descripcion del padre Operacion por limite
	public Tabla findOperaciones(String id){
		List<Tabla> lista = (List<Tabla>)getObjectSession("listaOperaciones");
		for(Tabla tabla: lista){
			if(tabla.getId().toString().equals(id)){
				return tabla;
			}
		}
		return null;
	}

	
	// es considerado para obtener las operacion. nueva tabla
	public Tabla findLimites(String id){
		List<Tabla> lista = (List<Tabla>)getObjectSession("listaLimites");
		for(Tabla tabla: lista){
			if(tabla.getId().toString().equals(id)){
				return tabla;
			}
		}
		return null;
	}

	public Empresa findEmpresa(String id,Long idPrograma){
		//List<Empresa> lista = (List<Empresa>)getObjectSession("listaEmpresas");
			try {
				List<Empresa> lista =empresaBO.listarEmpresasPorPrograma(idPrograma);
				for(Empresa empresa: lista){
					if(empresa.getId().toString().equals(id)){
						return empresa;
					}
				}
			} catch (Exception e) {
				 				
			}
		return null;
	}
	

	
	public Accionista findAccionista(String id){
		List<Accionista> lista = (List<Accionista>)getObjectSession("listaAccionistaAnexo");
		for(Accionista oaccionista: lista){
			if(oaccionista.getId()!=null && oaccionista.getId().toString().equals(id)){
				return oaccionista;
			}
		}
		return null;
	}
	


	public String deleteFila(){
		try {
			
			iniflagGrabar();
		
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			if(listaFilaAnexos!= null &&
			   listaFilaAnexos.size()>0){
				try{
					if(anexoBO.deleteFila(Integer.valueOf(posAnexo), listaFilaAnexos)){
						addActionMessage("Fila removida.");
					}
					actualizarPosicionesListaAnexos();
				}catch(BOException e){
					addActionError(e.getMessage());
					logger.error(StringUtil.getStackTrace(e));
				}
				setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
			}else{
				addActionError("La lista no tiene elementos.");
			}
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");
			calcularMontos(listaFilaAnexos,true);
			setFlagAnexo1(true);
			finflagGrabar();
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	public String deleteColumna(){
		
		try {
			
			iniflagGrabar();
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			if(listaFilaAnexos!= null &&
			  !listaFilaAnexos.isEmpty()){
				FilaAnexo ofilaAnexoCab=listaFilaAnexos.get(0);
				String numcol=ofilaAnexoCab.getAnexo().getNumColumna()==null?"0":ofilaAnexoCab.getAnexo().getNumColumna();
				int icontcol=Integer.valueOf(numcol);
				
				if (icontcol>0){
					for(FilaAnexo ofilaAnexo : listaFilaAnexos){		
											
							if (icontcol==1){
								ofilaAnexo.getAnexo().setColumna1("");
								ofilaAnexo.getAnexo().setActivoCol1("0");
							}	
							if (icontcol==2){
								ofilaAnexo.getAnexo().setColumna2("");
								ofilaAnexo.getAnexo().setActivoCol2("0");
							}
							if (icontcol==3){
								ofilaAnexo.getAnexo().setColumna3("");
								ofilaAnexo.getAnexo().setActivoCol3("0");
							}
							if (icontcol==4){
								ofilaAnexo.getAnexo().setColumna4("");
								ofilaAnexo.getAnexo().setActivoCol4("0");
							}
							if (icontcol==5){
								ofilaAnexo.getAnexo().setColumna5("");
								ofilaAnexo.getAnexo().setActivoCol5("0");
							}
							if (icontcol==6){
								ofilaAnexo.getAnexo().setColumna6("");
								ofilaAnexo.getAnexo().setActivoCol6("0");
							}
							if (icontcol==7){
								ofilaAnexo.getAnexo().setColumna7("");
								ofilaAnexo.getAnexo().setActivoCol7("0");
							}
							if (icontcol==8){
								ofilaAnexo.getAnexo().setColumna8("");
								ofilaAnexo.getAnexo().setActivoCol8("0");
							}
							if (icontcol==9){
								ofilaAnexo.getAnexo().setColumna9("");
								ofilaAnexo.getAnexo().setActivoCol9("0");
							}
							if (icontcol==10){
								ofilaAnexo.getAnexo().setColumna10("");
								ofilaAnexo.getAnexo().setActivoCol10("0");
							}
							ofilaAnexo.getAnexo().setNumColumna(String.valueOf(icontcol-1));					
					}	
					addActionMessage("Columna removida.");
				}else{
					addActionError("Las 13 primeras columnas no pueden ser eliminadas");
				}
				anexoBO.asignarActivoColumna(listaFilaAnexos)	;
				setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
			}
	
		
		listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
		listaBancos = (List<Tabla>)getObjectSession("listaBancos");
		listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
		listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
		setListaLimites((List<Tabla>)getObjectSession("listaLimites"));
		setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
		listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");
		calcularMontos(listaFilaAnexos,true);
		setFlagAnexo1(true);
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	/**
	 * Se realiza un calculo por empresa, banco y totales por bancos.
	 * @param lista
	 */
	public void calcularMontos(List<FilaAnexo> lista,boolean flagactiva){
		if(lista != null &&
		   !lista.isEmpty()){
			anexoBO.calcularMontosPorEmpresa(lista);
			anexoBO.calcularMontosPorAccionista(lista);
			anexoBO.calcularMontoPorBanco(lista);		
			if (flagactiva){
				filaTotal = anexoBO.calcularMontoTotal(lista);
			}
		}
	}
	
	
	public void asignarCodigoItem(List<FilaAnexo> lista){
		try {			

		if(lista != null && !lista.isEmpty()){			
			for(FilaAnexo filaAnexo : lista){
				
				if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){					
					if (filaAnexo.getAnexo().getCodigoItem()==null){
						String idbanco = this.findIdBancoEdit(filaAnexo.getAnexo().getDescripcion());
						filaAnexo.getAnexo().setCodigoItem(idbanco);
					}					
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo)){
					if (filaAnexo.getAnexo().getCodigoItem()==null){
						String idempresa = this.findIdEmpresaEdit(filaAnexo.getAnexo().getDescripcion());
						filaAnexo.getAnexo().setCodigoItem(idempresa);
					}
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)){					
					if (filaAnexo.getAnexo().getCodigoItem()==null){
						String idlimite = this.findIdOperacion(filaAnexo.getAnexo().getDescripcion());
						filaAnexo.getAnexo().setCodigoItem(idlimite);
					}
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){					
						filaAnexo.getAnexo().setCodigoItem("");
					
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){			
					if (filaAnexo.getAnexo().getCodigoItem()==null){
						String idoperacion = findIdOperacion2(filaAnexo.getAnexo().getDescripcion());
						filaAnexo.getAnexo().setCodigoItem(idoperacion);
					}					
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)){						
					if (filaAnexo.getAnexo().getCodigoItem()==null){
						String idaccionista  = findIdAccionistaEdit(filaAnexo.getAnexo().getDescripcion());
						filaAnexo.getAnexo().setCodigoItem(idaccionista);
					}
				}
				
			}//for
		} //lista
		
		} catch (Exception e) {
			 
		}
		
	}
	
	/**
	 * Registra los anexos
	 * @return
	 */
	public String saveAnexos(){
		try {
			iniflagGrabar();
			Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa programa = new Programa();
			programa.setId(idPrograma);
			UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			anexoBO.setPrograma(programa);
			anexoBO.setUsuarioSession(usuarioSession);
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			 List<FilaAnexo> olistaFilaAnexosTotal = new ArrayList<FilaAnexo>();
			 olistaFilaAnexosTotal.add(filaTotal);
			anexoBO.setListaFilaAnexos(listaFilaAnexos);
			anexoBO.setListaFilaAnexosTotal(olistaFilaAnexosTotal);
			anexoBO.saveAnexos();
			addActionMessage("Anexo registrado correctamente.");
			setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");

			calcularMontos(listaFilaAnexos,false);
			setFlagAnexo1(true);
			finflagGrabar();
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	public String saveGarantias(){
		try {
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");

			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");
			calcularMontos(listaFilaAnexos,false);
			
			Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa programa = new Programa();
			programa.setId(idPrograma);
			UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			getAnexoGarantiaBO().setPrograma(programa);
			getAnexoGarantiaBO().setUsuarioSession(usuarioSession);			
			getAnexoGarantiaBO().setListaGarantiaAnexo(listaGarantiaAnexo);
			getAnexoGarantiaBO().saveAnexosGarantia();
			listaGarantiaAnexo=anexoGarantiaBO.findAnexoXPrograma(programa);
			Collections.sort(listaGarantiaAnexo, AnexoGarantia.GarantiaComparator);
			addActionMessage("La Garantia del anexo se ha registrado correctamente.");			
		} catch (BOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	//ini MCG20130920
	
	public String limpiarAnexo(){
		try {
			listaFilaAnexos = new ArrayList<FilaAnexo>();			
			Map<String, Object> sessionparam = ActionContext.getContext().getSession();
			sessionparam.remove("listaFilaAnexosSession");
			
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			pos = 0;
			Anexo oanexoTot=new Anexo();			
			oanexoTot.setDescripcion("Total");
			oanexoTot.setTipoFila(TiposFila.TIPO_TOTAL.tipo);
			filaTotal.setAnexo(oanexoTot);
			setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
			addActionMessage("Se a limpiado la posicion de Cliente correctamente.");			
		
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		return "anexos";
	}
	
	//fin MCG20130920
	
	/**
	 * Este metodo registra los archivos de anexos 
	 * @return
	 */
	public String saveFileAnexo(){
		Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
		Programa programa = new Programa();
		programa.setId(idPrograma);
		UsuarioSesion usuarioSession= (UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
		ArchivoAnexo archivoAnexo = new ArchivoAnexo();
		String strNombreArchivo = fileAnexoFileName;
		if(fileAnexoFileName!=null && !fileAnexoFileName.equals("")){
			int extensionIndex = strNombreArchivo.lastIndexOf(".");
			String strExtension="";
	    	if(extensionIndex>0){
	    		strExtension = strNombreArchivo.substring(extensionIndex +1,strNombreArchivo.length());
	    	}
			if(fileAnexoFileName.split("\\.").length>0){
				strNombreArchivo = fileAnexoFileName.split("\\.")[0];
			}
	    	
			setCamposAuditoria(archivoAnexo, 
							   usuarioSession);
			archivoAnexo.setExtencion(strExtension);
			archivoAnexo.setNombreArchivo(strNombreArchivo);
			
			try {
				archivoAnexoBO.saveFileAnexo(fileAnexo, 
											 programa, 
											 archivoAnexo);
				
				setListaArchivosAnexo(archivoAnexoBO.findListaArchivos(programa));
				setObjectSession("listaArchivosAnexos", getListaArchivosAnexo());
				cargarAnexos(idPrograma);
			} catch (BOException e) {
				logger.error(StringUtil.getStackTrace(e));
			}
		}else{
			addActionError("Seleccione un archivo");
		}
		return "anexos";
	}
	
	 public String descargarArchivo() throws Exception { 	
			logger.info("INICIO download");
			String codigoDocumento=getCodigoArchivo();
			String extencionDocumento= getExtension();
			String nombreDocumento=this.getNombreArchivo();
			String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
			File file=null;
			String forward = SUCCESS;
			try {
				String dir  = getObjectParamtrosSession(Constantes.DIR_FILES_ANEXOS).toString();
				String pathToFile=dir+File.separator+nombreArchivo;
				String fileName=nombreDocumento; 
				file = new File(pathToFile);
			   	setContentType(new MimetypesFileTypeMap().getContentType(file));
			   	setContentDisposition("attachment;filename=\""+ fileName + "." + extencionDocumento + "\"");
				fileInputStream =  new FileInputStream(file);
			}catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "anexos";
			}
			return forward;
	}
	 
		public void nuevaFila(){
			String forward = "anexos";
			String anexoJSON = "";			
			try {
				
				if(getObjectSession("listaFilaAnexosSession") != null){
					listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
				}
				
				if (listaFilaAnexos!=null && listaFilaAnexos.size()>0){
								FilaAnexo filaAnexo =listaFilaAnexos.get(0);						
									anexoJSON = "{\"actCol1\":\"" +
									(filaAnexo.getAnexo().getActivoCol1()==null?"0":filaAnexo.getAnexo().getActivoCol1());
									anexoJSON += "\",\"actCol2\":\"" +
									(filaAnexo.getAnexo().getActivoCol2()==null?"0":filaAnexo.getAnexo().getActivoCol2());
									anexoJSON += "\",\"actCol3\":\"" +
									(filaAnexo.getAnexo().getActivoCol3()==null?"0":filaAnexo.getAnexo().getActivoCol3());
									anexoJSON += "\",\"actCol4\":\"" +
									(filaAnexo.getAnexo().getActivoCol4()==null?"0":filaAnexo.getAnexo().getActivoCol4());
									anexoJSON += "\",\"actCol5\":\"" +
									(filaAnexo.getAnexo().getActivoCol5()==null?"0":filaAnexo.getAnexo().getActivoCol5());
									anexoJSON += "\",\"actCol6\":\"" +
									(filaAnexo.getAnexo().getActivoCol6()==null?"0":filaAnexo.getAnexo().getActivoCol6());
									anexoJSON += "\",\"actCol7\":\"" +
									(filaAnexo.getAnexo().getActivoCol7()==null?"0":filaAnexo.getAnexo().getActivoCol7());
									anexoJSON += "\",\"actCol8\":\"" +
									(filaAnexo.getAnexo().getActivoCol8()==null?"0":filaAnexo.getAnexo().getActivoCol8());
									anexoJSON += "\",\"actCol9\":\"" +
									(filaAnexo.getAnexo().getActivoCol9()==null?"0":filaAnexo.getAnexo().getActivoCol9());
									anexoJSON += "\",\"actCol10\":\"" +
									(filaAnexo.getAnexo().getActivoCol10()==null?"0":filaAnexo.getAnexo().getActivoCol10());

									anexoJSON += "\",\"cabcolumna1\":\"" +
									(filaAnexo.getAnexo().getColumna1()==null?"":filaAnexo.getAnexo().getColumna1());
									anexoJSON += "\",\"cabcolumna2\":\"" +
									(filaAnexo.getAnexo().getColumna2()==null?"":filaAnexo.getAnexo().getColumna2());
									anexoJSON += "\",\"cabcolumna3\":\"" +
									(filaAnexo.getAnexo().getColumna3()==null?"":filaAnexo.getAnexo().getColumna3());
									anexoJSON += "\",\"cabcolumna4\":\"" +
									(filaAnexo.getAnexo().getColumna4()==null?"":filaAnexo.getAnexo().getColumna4());
									anexoJSON += "\",\"cabcolumna5\":\"" +
									(filaAnexo.getAnexo().getColumna5()==null?"":filaAnexo.getAnexo().getColumna5());
									anexoJSON += "\",\"cabcolumna6\":\"" +
									(filaAnexo.getAnexo().getColumna6()==null?"":filaAnexo.getAnexo().getColumna6());
									anexoJSON += "\",\"cabcolumna7\":\"" +
									(filaAnexo.getAnexo().getColumna7()==null?"":filaAnexo.getAnexo().getColumna7());
									anexoJSON += "\",\"cabcolumna8\":\"" +
									(filaAnexo.getAnexo().getColumna8()==null?"":filaAnexo.getAnexo().getColumna8());
									anexoJSON += "\",\"cabcolumna9\":\"" +
									(filaAnexo.getAnexo().getColumna9()==null?"":filaAnexo.getAnexo().getColumna9());
									anexoJSON += "\",\"cabcolumna10\":\"" +
									(filaAnexo.getAnexo().getColumna10()==null?"":filaAnexo.getAnexo().getColumna10());

						anexoJSON += "\"}";					
					
				}
				logger.info("JSON="+anexoJSON.toString());
				//System.out.println("JSON="+anexoJSON.toString());
				getResponse().setContentType("text/html");   
	            PrintWriter out = getResponse().getWriter(); 
	            out.print(anexoJSON.toString());
				
			} catch (NumberFormatException e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "anexos";
			} catch (IOException e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
				forward = "anexos";
			} 
		}
		
		 
	public void editFila(){
		String forward = "anexos";
		String anexoJSON = "";
		Anexo ocabAnexo =new Anexo();
		try {
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			
			if (listaFilaAnexos!=null && listaFilaAnexos.size()>0){
				ocabAnexo=listaFilaAnexos.get(0).getAnexo();				
			}
			for(FilaAnexo filaAnexo : listaFilaAnexos){
				
				
				if(filaAnexo.getPos() == Integer.valueOf(posAnexo)){
					anexoJSON = "{\"posAnexo\":\"" +
								filaAnexo.getPos() +
								"\",\"desFila\":\"" ;
								if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){
									anexoJSON += findIdBancoEdit(filaAnexo.getAnexo().getDescripcion()); 
								}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo)){
									anexoJSON += findIdEmpresaEdit(filaAnexo.getAnexo().getDescripcion()); 	
								}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)){
									String idAccionista=filaAnexo.getAnexo().getCodigoItem();										
									if ((idAccionista==null) || (idAccionista!=null && idAccionista.equals("")) ) {
										idAccionista=findIdAccionistaEdit(filaAnexo.getAnexo().getDescripcion());
									}
									anexoJSON +=idAccionista;									
									
								}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)){
									anexoJSON += findIdOperacion(filaAnexo.getAnexo().getDescripcion()); 
								}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){
									anexoJSON += findIdOperacion2(filaAnexo.getAnexo().getDescripcion()); 							
								}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
									anexoJSON += filaAnexo.getAnexo().getDescripcion();
								}else{
									anexoJSON += "";
								}
								anexoJSON += "\",\"tipoFila\":\"" +
								(filaAnexo.getAnexo().getTipoFila()==null?"":filaAnexo.getAnexo().getTipoFila()); 
								anexoJSON += "\",\"bureau\":\"" +
								(filaAnexo.getAnexo().getBureau()==null?"":filaAnexo.getAnexo().getBureau());
								anexoJSON += "\",\"rating\":\"" +
								(filaAnexo.getAnexo().getRating()==null?"":filaAnexo.getAnexo().getRating());
								anexoJSON += "\",\"fecha\":\"" +
								(filaAnexo.getAnexo().getFecha()==null?"":filaAnexo.getAnexo().getFecha());
								anexoJSON += "\",\"lteAutorizado\":\"" +
								(filaAnexo.getAnexo().getLteAutorizado()==null?"":filaAnexo.getAnexo().getLteAutorizado());
								anexoJSON += "\",\"lteForm\":\"" +
								(filaAnexo.getAnexo().getLteForm()==null?"":filaAnexo.getAnexo().getLteForm());
								anexoJSON += "\",\"rgoActual\":\"" +
								(filaAnexo.getAnexo().getRgoActual()==null?"":filaAnexo.getAnexo().getRgoActual());
								anexoJSON += "\",\"rgoPropBbvaBc\":\"" +
								(filaAnexo.getAnexo().getRgoPropBbvaBc()==null?"":filaAnexo.getAnexo().getRgoPropBbvaBc());
								anexoJSON += "\",\"propRiesgo\":\"" +
								(filaAnexo.getAnexo().getPropRiesgo()==null?"":filaAnexo.getAnexo().getPropRiesgo());
								anexoJSON += "\",\"observaciones\":\"" +
								(filaAnexo.getAnexo().getObservaciones()==null?"":filaAnexo.getAnexo().getObservaciones());
								anexoJSON += "\",\"columna1\":\"" +
								(filaAnexo.getAnexo().getColumna1()==null?"":filaAnexo.getAnexo().getColumna1());
								anexoJSON += "\",\"columna2\":\"" +
								(filaAnexo.getAnexo().getColumna2()==null?"":filaAnexo.getAnexo().getColumna2());
								anexoJSON += "\",\"columna3\":\"" +
								(filaAnexo.getAnexo().getColumna3()==null?"":filaAnexo.getAnexo().getColumna3());
								anexoJSON += "\",\"columna4\":\"" +
								(filaAnexo.getAnexo().getColumna4()==null?"":filaAnexo.getAnexo().getColumna4());
								anexoJSON += "\",\"columna5\":\"" +
								(filaAnexo.getAnexo().getColumna5()==null?"":filaAnexo.getAnexo().getColumna5());
								anexoJSON += "\",\"columna6\":\"" +
								(filaAnexo.getAnexo().getColumna6()==null?"":filaAnexo.getAnexo().getColumna6());
								anexoJSON += "\",\"columna7\":\"" +
								(filaAnexo.getAnexo().getColumna7()==null?"":filaAnexo.getAnexo().getColumna7());
								anexoJSON += "\",\"columna8\":\"" +
								(filaAnexo.getAnexo().getColumna8()==null?"":filaAnexo.getAnexo().getColumna8());
								anexoJSON += "\",\"columna9\":\"" +
								(filaAnexo.getAnexo().getColumna9()==null?"":filaAnexo.getAnexo().getColumna9());
								anexoJSON += "\",\"columna10\":\"" +
								(filaAnexo.getAnexo().getColumna10()==null?"":filaAnexo.getAnexo().getColumna10());
								anexoJSON += "\",\"contrato\":\"" +
								(filaAnexo.getAnexo().getContrato()==null?"":filaAnexo.getAnexo().getContrato());
								
								
								anexoJSON += "\",\"actCol1\":\"" +
								(filaAnexo.getAnexo().getActivoCol1()==null?"0":filaAnexo.getAnexo().getActivoCol1());
								anexoJSON += "\",\"actCol2\":\"" +
								(filaAnexo.getAnexo().getActivoCol2()==null?"0":filaAnexo.getAnexo().getActivoCol2());
								anexoJSON += "\",\"actCol3\":\"" +
								(filaAnexo.getAnexo().getActivoCol3()==null?"0":filaAnexo.getAnexo().getActivoCol3());
								anexoJSON += "\",\"actCol4\":\"" +
								(filaAnexo.getAnexo().getActivoCol4()==null?"0":filaAnexo.getAnexo().getActivoCol4());
								anexoJSON += "\",\"actCol5\":\"" +
								(filaAnexo.getAnexo().getActivoCol5()==null?"0":filaAnexo.getAnexo().getActivoCol5());
								anexoJSON += "\",\"actCol6\":\"" +
								(filaAnexo.getAnexo().getActivoCol6()==null?"0":filaAnexo.getAnexo().getActivoCol6());
								anexoJSON += "\",\"actCol7\":\"" +
								(filaAnexo.getAnexo().getActivoCol7()==null?"0":filaAnexo.getAnexo().getActivoCol7());
								anexoJSON += "\",\"actCol8\":\"" +
								(filaAnexo.getAnexo().getActivoCol8()==null?"0":filaAnexo.getAnexo().getActivoCol8());
								anexoJSON += "\",\"actCol9\":\"" +
								(filaAnexo.getAnexo().getActivoCol9()==null?"0":filaAnexo.getAnexo().getActivoCol9());
								anexoJSON += "\",\"actCol10\":\"" +
								(filaAnexo.getAnexo().getActivoCol10()==null?"0":filaAnexo.getAnexo().getActivoCol10());

								anexoJSON += "\",\"cabcolumna1\":\"" +
								(ocabAnexo.getColumna1()==null?"":ocabAnexo.getColumna1());
								anexoJSON += "\",\"cabcolumna2\":\"" +
								(ocabAnexo.getColumna2()==null?"":ocabAnexo.getColumna2());
								anexoJSON += "\",\"cabcolumna3\":\"" +
								(ocabAnexo.getColumna3()==null?"":ocabAnexo.getColumna3());
								anexoJSON += "\",\"cabcolumna4\":\"" +
								(ocabAnexo.getColumna4()==null?"":ocabAnexo.getColumna4());
								anexoJSON += "\",\"cabcolumna5\":\"" +
								(ocabAnexo.getColumna5()==null?"":ocabAnexo.getColumna5());
								anexoJSON += "\",\"cabcolumna6\":\"" +
								(ocabAnexo.getColumna6()==null?"":ocabAnexo.getColumna6());
								anexoJSON += "\",\"cabcolumna7\":\"" +
								(ocabAnexo.getColumna7()==null?"":ocabAnexo.getColumna7());
								anexoJSON += "\",\"cabcolumna8\":\"" +
								(ocabAnexo.getColumna8()==null?"":ocabAnexo.getColumna8());
								anexoJSON += "\",\"cabcolumna9\":\"" +
								(ocabAnexo.getColumna9()==null?"":ocabAnexo.getColumna9());
								anexoJSON += "\",\"cabcolumna10\":\"" +
								(ocabAnexo.getColumna10()==null?"":ocabAnexo.getColumna10());

					anexoJSON += "\"}";
					break;
				}
				
				
				
				
			}
			logger.info("JSON="+anexoJSON.toString());
			//System.out.println("JSON="+anexoJSON.toString());
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(anexoJSON.toString());
			
		} catch (NumberFormatException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} 
	}
	
	
	
	public String findIdOperacion(String descripcion){
		String id= "";
		listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
		for(Tabla operacion : listaOperaciones){
			if(operacion.getDescripcion().toUpperCase().equals(descripcion == null?"":descripcion.toUpperCase())){
				id = operacion.getId().toString(); 
			}
		}
		return id;
	}
	
	//ini MCG20130708
	public String findIdBancoEdit(String descripcion){
		String id= "";
		listaBancos = (List<Tabla>)getObjectSession("listaBancos");
		for(Tabla obancos : listaBancos){
			if(obancos.getDescripcion().toUpperCase().equals(descripcion == null?"":descripcion.toUpperCase())){
				id = obancos.getId().toString(); 
			}
		}
		return id;
	}
	public String findIdEmpresaEdit(String descripcion){
		String id= "";
		listaEmpresas = (List<Empresa>)getObjectSession("listaEmpresas");
		for(Empresa oempresa : listaEmpresas){
			if(oempresa.getNombre().trim().toUpperCase().equals(descripcion == null?"":descripcion.trim().toUpperCase())){
				id = oempresa.getId().toString(); 
			}
		}
		return id;
	}
	
	public String findIdAccionistaEdit(String descripcion){
		String id= "";		
		try {
				
			listaAccionistaAnexo = (List<Accionista>)getObjectSession("listaAccionistaAnexo");
			for(Accionista oaccionista : listaAccionistaAnexo){
				if(oaccionista.getNombre().trim().toUpperCase().equals(descripcion == null?"":descripcion.trim().toUpperCase())){
					id = oaccionista.getId()==null?"":oaccionista.getId().toString();
				}
			}			
			return id;	
		} catch (Exception e) {			
			return id;	
		}
		
	}
	//fin MCG20130708
	
	public String findIdOperacion2(String descripcion){
		String id= "";		
		listaLimites	= (List<Tabla>)getObjectSession("listaLimites");
		for(Tabla operacion2 : listaLimites){
			if(operacion2.getDescripcion().toUpperCase().equals(descripcion == null?"":descripcion.toUpperCase())){
				id = operacion2.getId().toString(); 
			}
		}
		return id;
	}
	
	public String eliminar(){
		String forward = "anexos";
		String codigoDocumento=getCodigoArchivo();
		String extencionDocumento= getExtension();
		String nombreArchivo=codigoDocumento + "." + extencionDocumento; 	
		File file = null;
		try {
			Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa programa = new Programa();
			programa.setId(idPrograma);
			ArchivoAnexo archivoAnexo = new ArchivoAnexo();
			archivoAnexo.setId(Long.valueOf(codigoDocumento));
			archivoAnexoBO.delete(archivoAnexo);
			String dir  = getObjectParamtrosSession(Constantes.DIR_FILES_ANEXOS).toString();
			String pathToFile=dir+File.separator+nombreArchivo;
			file = new File(pathToFile);
			file.delete();
			addActionMessage("Eliminado Correctamente");
			setListaArchivosAnexo(archivoAnexoBO.findListaArchivos(programa));
			setObjectSession("listaArchivosAnexos", getListaArchivosAnexo());
			cargarAnexos(idPrograma);
		}catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		}
		return forward;
	}
	
	//ini MCG 20121119
	public void loadDetalleSaldo() {
		List<SaldoCliente> listaSaldoCliente = new ArrayList<SaldoCliente>();
		List<SaldoCliente> listaSaldoClienteres = new ArrayList<SaldoCliente>();
		try {
			String posicion=getPosAnexo();
			logger.info("Posicion:"+ getPosAnexo());
			Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(programaId));
			String tipoempresa=oprograma.getTipoEmpresa().getId().toString();
			String codCliente="";
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			
			listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");
			
			Integer tipoFila=2;
			 List<Empresa> oListaEmpresa= new ArrayList<Empresa>();
			tipoFila=anexoBO.ObtenerTipoFilaPadre(listaFilaAnexos,posicion);
			if (tipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){				
				codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicion,listaAccionistaAnexo);
			}else{
				if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){					
					codCliente=oprograma.getIdEmpresa();
				}else{
					 oListaEmpresa=empresaBO.listarEmpresasPorPrograma(programaId);
					codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicion,listaAccionistaAnexo);
				}
			}
				listaSaldoCliente = anexoBO.findListaSaldoCliente(codCliente);
				
				String cadenacontratos="";
				String strcontratostotal="";
				for (FilaAnexo ofilaAnexo:listaFilaAnexos){
					if (ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)||
							ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)
							//||ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)
							){					
					     
						if (ofilaAnexo.getAnexo().getContrato()!=null 
								&& ofilaAnexo.getAnexo().getContrato().length()>0){
							String strcontratos =ofilaAnexo.getAnexo().getContrato();						
							strcontratostotal=strcontratostotal+strcontratos+",";
																		
						}
						
					}
				}
				List<SaldoCliente> listaSaldoClientedel= new ArrayList<SaldoCliente>();
				if (strcontratostotal.length()>0){
					String[] arrayContratos = strcontratostotal.split(",");
					
					boolean flag= false;
					for(SaldoCliente osaldoc : listaSaldoCliente ){
						for(int t = 0; t < arrayContratos.length; t++){
							if(arrayContratos[t]!=null && arrayContratos[t].toString().equals(osaldoc.getContrato())){							
								
								flag=true;
							}
						}
						if(!flag){
							listaSaldoClientedel.add(osaldoc);
						}
						flag= false;
					}
					listaSaldoClienteres=listaSaldoClientedel;
				} else{
					listaSaldoClienteres=listaSaldoCliente;
				}


			retornarTablaSaldos(listaSaldoClienteres);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//ini MCG 20130813
	public void loadDetalleSaldoSublimite() {
		List<SaldoCliente> listaSaldoCliente = new ArrayList<SaldoCliente>();
		List<SaldoCliente> listaSaldoClienteres = new ArrayList<SaldoCliente>();
		try {
			String posicion=getPosAnexo();
			logger.info("Posicion:"+ getPosAnexo());
			Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(programaId));
			String tipoempresa=oprograma.getTipoEmpresa().getId().toString();
			String codCliente="";
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			listaAccionistaAnexo=(List<Accionista>)getObjectSession("listaAccionistaAnexo");
			
			Integer tipoFila=2;
			 List<Empresa> oListaEmpresa= new ArrayList<Empresa>();
			tipoFila=anexoBO.ObtenerTipoFilaPadre(listaFilaAnexos,posicion);
			if (tipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){				
				codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicion,listaAccionistaAnexo);
			}else{
				
				if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){					
					codCliente=oprograma.getIdEmpresa();
				}else{
					oListaEmpresa=empresaBO.listarEmpresasPorPrograma(programaId);
					codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicion,listaAccionistaAnexo);
				}
			}
			
			listaSaldoCliente = anexoBO.findListaSaldoCliente(codCliente);				
				
			String strcontratostotal="";
			strcontratostotal=anexoBO.ObtenerContratosLimitePadre(listaFilaAnexos, posicion);
			
			
			String strcontratostotalasignados="";
			for (FilaAnexo ofilaAnexo:listaFilaAnexos){
				if (ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){					
				    
					if (ofilaAnexo.getAnexo().getContrato()!=null 
							&& ofilaAnexo.getAnexo().getContrato().length()>0){
						String strcontratos =ofilaAnexo.getAnexo().getContrato();						
						strcontratostotalasignados=strcontratostotalasignados+strcontratos+",";
																	
					}
				}
			}

				List<SaldoCliente> listaSaldoClienteSublimitebusq= new ArrayList<SaldoCliente>();
				List<SaldoCliente> listaSaldoClienteSublimite = new ArrayList<SaldoCliente>();
				if (strcontratostotal.length()>0){
					String[] arrayContratos = strcontratostotal.split(",");				
					for(SaldoCliente osaldoc : listaSaldoCliente ){
						for(int t = 0; t < arrayContratos.length; t++){
							if(arrayContratos[t]!=null && arrayContratos[t].toString().equals(osaldoc.getContrato())){							
								listaSaldoClienteSublimitebusq.add(osaldoc);								
							}
						}		
					}
					listaSaldoClienteSublimite=listaSaldoClienteSublimitebusq;
				} else{
					listaSaldoClienteSublimite=listaSaldoClienteSublimitebusq;
				}
				
				
				List<SaldoCliente> listaSaldoClientesinAsig= new ArrayList<SaldoCliente>();
				if (strcontratostotalasignados.length()>0){
					String[] arrayContratosA = strcontratostotalasignados.split(",");
					
					boolean flag= false;
					for(SaldoCliente osaldoc : listaSaldoClienteSublimite ){
						for(int t = 0; t < arrayContratosA.length; t++){
							if(arrayContratosA[t]!=null && arrayContratosA[t].toString().equals(osaldoc.getContrato())){							
								
								flag=true;
							}
						}
						if(!flag){
							listaSaldoClientesinAsig.add(osaldoc);
						}
						flag= false;
					}
					listaSaldoClienteres=listaSaldoClientesinAsig;
				} else{
					listaSaldoClienteres=listaSaldoClienteSublimite;
				}

			retornarTablaSaldos(listaSaldoClienteres);
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
	}
	
	//fin MCG201430813
	
	/**
	 * Genera Tabla de saldos
	 */
	public void retornarTablaSaldos(List<SaldoCliente> olistaSaldoCliente){
		PrintWriter out=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		try {
			
			String strTipocambio="";
			String strFechaTipoCambio="";
			if (olistaSaldoCliente!=null && olistaSaldoCliente.size()>0){
				SaldoCliente osaldoCliente=olistaSaldoCliente.get(0);
				strTipocambio=osaldoCliente.getTipocambio()==null?"":osaldoCliente.getTipocambio();
				strFechaTipoCambio=osaldoCliente.getFechaTipocambio()==null?"":osaldoCliente.getFechaTipocambio();
				if (strFechaTipoCambio.equals("DD/MM/YYYY")){
					strFechaTipoCambio="";
				}
			}
	    
			
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        stb.append("<table  id=\"tbRemsaldo\">");
			stb.append("<thead>");	
			    stb.append("<tr>");
			        stb.append("<th align=\"right\" colspan=\"7\">TC: "+strTipocambio+" / Fecha: "+strFechaTipoCambio+" </th>");
			        			   			       
		        stb.append("</tr>");
		        stb.append("<tr>");
			        stb.append("<th>#</th>");
			        stb.append("<th>Elija</th>");
			        stb.append("<th>Contrato</th>");
			        stb.append("<th>Saldo</th>");
			        stb.append("<th>Fecha Vencimiento</th>");
			        stb.append("<th>Prestamo DL</th>");
			        stb.append("<th></th>");
		        stb.append("</tr>");
	        stb.append("</thead>");
        stb.append("<tbody>");
        String creditoFlag="0";
		for(SaldoCliente osaldoCliente: olistaSaldoCliente){
			contador++;
			String contrato=osaldoCliente.getContrato()==null?"":osaldoCliente.getContrato().toString();
			String saldo=osaldoCliente.getSaldodeudolocal()==null?"":osaldoCliente.getSaldodeudolocal().toString();
			String fechaVencimiento=osaldoCliente.getFechaVencimiento()==null?"":osaldoCliente.getFechaVencimiento().toString();
			String lcredito=osaldoCliente.getIndfisca()==null?"":osaldoCliente.getIndfisca().toString();
			if (osaldoCliente.getIndfisca()!=null && osaldoCliente.getIndfisca().equals("DL")){
				creditoFlag="1";
			}else{
				creditoFlag="0";
			}
			

			stb.append("<tr>");
			stb.append("<td>"+contador+"</td>");
			stb.append("<td><input type=\"checkbox\" name=\"contratocheck\" id=\"idElijacontrato\" value="+ contrato+ " class=\"ui-button ui-widget ui-state-default ui-corner-all\"/></td>");
			//stb.append("<td><s:checkbox  cssClass=\"case\" id=\"check\" name=\"placacheck\" fieldValue="+contrato+ " theme=\"simple\"/></td>") ;
			
			stb.append("<td>"+(contrato)+"</td>");
			stb.append("<td align=\"right\">"+(saldo)+"</td>");
			stb.append("<td align=\"center\">"+(fechaVencimiento)+"</td>");			
			stb.append("<td align=\"center\">"+(lcredito)+"</td>");	
			stb.append("<td><input type=\"hidden\" name=\"creditohidden\" id=\"idcreditohidden\" value="+ creditoFlag+ " class=\"clscredito\"/></td>");
			
			stb.append("</tr>");

		}
		stb.append("</tbody>");
		 stb.append("</table>");
		
        out.print(stb.toString());
		
		
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
		
	}
	
	public String updatesaldo(){
		List<SaldoCliente> listaSaldoCliente = new ArrayList<SaldoCliente>();
			try {
				logger.info("editAnexo="+editAnexo);
				logger.info("posAnexo="+posAnexo);
				logger.info("contratos seleccionados="+addcontratos);	
				String posicionAnexo=getPosAnexo();
				Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
				Programa oprograma=new Programa();
				oprograma=programaBO.findById(Long.valueOf(programaId));
				String codCliente="";//oprograma.getIdEmpresa();
				String tipoempresa=oprograma.getTipoEmpresa().getId().toString();
				if(getObjectSession("listaFilaAnexosSession") != null){
					listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
				}
				if(getObjectSession("listaFilaAnexosSession") != null){
					listaAccionistaAnexo = (List<Accionista>)getObjectSession("listaAccionistaAnexo");
				}
				
				Integer tipoFila=2;
				 List<Empresa> oListaEmpresa= new ArrayList<Empresa>();
				tipoFila=anexoBO.ObtenerTipoFilaPadre(listaFilaAnexos,posicionAnexo);
				if (tipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){				
					codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicionAnexo,listaAccionistaAnexo);
				}else{
					
					if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){					
						codCliente=oprograma.getIdEmpresa();
					}else{
						 oListaEmpresa=empresaBO.listarEmpresasPorPrograma(programaId);
						codCliente=anexoBO.ObtenerCodigoEmpresa(listaFilaAnexos, oListaEmpresa, posicionAnexo,listaAccionistaAnexo);
					}
				}
				listaSaldoCliente = anexoBO.findListaSaldoCliente(codCliente);
				String strcontratos = getAddcontratos();
				float subtotalsaldo = 0;
				boolean flagsaldo=false;
				if (strcontratos.length()>0){
					flagsaldo=true;
					String[] arrayContratos = strcontratos.split(",");	
					for ( SaldoCliente osaldo:listaSaldoCliente){
						for (int t = 0; t < arrayContratos.length; t++) {
							if (arrayContratos[t].toString().equals(osaldo.getContrato())){
								subtotalsaldo+=osaldo.getSaldodeudolocal()==null?0:Float.valueOf(osaldo.getSaldodeudolocal().replace(",", ""));
							}
						}
					}
					
					String valor = FormatUtil.conversion(FormatUtil.round(subtotalsaldo,2));
	
					
					int posicion =0;
					boolean flaglimpiahijos=false;
					for(FilaAnexo item: listaFilaAnexos){
						if(item.getPos() == Integer.valueOf(posAnexo)){
							if (item.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)){
								flaglimpiahijos=true;
							}							
							if (item.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){								
								item.getAnexo().setLteAutorizado(valor);
								item.getAnexo().setLteForm(valor);								
							}
							item.getAnexo().setRgoActual(valor);
							item.getAnexo().setContrato(strcontratos);
							listaFilaAnexos.set(posicion, item);
							break;
						}
						posicion++;
					}
					limpirHijosLimite(listaFilaAnexos,posicionAnexo,flaglimpiahijos);				
					
				}else{
					if(getObjectSession("listaFilaAnexosSession") != null){
						listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
					}
				}				
					
				actualizarPosicionesListaAnexos();
				setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
				
				
			} catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
			}
			
			
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			listaLimites	= (List<Tabla>)getObjectSession("listaLimites");
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo = (List<Accionista>)getObjectSession("listaAccionistaAnexo");
			
			calcularMontos(listaFilaAnexos,true);
			setFlagAnexo1(true);
			return "anexos";
		}
	
	public String recalcularSaldo(){
		List<SaldoCliente> listaSaldoCliente = new ArrayList<SaldoCliente>();
		List<SaldoCliente> listaSaldoClientetmp = new ArrayList<SaldoCliente>();
			try {
						
				Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
				Programa oprograma=new Programa();
				oprograma=programaBO.findById(Long.valueOf(programaId));
				String codCliente="";
								
				String tipoempresa=oprograma.getTipoEmpresa().getId().toString();
				
				if(getObjectSession("listaFilaAnexosSession") != null){
					listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
				}
				
				
				if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){					
					codCliente=oprograma.getIdEmpresa();
					listaSaldoCliente = anexoBO.findListaSaldoCliente(codCliente);
					
				}else{
					 List<Empresa> oListaEmpresa=empresaBO.listarEmpresasPorPrograma(programaId);
					 for (Empresa oempresa: oListaEmpresa){						 
						 codCliente= oempresa.getCodigo();
						 listaSaldoClientetmp = new ArrayList<SaldoCliente>();
						 listaSaldoClientetmp = anexoBO.findListaSaldoCliente(codCliente);
						 listaSaldoCliente.addAll(listaSaldoClientetmp);
					 }	
					 
				}
				
				 List<Accionista> oListaAccionista=datosBasicosBO.getListaAccionistas(oprograma);
				 if (oListaAccionista!=null && oListaAccionista.size()>0){
					 for (Accionista oaccionista: oListaAccionista){
						 if (oaccionista.getCodigoCentral()!=null && !oaccionista.getCodigoCentral().equals("")){
							 codCliente= oaccionista.getCodigoCentral();
							 listaSaldoClientetmp = new ArrayList<SaldoCliente>();
							 listaSaldoClientetmp = anexoBO.findListaSaldoCliente(codCliente);
							 listaSaldoCliente.addAll(listaSaldoClientetmp);
						 }
					 }
				 }	
				
				String strcontratos = getAddcontratos();
				float subtotalsaldo = 0;
	
				
				for(FilaAnexo item: listaFilaAnexos){
					strcontratos=item.getAnexo().getContrato();
						if (strcontratos!=null && strcontratos.length()>0){							
							String[] arrayContratos = strcontratos.split(",");	
							subtotalsaldo=0;							
							for (int t = 0; t < arrayContratos.length; t++) {
								for ( SaldoCliente osaldo:listaSaldoCliente){	
									if (arrayContratos[t].toString().equals(osaldo.getContrato())){
										subtotalsaldo+=osaldo.getSaldodeudolocal()==null?0:Float.valueOf(osaldo.getSaldodeudolocal().replace(",", ""));
									}									
								}
							}
							
							String valor = FormatUtil.conversion(FormatUtil.round(subtotalsaldo,2));
							if (item.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){

								item.getAnexo().setLteAutorizado(valor);
								item.getAnexo().setLteForm(valor);
							}
							
							item.getAnexo().setRgoActual(valor);								
						}
				}					

				actualizarPosicionesListaAnexos();
				setObjectSession("listaFilaAnexosSession", listaFilaAnexos);
				
				
			} catch (Exception e) {
				addActionError(e.getMessage());
				logger.error(StringUtil.getStackTrace(e));
			}
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo=((List<Accionista>)getObjectSession("listaAccionistaAnexo"));

			calcularMontos(listaFilaAnexos,true);
			setFlagAnexo1(true);
			return "anexos";
		}
	//fin MCG 20121119
	
	
	public String agregarGarantia(){
		
		listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");

		listaBancos = (List<Tabla>)getObjectSession("listaBancos");
		listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
		listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
		setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
		setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
		listaAccionistaAnexo=((List<Accionista>)getObjectSession("listaAccionistaAnexo"));

		calcularMontos(listaFilaAnexos,false);
		
		if(getListaGarantiaAnexo()==null){
				setListaGarantiaAnexo(new ArrayList<AnexoGarantia>());
		}
		AnexoGarantia onewAnexoGarantia=new AnexoGarantia(listaGarantiaAnexo.size()+1);
		onewAnexoGarantia.setFlagSincro("NO");
		getListaGarantiaAnexo().add(onewAnexoGarantia);
		return "anexos";
	}
	public String eliminarGarantia(){
		try{
			
			listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
			listaBancos = (List<Tabla>)getObjectSession("listaBancos");
			listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
			listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
			setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
			setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
			listaAccionistaAnexo=((List<Accionista>)getObjectSession("listaAccionistaAnexo"));
			calcularMontos(listaFilaAnexos,false);
			
			int eliminar=0;
			Integer codigoGarantia=Integer.parseInt(indiceGarantia);	
			for(AnexoGarantia aux:listaGarantiaAnexo){				
				if(aux.getCodigoGarantiaAnexo().equals(codigoGarantia)){
					break;
				}
				eliminar++;
			}
			listaGarantiaAnexo.remove(eliminar);
			indiceGarantia="";
		
		}catch (Exception e) {
			addActionError(e.getMessage());
		}
		return "anexos";
	}
	
	public String sincronizarGarantia(){
		List<GarantiaHost> listGarantiaHost = new ArrayList<GarantiaHost>();	
		List<GarantiaHost> listGarantiaHosttmp = new ArrayList<GarantiaHost>();
		
		try{
			Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa oprograma=new Programa();
			oprograma=programaBO.findById(Long.valueOf(programaId));
			String codCliente="";							
			String tipoempresa=oprograma.getTipoEmpresa().getId().toString();
			
	
			
			if(getListaGarantiaAnexo()==null){
					setListaGarantiaAnexo(new ArrayList<AnexoGarantia>());
			}
			getListaGarantiaAnexo().add(new AnexoGarantia(listaGarantiaAnexo.size()));
			
			List<Tabla> olistaPlantillaTipoGarantia= tablaBO.obtieneHijaCodigoPadre(Constantes.COD_TIPOGARANTIA_CRGA);
			
			if(tipoempresa.equals(Constantes.ID_TIPO_EMPRESA_EMPR.toString())){	
				codCliente=oprograma.getIdEmpresa();
				Empresa empresa= new Empresa();	
				empresa=programaBO.findEmpresaByIdEmpresaPrograma(oprograma.getId(),codCliente);				
				listGarantiaHost = anexoBO.findListaGarantiaHost(empresa);
				
			}else{
				 List<Empresa> oListaEmpresa=empresaBO.listarEmpresasPorPrograma(programaId);
				 for (Empresa oempresa: oListaEmpresa){						 
					 codCliente= oempresa.getCodigo();		 
					 
					 listGarantiaHosttmp = new ArrayList<GarantiaHost>();
					 listGarantiaHosttmp = anexoBO.findListaGarantiaHost(oempresa);
					 listGarantiaHost.addAll(listGarantiaHosttmp);
				 }	
				 
			}
			listaGarantiaAnexo.clear();
			setListaGarantiaAnexo(getAsignarAnexoGarantia(listGarantiaHost,olistaPlantillaTipoGarantia));
			
			
		} catch (Exception e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
	
		listaFilaAnexos = (List<FilaAnexo>)getObjectSession("listaFilaAnexosSession");
	
		listaBancos = (List<Tabla>)getObjectSession("listaBancos");
		listaEmpresas =	(List<Empresa>)getObjectSession("listaEmpresas");
		listaOperaciones = (List<Tabla>)getObjectSession("listaOperaciones");
		setListaLimites((List<Tabla>)getObjectSession("listaLimites"));			
		setListaArchivosAnexo((List<ArchivoAnexo>)getObjectSession("listaArchivosAnexos"));
		listaAccionistaAnexo=((List<Accionista>)getObjectSession("listaAccionistaAnexo"));
	
		calcularMontos(listaFilaAnexos,false);	
		
		return "anexos";
	}
	
	private List<AnexoGarantia> getAsignarAnexoGarantia(List<GarantiaHost> olistGarantiaHost,List<Tabla> olistaPlantillaTipoGarantia ){
		 List<AnexoGarantia> olistaAnexoGarantia=new ArrayList<AnexoGarantia>();
		if (olistGarantiaHost!=null && olistGarantiaHost.size()>0){
			int cont=0;
			for(GarantiaHost ogarantiaHost:olistGarantiaHost){
				cont=cont+1;
				AnexoGarantia oanexoGarantia=new AnexoGarantia();
				oanexoGarantia.setCodigoGarantiaAnexo(cont);
				oanexoGarantia.setNumeroGarantia(ogarantiaHost.getNumeroGarantia());
				oanexoGarantia.setCodEmpresaGrupo(ogarantiaHost.getCodCentral());
				oanexoGarantia.setEmpresa(ogarantiaHost.getEmpresa());
				oanexoGarantia.setTipoGarantia(ogarantiaHost.getTipoGarantia());
				if (ogarantiaHost.getImporte().equals(".00")) {
					ogarantiaHost.setImporte("0.00");
				}
				String valorImporte = ogarantiaHost.getImporte().equals("")?"0":ogarantiaHost.getImporte();
				valorImporte = FormatUtil.roundTwoDecimalsPunto(Double.parseDouble(FormatUtil.FormatNumeroSinComa(valorImporte)));				
				ogarantiaHost.setImporte(valorImporte);

				oanexoGarantia.setFlagSincro("SI");
				oanexoGarantia.setImporte(ogarantiaHost.getImporte());
				oanexoGarantia.setDescripcionGarantia(getDescripcionByTipoGarantia(ogarantiaHost,olistaPlantillaTipoGarantia));
				olistaAnexoGarantia.add(oanexoGarantia);
			}
			Collections.sort(olistaAnexoGarantia, AnexoGarantia.GarantiaComparator);
		}
		
		return olistaAnexoGarantia;
	}
	
	private String getDescripcionByTipoGarantia(GarantiaHost ogarantiaHost,List<Tabla> olistaPlantillaTipoGarantia){
		String descripcion="";
		try {			
			 
		
			 String strplantilla="";
			
				 if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_HIPOTECA_LOCAL)){
					strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_HIPOTECA_CRGA);
					strplantilla=strplantilla.replace("<<subtipo>>",ogarantiaHost.getHisubtipo());
				    strplantilla=strplantilla.replace("<<importe>>",ogarantiaHost.getImporte());
				    strplantilla=strplantilla.replace("<<rango>>",ogarantiaHost.getHirango());
				    strplantilla=strplantilla.replace("<<fecha_de_tasacion>>",ogarantiaHost.getHifechaTasacion());
				    strplantilla=strplantilla.replace("<<tasador>>",ogarantiaHost.getHitasador());
				    strplantilla=strplantilla.replace("<<tasacion_monto>>",ogarantiaHost.getHitasacionMonto());
				    strplantilla=strplantilla.replace("<<direccion_de_propietario>>",ogarantiaHost.getHidireccionPropietario());
				    strplantilla=strplantilla.replace("<<valor_de_realizacion>>",ogarantiaHost.getHivalorRealizacion());
				    strplantilla=strplantilla.replace("<<nombre>>",ogarantiaHost.getHinombre());
				    strplantilla=strplantilla.replace("<<clase>>",ogarantiaHost.getHiclase());				 
					 
				 }else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_DEPOSITOAPLAZO_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_DEPOSITOAPLAZO_CRGA);
				 	strplantilla=strplantilla.replace("<<Importe>>",ogarantiaHost.getImporte());
				 	strplantilla=strplantilla.replace("<<numero_de_deposito>>",ogarantiaHost.getDanumeroDeposito());
				 	strplantilla=strplantilla.replace("<<glosa>>",ogarantiaHost.getDaglosa());
				 	strplantilla=strplantilla.replace("<<operacion>>",ogarantiaHost.getDaoperacion());
			 		
				}else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_CUENTAGARANTIA_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_CUENTAGARANTIA_CRGA);
				 	strplantilla=strplantilla.replace("<<Importe>>",ogarantiaHost.getImporte());
				 	strplantilla=strplantilla.replace("<<numero_de_deposito>>",ogarantiaHost.getCgnumeroDeposito());
				 	strplantilla=strplantilla.replace("<<glosa>>",ogarantiaHost.getCgglosa());
				 	strplantilla=strplantilla.replace("<<operacion>>",ogarantiaHost.getCgoperacion());
			 		
				}else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_FIANZASOLIDARIA_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_FIANZASOLIDARIA_CRGA);
				 	strplantilla=strplantilla.replace("<<Operacion>>",ogarantiaHost.getFsoperacion());
				 	strplantilla=strplantilla.replace("<<moneda>>",ogarantiaHost.getFsmoneda());
				 	strplantilla=strplantilla.replace("<<importe>>",ogarantiaHost.getImporte());
				 	strplantilla=strplantilla.replace("<<fiador>>",ogarantiaHost.getFsfiador());
				 	strplantilla=strplantilla.replace("<<documento_de_identidad>>",ogarantiaHost.getFsdocumentoIdentidad());
				 	strplantilla=strplantilla.replace("<<glosa>>",ogarantiaHost.getFsglosa());
			 		
				}else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_FONDOMUTUOS_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_FONDOMUTUO_CRGA);				 	
				 	strplantilla=strplantilla.replace("<<cuentaFM>>",ogarantiaHost.getFmctaPar());
				 	strplantilla=strplantilla.replace("<<cuotaFM>>",ogarantiaHost.getFmcuotas());
				 	strplantilla=strplantilla.replace("<<moneda>>",ogarantiaHost.getFmmoneda());
				 	strplantilla=strplantilla.replace("<<valorCotizado>>",ogarantiaHost.getFmvalorCotizacion());
				 	strplantilla=strplantilla.replace("<<fechaCotizacion>>",ogarantiaHost.getFmfechaCotizacion());
				 	strplantilla=strplantilla.replace("<<fechaLiberacion>>",ogarantiaHost.getFmfechaLiberacion());				 	
			 		
				}else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_WARRANT_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_WARRANT_CRGA);
				 	strplantilla=strplantilla.replace("<<Tipo_de_bien>>",ogarantiaHost.getWatipoBien());
				 	strplantilla=strplantilla.replace("<<descripcion>>",ogarantiaHost.getWadescripcion());
				 	strplantilla=strplantilla.replace("<<cantidad>>",ogarantiaHost.getWacantidad());
				 	strplantilla=strplantilla.replace("<<subtipo>>",ogarantiaHost.getWasubtipo());
				 	strplantilla=strplantilla.replace("<<fecha_de_vencimiento>>",ogarantiaHost.getWafechaVencimiento());				 
				}else if (ogarantiaHost.getCodGarantia().equals(Constantes.COD_STANDBY_LOCAL)){
				 	strplantilla=obtenerDesPlanillaTipoGarantia(olistaPlantillaTipoGarantia,Constantes.COD_TG_STANDBY_CRGA);
				 	strplantilla=strplantilla.replace("<<numeroCartaCredito>>",ogarantiaHost.getSbcartCred());
				 	strplantilla=strplantilla.replace("<<emisor>>",ogarantiaHost.getSbemisor());
				 	strplantilla=strplantilla.replace("<<cuidad>>",ogarantiaHost.getSbciudad());
				 	strplantilla=strplantilla.replace("<<montoCarta>>",ogarantiaHost.getSbcarta());
				 	strplantilla=strplantilla.replace("<<fechaVencimiento>>",ogarantiaHost.getSbvcto());
				 	strplantilla=strplantilla.replace("<<fechaRenovacion>>",ogarantiaHost.getSbrenovacion());
				 	strplantilla=strplantilla.replace("<<observaciones>>",ogarantiaHost.getSbobservacion());
			 							 
				}				 
				 descripcion=strplantilla;
				
		} catch (Exception e) {
			descripcion="";
		}		
		return descripcion;		
	}
	
	private String obtenerDesPlanillaTipoGarantia(List<Tabla> olistaTipoGarantia,String CodigoTipoGarantia){
		String descplanilla="";
		if (olistaTipoGarantia!=null && olistaTipoGarantia.size()>0){
		 for (Tabla otipoGarantia:olistaTipoGarantia){
			 if (otipoGarantia.getCodigo().equals(CodigoTipoGarantia)){
				 descplanilla=otipoGarantia.getDescripcion();
			 }
		 }
		}
		return descplanilla;
	}

	
	public void obtenerRatingAnexo(){
		String forward = "anexos";
		String anexoJSON = "";
		try {
			Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa oprograma=new Programa();
			oprograma.setId(programaId);			
			Empresa empresa = findEmpresa(getCodempresaRating(),programaId);
			String strcodempresa="";
			String strRuc="";
			if(empresa!=null){ 
				strcodempresa=empresa.getCodigo();
				strRuc=empresa.getRuc();
			}else{
				strcodempresa="";
				strRuc="";
			};			
			
			
			String orating ="";
			String strBureau="";
			String ofecharating ="";			
				try {	
					Map<String,Object> oratingFecha= ratingBO.ObtenerRatingConFecha(oprograma,strcodempresa);
					 orating = oratingFecha.get("mratingAnexo")==null?"":oratingFecha.get("mratingAnexo").toString();
					 ofecharating=oratingFecha.get("mfechagAnexo")==null?"":oratingFecha.get("mfechagAnexo").toString();
					 
										
				} catch (Exception e) {
					 
				}				
				strBureau=obtenerBureauBD(oprograma,strcodempresa);
			
			
					anexoJSON = "{\"rating\":\"" +
								    orating +
								"\",\"codempresar\":\"" +
								strcodempresa +
								"\",\"fecharating\":\"" +
								ofecharating +
								"\",\"bureau\":\"" +
								strBureau +
								"\"}"; 
					
			logger.info("JSON="+anexoJSON.toString());
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(anexoJSON.toString());
			
		} catch (NumberFormatException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} 
	}
	
	public String obtenerBureauBD(Programa oprograma, String CodEmpresa){
		List<DatosBasico> listDatosBasicos;
		String buro="";
		try {
			listDatosBasicos = datosBasicosBO.getListaDatosBasico(oprograma,CodEmpresa);
			if (listDatosBasicos!=null && listDatosBasicos.size()>0 ){
				   DatosBasico odatosBasico =listDatosBasicos.get(0);
				   buro=odatosBasico.getGrupoRiesgoBuro()==null?"":odatosBasico.getGrupoRiesgoBuro().toString();
			   }			
		} catch (BOException e) {
			 
			e.printStackTrace();
		}	   
	
		return buro;
		
	}	
	
//ini MCG20140819
	public void obtenerBureauAnexo(){
		String forward = "anexos";
		String anexoJSON = "";
		try {
			Long  programaId = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());
			Programa oprograma=new Programa();
			oprograma.setId(programaId);			
			Empresa empresa = findEmpresa(getCodempresaRating(),programaId);			
			String strRuc="";
			if(empresa!=null){ 				
				strRuc=empresa.getRuc();
			}else{				
				strRuc="";
			};				
			String strBureau="";
			String tipDocRuc = GenericAction.getObjectParamtrosSession(Constantes.EQUIV_RUC).toString();
			strBureau=obtenerBureauWS(strRuc,tipDocRuc);
					anexoJSON = "{\"bureau\":\"" +
									strBureau +
								"\"}"; 
					
			logger.info("JSON="+anexoJSON.toString());
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(anexoJSON.toString());
			
		} catch (NumberFormatException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} 
	}
//fin MCG20140819
//ini CCC17112014
	public void obtenerBuroAccionistaAnexo(){
		String forward = "anexos";
		String anexoJSON = "";
		try {
			Accionista accionista = findAccionista(getCodempresaAccionista());
			String strTipoNumeroDocumento="";
			if(accionista!=null){ 				
				strTipoNumeroDocumento=accionista.getTipoNumeroDocumentoHost();
			}else{				
				strTipoNumeroDocumento="";
			};				
			String strBureau="";
			String tipoDocumento = "";
			String numeroDocumento ="";
			if(strTipoNumeroDocumento!=null){
				StringTokenizer tokens=new StringTokenizer(strTipoNumeroDocumento,"|");
				tipoDocumento = tokens.nextToken();
				numeroDocumento = tokens.nextToken();
				tipoDocumento = obtieneEquivalenciaTipoDocumento(tipoDocumento);
			}
			
			strBureau=obtenerBureauWS(numeroDocumento.trim(),tipoDocumento.trim());
					anexoJSON = "{\"bureau\":\"" +
									strBureau +
								"\"}"; 
					
			logger.info("JSON="+anexoJSON.toString());
			getResponse().setContentType("text/html");   
            PrintWriter out = getResponse().getWriter(); 
            out.print(anexoJSON.toString());
			
		} catch (NumberFormatException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} catch (IOException e) {
			addActionError(e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
			forward = "anexos";
		} 
	}
	
	private String obtieneEquivalenciaTipoDocumento(String tipoDocumento){
		String tpdoc="";
		List<Tabla> listaTipoDocuEqui;
		try {
			listaTipoDocuEqui = tablaBO.listarHijos(Constantes.TABLA_EQUIVALENCIA_TIPDOC);
			for(Tabla tabla : listaTipoDocuEqui){
				if(tabla.getDescripcion().equals(tipoDocumento)){
					tpdoc=tabla.getAbreviado();
					break;
				}
			}
		} catch (BOException e) {
			tpdoc="";
			logger.error(StringUtil.getStackTrace(e));
		}	
		return tpdoc;
	}
	
	
//fin CCC17112014	
	
public String obtenerBureauWS(String documento,String tipoDocumento){	
		String bureau="";
		String buro;
		try {
			UsuarioSesion ousuario=(UsuarioSesion)getObjectSession(Constantes.USUARIO_SESSION);
			String pathWebService = getObjectParamtrosSession(Constantes.COD_ENLACE_BBVASOAP_ADDRESS).toString();
			buro = QueryWS.consultarGrupoBuro(documento,tipoDocumento,ousuario.getRegistroHost(),pathWebService);
			if(buro != null){
				bureau=buro;
			}
		} catch (WSException e) {
			bureau="";
		}
		return bureau;	
}
	
public void cargarComboAccionista() throws BOException  {
		
		PrintWriter out=null;
		List<Tabla> listTabla=null;
		int contador=0;
		StringBuilder stb = new StringBuilder();
		Programa oprograma=new Programa();
		try {
		Long  idPrograma = Long.valueOf(getObjectSession(Constantes.ID_PROGRAMA_SESSION).toString());	
		oprograma.setId(idPrograma);
		getResponse().setContentType("text/html");   
        out = getResponse().getWriter(); 
        
        String idEmpresa=getCodempresaAccionista(); 
        Empresa oempresa=new Empresa();
        oempresa=findEmpresa(idEmpresa,idPrograma);
        datosBasicosBO.setPrograma(oprograma);
        listaAccionistaAnexo = datosBasicosBO.getListaAccionistas(oempresa.getCodigo());
        stb.append("<option value=''>SELECCIONE</option>");
		for(Accionista oaccionista: listaAccionistaAnexo){
			stb.append("<option value='"+oaccionista.getId()+"'>"+oaccionista.getNombre()+"</option>");	
		}
		// ponemos en session
		this.setObjectSession("listaAccionistaAnexo",
				listaAccionistaAnexo);
		out.print(stb.toString());
		
		
		} catch (BOException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (IOException e) {
			logger.error(StringUtil.getStackTrace(e));
		}finally{
			if(out !=null) out.close();
		}
	}

	private void iniflagGrabar(){
		try {
			setObjectSession(Constantes.COD_FLAG_ACTIVO_ANEXO, "0");
		} catch (Exception e) {
			 
		}
	}
	private void finflagGrabar(){
		try {
			setObjectSession(Constantes.COD_FLAG_ACTIVO_ANEXO, "1");
		} catch (Exception e) {
			 
		}
	}
	
	public List<AnexoColumna> getListaColumnas() {
		return listaColumnas;
	}

	public void setListaColumnas(List<AnexoColumna> listaColumnas) {
		this.listaColumnas = listaColumnas;
	}

	public TablaBO getTablaBO() {
		return tablaBO;
	}

	public void setTablaBO(TablaBO tablaBO) {
		this.tablaBO = tablaBO;
	}

	public List<Tabla> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Tabla> listaBancos) {
		this.listaBancos = listaBancos;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public List<Tabla> getListaOperaciones() {
		return listaOperaciones;
	}

	public void setListaOperaciones(List<Tabla> listaOperaciones) {
		this.listaOperaciones = listaOperaciones;
	}

	public List<Anexo> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<Anexo> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public Integer getTipoFila() {
		return tipoFila;
	}

	public void setTipoFila(Integer tipoFila) {
		this.tipoFila = tipoFila;
	}

	public List<FilaAnexo> getListaFilaAnexos() {
		return listaFilaAnexos;
	}

	public void setListaFilaAnexos(List<FilaAnexo> listaFilaAnexos) {
		this.listaFilaAnexos = listaFilaAnexos;
	}



	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

//	public String getCodigoEmpresa() {
//		return codigoEmpresa;
//	}
//
//	public void setCodigoEmpresa(String codigoEmpresa) {
//		this.codigoEmpresa = codigoEmpresa;
//	}
	public String getIdEmpresaAnexo() {
		return idEmpresaAnexo;
	}

	public void setIdEmpresaAnexo(String idEmpresaAnexo) {
		this.idEmpresaAnexo = idEmpresaAnexo;
	}

	public String getOperacion() {
		return operacion;
	}



	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getNombreColumna() {
		return nombreColumna;
	}

	public void setNombreColumna(String nombreColumna) {
		this.nombreColumna = nombreColumna;
	}

	public List<AnexoColumna> getListaColumnasFormulario() {
		return listaColumnasFormulario;
	}

	public void setListaColumnasFormulario(
			List<AnexoColumna> listaColumnasFormulario) {
		this.listaColumnasFormulario = listaColumnasFormulario;
	}

	public AnexoBO getAnexoBO() {
		return anexoBO;
	}

	public void setAnexoBO(AnexoBO anexoBO) {
		this.anexoBO = anexoBO;
	}

	public FilaAnexo getFilaTotal() {
		return filaTotal;
	}

	public void setFilaTotal(FilaAnexo filaTotal) {
		this.filaTotal = filaTotal;
	}

	public static int getPos() {
		return pos;
	}

	public static void setPos(int pos) {
		AnexoAction.pos = pos;
	}

	public EmpresaBO getEmpresaBO() {
		return empresaBO;
	}

	public void setEmpresaBO(EmpresaBO empresaBO) {
		this.empresaBO = empresaBO;
	}

	public boolean isFlagAnexo1() {
		return flagAnexo1;
	}

	public void setFlagAnexo1(boolean flagAnexo1) {
		this.flagAnexo1 = flagAnexo1;
	}

	public boolean isFlagAnexo2() {
		return flagAnexo2;
	}

	public void setFlagAnexo2(boolean flagAnexo2) {
		this.flagAnexo2 = flagAnexo2;
	}

	public boolean isFlagAnexo3() {
		return flagAnexo3;
	}

	public void setFlagAnexo3(boolean flagAnexo3) {
		this.flagAnexo3 = flagAnexo3;
	}

	public File getFileAnexo() {
		return fileAnexo;
	}

	public void setFileAnexo(File fileAnexo) {
		this.fileAnexo = fileAnexo;
	}

	public String getFileAnexoFileName() {
		return fileAnexoFileName;
	}

	public void setFileAnexoFileName(String fileAnexoFileName) {
		this.fileAnexoFileName = fileAnexoFileName;
	}

	public List<ArchivoAnexo> getListaArchivosAnexo() {
		return listaArchivosAnexo;
	}

	public void setListaArchivosAnexo(List<ArchivoAnexo> listaArchivosAnexo) {
		this.listaArchivosAnexo = listaArchivosAnexo;
	}

	public String getCodigoArchivo() {
		return codigoArchivo;
	}

	public void setCodigoArchivo(String codigoArchivo) {
		this.codigoArchivo = codigoArchivo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public String getIdAnexo() {
		return idAnexo;
	}

	public void setIdAnexo(String idAnexo) {
		this.idAnexo = idAnexo;
	}

	public String getPosAnexo() {
		return posAnexo;
	}

	public void setPosAnexo(String posAnexo) {
		this.posAnexo = posAnexo;
	}

	public String getEditAnexo() {
		return editAnexo;
	}

	public void setEditAnexo(String editAnexo) {
		this.editAnexo = editAnexo;
	}

	public String getSubLimite() {
		return subLimite;
	}

	public void setSubLimite(String subLimite) {
		this.subLimite = subLimite;
	}

	public String getAddcontratos() {
		return addcontratos;
	}

	public void setAddcontratos(String addcontratos) {
		this.addcontratos = addcontratos;
	}

	public List<Tabla> getListaLimites() {
		return listaLimites;
	}

	public void setListaLimites(List<Tabla> listaLimites) {
		this.listaLimites = listaLimites;
	}

	public String getLimite() {
		return limite;
	}

	public void setLimite(String limite) {
		this.limite = limite;
	}

	public boolean isFlagAnexo4() {
		return flagAnexo4;
	}

	public void setFlagAnexo4(boolean flagAnexo4) {
		this.flagAnexo4 = flagAnexo4;
	}

	public List<AnexoGarantia> getListaGarantiaAnexo() {
		return listaGarantiaAnexo;
	}

	public void setListaGarantiaAnexo(List<AnexoGarantia> listaGarantiaAnexo) {
		this.listaGarantiaAnexo = listaGarantiaAnexo;
	}

	public String getIndiceGarantia() {
		return indiceGarantia;
	}

	public void setIndiceGarantia(String indiceGarantia) {
		this.indiceGarantia = indiceGarantia;
	}

	public AnexoGarantiaBO getAnexoGarantiaBO() {
		return anexoGarantiaBO;
	}

	public void setAnexoGarantiaBO(AnexoGarantiaBO anexoGarantiaBO) {
		this.anexoGarantiaBO = anexoGarantiaBO;
	}

	public String getCodempresaRating() {
		return codempresaRating;
	}

	public void setCodempresaRating(String codempresaRating) {
		this.codempresaRating = codempresaRating;
	}

	public String getFechaRatingA() {
		return fechaRatingA;
	}

	public void setFechaRatingA(String fechaRatingA) {
		this.fechaRatingA = fechaRatingA;
	}


	public String getCodempresaAccionista() {
		return codempresaAccionista;
	}

	public void setCodempresaAccionista(String codempresaAccionista) {
		this.codempresaAccionista = codempresaAccionista;
	}


	public List<Accionista> getListaAccionistaAnexo() {
		
		if (getObjectSession("listaAccionistaAnexo") != null){
			listaAccionistaAnexo = (List<Accionista>) getObjectSession("listaAccionistaAnexo");		
		}	
		
		return listaAccionistaAnexo;
	}

	public void setListaAccionistaAnexo(List<Accionista> listaAccionistaAnexo) {
		this.listaAccionistaAnexo = listaAccionistaAnexo;
	}

	public String getIdAccionistaAnexo() {
		return idAccionistaAnexo;
	}

	public void setIdAccionistaAnexo(String idAccionistaAnexo) {
		this.idAccionistaAnexo = idAccionistaAnexo;
	}

	public String getFlagActivar() {
		return flagActivar;
	}

	public void setFlagActivar(String flagActivar) {
		this.flagActivar = flagActivar;
	}

	public String getColumnBureau() {
		return columnBureau;
	}

	public void setColumnBureau(String columnBureau) {
		this.columnBureau = columnBureau;
	}

	public String getColumnRating() {
		return columnRating;
	}

	public void setColumnRating(String columnRating) {
		this.columnRating = columnRating;
	}

	public String getColumnFecha() {
		return columnFecha;
	}

	public void setColumnFecha(String columnFecha) {
		this.columnFecha = columnFecha;
	}

	public String getColumnLteAutorizado() {
		return columnLteAutorizado;
	}

	public void setColumnLteAutorizado(String columnLteAutorizado) {
		this.columnLteAutorizado = columnLteAutorizado;
	}

	public String getColumnLteForm() {
		return columnLteForm;
	}

	public void setColumnLteForm(String columnLteForm) {
		this.columnLteForm = columnLteForm;
	}

	public String getColumnRgoActual() {
		return columnRgoActual;
	}

	public void setColumnRgoActual(String columnRgoActual) {
		this.columnRgoActual = columnRgoActual;
	}

	public String getColumnRgoPropBbvaBc() {
		return columnRgoPropBbvaBc;
	}

	public void setColumnRgoPropBbvaBc(String columnRgoPropBbvaBc) {
		this.columnRgoPropBbvaBc = columnRgoPropBbvaBc;
	}

	public String getColumnPropRiesgo() {
		return columnPropRiesgo;
	}

	public void setColumnPropRiesgo(String columnPropRiesgo) {
		this.columnPropRiesgo = columnPropRiesgo;
	}

	public String getColumnObservaciones() {
		return columnObservaciones;
	}

	public void setColumnObservaciones(String columnObservaciones) {
		this.columnObservaciones = columnObservaciones;
	}

	public String getColumnColumna1() {
		return columnColumna1;
	}

	public void setColumnColumna1(String columnColumna1) {
		this.columnColumna1 = columnColumna1;
	}

	public String getColumnColumna2() {
		return columnColumna2;
	}

	public void setColumnColumna2(String columnColumna2) {
		this.columnColumna2 = columnColumna2;
	}

	public String getColumnColumna3() {
		return columnColumna3;
	}

	public void setColumnColumna3(String columnColumna3) {
		this.columnColumna3 = columnColumna3;
	}

	public String getColumnColumna4() {
		return columnColumna4;
	}

	public void setColumnColumna4(String columnColumna4) {
		this.columnColumna4 = columnColumna4;
	}

	public String getColumnColumna5() {
		return columnColumna5;
	}

	public void setColumnColumna5(String columnColumna5) {
		this.columnColumna5 = columnColumna5;
	}

	public String getColumnColumna6() {
		return columnColumna6;
	}

	public void setColumnColumna6(String columnColumna6) {
		this.columnColumna6 = columnColumna6;
	}

	public String getColumnColumna7() {
		return columnColumna7;
	}

	public void setColumnColumna7(String columnColumna7) {
		this.columnColumna7 = columnColumna7;
	}

	public String getColumnColumna8() {
		return columnColumna8;
	}

	public void setColumnColumna8(String columnColumna8) {
		this.columnColumna8 = columnColumna8;
	}

	public String getColumnColumna9() {
		return columnColumna9;
	}

	public void setColumnColumna9(String columnColumna9) {
		this.columnColumna9 = columnColumna9;
	}

	public String getColumnColumna10() {
		return columnColumna10;
	}

	public void setColumnColumna10(String columnColumna10) {
		this.columnColumna10 = columnColumna10;
	}

	public String getColumnContrato() {
		return columnContrato;
	}

	public void setColumnContrato(String columnContrato) {
		this.columnContrato = columnContrato;
	}	
	
	
	
	
	
}

