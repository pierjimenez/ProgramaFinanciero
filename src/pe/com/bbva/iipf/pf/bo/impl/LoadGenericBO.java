package pe.com.bbva.iipf.pf.bo.impl;

import javax.annotation.Resource;

import pe.com.bbva.iipf.pf.bo.CorreoBO;
import pe.com.bbva.iipf.pf.dao.impl.LoadGenericDao;
import pe.com.bbva.iipf.pf.model.MensajeCorreo;


public class LoadGenericBO<T>{

	@Resource
	private CorreoBO correoBO;
	
	public void enviarCorreo(LoadGenericDao<T> loadGenericDao,Integer tipoArchivo){
		MensajeCorreo mensajeCorreo=loadGenericDao.obtenerDatosCorreo(tipoArchivo);
		correoBO.enviarCorreo(mensajeCorreo);
	}
}
