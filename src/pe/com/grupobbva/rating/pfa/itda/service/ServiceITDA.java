package pe.com.grupobbva.rating.pfa.itda.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

import pe.com.grupobbva.rating.pfa.itda.CtBodyRq;
import pe.com.grupobbva.rating.pfa.itda.CtConPFaITDaRq;
import pe.com.grupobbva.rating.pfa.itda.CtConPFaITDaRs;
import pe.com.grupobbva.rating.pfa.itda.CtEjercicio;
import pe.com.grupobbva.rating.pfa.itda.DLG_RIG7_Service;
import pe.com.grupobbva.rating.pfa.itda.DLG_RIG7_ServiceLocator;
import pe.com.grupobbva.xsd.ps9.CtHeaderRq;
import pe.com.stefanini.core.util.StringUtil;

public class ServiceITDA {
	
	public static final String MES_DICIEMBRE = "12";
	public static final String ESTADO_C = "C";
	public static final String ESTADO_RT_F ="F";
	public static final String ESTADO_RT_V ="V";
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public static final String[] LISTA_MODELOS_GRUPO_ECONOMICO = {"3020","4020"};//PARAMETRO DE BD PF
	
	public SalidaITDA obtieneListaEjercicio(CtHeaderRq header,CtBodyRq data,String ip_port){
		
		
		List<Ejercicio> listaFinalIndividual = new ArrayList<Ejercicio>();
		List<Ejercicio> listaFinalGrupo = new ArrayList<Ejercicio>();
		
		try {
			DLG_RIG7_Service service = new DLG_RIG7_ServiceLocator(ip_port);
			CtConPFaITDaRq request = new CtConPFaITDaRq();
			request.setHeader(header);
			request.setData(data);

			CtConPFaITDaRs response =  service.getDLG_RIG7_PortType().obtenerEjercicios(request);
			
			if(response!=null && response.getData()!=null){
				/*SE OBTIENE TODOS LOS EJERCICIOS QUE TIENEN EL "COD_STATUS" IGUAL A "C" Y EL
				 * COD_ESTADO_RT IGUAL A "F" Y "V"
				 */
				List<Ejercicio> listaEjercicioIndividual= new ArrayList<Ejercicio>();
				List<Ejercicio> listaEjercicioGrupo= new ArrayList<Ejercicio>();
				Ejercicio ejercicio=null;
				boolean flagGrupoIndividual=false;
				for(CtEjercicio e : response.getData()){
					
					
					flagGrupoIndividual=false;
					if(ESTADO_C.equals(e.getCodStatus()) && 
					   (ESTADO_RT_F.equals(e.getCodEstadoRt()) || ESTADO_RT_V.equals(e.getCodEstadoRt()))){
						
						for(String modelo:LISTA_MODELOS_GRUPO_ECONOMICO){
							if(modelo.equals(e.getCodTmodelo())){
								flagGrupoIndividual = true;
								break;
							}
						}
						
						ejercicio = new Ejercicio();
						ejercicio.setAnio(e.getAnio());
						ejercicio.setMes(e.getMes());
						ejercicio.setCodStatus(e.getCodStatus());
						ejercicio.setCodTmodelo(e.getCodTmodelo());
						ejercicio.setCodAnalisis(e.getCodAnalisis());
						ejercicio.setCodBalance(e.getCodBalance());
						ejercicio.setCodTipBalaDesc(e.getCodTipBalaDesc());
						
						if(flagGrupoIndividual){ //TRUE : SI ES GRUPO
							ejercicio.setFlagGrupoEjercicio("C");//CONSOLIDADO O GRUPO
							listaEjercicioGrupo.add(ejercicio);
						}else{
							ejercicio.setFlagGrupoEjercicio("I");//INDIVIDUAL O EMPRESA
							listaEjercicioIndividual.add(ejercicio);
						}
					}
				}
				
				Ejercicio situcional=null;
				if(listaEjercicioIndividual!=null && !listaEjercicioIndividual.isEmpty()){
					//INICIO : PERIODOS PARA EMPRESA
					//ORDENANDO LISTA DE EJERCICIOS POR AÑO y MES INDIVIDUAL
					Collections.sort(listaEjercicioIndividual, Ejercicio.EjerAnioMesComparator);
					
					//OBTENIENDO LOS 3 PERIODOS CERRADOS INDIVIDUAL
					List<Ejercicio> listaSobranteIndividual = new ArrayList<Ejercicio>(listaEjercicioIndividual);
					listaFinalIndividual = obtenerPeriodosCerrados(listaEjercicioIndividual,listaSobranteIndividual);
					
					//OBTENIENDO EL PERIODO SITUACIONAL INDIVIDUAL
					situcional = obtenerPeriodoSituacional(listaSobranteIndividual,listaFinalIndividual);
					listaFinalIndividual.add(situcional);
					//FIN : PERIODOS PARA EMPRESA
				}

				
				if(listaEjercicioGrupo!=null && !listaEjercicioGrupo.isEmpty()){
					//INICIO : PERIODOS PARA EL GRUPO
					//ORDENANDO LISTA DE EJERCICIOS POR AÑO y MES DEL GRUPO
					Collections.sort(listaEjercicioGrupo, Ejercicio.EjerAnioMesComparator);
					
					//OBTENIENDO LOS 3 PERIODOS CERRADOS DEL GRUPO
					List<Ejercicio> listaSobranteGrupo = new ArrayList<Ejercicio>(listaEjercicioGrupo);
					listaFinalGrupo = obtenerPeriodosCerrados(listaEjercicioGrupo,listaSobranteGrupo);
	
					//OBTENIENDO EL PERIODO SITUACIONAL DEL GRUPO
					situcional = obtenerPeriodoSituacional(listaSobranteGrupo,listaFinalIndividual);
					listaFinalGrupo.add(situcional);
					//FIN : PERIODOS PARA EL GRUPO
				}
				
				
			}
			
		} catch (RemoteException e) {
			logger.error(StringUtil.getStackTrace(e));
		} catch (ServiceException e) {
			logger.error(StringUtil.getStackTrace(e));			
		}
		SalidaITDA salida = new SalidaITDA();
		salida.setEjercicioGrupal(listaFinalGrupo);
		salida.setEjercicioIndividual(listaFinalIndividual);
		
		return salida;
		
	}
	
	
	public List<Ejercicio> obtenerPeriodosCerrados(List<Ejercicio> listaEjercicio,List<Ejercicio> listaSobrante){
		//OBTENIENDO LOS 3 PERIODOS CERRADOS
		List<Ejercicio> listaFinalEjercicio = new ArrayList<Ejercicio>();
		int cont = 0; 
		int i=0;
		for(Ejercicio ejerc : listaEjercicio){
			if(MES_DICIEMBRE.equals(ejerc.getMes()) ){
				if(cont!=3){
					listaFinalEjercicio.add(ejerc);
					listaSobrante.set(i, null);
					cont++;
				}else{
					break;
				}
			}
			i++;
		}
		
		listaSobrante.removeAll(Collections.singleton(null));
		
		return listaFinalEjercicio;
	}
	
