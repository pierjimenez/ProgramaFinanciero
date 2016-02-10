package pe.com.bbva.iipf.pf.bo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;



import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.bo.AnexoBO;
import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.pf.bo.ProgramaBO;
import pe.com.bbva.iipf.pf.dao.AnexoDAO;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.Anexo;
import pe.com.bbva.iipf.pf.model.AnexoColumna;
import pe.com.bbva.iipf.pf.model.FilaAnexo;
import pe.com.bbva.iipf.pf.model.GarantiaHost;
import pe.com.bbva.iipf.pf.model.LimiteFormalizado;
import pe.com.bbva.iipf.pf.model.Parametro;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.SaldoCliente;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.bo.GenericBOImpl;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.exceptions.DAOException;
import pe.com.stefanini.core.util.FormatUtil;

/**
 * 
 * @author EPOMAYAY
 *
 */
@Service("anexoBO")
public class AnexoBOImpl extends GenericBOImpl<Anexo> implements AnexoBO {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private ProgramaBO programaBO;
	
	@Resource
	private AnexoDAO anexoDAO;
	
	@Resource
	private ParametroBO parametroBO;
	
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
	
	private List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
	private List<FilaAnexo> listaFilaAnexosTotal = new ArrayList<FilaAnexo>();
	private Programa programa;

	/**
	 * calcula el total por bancos
	 */
	@Override
	public void calcularMontoPorBanco(List<FilaAnexo> lista) {
		//sumar
		//BUREAU RATING  FECHA  LTE_AUTORIZADO  LTE_FORM  RGO_ACTUAL  RGO_PROP_BBVA_BC PROP RIESGO(6) OBSER(7) 
		//0			1		2		3	4	5	6  7
		if(lista != null){
			for(int f=0; f<lista.size(); f++){
				double total_rating=0;
				double total_lte_autorizado=0;
				double total_lte_form=0;
				double total_rgo_actual=0;
				double total_rgo_prop_bbva_bc=0;
				double total_prop_riesgo=0;
								
				FilaAnexo fila = lista.get(f);
				if(!fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_CABECERA.tipo) && !fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_TOTAL.tipo)){
					if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){
						FilaAnexo totalBanco= lista.get(f);
						f++;
						if(f>lista.size()-1){
							break;
						}
						fila = lista.get(f);
						
						
					
						
						while(!fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){
							if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo)||
									fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)){
	
									
										if(fila.getAnexo().getLteAutorizado()!=null && !fila.getAnexo().getLteAutorizado().equals("")){
											total_lte_autorizado +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteAutorizado()));
										}
									
										if(fila.getAnexo().getLteForm()!=null && !fila.getAnexo().getLteForm().equals("")){
											total_lte_form +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteForm()));
										}
									
										if(fila.getAnexo().getRgoActual()!=null &&  !fila.getAnexo().getRgoActual().equals("")){
											total_rgo_actual +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoActual()));
										}
									
										if(fila.getAnexo().getRgoPropBbvaBc()!=null &&  !fila.getAnexo().getRgoPropBbvaBc().equals("")){
											total_rgo_prop_bbva_bc +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoPropBbvaBc()));
										}
									
										if(fila.getAnexo().getPropRiesgo()!=null && !fila.getAnexo().getPropRiesgo().equals("")){
											total_prop_riesgo +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getPropRiesgo()));
										}
								
							}
							f++;
							if(f>lista.size()-1){
								break;
							}
							fila = lista.get(f);
						}//while
	//					List<AnexoColumna> columnasEmpresas = totalBanco.getListaAnexoColumna();
	//					columnasEmpresas.get(0).setValor(FormatUtil.round(total_rating,2)+"");
						totalBanco.getAnexo().setLteAutorizado(FormatUtil.conversion(FormatUtil.round(total_lte_autorizado,2)));
						totalBanco.getAnexo().setLteForm(FormatUtil.conversion(FormatUtil.round(total_lte_form,2)));
						totalBanco.getAnexo().setRgoActual(FormatUtil.conversion(FormatUtil.round(total_rgo_actual,2)));
						totalBanco.getAnexo().setRgoPropBbvaBc(FormatUtil.conversion(FormatUtil.round(total_rgo_prop_bbva_bc,2)));
						totalBanco.getAnexo().setPropRiesgo(FormatUtil.conversion(FormatUtil.round(total_prop_riesgo,2)));
					}
					f--;
					if(f<0){
						break;
					}
				}//if tipo cabecera
				
			}
		}
	}
	@Override
	public FilaAnexo calcularMontoTotal(List<FilaAnexo> lista) {
		//sumar
		//BUREAU RATING  FECHA  LTE_AUTORIZADO  LTE_FORM  RGO_ACTUAL  RGO_PROP_BBVA_BC PROP RIESGO(7) OBSER(8)
		//0			1		2		3	4	5	6  7 8
		double total_rating=0;
		double total_lte_autorizado=0;
		double total_lte_form=0;
		double total_rgo_actual=0;
		double total_rgo_prop_bbva_bc=0;
		double total_prop_riesgo=0;
		FilaAnexo filaTotal = new FilaAnexo();
		//ini MCG
		Anexo oanexoTot=new Anexo();			
		oanexoTot.setDescripcion("Total");
		oanexoTot.setTipoFila(TiposFila.TIPO_TOTAL.tipo);
		filaTotal.setAnexo(oanexoTot);	
		//fin MCG
		if(lista != null){
			for(int f =0; f<lista.size(); f++){
				FilaAnexo fila = lista.get(f);
				if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo) ){					
						
							if(fila.getAnexo().getLteAutorizado()!=null && !fila.getAnexo().getLteAutorizado().equals("")){
								total_lte_autorizado +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteAutorizado()));
							}
						
							if(fila.getAnexo().getLteForm()!=null && !fila.getAnexo().getLteForm().equals("")){
								total_lte_form +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteForm()));
							}
						
							if(fila.getAnexo().getRgoActual()!=null &&  !fila.getAnexo().getRgoActual().equals("")){
								total_rgo_actual +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoActual()));
							}
						
							if(fila.getAnexo().getRgoPropBbvaBc()!=null && !fila.getAnexo().getRgoPropBbvaBc().equals("")){
								total_rgo_prop_bbva_bc +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoPropBbvaBc()));
							}
						
							if(fila.getAnexo().getPropRiesgo()!=null && !fila.getAnexo().getPropRiesgo().equals("")){
								total_prop_riesgo +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getPropRiesgo()));
							}
						
					
				}
			}
			
		}

		filaTotal.getAnexo().setLteAutorizado(FormatUtil.conversion(FormatUtil.round(total_lte_autorizado,2)));
		filaTotal.getAnexo().setLteForm(FormatUtil.conversion(FormatUtil.round(total_lte_form,2)));
		filaTotal.getAnexo().setRgoActual(FormatUtil.conversion(FormatUtil.round(total_rgo_actual,2)));
		filaTotal.getAnexo().setRgoPropBbvaBc(FormatUtil.conversion(FormatUtil.round(total_rgo_prop_bbva_bc,2)));
		filaTotal.getAnexo().setPropRiesgo(FormatUtil.conversion(FormatUtil.round(total_prop_riesgo,2)));
		return filaTotal;
	}
	@Override
	public void calcularMontosPorEmpresa(List<FilaAnexo> lista) {
		//sumar
		//BUREAU RATING  FECHA  LTE_AUTORIZADO  LTE_FORM  RGO_ACTUAL  RGO_PROP_BBVA_BC  PROP RIESGO(7) OBSER (8)
		//0	     1	     2	    3	4	5	6  7 8
		if(lista != null){
			for(int f =0; f<lista.size(); f++){
				double total_rating=0;
				double total_lte_autorizado=0;
				double total_lte_form=0;
				double total_rgo_actual=0;
				double total_rgo_prop_bbva_bc=0;
				double total_prop_riesgo=0;
				FilaAnexo fila = lista.get(f);
				
				if(!fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_CABECERA.tipo)){
					if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo) ){
						//avanzar dos posiciones para pasar la fila de la empresa
						//y llegar hasta la primera posicion de las filas de las operaciones
						FilaAnexo totalEmpesa = fila;
						f++;
						if(f>lista.size()-1){
							break;
						}
						fila = lista.get(f);
						while(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo) ||
							  fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)||
							  fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)
							  ){
							
							if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)||
									fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){
								
								if(fila.getAnexo().getLteAutorizado()!=null && !fila.getAnexo().getLteAutorizado().equals("")){
									total_lte_autorizado +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteAutorizado()));
								}
							
								if(fila.getAnexo().getLteForm()!=null && !fila.getAnexo().getLteForm().equals("")){
									total_lte_form +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteForm()));
								}
							
								if(fila.getAnexo().getRgoActual()!=null && !fila.getAnexo().getRgoActual().equals("")){
									total_rgo_actual +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoActual()));
								}
							
								if(fila.getAnexo().getRgoPropBbvaBc()!=null && !fila.getAnexo().getRgoPropBbvaBc().equals("")){
									total_rgo_prop_bbva_bc +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoPropBbvaBc()));
								}
							
								if(fila.getAnexo().getPropRiesgo()!=null && !fila.getAnexo().getPropRiesgo().equals("")){
									total_prop_riesgo +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getPropRiesgo()));
								}						
							}
							f++;
							if(f>lista.size()-1){
								break;
							}
							fila = lista.get(f);
						}
	//					List<AnexoColumna> columnasEmpresas = totalEmpesa.getListaAnexoColumna();
	////					columnasEmpresas.get(0).setValor(FormatUtil.round(total_rating,2)+"");
	//					columnasEmpresas.get(3).setValor(FormatUtil.conversion(FormatUtil.round(total_lte_autorizado,2)));
	//					columnasEmpresas.get(4).setValor(FormatUtil.conversion(FormatUtil.round(total_lte_form,2)));
	//					columnasEmpresas.get(5).setValor(FormatUtil.conversion(FormatUtil.round(total_rgo_actual,2)));
	//					columnasEmpresas.get(6).setValor(FormatUtil.conversion(FormatUtil.round(total_rgo_prop_bbva_bc,2)));
	//					
	//					columnasEmpresas.get(7).setValor(FormatUtil.conversion(FormatUtil.round(total_prop_riesgo,2)));
	//					
						totalEmpesa.getAnexo().setLteAutorizado(FormatUtil.conversion(FormatUtil.round(total_lte_autorizado,2)));
						totalEmpesa.getAnexo().setLteForm(FormatUtil.conversion(FormatUtil.round(total_lte_form,2)));
						totalEmpesa.getAnexo().setRgoActual(FormatUtil.conversion(FormatUtil.round(total_rgo_actual,2)));
						totalEmpesa.getAnexo().setRgoPropBbvaBc(FormatUtil.conversion(FormatUtil.round(total_rgo_prop_bbva_bc,2)));					
						totalEmpesa.getAnexo().setPropRiesgo(FormatUtil.conversion(FormatUtil.round(total_prop_riesgo,2)));
						
						//retornar a la posicion de la empresa
						f--;
					}
				}
				
			}
		}
	}
	
	//ini MCG20140620
	@Override
	public void calcularMontosPorAccionista(List<FilaAnexo> lista) {
		//sumar
		//BUREAU RATING  FECHA  LTE_AUTORIZADO  LTE_FORM  RGO_ACTUAL  RGO_PROP_BBVA_BC  PROP RIESGO(7) OBSER (8)
		//0			1		2	3	4	5	6  7 8
		if(lista != null){
			for(int f =0; f<lista.size(); f++){
				double total_rating=0;
				double total_lte_autorizado=0;
				double total_lte_form=0;
				double total_rgo_actual=0;
				double total_rgo_prop_bbva_bc=0;
				double total_prop_riesgo=0;
				FilaAnexo fila = lista.get(f);
				
				if(!fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_CABECERA.tipo)){
					if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo) ){
						//avanzar dos posiciones para pasar la fila de la empresa
						//y llegar hasta la primera posicion de las filas de las operaciones
						FilaAnexo totalAccionista = fila;
						f++;
						if(f>lista.size()-1){
							break;
						}
						fila = lista.get(f);
						while(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo) ||
							  fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)||
							  fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)
							  ){
							
							if(fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)||
									fila.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){
								
	
									
										if(fila.getAnexo().getLteAutorizado()!=null && !fila.getAnexo().getLteAutorizado().equals("")){
											total_lte_autorizado +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteAutorizado()));
										}
									
										if(fila.getAnexo().getLteForm()!=null && !fila.getAnexo().getLteForm().equals("")){
											total_lte_form +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getLteForm()));
										}
									
										if(fila.getAnexo().getRgoActual()!=null && !fila.getAnexo().getRgoActual().equals("")){
											total_rgo_actual +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoActual()));
										}
									
										if(fila.getAnexo().getRgoPropBbvaBc()!=null && !fila.getAnexo().getRgoPropBbvaBc().equals("")){
											total_rgo_prop_bbva_bc +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getRgoPropBbvaBc()));
										}
									
										if(fila.getAnexo().getPropRiesgo()!=null && !fila.getAnexo().getPropRiesgo().equals("")){
											total_prop_riesgo +=Double.parseDouble(FormatUtil.FormatNumeroSinComa(fila.getAnexo().getPropRiesgo()));
										}							
							}
							f++;
							if(f>lista.size()-1){
								break;
							}
							fila = lista.get(f);
						}
	//					List<AnexoColumna> columnasAccionistas = totalAccionista.getListaAnexoColumna();
	//					columnasEmpresas.get(0).setValor(FormatUtil.round(total_rating,2)+"");
						totalAccionista.getAnexo().setLteAutorizado(FormatUtil.conversion(FormatUtil.round(total_lte_autorizado,2)));
						totalAccionista.getAnexo().setLteForm(FormatUtil.conversion(FormatUtil.round(total_lte_form,2)));
						totalAccionista.getAnexo().setRgoActual(FormatUtil.conversion(FormatUtil.round(total_rgo_actual,2)));
						totalAccionista.getAnexo().setRgoPropBbvaBc(FormatUtil.conversion(FormatUtil.round(total_rgo_prop_bbva_bc,2)));					
						totalAccionista.getAnexo().setPropRiesgo(FormatUtil.conversion(FormatUtil.round(total_prop_riesgo,2)));
						
						//retornar a la posicion de la empresa
						f--;
					}
				}
			}
		}
	}
	
	//fin MCF20140620
	@Override
	public boolean validarFilas(List<FilaAnexo> lista, 
								FilaAnexo filaAnexo,String posicion,String strAccion ) throws BOException{
		int nuevoTipoFila = filaAnexo.getAnexo().getTipoFila();
		
		
		
		if(!lista.isEmpty()){
			//verificamos que siempre la primera posicion sea un banco
			
			if (lista.size()==1){
				if(nuevoTipoFila!=TiposFila.TIPO_BANCO.tipo){
					throw new BOException("Debe primero ingresar un Banco");
				}
			}
			
			if(filaAnexo.getPos() == 1 &&
			   filaAnexo.getAnexo().getTipoFila() != TiposFila.TIPO_BANCO.tipo){
				throw new BOException("El primer registro debe ser un Banco");
			}
//			int ultimoTipoFila =lista.get(lista.size()-1).getAnexo().getTipoFila();
//			if(ultimoTipoFila ==TiposFila.TIPO_BANCO.tipo && 
//			   nuevoTipoFila==TiposFila.TIPO_LIMITES.tipo){
//				throw new BOException("Debe primero ingresar una empresa");
//			}
//			if(ultimoTipoFila ==TiposFila.TIPO_BANCO.tipo && 
//			   nuevoTipoFila==TiposFila.TIPO_BANCO.tipo){
//				throw new BOException("Debe primero ingresar una empresa y luego una operación");
//			}
//			if(ultimoTipoFila ==TiposFila.TIPO_EMPRESA.tipo && 
//			   nuevoTipoFila==TiposFila.TIPO_EMPRESA.tipo){
//				throw new BOException("Debe ingresar una operación");
//			}
//			if(ultimoTipoFila ==TiposFila.TIPO_EMPRESA.tipo && 
//			   nuevoTipoFila==TiposFila.TIPO_BANCO.tipo){
//				throw new BOException("Debe ingresar una operación");
//			}
////			if(ultimoTipoFila ==TiposFila.TIPO_LIMITES.tipo && 
////			   nuevoTipoFila==TiposFila.TIPO_EMPRESA.tipo){
////				throw new BOException("Debe ingresar una operación o un banco");
////			}
		}
//		else{
//			if(nuevoTipoFila!=TiposFila.TIPO_BANCO.tipo){
//				throw new BOException("Debe primero ingresar un Banco");
//			}
//		}
		if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)||
			filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo)||
			filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)||
			filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)||
			filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)){
			int iposicion=lista.size()-1;
			try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
			if(iposicion<(lista.size()-1)){
				FilaAnexo filaAnexoFilaSiguiente=lista.get(iposicion+1);
				boolean flagerror=true;
				String strMensajeError="No se puede agregar la fila en esta posición";
				if(filaAnexoFilaSiguiente.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
					
					if(strAccion.equals("1")) {
						strMensajeError="No se puede editar la Fila en esta posición.La fila a editar debe ser de Tipo limite";
						//FilaAnexo filaAnexoFilaActual=lista.get(iposicion);
						if (filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo) ){
							flagerror=false;
						}
					} 
					if (flagerror){						
					throw new BOException(strMensajeError);
					}
				}
				
			}
		}
		if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
				int iposicion=lista.size()-1;
				try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
				if(iposicion<(lista.size()-1)){
					FilaAnexo filaAnexoFilaSiguiente=lista.get(iposicion+1);					
					String strMensajeError="No se puede agregar la fila en esta posición";
					if(filaAnexoFilaSiguiente.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
						
						if(strAccion.equals("1")) {
							strMensajeError="No se puede editar la Fila en esta posición.La fila a editar debe ser de Tipo limite";
							FilaAnexo filaAnexoFilaActual=lista.get(iposicion);
							if (filaAnexoFilaActual.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo) ){
								
								throw new BOException(strMensajeError);
							}
							
						} 

					}
					
				}
			}
		if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)||
			filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)){
			
			int iposicion=lista.size()-1;
			try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
			FilaAnexo filaAnexoAnterior=lista.get(iposicion);
			if(filaAnexoAnterior.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){				
				throw new BOException("La operación o límite solo se puede agregar a una empresa");
			}
		}
		if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
			int iposicion=lista.size()-1;
			try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
			FilaAnexo filaAnexoAnterior=lista.get(iposicion);
			if(!(filaAnexoAnterior.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo) || 
				filaAnexoAnterior.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)) ){
				throw new BOException("El sublimite sólo se puede agregar a un límite");
			}
		}
		
		/*Validar que la empresa que se esta agregando no sea repetida*/
		/*Inicio*/
