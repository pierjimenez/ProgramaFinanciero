
package pe.com.bbva.pfa.rating;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.ws.QueryWS;
import pe.com.grupobbva.pf.pec6.InputPec6;
import pe.com.grupobbva.pf.pec6.OutputPec6;
import pe.com.grupobbva.rating.itcu.Constantes;
import pe.com.grupobbva.rating.itcu.HeaderITUC;
import pe.com.grupobbva.rating.itcu.InputITUC;
import pe.com.grupobbva.rating.itcu.OutputITUC;
import pe.com.grupobbva.rating.itcu.ServiceITUC;
import pe.com.grupobbva.rating.pfa.itda.service.Ejercicio;
import pe.com.grupobbva.rating.pfa.itda.service.SalidaITDA;
import pe.com.grupobbva.rating.pfa.itda.service.ServiceITDA;
import pe.com.grupobbva.xsd.ps9.CtHeaderRq;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.util.StringUtil;

public class RatingService implements Serializable {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	static ServiceITUC ituc;
	static ServiceITDA itda;
	
	public static final String TIPO_EMPRESA ="I";//PARAMETRO DE BD PF
	public static final String TIPO_GRUPO ="C";//PARAMETRO DE BD PF
	
	static public String codigoCliente = "02007002" ;//VALOR ENVIADO POR EL APLICATIVO PF 09336796 / 00857750 /20964432 /14473572 /04735927
	static public String usuario = "P007397";//VALOR ENVIADO POR EL APLICATIVO PF // P016359
	static public String flagEmpresaGrupo  ="I"; //VALOR ENVIADO POR EL APLICATIVO PF
	
	public static final String OPCION_APLICACION ="00";//PARAMETRO DE BD PF
	public static final String TERMINAL_LOGICO ="%410";//PARAMETRO DE BD PF 
	public static final String TERMINAL_CONTABLE ="%410";//PARAMETRO DE BD PF
	
	public static final String ITDA_APLICATIVO ="RT";//PARAMETRO DE BD PF
	public static final String ITDA_TRATAMIENTO ="C";//PARAMETRO DE BD PF
	public static final String ITDA_IP_PORT ="http://118.180.36.28:7801";//PARAMETRO DE BD PF
	
	
	public static final String ITCU_TRANSACCION ="ITUC";//PARAMETRO DE BD PF
	public static final String ITCU_TIPO_MENSAJE ="1";//PARAMETRO DE BD PF
	public static final String ITCU_TIPO_PROCESO ="O";//PARAMETRO DE BD PF
	public static final String ITCU_CANAL_COMUNICACION ="PF";//PARAMETRO DE BD PF
	public static final String ITCU_INDICADOR_PREFORMATO ="N";//PARAMETRO DE BD PF
	public static final String ITCU_TIPO_MENSAJE_ME ="B";//PARAMETRO DE BD PF
	public static final String ITCU_DIST_QNAME_IN ="QLC.SPFI.RESP";//PARAMETRO DE BD PF
	public static final String ITCU_DIST_RQNAME_OUT ="QRC.SPFI.ENVIO.MPD1";//PARAMETRO DE BD PF
	public static final String ITCU_HOST_RQNAME_OUT ="QRC.SPFI.RESP.QMPCINT1";//PARAMETRO DE BD PF
	public static final String ITCU_HOST_QMGR_NAME ="MPD1";//PARAMETRO DE BD PF
	
	public static final String ITCU_IP_PORT ="http://118.180.36.28:7801";//PARAMETRO DE BD PF
	
	public static final String ITCU_TIPO_EMPRESA ="0";//PARAMETRO DE BD PF
	public static final String ITCU_OPCION ="1";//PARAMETRO DE BD PF
	
	
	
	
	
