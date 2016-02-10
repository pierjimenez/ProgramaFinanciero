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
 * Servlet implementation class FileServlet
 */
public class FileServlet extends HttpServlet {
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String fileCode = request.getParameter("fileCode"); //"//118.247.4.245/xcobranzas/DATAAS/AnalisisSecorial"; 
        //ProductDao dao = DaoFactory.getInstance().getProductDao();
        //List<Product> list = dao.getProductsByCode(productCode);
        System.out.print("fileCode: " + fileCode);
        try {
        	out.printf("fileCode:" + fileCode);
        String dir = fileCode;	
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
    			
        		out.printf("<tr>");
        		out.printf("<th>Tipo</th>");
        		//out.printf("<th>Directory</th>"); 
        		out.printf("<th>Nombre</th>"); 
        		out.printf("<th>Accion</th>");
        		out.printf("<th>Adjuntar</th>"); 
        		
        		out.printf("</tr>");
	        	for (int i = 0; i < files.length; i++) {				
					String file=files[i];
				    if (!new File(dir, file).isDirectory()) {
						int dotIndex = file.lastIndexOf('.');
						String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
						out.printf("<tr>");
						out.printf("<td>" + ext + "</td>");
						//out.printf("<td>" + dir +  "</td>");
						out.printf("<td width=\"300px\">" + file + "</td>");										
						out.printf("<td><img src=\"image/pdf.png\" alt=\"download\"><a href=\"#\" onClick=\"donwload1('" + dir + "','"+ file +"')\">Abrir</></td>");
						out.printf("<td><img src=\"image/Attach.png\" alt=\"download\"><a href=\"doadjuntar.do?dirbuscar=" + dir + "&filebuscar=" + file + "\">Adjuntar</a></td>");
						
						out.printf("</tr>");
				    	}
	        	}
            
    		}   
            
            
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
