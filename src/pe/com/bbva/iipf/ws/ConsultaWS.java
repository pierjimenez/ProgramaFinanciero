package pe.com.bbva.iipf.ws;

import java.rmi.RemoteException;
import java.util.HashMap;

import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.grupobbva.pf.rig4.CtBodyRq;
import pe.com.grupobbva.pf.rig4.CtConDlgRIG4Rq;
import pe.com.grupobbva.pf.rig4.CtConDlgRIG4Rs;
import pe.com.grupobbva.pf.rig4.MS_PF_RIG4PortTypeProxy;
import pe.com.grupobbva.xsd.ps9.CtHeaderRq;

public class ConsultaWS {

	
	public static HashMap<String,String> consularDatosReporteCredito2(String codCentral,	ConfiguracionWS configuracionWS) throws Exception{
//		HashMap<String,String> persona = new HashMap<String,String>();
//		MS_PF_RIG4SOAP_HTTP_PortProxy RIG4SOAP_HTTP_PortProxy= new MS_PF_RIG4SOAP_HTTP_PortProxy();
//		
//		CtConDlgRIG4Rq conDlgRIG4Rq = new CtConDlgRIG4Rq();
//		//definicion de la peticion codigo del grupo y valor del control
//		CtBodyRq bodyRq= new CtBodyRq();
//		
//		bodyRq.setCodCentral(codCentral);
//		bodyRq.setCodEjecutivoCuenta(configuracionWS.getCodigoEjecutivoCuenta());
//		bodyRq.setCodOficinaAlta(configuracionWS.getCodigoOficinaCuenta());
//		conDlgRIG4Rq.setData(bodyRq);
//		//definiendo cabecera tecla pulsada 00 o 08 y codigo de usuario host
//		CtHeaderRq header = new CtHeaderRq();
//		header.setUsuario(configuracionWS.getCodigoUsuario());
////		header.setOpcionAplicacion(configuracionWS.getCodigoAplicacion());
////		header.setTerminalContable(configuracionWS.getTerminalContable());
////		header.setTerminalLogico(configuracionWS.getTerminalLogico());
//		conDlgRIG4Rq.setHeader(header);
//		
//		
//		CtConDlgRIG4Rs conDlgRIG4Rs = new CtConDlgRIG4Rs();
//		
//		
//		
//		try {
//			CtConDlgRIG4Rs consultaDatosRCResponse = RIG4SOAP_HTTP_PortProxy.callrig4(conDlgRIG4Rq);
//				if(consultaDatosRCResponse!=null){
//					persona.put("codigoCentral", codCentral);
//					persona.put("clasificacionBanco", consultaDatosRCResponse.getData().getClasifBancoCliente()==null?"":consultaDatosRCResponse.getData().getClasifBancoCliente().trim());
//					persona.put("codRegistroGestor", consultaDatosRCResponse.getData().getCodRegistroGestor()==null?"":consultaDatosRCResponse.getData().getCodRegistroGestor().trim());
//					persona.put("nombreGestor", consultaDatosRCResponse.getData().getNomCompletoGestor()==null?"":consultaDatosRCResponse.getData().getNomCompletoGestor().trim());
//				}else{
//					throw new Exception("No se encontrarón los datos basicos para la empresa "+ codCentral);
//				}
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}
	public static HashMap<String,String> consularDatosReporteCredito(String codCentral,	ConfiguracionWS configuracionWS,String endPoint) throws Exception{
		HashMap<String,String> persona = new HashMap<String,String>();
		MS_PF_RIG4PortTypeProxy proxi= new MS_PF_RIG4PortTypeProxy(endPoint);
		
		
		
		CtConDlgRIG4Rq conDlgRIG4Rq = new CtConDlgRIG4Rq();
		//definicion de la peticion codigo del grupo y valor del control
		CtBodyRq bodyRq= new CtBodyRq();
		
		bodyRq.setCodCentral(codCentral);
		bodyRq.setCodEjecutivoCuenta(configuracionWS.getCodigoEjecutivoCuenta());
		bodyRq.setCodOficinaAlta(configuracionWS.getCodigoOficinaCuenta());
		conDlgRIG4Rq.setData(bodyRq);
		//definiendo cabecera tecla pulsada 00 o 08 y codigo de usuario host
		CtHeaderRq header = new CtHeaderRq();
		header.setUsuario(configuracionWS.getCodigoUsuario());
//		header.setOpcionAplicacion(configuracionWS.getCodigoAplicacion());
//		header.setTerminalContable(configuracionWS.getTerminalContable());
//		header.setTerminalLogico(configuracionWS.getTerminalLogico());
		conDlgRIG4Rq.setHeader(header);
		
		
		CtConDlgRIG4Rs conDlgRIG4Rs = new CtConDlgRIG4Rs();
		
		
		
		try {
			String codigo="0000";
			String descError="";
			
			CtConDlgRIG4Rs consultaDatosRCResponse = proxi.callrig4(conDlgRIG4Rq);
				if(consultaDatosRCResponse!=null){					
					persona.put("codigoCentral", codCentral);
					
					if(consultaDatosRCResponse.getData()!=null){
						
						persona.put("codigoError", codigo);
						persona.put("descripcionError", "");
						
						persona.put("clasificacionBanco", consultaDatosRCResponse.getData().getClasifBancoCliente()==null?"":consultaDatosRCResponse.getData().getClasifBancoCliente().trim());						
						persona.put("codRegistroGestor", consultaDatosRCResponse.getData().getCodRegistroGestor()==null?"":consultaDatosRCResponse.getData().getCodRegistroGestor().trim());
						persona.put("nombreGestor", consultaDatosRCResponse.getData().getNomCompletoGestor()==null?"":consultaDatosRCResponse.getData().getNomCompletoGestor().trim());
						//add
						persona.put("descOficinaAlta", consultaDatosRCResponse.getData().getDescOficinaAlta()==null?"":consultaDatosRCResponse.getData().getDescOficinaAlta().trim());//descOficinaAlta
						persona.put("codOficinaPrincipal", consultaDatosRCResponse.getData().getCodOficinaPrincipal()==null?"":consultaDatosRCResponse.getData().getCodOficinaPrincipal().trim());//codOficinaPrincipal
						persona.put("descOficinaPrincipal", consultaDatosRCResponse.getData().getDescOficinaPrincipal()==null?"":consultaDatosRCResponse.getData().getDescOficinaPrincipal().trim());//codOficinaPrincipal
						
						persona.put("codEtiqueta", consultaDatosRCResponse.getData().getCodEtiqueta()==null?"":consultaDatosRCResponse.getData().getCodEtiqueta().trim());//codEtiqueta
						persona.put("descEtiqueta", consultaDatosRCResponse.getData().getDescEtiqueta()==null?"":consultaDatosRCResponse.getData().getDescEtiqueta().trim());//descEtiqueta
					}else{
						
						if (consultaDatosRCResponse.getHeader()!=null){
							codigo=consultaDatosRCResponse.getHeader().getCodigo().toString().trim();
							descError=consultaDatosRCResponse.getHeader().getDescripcion()==null?"":consultaDatosRCResponse.getHeader().getDescripcion().toString().trim();
						}
						
						persona.put("codigoError", codigo);
						persona.put("descripcionError", descError);
												
						persona.put("clasificacionBanco", "");
						persona.put("codRegistroGestor", "");
						persona.put("nombreGestor", "");
						//add
						persona.put("descOficinaAlta", "");
						persona.put("codOficinaPrincipal", "");
						persona.put("descOficinaPrincipal", "");
						
						persona.put("codEtiqueta", "");
						persona.put("descEtiqueta", "");
						

					}
					
				
				}else{
					throw new Exception("No se encontrarón los datos basicos para la empresa "+ codCentral);
				}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return persona;
}
	public static void consultar(String codCentral,	String sUsuario){
		/*try{
			HashMap<String,String> parametros=ConsultaWS.consularDatosReporteCredito(codCentral,sUsuario,"00","00");
			Iterator<String> keys=parametros.keySet().iterator();
			
			while(keys.hasNext()){
				String key=keys.next();			
				System.out.println(key+"\t"+parametros.get(key));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	
}