	public Ejercicio obtenerPeriodoSituacional(List<Ejercicio> listaSobrante,List<Ejercicio> listaCerrados){
		
		Ejercicio situacional = new Ejercicio();
		Ejercicio cerrado = new Ejercicio();
		
		Integer aniocerrado = 0;
		Integer aniosituacional = 0;
		
		if(!listaCerrados.isEmpty() && !listaSobrante.isEmpty()){
			cerrado = listaCerrados.get(0);
			aniocerrado = Integer.parseInt(cerrado.getAnio());
			for(Ejercicio ejer : listaSobrante){
				aniosituacional = Integer.parseInt(ejer.getAnio());
				if(!MES_DICIEMBRE.equals(ejer.getMes())){
					if(aniosituacional>aniocerrado){
						
						situacional = new Ejercicio();
						situacional.setAnio(ejer.getAnio());
						situacional.setMes(ejer.getMes());
						situacional.setCodTmodelo(ejer.getCodTmodelo());
						situacional.setCodStatus(ejer.getCodStatus());
						situacional.setCodAnalisis(ejer.getCodAnalisis());
						situacional.setCodBalance(ejer.getCodBalance());
						situacional.setCodTipBalaDesc(ejer.getCodTipBalaDesc());
												
						break;
						
					}
					
				}
			}
			
		}

		return situacional;
	}
	
