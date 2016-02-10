package pe.com.bbva.iipf.mantenimiento.model;

import javax.persistence.Entity;

@Entity
public class SelectItem {
	
	

	private String label;
	private String value;
	private String codigo;
	
	public SelectItem(String value,String label){
		this.value=value;
		this.label=label;
	}
	
	public SelectItem(String value,String label,String codigo){
		this.value=value;
		this.label=label;
		this.codigo=codigo;
	}
	
	public SelectItem(){
		this.value=new String();
		this.label=new String();
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
