package pe.com.bbva.iipf.pf.bo;

import java.util.List;

import pe.com.bbva.iipf.pf.model.AnexoGarantia;
import pe.com.bbva.iipf.pf.model.Programa;
import pe.com.stefanini.core.exceptions.BOException;
import pe.com.stefanini.core.host.UsuarioSesion;

public interface AnexoGarantiaBO {

	public void setListaGarantiaAnexo(List<AnexoGarantia> listaGarantiaAnexo);
	public void setPrograma(Programa programa);
	public void saveAnexosGarantia() throws BOException;
	public void setUsuarioSession(UsuarioSesion usuarioSession);
	public List<AnexoGarantia> findAnexoXPrograma(Programa programa) throws  BOException;
	public List<AnexoGarantia> findAnexoXPrograma(Programa programa,String EmpresaGrupo) throws  BOException;
	
	public void saveCopiaAnexoGarantia(List<AnexoGarantia> olistAnexoGarantia) throws BOException;
	public void delete(AnexoGarantia t) throws BOException;
}
