package pe.com.bbva.iipf.pf.bo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.com.bbva.iipf.mantenimiento.model.Empresa;
import pe.com.bbva.iipf.pf.model.Comex;
import pe.com.bbva.iipf.pf.model.EfetividadCartera;
import pe.com.bbva.iipf.pf.model.LineasRelacionBancarias;
import pe.com.bbva.iipf.pf.model.OpcionPool;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.bbva.iipf.pf.model.Rentabilidad;
import pe.com.bbva.iipf.pf.model.RentabilidadBEC;
import pe.com.bbva.iipf.pf.model.Tbanco;
import pe.com.bbva.iipf.pf.model.Tempresa;
import pe.com.bbva.iipf.pf.model.rcd;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface RelacionesBancariasBO {

	public List<HashMap<String,BigDecimal>> findByClasificacionSistemaFinanciero2(String codEmpresa) throws BOException ;
	
	public List<rcd> findByCalificacionBanco(String codEmpresa,String tipoDocumento) throws Exception;
	public List<LineasRelacionBancarias> findByLineasRelacionesBancarias(Long Idprograma) throws BOException ;
	
	public List<OpcionPool> findOpcionPool(String Idprograma,String tipoOpcionPool) throws Exception;
	
	public List<EfetividadCartera> findEfectividadCartera(String codEmpresa,String codServicio) throws Exception;
	public List<Rentabilidad> findRentabilidad(String codCentral,String tipoarchivo) throws Exception;
	public List<RentabilidadBEC> findRentabilidadBEC(String Idprograma) throws Exception ;
	public List<RentabilidadBEC> generaModeloRentabilidad(String idPrograma) ;
	public List<RentabilidadBEC> generaModeloRentabilidad(String idPrograma,String codigoCentral);
	public List<RentabilidadBEC> ObtenerRentabilidad(String idPrograma);
	public List<RentabilidadBEC> ObtenerRentabilidadByEmpresa(String idPrograma,String codCentral);
	//public String savelineas(LineasRelacionBancarias lineasRelacionBancarias) throws Exception;
	public void updateRelacionesBancarias() throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public void setPrograma(Programa programa);
	public void setComenLineas(String comenLineas);
	public void setListLineasRelacionesBancarias(List<LineasRelacionBancarias> listLineasRelacionesBancarias); 
	public void setListRentabilidadBEC(List<RentabilidadBEC> listRentabilidadBEC) ;
	public void saveRentabilidaBECrefresh(List<RentabilidadBEC> listRentabilidadBEC, String idPrograma) throws BOException;
	public void saveRentabilidaBECrefresh(List<RentabilidadBEC> listRentabilidadBEC, String idPrograma,String codigoCentral) throws BOException;
	public List<List>  findPoolBancario(String tipoDocumento,String codigo, String tipoDeuda,String codBanco,String CodOtro,String codtipoEmpresaGrupo,String idPrograma) throws BOException;
	public List<List>  findPoolBancarioBD(String tipoDocumento,String codigo, String tipoDeuda,String codBanco,String codtipoEmpresaGrupo,String idPrograma) throws BOException;

	public void setCodTipoDeudas(String codTipoDeudas);
	public void setListaEmpresaSelect(List<Tempresa> listaEmpresaSelect) ;
	public void setListaBancoSelect(List<Tbanco> listaBancoSelect) ;
	public List<Tbanco> findBancosPoolBancarioSQL(String idPrograma,String codEmpresa,String tipoEmpresa) throws BOException;
	
	public void saveLineasRelacionesBancarias() throws BOException;
	public void saveCopiaLineasRelaciones(List<LineasRelacionBancarias> olistLineasRelaciones ) throws BOException;
	public double calcularCuotaFinancieraEmpresa(Programa programa) throws BOException;
	
	public double calcularCuotaFinancieraGrupo(Programa programa, 
											   List<Empresa> listaEmpresas) throws BOException;
	
	public void savePoolBancario() throws BOException;
	//Obtener Fecha de Cuota Financiera
	public String obtenerFechaCuotaFinancieraEmpresa(Programa programa) throws BOException;
	
	public List<RentabilidadBEC> crearRentabilidadGrupo(List<RentabilidadBEC> listRentabilidadBECTodoEmp,String codGrupo,String idPrograma);
	public String obtenerEquivClasificacionBanco(String clasificacion);
	
	public List<Comex> obtenerComex(String idPrograma,String codigoCentral,boolean flagHost);
	public void setListaComex(List<Comex> listaComex);
	
	public List<Comex> obtenerComexByType(List<Comex> olistaComex,String TipoComex);
	
	public String ObtenerRatioReprocidadImp(List<Comex> olistaComexImportacion,Map<String, String> mapListaDescripcionComex);
	public String ObtenerRatioReprocidadExp(List<Comex> olistaComexExportacion,Map<String, String> mapListaDescripcionComex);
	
	public List<Comex> ObtenerComexOfFileHost(String idPrograma,String codCentral);
	public void saveComex() throws BOException;
}