//		boolean empresaRepetida = false;
//		int iposicion=0;
//		try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
//		for(FilaAnexo filaAnexos:lista){
//			if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo) && 
//				filaAnexo.getAnexo().getDescripcion().equals(filaAnexos.getAnexo().getDescripcion())){
//				if(strAccion.equals("1") && filaAnexo.getPos()==iposicion ){
//					continue;
//				}
//				empresaRepetida = true;
//				break;
//				
//			}
//		}
//		if(empresaRepetida){
//			throw new BOException("No se puede agregar fila, la empresa ya existe");
//		}
		if (existeEmpresa(lista,filaAnexo,posicion,strAccion)){	
			String strMensajeError="";
			if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)){
				if (strAccion.equals("0")){
					strMensajeError="No se puede agregar la fila, El accionista ya existe";
				}else{
					strMensajeError="No se puede editar la fila, El accionista ya existe";
				}
			}else{		
				if (strAccion.equals("0")){
					strMensajeError="No se puede agregar la fila, la empresa ya existe";
				}else{
					strMensajeError="No se puede editar la fila, la empresa ya existe";
				}
			}
			throw new BOException(strMensajeError);
		}
		
		/*Fin*/

		/*Validar que el banco que se esta agregando no sea repetido*/		
		if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){
			boolean bancoRepetido = false;
			int iposicion=0;
			try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
			for(FilaAnexo filaAnexos:lista){
				if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo) && 
					filaAnexo.getAnexo().getDescripcion().equals(filaAnexos.getAnexo().getDescripcion())){
					if(strAccion.equals("1") && filaAnexos.getPos()==iposicion ){
						continue;
					}
					bancoRepetido = true;
					break;
					
				}
			}
			if(bancoRepetido){
				throw new BOException("No se puede agregar la fila, el banco ya existe");
			}
		}
		
		
		return true;
	}
	public boolean existeEmpresa(List<FilaAnexo> lista, 
			FilaAnexo ofilaAnexo,String posicion,String strAccion){		
		boolean booexiste=false;
		
		try {		

			if(lista != null && lista.size()>0){
							
				int iposicion=lista.size()-1;
				try{iposicion=Integer.parseInt(posicion);}catch (Exception e) {}
				Long idbancoPadre=null;
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadreoperacion=null;
				List<FilaAnexo> olistaFilaAnexosBancos=new ArrayList<FilaAnexo>();	
				List<FilaAnexo> olistaFilaAnexosvalidar=new ArrayList<FilaAnexo>();
				
			
				Long idcont=0L;
				//se utiliza id temporal porque lo que se agrega no tiene Id
				for(FilaAnexo filaAnexo : lista){	
					idcont=idcont+1;
					filaAnexo.getAnexo().setIdtemp(idcont);
				}
				
				for(FilaAnexo filaAnexo : lista){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						olistaFilaAnexosBancos.add(filaAnexo);
						lpadrebanco=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7)) ){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadreoperacion=filaAnexo.getAnexo().getIdtemp();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadreoperacion);						
					}
					
					if (iposicion==filaAnexo.getPos()){
						idbancoPadre=lpadrebanco;
					}
