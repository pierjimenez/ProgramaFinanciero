package pe.com.bbva.iipf.pf.servlets;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class directoryFileServlet
 */
public class directoryFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //ProductCodeDao dao = DaoFactory.getInstance().getProductCodeDao();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String dir = request.getParameter("directoryCode");       
        try {       
	        if (dir == null) {
	        	return;
	        }	    	
	    	if (dir.charAt(dir.length()-1) == '\\') {
	        	dir = dir.substring(0, dir.length()-1) + "/";
	    	} else if (dir.charAt(dir.length()-1) != '/') {
	    	    dir += "/";
	    	}	    	
	    	dir = java.net.URLDecoder.decode(dir, "UTF-8");		    	
	        if (new File(dir).exists()) {
	    		String[] files = new File(dir).list(new FilenameFilter() {
	    		    public boolean accept(File dir, String name) {
	    				return name.charAt(0) != '.';
	    		    }
	    		});
	    		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);    		
	    		 out.println("<option value=''>Seleccione Directorio</option>");
	    		// All dirs
	    		for (int i = 0; i < files.length; i++) {				
	    				String file=files[i];
	    			
	    		    if (new File(dir, file).isDirectory()) {
	    		    	 out.printf("<option value='%1s'>%2s</option>", dir+file, dir+file);
	    		    }
	    		}	        
	        }  
        } finally {
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 processRequest(request, response);
	}
	
    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
