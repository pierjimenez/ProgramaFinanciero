package pe.com.bbva.iipf.ws;

import grupobbva.pe.com.EnlaceBBVA.Cabecera;


import grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroRequest;
import grupobbva.pe.com.EnlaceBBVA.ConsultaGruposRiesoBuroResponse;
import grupobbva.pe.com.EnlaceBBVA.EnlaceBBVAProxy;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSPe21;
import pe.com.grupobbva.pf.pec6.CtBodyRq;
import pe.com.grupobbva.pf.pec6.CtConSpfiPEC6Rq;
import pe.com.grupobbva.pf.pec6.CtConSpfiPEC6Rs;
import pe.com.grupobbva.pf.pec6.CtHeader;
import pe.com.grupobbva.pf.pec6.CtRela;
import pe.com.grupobbva.pf.pec6.InputPec6;
import pe.com.grupobbva.pf.pec6.OutputPec6;
import pe.com.grupobbva.pf.pec6.SPFI_PEC6SOAP_HTTP_ServiceLocator;
import pe.com.grupobbva.rating.pe21.HeaderPE21;
import pe.com.grupobbva.rating.pe21.InputPE21;
import pe.com.grupobbva.rating.pe21.OutputPE21;
import pe.com.grupobbva.rating.pe21.ServicePE21;
import pe.com.stefanini.core.exceptions.WSException;
import pe.com.stefanini.core.host.Constantes;
import pe.com.stefanini.core.host.FEL1;
import pe.com.stefanini.core.host.TramaSalida;
import pe.com.stefanini.core.host.UsuarioSesion;
import pe.com.stefanini.core.host.Util;
import pe.com.stefanini.core.util.StringUtil;
import pe.com.stefanini.core.util.ValidateUtil;

import com.stefanini.pe.framework.collections.StringCollection;
import com.stefanini.pe.framework.to.FBoolean;
import com.stefanini.pe.framework.to.FString;
import com.stefanini.pe.gnrws.services.TransferenciaProxy;

/**
 * 
 * @author epomayay
 *
 */
public class QueryWS {

	private static Logger logger = Logger.getLogger(QueryWS.class);
	private TransferenciaProxy transferencia ;

	/**
	 * Obtiene Datos de la PEC6,para invocar mandar la lista empresa en null 
	 * @param inputPec6
	 * @return
	 * @throws WSException
	 */
	
