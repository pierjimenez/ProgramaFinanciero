package pe.com.bbva.iipf.pf.bo;

import java.util.Map;

import pe.com.bbva.iipf.pf.model.MensajeCorreo;

public interface CorreoBO {

	public void enviarCorreo(Map<String, Object> parametros) throws Exception ;
	public void enviarCorreo(MensajeCorreo mensajeCorreo);
	 public void enviarCorreoPrubeba();
}
