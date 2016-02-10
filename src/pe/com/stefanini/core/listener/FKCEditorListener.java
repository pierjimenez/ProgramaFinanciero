package pe.com.stefanini.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pe.com.bbva.iipf.pf.bo.ParametroBO;
import pe.com.bbva.iipf.util.Constantes;
import pe.com.stefanini.core.exceptions.BOException;

import net.fckeditor.handlers.PropertiesLoader;

/**
 * Listener para iniciar las propiedades del FCKEditor
 * @author epomayay
 *
 */
public class FKCEditorListener implements ServletContextListener {

	 Logger logger = Logger.getLogger(this.getClass());
	 
	private ApplicationContext context = null;
	private ParametroBO parametroBO = null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		
		try {
			context = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
			parametroBO = (ParametroBO)context.getBean("paremetroBO");
			String urlUploadImagenes = parametroBO.findByNombreParametro(Constantes.DIR_UPLOADFILE_URL).getValor();
			String dirUploadImagenes = parametroBO.findByNombreParametro(Constantes.DIR_UPLOAD_IMAGEN_PDF).getValor();
			logger.info("FCKEDITOR urlUploadImagenes="+urlUploadImagenes);
			logger.info("FCKEDITOR dirUploadImagenes="+dirUploadImagenes);
			
			//connector.userActionImpl=net.fckeditor.requestcycle.impl.EnabledUserAction
			//connector.impl=net.fckeditor.connector.impl.LocalConnector
			//connector.userPathBuilderImpl=net.fckeditor.requestcycle.impl.ServerRootPathBuilder

			
			//connector.userFilesPath=http://118.180.201.246/archivoImagen
			//connector.userFilesAbsolutePath=C:\\wamp\\www\\archivoImagen
			
			//connector.resourceType.image.path=
				
			PropertiesLoader.setProperty("connector.userActionImpl", 
										 "net.fckeditor.requestcycle.impl.EnabledUserAction");
			PropertiesLoader.setProperty("connector.impl", 
			 							 "net.fckeditor.connector.impl.LocalConnector");
			PropertiesLoader.setProperty("connector.userPathBuilderImpl",
			                   			 "net.fckeditor.requestcycle.impl.ServerRootPathBuilder");
			PropertiesLoader.setProperty("connector.userFilesPath",
										 urlUploadImagenes);
			PropertiesLoader.setProperty("connector.userFilesAbsolutePath",
										 dirUploadImagenes);
			PropertiesLoader.setProperty("connector.resourceType.image.path",
				 						 "");
			
		} catch (BOException e) {
			 
			e.printStackTrace();
		}		
	}

}
