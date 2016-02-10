package pe.com.grupobbva.rating.pe21;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import pe.com.grupobbva.rating.ps9.BodyRQ;
import pe.com.grupobbva.rating.ps9.BodyRS;
import pe.com.grupobbva.rating.ps9.HeaderRQ;
import pe.com.grupobbva.rating.ps9.MS_PS9_Service;
import pe.com.grupobbva.rating.ps9.MS_PS9_ServiceLocator;
import pe.com.grupobbva.rating.ps9.Ps9RQ;
import pe.com.grupobbva.rating.ps9.Ps9RS;
import pe.com.stefanini.core.host.Util;

public class ServicePE21 {
	
Logger logger = Logger.getLogger(this.getClass());
	
	
	private String crearIH(HeaderPE21 header){
		
		StringBuilder ih = new StringBuilder();
		ih.append(Constantes.INDENTIFICACION_PROTOCOLO);
		ih.append(rellenarCadena(header.getTerminalLogico(),8,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		ih.append(rellenarCadena(header.getTerminalContable(),8,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		ih.append(rellenarCadena(header.getUsuario(),8,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		ih.append(Constantes.NUMERO_SECUENCIA);
		ih.append(rellenarCadena(header.getCodigoTransaccion(),8,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		ih.append(Constantes.OPCION_APLICACION);
		ih.append(rellenarCadena(String.valueOf(Constantes.LONGITUD_IH+Constantes.LONGITUD_MENSAJE),5,Constantes.RELLENO_CERO,Constantes.IZQUIERDA));
		ih.append(Constantes.INDICADOR_COMMIT);
		ih.append(header.getTipoMensaje());
		ih.append(header.getTipoProceso());
		ih.append(header.getCanalComunicacion());
		ih.append(header.getIndicadorPreformato());
		ih.append(Constantes.LENGUAJE);
		
		return ih.toString();
		
	}
	
	/**
	 * 
	 * @param input
	 * @param header
	 * @return ME que se envia a HOST
	 */
	private String crearME(InputPE21 input,HeaderPE21 header){
				
		StringBuilder me = new StringBuilder();
		me.append(rellenarCadena(String.valueOf(Constantes.LONGITUD_MENSAJE),4,Constantes.RELLENO_CERO,Constantes.IZQUIERDA));
		me.append(header.getTipoMensajeME());
		
		if(Constantes.TD_CODDIGO_CENTRAL.equals(input.getTipoDocumentoIdentidad())){
			me.append(rellenarCadena(Constantes.FILLER_PE21_REQ,15,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
			me.append(rellenarCadena(input.getCodigoCentral(),8,Constantes.RELLENO_CERO,Constantes.IZQUIERDA));
			me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,613,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
			
		}else{
			if(Constantes.TD_RUC.equals(input.getTipoDocumentoIdentidad()) && Constantes.TAMANIO_RUC==input.getNumeroDocumentoIdentidad().length()){
					me.append(rellenarCadena(Constantes.FILLER_PE21_REQ,176,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
					me.append(input.getTipoDocumentoIdentidad());
					me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
					me.append(input.getNumeroDocumentoIdentidad().trim().substring(0, 10));
					me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
					me.append(input.getNumeroDocumentoIdentidad().substring(10, 11));
					me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,442,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
			}else{
					//CUALQUIER DOCUMENTO : DNI (L) /CARNET EXTRANJERIA(E) /CARNET IDENT.MILITAR (M) /CARNET DE FUERZAS POLICIAL (T) /PASAPORTE(P)
					me.append(rellenarCadena(Constantes.FILLER_PE21_REQ,176,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
					me.append(input.getTipoDocumentoIdentidad());
					me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
					me.append(input.getNumeroDocumentoIdentidad().trim());
					int tamanio = input.getNumeroDocumentoIdentidad().trim().length();
					me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,(456-tamanio),Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
			}
		}

		return me.toString();
	}
	
	
	/**
	 * Transacción que devuelve los balances
	 * 
	 * @param header
	 * @param input
	 * @return
	 */
	public OutputPE21 invocaPE21(HeaderPE21 header,InputPE21 input,String ip_port,String encoding){
		
		OutputPE21 salida=null;
		try {
			MS_PS9_Service service = new MS_PS9_ServiceLocator(ip_port);
			
			HeaderRQ headerRQ = new HeaderRQ();
			headerRQ.setDist_qname_in(header.getDistQnameIn());
			headerRQ.setDist_qname_out(header.getDistRqnameOut());
			headerRQ.setHost_qmanager(header.getHostQmgrName());
			headerRQ.setHost_qname_out(header.getHostRqnameOut());
	
			BodyRQ bodyRQ = new BodyRQ();
			bodyRQ.setOPCION(Constantes.OPCION);
			bodyRQ.setHOST_RQ(Constantes.HOST_RQ);
			bodyRQ.setIH(crearIH(header));
			bodyRQ.setME(crearME(input,header));
			
			
			Ps9RQ request = new Ps9RQ();
			request.setHeader(headerRQ);
			request.setBody(bodyRQ);
			Ps9RS response;
			
			response = service.getMS_PS9_Port().sendHost(request);
		
			BodyRS bodyRS = response.getBody();
			
			if (bodyRS != null) {
				String lineInfo = bodyRS.getTramaRSBLOB();
				String retorna = convert(hexStringToStringArray(lineInfo),encoding);// Constantes.ENCODING_CONVERSION
				retorna = retorna.replace("</SG>","|");
				retorna = retorna.replace("<SG>","|");
				retorna = retorna.replace("#","Ñ");
				
				
				StringTokenizer tokens=new StringTokenizer(retorna, "|");
				
				tokens.nextToken();
		        String str=tokens.nextToken();
		        
		        salida = new OutputPE21();
		        
		        salida.setPrf_o_numcli(str.substring(25,33));
		        salida.setPrf_o_sujgrup(str.substring(36,37));
		        salida.setPrf_o_sujsubg1(str.substring(40,42));
		        salida.setPrf_o_desujeto(str.substring(45,59));
		        salida.setPrf_o_rfc(str.substring(62,72));
		        salida.setPrf_o_homonimi(str.substring(75,79));
		        salida.setPrf_o_iugestor(str.substring(82,88));
		        salida.setPrf_o_iempresa(str.substring(91,95));
		        salida.setPrf_o_ncempres(str.substring(98,112));
		        salida.setPrf_o_isucursa(str.substring(115,119));
		        salida.setPrf_o_nsucursa(str.substring(122,132));
		        salida.setPrf_o_itipocte(str.substring(135,136));
		        salida.setPrf_o_ntipocte(str.substring(139,149));
		        salida.setPrf_o_iorigen(str.substring(152,153));
		        salida.setPrf_o_norigen(str.substring(156,170));
		        salida.setPrf_o_feantig(str.substring(173,183));
		        salida.setPrf_o_itipiden(str.substring(186,187));
		        salida.setPrf_o_claident(str.substring(190,200));
		        salida.setPrf_o_digident(str.substring(203,204));
		        salida.setPrf_o_secuiden(str.substring(207,209));
		        salida.setPrf_o_ntipiden(str.substring(212,227));
		        salida.setPrf_o_fveniden(str.substring(230,240));
		        salida.setPrf_o_titulo(str.substring(243,248));
		        salida.setPrf_o_nombre(str.substring(251,271));
		        salida.setPrf_o_priape(str.substring(274,294));
		        salida.setPrf_o_segape(str.substring(297,317));
		        salida.setPrf_o_nombtarj(str.substring(320,346));
		        salida.setPrf_o_razsoc(str.substring(349,409));
		        salida.setPrf_o_estcivil(str.substring(412,413));
		        salida.setPrf_o_destcivil(str.substring(416,426));
		        salida.setPrf_o_sexo(str.substring(429,430));
		        salida.setPrf_o_dsexo(str.substring(433,443));
		        salida.setPrf_o_fnacimi(str.substring(446,456));
		        salida.setPrf_o_girobm(str.substring(459,470));
		        salida.setPrf_o_dgirobm(str.substring(473,500));
		        salida.setPrf_o_irelpat(str.substring(503,504));
		        salida.setPrf_o_nrelpat(str.substring(507,517));
		        salida.setPrf_o_ocupacio(str.substring(520,523));
		        salida.setPrf_o_docupacio(str.substring(526,536));
		        salida.setPrf_o_icalicte(str.substring(539,541));
		        salida.setPrf_o_ncalicte(str.substring(544,554));
		        salida.setPrf_o_inacres(str.substring(557,561));
		        salida.setPrf_o_nnacres(str.substring(564,574));
		        salida.setPrf_o_sprivil(str.substring(577,578));
		        salida.setPrf_o_fealtcli(str.substring(581,591));
		        salida.setPrf_o_feulbaja(str.substring(594,604));
		        salida.setPrf_o_feulmod(str.substring(607,617));
		        salida.setPrf_o_numter(str.substring(620,624));
		        salida.setPrf_o_horulmod(str.substring(627,635));
		        salida.setPrf_o_usuario(str.substring(638,646));

			}
		
		} catch (RemoteException e) {
			//logger.error(StringUtil.getStackTrace(e));
		} catch (ServiceException e) {
			//logger.error(StringUtil.getStackTrace(e));
		}
		
		return salida;
	}
	
	
	/*
	 *Rellena una cadena segun el caracter y tamanio especificado
	 **/
	public String rellenarCadena(String cadena,Integer tamanio,String caracter,String izqDer){
		StringBuilder salida = new StringBuilder();
		if(tamanio>cadena.length()){
			if(Constantes.DERECHA.equals(izqDer)){
				salida.append(cadena);
			}	
			for(int i=0;i<tamanio-cadena.length();i++){
				salida.append(caracter);
			}
			if(Constantes.IZQUIERDA.equals(izqDer)){
				salida.append(cadena);
			}
		}else{
			salida.append(cadena);
		}
		return salida.toString();
	}
	
	public static String convert(String[] hexDigits, String charsetName){
		String result="";
		try{
		    byte[] bytes = new byte[hexDigits.length];
	
		    for(int i=0;i<bytes.length;i++)
		        bytes[i] = Integer.decode("0x"+hexDigits[i]).byteValue();	    
		    result = new String(bytes,charsetName);
		    //result = new String(bytes, Charset.forName(charsetName));
		}catch (Exception e) {
			e.printStackTrace();
		}
	    return result;
	}
	
	
	public static String[] hexStringToStringArray(String lineInfo) {
		char[] caracteres = lineInfo.toCharArray();
		int tamanho = (int) (caracteres.length/2);
		
		String[] valoresHex = new String[tamanho];
		int a = 0;
		for (int i=0; i< caracteres.length; i=i+2) {
			valoresHex[a] = caracteres[i]+""+caracteres[i+1];
			if("00".equalsIgnoreCase(valoresHex[a])) valoresHex[a] = "40";
			a++;
		}
		return valoresHex;
	}
	
	public static void main(String[] arg){
		
		ServicePE21 ser = new ServicePE21();
		
		//IH	
		//Identificación de Protocolo  CHAR(2) = 26   (Constante)  
		//Terminal lógico              CHAR(8) = D90L
		//Terminal Contable 		   CHAR(8) = D90L
		//Usuario       			   CHAR(8) = P007395
		//Numero de Secuencia		   CHAR(8) = 10010149
		//Código de Transacción		   CHAR(8) = PEC6  
		//Opción de la aplicación	   CHAR(2) = 00 (default 00)
		//Longitud de toda la trama    DECI(5) = 01183
		//Indicador de Commit          CHAR(1) = 1 (Constante)
		//Tipo de Mensaje              CHAR(1) = 1 (default 1)   - VALORES : 1(NUEVA SOL),2(AUTORIZACION),3(CONITNUACION CONVERSA),5,6,7
		//Tipo de Proceso			   CHAR(1) = O (default O)   - VALORES : Online(O) / Offline(F)
		//Canal de Comunicación        CHAR(2) = PF 
		//Indicador de Preformato	   CHAR(1) = N - VALORES : N,S
		//Lenguaje                     CHAR(1) = R 
	
		String ih = "26D90L    D90L    P007395 10010149PEC6    000118311OPFNR";
	//ME
		//Longitud de mensaje          DECI(4) = 1104
		//Tipo de mensaje 			   CHAR(1) = C  - VALORES : Copy(C) / BMS(B)
		//Mensaje de entrada  (Obs: Longitud máxima de cada campo 143 y longitud Total  de 9867)  
		//

		
		String ih_pe21 = "26D90L    D90L    P007395 10010149PE21    000071517OPFNR";
		                  
		
		//String me_itcu = "0055B               09336796   2013   12   0   3004   1     ";
		
		String me_pe21_cen = "0636C               03355306                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ";
		String me_pe21_dni = "0636C                                                                                                                                                                                L   21975142                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
		String me_pe21_ruc = "0636C                                                                                                                                                                                R   1073657546   4                                                                                                                                                                                                                                                                                                                                                                                                                                                          ";
		
		
		int intterminalLogico=Util.randInt(420, 469);
		System.out.println("TerminalLogico:"+ String.valueOf(intterminalLogico));
		HeaderPE21 header = new HeaderPE21();
		header.setTerminalLogico("%"+String.valueOf(intterminalLogico));//%420-%469
		header.setTerminalContable("%420");
		header.setUsuario("P006125");
		header.setCodigoTransaccion("PE21");
		header.setTipoMensaje("7");
		header.setTipoProceso("O");
		header.setCanalComunicacion("PF");
		header.setIndicadorPreformato("N");
		header.setTipoMensajeME("B");
						
		header.setDistQnameIn("QLP.SPFI.RESPA");
		header.setDistRqnameOut("QRP.SPFI.ENVIO.MPP1");
		header.setHostRqnameOut("QRP.SPFI.RESP.QMPPINT1");
		header.setHostQmgrName("MPP1");
		
		InputPE21 input =  new InputPE21();
		input.setCodigoCentral("01802208");
		input.setNumeroDocumentoIdentidad("");
		input.setTipoDocumentoIdentidad(Constantes.TD_CODDIGO_CENTRAL);

		OutputPE21 salida = ser.invocaPE21(header, input, "http://118.180.54.185:7801", Constantes.ENCODING_CONVERSION);
		
		/*input.setCodigoCliente("04735927");
		input.setAnioEjercicio("2009");
		input.setMesEjercicio("12");
		input.setTipoEmpresa("0");
		input.setModelo("3003");
		input.setCodigoAnalisis("3103");
		input.setOpcion("1");*/
		
		//String ih_itcu = "26D90L    D90L    P016359 10010149ITCU    000013411OPFNR";
		//System.out.println(ser.crearIH(header));
		//String me_itcu = "0055B               04735927   2013   12   0   3004   1     ";
		//System.out.println(ser.crearME(input,header));
		
		//List<OutputPE21> sal = ser.invocaITUC(header, input,"http://118.180.36.26:7808",Constantes.ENCODING_CONVERSION);
		/*System.out.println(st);
		System.out.println(ser.rellenarCadena(st,15,"0",Constantes.DERECHA));
		*/
		/*for(OutputPE21 out : sal){
			System.out.println(ToStringBuilder.reflectionToString(out,ToStringStyle.MULTI_LINE_STYLE));
		}*/
		
		System.out.println(ToStringBuilder.reflectionToString(salida,ToStringStyle.MULTI_LINE_STYLE));
		
	}

}
