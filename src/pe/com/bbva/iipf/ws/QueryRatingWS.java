package pe.com.bbva.iipf.ws;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.ConfiguracionWSRating;
import pe.com.bbva.pfa.rating.DataRating;
import pe.com.bbva.pfa.rating.HeaderRating;
import pe.com.bbva.pfa.rating.InputRating;
import pe.com.bbva.pfa.rating.RatingService;
import pe.com.bbva.pfa.rating.Ratingws;
import pe.com.stefanini.core.util.StringUtil;

public class QueryRatingWS {
	private static Logger logger = Logger.getLogger(QueryRatingWS.class);

	public static List<Ratingws> obtenerRatingWS(ConfiguracionWSRating oConfiguracion,List<Empresa> olistaEmpresa,String flagEmpresaGrupo){
		RatingService service = new RatingService();
		InputRating input = new InputRating();
		HeaderRating header = new HeaderRating();
		DataRating data =  new DataRating();
		List<Ratingws> olistaRatingWS =new ArrayList<Ratingws>();
		
		
		String TIPO_EMPRESA =oConfiguracion.getTipoEmpresa();//"I";//PARAMETRO DE BD PF
		String TIPO_GRUPO =oConfiguracion.getTipoGrupo();//"C";//PARAMETRO DE BD PF
		
		//static public String codigoCliente = "04735927";//VALOR ENVIADO POR EL APLICATIVO PF
		String usuario =oConfiguracion.getCodigoUsuario();// "P016359";//VALOR ENVIADO POR EL APLICATIVO PF
		//static public String flagEmpresaGrupo  ="I"; //VALOR ENVIADO POR EL APLICATIVO PF
		
		String OPCION_APLICACION =oConfiguracion.getOpcionAplicacion();//"00";//PARAMETRO DE BD PF
		String TERMINAL_LOGICO =oConfiguracion.getTerminalLogico();//"D90L";//PARAMETRO DE BD PF 
		String TERMINAL_CONTABLE =oConfiguracion.getTerminalContable();//"D90L";//PARAMETRO DE BD PF
		
		String ITDA_APLICATIVO =oConfiguracion.getItdaAplicativo();//"RT";//PARAMETRO DE BD PF
		String ITDA_TRATAMIENTO =oConfiguracion.getItdaTratamiento();//"C";//PARAMETRO DE BD PF
		String ITDA_IP_PORT = oConfiguracion.getItdaIpPort();//"http://118.180.36.26:7801";//PARAMETRO DE BD PF

		
		String ITCU_TRANSACCION =oConfiguracion.getItcuTransaccion();//"ITCU";//PARAMETRO DE BD PF
		String ITCU_TIPO_MENSAJE =oConfiguracion.getItcuTipoMensaje();//"1";//PARAMETRO DE BD PF
		String ITCU_TIPO_PROCESO =oConfiguracion.getItcuTipoProceso();//"O";//PARAMETRO DE BD PF
		String ITCU_CANAL_COMUNICACION =oConfiguracion.getItcuCanalComunicacion();//"PF";//PARAMETRO DE BD PF
		String ITCU_INDICADOR_PREFORMATO =oConfiguracion.getItcuIndicadorPreformato();//"N";//PARAMETRO DE BD PF
		String ITCU_TIPO_MENSAJE_ME =oConfiguracion.getItcuTipoMensajeMe();//"B";//PARAMETRO DE BD PF
		String ITCU_DIST_QNAME_IN =oConfiguracion.getItcuDistQnameIn();//"QLT.GEC.RESPA";//PARAMETRO DE BD PF
		String ITCU_DIST_RQNAME_OUT =oConfiguracion.getItcuDistRqnameOut();//"QRT.GEC.ENVIO.MPD1";//PARAMETRO DE BD PF
		String ITCU_HOST_RQNAME_OUT =oConfiguracion.getItcuHostRqnameOut();//"QRT.GEC.RESP.QMPCINT8";//PARAMETRO DE BD PF
		String ITCU_HOST_QMGR_NAME =oConfiguracion.getItcuHostQmgrName();//"MPD1";//PARAMETRO DE BD PF
		
		String ITCU_TIPO_EMPRESA =oConfiguracion.getItcuTipoEmpresa();//"0";//PARAMETRO DE BD PF
		String ITCU_OPCION =oConfiguracion.getItcuOpcion();//"1";//PARAMETRO DE BD PF
		
		String ITCU_IP_PORT = oConfiguracion.getItcuIpPort();//"http://118.180.36.26:7808" //PARAMETRO DE BD PF
		String ITCU_ENCODING = oConfiguracion.getItcuEncoding();//"IBM-500"//PARAMETRO DE BD PF
			
		
		
				
		
		//INICIO : HEADER
		header.setOpcionAplicacion(OPCION_APLICACION);
		header.setTerminalContable(TERMINAL_CONTABLE);
		header.setTerminalLogico(TERMINAL_LOGICO);
		
		//ITDA
		header.setItdaAplicativo(ITDA_APLICATIVO);
		header.setItdaTratamiento(ITDA_TRATAMIENTO);
		header.setItdaIpPort(ITDA_IP_PORT);
				
		//ITCU
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
		header.setItcuEncoding(ITCU_ENCODING);
		
		input.setHeader(header);
		//FIN : HEADER
		
		
		//INICIO : DATA
		List<DataRating> listaRa = new ArrayList<DataRating>();
		for (Empresa oempresa : olistaEmpresa){
			String codigoCliente=oempresa.getCodigo();
			data =  new DataRating();
			data.setUsuario(usuario);
			data.setCodigoCliente(codigoCliente);
			data.setFlagEmpresaGrupo(flagEmpresaGrupo);//empresa I  grupo C		
			listaRa.add(data);
		}
		
		
		input.setData(listaRa);

		//FIN : DATA
		try {
			olistaRatingWS = service.obtenerListadoRating(input);
			//olistaRatingWS= new ArrayList<Ratingws>();//MCG quitar luego 
			//olistaRatingWS=(List<Ratingws>)StringUtil.deserializaObjeto("D:\\profinws\\ListaRating");
			
		} catch (Exception e) {
			 
			String f=e.getMessage();
			logger.error(StringUtil.getStackTrace(e));
		}
		return olistaRatingWS;
	
		
	}
	
	
}
