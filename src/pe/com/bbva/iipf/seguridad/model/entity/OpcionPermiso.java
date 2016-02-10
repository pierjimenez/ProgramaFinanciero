package pe.com.bbva.iipf.seguridad.model.entity;

import java.util.List;

public class OpcionPermiso 
{

	private String codigo; //id asignacion - id opcion -(Asignado Inicial A/I) [ - Asignado en ejecucion A/I] 
	private boolean seleccionado;
	private String descripcion;
	private List<OpcionPermiso> hijos;
	
	public List<OpcionPermiso> getHijos() {
		return hijos;
	}
	public void setHijos(List<OpcionPermiso> hijos) {
		this.hijos = hijos;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public boolean getSeleccionado() {
		return seleccionado;
	}
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getHtml() 
	{
		
		StringBuffer sb = new StringBuffer();
		sb.append("<li>");
		sb.append("<input type=\"checkbox\" ");
		sb.append(" name = 'chkPermisos' ");
		if(this.getSeleccionado())		
			sb.append(" checked='true' ");
		sb.append(" value = '");
		sb.append(this.getCodigo());
		sb.append("'>");
		sb.append(this.getDescripcion());
		if(this.getHijos().size() > 0)
		{
			sb.append("<ul>");
			for(int i = 0; i < this.getHijos().size(); i++)
			{
				sb.append(getHijos().get(i).getHtml());
			}				
			sb.append("</ul>");
		}
		
		return sb.toString();
		
	}
	
	
	
}
