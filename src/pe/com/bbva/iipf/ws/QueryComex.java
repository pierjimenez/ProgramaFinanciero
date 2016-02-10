package pe.com.bbva.iipf.ws;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.kc10comex;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.grupobbva.pf.kc10.CtBodyRq;
import pe.com.grupobbva.pf.kc10.CtConKC10Rq;
import pe.com.grupobbva.pf.kc10.CtConKC10Rs;
import pe.com.grupobbva.pf.kc10.MS_KC_KC10PortTypeProxy;
import pe.com.grupobbva.xsd.ps9.CtHeaderRq;
import pe.com.stefanini.core.util.StringUtil;

public class QueryComex {
	private static Logger logger = Logger.getLogger(QueryComex.class);
	public static HashMap<String, Object> consularDatosComex(String codCentral,String anio,String TipoComex,	ConfiguracionWS configuracionWS,String endPoint) throws Exception{
		
		HashMap<String,Object> resultado = new HashMap<String,Object>();
		List<kc10comex> listakc10comex=new ArrayList<kc10comex>();
		MS_KC_KC10PortTypeProxy serivce=new MS_KC_KC10PortTypeProxy(endPoint);
		CtConKC10Rq req = new CtConKC10Rq();
		
		CtHeaderRq header = new CtHeaderRq();
		header.setUsuario(configuracionWS.getCodigoUsuario());
		header.setTerminalContable(configuracionWS.getTerminalContable());
		header.setTerminalLogico(configuracionWS.getTerminalLogico());
		header.setOpcionAplicacion(configuracionWS.getCodigoAplicacion());
		req.setHeader(header);
		
		
		CtBodyRq body = new CtBodyRq();
	  
		body.setNumeroDocumento(codCentral);		
		body.setAnio(anio);
		body.setExpimp(TipoComex); //I o E	
		
		req.setData(body);
		
		CtConKC10Rs  conKC10Rs=new CtConKC10Rs();
		
		String codigo="0000";
		String descError="";
		try {
			conKC10Rs =serivce.consultaPresupuestoCliente(req);
			
			if (conKC10Rs!=null){
				
				if (conKC10Rs.getData()!=null){				
					
					kc10comex ocomexHost=new kc10comex();
					ocomexHost.setAnio(anio);
					ocomexHost.setCodigoCentral(codCentral);					
					ocomexHost.setTipoComex(TipoComex);				
					String detalle01=conKC10Rs.getData().getDetalle01();
					detalle01=StringUtils.leftPad(detalle01, 56, " ");					
					ocomexHost.setDescripcion(detalle01.substring(0, 20)); //20
					ocomexHost.setCantidad(detalle01.substring(21, 26));  //5
					ocomexHost.setImporteAcumulado(detalle01.substring(27, 41));//14
					ocomexHost.setComisionesAcumuladas(detalle01.substring(42, 56));//14					
					listakc10comex.add(ocomexHost);
					
					kc10comex ocomexHost2=new kc10comex();
					ocomexHost2.setAnio(anio);
					ocomexHost2.setCodigoCentral(codCentral);					
					ocomexHost2.setTipoComex(TipoComex);
					String detalle02=conKC10Rs.getData().getDetalle02();
					detalle02=StringUtils.leftPad(detalle02, 56, " ");
					ocomexHost2.setDescripcion(detalle02.substring(0, 20)); 
					ocomexHost2.setCantidad(detalle02.substring(21, 26));
					ocomexHost2.setImporteAcumulado(detalle02.substring(27, 41));
					ocomexHost2.setComisionesAcumuladas(detalle02.substring(42, 56));
					listakc10comex.add(ocomexHost2);
					
					kc10comex ocomexHost3=new kc10comex();
					ocomexHost3.setAnio(anio);
					ocomexHost3.setCodigoCentral(codCentral);					
					ocomexHost3.setTipoComex(TipoComex);
					String detalle03=conKC10Rs.getData().getDetalle03();
					detalle03=StringUtils.leftPad(detalle03, 56, " ");
					ocomexHost3.setDescripcion(detalle03.substring(0, 20).trim());
					ocomexHost3.setCantidad(detalle03.substring(21, 26).trim());
					ocomexHost3.setImporteAcumulado(detalle03.substring(27, 41).trim());
					ocomexHost3.setComisionesAcumuladas(detalle03.substring(42, 56).trim());
					listakc10comex.add(ocomexHost3);
					
					
					kc10comex ocomexHost4=new kc10comex();
					ocomexHost4.setAnio(anio);
					ocomexHost4.setCodigoCentral(codCentral);					
					ocomexHost4.setTipoComex(TipoComex);
					String detalle04=conKC10Rs.getData().getDetalle04(); 
					detalle04=StringUtils.leftPad(detalle04, 56, " ");
					ocomexHost4.setDescripcion(detalle04.substring(0, 20).trim());
					ocomexHost4.setCantidad(detalle04.substring(21, 26).trim());
					ocomexHost4.setImporteAcumulado(detalle04.substring(27, 41).trim());
					ocomexHost4.setComisionesAcumuladas(detalle04.substring(42, 56).trim());
					listakc10comex.add(ocomexHost4);
					
					kc10comex ocomexHost5=new kc10comex();
					ocomexHost5.setAnio(anio);
					ocomexHost5.setCodigoCentral(codCentral);					
					ocomexHost5.setTipoComex(TipoComex);
					String detalle05=conKC10Rs.getData().getDetalle05();
					detalle05=StringUtils.leftPad(detalle05, 56, " ");
					ocomexHost5.setDescripcion(detalle05.substring(0, 20).trim());
					ocomexHost5.setCantidad(detalle05.substring(21, 26).trim());
					ocomexHost5.setImporteAcumulado(detalle05.substring(27, 41).trim());
					ocomexHost5.setComisionesAcumuladas(detalle05.substring(42, 56).trim());
					listakc10comex.add(ocomexHost5);
					
					kc10comex ocomexHost6=new kc10comex();
					ocomexHost6.setAnio(anio);
					ocomexHost6.setCodigoCentral(codCentral);					
					ocomexHost6.setTipoComex(TipoComex);
					String detalle06=conKC10Rs.getData().getDetalle06();
					detalle06=StringUtils.leftPad(detalle06, 56, " ");
					ocomexHost6.setDescripcion(detalle06.substring(0, 20).trim());
					ocomexHost6.setCantidad(detalle06.substring(21, 26).trim());
					ocomexHost6.setImporteAcumulado(detalle06.substring(27, 41).trim());
					ocomexHost6.setComisionesAcumuladas(detalle06.substring(42, 56).trim());
					listakc10comex.add(ocomexHost6);
					
					resultado.put("ListaComexHost", listakc10comex);
					resultado.put("codigoError", codigo);
					resultado.put("descripcionError", "");
					
				}else{
					codigo="9999";
					descError=Constantes.COD_ERROR_COMEX;
					if (conKC10Rs.getHeader()!=null){
						codigo=conKC10Rs.getHeader().getCodigo().toString().trim();
						descError=conKC10Rs.getHeader().getDescripcion()==null?"":conKC10Rs.getHeader().getDescripcion().toString().trim();
					}
					
					listakc10comex=new ArrayList<kc10comex>();
					resultado.put("ListaComexHost", listakc10comex);
					resultado.put("codigoError", codigo);
					resultado.put("descripcionError", descError);											
					
				}				
				
			}else{		
				listakc10comex=new ArrayList<kc10comex>();
				resultado.put("ListaComexHost", listakc10comex);
				resultado.put("codigoError", "9999");
				resultado.put("descripcionError", Constantes.COD_ERROR_COMEX+"No se encontrarón los datos comex para la empresa "+ codCentral);
				
			}
			
			
			
		} catch (RemoteException er) {			
			er.printStackTrace();
			listakc10comex=new ArrayList<kc10comex>();
			resultado.put("ListaComexHost", listakc10comex);
			resultado.put("codigoError", "9999");
			resultado.put("descripcionError", Constantes.COD_ERROR_COMEX+er.getMessage());
			logger.error(StringUtil.getStackTrace(er));
		} catch (Exception e) {			
			e.printStackTrace();
			listakc10comex=new ArrayList<kc10comex>();
			resultado.put("ListaComexHost", listakc10comex);
			resultado.put("codigoError", "9999");
			resultado.put("descripcionError", Constantes.COD_ERROR_COMEX+e.getMessage());
			logger.error(StringUtil.getStackTrace(e));
		}
		
		return resultado;
	
	}


}
