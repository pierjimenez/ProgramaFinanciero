/**
 * 
 */
package pe.com.stefanini.core.seguridad.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import pe.com.bbva.iipf.util.Constantes;

/**
 * @author indra
 *
 */
public class ExtendsStruts2 extends StrutsPrepareAndExecuteFilter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 
		// place your code here
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		/*HttpServletResponse httpResponse = (HttpServletResponse)response;
		HttpSession session = ((HttpServletRequest) request).getSession(false);*/
		if(validarURL(httpRequest.getServletPath())){
			chain.doFilter(request, response);
			return;
		}else{
			super.doFilter(request, response, chain);
		}
		
	}
	
	public boolean validarURL(String url){
		List<String> listaUrls = new ArrayList<String>();
		listaUrls.add("/GeneradorReporteP05Excel");
		listaUrls.add("/fckeditor/editor/filemanager/connectors");		
		if(!listaUrls.contains(url)){
			return false;
		}
		return true;
	}

}
