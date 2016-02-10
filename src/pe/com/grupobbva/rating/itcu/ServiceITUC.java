package pe.com.grupobbva.rating.itcu;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
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
import pe.com.stefanini.core.util.StringUtil;

public class ServiceITUC {
	
	/**
	 * 
	 * @param header
	 * @return IH que se envia a HOST 
	 */
	
	Logger logger = Logger.getLogger(this.getClass());
	
	
	private String crearIH(HeaderITUC header){
		
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
	private String crearME(InputITUC input,HeaderITUC header){
				
		StringBuilder me = new StringBuilder();
		me.append(rellenarCadena(String.valueOf(Constantes.LONGITUD_MENSAJE),4,Constantes.RELLENO_CERO,Constantes.IZQUIERDA));
		me.append(header.getTipoMensajeME());
		
		me.append(rellenarCadena(Constantes.FILLER_ITCU_REQ,12,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(input.getCodigoCliente(),8,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(input.getAnioEjercicio(),4,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(input.getMesEjercicio(),2,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(input.getTipoEmpresa());
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(input.getModelo(),4,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(input.getCodigoAnalisis(),4,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(input.getOpcion());
		
		me.append(rellenarCadena(Constantes.ESPACIO_BLANCO,3,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		me.append(rellenarCadena(Constantes.ORIGEN_ITCU_REQ,2,Constantes.ESPACIO_BLANCO,Constantes.DERECHA));
		
		return me.toString();
	}
	
	
	/**
	 * Transacción que devuelve los balances
	 * 
	 * @param header
	 * @param input
	 * @return
	 */
	public List<OutputITUC> invocaITUC(HeaderITUC header,InputITUC input,String ip_port,String encoding){
		
		List<OutputITUC> listaITUC = new ArrayList<OutputITUC>();
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
				retorna = retorna.replace("</OC>","|");
				retorna = retorna.replace("<OC>","|");
				retorna = retorna.replace("#","Ñ");
				
				
				StringTokenizer tokens=new StringTokenizer(retorna, "|");
				OutputITUC salida=null;
				listaITUC.clear();
		        while(tokens.hasMoreTokens()){
		            String str=tokens.nextToken();
		            salida = new OutputITUC();
		            
		            if(Constantes.TIPO_REGISTRO_03.equals(str.substring(25,27))){
		            	salida.setTipoRegistro(str.substring(25,27));//2
		            	salida.setCodigoCuenta(str.substring(28,44));//16
		            	salida.setImporte(str.substring(45,61));//16
		            	salida.setIndicadorAjuste(str.substring(62,63));//1
		            	salida.setDescripcionCuenta(str.substring(64,109));//45
		            	
		            	listaITUC.add(salida);
		            }else{
		            	if(Constantes.TIPO_REGISTRO_01.equals(str.substring(25,27))){
		            		salida.setTipoRegistro(str.substring(25,27));//2
			            	salida.setCodigoCliente(str.substring(28,36));//8
			            	salida.setAnio(str.substring(37,41));//4
			            	salida.setMes(str.substring(42,44));//2
			            	salida.setTipoEmpresa(str.substring(45,46));//1
			            	salida.setCodigoModelo(str.substring(47,51));//4
			            	salida.setCodigoAnalisis(str.substring(52,56));//4
			            	salida.setInflacionActual(str.substring(57,66));//9
			            	salida.setInflacionAcumulada(str.substring(67,76));//9
			            	
			            	salida.setCalificacionRating(str.substring(77,82));//5
			            	salida.setCodigoEstado(str.substring(83,88));//5
			            	salida.setFacturacion(str.substring(89,105));//16

			            	listaITUC.add(salida);

		            	}else{
		            		if(Constantes.TIPO_REGISTRO_02.equals(str.substring(25,27))){
		            			
		            			salida.setTipoRegistro(str.substring(25,27));//2
			            		salida.setCodigoEstudio(str.substring(28,36));//8
			            		salida.setFechaAnalisis(str.substring(37,47));//10
			            		salida.setPuntuacionCuantitativa(str.substring(48,64));//16
			            		salida.setPuntuacionCualitativa(str.substring(65,81));//16
			            		salida.setPuntuacionCuliTotal(str.substring(82,98));//16
			            		salida.setBuro(str.substring(99,115));//16
			            		salida.setPeriodo(str.substring(116,122));//6
			            		salida.setEstado(str.substring(123,124));//1
			            		
			            		listaITUC.add(salida);
		            		}
		            	}
		            }
		        }
		        
			}
		
		} catch (RemoteException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (ServiceException e) {
			logger.error(StringUtil.getStackTrace(e));
		}
		
		return listaITUC;
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

	    //result = new String(bytes, Charset.forName(charsetName));
	    result = new String(bytes,charsetName);

	    
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
		
		ServiceITUC ser = new ServiceITUC();
		
		
		String ih_ituc = "26D90L    D90L    P016359 10010149ITUC    000014111OPFNR";
		//String me_itcu = "0055B               09336796   2013   12   0   3004   1     ";
		
		String me_ituc = "0062B               04735927   2009   12   0   3003   3103   1     ";
		
		
		HeaderITUC header = new HeaderITUC();
		header.setTerminalLogico("%420");
		header.setTerminalContable("%420");
		header.setUsuario("P006125");
		header.setCodigoTransaccion("ITUC");
		header.setTipoMensaje("1");
		header.setTipoProceso("O");
		header.setCanalComunicacion("PF");
		header.setIndicadorPreformato("N");
		header.setTipoMensajeME("B");
						
		header.setDistQnameIn("QLP.SPFI.RESPA");
		header.setDistRqnameOut("QRP.SPFI.ENVIO.MPP1");
		header.setHostRqnameOut("QRP.SPFI.RESP.QMPPINT1");
		header.setHostQmgrName("MPP1");
		
		InputITUC input =  new InputITUC();
		input.setCodigoCliente("02479036");
		input.setAnioEjercicio("2011");
		input.setMesEjercicio("12");
		input.setTipoEmpresa("0");
		input.setModelo("3004");
		input.setCodigoAnalisis("3104");
		input.setOpcion("1");
		
		//String ih_itcu = "26D90L    D90L    P016359 10010149ITCU    000013411OPFNR";
		//System.out.println(ser.crearIH(header));
		//String me_itcu = "0055B               04735927   2013   12   0   3004   1     ";
		//System.out.println(ser.crearME(input,header));
		
		List<OutputITUC> sal = ser.invocaITUC(header, input,"http://118.180.54.185:7801",Constantes.ENCODING_CONVERSION);
		/*System.out.println(st);
		System.out.println(ser.rellenarCadena(st,15,"0",Constantes.DERECHA));
		*/
		for(OutputITUC out : sal){
			System.out.println(ToStringBuilder.reflectionToString(out,ToStringStyle.MULTI_LINE_STYLE));
		}
	}

}