//					logger.info("Asignacion de padre a Anexos:");
//					logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getIdtemp() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	

				}
				
				
						
				if (olistaFilaAnexosBancos!=null && olistaFilaAnexosBancos.size()>0){
					for (FilaAnexo xfilaAnexo: olistaFilaAnexosBancos){
						 if (idbancoPadre!=null && idbancoPadre.equals(xfilaAnexo.getAnexo().getIdtemp())){
								olistaFilaAnexosvalidar.add(xfilaAnexo);
								buscarHijosIdTemp(xfilaAnexo.getAnexo().getIdtemp(),lista,olistaFilaAnexosvalidar);
							}
					}
				}							
//									
//				for (FilaAnexo afilaAnexo: olistaFilaAnexosvalidar){				
//					logger.info("copiarAnexo:::Anexo validar:");
//					logger.info("Descripcion Anexo:"+ afilaAnexo.getAnexo().getDescripcion() +"::" 
//								+ "id:"+ afilaAnexo.getAnexo().getIdtemp() +"::"
//								+ "idpadre:"+ afilaAnexo.getAnexo().getPadretmp());				
//				}
				
				
				boolean empresaRepetida = false;			
				for(FilaAnexo filaAnexos:olistaFilaAnexosvalidar){
					if(ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo) && 
							ofilaAnexo.getAnexo().getDescripcion().trim().equals(filaAnexos.getAnexo().getDescripcion().trim())){
						if(strAccion.equals("1") && filaAnexos.getPos()==iposicion ){
							continue;
						}
						empresaRepetida = true;
						break;
						
					}
				}
				if(empresaRepetida){
					booexiste=true;				
				}	
				
				//ini para accionista
				boolean accionistaRepetida = false;			
				for(FilaAnexo filaAnexos:olistaFilaAnexosvalidar){
					if(ofilaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo) && 
							ofilaAnexo.getAnexo().getDescripcion().trim().equals(filaAnexos.getAnexo().getDescripcion().trim())){
						if(strAccion.equals("1") && filaAnexos.getPos()==iposicion ){
							continue;
						}
						accionistaRepetida = true;
						break;
						
					}
				}
				if(accionistaRepetida){
					booexiste=true;				
				}	
				
				//fin
			 }
			
			} catch (Exception e) {
				booexiste=false;			
			}
		return booexiste;
	}
	
	
	//ini MCG20130621
	@Override
	public String ObtenerCodigoEmpresa(List<FilaAnexo> lista,List<Empresa> oListaEmpresa,String posicion,List<Accionista> olistaAccionista){		
		String codigoEmpresa="";
		
		try {		

			if(lista != null && lista.size()>0){
							
				int iposicion=lista.size()-1;
				try{ 
					if (posicion!=null && !posicion.equals("") ) {
						iposicion=Integer.parseInt(posicion);
					}
				}catch (Exception e) {}
				Long idbancoPadre=null;
				String empresaPadre="";
				Long idEmpresaPadre=null;
				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				
				Long lpadrelimite=null;
				List<FilaAnexo> olistaFilaAnexosBancos=new ArrayList<FilaAnexo>();
				
				Integer  itipoFila=2;
				Integer idtipoFila=2;
			
				Long idcont=0L;
				//se utiliza id temporal porque lo que se agrega no tiene Id
				for(FilaAnexo filaAnexo : lista){	
					idcont=idcont+1;
					filaAnexo.getAnexo().setIdtemp(idcont);
				}
				
				for(FilaAnexo filaAnexo : lista){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						olistaFilaAnexosBancos.add(filaAnexo);
						lpadrebanco=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getIdtemp();
						itipoFila=filaAnexo.getAnexo().getTipoFila();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getIdtemp();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (iposicion==filaAnexo.getPos()){
						idbancoPadre=lpadrebanco;
						idEmpresaPadre=lpadreempresa;
						idtipoFila=itipoFila;
					}
//					logger.info("Asignacion de padre a Anexos:");
//					logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getIdtemp() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	
					
				}
				for(FilaAnexo filaAnexobusq : lista){	
					if (idEmpresaPadre.equals(filaAnexobusq.getAnexo().getIdtemp())){
						empresaPadre=filaAnexobusq.getAnexo().getDescripcion()==null?"":filaAnexobusq.getAnexo().getDescripcion().toString().trim();
						break;
					}					
				}
//				logger.info("id tmp Empresa Padre:"+idEmpresaPadre);
//				logger.info("Empresa Padre:"+empresaPadre);
				
				if (idtipoFila.equals(TiposFila.TIPO_ACCIONISTA.tipo)){	
						if (olistaAccionista!=null && olistaAccionista.size()>0){
							for (Accionista oaccionista: olistaAccionista){
								 if (empresaPadre!=null && empresaPadre.equals(oaccionista.getNombre().toString().trim())){
									 codigoEmpresa=oaccionista.getCodigoCentral()==null?"":oaccionista.getCodigoCentral().toString();
								 }
							}
						}else{
							codigoEmpresa="";
						}							
				}else{					
						if (oListaEmpresa!=null && oListaEmpresa.size()>0){
							for (Empresa xEmpresa: oListaEmpresa){
								 if (empresaPadre!=null && empresaPadre.equals(xEmpresa.getNombre().toString().trim())){
									 codigoEmpresa=xEmpresa.getCodigo()==null?"":xEmpresa.getCodigo().toString();
								 }
							}
						}else{
							codigoEmpresa="";
						}					
				}
	
			 }else {
				 codigoEmpresa="";
			 }
			
			} catch (Exception e) {
				codigoEmpresa="";			
			}
		return codigoEmpresa;
	}
	
	//fin MCG20130621
	
	//ini MCG20140623
	@Override
	public Integer ObtenerTipoFilaPadre(List<FilaAnexo> lista,String posicion){		
		Integer resulIdTipoFila=2;
		
		try {		

			if(lista != null && lista.size()>0){
							
				int iposicion=lista.size()-1;
				try{ 
					if (posicion!=null && !posicion.equals("") ) {
						iposicion=Integer.parseInt(posicion);
					}
				}catch (Exception e) {}
				Long idbancoPadre=null;
				String empresaPadre="";
				Long idEmpresaPadre=null;
				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				
				Long lpadrelimite=null;
				List<FilaAnexo> olistaFilaAnexosBancos=new ArrayList<FilaAnexo>();
				
				Integer  itipoFila=2;
				Integer idtipoFila=2;
			
				Long idcont=0L;
				//se utiliza id temporal porque lo que se agrega no tiene Id
				for(FilaAnexo filaAnexo : lista){	
					idcont=idcont+1;
					filaAnexo.getAnexo().setIdtemp(idcont);
				}
				
				for(FilaAnexo filaAnexo : lista){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						olistaFilaAnexosBancos.add(filaAnexo);
						lpadrebanco=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getIdtemp();
						itipoFila=filaAnexo.getAnexo().getTipoFila();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getIdtemp();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (iposicion==filaAnexo.getPos()){
						idbancoPadre=lpadrebanco;
						idEmpresaPadre=lpadreempresa;
						idtipoFila=itipoFila;
					}

				}
				resulIdTipoFila=idtipoFila;
	
			 }else {
				 resulIdTipoFila=2;
			 }
			
			} catch (Exception e) {
				resulIdTipoFila=2;			
			}
		return resulIdTipoFila;
	}
	
	//fin MCG20140623
	
	//ini MCG20130813
	@Override
	public String ObtenerContratosLimitePadre(List<FilaAnexo> lista,String posicion){		
		String ContratosLimite="";
		
		try {		

			if(lista != null && lista.size()>0){
							
				int iposicion=lista.size()-1;
				try{ 
					if (posicion!=null && !posicion.equals("") ) {
						iposicion=Integer.parseInt(posicion);
					}
				}catch (Exception e) {}
				Long idbancoPadre=null;
				//String ContratosLimite="";
				Long idEmpresaPadre=null;
				Long idlimitePadre=null;
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadrelimite=null;
				List<FilaAnexo> olistaFilaAnexosBancos=new ArrayList<FilaAnexo>();	
				
			
				Long idcont=0L;
				//se utiliza id temporal porque lo que se agrega no tiene Id
				for(FilaAnexo filaAnexo : lista){	
					idcont=idcont+1;
					filaAnexo.getAnexo().setIdtemp(idcont);
				}
				
				for(FilaAnexo filaAnexo : lista){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						olistaFilaAnexosBancos.add(filaAnexo);
						lpadrebanco=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getIdtemp();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (iposicion==filaAnexo.getPos()){
						idbancoPadre=lpadrebanco;
						idEmpresaPadre=lpadreempresa;	
						idlimitePadre=lpadrelimite;
					}
					logger.info("Asignacion de padre a Anexos:");
					logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getIdtemp() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	
					
				}
				for(FilaAnexo filaAnexobusq : lista){	
					if (idlimitePadre.equals(filaAnexobusq.getAnexo().getIdtemp())){
						   //MCG20140818 se aumenta +1 a cada columna por add bureau ::: 4 a 5
							if (filaAnexobusq.getAnexo().getContrato()!=null 
									&& filaAnexobusq.getAnexo().getContrato().length()>0){
								ContratosLimite =filaAnexobusq.getAnexo().getContrato();																			
							}												
						break;						
					}					
				}
				logger.info("id tmp limite Padre:"+idlimitePadre);
				logger.info("contratos de limite:"+ContratosLimite);
			

	
			 }else {
				 ContratosLimite="";
			 }
			
			} catch (Exception e) {
				ContratosLimite="";			
			}
		return ContratosLimite;
	}
	
	
	@Override
	public List<FilaAnexo> ObtenerHijosByIdPabre(List<FilaAnexo> lista,String posicion){		
		
		List<FilaAnexo> olistahijos =new ArrayList<FilaAnexo>();
		
		try {		

			if(lista != null && lista.size()>0){
							
				int iposicion=lista.size()-1;
				try{ 
					if (posicion!=null && !posicion.equals("") ) {
						iposicion=Integer.parseInt(posicion);
					}
				}catch (Exception e) {}
				Long idbancoPadre=null;				
				Long idEmpresaPadre=null;
				Long idLimitePadre=null;
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadrelimite=null;
				List<FilaAnexo> olistaFilaAnexosBancos=new ArrayList<FilaAnexo>();	
				
			
				Long idcont=0L;
				//se utiliza id temporal porque lo que se agrega no tiene Id
				for(FilaAnexo filaAnexo : lista){	
					idcont=idcont+1;
					filaAnexo.getAnexo().setIdtemp(idcont);
				}
				
				for(FilaAnexo filaAnexo : lista){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);
						olistaFilaAnexosBancos.add(filaAnexo);
						lpadrebanco=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getIdtemp();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getIdtemp();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (iposicion==filaAnexo.getPos()){
						idbancoPadre=lpadrebanco;
						idEmpresaPadre=lpadreempresa;	
						idLimitePadre=lpadrelimite;
					}
					logger.info("Asignacion de padre a Anexos:");
					logger.info("Descripcion Anexo:"+ filaAnexo.getAnexo().getDescripcion() +"::" + "id:"+ filaAnexo.getAnexo().getIdtemp() +"::"+ "padre:"+ filaAnexo.getAnexo().getPadretmp());	
					
				}	
				if (idLimitePadre!=null){
					olistahijos=listaAnexosByIdPadreIdTemp(lista, idLimitePadre);	
				}				
				if (olistahijos==null){
					 olistahijos =new ArrayList<FilaAnexo>();
				}
	
			 }else {
				 olistahijos =new ArrayList<FilaAnexo>();
			 }
			
			} catch (Exception e) {
				olistahijos =new ArrayList<FilaAnexo>();		
			}
		return olistahijos;
	}
	
		
	//fin MCG20130813
	
	
	
	
	
	/**
	 * 
	 */
	public void beforeSaveAnexo(){
		int posFila=0;
		if(listaFilaAnexos != null){
			for(FilaAnexo filaAnexo : listaFilaAnexos){
				Anexo anexo = filaAnexo.getAnexo();
				anexo.setId(null);
				anexo.setPosFila(posFila);
				anexo.setPrograma(programa);
				posFila++;
				int posCol =0;
			}
		}
		
		if(listaFilaAnexosTotal != null){
			for(FilaAnexo filaAnexo : listaFilaAnexosTotal){
				Anexo anexo = filaAnexo.getAnexo();
				anexo.setId(null);
				anexo.setPosFila(posFila);
				anexo.setPrograma(programa);
				posFila++;
				int posCol =0;
			}
		}
		
	}

	public boolean validate ()throws BOException{
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveAnexos()throws BOException{
		beforeSaveAnexo();
		if(validate()){
			if(listaFilaAnexos != null){
				List<FilaAnexo> olistaFilaAnexosGeneral = new ArrayList<FilaAnexo>();
				try {
				
				List parametros = new ArrayList();
				parametros.add(programa.getId());				
				//super.executeNamedQuery("deleteColumnasByAnexoidProgram",parametros);
				super.executeNamedQuery("deleteAnexoByIdProgram",parametros);
				} catch (BOException e) {
					throw new BOException(e.getMessage());
				}catch (Exception e) {
					throw new BOException(e.getMessage());
				}
				
				//guardando la lista actual

				//Add fileAnexo
				olistaFilaAnexosGeneral.addAll(listaFilaAnexos);
				if (listaFilaAnexosTotal!=null && listaFilaAnexosTotal.size()>0){
					FilaAnexo filaAnexo = listaFilaAnexosTotal.get(0);
					olistaFilaAnexosGeneral.add(filaAnexo);
				}
				for(FilaAnexo filaAnexo : olistaFilaAnexosGeneral){
					Anexo anexo = filaAnexo.getAnexo();
					try {	
						saveSQLAnexo(anexo);
						programaBO.actualizarFechaModificacionPrograma(programa.getId());
					} catch (BOException e) {
						logger.error(ExceptionUtils.getStackTrace(e));
						throw new BOException(e.getMessage());
					}catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
						throw new BOException(e.getMessage());
					}
				}
				//guardando la lista actual
//				int primero=0;
//				for(FilaAnexo filaAnexo : listaFilaAnexosTotal){				
//					if (primero==0) {						
//						Anexo anexo = filaAnexo.getAnexo();
//						try {
//							saveSQLAnexo(anexo);
//							programaBO.actualizarFechaModificacionPrograma(programa.getId());							
//							
//						} catch (BOException e) {
//							logger.error(ExceptionUtils.getStackTrace(e));
//							throw new BOException(e.getMessage());
//						}catch (Exception e) {
//							logger.error(ExceptionUtils.getStackTrace(e));
//							throw new BOException(e.getMessage());
//						}
//						primero++;
//					}
//				}
				
				
				
			}
		}
	}
	
	private void saveSQLAnexoColumna(List<AnexoColumna> listaAnexoColumna) throws BOException{
		try {
				for (AnexoColumna oanexoColumna :listaAnexoColumna){
					List parametros = new ArrayList();
					parametros.add(oanexoColumna.getAnexo().getId());
					parametros.add(oanexoColumna.getPosColumna());
					parametros.add(oanexoColumna.getDescripcion()==null?"":oanexoColumna.getDescripcion());
					parametros.add(oanexoColumna.getValor()==null?"":oanexoColumna.getValor());
					parametros.add(oanexoColumna.getContratos()==null?"":oanexoColumna.getContratos());			
					String sqlqueryp = " INSERT INTO PROFIN.TIIPF_ANEXO_COLUMNAS (ID_COLUMNA,ID_ANEXO,POS_COL,DESCRIPCION,VALOR,CONTRATO)" +								
			  		" VALUES(PROFIN.SEQ_ANEXO_COLUMNA.NEXTVAL, ?,?,?,?,?)";								
					super.executeSQLQuery(sqlqueryp,parametros);				
				}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new BOException(e.getMessage());
		}
		
	}
	
	private void saveSQLAnexo(Anexo oanexo) throws BOException{
		try {
			

					List parametros = new ArrayList();
					parametros.add(oanexo.getTipoFila());
					parametros.add(oanexo.getPosFila());
					parametros.add(oanexo.getPrograma().getId());
					parametros.add(oanexo.getCodigoFila()==null?"":oanexo.getCodigoFila());
					parametros.add(oanexo.getDescripcion()==null?"":oanexo.getDescripcion());
					parametros.add(oanexo.getIdpadre()==null?"":oanexo.getIdpadre());
					
					parametros.add(oanexo.getBureau()==null?"":oanexo.getBureau());
					parametros.add(oanexo.getRating()==null?"":oanexo.getRating());
					parametros.add(oanexo.getFecha()==null?"":oanexo.getFecha());
					parametros.add(oanexo.getLteAutorizado()==null?"":oanexo.getLteAutorizado());
					parametros.add(oanexo.getLteForm()==null?"":oanexo.getLteForm());
					parametros.add(oanexo.getRgoActual()==null?"":oanexo.getRgoActual());
					parametros.add(oanexo.getRgoPropBbvaBc()==null?"":oanexo.getRgoPropBbvaBc());
					parametros.add(oanexo.getPropRiesgo()==null?"":oanexo.getPropRiesgo());
					parametros.add(oanexo.getObservaciones()==null?"":oanexo.getObservaciones());
					
					parametros.add(oanexo.getColumna1()==null?"":oanexo.getColumna1());
					parametros.add(oanexo.getColumna2()==null?"":oanexo.getColumna2());
					parametros.add(oanexo.getColumna3()==null?"":oanexo.getColumna3());
					parametros.add(oanexo.getColumna4()==null?"":oanexo.getColumna4());
					parametros.add(oanexo.getColumna5()==null?"":oanexo.getColumna5());
					parametros.add(oanexo.getColumna6()==null?"":oanexo.getColumna6());
					parametros.add(oanexo.getColumna7()==null?"":oanexo.getColumna7());
					parametros.add(oanexo.getColumna8()==null?"":oanexo.getColumna8());
					parametros.add(oanexo.getColumna9()==null?"":oanexo.getColumna9());
					parametros.add(oanexo.getColumna10()==null?"":oanexo.getColumna10());
					
					parametros.add(oanexo.getNumColumna()==null?"0":oanexo.getNumColumna());
					parametros.add(oanexo.getCodigoItem()==null?"":oanexo.getCodigoItem());
					parametros.add(oanexo.getContrato()==null?"":oanexo.getContrato());
					
				
					
					String sqlqueryp = "insert into PROFIN.TIIPF_ANEXOPF " +
							"(ID_ANEXO, TIPO_FILA, POS_FILA, IIPF_PROGRAMA_ID, CODIGO_FILA, DESC_FILA, ID_ANEXO_PADRE, " +							
							"BUREAU, RATING, FECHA, LTE_AUTORIZADO, LTE_FORM, RGO_ACTUAL, RGO_PROP_BBVA_BC, PROP_RIESGO, OBSERVACIONES, " +
							"COLUMNA1, COLUMNA2, COLUMNA3, COLUMNA4, COLUMNA5, COLUMNA6, COLUMNA7, COLUMNA8, COLUMNA9, COLUMNA10, " +
							"NUM_COLUMNA, CODIGOITEM, CONTRATO)"+
							" values (PROFIN.SEQ_ANEXO.NEXTVAL,?,?,?,?,?,?," +
							"?,?,?,?,?,?,?,?,?," +
							"?,?,?,?,?,?,?,?,?,?," +
							"?,?,?)";	

					
					super.executeSQLQuery(sqlqueryp,parametros);
				
				
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new BOException(e.getMessage());
		}
		
	}
	
	//ini MCG20130816
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveAnexosTotal()throws BOException{
		//beforeSaveAnexo();
		if(validate()){
			if(listaFilaAnexosTotal != null){
				List<FilaAnexo> listaTotal =  findAnexosTotal();
//				//eliminando los que fueron quitados de la lista
				for(FilaAnexo filaAnexo : listaTotal){
					Anexo anexo = filaAnexo.getAnexo();
					try {
						List<Anexo> lstAnexo = new ArrayList<Anexo>();
						lstAnexo.add(anexo);
						//elimnando detalle
						super.deleteCollection(filaAnexo.getListaAnexoColumna());
						//eliminando cabecera
						super.deleteCollection(lstAnexo);
					} catch (BOException e) {
						throw new BOException(e.getMessage());
					}catch (Exception e) {
						throw new BOException(e.getMessage());
					}
				}
				
				//guardando la lista actual
				for(FilaAnexo filaAnexo : listaFilaAnexosTotal){
					Anexo anexo = filaAnexo.getAnexo();
					try {
						//guardo cabecera
						super.save(anexo);
						//guardando detalle					
						saveCollection(filaAnexo.getListaAnexoColumna());
						
						programaBO.actualizarFechaModificacionPrograma(programa.getId());
					} catch (BOException e) {
						logger.error(ExceptionUtils.getStackTrace(e));
						throw new BOException(e.getMessage());
					}catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e));
						throw new BOException(e.getMessage());
					}
				}
			}
		}
	}
	//ini MCG20130816
	
	//ini mcg 20120503
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveCopiaAnexos(List<FilaAnexo> olistaFilaAnexos)throws BOException{	
	
			if(olistaFilaAnexos != null && olistaFilaAnexos.size()>0){
				//guardando la lista actual
				for(FilaAnexo filaAnexo : olistaFilaAnexos){
					Anexo anexo = filaAnexo.getAnexo();
					try {
						saveSQLAnexo(anexo);						
					} catch (BOException e) {
						throw new BOException(e.getMessage());
					}catch (Exception e) {
						throw new BOException(e.getMessage());
					}
				}
			}
		
	}
	//fin mcg 20120503
	
	@Override
	public List<AnexoColumna> findColumnas()throws BOException{
		List<FilaAnexo> listaFilaAnexo = new ArrayList<FilaAnexo>();
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", programa.getId());
		List<AnexoColumna> listaColumnas = new ArrayList<AnexoColumna>();
		try {
			List<Anexo> listaAnexos = super.findByParams(Anexo.class,  parametros);
			Long idAnexoMin = 0L;
			if(!listaAnexos.isEmpty()){
				idAnexoMin = listaAnexos.get(0).getId();
				HashMap<String, Long> parametrosCol = new HashMap<String, Long>();
				parametrosCol.put("anexo", idAnexoMin);
				List<AnexoColumna> listaAnexosColumna = super.findByParams2(AnexoColumna.class, parametrosCol);
				if(!listaAnexosColumna.isEmpty()){
					
					//ini MCG20130802
					insertNuevaColumna(listaAnexosColumna);
					//ini MCG20130802
					listaColumnas = listaAnexosColumna;
				}
			}

		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		
		return listaColumnas;

	}
	/**
	 * construye la lista de anexos 
	 */
	@Override
	public List<FilaAnexo> findAnexos()throws BOException{
		List<FilaAnexo> listaFilaAnexo = new ArrayList<FilaAnexo>();
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", programa.getId());
				
		HashMap<String, String> orders = new HashMap<String, String>();
		orders.put("posFila","asc");
		
		try {
			List<Anexo> listaAnexos = super.findByParamsOrder(Anexo.class,  
															  parametros, 
															  orders);
			addFilaCabeceraAnexo(listaAnexos,programa.getId());
			if(!listaAnexos.isEmpty()){
								
				for(Anexo anexo : listaAnexos){
					FilaAnexo filaAnexo = new FilaAnexo();
					filaAnexo.setAnexo(anexo);
					listaFilaAnexo.add(filaAnexo);
				}
				asignarActivoColumna(listaFilaAnexo);
			}

		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}		
		return listaFilaAnexo;
	}	
	@Override
	public void asignarActivoColumna(List<FilaAnexo> listaFilaAnexo){
		boolean actCol1=false;
		boolean actCol2=false;
		boolean actCol3=false;
		boolean actCol4=false;
		boolean actCol5=false;
		boolean actCol6=false;
		boolean actCol7=false;
		boolean actCol8=false;
		boolean actCol9=false;
		boolean actCol10=false;
		try {
			if (listaFilaAnexo!=null && listaFilaAnexo.size()>0){
				for(FilaAnexo ofilaAnexo : listaFilaAnexo){
					ofilaAnexo.getAnexo().setActivoCol1("0");
					ofilaAnexo.getAnexo().setActivoCol2("0");
					ofilaAnexo.getAnexo().setActivoCol3("0");
					ofilaAnexo.getAnexo().setActivoCol4("0");
					ofilaAnexo.getAnexo().setActivoCol5("0");
					ofilaAnexo.getAnexo().setActivoCol6("0");
					ofilaAnexo.getAnexo().setActivoCol7("0");
					ofilaAnexo.getAnexo().setActivoCol8("0");
					ofilaAnexo.getAnexo().setActivoCol9("0");
					ofilaAnexo.getAnexo().setActivoCol10("0");
					
					if (ofilaAnexo.getAnexo().getColumna1()!=null && !ofilaAnexo.getAnexo().getColumna1().equals("")){
						actCol1=true;
					}
					if (ofilaAnexo.getAnexo().getColumna2()!=null && !ofilaAnexo.getAnexo().getColumna2().equals("")){
						actCol2=true;
					}
					if (ofilaAnexo.getAnexo().getColumna3()!=null && !ofilaAnexo.getAnexo().getColumna3().equals("")){
						actCol3=true;
					}
					if (ofilaAnexo.getAnexo().getColumna4()!=null && !ofilaAnexo.getAnexo().getColumna4().equals("")){
						actCol4=true;
					}
					if (ofilaAnexo.getAnexo().getColumna5()!=null && !ofilaAnexo.getAnexo().getColumna5().equals("")){
						actCol5=true;
					}
					if (ofilaAnexo.getAnexo().getColumna6()!=null && !ofilaAnexo.getAnexo().getColumna6().equals("")){
						actCol6=true;
					}
					if (ofilaAnexo.getAnexo().getColumna7()!=null && !ofilaAnexo.getAnexo().getColumna7().equals("")){
						actCol7=true;
					}
					if (ofilaAnexo.getAnexo().getColumna8()!=null && !ofilaAnexo.getAnexo().getColumna8().equals("")){
						actCol8=true;
					}
					if (ofilaAnexo.getAnexo().getColumna9()!=null && !ofilaAnexo.getAnexo().getColumna9().equals("")){
						actCol9=true;
					}
					if (ofilaAnexo.getAnexo().getColumna10()!=null && !ofilaAnexo.getAnexo().getColumna10().equals("")){
						actCol10=true;
					}
				}
				int contCol=0;
				int firtval=0;
				for(FilaAnexo ofilaAnexo : listaFilaAnexo){
					if (actCol1){
						ofilaAnexo.getAnexo().setActivoCol1("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol2){
						ofilaAnexo.getAnexo().setActivoCol2("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol3){
						ofilaAnexo.getAnexo().setActivoCol3("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol4){
						ofilaAnexo.getAnexo().setActivoCol4("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol5){
						ofilaAnexo.getAnexo().setActivoCol5("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol6){
						ofilaAnexo.getAnexo().setActivoCol6("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol7){
						ofilaAnexo.getAnexo().setActivoCol7("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol8){
						ofilaAnexo.getAnexo().setActivoCol8("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol9){
						ofilaAnexo.getAnexo().setActivoCol9("1");
						if (firtval==0)
							contCol+=1;
					}
					if (actCol10){
						ofilaAnexo.getAnexo().setActivoCol10("1");
						if (firtval==0)
							contCol+=1;
					}
					firtval=+1;
					ofilaAnexo.getAnexo().setNumColumna(String.valueOf(contCol));
				}
			}
		} catch (Exception e) {
			 
		}
	}

/**
 * construye la lista de anexos 
 */
@Override
public List<FilaAnexo> findAnexosTotal()throws BOException{
	List<FilaAnexo> listaFilaAnexo = new ArrayList<FilaAnexo>();
	HashMap<String, Object> parametros = new HashMap<String, Object>();
	parametros.put("programa", programa.getId());
	parametros.put("tipoTotal", "TT");
	
	HashMap<String, String> orders = new HashMap<String, String>();
	orders.put("posFila","asc");
	
	try {
		List<Anexo> listaAnexos = super.findByParamsOrder(Anexo.class,  
														  parametros, 
														  orders);
		//removerFilaTotalAnexo(listaAnexos,activaRemovetotal);
		
		Long idAnexoMin = 0L;
		Long idAnexoMax = 0L;
		if(!listaAnexos.isEmpty()){
			idAnexoMin = listaAnexos.get(0).getId();
			idAnexoMax = listaAnexos.get(0).getId();
			List listparam = new ArrayList();

			
			//ini MCG20131023
//			listparam.add(idAnexoMin);
//			listparam.add(idAnexoMax);
//			List<AnexoColumna> listaAnexosColumna = super.executeListNamedQuery("findColumnasByAnexo", 
//																				listparam);																listparam);
			listparam.add(programa.getId()); 
			List<AnexoColumna> listaAnexosColumna = super.executeListNamedQuery("findColumnasByAnexoidProgram", 
					listparam);				
			//fin MCG20131023
			

			for(Anexo anexo : listaAnexos){
				FilaAnexo filaAnexo = new FilaAnexo();
				List<AnexoColumna> listaTemp = new ArrayList<AnexoColumna>();
				filaAnexo.setAnexo(anexo);					
				for(AnexoColumna ac : listaAnexosColumna){
					if(ac.getAnexo().getId().equals(anexo.getId())){
						listaTemp.add(ac);
					}	
				}
				//ini MCG20130802
				insertNuevaColumna(listaTemp);
				//fin MCG20130802
				filaAnexo.setListaAnexoColumna(listaTemp);
				listaFilaAnexo.add(filaAnexo);
			}
		}

	} catch (BOException e) {
		throw new BOException(e.getMessage());
	}
	
	return listaFilaAnexo;
}
	
	public void removerFilaTotalAnexo(List<Anexo> listaAnexos,boolean activaRemovetotal){		
		try {
			if (activaRemovetotal){
				if(!listaAnexos.isEmpty()){
					for (Anexo oanexo:listaAnexos){
						if (oanexo.getTipoFila()!=null && oanexo.getTipoFila()==6){
							listaAnexos.remove(oanexo);
						}					
					}				
				}
			}
		} catch (Exception e) {
			 
		}
	}
	
	public void addFilaCabeceraAnexo(List<Anexo> listaAnexos,Long Idprograma){
		boolean encontroCab=false;
		if (listaAnexos!=null && listaAnexos.size()>0){
			for (Anexo oanexo :listaAnexos){
				if (oanexo.getTipoFila().equals(TiposFila.TIPO_CABECERA.tipo)){
					encontroCab=true;
					break;
				}				
			}
		}
		if (!encontroCab){
			Programa oprograma=new Programa();
			oprograma.setId(Idprograma);
			Anexo oanexoCab=new Anexo();
			oanexoCab.setId(null);
			oanexoCab.setPosFila(-1);
			oanexoCab.setPrograma(oprograma);
			oanexoCab.setTipoFila(0);
			oanexoCab.setCodigoFila("CAB000001");
			oanexoCab.setDescripcion("DESCRIPCION");
			oanexoCab.setBureau("BURO");			
			oanexoCab.setRating("RATING");			
			oanexoCab.setFecha("FECHA DE VENCIMIENTO");		
			oanexoCab.setLteAutorizado("LTE AUTORIZADO");	
			oanexoCab.setLteForm("LTE FORM");			
			oanexoCab.setRgoActual("RGO ACTUAL");	
			oanexoCab.setRgoPropBbvaBc("RGO PROP BBVA BC");	
			oanexoCab.setPropRiesgo("PROP RIESGO");		
			oanexoCab.setObservaciones("OBSERVACION");	
			listaAnexos.add(0,oanexoCab);
		}
	}
	
	public  void insertNuevaColumna(List<AnexoColumna> olistaAnexoColumna) {
		int contcol=0;
		int contcol2=0;
		if (olistaAnexoColumna!=null && olistaAnexoColumna.size()>0){
		
			for(AnexoColumna ac : olistaAnexoColumna){
				if (ac.getDescripcion().trim().equals(Constantes.DESC_COLUMNA_PROP_RIESGO)){
					contcol=contcol+1;
				}
				if (ac.getDescripcion().trim().equals(Constantes.DESC_BUREAU)){
					contcol2=contcol2+1;
				}
			}		
			if (contcol==0){
				AnexoColumna propRiesgo = new AnexoColumna();
				propRiesgo.setId(0L);
				propRiesgo.setDescripcion(Constantes.DESC_COLUMNA_PROP_RIESGO);
				//propRiesgo.setPosColumna(6); MCG20140818
				propRiesgo.setPosColumna(7);
				//propRiesgo.setValor("");
				olistaAnexoColumna.add(propRiesgo);
			}				
			if (contcol2==0){
				AnexoColumna bureau = new AnexoColumna();
				bureau.setId(0L);
				bureau.setDescripcion(Constantes.DESC_BUREAU);
				bureau.setPosColumna(0);
				olistaAnexoColumna.add(bureau);
			}		
		}
		
		
		Collections.sort(olistaAnexoColumna); 		
	}
	
	//ini mlj 20130404	
	@Override
	public String findValorByColumnaAnexos(Long idprograma, String descEmpresa, String nombreColumna)throws BOException{
		String valor = "";
		try {
		
			List listparam = new ArrayList();
			listparam.add(idprograma);
			listparam.add(descEmpresa);
			listparam.add(nombreColumna);
			List<AnexoColumna> listaAnexosColumna = super.executeListNamedQuery("findColumnaByAnexoAndDescripcion", 
																				listparam);
			if(listaAnexosColumna!=null && !listaAnexosColumna.isEmpty()){
				valor = listaAnexosColumna.get(0).getValor();
			}

		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		
		return valor;
	}
	//fin mlj 20130404
	

	//ini MCG20130822
	@Override
	public Anexo findValorByColumnaAnexosByEmpresa(Long idprograma, String descEmpresa)throws BOException{
		String valor = "";
		List<AnexoColumna> listaAnexosColumna=new ArrayList<AnexoColumna>();
		Anexo oAnexos=new Anexo();
		try {		
		
			
			String bancolocal="";
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
				bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
			} catch (Exception e) {
				bancolocal=Constantes.NOM_BANCO_DEFAULT;
			}
			List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
			List<FilaAnexo> olistaFilaAnexosHijos=new ArrayList<FilaAnexo>();
			Programa oprograma=new Programa();
			oprograma.setId(idprograma);
			
			listaFilaAnexos=findAnexosByPrograma(oprograma);
			if(listaFilaAnexos!= null && !listaFilaAnexos.isEmpty()){				

				Long idbancoPadre=null;				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadrelimite=null;
								
				for(FilaAnexo filaAnexo : listaFilaAnexos){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);					
						lpadrebanco=filaAnexo.getAnexo().getId();						
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getId();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getId();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (filaAnexo.getAnexo().getTipoFila().equals(1) && filaAnexo.getAnexo().getDescripcion().equals(bancolocal) ){							
							idbancoPadre=lpadrebanco;												
					}				
				}
				olistaFilaAnexosHijos=listaAnexosByIdPadre(listaFilaAnexos, idbancoPadre);
				if (olistaFilaAnexosHijos!=null && olistaFilaAnexosHijos.size()>0){
					for(FilaAnexo filaAnexo : olistaFilaAnexosHijos){						
						if (filaAnexo.getAnexo().getTipoFila().equals(2) && filaAnexo.getAnexo().getDescripcion()!=null && filaAnexo.getAnexo().getDescripcion().trim().equals(descEmpresa)){						
							//listaAnexosColumna=filaAnexo.getListaAnexoColumna();
							oAnexos=filaAnexo.getAnexo();
							break;
						}
					}					
				}				
			}			
			


		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		
		return oAnexos;
	}
	@Override
	public List<FilaAnexo> obtenerListAnexosHijosByEmpresa(Long idprograma,String nombreEmpresa)throws BOException{
		
		List<FilaAnexo>  listaAnexosHijosEmpresa=new ArrayList<FilaAnexo>() ;
		try {				
			
			String bancolocal="";
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
				bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
			} catch (Exception e) {
				bancolocal=Constantes.NOM_BANCO_DEFAULT;
			}
			List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
			List<FilaAnexo> olistaFilaAnexosHijosBanco=new ArrayList<FilaAnexo>();
			
			Programa oprograma=new Programa();
			oprograma.setId(idprograma);
			
			listaFilaAnexos=findAnexosByPrograma(oprograma);
			if(listaFilaAnexos!= null && !listaFilaAnexos.isEmpty()){				
				Long idEmpresa=null;	
				Long idbancoPadre=null;				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadrelimite=null;
								
				for(FilaAnexo filaAnexo : listaFilaAnexos){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);					
						lpadrebanco=filaAnexo.getAnexo().getId();						
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getId();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getId();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (filaAnexo.getAnexo().getTipoFila().equals(1) && filaAnexo.getAnexo().getDescripcion().equals(bancolocal) ){							
							idbancoPadre=filaAnexo.getAnexo().getId();												
					}				
				}
				olistaFilaAnexosHijosBanco=listaAnexosByIdPadre(listaFilaAnexos, idbancoPadre);
				if (olistaFilaAnexosHijosBanco!=null && olistaFilaAnexosHijosBanco.size()>0){
					for (FilaAnexo ofilaAnexo: olistaFilaAnexosHijosBanco){
						if (ofilaAnexo.getAnexo().getDescripcion()!=null ){
						    if ( ofilaAnexo.getAnexo().getDescripcion().trim().equals(nombreEmpresa.trim())){							
							 idEmpresa=ofilaAnexo.getAnexo().getId();
							 break;
							}
						}						
					}
					if (idEmpresa!=null){
						listaAnexosHijosEmpresa=listaAnexosByIdPadre(olistaFilaAnexosHijosBanco, idEmpresa);
					}
				}	
				
				
			}
		} catch (BOException e) {
			listaAnexosHijosEmpresa=new ArrayList<FilaAnexo>() ;			
		}		
		return listaAnexosHijosEmpresa;
	}
	@Override
	public List<FilaAnexo> obtenerListAnexosHijosByEmpresaInclueEmpresa(Long idprograma,String nombreEmpresa)throws BOException{
		
		List<FilaAnexo>  listaAnexosHijosEmpresa=new ArrayList<FilaAnexo>() ;
		List<FilaAnexo>  listaAnexosHijosTemp=new ArrayList<FilaAnexo>() ;
		try {				
			
			String bancolocal="";
			try {
				Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
				bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
			} catch (Exception e) {
				bancolocal=Constantes.NOM_BANCO_DEFAULT;
			}
			List<FilaAnexo> listaFilaAnexos = new ArrayList<FilaAnexo>();
			List<FilaAnexo> olistaFilaAnexosHijosBanco=new ArrayList<FilaAnexo>();
			
			Programa oprograma=new Programa();
			oprograma.setId(idprograma);
			
			listaFilaAnexos=findAnexosByPrograma(oprograma);
			if(listaFilaAnexos!= null && !listaFilaAnexos.isEmpty()){				
				Long idEmpresa=null;	
				Long idbancoPadre=null;				
				Long lpadrebanco=null;
				Long lpadreempresa=null;
				Long lpadrelimite=null;
								
				for(FilaAnexo filaAnexo : listaFilaAnexos){						
					if (filaAnexo.getAnexo().getTipoFila().equals(1)){						
						filaAnexo.getAnexo().setPadretmp(null);					
						lpadrebanco=filaAnexo.getAnexo().getId();						
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(2))||(filaAnexo.getAnexo().getTipoFila().equals(7))){						
						filaAnexo.getAnexo().setPadretmp(lpadrebanco);
						lpadreempresa=filaAnexo.getAnexo().getId();
					}else if ((filaAnexo.getAnexo().getTipoFila().equals(3))||(filaAnexo.getAnexo().getTipoFila().equals(5))){						
						filaAnexo.getAnexo().setPadretmp(lpadreempresa);
						lpadrelimite=filaAnexo.getAnexo().getId();
					}else if (filaAnexo.getAnexo().getTipoFila().equals(4)){						
						filaAnexo.getAnexo().setPadretmp(lpadrelimite);						
					}
					
					if (filaAnexo.getAnexo().getTipoFila().equals(1) && filaAnexo.getAnexo().getDescripcion().equals(bancolocal) ){							
							idbancoPadre=filaAnexo.getAnexo().getId();												
					}				
				}
				olistaFilaAnexosHijosBanco=listaAnexosByIdPadre(listaFilaAnexos, idbancoPadre);
				FilaAnexo ofilaAnexoEmpresa=new FilaAnexo();
				if (olistaFilaAnexosHijosBanco!=null && olistaFilaAnexosHijosBanco.size()>0){
					for (FilaAnexo ofilaAnexo: olistaFilaAnexosHijosBanco){
						if (ofilaAnexo.getAnexo().getDescripcion()!=null ){
						    if ( ofilaAnexo.getAnexo().getDescripcion().trim().equals(nombreEmpresa.trim())){							
							 idEmpresa=ofilaAnexo.getAnexo().getId();
							 ofilaAnexoEmpresa=ofilaAnexo;
							 break;
							}
						}						
					}
					if (idEmpresa!=null){
						
						listaAnexosHijosTemp=listaAnexosByIdPadre(olistaFilaAnexosHijosBanco, idEmpresa);
						if (listaAnexosHijosTemp!=null && listaAnexosHijosTemp.size()>0){
							listaAnexosHijosEmpresa.add(ofilaAnexoEmpresa);
							for (FilaAnexo ofilaAnexoE: listaAnexosHijosTemp){
								listaAnexosHijosEmpresa.add(ofilaAnexoE);
							}
						}
						
					}
				}	
				
				
			}
		} catch (BOException e) {
			listaAnexosHijosEmpresa=new ArrayList<FilaAnexo>() ;			
		}		
		return listaAnexosHijosEmpresa;
	}
	
	//fin MCG20130822
	
	//INI MCG 20120503
	@Override
	public List<FilaAnexo> findAnexosByPrograma(Programa oprograma)throws BOException{
		List<FilaAnexo> listaFilaAnexo = new ArrayList<FilaAnexo>();
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("programa", oprograma.getId());		
		HashMap<String, String> orders = new HashMap<String, String>();
		orders.put("posFila","asc");
		
		try {
			List<Anexo> listaAnexos = super.findByParamsOrder(Anexo.class,parametros,orders);
			addFilaCabeceraAnexo(listaAnexos,oprograma.getId());
			if(!listaAnexos.isEmpty()){
				for(Anexo anexo : listaAnexos){
					FilaAnexo filaAnexo = new FilaAnexo();					
					filaAnexo.setAnexo(anexo);
					listaFilaAnexo.add(filaAnexo);
				}
				asignarActivoColumna(listaFilaAnexo);
			}
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		
		return listaFilaAnexo;
	}
	//FIN MCG 20120503
	@Override
	public boolean deleteFila(int posAnexo, 
						   List<FilaAnexo> listaFilaAnexosTemp)throws BOException{
		boolean flagEliminado = false;
		FilaAnexo filaAnexoSig = new FilaAnexo();
		for(int i=0 ; i<listaFilaAnexosTemp.size(); i++){
			FilaAnexo filaAnexo = listaFilaAnexosTemp.get(i);
			if(filaAnexo.getPos()==posAnexo){
				//si es la utlima posicion se elimina directamente
				if(i+1==listaFilaAnexosTemp.size()){
					listaFilaAnexosTemp.remove(i);
					flagEliminado = true;
					break;
				}
				if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_BANCO.tipo)){
					if(i+1<listaFilaAnexosTemp.size()){
						filaAnexoSig = listaFilaAnexosTemp.get(i+1);
						if((!filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo)) && (!filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo)))  {
							listaFilaAnexosTemp.remove(i);
							flagEliminado = true;
							break;
						}else{
							throw new BOException("El item tiene un detalle no es posible eliminarlo");
						}
					}
				}else if((filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_EMPRESA.tipo))||(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_ACCIONISTA.tipo))){
					if(i+1<listaFilaAnexosTemp.size()){
						filaAnexoSig = listaFilaAnexosTemp.get(i+1);
						if(!(filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)) && !(filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo))){
							listaFilaAnexosTemp.remove(i);
							flagEliminado = true;
							break;
						}else{
							throw new BOException("El item tiene un detalle no es posible eliminarlo");
						}
					}
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)){
					if(i+1<listaFilaAnexosTemp.size()){
						FilaAnexo filaAnexoAnt = listaFilaAnexosTemp.get(i-1);
						filaAnexoSig = listaFilaAnexosTemp.get(i+1);
						if((
								filaAnexoAnt.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)||
								filaAnexoAnt.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)
								) ||
								(
									 filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)||
									 filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_LIMITES.tipo)
								)){
							listaFilaAnexosTemp.remove(i);
							flagEliminado = true;
							break;
						}else{
							throw new BOException("No es posible eliminar el Item");
						}						
					}
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_OPERACION.tipo)){
					if(i+1<listaFilaAnexosTemp.size()){
						FilaAnexo filaAnexoAnt = listaFilaAnexosTemp.get(i-1);
						filaAnexoSig = listaFilaAnexosTemp.get(i+1);
						if(!(filaAnexoSig.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo))){
							listaFilaAnexosTemp.remove(i);
							flagEliminado = true;
							break;
						}else{
							throw new BOException("No es posible eliminar el Item");
						}						
					}
				}else if(filaAnexo.getAnexo().getTipoFila().equals(TiposFila.TIPO_SUB_LIMITE.tipo)){
					listaFilaAnexosTemp.remove(i);
					flagEliminado = true;
					break;
				}
			}
		}
		return flagEliminado;
	}
	
	/**
	 * Buscar un objeto anexo columan por el id anexo
	 */
	@Override
	public List<AnexoColumna> findByAnexoId(Long idAnexo)throws BOException{
		HashMap<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("anexo", idAnexo);
		List<AnexoColumna> listaAnexoColumna = (List<AnexoColumna>)super.findByParams2(AnexoColumna.class, parametros);
		//ini MCG20130802
		insertNuevaColumna(listaAnexoColumna);
		//fin MCG20130802
		return listaAnexoColumna;
	}
	
	//INI MCG20121119
	@Override
	public List<SaldoCliente> findListaSaldoCliente(String codCliente) throws BOException {
		List<SaldoCliente> listSaldoCliente = null;	
		try{			
		listSaldoCliente = anexoDAO.findListaSaldoCliente(codCliente);
		
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
		return listSaldoCliente;
	}
	//FIN MCG20121119
	
	//ini MCG20141006
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarAnexoRatingpf(Long idAnexo,String strRating,String strFechaRating) throws BOException{
		List parametros = new ArrayList();
		parametros.add(strRating);
		parametros.add(strFechaRating);
		parametros.add(idAnexo);		
		
		try {
			super.executeNamedQuery("updateRatingAnexopf",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	//fin MCG20141006
	
	//ini MCG20130709
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarAnexoRating(Long idColumnaAnexo,String strRating) throws BOException{
		List parametros = new ArrayList();
		parametros.add(strRating);
		parametros.add(idColumnaAnexo);		
		
		try {
			super.executeNamedQuery("updateRatingAnexoActulizacion",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void actualizarAnexoRatingFecha(Long idColumnaAnexo,String strFechaRating) throws BOException{
		List parametros = new ArrayList();
		parametros.add(strFechaRating);
		parametros.add(idColumnaAnexo);		
		
		try {
			super.executeNamedQuery("updateRatingAnexoActulizacion",parametros);
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
	}
	
	//fin MCG20130709
	
	public List<FilaAnexo> getListaFilaAnexos() {
		return listaFilaAnexos;
	}

	public void setListaFilaAnexos(List<FilaAnexo> listaFilaAnexos) {
		this.listaFilaAnexos = listaFilaAnexos;
	}
	
	

	public List<FilaAnexo> getListaFilaAnexosTotal() {
		return listaFilaAnexosTotal;
	}

	public void setListaFilaAnexosTotal(List<FilaAnexo> listaFilaAnexosTotal) {
		this.listaFilaAnexosTotal = listaFilaAnexosTotal;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ProgramaBO getProgramaBO() {
		return programaBO;
	}

	public void setProgramaBO(ProgramaBO programaBO) {
		this.programaBO = programaBO;
	}

	public AnexoDAO getAnexoDAO() {
		return anexoDAO;
	}

	public void setAnexoDAO(AnexoDAO anexoDAO) {
		this.anexoDAO = anexoDAO;
	}
	@Override
	public void buscarHijos(Long idfila, List<FilaAnexo> olistaFilaAnexos,
			List<FilaAnexo> olistaFilaAnexosFinal) {
		for (FilaAnexo filaAnexo : olistaFilaAnexos) {
			if (filaAnexo.getAnexo().getPadretmp() != null
					&& filaAnexo.getAnexo().getPadretmp().equals(idfila)) {
				olistaFilaAnexosFinal.add(filaAnexo);
				buscarHijos(filaAnexo.getAnexo().getId(), olistaFilaAnexos,
						olistaFilaAnexosFinal);
			}
		}
	}
	
	public void buscarHijosIdTemp(Long idfila, List<FilaAnexo> olistaFilaAnexos,
			List<FilaAnexo> olistaFilaAnexosFinal) {
		for (FilaAnexo filaAnexo : olistaFilaAnexos) {
			if (filaAnexo.getAnexo().getPadretmp() != null
					&& filaAnexo.getAnexo().getPadretmp().equals(idfila)) {
				olistaFilaAnexosFinal.add(filaAnexo);
				buscarHijosIdTemp(filaAnexo.getAnexo().getIdtemp(), olistaFilaAnexos,
						olistaFilaAnexosFinal);
			}
		}
	}
	@Override
	public Long findIdAnexoBancoLocal(List<FilaAnexo> listaAllAnexos, Long idEmpresa) throws BOException {
		String bancolocal;
		Long idAnexoBanco = null;
		Anexo filaBanco = null;
		try {
			Parametro parametro = parametroBO
					.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
			bancolocal = parametro.getValor() == null ? Constantes.NOM_BANCO_DEFAULT
					: parametro.getValor().toString();

		if (listaAllAnexos != null && listaAllAnexos.size() > 0) {
			for (FilaAnexo filaAnexo : listaAllAnexos) {
				if (filaAnexo.getAnexo().getTipoFila().equals(2) && filaAnexo.getAnexo().getId().equals(idEmpresa)){
					filaBanco =  new Anexo();
					filaBanco = findFilaAnexoById(filaAnexo.getAnexo().getPadretmp());
					if(filaBanco.getDescripcion().equals(bancolocal)){
						//MCG20030805 idAnexoBanco = filaAnexo.getAnexo().getId();
						idAnexoBanco = filaAnexo.getAnexo().getPadretmp();
						break;
					}
				}
			}
		}
		
		} catch (BOException e) {
			throw new BOException(e.getMessage());
		} catch (Exception e) {
			bancolocal = Constantes.NOM_BANCO_DEFAULT;
		}
		return idAnexoBanco;
	}
	@Override
	public Long findIdAnexoByEmpresa(List<FilaAnexo> olistaFilaAnexo,
			String nombreEmpresa) {
		Long idAnexoEmpresa = null;
		for (FilaAnexo ofilaAnexo : olistaFilaAnexo) {
			if (ofilaAnexo.getAnexo().getTipoFila().equals(2)
					&& ofilaAnexo.getAnexo().getDescripcion().trim().equals(
							nombreEmpresa.trim())) {
				idAnexoEmpresa = ofilaAnexo.getAnexo().getId();
				break;
			}
		}

		return idAnexoEmpresa;
	}
	@Override
	public List<FilaAnexo> listaAnexosByIdPadre(
			List<FilaAnexo> olistaFilaAnexo, Long idPadre) {
		List<FilaAnexo> olistaFilaAnexoFinal=new ArrayList<FilaAnexo>();
		buscarHijos(idPadre,olistaFilaAnexo,olistaFilaAnexoFinal);
		return olistaFilaAnexoFinal;
	}
	@Override
	public List<FilaAnexo> listaAnexosByIdPadreIdTemp(
			List<FilaAnexo> olistaFilaAnexo, Long idPadre) {
		List<FilaAnexo> olistaFilaAnexoFinal=new ArrayList<FilaAnexo>();
		buscarHijosIdTemp(idPadre,olistaFilaAnexo,olistaFilaAnexoFinal);
		return olistaFilaAnexoFinal;
	}
	@Override
	public Anexo findFilaAnexoById(Long idAnexo) throws BOException {
		List<Anexo> anexos = null;
		try 
		{
			List parametros = new ArrayList();
			parametros.add(idAnexo);			
			anexos = super.listNamedQuery("obtieneAnexoPorId", parametros);
		} 
		catch (Exception e) {					
			e.printStackTrace();
			throw new BOException(e.getMessage());
		}
		
		if(anexos.size() == 0)
			return null;
		else
			return anexos.get(0);
	}
	@Override
	public String findValorColumnaById(Long idprograma, String descEmpresa,
			String nombreColumna, Long idAnexo) throws BOException {
		String valor = "";
		try {
		
			List listparam = new ArrayList();
			listparam.add(idprograma);
			listparam.add(descEmpresa);
			listparam.add(nombreColumna);
			listparam.add(idAnexo);
			List<AnexoColumna> listaAnexosColumna = super.executeListNamedQuery("findColumnaByIdAndDescripcion", 
																				listparam);
			if(listaAnexosColumna!=null && !listaAnexosColumna.isEmpty()){
				valor = listaAnexosColumna.get(0).getValor();
			}

		} catch (BOException e) {
			throw new BOException(e.getMessage());
		}
		
		return valor;
	}


	//ini MCG20130805
	@Override
	public List<LimiteFormalizado> loadLimiteFormalizadoByAnexos(Programa oprograma) throws BOException {
		
		List<LimiteFormalizado> listLimiteFormalizado=new ArrayList<LimiteFormalizado>();		
		Long idEmpresa = null;	
		String bancolocal="";
		try {			
		
				
				List<FilaAnexo> olistaFilaAnexos=findAnexosByPrograma(oprograma);	
				List<FilaAnexo> olistaFilaAnexosTotal=new ArrayList<FilaAnexo>();
				List<FilaAnexo> olistaFilaAnexosxBanco=new ArrayList<FilaAnexo>();
				
				try {
					Parametro parametro = parametroBO.findByNombreParametro(Constantes.NOM_BANCO_BBVA);
					bancolocal=parametro.getValor()==null?Constantes.NOM_BANCO_DEFAULT:parametro.getValor().toString() ;
				} catch (Exception e) {
					bancolocal=Constantes.NOM_BANCO_DEFAULT;
				}
				if(olistaFilaAnexos != null && olistaFilaAnexos.size()>0){			
					for(FilaAnexo filaAnexo : olistaFilaAnexos){
						
						if (filaAnexo.getAnexo().getTipoFila().equals(1) && 
								filaAnexo.getAnexo().getDescripcion().equals(bancolocal)) {							
							olistaFilaAnexosxBanco.add(filaAnexo);
						}else if (filaAnexo.getAnexo().getTipoFila().equals(6)){						
							olistaFilaAnexosTotal.add(filaAnexo);						
						}						
					}					

					List<FilaAnexo> olistaFilaAnexostotalfiltro=new ArrayList<FilaAnexo>();
					if (olistaFilaAnexosTotal!=null && olistaFilaAnexosTotal.size()>0){
						olistaFilaAnexostotalfiltro.addAll(olistaFilaAnexosTotal);
					}else{
						if (olistaFilaAnexosxBanco!=null && olistaFilaAnexosxBanco.size()>0){
							olistaFilaAnexostotalfiltro.addAll(olistaFilaAnexosxBanco);
						}						
					}
						
					if (olistaFilaAnexostotalfiltro!=null && olistaFilaAnexostotalfiltro.size()>0){						
				        float totallimiteAutorizado = 0;			
						float totallimiteFormalizado = 0;			
						float totallimitePropuesto = 0;						
					
						for (FilaAnexo afilaAnexo: olistaFilaAnexostotalfiltro){		
							String limiteAutorizado="";
							String limiteFormalizado="";
							String limitePropuesto="";
							
							if(afilaAnexo.getAnexo().getTipoFila().equals(1)||afilaAnexo.getAnexo().getTipoFila().equals(6)){
								
								//COLUMN_LIMITE_AUTORIZADO ="LTE AUTORIZADO";
								//COLUMN_LIMITE_FORM ="LTE FORM";
								//COLUMN_RIESGO_PROPUESTO ="RGO PROP BBVA BC";				
								limiteAutorizado=afilaAnexo.getAnexo().getLteAutorizado()==null?"":afilaAnexo.getAnexo().getLteAutorizado();																	
								
								totallimiteAutorizado += Float.valueOf(FormatUtil.FormatNumeroSinComa(limiteAutorizado));
								
								limiteFormalizado=afilaAnexo.getAnexo().getLteForm()==null?"":afilaAnexo.getAnexo().getLteForm();
								
								totallimiteFormalizado += Float.valueOf(FormatUtil.FormatNumeroSinComa(limiteFormalizado));
								
								limitePropuesto=afilaAnexo.getAnexo().getRgoPropBbvaBc()==null?"":afilaAnexo.getAnexo().getRgoPropBbvaBc();
								totallimitePropuesto += Float.valueOf(FormatUtil.FormatNumeroSinComa(limitePropuesto));
								
							}
						}
						String TotalAuto=""+FormatUtil.roundSinDecimalsPunto(totallimiteAutorizado);		
						String Totalformal=""+FormatUtil.roundSinDecimalsPunto(totallimiteFormalizado);		
						String Totalprop=""+FormatUtil.roundSinDecimalsPunto(totallimitePropuesto);
						LimiteFormalizado olimite=new LimiteFormalizado();
						olimite.setLimiteAutorizado(TotalAuto);
						olimite.setTotal(Totalformal);
						olimite.setLimitePropuesto(Totalprop);
						listLimiteFormalizado.add(olimite);
					}			
				}
				
		
		} catch (BOException  e) {
			 
			listLimiteFormalizado=new ArrayList<LimiteFormalizado>();
		}catch ( Exception e){	
			listLimiteFormalizado=new ArrayList<LimiteFormalizado>();
		}
		return listLimiteFormalizado;
	}
	
	//fin MCG20130805
	//fin MCG20141111
	@Override
	public List<GarantiaHost> findListaGarantiaHost(Empresa oempresa) throws BOException {
		List<GarantiaHost> listGarantiaHost = null;	
		try{			
		listGarantiaHost = anexoDAO.findListaGarantiaHost(oempresa);
		
		} catch (DAOException e) {
			throw new BOException(e.getMessage());
		} 
		return listGarantiaHost;
	}
	//fin MCG20141111

	
}
