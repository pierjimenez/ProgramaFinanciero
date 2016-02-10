package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.DatosBasicoBlob;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface DatosBasicosBlobBO {
	public void save(DatosBasicoBlob datosBasicoBlob) throws BOException;
	public DatosBasicoBlob findDatosBasicoBlobByPrograma(Programa programa,String codigoEmpresa)throws BOException;
	public List<DatosBasicoBlob> listaDatosBasicoBlobByPrograma(Programa oprograma) throws BOException;
	public void savecopia(DatosBasicoBlob t) throws BOException;
	public void setCampoBlob(String campoBlog) ;
	public void setValorBlob(String valorBlob) ;
	public void setCodEmpresa(String codEmpresa) ;
	public void setPrograma(Programa programa);
	public void setSysCodificacion (String sysCodificacion);
	public void delete(DatosBasicoBlob t) throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	
	public void setPatronesEditor(String patronesEditor);
}
