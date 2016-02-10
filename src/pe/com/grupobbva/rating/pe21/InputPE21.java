package pe.com.grupobbva.rating.pe21;

public class InputPE21 {
	
	private String tipoDocumentoIdentidad;
	private String numeroDocumentoIdentidad;
	private String codigoCentral;
	
	public InputPE21(){
		this.tipoDocumentoIdentidad = " ";
		this.numeroDocumentoIdentidad = "";
		this.codigoCentral = "        ";
	}
	
	public String getTipoDocumentoIdentidad() {
		return tipoDocumentoIdentidad;
	}
	public void setTipoDocumentoIdentidad(String tipoDocumentoIdentidad) {
		this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
	}
	public String getNumeroDocumentoIdentidad() {
		return numeroDocumentoIdentidad;
	}
	public void setNumeroDocumentoIdentidad(String numeroDocumentoIdentidad) {
		this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
	}
	public String getCodigoCentral() {
		return codigoCentral;
	}
	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}
	
	
	

}