	public List<Ratingws> obtenerListadoRating(InputRating input){
		
		ituc = new ServiceITUC();
		itda = new ServiceITDA();
		
		List<Ratingws> listaArchvioRating = new ArrayList<Ratingws>();
		List<Ratingws> listaArchvioRatingFinal = new ArrayList<Ratingws>();
		
		try{
			//INICIO: CABECERA PARA LA LLAMADA A LOS WEB SERVICE ITDA
			CtHeaderRq header = new CtHeaderRq();
			header.setOpcionAplicacion(input.getHeader().getOpcionAplicacion());
			header.setTerminalContable(input.getHeader().getTerminalContable());
			header.setTerminalLogico(input.getHeader().getTerminalLogico());
			//FIN : CABECERA PARA LA LLAMADA A LOS WEB SERVICE ITDA
			
			//INICIO : DATA PARA LA ITDA
			pe.com.grupobbva.rating.pfa.itda.CtBodyRq dataITDA = new pe.com.grupobbva.rating.pfa.itda.CtBodyRq();
			dataITDA.setAplicativo(input.getHeader().getItdaAplicativo());
			dataITDA.setTratamiento(input.getHeader().getItdaTratamiento());
			
			//FIN : DATA PARA LA ITDA
			
			
			//INICIO: HEADER y DATA PARA LA ITUC
			HeaderITUC headerITUC = new HeaderITUC();
			headerITUC.setTerminalLogico(input.getHeader().getTerminalLogico());
			headerITUC.setTerminalContable(input.getHeader().getTerminalContable());
			
			headerITUC.setCodigoTransaccion(input.getHeader().getItcuTransaccion());
			headerITUC.setTipoMensaje(input.getHeader().getItcuTipoMensaje());
			headerITUC.setTipoProceso(input.getHeader().getItcuTipoProceso());
			headerITUC.setCanalComunicacion(input.getHeader().getItcuCanalComunicacion());
			headerITUC.setIndicadorPreformato(input.getHeader().getItcuIndicadorPreformato());
			headerITUC.setTipoMensajeME(input.getHeader().getItcuTipoMensajeME());
			
			//COLAS PARA EL ITUC				
			headerITUC.setDistQnameIn(input.getHeader().getItcuDistQnameIn());
			headerITUC.setDistRqnameOut(input.getHeader().getItcuDistRqnameOut());
			headerITUC.setHostRqnameOut(input.getHeader().getItcuHostRqnameOut());
			headerITUC.setHostQmgrName(input.getHeader().getItcuHostQmgrName());
			
			headerITUC.setIp_port(input.getHeader().getItcuIpPort());
			headerITUC.setEncoding(input.getHeader().getItcuEncoding());
					
			InputITUC dataITUC =  new InputITUC();
			
			dataITUC.setTipoEmpresa(input.getHeader().getItcuTipoEmpresa());
			dataITUC.setOpcion(input.getHeader().getItcuOpcion());
			//FIN : HEADER y DATA PARA LA ITCU
			
			listaArchvioRatingFinal.clear();
			
			for(DataRating empresa : input.getData()){
				
				//HEADER
				header.setUsuario(empresa.getUsuario());
				//ITDA
				dataITDA.setCodigoCliente(empresa.getCodigoCliente());
				//ITCU
				headerITUC.setUsuario(empresa.getUsuario());
				dataITUC.setCodigoCliente(empresa.getCodigoCliente());

				SalidaITDA salidaITDA = itda.obtieneListaEjercicio(header, dataITDA,input.getHeader().getItdaIpPort());
				
				listaArchvioRating.clear();
				if(TIPO_EMPRESA.equals(empresa.getFlagEmpresaGrupo()) && 
				   salidaITDA.getEjercicioIndividual()!=null &&
				   !salidaITDA.getEjercicioIndividual().isEmpty()){
						
						listaArchvioRating = obtenerListaRating(salidaITDA.getEjercicioIndividual(),header,
														    dataITUC,
														    headerITUC,
														    empresa.getCodigoCliente());
				}
				
				if(TIPO_GRUPO.equals(empresa.getFlagEmpresaGrupo()) && 
				   salidaITDA.getEjercicioGrupal()!=null &&
				   !salidaITDA.getEjercicioGrupal().isEmpty()){
						
						listaArchvioRating = obtenerListaRating(salidaITDA.getEjercicioGrupal(),header,
														    dataITUC,
														    headerITUC,
														    empresa.getCodigoCliente());
					
					
				}
				
				if(!listaArchvioRating.isEmpty() && listaArchvioRating!=null){
					listaArchvioRatingFinal.addAll(listaArchvioRating);
					if(TIPO_GRUPO.equals(empresa.getFlagEmpresaGrupo())){
						break;//CUANDO SE SOLICITA INFORMACION DEL GRUPO, SI SE ENCUENTRA EN UNA DE LAS EMPRESAS TERMINA LA BUSQUEDA DE LA INFORMACION DEL GRUPO
					}
				}
				
				
			}
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
		return listaArchvioRatingFinal;
	}
	
	
	public List<Ratingws> obtenerListaRating(List<Ejercicio> listaEjercicio,CtHeaderRq header,
										   InputITUC dataITUC,
										   HeaderITUC headerITUC,
										   String codigoCliente){
		
		List<OutputITUC> listaBalance = new ArrayList<OutputITUC>();
		List<Ratingws> listaArchvioRating = new ArrayList<Ratingws>();
		
		Ratingws rating = null;
		OutputITUC datosComplementarios = null;
		BigDecimal puntuacionRating = null;	
		try{
			for(Ejercicio ejercicio : listaEjercicio){
				
				dataITUC.setAnioEjercicio(ejercicio.getAnio());
				dataITUC.setMesEjercicio(ejercicio.getMes());
				dataITUC.setModelo(ejercicio.getCodTmodelo());
				dataITUC.setCodigoAnalisis(ejercicio.getCodAnalisis());
	
				listaBalance = ituc.invocaITUC(headerITUC, dataITUC,headerITUC.getIp_port(),headerITUC.getEncoding());
	
				
				datosComplementarios = new OutputITUC();
					
									
						for(OutputITUC balance:listaBalance){
							puntuacionRating = BigDecimal.ZERO;
							if(Constantes.TIPO_REGISTRO_03.equals(balance.getTipoRegistro())){
								rating = new Ratingws();
								rating.setFechaPeriodoCalculado("00-"+ejercicio.getMes() + "-" + ejercicio.getAnio());
								rating.setNombreEmpresa("");
								rating.setCodigoCentralCliente(codigoCliente);
								rating.setMesEjercicio(ejercicio.getMes());
								rating.setInflacion(datosComplementarios.getInflacionAcumulada());
								rating.setCalificacionRating(datosComplementarios.getCalificacionRating());
								rating.setCodigoCuenta(balance.getCodigoCuenta());
								rating.setDescripcionCuenta(balance.getDescripcionCuenta());
								rating.setMonto(balance.getImporte());
								rating.setStatus(ejercicio.getCodBalance());
								rating.setTipoEstadoFinanciero(ejercicio.getFlagGrupoEjercicio());
								rating.setFactorCualitativo(datosComplementarios.getPuntuacionCualitativa());
								rating.setFactorCuantitativo(datosComplementarios.getPuntuacionCuantitativa());
								rating.setFactorBureau(datosComplementarios.getBuro());
								//INICIO : CALCULO PUNTUACION RATING
								
								try{
									BigDecimal factBureau = new BigDecimal(rating.getFactorBureau());
									BigDecimal factCualit = new BigDecimal(rating.getFactorCualitativo());
									BigDecimal factCuanti = new BigDecimal(rating.getFactorCuantitativo());
									
									puntuacionRating = factBureau.add(factCualit).add(factCuanti);
									
								}catch (Exception e) {
									puntuacionRating=BigDecimal.ZERO;
								}
								
								rating.setPuntuacionRating(puntuacionRating.toString());
								//FIN: CALCULO PUNTUACION RATING
								
								listaArchvioRating.add(rating);
							}else{
								if(Constantes.TIPO_REGISTRO_01.equals(balance.getTipoRegistro())){
									//datosComplementarios.setCodigoCliente(balance.getCodigoCliente());
									//datosComplementarios.setAnio(balance.getAnio());
									//datosComplementarios.setMes(balance.getMes());
									//datosComplementarios.setTipoEmpresa(balance.getTipoEmpresa());
									datosComplementarios.setInflacionActual(balance.getInflacionActual());
									datosComplementarios.setInflacionAcumulada(balance.getInflacionAcumulada());
									datosComplementarios.setCalificacionRating(balance.getCalificacionRating());
								}else{
									if(Constantes.TIPO_REGISTRO_02.equals(balance.getTipoRegistro())){
										//datosComplementarios.setCodigoEstudio(balance.getCodigoEstudio());
										//datosComplementarios.setFechaAnalisis(balance.getFechaAnalisis());
										datosComplementarios.setPuntuacionCuantitativa(convierteFormatoDecimalString(balance.getPuntuacionCuantitativa()));
										datosComplementarios.setPuntuacionCualitativa(convierteFormatoDecimalString(balance.getPuntuacionCualitativa()));
										datosComplementarios.setPuntuacionCuliTotal(convierteFormatoDecimalString(balance.getPuntuacionCuliTotal()));
										datosComplementarios.setBuro(convierteFormatoDecimalString(balance.getBuro()));
										//datosComplementarios.setPeriodo(balance.getPeriodo());
										//datosComplementarios.setEstado(balance.getEstado());
										
									}
								}
							}
						}
				}
		   }catch (Exception e) {
				logger.error(StringUtil.getStackTrace(e));
		   }
		return listaArchvioRating;
	}
	
	private String convierteFormatoDecimalString(String valor){
		String valorConvertido="0";
		try{
			valor = valor.trim().replace(" ", "");
			if(valor.length()>1){
				String decimal = valor.substring(valor.length()-2,valor.length());
				String entero = valor.substring(0,valor.length()-2);
				BigDecimal val = new BigDecimal((entero.length()==0?"0":entero) +"."+decimal);
				valorConvertido=val.toString();
			}
		}catch (Exception e) {
			valorConvertido="0";
		}
		
		return valorConvertido;
	}
	public static void main(String[] arg){
		
		RatingService service = new RatingService();
		InputRating input = new InputRating();
		HeaderRating header = new HeaderRating();
		DataRating data =  new DataRating();
		
		/*
		//INICIO : HEADER
		header.setOpcionAplicacion(OPCION_APLICACION);
		header.setTerminalContable(TERMINAL_CONTABLE);
		header.setTerminalLogico(TERMINAL_LOGICO);
		
		//ITUC
		header.setItcuTransaccion(ITCU_TRANSACCION);
		header.setItcuTipoMensaje(ITCU_TIPO_MENSAJE);
		header.setItcuTipoProceso(ITCU_TIPO_PROCESO);
		header.setItcuCanalComunicacion(ITCU_CANAL_COMUNICACION);
		header.setItcuIndicadorPreformato(ITCU_INDICADOR_PREFORMATO);
		header.setItcuTipoMensajeME(ITCU_TIPO_MENSAJE_ME);
						
		header.setItcuDistQnameIn(ITCU_DIST_QNAME_IN);
		header.setItcuDistRqnameOut(ITCU_DIST_RQNAME_OUT);
		header.setItcuHostRqnameOut(ITCU_HOST_RQNAME_OUT);
		header.setItcuHostQmgrName(ITCU_HOST_QMGR_NAME);
				
		header.setItcuTipoEmpresa(ITCU_TIPO_EMPRESA);
		header.setItcuOpcion(ITCU_OPCION);
		header.setItcuIpPort(ITCU_IP_PORT);		
		header.setItcuEncoding(Constantes.ENCODING_CONVERSION);//traer de BD
		
		//ITDA
		header.setItdaTratamiento(ITDA_TRATAMIENTO);
		header.setItdaAplicativo(ITDA_APLICATIVO);
		header.setItdaIpPort(ITDA_IP_PORT);
		*/
		
		header.setOpcionAplicacion(OPCION_APLICACION);
		header.setTerminalContable("%420");
		header.setTerminalLogico("%420");
		
		//ITUC
		header.setItcuTransaccion(ITCU_TRANSACCION);
		header.setItcuTipoMensaje(ITCU_TIPO_MENSAJE);
		header.setItcuTipoProceso(ITCU_TIPO_PROCESO);
		header.setItcuCanalComunicacion(ITCU_CANAL_COMUNICACION);
		header.setItcuIndicadorPreformato(ITCU_INDICADOR_PREFORMATO);
		header.setItcuTipoMensajeME(ITCU_TIPO_MENSAJE_ME);
						
		header.setItcuDistQnameIn("QLP.SPFI.RESPA");
		header.setItcuDistRqnameOut("QRP.SPFI.ENVIO.MPP1");
		header.setItcuHostRqnameOut("QRP.SPFI.RESP.QMPPINT1");
		header.setItcuHostQmgrName("MPP1");
				
		header.setItcuTipoEmpresa(ITCU_TIPO_EMPRESA);
		header.setItcuOpcion(ITCU_OPCION);
		header.setItcuIpPort("http://118.180.54.185:7817");		
		header.setItcuEncoding(Constantes.ENCODING_CONVERSION);//traer de BD
		
		//ITDA
		header.setItdaTratamiento(ITDA_TRATAMIENTO);
		header.setItdaAplicativo(ITDA_APLICATIVO);
		header.setItdaIpPort("http://118.180.54.185:7856");
		
		
		input.setHeader(header);
		//FIN : HEADER
		
		
		//INICIO : DATA
		data.setUsuario(usuario);
		data.setCodigoCliente("20066837");
		data.setFlagEmpresaGrupo(flagEmpresaGrupo);
		
		
		List<DataRating> listaRa = new ArrayList<DataRating>();
		
		listaRa.add(data);
		input.setData(listaRa);
		//FIN : DATA
		

		Long antes = System.currentTimeMillis();
		List<Ratingws> listaArchvioRatingf = service.obtenerListadoRating(input);
		Long despues = System.currentTimeMillis();
		
		//System.out.println("DESPUES INVOCACION : " + System.currentTimeMillis());
		
		/*String valor = "-00000999988875";
		valor = valor.trim().replace(" ", "");
		if(valor.length()>1){
			String decimal = valor.substring(valor.length()-2,valor.length());
			String entero = valor.substring(0,valor.length()-2);
			BigDecimal val = new BigDecimal((entero.length()==0?"0":entero) +"."+decimal);
			System.out.println(val.toString());
		}else{
			System.out.println("VACIO");
		}
		*/
		/*try {
			StringUtil.serializaObjeto("ListaRating", listaArchvioRatingf);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		for(Ratingws ra:listaArchvioRatingf){
			
			System.out.println(ra.getFechaPeriodoCalculado() + "       " + ra.getNombreEmpresa() + "       " + 
							   ra.getCodigoCentralCliente() + "       " + ra.getMesEjercicio() + "       " + 
							   ra.getInflacion() + "       " + ra.getCalificacionRating() + "       " + 
							   ra.getCodigoCuenta() + "       " + ra.getDescripcionCuenta() + "       " +
							   ra.getMonto() + "       " + ra.getStatus() + "       " +
							   ra.getTipoEstadoFinanciero() + "       " + ra.getFactorCualitativo() + "       " +
							   ra.getFactorCuantitativo() + "       " + ra.getFactorBureau() + "       " + 
							   ra.getPuntuacionRating());
			//System.out.println(ToStringBuilder.reflectionToString(ra,ToStringStyle.MULTI_LINE_STYLE));
		}
		
		System.out.println("Tiempo en MiliSegundo: " + (despues - antes));
		
		
	 /* try
	  {
	     
				 
		 FileOutputStream fileOut =new FileOutputStream("C:\\Users\\p018958\\Desktop\\PRUEBA\\RatingwsGrupo.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(listaArchvioRatingf);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved in /tmp/RatingwsGrupo.ser");
    
		  
	  }catch(IOException i)
	  {
	      i.printStackTrace();
	  }
	  
	  try{
		   List<Ratingws> lista =  new ArrayList<Ratingws>();
		   FileInputStream fin = new FileInputStream("C:\\Users\\p018958\\Desktop\\PRUEBA\\RatingwsGrupo.ser");
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   lista = (List<Ratingws>) ois.readObject();
		   ois.close();

		   System.out.println("--------- LEER SERIALIZADO ---------------");
		   System.out.println("--------- LEER SERIALIZADO ---------------");
		   System.out.println("--------- LEER SERIALIZADO ---------------");
		   
		   for(Ratingws ra:lista){
			   System.out.println(ra.getFechaPeriodoCalculado() + "       " + ra.getNombreEmpresa() + "       " + 
					   ra.getCodigoCentralCliente() + "       " + ra.getMesEjercicio() + "       " + 
					   ra.getInflacion() + "       " + ra.getCalificacionRating() + "       " + 
					   ra.getCodigoCuenta() + "       " + ra.getDescripcionCuenta() + "       " +
					   ra.getMonto() + "       " + ra.getStatus() + "       " +
					   ra.getTipoEstadoFinanciero() + "       " + ra.getFactorCualitativo() + "       " +
					   ra.getFactorCuantitativo() + "       " + ra.getFactorBureau() + "       " + 
					   ra.getPuntuacionRating());
		   }

	   }catch(Exception ex){
		   ex.printStackTrace();
		   
	   } */
	      
		
		//EJECUTANDO LA PEC6 00438340
		List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();
		
		try {
			
			InputPec6 entradaPec6 = new InputPec6();
			entradaPec6.setFlagControl("");
			entradaPec6.setNumeroCliente("G0000132");
			entradaPec6.setPathWebServicePEC6("http://118.180.36.26:7802/spfi/pec6/");
			entradaPec6.setTeclaPulsada("00");
			entradaPec6.setUsuario("P007395");
			
			/*HashMap<String, Object> salida = QueryWS.consultarGrupoEconomico(listaGrupoEmpresas,entradaPec6,
										 "http://118.180.36.26:7801/BBVASERVICIOS/EnlaceBBVA/")	;
			
			*/
			
			//OutputPec6 salidaPec6 = QueryWS.obtieneDatosEmpresaGrupo(entradaPec6,null);
			
			//System.out.println(salidaPec6);
						
			
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		
		
		
	}

}
