package pe.com.grupobbva.pf.pec6;

import java.io.Serializable;
import java.util.List;

public class OutputPec6 implements Serializable {
	
	private List<CtRela> listaEmpresaGrupo;
	private String nombreGrupo;
	
	public List<CtRela> getListaEmpresaGrupo() {
		return listaEmpresaGrupo;
	}
	public void setListaEmpresaGrupo(List<CtRela> listaEmpresaGrupo) {
		this.listaEmpresaGrupo = listaEmpresaGrupo;
	}
	public String getNombreGrupo() {
		return nombreGrupo;
	}
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	
	

}