	public static OutputPec6 obtieneDatosEmpresaGrupo(InputPec6 inputPec6,List<CtRela> listaEmpresa) throws WSException{

		List<CtRela> listaData = null;
		if(listaEmpresa==null){
			listaData = new ArrayList<CtRela>();
		}else{
			listaData = listaEmpresa;
		}
		
		CtRela ctRela[] = null;
		OutputPec6 salidaPec6 = null;		
		SPFI_PEC6SOAP_HTTP_ServiceLocator servicePec6 = new SPFI_PEC6SOAP_HTTP_ServiceLocator(inputPec6.getPathWebServicePEC6());
		
		CtConSpfiPEC6Rq conSpfiPEC6Rq = new CtConSpfiPEC6Rq();
		//definicion de la peticion codigo del grupo y valor del control
		
		CtBodyRq bodyRq= new CtBodyRq();
		bodyRq.setControl(inputPec6.getFlagControl());
		bodyRq.setNumCliente(inputPec6.getNumeroCliente().toUpperCase());
		conSpfiPEC6Rq.setData(bodyRq);
		
		//definiendo cabecera tecla pulsada 00 o 08 y codigo de usuario host
		
		CtHeader header = new CtHeader();
		header.setTeclaPulsada(inputPec6.getTeclaPulsada());
		header.setUsuario(inputPec6.getUsuario());
		conSpfiPEC6Rq.setHeader(header);
		
		CtConSpfiPEC6Rs conSpfiPEC6Rs = new CtConSpfiPEC6Rs();
		
		try {
			conSpfiPEC6Rs = servicePec6.getSPFI_PEC6SOAP_HTTP_Port().callPEC6(conSpfiPEC6Rq);
			if(conSpfiPEC6Rs == null){
				
				throw new WSException("No se encontrarón empresas para el Grupo con código "+inputPec6.getNumeroCliente().toUpperCase());
				
			}else{
				if(conSpfiPEC6Rs.getData() == null){
					String erroheader="";
					if (conSpfiPEC6Rs.getHeader()!=null){
						erroheader=conSpfiPEC6Rs.getHeader().getDescripcion()==null?"":"Error: "+conSpfiPEC6Rs.getHeader().getDescripcion();
					}
					
					logger.error("Error : No se obtuvo información de HOST- obtieneDatosEmpresaGrupo " + inputPec6.getNumeroCliente() + ". " + erroheader);
					throw new WSException("No se encontraron empresas para el Grupo con código "+ inputPec6.getNumeroCliente().toUpperCase() + "por favor comuníquese con el Administrador.");
					
				}else{
						salidaPec6 = new OutputPec6();
						salidaPec6.setNombreGrupo(conSpfiPEC6Rs.getData().getNombre());
								
						ctRela = conSpfiPEC6Rs.getData().getRelaciones();
								
						if(ctRela!=null){
							for(CtRela data : ctRela){
								if(StringUtils.isNotBlank(data.getDescripcion()) && 
								   StringUtils.isNotBlank(data.getNombre()) && 
								   StringUtils.isNotBlank(data.getNumCliente()) ){
									listaData.add(data);
									}
								}
						}
						salidaPec6.setListaEmpresaGrupo(listaData);
						
						
						//Se verifica si hay mas empresas en el grupo /  Asi esta configurada la transaccion HOST pagina de 12 en 12
						if(conSpfiPEC6Rs.getData().getControl() != null && !"".equals(conSpfiPEC6Rs.getData().getControl().trim())){
								inputPec6.setTeclaPulsada(pe.com.bbva.iipf.util.Constantes.TECLA_HOST_F8);
								inputPec6.setFlagControl(conSpfiPEC6Rs.getData().getControl());
								obtieneDatosEmpresaGrupo(inputPec6,listaData);
						}	
				}	
						
			}

		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			throw new WSException("Se produjo en error en la comunicación con HOST, por favor comuníquese con el Administrador.");
		}		
		return salidaPec6;
	}

	
	/**
	 * Consulta de empresas de grupo
	 * 
	 * @param listaGrupoEmpresas
	 * @param numCliente
	 * @param teclaPulsada
	 * @param usuario
	 * @param control
	 * @return
	 * @throws WSException
	 */
	//PARA PRODUCCION CAMBIAR consultarGrupoEconomico_ORIG POR consultarGrupoEconomico
	public static HashMap<String, Object> consultarGrupoEconomico(InputPec6 entradaPec6,
																	ConfiguracionWSPe21 oConfiguracionWSPe21,String VAL_TIPO_DOC_COD_CENTRAL
																  )throws WSException{
		HashMap<String,Object> resultado = new HashMap<String,Object>();
		List<Empresa> listaGrupoEmpresas = new ArrayList<Empresa>();
		try {
			

				OutputPec6 salidaPec6= obtieneDatosEmpresaGrupo(entradaPec6,null); 
				if( salidaPec6.getListaEmpresaGrupo() != null){
					
					for(CtRela empr : salidaPec6.getListaEmpresaGrupo()){
						//valida que el elemento del arreglo tenga datos validos de una empresa
						if(empr.getNumCliente()!= null && !empr.getNumCliente().equals("")){
							Empresa empresa = new Empresa();
							empresa.setCodigo(empr.getNumCliente());
							String ruc="";
							try {						
								ruc = consularDatosBasicos(empr.getNumCliente(), 
														  VAL_TIPO_DOC_COD_CENTRAL,
														   entradaPec6.getUsuario(),
														   oConfiguracionWSPe21).get("ruc");
							} catch (Exception e) {	
								logger.error("Error consultar Datos Basicos - consultarGrupoEconomico" + empr.getNumCliente() +". "+  StringUtil.getStackTrace(e));
								ruc="";
							}
							
							empresa.setRuc(ruc);
							empresa.setNombre(empr.getNombre());
							listaGrupoEmpresas.add(empresa);
						}
					}
					
					
					
				}
				
				resultado.put("listaEmpresas", listaGrupoEmpresas);
				resultado.put("nombreGrupo", salidaPec6.getNombreGrupo());
	
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			throw new WSException("Se produjo en error en la comunicación con HOST, por favor comuniquese con el Administrador");
		}
		
		return resultado;
	}
	
	
	//ini MCG20141114
	public static HashMap<String, String> obtenerGrupoEconomicoWS(InputPec6 entradaPec6) throws WSException {
		HashMap<String, String> resultado = new HashMap<String, String>();
		
		try {

			OutputPec6 salidaPec6 = obtieneDatosEmpresaGrupo(entradaPec6, null);
			if (salidaPec6.getListaEmpresaGrupo() != null) {
				String codGrupo="";
				for (CtRela grupo : salidaPec6.getListaEmpresaGrupo()) {					
					if (grupo.getNumCliente() != null && !grupo.getNumCliente().equals("")) {
						codGrupo=grupo.getNumCliente()==null?"":grupo.getNumCliente();
						break;
					}
				}
				if (!codGrupo.equals("")){
					 resultado.put("codGrupo", codGrupo);
					 resultado.put("codError", Constantes.COD_OK_WS);
					 resultado.put("msnError", "");	
				}else{
					resultado.put("codGrupo", "");
					resultado.put("codError", Constantes.COD_ERROR_WS);
					resultado.put("msnError", "La Empresa no pertenece a ningún Grupo o ha sido dado de baja");	
				}

			}

		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			resultado = new HashMap<String,String>();
			resultado.put("codGrupo", "");
			resultado.put("codError", Constantes.COD_ERROR_WS);
			resultado.put("msnError", "Se produjo en error en la comunicación con HOST");
			
		}

		return resultado;
	}
	
	//PARA PRUEBAS
	public static HashMap<String, String> obtenerGrupoEconomicoWS_TEST(InputPec6 entradaPec6) throws WSException {
		HashMap<String, String> resultado = new HashMap<String, String>();
		
		try {

				String codGrupo="G0000239";				
				 resultado.put("codGrupo", codGrupo);
				 resultado.put("codError", Constantes.COD_OK_WS);
				 resultado.put("msnError", "");		

		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			resultado = new HashMap<String,String>();
			resultado.put("codGrupo", "");
			resultado.put("codError", Constantes.COD_ERROR_WS);
			resultado.put("msnError", "Se produjo en error en la comunicación con HOST");
			
		}

		return resultado;
	}
	//fin MCG20141114
	
