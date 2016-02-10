package pe.com.stefanini.core.seguridad.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pe.com.bbva.iipf.util.Constantes;

/**
 * Servlet Filter implementation class Seguridad
 */
public class Seguridad implements Filter {
	
	private static String URL_LOGIN = "/login-ldap.jsp";
	
	private static String URL_LOGINOUT = "/pages/logout.jsp";
	private static String URL_LOGIN_PRE = "/login_ldap_pre.jsp"; //BORRAR
	
    /**
     * Default constructor. 
     */
    public Seguridad() {
         
    }
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		 
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 
		// place your code here
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		if(validarURL(httpRequest.getServletPath())){
			chain.doFilter(request, response);
			return;
		}
		if (session == null || session.getAttribute(Constantes.USUARIO_SESSION) == null){
			//httpResponse.sendRedirect(httpRequest.getContextPath() + URL_LOGIN);
			//httpResponse.sendRedirect(httpRequest.getContextPath() + URL_LOGINOUT); //DESCOMENTAR 11-07-2014
			httpResponse.sendRedirect(httpRequest.getContextPath() + URL_LOGIN_PRE);
		}else{
			chain.doFilter(request, response);
		}
		//chain.doFilter(request, response);
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		 
	}

	public boolean validarURL(String url){
		List<String> listaUrls = new ArrayList<String>();
		listaUrls.add("");
		listaUrls.add("/login-ldap.jsp");
		listaUrls.add("/login_ldap_pre.jsp");//BORRAR
		listaUrls.add("/loginAction.do");
		listaUrls.add("/accesoAction.do");
		listaUrls.add("/pages/logout.jsp");		
		listaUrls.add("/GeneradorReporteP05Excel");
		if(!listaUrls.contains(url)){
			return false;
		}
		return true;
	}
}
