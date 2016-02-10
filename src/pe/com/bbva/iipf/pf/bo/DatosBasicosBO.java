package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.Accionista;
import pe.com.bbva.iipf.pf.model.CabTabla;
import pe.com.bbva.iipf.pf.model.CapitalizacionBursatil;
import pe.com.bbva.iipf.pf.model.CompraVenta;
import pe.com.bbva.iipf.pf.model.ConfiguracionWS;
import pe.com.bbva.iipf.pf.model.DatosBasico;
import pe.com.bbva.iipf.pf.model.NegocioBeneficio;
import pe.com.bbva.iipf.pf.model.Participaciones;
import pe.com.bbva.iipf.pf.model.Planilla;
import pe.com.bbva.iipf.pf.model.PrincipalesEjecutivos;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.RatingExterno;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface DatosBasicosBO {

	public void updateDatosBasicos() throws BOException;
	public void updateDatosBasicos(String codEmpresaGrupo) throws BOException;
	public void refreshDatosBasicos(String codEmpresaGrupo) throws BOException;
	
	public void setUsuarioSession(UsuarioSesion usuarioSession);	
	public void setPathWebService(String pathWebService);
	public void setConfiguracionWS(ConfiguracionWS configuracionWS);	
	public void setListaGrupoEmpresas(List<Empresa> listaGrupoEmpresas) ;
	
	public void setListaPlanilla(List<Planilla> listaTempPlanilla);
	public void setTotalPlanilla(Planilla totalPlanilla) ;
	public void setPlanillaAdmin(Planilla planillaAdmin);
	public Planilla getPlanillaNoAdmin() ;
	public void setPlanillaNoAdmin(Planilla planillaNoAdmin) ;
	public void setPrograma(Programa programa);
	public void setSintesisEmpresa(String sintesisEmpresa);
	public void setListaAccionistas(List<Accionista> listaAccionistas) ;
	public void setListaCapitalizacion(List<CapitalizacionBursatil> listaCapitalizacion);
	//ini MCG20111025
	public void setListaCabTabla(List<CabTabla> listaCabTabla);
	public void setListaCabTablaPlanilla(List<CabTabla> listaCabTablaPlanilla) ;
	public void setListaCabTablaCompra(List<CabTabla> listaCabTablaCompra);
	public void setListaCabTablaVenta(List<CabTabla> listaCabTablaVenta) ;
	public void setListaCabTablaActividad(List<CabTabla> listaCabTablaActividad);
	public void setListaCabTablaNegocio(List<CabTabla> listaCabTablaNegocio);
	//fin MCG20111025
	public void saveUDAccionista()throws BOException;
	public void saveUDAccionista(String codEmpresaGrupo )throws BOException;
	public void saveUDCapitalizacion(String codEmpresaGrupo)throws BOException;
	public void setListaPrinciEjecutivos(List<PrincipalesEjecutivos> listaPrinciEjecutivos);
	public void saveUDEjecutivos() throws BOException;
	public void saveUDEjecutivos(String codEmpresaGrupo) throws BOException;
	public void setListaParticipaciones(List<Participaciones> listaParticipaciones); 
	public void saveUDParticipaciones() throws BOException;
	public void saveUDParticipaciones(String codEmpresaGrupo) throws BOException;
	public void setListaRatingExterno(List<RatingExterno> listaRatingExterno) ;
	public void saveUDRatingExerno()throws BOException;
	public void saveUDRatingExerno(String codEmpresaGrupo) throws BOException ;
	public void setListaCompraVenta(List<CompraVenta> listaCompraVenta);
	public void saveUDCompraVenta()throws BOException;
	public void setListaNBActividades(List<NegocioBeneficio> listaNBActividades);
	public void saveUDActividades()throws BOException;
	public void saveUDActividades(String codEmpresaGrupo) throws BOException ;
	public void setListaNBNegocio(List<NegocioBeneficio> listaNBNegocio) ;
	public void saveUDNegocio()throws BOException;
	public void saveUDNegocio(String codEmpresaGrupo) throws BOException;
	public List<Planilla> getListaPlanilla()throws BOException;
	public List<Planilla> getListaPlanilla(String CodEmpresaGrupo)throws BOException;
	public List<Planilla> getListaPlanilla(Programa oprograma)throws BOException ;
	public List<Planilla> getListaPlanilla(Programa oprograma,String CodEmpresaGrupo)throws BOException;
	public List<Accionista> getListaAccionistas() throws BOException;
	public List<Accionista> getListaAccionistas(String CodEmpresaGrupo) throws BOException ;
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(String CodEmpresaGrupo) throws BOException ;
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(Programa oprograma,String CodEmpresaGrupo) throws BOException;
	public List<CapitalizacionBursatil> getListaCapitalizacionBursatil(Programa oprograma) throws BOException;
	
	public List<Accionista> getListaAccionistasByOrdenPorc(Long idprograma) throws BOException;
	public List<Accionista> getListaAccionistasByOrdenPorc(Long idprograma,String codEmpresaGrupo) throws BOException ;
	public List<Accionista> getListaAccionistas(Programa oprograma) throws BOException ;
	public List<Accionista> getListaAccionistas(Programa oprograma,String CodEmpresaGrupo) throws BOException;
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos()throws BOException;
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(String codEmpresaGrupo)	throws BOException;
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(Programa oprograma)	throws BOException;
	public List<PrincipalesEjecutivos> getListaPrinciEjecutivos(Programa oprograma,String CodEmpresaGrupo)	throws BOException;
	public List<Participaciones> getListaParticipaciones()throws BOException; 
	public List<Participaciones> getListaParticipaciones(String codEmpresaGrupo) throws BOException ;
	public List<Participaciones> getListaParticipaciones(Programa oprograma) throws BOException;
	public List<Participaciones> getListaParticipaciones(Programa oprograma,String CodEmpresaGrupo) throws BOException ;
	public List<RatingExterno> getListaRatingExterno() throws BOException;
	public List<RatingExterno> getListaRatingExterno(String codEmpresaGrupo) throws BOException ;
	public List<RatingExterno> getListaRatingExterno(Programa oprograma) throws BOException;
	public List<RatingExterno> getListaRatingExterno(Programa oprograma,String CodEmpresaGrupo) throws BOException;
	public List<CompraVenta> getListaCompraVenta()throws BOException;
	public List<CompraVenta> getListaCompraVenta(String codEmpresaGrupo) throws BOException ;
	public List<CompraVenta> getListaCompraVenta(Programa oprograma) throws BOException ;
	public List<CompraVenta> getListaCompraVenta(Programa oprograma,String CodEmpresaGrupo) throws BOException ;
	public List<NegocioBeneficio> getListaNBActividades()throws BOException;
	public List<NegocioBeneficio> getListaNBActividades(String codEmpresaGrupo) throws BOException;
	public List<NegocioBeneficio> getListaNBActividades(Programa oprograma) throws BOException ;
	public List<NegocioBeneficio> getListaNBActividades(Programa oprograma,String CodEmpresaGrupo) throws BOException;
	public List<NegocioBeneficio> getListaNBNegocio() throws BOException;
	public List<NegocioBeneficio> getListaNBNegocio(String codEmpresaGrupo) throws BOException ;
	public List<NegocioBeneficio> getListaNBNegocio(Programa oprograma) throws BOException;
	public List<NegocioBeneficio> getListaNBNegocio(Programa oprograma,String CodEmpresaGrupo) throws BOException;
		
	public List<CabTabla> getListaCabTablaByTipoTabla(Long Idprograma,Long idTipoTabla) throws BOException;
	
	public List<DatosBasico> getListaDatosBasico(String CodEmpresaGrupo)throws BOException;
	public List<DatosBasico> getListaDatosBasico(Programa oprograma,String CodEmpresaGrupo)throws BOException;
	
	public void ActualizarDatosBasicosByPrograma(Programa oprograma,List<Empresa> listaEmpresasGrupofin) throws BOException;
	
	public Accionista obtenerAccionista(Accionista oaccionista,String strUsuarioHost) throws BOException;
			
}
