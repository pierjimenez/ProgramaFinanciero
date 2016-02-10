package pe.com.bbva.iipf.pf.servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import pe.com.bbva.iipf.pf.bo.RelacionesBancariasBO;
import pe.com.stefanini.core.exceptions.BOException;


/**
 * Servlet implementation class PoolBancarioServlet
 */

public class PoolBancarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<List> listPoolBancario = new ArrayList<List>();
	 
	
	
	private RelacionesBancariasBO relacionesBancariasBO;
	
    public PoolBancarioServlet() {
        super();
         
    }



    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException ,BOException{
    	
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String tipoDocumento = request.getParameter("tipodocumento"); 
        String Code = request.getParameter("codigo");
        
//            try {
//       
//        		listPoolBancario=relacionesBancariasBO.findPoolBancario("R", "100000","1","f","f");
//        	
//        		out.printf("<tr>");		    		
//					Map<String,String> hm = new HashMap<String,String>();
//					hm=listPoolBancario.get(0);	
//					
//					Iterator it = hm.entrySet().iterator();
//					while (it.hasNext()) {
//						Map.Entry e = (Map.Entry)it.next();
//						//System.out.println(e.getKey() + " " + e.getValue());
//						out.printf("<th>" + e.getKey() + "</th>");
//					}        		
//        		out.printf("</tr>");
//        		
//        		
//    			for (int i=0;i<listPoolBancario.size();i++) {
//    			out.printf("<tr>");	
//					Map<String,String> hmr = new HashMap<String,String>();
//					hmr=listPoolBancario.get(i);	
//					
//					Iterator itr = hm.entrySet().iterator();
//					while (itr.hasNext()) {
//						Map.Entry er = (Map.Entry)itr.next();
//						//System.out.println(er.getKey() + " " + er.getValue());
//						out.printf("<td>" + er.getValue() + "</td>");
//					}
//				out.printf("</tr>");
//    			}
    	
		      		
        		
//	        	for (int i = 0; i < files.length; i++) {				
//					String file=files[i];
//				    if (!new File(dir, file).isDirectory()) {
//						int dotIndex = file.lastIndexOf('.');
//						String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
//						out.printf("<tr>");
//						out.printf("<td>" + ext + "</td>");
//						//out.printf("<td>" + dir +  "</td>");
//						out.printf("<td width=\"300px\">" + file + "</td>");										
//						out.printf("<td><img src=\"image/pdf.png\" alt=\"download\"><a href=\"#\" onClick=\"donwload1('" + dir + "','"+ file +"')\">Abrir</></td>");
//						out.printf("<td><img src=\"image/Attach.png\" alt=\"download\"><a href=\"doadjuntar.do?dirbuscar=" + dir + "&filebuscar=" + file + "\">Adjuntar</a></td>");
//						
//						out.printf("</tr>");
//				    	}
//	        	}
            
    		
//        } catch (BOException e) {					
//			
//		}catch(Exception e){	 
//            
//        } finally {
//            out.close();
//        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		processRequest(request, response);
		} catch (Exception e) {
			 
		}
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		processRequest(request, response);
		} catch (Exception e) {
			 
		}
        
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


//	public List<HashMap<String, String>> getListPoolBancario() {
//		return listPoolBancario;
//	}
//
//
//	public void setListPoolBancario(List<HashMap<String, String>> listPoolBancario) {
//		this.listPoolBancario = listPoolBancario;
//	}



	public RelacionesBancariasBO getRelacionesBancariasBO() {
		return relacionesBancariasBO;
	}



	public void setRelacionesBancariasBO(RelacionesBancariasBO relacionesBancariasBO) {
		this.relacionesBancariasBO = relacionesBancariasBO;
	}


//	public RelacionesBancariasBO getRelacionesBancariasBO() {
//		return relacionesBancariasBO;
//	}
//
//
//	public void setRelacionesBancariasBO(RelacionesBancariasBO relacionesBancariasBO) {
//		this.relacionesBancariasBO = relacionesBancariasBO;
//	}
    
	
    
    
}