	/**
	 * ESTE METODO SE DEJA DE USAR PUESTO QUE SOLO PUEDE RECUPERAR 6 EMPRESAS COMO 
	 * MAXIMO PARA UN GRUPO A PARTIR DE LA FECHA SE USA EL METODO  consultarGrupoEconomico
	 * El metodo retorna un hash que contiene dos elementos 
	 * el primero es el nombre del grupo y el segundo es 
	 * la lista de las empresas que conforman el grupo
	 * @param codigo
	 * @return 
	 * @throws WSException
	 */
//	public static HashMap<String,Object> consultarGrupoEconomico_antiguo(String codigo,
//																 String sUsuario,
//																 String pathWebService) throws WSException{
//		List<Empresa> listaGrupoEmpresas= new ArrayList<Empresa>();
//		HashMap<String,Object> resultado = new HashMap<String,Object>();
//		EnlaceBBVA_PortTypeProxy enlaceBBVA_PortTypeProxy= new EnlaceBBVA_PortTypeProxy(pathWebService);
//		ConsultaGruposEconomicoRequest parameters = new ConsultaGruposEconomicoRequest();
//		Cabecera cabecera = new Cabecera();
//		cabecera.setUsuario(completarEspaciosDerecha(sUsuario,1));
//		parameters.setCabecera(cabecera);
//		parameters.setCodigoGrupoEconomico(codigo);
//		Empresa empresa = null;
//		try {
//			ConsultaGruposEconomicoReponse consultaGruposEconomicoReponse = enlaceBBVA_PortTypeProxy.consultaGruposEconomico(parameters);
//			if(consultaGruposEconomicoReponse!= null){
//				if(consultaGruposEconomicoReponse.getCodigoCentral()!= null &&
//				!consultaGruposEconomicoReponse.getCodigoCentral().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral(), 
//													 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//													 sUsuario,
//													 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa());
//					listaGrupoEmpresas.add(empresa);
//				} if(consultaGruposEconomicoReponse.getCodigoCentral2()!= null &&
//					!consultaGruposEconomicoReponse.getCodigoCentral2().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral2());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral2(), 
//							 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//							 sUsuario,
//							 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa2());
//					listaGrupoEmpresas.add(empresa);
//				} if(consultaGruposEconomicoReponse.getCodigoCentral3()!= null &&
//					!consultaGruposEconomicoReponse.getCodigoCentral3().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral3());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral3(), 
//							 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//							 sUsuario,
//							 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa3());
//					listaGrupoEmpresas.add(empresa);
//				} if(consultaGruposEconomicoReponse.getCodigoCentral4()!= null &&
//					!consultaGruposEconomicoReponse.getCodigoCentral4().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral4());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral4(), 
//							 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//							 sUsuario,
//							 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa4());
//					listaGrupoEmpresas.add(empresa);
//				} if(consultaGruposEconomicoReponse.getCodigoCentral5()!= null &&
//					!consultaGruposEconomicoReponse.getCodigoCentral5().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral5());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral5(), 
//							 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//							 sUsuario,
//							 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa5());
//					listaGrupoEmpresas.add(empresa);
//				} if(consultaGruposEconomicoReponse.getCodigoCentral6()!= null &&
//					!consultaGruposEconomicoReponse.getCodigoCentral6().trim().equals("")){
//					empresa = new Empresa();
//					empresa.setCodigo(consultaGruposEconomicoReponse.getCodigoCentral6());
//					String ruc = consularDatosBasicos(consultaGruposEconomicoReponse.getCodigoCentral6(), 
//							 pe.com.bbva.iipf.util.Constantes.VAL_TIPO_DOC_COD_CENTRAL,
//							 sUsuario,
//							 pathWebService).get("ruc");
//					empresa.setRuc(ruc);
//					empresa.setNombre(consultaGruposEconomicoReponse.getNombreEmpresa6());
//					listaGrupoEmpresas.add(empresa);
//				}
//			}
//			else{
//				resultado = null;
//				throw new WSException("No se encontrarón empresas para el Grupo con código "+codigo);
//			}
//			resultado.put("listaEmpresas", listaGrupoEmpresas);
//			resultado.put("nombreGrupo", codigo);
//		} catch (RemoteException e) {
//			logger.error(StringUtil.getStackTrace(e));
//			throw new WSException("Se produjo en error en la comunicación con HOST");
//		}
//
//		
//		return resultado;
//	}
	
	
	//INI MCG 20120503 PARA TEST
	//TEST::PARA PRODUCCION CAMBIAR 	consultarGrupoEconomico POR consultarGrupoEconomico_Test
	public static HashMap<String,Object> consultarGrupoEconomico_TEST(InputPec6 entradaPec6,
																	   ConfiguracionWSPe21 oConfiguracionWSPe21,String VAL_TIPO_DOC_COD_CENTRAL)	throws WSException{
		HashMap<String,Object> resultado = new HashMap<String,Object>();
		if(entradaPec6.getNumeroCliente().equals("G0003333")|| entradaPec6.getNumeroCliente().equals("G0000239") ){//00094471
			resultado.put("nombreGrupo", "GRUPO HOCHSCHILD");
			List<Empresa> olistaGrupoEmpresas= new ArrayList<Empresa>();
			Empresa empresaPrincipal = new Empresa();
			empresaPrincipal.setCodigo("21587534");
			empresaPrincipal.setRuc("20100049261");
			empresaPrincipal.setNombre("CEMENTOS PACASMAYO");
			olistaGrupoEmpresas.add(empresaPrincipal);
			
			Empresa empresa2 = new Empresa();
			empresa2.setCodigo("00020770");
			empresa2.setRuc("104254670811");
			empresa2.setNombre("CIA. MINERA ARES");
			olistaGrupoEmpresas.add(empresa2);
			
			Empresa empresa3 = new Empresa();
			empresa3.setCodigo("00077194");
			empresa3.setRuc("104254670911");
			empresa3.setNombre("HOSCHILD MINIG PLC");
			olistaGrupoEmpresas.add(empresa3);
			
			Empresa empresa4 = new Empresa();
			empresa4.setCodigo("00408921");
			empresa4.setRuc("104254671011");
			empresa4.setNombre("TEXSA MORTEROS");
			olistaGrupoEmpresas.add(empresa4);
			
			Empresa empresa5 = new Empresa();
			empresa5.setCodigo("22528388");
			empresa5.setRuc("104254671234");
			empresa5.setNombre("MINERA CARABAYLLO SA");
			olistaGrupoEmpresas.add(empresa5);
		
			
			
			resultado.put("listaEmpresas", olistaGrupoEmpresas);
		}else{
			resultado = null;
			throw new WSException("No se encontrarón empresas para el grupo con código "+entradaPec6.getNumeroCliente());
		}
		return resultado;
	}
	//FIN MCG
	
	
	public static void LoginFeloCopy00(HttpServletRequest request,
									   HttpServletResponse response, 
									   String sUsuario, 
									   String sClave,
									   FBoolean bRefTodoOk, 
									   FString sRefMensaje,
									   StringCollection sRefCampos, 
									   UsuarioSesion oRefUsuarioSesion) throws WSException {
		boolean FlagProduccion = false;
		TransferenciaProxy oTransferenciaProxy = null;
		String sCadenaRespuesta = "";
		String sCadenaEntrada = "";
		try {
			oTransferenciaProxy = new TransferenciaProxy();
			TramaSalida oTramaSalida = new TramaSalida(	3, 3);
			if(FlagProduccion){
				sCadenaEntrada = FEL1.Entrada("", 
											  sUsuario, 
											  sClave, 
											  "BG");//PONER DOS ESPACIOS POR DEFECTO
				if (Util.RetornaTestConColas()) {
						sCadenaRespuesta = oTransferenciaProxy.soloTrama(Constantes.SESSION_FUNCTION, 
																	     sCadenaEntrada, 
																	     Util.RetornaAmbienteHOST(), 
																	     request.getRemoteAddr(), 
																	     Util.RetornaCodigoSistema());
	//			sCadenaRespuesta =  oTransferenciaProxy.validarUsuarioPorTipo(Constantes.SESSION_FUNCTION,
	//																		  Util.RetornaAmbienteHOST(), 
	//																		  request.getRemoteAddr(), 
	//																		  Constantes.COD_DEFALULT_SESSION, 
	//																		  sUsuario, 
	//																		  sClave, 
	//																		  "");
				}
			}else{
				sCadenaRespuesta = "33036|N|001000205|002000TEDDY ALFONSO GONZALES RIOS";
			}
			
			oTramaSalida.ObtenerFormateado(sCadenaRespuesta, 
										   sRefMensaje,
										   bRefTodoOk);
		if (bRefTodoOk.getValue().booleanValue()) {
			sRefCampos = oTramaSalida.ObtenerCampos();
			oRefUsuarioSesion.setRegistroHost(sUsuario);
			oRefUsuarioSesion.setPassword(sClave);
			oRefUsuarioSesion.setNombre(sRefCampos.get(0));
			oRefUsuarioSesion.setPerfilHost("01");
			oRefUsuarioSesion.setSesion(oTramaSalida.ObtenerSession());
		}
		} catch (Exception ex) {
			sRefMensaje.setValue("Problemas de Comunicación con el Servicio");
			ex.printStackTrace();
			logger.error(StringUtil.getStackTrace(ex));
		} finally {
		}
	}
	//PARA PRODUCCION CAMBIAR consularDatosBasicos_ORIG A consularDatosBasicos
	public static HashMap<String,String> consularDatosBasicos(String codigo, 
															  String tipoDocumento,
															  String sUsuario,
															  ConfiguracionWSPe21 oConfiguracion) throws WSException{
		HashMap<String, String> persona = new HashMap<String, String>();
		
		ServicePE21 ser = new ServicePE21();
		HeaderPE21 header = new HeaderPE21();
		header.setTerminalLogico(oConfiguracion.getTerminalLogico());
		header.setTerminalContable(oConfiguracion.getTerminalContable());
		header.setUsuario(sUsuario);
		header.setCodigoTransaccion(oConfiguracion.getPe21Transaccion());
		header.setTipoMensaje(oConfiguracion.getPe21TipoMensaje());
		header.setTipoProceso(oConfiguracion.getPe21TipoProceso());
		header.setCanalComunicacion(oConfiguracion.getPe21CanalComunicacion());
		header.setIndicadorPreformato(oConfiguracion.getPe21IndicadorPreformato());
		header.setTipoMensajeME(oConfiguracion.getPe21TipoMensajeMe());
						
		header.setDistQnameIn(oConfiguracion.getPe21DistQnameIn());
		header.setDistRqnameOut(oConfiguracion.getPe21DistRqnameOut());
		header.setHostRqnameOut(oConfiguracion.getPe21HostRqnameOut());
		header.setHostQmgrName(oConfiguracion.getPe21HostQmgrName());
		
		InputPE21 input =  new InputPE21();
		input.setCodigoCentral(codigo);
		input.setNumeroDocumentoIdentidad(codigo);
		input.setTipoDocumentoIdentidad(tipoDocumento);

		try {
			OutputPE21 salida = ser.invocaPE21(header, input, oConfiguracion.getPe21IpPort(), oConfiguracion.getPe21Encoding());
			String nombre = "";
			String priape = "";
			String segape = "";
			
			if (salida!=null && (salida.getPrf_o_numcli()!=null && salida.getPrf_o_numcli().trim().equals("") 
							||salida.getPrf_o_claident()!=null && salida.getPrf_o_claident().trim().equals(""))){
				 salida = ser.invocaPE21(header, input, oConfiguracion.getPe21IpPort(), oConfiguracion.getPe21Encoding());
			}
			
			if(salida!=null){

				//
				nombre = salida.getPrf_o_nombre() == null ? "": salida.getPrf_o_nombre().trim();
				priape = salida.getPrf_o_priape() == null ? "": salida.getPrf_o_priape().trim();
				segape = salida.getPrf_o_segape() == null ? "": salida.getPrf_o_segape().trim();
				
				String tipoDocuHost = salida.getPrf_o_itipiden() == null ? "":salida.getPrf_o_itipiden();
				String numeDocuHost = salida.getPrf_o_claident() == null ? "":salida.getPrf_o_claident();
				String digitoIdenti = salida.getPrf_o_digident() == null ? "":salida.getPrf_o_digident();
				
				persona.put("codigoCentral",salida.getPrf_o_numcli() == null ? "": salida.getPrf_o_numcli().trim());
				
				if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocuHost)){
				persona.put("ruc",numeDocuHost+digitoIdenti);
				}else{
				  persona.put("ruc",numeDocuHost);	
				}
				
				
				persona.put("pais",salida.getPrf_o_nnacres()== null ? "": salida.getPrf_o_nnacres().trim());
				persona.put("codigoPais",salida.getPrf_o_inacres()== null ? "": salida.getPrf_o_inacres().trim());
				//prf_o_fnacimi=1992-09-09
				persona.put("fechaNacimiento", salida.getPrf_o_fnacimi() == null ? "": salida.getPrf_o_fnacimi().trim());
				
				//prf_o_feantig=2004-11-30
				persona.put("fechaAntig",salida.getPrf_o_feantig() == null ? "": salida.getPrf_o_feantig().trim());
				
				//prf_o_fealtcli=17-04-2001
				persona.put("fechaAltaCliente",salida.getPrf_o_fealtcli()== null ? "": salida.getPrf_o_fealtcli().trim());
				
				persona.put("tipNumDocumento", tipoDocuHost + "|" + numeDocuHost+digitoIdenti);
				
				
				
				if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocumento)){
					persona.put("nombreEmpesa", salida.getPrf_o_razsoc() == null ? "": salida.getPrf_o_razsoc().trim());
				}else{
					if(pe.com.grupobbva.rating.pe21.Constantes.TD_CODDIGO_CENTRAL.equals(tipoDocumento)){
								if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocuHost)){
									persona.put("nombreEmpesa", salida.getPrf_o_razsoc() == null ? "": salida.getPrf_o_razsoc().trim());
								}else{
									//PARA LOS DEMAS CASOS QUE NO SON EMPRESA
									persona.put("nombreEmpesa", nombre + " " + priape + " " +segape);
								}
					}else{
						//PARA LOS DEMAS CASOS QUE NO SON EMPRESA
						persona.put("nombreEmpesa", nombre + " " + priape + " " +segape);
					}
				}

				//
				
				
			}else{
				throw new WSException("No se encontrarón los datos basicos para la empresa "+ codigo);
			}
			
		} catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
			throw new WSException("Se produjo en error en la comunicación con HOST");	
			
		}
		return persona;
	}
	
	
	//ini MCG20140616
	/*public static HashMap<String, String> consularDatosAccionista(String codigo,String tipoDocumento, String sUsuario, String pathWebService)
			throws WSException {
		HashMap<String, String> persona = new HashMap<String, String>();
		EnlaceBBVA_PortTypeProxy enlaceBBVA_PortTypeProxy = new EnlaceBBVA_PortTypeProxy(pathWebService);
		ConsultaDatosClienteRequest consultaDatosClienteRequest = new ConsultaDatosClienteRequest();
		Cabecera cabecera = new Cabecera();
		cabecera.setUsuario(completarEspaciosDerecha(sUsuario, 1));
		consultaDatosClienteRequest.setCabecera(cabecera);
		// buscando por codigo central
		if (tipoDocumento.equals("C")) {
			// busqueda por Código Central
			consultaDatosClienteRequest.setNumeroDocumentoIdentidad("");
			consultaDatosClienteRequest.setTipoDocumentoIdentidad("");
			consultaDatosClienteRequest.setCodigoCentral(codigo);			
		} else {
			//RUC
			consultaDatosClienteRequest.setNumeroDocumentoIdentidad(codigo);
			consultaDatosClienteRequest.setTipoDocumentoIdentidad(tipoDocumento);
			consultaDatosClienteRequest.setCodigoCentral(completarEspaciosDerecha("", 8));
		}
		
		try {
			ConsultaDatosClienteResponse consultaDatosClienteResponse = enlaceBBVA_PortTypeProxy.consultaDatosCliente(consultaDatosClienteRequest);
			if (consultaDatosClienteResponse != null) {
				persona.put("codigoCentral", consultaDatosClienteResponse.getCodigoCentral() == null ? "": consultaDatosClienteResponse.getCodigoCentral().trim());
				persona.put("ruc",consultaDatosClienteResponse.getRUC() == null ? "": consultaDatosClienteResponse.getRUC().trim());
				persona.put("nombreEmpesa", consultaDatosClienteResponse.getNombreEmpresa() == null ? "": consultaDatosClienteResponse.getNombreEmpresa().trim());
				persona.put("codError","OK");
				
			} else {
				persona.put("codigoCentral","");
				persona.put("ruc","");
				persona.put("nombreEmpesa","");
				persona.put("codError","No se encontrarón los datos basicos para el Accionista "+ codigo);//"No se encontrarón los datos basicos para la empresa "
							
			}
		} catch (RemoteException e) {			
			String error ="";
			if(StringUtil.getStackTrace(e)!= null){				
				error = extracMsjError(StringUtil.getStackTrace(e));				
			}else{
				error="Se produjo en error en la comunicación con HOST";	
			}
			persona.put("codigoCentral","");
			persona.put("ruc","");
			persona.put("nombreEmpesa","");
			persona.put("codError",error);//Se produjo en error en la comunicación con HOST");
			logger.error(StringUtil.getStackTrace(e));

		} catch (Exception e) {
			persona.put("codigoCentral","");
			persona.put("ruc","");
			persona.put("nombreEmpesa","");
			persona.put("codError","Se produjo en error en la comunicación con HOST");//"Se produjo en error en la comunicación con HOST"
			logger.error(StringUtil.getStackTrace(e));
		}
		return persona;
	}	*/
	
	
	//fin MCG20140616
	
	//ini CCA20140922
		public static HashMap<String, String> consularDatosAccionista(String codigo,String tipoDocumento, String sUsuario,ConfiguracionWSPe21 oConfiguracion)
			throws WSException {
			HashMap<String, String> persona = new HashMap<String, String>();
		
			ServicePE21 ser = new ServicePE21();
			HeaderPE21 header = new HeaderPE21();
			header.setTerminalLogico(oConfiguracion.getTerminalLogico());
			header.setTerminalContable(oConfiguracion.getTerminalContable());
			header.setUsuario(sUsuario);
			header.setCodigoTransaccion(oConfiguracion.getPe21Transaccion());
			header.setTipoMensaje(oConfiguracion.getPe21TipoMensaje());
			header.setTipoProceso(oConfiguracion.getPe21TipoProceso());
			header.setCanalComunicacion(oConfiguracion.getPe21CanalComunicacion());
			header.setIndicadorPreformato(oConfiguracion.getPe21IndicadorPreformato());
			header.setTipoMensajeME(oConfiguracion.getPe21TipoMensajeMe());
							
			header.setDistQnameIn(oConfiguracion.getPe21DistQnameIn());
			header.setDistRqnameOut(oConfiguracion.getPe21DistRqnameOut());
			header.setHostRqnameOut(oConfiguracion.getPe21HostRqnameOut());
			header.setHostQmgrName(oConfiguracion.getPe21HostQmgrName());
			
			InputPE21 input =  new InputPE21();
			input.setCodigoCentral(codigo);
			input.setNumeroDocumentoIdentidad(codigo);
			input.setTipoDocumentoIdentidad(tipoDocumento);

		
		try {
			//ConsultaDatosClienteResponse consultaDatosClienteResponse = enlaceBBVA_PortTypeProxy.consultaDatosCliente(consultaDatosClienteRequest);
			OutputPE21 salida = ser.invocaPE21(header, input, oConfiguracion.getPe21IpPort(), oConfiguracion.getPe21Encoding());
			String nombre = "";
			String priape = "";
			String segape = "";
			if (salida!=null && (salida.getPrf_o_numcli()!=null && salida.getPrf_o_numcli().trim().equals("")
							 ||salida.getPrf_o_claident()!=null && salida.getPrf_o_claident().trim().equals(""))){
				 salida = ser.invocaPE21(header, input, oConfiguracion.getPe21IpPort(), oConfiguracion.getPe21Encoding());
			}
			
			if (salida != null) {
				nombre = salida.getPrf_o_nombre() == null ? "": salida.getPrf_o_nombre().trim();
				priape = salida.getPrf_o_priape() == null ? "": salida.getPrf_o_priape().trim();
				segape = salida.getPrf_o_segape() == null ? "": salida.getPrf_o_segape().trim();
				
				String tipoDocuHost = salida.getPrf_o_itipiden() == null ? "":salida.getPrf_o_itipiden();
				String numeDocuHost = salida.getPrf_o_claident() == null ? "":salida.getPrf_o_claident();
				String digitoIdenti = salida.getPrf_o_digident() == null ? "":salida.getPrf_o_digident();
				
				persona.put("codigoCentral",salida.getPrf_o_numcli() == null ? "": salida.getPrf_o_numcli().trim());
				//persona.put("ruc",numeDocuHost);
				if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocuHost)){
					persona.put("ruc",numeDocuHost+digitoIdenti);
					}else{
					  persona.put("ruc",numeDocuHost);	
				}
				
				persona.put("fechaNacimiento", salida.getPrf_o_fnacimi() == null ? "": salida.getPrf_o_fnacimi().trim());
				persona.put("tipNumDocumento", tipoDocuHost + "|" + numeDocuHost+digitoIdenti);
				
				if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocumento)){
					persona.put("nombreEmpesa", salida.getPrf_o_razsoc() == null ? "": salida.getPrf_o_razsoc().trim());
				}else{
					if(pe.com.grupobbva.rating.pe21.Constantes.TD_CODDIGO_CENTRAL.equals(tipoDocumento)){
								if(pe.com.grupobbva.rating.pe21.Constantes.TD_RUC.equals(tipoDocuHost)){
									persona.put("nombreEmpesa", salida.getPrf_o_razsoc() == null ? "": salida.getPrf_o_razsoc().trim());
								}else{
									//PARA LOS DEMAS CASOS QUE NO SON EMPRESA
									persona.put("nombreEmpesa", nombre + " " + priape + " " +segape);
								}
					}else{
						//PARA LOS DEMAS CASOS QUE NO SON EMPRESA
						persona.put("nombreEmpesa", nombre + " " + priape + " " +segape);
					}
				}
				
				persona.put("codError","OK");
				
			} else {
				persona.put("codigoCentral","");
				persona.put("ruc","");
				persona.put("nombreEmpesa","");
				persona.put("fechaNacimiento","");
				persona.put("nombreEmpesa","");
				persona.put("codError","No se encontrarón los datos basicos para el Accionista "+ codigo);//"No se encontrarón los datos basicos para la empresa "
							
			}
		} catch (Exception e) {
			persona.put("codigoCentral","");
			persona.put("ruc","");
			persona.put("nombreEmpesa","");
			persona.put("fechaNacimiento","");
			persona.put("codError","Se produjo en error en la comunicación con HOST");//"Se produjo en error en la comunicación con HOST"
			logger.error(StringUtil.getStackTrace(e));
		}
		return persona;
		}	
	//fin CCA20140922
	
	
	//INI MCG 20120503 PARA TEST
	//PARA PRODUCCION CAMBIAR consularDatosBasicos POR consularDatosBasicos_Test
	public static HashMap<String,String> consularDatosBasicos_TEST(String codigo, 
			  													   String tipoDocumento,
			  													   String sUsuario,
			  													   ConfiguracionWSPe21 oConfiguracion) throws WSException{
		
		//INI MCG 20120427
		HashMap<String,String> datosB1 = new HashMap<String,String>();
		datosB1.put("codigoCentral","21587534" );
		datosB1.put("ruc","20100049261" );
		datosB1.put("nombreEmpesa", "CEMENTOS PACASMAYO");
		datosB1.put("pais","PERU" );
		datosB1.put("codigoPais","100" );
		datosB1.put("fechaNacimiento","2005-02-10");
		datosB1.put("fechaAntig","2005-07-25");
		
		HashMap<String,String> datosB2 = new HashMap<String,String>();
		datosB2.put("codigoCentral","08049360" );
		datosB2.put("ruc","20100049262" );
		datosB2.put("nombreEmpesa", "BIDONE CONSTRUCCIONE");
		datosB2.put("pais","PERU" );
		datosB2.put("codigoPais","100" );
		datosB2.put("fechaNacimiento","2005-02-11");
		datosB2.put("fechaAntig","2005-07-20");
		
		
		HashMap<String,String> datosB3 = new HashMap<String,String>();
		datosB3.put("codigoCentral","00020770" );
		datosB3.put("ruc","104254670811" );
		datosB3.put("nombreEmpesa", "CIA. MINERA ARES");
		datosB3.put("pais","PERU" );
		datosB3.put("codigoPais","100" );
		datosB3.put("fechaNacimiento","2005-02-13");
		datosB3.put("fechaAntig","2005-07-08");
		
		HashMap<String,String> datosB4 = new HashMap<String,String>();
		datosB4.put("codigoCentral","00077194" );
		datosB4.put("ruc","104254670911" );
		datosB4.put("nombreEmpesa", "HOSCHILD MINIG PLC");
		datosB4.put("pais","PERU" );
		datosB4.put("codigoPais","100" );
		datosB4.put("fechaNacimiento","2006-02-17");
		datosB4.put("fechaAntig","2007-07-03");
		
		HashMap<String,String> datosB5 = new HashMap<String,String>();
		datosB5.put("codigoCentral","00408921" );
		datosB5.put("ruc","104254671011" );
		datosB5.put("nombreEmpesa", "TEXSA MORTEROS");
		datosB5.put("pais","PERU" );
		datosB5.put("codigoPais","100" );
		datosB5.put("fechaNacimiento","2008-02-09");
		datosB5.put("fechaAntig","2009-07-18");
		
		HashMap<String,String> datosB6 = new HashMap<String,String>();
		datosB6.put("codigoCentral","00001171" );
		datosB6.put("ruc","20410343275" );
		datosB6.put("nombreEmpesa", "TRANSPORTES PALOMINO EIRL");
		datosB6.put("pais","PERU" );
		datosB6.put("codigoPais","100" );
		datosB6.put("fechaNacimiento","2010-02-10");
		datosB6.put("fechaAntig","2011-07-15");
		
		 
		
		HashMap<String,String> datosB7 = new HashMap<String,String>();
		datosB7.put("codigoCentral","22528388" );
		datosB7.put("ruc","20410348888" );
		datosB7.put("nombreEmpesa", "MINERA CARABAYLLO SA");
		datosB7.put("pais","PERU" );
		datosB7.put("codigoPais","100" );
		datosB7.put("fechaNacimiento","2010-02-10");
		datosB7.put("fechaAntig","2011-07-15");
		
		
		//FIN MCG 20120427
		
		
		
		
		// String[] datosB
		// ={"100000","104254670711","COSAPI","100","PERU","15/02/1870","05/07/1900"};
		
//		String[] datosB1 = { "21587534", "20100049261",
//				"INVERSIONES FESA SCRL", "100", "PERU", "15/02/1870",
//				"05/07/1900" };
//		String[] datosB2 = { "08049360", "20100049262", "BIDONE CONSTRUCCIONE",
//				"100", "PERU", "15/02/1875", "05/07/1901" };
		
		if (tipoDocumento
				.equals("R")) {
			if (codigo.equals("20100049261")) {
				return datosB1;
			} else if (codigo.equals("20100049262")) {
				return datosB2;
			} else if (codigo.equals("104254670811")) {
				return datosB3;
			} else if (codigo.equals("104254670911")) {
				return datosB4;
			} else if (codigo.equals("104254671011")) {
				return datosB5;	
			} else if (codigo.equals("20410343275")) {
				return datosB6;	
			} else {
				datosB1 = null;
				datosB2 = null;				
				datosB3 = null;
				datosB4 = null;
				datosB5 = null;
				datosB6 = null;
				throw new WSException(
						"No se encontrarón los datos basicos para la empresa "
								+ codigo);
			}
		} else {
			if (codigo.equals("21587534")) {
				return datosB1;
			} else if (codigo.equals("08049360")) {
				return datosB2;
			} else if (codigo.equals("00020770")) {
				return datosB3;
			} else if (codigo.equals("00077194")) {
				return datosB4;
			} else if (codigo.equals("00408921")) {
				return datosB5;
			} else if (codigo.equals("00001171")) {
				return datosB6;
			} else if (codigo.equals("22528388")) {
				return datosB7;
			
			} else {
				datosB1 = null;
				datosB2 = null;				
				datosB3 = null;
				datosB4 = null;
				datosB5 = null;
				datosB6 = null;
				datosB7 = null;
				throw new WSException(
						"No se encontrarón los datos basicos para la empresa "
								+ codigo);
			}
		}
	}
	//FIN MCG 20120503
	
	//PARA PRODUCCION CAMBIAR consultarGrupoBuro_ORIG A consultarGrupoBuro
	public static String consultarGrupoBuro(String documento,
											String tipoDocumento,
											String sUsuario,
											String pathWebService) throws WSException{
		logger.info("Consultando grupo buro");
		logger.info("ruc="+documento);
		logger.info("usuario="+sUsuario);
		String grupo="";
		//EnlaceBBVA_PortTypeProxy enlaceBBVA_PortTypeProxy= new EnlaceBBVA_PortTypeProxy(pathWebService);
		EnlaceBBVAProxy service = new EnlaceBBVAProxy(pathWebService);
		ConsultaGruposRiesoBuroRequest parameters =  new ConsultaGruposRiesoBuroRequest();
		//parameters.setNumeroDocumentoIdentidad(completarEspaciosDerecha(ruc,1));
		parameters.setNumeroDocumentoIdentidad(documento);
		parameters.setTipoDocumentoIdentidad(tipoDocumento);
		Cabecera cabecera = new Cabecera();
		cabecera.setUsuario(completarEspaciosDerecha(sUsuario,1));
		parameters.setCabecera(cabecera);
		try {
			ConsultaGruposRiesoBuroResponse consultaGruposRiesoBuroResponse;
			consultaGruposRiesoBuroResponse = service.consultaGruposRiesoBuro(parameters);
			 
			if(consultaGruposRiesoBuroResponse!=null){
				grupo = consultaGruposRiesoBuroResponse.getCodigoGrupoRiesgoBuro()==null?"":consultaGruposRiesoBuroResponse.getCodigoGrupoRiesgoBuro().trim();
				//consultaGruposRiesoBuroResponse.getDescripcionGrupoRiesgoBuro();
			}
		} catch (RemoteException e) {
			logger.error(StringUtil.getStackTrace(e));
			
			if(StringUtil.getStackTrace(e)!= null){
				String msj = extracMsjError(StringUtil.getStackTrace(e));
				//este mensaje es obtenido por que la empresa no tiene grupo buro
				//CSE00070NO EXISTEN OPERACIONES PARA ESTE CLIENTE 
				if(ValidateUtil.countMatches("CSE00070", msj.toUpperCase())>0){
					logger.info("no se encontro un codigo de grupo buro asignar vacio");
					grupo="";
				}else{
					logger.info("no se encontro un codigo de grupo buro");
					throw new WSException(msj);
				}
			}else{
				throw new WSException("Se produjo en error en la comunicación con HOST");
			}
		}catch (Exception e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		return grupo;
	}
	
	//MCG PARA TEST
	//TEST:: PARA PRODUCCION CAMBIAR consultarGrupoBuro A consultarGrupoBuro_Test
	public static String consultarGrupoBuro_Test(String ruc,
			String sUsuario,
			String pathWebService ) throws WSException{
		String grupo="1";
		return grupo;
	}

	/**
     * agrega la cantidad de espacios indicados a una 
     * cadena.
     * 
     * @param cadena
     * @param num
     * @return
     */
    public static String completarEspaciosDerecha(String cadena, 
    									   int num){
    	String espacios = "";
    	for(int i=0; i<num; i++){
    		espacios+=" ";
    	}
    	if(cadena== null){
    		cadena = "";
    	}
    	return cadena.concat(espacios);
    }
    
    public static String extracMsjError(String msj){
    	String error  = "Se produjo en error en la comunicación con HOST";
    	Object[] list = ValidateUtil.pregMatchs("<mensajeError>.*</mensajeError>", msj);
    	if(list !=null &&
    	   list.length>0){
    		error = ValidateUtil.pregMatchs("<mensajeError>.*</mensajeError>", msj)[0].toString();
    		error = error.replaceAll("mensajeError", "");
    		error = error.replace("<", "");
    		error = error.replace(">", "");
    		error = error.replace("/", "");
    	}
		
    	return error;
    }
    
	//INI MCG20141020
	public static HashMap<String, String>  obtenerGrupoEconomicoWS(
																	String numCliente,
																	String teclaPulsada, 
																	String usuario, 
																	String control,
																	String pathWebServicePEC6, 
																	String pathWebService)
																	throws WSException {
		numCliente = numCliente.toUpperCase();
		HashMap<String,String> resultado = new HashMap<String,String>();
		
		return resultado;
	}
	
	//FIN MCG20141020
}