	public static void main(String[] arg){
		CtHeaderRq header = new CtHeaderRq();
		header.setOpcionAplicacion("00");
		header.setTerminalContable("D90L");
		header.setTerminalLogico("D90L");
		header.setUsuario("P007395");
		
		CtBodyRq data = new CtBodyRq();
		data.setAplicativo("RT");
		data.setCodigoCliente("77814063");// 00857750
		data.setTratamiento("C");
		
		ServiceITDA service = new ServiceITDA();
		SalidaITDA listaRating = service.obtieneListaEjercicio(header,data,"http://118.180.36.26:7801");
		
		System.out.println("-----------------------------------------------------");
		System.out.println("------------EJERCICIOS RATING------------------------");
		System.out.println("-----------------------------------------------------");
		Date date = new Date();
		System.out.println(date.getTime());
		List<Ejercicio> ejercicioIndividual = listaRating.getEjercicioIndividual();
		List<Ejercicio> ejercicioGrupo = listaRating.getEjercicioGrupal();
		System.out.println(date.getTime());
				
		for(Ejercicio ejercicio:ejercicioIndividual){
			System.out.println(ejercicio.getAnio());
			System.out.println(ejercicio.getMes());
			System.out.println(ejercicio.getCodStatus());
			System.out.println(ejercicio.getCodTmodelo());
			System.out.println("-----------------------------------------------------");
		}
		
		System.out.println("----------------GRUPO-------------------------");
		for(Ejercicio ejercicio:ejercicioGrupo){
			System.out.println(ejercicio.getAnio());
			System.out.println(ejercicio.getMes());
			System.out.println(ejercicio.getCodStatus());
			System.out.println(ejercicio.getCodTmodelo());
			System.out.println("-----------------------------------------------------");
		}
		
		//PRUEBA PARA VER COMO SE OBTIENEN LOS PERIODOS 
		/*
		List<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		Ejercicio ejer = new Ejercicio();
		ejer.setAnio("2012");
		ejer.setMes("09");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2013");
		ejer.setMes("09");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2014");
		ejer.setMes("09");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2008");
		ejer.setMes("06");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2013");
		ejer.setMes("10");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2013");
		ejer.setMes("12");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2012");
		ejer.setMes("12");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2009");
		ejer.setMes("12");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2011");
		ejer.setMes("09");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2010");
		ejer.setMes("12");
		lista.add(ejer);
		
		ejer = new Ejercicio();
		ejer.setAnio("2008");
		ejer.setMes("12");
		lista.add(ejer);
		
		Collections.sort(lista, Ejercicio.EjerAnioMesComparator);
		ServiceITDA service = new ServiceITDA();
		List<Ejercicio> listaSobranteIndividual = new ArrayList<Ejercicio>(lista);
		List<Ejercicio> cerrados = service.obtenerPeriodosCerrados(lista,listaSobranteIndividual);
		Ejercicio  situacional = service.obtenerPeriodoSituacional(listaSobranteIndividual, cerrados);
		
		System.out.println("----------------LISTA CERRADOS-------------------------");
		for(Ejercicio ejercicio:cerrados){
			System.out.println(ejercicio.getAnio());
			System.out.println(ejercicio.getMes());
			System.out.println("-----------------------------------------------------");
		}
		
		System.out.println("----------------SITUACIONAL -------------------------");
		
		System.out.println(situacional.getAnio());
		System.out.println(situacional.getMes());
		*/
		
		
		
	}

}
